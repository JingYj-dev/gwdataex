package com.hnjz.common.plugins;

public interface IPluginFactory {
    String ROLE = com.hnjz.common.plugins.IPluginFactory.class.getName();

    void initialize();

    Object lookup(String var1);

    <T> T lookup(Class<T> var1);

    <T> T lookup(Class<T> var1, String var2);

    Object lookup(String var1, String var2);

    void destroy();
}
