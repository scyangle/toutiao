package com.scy.controller;

import com.scy.model.*;
import com.scy.service.CommentService;
import com.scy.service.NewsService;
import com.scy.service.QiniuService;
import com.scy.service.UserService;
import com.scy.utils.ToutiaoUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shichengyao on 1/5/17.
 */
@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/image",method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtils.IMAGE_DIR+imageName)), response.getOutputStream());
        } catch (IOException e) {
            logger.error("读取图片错误" + imageName, e);
        }
    }

    @RequestMapping(path = {"/uploadImage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
//            String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtils.getJsonString(1, "图片上传失败");
            }
            return ToutiaoUtils.getJsonString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtils.getJsonString(1, "上传失败");
        }
    }

    @RequestMapping(value = {"/news/{newsId}"}, method = RequestMethod.GET)
    public String NewsDetail(@PathVariable(value = "newsId") Integer newsId,Model model) {
        try {
            News news = newsService.getById(newsId);
            if (news != null) {
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), Entity.ENTITY_NEWS);
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                for (Comment comment : comments) {
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment", comment);
                    commentVO.set("user", userService.getUser(comment.getUserId()));
                    commentVOs.add(commentVO);
                }
                model.addAttribute("comments", commentVOs);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        } catch (Exception e) {
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";
    }


    @RequestMapping(path = {"/addComment"}, method = RequestMethod.POST)
    public String addCommnet(@Param("newsId") int newsId, @Param("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setStatus(0);
            comment.setEntityType(Entity.ENTITY_NEWS);
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setUserId(hostHolder.getUser().getId());
            comment.setCreatedDate(new Date());
            commentService.addComment(comment);
        } catch (Exception e) {
            logger.info("添加评论错误",e);
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
}
