package com.hnjz.webbase.webwork.action;

import com.hnjz.core.model.IUser;
import com.hnjz.util.Ajax;
import com.hnjz.util.Messages;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.webwork.WebworkUtil;
import com.hnjz.webbase.webwork.action.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class UserAction extends AbstractAction {
    private static Log logger = LogFactory.getLog(com.hnjz.webbase.webwork.action.UserAction.class);
    public IUser sUser = null;

    public UserAction() {
    }

    public String go() {
        this.sUser = WebBaseUtil.getCurrentUser();
        if (this.sUser == null) {
            String msg = Messages.getString("systemMsg.sessionInvalid");
            this.setMessage(msg);
            this.result = Ajax.xmlResult(3, msg);
            logger.error(WebworkUtil.getRemoteAddress() + " -- " + this.result);
            return "error";
        } else {
            return this.userGo();
        }
    }

    protected abstract String userGo();

    public IUser getSUser() {
        return this.sUser;
    }

    public void setSUser(IUser user) {
        this.sUser = user;
    }
}
