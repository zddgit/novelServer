package com.free.novel.service;

import com.free.novel.entity.Novel;
import com.free.novel.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class NovelService {

    @Resource
    private NovelMapper novelMapper;


    @Transactional
    public Novel getNovelById(int novelId) {
        return novelMapper.selectByPrimaryKey(novelId);
    }
}
