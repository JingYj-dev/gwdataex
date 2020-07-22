package com.hnjz.apps.base.common.module;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.QueryDict;
import com.hnjz.apps.base.func.action.FuncTree;
import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.common.plugins.IDisposable;
import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.db.query.QueryCache;
import com.hnjz.apps.base.menu.action.MenuTree;
import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.apps.base.org.action.OrgTree;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.role.model.SRole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class CacheLoader extends AbstractConfigurablePlugin implements
		IDisposable, Initializable {
	private static Log log = LogFactory.getLog(CacheLoader.class);
	@Override
	protected void doConfig(Map<String, String> arg0) {

	}

	@Override
	public void dispose() {
		MemCachedFactory.buildClient().flushAll();
	}

	@Override
	public void initialize() {
		//加载字典
		java.util.List<String> lst = new QueryDict("select uuid from SDict").list();
		QueryDict.idToObj(SDict.class, lst);
		log.info("已加载数据字典!");
		//加载菜单树
		MenuTree.getInstance();
		lst = new QueryCache("select menuId from SMenu").list();
		QueryCache.idToObj(SMenu.class, lst);
		log.info("已加载系统菜单!");
		//加载功能树
		FuncTree.getInstance();
		lst = new QueryCache("select uuid from SFunc").list();
		QueryCache.idToObj(SFunc.class, lst);
		log.info("已加载系统功能!");
		//加载机构树
		OrgTree.getInstance();
		//加载机构
		lst = new QueryCache("select uuid from SOrg").list();
		QueryCache.idToObj(SOrg.class, lst);
		log.info("已加载系统机构!");
		//加载角色
		lst = new QueryCache("select uuid from SRole").list();
		QueryCache.idToObj(SRole.class, lst);
		log.info("已加载系统角色!");
		//加载岗位
		lst = new QueryCache("select uuid from SPost").list();
		QueryCache.idToObj(SPost.class, lst);
		log.info("已加载系统岗位!");
	}
}
