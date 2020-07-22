package com.hnjz.db.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:39
 */
public class StringHelper {
    private static final int ALIAS_TRUNCATE_LENGTH = 10;
    private static final String operators = "><=(),\"\\{\\}";
    private static final String[] replaceFalg = new String[]{"{", "}"};
    private static final String regex1 = "[\"].*?[^\\\\]\"";
    private static final String regex2 = "\\\\n|\\\\t|\\\\r";
    private static final String regex3 = "\\s+";
    private static final String regex4 = "(\\s[><=(),\"\\{\\}]+\\s)|(\\s[><=(),\"\\{\\}]+)|([><=(),\"\\{\\}]+\\s)";
    private static final String regex5;
    private static final String regex6 = "(?!([#\\s]{2,}))#.+?#";
    private static final String regex7 = "(?!(@{2,}))@[\\u4e00-\\u9fa5A-Za-z0-9_]+?([\\s@]|\\b)";

    static {
        regex5 = "\\" + replaceFalg[0] + ".*?" + "\\" + replaceFalg[1];
    }

    private StringHelper() {
    }

    public static String trimSentence(String str) {
        List<String> replaced = new ArrayList();
        Pattern p = Pattern.compile("[\"].*?[^\\\\]\"", 2);
        Matcher m = p.matcher(str);
        int i = 0;

        String replaceStr1;
        for(replaceStr1 = str; m.find(); ++i) {
            replaced.add(i, m.group());
            replaceStr1 = m.replaceFirst(replaceFalg[0] + i + replaceFalg[1]);
        }

        replaceStr1 = replaceStr1.replaceAll("\\\\n|\\\\t|\\\\r", " ");
        replaceStr1 = replaceStr1.replaceAll("\\s+", " ");
        p = Pattern.compile("(\\s[><=(),\"\\{\\}]+\\s)|(\\s[><=(),\"\\{\\}]+)|([><=(),\"\\{\\}]+\\s)", 2);
        m = p.matcher(replaceStr1);

        while(m.find()) {
            if (m.group() != null) {
                replaceStr1 = replaceStr1.replace(m.group(), m.group().trim());
            }
        }

        return MessageFormat.format(replaceStr1, replaced.toArray());
    }

    public static String replaceString(String str, List<String> replaced) {
        Pattern p = Pattern.compile("(?!([#\\s]{2,}))#.+?#");
        Matcher m = p.matcher(str);
        int i = 0;

        StringBuffer replaceStr1;
        for(replaceStr1 = new StringBuffer(); m.find(); ++i) {
            replaced.add(i, m.group().trim());
            m.appendReplacement(replaceStr1, replaceFalg[0] + i + replaceFalg[1]);
        }

        return replaceStr1.toString();
    }

    public static String checkString(String str) {
        Pattern pattern = Pattern.compile("(?!(@{2,}))@[\\u4e00-\\u9fa5A-Za-z0-9_]+?([\\s@]|\\b)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sbr = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(sbr, "Java");
        }

        matcher.appendTail(sbr);
        return "";
    }

    public static String formatArticle(String s) {
        if (isEmpty(s)) {
            return "";
        } else {
            s = "<br/>" + s + "<br/>";
            s = s.replaceAll("\t", " ");
            //s = s.replaceAll("??", "");
            s = s.replace("??", "");
            s = s.replaceAll("  ", "??");
           // s = s.replaceAll("??", "");
            s = s.replace("??", "");
            s = s.replaceAll("<br/> ", "<br/>");
            boolean var1 = true;

            while(true) {
                int s_index = s.indexOf("<br/><br/>");
                if (s_index < 0) {
                    s = s.replaceAll("<br/>", "<br/><br/>????");
                    return s.substring("<br/><br/>".length(), s.length() - "<br/><br/>????".length());
                }

                s = s.replaceAll("<br/><br/>", "<br/>");
            }
        }
    }

    public static char getChar(String a) {
        char ch = a.charAt(0);
        return (char)(65 + ch - 49);
    }

    public static String getSubString(String sOurce, int len) {
        if (isEmpty(sOurce)) {
            return "";
        } else {
            return sOurce.length() <= len ? sOurce : sOurce.substring(0, len);
        }
    }

    public static String getAnwStr(String str) {
        String strTmp = "";
        String[] arr = str.toUpperCase().split(",");
        if (arr == null) {
            return "";
        } else {
            for(int i = 0; i < arr.length; ++i) {
                if (isNotEmpty(arr[i])) {
                    char ch = getChar(arr[i]);
                    strTmp = strTmp + String.valueOf(ch) + ",";
                }
            }

            strTmp = strTmp.substring(0, strTmp.length() - 1);
            return strTmp;
        }
    }

    public static String jiequ(String str, String ch, int n) {
        try {
            String tmp = "";

            for(int i = 0; i < n; ++i) {
                int index = str.indexOf(ch);
                tmp = tmp + str.substring(0, index + 1);
                str = str.substring(index + 1);
            }

            tmp = tmp.substring(0, tmp.length() - 1);
            return tmp;
        } catch (Exception var6) {
            return "";
        }
    }

    public static String digitToString(String a) {
        String[] digit = new String[]{"??", "?", "??", "??", "??", "??", "?", "??", "??", "??"};
        String[] weight = new String[]{"", "", "?", "??", "?", "??", "?", "??", "?", "??", "?", "??", "?"};
        String jg = new String();
        new String();
        String xs = new String();
        int dot = a.indexOf(".");
        String zs;
        if (dot > 0) {
            zs = a.substring(0, dot);
            xs = a.substring(dot + 1);
        } else {
            zs = a.substring(0);
        }

        boolean flag = false;

        int i;
        int w;
        for(i = 0; i < zs.length(); ++i) {
            w = Integer.parseInt(zs.substring(i, i + 1));
            if (w == 0) {
                if (zs.length() - i == 5) {
                    jg = jg + "??";
                }

                if (zs.length() - i == 9) {
                    jg = jg + "??";
                }

                flag = true;
            } else {
                if (flag) {
                    jg = jg + "??";
                    flag = false;
                }

                jg = jg + digit[w];
                jg = jg + weight[zs.length() - i];
            }
        }

        if (dot > 0) {
            jg = jg + "??";

            for(i = 0; i < xs.length(); ++i) {
                w = Integer.parseInt(xs.substring(i, i + 1));
                jg = jg + digit[w];
            }
        }

        return jg;
    }

    public static boolean findStr(String sSource, String sFind) {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("(" + sFind + ")", 2);
        m = p.matcher(sSource);
        return m.find();
    }

    public static String getCharStr(int i) {
        char ch = (char)(65 + i);
        return String.valueOf(ch);
    }

    public static String getKeyword(String sOurce, int len, String highlightWord) {
        String keyword = getByteString(sOurce, len);
        if (isNotEmpty(highlightWord)) {
            keyword = replaceString(keyword, highlightWord, "<span class=highlightCls>" + highlightWord + "</span>");
        }

        return keyword;
    }

    public static String getByteString(String sOurce, int len) {
        try {
            byte[] hanzi = sOurce.getBytes("gb2312");
            if (hanzi.length <= len) {
                return sOurce;
            } else {
                int lenTmp = 0;

                int i;
                for(i = 0; i < 2 * len; ++i) {
                    String tmp = sOurce.substring(i, i + 1);
                    lenTmp += tmp.getBytes("gb2312").length;
                    if (lenTmp > len) {
                        break;
                    }
                }

                return sOurce.substring(0, i) + "...";
            }
        } catch (Exception var6) {
            return "";
        }
    }

    public static int getByteLen(String sOurce) {
        try {
            byte[] hanzi = sOurce.getBytes("gb2312");
            return hanzi.length;
        } catch (Exception var2) {
            return 0;
        }
    }

    public static int getContByteLen(String sOurce) {
        sOurce = sOurce.replaceAll("<br/>", "a");
        int length = sOurce.replaceAll("<br/>", "a").length();

        try {
            Pattern pattern = Pattern.compile("[一-龥]+");

            for(Matcher matcher = pattern.matcher(sOurce); matcher.find(); length += matcher.group().length()) {
            }
        } catch (Exception var4) {
        }

        return length;
    }

    public static List formatText(String s) {
        if (isEmpty(s)) {
            return null;
        } else {
            s = s.replaceAll("\t", " ");
            s = s.replaceAll("\r\n", "\n");
            //s = s.replaceAll("??", " ");
            s = s.replace("??", " ");

            do {
                s = s.replaceAll("  ", " ");
            } while(s.indexOf("  ") >= 0);

            String[] str = s.split("\n");
            List lst = new ArrayList();

            for(int i = 0; i < str.length; ++i) {
                String tmp = str[i].trim();
                if (isNotEmpty(tmp)) {
                    lst.add(str[i]);
                }
            }

            return lst;
        }
    }

    public static List chopWordSub(String s, int len) {
        ArrayList resLst = new ArrayList();

        while(true) {
            String tmp = getByteString(s, len);
            resLst.add(tmp);
            if (tmp.equals(s)) {
                return resLst;
            }

            s = s.substring(tmp.length());
        }
    }

    public static List chopWord(String s, int len) {
        try {
            List lst = formatText(s);
            if (lst != null && lst.size() >= 1) {
                List resLst = new ArrayList();
                int i = 0;
                String tmp = "";
                String str = "";
                i = i + 1;
                tmp = (String)lst.get(i);

                while(true) {
                    if (getByteLen(str + "\n" + tmp) <= len) {
                        str = str + (isEmpty(str) ? tmp : "\n" + tmp);
                        if (i >= lst.size()) {
                            resLst.add(str);
                            break;
                        }

                        tmp = (String)lst.get(i++);
                    } else if (isEmpty(str)) {
                        resLst.addAll(chopWordSub(tmp, len));
                        if (i >= lst.size()) {
                            break;
                        }

                        tmp = (String)lst.get(i++);
                    } else {
                        resLst.add(str);
                        str = "";
                    }
                }

                return resLst;
            } else {
                return null;
            }
        } catch (Exception var7) {
            return null;
        }
    }

    public static String join(String seperator, String[] strings) {
        int length = strings.length;
        if (length == 0) {
            return "";
        } else {
            StringBuffer buf = (new StringBuffer(length * strings[0].length())).append(strings[0]);

            for(int i = 1; i < length; ++i) {
                buf.append(seperator).append(strings[i]);
            }

            return buf.toString();
        }
    }

    public static String join(String seperator, Iterator objects) {
        StringBuffer buf = new StringBuffer();
        if (objects.hasNext()) {
            buf.append(objects.next());
        }

        while(objects.hasNext()) {
            buf.append(seperator).append(objects.next());
        }

        return buf.toString();
    }

    public static String[] add(String[] x, String sep, String[] y) {
        String[] result = new String[x.length];

        for(int i = 0; i < x.length; ++i) {
            result[i] = x[i] + sep + y[i];
        }

        return result;
    }

    public static String repeat(String string, int times) {
        StringBuffer buf = new StringBuffer(string.length() * times);

        for(int i = 0; i < times; ++i) {
            buf.append(string);
        }

        return buf.toString();
    }

    public static String replace(String template, String placeholder, String replacement) {
        return replace(template, placeholder, replacement, false);
    }

    public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
        int loc = template.indexOf(placeholder);
        if (loc < 0) {
            return template;
        } else {
            boolean actuallyReplace = !wholeWords || loc + placeholder.length() == template.length() || !Character.isJavaIdentifierPart(template.charAt(loc + placeholder.length()));
            String actualReplacement = actuallyReplace ? replacement : placeholder;
            return template.substring(0, loc) + actualReplacement + replace(template.substring(loc + placeholder.length()), placeholder, replacement, wholeWords);
        }
    }

    public static String replaceOnce(String template, String placeholder, String replacement) {
        int loc = template.indexOf(placeholder);
        return loc < 0 ? template : template.substring(0, loc) + replacement + template.substring(loc + placeholder.length());
    }

    public static String[] split(String seperators, String list) {
        return split(seperators, list, false);
    }

    public static String[] split(String seperators, String list, boolean include) {
        StringTokenizer tokens = new StringTokenizer(list, seperators, include);
        String[] result = new String[tokens.countTokens()];

        for(int var5 = 0; tokens.hasMoreTokens(); result[var5++] = tokens.nextToken()) {
        }

        return result;
    }

    public static String unqualify(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }

    public static String qualifier(String qualifiedName) {
        int loc = qualifiedName.lastIndexOf(".");
        return loc < 0 ? "" : qualifiedName.substring(0, loc);
    }

    public static String[] suffix(String[] columns, String suffix) {
        if (suffix == null) {
            return columns;
        } else {
            String[] qualified = new String[columns.length];

            for(int i = 0; i < columns.length; ++i) {
                qualified[i] = suffix(columns[i], suffix);
            }

            return qualified;
        }
    }

    private static String suffix(String name, String suffix) {
        return suffix == null ? name : name + suffix;
    }

    public static String root(String qualifiedName) {
        int loc = qualifiedName.indexOf(".");
        return loc < 0 ? qualifiedName : qualifiedName.substring(0, loc);
    }

    public static String unroot(String qualifiedName) {
        int loc = qualifiedName.indexOf(".");
        return loc < 0 ? qualifiedName : qualifiedName.substring(loc + 1, qualifiedName.length());
    }

    public static boolean booleanValue(String tfString) {
        String trimmed = tfString.trim().toLowerCase();
        return trimmed.equals("true") || trimmed.equals("t");
    }

    public static String toString(Object[] array) {
        int len = array.length;
        if (len == 0) {
            return "";
        } else {
            StringBuffer buf = new StringBuffer(len * 12);

            for(int i = 0; i < len - 1; ++i) {
                buf.append(array[i]).append(", ");
            }

            return buf.append(array[len - 1]).toString();
        }
    }

    public static String[] multiply(String string, Iterator placeholders, Iterator replacements) {
        String[] result;
        for(result = new String[]{string}; placeholders.hasNext(); result = multiply(result, (String)placeholders.next(), (String[])replacements.next())) {
        }

        return result;
    }

    private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
        String[] results = new String[replacements.length * strings.length];
        int n = 0;

        for(int i = 0; i < replacements.length; ++i) {
            for(int j = 0; j < strings.length; ++j) {
                results[n++] = replaceOnce(strings[j], placeholder, replacements[i]);
            }
        }

        return results;
    }

    public static int countUnquoted(String string, char character) {
        if ('\'' == character) {
            throw new IllegalArgumentException("Unquoted count of quotes is invalid");
        } else if (string == null) {
            return 0;
        } else {
            int count = 0;
            int stringLength = string.length();
            boolean inQuote = false;

            for(int indx = 0; indx < stringLength; ++indx) {
                char c = string.charAt(indx);
                if (inQuote) {
                    if ('\'' == c) {
                        inQuote = false;
                    }
                } else if ('\'' == c) {
                    inQuote = true;
                } else if (c == character) {
                    ++count;
                }
            }

            return count;
        }
    }

    public static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

    public static boolean isNotEmptyByTrim(String string) {
        return string != null && string.length() > 0 && string.trim().length() > 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isEmptyByTrim(String string) {
        return string == null || string.length() == 0 || string.trim().length() == 0;
    }

    public static boolean isEmpty(String... args) {
        String[] var4 = args;
        int var3 = args.length;

        for(int var2 = 0; var2 < var3; ++var2) {
            String str = var4[var2];
            if (isEmpty(str)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotEmpty(String... args) {
        return !isEmpty(args);
    }

    public static String qualify(String prefix, String name) {
        if (name != null && prefix != null) {
            return (new StringBuffer(prefix.length() + name.length() + 1)).append(prefix).append('.').append(name).toString();
        } else {
            throw new NullPointerException();
        }
    }

    public static String[] qualify(String prefix, String[] names) {
        if (prefix == null) {
            return names;
        } else {
            int len = names.length;
            String[] qualified = new String[len];

            for(int i = 0; i < len; ++i) {
                qualified[i] = qualify(prefix, names[i]);
            }

            return qualified;
        }
    }

    public static int firstIndexOfChar(String sqlString, String string, int startindex) {
        int matchAt = -1;

        for(int i = 0; i < string.length(); ++i) {
            int curMatch = sqlString.indexOf(string.charAt(i), startindex);
            if (curMatch >= 0) {
                if (matchAt == -1) {
                    matchAt = curMatch;
                } else {
                    matchAt = Math.min(matchAt, curMatch);
                }
            }
        }

        return matchAt;
    }

    public static String truncate(String string, int length) {
        return string.length() <= length ? string : string.substring(0, length);
    }

    public static String generateAlias(String description, int unique) {
        return generateAliasRoot(description) + Integer.toString(unique) + '_';
    }

    private static String generateAliasRoot(String description) {
        String result = truncate(unqualify(description), 10).toLowerCase().replace('$', '_');
        return Character.isDigit(result.charAt(result.length() - 1)) ? result + "x" : result;
    }

    public static String generateAlias(String description) {
        return generateAliasRoot(description) + '_';
    }

    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String dealNull(String str) {
        return str == null ? "" : str.trim();
    }

    public static Object dealNull(Object str) {
        return str == null ? "" : str;
    }

    public static String replaceString(String str, String str1, String str2) {
        String outstr = "";
        if (isEmpty(str)) {
            return "";
        } else if (isEmpty(str1)) {
            return str;
        } else {
            while(true) {
                int s_index = str.indexOf(str1);
                if (s_index < 0) {
                    outstr = outstr + str;
                    return outstr;
                }

                outstr = outstr + str.substring(0, s_index) + str2;
                str = str.substring(s_index + str1.length());
            }
        }
    }

    public static String getFileExt(String fileName) {
        int iIndex = fileName.lastIndexOf(".");
        return iIndex < 0 ? "" : fileName.substring(iIndex + 1).toLowerCase();
    }

    public static String getHttpFileExt(String imgName) {
        String str = "";
        String p = "(.jpg|.JPG|.gif|.GIF|.png|.PNG|.bmp|.BMP).*$";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(imgName);
        if (matcher.find()) {
            str = matcher.group();
        }

        return str;
    }

    public static boolean checkValue(String scd, String dcd) {
        return !isEmpty(dcd) && !isEmpty(scd) ? dcd.equals(dcd) : false;
    }

    public static String getLikeStr(String field) {
        return "%" + field + "%";
    }

    public static String getStrRomovedot(String field) {
        String str = field;
        if (isNotEmpty(field) && field.indexOf(".") > 0) {
            String str1 = field.substring(field.indexOf(".") + 1, field.indexOf(".") + 2);
            if (str1.equals("0")) {
                str = field.substring(0, field.indexOf("."));
            }
        }

        return str;
    }

    public static String[] strToArr(String str) {
        return isNotEmpty(str) ? str.split(",") : null;
    }

    public static List<String> strToList(String str) {
        return isNotEmpty(str) ? Arrays.asList(str.split(",")) : null;
    }

    public static String getStrRemoveHtmlTag(String str) {
        String result = str.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
        result = result.replaceAll("[(/>)<]", "");
        return result;
    }

    public static String getStrRemoveEscapeStr(String str) {
        return str.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", " ").replaceAll("\b", "").replaceAll("\f", "");
    }

    public static String replaceQuotes(String oldStr) {
        return oldStr.replace("'", "??");
    }

    public static String splitAndFilterString(String input) {
        if (input != null && !input.trim().equals("")) {
            String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
            str = str.replaceAll("[(/>)<]", "");
            int len = str.length();
            return str;
        } else {
            return "";
        }
    }

    public static String removeLast(String str) {
        if (isNotEmpty(str)) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public static String removeLast(String str, String lastStr) {
        if (isNotEmpty(str, lastStr)) {
            int lastStrStart = str.lastIndexOf(lastStr);
            if (lastStrStart == str.length() - 1) {
                str = str.substring(0, lastStrStart);
            }
        }

        return str;
    }

    public static String fillzero(int num, int ws) {
        String formatstr = "";

        for(int i = 0; i < ws; ++i) {
            formatstr = formatstr + "0";
        }

        DecimalFormat df = new DecimalFormat(formatstr);
        return df.format((long)num);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "<span style=\"font-size:12px;\"> </span><p><span style=\"font-family:\" calibri\",\"sans-serif\";font-size:12pt;\"=\"\"><span style=\"font-family:????;font-size:12px;\">?????????<b><span style=\"font-size:12px;\">???</span></b>????????????</span><span style=\"font-family:\" calibri\",\"sans-serif\";font-size:12px;\"=\"\">I </span></span></p>";
        System.out.println(splitAndFilterString(s));
    }
}
