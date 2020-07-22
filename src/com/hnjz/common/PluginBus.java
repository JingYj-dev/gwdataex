package com.hnjz.common;

import com.hnjz.common.plugins.IPluginFactory;
import com.hnjz.common.plugins.PluginException;
import com.hnjz.common.plugins.config.IConfigParser;
import com.hnjz.common.plugins.config.IPluginBuilder;
import com.hnjz.common.plugins.impl.DefaultPluginFactory;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:38
 */
public class PluginBus {
    private static IPluginFactory fac = null;
    private static boolean _hasInitialized = false;
    private static Object lock = new byte[0];

    public PluginBus() {
    }

    public static void init(IConfigParser iConfigParser, IPluginBuilder iPluginBuilder) {
        synchronized(lock) {
            if (!_hasInitialized) {
                fac = new DefaultPluginFactory(iConfigParser, iPluginBuilder);
                fac.initialize();
                _hasInitialized = true;
            } else {
                throw new PluginException("组件工厂已初始化...");
            }
        }
    }

    public static void init() {
        init(null, null);
    }

    public static <T> T getPlugin(Class<T> clz, String key) {
        if (fac == null) {
            init();
        }

        return fac == null ? null : fac.lookup(clz, key);
    }

    public static <T> T getPlugin(Class<T> clz) {
        return (T) getPlugin((Class)clz, (String)null);
    }

    public static Object getPlugin(String clz, String key) {
        if (fac == null) {
            init();
        }

        return fac == null ? null : fac.lookup(clz, key);
    }

    public static void dispose() {
        if (fac != null) {
            fac.destroy();
            fac = null;
        }
    }
}
