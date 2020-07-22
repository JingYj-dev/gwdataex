package com.hnjz.webbase.webwork.action;

import com.hnjz.webbase.webwork.action.UserAction;

public class DefaultForwardUserAction extends UserAction {
    public DefaultForwardUserAction() {
    }

    protected String userGo() {
        return "success";
    }
}
