package com.hnjz.apps.oa.dataexsys.admin.model;


public class DataexSysDirWs {
	
	// Fields
	private String wsId; //交换接口服务配置标识
	private String dirId; //节点标识
	private String wsType; //接口类型：1、握手   2、发送 
	private String dataServiceUrl; //数据服务地址
	private String methodName; //方法名称
	private String memo; //备注

	// Constructors

	/** default constructor */
	public DataexSysDirWs() {
	}

	/** minimal constructor */
	public DataexSysDirWs(String wsId) {
		this.wsId = wsId;
	}
	
	// Property accessors
	
	public String getWsId() {
		return wsId;
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}

	public String getWsType() {
		return wsType;
	}

	public void setWsType(String wsType) {
		this.wsType = wsType;
	}

	public String getDataServiceUrl() {
		return dataServiceUrl;
	}

	public void setDataServiceUrl(String dataServiceUrl) {
		this.dataServiceUrl = dataServiceUrl;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
