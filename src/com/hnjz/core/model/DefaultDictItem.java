package com.hnjz.core.model;

import com.hnjz.core.model.IDictable;

import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:51
 */
public class DefaultDictItem implements com.hnjz.core.model.IDictable {
    private String itemCode;
    private int intCode;
    private String itemName;
    private String code;
    private String remark;

    public DefaultDictItem() {
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIntCode() {
        return this.intCode;
    }

    public void setIntCode(int incCode) {
        this.intCode = incCode;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{itemCode='").append(this.itemCode).append("',itemName='").append(this.itemName).append("',code='").append(this.code).append("',remark='").append(this.remark).append("'}");
        return buf.toString();
    }

    public String objectId() {
        return this.code + "@" + this.itemCode;
    }

    public String toJson() {
        StringBuffer buf = new StringBuffer();
        buf.append("{\"itemCode\":\"").append(this.itemCode).append("\",\"itemName\":\"").append(this.itemName).append("\",\"code\":\"").append(this.code).append("\",\"remark\":\"").append(this.remark).append("\"}");
        return buf.toString();
    }

    public List<IDictable> getChildren() {
        return null;
    }
}
