package com.hnjz.common.plugins.config.model;

import com.hnjz.common.plugins.config.model.ConfigNode;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:59
 */
public class PluginDescription {
    public static final String PLUGIN_TYPE_LOCAL = "local";
    public static final String PLUGIN_TYPE_REMOTE = "remote";
    private String id;
    private String key;
    private String alias;
    private String implementClass;
    private boolean singleton = true;
    private String name;
    private String memo;
    private String provider;
    private com.hnjz.common.plugins.config.model.ConfigNode config;
    private String version;
    private int loadLevel = 5;
    private String type = "local";

    public PluginDescription() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String role) {
        this.id = role;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImplementClass() {
        return this.implementClass;
    }

    public void setImplementClass(String implementClass) {
        this.implementClass = implementClass;
    }

    public boolean isSingleton() {
        return this.singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public com.hnjz.common.plugins.config.model.ConfigNode getConfig() {
        return this.config;
    }

    public void setConfig(ConfigNode config) {
        this.config = config;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLoadLevel() {
        return this.loadLevel;
    }

    public void setLoadLevel(int loadLevel) {
        this.loadLevel = loadLevel;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
