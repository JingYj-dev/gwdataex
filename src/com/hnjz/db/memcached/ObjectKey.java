package com.hnjz.db.memcached;

import java.lang.reflect.Method;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:34
 */
public class ObjectKey {
    private static Object[] ARGS = null;
    private String keyname = null;
    private Method valueMethod = null;

    public ObjectKey(String key, Method method) {
        this.keyname = key;
        this.valueMethod = method;
    }

    public String Key() {
        return this.keyname;
    }

    public String ID(Object ins) {
        String id = null;

        try {
            id = this.valueMethod.invoke(ins, ARGS).toString();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return id;
    }
}
