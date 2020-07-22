package com.hnjz.apps.base.log.model;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.util.HtmlConverter;
import com.hnjz.util.StringHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SLogExt implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String className = SLogExt.class.getName();
	private String logId; //主键
	private String sysId; //系统ID
	private String funcId; //功能ID
	private String logLevel; //日志级别
	private String logType; //日志类型:业务 管理 安全
	private String logData; //日志内容
	private String opId; //操作人ID
	private String opName; //操作人姓名
	private String opType; //操作类型
	private Date opTime; //操作时间
	private String opIp; //操作人IP
	private int durationTime; //持续时间
	private String serverIp; //服务器IP
	private String serverName; //服务器名字
	private String opResult; //操作结果
	
	public String getLogExtInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(HtmlConverter.appendHtmlNameField("主键", this.getLogId()));
		sb.append(HtmlConverter.appendHtmlNameField("系统ID", this.getSysId()));
		sb.append(HtmlConverter.appendHtmlNameField("功能ID", this.getFuncId()));
		sb.append(HtmlConverter.appendHtmlNameField("日志级别", StringHelper.isNotEmpty(this.getLogLevel()) ? DictMan.getDictType("d_loglevel", this.getLogLevel()).getName() : ""));
		sb.append(HtmlConverter.appendHtmlNameField("日志类型", StringHelper.isNotEmpty(this.getLogType()) ? DictMan.getDictType("d_logtype", this.getLogType()).getName() : ""));
		sb.append(HtmlConverter.appendHtmlNameField("日志内容", this.getLogData()));
		sb.append(HtmlConverter.appendHtmlNameField("操作人ID", this.getOpId()));
		sb.append(HtmlConverter.appendHtmlNameField("操作人姓名", this.getOpName()));
		sb.append(HtmlConverter.appendHtmlNameField("操作类型", StringHelper.isNotEmpty(this.getOpType()) ? DictMan.getDictType("d_opertype", this.getOpType()).getName() : ""));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(HtmlConverter.appendHtmlNameField("操作时间", this.getOpTime() != null ? sdf.format(this.getOpTime()) : ""));
		sb.append(HtmlConverter.appendHtmlNameField("操作人IP", this.getOpIp()));
		sb.append(HtmlConverter.appendHtmlNameField("持续时间", this.getDurationTime()));
		sb.append(HtmlConverter.appendHtmlNameField("服务器IP", this.getServerIp()));
		sb.append(HtmlConverter.appendHtmlNameField("服务器名字", this.getServerName()));
		sb.append(HtmlConverter.appendHtmlNameField("操作结果", this.getOpResult()));
		
		return sb.toString();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getLogData() {
		return logData;
	}

	public void setLogData(String logData) {
		this.logData = logData;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getOpIp() {
		return opIp;
	}

	public void setOpIp(String opIp) {
		this.opIp = opIp;
	}

	public int getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getOpResult() {
		return opResult;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}
	
	
	
}
