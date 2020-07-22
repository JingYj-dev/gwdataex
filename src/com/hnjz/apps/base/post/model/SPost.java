package com.hnjz.apps.base.post.model;

import com.hnjz.core.model.IPost;
import com.hnjz.db.query.JoinList;
import com.hnjz.db.query.QueryCache;
import com.hnjz.apps.base.role.model.SRole;

/**
 * SPost entity. @author MyEclipse Persistence Tools
 */

public class SPost implements IPost,java.io.Serializable {

	// Fields

	private String uuid;
	private String name;
	private String remark;
	private transient JoinList postRoleList = null;

	// Constructors

	/** default constructor */
	public SPost() {
	}

	/** minimal constructor */
	public SPost(String uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	/** full constructor */
	public SPost(String uuid, String name, String remark) {
		this.uuid = uuid;
		this.name = name;
		this.remark = remark;
	}

	// Property accessors

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

 
	public String getPostId() {  
		return this.uuid;
	}


	public String getPostName() {
		return this.name;
	}
	
	public JoinList getPostRoleList() {
		  if(postRoleList == null){
			  QueryCache qc = new QueryCache(
				"select r.roleId from SPostRole r where r.postId = :postId ")
				.setParameter("postId", uuid);
			  postRoleList = new JoinList(SRole.class, qc);
		  }
		return postRoleList;
	}

}
