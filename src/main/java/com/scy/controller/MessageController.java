package com.scy.controller;

import com.alibaba.fastjson.JSONObject;
import com.scy.model.HostHolder;
import com.scy.model.Message;
import com.scy.model.User;
import com.scy.model.ViewObject;
import com.scy.service.MessageService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shichengyao on 1/15/17.
 */
@Controller
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDeatil(@RequestParam("conversationId") String conversationId,Model model) {
        User localUser = hostHolder.getUser();
        List<Message> conversationDetail = messageService.getConversationDetail(conversationId, 0, 10);
        List<ViewObject> viewObjects = new ArrayList<>();
        for (Message message : conversationDetail) {
            User user = userService.getUser(message.getFromId());
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            vo.set("headUrl",user.getHeadUrl());
            viewObjects.add(vo);
        }
        model.addAttribute("messages", viewObjects);
        return "letterDetail";
    }

    @RequestMapping(value = "/msg/addMessage",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message message = new Message();
        message.setCreatedDate(new Date());
        message.setToId(toId);
        message.setFromId(fromId);
        message.setContent(content);
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
        return ToutiaoUtils.getJsonString(message.getId());
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        User localUser = hostHolder.getUser();
        List<Message> conversationList = messageService.getConversationList(localUser.getId(), 0, 10);
        List<ViewObject> viewObjects = new ArrayList<>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            int targetId = localUser.getId() == message.getToId() ? message.getFromId() : message.getToId();
            User user = userService.getUser(targetId);
            vo.set("conversation", message);
            vo.set("headUrl",user.getHeadUrl());
            vo.set("userName", user.getName());
            vo.set("unreadCount", messageService.getConversationUnReadCount(localUser.getId(), message.getConversationId()));
            viewObjects.add(vo);
        }
        model.addAttribute("conversations", viewObjects);
        return "letter";
    }
}
