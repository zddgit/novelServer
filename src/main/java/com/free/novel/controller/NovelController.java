package com.free.novel.controller;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Novel;
import com.free.novel.service.NovelService;
import com.free.novel.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;

@RestController
public class NovelController {

    @Autowired
    private NovelService novelService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取小说信息
     *
     * @param novelId
     * @return
     */
    @GetMapping("/getNovel/{novelId}")
    public Novel getNovel(@PathVariable("novelId") int novelId) {
        return novelService.getNovelById(novelId);
    }

    /**
     * 获取小说目录
     *
     * @param novelId
     * @return
     */
    @GetMapping("/getChapters/{novelId}")
    public List<Chapter> getChapters(@PathVariable("novelId") int novelId) {
        return novelService.getChapters(novelId);
    }

    /**
     * 获取小说封面
     *
     * @param novelId
     * @param response
     * @throws IOException
     */
    @GetMapping("/getImage/{novelId}.jpg")
    public void getImage(@PathVariable("novelId") int novelId, HttpServletResponse response) throws IOException {
        String image = stringRedisTemplate.opsForValue().get(Constant.image.getKey() + novelId);
        response.setHeader("Content-Type", "image/jpeg");
        byte[] bytes = Base64.getDecoder().decode(image);
        response.setHeader("Content-length", bytes.length + "");
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
        out.close();
    }

    /**
     * 获取章节内容
     *
     * @param novelId
     * @param chapterId
     * @return
     */
    @GetMapping("/getNovelDetail/{novelId}/{chapterId}")
    public String getNovelDetail(@PathVariable("novelId") int novelId, @PathVariable("chapterId") int chapterId) {
        return (String) stringRedisTemplate.opsForHash().get(Constant.contents.getKey() + novelId, chapterId + "");
    }


    /**
     * 测试使用的
     *
     * @param url
     * @param novelId
     * @throws IOException
     */
    @GetMapping("/setImage/{novelId}")
    public void setImage(String url, @PathVariable("novelId") int novelId) throws IOException {
        //https://www.biqiuge.com/files/article/image/4/4772/4772s.jpg
        URL fileURL = new URL(url); // 创建URL
        URLConnection urlconn = fileURL.openConnection(); // 试图连接并取得返回状态码
        urlconn.connect();
        HttpURLConnection httpconn = (HttpURLConnection) urlconn;
        int HttpResult = httpconn.getResponseCode();
        if (HttpResult != HttpURLConnection.HTTP_OK) {
            System.out.print("无法连接到");
        } else {
            int length = httpconn.getContentLength();
            InputStream inputStream = urlconn.getInputStream();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            String image = Base64.getEncoder().encodeToString(bytes);
            stringRedisTemplate.opsForValue().append("image_" + novelId, image);
            inputStream.close();
        }
    }

}