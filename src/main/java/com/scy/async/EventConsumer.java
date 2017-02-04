package com.scy.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scy.utils.JedisAdapter;
import com.scy.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Shichengyao on 2/1/17.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;
    @Autowired
    private JedisAdapter jedisAdapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans= applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            Set<Map.Entry<String, EventHandler>> entries = beans.entrySet();
            for (Map.Entry<String, EventHandler> map : entries) {
                List<EventType> supportEventTypes = map.getValue().getSupportEventTypes();
                for (EventType eventType : supportEventTypes) {
                    if(!config.containsKey(eventType)){
                        config.put(eventType, new ArrayList<EventHandler>());
                    }
                    config.get(eventType).add(map.getValue());
                }
            }

        }
        //启动线程去消费事件
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> messages = jedisAdapter.brpop(0, key);
                    //第一个元素是key的名字
                    for (String message:messages){
                        if (message.equals(key)) {
                            continue;
                        }
                       // 或者：EventModel eventModel= JSON.parseObject(message, EventModel.class);
                        EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
                        //寻找处理这个事件的Handler列表
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for (EventHandler eventHandler:config.get(eventModel.getType())){
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}













































