package com.hnjz.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:16
 */
public class StringHelper {
    private StringHelper() {
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

    public static boolean isAnyEmpty(String... args) {
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

    public static boolean isNotAnyEmpty(String... args) {
        return !isAnyEmpty(args);
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

    public static String getFileExt(String fileName) {
        int iIndex = fileName.lastIndexOf(".");
        return iIndex < 0 ? "" : fileName.substring(iIndex + 1).toLowerCase();
    }

    public static String[] strToArr(String str) {
        return isNotEmpty(str) ? str.split(",") : null;
    }

    public static List<String> strToList(String str) {
        return isNotEmpty(str) ? Arrays.asList(str.split(",")) : null;
    }

    public static String listToStrQuoted(List<String> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for(int i = 0; i < list.size(); ++i) {
                str = str + "'" + (String)list.get(i) + (i != list.size() - 1 ? "'," : "'");
            }
        }

        return str;
    }

    public static String listToStr(List<String> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for(int i = 0; i < list.size(); ++i) {
                str = str + (String)list.get(i) + (i != list.size() - 1 ? "," : "");
            }
        }

        return str;
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
}
