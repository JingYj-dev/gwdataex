package com.hnjz.webbase.webwork.conversion;

import com.opensymphony.webwork.util.WebWorkTypeConverter;
import com.opensymphony.xwork.ActionContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class WebWorkMapConverter extends WebWorkTypeConverter {
    public WebWorkMapConverter() {
    }

    public Object convertFromString(Map context, String[] values, Class toClass) {
        return this.doConvertToMap(values[0]);
    }

    public Object doConvertToMap(String res) {
        ActionContext ctx = ActionContext.getContext();
        Map params = null;
        if (ctx != null) {
            params = ctx.getParameters();
        }

        StringTokenizer st = new StringTokenizer(res, "[,]");
        HashMap param = new HashMap(st.countTokens());

        while(true) {
            while(true) {
                String[] entry;
                do {
                    if (!st.hasMoreTokens()) {
                        return param;
                    }

                    entry = st.nextToken().split(":");
                } while(entry.length <= 1);

                if (entry[1].startsWith("%") && params != null) {
                    Object obj = params.get(entry[1].substring(1));
                    if (obj != null) {
                        param.put(entry[0], obj);
                    }
                } else {
                    if (entry[1].startsWith("\\")) {
                        entry[1] = entry[1].substring(1);
                    }

                    param.put(entry[0], entry[1]);
                }
            }
        }
    }

    public String convertToString(Map context, Object o) {
        StringBuffer content = (new StringBuffer()).append("[");
        if (o instanceof Map) {
            Map map = (Map)o;
            Iterator itertor = map.entrySet().iterator();

            while(itertor.hasNext()) {
                Map.Entry entry = (Map.Entry)itertor.next();
                content.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            }

            if (content.charAt(content.length() - 1) == ',') {
                content.deleteCharAt(content.length() - 1);
            }
        }

        content.append("]");
        return content.toString();
    }
}
