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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class UpdUserStatus extends AdminAction {
	private static final Log log = LogFactory.getLog(UpdUserStatus.class);
	private String  ids;
	private String openFlag;
	
	public UpdUserStatus() {
	}

	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (StringHelper.isEmpty(ids) || StringHelper.isEmpty(openFlag)) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			List lst = StringHelper.strToList(ids);
			List<SUser> itemLst = (List<SUser>) QueryCache.idToObj(SUser.class, lst);
			
			tx = new TransactionCache();
			for(SUser item : itemLst ){
				if("1".equals(openFlag)){
					item.setFailedLoginCount(0);
				}
				item.setOpenFlag(openFlag);
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
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
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

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	



}
