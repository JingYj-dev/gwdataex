package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.func.model.SFuncAction;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author FangWQ 功能项分页查询
 */
@SuppressWarnings("serial")
public class DirFuncPoint extends AdminAction {
	private static Log log = LogFactory.getLog(DirFuncPoint.class);
	private Page page;
	private String uuid;

	public DirFuncPoint() {
		page = new Page();
		page.setCurrentPage(1);
		page.setCountField("a.funcCode");
	}

	protected String adminGo() {
		try {
			if (StringHelper.isEmpty(uuid)) {
				throw new Exception("不合法节点.");
			}
			SFunc fun = FuncPointItem.getFunc(uuid);
			if (null == fun) {
				throw new Exception("不合法节点.");
			}
			QueryCache qc = new QueryCache(
					" select a.uuid from SFuncAction a where a.funcCode=:funcCode ")
					.setParameter("funcCode", fun.getUuid());
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SFuncAction.class, page
					.getResults()));
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}