package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.role.action.GetRole;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author FangWQ
 * @version v0.1
 * @since 2014-3-5 下午04:45:39
 */
@SuppressWarnings("serial")
public class GetFuncPoint extends AdminAction {
	private static Log log = LogFactory.getLog(GetRole.class);
	private String uuid;
	private SFunc item;

	public GetFuncPoint() {
		
	} 

	@Override
	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(uuid)) {
				item = QueryCache.get(SFunc.class, uuid);
			}
			if(item==null){
				setMessage(Messages.getString("systemMsg.NoFunc"));
				return Action.ERROR;
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public SFunc getItem() {
		return item;
	}

	public void setItem(SFunc item) {
		this.item = item;
	}
}
