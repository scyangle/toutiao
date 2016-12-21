package com.scy.controller;

import com.scy.model.User;
import com.scy.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.Param;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Shichengyao on 11/15/16.
 */
@Controller
public class IndexController {
    private Logger log = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private ToutiaoService toutiaoService;

//    @RequestMapping("/")
//    @ResponseBody
//    public String index() {
//        System.out.println("/");
//        return "Hello Scy"+"<br>"+toutiaoService.say();
//    }

    @RequestMapping(value = "/vm", produces = "application/json; charset=UTF-8"
    )
    public String news(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String temp = headerNames.nextElement();
            System.out.println(temp + " : " + request.getHeader(temp));
        }
        System.out.println("==============================");
//        Collection<String> responseHeaderNames = response.getHeaderNames();
//        Iterator<String> iterator = responseHeaderNames.iterator();
//        while (iterator.hasNext()) {
//            String s = iterator.next();
//            System.out.println(s + " : " + response.getHeader(s));
//        }
//        System.out.println("request: "+request.getContentType());
//        System.out.println(response.getContentType());
//        model.addAttribute("name", "Tom");
//        List<String> list = Arrays.asList(new String[]{"Red", "Green", "Blue"});
//        User user = new User("Giant");
//        model.addAttribute(user);
//        model.addAttribute("msg", session.getAttribute("msg"));
        return "news";
    }

    @RequestMapping(value = "/request", produces = "*;charset=UTF-8")
    @ResponseBody
    public String header(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuffer buffer = new StringBuffer();
        while (headerNames.hasMoreElements()) {
            String temp = headerNames.nextElement();
            buffer.append(temp + " : " + request.getHeader(temp) + "<br>");
        }
        buffer.append("requestQuery : " + request.getQueryString());
        log.info("======log=======");
        return buffer.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String setResponse(@CookieValue(name = "userId", defaultValue = "007") String newCode, HttpServletResponse response, @RequestParam(value = "key", defaultValue = "nk") String key,
                              @RequestParam(value = "value", defaultValue = "008") String value) {
        response.addCookie(new Cookie(key, value));
        return "NewCoder from cookie " + newCode;
    }

    //    @RequestMapping("/redirect/{code}" )
//    public RedirectView redierct(@PathVariable int code) {
//        RedirectView redirectView=new RedirectView("/", true);
//        if (code == 301) {
//            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        }
//        return  redirectView;
//    }
//    @RequestMapping("/redirect")
//    public String redierct(){
//        return "redirect:/vm";
//    }
    @RequestMapping(value = "/redirect/{code}", produces = "*/*;charset=UTF-8")
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("*/*;charset=UTF-8");
        RedirectView red = new RedirectView("/vm");
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
//        session.setAttribute("msg", "Jump from redirect.");
//        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(name = "key", required = false) String key) {
        if ("admin".equals(key)) {
            return "Hello admin";
        }
        throw new IllegalArgumentException("Key 错误");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }
}
