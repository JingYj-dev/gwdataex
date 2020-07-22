/*
 * Created on 2006-7-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.common.upload;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.configuration.Environment;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author Administrator TODO To change the template for this generated Attach
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class AttachItem {
	private static Log log = LogFactory.getLog(AttachItem.class);
	public static String attachPath = "attachPath";
	private int serverId;

	public static String filePath(Integer serverId){
		SDict item = DictMan.getDictType("d_serverid", serverId.toString());
		if(item == null || StringHelper.isEmpty(item.getRemark())){return "";}
		String filepath = item.getRemark();
		return filepath.replace("\\", "/");
	}
	public static String urlPath(Integer serverId){
		SDict item = DictMan.getDictType("d_serverid", serverId.toString());
		if(item == null || StringHelper.isEmpty(item.getRemark())){return "";}
		String filepath = item.getItemName();
		return filepath.replace("\\", "/");
	}
	public static String getAttachImp(Integer id) {
		try {
			String setting = DictMan.getDictType("d_para_g", id.toString()).getName();
			String[] val = setting.split("@");
			int serverId = Integer.parseInt(val[0]);
			int maxNum = Integer.parseInt(val[1]);
			String path = DictMan.getDictType("d_serverid", String.valueOf(serverId)).getRemark();
			File dir = new File(path);
			if (!dir.exists()){
				if(!dir.mkdirs())return null;
			}
			int i = getFolder(path, 0, maxNum);
			return path + i;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}
	}
	public static int getFolder(String dictPath, int start, int maxNum) {
		int i = start;
		File dir = null;
		while (true) {
			String tmp = dictPath + i;
			dir = new File(tmp);
			if (!dir.exists()) {
				dir.mkdir();
				break;
			} else {
				if(dir.listFiles().length < maxNum)
					break;
			}
			i++;
		}
		return i;
	}
	public static FileInfo getAttach(Integer id) {
		try {
			String path = getAttachCache(id);
			if (path == null)
				return null;
			File dir = new File(path);
			if (!dir.exists()){
				if(!dir.mkdirs())return null;
			}
			FileInfo fi = new FileInfo();
			String setting = DictMan.getDictType("d_para_g", id.toString()).getName();
			String[] val = setting.split("@");
			int serverId = Integer.parseInt(val[0]);
			int maxNum = Integer.parseInt(val[1]);
			SDict dt = DictMan.getDictType("d_serverid", String.valueOf(serverId));
			fi.setDictPath(dt.getRemark());
			fi.setDictUrl(dt.getItemName());
			fi.setMaxNum(maxNum);
			fi.setServerId(serverId);
			int start = -1;
			if (path.indexOf(dt.getRemark()) < 0) // 字典有更新
				start = 0;
			if (start < 0 && dir.listFiles().length < maxNum) {
				int index = path.lastIndexOf("/");
				start = Integer.parseInt(path.substring(index + 1));
				fi.setFilePath(path);
				fi.setRelativePath(start + "");
				return fi;
			}
			start = getFolder(fi.getDictPath(), start, maxNum);
			path = fi.getDictPath() + start;
			fi.setRelativePath(start + "");
			fi.setFilePath(path);
			String key = getKey(id.toString());
			MemCachedFactory.set(key, path);
			return fi;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}
	}
	public static String getKey(String id) {
		String key = QueryCache.getKey(Environment.SYSTEM_NAME, AttachItem.attachPath, id);
		return key;
	}
	public static String getAttachCache(Integer id) {
		if (id == null)
			return null;
		String key = getKey(id.toString());
		String value = (String) MemCachedFactory.get(key);
		if (value == null) {
			value = getAttachImp(id);
			MemCachedFactory.set(key, value);
		}
		return value;
	}
	public static void downloadAttach(String filename, String filepath, String oldMd5,HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!md5Check(filepath, oldMd5)){
			String msg = Messages.getString("systemMsg.MD5CheckError");
			throw new Exception(msg);
		}
		downloadAttach(filename, filepath, request, response);
	}
	
	public static void downloadAttach(String filename, String filepath, HttpServletRequest request, HttpServletResponse response) throws Exception{
		FileInputStream fin = null;
		ServletOutputStream fout = null;
		try {
			if (filename != null) {
				String showFilename = null;
				if(StringHelper.isNotEmpty(request.getHeader("User-Agent"))){
					if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)
						showFilename = new String(filename.getBytes("UTF-8"), "ISO8859-1");// firefox浏览器
					else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
						showFilename = URLEncoder.encode(filename, "UTF-8");// IE浏览器
					else if (request.getHeader("User-Agent").toUpperCase().indexOf("Mozilla") > 0)
						showFilename = new String(filename.getBytes("UTF-8"), "ISO8859-1");// chrome
					else
						showFilename = URLEncoder.encode(filename, "UTF-8");// other
				}

				// System.out.println(request.getHeader("User-Agent"));

				response.reset();
				response.setHeader("Pragma", "No-cache");
				response.setDateHeader("Expires", 0);
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-disposition", "attachment;filename=\"" + showFilename + "\"");
				fin = new FileInputStream(filepath);
				fout = response.getOutputStream();
				int byteRead;
				while ((byteRead = fin.read()) != -1) {
					fout.write(byteRead);
				}
				fout.flush();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (fin != null) {
				try {
					fin.close();
					fin = null;
				} catch (IOException e) {
				}
			}
			if (fout != null) {
				try {
					fout.close();
					fout = null;
				} catch (IOException e) {
				}
			}
		}
		
	}
	
	public static boolean md5Check(String filePath,String oldMd5) throws Exception{
		if(!"true".equals(SecurityService.getParamValue(SecurityParam.MD5_CHECK))){return true;}
		if(StringHelper.isAnyEmpty(filePath,oldMd5)){
			return false;
		}
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			String msg = Messages.getString("systemMsg.fileNotExist");
			throw new IOException(msg);
		}
		String fileMd5 = "";
		try {
			fileMd5 = Md5Util.getMD5(file);
		} catch (Exception e) {
			String msg = Messages.getString("systemMsg.MD5GetError");
			throw new Exception(msg,e);
		}
		if(oldMd5.equals(fileMd5)){
			return true;
		}
		return false;
	}
}
