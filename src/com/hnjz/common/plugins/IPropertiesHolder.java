package com.hnjz.common.plugins;

public interface IPropertiesHolder {
    String ROLE = com.hnjz.common.plugins.IPropertiesHolder.class.getName();

    String getProperty(String var1);

    String getProperty(String var1, String var2);

    String translateVariables(String var1);
}
