package com.scy.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shichengyao on 12/18/16.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();

    public void set(String key, Object val) {
        objs.put(key, val);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
