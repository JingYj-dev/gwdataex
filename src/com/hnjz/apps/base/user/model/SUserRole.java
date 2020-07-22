package com.hnjz.apps.base.user.model;

/**
 * SUserRoleId entity. @author MyEclipse Persistence Tools
 */

public class SUserRole implements java.io.Serializable {

	// Fields

	private String uuid;
	private String roleId;
	private String userId;
	private String sysId;

	// Constructors

	/** default constructor */
	public SUserRole() {
	}

	/** minimal constructor */
	public SUserRole(String roleId, String userId) {
		this.roleId = roleId;
		this.userId = userId;
	}

	/** full constructor */
	public SUserRole(String roleId, String userId, String sysId) {
		this.roleId = roleId;
		this.userId = userId;
		this.sysId = sysId;
	}

	// Property accessors

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysId() {
		return this.sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SUserRole))
			return false;
		SUserRole castOther = (SUserRole) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(
				castOther.getRoleId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())))
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
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getSysId() == null ? 0 : this.getSysId().hashCode());
		return result;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
