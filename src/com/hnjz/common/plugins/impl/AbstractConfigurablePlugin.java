package com.hnjz.common.plugins.impl;

import com.hnjz.common.plugins.IConfigurable;
import com.hnjz.common.plugins.IPlugable;
import com.hnjz.common.plugins.IPluginFactory;
import com.hnjz.common.plugins.IPropertiesHolder;
import com.hnjz.common.plugins.config.model.ConfigNode;
import com.hnjz.common.plugins.impl.AbstractPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:47
 */
public abstract class AbstractConfigurablePlugin extends AbstractPlugin implements IConfigurable, IPlugable {
    private IPluginFactory _fac = null;

    public AbstractConfigurablePlugin() {
    }

    public void plug(IPluginFactory fac) {
        this._fac = fac;
    }

    public void configure(ConfigNode config) {
        if (config != null) {
            Map<String, String> paramsMap = new HashMap();
            Iterator var4 = config.getChildren().iterator();

            while(var4.hasNext()) {
                ConfigNode cn = (ConfigNode)var4.next();
                this.transform(paramsMap, cn, "");
            }

            this.doConfig(paramsMap);
        }
    }

    private void transform(Map<String, String> paramsMap, ConfigNode cnf, String parentPath) {
        if (cnf != null) {
            IPropertiesHolder holder = (IPropertiesHolder)this._fac.lookup(IPropertiesHolder.class);
            String value = cnf.getValue();
            String nodePath = parentPath != null && !parentPath.trim().equals("") ? parentPath + "." + cnf.getName() : cnf.getName();
            if (value != null && !value.trim().equals("")) {
                if (holder != null) {
                    value = holder.translateVariables(value);
                }

                paramsMap.put(nodePath, value);
            }

            Iterator var8;
            String key;
            String attrValue;
            for(var8 = cnf.getAttributeMap().entrySet().iterator(); var8.hasNext(); paramsMap.put(nodePath + ".@" + key, attrValue)) {
                Map.Entry<String, String> attrEntry = (Map.Entry)var8.next();
                key = (String)attrEntry.getKey();
                attrValue = (String)attrEntry.getValue();
                if (holder != null) {
                    attrValue = holder.translateVariables(attrValue);
                }
            }

            var8 = cnf.getChildren().iterator();

            while(var8.hasNext()) {
                ConfigNode cc = (ConfigNode)var8.next();
                this.transform(paramsMap, cc, nodePath);
            }

        }
    }

    protected abstract void doConfig(Map<String, String> var1);
}
