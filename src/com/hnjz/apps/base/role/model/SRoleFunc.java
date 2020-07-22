package com.hnjz.apps.base.role.model;

import com.hnjz.db.query.QueryCache;

import java.util.List;

/**
 * SRoleFuncId entity. @author MyEclipse Persistence Tools
 */

public class SRoleFunc implements java.io.Serializable {

	// Fields
	private String uuid;
	private String roleId;
	private String funcId;

	// Constructors

	/** default constructor */
	public SRoleFunc() {
	}

	/** full constructor */
	public SRoleFunc(String roleId, String funcId) {
		this.roleId = roleId;
		this.funcId = funcId;
	}

	// Property accessors

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getFuncId() {
		return this.funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SRoleFunc))
			return false;
		SRoleFunc castOther = (SRoleFunc) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(
				castOther.getRoleId())))
				&& ((this.getFuncId() == castOther.getFuncId()) || (this
						.getFuncId() != null
						&& castOther.getFuncId() != null && this.getFuncId()
						.equals(castOther.getFuncId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getFuncId() == null ? 0 : this.getFuncId().hashCode());
		return result;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public static List<SRoleFunc> getRoleFuncByfuncId(String funcId) {
		QueryCache qc = new QueryCache(
				" select a.uuid from SRoleFunc a where a.funcId=:funcId ")
				.setParameter("funcId", funcId);
		return QueryCache.idToObj(SRoleFunc.class, qc.list());
	}
}
