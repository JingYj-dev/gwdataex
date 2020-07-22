package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.model.SUserOrg;
import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class AddUser extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddUser.class);
	private SUser item;
	private SUserOrg userorg = new SUserOrg();

	public AddUser() { 
		this.item = new SUser();
	}
	
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getLoginName())
					||StringHelper.isEmpty(item.getRealName())
					||StringHelper.isEmpty(item.getOrgId())
				    ||StringHelper.isEmpty(item.getOpenFlag())
				    ||StringHelper.isEmpty(item.getUserType())
				    ||StringHelper.isEmpty(item.getSecLevel())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			//不能出现相同的登录名
			Object id = new QueryCache("select a.uuid from SUser a where a.loginName=:loginName ")
				.setParameter("loginName",item.getLoginName()).setMaxResults(1).uniqueResult();
			if(id != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("sysmgrMsg.loginnameExsit",new String[]{item.getLoginName()}));				
				return Action.ERROR;
			}
			item.setDelFlag(Environment.UN_DELETE_STATE);
			item.setIssueDate(new Date());
			item.setIssueId(sUser.getUserId());
			item.setIssueName(sUser.getRealName());
			item.setEditDate(new Date());
			item.setTotalLoginCount(0);
			item.setFailedLoginCount(0);
			String password = SecurityService.getParamValue(SecurityParam.INIT_PASSWORD);
			if(StringHelper.isEmpty(password)){
				item.setPassword(Md5Util.MD5Encode(Md5Util.MD5Encode("admin111111")));
			}else{
				item.setPassword(Md5Util.MD5Encode(Md5Util.MD5Encode(password)));
			}
			if(item.getUserType().equals(BaseEnvironment.USERTYPE_SECURITY)){
				item.setActiveStatus("3");
			}else{
				item.setActiveStatus("1");
			}
			item.setFailedLoginCount(0);
			tx = new TransactionCache();
			tx.save(item);
			userorg.setOrgId(item.getOrgId());
			userorg.setUserId(item.getUuid());
			tx.save(userorg);
			tx.commit();
			tx=null;
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SUser.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SUser.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(item));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getUuid());
			return Action.SUCCESS;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		} finally {
			
		}
	}

	public SUser getModel() {
		return item;
	}
	public SUser getItem() {
		return item;
	}

	public void setItem(SUser item) {
		this.item = item;
	}
	
}
