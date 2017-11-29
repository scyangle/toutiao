package com.scy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scy.model.Qiuniu;
import com.scy.model.User;
import com.scy.model.ViewObject;
import com.scy.service.QiniuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;


@Controller
@RequestMapping("/test/")
public class TestController {
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

}
