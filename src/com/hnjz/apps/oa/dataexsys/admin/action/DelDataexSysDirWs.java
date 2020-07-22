package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelDataexSysDirWs extends AdminAction{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DelDataexSysDirWs.class);
	private String ids;
	public DelDataexSysDirWs(){}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			List<String> listIds = StringHelper.strToList(ids);
			List<DataexSysDirWs> postList = QueryCache.idToObj(DataexSysDirWs.class,listIds);
			tx = new TransactionCache();
			tx.delete(postList);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
			
		}catch(Exception ex){
			if(tx != null){
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
