package com.hnjz.common.plugins.impl;

import com.hnjz.common.plugins.IPlugin;
import com.hnjz.common.plugins.IPluginMeta;
import com.hnjz.common.plugins.config.model.PluginDescription;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:49
 */
public abstract class AbstractPlugin implements IPlugin, IPluginMeta {
    private String pluginId;
    private PluginDescription pluginDescription;

    public AbstractPlugin() {
    }

    public String getPluginId() {
        return this.pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public PluginDescription getPluginDescription() {
        return this.pluginDescription;
    }

    public void setPluginDescription(PluginDescription pluginDescription) {
        this.pluginDescription = pluginDescription;
    }
}
