package com.hnjz.webbase.webwork.interceptors;

import com.hnjz.common.PluginBus;
import com.hnjz.common.plugins.IPlugin;
import com.hnjz.common.plugins.annotations.Plugin;
import com.hnjz.util.StringHelper;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PluginInterceptor extends AroundInterceptor {
    public PluginInterceptor() {
    }

    protected void after(ActionInvocation arg0, String arg1) throws Exception {
    }

    protected void before(ActionInvocation arg0) throws Exception {
        Action action = arg0.getAction();
        if (action != null) {
            List<Field> fields = new LinkedList();
            fields.addAll(Arrays.asList(action.getClass().getDeclaredFields()));
            fields.addAll(Arrays.asList(action.getClass().getSuperclass().getDeclaredFields()));
            Iterator var5 = fields.iterator();

            while(var5.hasNext()) {
                Field field = (Field)var5.next();
                if (field.isAccessible()) {
                    this.processActionField(action, field);
                } else {
                    field.setAccessible(true);
                    this.processActionField(action, field);
                    field.setAccessible(false);
                }
            }
        }

    }

    private void processActionField(Action action, Field field) throws Exception {
        Object fv = field.get(action);
        if (fv == null) {
            Plugin p = (Plugin)field.getAnnotation(Plugin.class);
            if (p != null) {
                String id = p.id();
                String key = p.key();
                if (!StringHelper.isEmpty(id)) {
                    IPlugin plugin = (IPlugin)PluginBus.getPlugin(id, key);
                    if (plugin != null) {
                        field.set(action, plugin);
                    }
                }
            }

        }
    }
}
