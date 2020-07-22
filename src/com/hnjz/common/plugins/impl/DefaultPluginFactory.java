package com.hnjz.common.plugins.impl;

import com.hnjz.common.plugins.*;
import com.hnjz.common.plugins.config.IConfigParser;
import com.hnjz.common.plugins.config.IPluginBuilder;
import com.hnjz.common.plugins.config.impl.DefaultConfigParser;
import com.hnjz.common.plugins.config.impl.DefaultPluginBuilder;
import com.hnjz.common.plugins.config.model.ConfigNode;
import com.hnjz.common.plugins.config.model.NamedPlugin;
import com.hnjz.common.plugins.config.model.PluginDescription;
import com.hnjz.common.plugins.config.util.ConfigUtil;

import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:49
 */
public class DefaultPluginFactory implements IPluginFactory {
    private static final String conf = "META-INF/hnjz-plugins.xml";
    private List<PluginDescription> pluginDes;
    private Map<String, PluginDescription> pluginDesMap;
    private Map<String, Object> plugins;
    private IPluginBuilder builder;
    private IConfigParser parser;

    public DefaultPluginFactory(IConfigParser parser0, IPluginBuilder builder0) {
        this.pluginDes = null;
        this.pluginDesMap = new HashMap();
        this.plugins = new HashMap();
        this.builder = null;
        this.parser = null;
        if (builder0 != null) {
            this.builder = builder0;
        } else {
            this.builder = new DefaultPluginBuilder();
        }

        if (parser0 != null) {
            this.parser = parser0;
        } else {
            this.parser = new DefaultConfigParser();
        }

    }

    public DefaultPluginFactory() {
        this(new DefaultConfigParser(), new DefaultPluginBuilder());
    }

    protected ClassLoader getPluginCloassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = this.getClass().getClassLoader();
        }

        return loader;
    }

    public void initialize() {
        if (this.builder == null) {
            throw new PluginException("未设置插件构建组件!");
        } else if (this.parser == null) {
            throw new PluginException("未设置插件配置解析组件!");
        } else {
            //TODO
            this.pluginDes = this.parser.parseConfig(conf);
            if (this.pluginDes == null) {
                this.pluginDes = new ArrayList();
            }
            Collections.sort(this.pluginDes, new Comparator<PluginDescription>() {
                public int compare(PluginDescription o1, PluginDescription o2) {
                    return o1.getLoadLevel() - o2.getLoadLevel();
                }
            });
            ClassLoader loader = this.getPluginCloassLoader();
            Iterator var3 = this.pluginDes.iterator();

            while(true) {
                PluginDescription pd;
                NamedPlugin np;
                String[] names;
                int var8;
                do {
                    do {
                        if (!var3.hasNext()) {
                            return;
                        }

                        pd = (PluginDescription)var3.next();
                    } while(pd == null);

                    np = this.builder.buildPlugin(pd, loader);
                    names = np.getPluginNames();
                    String[] var9 = names;
                    var8 = names.length;

                    for(int var7 = 0; var7 < var8; ++var7) {
                        String name = var9[var7];
                        this.pluginDesMap.put(name, pd);
                    }
                } while(!pd.isSingleton());

                Object plugin = np.getPlugin();
                if (plugin != null) {
                    this.handle(plugin, pd);
                }

                String[] var10 = names;
                int var13 = names.length;

                for(var8 = 0; var8 < var13; ++var8) {
                    String name = var10[var8];
                    if (!this.plugins.containsKey(name)) {
                        this.plugins.put(name, plugin);
                    }
                }
            }
        }
    }

    private void handle(Object plugin, PluginDescription pd) {
        if (IPluginMeta.class.isAssignableFrom(plugin.getClass())) {
            ((IPluginMeta)plugin).setPluginDescription(pd);
        }

        if (IPlugable.class.isAssignableFrom(plugin.getClass())) {
            ((IPlugable)plugin).plug(this);
        }

        if (IConfigurable.class.isAssignableFrom(plugin.getClass())) {
            this.configPlugin(pd.getConfig(), (IConfigurable)plugin);
        }

        if (Initializable.class.isAssignableFrom(plugin.getClass())) {
            ((Initializable)plugin).initialize();
        }

    }

    private void configPlugin(ConfigNode configNode, IConfigurable confg) {
        if (configNode != null && (!configNode.getChildren().isEmpty() || !this.isEmpty(configNode.getAttribute("file")))) {
            if (configNode.getChildren().isEmpty()) {
                String file = configNode.getAttribute("file");
                ConfigUtil.loadConfig(file, configNode);
            }
        } else {
            configNode = ConfigUtil.loadSystemConfig();
        }

        confg.configure(configNode);
    }

    private boolean isEmpty(String src) {
        return src == null || src.trim().equals("");
    }

    public Object lookup(String id) {
        Object plugin = this.plugins.get(id);
        if (plugin == null) {
            PluginDescription pd = (PluginDescription)this.pluginDesMap.get(id);
            if (pd != null) {
                NamedPlugin np = this.builder.buildPlugin(pd, this.getPluginCloassLoader());
                plugin = np.getPlugin();
                if (plugin != null) {
                    this.handle(plugin, pd);
                }
            }
        }

        return plugin;
    }

    public Object lookup(String id, String key) {
        if (this.isEmpty(id)) {
            return null;
        } else {
            if (!this.isEmpty(key)) {
                id = id + "/" + key;
            }

            return this.lookup(id);
        }
    }

    public <T> T lookup(Class<T> t) {
        return (T) this.lookup(t.getName());
    }

    public <T> T lookup(Class<T> t, String key) {
        return (T) this.lookup(t.getName(), key);
    }

    public Collection<Object> getPlugins() {
        return this.plugins.values();
    }

    public void destroy() {
        Iterator var2 = this.plugins.values().iterator();

        while(var2.hasNext()) {
            Object plugin = var2.next();
            if (IDisposable.class.isAssignableFrom(plugin.getClass())) {
                ((IDisposable)plugin).dispose();
            }
        }

        this.plugins.clear();
        this.pluginDesMap.clear();
        if (this.pluginDes != null) {
            this.pluginDes.clear();
            this.pluginDes = null;
        }

    }
}
