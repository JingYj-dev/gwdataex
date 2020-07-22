package com.hnjz.webbase.webwork.action;

import com.hnjz.util.Ajax;
import com.hnjz.util.Messages;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.webwork.WebworkUtil;
import com.hnjz.webbase.webwork.action.UserAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AdminAction extends UserAction {
    private static final long serialVersionUID = 1L;
    private static Log logger = LogFactory.getLog(com.hnjz.webbase.webwork.action.AdminAction.class);
    public String funcid;

    public AdminAction() {
    }

    public String userGo() {
        String funcPointCode = this.getActionFuncCode();
        if (StringHelper.isEmptyByTrim(funcPointCode)) {
            funcPointCode = WebworkUtil.getRequstFuncPointCode();
        }

        if (!WebBaseUtil.hasPrivilege(this.sUser, funcPointCode)) {
            String msg = Messages.getString("systemMsg.authError");
            this.setMessage(msg);
            this.result = Ajax.JSONResult(4, msg);
            logger.error(funcPointCode + "    " + this.sUser.getUserId() + "," + this.sUser.getLoginName() + "," + WebworkUtil.getRemoteAddress() + "," + this.result);
            return "error";
        } else {
            return this.adminGo();
        }
    }

    protected abstract String adminGo();

    protected String getActionFuncCode() {
        return null;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFuncid() {
        return this.funcid;
    }

    public void setFuncid(String funcid) {
        this.funcid = funcid;
    }
}
