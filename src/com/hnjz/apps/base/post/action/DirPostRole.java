package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.post.service.SPostItem;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.role.action.RoleItem;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public  class DirPostRole extends AdminAction{
	private static Log log = LogFactory.getLog(DirPostRole.class);
	private Page page;
	private String postId;
	private List RoleList;
	private boolean Flag = true;
	public DirPostRole() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(11);
		page.setCountField("a.uuid");
	}

	@Override 
	protected String adminGo() {
		try {
			SPost sPost = QueryCache.get(SPost.class, postId);
			if(sPost == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.failed"));	
				return Action.ERROR;	
			}
			 RoleList = SPostItem.getRoleList(postId);
			 Long roleCount = null;
			 QueryCache qc = new QueryCache("select count(a.uuid) from SPostRole a where a.postId=:postId")
			 .setParameter("postId", postId);
			 List<Integer> Ids = qc.listCache();
			 if(Ids!= null)
					roleCount  = (Long)qc.uniqueResult();
             if(roleCount == RoleItem.getSRoleCount()){
            	Flag = false;
            }
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public List getRoleList() {
		return RoleList;
	}

	public void setRoleList(List roleList) {
		RoleList = roleList;
	}

	public boolean isFlag() {
		return Flag;
	}

	public void setFlag(boolean flag) {
		Flag = flag;
	}


}
