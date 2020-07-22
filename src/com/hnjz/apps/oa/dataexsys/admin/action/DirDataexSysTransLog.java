/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirLogAction.java creation date: [Jan 13, 2014 1:54:27 PM] by mazhh
 * http://www.css.com.cn
 */
package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransAccount;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class DirDataexSysTransLog extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	
	/*private String serverId; //服务器ID
	private String sendOrgName; //发送机关
	private String targetOrgName; //接收机关
	private String sendStatus; //发送状态
	private String packType; //报文类型
	private Date beginSendTime;
	private Date endSendTime;
	private String docTitle; //公文标题*/
	private String serverId;	//服务器id
	private Date beginSendTime; 
	private Date endSendTime;
	private String sendStatus; //发送状态
	private String packType; //报文类型
	
	//
	private String transId;
	
	private Page page;
	
	public DirDataexSysTransLog(){
		page = new Page();
		page.setCountField("a.uuid");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo() {
		try{
	//		trimStr(); //过滤空格
			StringBuffer sb = new StringBuffer(" select a.uuid from DataexSysTransAccount a " + getWhere() + getOrder());
			QueryCache qc = new QueryCache(sb.toString());
			setWhere(qc);
			page = qc.page(page);
			List<DataexSysTransAccount> logList = QueryCache.idToObj(DataexSysTransAccount.class, page.getResults());
			page.setResults(logList);
        	return Action.SUCCESS;
		}catch(Exception ex){
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1 = 1 ");
		
		if(StringHelper.isNotEmpty(serverId)) {
			sb.append(" and a.serverId =:serverId ");
		}
		
		if(beginSendTime != null) {
			sb.append(" and a.opTime >=:beginSendTime");
		}
		
		if(endSendTime != null) {
			sb.append(" and a.opTime <:endSendTime");
		}
		if(StringHelper.isNotEmpty(sendStatus)){
			sb.append(" and a.opStatus =:sendStatus ");
		}
		if(StringHelper.isNotEmpty(packType)){
			sb.append(" and a.msgType =:packType");
		}
		if (StringHelper.isNotEmptyByTrim(transId)) {
			sb.append(" and a.transId =:transId");
		}
		/*if (StringHelper.isNotEmpty(sendStatus)) {
			sb.append(" and a.opStatus = :sendStatus ");
		}
		if(StringHelper.isNotEmpty(sendOrgName)) {
			sb.append(" and a.sendOrg in (:sendOrgList) ");
		}
		if(StringHelper.isNotEmpty(targetOrgName)) {
			sb.append(" and a.targetOrg in (:targetOrgIdList) ");
		}
		
		if(StringHelper.isNotEmpty(packType)) {
			sb.append(" and a.packType =:packType ");
		}
		if(StringHelper.isNotEmpty(docTitle)) {
			sb.append(" and a.contentId in (:contentIdList)");
		}*/
		//if(StringHelper.isNotEmpty(opStatus))
		return sb.toString();
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by a.opTime desc";
	}
	public void setWhere(QueryCache qc) throws ParseException {
		
		if (StringHelper.isNotEmpty(serverId)) {
			qc.setParameter("serverId", serverId);
		}
		
		if(beginSendTime != null){
			qc.setParameter("beginSendTime",beginSendTime);
		}
		
		if(endSendTime != null){
			qc.setParameter("endSendTime",DateUtil.addDate(endSendTime, 1));
		}
		
		if (StringHelper.isNotEmpty(sendStatus)) {
			qc.setParameter("sendStatus", sendStatus);
		}
		if(StringHelper.isNotEmpty(packType)) {
			qc.setParameter("packType", packType);
		}
		if (StringHelper.isNotEmptyByTrim(transId)) {
			qc.setParameter("transId", transId);
		}
		/*
		if(StringHelper.isNotEmpty(sendOrgName)) {
			qc.setParameter("sendOrgList", getOrgIdListByName(sendOrgName));
		}
		
		if(StringHelper.isNotEmpty(targetOrgName)) {
			qc.setParameter("targetOrgIdList", getOrgIdListByName(targetOrgName));
		}
		
		if(StringHelper.isNotEmpty(packType)) {
			qc.setParameter("packType", packType);
		}
		if(StringHelper.isNotEmpty(docTitle)) {
			qc.setParameter("contentIdList", getContentIdListByDocTitle(docTitle));
		}*/
	}
	/**
	 * 根据机构名称得到多个机构ID
	 * @param orgName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOrgIdListByName(String orgName) {
		List<String> orgIdList = null;
		QueryCache qc = new QueryCache("select a.uuid from SOrg a where a.name like :orgName ").setParameter("orgName", "%" + orgName + "%");
		orgIdList = qc.list();
		return orgIdList;

	}
	/**
	 * 根据公文标题在内容表内得到多个内容ID
	 * @param docTitle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getContentIdListByDocTitle(String docTitle) {
		List<String> contentIdList = null;
		QueryCache qc = new QueryCache("select a.uuid from DataexSysTransContent a where a.docTitle like :docTitle ").setParameter("docTitle", "%" + docTitle + "%");
		contentIdList = qc.list();
		return contentIdList;

	}
//	private void trimStr() {
//		sendOrgName = StringHelper.isNotEmpty(sendOrgName) ? sendOrgName.trim() : "";
//		targetOrgName = StringHelper.isNotEmpty(targetOrgName) ? targetOrgName.trim() : "";
//		docTitle = StringHelper.isNotEmpty(docTitle) ? docTitle.trim() : "";
//	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	public Date getBeginSendTime() {
		return beginSendTime;
	}

	public void setBeginSendTime(Date beginSendTime) {
		this.beginSendTime = beginSendTime;
	}

	public Date getEndSendTime() {
		return endSendTime;
	}

	public void setEndSendTime(Date endSendTime) {
		this.endSendTime = endSendTime;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
/*
	public String getSendOrgName() {
		return sendOrgName;
	}

	public void setSendOrgName(String sendOrgName) {
		this.sendOrgName = sendOrgName;
	}

	public String getTargetOrgName() {
		return targetOrgName;
	}

	public void setTargetOrgName(String targetOrgName) {
		this.targetOrgName = targetOrgName;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}*/

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
}
