package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.base.common.upload.FileInfo;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysAttachment;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.UUID;

public class UploadOfficialDocSys extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UploadOfficialDocSys.class);
	public File uploadFile;

	public UploadOfficialDocSys() {
		
	}
	
	@Override
	protected String adminGo() {
		try {
			/*if(!checkItem()){
				result = Ajax.xmlResult(1,"先保存后上传!");
				return Action.ERROR;
			}*/
			//checkExt(StringHelper.getFileExt(uploadFile.getName()));
			//checkSize(uploadFile.length());
			FileInfo fi = saveFile();
			saveDB(fi);
			//result = Ajax.JSONResult(0, Messages.getString("systemMsg.success"), fi);
			result = Ajax.xmlResult(Environment.RESULT_CODE_SUCCESS, fi.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success")));
			return Action.SUCCESS;
		} catch (Exception ex) {
			String msg = ex.getMessage();
			log.error(msg,ex);
			result = Ajax.xmlResult(1, msg);
			return Action.ERROR;
		} 
	}
	
	/**
	 * 将上传的公文包信息保存到附件表中
	 * @param fi
	 * @throws Exception
	 */
	public void saveDB(FileInfo fi) throws Exception {
		if (fi == null) {
			throw new NullPointerException("FileInfo must not be null");
		}
		TransactionCache tx = null;
		try {
			DataexSysAttachment attachment = new DataexSysAttachment();
			String uuid = UuidUtil.getUuid();
			attachment.setUuid(uuid);
			attachment.setServerId(fi.getServerId() + "");
			attachment.setPath(fi.getRelativePath() + File.separator + fi.getFileName());
			//真实文件名称
			attachment.setFileName(fi.getDescription());
			tx = new TransactionCache();
			tx.save(attachment);
			tx.commit();
			fi.setSid(uuid);
			fi.setPath(fi.getFilePath() + File.separator + fi.getFileName());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			if(fi != null){
				FileUtils.forceDelete(new File(fi.getFilePath() + File.separator + fi.getFileName()));
			}
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
	 * 校验条目是否存在
	 * @return
	 * @throws Exception
	 */
	protected boolean checkItem() throws Exception{
		return true;
	}
	
	public Integer imageAttach() {
		return new Integer(Constants.RECV_SYS_DIR_ID);
	};
	
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
		String fileName = fi.getFilePath() + File.separator + fi.getFileName();
		FileUtils.copyFile(uploadFile, new File(fileName));
		return fi;
	}
	
	public File getUploadFile() {
		return uploadFile;
	}
	
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

}


