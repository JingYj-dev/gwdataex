package com.hnjz.common.plugins.config.util;

import com.hnjz.common.dom4j.IgnoreDTDEntityResolver;
import com.hnjz.common.plugins.config.model.ConfigNode;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:58
 */
public class PluginConfigUtil {


    /**
     * dom4j解析dom
     *
     * @param element
     * @return
     */
    private static ConfigNode build(Element element) {
        ConfigNode configNode = new ConfigNode();
        configNode.setName(element.getName());
        configNode.setValue(element.getStringValue());
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            configNode.addAttribute(attribute.getName(), attribute.getValue());
        }
        List<Element> childrenElements = element.elements();
        for (Element childrenElement : childrenElements) {
            ConfigNode childrenConfigNode = build(childrenElement);
            configNode.addChild(childrenConfigNode);
        }
        return configNode;
    }


    public static ConfigNode build(String xml) throws Exception {
        SAXReader saxReader = new SAXReader(false);
        saxReader.setEntityResolver(new IgnoreDTDEntityResolver());
        Document document = saxReader.read(xml);
        Element root = document.getRootElement();
        return build(root);
    }

    public static ConfigNode build(InputStream xml) throws Exception {
        //InputStream解析时,忽略dtd
        SAXReader saxReader = new SAXReader(false);
        saxReader.setEntityResolver(new IgnoreDTDEntityResolver());
        Document document = saxReader.read(xml);
        Element root = document.getRootElement();
        return build(root);
    }

}
