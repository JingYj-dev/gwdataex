package com.hnjz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:19
 */
public class Md5Util {
    public Md5Util() {
    }

    public static String MD5Encode(String src) {
        String resultString = null;

        try {
            resultString = getMD5(src);
        } catch (Exception var3) {
            var3.printStackTrace();
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

    public static String getMD5(File file) throws Exception {
        FileInputStream fis = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            boolean var4 = true;

            int length;
            while((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            byte[] b = md.digest();
            String var7 = byte2hexString(b);
            return var7;
        } finally {
            try {
                fis.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }
    }

    public static String getMD5(String message) throws Exception {
        if (message == null) {
            return null;
        } else {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(message.getBytes("utf-8"));
            return byte2hexString(b);
        }
    }
}
