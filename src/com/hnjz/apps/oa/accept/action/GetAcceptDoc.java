package com.hnjz.apps.oa.accept.action;

import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.admin.action.GetTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDocAttach;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

@SuppressWarnings("serial")
public class GetAcceptDoc extends AdminAction {
private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(GetTransContent.class);
	
	private String contentId;
	private DataexSysTransContent item;
	private List<DataexSysDocAttach> docAttachmentList;  //附件列表
	
	public GetAcceptDoc(){
		item = new DataexSysTransContent();
	}

	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(contentId)) {
				item =  QueryCache.get(DataexSysTransContent.class, contentId);
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

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

}
