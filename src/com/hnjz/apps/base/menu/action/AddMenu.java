package com.hnjz.apps.base.menu.action;

import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.apps.base.menu.service.MenuService;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.opensymphony.xwork.ModelDriven;


public class AddMenu extends AdminAction implements ModelDriven{

	private SMenu item;
	private String getmenu_selSys;
	private String getmenu_selFunc;
	
	public AddMenu() {
		this.item = new SMenu();
	}

	@Override
	public String adminGo() {
		IServiceResult<?> res=MenuService.addMenu(item,getmenu_selSys,getmenu_selFunc);
		result = Ajax.JSONResult(res.getResultCode(), res.getResultDesc(),res.getResultObject());
		return res.toActionResult();
	}

	@Override
	public Object getModel() {
		return item;
	}

	public String getGetmenu_selSys() {
		return getmenu_selSys;
	}

	public void setGetmenu_selSys(String getmenu_selSys) {
		this.getmenu_selSys = getmenu_selSys;
	}

	public String getGetmenu_selFunc() {
		return getmenu_selFunc;
	}

	public void setGetmenu_selFunc(String getmenuSelFunc) {
		getmenu_selFunc = getmenuSelFunc;
	}
}
