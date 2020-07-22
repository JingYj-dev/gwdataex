package com.hnjz.core.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:39
 */
public final class ConfigurationManager {
    private Properties props = new Properties();
    private static com.hnjz.core.configuration.ConfigurationManager instance = null;
    private static Log log = LogFactory.getLog(com.hnjz.core.configuration.ConfigurationManager.class);

    public ConfigurationManager() {
    }

    public static synchronized com.hnjz.core.configuration.ConfigurationManager getConfigurationManager() {
        if (instance == null) {
            instance = new com.hnjz.core.configuration.ConfigurationManager();
            instance.init();
        }

        return instance;
    }

    private void init() {
        try {
            this.props.load(com.hnjz.core.configuration.ConfigurationManager.class.getResourceAsStream("/appconfig.properties"));
        } catch (Exception var2) {
            log.error("装载系统配置文件【appconfig.properties】失败", var2);
        }

    }

    public String getSysConfigure(String key) {
        return this.props.getProperty(key);
    }

    public String getSysConfigure(String key, String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }

    public int getSysConfigureAsInt(String key, int defaultValue) {
        String cfg = this.props.getProperty(key);
        return cfg == null ? defaultValue : Integer.parseInt(cfg.trim());
    }

    public boolean getSysConfigureAsBool(String key, boolean dv) {
        String cfg = this.props.getProperty(key);
        return cfg == null ? dv : Boolean.parseBoolean(cfg.trim().toLowerCase());
    }

    public void removeSysConfigure(String key) {
        this.props.remove(key);
    }

    public void updateSysConfigure(String key, String value) {
        this.props.setProperty(key, value);
    }

    public static int getCacheTime() {
        return getConfigurationManager().getSysConfigureAsInt("app.cacher.time", 60) * 1000;
    }

    public static int getPageSize() {
        return getConfigurationManager().getSysConfigureAsInt("app.pager.pagesize", 10);
    }

    public static boolean isSecured() {
        return !isAdminMode() ? true : getConfigurationManager().getSysConfigureAsBool("app.global.security", true);
    }

    public static String getCacheType() {
        return getConfigurationManager().getSysConfigure("app.cacher", "memory");
    }

    public static String getMode() {
        return getConfigurationManager().getSysConfigure("app.mode", "run");
    }

    public static boolean isAdminMode() {
        String mode = getMode();
        return "admin".equalsIgnoreCase(mode);
    }

    public static String getAppName() {
        String version = getConfigurationManager().getSysConfigure("app.version");
        String appName = getConfigurationManager().getSysConfigure("app.name") + "-" + version;
        return appName;
    }

    public static boolean isDebug() {
        return getConfigurationManager().getSysConfigureAsBool("app.logger.debug", false);
    }
}
