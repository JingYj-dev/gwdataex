package com.hnjz.common.plugins.config;

import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.config.model.PluginDescription;

import java.io.InputStream;
import java.util.List;

public interface IConfigParser {
    List<PluginDescription> parseConfig(InputStream var1) throws PluginConfigException;

    List<PluginDescription> parseConfig(String var1) throws PluginConfigException;
}
