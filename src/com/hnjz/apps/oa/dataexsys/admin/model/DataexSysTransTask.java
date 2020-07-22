package com.hnjz.apps.oa.dataexsys.admin.model;

import com.hnjz.db.query.QueryCache;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;

import java.util.Date;


public class DataexSysTransTask  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String uuid;
	private String transId;
	private String identityId;
	private String contentId;
	private String packetUrl;
	private String serverId;
	private String packType;
	private String sendOrg;
	private String sendOrgName;
	private String sendOrgDesc;
	private String targetOrg;
	private String targetOrgName;
	private String targetOrgDesc;
	private Date startTime;
	private Date endTime;
	private Integer resendTimes;
	private String sendStatus;
	private String receiptStatus;
	
	private String startNum;
	private String endNum;
	
	
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
	private Identity receiver = null;
	public Identity getReceiver(){
		if(receiver==null){
			receiver = new Identity();
			receiver.setIdentityFlag(targetOrg);
			receiver.setIdentityName(targetOrgName);
			receiver.setIdentityDesc(targetOrgDesc);
		}
		return receiver;
	}
	private DataexSysTransContent content;
	public DataexSysTransContent getContent(){
		if(content==null){
			content = QueryCache.get(DataexSysTransContent.class, contentId);
		}
		return content;
	}
	
	
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

	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getPacketUrl() {
		return packetUrl;
	}
	public void setPacketUrl(String packetUrl) {
		this.packetUrl = packetUrl;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public String getSendOrg() {
		return sendOrg;
	}
	public void setSendOrg(String sendOrg) {
		this.sendOrg = sendOrg;
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
	public String getTargetOrg() {
		return targetOrg;
	}
	public void setTargetOrg(String targetOrg) {
		this.targetOrg = targetOrg;
	}
	public String getTargetOrgName() {
		return targetOrgName;
	}
	public void setTargetOrgName(String targetOrgName) {
		this.targetOrgName = targetOrgName;
	}
	public String getTargetOrgDesc() {
		return targetOrgDesc;
	}
	public void setTargetOrgDesc(String targetOrgDesc) {
		this.targetOrgDesc = targetOrgDesc;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getResendTimes() {
		return resendTimes;
	}
	public void setResendTimes(Integer resendTimes) {
		this.resendTimes = resendTimes;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getStartNum() {
		return startNum;
	}
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}

}
