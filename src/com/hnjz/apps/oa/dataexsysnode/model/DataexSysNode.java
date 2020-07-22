package com.hnjz.apps.oa.dataexsysnode.model;

public class DataexSysNode implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1031389027935555940L;
	private String exnodeId;

	private String exnodeName;

	private String exnodeIp;

	private String exnodeProvider;

	private String exnodeType;

	private String exnodeStatus;

	//private java.lang.String compress;

	/*public java.lang.String getCompress() {
		return this.compress;
	}

	public void setCompress(java.lang.String compress) {
		this.compress = compress;
	}*/

	private String signature;

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	//private java.lang.String encryption;

	/*public java.lang.String getEncryption() {
		return this.encryption;
	}

	public void setEncryption(java.lang.String encryption) {
		this.encryption = encryption;
	}*/

	private Integer serverId;

	public Integer getServerId() {
		return this.serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	private String certPath;

	public String getCertPath() {
		return this.certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	private java.util.Date createdTime;

	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getExnodeName() {
		return exnodeName;
	}

	public void setExnodeName(String exnodeName) {
		this.exnodeName = exnodeName;
	}

	public String getExnodeType() {
		return exnodeType;
	}

	public void setExnodeType(String exnodeType) {
		this.exnodeType = exnodeType;
	}

	public String getExnodeStatus() {
		return exnodeStatus;
	}

	public void setExnodeStatus(String exnodeStatus) {
		this.exnodeStatus = exnodeStatus;
	}

	public String getExnodeId() {
		return exnodeId;
	}

	public void setExnodeId(String exnodeId) {
		this.exnodeId = exnodeId;
	}

	public String getExnodeIp() {
		return exnodeIp;
	}

	public void setExnodeIp(String exnodeIp) {
		this.exnodeIp = exnodeIp;
	}

	public String getExnodeProvider() {
		return exnodeProvider;
	}

	public void setExnodeProvider(String exnodeProvider) {
		this.exnodeProvider = exnodeProvider;
	}

}
