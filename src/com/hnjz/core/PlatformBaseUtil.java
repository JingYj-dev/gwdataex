package com.hnjz.core;

import com.hnjz.common.PluginBus;
import com.hnjz.core.plugins.base.IDictProvider;
import com.hnjz.core.plugins.base.ILogProvider;
import com.hnjz.core.plugins.base.IOrgProvider;
import com.hnjz.core.plugins.base.IUserProdiver;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:34
 */
public class PlatformBaseUtil {
    private static ILogProvider logger = null;
    private static IDictProvider dictProvider = null;
    private static IUserProdiver userProvider = null;
    private static IOrgProvider orgProvider = null;

    public PlatformBaseUtil() {
    }

    public static ILogProvider getAppLogProvider() {
        if (logger == null) {
            logger = (ILogProvider) PluginBus.getPlugin(ILogProvider.class);
        }

        return logger;
    }

    public static IDictProvider getDictProvider() {
        if (dictProvider == null) {
            dictProvider = (IDictProvider)PluginBus.getPlugin(IDictProvider.class);
        }

        return dictProvider;
    }

    public static IUserProdiver getUserProvider() {
        if (userProvider == null) {
            userProvider = (IUserProdiver)PluginBus.getPlugin(IUserProdiver.class);
        }

        return userProvider;
    }

    public static IOrgProvider getOrgProvider() {
        if (orgProvider == null) {
            orgProvider = (IOrgProvider)PluginBus.getPlugin(IOrgProvider.class);
        }

        return orgProvider;
    }

    public static <T> T getBaseComponent(Class<T> plug) {
        return PluginBus.getPlugin(plug);
    }

    public static <T> T getBaseComponent(Class<T> plug, String key) {
        return PluginBus.getPlugin(plug, key);
    }
}
