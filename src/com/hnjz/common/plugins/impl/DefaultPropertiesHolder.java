package com.hnjz.common.plugins.impl;

import com.hnjz.common.plugins.IConfigurable;
import com.hnjz.common.plugins.IPropertiesHolder;
import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.config.model.ConfigNode;
import com.hnjz.common.plugins.impl.AbstractPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:51
 */
public class DefaultPropertiesHolder extends AbstractPlugin implements IPropertiesHolder, IConfigurable {
    private String propertyFile = null;
    private Properties props = new Properties();

    public DefaultPropertiesHolder() {
    }

    public void configure(ConfigNode config) {
        this.propertyFile = config.getChildText("file");
        InputStream is = null;

        try {
            if (this.propertyFile != null && !this.propertyFile.trim().equals("")) {
                is = com.hnjz.common.plugins.impl.DefaultPropertiesHolder.class.getResourceAsStream(this.propertyFile);
                if (is == null) {
                    throw new PluginConfigException("应用属性配置文件" + this.propertyFile + "不存在!");
                }

                this.props.load(is);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }

    }

    public String getProperty(String prop) {
        if (prop == null) {
            return null;
        } else {
            String res = null;
            if (!this.props.isEmpty()) {
                try {
                    res = this.props.getProperty(prop);
                } catch (MissingResourceException var4) {
                    res = null;
                }
            }

            if (res == null) {
                res = System.getProperty(prop);
            }

            if (res == null) {
                res = System.getenv(prop);
            }

            return res;
        }
    }

    public String getProperty(String prop, String defValue) {
        String res = this.getProperty(prop);
        return res == null ? defValue : res;
    }

    public String translateVariables(String expression) {
        while(true) {
            int x = expression.indexOf("${");
            int y = expression.indexOf("}", x);
            if (x == -1 || y == -1) {
                return expression;
            }

            String var = expression.substring(x + 2, y);
            String o = this.getProperty(var);
            if (o != null) {
                expression = expression.substring(0, x) + o + expression.substring(y + 1);
            } else {
                expression = expression.substring(0, x) + expression.substring(y + 1);
            }
        }
    }
}
