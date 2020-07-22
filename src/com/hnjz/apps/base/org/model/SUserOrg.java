package com.hnjz.apps.base.org.model;

/**
 * SUserOrgId entity. @author MyEclipse Persistence Tools
 */

public class SUserOrg implements java.io.Serializable {

	// Fields
	private String uuid;
	private String orgId;
	private String userId;

	// Constructors

	/** default constructor */
	public SUserOrg() {
	}

	/** full constructor */
	public SUserOrg(String orgId, String userId) {
		this.orgId = orgId;
		this.userId = userId;
	}

	// Property accessors

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
		if (!(other instanceof SUserOrg))
			return false;
		SUserOrg castOther = (SUserOrg) other;

		return ((this.getOrgId() == castOther.getOrgId()) || (this.getOrgId() != null
				&& castOther.getOrgId() != null && this.getOrgId().equals(
				castOther.getOrgId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
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

}
