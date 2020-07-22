package com.hnjz.apps.base.menu.service;

import com.hnjz.apps.base.menu.action.MenuTree;
import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.core.model.IUser;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.config.ConfigurationManager;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.menu.IMenuProvider;
import com.hnjz.webbase.menu.MenuItem;

import java.util.List;
import java.util.Map;

public class DBMenuProvider extends AbstractConfigurablePlugin implements IMenuProvider {
	private boolean cached = true;
	private String cacheName = "_user_menu_cache/";
	private boolean ignoreOneTop = false;
	@Override
	protected void doConfig(Map<String, String> configMap) {
		if(configMap==null)return;
		if("false".equals(configMap.get("cache"))){
			cached = false;
		}
		if("true".equals(configMap.get("ignoreOneTop"))){
			ignoreOneTop = true;
		}
	}	

	@Override
	public List<MenuItem> getAllMenu(IUser user) {
			List<MenuItem> items=null;
			if(cached){
				items=getCachedListItem(user);
				if(items!=null) return items;
			}
			items = new java.util.ArrayList<MenuItem>();
			MenuTree menus=MenuTree.getInstance();
			//根节点
			TreeNode root= menus.getRootNode();
			int level=1;
			for(TreeNode node1 : root.getChildren()){
				MenuItem mi = transpass(node1,level,user);
				if(mi!=null)
					items.add(mi);	
			}
			if(items.size()==1 && ignoreOneTop){
				MenuItem mi=items.get(0);
				if(StringHelper.isEmptyByTrim(mi.getPath()))
					items = mi.getMenus();
			}
			if(cached)cacheListItem(user,items);
			return items;
	}
	
	/**
	 * 遍历菜单树，获取系统菜单
	 * @param menuItem
	 * @param node
	 * @param level
	 * @param funcCodes
	 */
	private MenuItem transpass(TreeNode node,int level,IUser user) {
		//获取所有子节点
		List<TreeNode> sMenuNodes = node.getChildren();
		String curMenuId =  node.getNodeId();
		MenuItem menuItem = new MenuItem(); 
		SMenu menu= QueryCache.get(SMenu.class, curMenuId);
		if(menu!=null && "1".equals(menu.getOpenFlag())){
			String funcId = menu.getFuncId();
			String url = menu.getUrl();
			/*
			 Set<String> funcCodes = user.getFunctions();
			 if(isSecured  && !(StringHelper.isEmptyByTrim(funcId) && StringHelper.isEmptyByTrim(url)) && !funcCodes.contains(funcId))
				return null;*/
			 //暂时修改，for测试
			if(!(StringHelper.isEmptyByTrim(funcId) && StringHelper.isEmptyByTrim(url)) && !WebBaseUtil.hasPrivilege(user,funcId))
				return null;
			menuItem.setId(menu.getMenuId());
			menuItem.setIcon(menu.getIcon());
			menuItem.setLevel(level);
			menuItem.setName(menu.getMenuName());
			menuItem.setOpenIcon(menu.getIcon());
			menuItem.setPath(url);
			menuItem.setFuncode(menu.getFuncId());
			menuItem.setParentId(menu.getParentId());
			menuItem.setVisible(true);
			if(sMenuNodes.isEmpty())
				menuItem.setIsLast(true);
			else
				menuItem.setIsLast(false);
				
		}else
			return null;
		
		if(sMenuNodes!=null && !sMenuNodes.isEmpty()){
			for(TreeNode node1 : sMenuNodes){
				MenuItem mi = transpass(node1,level+1,user);
				if(mi!=null)
					menuItem.getMenus().add(mi);	
			}
			String funcId = menu.getFuncId();
			String url = menu.getUrl();
			
			if(StringHelper.isEmptyByTrim(funcId) && StringHelper.isEmptyByTrim(url) && menuItem.getMenus().isEmpty())
				return null;
		}			
		return menuItem;
	}
	
	/**
	 * 从缓存中获取用户菜单
	 * @return
	 */
	private List<MenuItem> getCachedListItem(IUser user){
		if(user==null)return null;
		return (List<MenuItem>) MemCachedFactory.get(getUserKey(user));
	}
	
	/**
	 * 缓存用户菜单
	 * @return
	 */
	private void cacheListItem(IUser user,List<MenuItem>  items){
		if(items==null)
			return;
		if(user==null)
			return ;
		MemCachedFactory.set(getUserKey(user),items,ConfigurationManager.getCacheTime());
	}
	
	/**
	 * 返回用户缓存键值
	 * @param user
	 * @return
	 */
	private  String getUserKey(IUser user){
		return cacheName+"/"+user.getOrganId()+"/"+user.getUserId();
	}
}
