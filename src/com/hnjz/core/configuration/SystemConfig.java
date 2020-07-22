package com.hnjz.core.configuration;

import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.util.DesUtil;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:38
 */
public class SystemConfig {
    private Properties props = new Properties();
    private static com.hnjz.core.configuration.SystemConfig instance = null;
    private static Log log = LogFactory.getLog(ConfigurationManager.class);

    public SystemConfig() {
    }

    public static synchronized com.hnjz.core.configuration.SystemConfig getSystemConfig() {
        if (instance == null) {
            instance = new com.hnjz.core.configuration.SystemConfig();
            instance.init();
        }

        return instance;
    }

    private void init() {
        try {
            String gfile = "/appconfig.properties";
            boolean hasNoneEncryptedLines = false;
            StringBuffer buf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ConfigurationManager.class.getResourceAsStream(gfile), "UTF-8"));

            String key;
            String lineSep;
            for(String line = null; (line = reader.readLine()) != null; buf.append(line).append(lineSep)) {
                if (!line.trim().startsWith("#")) {
                    line = line.trim();
                    int index = line.indexOf("=");
                    if (index > 0) {
                        key = line.substring(0, index).trim();
                        String value = line.substring(index + 1).trim();
                        String decodeV = null;

                        try {
                            decodeV = DesUtil.decrypt(value);
                        } catch (Exception var11) {
                        }

                        if (decodeV != null) {
                            line = key + "  = " + value;
                            value = decodeV;
                        } else {
                            hasNoneEncryptedLines = true;
                            line = key + "  = " + DesUtil.encrypt(value);
                        }

                        this.props.put(key, value);
                    }
                }

                lineSep = System.getProperty("line.separator");
                if (StringHelper.isEmptyByTrim(lineSep)) {
                    lineSep = "\n";
                }
            }

            reader.close();
            reader = null;
            if (hasNoneEncryptedLines) {
                URL url = ConfigurationManager.class.getResource(gfile);
                key = url.getFile();
                if (key != null) {
                    key = URLDecoder.decode(key, "UTF-8");
                }

                FileWriter fw = new FileWriter(key);
                fw.write(buf.toString());
                fw.close();
            }
        } catch (Exception var12) {
            log.error("装载系统配置文件【appconfig.properties】失败", var12);
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
        return ConfigurationManager.getConfigurationManager().getSysConfigureAsInt("app.cacher.time", 60) * 1000;
    }

    public static int getPageSize() {
        return ConfigurationManager.getConfigurationManager().getSysConfigureAsInt("app.pager.pagesize", 10);
    }

    public static boolean isSecured() {
        return ConfigurationManager.getConfigurationManager().getSysConfigureAsBool("app.global.security", true);
    }

    public static String getCacheType() {
        return ConfigurationManager.getConfigurationManager().getSysConfigure("app.cacher", "memory");
    }

    public static String getMode() {
        return "run";
    }

    public static boolean isAdminMode() {
        String mode = getMode();
        return "admin".equalsIgnoreCase(mode);
    }

    public static String getAppName() {
        String version = ConfigurationManager.getConfigurationManager().getSysConfigure("app.version");
        String appName = ConfigurationManager.getConfigurationManager().getSysConfigure("app.name") + "-" + version;
        return appName;
    }
}
