package com.hnjz.common.plugins.config.impl;

import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.config.impl.AbstractPluginBuilder;
import com.hnjz.common.plugins.config.model.PluginDescription;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:02
 */
public class DefaultPluginBuilder extends AbstractPluginBuilder {
    public DefaultPluginBuilder() {
    }

    protected Object doBuildPlugin(PluginDescription config, ClassLoader loader) {
        String clazz = config.getImplementClass();
        if (this.isEmpty(clazz)) {
            throw new PluginConfigException("构建插件异常,缺少class属性");
        } else {
            String id = config.getId();
            if (this.isEmpty(id)) {
                id = clazz;
            }

            return this.buildPlugin(id, clazz, loader);
        }
    }

    private Object buildPlugin(String id, String clazz, ClassLoader loader) {
        Object plugin0 = null;

        try {
            if (loader != null) {
                plugin0 = loader.loadClass(clazz.trim()).newInstance();
            } else {
                plugin0 = Class.forName(clazz.trim()).newInstance();
            }

            return plugin0;
        } catch (Exception var6) {
            throw new PluginConfigException("构建插件" + id + "," + clazz + "失败：", var6);
        }
    }
}
