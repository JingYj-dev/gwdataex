package com.hnjz.components.cache.impl;

import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.components.cache.ICacher;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:25
 */
public class InProcessCacher extends AbstractConfigurablePlugin implements ICacher, Initializable {
    private static Log _log = LogFactory.getLog(com.hnjz.components.cache.impl.InProcessCacher.class);
    Map<Object, com.hnjz.components.cache.impl.InProcessCacher.CacheItem> memCacher = Collections.synchronizedMap(new HashMap());
    private Timer timer = null;
    private long scanInterval = 300000L;

    public InProcessCacher() {
    }

    public Object getObject(Object key) {
        if (key == null) {
            return null;
        } else {
            com.hnjz.components.cache.impl.InProcessCacher.CacheItem item = (com.hnjz.components.cache.impl.InProcessCacher.CacheItem)this.memCacher.get(key);
            if (item != null) {
                if (item.hasExpired()) {
                    this.remove(key);
                    _log.debug("缓存键[" + key + "]=" + item.getValue() + ",已过期");
                    return null;
                } else {
                    return item.value;
                }
            } else {
                return null;
            }
        }
    }

    public Map<String, Object> getObjects(String... keys) {
        Map<String, Object> datum = new HashMap();
        String[] arr$ = keys;
        int len$ = keys.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String key = arr$[i$];
            Object obj = this.getObject(key);
            if (obj != null) {
                datum.put(key, obj);
            }
        }

        return datum;
    }

    public void remove(Object key) {
        if (key != null) {
            this.memCacher.remove(key);
        }
    }

    public void removeAll() {
        this.memCacher.clear();
    }

    public void setObject(String key, Object value) {
        if (key != null) {
            com.hnjz.components.cache.impl.InProcessCacher.CacheItem item = (com.hnjz.components.cache.impl.InProcessCacher.CacheItem)this.memCacher.get(key);
            if (item == null) {
                this.memCacher.put(key, new com.hnjz.components.cache.impl.InProcessCacher.CacheItem(value));
            } else {
                item.setValue(value);
            }

        }
    }

    public void setObject(String key, Object value, long expiredTime) {
        if (key != null) {
            com.hnjz.components.cache.impl.InProcessCacher.CacheItem item = (com.hnjz.components.cache.impl.InProcessCacher.CacheItem)this.memCacher.get(key);
            if (item == null) {
                this.memCacher.put(key, new com.hnjz.components.cache.impl.InProcessCacher.CacheItem(value, expiredTime));
            } else {
                item.setValue(value);
                item.setExpiredTime(expiredTime);
            }

        }
    }

    public void initialize() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            public void run() {
                com.hnjz.components.cache.impl.InProcessCacher.this.refreshCache();
            }
        }, 0L, this.scanInterval);
    }

    private void refreshCache() {
        Iterator entries = this.memCacher.entrySet().iterator();

        while(entries.hasNext()) {
            Map.Entry<Object, com.hnjz.components.cache.impl.InProcessCacher.CacheItem> entry = (Map.Entry)entries.next();
            com.hnjz.components.cache.impl.InProcessCacher.CacheItem item = (com.hnjz.components.cache.impl.InProcessCacher.CacheItem)entry.getValue();
            if (item.hasExpired()) {
                entries.remove();
            }
        }

    }

    public boolean addObject(String key, Object value, long time) {
        synchronized(this) {
            if (this.getObject(key) != null) {
                this.setObject(key, value, time);
                return true;
            } else {
                return false;
            }
        }
    }

    public void doConfig(Map<String, String> config) {
        String value = (String)config.get("scan-interval");
        if (!StringHelper.isEmpty(value)) {
            try {
                this.scanInterval = Long.parseLong(value.trim());
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    private static class CacheItem {
        private Object value;
        private long expiredTime = -1L;

        public CacheItem(Object value) {
            this.value = value;
        }

        public CacheItem(Object value, long time) {
            this.value = value;
            this.expiredTime = time;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public long getExpiredTime() {
            return this.expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public boolean hasExpired() {
            long tm = System.currentTimeMillis();
            return this.expiredTime > 0L && tm > this.expiredTime;
        }
    }
}
