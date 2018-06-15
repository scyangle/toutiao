package com.scy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scy.aspect.NeedTest;
import com.scy.model.Qiuniu;
import com.scy.model.User;
import com.scy.model.ViewObject;
import com.scy.service.QiniuService;
import com.scy.utils.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.concurrent.*;


@Controller
@RequestMapping("/test/")
public class TestController {
    @Autowired
    private JedisAdapter jedisAdapter;
    @Autowired
    private Qiuniu qiuniu;
    @Value("${qiniu.bucketname}")
    private String st;

    @RequestMapping("/pt")
    @ResponseBody
    public String pt(Model model, @RequestParam String name, @RequestParam String age) {
//        User user = new User();
//        user.setName(name);
//        user.setPassword(age);
//        return JSON.toJSONString(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("age", age);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = {"/ajax"})
    public String ajaxTest() {
        return "ajaxTest";
    }

    @NeedTest
    @RequestMapping(value = {"/getScript"})
    @ResponseBody
    public String getScript() {
        return String.format("alert('%s')", "Hello,return some script");
    }

    @RequestMapping("array.do")
    @ResponseBody
    public String arrayTest(String[] arr) {
        StringBuilder strB = new StringBuilder();
        for (String temp : arr) {
            strB.append(temp).append(" ");
        }
        return strB.toString();
    }

    private ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);

    @NeedTest
    @RequestMapping("/redis/lock")
    @ResponseBody
    public Object testRedisLock(String name) {
        for(int i=0;i<10;i++) {
            newFixedThreadPool.execute(() -> {
                boolean status = jedisAdapter.tryGetDistributedLock(name, String.valueOf(Thread.currentThread().getId()), 60000L);
                System.out.println(Thread.currentThread().getId()+" : "+status);
//                if (status == true) {
//                    boolean releaseStatus = jedisAdapter.releaseDistributedLock(name, String.valueOf(Thread.currentThread().getId()));
//                    System.out.println(releaseStatus + " : " + Thread.currentThread().getId());
//                }
            });
        }
        return "redis";
    }

    @NeedTest(true)
    @RequestMapping("/needTest")
    @ResponseBody
    public Object needTestDemo1() {
        return "This is needTest controller demo";
    }

    @NeedTest(false)
    @RequestMapping("/notNeedTest")
    @ResponseBody
    public Object needTestDemo2() {
        return "This isn't needTest controller demo";
    }

}
