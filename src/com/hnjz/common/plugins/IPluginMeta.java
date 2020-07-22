package com.hnjz.common.plugins;

import com.hnjz.common.plugins.config.model.PluginDescription;

public interface IPluginMeta {
    void setPluginDescription(PluginDescription var1);

    PluginDescription getPluginDescription();
}
