package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.audit.service.AuditParamService;
import com.hnjz.apps.base.audit.service.AuditParameter;
import com.hnjz.apps.base.log.service.LogService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogSizeOut extends AdminAction{

	private static Log log = LogFactory.getLog(LogSizeOut.class);
	
	public LogSizeOut() {
	}

	@Override
	protected String adminGo() {
		try{
//			if(logSizeOut()){
//				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, "", 0);
//			}else{
//				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, "", 1);
//			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, msg);
			return Action.ERROR;
		}
	}
	
	public static boolean logSizeOut(){
		double warn = AuditParameter.DEFAULT_LOG_WARN / 100f;
		long max = AuditParameter.DEFAULT_LOG_MAX;
		try {
			String warnStr = AuditParamService.getParamValue(AuditParameter.LOG_WARN);
			String maxStr = AuditParamService.getParamValue(AuditParameter.LOG_MAX);
			if (StringHelper.isNotEmpty(warnStr)) {
				warn = Double.parseDouble(warnStr) / 100f;
			}
			if (StringHelper.isNotEmpty(maxStr)) {
				max = Long.parseLong(maxStr);
			}
		} catch (Exception e) {
		}
		max = (long) (max * warn);
		long count =  LogService.getLogCount();
		if(count >= max){
			return true;
		}else{
			return false;
		}
	}
	public static String warnNum(){
		String warnStr = AuditParamService.getParamValue(AuditParameter.LOG_WARN);
		if (StringHelper.isEmpty(warnStr)) {
			warnStr = String.valueOf(AuditParameter.DEFAULT_LOG_WARN);
		}
		return warnStr;
		 
	}

}
