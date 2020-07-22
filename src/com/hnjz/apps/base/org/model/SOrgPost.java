package com.hnjz.apps.base.org.model;

/**
 * SOrgPostId entity. @author MyEclipse Persistence Tools
 */

public class SOrgPost implements java.io.Serializable {

	// Fields
	private String uuid;
	private String orgId;
	private String postId;

	// Constructors

	/** default constructor */
	public SOrgPost() {
	}

	/** full constructor */
	public SOrgPost(String orgId, String postId) {
		this.orgId = orgId;
		this.postId = postId;
	}

	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SOrgPost))
			return false;
		SOrgPost castOther = (SOrgPost) other;

		return ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null
				&& castOther.getOrgId() != null && this.getOrgId().equals(
				castOther.getOrgId())))
				&& ((this.getPostId() == castOther.getPostId()) || (this
						.getPostId() != null
						&& castOther.getPostId() != null && this.getPostId()
						.equals(castOther.getPostId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result
				+ (getPostId() == null ? 0 : this.getPostId().hashCode());
		return result;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
