package com.hnjz.apps.base.user.model;

/**
 * SUserPostId entity. @author MyEclipse Persistence Tools
 */

public class SUserPost implements java.io.Serializable {

	// Fields
	private String uuid;
	private String postId;
	private String userId;
	private String orgId;

	// Constructors

	/** default constructor */
	public SUserPost() {
	}

	/** full constructor */
	public SUserPost(String postId, String userId,String orgId) {
		this.postId = postId;
		this.userId = userId;
		this.orgId = orgId;
	}

	// Property accessors

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SUserPost))
			return false;
		SUserPost castOther = (SUserPost) other;

		return ((this.getPostId() == castOther.getPostId()) || (this
				.getPostId() != null
				&& castOther.getPostId() != null && this.getPostId().equals(
				castOther.getPostId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPostId() == null ? 0 : this.getPostId().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		return result;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
