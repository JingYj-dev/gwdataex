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
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdDataexSysAppid extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1269510357007181416L;
	private static Log log = LogFactory.getLog(UpdDataexSysAppid.class);
	private DataexSysAppid item = null;
	
	public UpdDataexSysAppid(){
		item = new DataexSysAppid();
	}
	@Override
	protected String adminGo() {
		if (StringHelper.isEmpty(item.getUuid())) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			DataexSysAppid oldItem = QueryCache.get(DataexSysAppid.class, item.getUuid());
			if(oldItem == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.recordnotfound"));				
				return Action.ERROR;
			}
			if(!check("appidCode",item.getAppidCode())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, "该Appid编码已存在，请重新输入！");
				return Action.ERROR;
			}
			oldItem.setAppidCode(item.getAppidCode());
			oldItem.setRemark(item.getRemark());
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
	private Boolean check(String name,String value) {
		Object id = new QueryCache("select uuid from DataexSysAppid where "
				+name+"='"+value+"'").setMaxResults(1).uniqueResult();
		if (id == null) {
			return true;
		}
		return false;
	}
}
