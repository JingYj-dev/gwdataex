package com.hnjz.apps.base.audit.action;

import com.hnjz.apps.base.audit.model.AuditParam;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetAuditParam extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetAuditParam.class);
	private String paramId;
	private AuditParam item;
	
	public GetAuditParam() {
	}
	@Override
	protected String adminGo() {
		try{
			if (!StringHelper.isEmpty(paramId) ) {
				item = QueryCache.get(AuditParam.class, paramId);
				if(StringHelper.isNotEmpty(item.getParamMemo())){
					String str = RegexCheck.TagReverse(item.getParamMemo());
					item.setParamMemo(str);
				}
			}else{
				item = new AuditParam();
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
	public AuditParam getItem() {
		return item;
	}
	public void setItem(AuditParam item) {
		this.item = item;
	}
}
