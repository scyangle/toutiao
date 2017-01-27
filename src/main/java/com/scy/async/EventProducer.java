package com.scy.async;

import com.alibaba.fastjson.JSONObject;
import com.scy.utils.JedisAdapter;
import com.scy.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shichengyao on 1/27/17.
 */
@Service
public class EventProducer {
    @Autowired
    private JedisAdapter jedisAdapter;

    private static Logger logger = LoggerFactory.getLogger(EventProducer.class);

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
