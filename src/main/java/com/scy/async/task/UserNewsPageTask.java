package com.scy.async.task;

import com.scy.service.NewsService;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Shichengyao on 4/27/17.
 */
public class UserNewsPageTask extends RecursiveTask<Long> {
    private NewsService newsService;

    private Integer userId;

    public UserNewsPageTask(Integer userId,NewsService newsService) {
        this.newsService = newsService;
        this.userId = userId;
    }

    @Override
    protected Long compute() {
        return newsService.getUserNewsAllCount(userId);
    }
}
