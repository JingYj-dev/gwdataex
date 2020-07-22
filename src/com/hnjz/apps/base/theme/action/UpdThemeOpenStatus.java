package com.hnjz.apps.base.theme.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.theme.model.Theme;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class UpdThemeOpenStatus extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UpdThemeOpenStatus.class);
	private String ids;
	public String openFlag;
	@Override
	protected String adminGo() {
		try {
			if(StringHelper.isEmpty(ids)||StringHelper.isEmpty(openFlag)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;		
			}
			List<String> listIds = StringHelper.strToList(ids);
			if (listIds != null && listIds.size() > 0) {
				List<Theme> list = QueryCache.idToObj(Theme.class, listIds);
				for (Theme o : list) {
					o.setOpenFlag(openFlag);
				}
				new TransactionCache().xupdate(list);
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
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
	public String getOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}
}
