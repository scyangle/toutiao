package com.scy.model;

import org.springframework.stereotype.Component;

/**
 * Created by Shichengyao on 12/29/16.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void serUser(User user) {
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
