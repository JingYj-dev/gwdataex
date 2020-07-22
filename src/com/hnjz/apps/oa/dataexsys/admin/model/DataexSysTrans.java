package com.hnjz.apps.oa.dataexsys.admin.model;

import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;

import java.util.Date;

public class DataexSysTrans {
	
	// Fields
	private String uuid; //流水号
	private String sendSysId; //发送系统标识
	private String sendSysName; //发送系统标识
	private String sendSysDesc; //发送系统标识
	private String sendSysType; //发送系统类型
	private String packType; //报文类型
	private Integer packSize; //报文大小
	private String serverId; //位置ID
	private String docUrl; //报文内容URL
	private String transStatus; //交易状态
	private Date recvTime; //接收时间
	private Date sendTime; //发送完成时间
	private String sourceType; //来源类型
	private String clientIp;
	private Date recvStartTime;


	private Identity sender = null;
	public Identity getSender(){
		if(sender==null){
			sender = new Identity();
			sender.setIdentityFlag(sendSysId);
			sender.setIdentityName(sendSysName);
			sender.setIdentityDesc(sendSysDesc);
		}
		return sender;
	}

	// Constructors

	/** default constructor */
	public DataexSysTrans() {
	}

	/** minimal constructor */
	public DataexSysTrans(String uuid) {
		this.uuid = uuid;
	}

	// Property accessors

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSendSysId() {
		return sendSysId;
	}

	public void setSendSysId(String sendSysId) {
		this.sendSysId = sendSysId;
	}

	public String getSendSysType() {
		return sendSysType;
	}

	public void setSendSysType(String sendSysType) {
		this.sendSysType = sendSysType;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Date getRecvStartTime() {
		return recvStartTime;
	}

	public void setRecvStartTime(Date recvStartTime) {
		this.recvStartTime = recvStartTime;
	}

	public String getSendSysName() {
		return sendSysName;
	}

	public void setSendSysName(String sendSysName) {
		this.sendSysName = sendSysName;
	}

	public String getSendSysDesc() {
		return sendSysDesc;
	}

	public void setSendSysDesc(String sendSysDesc) {
		this.sendSysDesc = sendSysDesc;
	}
}
