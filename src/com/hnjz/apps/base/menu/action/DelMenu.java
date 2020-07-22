package com.hnjz.apps.base.menu.action;
import com.hnjz.apps.base.menu.service.MenuService;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;

public class DelMenu extends AdminAction{
	private String ids;
	
	public DelMenu() {
	}
	
	public String adminGo() {
		IServiceResult<?> res= MenuService.delMenu(ids);
		result = Ajax.JSONResult(res.getResultCode(), res.getResultDesc(),res.getResultObject());
		return res.toActionResult();
	}
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
