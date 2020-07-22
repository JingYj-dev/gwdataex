package com.hnjz.common.plugins.config.util;

import com.hnjz.common.plugins.PluginException;
import com.hnjz.common.plugins.config.model.ConfigNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:57
 */
public class ConfigUtil {
    private static final String conf_def = "/appconfig.properties";
    private static ConfigNode sysConfig = null;

    public ConfigUtil() {
    }

    public static ConfigNode loadSystemConfig() {
        if (sysConfig == null) {
            sysConfig = new ConfigNode();
            sysConfig.setName("plugin-config");
            loadConfig("/appconfig.properties", sysConfig);
        }

        return sysConfig;
    }

    public static void loadConfig(String path, ConfigNode config) {
        if (path != null && !path.trim().equals("") && config != null) {
            Properties prop = new Properties();
            InputStream is = ConfigUtil.class.getResourceAsStream(path);
            if (is != null) {
                try {
                    prop.load(is);
                    is.close();
                } catch (IOException var12) {
                    throw new PluginException("加载插件配置" + path + "失败!");
                } finally {
                    try {
                        is.close();
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }

                }

                Iterator var5 = prop.keySet().iterator();

                while(var5.hasNext()) {
                    Object key1 = var5.next();
                    ConfigNode attrNode = new ConfigNode();
                    attrNode.setName((String)key1);
                    attrNode.setValue((String)prop.get(key1));
                    config.addChild(attrNode);
                }
            }

        }
    }
}
