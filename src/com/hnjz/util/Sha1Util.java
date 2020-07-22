package com.hnjz.util;

import java.security.MessageDigest;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:16
 */
public class Sha1Util {
    public Sha1Util() {
    }

    public static String SHA1Encode(String sourceString) {
        String resultString = null;

        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
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

        return buf.toString().toUpperCase();
    }
}
