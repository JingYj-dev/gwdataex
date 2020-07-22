package com.hnjz.apps.base.common.action;

import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.UserAction;
import com.hnjz.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class ClearCache extends UserAction {
	private static Log log = LogFactory.getLog(ClearCache.class);
	@Override
	protected String userGo() {
		 try {
			MemCachedFactory.buildClient().flushAll();
			 setResult(Ajax.JSONResult(IServiceResult.RESULT_OK,
								Messages.getString("systemMsg.success")));
		} catch (Exception ex) {
			String msg=Messages.getString("systemMsg.failed");
			log.error(ex.getMessage(), ex);
			setResult(Ajax.JSONResult(IServiceResult.RESULT_OK,
					msg));
			return ERROR;
		}		 
		return SUCCESS;
	}

}
