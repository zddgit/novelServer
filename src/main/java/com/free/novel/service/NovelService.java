package com.free.novel.service;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Dictionary;
import com.free.novel.entity.Novel;
import com.free.novel.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
}
