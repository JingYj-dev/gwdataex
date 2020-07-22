package com.hnjz.apps.base.user.spi.impl;

import com.hnjz.apps.base.func.action.FuncItem;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.spi.IUserListener;
import com.hnjz.common.exception.base.CssBaseException;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;

import java.util.List;
import java.util.Map;

public class DefaultUserListener extends AbstractConfigurablePlugin implements IUserListener {
	@Override
	public void afterLogin(SUser sUser) throws CssBaseException {
		if(sUser==null)
			return;
		//获取功能ID列表:从缓存中获取
		List<String> funcIdList = FuncItem.getSFuncJoinList(sUser.getUuid());
		//设置用户（userId）在系统中所拥有的功能:从缓存中获取
		sUser.setFunctions(FuncItem.getSFuncIdSet(funcIdList));
		//设置用户（userId）在系统中所拥有的功能点:从缓存中获取
		sUser.setFuncActions(FuncItem.getSFuncActionJoinList(funcIdList));
	}

	@Override
	public void beforeLogout(SUser user) throws CssBaseException {}

	@Override
	protected void doConfig(Map<String, String> configMap) {}
}
