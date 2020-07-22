package com.hnjz.apps.oa.dataexsys.common;

import javax.servlet.ServletRequest;

public class ClientInfo {
	
	private java.util.Date startTime;
	
	private ServletRequest request;

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public ServletRequest getRequest() {
		return request;
	}

	public void setRequest(ServletRequest request) {
		this.request = request;
	}
}
