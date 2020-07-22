package com.hnjz.db.util;

import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.db.util.Md5Util;
import com.hnjz.db.util.StringHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:36
 */
public class LockUtil {
    public static final String LOCK_KEY = "_memcached_lock:";

    public LockUtil() {
    }

    public static boolean getLock(String key) {
        Date dt = Calendar.getInstance().getTime();
        return getLock(key, dt);
    }

    public static Object getLockObject(String key) {
        String innerKey = Md5Util.MD5Encode("_memcached_lock:" + key);
        return MemCachedFactory.buildClient().get(innerKey);
    }

    public static boolean getLock(String key, Object lockObj) {
        if (StringHelper.isEmpty(key)) {
            return false;
        } else {
            try {
                String innerKey = Md5Util.MD5Encode("_memcached_lock:" + key);
                boolean hasNoLock = MemCachedFactory.buildClient().add(innerKey, lockObj);
                if (hasNoLock) {
                    return true;
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return false;
        }
    }

    public static void releaseLock(String key) {
        if (!StringHelper.isEmpty(key)) {
            String innerKey = Md5Util.MD5Encode("_memcached_lock:" + key);
            MemCachedFactory.buildClient().delete(innerKey);
        }
    }
}
