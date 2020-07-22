package com.hnjz.util;

import com.hnjz.util.DateTimeUtil;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:20
 */
public class Json {
    public Json() {
    }

    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (!(obj instanceof String) && !(obj instanceof Integer) && !(obj instanceof Float) && !(obj instanceof Boolean) && !(obj instanceof Short) && !(obj instanceof Double) && !(obj instanceof Long) && !(obj instanceof BigDecimal) && !(obj instanceof BigInteger) && !(obj instanceof Byte) && !obj.getClass().isPrimitive()) {
            if (obj instanceof Date) {
                json.append("\"").append(DateTimeUtil.getDateTimeString((Date)obj)).append("\"");
            } else if (obj instanceof Object[]) {
                json.append(array2json((Object[])obj));
            } else if (obj instanceof List) {
                json.append(list2json((List)obj));
            } else if (obj instanceof Map) {
                json.append(map2json((Map)obj));
            } else if (obj instanceof Set) {
                json.append(set2json((Set)obj));
            } else {
                json.append(bean2json(obj));
            }
        } else if (obj instanceof Number) {
            json.append(obj.toString());
        } else {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        }

        return json.toString();
    }

    public static String bean2json(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = (PropertyDescriptor[])null;

        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException var8) {
        }

        if (props != null) {
            for(int i = 0; i < props.length; ++i) {
                try {
                    String name = object2json(props[i].getName());
                    Method method = props[i].getReadMethod();
                    if (method != null && !method.isAnnotationPresent(com.hnjz.util.Json.Unjson.class)) {
                        String value = object2json(method.invoke(bean));
                        json.append(name);
                        json.append(":");
                        json.append(value);
                        json.append(",");
                    }
                } catch (Exception var7) {
                }
            }

            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }

        return json.toString();
    }

    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                json.append(object2json(obj));
                json.append(",");
            }

            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }

        return json.toString();
    }

    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            Object[] var5 = array;
            int var4 = array.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                Object obj = var5[var3];
                json.append(object2json(obj));
                json.append(",");
            }

            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }

        return json.toString();
    }

    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            Iterator var3 = map.keySet().iterator();

            while(var3.hasNext()) {
                Object key = var3.next();
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }

            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }

        return json.toString();
    }

    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                json.append(object2json(obj));
                json.append(",");
            }

            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }

        return json.toString();
    }

    public static String string2json(String s) {
        if (s == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < s.length(); ++i) {
                char ch = s.charAt(i);
                switch(ch) {
                    case '\b':
                        sb.append("\\b");
                        continue;
                    case '\t':
                        sb.append("\\t");
                        continue;
                    case '\n':
                        sb.append("\\n");
                        continue;
                    case '\f':
                        sb.append("\\f");
                        continue;
                    case '\r':
                        sb.append("\\r");
                        continue;
                    case '"':
                        sb.append("\\\"");
                        continue;
                    case '/':
                        sb.append("\\/");
                        continue;
                    case '\\':
                        sb.append("\\\\");
                        continue;
                }

                if (ch >= 0 && ch <= 31) {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");

                    for(int k = 0; k < 4 - ss.length(); ++k) {
                        sb.append('0');
                    }

                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }

            return sb.toString();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.FIELD})
    public @interface Unjson {
    }
}
