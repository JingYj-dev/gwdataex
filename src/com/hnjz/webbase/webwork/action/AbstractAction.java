package com.hnjz.webbase.webwork.action;

import com.hnjz.common.PluginBus;
import com.hnjz.core.model.ILog;
import com.hnjz.core.plugins.base.ILicenser;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.WebworkUtil;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class AbstractAction extends ActionSupport {
    private static final Log log = LogFactory.getLog(com.hnjz.webbase.webwork.action.AbstractAction.class);
    private String message;
    private String actionName;
    protected String result;

    public AbstractAction() {
    }

    public String execute() throws Exception {
        //老的方法execute123; 改造去掉licenser验证
        try {
            if (this.hasErrors()) {
                log.debug("action not executed, field or action errors");
                log.debug("Field errors: " + this.getFieldErrors());
                log.debug("Action errors: " + this.getActionErrors());
                return "input";
            } else {
                this.actionName = WebworkUtil.getRequstActionName() + ".action";
                if (log.isDebugEnabled()) {
                    log.debug("executing action " + this.actionName + " : " + this);
                }

                String sRet = this.go();
                return sRet;
            }
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return "error";
        }
    }

    public String execute123() throws Exception {
        try {
            ILicenser licen = (ILicenser) PluginBus.getPlugin(ILicenser.class);
            if (licen != null && licen.validate()) {
                if (this.hasErrors()) {
                    log.debug("action not executed, field or action errors");
                    log.debug("Field errors: " + this.getFieldErrors());
                    log.debug("Action errors: " + this.getActionErrors());
                    return "input";
                } else {
                    this.actionName = WebworkUtil.getRequstActionName() + ".action";
                    if (log.isDebugEnabled()) {
                        log.debug("executing action " + this.actionName + " : " + this);
                    }

                    String sRet = this.go();
                    return sRet;
                }
            } else {
                this.setMessage("Invalid license!");
                this.result = Ajax.JSONResult(1, "Invalid license!");
                return "error";
            }
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return "error";
        }
    }

    protected abstract String go();

    /**
     * @deprecated
     */
    @Deprecated
    protected Object appGet(String name) {
        return ActionContext.getContext().getApplication().get(name);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected void appSet(String name, Object value) {
        ActionContext.getContext().getApplication().put(name, value);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected void appRemove(Object key) {
        Object obj = ActionContext.getContext().getApplication().get(key);
        if (obj != null) {
            obj = null;
            ActionContext.getContext().getApplication().remove(key);
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    protected Object get(String name) {
        return ActionContext.getContext().getSession().get(name);
    }

    /**
     * 保存用户信息到session
     * @param name
     * @param value
     */
    @Deprecated
    protected void set(String name, Object value) {
        ActionContext.getContext().getSession().put(name, value);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected void remove(Object key) {
        ActionContext.getContext().getSession().remove(key);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
        WebworkUtil.getActionContext().getSession().put("message", message);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getActionName() {
        return this.actionName;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public HttpServletRequest getRequst() {
        return (HttpServletRequest) ActionContext.getContext().get("com.opensymphony.xwork.dispatcher.HttpServletRequest");
    }

    /**
     * @deprecated
     */
    @Deprecated
    public HttpServletResponse getResponse() {
        return (HttpServletResponse) ActionContext.getContext().get("com.opensymphony.xwork.dispatcher.HttpServletResponse");
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected HttpSession getSession() {
        return this.getRequst().getSession();
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ILog getActionLog() {
        return (ILog) WebworkUtil.getHttpRequest().getAttribute(ILog.class.getName());
    }
}
