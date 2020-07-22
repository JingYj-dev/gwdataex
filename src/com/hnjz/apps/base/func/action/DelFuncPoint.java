package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.func.model.SFuncAction;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * 功能项删除
 * 
 * @author FangWQ
 * @version v0.1
 * @since 2014-3-5 下午03:37:07
 */
@SuppressWarnings("serial")
public class DelFuncPoint extends AdminAction {
	private static Log log = LogFactory.getLog(DelFuncPoint.class);
	private String ids;

	public DelFuncPoint() {
	}

	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (StringHelper.isEmpty(ids)) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			List<String> listIds = StringHelper.strToList(ids);
			List<SFuncAction> funcActionList = QueryCache.idToObj(SFuncAction.class, listIds);
			SFunc funcObj = QueryCache.get(SFunc.class, funcActionList.get(0).getFuncCode());
			tx = new TransactionCache();
			tx.delete(funcActionList);
			tx.commit();
			funcObj.getFuncActionList().removeAll();
			for(SFuncAction funcAction : funcActionList){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SFuncAction.class.getName());
				lp.setOpObjId(funcAction.getUuid());
				lp.setRelObjType(SFunc.class.getName());
				lp.setRelObjId(funcAction.getFuncCode());
				lp.setOpType(LogConstant.LOG_TYPE_DELETE);
				lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
				lp.setLogData("");
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages
					.getString("systemMsg.success"), listIds.toString());
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
