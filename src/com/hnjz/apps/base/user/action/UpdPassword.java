package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UpdPassword extends AdminAction {
	private static final Log log = LogFactory.getLog(UpdPassword.class);
	private String ids;
	private String userPassword;
	
	public UpdPassword() { 
	}

	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (StringHelper.isEmpty(ids)) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			List lst = StringHelper.strToList(ids);
			List<SUser> itemLst = (List<SUser>) QueryCache.idToObj(SUser.class, lst);
			
			tx = new TransactionCache();
			for(SUser item : itemLst ){
				String time = SecurityService.getParamValue(SecurityParam.ACTIVE_TIME);
				Calendar cc = new GregorianCalendar();
				cc.set(Calendar.MINUTE, cc.get(Calendar.MINUTE) + Integer.parseInt(time));
				item.setActiveDeadLine(cc.getTime());
				item.setActiveStatus("2");
				item.setPassword(Md5Util.MD5Encode(userPassword));
				tx.update(item);
			}
			tx.commit();
			
			for(SUser item : itemLst ){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SUser.class.getName());
				lp.setOpObjId(item.getUuid());
				lp.setRelObjType(SUser.class.getName());
				lp.setRelObjId(item.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
				lp.setLogData(Json.object2json(item));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}




}

