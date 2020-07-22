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
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdDataexSysDirWs extends AdminAction implements ModelDriven{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UpdDataexSysDirWs.class);
	private DataexSysDirWs item;
	public UpdDataexSysDirWs(){
		item = new DataexSysDirWs();
	}
	@Override
	protected String adminGo() {
		if(item == null 
				|| StringHelper.isEmpty(item.getDataServiceUrl())
				|| StringHelper.isEmpty(item.getMethodName())
				|| StringHelper.isEmpty(item.getWsType())){
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try{
			DataexSysDirWs oldItem = QueryCache.get(DataexSysDirWs.class, item.getWsId());
			if(oldItem == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
			}
			//节点标识、接口类型不能重复
			String id = (String)new QueryCache("select a.wsId from DataexSysDirWs a where a.dirId=:dirId and a.wsType=:wsType and a.wsId !=:wsId")
			.setParameter("dirId", item.getDirId()).setParameter("wsType",item.getWsType()).setParameter("wsId", item.getWsId()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotEmpty(id)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,"记录已存在");
				return Action.ERROR;
			}
			tx = new TransactionCache();
			tx.update(item);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS,Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		}catch(Exception ex){
			if(tx != null){
				tx.rollback();
			}
			log.error(ex.getMessage(),ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	@Override
	public Object getModel() {
		return item;
	}

}
