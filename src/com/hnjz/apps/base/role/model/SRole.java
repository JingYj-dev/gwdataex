package com.hnjz.apps.base.role.model;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.model.IRole;
import com.hnjz.db.query.JoinList;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.HtmlConverter;
import com.hnjz.util.StringHelper;

//import com.hnjz.apps.schd.model.SchdTypeSequence;


/**
 * SRole entity. @author MyEclipse Persistence Tools
 */

public class SRole implements  IRole,java.io.Serializable {

	// Fields
	private String className = SRole.class.getName();
	private String uuid;
	private String name;
	private String roleType;
	private String sysId;
	private String delFlag;
	private String openFlag;
	private String remark;

	
	private transient JoinList roleFuncList = null;
	// Constructors

	/** default constructor */
	public SRole() {
	}

	public String egetLogInfo() {
		StringBuffer sb = new StringBuffer();
		 sb.append(HtmlConverter.appendHtmlNameField("主键", this.getUuid()));
		sb.append(HtmlConverter.appendHtmlNameField("名称", this.getName()));
		sb.append(HtmlConverter.appendHtmlNameField("角色类型",StringHelper.isNotEmpty(this.getRoleType()) ? DictMan.getDictType("d_roletype", this.getRoleType()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("系统id", this.getSysId()));
		sb.append(HtmlConverter.appendHtmlNameField("删除标记", StringHelper.isNotEmpty(this.getDelFlag()) ?  DictMan.getDictType("d_dealflag", this.getDelFlag()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("开启标记", StringHelper.isNotEmpty(this.getOpenFlag()) ? DictMan.getDictType("d_openflag", this.getOpenFlag()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("备注",this.getRemark()));
 
		return sb.toString();
	}
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

 
	public String getRoleId() {	 
		return this.uuid;
	}
 
	public String getRoleName() {	 
		return this.name;
	}
	
	public String getSystemId() {
		return this.sysId;
	}

	public JoinList getRoleFuncList() {
		  if("2".equals(openFlag)){
			  return null;
		  }
		  if(roleFuncList == null){
			  QueryCache qc = new QueryCache(
				"select r.funcId from SRoleFunc r where r.roleId = :roleId ")
				.setParameter("roleId", uuid);
			  roleFuncList = new JoinList(SRoleFunc.class, qc);
		  }
		return roleFuncList;
	}

	
}