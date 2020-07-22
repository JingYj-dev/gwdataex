package com.hnjz.apps.base.menu.action;

import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMenu extends AdminAction{
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetMenu.class);
	private String menuId;
	private SMenu item;
	private String parentId;
	public GetMenu() {
		this.item = new SMenu();
	}

	@Override
	protected String adminGo() {
		try{
			if(StringHelper.isNotEmpty(menuId)){
				item = QueryCache.get(SMenu.class,menuId);
				
			}else{
				item.setParentId(parentId);
				item.setOpenFlag("1");
			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			return Action.ERROR;
		}
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public SMenu getItem() {
		return item;
	}

	public void setItem(SMenu item) {
		this.item = item;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
