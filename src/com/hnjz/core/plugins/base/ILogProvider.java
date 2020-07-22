package com.hnjz.core.plugins.base;

import com.hnjz.core.model.ILog;

public interface ILogProvider {
    String ROLE = com.hnjz.core.plugins.base.ILogProvider.class.getName();

    void log(ILog var1);
}
