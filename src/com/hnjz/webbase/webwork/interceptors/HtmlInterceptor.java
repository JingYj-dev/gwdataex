package com.hnjz.webbase.webwork.interceptors;

import com.hnjz.util.RegexCheck;
import com.hnjz.webbase.webwork.interceptors.HtmlParameter;
import com.hnjz.webbase.webwork.interceptors.HtmlTagParse;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HtmlInterceptor extends AroundInterceptor {
    public HtmlInterceptor() {
    }

    protected void after(ActionInvocation dispatcher, String result) throws Exception {
    }

    protected void before(ActionInvocation invocation) throws Exception {
        if (!(invocation.getAction() instanceof HtmlParameter)) {
            Map parameters = ActionContext.getContext().getParameters();
            if (this.log.isDebugEnabled()) {
                this.log.debug("Setting params " + this.serializeMap(parameters));
            }

            Map tmpMap = new HashMap();
            ActionContext invocationContext = invocation.getInvocationContext();

            try {
                invocationContext.put("report.conversion.errors", Boolean.TRUE);
                if (parameters != null) {
                    Map fieldMap = HtmlTagParse.getInstance().getFieldNameMap();
                    Iterator iterator = parameters.keySet().iterator();

                    while(iterator.hasNext()) {
                        String key = (String)iterator.next();
                        String[] value = (String[])parameters.get(key);

                        for(int i = 0; i < value.length; ++i) {
                            if (fieldMap.size() > 0 && fieldMap.get(key) == null) {
                                value[i] = RegexCheck.TagReplace(value[i].trim());
                            }
                        }

                        tmpMap.put(key, value);
                    }

                    ActionContext.getContext().setParameters(tmpMap);
                }
            } finally {
                invocationContext.put("report.conversion.errors", Boolean.FALSE);
            }
        }

    }

    private String serializeMap(Map data) {
        StringBuffer buf = new StringBuffer("{");

        for(Iterator var4 = data.keySet().iterator(); var4.hasNext(); buf.append(",")) {
            Object key = var4.next();
            buf.append(key).append("=");
            Object value = data.get(key);
            if (value != null && value.getClass().isArray()) {
                Object[] lv = (Object[])value;
                int leng = lv.length;
                if (leng == 1) {
                    buf.append(lv[0]);
                } else {
                    buf.append("[");

                    for(int idx = 0; idx < leng; ++idx) {
                        buf.append(Array.get(value, idx)).append(",");
                    }

                    buf.setCharAt(buf.length() - 1, ']');
                }
            }
        }

        if (buf.charAt(buf.length() - 1) == ',') {
            buf.setCharAt(buf.length() - 1, '}');
        } else {
            buf.append("}");
        }

        return buf.toString();
    }
}
