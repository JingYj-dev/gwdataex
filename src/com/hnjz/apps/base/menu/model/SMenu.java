package com.hnjz.apps.base.menu.model;

import net.sf.json.JSONObject;

public class SMenu implements java.io.Serializable{

	private String  menuId;
	private String funcId;
	private String menuName;
	private String parentId;
	private String url;
	private String openFlag;
	private String sysId;
	private Integer orderNum;
	private String icon;
	
	public SMenu() {
		super();
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public JSONObject toJson() {
		return new JSONObject().element("menuId", menuId).element("funcId", funcId).element("menuName", menuName)
				.element("parentId", parentId).element("url", url)
				.element("orderNum", orderNum)
				.element("openFlag", openFlag).element("sysId", sysId)
				.element("icon", icon);
	}
}
