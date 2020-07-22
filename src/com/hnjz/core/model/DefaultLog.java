package com.hnjz.core.model;

import com.hnjz.core.model.ILog;

import java.util.Date;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:50
 */
public class DefaultLog implements ILog {
    private String logId;
    private String sysid = "0";
    private String funcId = "";
    private Integer logLevel = 0;
    private Integer opType = 0;
    private String logData = "";
    private String opId = "";
    private String opName = "";
    private Date opTime;
    private long durationTime;
    private String packageName;
    private String result;
    private String operatorType;
    private String serverIp;
    private String serverName;
    private Integer eventType = 0;
    private String opIp;

    public Date getOpTime() {
        return this.opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public long getDurationTime() {
        return this.durationTime;
    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public DefaultLog() {
    }

    public DefaultLog(String logId) {
        this.logId = logId;
    }

    public String getLogId() {
        return this.logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getSysid() {
        return this.sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public Integer getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Integer getOpType() {
        return this.opType;
    }

    public void setOpType(Integer logType) {
        this.opType = logType;
    }

    public String getLogData() {
        return this.logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public String getOpId() {
        return this.opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getOpName() {
        return this.opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getOpIp() {
        return this.opIp;
    }

    public void setOpIp(String opIp) {
        this.opIp = opIp;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getOperatorType() {
        return this.operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getEventType() {
        return this.eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }
}
