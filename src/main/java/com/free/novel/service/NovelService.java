package com.free.novel.service;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Dictionary;
import com.free.novel.entity.Novel;
import com.free.novel.entity.User;
import com.free.novel.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NovelService {

    @Resource
    private NovelMapper novelMapper;


    public Novel getNovelById(int novelId) {
        return novelMapper.selectByPrimaryKey(novelId);
    }

    public List<Chapter> getChapters(int novelId) {
        return  novelMapper.getChapters(novelId);
    }
    public List<Chapter> getDirectory(int novelId,int limit) {
        return  novelMapper.getDirectory(novelId,limit);
    }

    public Chapter getChapterByChapterId(int novelId, int chapterId) {
        return novelMapper.getChapterByChapterId(novelId,chapterId);
    }

    public List<Novel> getRecommentNovelsTop10() {
        return novelMapper.getRecommentNovelsTop10();
    }

    public List<Novel> getNovelsByNameOrAuthor(String keyword) {
        return novelMapper.getNovelsByNameOrAuthor(keyword);
    }

    public List<Dictionary> getDicByType(String type,Integer status) {
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
            user = novelMapper.selectByEmail(account);
        }else if("mobile".equals(type)){
            user = novelMapper.selectByPhone(account);
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
            user.setPwd(pwd);
            user.setRegisterTime(Integer.parseInt(System.currentTimeMillis()/1000+""));
            novelMapper.register(user);
            map.put("message","注册成功");
        }else {
            if(pwd.equals(user.getPwd())){
                map.put("message","登陆成功");
            }else {
                map.put("message","密码错误");
            }
            map.put("data",user);
        }
        return map;
    }
}
