package com.scy.controller;

import com.alibaba.fastjson.JSONObject;
import com.scy.model.Qiuniu;
import com.scy.service.QiniuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Shichengyao on 1/11/17.
 */
@RestController
public class TestController {
    @Autowired
    private Qiuniu qiuniu;
    @Value("${qiniu.bucketname}")
    private String st;
    @RequestMapping("/pt")
    public String pt(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", qiuniu.getBucketname());
        return jsonObject.toJSONString();
    }

}
