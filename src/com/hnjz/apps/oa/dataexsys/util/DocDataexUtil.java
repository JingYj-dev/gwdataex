package com.hnjz.apps.oa.dataexsys.util;

import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysTaskHelper;
import com.hnjz.db.query.QueryCache;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

//公文发送工具类

public class DocDataexUtil {
	 private static final int TIME_OUT = 200*1000;   //超时时间
	 private static final String CHARSET = "utf-8"; //设置编码
	 
	 
	 //发送公文
	 public static String sendDoc(String urlStr, String xmlStr, String zipFilePath) {
			//要发送的文件路径
		 String result = "";
			try {
				File file = new File(zipFilePath);
				String xmlInfo = URLEncoder.encode(xmlStr, "UTF-8");
	        	String urls = urlStr + "?"+"xmlstr="+xmlInfo+"&fileName="+file.getName();
				URL url = new URL(urls);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setChunkedStreamingMode(1024);
				connection.setReadTimeout(TIME_OUT);
				connection.setConnectTimeout(TIME_OUT);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setRequestProperty("content-type", "binary/octet-stream");
				BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
				// 读取文件上传到服务器			
				FileInputStream fis = new FileInputStream(file);
				byte[] bytes = new byte[1024*100];//最大100m
				int len = 0;
				while ((len = fis.read(bytes, 0, bytes.length)) > 0) {
					out.write(bytes, 0, len);
				}
				bytes = null;
				out.flush();
				out.close();
				fis.close();
				
				//返回信息
				int res = connection.getResponseCode();  
	            if(res==200)
	             {
	                // 读取URLConnection的响应
	    			DataInputStream in = new DataInputStream(connection.getInputStream());
	    			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    			byte[] data = new byte[1024];
	    			int count = -1;
	    			while ((count = in.read(data, 0, 1024)) != -1) {
	    				outStream.write(data, 0, count);
	    			}
	    			data = null;
	    			result = new String(outStream.toByteArray(), "UTF-8");
	    			System.out.println("2.调用方正真实发文交换箱返回结果========"+result);
	    			in.close();
	    			outStream.close();
	            }				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
	}    
    
    //调用公文交换系统方法
    public static String docExc(String urlStr, String xmlStr) {
    	String result = null; 

        try {
        	System.out.println("调用方正交换开始。。。"+xmlStr);
        	String xmlInfo = URLEncoder.encode(xmlStr, "UTF-8");
        	String urls = urlStr + "?"+"xmlstr="+xmlInfo; 
        	System.out.println(urls);
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(TIME_OUT);
//            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.connect();

            /**
            * 获取响应码  200=成功
            * 当响应成功，获取响应的流  
            */
            int res = conn.getResponseCode();  
            if(res==200)
            {
            	// 读取URLConnection的响应
				DataInputStream in = new DataInputStream(conn.getInputStream());
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] data = new byte[1024];
				int count = -1;
				while ((count = in.read(data, 0, 1024)) != -1) {
					outStream.write(data, 0, count);
				}
				data = null;
				result = new String(outStream.toByteArray(), "UTF-8");
				System.out.println("1.调用方正接口返回真实交换箱地址==="+result);
				in.close();
				outStream.close();
               }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }    
    
    
    //根据接收机构获取节点发送地址
    public static String getFzAddr(String receiveId) {
    	String endpoint = ""; 
    	try {
    		SOrg recvOrg = OrgItem.getOrg(receiveId);
			Identity receiver = new Identity();
			receiver.setIdentityFlag(recvOrg.getUnitCode());
			receiver.setIdentityName(recvOrg.getName());
			DataexSysDir targetInfo = TaskHelper.findDataexSysDir(receiver);
			DataexSysNode exnode = QueryCache.get(DataexSysNode.class, targetInfo.getExnodeId());
			List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(), "2");//1.握手 2.发送
			if (ws!=null && ws.size() > 0) {
				endpoint = ws.get(0).getDataServiceUrl();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

        return endpoint;
    } 
}
