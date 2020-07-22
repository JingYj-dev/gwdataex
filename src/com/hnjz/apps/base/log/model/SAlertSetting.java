package com.hnjz.apps.base.log.model;

public class SAlertSetting implements java.io.Serializable {

	private String setId;
	private String eventType;//事件类型
	private String severLevel;//严重程度
	private String alertType;//预警方式
	
	public SAlertSetting() {
		super();
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getSeverLevel() {
		return severLevel;
	}

	public void setSeverLevel(String severLevel) {
		this.severLevel = severLevel;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	
}