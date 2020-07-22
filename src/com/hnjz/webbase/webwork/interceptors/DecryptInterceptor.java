package com.hnjz.webbase.webwork.interceptors;

import com.hnjz.util.Base64Util;
import com.hnjz.util.DesUtil;
import com.hnjz.util.StringHelper;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DecryptInterceptor extends AroundInterceptor {
    private static final String encoding = "UTF8";

    public DecryptInterceptor() {
    }

    protected void before(ActionInvocation invocation) throws Exception {
        Map parameters = ActionContext.getContext().getParameters();
        String[] encryptMarks = (String[])parameters.get("form.encrypt");
        if (encryptMarks != null && encryptMarks.length > 0) {
            String mark = encryptMarks[0];
            if ("true".equalsIgnoreCase(mark)) {
                Map tmpMap = new HashMap();
                ActionContext invocationContext = invocation.getInvocationContext();
                String desKey = (String)invocation.getInvocationContext().getSession().get("form.crypto.des.key");
                if (StringHelper.isEmpty(desKey)) {
                    desKey = "EDCioui1234";
                }

                try {
                    invocationContext.put("report.conversion.errors", Boolean.TRUE);
                    if (parameters != null) {
                        Iterator iterator = parameters.keySet().iterator();

                        while(true) {
                            String key;
                            do {
                                if (!iterator.hasNext()) {
                                    ActionContext.getContext().setParameters(tmpMap);
                                    return;
                                }

                                key = (String)iterator.next();
                            } while("form.encrypt".equals(key));

                            String[] value = (String[])parameters.get(key);

                            for(int i = 0; i < value.length; ++i) {
                                if (StringHelper.isNotEmpty(value[i])) {
                                    value[i] = new String(DesUtil.decrypt(Base64Util.base64Decode(value[i]), desKey.getBytes("UTF8")), "UTF8");
                                }
                            }

                            tmpMap.put(key, value);
                        }
                    }
                } finally {
                    invocationContext.put("report.conversion.errors", Boolean.FALSE);
                }

            }
        }
    }

    protected void after(ActionInvocation arg0, String arg1) throws Exception {
    }
}
