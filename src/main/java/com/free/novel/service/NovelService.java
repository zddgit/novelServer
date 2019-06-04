package com.free.novel.service;

import com.free.novel.domain.*;
import com.free.novel.mapper.NovelMapper;
import com.free.novel.util.EncryptUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NovelService {

    @Resource
    private NovelMapper novelMapper;
    @Resource
    private RedisTemplate redisTemplate;


    public Novel getNovelById(int novelId) {
        return novelMapper.selectByPrimaryKey(novelId);
    }

    public List<Chapter> getChapters(int novelId) {
        return  novelMapper.getChapters(novelId);
    }

    public List<Chapter> getDirectory(int novelId,int limit) {
        Novel novel = novelMapper.selectNovelWithOutCoverAndintroduction(novelId);
        int sourceid = novel.getSourceid();
        String tableName = novelMapper.selectRouterTable(sourceid,novelId);
        return  novelMapper.getDirectory(tableName,novelId,limit);
    }

    public Chapter getChapterByChapterId(int novelId, int chapterId) {
        Novel novel = novelMapper.selectNovelWithOutCoverAndintroduction(novelId);
        int sourceid = novel.getSourceid();
        String tableName = novelMapper.selectRouterTable(sourceid,novelId);
        return novelMapper.getChapterByChapterId(tableName,novelId,chapterId);
    }

    public List<Novel> getRecommentNovelsTop10() {
        return novelMapper.getRecommentNovelsTop10();
    }

    public List<Novel> getNovelsByNameOrAuthor(String keyword) {
        return novelMapper.getNovelsByNameOrAuthor(keyword);
    }

    public List<Dictionary> getDicByType(String type, Integer status) {
        return novelMapper.getDicByType(type,status);
    }

    public List<Novel> getNovelsByTag(Integer tagId,Integer page) {
        return novelMapper.getNovelsByTag(tagId,page);
    }

    public Object loginOrRegister(String type, String account, String pwd) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        User user;
        if("email".equals(type)){
            user = novelMapper.selectUserByEmail(account);
        }else if("mobile".equals(type)){
            user = novelMapper.selectUserByPhone(account);
        }else {
            map.put("code",1);
            map.put("message","非法参数");
            return map;
        }
        if(user==null){
            user =new User();
            if("email".equals(type)){
                user.setEmail(account);
            }else {
                user.setPhone(account);
            }
            user.setNick(account);
            user.setPwd(pwd);
            Integer time = Integer.parseInt(System.currentTimeMillis()/1000+"");
            user.setRegisterTime(time);
            user.setLastLoginTime(time);
            user.setGoldenBean(300);
            user.setExpireDate(time+14*24*3600);
            novelMapper.insertOrUpdateUser(user);
            map.put("message","注册成功");
            map.put("data",user);
        }else {
            if(pwd.equals(user.getPwd())){
                Integer time = Integer.parseInt(System.currentTimeMillis()/1000+"");
                user.setLastLoginTime(time);
                novelMapper.insertOrUpdateUser(user);
                map.put("message","登陆成功");
                map.put("data",user);
            }else {
                map.put("code",1);
                map.put("message","密码错误");
            }
        }
        return map;
    }

    public Object signIn(User user, String verify) {
        Map<String,Object> map = new HashMap<>();
        User olduser = novelMapper.selectUserByPrimaryKey(user.getId());
        String gb = EncryptUtil.decryptStr(verify);
        if(Integer.parseInt(gb) != user.getGoldenBean()){
            map.put("code",1);
            map.put("message","非法参数");
            return map;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String signInTime  = sdf.format(new Date());
        if(olduser.getSignInTime().equals(signInTime)){
            map.put("code",1);
            map.put("message","已经签过到了");
            return map;
        }
        user.setSignInTime(signInTime);
        novelMapper.insertOrUpdateUser(user);
        map.put("code",0);
        map.put("message","成功");
        return map;
    }

    public Object getMessages(Integer userid, String verify) {
        Map<String,Object> map = new HashMap();
        String userId = EncryptUtil.decryptStr(verify);
        if(userid!=Integer.parseInt(userId)){
            map.put("code",1);
            map.put("message","非法参数");
            return map;
        }
        List<Message> messages = novelMapper.getMessages(userid);
        map.put("code",0);
        map.put("message","成功");
        map.put("data",messages);
        return map;
    }

    public Object markRead(Integer messageId, String verify) {
        Map<String,Object> map = new HashMap();
        String msgId = EncryptUtil.decryptStr(verify);
        if(messageId!=Integer.parseInt(msgId)){
            map.put("code",1);
            map.put("message","非法参数");
            return map;
        }
        novelMapper.markRead(messageId);
        map.put("code",0);
        map.put("message","成功");
        return map;
    }

    public Map feedback(Integer userId, String feedback, String verify) {
        Map<String,Object> map = new HashMap();
        String userid = EncryptUtil.decryptStr(verify);
        if(userId!=Integer.parseInt(userid)){
            map.put("code",1);
            map.put("message","非法参数");
            return map;
        }
        Long time = System.currentTimeMillis()/1000;
        Message message = new Message();
        message.setFeedback(feedback);
        message.setFeedbackUserId(userId);
        message.setFeedbackTime(time.intValue());
        novelMapper.insertOrUpdateMessage(message);
        map.put("code",0);
        map.put("message","成功");
        return map;
    }

    public App getCurrentAPP() {
        return  novelMapper.getCurrentAPP();
    }
}
