/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetYgd.java creation date: [2014-6-27 上午9:21:25] by mazhh
 * http://www.css.com.cn
 */
package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDocAttach;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class GetTransContent extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(GetTransContent.class);
	
	private String transId;
	private DataexSysTransContent item;
	private List<DataexSysDocAttach> docAttachmentList;  //附件列表
	
	public GetTransContent(){
		item = new DataexSysTransContent();
	}

	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(transId)) {
				Object id = new QueryCache("select a.uuid from DataexSysTransContent a where a.transId =:transId ")
															.setParameter("transId", transId).setMaxResults(1).uniqueResult();
				item = QueryCache.get(DataexSysTransContent.class, (String) id);
			}
			if (item != null && StringHelper.isNotEmpty(item.getUuid())) {
				docAttachmentList = this.getDocAttachment(item.getUuid());
			}
			return Action.SUCCESS;	 
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}	
	}
	@SuppressWarnings("unchecked")
	public List<DataexSysDocAttach> getDocAttachment(String contentId) throws Exception {
		QueryCache qc = new QueryCache(" select a.uuid from DataexSysDocAttach a where a.contentId =:contentId ").setParameter("contentId", contentId);
		return qc.idToObject(DataexSysDocAttach.class, qc.list());
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public DataexSysTransContent getItem() {
		return item;
	}
	public void setItem(DataexSysTransContent item) {
		this.item = item;
	}

	public List<DataexSysDocAttach> getDocAttachmentList() {
		return docAttachmentList;
	}

	public void setDocAttachmentList(List<DataexSysDocAttach> docAttachmentList) {
		this.docAttachmentList = docAttachmentList;
	}
}