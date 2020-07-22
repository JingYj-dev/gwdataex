package com.hnjz.db.util;

import com.hnjz.db.util.StringHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:38
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

    public static Map toMap(String property, String delim, Properties properties) {
        Map map = new HashMap();
        String propValue = properties.getProperty(property);
        if (propValue != null) {
            StringTokenizer tokens = new StringTokenizer(propValue, delim);

            while(tokens.hasMoreTokens()) {
                map.put(tokens.nextToken(), tokens.hasMoreElements() ? tokens.nextToken() : "");
            }
        }

        return map;
    }

    public static String[] toStringArray(String property, String delim, Properties properties) {
        return toStringArray(properties.getProperty(property), delim);
    }

    public static String[] toStringArray(String propValue, String delim) {
        return propValue != null ? StringHelper.split(delim, propValue) : null;
    }

    public static Properties maskOut(Properties props, String key) {
        Properties clone = (Properties)props.clone();
        if (clone.get(key) != null) {
            clone.setProperty(key, "****");
        }

        return clone;
    }

    private PropertiesHelper() {
    }
}
