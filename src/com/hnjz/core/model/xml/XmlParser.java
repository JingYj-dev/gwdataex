package com.hnjz.core.model.xml;

import com.hnjz.core.model.xml.XmlNode;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:42
 */
public class XmlParser {
    public XmlParser() {
    }

    public static com.hnjz.core.model.xml.XmlNode parse(String xml) throws Exception {
        new com.hnjz.core.model.xml.XmlNode();
        FileInputStream is = null;

        com.hnjz.core.model.xml.XmlNode node;
        try {
            is = new FileInputStream(xml);
            node = parse((InputStream)is);
        } finally {
            if (is != null) {
                is.close();
            }

        }

        return node;
    }

    public static com.hnjz.core.model.xml.XmlNode parse(InputStream xml) throws Exception {
        new com.hnjz.core.model.xml.XmlNode();
        DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder dombuilder = domfac.newDocumentBuilder();
        Document doc = dombuilder.parse(xml);
        com.hnjz.core.model.xml.XmlNode node = transpass(doc.getDocumentElement());
        return node;
    }

    private static com.hnjz.core.model.xml.XmlNode transpass(Element e) {
        com.hnjz.core.model.xml.XmlNode node = new XmlNode();
        node.setName(e.getNodeName());
        node.setValue(e.getTextContent());
        NamedNodeMap map = e.getAttributes();
        int n;
        if (map != null) {
            n = map.getLength();

            for(n = 0; n < n; ++n) {
                node.addAttribute(map.item(n).getNodeName(), map.item(n).getNodeValue());
            }
        }

        NodeList lst = e.getChildNodes();
        if (lst != null) {
            n = lst.getLength();

            for(int i = 0; i < n; ++i) {
                Node nd = lst.item(i);
                if (nd instanceof Element) {
                    node.addChild(transpass((Element)nd));
                }
            }
        }

        return node;
    }
}
