package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.common.OrgProvider;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DirOrgPost extends AdminAction{
	private static Log log = LogFactory.getLog(DirOrgPost.class);
	private Page page;
	private String orgId;
	private String name;
	private List PostList;
//	private boolean Flag = true;
	public DirOrgPost() {
		page = new Page();
		page.setCurrentPage(1);
		page.setCountField("a.uuid");
	}
	protected String adminGo() {
		if(StringHelper.isEmpty(orgId)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;
		}
		try {
			List<String> orgPostList = OrgProvider.queryPostId(orgId);//岗位目前分配的岗位列表
            List<String> postIdList = new ArrayList<String>();
			if(StringHelper.isEmpty(name)){
	            for(String postid:orgPostList){
	            	postIdList.add(postid.trim());
	            }	
			}else{
	            for(String postid:orgPostList){
	            	SPost post = QueryCache.get(SPost.class,postid.trim());	
	            	if(post.getName().contains(name)){
	            		postIdList.add(postid.trim());
	            	}
	            	if(name.equals(post.getName())){
	            		postIdList.add(postid.trim());
	            	}
	            }
			}
			if(postIdList.size()==0){
				PostList = null;
			}else{
			  PostList= QueryCache.idToObj(SPost.class,postIdList);	
			}

/*            page.setResults(PostList);
            loadPage(page);*/
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public List getPostList() {
		return PostList;
	}
	public void setPostList(List postList) {
		PostList = postList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private void loadPage(Page page){
		List results = page.getResults();
		if(results != null && results.size() > 0){
			page.setTotalRows(results.size());
			page.setTotalPages((int)Math.ceil(results.size()*1.0/page.getPageSize()));
			int total = results.size();
			int curPage = page.getCurrentPage();
			int pageSize = page.getPageSize();
			int startIndex = (curPage>0?curPage-1:0) * pageSize;
			if (startIndex > total) {
				page.setTotalRows(0);
				page.setResults(null);
				return;
			}
			int endIndex = startIndex + pageSize;
			if(endIndex > total){endIndex=total;}
			results = results.subList(startIndex, endIndex );
			page.setResults(results);
		}
	}
}
