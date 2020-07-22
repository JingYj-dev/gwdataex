package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.post.service.SPostItem;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserPost;
import com.hnjz.apps.base.user.model.SUserRole;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class AddUserPost  extends AdminAction{
	private static Log log = LogFactory.getLog(AddUserPost.class);
	private String userId;
	private String ids; //岗位id
	private String delRoleIds;
	private String name;
	public AddUserPost(){
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(ids)|| StringHelper.isEmpty(userId)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.PostIdEmpty"));	
				return Action.ERROR;		
			}
			SUser sUser = QueryCache.get(SUser.class, userId);
			if(sUser == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.failed"));	
				return Action.ERROR;	
			}
			//保存功能
			List<String> rlist = StringHelper.strToList(ids);
			List<SUserPost> items= new ArrayList<SUserPost>();
			List<SUserRole> userRoleList = new ArrayList<SUserRole>();
			for (int i = 0; i < rlist.size() ; i++) {
				String addUserRoleIds = "";
				String rid=rlist.get(i);
				SUserPost item = new SUserPost();
				item.setUserId(userId);
				item.setPostId(rid.trim());
				item.setOrgId(sUser.getOrgId());
				items.add(item);
				sUser.getPostList().add(rid.trim());
				
				List<SRole> roleList = SPostItem.getRoleList(rid); // 获取岗位所分配的角色列表
				List<String> userrolelist= sUser.getRoleList().getListById();
				if(roleList!=null && roleList.size()!=0){
					for(SRole role : roleList){
						if(!(delRoleIds.contains(role.getUuid()) || userrolelist.contains(role.getUuid()))){   //去掉不用添加的角色id
							addUserRoleIds += role.getUuid();
							addUserRoleIds += ",";
						}else{
							if(delRoleIds.contains(role.getUuid())){
								delRoleIds = delRoleIds.replace(role.getUuid(), "");
							}
						}
					}
				}
				if(StringHelper.isNotEmpty(addUserRoleIds)){
					addUserRoleIds = addUserRoleIds.substring(0, addUserRoleIds.length()-1);
				}
				if(StringHelper.isNotEmpty(addUserRoleIds)){
					List<String> listIds = StringHelper.strToList(addUserRoleIds);
					List<SRole> rolelist= QueryCache.idToObj(SRole.class, listIds);
					for(SRole role : rolelist){
						SUserRole userRole = new SUserRole();
						userRole.setUserId(userId);
						userRole.setRoleId(role.getUuid());
						userRole.setSysId(role.getSysId());
						userRoleList.add(userRole);
						sUser.getRoleList().add(role.getUuid());
					}
				}
			}
			
			tx = new TransactionCache();
			tx.save(items);
			tx.save(userRoleList);
			tx.commit();
			
			for(SUserPost userPost : items){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SUserPost.class.getName());
				lp.setOpObjId(userPost.getUuid());
				lp.setRelObjType(SUser.class.getName());
				lp.setRelObjId(userId);
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
				lp.setLogData(Json.object2json(userPost));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			
			for(SUserRole userRole : userRoleList){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SUserPost.class.getName());
				lp.setOpObjId(userRole.getUuid());
				lp.setRelObjType(SUser.class.getName());
				lp.setRelObjId(userId);
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
				lp.setLogData(Json.object2json(userRole));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
	
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDelRoleIds() {
		return delRoleIds;
	}
	public void setDelRoleIds(String delRoleIds) {
		this.delRoleIds = delRoleIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
