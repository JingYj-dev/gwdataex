package com.hnjz.apps.oa.dataexsys.admin.model;

import java.util.Date;


public class DataexSysTransQueue  implements java.io.Serializable {

    // Fields    
	private static final long serialVersionUID = 1L;
	
	private String sendId; //发送ID
	private String contentId; //内容ID
	private String title; //标题
	private String sendOrg; //发文机关
	private String recvOrg; //收文机关
	private String recvOrgName; //收文机关
	private String docSecurity; //密级
	private String docEmergency; //紧急程度
	private Date startTime; //开始时间
	private Date updateTime; //最后更新时间
	private String sendStatus; //发送状态
	private Integer sendTimes; //已发送次数
	private String serverName; //服务器名称
	private String serverIp; //服务器IP
	private String memo; //备注

    // Constructors

    /** default constructor */
    public DataexSysTransQueue() {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSendOrg() {
		return sendOrg;
	}

	public void setSendOrg(String sendOrg) {
		this.sendOrg = sendOrg;
	}

	public String getRecvOrg() {
		return recvOrg;
	}

	public void setRecvOrg(String recvOrg) {
		this.recvOrg = recvOrg;
	}

	public String getDocSecurity() {
		return docSecurity;
	}

	public void setDocSecurity(String docSecurity) {
		this.docSecurity = docSecurity;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Integer getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(Integer sendTimes) {
		this.sendTimes = sendTimes;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDocEmergency() {
		return docEmergency;
	}

	public void setDocEmergency(String docEmergency) {
		this.docEmergency = docEmergency;
	}

	public String getRecvOrgName() {
		return recvOrgName;
	}

	public void setRecvOrgName(String recvOrgName) {
		this.recvOrgName = recvOrgName;
	}

}