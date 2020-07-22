package com.hnjz.apps.base.sys.model;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.util.HtmlConverter;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.user.model.SUserRole;

import java.util.List;

/**
 * SSys entity. @author MyEclipse Persistence Tools
 */
public class SSys implements java.io.Serializable {
	// Fields
	private String className = SSys.class.getName();
	private String uuid;
	private String name;
	private String url;
	private String remark;
	private String openFlag;
	private String sysId;
	private List<SRole> roleList;//reference
	private List<SUserRole> userrolelist;

	// Constructors

	/** default constructor */
	public SSys() {
	}
	public String getLogInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(HtmlConverter.appendHtmlNameField("主键", this.getUuid()));
		sb.append(HtmlConverter.appendHtmlNameField("名称", this.getName()));
		sb.append(HtmlConverter.appendHtmlNameField("登录地址", this.getUrl()));
		sb.append(HtmlConverter.appendHtmlNameField("状态", StringHelper.isNotEmpty(this.getOpenFlag()) ? DictMan.getDictType("d_openflag", this.getOpenFlag()).getName() : ""));
		sb.append(HtmlConverter.appendHtmlNameField("备注", this.getRemark()));
		sb.append(HtmlConverter.appendHtmlNameField("系统ID", this.getSysId()));
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


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<SRole> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<SRole> roleList) {
		this.roleList = roleList;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public List<SUserRole> getUserrolelist() {
		return userrolelist;
	}
	public void setUserrolelist(List<SUserRole> userrolelist) {
		this.userrolelist = userrolelist;
	}

	
	
}