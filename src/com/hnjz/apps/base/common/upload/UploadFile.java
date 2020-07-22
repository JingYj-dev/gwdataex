package com.hnjz.apps.base.common.upload;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.UUID;

/**
 * <action name="uploadNewsAttach" class="com.hnjz.apps.information.news.action.UploadNewsAttach" caption="上传文件">
         <result name="success" type="xml" />
			<result name="error" type="xml" />
            <interceptor-ref name="defaultStack" /> 
        	<interceptor-ref name="fileUploadStack" />  
	</action>
 * @author  FangWQ
 * @version v0.1  
 * @since   2014-5-26 下午03:51:30
 */
public class UploadFile extends AdminAction {
	private static Log log = LogFactory.getLog(UploadFile.class);
	public String uuid;
	public File uploadFile;
	public UploadFile(){}
	
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
	
	/**文件*/
	protected Integer imageAttach(){return new Integer(11);};
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
	 * 保存数据库
	 * @param fi
	 * @throws Exception
	 */
	protected void saveDB(FileInfo fi) throws Exception{}
	
	/**
	 * 校验条目是否存在
	 * @return
	 * @throws Exception
	 */
	protected boolean checkItem() throws Exception{
		return true;
	}
	@Override
	protected String adminGo() {
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
			result =Ajax.xmlResult(0, fi.JSONResult(0, Messages.getString("systemMsg.success")));
			return Action.SUCCESS;
		} catch (Exception ex) {
			String msg = ex.getMessage();
			log.error(msg,ex);
			result = Ajax.xmlResult(1, msg);
			return Action.ERROR;
		} 
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


