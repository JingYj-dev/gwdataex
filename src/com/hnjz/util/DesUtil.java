package com.hnjz.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:24
 */
public class DesUtil {
    private static final String PASSWORD_CRYPT_KEY = "EDCioui1234";
    private static final String DES = "DES";

    public DesUtil() {
    }

    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, securekey, sr);
        return cipher.doFinal(src);
    }

    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, sr);
        return cipher.doFinal(src);
    }

    public static final String decrypt(String data) throws Exception {
        return new String(decrypt(hex2byte(data.getBytes()), "EDCioui1234".getBytes()));
    }

    public static final String decrypt(String data, String pwd) throws Exception {
        if (pwd == null) {
            pwd = "EDCioui1234";
        }

        return new String(decrypt(hex2byte(data.getBytes()), pwd.getBytes()));
    }

    public static final String encrypt(String content) {
        try {
            return byte2hex(encrypt(content.getBytes(), "EDCioui1234".getBytes()));
        } catch (Exception var2) {
            return null;
        }
    }

    public static final String encrypt(String content, String pwd) {
        if (pwd == null) {
            pwd = "EDCioui1234";
        }

        try {
            return byte2hex(encrypt(content.getBytes(), pwd.getBytes()));
        } catch (Exception var3) {
            return null;
        }
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

    public static byte[] hex2byte(String s) {
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] b2 = new byte[s.length() / 2];

            for(int n = 0; n < s.length(); n += 2) {
                String item = s.substring(n, n + 2);
                b2[n / 2] = (byte)Integer.parseInt(item, 16);
            }

            return b2;
        }
    }

    public static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] b2 = new byte[b.length / 2];

            for(int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                b2[n / 2] = (byte)Integer.parseInt(item, 16);
            }

            return b2;
        }
    }
}
