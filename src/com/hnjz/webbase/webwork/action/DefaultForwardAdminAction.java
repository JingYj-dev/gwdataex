package com.hnjz.webbase.webwork.action;

import com.hnjz.webbase.webwork.action.AdminAction;

public class DefaultForwardAdminAction extends AdminAction {
    public DefaultForwardAdminAction() {
    }

    protected String adminGo() {
        return "success";
    }
}
