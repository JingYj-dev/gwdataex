package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysAppid;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelDataexSysAppid extends AdminAction {
	private static final long serialVersionUID = -2367344806409120288L;
	private static Log log = LogFactory.getLog(DelDataexSysAppid.class);
	private static final String HINT = "该交换Appid已被使用！";
	private String ids;
	public DelDataexSysAppid(){
	}
	@Override
	protected String adminGo(){
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<DataexSysAppid> postList = QueryCache.idToObj(DataexSysAppid.class, listIds);
			//判断[历史消息表]是否已经被关联
			if (checkExists(postList)) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, HINT);	
				return Action.ERROR;
			}
			tx = new TransactionCache();
			tx.delete(postList);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	private boolean checkExists(List<DataexSysAppid> postList) {
		for (DataexSysAppid data : postList) {
			QueryCache qc = new QueryCache("select a.uuid from DataexSysDir a where a.appid=:codeId").setParameter("codeId",data.getUuid());
			List<String> ids = qc.list();
			if (ids != null && ids.size() > 0) {
				return true;
			}
		}
		return false;
	}

}
