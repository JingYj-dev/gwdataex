package com.hnjz.components.cache;

import java.util.Map;

public interface ICacher {
    String ROLE = com.hnjz.components.cache.ICacher.class.getName();

    Object getObject(Object var1);

    Map<String, Object> getObjects(String... var1);

    void setObject(String var1, Object var2);

    void setObject(String var1, Object var2, long var3);

    void remove(Object var1);

    void removeAll();

    boolean addObject(String var1, Object var2, long var3);
}
