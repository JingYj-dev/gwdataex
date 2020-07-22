package com.hnjz.apps.base.theme.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.theme.model.Theme;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class AddTheme extends AdminAction implements ModelDriven{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddTheme.class);
	private Theme item;
	public AddTheme(){
		item = new Theme();
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getCode()) || StringHelper.isEmpty(item.getName()) || StringHelper.isEmpty(item.getTypeId()) || StringHelper.isEmpty(item.getOpenFlag())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			//主题编码唯一
			String strCode = (String)new QueryCache("select a.uuid from Theme a where a.code=:code").setParameter("code", item.getCode()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotEmpty(strCode)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.themecodeexist",new String[]{item.getCode()}));
				return Action.ERROR;
			}
			tx = new TransactionCache();
			Theme theme = new Theme();
			theme.setCode(item.getCode());
			theme.setCreateTime(new Date());
			theme.setName(item.getName());
			theme.setOpenFlag(item.getOpenFlag());
			theme.setOrderNum(item.getOrderNum());
			theme.setRemark(RegexCheck.TagReverse(item.getRemark()));
			theme.setTypeId(item.getTypeId());
			theme.setUuid(UuidUtil.getUuid());
			tx.save(theme);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			if(null != tx){
				tx.rollback();
			}
			log.error(ex.getMessage(), ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));		
			return Action.ERROR;
		}
	}

	@Override
	public Object getModel() {
		return item;
	}
	public Theme getItem() {
		return item;
	}
	public void setItem(Theme item) {
		this.item = item;
	}

}
