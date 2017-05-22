package com.scy.controller;

import com.scy.async.EventModel;
import com.scy.async.EventProducer;
import com.scy.async.EventType;
import com.scy.model.Entity;
import com.scy.model.HostHolder;
import com.scy.model.News;
import com.scy.service.LikeService;
import com.scy.service.NewsService;
import com.scy.utils.ToutiaoUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    NewsService newsService;

    @RequestMapping(value = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String like(@Param("newsId") int newsId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(), Entity.ENTITY_NEWS, newsId);
        //更新喜欢数量
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId));
        return ToutiaoUtils.getJsonString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), Entity.ENTITY_NEWS, newsId);
        // 更新喜欢数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return ToutiaoUtils.getJsonString(0, String.valueOf(likeCount));
    }

}
