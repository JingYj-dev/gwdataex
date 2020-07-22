package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.hnjz.util.Ajax;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddDataexSysDirWs extends AdminAction implements ModelDriven{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddDataexSysDir.class);
	private DataexSysDirWs item;
	public AddDataexSysDirWs(){
		item = new DataexSysDirWs();
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(item == null
					|| StringHelper.isEmpty(item.getDirId())
					|| StringHelper.isEmpty(item.getWsType())
					|| StringHelper.isEmpty(item.getMethodName())
					|| StringHelper.isEmpty(item.getDataServiceUrl())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			if(!check(item)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						"接口配置已存在");
				return Action.ERROR;
			}
			DataexSysDirWs sysDirWs = new DataexSysDirWs();
			sysDirWs.setWsId(UuidUtil.getUuid());
			sysDirWs.setDataServiceUrl(item.getDataServiceUrl());
			sysDirWs.setDirId(item.getDirId());
			sysDirWs.setMethodName(item.getMethodName());
			sysDirWs.setWsType(item.getWsType());
			sysDirWs.setMemo(item.getMemo());
			
			tx = new TransactionCache();
			tx.save(sysDirWs);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		}catch(Exception ex){
			if(tx != null)
				tx.rollback();
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(),ex);
			return Action.ERROR;
		}
	}
	private Boolean check(DataexSysDirWs item){
	String wsId =(String) new QueryCache("select a.wsId from DataexSysDirWs a where a.dirId=:dirId and a.wsType=:wsType").setParameter("dirId", item.getDirId())
		.setParameter("wsType", item.getWsType()).setMaxResults(1).uniqueResult();
	if(StringHelper.isNotEmpty(wsId)){
		return false;
	}	
	return true;
	}
	@Override
	public Object getModel() {
		return item;
	} 
}
