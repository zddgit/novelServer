package com.free.novel.service;

import com.free.novel.entity.Chapter;
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
    public List<Chapter> getDirectory(int novelId) {
        return  novelMapper.getDirectory(novelId);
    }

    public Chapter getChapterByChapterId(int novelId, int chapterId) {
        return novelMapper.getChapterByChapterId(novelId,chapterId);
    }
}
