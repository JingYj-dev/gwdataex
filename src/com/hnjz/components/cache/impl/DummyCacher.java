package com.hnjz.components.cache.impl;

import com.hnjz.components.cache.ICacher;

import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:26
 */
public class DummyCacher implements ICacher {
    public DummyCacher() {
    }

    public Object getObject(Object key) {
        return null;
    }

    public Map<String, Object> getObjects(String... keys) {
        return null;
    }

    public void setObject(String key, Object value) {
    }

    public void setObject(String key, Object value, long time) {
    }

    public void remove(Object key) {
    }

    public void removeAll() {
    }

    public boolean addObject(String key, Object value, long time) {
        return false;
    }
}
