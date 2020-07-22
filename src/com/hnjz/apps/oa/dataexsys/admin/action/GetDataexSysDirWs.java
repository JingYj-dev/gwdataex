package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDataexSysDirWs extends AdminAction{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetDataexSysDir.class);
	private DataexSysDirWs item;
	private String wsId;
	private String dirId;
	public GetDataexSysDirWs() {
	}
	public String adminGo() {
		try {
			
			if(StringHelper.isNotEmpty(wsId)){
				item = QueryCache.get(DataexSysDirWs.class, wsId);
				if(StringHelper.isNotEmpty(item.getMemo())){
					String str = RegexCheck.TagReverse(item.getMemo());
					item.setMemo(str);
				}
			}else{
				item = new DataexSysDirWs();
				item.setDirId(dirId);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}

	public DataexSysDirWs getItem() {
		return item;
	}

	public void setItem(DataexSysDirWs item) {
		this.item = item;
	}

	public String getWsId() {
		return wsId;
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}
	
	

}
