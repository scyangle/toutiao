package com.scy.dao;

import com.scy.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface NewsDao {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = "title,link,image,like_count,comment_count,created_date,user_id";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")values " +
            "(#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select",SELECT_FIELDS," from ",TABLE_NAME," where id=#{newsId}"})
    News getById(Integer newsId);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit")int limit);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    @Update({"update ", TABLE_NAME, " set comment_count=#{count} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("count") int count);

    @Select({"select count(id) from ",TABLE_NAME})
    long getNewsAllCount();

    @Select({"select count(id) from ",TABLE_NAME," where user_id=#{userId}"})
    long getUserNewsAllCount( @Param("userId") int userId);
}
