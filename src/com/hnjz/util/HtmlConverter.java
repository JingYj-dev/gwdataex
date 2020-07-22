package com.hnjz.util;

import com.hnjz.util.StringHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:22
 */
public class HtmlConverter {
    public HtmlConverter() {
    }

    public static String appendHtml(String title, Object value) {
        String str = "";
        if (value != null && com.hnjz.util.StringHelper.isNotEmpty(value.toString())) {
            str = "<span class=log_title>" + title + "：</span>" + value + "<br/>";
        }

        return str;
    }

    public static String appendHtmlNameField(String title, Object value) {
        String str = "";
        if (value != null && com.hnjz.util.StringHelper.isNotEmpty(value.toString())) {
            str = "<span class=\"log_title\">" + title + "：</span>" + "<span class=\"log_value\">" + value + "</span><br/>";
        }

        return str;
    }

    public static String append(Object o, String name) {
        return append(o, name, false);
    }

    public static String append(Object o, String name, boolean html) {
        if (o != null) {
            String str = o.toString();
            if (StringHelper.isEmpty(str)) {
                return "";
            } else {
                if (html) {
                    str = "<![CDATA[" + str + "]]>";
                }

                return "<" + name + ">" + str + "</" + name + ">";
            }
        } else {
            return "";
        }
    }

    public static String appendDate(Object o, String name) {
        if (o != null) {
            String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.ENGLISH)).format(o);
            return "<" + name + ">" + str + "</" + name + ">";
        } else {
            return "";
        }
    }
}
