package com.free.novel.controller;

import com.free.novel.entity.Novel;
import com.free.novel.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NovelController {

    @Autowired
    private NovelService novelService;

    @GetMapping("/novel/{novelId}")
    public Novel getNovelById(@PathVariable("novelId") int novelId){

        return novelService.getNovelById(novelId);
    }

}
