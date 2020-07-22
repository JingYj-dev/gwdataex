package com.hnjz.apps.oa.dataexsys.util;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.util.StringHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class DocAttachUtil {
	
	//下载公文附件到交换路径
    public static void downDocAttach(String path,String fileName,String zwFile) {

        try {
        	 File file = new File(path);
             FileInputStream is = new FileInputStream(file);
             
             //附件输出路径
             String filepath = "";
             int nServerId = Integer.parseInt(DocConstants.DOWN_DIR_ID);//下载所有公文文件路径  ;
             if (StringHelper.isEmpty(fileName)) {
         		 filepath = AttachItem.filePath(nServerId)+zwFile+"/"+file.getName();   		 
			 }else{
        		 filepath = AttachItem.filePath(nServerId)+zwFile+"/"+fileName;   		 
			 }
             File newFile = new File(filepath);
             if (newFile.exists()) {
            	 newFile.delete();
			 } else {
				 if (!newFile.getParentFile().exists()) {
					 newFile.getParentFile().mkdirs();
				 }
			 }
             FileOutputStream os = new FileOutputStream(newFile);
           
             byte[] b = new byte[(int) file.length()];
             int len;
             while ((len = is.read(b))!=-1 ) {
            	 os.write(b,0,len);
            	 os.flush();
			}
             is.close();
             os.close();
                     
        }catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
  //压缩公文附件到交换路径
    public static void zipDocAttach(String sourceFilePath,String zwFile) throws Exception {

        try {            
             //文件输出路径
             String fileName = DocConstants.DOCZIPNAME+".zip"; //公文压缩包名称
             int nServerId = Integer.parseInt(DocConstants.SEND_DIR_ID);//发送公文zip包路径
             String zipFilePath = AttachItem.filePath(nServerId)+zwFile+"/" +fileName;
             File newFile = new File(zipFilePath);
             if (newFile.exists()) {
            	 newFile.delete();
			 } else {
				 if (!newFile.getParentFile().exists()) {
					 newFile.getParentFile().mkdirs();
				 }
			 }
             //压缩文件
             ZipUtil.writeByApacheZipOs(sourceFilePath, zipFilePath, "");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public static void main(String[] args) {
//    	downDocAttach("D:/mnt/nas/cssoa/cssoa/webapps/cssoa/upload/fw/attach/0/5504403c-0c31-4b32-b131-202eeef6006c.txt","");
    	try {
//			zipDocAttach("D:/mnt/nas/cssoa/cssoa/webapps/cssoa/upload/docdataex/docfiles/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
