package com.hnjz.apps.base.log.model;

import com.hnjz.apps.base.log.service.LogMan;
import com.hnjz.util.StringHelper;

@SuppressWarnings("serial")
public class LogPart implements java.io.Serializable,Cloneable{
	// 操作类型
	private Integer opType;
	// 操作对象ID
	private String opObjId;
	// 操作对象类型
	private String opObjType;
	// 关联对象ID
	private String relObjId;
	// 关联对象类型
	private String relObjType;
	// 操作内容
	private String logData;
    //日志级别
	private Integer logLevel=0;
	//操作结果
	private String result;
	//操作描述
	private String memo;
	//用户ID
	private String opId;
	//用户名称
	private String opName;
	
	public Integer getOpType() {
		return opType;
	}

	public LogPart setOpType(Integer opType) {
		this.opType = opType;
		return this;
	}
	 
	public String getOpObjId() {
		return opObjId;
	}

	public LogPart setOpObjId(String opObjId) {
		this.opObjId = opObjId;
		return this;
	}

	public String getOpObjType() {
		return opObjType;
	}

	public LogPart setOpObjType(String opObjType) {
		this.opObjType = opObjType;
		return this;
	}

	public String getRelObjId() {
		return relObjId;
	}

	public LogPart setRelObjId(String relObjId) {
		this.relObjId = relObjId;
		return this;
	}

	public String getRelObjType() {
		return relObjType;
	}

	public LogPart setRelObjType(String relObjType) {
		this.relObjType = relObjType;
		return this;
	}

	public String getLogData() {
		return logData;
	}

	public LogPart setLogData(String logData) {
		this.logData = logData;
		return this;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public LogPart setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	public String getResult() {
		return result;
	}

	public LogPart setResult(String result) {
		this.result = result;
		return this;
	}

	public String getMemo() {
		return memo;
	}

	public LogPart setMemo(String memo) {
		this.memo = memo;
		return this;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		LogPart lp=(LogPart)super.clone();
		lp.setLogData(logData);
		lp.setLogLevel(logLevel);
		lp.setOpObjId(opObjId);
		lp.setOpObjType(opObjType);
		lp.setOpType(opType);
		lp.setRelObjId(relObjId);
		lp.setRelObjType(relObjType);
		lp.setResult(result);
		lp.setMemo(memo);
		return lp;
	}
	
	/**
	 * 记录日志
	 * @return
	 */
	public boolean save(){
		return LogMan.addLogPart(this);
	}
	
	/**
	 * 如果操作对象ID或操作内容不为空，则记录日志
	 * @return
	 */
	public boolean saveNotEmpty(){
		if(StringHelper.isNotEmptyByTrim(opObjId) || StringHelper.isNotEmptyByTrim(logData)){
			return LogMan.addLogPart(this);
		}
		return true;
	}

	public String getOperId() {
		return opId;
	}

	public LogPart setOperId(String opId) {
		this.opId = opId;
		return this;
	}

	public String getOperName() {
		return opName;
	}

	public LogPart setOperName(String opName) {
		this.opName = opName;
		return this;
	}	
}
