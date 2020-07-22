package com.hnjz.core.model;

import com.hnjz.util.StringHelper;

import java.io.Serializable;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:51
 */
public class DefaultActionLog implements Serializable {
    private String systemId;
    private String actionId;
    private String packageName;
    private String actionName;
    private String actionDesc;
    private String type;
    private String eventType;
    private String url;
    private String status;

    public DefaultActionLog() {
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionDesc() {
        return this.actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getActionId() {
        if (StringHelper.isEmpty(this.actionId)) {
            if (StringHelper.isEmpty(this.packageName)) {
                this.actionId = this.actionName;
            } else {
                this.actionId = this.packageName + "/" + this.actionName;
            }
        }

        return this.actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
