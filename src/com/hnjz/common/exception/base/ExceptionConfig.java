package com.hnjz.common.exception.base;

import java.text.MessageFormat;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:07
 */
public class ExceptionConfig {
    private static final String BUNDLE_NAME = "messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    private ExceptionConfig() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException var2) {
            return null;
        }
    }

    public static String getString(String key, String[] paras) {
        try {
            String message = RESOURCE_BUNDLE.getString(key);
            return MessageFormat.format(message, paras);
        } catch (MissingResourceException var3) {
            return null;
        }
    }

    public static String getString(String key, List arg) {
        try {
            if (arg.isEmpty()) {
                return "";
            } else {
                String[] paras = new String[arg.size()];

                for(int i = 0; i < arg.size(); ++i) {
                    paras[i] = (String)arg.get(i);
                }

                return getString(key, paras);
            }
        } catch (MissingResourceException var4) {
            return null;
        }
    }
}
