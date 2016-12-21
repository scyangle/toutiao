package com.scy.service;

import com.scy.dao.NewsDao;
import com.scy.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shichengyao on 12/18/16.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getLatesNews(int userId, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(userId,offset,limit);
    }

}
