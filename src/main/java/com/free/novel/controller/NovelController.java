package com.free.novel.controller;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Dictionary;
import com.free.novel.entity.Novel;
import com.free.novel.entity.User;
import com.free.novel.service.NovelService;
import com.free.novel.util.EncryptUtil;
import com.free.novel.util.ZLibUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NovelController {

    @Autowired
    private NovelService novelService;




    @GetMapping("/getRecommentNovelsTop10")
    public List<Novel> getRecommentNovelsTop10() {
        return novelService.getRecommentNovelsTop10();
    }

    @GetMapping("/getNovelsByNameOrAuthor")
    public List<Novel> getNovelsByNameOrAuthor(String keyword) {
        return novelService.getNovelsByNameOrAuthor(keyword);
    }

    @GetMapping("/getNovelsByTag")
    public List<Novel> getNovelsByTag(Integer tagId,Integer page) {
        return novelService.getNovelsByTag(tagId,page);
    }

    @GetMapping("/getDicByType")
    public List<Dictionary> getDicByType(String type,Integer status) {
        return novelService.getDicByType(type,status);
    }

    @GetMapping("/loginOrRegister")
    public Object loginOrRegister(String type,String account,String pwd) {
        return novelService.loginOrRegister(type,account,pwd);
    }

    @GetMapping("/signIn")
    public Object signIn(User user,String verify) {
        return novelService.signIn(user,verify);
    }
    @GetMapping("/getMessages")
    public Object getMessages(Integer userid,String verify){
        return novelService.getMessages(userid,verify);
    }
    @GetMapping("/markRead")
    public Object markRead(Integer messageId,String verify){
        return novelService.markRead(messageId,verify);
    }

    @GetMapping("/feedback")
    public Object feedback(Integer userId,String feedback,String verify){
        return novelService.feedback(userId,feedback,verify);
    }


    /**
     * 获取小说信息
     *
     * @param novelId
     * @return
     */
    @GetMapping("/getNovel/{novelId}")
    public Novel getNovel(@PathVariable("novelId") String novelId) {
        int id = EncryptUtil.decryptInt(novelId);
        return novelService.getNovelById(id);
    }

    /**
     * 获取小说目录
     *
     * @param novelId
     * @return
     */
    @GetMapping("/getChapters/{novelId}/{limit}")
    public List<Chapter> getChapters(@PathVariable("novelId") String novelId,@PathVariable("limit") int limit) {
        int id = EncryptUtil.decryptInt(novelId);
        return novelService.getDirectory(id,limit);
    }

    /**
     * 获取小说封面
     *
     * @param novelId
     * @param response
     * @throws IOException
     */
    @GetMapping("/getImage/{novelId}.jpg")
    public void getImage(@PathVariable("novelId") String novelId, HttpServletResponse response) throws IOException {
        int id = EncryptUtil.decryptInt(novelId);
        Novel novel = novelService.getNovelById(id);
        response.setHeader("Content-Type", "image/jpeg");
        response.setHeader("Content-length", novel.getCover().length + "");
        OutputStream out = response.getOutputStream();
        out.write(novel.getCover());
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
    public Chapter getNovelDetail(@PathVariable("novelId") String novelId, @PathVariable("chapterId") String chapterId) throws UnsupportedEncodingException {
        int nid = EncryptUtil.decryptInt(novelId);
        int cid = EncryptUtil.decryptInt(chapterId);
        Chapter chapter = novelService.getChapterByChapterId(nid, cid);
        byte[] content = chapter.getContent();
        content = ZLibUtils.decompress(content);
        chapter.setContent_str(new String(content, "GBK").replace("。", "。\n    "));
        chapter.setContent(null);
        return chapter;
    }


    /**
     * 测试使用的
     *
     * @param url
     * @param novelId
     * @throws IOException
     */
//    @GetMapping("/setImage/{novelId}")
//    public void setImage(String url, @PathVariable("novelId") int novelId) throws IOException {
//        //https://www.biqiuge.com/files/article/image/4/4772/4772s.jpg
//        URL fileURL = new URL(url); // 创建URL
//        URLConnection urlconn = fileURL.openConnection(); // 试图连接并取得返回状态码
//        urlconn.connect();
//        HttpURLConnection httpconn = (HttpURLConnection) urlconn;
//        int HttpResult = httpconn.getResponseCode();
//        if (HttpResult != HttpURLConnection.HTTP_OK) {
//            System.out.print("无法连接到");
//        } else {
//            int length = httpconn.getContentLength();
//            InputStream inputStream = urlconn.getInputStream();
//            byte[] bytes = new byte[length];
//            inputStream.read(bytes);
//            String image = Base64.getEncoder().encodeToString(bytes);
//            stringRedisTemplate.opsForValue().append("image_" + novelId, image);
//            inputStream.close();
//        }
//    }

}
