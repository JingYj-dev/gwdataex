package com.hnjz.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:28
 */
public class DateUtil {
    public DateUtil() {
    }

    public static final long daysInterval(Date fromDate, Date toDate) {
        return (toDate.getTime() - fromDate.getTime()) / 86400000L;
    }

    public static final long daysInterval(Date fromDate) {
        if (fromDate == null) {
            return 0L;
        } else {
            Date toDate = new Date();
            return daysInterval(fromDate, toDate);
        }
    }

    public static final int yearsInterval(Date fromDate, Date toDate) {
        return fromDate != null && toDate != null ? toDate.getYear() - fromDate.getYear() : 0;
    }

    public static final String leftDays(Date fromDate) {
        Date toDate = new Date();
        long l = daysInterval(fromDate, toDate);
        l = l * -1L + 1L;
        return l <= 0L ? "到期" : String.valueOf(l);
    }

    public static final int yearsInterval(Date fromDate) {
        if (fromDate == null) {
            return 0;
        } else {
            Date toDate = new Date();
            return yearsInterval(fromDate, toDate);
        }
    }

    public static boolean compareDate(Date d1, Date d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d1).equals(sdf.format(d2));
    }

    public static String getMinu(int minutes) {
        GregorianCalendar calTmp = new GregorianCalendar();
        calTmp.add(12, -1 * minutes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(calTmp.getTime());
    }

    public static String getHour(int hours) {
        GregorianCalendar calTmp = new GregorianCalendar();
        calTmp.add(10, -1 * hours);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calTmp.getTime());
    }

    public static String getDate(int days) {
        GregorianCalendar calTmp = new GregorianCalendar();
        calTmp.add(5, -1 * days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calTmp.getTime());
    }

    public static Date getFirstWeek(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        int dayOfWeek = cal.get(7);
        cal.add(5, cal.getActualMinimum(7) - dayOfWeek + 1);
        return cal.getTime();
    }

    public static Date getLastWeek(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        int dayOfWeek = cal.get(7);
        cal.add(5, cal.getActualMaximum(7) - dayOfWeek + 1);
        return cal.getTime();
    }

    public static Date getFirstMonth(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        int dayOfWeek = cal.get(5);
        cal.add(5, cal.getActualMinimum(5) - dayOfWeek);
        return cal.getTime();
    }

    public static Date getLastMonth(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        int dayOfWeek = cal.get(5);
        cal.add(5, cal.getActualMaximum(5) - dayOfWeek);
        return cal.getTime();
    }

    public static Date getFirstYear(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        int dayOfWeek = cal.get(6);
        cal.add(5, cal.getActualMinimum(6) - dayOfWeek);
        return cal.getTime();
    }

    public static Date getLastYear(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        int dayOfWeek = cal.get(6);
        cal.add(5, cal.getActualMaximum(6) - dayOfWeek);
        return cal.getTime();
    }

    public static String getDateStr(Date date, String pattern) {
        String p = pattern == null ? "yyyy-MM-dd" : pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        Date d = date == null ? new Date() : date;
        return sdf.format(d);
    }

    public static Date getDate(String dateStr, String pattern) {
        String p = pattern == null ? "yyyy-MM-dd" : pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        Date d = null;

        try {
            d = sdf.parse(dateStr);
        } catch (Exception var6) {
        }

        return d;
    }

    public static Date addDate(Date date, int days) {
        Date d = null;
        d = new Date(date.getTime() + (long)(days * 24 * 60 * 60) * 1000L);
        return d;
    }
}
