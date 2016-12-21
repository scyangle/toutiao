package com.scy.service;

import com.scy.dao.UserDao;
import com.scy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shichengyao on 12/18/16.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUser(int id) {
        return userDao.selectById(id);
    }
}
