package com.free.novel.mapper;

import com.free.novel.entity.Novel;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface NovelMapper {

    @Select("select * from novel where id = #{novelId}")
    @ResultType(Novel.class)
    Novel selectByPrimaryKey(int novelId);
}
