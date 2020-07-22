package com.hnjz.common.plugins;

import com.hnjz.common.plugins.config.model.ConfigNode;

public interface IConfigurable {
    String ROLE = com.hnjz.common.plugins.IConfigurable.class.getName();

    void configure(ConfigNode var1);
}
