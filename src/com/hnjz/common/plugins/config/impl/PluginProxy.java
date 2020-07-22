package com.hnjz.common.plugins.config.impl;

import com.hnjz.common.plugins.IPlugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:03
 */
public class PluginProxy implements InvocationHandler {
    private Object plugin = null;

    public PluginProxy(Object p) {
        if (p == null) {
            throw new NullPointerException("Plugin Proxy Delegate!");
        } else {
            this.plugin = p;
        }
    }

    public Object getProxy() {
        List<Class<?>> interfaceList = this.getInterfaces(this.plugin);
        if (!interfaceList.contains(IPlugin.class)) {
            interfaceList.add(IPlugin.class);
        }

        return Proxy.newProxyInstance(this.plugin.getClass().getClassLoader(), (Class[]) interfaceList.toArray(new Class[0]), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        result = method.invoke(this.plugin, args);
        return result;
    }

    private List<Class<?>> getInterfaces(Object plugin) {
        List<Class<?>> interfaceList = new LinkedList();

        for (Class pluginClass = plugin.getClass(); !pluginClass.equals(Object.class); pluginClass = pluginClass.getSuperclass()) {
            Class[] var7;
            int var6 = (var7 = pluginClass.getInterfaces()).length;

            for (int var5 = 0; var5 < var6; ++var5) {
                Class<?> cls = var7[var5];
                if (!interfaceList.contains(cls)) {
                    interfaceList.add(cls);
                }
            }
        }

        return interfaceList;
    }
}
