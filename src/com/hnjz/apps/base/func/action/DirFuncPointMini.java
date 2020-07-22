package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.func.model.SFuncAction;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.DefaultActionLog;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FangWQ 功能项分页查询
 */
@SuppressWarnings("serial")
public class DirFuncPointMini extends AdminAction {
	private static Log log = LogFactory.getLog(DirFuncPointMini.class);
	private Page page;
	private String sysIdSearch;
	private String sysId;
	private String packageId;
	private String funcCode;
	private SFunc item;
	private String uuid;

	public DirFuncPointMini() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(5);
	}

	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(uuid)) {
				item = QueryCache.get(SFunc.class, uuid);
			}
			if(item==null){
				setMessage(Messages.getString("systemMsg.NoAddUserRole"));
				return Action.ERROR;
			}
			List<DefaultActionLog> actions = null;
			if (StringHelper.isNotEmpty(item.getSysId()))
			{
				List<SFuncAction> funcActions = FuncPointItem.getFuncActionByCode(item.getUuid());//获取当前功能已经添加的功能项
				actions = FuncPointItem.getActions(item.getSysId(), packageId);
				if(actions != null){
					//过滤已经添加的功能项
					actions = ActionsFilter(actions, funcActions);
				}
			}
			page.setResults(actions);
			//设置分页
			loadPage(page);
			return SUCCESS;
			
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	
	private List<DefaultActionLog> ActionsFilter(List<DefaultActionLog> actions,List<SFuncAction> funcActions){
		if(actions == null || funcActions == null){return actions;}
		List<DefaultActionLog> result = new ArrayList<DefaultActionLog>();
		Map<String, String> filterMap = new HashMap<String, String>();
		for(SFuncAction funcAction : funcActions){
			filterMap.put(funcAction.getActionCode(), funcAction.getActionCode());
		}
		for(int i = 0; i < actions.size(); i++) {
			DefaultActionLog action = actions.get(i);
			if(!filterMap.containsKey(action.getUrl())) {
				result.add(actions.get(i));
			}
		}
		return result;
	}
	
	private void loadPage(Page page){
		List results = page.getResults();
		if(results != null && results.size() > 0){
			page.setTotalRows(results.size());
			page.setTotalPages((int)Math.ceil(results.size()*1.0/page.getPageSize()));
			int total = results.size();
			int curPage = page.getCurrentPage();
			int pageSize = page.getPageSize();
			int startIndex = (curPage>0?curPage-1:0) * pageSize;
			if (startIndex > total) {
				page.setTotalRows(0);
				page.setResults(null);
				return;
			}
			int endIndex = startIndex + pageSize;
			if(endIndex > total){endIndex=total;}
			results = results.subList(startIndex, endIndex );
			page.setResults(results);
		}
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getSysIdSearch() {
		return sysIdSearch;
	}

	public void setSysIdSearch(String sysIdSearch) {
		this.sysIdSearch = sysIdSearch;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	
	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
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

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
}