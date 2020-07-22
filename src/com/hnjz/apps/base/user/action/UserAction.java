/*
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file CheckAdminAction.java creation date: [2009-7-9 02:11:37] by jiadw
 * http://www.css.com.cn
 **/
package com.hnjz.apps.base.user.action;

import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.apps.base.user.model.UserInfo;

public abstract class UserAction extends AbstractAction {
	public UserInfo gUser = null;
	protected Integer year;
	protected String menu;
	protected Integer show;
	public UserAction() {
	}
	public String go() {
		return userGo();
	}
	protected abstract String userGo();
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public Integer getShow() {
		return show;
	}
	public void setShow(Integer show) {
		this.show = show;
	}
	public UserInfo getGUser() {
		return gUser;
	}
	public void setGUser(UserInfo user) {
		gUser = user;
	}
}
