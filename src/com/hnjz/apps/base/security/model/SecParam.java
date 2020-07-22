package com.hnjz.apps.base.security.model;

public class SecParam implements java.io.Serializable{

	private Integer paramId;
	private String paramName;
	private String paramValue;
	private String paramMemo;
	
	public SecParam() {
	}

	public Integer getParamId() {
		return paramId;
	}

	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamMemo() {
		return paramMemo;
	}

	public void setParamMemo(String paramMemo) {
		this.paramMemo = paramMemo;
	}
}
