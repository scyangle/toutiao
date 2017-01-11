package com.scy.controller;

import com.scy.service.QiniuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Shichengyao on 1/11/17.
 */
@RestController
public class TestController {
    @Autowired
    private static QiniuService qiniuService;

    @RequestMapping("/pt")
    public String pt(){
        QiniuService q = qiniuService;
        System.out.println(qiniuService.toString());
        return qiniuService.toString();

    }

}
