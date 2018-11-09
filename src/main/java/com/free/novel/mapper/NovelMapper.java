package com.free.novel.mapper;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Novel;
import org.apache.ibatis.annotations.Param;
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

    @Select("select novelId,chapterId,title from chapter where novelId = #{novelId}")
    @ResultType(Chapter.class)
    List<Chapter> getDirectory(int novelId);

    @Select("select * from chapter where novelId = #{novelId} and chapterId = #{chapterId}")
    @ResultType(Chapter.class)
    Chapter getChapterByChapterId(@Param("novelId") int novelId,@Param("chapterId") int chapterId);

    @Select("select * from novel limit 10")
    @ResultType(Novel.class)
    List<Novel> getRecommentNovelsTop10();

    @Select("select * from novel where name like '%${keyword}%' or author like '%${keyword}%' limit 10")
    @ResultType(Novel.class)
    List<Novel> getNovelsByNameOrAuthor(@Param("keyword") String keyword);
}
