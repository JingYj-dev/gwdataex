package com.hnjz.components.cache;

import com.hnjz.core.model.IDictable;

import java.util.List;

public interface IDictCache {
    String ROLE = com.hnjz.components.cache.IDictCache.class.getName();

    List<? extends IDictable> getDictableList(String var1);
}
