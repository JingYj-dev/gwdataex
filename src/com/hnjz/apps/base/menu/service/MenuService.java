package com.hnjz.apps.base.menu.service;

import com.hnjz.apps.base.menu.action.MenuTree;
import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.core.model.ModeFactory;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.IAtom;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.StringHelper;
import com.hnjz.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

import java.util.List;

public class MenuService {

	private static Log log = LogFactory.getLog(MenuService.class);
	
	public static IServiceResult<?> delMenu(String ids) {
		IServiceResult<?> res = null;
		if(StringHelper.isEmpty(ids)){
			res = ModeFactory.getModeFactory().buildNewServiceResult(
					IServiceResult.RESULT_FAILED,
					Messages.getString("systemMsg.fieldEmpty"),null);				
			return res;
		} 
		try{
			final List<?> listIds = StringHelper.strToList(ids);
			if(checkDel(listIds)){//删除树的时候，查看是否有子节点
				res = ModeFactory.getModeFactory().buildNewServiceResult(
						IServiceResult.RESULT_FAILED,
						Messages.getString("systemMsg.DeleteMenuChilde"),listIds);	
				return res;
			}
			new TransactionCache().executeTrans(new IAtom(){
				public Object execute(TransactionCache tc)
						throws HibernateException {
					java.util.Map<String, Object> params=new java.util.HashMap<String, Object>();
					params.put("menuIds", listIds);
					tc.executeUpdate("delete from SMenu where menuId in (:menuIds)", params);
					QueryCache onlyCache=new QueryCache();
					onlyCache.remove(SMenu.class, listIds);	//清理缓存
					return  listIds.size();
				}
			});
			res = ModeFactory.getModeFactory().buildNewServiceResult(
					IServiceResult.RESULT_OK, Messages.getString("systemMsg.success"), listIds);			
		} catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");;
			log.error(msg, ex);				 
			res = ModeFactory.getModeFactory().buildNewServiceResult(
					IServiceResult.RESULT_FAILED, msg, null);
		}
		return res;
	}
	
	public static boolean checkDel(List<?> listIds){//是否有子节点
		List<String> sids = new QueryCache("select a.menuId from SMenu a where a.parentId in(:parentIds) ").setParameter("parentIds", listIds).list();
		if(!sids.isEmpty() && sids.size()>0){
			return true;
		}
		return false;
	}
	
	public static IServiceResult<?> addMenu(SMenu model,String sFuncSysId,String getmenu_selFunc){
		IServiceResult<?> res = null;
		String result="";
		if (StringHelper.isEmpty(model.getIcon()) || StringHelper.isEmpty(model.getMenuName()) || StringHelper.isEmpty(model.getOpenFlag())){
			res = ModeFactory.getModeFactory().buildNewServiceResult(
					IServiceResult.RESULT_FAILED,
					Messages.getString("systemMsg.fieldEmpty"),null);
			return res;
		}
			try {
				if(StringHelper.isEmpty(model.getMenuId())&& check(model)){//是否有该对象，并且新添加的菜单名不能重复
					model.setMenuId(com.hnjz.util.UuidUtil.getUuid());
					model.setFuncId(getmenu_selFunc);
					model.setSysId(sFuncSysId);
	 				new TransactionCache().xsave(model);
	 				TreeNode node = new TreeNode();//功能树上添加新的节点
	 				node.setNodeId(model.getMenuId());
	 				node.setParentId(model.getParentId());
	 				MenuTree.getInstance().addTreeNode(node);
					res=ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_OK, Messages.getString("systemMsg.success"), model.toJson());			 
					return res;
				}
				res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_FAILED,Messages.getString("systemMsg.nameMenuExist"),model.toJson());
			} catch (Throwable ex) {
				String msg =  Messages.getString("systemMsg.dbFaild");
				log.error(msg, ex);
				res=ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_FAILED,msg,result);		 
			}
		return res;
	}
	
	public static IServiceResult<?> updateMenu(SMenu model,String sFuncSysId,String getmenu_selFunc){
		IServiceResult<?> res = null;
		if (model == null || StringHelper.isEmpty(model.getMenuId())){
			res=ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_FAILED, Messages.getString("systemMsg.fieldEmpty"), null);
			return res;
		}
			try {
				SMenu tmp = QueryCache.get(SMenu.class,model.getMenuId());
				if(tmp == null){
					res = ModeFactory.getModeFactory().buildNewServiceResult(
							IServiceResult.RESULT_FAILED,
							Messages.getString("systemMsg.fieldEmpty"),null);
					return res;
				}
				if(!checkUpd(model)){
					tmp.setFuncId(getmenu_selFunc);
					tmp.setSysId(sFuncSysId);
					tmp.setIcon(model.getIcon());
					tmp.setMenuName(model.getMenuName());
					tmp.setOpenFlag(model.getOpenFlag());
					tmp.setUrl(model.getUrl());
					tmp.setOrderNum(model.getOrderNum());
	 					new TransactionCache().xupdate(tmp);
					res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_OK, Messages.getString("systemMsg.success"),tmp.toJson());
					return res;
				}
				res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_FAILED,Messages.getString("systemMsg.nameMenuExist"),model.toJson());
			} catch (Throwable ex) {
				String msg =  Messages.getString("systemMsg.dbFaild");
				log.error(msg, ex);
				res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_FAILED,msg);		 
			}
		return res;
	}
	
	public static boolean check(SMenu item){//添加菜单时，菜单名不能重复
		String sid = (String)new QueryCache("select a.menuId from SMenu a where a.menuName =:menuName").setParameter("menuName", item.getMenuName()).setMaxResults(1).uniqueResult();
		if(StringHelper.isNotEmpty(sid))
			return false;
		return true; 
	}
	public static boolean checkUpd(SMenu item){//添加菜单时，菜单名不能重复
		String sid = (String)new QueryCache("select a.menuId from SMenu a where a.menuName =:menuName and a.menuId !=:menuId").setParameter("menuId", item.getMenuId()).setParameter("menuName", item.getMenuName()).setMaxResults(1).uniqueResult();
		if(StringHelper.isNotEmpty(sid))
			return true;
		return false; 
	}
}