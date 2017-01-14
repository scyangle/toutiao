package com.scy.dao;

import com.scy.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Shichengyao on 12/17/16.
 */
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
}
