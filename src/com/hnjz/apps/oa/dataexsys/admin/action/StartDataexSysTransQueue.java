package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransQueue;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysTransParamService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

public class StartDataexSysTransQueue extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(StartDataexSysTransQueue.class);
	private String ids;
	public StartDataexSysTransQueue(){
	}
	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo(){
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List<String> listIds = StringHelper.strToList(ids);
			List<DataexSysTransQueue> postList = QueryCache.idToObj(DataexSysTransQueue.class, listIds);
			String sendStatus = "";
			Integer sendTimes = null;
			String maxSendTimes = "";
			if (postList != null && postList.size() > 0) {
				for (DataexSysTransQueue queue : postList) {
					sendStatus = queue == null ? "" : queue.getSendStatus();
					sendTimes = queue == null ? null : queue.getSendTimes();
					if (StringHelper.isNotEmpty(sendStatus) || sendTimes != null) {
						maxSendTimes = DataexSysTransParamService.getParamValue(Constants.MAX_SEND_TIMES_PARAM_ID);
						if (StringHelper.isEmpty(maxSendTimes)) {
							result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.notStartQueueWithNoParam", new String[]{Constants.MAX_SEND_TIMES_PARAM_ID.toString()}));
							return Action.ERROR;
						}
						if (!(Constants.kqueue_failure.equals(sendStatus) && sendTimes >= Integer.parseInt(maxSendTimes))) {
							result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.notStartQueue", new String[]{maxSendTimes}));
							return Action.ERROR;
						}
						queue.setUpdateTime(new Date());
						queue.setSendStatus(Constants.kqueue_failure);
						queue.setSendTimes(0);
					}
				}
			} else {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
				return Action.ERROR;
			}
			
			tx = new TransactionCache();
			tx.update(postList);
			tx.commit();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if(tx != null)
				tx.rollback();
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
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
