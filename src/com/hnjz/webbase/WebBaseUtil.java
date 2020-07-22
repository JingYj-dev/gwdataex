package com.hnjz.webbase;

import com.hnjz.common.PluginBus;
import com.hnjz.core.PlatformBaseUtil;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.model.DefaultLog;
import com.hnjz.core.model.IUser;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.plugins.privilege.IPrivilegeChecker;
import com.hnjz.webbase.webwork.WebworkUtil;
import com.opensymphony.xwork.ActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WebBaseUtil extends PlatformBaseUtil {
    private static boolean isSecured = ConfigurationManager.isSecured();
    private static Set<String> excludesActions = null;
    private static final Log log = LogFactory.getLog(com.hnjz.webbase.WebBaseUtil.class);

    public WebBaseUtil() {
    }

    public static IUser getCurrentUser() {
        ActionContext ctx = ActionContext.getContext();
        IUser userData = null;

        try {
            userData = (IUser)ctx.getSession().get("sUser");
        } catch (Exception var3) {
            ;
        }

        return userData;
    }

    public static Set<String> getUserFuncActions() {
        IUser dv = getCurrentUser();
        return (Set)(dv == null ? new HashSet() : dv.getFuncActions());
    }

    public static Set<String> getExcludesActions() {
        if (excludesActions == null) {
            String excludes = ConfigurationManager.getConfigurationManager().getSysConfigure("app.popedom.excludes", "");
            excludesActions = new HashSet(Arrays.asList(excludes.split(",")));
        }

        return excludesActions;
    }

    public static boolean hasPrivilege(IUser user, String actionCode) {
        IPrivilegeChecker checker = (IPrivilegeChecker)PluginBus.getPlugin(IPrivilegeChecker.class);
        if (checker != null) {
            try {
                checker.checkPrivilege(user, actionCode);
                return true;
            } catch (Exception var4) {
                log.info(var4.getMessage());
                return false;
            }
        } else if (!isSecured) {
            return true;
        } else if (!StringHelper.isEmpty(actionCode) && !getExcludesActions().contains(actionCode)) {
            Set<String> actionPoints = user.getFuncActions();
            if (actionPoints != null && actionPoints.contains(actionCode)) {
                return true;
            } else {
                actionPoints = user.getFunctions();
                return actionPoints != null && actionPoints.contains(actionCode);
            }
        } else {
            return true;
        }
    }

    public static boolean hasUserPrivilege(String actionCode) {
        if (!isSecured) {
            return true;
        } else {
            IUser user = getCurrentUser();
            return user == null ? false : hasPrivilege(user, actionCode);
        }
    }

    public static void setLogData(String data) {
        DefaultLog log = (DefaultLog)WebworkUtil.getHttpRequest().getAttribute(DefaultLog.class.getName());
        if (log != null) {
            log.setLogData(data);
        }

    }
}
