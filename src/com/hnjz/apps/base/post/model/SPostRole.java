package com.hnjz.apps.base.post.model;

/**
 * SUserRoleId entity. @author MyEclipse Persistence Tools
 */

public class SPostRole implements java.io.Serializable {

	// Fields

	private String uuid;
	private String roleId;
	private String postId;
	private String sysId;

	// Constructors

	/** default constructor */
	public SPostRole() {
	}

	/** minimal constructor */
	public SPostRole(String roleId, String postId) {
		this.roleId = roleId;
		this.postId = postId;
	}

	/** full constructor */
	public SPostRole(String roleId, String postId, String sysId) {
		this.roleId = roleId;
		this.postId = postId;
		this.sysId = sysId;
	}

	// Property accessors

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SPostRole))
			return false;
		SPostRole castOther = (SPostRole) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(
				castOther.getRoleId())))
				&& ((this.getPostId() == castOther.getPostId()) || (this
						.getPostId() != null
						&& castOther.getPostId() != null && this.getPostId()
						.equals(castOther.getPostId())))
				&& ((this.getSysId() == castOther.getSysId()) || (this
						.getSysId() != null
						&& castOther.getSysId() != null && this.getSysId()
						.equals(castOther.getSysId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getPostId() == null ? 0 : this.getPostId().hashCode());
		result = 37 * result
				+ (getSysId() == null ? 0 : this.getSysId().hashCode());
		return result;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
