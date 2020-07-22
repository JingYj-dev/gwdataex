package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelDataexSysNode extends AdminAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8745788594191246799L;
	private static Log log = LogFactory.getLog(DelDataexSysNode.class);
	private static final String HINT = "该交换节点已被使用！";
	private String ids;
	public DelDataexSysNode(){
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
			List<DataexSysNode> postList = QueryCache.idToObj(DataexSysNode.class, listIds);
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
	
	private boolean checkExists(List<DataexSysNode> postList) {
		for (DataexSysNode data : postList) {
			QueryCache qc = new QueryCache("select a.uuid from DataexSysDir a where a.exnodeId=:exnodeId")
			.setParameter("exnodeId",data.getExnodeId());
			List<String> ids = qc.list();
			if (ids != null && ids.size() > 0) {
				return true;
			}
		}
		return false;
	}

}
