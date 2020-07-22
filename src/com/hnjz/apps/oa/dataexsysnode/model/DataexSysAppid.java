package com.hnjz.apps.oa.dataexsysnode.model;

import java.util.Date;

public class DataexSysAppid implements java.io.Serializable {
	private static final long serialVersionUID = 3826228183283789424L;
	private String uuid;
	private String appidCode;
	private String appidName;
	private Date createdTime;
	private String remark;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getAppidCode() {
		return appidCode;
	}
	public void setAppidCode(String appidCode) {
		this.appidCode = appidCode;
	}
	public String getAppidName() {
		return appidName;
	}
	public void setAppidName(String appidName) {
		this.appidName = appidName;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
