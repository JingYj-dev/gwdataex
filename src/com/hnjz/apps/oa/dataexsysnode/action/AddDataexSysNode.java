package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;

public class AddDataexSysNode extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AddDataexSysNode.class);
	private final String HINT = "该交换节点名称已存在，请重新输入！";
	private DataexSysNode item;
	
	public AddDataexSysNode() { 
		this.item = new DataexSysNode();
	}
	
	protected String adminGo() {	
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getExnodeName()) || StringHelper.isEmpty(item.getExnodeType()) || StringHelper.isEmpty(item.getExnodeStatus())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			if(!check(item)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, HINT);
				return Action.ERROR;
			}
			tx = new TransactionCache();
			String uuid = UuidUtil.getUuid();
			item.setExnodeId(uuid);
			item.setCreatedTime(Calendar.getInstance().getTime());
			tx.save(item);
			tx.commit();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getExnodeId());
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

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return item;
	}
	
	private Boolean check(DataexSysNode item) {
		Object id = new QueryCache("select a.exnodeId from DataexSysNode a where a.exnodeName=:exnodeName")
		.setParameter("exnodeName",item.getExnodeName()).setMaxResults(1).uniqueResult();
		DataexSysNode data = QueryCache.get(DataexSysNode.class, (String) id);
		if (data == null) {
			return true;
		}
		return false;
	}
}
