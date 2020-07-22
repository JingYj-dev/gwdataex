package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class GetUserPost extends AdminAction{
	private static Log log = LogFactory.getLog(GetUserPost.class);
	private String userId;
	private Page page;
	private List PostList;
	public GetUserPost() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(5);
		page.setCountField("a.uuid");
	}

	@Override
	protected String adminGo() {
		try {
			SUser sUser = QueryCache.get(SUser.class, userId);
			if(sUser == null){
				setMessage(Messages.getString("systemMsg.failed"));
				return Action.ERROR;	
			}
            List<String> orgPostIdList  =  QueryCache.get(SOrg.class,sUser.getOrgId()).getPostList().getListById();
			List<String> postIdList = new ArrayList<String>();
			if(orgPostIdList.size()==0){
				setMessage(Messages.getString("systemMsg.NoAddOrgPost"));
				return Action.ERROR;
			}
			for(String ids : orgPostIdList){
				postIdList.add(ids.trim());
			}
            List<String> userPostList = UserProvider.getPostIdList(userId);
            for(String  ids : userPostList){
            	postIdList.remove(ids.trim());
            }
            PostList= QueryCache.idToObj(SPost.class,postIdList);
            if(PostList.size()==0){
				setMessage(Messages.getString("systemMsg.NoAddPost"));
				return Action.ERROR;	
            }
//            int totalrows = PostList.size();
//            int totalpages = (totalrows/page.getPageSize())+1;
//			int begin = (page.getCurrentPage()-1)*page.getPageSize();
//			int end = begin + page.getPageSize();
//			if(end > PostList.size()){
//				page.setResults(PostList.subList(begin, PostList.size()));
//			}else{
//				page.setResults(PostList.subList(begin, end));
//			}
//            page.setTotalRows(PostList.size());
//            page.setTotalPages(totalpages);
			page.setResults(PostList);
			//设置分页
			loadPage(page);
			return SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	private void loadPage(Page page){
		List results = page.getResults();
		if(results != null && results.size() > 0){
			page.setTotalRows(results.size());
			page.setTotalPages((int)Math.ceil(results.size()*1.0/page.getPageSize()));
			int total = results.size();
			int curPage = page.getCurrentPage();
			int pageSize = page.getPageSize();
			if(curPage > page.getTotalPages()){
				curPage = page.getTotalPages();
			}
			int startIndex = (curPage>0?curPage-1:0) * pageSize;
			if(startIndex > total){
				startIndex =0;
			}
			int endIndex = startIndex + pageSize;
			if(endIndex > total){endIndex=total;}
			results = results.subList(startIndex, endIndex );
			page.setResults(results);
		}
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List getPostList() {
		return PostList;
	}

	public void setPostList(List postList) {
		PostList = postList;
	}



}
