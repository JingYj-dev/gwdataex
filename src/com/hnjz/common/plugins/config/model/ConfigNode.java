package com.hnjz.common.plugins.config.model;

import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:58
 */
public class ConfigNode {
    public static final String NODE_INCLUDE = "include";
    public static final String NODE_PLUGIN = "plugin";
    public static final String NODE_CONFIG = "plugin-config";
    private String name;
    private String value;
    private Map<String, String> attributeMap = new HashMap();
    private List<ConfigNode> children = new LinkedList();

    public ConfigNode() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getAttributeMap() {
        return this.attributeMap;
    }

    public String getAttribute(String name) {
        return (String)this.attributeMap.get(name);
    }

    public void addAttributeMap(Map<String, String> attributeMap) {
        this.attributeMap.putAll(attributeMap);
    }

    public void addAttribute(String name, String value) {
        this.attributeMap.put(name, value);
    }

    public List<ConfigNode> getChildren() {
        return this.children;
    }

    public void addChildren(List<ConfigNode> children) {
        this.children.addAll(children);
    }

    public void addChild(ConfigNode node) {
        this.children.add(node);
    }

    public ConfigNode[] getChildren(String name) {
        if (name != null && !name.trim().equals("")) {
            List<ConfigNode> nodes = new LinkedList();
            Iterator var4 = this.children.iterator();

            while(var4.hasNext()) {
                ConfigNode mode = (ConfigNode)var4.next();
                if (name.trim().equals(mode.getName())) {
                    nodes.add(mode);
                }
            }

            return (ConfigNode[])nodes.toArray(new ConfigNode[0]);
        } else {
            return null;
        }
    }

    public ConfigNode getChild(String name) {
        if (name != null && !name.trim().equals("")) {
            Iterator var3 = this.children.iterator();

            while(var3.hasNext()) {
                ConfigNode mode = (ConfigNode)var3.next();
                if (name.trim().equals(mode.getName())) {
                    return mode;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public String getChildText(String name) {
        if (name != null && !name.trim().equals("")) {
            ConfigNode node = this.getChild(name);
            return node == null ? null : node.getValue();
        } else {
            return null;
        }
    }
}
