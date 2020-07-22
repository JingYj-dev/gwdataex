package com.hnjz.webbase.webwork.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HtmlTagParse {
    private static final Log log = LogFactory.getLog(com.hnjz.webbase.webwork.interceptors.HtmlTagParse.class);
    private static com.hnjz.webbase.webwork.interceptors.HtmlTagParse instance;
    private static Map fieldNameMap = new HashMap();

    public HtmlTagParse() {
    }

    public static synchronized com.hnjz.webbase.webwork.interceptors.HtmlTagParse getInstance() {
        try {
            if (instance != null) {
                return instance;
            }

            instance = new com.hnjz.webbase.webwork.interceptors.HtmlTagParse();
            readXmlFile("/htmltag.xml");
        } catch (Exception var1) {
            instance = null;
            log.error("HtmlTagParse::getInstance:" + var1.getMessage(), var1);
        }

        return instance;
    }

    protected static void readXmlFile(String fileName) throws Exception {
        fieldNameMap.clear();
        InputStream cfgIn = getConfigurationInputStream(fileName);
        if (cfgIn != null) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = dbf.newDocumentBuilder();
                Document doc = docBuilder.parse(cfgIn);
                NodeList iter = doc.getDocumentElement().getElementsByTagName("field");
                int i = 0;

                for(int n = iter.getLength(); i < n; ++i) {
                    Element ele = (Element)iter.item(i);
                    String fieldName = ele.getAttribute("name");
                    fieldNameMap.put(fieldName, fieldName);
                }
            } finally {
                if (cfgIn != null) {
                    cfgIn.close();
                }

            }

        }
    }

    public Map getFieldNameMap() {
        return fieldNameMap;
    }

    protected static InputStream getConfigurationInputStream(String resource) {
        log.debug("Configuration resource: " + resource);
        InputStream stream = com.hnjz.webbase.webwork.interceptors.HtmlTagParse.class.getResourceAsStream(resource);
        if (stream == null) {
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        }

        return stream;
    }
}
