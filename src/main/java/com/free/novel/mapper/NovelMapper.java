package com.free.novel.mapper;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Dictionary;
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

    @Select("select novelId,chapterId,title from chapter where novelId = #{novelId} limit #{limit},50")
    @ResultType(Chapter.class)
    List<Chapter> getDirectory(@Param("novelId")int novelId,@Param("limit")int limit);

    @Select("select * from chapter where novelId = #{novelId} and chapterId = #{chapterId}")
    @ResultType(Chapter.class)
    Chapter getChapterByChapterId(@Param("novelId") int novelId,@Param("chapterId") int chapterId);

    @Select("select * from novel where cover is not null limit 10")
    @ResultType(Novel.class)
    List<Novel> getRecommentNovelsTop10();

    @Select("select * from novel where cover is not null and (name like '%${keyword}%' or author like '%${keyword}%') limit 10")
    @ResultType(Novel.class)
    List<Novel> getNovelsByNameOrAuthor(@Param("keyword") String keyword);

    @Select("select * from dictionary where type = #{type}")
    @ResultType(Dictionary.class)
    List<Dictionary> getDicByType(@Param("type") String type);

    @Select("select id,name,author,introduction from novel where tagid = #{tagId} and cover is not null limit ${(page-1)*10},10")
    @ResultType(Novel.class)
    List<Novel> getNovelsByTag(@Param("tagId") Integer tagId,@Param("page") Integer page);
}
