package com.hnjz.apps.oa.dataexsys.admin.model;

public class DataexFzRelation {
	private String uuid;
	private String fzSendFileId;
	private String zrSendFileId;
	private String sendUnitCode;
	private String sendUnitName;
	private String sendUnitRemark;
	private String receiverUnitCode;
	private String docTitle;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFzSendFileId() {
		return fzSendFileId;
	}
	public void setFzSendFileId(String fzSendFileId) {
		this.fzSendFileId = fzSendFileId;
	}
	public String getZrSendFileId() {
		return zrSendFileId;
	}
	public void setZrSendFileId(String zrSendFileId) {
		this.zrSendFileId = zrSendFileId;
	}
	public String getSendUnitCode() {
		return sendUnitCode;
	}
	public void setSendUnitCode(String sendUnitCode) {
		this.sendUnitCode = sendUnitCode;
	}
	public String getSendUnitName() {
		return sendUnitName;
	}
	public void setSendUnitName(String sendUnitName) {
		this.sendUnitName = sendUnitName;
	}
	public String getSendUnitRemark() {
		return sendUnitRemark;
	}
	public void setSendUnitRemark(String sendUnitRemark) {
		this.sendUnitRemark = sendUnitRemark;
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	public String getReceiverUnitCode() {
		return receiverUnitCode;
	}
	public void setReceiverUnitCode(String receiverUnitCode) {
		this.receiverUnitCode = receiverUnitCode;
	}
}
