package com.hnjz.util;

import com.hnjz.util.Json;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:30
 */
public class Ajax {
    public static String xmlHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data>";
    public static String wsHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    public static String xmlFoot = "</data>";

    public Ajax() {
    }

    public static String xmlResult(int code, String description) {
        return xmlResult(code, description, "");
    }

    public static String xmlResult(int code, String description, String content) {
        StringBuffer sb = new StringBuffer();
        sb.append(xmlHead);
        sb.append("<code>").append(code).append("</code>").append("<desc><![CDATA[").append(description).append("]]></desc>");
        sb.append(content);
        sb.append(xmlFoot);
        return sb.toString();
    }

    public static String JSONResult(int code, String description) {
        return JSONResult(code, description, (Object) null);
    }

    public static String JSONResult(int code, String description, Object info) {
        StringBuffer buf = new StringBuffer();
        buf.append("{\"result\":").append(code);
        if (description != null) {
            buf.append(",\"msg\":\"").append(com.hnjz.util.Json.string2json(description)).append("\"");
        }

        if (info != null) {
            if (info instanceof String) {
                buf.append(",\"info\":\"").append(com.hnjz.util.Json.string2json((String) info)).append("\"");
            } else {
                buf.append(",\"info\":").append(com.hnjz.util.Json.object2json(info));
            }
        }

        buf.append("}");
        return buf.toString();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String JSONResult(int code, String description, String info, boolean isJson) {
        StringBuffer buf = new StringBuffer();
        buf.append("{\"result\":").append(code);
        if (description != null) {
            buf.append(",\"msg\":\"").append(com.hnjz.util.Json.string2json(description)).append("\"");
        }

        if (info != null) {
            if (isJson) {
                buf.append(",\"info\":").append(info);
            } else {
                buf.append(",\"info\":\"").append(Json.string2json(info)).append("\"");
            }
        }

        buf.append("}");
        return buf.toString();
    }
}
