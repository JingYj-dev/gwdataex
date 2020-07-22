package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTrans;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirDataexInbox.java creation date: [Jun 30, 2014 10:00:27 PM] by mazhh
 * http://www.css.com.cn
 */

public class DirDataexSysTrans extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirDataexSysTrans.class);
	
	private String uuid; //流水号
	//private String sendSysId; //发送系统标识
	private String sendSysType; //发送系统类型
	//private String packType; //报文类型
	//private String packSize; //报文大小
	//private String serverId; //位置ID
	//private String docUrl; //报文内容URL
	private String transStatus; //交易状态
	private Date beginRecvTime; //开始接收时间
	private Date endRecvTime; //结束接收时间
	private Date beginSendTime; //开始发送时间
	private Date endSendTime; //结束发送时间
	private String sourceType; //来源类型
	private String docTitle; //公文标题
	
	private Page page;
	
	public DirDataexSysTrans(){
		page = new Page();
		page.setCountField("a.uuid");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo() {
		try{
			trimStr();
			StringBuffer sb = new StringBuffer(" select a.uuid from DataexSysTrans a " + getWhere() + getOrder());
			QueryCache qc = new QueryCache(sb.toString());
			setWhere(qc);
			page = qc.page(page);
			List<DataexSysTrans> listDataexSysTrans = QueryCache.idToObj(DataexSysTrans.class, page.getResults());
			page.setResults(listDataexSysTrans);
        	return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1 = 1 ");
		if (StringHelper.isNotEmpty(sendSysType)) {
			sb.append(" and a.sendSysType =:sendSysType ");
		}
		if (StringHelper.isNotEmpty(transStatus)) {
			sb.append(" and a.transStatus =:transStatus ");
		}
		if(beginRecvTime != null) {
			sb.append(" and a.recvTime >=:beginRecvTime ");
		}
		if(endRecvTime != null) {
			sb.append(" and a.recvTime <:endRecvTime ");
		}
		if(beginSendTime != null) {
			sb.append(" and a.sendTime >=:beginSendTime ");
		}
		if(endSendTime != null) {
			sb.append(" and a.sendTime <:endSendTime ");
		}
		if(StringHelper.isNotEmpty(sourceType)) {
			sb.append(" and a.sourceType =:sourceType ");
		}
		if(StringHelper.isNotEmpty(sourceType)) {
			sb.append(" and a.sourceType =:sourceType ");
		}
		if(StringHelper.isNotEmpty(docTitle)) {
			sb.append(" and a.uuid in (:transIdList) ");
		}
		return sb.toString();
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by a.recvTime desc";
	}
	public void setWhere(QueryCache qc) throws ParseException {
		if(StringHelper.isNotEmpty(sendSysType)) {
			qc.setParameter("sendSysType", sendSysType);
		}
		if (StringHelper.isNotEmpty(transStatus)) {
			qc.setParameter("transStatus", transStatus);
		}
		if(beginRecvTime != null){
			qc.setParameter("beginRecvTime", beginRecvTime);
		}
		if(endRecvTime != null){
			qc.setParameter("endRecvTime", DateUtil.addDate(endRecvTime, 1));
		}
		if(beginSendTime != null){
			qc.setParameter("beginSendTime", beginSendTime);
		}
		if(endSendTime != null){
			qc.setParameter("endSendTime", DateUtil.addDate(endSendTime, 1));
		}
		if(StringHelper.isNotEmpty(sourceType)) {
			qc.setParameter("sourceType", sourceType);
		}
		if(StringHelper.isNotEmpty(docTitle)) {
			qc.setParameter("transIdList", getTransIdListByDocTitle(docTitle));
		}
	}
	/**
	 * 根据公文标题在内容表内得到多个流水ID
	 * @param docTitle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTransIdListByDocTitle(String docTitle) {
		List<String> transIdList = null;
		QueryCache qc = new QueryCache("select a.transId from DataexSysTransContent a where a.docTitle like :docTitle ").setParameter("docTitle", "%" + docTitle + "%");
		transIdList = qc.list();
		return transIdList;

	}
	private void trimStr() {
		docTitle = StringHelper.isNotEmpty(docTitle) ? docTitle.trim() : "";
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSendSysType() {
		return sendSysType;
	}

	public void setSendSysType(String sendSysType) {
		this.sendSysType = sendSysType;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public Date getBeginRecvTime() {
		return beginRecvTime;
	}

	public void setBeginRecvTime(Date beginRecvTime) {
		this.beginRecvTime = beginRecvTime;
	}

	public Date getEndRecvTime() {
		return endRecvTime;
	}

	public void setEndRecvTime(Date endRecvTime) {
		this.endRecvTime = endRecvTime;
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

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
}
