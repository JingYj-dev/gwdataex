package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysAppid;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDataexSysAppid extends AdminAction {
	private static final long serialVersionUID = -8974622323770496897L;
	private static Log log = LogFactory.getLog(GetDataexSysAppid.class);
	private String uuid;
	private DataexSysAppid item;
	
	public GetDataexSysAppid() {
	}
	@Override
	protected String adminGo() {
		try{
			if (!StringHelper.isEmpty(uuid) ) {
				item = QueryCache.get(DataexSysAppid.class, uuid);
			}else{
				item = new DataexSysAppid();
			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			return Action.ERROR;
		}
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public DataexSysAppid getItem() {
		return item;
	}
	public void setItem(DataexSysAppid item) {
		this.item = item;
	}
	
}
