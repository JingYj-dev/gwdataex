package com.hnjz.core.model.xml;

import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:42
 */
public class XmlNode {
    private String name;
    private String value;
    private Map<String, String> attributeMap = new HashMap();
    private List<com.hnjz.core.model.xml.XmlNode> children = new LinkedList();

    public XmlNode() {
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

    public List<com.hnjz.core.model.xml.XmlNode> getChildren() {
        return this.children;
    }

    public void addChildren(List<com.hnjz.core.model.xml.XmlNode> children) {
        this.children.addAll(children);
    }

    public void addChild(com.hnjz.core.model.xml.XmlNode node) {
        this.children.add(node);
    }

    public com.hnjz.core.model.xml.XmlNode[] getChildren(String name) {
        if (name != null && !name.trim().equals("")) {
            List<com.hnjz.core.model.xml.XmlNode> nodes = new LinkedList();
            Iterator var4 = this.children.iterator();

            while(var4.hasNext()) {
                com.hnjz.core.model.xml.XmlNode mode = (com.hnjz.core.model.xml.XmlNode)var4.next();
                if (name.trim().equals(mode.getName())) {
                    nodes.add(mode);
                }
            }

            return (com.hnjz.core.model.xml.XmlNode[])nodes.toArray(new com.hnjz.core.model.xml.XmlNode[0]);
        } else {
            return null;
        }
    }

    public String getChildText(String name) {
        if (name != null && !name.trim().equals("")) {
            com.hnjz.core.model.xml.XmlNode node = null;
            Iterator var4 = this.children.iterator();

            while(var4.hasNext()) {
                com.hnjz.core.model.xml.XmlNode mode = (com.hnjz.core.model.xml.XmlNode)var4.next();
                if (name.trim().equals(mode.getName())) {
                    node = mode;
                }
            }

            if (node == null) {
                return null;
            } else {
                return node.getValue();
            }
        } else {
            return null;
        }
    }
}
