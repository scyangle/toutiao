package com.scy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Shichengyao on 1/24/17.
 */
@Component
public class JedisAdapter implements InitializingBean{
    private static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private Jedis jedis;
    private JedisPool jedisPool;
    @Value("${jedis.host}")
    private String jedisHost;
    @Value("${jedis.port}")
    private Integer jedisPort;
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool(jedisHost,jedisPort);
    }
    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("jedis's set is wrong {}",e.getMessage(),e);
        }finally {
            jedis.close();
        }
    }

}
