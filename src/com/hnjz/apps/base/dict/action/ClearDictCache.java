package com.hnjz.apps.base.dict.action;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClearDictCache extends AbstractAction {
	private static Log log = LogFactory.getLog(ClearDictCache.class);

	private String tableName;
	private String result;
	
	@Override
	protected String go() {
		try{
			if(StringHelper.isEmpty(tableName)){
				result = Ajax.xmlResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			DictMan.clearTableCache(tableName);
		}catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			result = Ajax.xmlResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.dbFaild"));
			return ERROR;
		}
		return Action.SUCCESS;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
