package com.hnjz.util.numeric;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:32
 */
public class BigDecimalUtil {
    public BigDecimalUtil() {
    }

    public static String divideBetweenDoubleNum(Long v1, Long v2, int len, String describle) {
        return addZero(divide((double)v1, (double)v2, len), len) + describle;
    }

    public static double divide(double v1, double v2, int scale) {
        double c = div(v1, v2, scale + 2);
        return div(c * 100.0D, 1.0D, scale);
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, 4).doubleValue();
        }
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 4).doubleValue();
        }
    }

    public static String getDiv(double v1, double v2) {
        DecimalFormat df = new DecimalFormat("###.00");
        return df.format(v1 / v2);
    }

    public static String addZero(double v, int len) {
        String temp = String.valueOf(v);
        int point = temp.indexOf(".");
        int length = temp.length();
        int limitLen = length - 1 - point;
        if (limitLen > len) {
            return temp.substring(0, point + len);
        } else if (limitLen >= len) {
            return temp;
        } else {
            StringBuffer sb = new StringBuffer(temp);

            for(int i = 0; i < len - limitLen; ++i) {
                sb.append(0);
            }

            return sb.toString();
        }
    }
}
