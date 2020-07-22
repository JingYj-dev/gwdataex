package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class UpdUser extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdUser.class);
	private SUser item;
	
	public UpdUser() { 
		this.item = new SUser();
	}
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getUuid())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.userNotExist"));
				return Action.ERROR;
			}
			Object userid = new QueryCache("select a.uuid from SUser a where a.uuid=:uuid")
			.setParameter("uuid",item.getUuid()).setMaxResults(1).uniqueResult();//此处不能有缓存
			if(userid == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.userNotExist"));
				return Action.ERROR;
			} 
			if(StringHelper.isEmpty(item.getLoginName())
					||StringHelper.isEmpty(item.getRealName())
					||StringHelper.isEmpty(item.getOrgId())
					||StringHelper.isEmpty(item.getOpenFlag())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			
			Object id = new QueryCache("select a.uuid from SUser a where a.loginName=:loginName and a.uuid!=:uuid and a.delFlag='2'")
				.setParameter("loginName",item.getLoginName()).setParameter("uuid", item.getUuid()).setMaxResults(1).uniqueResult();//此处不能有缓存
			if(id != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("sysmgrMsg.loginnameExsit",new String[]{item.getLoginName()}));				
				return Action.ERROR;
			} 
			SUser tmp = QueryCache.get(SUser.class, item.getUuid());
			
			tx = new TransactionCache();
			tmp.setRealName(item.getRealName());
			tmp.setLoginName(item.getLoginName());
			tmp.setSex(item.getSex());
			tmp.setMobile(item.getMobile());
			tmp.setPhone(item.getPhone());
			tmp.setEmail(item.getEmail());
			tmp.setUserType(item.getUserType());
			tmp.setOrderNum(item.getOrderNum());
			tmp.setOpenFlag(item.getOpenFlag());
			tmp.setEditDate(new Date());
			tmp.setRemark(item.getRemark());
			tmp.setOrgId(item.getOrgId());
			tmp.setSecLevel(item.getSecLevel());
			tx.update(tmp);
			tx.commit();
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SUser.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SUser.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(tmp));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	
	public SUser getModel() {
		return item;
	}
	public SUser getUser() {
		return item;
	}
	public void setUser(SUser user) {
		this.item = user;
	}
	public SUser getItem() {
		return item;
	}
	public void setItem(SUser item) {
		this.item = item;
	}


	
}
