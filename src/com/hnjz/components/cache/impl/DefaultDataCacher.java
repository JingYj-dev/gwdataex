package com.hnjz.components.cache.impl;

import com.hnjz.components.cache.impl.AbstractDataCache;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:28
 */
public class DefaultDataCacher extends AbstractDataCache {
    public DefaultDataCacher() {
    }

    protected Object getInitialData(Object key) {
        return null;
    }
}
