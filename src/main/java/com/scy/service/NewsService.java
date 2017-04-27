package com.scy.service;

import com.scy.dao.NewsDao;
import com.scy.model.News;
import com.scy.utils.ToutiaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

    public News getById(int newsId) {
        return newsDao.getById(newsId);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtils.isFileAllowed(fileExt)) {
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtils.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtils.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    public News getById(Integer newsId) {
        return newsDao.getById(newsId);
    }

    public int updateLikeCount(int id, int count) {
        return newsDao.updateLikeCount(id, count);
    }
    public int addNews(News news) {
        newsDao.addNews(news);
        return news.getId();
    }
    public int updateCommentCount(int id,int count) {
        return newsDao.updateCommentCount(id, count);
    }

    public long getNewsAllCount() {
        return newsDao.getNewsAllCount();
    }
    public long getUserNewsAllCount(int userId) {
        return newsDao.getUserNewsAllCount(userId);
    }
}
