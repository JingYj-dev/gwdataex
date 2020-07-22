package com.hnjz.components.cache.impl;

import com.hnjz.components.cache.ICacher;
import com.hnjz.components.cache.IDataCache;
import com.hnjz.components.cache.impl.CacheUtil;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.common.plugins.IPluginFactory;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.db.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:22
 */
public abstract class AbstractDataCache extends AbstractConfigurablePlugin implements IDataCache {
    private static Log logger = LogFactory.getLog(com.hnjz.components.cache.impl.AbstractDataCache.class);
    private String cacheName = null;
    private long refreshInterval = 0L;
    private String cacheType = null;
    protected IPluginFactory fac = null;

    public AbstractDataCache() {
    }

    public String getCacheName() {
        return this.cacheName;
    }

    protected void doConfig(Map<String, String> conf) {
        this.cacheName = (String)conf.get("cache-name");
        String value = (String)conf.get("refresh-interval");
        if (!StringHelper.isEmpty(value)) {
            try {
                this.refreshInterval = Long.parseLong(value.trim());
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        this.cacheType = (String)conf.get("cache-type");
        if (this.cacheType == null) {
            this.cacheType = ConfigurationManager.getConfigurationManager().getSysConfigure("app.cacher", "none");
        }

    }

    protected long getRefreshInterval() {
        return this.refreshInterval;
    }

    public void plug(IPluginFactory fac) {
        this.fac = fac;
    }

    protected ICacher getInnerCacher() {
        return (ICacher)this.fac.lookup(ICacher.class, this.cacheType);
    }

    protected String getCachedKey(Object key) {
        return key == null ? null : CacheUtil.getCacheName(this.getCacheName(), key);
    }

    public Object getObject(Object key) {
        ICacher cacher = this.getInnerCacher();
        if (cacher == null) {
            return null;
        } else {
            String ck = this.getCachedKey(key);
            if (ck == null) {
                return null;
            } else {
                Object obj = cacher.getObject(ck);
                if (obj == null) {
                    obj = this.getInitialData(key);
                    if (obj != null) {
                        long interval = this.getRefreshInterval();
                        if (interval <= 0L) {
                            cacher.setObject(ck, obj);
                        } else {
                            cacher.setObject(ck, obj, interval);
                        }
                    }
                } else {
                    logger.debug("�ӻ����ȡ[" + this.cacheName + "][" + key + "]=[" + obj + "]");
                }

                return obj;
            }
        }
    }

    public Map<String, Object> getObjects(String... keys) {
        ICacher cacher = this.getInnerCacher();
        if (cacher != null && keys != null && keys.length != 0) {
            Map<String, Object> result = null;
            String[] keyeds = new String[keys.length];
            int i = 0;
            String[] arr$ = keys;
            int n = keys.length;

            for(int i$ = 0; i$ < n; ++i$) {
                String key = arr$[i$];
                keyeds[i++] = this.getCachedKey(key);
            }

            result = cacher.getObjects(keyeds);
            if (result == null) {
                result = new HashMap();
            }

            Map<String, Object> resultR = new HashMap();
            i = 0;

            for(n = keyeds.length; i < n; ++i) {
                String ck = keyeds[i];
                Object obj = ((Map)result).get(ck);
                if (obj == null) {
                    obj = this.getInitialData(keys[i]);
                    if (obj != null) {
                        ((Map)result).put(ck, obj);
                        long interval = this.getRefreshInterval();
                        if (interval <= 0L) {
                            cacher.setObject(ck, obj);
                        } else {
                            cacher.setObject(ck, obj, interval);
                        }
                    }
                }

                if (obj != null) {
                    resultR.put(keys[i], obj);
                }
            }

            return resultR;
        } else {
            return null;
        }
    }

    public void setObject(String key, Object value) {
        ICacher cacher = this.getInnerCacher();
        if (cacher != null) {
            String ck = this.getCachedKey(key);
            if (ck != null) {
                cacher.setObject(ck, value);
            }
        }
    }

    public void setObject(String key, Object value, long time) {
        ICacher cacher = this.getInnerCacher();
        if (cacher != null) {
            String ck = this.getCachedKey(key);
            if (ck != null) {
                cacher.setObject(ck, value, time);
            }
        }
    }

    public void remove(Object key) {
        ICacher cacher = this.getInnerCacher();
        if (cacher != null) {
            String ck = this.getCachedKey(key);
            if (ck != null) {
                cacher.remove(ck);
                logger.debug("�ӻ����Ƴ�[" + this.cacheName + "][" + ck + "]");
            }
        }
    }

    public void removeAll() {
        ICacher cacher = this.getInnerCacher();
        if (cacher != null) {
            cacher.removeAll();
        }
    }

    public boolean addObject(String key, Object value, long time) {
        ICacher cacher = this.getInnerCacher();
        if (cacher == null) {
            return false;
        } else {
            String ck = this.getCachedKey(key);
            return ck == null ? false : cacher.addObject(ck, value, time);
        }
    }

    protected abstract Object getInitialData(Object var1);
}
