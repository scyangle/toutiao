package com.scy.controller;

import com.scy.service.UserService;
import com.scy.utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Shichengyao on 1/1/17.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam(value="username",required = false) String username,
                                   @RequestParam(value="password",required = false) String password,
                                   @RequestParam(value="rember",defaultValue="0") Integer remember,
                                   HttpServletResponse response) {
        try{
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtils.getJsonString(0, "注册成功");
            }else{
                return ToutiaoUtils.getJsonString(1, map);
            }
        }catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtils.getJsonString(1, "注册异常");
        }

    }


    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember",defaultValue="0") Integer remember,
                        HttpServletResponse response) {
        Map<String, Object> map = userService.login(username, password);
        if (map.containsKey("ticket")) {
            Cookie cookie=new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath("/");
            if (remember > 0) {
                cookie.setMaxAge(3600*24*5);
            }
            response.addCookie(cookie);
            return ToutiaoUtils.getJsonString(0, "登录成功");
        }else{
            return ToutiaoUtils.getJsonString(1, map);
        }

    }
}
