package com.free.novel.mapper;

import com.free.novel.entity.*;
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

    @Select("select *,getMessages(id) messages from user where email=#{account}")
    @ResultType(User.class)
    User selectUserByEmail(String account);
    @Select("select *,getMessages(id) messages from user where phone=#{account}")
    @ResultType(User.class)
    User selectUserByPhone(String account);

    @InsertProvider(type =sqlbuild.class,method = "insertOrUpdateUser")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
    int insertOrUpdateUser(User user);
    @Select("select * from user where id = #{id}")
    @ResultType(User.class)
    User selectUserByPrimaryKey(Integer id);

    @Select("select * from message where feedbackUserId = #{userid} and read is not null")
    @ResultType(Message.class)
    List<Message> getMessages(Integer userid);

    @Update("UPDATE message set `read` = 1 where id = #{messageId} ")
    int markRead(Integer messageId);

    @InsertProvider(type =sqlbuild.class,method = "insertOrUpdateMessage")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
    int insertOrUpdateMessage(Message message);



    class sqlbuild{
       public String insertOrUpdateMessage(Message message){
           StringBuilder sb = new StringBuilder();
           String sql = new SQL(){{
               INSERT_INTO("message");
               if(message.getId()!=null){
                   VALUES("id","#{id}");
               }
               if(message.getFeedbackUserId()!=null){
                   VALUES("feedbackUserId","#{feedbackUserId}");
               }
               if(message.getFeedback()!=null){
                   VALUES("feedback","#{feedback}");
               }
               if(message.getFeedbackTime()!=null){
                   VALUES("feedbackTime","#{feedbackTime}");
               }
               if(message.getReply()!=null){
                   VALUES("reply","#{reply}");
               }
               if(message.getReplyTime()!=null){
                   VALUES("replyTime","#{replyTime}");
               }
               if(message.getReplyUserId()!=null){
                   VALUES("replyUserId","#{replyUserId}");
               }
               if(message.getRead()!=null){
                   VALUES("read","#{read}");
               }
           }}.toString();
           sb.append(sql).append(" on DUPLICATE KEY UPDATE ");
           sb.append(message.getRead()!=null?"read=VALUES(read),":"");
           sb.append(message.getReplyUserId()!=null?"replyUserId=VALUES(replyUserId),":"");
           sb.append(message.getReply()!=null?"reply=VALUES(reply),":"");
           sb.append(message.getReplyTime()!=null?"replyTime=VALUES(replyTime),":"");
           sql = sb.toString();
           sql = sql.substring(0,sql.length()-1);
           return sql;
       }

       public String insertOrUpdateUser(User user){
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
                if(user.getSignInTime()!=null){
                    VALUES("signInTime","#{signInTime}");
                }

            }}.toString();
            sb.append(sqlbuild).append(" on DUPLICATE KEY UPDATE ");
            sb.append(user.getPwd()!=null?"pwd=VALUES(pwd),":"");
            sb.append(user.getLastLoginTime()!=null?"lastLoginTime=VALUES(lastLoginTime),":"");
            sb.append(user.getNick()!=null?"nick=VALUES(nick),":"");
            sb.append(user.getGoldenBean()!=null?"goldenBean=VALUES(goldenBean),":"");
            sb.append(user.getExpireDate()!=null?"expireDate=VALUES(expireDate),":"");
            sb.append(user.getSignInTime()!=null?"signInTime=VALUES(signInTime),":"");
            String sql = sb.toString();
            sql = sql.substring(0,sql.length()-1);
            return sql;

        }

    }
}
