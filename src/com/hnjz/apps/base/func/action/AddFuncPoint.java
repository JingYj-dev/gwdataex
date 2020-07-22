package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.func.model.SFuncAction;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能项添加
 * @author  FangWQ
 * @version v0.1  
 * @since   2014-3-5 下午03:37:46
 */
@SuppressWarnings("serial")
public class AddFuncPoint extends AdminAction {
	private static Log log = LogFactory.getLog(AddFuncPoint.class);
	private String sysId;
	private String funcCode;
	private String actionCodes;
	private String actionNames;
	public AddFuncPoint(){
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			//保存功能
			if(StringHelper.isEmpty(actionCodes) || StringHelper.isEmpty(actionNames) || StringHelper.isEmpty(funcCode)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			SFunc funcObj = QueryCache.get(SFunc.class, funcCode);
			List<String> actionCodeList = StringHelper.strToList(actionCodes);
			List<String> actionNameList = StringHelper.strToList(actionNames);
			List<SFuncAction> actions = new ArrayList<SFuncAction>();
			for (int i = 0; i < actionCodeList.size() && i < actionNameList.size(); i++) {
				String code = actionCodeList.get(i);
				String name = actionNameList.get(i);
				if(StringHelper.isNotEmpty(code) && StringHelper.isNotEmpty(name)){
					SFuncAction action = new SFuncAction();
					action.setSysId(sysId);
					action.setFuncCode(funcCode);
					action.setActionCode(code);
					action.setActionName(name);
					action.setUuid(UuidUtil.getUuid());
					action.setOpenFlag("1");
					actions.add(action);
					funcObj.getFuncActionList().add(action.getActionCode());
				}
			}
			tx = new TransactionCache();
			tx.save(actions);
			tx.commit();
			
			
			
			
			for (SFuncAction funcaction : actions) {
				LogPart lp = new LogPart();		
				lp.setOpObjType(SFuncAction.class.getName());
				lp.setOpObjId(funcaction.getUuid());
				lp.setRelObjType(SFunc.class.getName());
				lp.setRelObjId(funcCode);
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(funcaction));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			
			
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
	
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	public String getActionCodes() {
		return actionCodes;
	}

	public void setActionCodes(String actionCodes) {
		this.actionCodes = actionCodes;
	}

	public String getActionNames() {
		return actionNames;
	}

	public void setActionNames(String actionNames) {
		this.actionNames = actionNames;
	}

}
