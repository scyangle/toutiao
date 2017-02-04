package com.scy.async.handler;

import com.scy.async.EventHandler;
import com.scy.async.EventModel;
import com.scy.async.EventType;
import com.scy.model.Message;
import com.scy.model.User;
import com.scy.service.MessageService;
import com.scy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Shichengyao on 1/27/17.
 */
@Component
public class LikeHandler implements EventHandler{
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        User user = userService.getUser(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setContent("用户" + user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(eventModel.getEntityId()));
        message.setFromId(3);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
