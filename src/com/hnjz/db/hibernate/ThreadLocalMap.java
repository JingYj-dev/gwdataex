package com.hnjz.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:22
 */
public class ThreadLocalMap {
    private static Log logger = LogFactory.getLog(ThreadLocalMap.class);
    protected static final ThreadLocal<Map<String, Object>> threadContext=new ThreadLocalMap.MapThreadLocal();

    private ThreadLocalMap() {
    }

    public static void put(String key, Object value) {
        getContextMap().put(key, value);
    }

    public static Object remove(String key) {
        return getContextMap().remove(key);
    }

    public static Object get(String key) {
        return getContextMap().get(key);
    }

    protected static Map<String, Object> getContextMap() {
        return (Map) threadContext.get();
    }

    public static void reset() {
        getContextMap().clear();
    }

    private static class MapThreadLocal extends ThreadLocal<Map<String, Object>> {
        private MapThreadLocal() {
        }

        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>() {
                private static final long serialVersionUID = 3637958959138295593L;

                public Object put(String key, Object value) {
                    if (ThreadLocalMap.logger.isDebugEnabled()) {
                        if (this.containsKey(key)) {
                            ThreadLocalMap.logger.debug("Overwritten attribute to thread context: " + key + " = " + value);
                        } else {
                            ThreadLocalMap.logger.debug("Added attribute to thread context: " + key + " = " + value);
                        }
                    }

                    return super.put(key, value);
                }
            };
        }
    }
}
