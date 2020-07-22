package com.hnjz.common.plugins.config;

import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.config.model.NamedPlugin;
import com.hnjz.common.plugins.config.model.PluginDescription;

public interface IPluginBuilder {
    NamedPlugin buildPlugin(PluginDescription var1, ClassLoader var2) throws PluginConfigException;

}
