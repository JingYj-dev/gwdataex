package com.hnjz.util.numeric;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:33
 */
public class ComputationUtil {
    public static final int DEF_DIV_SCALE = 10;
    public static final int DEF_FRAC_SCALE = 2;

    public ComputationUtil() {
    }

    public static double add(double v1, double v2) {
        return v1 + v2;
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(getDecimalStr(v1, 2));
        BigDecimal b2 = new BigDecimal(getDecimalStr(v2, 2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(getDecimalStr(v1, 2));
        BigDecimal b2 = new BigDecimal(getDecimalStr(v2, 2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(getDecimalStr(v1, 2));
            BigDecimal b2 = new BigDecimal(getDecimalStr(v2, 2));
            return b1.divide(b2, scale, 4).doubleValue();
        }
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            return v;
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 4).doubleValue();
        }
    }

    private static String getDecimalStr(double t, int scale) {
        if (scale <= 0) {
            return Double.toString(t);
        } else {
            t = round(t, scale);
            return Double.toString(t);
        }
    }

    private static String getDecimalA(double t, int scale) {
        String str = Double.toString(t);
        StringBuffer pattern = new StringBuffer("#0.");
        if (scale <= 0) {
            return str;
        } else {
            while(scale > 0) {
                pattern.append("0");
                --scale;
            }

            DecimalFormat df = new DecimalFormat(pattern.toString());
            return df.format(t);
        }
    }

    public static void main(String[] args) {
        double b = round(1.5609652342D, 5);
        System.out.println("b = " + b);
        System.out.println((new BigDecimal(1.23456789333333606E18D)).toString());
        System.out.println((new BigDecimal("123456789.02")).toString());
    }
}
