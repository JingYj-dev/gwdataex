package com.hnjz.apps.oa.dataexsys.admin.model;

import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;

import java.util.Date;

public class DataexSysTransContent {
	
	// Fields
	private String uuid; //内容ID
	private String transId; //流水号
	private String docId; //公文ID
	private String sendOrg; //发送机关标识
	private String sendOrgName; //发送机关名称
	private String sendOrgDesc; //发送机关描述
	private String packType; //报文类型
	private String docTitle; //公文标题
	private String docSecurity; //密级
	private String docEmergency; //紧急程度
	private Date recvTime; //接收时间
	private String docAddress; //内容地址
	private String serverId; //位置ID
	private String remark; //备注
	
	private String clientIp;//发送系统IP
	private Date recvStartTime;//接收开始时间
	private Date sendTime; //发送完成时间
	private Integer packSize; //报文大小
	private String transStatus;

	private String expansion;
	private String receivOrgName;


	private Identity sender = null;
	public Identity getSender(){
		if(sender==null){
			sender = new Identity();
			sender.setIdentityFlag(sendOrg);
			sender.setIdentityName(sendOrgName);
			sender.setIdentityDesc(sendOrgDesc);
		}
		return sender;
	}


	// Constructors

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** default constructor */
	public DataexSysTransContent() {
	}

	/** minimal constructor */
	public DataexSysTransContent(String uuid) {
		this.uuid = uuid;
	}

	// Property accessors

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getSendOrg() {
		return sendOrg;
	}

	public void setSendOrg(String sendOrg) {
		this.sendOrg = sendOrg;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public String getDocSecurity() {
		return docSecurity;
	}

	public void setDocSecurity(String docSecurity) {
		this.docSecurity = docSecurity;
	}

	public String getDocEmergency() {
		return docEmergency;
	}

	public void setDocEmergency(String docEmergency) {
		this.docEmergency = docEmergency;
	}

	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public String getDocAddress() {
		return docAddress;
	}

	public void setDocAddress(String docAddress) {
		this.docAddress = docAddress;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getSendOrgName() {
		return sendOrgName;
	}

	public void setSendOrgName(String sendOrgName) {
		this.sendOrgName = sendOrgName;
	}


	public String getSendOrgDesc() {
		return sendOrgDesc;
	}

	public void setSendOrgDesc(String sendOrgDesc) {
		this.sendOrgDesc = sendOrgDesc;
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

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getExpansion() {
		return expansion;
	}

	public void setExpansion(String expansion) {
		this.expansion = expansion;
	}


	public String getReceivOrgName() {
		return receivOrgName;
	}


	public void setReceivOrgName(String receivOrgName) {
		this.receivOrgName = receivOrgName;
	}
}
