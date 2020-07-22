package com.hnjz.util;

import com.hnjz.util.StringHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:17
 */
public class RegexCheck {
    public RegexCheck() {
    }

    public static String imgCheck(String sSource) {
        Pattern p = null;
        Matcher m = null;
        String s = null;
        String sTmp = null;
        StringBuffer sb = null;
        p = Pattern.compile("((<|</)(form|script)|^\\son)", 2);
        m = p.matcher(sSource);

        for(sb = new StringBuffer(); m.find(); m.appendReplacement(sb, s)) {
            sTmp = m.group().toLowerCase();
            if (sTmp.substring(0, 1).equals("<")) {
                s = "&lt;" + m.group().substring(1);
            } else {
                s = " &#111n";
            }
        }

        m.appendTail(sb);
        return sb.toString();
    }

    public static String TagReverse(String sSource) {
        if (com.hnjz.util.StringHelper.isEmpty(sSource)) {
            return "";
        } else {
            Pattern p = null;
            Matcher m = null;
            String s = null;
            String sTmp = null;
            StringBuffer sb = null;
            p = Pattern.compile("(<br/>|\\&lt;|\\&gt;)", 2);
            m = p.matcher(sSource);

            for(sb = new StringBuffer(); m.find(); m.appendReplacement(sb, s)) {
                sTmp = m.group().toLowerCase();
                if (sTmp.equals("<br/>")) {
                    s = "\n";
                } else if (sTmp.equals("&lt;")) {
                    s = "<";
                } else if (sTmp.equals("&gt;")) {
                    s = ">";
                }
            }

            m.appendTail(sb);
            return sb.toString();
        }
    }

    public static String TagReplace(String sSource) {
        if (StringHelper.isEmpty(sSource)) {
            return "";
        } else {
            Pattern p = null;
            Matcher m = null;
            String s = null;
            String sTmp = null;
            StringBuffer sb = null;
            p = Pattern.compile("(\\n|\\r|<|>)", 2);
            m = p.matcher(sSource);

            for(sb = new StringBuffer(); m.find(); m.appendReplacement(sb, s)) {
                sTmp = m.group().toLowerCase();
                if (sTmp.equals("\n")) {
                    s = "<br/>";
                } else if (sTmp.equals("\r")) {
                    s = "";
                } else if (sTmp.equals("<")) {
                    s = "&lt;";
                } else if (sTmp.equals(">")) {
                    s = "&gt;";
                }
            }

            m.appendTail(sb);
            return sb.toString();
        }
    }
}
