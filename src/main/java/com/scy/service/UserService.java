package com.scy.service;

import com.scy.dao.LoginTicketDao;
import com.scy.dao.UserDao;
import com.scy.model.LoginTicket;
import com.scy.model.User;
import com.scy.utils.ToutiaoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public User getUser(int id) {
        return userDao.selectById(id);
    }

    public Map<String, Object> register(String name, String password) {
        Map<String, Object> map = new HashMap();
        if (StringUtils.isBlank(name)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDao.selectByName(name);
        if (user != null) {
            map.put("msgname", "用户已经被注册");
            return map;
        }

        //密码强度
        user = new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtils.MD5(password + user.getSalt()));
        userDao.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String name, String password) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isBlank(name)) {
            result.put("msgname", "用户名不能为空");
            return result;
        }
        if (StringUtils.isBlank(password)) {
            result.put("msgpwd", "密码不能为空");
            return result;
        }
        User user = userDao.selectByName(name);
        if (user == null) {
            result.put("msgname", "用户不存在");
            return result;
        }
        if (!ToutiaoUtils.MD5(password+user.getSalt()).equals(user.getPassword())) {
            result.put("msgpwd", "密码错误");
            return result;
        }
        String ticket = addLoginTicket(user.getId());
        result.put("ticket", ticket);
        return result;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

}
