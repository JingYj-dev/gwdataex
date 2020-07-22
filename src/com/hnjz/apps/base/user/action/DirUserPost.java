package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DirUserPost extends AdminAction{
	private static Log log = LogFactory.getLog(DirUserRole.class);
	private Page page;
	private String userId;
	private List PostList;
	public DirUserPost() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(11);
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try {
			SUser sUser = QueryCache.get(SUser.class, userId);
			if(sUser == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.failed"));	
				return Action.ERROR;	
			}
            List<String> userPostList = sUser.getPostList().getListById();//用户目前分配的岗位列表
            PostList= QueryCache.idToObj(SPost.class,userPostList);
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List getPostList() {
		return PostList;
	}
	public void setPostList(List postList) {
		PostList = postList;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	
	

}
