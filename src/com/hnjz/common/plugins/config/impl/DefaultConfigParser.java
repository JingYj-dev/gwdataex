package com.hnjz.common.plugins.config.impl;

import com.hnjz.common.plugins.config.IConfigParser;
import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.config.model.ConfigNode;
import com.hnjz.common.plugins.config.model.PluginDescription;
import com.hnjz.common.plugins.config.util.PluginConfigUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:01
 */
public class DefaultConfigParser implements IConfigParser {
    public DefaultConfigParser() {
    }

    public List<PluginDescription> parseConfig(InputStream is) {
        if (is == null) {
            return null;
        } else {
            LinkedList lst = new LinkedList();

            try {
                ConfigNode root = PluginConfigUtil.build(is);
                List<ConfigNode> children = root.getChildren();
                PluginDescription pd = null;
                Iterator var7 = children.iterator();

                while(var7.hasNext()) {
                    ConfigNode child = (ConfigNode)var7.next();
                    String name = child.getName();
                    if ("include".equals(name)) {
                        String file = child.getAttribute("file");
                        if (this.isEmpty(file)) {
                            throw new PluginConfigException("配置标签【include】缺少 file属性");
                        }

                        lst.addAll(this.parseConfig(file));
                    } else {
                        if (!"plugin".equals(name)) {
                            throw new PluginConfigException("配置标签 " + name + " 无效!");
                        }

                        pd = this.parseConfig(child, this.getClass().getClassLoader());
                        if (pd != null) {
                            lst.add(pd);
                        }
                    }
                }

                return lst;
            } catch (Exception var10) {
                if (var10 instanceof PluginConfigException) {
                    throw (PluginConfigException)var10;
                } else {
                    throw new PluginConfigException(var10);
                }
            }
        }
    }

    public List<PluginDescription> parseConfig(String url) {
        InputStream is = null;
        URL url0 = null;
        LinkedList lst = new LinkedList();

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }

            Enumeration<URL> enu = loader.getResources(url);
            boolean hasRes = false;

            while(enu.hasMoreElements()) {
                if (!hasRes) {
                    hasRes = true;
                }

                url0 = (URL)enu.nextElement();
                is = url0.openStream();
                lst.addAll(this.parseConfig(is));
                is.close();
            }

            is = null;
            if (!hasRes) {
                throw new PluginConfigException("配置文件" + url + "不存在");
            }
        } catch (Exception var15) {
            if (url0 != null) {
                throw new PluginConfigException("加载组件配置[" + url0 + "]失败!", var15);
            }

            throw new PluginConfigException("加载组件文件[" + url + "]失败!", var15);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return lst;
    }

    private PluginDescription parseConfig(ConfigNode conf, ClassLoader loader) {
        PluginDescription pluginDesc = new PluginDescription();
        String className = conf.getAttribute("class");
        if (this.isEmpty(className)) {
            throw new PluginConfigException("插件类" + className + "配置错误,缺少class属性");
        } else {
            //TODO
            className = className.trim();
            pluginDesc.setImplementClass(className);

            try {
                if (loader != null) {
                    loader.loadClass(className).newInstance();
                } else {
                    Class.forName(className).newInstance();
                }
            } catch (Exception var8) {
                throw new PluginConfigException("加载组件类 " + className + " 异常：", var8);
            }

            pluginDesc.setKey(conf.getAttribute("key"));
            String id = conf.getAttribute("id");
            if (this.isEmpty(id)) {
                id = className;
            }

            pluginDesc.setId(id.trim());
            pluginDesc.setAlias(conf.getAttribute("alias"));
            pluginDesc.setType(conf.getAttribute("type"));
            if ("false".equals(conf.getAttribute("singleton"))) {
                pluginDesc.setSingleton(false);
            }

            pluginDesc.setName(conf.getChildText("name"));
            pluginDesc.setConfig(conf.getChild("plugin-config"));
            pluginDesc.setMemo(conf.getChildText("memo"));
            pluginDesc.setVersion(conf.getChildText("version"));
            pluginDesc.setProvider(conf.getChildText("provider"));

            try {
                String level = conf.getChildText("load-level");
                if (!this.isEmpty(level)) {
                    pluginDesc.setLoadLevel(Integer.parseInt(level));
                }
            } catch (Exception var7) {
            }

            return pluginDesc;
        }
    }

    private boolean isEmpty(String src) {
        return src == null || src.trim().equals("");
    }
}
