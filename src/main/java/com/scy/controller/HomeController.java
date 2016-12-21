package com.scy.controller;

import com.scy.model.News;
import com.scy.model.ViewObject;
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

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> latesNews = newsService.getLatesNews(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (News news : latesNews) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("news", news);
            viewObject.set("user",userService.getUser(news.getUserId()));
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
