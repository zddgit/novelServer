package com.free.novel.controller;

import com.free.novel.domain.*;
import com.free.novel.service.NovelService;
import com.free.novel.util.EncryptUtil;
import com.free.novel.util.ZLibUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
public class NovelController {

    @Autowired
    private NovelService novelService;
    @Resource
    private HttpServletResponse response;
    @Resource
    private HttpServletRequest request;


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
    public List<Dictionary> getDicByType(String type, Integer status) {
        List<Dictionary> dictionaries = novelService.getDicByType(type,status);
        if("tag".equals(type)){
            dictionaries.add(dictionaries.remove(0));
        }
        return dictionaries;
    }

    @GetMapping("/loginOrRegister")
    public Object loginOrRegister(String type,String account,String pwd) {
        String session = request.getSession().getId();
        Cookie remoteSessionCookie= new Cookie("remoteSession", session);
        remoteSessionCookie.setMaxAge(-1);
        response.addCookie(remoteSessionCookie);
        return novelService.loginOrRegister(type,account,pwd);
    }

    @GetMapping("/signIn")
    public Object signIn(User user, String verify) {
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
    public Map feedback(Integer userId, String feedback, String verify){
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
    public List<Chapter> getChapters(@PathVariable("novelId") String novelId, @PathVariable("limit") int limit) {
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
        chapter.setContent_str(new String(content, "utf-8").replace("。", "。\n    "));
        chapter.setContent(null);
        return chapter;
    }
    @GetMapping("/getVersion")
    public Object getVersion(){
        App app = novelService.getCurrentAPP();
        return app==null?null:app.getVersion();
    }

    @GetMapping("/autoUpdate")
    public void autoUpdate() throws IOException {
        App app = novelService.getCurrentAPP();
        File file = new File(app.getDownload());
        FileInputStream fileInputStream = new FileInputStream(file);
        response.setHeader("Content-Disposition","attachment;filename=freenovel"+app.getVersion()+".apk");
        int len = fileInputStream.available();
        byte[] bytes = new byte[len];
        response.setContentLength(len);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        dataInputStream.readFully(bytes);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        dataInputStream.close();
        fileInputStream.close();
        outputStream.close();
    }
//    @RequestMapping("/error")
//    public Object urlErr(){
//        return "错误的页面";
//    }

}
