package com.hnjz.apps.base.backup.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SDataBackup implements Serializable{

	//备份ID
	private String backupId;
	//备份名称
	private String backupName;
	//数据备份路径
	private String dataBakPath;
	//开始备份时间
	private Date backupStartTime;
	//备份完成时间
	private Date backupEndTime;
	//备份状态
	private String backupStatus;
	//压缩标识
	private String zipMark;
	//加密标识
	private String encryptMark;
	//加密密码
	private String encryptKey;
	//备份人ID
	private String backupCreator;
	//备份人名称
	private String backupCreatorName;
	//备注
	private String backupMemo;
	
	
	public String getBackupId() {
		return backupId;
	}
	public void setBackupId(String backupId) {
		this.backupId = backupId;
	}
	public String getBackupName() {
		return backupName;
	}
	public void setBackupName(String backupName) {
		this.backupName = backupName;
	}
	public String getDataBakPath() {
		return dataBakPath;
	}
	public void setDataBakPath(String dataBakPath) {
		this.dataBakPath = dataBakPath;
	}
	public Date getBackupStartTime() {
		return backupStartTime;
	}
	public void setBackupStartTime(Date backupStartTime) {
		this.backupStartTime = backupStartTime;
	}
	public Date getBackupEndTime() {
		return backupEndTime;
	}
	public void setBackupEndTime(Date backupEndTime) {
		this.backupEndTime = backupEndTime;
	}
	public String getBackupStatus() {
		return backupStatus;
	}
	public void setBackupStatus(String backupStatus) {
		this.backupStatus = backupStatus;
	}
	public String getZipMark() {
		return zipMark;
	}
	public void setZipMark(String zipMark) {
		this.zipMark = zipMark;
	}
	public String getEncryptMark() {
		return encryptMark;
	}
	public void setEncryptMark(String encryptMark) {
		this.encryptMark = encryptMark;
	}
	public String getEncryptKey() {
		return encryptKey;
	}
	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}
	public String getBackupCreator() {
		return backupCreator;
	}
	public void setBackupCreator(String backupCreator) {
		this.backupCreator = backupCreator;
	}
	public String getBackupCreatorName() {
		return backupCreatorName;
	}
	public void setBackupCreatorName(String backupCreatorName) {
		this.backupCreatorName = backupCreatorName;
	}
	public String getBackupMemo() {
		return backupMemo;
	}
	public void setBackupMemo(String backupMemo) {
		this.backupMemo = backupMemo;
	}
	
	
}
