package com.scy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Jedis与Redis整合
 *
 * Created by Shichengyao on 1/24/17.
 */
@Component
public class JedisAdapter implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private Jedis jedis;
    private JedisPool jedisPool;
    @Value("${jedis.host}")
    private String jedisHost;
    @Value("${jedis.port}")
    private Integer jedisPort;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool(jedisHost, jedisPort);
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("jedis's set is wrong {}", e.getMessage(), e);
        } finally {
            jedis.close();
        }
    }

    public String get(String key) {
        String result = null;
        if (key != null) {
            try {
                Jedis jedis = getJedis();
                result = jedis.get(key);
            } catch (Exception e) {
                logger.error("jedis's get is wrong {}", e.getMessage(), e);
            } finally {
                jedis.close();
            }
        }
        return result;
    }

    public long sadd(String key, String value) {
        try {
            Jedis jedis = getJedis();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("Jedis's sadd is wrong {}", e.getMessage(), e);
            return 0;
        } finally {
            jedis.close();
        }
    }

    public long srem(String key, String value) {
        try {
            Jedis jedis = getJedis();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("Jedis's srem is wrong {}", e.getMessage(), e);
            return 0;
        } finally {
            jedis.close();
        }
    }

    public boolean sismenber(String key, String value) {
        try {
            Jedis jedis = getJedis();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("Jedis's sismember is wrong {}", e.getMessage(), e);
            return false;
        } finally {
            jedis.close();
        }
    }

    public long scard(String key) {
        try {
            Jedis jedis = getJedis();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("Jedis's scard is wrong {}", e.getMessage(), e);
            return 0;
        } finally {
            jedis.close();
        }
    }

    /**
     * 设置验证码,防止机器注册，记录上次登陆时间，有效期3day
     * @param key
     * @param value
     */
    public void setex(String key, String value) {
        try {
            Jedis jedis = getJedis();
            jedis.setex(key, 72 * 3600, value);
        } catch (Exception e) {
            logger.error("Jedis's setex is wrong {}",e.getMessage(),e);
        } finally {
            jedis.close();
        }
    }
}
