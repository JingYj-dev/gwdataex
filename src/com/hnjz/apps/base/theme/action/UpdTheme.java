package com.hnjz.apps.base.theme.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.theme.model.Theme;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdTheme extends AdminAction implements ModelDriven{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UpdTheme.class);
	private Theme item;
	public UpdTheme(){
		item = new Theme();
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (item == null || StringHelper.isEmpty(item.getUuid())
					|| StringHelper.isEmpty(item.getCode())
					|| StringHelper.isEmpty(item.getName())
					|| StringHelper.isEmpty(item.getOpenFlag())
					|| StringHelper.isEmpty(item.getTypeId())) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			Theme theme = QueryCache.get(Theme.class, item.getUuid());
			if (theme == null) {
				result = Ajax.JSONResult(1, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
		 	}
			//主题编码不能重复
			String themeCode = (String)new QueryCache("select a.uuid from Theme a where a.code=:code and a.uuid!=:uuid ").setParameter("code", item.getCode()).setParameter("uuid",item.getUuid()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotAnyEmpty(themeCode)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.themecodeexist",new String[]{item.getCode()}));
				return Action.ERROR;
			}
			theme.setCode(item.getCode());
			theme.setName(item.getName());
			theme.setOpenFlag(item.getOpenFlag());
			theme.setOrderNum(item.getOrderNum());
			theme.setRemark(RegexCheck.TagReverse(item.getRemark()));
			theme.setTypeId(item.getTypeId());
			tx = new TransactionCache();
			tx.update(theme);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
					Messages.getString("systemMsg.dbFaild"));
			return Action.ERROR;
		}
	}
	@Override
	public Object getModel() {
		return item;
	}

}
