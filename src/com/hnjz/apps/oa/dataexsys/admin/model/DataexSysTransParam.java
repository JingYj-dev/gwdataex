package com.hnjz.apps.oa.dataexsys.admin.model;

public class DataexSysTransParam implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer paramId;
	private String paramName;
	private String paramValue;
	private String paramMemo;
	
	public DataexSysTransParam() {
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
