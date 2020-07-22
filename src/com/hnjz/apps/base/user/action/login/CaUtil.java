package com.hnjz.apps.base.user.action.login;


import com.westone.jseapi.JseException;
import com.westone.jseapi.base.data.x509.Cert;
import com.westone.jseapi.device.BaseInterface;
import com.westone.jseapi.device.InterProvider;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;


public class CaUtil {
	public static String getDN(String cerContent) throws  FileNotFoundException, CertificateException, JseException{
		BaseInterface baseInterface= InterProvider.createInstanse();
		baseInterface.initialize();
		baseInterface.openDevice(3);
		baseInterface.loginDevice("11111111".getBytes());
		Cert cert=new Cert(baseInterface.base64Decode(cerContent.getBytes()));
		String dnFromCert=baseInterface.getDnFromCert(cert);
		baseInterface.closeDevice();
		baseInterface.finaliz();
		return dnFromCert;
		 
		 
	}
	public static void main(String[] args) throws CertificateException, FileNotFoundException, JseException {
		String s="MIICxzCCAlOgAwIBAgIIECEEEAAAAAMwCwYJKoEcgUUBh2gLMIGSMQswCQYDVQQDDAJDQTEbMBkGA1UECwwS5YaF572R566h55CG5Lit5b+DMR4wHAYDVQQKDBXmsrPljZfnnIHlp5Tlip7lhazljoUxEjAQBgNVBAcMCemDkeW3nuW4gjESMBAGA1UECAwJ5rKz5Y2X55yBMR4wHAYDVQQGDBXkuK3ljY7kurrmsJHlhbHlkozlm70wHhcNMTcwNjA1MTYwMDAwWhcNMjcwNjA2MTU1OTU5WjCBnjEdMBsGCgmSJomT8ixkAQEMDTEwMjEwNDAwMDAwMDExFTATBgNVBAMMDOa1i+ivleeUqOaItzEeMBwGA1UECgwV5rKz5Y2X55yB5aeU5Yqe5YWs5Y6FMRIwEAYDVQQHDAnpg5Hlt57luIIxEjAQBgNVBAgMCeays+WNl+ecgTEeMBwGA1UEBgwV5Lit5Y2O5Lq65rCR5YWx5ZKM5Zu9MHowFAYHKoZIzj0CAQYJKoEcgUUBh2gIA2IABDtcbbFWzQ183WBcQ9Wv0dWL4I/j1ilSMlIkkFxgTjXLfXKKLukfB1vwHc0bYGvPLYJWmStzeTfX3XXX2oWr42xNL/BC8/73RjSB1+C+CCuo9zgzrgZuW8+baPvhLXry+qNjMGEwCQYDVR0TBAIwADALBgNVHQ8EBAMCBsAwRwYDVR0lBEAwPgYIKwYBBQUHAwMGCisGAQQBgjcUAgIGCCsGAQUFBwMCBggrBgEFBQcDCQYIKwYBBQUHAwgGCCsGAQUFBwMEMAsGCSqBHIFFAYdoCwNhAIITI1I5ZxCu2N4rYnh+VWsjK9svQ1X3Y3BdLFzK86CuxG2lxbqm26ibnbm84HvcAIes3MwQThXzRlnu+hZ8/ouo56KtYClN8K1QX0Axlt58ic87UfskCvW/9zPXFWlwDQ==";
		
		System.out.println(getDN(s));
	}
}
