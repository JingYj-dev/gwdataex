package com.hnjz.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:18
 */
public class ReflectionUtil {
    public ReflectionUtil() {
    }

    public Object getProperty(Object obj, String fieldname) throws Exception {
        Object result = null;
        Class objClass = obj.getClass();
        Field field = objClass.getField(fieldname);
        result = field.get(obj);
        return result;
    }

    public Object getStaticProperty(String className, String fieldName) throws Exception {
        Class cls = Class.forName(className);
        Field field = cls.getField(fieldName);
        Object provalue = field.get(cls);
        return provalue;
    }

    public Object getPrivatePropertyValue(Object obj, String propertyName) throws Exception {
        Class cls = obj.getClass();
        Field field = cls.getDeclaredField(propertyName);
        field.setAccessible(true);
        Object retvalue = field.get(obj);
        return retvalue;
    }

    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Class cls = owner.getClass();
        Class[] argclass = new Class[args.length];
        int i = 0;

        for(int j = argclass.length; i < j; ++i) {
            argclass[i] = args[i].getClass();
        }

        Method method = cls.getMethod(methodName, argclass);
        return method.invoke(owner, args);
    }

    public Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class cls = Class.forName(className);
        Class[] argclass = new Class[args.length];
        int i = 0;

        for(int j = argclass.length; i < j; ++i) {
            argclass[i] = args[i].getClass();
        }

        Method method = cls.getMethod(methodName, argclass);
        return method.invoke((Object)null, args);
    }

    public Object newInstance(String className, Object[] args) throws Exception {
        Class clss = Class.forName(className);
        Class[] argclass = new Class[args.length];
        int i = 0;

        for(int j = argclass.length; i < j; ++i) {
            argclass[i] = args[i].getClass();
        }

        Constructor cons = clss.getConstructor(argclass);
        return cons.newInstance();
    }

    public Object newInstance(Class newoneClass, Object... args) throws Exception {
        if (args == null) {
            return newoneClass.newInstance();
        } else {
            Class[] argsClass = new Class[args.length];
            int i = 0;

            for(int j = args.length; i < j; ++i) {
                argsClass[i] = args[i].getClass();
            }

            Constructor cons = newoneClass.getConstructor(argsClass);
            return cons.newInstance(args);
        }
    }

    public boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }

    public static void main(String[] args) throws Exception {
    }
}
