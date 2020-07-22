package com.hnjz.common.plugins.config.model;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:59
 */
public class NamedPlugin {
    private String[] pluginNames;
    private Object plugin;

    public NamedPlugin() {
    }

    public String[] getPluginNames() {
        return this.pluginNames;
    }

    public void setPluginNames(String[] pluginNames) {
        this.pluginNames = pluginNames;
    }

    public Object getPlugin() {
        return this.plugin;
    }

    public void setPlugin(Object plugin) {
        this.plugin = plugin;
    }
}
