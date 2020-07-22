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
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdDataexSysNode extends AdminAction implements ModelDriven {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UpdDataexSysNode.class);
	private DataexSysNode item = null;
	
	public UpdDataexSysNode(){
		item = new DataexSysNode();
	}
	@Override
	protected String adminGo() {
		if (StringHelper.isEmpty(item.getExnodeId())) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			DataexSysNode oldItem = QueryCache.get(DataexSysNode.class, item.getExnodeId());
			if(oldItem == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.recordnotfound"));				
				return Action.ERROR;
			}
//			oldItem.setExnodeName(item.getExnodeName());
			oldItem.setExnodeType(item.getExnodeType());
			oldItem.setExnodeStatus(item.getExnodeStatus());
			//oldItem.setEncryption(item.getEncryption());
			oldItem.setSignature(item.getSignature());
			tx = new TransactionCache();
			tx.update(oldItem);
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
	
	public Object getModel() {
		return item;
	}

}
