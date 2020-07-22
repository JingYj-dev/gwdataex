package com.hnjz.core.plugins.base;

import com.hnjz.core.model.IDictable;
import com.hnjz.core.plugins.base.ILogProvider;

import java.util.List;

public interface IDictProvider {
    String ROLE = ILogProvider.class.getName();

    IDictable getDictType(String var1, String var2);

    List<? extends IDictable> getDictType(String var1);

    List<? extends IDictable> getDictListQuery(String var1);

    List<? extends IDictable> getDictListSel(String var1);
}
