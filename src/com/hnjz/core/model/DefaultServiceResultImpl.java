package com.hnjz.core.model;

import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:50
 */
public class DefaultServiceResultImpl<T> implements IServiceResult<T> {
    private int resultCode;
    private String resultDesc;
    private T resultObject;

    public DefaultServiceResultImpl() {
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return this.resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public T getResultObject() {
        return this.resultObject;
    }

    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }

    public String toActionResult() {
        if (this.resultCode != 2 && this.resultCode != 4 && this.resultCode != 9) {
            return this.resultCode == 3 ? "login" : "success";
        } else {
            return "error";
        }
    }

    public String toJson() {
        return Ajax.JSONResult(this.resultCode, this.resultDesc, this.resultObject);
    }
}
