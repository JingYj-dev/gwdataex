package com.hnjz.db.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:36
 */
public class Md5Util {
    public static final Log logger = LogFactory.getLog(Md5Util.class);

    public Md5Util() {
    }

    public static String MD5Encode(String sourceString) {
        String resultString = null;

        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byte2hexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception var3) {
        }

        return resultString;
    }

    public static final String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if ((bytes[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buf.toString();
    }

    public static String getMD5(File file) {
        FileInputStream fis = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            long var5 = System.currentTimeMillis();

            int length;
            while((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            byte[] b = md.digest();
            String var9 = byte2hexString(b);
            return var9;
        } catch (Exception var17) {
            logger.error("error when MD5.", var17);
        } finally {
            try {
                fis.close();
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

        return null;
    }

    public static String getMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(message.getBytes("utf-8"));
            return byte2hexString(b);
        } catch (Exception var3) {
            logger.error("error when MD5.", var3);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(MD5Encode("123456"));
    }
}
