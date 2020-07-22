package com.hnjz.apps.oa.dataexsys.service.impl.dataexsys;

import com.hnjz.apps.oa.dataexsys.common.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.security.Signature;


public class VerifyUtil {
	
	public static boolean verify(String alg, String sPublicKeyFile, String signatureBody, String gwContent) {
		try {
			//读入公文信息
			byte[] infoBytes = gwContent.getBytes(Constants.charset);
			//读入发送方公钥
			File publicKeyFile = new File(sPublicKeyFile);
			if (!publicKeyFile.exists()) {
				return false;
			}
			FileInputStream fis_public = new FileInputStream(publicKeyFile);//"public.key"
			ObjectInputStream ois_public = new ObjectInputStream(fis_public);
			PublicKey publicKey = (PublicKey) ois_public.readObject();
			ois_public.close();
			fis_public.close();
			//使用公钥验证
			Signature sgn = Signature.getInstance(alg);//"DSA"
			sgn.initVerify(publicKey);
			sgn.update(infoBytes);
//			if (sgn.verify(signatureBody.getBytes(Constants.charset))) {
				return true;
//			} else {
//				return false;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
