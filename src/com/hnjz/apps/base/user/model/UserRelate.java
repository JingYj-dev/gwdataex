package com.hnjz.apps.base.user.model;

/**
 * SUserRoleId entity. @author MyEclipse Persistence Tools
 */

public class UserRelate implements java.io.Serializable {

	// Fields
	private String uuid;
	private String userId;
	private String relateId;
	private String relateType;//1人2岗位
	private String relateOrgId;

	// Constructors

	/** default constructor */
	public UserRelate() {
	}

	/** full constructor */
	public UserRelate(String uuid, String userId, String relateId,
			String relateType, String relateOrgId) {
		this.uuid = uuid;
		this.userId = userId;
		this.relateId = relateId;
		this.relateType = relateType;
		this.relateOrgId = relateOrgId;
	}

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public String getRelateType() {
		return relateType;
	}

	public void setRelateType(String relateType) {
		this.relateType = relateType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRelateOrgId() {
		return relateOrgId;
	}

	public void setRelateOrgId(String relateOrgId) {
		this.relateOrgId = relateOrgId;
	}
	
}