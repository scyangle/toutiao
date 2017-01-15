package com.scy.controller;

import com.alibaba.fastjson.JSONObject;
import com.scy.model.HostHolder;
import com.scy.model.Message;
import com.scy.service.MessageService;
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

import java.util.Date;

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

//    @RequestMapping(value = {"/msg/detail"},method = RequestMethod.GET)
//    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
//
//    }

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
}
