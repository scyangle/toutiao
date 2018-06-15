package com.scy.controller;

import com.scy.aspect.LogExecutionTime;
import com.scy.async.task.PageTask;
import com.scy.async.task.UserNewsPageTask;
import com.scy.model.*;
import com.scy.service.LikeService;
import com.scy.service.NewsService;
import com.scy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;


@Controller
public class HomeController implements InitializingBean {
    private Logger logger;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @Autowired
    private PageTask pageTask;


    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> latesNews = newsService.getLatesNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (News news : latesNews) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("news", news);
            viewObject.set("user", userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                viewObject.set("like", likeService.getLikeStatus(localUserId, Entity.ENTITY_NEWS, news.getId()));
            } else {
                viewObject.set("like", 0);
            }
            vos.add(viewObject);
        }
        return vos;
    }

    @LogExecutionTime
    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop,
                        @RequestParam(value = "page", defaultValue = "1") Integer page) {
        if (page > 0) {
            int hasReadPage = page - 1;
            int pageSize = 10;
            model.addAttribute("currentPage",page);
            model.addAttribute("vos", getNews(0, hasReadPage * 10, pageSize));
        } else {
            model.addAttribute("currentPage",page);
            model.addAttribute("vos", getNews(0, 0, 10));
        }
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(pageTask);
        try {
            Long pageTaskCount = pageTask.get();
            Long pageCount = pageTaskCount % 10 == 0 ? pageTaskCount / 10 : pageTaskCount / 10 + 1;
            int[] pageNums = new int[pageCount.intValue()];
            model.addAttribute("pageNums", pageNums);
            model.addAttribute("pageCount", pageCount);
        } catch (InterruptedException e) {
            logger.error("HomeController index : {}", e);
        } catch (ExecutionException e) {
            logger.error("HomeController index : {}", e);
        } finally {
            pool.shutdown();
        }
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(value = {"/user/{userId}"}, method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable Integer userId,
                            @RequestParam(value = "page", defaultValue = "1") Integer page,HttpServletRequest request) {
        if (page > 0) {
            int hasReadPage = page - 1;
            int pageSize = 10;
            model.addAttribute("currentPage",page);
            model.addAttribute("vos", getNews(userId, hasReadPage * 10, pageSize));
        } else {
            model.addAttribute("currentPage",page);
            model.addAttribute("vos", getNews(userId, 0, 10));
        }
        ForkJoinPool pool = new ForkJoinPool();
        UserNewsPageTask userNewsPageTask = new UserNewsPageTask(userId,newsService);
        pool.submit(userNewsPageTask);
        try {
            Long pageTaskCount = userNewsPageTask.get();
            Long pageCount = pageTaskCount % 10 == 0 ? pageTaskCount / 10 : pageTaskCount / 10 + 1;
            int[] pageNums = new int[pageCount.intValue()];
            model.addAttribute("pageNums", pageNums);
            model.addAttribute("pageCount", pageCount);
        } catch (InterruptedException e) {
            logger.error("HomeController /user/{} : {}",userId,e);
        } catch (ExecutionException e) {
            logger.error("HomeController /user/{} : {}",userId,e);
        } finally {
            pool.shutdown();
        }
        String requestURI = request.getRequestURI();
        model.addAttribute("requestURI", requestURI);
//        model.addAttribute("vos", getNews(userId, 0, 10));
        return "news";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger = LoggerFactory.getLogger(HomeController.class);
    }
}
