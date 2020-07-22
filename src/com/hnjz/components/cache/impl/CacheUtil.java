package com.hnjz.components.cache.impl;

import com.hnjz.components.cache.ICacher;
import com.hnjz.util.Md5Util;

import java.sql.Date;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:24
 */
public class CacheUtil {
    public CacheUtil() {
    }

    public static String getCacheName(Object cacheName, Object value) {
        if (cacheName == null) {
            cacheName = "";
        }

        return Md5Util.MD5Encode((String)cacheName + value);
    }

    public static Date getCacheTime(int time) {
        return new Date((long)(time * 60 * 1000));
    }

    public static Date getCacheTime(String time) throws Exception {
        return new Date((long)(Integer.valueOf(time) * 60 * 1000));
    }

    public static Object getObject(ICacher cacher, String cacheKey) {
        if (cacher == null) {
            return null;
        } else {
            if (cacheKey == null || cacheKey.trim().equals("")) {
                cacheKey = "";
            }

            return cacher.getObject(cacheKey);
        }
    }

    public static void removeObject(ICacher cacher, String cacheKey) {
        if (cacher != null) {
            if (cacheKey == null || cacheKey.trim().equals("")) {
                cacheKey = "";
            }

            cacher.remove(cacheKey);
        }
    }

    public static void setObject(ICacher cacher, String cacheKey, Object value) {
        if (cacher != null) {
            if (cacheKey == null || cacheKey.trim().equals("")) {
                cacheKey = "";
            }

            cacher.setObject(cacheKey, value);
        }
    }

    public static void setObjectWithExpiredMinutes(ICacher cacher, String cacheKey, Object value, int time) {
        if (cacher != null) {
            if (cacheKey == null || cacheKey.trim().equals("")) {
                cacheKey = "";
            }

            cacher.setObject(cacheKey, value, getCacheTime(time).getTime() + System.currentTimeMillis());
        }
    }

    public static void setObjectWithExpiredMicroMinutes(ICacher cacher, String cachedKey, Object value, long time) {
        if (cacher != null) {
            if (cachedKey == null || cachedKey.trim().equals("")) {
                cachedKey = "";
            }

            cacher.setObject(cachedKey, value, time + System.currentTimeMillis());
        }
    }
}
