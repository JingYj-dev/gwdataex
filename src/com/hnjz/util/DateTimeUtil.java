package com.hnjz.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:29
 */
public class DateTimeUtil {
    public DateTimeUtil() {
    }

    public static String getDateTimeString(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = new Date();
        String DateTimeString = df.format(date);
        return DateTimeString;
    }

    public static String getDateTimeString(int days, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Calendar ca = Calendar.getInstance();
        ca.add(5, days);
        String DateTimeString = df.format(ca.getTime());
        return DateTimeString;
    }

    public static String getDateTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String DateTimeString = df.format(date);
        return DateTimeString;
    }

    public static String getDateVersionString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String DateTimeString = df.format(date);
        return "V" + DateTimeString;
    }

    public static String getDateTimeVersionString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmm");
        Date date = new Date();
        String DateTimeString = df.format(date);
        return "V" + DateTimeString;
    }

    public static String getDateString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            date = new Date();
        }

        String DateTimeString = df.format(date);
        return DateTimeString;
    }

    public static String getDateTimeString(Date date) {
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String getLogDateTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String DateTimeString = df.format(date);
        return DateTimeString;
    }

    public static String getLogDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String DateTimeString = df.format(date);
        return DateTimeString;
    }

    public static String getDateString(Date date, String format) {
        if (date != null && format != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String result = sdf.format(date);
            return result;
        } else {
            return null;
        }
    }

    public static Date stringToUtilDate(String str, String format) {
        if (str != null && format != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = null;

            try {
                date = sdf.parse(str);
            } catch (Exception var5) {
            }

            return date;
        } else {
            return null;
        }
    }

    public static Date stringToUtilDate(String str) {
        return stringToUtilDate(str, "yyyy-MM-dd HH:mm:ss");
    }
}
