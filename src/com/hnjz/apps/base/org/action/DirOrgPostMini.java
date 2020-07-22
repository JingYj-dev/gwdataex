package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.user.action.GetUserPost;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DirOrgPostMini extends AdminAction{
	private static Log log = LogFactory.getLog(GetUserPost.class);
	private String uuid;
	private String ids;
	private String postName;
	private String postRemark;
	private String orgId;
	private String postname;
	private Page page;
	private SOrg item;

	public DirOrgPostMini() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(5);
		page.setCountField("a.uuid");
	}
	protected String adminGo() {
		if (StringHelper.isNotEmpty(orgId)) {
			item = QueryCache.get(SOrg.class, orgId);
			if(item==null || "1".equals(item.getDelFlag())){
				setMessage(Messages.getString("systemMsg.NoOrg"));
				return Action.ERROR;	
			}
			QueryCache qc = new QueryCache("select count(a.postId) from SOrgPost a where a.orgId=:orgId")
			.setParameter("orgId", orgId);
			QueryCache qs = new QueryCache("select count(a.uuid) from SPost a");
			Long orgpostCount = (Long) qc.uniqueResult();
			Long postCount = (Long) qs.uniqueResult();
			if(orgpostCount.equals(postCount)){
				setMessage(Messages.getString("systemMsg.NoAddPost"));
				return Action.ERROR;	
			}
		}
		try {
			SOrg orgObj = QueryCache.get(SOrg.class,orgId);
			List<String> postIds = orgObj.getPostList().getListById();
/*			if(postIds == null || postIds.size() == 0){
				setMessage(Messages.getString("systemMsg.NoAddOrgPost"));
				return Action.ERROR;	
			}*/
			List<String> postIdList = new ArrayList<String>();
			for(String postId : postIds){
				postIdList.add(postId.trim());
			}
			QueryCache qc =  null;
			if(postIds!=null && postIds.size()>0){
				qc = new QueryCache("select a.uuid from SPost a where a.uuid not in (:postIds) " + getWhere()).setParameter("postIds", postIdList); 
			}else{
				qc = new QueryCache("select a.uuid from SPost a " + getWheres());
			}
			setWhere(qc);
			Long postCount ;
			QueryCache qs =  null;
			if(postIds!=null && postIds.size()>0){
				qs = new QueryCache("select count(a.uuid) from SPost a where a.uuid not in (:postIds) ").setParameter("postIds", postIdList); 
			}else{
				qs = new QueryCache("select count(a.uuid) from SPost a ");
			}
			postCount = (Long)qs.uniqueResult();
			if(postCount == null || postCount == 0){
				setMessage(Messages.getString("systemMsg.NoAddPost"));
				return Action.ERROR;	
			}
			page = qc.page(page); 
			List<SPost> sPostlist= QueryCache.idToObj(SPost.class, page.getResults());
			page.setResults(sPostlist);	
			return Action.SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" and 1=1 ");
		if(StringHelper.isNotEmpty(postname) )
			sb.append(" and a.name like :postName ");
		return sb.toString();
	}
	private String getWheres(){
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if(StringHelper.isNotEmpty(postname) )
			sb.append(" and a.name like :postName ");
		return sb.toString();
	}
	public void setWhere(QueryCache qc) {
		if(StringHelper.isNotEmpty(postname))
			qc.setParameter("postName",  "%" + postname.trim() + "%");
	}
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
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
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getPostRemark() {
		return postRemark;
	}
	public void setPostRemark(String postRemark) {
		this.postRemark = postRemark;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public SOrg getItem() {
		return item;
	}
	public void setItem(SOrg item) {
		this.item = item;
	}
	
}

