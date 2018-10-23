package com.free.novel.mapper;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Novel;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NovelMapper {

    @Select("select * from novel where id = #{novelId}")
    @ResultType(Novel.class)
    Novel selectByPrimaryKey(int novelId);

    @Select("select * from chapter where novelId = #{novelId}")
    @ResultType(Chapter.class)
    List<Chapter> getChapters(int novelId);
}
