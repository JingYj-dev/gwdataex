package com.hnjz.apps.base.func.model;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.model.IFunction;
import com.hnjz.db.query.JoinList;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.HtmlConverter;
import com.hnjz.util.StringHelper;
import net.sf.json.JSONObject;


/**
 * SFunc entity. @author MyEclipse Persistence Tools
 */

public class SFunc implements IFunction,java.io.Serializable {

	// Fields
	private String className = SFunc.class.getName();
	private String uuid;
	private String funcId;
	private String name; 
	private String funcType;
	private String operType;
	private String logLevel;
	private String parentId;
	private String url;
	private Integer orderNum;
	private String openFlag;
	private String sysId;
	private String delFlag;
	private String remark;
	// Constructors
	private transient JoinList funcActionList = null;
	/** default constructor */
	public SFunc() {
	}

	public String getLogInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(HtmlConverter.appendHtmlNameField("主键", this.getUuid()));
		sb.append(HtmlConverter.appendHtmlNameField("功能ID", this.getFuncId()));
		sb.append(HtmlConverter.appendHtmlNameField("名称", this.getName()));
		sb.append(HtmlConverter.appendHtmlNameField("功能类型", StringHelper.isNotEmpty(this.getFuncType()) ? DictMan.getDictType("d_functype", this.getFuncType()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("URL", this.getUrl()));
		sb.append(HtmlConverter.appendHtmlNameField("父级ID", this.getParentId()));
		sb.append(HtmlConverter.appendHtmlNameField("系统id", this.getSysId()));
		sb.append(HtmlConverter.appendHtmlNameField("开启标记", StringHelper.isNotEmpty(this.getOpenFlag()) ? DictMan.getDictType("d_openflag", this.getOpenFlag()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("删除标记", StringHelper.isNotEmpty(this.getDelFlag()) ? DictMan.getDictType("d_dealflag", this.getDelFlag()).getName() : ""));
		sb.append(HtmlConverter.appendHtmlNameField("日志级别", StringHelper.isNotEmpty(this.getLogLevel()) ? DictMan.getDictType("d_loglevel", this.getLogLevel()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("操作类型", StringHelper.isNotEmpty(this.getOperType()) ? DictMan.getDictType("d_opertype", this.getOperType()).getName():""));
		sb.append(HtmlConverter.appendHtmlNameField("备注",this.getRemark()));
		return sb.toString();
	}
	/** minimal constructor */
	public SFunc(String uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 转换为单个json字符串
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		return new JSONObject().element("uuid", uuid).element("funcId", funcId).element("name", name)
				.element("funcType", funcType).element("operType", operType)
				.element("logLevel", logLevel).element("url", url)
				.element("parentId", parentId).element("orderNum", orderNum)
				.element("delFlag", delFlag).element("openFlag", openFlag)
				.element("remark", remark).element("sysId", sysId);
	}

	public String getFuncCode() {		 
		return this.funcId;
	}

	public String getFuncName() {
		 
		return this.name;
	}

	public String getLeafMark() {
		return this.remark;
	}

	public String getParentCode() {
		return this.parentId;
	}

	public Double getShowOrder() {
		if(this.orderNum==null)
			this.orderNum=0;
		return Double.valueOf(this.orderNum);
	}

	public Short getStatus() {
		if(StringHelper.isEmpty(this.delFlag))
			this.delFlag = "2";
		return Short.decode(this.delFlag);
	}

	public String getSystemId() {
		return this.sysId;
	}

	public JoinList getFuncActionList() {
		  if(funcActionList == null){
			  QueryCache qc = new QueryCache(
				"select r.actionCode from SFuncAction r where r.funcCode = :funcCode ")
				.setParameter("funcCode", uuid);
			  funcActionList = new JoinList(SFuncAction.class, qc);
		  }
		return funcActionList;
	}
	
}