package com.free.novel.mapper;

import com.free.novel.entity.Chapter;
import com.free.novel.entity.Dictionary;
import com.free.novel.entity.Novel;
import com.free.novel.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

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

    @Select("select * from dictionary where type = #{type} and status = #{status}")
    @ResultType(Dictionary.class)
    List<Dictionary> getDicByType(@Param("type") String type,@Param("status") Integer status);

    @Select("select id,name,author,introduction from novel where tagid = #{tagId} and cover is not null limit ${(page-1)*10},10")
    @ResultType(Novel.class)
    List<Novel> getNovelsByTag(@Param("tagId") Integer tagId,@Param("page") Integer page);

    @Select("select * from user where email=#{account}")
    @ResultType(User.class)
    User selectUserByEmail(String account);
    @Select("select * from user where phone=#{account}")
    @ResultType(User.class)
    User selectUserByPhone(String account);

    @InsertProvider(type =sqlbuild.class,method = "insertOrUpdate")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
    int insertOrUpdate(User user);
    @Select("select * from user where id = #{id}")
    @ResultType(User.class)
    User selectUserByPrimaryKey(Integer id);

    class sqlbuild{

       public String insertOrUpdate(User user){
           StringBuilder sb = new StringBuilder();
            String sqlbuild = new SQL(){{
                INSERT_INTO("user");
                if(user.getId()!=null){
                    VALUES("id","#{id}");
                }
                if(user.getDeviceID()!=null){
                    VALUES("deviceID","#{deviceID}");
                }
                if(user.getEmail()!=null){
                    VALUES("email","#{email}");
                }
                if(user.getPhone()!=null){
                    VALUES("phone","#{phone}");
                }
                if(user.getLastLoginTime()!=null){
                    VALUES("lastLoginTime","#{lastLoginTime}");
                }
                if(user.getPwd()!=null){
                    VALUES("pwd","#{pwd}");
                }
                if(user.getNick()!=null){
                    VALUES("nick","#{nick}");
                }
                if(user.getRegisterTime()!=null){
                    VALUES("registerTime","#{registerTime}");
                }
                if(user.getGoldenBean()!=null){
                    VALUES("goldenBean","#{goldenBean}");
                }
                if(user.getExpireDate()!=null){
                    VALUES("expireDate","#{expireDate}");
                }

            }}.toString();
            sb.append(sqlbuild).append(" on DUPLICATE KEY UPDATE ");
            sb.append(user.getPwd()!=null?"pwd=VALUES(pwd),":"");
            sb.append(user.getLastLoginTime()!=null?"lastLoginTime=VALUES(lastLoginTime),":"");
            sb.append(user.getNick()!=null?"nick=VALUES(nick),":"");
            sb.append(user.getGoldenBean()!=null?"goldenBean=VALUES(goldenBean),":"");
            sb.append(user.getExpireDate()!=null?"expireDate=VALUES(expireDate),":"");
            String sql = sb.toString();
            sql = sql.substring(0,sql.length()-1);
            return sql;

        }

    }
}
