package com.scy.controller;

import com.scy.model.*;
import com.scy.service.LikeService;
import com.scy.service.NewsService;
import com.scy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shichengyao on 12/18/16.
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> latesNews = newsService.getLatesNews(userId, offset, limit);
        int localUserId=hostHolder.getUser()!=null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (News news : latesNews) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("news", news);
            viewObject.set("user",userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                viewObject.set("like",likeService.getLikeStatus(localUserId, Entity.ENTITY_NEWS, news.getId()));
            }else{
                viewObject.set("like", 0);
            }
            vos.add(viewObject);
        }
        return vos;
    }

    @RequestMapping(value = {"/", "/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getNews(0, 0, 10));
        return "home";
    }

}
