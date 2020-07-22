package com.hnjz.webbase.webwork.interceptors;

import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.model.DefaultLog;
import com.hnjz.core.model.ILog;
import com.hnjz.core.model.IUser;
import com.hnjz.core.plugins.base.ILogProvider;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.FunctionManager;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.webwork.WebworkUtil;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CssLogInterceptor extends AroundInterceptor {
    private static final Log _log = LogFactory.getLog(com.hnjz.webbase.webwork.interceptors.CssLogInterceptor.class);
    private static final String logDebug = ConfigurationManager.getConfigurationManager().getSysConfigure("app.logger.debug", "false");
    private static final List<String> excludesAuditActions = StringHelper.strToList(ConfigurationManager.getConfigurationManager().getSysConfigure("app.audit.excludes", ""));

    public CssLogInterceptor() {
    }

    protected void after(ActionInvocation arg0, String arg1) throws Exception {
        FunctionManager mgr = FunctionManager.getFunctionManager();
        if (WebworkUtil.getHttpRequest() != null) {
            DefaultLog log = (DefaultLog)WebworkUtil.getHttpRequest().getAttribute(ILog.class.getName());
            if (log != null) {
                log.setLogId(UuidUtil.getUuid());
                IUser user = WebBaseUtil.getCurrentUser();
                if (user != null) {
                    log.setOpId(String.valueOf(user.getUserId()));
                    log.setOpName(user.getRealName());
                    log.setOperatorType(user.getType());
                }

                String packageName = arg0.getProxy().getConfig().getPackageName();
                String actionName = arg0.getProxy().getActionName();
                if (!mgr.transform(packageName, actionName, log)) {
                    log.setFuncId(arg0.getProxy().getActionName());
                    _log.warn("未能实现日志对象转换，可能Action类[" + arg0.getAction().getClass().getName() + "]未用注解来设置相关功能点内容.");
                } else {
                    log.setFuncId(packageName + "/" + actionName);
                }

                if ("true".equals(logDebug)) {
                    Map<?, ?> reqMap = ActionContext.getContext().getParameters();
                    String strLog = log.getLogData();
                    if (StringHelper.isEmpty(strLog)) {
                        strLog = "";
                    } else {
                        strLog = strLog + ",";
                    }

                    StringBuffer reqBuf = (new StringBuffer(strLog)).append("[").append(packageName).append(",").append(actionName).append("] => ").append(arg1).append(":{");
                    Iterator var12 = reqMap.entrySet().iterator();

                    while(var12.hasNext()) {
                        Map.Entry<?, ?> entry = (Map.Entry)var12.next();
                        reqBuf.append(entry.getKey()).append(":").append(this.formatValue(entry.getValue())).append(",");
                    }

                    if (reqBuf.charAt(reqBuf.length() - 1) == ',') {
                        reqBuf.setCharAt(reqBuf.length() - 1, '}');
                    } else {
                        reqBuf.append('}');
                    }

                    log.setLogData(reqBuf.toString());
                }

                log.setResult(arg0.getResultCode());
                ILogProvider logger = WebBaseUtil.getAppLogProvider();
                if (logger == null) {
                    _log.warn("系统未设置日志记录器或尚未启动日志记录功能！");
                    return;
                }

                InetAddress addr = InetAddress.getLocalHost();
                log.setServerIp(addr.getHostAddress());
                log.setServerName(addr.getHostName());
                long ts = Calendar.getInstance().getTime().getTime() - log.getOpTime().getTime();
                _log.debug("本次调用" + log.getFuncId() + "耗时(" + ts + ") ms.");
                log.setDurationTime(ts);
                if (StringHelper.isEmpty(log.getResult())) {
                    log.setResult("");
                }

                logger.log(log);
            }

        }
    }

    protected void before(ActionInvocation arg0) throws Exception {
        String packageName = arg0.getProxy().getConfig().getPackageName();
        String actionName = arg0.getProxy().getActionName();
        String funcCode = packageName + "/" + actionName;
        if (!excludesAuditActions.contains(funcCode)) {
            DefaultLog log = new DefaultLog();
            log.setOpTime(Calendar.getInstance().getTime());
            log.setOpIp(WebworkUtil.getRemoteAddress());
            if (WebworkUtil.getHttpRequest() != null) {
                WebworkUtil.getHttpRequest().setAttribute(ILog.class.getName(), log);
            }

        }
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "''";
        } else if (!value.getClass().isArray()) {
            return "'" + value.toString() + "'";
        } else {
            StringBuffer buf = new StringBuffer("[");
            Object[] objs = (Object[])value;
            if (objs.length == 1) {
                return objs[0].toString();
            } else {
                Object[] var7 = objs;
                int var6 = objs.length;

                for(int var5 = 0; var5 < var6; ++var5) {
                    Object va = var7[var5];
                    buf.append("'").append(va).append("',");
                }

                if (buf.charAt(buf.length() - 1) == ',') {
                    buf.setCharAt(buf.length() - 1, ']');
                } else {
                    buf.append(']');
                }

                return buf.toString();
            }
        }
    }

    protected String getLogType(String packageName, String actionName, String sysId) {
        if (!"loginAjax".equals(actionName) && !"quit".equals(actionName)) {
            if (!"admin".equals(packageName) && !"func".equals(packageName)) {
                return !"1".equals(sysId) && !"2".equals(sysId) ? "4" : "3";
            } else {
                return "2";
            }
        } else {
            return "1";
        }
    }
}