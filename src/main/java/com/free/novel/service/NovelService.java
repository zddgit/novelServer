package com.free.novel.service;

import com.free.novel.domain.Dictionary;
import com.free.novel.domain.*;
import com.free.novel.entity.RedisKey;
import com.free.novel.mapper.NovelMapper;
import com.free.novel.util.EncryptUtil;
import com.free.novel.util.ZLibUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NovelService {

    @Resource
    private NovelMapper novelMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public Novel getNovelById(int novelId) {
        return novelMapper.selectByPrimaryKey(novelId);
    }

    public List<Chapter> getChapters(int novelId) {
        return novelMapper.getChapters(novelId);
    }

    //获取目录添加缓存信息
    public List<Chapter> getDirectory(int novelId, int limit) {
        String novelTitlesKey = RedisKey.getNovelTitles(novelId+"");
        String novelChapterIdskey = RedisKey.getNovelChapterIds(novelId+"");
        Set<String> ids = stringRedisTemplate.opsForZSet().rangeByScore(novelChapterIdskey,limit,limit+300);
        List<Chapter> chapterList;
        if(ids.size()!=0){
            chapterList =  stringRedisTemplate.execute(new RedisCallback<List<Chapter>>() {
                @Override
                public List<Chapter> doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.openPipeline();
                    List<Chapter> chapters = new ArrayList<>();
                    ids.forEach(id->{
                        Chapter chapter = new Chapter();
                        chapter.setNovelId(novelId);
                        chapter.setChapterId(Integer.parseInt(id));
                        connection.hGet(novelTitlesKey.getBytes(),id.getBytes());
                        chapters.add(chapter);
                    });
                    List<Object> objects = connection.closePipeline();
                    for (int i = 0; i < objects.size(); i++) {
                        Chapter chapter = chapters.get(i);
                        byte[] content = (byte[]) objects.get(i);
                        String title = new String(content,Charset.forName("utf-8"));
                        chapter.setTitle(title);
                    }
                    return chapters;
                }
            });
        }else {
            String sourceid = stringRedisTemplate.opsForHash().get(RedisKey.getNovelKey(novelId+""),"sourceid").toString();
            String tableName = novelMapper.selectRouterTable(Integer.parseInt(sourceid), novelId);
            chapterList =  novelMapper.getDirectory(tableName, novelId, limit);
            saveNovelChaptersToRedis(chapterList,novelTitlesKey,novelChapterIdskey,limit);
        }
        return  chapterList;
    }

    private void saveNovelChaptersToRedis(List<Chapter> chapterList,String novelTitlesKey,String novelChapterIdskey,int limit) {
        if(chapterList.size()>0){
            Map<String,String> map = new HashMap<>();
            Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
            double score = limit + 0.0d;
            for (int i = 0; i < chapterList.size(); i++) {
                Chapter chapter = chapterList.get(i);
                DefaultTypedTuple defaultTypedTuple = new DefaultTypedTuple(chapter.getChapterId()+"",score);
                map.put(chapter.getChapterId()+"",chapter.getTitle());
                set.add(defaultTypedTuple);
                score++;
            }

            // 保存章节id到有序列表
            stringRedisTemplate.opsForZSet().add(novelChapterIdskey,set);
            // 保存章节标题到散列
            stringRedisTemplate.opsForHash().putAll(novelTitlesKey,map);
        }

    }
    // 获取具体章节内容添加缓存
    public Chapter getChapterByChapterId(int novelId, int chapterId) {
        String novelContentKey = RedisKey.getNovelContents(novelId+"");
        String novelTitleKey = RedisKey.getNovelTitles(novelId+"");
        String content = (String) stringRedisTemplate.opsForHash().get(novelContentKey,chapterId+"");
        String title = (String) stringRedisTemplate.opsForHash().get(novelTitleKey,chapterId+"");
        Chapter chapter = null;
        if(content!=null){
            chapter = new Chapter();
            chapter.setNovelId(novelId);
            chapter.setTitle(title);
            chapter.setContent(ZLibUtils.compress(content.getBytes()));
            chapter.setChapterId(chapterId);
        }else {
            String sourceid = stringRedisTemplate.opsForHash().get(RedisKey.getNovelKey(novelId+""),"sourceid").toString();
            String tableName = novelMapper.selectRouterTable(Integer.parseInt(sourceid), novelId);
            chapter =  novelMapper.getChapterByChapterId(tableName, novelId, chapterId);
            saveChapterToRedis(chapter);
        }
        return  chapter;
    }

    private void saveChapterToRedis(Chapter chapter) {
        String novelContentKey = RedisKey.getNovelContents(chapter.getNovelId()+"");
        String noveltitlesKey = RedisKey.getNovelTitles(chapter.getNovelId()+"");
        String content = new String(ZLibUtils.decompress(chapter.getContent()),Charset.forName("utf-8"));
        stringRedisTemplate.opsForHash().put(novelContentKey,chapter.getChapterId()+"",content);
        stringRedisTemplate.opsForHash().put(noveltitlesKey,chapter.getChapterId()+"",chapter.getTitle());
    }

    public List<Novel> getRecommentNovelsTop10() {
        return novelMapper.getRecommentNovelsTop10();
    }

    public List<Novel> getNovelsByNameOrAuthor(String keyword) {
        List<Novel>  novels= novelMapper.getNovelsByNameOrAuthor(keyword);
        novels.forEach(novel -> {
            Map<String,String> map = new HashMap<>();
            map.put("id",novel.getId()+"");
            map.put("name",novel.getName());
            map.put("author",novel.getAuthor());
            map.put("introduction",novel.getIntroduction());
            map.put("sourceid",novel.getSourceid()+"");
            stringRedisTemplate.opsForHash().putAll(RedisKey.getNovelKey((novel.getId()+"")), map);
        });
        return novels;
    }

    public List<Dictionary> getDicByType(String type, Integer status) {
        return novelMapper.getDicByType(type, status);
    }

    // 按类别查询添加换信息
    public List<Novel> getNovelsByTag(Integer tagId, Integer page) {
        String tagRediskey = RedisKey.getTagKey(tagId.toString());
        Long len = stringRedisTemplate.opsForZSet().zCard(tagRediskey);
        List<Novel> novels = null;
        if (len >= page * 10) {
            novels = getNovelsFromRedis(page, tagRediskey);
        }else {
            novels = novelMapper.getNovelsByTag(tagId, page);
            saveNovelToRedis(tagRediskey,novels);
        }
        return novels;
    }
    // 保存小说基础信息到redis
    private void saveNovelToRedis(String tagRediskey, List<Novel> novels) {
        if(novels.size()>0){
            novels.forEach(novel -> stringRedisTemplate.opsForZSet().add(tagRediskey,novel.getId()+"",0.0d));
            novels.forEach(novel -> {
                Map<String,String> map = new HashMap<>();
                map.put("id",novel.getId()+"");
                map.put("name",novel.getName());
                map.put("author",novel.getAuthor());
                map.put("introduction",novel.getIntroduction());
                map.put("sourceid",novel.getSourceid()+"");
                stringRedisTemplate.opsForHash().putAll(RedisKey.getNovelKey((novel.getId()+"")), map);
            });
        }
    }
    // 从redis获取消息
    private List<Novel> getNovelsFromRedis(Integer page, String tagRediskey) {
        List<Novel> novels;
        Set<String> ids = stringRedisTemplate.opsForZSet().range(tagRediskey, (page - 1) * 10, (page * 10-1));
        novels = stringRedisTemplate.execute(new RedisCallback<List<Novel>>() {
            @Override
            public List<Novel> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (String id : ids) {
                    String novelkey = RedisKey.getNovelKey(id);
                    redisConnection.hGet(novelkey.getBytes(), "id".getBytes());
                    redisConnection.hGet(novelkey.getBytes(), "name".getBytes());
                    redisConnection.hGet(novelkey.getBytes(), "author".getBytes());
                    redisConnection.hGet(novelkey.getBytes(), "introduction".getBytes());
                }
                List<Object> list = redisConnection.closePipeline();
                List<Novel> novelList = new ArrayList<>();
                Novel novel = null;
                for (int i = 0; i < list.size(); i++) {
                    byte[] v = (byte[]) list.get(i);
                    if (i % 4 == 0) { // id
                        novel = new Novel();
                        novel.setId(Integer.parseInt(new String(v, Charset.forName("utf-8"))));
                    }
                    if (i % 4 == 1) { // name
                        novel.setName(new String(v, Charset.forName("utf-8")));
                    }
                    if (i % 4 == 2) { // author
                        novel.setAuthor(new String(v, Charset.forName("utf-8")));
                    }
                    if (i % 4 == 3) { // introduction
                        novel.setIntroduction(new String(v, Charset.forName("utf-8")));
                        novelList.add(novel);
                    }
                }
                return novelList;
            }
        });
        return novels;
    }

    public Object loginOrRegister(String type, String account, String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        User user;
        if ("email".equals(type)) {
            user = novelMapper.selectUserByEmail(account);
        } else if ("mobile".equals(type)) {
            user = novelMapper.selectUserByPhone(account);
        } else {
            map.put("code", 1);
            map.put("message", "非法参数");
            return map;
        }
        if (user == null) {
            user = new User();
            if ("email".equals(type)) {
                user.setEmail(account);
            } else {
                user.setPhone(account);
            }
            user.setNick(account);
            user.setPwd(pwd);
            Integer time = Integer.parseInt(System.currentTimeMillis() / 1000 + "");
            user.setRegisterTime(time);
            user.setLastLoginTime(time);
            user.setGoldenBean(300);
            user.setExpireDate(time + 14 * 24 * 3600);
            novelMapper.insertOrUpdateUser(user);
            map.put("message", "注册成功");
            map.put("data", user);
        } else {
            if (pwd.equals(user.getPwd())) {
                Integer time = Integer.parseInt(System.currentTimeMillis() / 1000 + "");
                user.setLastLoginTime(time);
                novelMapper.insertOrUpdateUser(user);
                map.put("message", "登陆成功");
                map.put("data", user);
            } else {
                map.put("code", 1);
                map.put("message", "密码错误");
            }
        }
        return map;
    }

    public Object signIn(User user, String verify) {
        Map<String, Object> map = new HashMap<>();
        User olduser = novelMapper.selectUserByPrimaryKey(user.getId());
        String gb = EncryptUtil.decryptStr(verify);
        if (Integer.parseInt(gb) != user.getGoldenBean()) {
            map.put("code", 1);
            map.put("message", "非法参数");
            return map;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String signInTime = sdf.format(new Date());
        if (olduser.getSignInTime().equals(signInTime)) {
            map.put("code", 1);
            map.put("message", "已经签过到了");
            return map;
        }
        user.setSignInTime(signInTime);
        novelMapper.insertOrUpdateUser(user);
        map.put("code", 0);
        map.put("message", "成功");
        return map;
    }

    public Object getMessages(Integer userid, String verify) {
        Map<String, Object> map = new HashMap();
        String userId = EncryptUtil.decryptStr(verify);
        if (userid != Integer.parseInt(userId)) {
            map.put("code", 1);
            map.put("message", "非法参数");
            return map;
        }
        List<Message> messages = novelMapper.getMessages(userid);
        map.put("code", 0);
        map.put("message", "成功");
        map.put("data", messages);
        return map;
    }

    public Object markRead(Integer messageId, String verify) {
        Map<String, Object> map = new HashMap();
        String msgId = EncryptUtil.decryptStr(verify);
        if (messageId != Integer.parseInt(msgId)) {
            map.put("code", 1);
            map.put("message", "非法参数");
            return map;
        }
        novelMapper.markRead(messageId);
        map.put("code", 0);
        map.put("message", "成功");
        return map;
    }

    public Map feedback(Integer userId, String feedback, String verify) {
        Map<String, Object> map = new HashMap();
        String userid = EncryptUtil.decryptStr(verify);
        if (userId != Integer.parseInt(userid)) {
            map.put("code", 1);
            map.put("message", "非法参数");
            return map;
        }
        Long time = System.currentTimeMillis() / 1000;
        Message message = new Message();
        message.setFeedback(feedback);
        message.setFeedbackUserId(userId);
        message.setFeedbackTime(time.intValue());
        novelMapper.insertOrUpdateMessage(message);
        map.put("code", 0);
        map.put("message", "成功");
        return map;
    }

    public App getCurrentAPP() {
        return novelMapper.getCurrentAPP();
    }
}
