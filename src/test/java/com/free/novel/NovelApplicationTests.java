package com.free.novel;

import com.free.novel.domain.Chapter;
import com.free.novel.domain.Novel;
import com.free.novel.entity.RedisKey;
import com.free.novel.mapper.NovelMapper;
import com.free.novel.service.NovelService;
import com.free.novel.util.JsonUtil;
import com.free.novel.util.ZLibUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NovelApplicationTests {
    @Resource
    private NovelMapper novelMapper;

    @Resource
    private StringRedisTemplate redisTemplate;
    @Resource
    private NovelService novelService;

    @Test
    public void contextLoads() throws UnsupportedEncodingException {
        List<Chapter> list =  novelMapper.getChapters(1);
        Chapter chapter = list.get(0);
        byte[] contentByte = ZLibUtils.decompress(chapter.getContent());
        System.out.println(contentByte.length);
        System.out.println(new String(contentByte,"GBK"));
        System.out.println(chapter);
    }

    @Test
    public void redis_test(){
        Set<String> set = redisTemplate.opsForZSet().rangeByScore("tag:1",0,10);
        System.out.println(set);
    }

    @Test
    public void redis_test_pip(){
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        List novels = redisTemplate.execute(new RedisCallback<List<Novel>>() {
            @Override
            public List<Novel> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (String id : ids) {
                    String novelkey = RedisKey.getNovelKey(id);
                    redisConnection.hGet(novelkey.getBytes(),"id".getBytes());
                    redisConnection.hGet(novelkey.getBytes(),"name".getBytes());
                    redisConnection.hGet(novelkey.getBytes(),"author".getBytes());
                    redisConnection.hGet(novelkey.getBytes(),"introduction".getBytes());
                    // id,name,author,introduction
                }

                List<Object> list = redisConnection.closePipeline();
                List<Novel> novelList = new ArrayList<>();
                Novel novel=null;
                for (int i = 0; i < list.size(); i++) {
                    byte[] v = (byte[]) list.get(i);
                    if(i%4==0){
                        novel = new Novel();
                        novel.setId(Integer.parseInt(new String(v, Charset.forName("utf-8"))));
                    }
                    if(i%4==1){
                        novel.setName(new String(v, Charset.forName("utf-8")));
                    }
                    if(i%4==2){
                        novel.setAuthor(new String(v, Charset.forName("utf-8")));
                    }
                    if(i%4==3){
                        novel.setIntroduction(new String(v, Charset.forName("utf-8")));
                        novelList.add(novel);
                    }
                }
                return novelList;
            }
        });
        System.out.println(novels);
    }
    @Test
    public void testgetNovelsByTag(){
        Date start = new Date();
        List<Novel> novels  = novelService.getNovelsByTag(1,2);
        System.out.println(System.currentTimeMillis()-start.getTime());
        System.out.println(novels);
    }

}
