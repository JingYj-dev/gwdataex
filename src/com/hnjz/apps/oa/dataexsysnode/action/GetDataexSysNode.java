package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDataexSysNode extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetDataexSysNode.class);
	private String exnodeId;
	private DataexSysNode item;
	
	public GetDataexSysNode() {
	}
	@Override
	protected String adminGo() {
		try{
			if (!StringHelper.isEmpty(exnodeId) ) {
				item = QueryCache.get(DataexSysNode.class, exnodeId);
			}else{
				item = new DataexSysNode();
			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			return Action.ERROR;
		}
	}
	public String getExnodeId() {
		return exnodeId;
	}
	public void setExnodeId(String exnodeId) {
		this.exnodeId = exnodeId;
	}
	public DataexSysNode getItem() {
		return item;
	}
	public void setItem(DataexSysNode item) {
		this.item = item;
	}
}
