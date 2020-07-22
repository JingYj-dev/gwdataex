package com.hnjz.apps.base.log.model;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.util.HtmlConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class SLog extends LogPart implements java.io.Serializable{
	//日志ID
	private String logId;
	//系统ID
	private String sysid ="";
	//功能ID
	private String  funcId = "";
	//操作人ID
	private String opId = "";
	//操作人姓名
	private String opName = "";
	//操作时间
	private Date opTime;
	//持续时间
	private long durationTime;
	//模块名称
	private String packageName;
 
	//操作人类型
	private String operatorType;
	//服务器IP
	private String serverIp;
	//服务器主机名
	private String serverName;
	//事件类型
	private Integer eventType = 0;
	//操作人
	private String opIp; 
 
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
 
	public long getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime;
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
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
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

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
 
	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	} 
	public String getLogInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(HtmlConverter.appendHtmlNameField("主键", this.getLogId()));
		sb.append(HtmlConverter.appendHtmlNameField("操作人ID", this.getOpId()));
		sb.append(HtmlConverter.appendHtmlNameField("操作人姓名", this.getOpName()));
		sb.append(HtmlConverter.appendHtmlNameField("操作人IP", this.getOpIp()));
		sb.append(HtmlConverter.appendHtmlNameField("操作类型",  this.getOpType()!=null ? DictMan.getDictType("d_opertype", String.valueOf(getOpType())).getName() : ""));
		sb.append(HtmlConverter.appendHtmlNameField("日志内容", this.getLogData()));
		sb.append(HtmlConverter.appendHtmlNameField("日志级别",  this.getLogLevel()!=null ? DictMan.getDictType("d_loglevel", String.valueOf(getLogLevel())).getName() : ""));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(HtmlConverter.appendHtmlNameField("记录日期", this.getOpTime() != null ? sdf.format(this.getOpTime()) : ""));
		sb.append(HtmlConverter.appendHtmlNameField("功能ID", this.getFuncId()));
		return sb.toString();
	}
	 
}
