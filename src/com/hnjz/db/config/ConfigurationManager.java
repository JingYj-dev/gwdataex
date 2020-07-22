package com.hnjz.db.config;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:14
 */
public class ConfigurationManager {
    public static final String MAPPING_FILE = "/hibernate.cfg.xml";

    public ConfigurationManager() {
    }

    public static int getCacheTime() {
        return com.hnjz.core.configuration.ConfigurationManager.getCacheTime();
    }

    public static int getPageSize() {
        return com.hnjz.core.configuration.ConfigurationManager.getPageSize();
    }
}
