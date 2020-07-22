package com.hnjz.util.numeric;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:32
 */
public class Arithmetic {
    public Arithmetic() {
    }

    public static double arithmetic(String exp) {
        String result = parseExp(exp).replaceAll("[\\[\\]]", "");
        return Double.parseDouble(result);
    }

    public static Integer evaluate(String exp) {
        String result = parseExp(exp).replaceAll("[\\[\\]]", "");
        return Integer.parseInt(result);
    }

    public static String parseExp(String expression) {
        expression = expression.replaceAll("\\s+", "").replaceAll("^\\((.+)\\)$", "$1");
        String minExp = "^((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))$";
        String noParentheses;
        if (expression.matches(minExp)) {
            noParentheses = calculate(expression);
            return "[" + noParentheses + "]";
        } else if (expression.matches("^[+-]?\\d+\\.?\\d?$")) {
            return expression;
        } else {
            noParentheses = "^[^\\(\\)]+$";
            String priorOperatorExp = "(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))";
            String operatorExp = "(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))";
            if (expression.matches(noParentheses)) {
                Pattern patt = Pattern.compile(priorOperatorExp);
                Matcher mat = patt.matcher(expression);
                String tempMinExp;
                if (mat.find()) {
                    tempMinExp = mat.group();
                    expression = expression.replaceFirst(priorOperatorExp, parseExp(tempMinExp));
                } else {
                    patt = Pattern.compile(operatorExp);
                    mat = patt.matcher(expression);
                    if (mat.find()) {
                        tempMinExp = mat.group();
                        expression = expression.replaceFirst(operatorExp, parseExp(tempMinExp));
                    }
                }

                return parseExp(expression);
            } else {
                String minParentheses = "\\([^\\(\\)]+\\)";
                Pattern patt = Pattern.compile(minParentheses);
                Matcher mat = patt.matcher(expression);
                if (mat.find()) {
                    String tempMinExp = mat.group();
                    expression = expression.replaceFirst(minParentheses, parseExp(tempMinExp));
                }

                return parseExp(expression);
            }
        }
    }

    public static String calculate(String exp) {
        exp = exp.replaceAll("[\\[\\]]", "");
        String[] number = exp.replaceFirst("(\\d)[\\+\\-\\*\\/]", "$1,").split(",");
        BigDecimal number1 = new BigDecimal(number[0]);
        BigDecimal number2 = new BigDecimal(number[1]);
        BigDecimal result = null;
        String operator = exp.replaceFirst("^.*\\d([\\+\\-\\*\\/]).+$", "$1");
        if ("+".equals(operator)) {
            result = number1.add(number2);
        } else if ("-".equals(operator)) {
            result = number1.subtract(number2);
        } else if ("*".equals(operator)) {
            result = number1.multiply(number2);
        } else if ("/".equals(operator)) {
            result = number1.divide(number2);
        }

        return result != null ? result.toString() : null;
    }
}
