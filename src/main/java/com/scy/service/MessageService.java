package com.scy.service;

import com.scy.dao.MessageDao;
import com.scy.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shichengyao on 1/15/17.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    public int addMessage(Message message) {
        return messageDao.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId, offset, limit);
    }

    public int getConversationUnReadCount(int userId, String conversation) {
        return messageDao.getConversationUnReadCount(userId, conversation);
    }

    public int deleteConversationDetail(int msgId) {
        return messageDao.deleteConversationDetail(msgId);
    }
}
