package com.scy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Description:  读取配置文件工具类
 *
 * @author shichengyao
 * @Date 2017/11/29
 */
public class PropertiesUtils {
    private final static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
    private static Properties props;
    private static Set<String> fileNameSet = new HashSet<>();
    private final static String defaultFile = "application.properties";
    private static Map<String, Object> addedMap = new ConcurrentHashMap<>();

    static {
        props = new Properties();
        loadProp();
    }

    private static void loadProp() {
        InputStream in = null;
        try {
            fileNameSet.add(defaultFile);
            for (String temp : fileNameSet) {
                in = PropertiesUtils.class.getClassLoader().getResourceAsStream(temp);
                props.load(in);
            }
        } catch (IOException e) {
            logger.error("PropertiesUtils load file is error:", e);
        }
    }

    public static Object getProperty(String key) {
        String value = props.getProperty(key, "");
        return value;
    }

    public static void setProperty(String key, Object value) {
        props.put(key, value);
        addedMap.put(key, value);
    }

    public static void appendPropertyFile(String... fileName) {
        for (String temp : fileName) {
            PropertiesUtils.fileNameSet.add(temp);
        }
        logger.info("PropertiesUtils append file filename=" + Arrays.toString(fileName));
        loadProp();
        props.putAll(addedMap);
    }
}
