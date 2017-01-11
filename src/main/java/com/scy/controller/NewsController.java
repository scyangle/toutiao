package com.scy.controller;

import com.scy.service.NewsService;
import com.scy.service.QiniuService;
import com.scy.utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Shichengyao on 1/5/17.
 */
@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private NewsService newsService;

    @Autowired
    private QiniuService qiniuService;

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
}
