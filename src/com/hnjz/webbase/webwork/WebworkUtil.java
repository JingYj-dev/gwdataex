package com.hnjz.webbase.webwork;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.ApplicationMap;
import com.opensymphony.webwork.dispatcher.RequestMap;
import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.webwork.dispatcher.SessionMap;
import com.opensymphony.webwork.util.AttributeMap;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebworkUtil {
    public WebworkUtil() {
    }

    public static ActionContext getActionContext() {
        return ActionContext.getContext();
    }

    public static String translateVariables2(String expression, OgnlValueStack stack) {
        if (expression == null) {
            return "";
        } else {
            while(true) {
                int x = expression.indexOf("%{");
                int y = expression.indexOf("}", x);
                if (x == -1 || y == -1) {
                    return expression;
                }

                String var = expression.substring(x + 2, y);
                Object o = stack.findValue(var);
                if (o != null) {
                    expression = expression.substring(0, x) + o + expression.substring(y + 1);
                } else {
                    expression = expression.substring(0, x) + expression.substring(y + 1);
                }
            }
        }
    }

    public static String translateVariables(String expression, OgnlValueStack stack) {
        while(true) {
            int x = expression.indexOf("${");
            int y = expression.indexOf("}", x);
            if (x == -1 || y == -1) {
                return expression;
            }

            String var = expression.substring(x + 2, y);
            Object o = stack.findValue(var);
            if (o != null) {
                expression = expression.substring(0, x) + o + expression.substring(y + 1);
            } else {
                expression = expression.substring(0, x) + expression.substring(y + 1);
            }
        }
    }

 /*   public static OgnlValueStack getStack(PageContext pageContext) {
        if (pageContext == null) {
            return ActionContext.getContext().getValueStack();
        } else {
            HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
            OgnlValueStack stack = (OgnlValueStack)req.getAttribute("webwork.valueStack");
            if (stack == null) {
                stack = new OgnlValueStack();
                HttpServletResponse res = (HttpServletResponse)pageContext.getResponse();
                Map extraContext = ServletDispatcher.createContextMap(new RequestMap(req), req.getParameterMap(), new SessionMap(req), new ApplicationMap(pageContext.getServletContext()), req, res, pageContext.getServletConfig());
                extraContext.put("com.opensymphony.xwork.dispatcher.PageContext", pageContext);
                stack.getContext().putAll(extraContext);
                req.setAttribute("webwork.valueStack", stack);
                ActionContext.setContext(new ActionContext(stack.getContext()));
            } else {
                Map context = stack.getContext();
                context.put("com.opensymphony.xwork.dispatcher.PageContext", pageContext);
                if (!context.containsKey("attr")) {
                    AttributeMap attrMap = new AttributeMap(context);
                    context.put("attr", attrMap);
                }
            }

            return stack;
        }
    }
*/
    public static OgnlValueStack getStack() {
        return ActionContext.getContext().getValueStack();
    }

    public static <T> T findValue(Class<T> clz, String exp, OgnlValueStack ovs) {
        return (T) ovs.findValue(exp, clz);
    }

    public static Object findValue(String exp, OgnlValueStack ovs) {
        return ovs.findValue(exp);
    }

/*    public static <T> T findValue(Class<T> clz, String exp, PageContext pc) {
        return findValue(clz, exp, getStack(pc));
    }*/

//    public static <T> T findValue(Class<T> clz, String exp) {
//        return findValue(clz, exp, getStack(getPageContext()));
//    }
//
//    public static Object findValue(String exp, PageContext pc) {
//        return findValue(exp, getStack(pc));
//    }
//
//    public static Object findValue(String exp) {
//        return findValue(exp, getPageContext());
//    }
//
//    public static Object pop(PageContext pc) {
//        return getStack(pc).pop();
//    }
//
//    public static Object peek(PageContext pc) {
//        return getStack(pc).peek();
//    }
//
//    public static void push(Object obj, PageContext pc) {
//        getStack(pc).push(obj);
//    }
//
//    public static void put(String expr, Object obj, PageContext pc) {
//        getStack(pc).getContext().put(expr, obj);
//    }
//
//    public static void remove(String expr, PageContext pc) {
//        getStack(pc).getContext().remove(expr);
//    }

    public static HttpServletRequest getHttpRequest() {
        return ServletActionContext.getRequest();
    }
//
//    public static PageContext getPageContext() {
//        return ServletActionContext.getPageContext();
//    }

    public static HttpServletResponse getHttpResponse() {
        return ServletActionContext.getResponse();
    }

    public static ServletConfig getServletConfig() {
        return ServletActionContext.getServletConfig();
    }

    public static ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    public static HttpSession getHttpSession() {
        return ServletActionContext.getRequest().getSession();
    }

    public static String getRemoteAddress() {
        HttpServletRequest request = getHttpRequest();
        if (request == null) {
            return null;
        } else {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            return ip;
        }
    }

    /** @deprecated */
    public static String getRequstActionUrl() {
        ActionContext ctx = ActionContext.getContext();
        ActionInvocation arg0 = ctx.getActionInvocation();
        if (arg0 == null) {
            return "";
        } else {
            String url = arg0.getProxy().getConfig().getPackageName();
            if (url != null && !url.trim().equals("")) {
                url = url + "/" + arg0.getProxy().getActionName();
            } else {
                url = arg0.getInvocationContext().getName();
            }

            return url;
        }
    }

    public static String getRequstActionName() {
        ActionContext ctx = ActionContext.getContext();
        return ctx == null ? "" : ctx.getName();
    }

    public static String getRequstFuncPointCode() {
        ActionContext ctx = ActionContext.getContext();
        ActionInvocation arg0 = ctx.getActionInvocation();
        if (arg0 == null) {
            return "";
        } else {
            String url = arg0.getProxy().getConfig().getPackageName();
            if (url != null && !url.trim().equals("")) {
                url = url + "/" + arg0.getProxy().getActionName();
            } else {
                url = arg0.getInvocationContext().getName();
            }

            return url;
        }
    }

    public static String getRequestUrl() {
        return getHttpRequest().getRequestURL().toString();
    }

    public static String getBasePath() {
        HttpServletRequest request = getHttpRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }
}
