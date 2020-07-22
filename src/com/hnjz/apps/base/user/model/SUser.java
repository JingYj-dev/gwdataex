package com.hnjz.apps.base.user.model;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.model.IUser;
import com.hnjz.db.query.JoinList;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Json.Unjson;
import com.hnjz.util.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SUser entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public  class SUser implements java.io.Serializable,IUser {
	// Fields
	private String uuid;
	private String realName;
	private String loginName;
	private String reviewPwd;
	private String password;
	private String sex;
	private String mobile;
	private String phone;
	private String email;
	private String userType;
	private Integer orderNum;
	private String delFlag;
	private String openFlag;
	private Date issueDate;
	private String issueId;
	private String issueName;
	private Date editDate;
	private String remark;
	private Date lastLoginTime;
	private Integer totalLoginCount;
	private Integer failedLoginCount;
	private String orgId;//reference
	private String orgName;
	private Date editPwdTime;
	private String activeStatus;
	private Date activeDeadLine;
	private String secLevel;
	private String skinId;
	
	private String realUserId;
	//证书
	private byte[] cert;
	
	// Constructors
	//功能列表
	private java.util.Set<String>  functions = new java.util.HashSet<String>();
	//功能点列表
	private java.util.Set<String>  funcActions = new java.util.HashSet<String>();
	
	private transient JoinList postList = null;
	private transient JoinList roleList = null;
	/** default constructor */
	public SUser() {
	}
	public boolean getAdmin(){
		return BaseEnvironment.USERTYPE_ADMIN.equals(userType) || BaseEnvironment.USERTYPE_SECURITY.equals(userType) 
			|| BaseEnvironment.USERTYPE_AUDITOR.equals(userType);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getTotalLoginCount() {
		return totalLoginCount;
	}

	public void setTotalLoginCount(Integer totalLoginCount) {
		this.totalLoginCount = totalLoginCount;
	}

	public Integer getFailedLoginCount() {
		return failedLoginCount;
	}

	public void setFailedLoginCount(Integer failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}	
	
	public java.util.Set<String> getFunctions() {
		return functions;
	}

	public void setFunctions(java.util.Set<String> functions) {
		if(functions!=null){
			this.functions.clear();
			this.functions.addAll(functions);
		}
	}

	public java.util.Set<String> getFuncActions() {
		return funcActions;
	}

	public void setFuncActions(java.util.Set<String> funcActions) {
		//this.funcActions = funcActions;
		if(funcActions!=null){
			this.funcActions.clear();
			this.funcActions.addAll(funcActions);
		}
	}

	public String getUserId() {
		 return this.uuid;
	}

	public void setUserId(String uid) {
		this.uuid=uid;
		
	}

	public String getType() {
		return this.userType;
	}
	
	public String getOrganId() {		 
		return this.orgId;
	}
	
	public String getStatus() {		 
		return this.openFlag;
	}

	public String getReviewPwd() {
		return reviewPwd;
	}

	public void setReviewPwd(String reviewPwd) {
		this.reviewPwd = reviewPwd;
	}
	@Unjson
	public JoinList getPostList() {
		if(postList == null){
			QueryCache qc = new QueryCache("select a.postId from SUserPost a where a.userId=:userId")
			.setParameter("userId", uuid);
			postList = new JoinList(SPost.class, qc);
		}
		return postList;
	}
	@Unjson
	public JoinList getRoleList() {
		if(roleList == null){
			QueryCache qc = new QueryCache("select a.roleId from SUserRole a where a.userId=:userId")
			.setParameter("userId", uuid);
			roleList = new JoinList(SRole.class, qc);
		}
		return roleList;
	}
	public Date getEditPwdTime() {
		return editPwdTime;
	}

	public void setEditPwdTime(Date editPwdTime) {
		this.editPwdTime = editPwdTime;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getActiveDeadLine() {
		return activeDeadLine;
	}

	public void setActiveDeadLine(Date activeDeadLine) {
		this.activeDeadLine = activeDeadLine;
	}
	public String getOrgName() {
		SOrg org = null;
		if(StringHelper.isEmpty(orgId))
			org = new SOrg();
		else{
			org = QueryCache.get(SOrg.class, orgId); 
			orgName = org.getName();
		}
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public static String getUserPostName(String userId) {
		String postName = "";
		SUser item= QueryCache.get(SUser.class, userId);
        List<String> userPostList = item.getPostList().getListById();//用户目前分配的岗位列表
        List<String> postIdList = new ArrayList<String>();
        for(String postid:userPostList){
        	postIdList.add(postid.trim());
        	SPost post = QueryCache.get(SPost.class, postid.trim());
        	postName += post.getName();
        	postName += " ; ";
        }
        if(postName.length()>2){
	        String post = postName.substring(0, postName.length()-2);
	        return post;
        }else{
        	return postName;
        }
	}
	public String getSecLevel() {
		return secLevel;
	}
	public void setSecLevel(String secLevel) {
		this.secLevel = secLevel;
	}
	public String getSkinId() {
		return skinId;
	}
	public void setSkinId(String skinId) {
		this.skinId = skinId;
	}
	public String getRealUserId() {
		return realUserId;
	}
	public void setRealUserId(String realUserId) {
		this.realUserId = realUserId;
	}
	public byte[] getCert() {
		return cert;
	}
	public void setCert(byte[] cert) {
		this.cert = cert;
	}
	
}