package com.hnjz.common.plugins.config.impl;

import com.hnjz.common.plugins.config.IPluginBuilder;
import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.config.model.NamedPlugin;
import com.hnjz.common.plugins.config.model.PluginDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:00
 */
public abstract class AbstractPluginBuilder implements IPluginBuilder {
    public AbstractPluginBuilder() {
    }

    public NamedPlugin buildPlugin(PluginDescription config, ClassLoader loader) throws PluginConfigException {
        String id = config.getId();
        if (this.isEmpty(id)) {
            throw new PluginConfigException("插件id属性不能为空,且必须为接口类名!");
        } else {
            if (loader == null) {
                loader = Thread.currentThread().getContextClassLoader();
            }

            Object plugin = this.doBuildPlugin(config, loader);
            String[] pluginNames = this.buildPluginNames(id, config.getKey(), config.getAlias());
            NamedPlugin np = new NamedPlugin();
            np.setPlugin(plugin);
            np.setPluginNames(pluginNames);
            return np;
        }
    }

    protected abstract Object doBuildPlugin(PluginDescription var1, ClassLoader var2);

    protected boolean isEmpty(String src) {
        return src == null || src.trim().equals("");
    }

    private String[] buildPluginNames(String id, String key, String alias) {
        List<String> pluginNames = new ArrayList();
        String ukey = this.isEmpty(key) ? id : id + "/" + key;
        pluginNames.add(ukey);
        if (!this.isEmpty(alias)) {
            String[] var9;
            int var8 = (var9 = alias.trim().split(",")).length;

            for (int var7 = 0; var7 < var8; ++var7) {
                String aliasName = var9[var7];
                String plugName = id + "/" + aliasName.trim();
                if (!pluginNames.contains(plugName)) {
                    pluginNames.add(plugName);
                }
            }
        }

        return (String[]) pluginNames.toArray(new String[0]);
    }
}
