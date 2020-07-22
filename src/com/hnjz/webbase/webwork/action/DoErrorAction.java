package com.hnjz.webbase.webwork.action;

import com.hnjz.webbase.webwork.action.AbstractAction;

public class DoErrorAction extends AbstractAction {
    private Integer id;
    private String funName;

    public DoErrorAction() {
    }

    public String go() {
        return "success";
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFunName() {
        return this.funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }
}
