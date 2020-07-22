package com.hnjz.util;

import java.util.Properties;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:18
 */
public class PropertiesHelper {
    public static boolean getBoolean(String property, Properties properties) {
        return Boolean.valueOf(properties.getProperty(property));
    }

    public static boolean getBoolean(String property, Properties properties, boolean defaultValue) {
        String setting = properties.getProperty(property);
        return setting == null ? defaultValue : Boolean.valueOf(setting);
    }

    public static int getInt(String property, Properties properties, int defaultValue) {
        String propValue = properties.getProperty(property);
        return propValue == null ? defaultValue : Integer.parseInt(propValue);
    }

    public static long getLong(String property, Properties properties, long defaultValue) {
        String propValue = properties.getProperty(property);
        return propValue == null ? defaultValue : Long.parseLong(propValue);
    }

    public static String getString(String property, Properties properties, String defaultValue) {
        String propValue = properties.getProperty(property);
        return propValue == null ? defaultValue : propValue;
    }

    public static Integer getInteger(String property, Properties properties) {
        String propValue = properties.getProperty(property);
        return propValue == null ? null : Integer.valueOf(propValue);
    }

    private PropertiesHelper() {
    }
}
