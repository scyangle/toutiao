package com.scy.async.task;

import com.scy.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Shichengyao on 4/24/17.
 */
@Component
public class PageTask extends RecursiveTask<Long>{
    @Autowired
    private NewsService newsService;

    @Override
    protected Long compute() {
        return newsService.getNewsAllCount();
    }
}
