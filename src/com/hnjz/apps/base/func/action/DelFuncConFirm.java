package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.role.model.SRoleFunc;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelFuncConFirm extends AdminAction{
		private static Log log = LogFactory.getLog(DelFuncConFirm.class);
	private boolean ConfirmFlag = true;
	private String ids;
	public DelFuncConFirm(){
		
	}
	public boolean isConfirmFlag() {
		return ConfirmFlag;
	}
	
	public void setConfirmFlag(boolean confirmFlag) {
		ConfirmFlag = confirmFlag;
	}
	
	public String getIds() {
		return ids;
	}
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Override
	protected String adminGo() {
		String str = ids;
		str += ";";
		if(StringHelper.isEmpty(ids)){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
			return Action.ERROR;		
		}
		List<String> listIds = StringHelper.strToList(ids);
		List<SFunc> funcList = QueryCache.idToObj(SFunc.class, listIds);
		for(SFunc sfunc:funcList){
			List<SRoleFunc> roleFuncList = SRoleFunc.getRoleFuncByfuncId(sfunc.getUuid());
			if(roleFuncList!=null && roleFuncList.size()>0){
				ConfirmFlag = false;
			}
		}
		str += ConfirmFlag;
		result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"),str);
		return Action.SUCCESS;
	}

}
