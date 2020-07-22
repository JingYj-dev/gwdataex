package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysAppid;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;

public class AddDataexSysAppid extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = -2119502869272885466L;
	private static final Log log = LogFactory.getLog(AddDataexSysAppid.class);
	private final String HINT = "该Appid名称已存在，请重新输入！";
	private final String CODE = "该Appid编码已存在，请重新输入！";
	private DataexSysAppid item;
	
	public AddDataexSysAppid() { 
		this.item = new DataexSysAppid();
	}
	
	protected String adminGo() {	
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getAppidName()) || StringHelper.isEmpty(item.getAppidCode())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			if(!check("appidName",item.getAppidName())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, HINT);
				return Action.ERROR;
			}
			if(!check("appidCode",item.getAppidCode())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, CODE);
				return Action.ERROR;
			}
			tx = new TransactionCache();
			String uuid = UuidUtil.getUuid();
			item.setUuid(uuid);
			item.setCreatedTime(Calendar.getInstance().getTime());
			tx.save(item);
			tx.commit();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getUuid());
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
	
	private Boolean check(String name,String value) {
		Object id = new QueryCache("select uuid from DataexSysAppid where "
				+name+"='"+value+"'").setMaxResults(1).uniqueResult();
		if (id == null) {
			return true;
		}
		return false;
	}
}
