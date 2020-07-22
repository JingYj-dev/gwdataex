package com.hnjz.apps.oa.accept.model;

import java.util.Date;


public class DataexSysReceipt  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String uuid;
	private String sendId;
	private String contentId;
	private String sendOrg;
	private String sendOrgName;
	private String targetOrg;
	private String targetOrgName;
	private String receiptContent;
	private String receiptMemo;
	private Date receiveTime;
	private String senderId;
	private String senderName;
	private String sendStatus;

    // Constructors

    /** default constructor */
    public DataexSysReceipt() {
    }

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	public String getReceiptMemo() {
		return receiptMemo;
	}

	public void setReceiptMemo(String receiptMemo) {
		this.receiptMemo = receiptMemo;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	
}
