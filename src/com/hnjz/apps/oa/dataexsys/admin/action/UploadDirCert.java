package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.base.common.upload.FileInfo;
import com.hnjz.apps.base.common.upload.UploadFile;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexAttachment;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.UUID;

public class UploadDirCert extends AdminAction{

	private static final long serialVersionUID = 1L;


	public UploadDirCert(){}
	private String attach;
	
	private static Log log = LogFactory.getLog(UploadFile.class);
	public String uuid;
	public File uploadFile;
	
	
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(!checkItem()){
				result = Ajax.xmlResult(1,"先保存后上传!");
				return Action.ERROR;
			}
			checkExt(StringHelper.getFileExt(uploadFile.getName()));
			checkSize(uploadFile.length());
			FileInfo fi = saveFile();
			saveDB(fi);
			//result = Ajax.JSONResult(0, Messages.getString("systemMsg.success"), fi);
			
			//将证书URL插入数据库
			DataexSysNode item = QueryCache.get(DataexSysNode.class,uuid);
			if(item == null){
				result = Ajax.xmlResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
		 	}
			tx = new TransactionCache();
			DataexSysNode exnode = QueryCache.get(DataexSysNode.class, item.getExnodeId());
			exnode.setCertPath(fi.getFilePath()+"/"+fi.getFileName());
			tx.update(exnode);
			tx.commit();
			result =Ajax.xmlResult(0, fi.JSONResult(0, Messages.getString("systemMsg.success")));
			return Action.SUCCESS;
		} catch (Exception ex) {
			if(tx != null){
				tx.rollback();
			}
			String msg = ex.getMessage();
			log.error(msg,ex);
			result = Ajax.xmlResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.failed"));
			return Action.ERROR;
		} 
	}
	
	/**校验文件格式*/
	protected void checkExt(String sExt) throws Exception{
		checkFileExt(sExt);
	}
	public final void checkFileExt(String sExt) throws Exception{
	}
	public final void checkImgExt(String sExt) throws Exception{
		String ext = DictMan.getDictType("d_para_g", "4").getName();
		if (sExt.equals("") || ext.indexOf(sExt) < 0) {
			throw new Exception(Messages.getString("systemMsg.invalidFileExt"));
		}
	}
	/**校验文件大小*/
	protected void checkSize(long sSize) throws Exception{
		checkFileSize(sSize);
	}
	public final void checkImgSize(long sSize) throws Exception{
		long size = 1000 * Integer.parseInt(DictMan.getDictType("d_para_g", "5").getName());
		if ( sSize > size) {
			throw new Exception(Messages.getString("systemMsg.totalMaxSize"));
		}
	}
	public final void checkFileSize(long sSize) throws Exception{
		long size = 1000 * 1000 * Integer.parseInt(DictMan.getDictType("d_para_g", "9").getName());
		if ( sSize > size) {
			throw new Exception(Messages.getString("systemMsg.totalMaxSize"));
		}
	}
	
	/**文件信息*/
	protected FileInfo getFileInfo (){
		return AttachItem.getAttach(imageAttach());
	}
	/**
	 * 保存文件
	 * @return
	 * @throws Exception
	 */
	protected FileInfo saveFile() throws Exception{
		FileInfo fi = getFileInfo();
		String sExt = StringHelper.getFileExt(uploadFile.getName());
		if (fi == null) {
			throw new Exception(Messages.getString("systemMsg.invalidFilePath"));
		}
		fi.setDescription(uploadFile.getName());
		fi.setFileName(UUID.randomUUID() + "." + sExt);
		fi.setExtension(sExt);
		fi.setFileSize(uploadFile.getTotalSpace());
		String fileName = fi.getFilePath() + "/" + fi.getFileName();
		FileUtils.copyFile(uploadFile, new File(fileName));
		return fi;
	}

	/**
	 * 校验条目是否存在
	 * @return
	 * @throws Exception
	 */
	protected boolean checkItem() throws Exception{
		return true;
	}

	public Integer imageAttach(){return new Integer(Constants.UPLOAD_SYS_DIR_CERT_ID);};
	
	/**
	 * 保存数据库
	 * @param fi
	 * @throws Exception
	 */
	public void saveDB(FileInfo fi) throws Exception{
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			String uuid = UuidUtil.getUuid();
			DataexAttachment attachment = new DataexAttachment();
			attachment.setUuid(uuid);
			attachment.setServerId(fi.getServerId() + "");
			attachment.setPath(fi.getRelativePath() + "/" + fi.getFileName());
			//真实文件名称
			attachment.setFileName(fi.getDescription());
			tx.save(attachment);
			tx.commit();
			fi.setSid(uuid);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			if(fi != null){
				FileUtils.forceDelete(new File(fi.getFilePath() + "/" + fi.getFileName()));
			}
			throw ex;
		} 
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
}
