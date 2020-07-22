/*
 * Created on 2006-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.dict.model;

import com.hnjz.core.model.IDictable;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class SDict  implements Serializable,IDictable {
	private String uuid;
	private String parentId;
	private String tableName;
	private String code;
	private String name;
	private String remark;
	private Integer orderNum;
	private List<SDict> children;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public List<SDict> getChildren() {
		return children;
	}
	public void setChildren(List<SDict> children) {
		this.children = children;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getItemCode() {
		return this.code;
	}
	public int getIntCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getItemName() {
		return this.name;
	}
}
