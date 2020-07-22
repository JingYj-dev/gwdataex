package com.hnjz.apps.oa.dataexsys.util;

import com.hnjz.util.StringHelper;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Base64Util {
	public static void main(String[] args) {
		String mis = getBASE64("partnerId#applicationId#campaignId:password");
//		System.out.println(mis);
		String dec = getFromBASE64(mis);
//		System.out.println(dec);

	}

	// 将 s 进行 BASE64 编码
	public static String getBASE64(String s) {
		if (StringHelper.isEmpty(s))
			return null;
		return (new BASE64Encoder()).encode(s.getBytes());
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (StringHelper.isEmpty(s))
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	/**
     * 将文件转成base64 字符串
     *
     * @param path文件路径
     * @return *
     * @throws Exception
     */
 
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        if(file.exists()){
        	FileInputStream inputFile = new FileInputStream(file);
        	//稍后调整读取方式
        	/* while () {
        	
        }
        byte[] buf = new byte[1024];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        bais.*/
        	byte[] buffer = new byte[(int) file.length()];
        	inputFile.read(buffer);
        	inputFile.close();
        	return new BASE64Encoder().encode(buffer);
        }
        System.out.println("gwContent=================为空");
        return "";
 
    }
 
    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
 
    public static void decoderBase64File(String base64Code, String targetPath)
            throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
 
    }

}
