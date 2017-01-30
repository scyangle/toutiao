package com.scy.async;

import com.scy.utils.JedisAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Shichengyao on 1/27/17.
 */
@Service
public class EventConsumer implements InitializingBean{
    private static Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private JedisAdapter jedisAdapter;
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
