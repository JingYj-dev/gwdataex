package com.hnjz.core.model;

import com.hnjz.core.model.IFunction;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:51
 */
public class DefaultFunc implements IFunction {
    private String funcCode;
    private String funcName;
    private String systemId;
    private Short status;
    private String leafMark;
    private Double showOrder;
    private String parentCode;

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public DefaultFunc() {
    }

    public String getFuncCode() {
        return this.funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Short getStatus() {
        return this.status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getLeafMark() {
        return this.leafMark;
    }

    public void setLeafMark(String leafMark) {
        this.leafMark = leafMark;
    }

    public Double getShowOrder() {
        return this.showOrder;
    }

    public void setShowOrder(Double showOrder) {
        this.showOrder = showOrder;
    }

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String objectId() {
        return this.funcCode;
    }
}
