package com.scy.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;

/**
 * Jedis与Redis整合
 * <p>
 * Created by Shichengyao on 1/24/17.
 */
@Named
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

    public void set(String key, Object o) {
        set(key, JSONObject.toJSONString(o));
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String result = jedis.get(key);
            return result;
        } catch (Exception e) {
            logger.error("jedis's get is wrong {}", e.getMessage(), e);
            return null;
        } finally {
            jedis.close();
        }
    }

    public <T> T get(String key, Class<T> c) {
        String result = get(key);
        if (result != null) {
            return JSONObject.parseObject(result, c);
        }
        return null;
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("Jedis's sadd is wrong {}", e.getMessage(), e);
            return 0;
        } finally {
            jedis.close();
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("Jedis's srem is wrong {}", e.getMessage(), e);
            return 0;
        } finally {
            jedis.close();
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("Jedis's sismember is wrong {}", e.getMessage(), e);
            return false;
        } finally {
            jedis.close();
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
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
     *
     * @param key
     * @param value
     */
    public void setex(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.setex(key, 72 * 3600, value);
        } catch (Exception e) {
            logger.error("Jedis's setex is wrong {}", e.getMessage(), e);
        } finally {
            jedis.close();
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("Jedis's lpush is wrong {}", e.getMessage(), e);
            return 0;
        } finally {
            jedis.close();
        }
    }

    public List<String> brpop(Integer timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("Jedis's brpop is wrong {}", e.getMessage(), e);
            return null;
        } finally {
            jedis.close();
        }
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public boolean tryGetDistributedLock(String lockKey, String requestId, Long expireTime) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }

    private static final Long RELEASE_SUCCESS = 1L;

    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = getJedis();
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }


}
