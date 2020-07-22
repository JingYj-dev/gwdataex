package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransParam;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDataexSysTransParam extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetDataexSysTransParam.class);
	private String paramId;
	private DataexSysTransParam item;
	
	public GetDataexSysTransParam() {
	}
	@Override
	protected String adminGo() {
		try{
			if (!StringHelper.isEmpty(paramId)) {
				item = QueryCache.get(DataexSysTransParam.class, paramId);
				if(StringHelper.isNotEmpty(item.getParamMemo())){
					String str = RegexCheck.TagReverse(item.getParamMemo());
					item.setParamMemo(str);
				}
			} else { 
				item = new DataexSysTransParam();
			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			return Action.ERROR;
		}
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public DataexSysTransParam getItem() {
		return item;
	}
	public void setItem(DataexSysTransParam item) {
		this.item = item;
	}
}
