package com.hnjz.apps.oa.dataexsys.admin.model;

import net.sf.json.JSONObject;

import java.util.Date;

public class DataexSysDir {
	
	// Fields
	private String uuid; //节点标识
//	private String identityId; //此字段未用到
	private String dirName; //节点名称
	private String dirOrg; //机构编码
	private String parentId; //父节点标识
//	private String dirIp; //交换系统ip
//	private String dirProvider; //提供商
//	private String dataServiceUrl; //数据服务地址
//	private String dirType; //节点类型
	private String dirStatus; //节点状态
//	private String certPath; //证书路径
	private Date createdTime; //创建时间

//	private String compress; //压缩
//	private String signature; //签名
//	private String encryption; //加密
	
	private String exnodeId;
	
	private String realOrgId;
	private String appid;//用户方正交换，appid表主键
	
	// Constructors

	/** default constructor */
	public DataexSysDir() {
	}

	/** minimal constructor */
	public DataexSysDir(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 转换为单个json字符串
	 * @return
	 */
	public JSONObject toJson() {
		return new JSONObject().element("uuid", uuid).element("dirName", dirName)
				.element("parentId", parentId);
	}


	// Property accessors
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getDirOrg() {
		return dirOrg;
	}

	public void setDirOrg(String dirOrg) {
		this.dirOrg = dirOrg;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getDirStatus() {
		return dirStatus;
	}

	public void setDirStatus(String dirStatus) {
		this.dirStatus = dirStatus;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/*public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}*/

	public String getExnodeId() {
		return exnodeId;
	}

	public void setExnodeId(String exnodeId) {
		this.exnodeId = exnodeId;
	}

	public String getRealOrgId() {
		return realOrgId;
	}

	public void setRealOrgId(String realOrgId) {
		this.realOrgId = realOrgId;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	
}
