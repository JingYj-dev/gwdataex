package com.hnjz.license;

import com.hnjz.license.impl.DefaultFileExtractionAlgorithm;
import com.hnjz.license.impl.PeriodicLicenseFileExtractor;
import com.hnjz.license.impl.common.PeriodicLicenseBean;
import com.hnjz.license.impl.common.PeriodicLicenseValidator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:28
 */
public final class LicenseHelper {
    public LicenseHelper() {
    }

    private String getDateTimeString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            date = new Date();
        }

        String DateTimeString = df.format(date);
        return DateTimeString;
    }

    public void validate() throws Exception {
        this.validate(com.hnjz.license.LicenseHelper.class.getResource("/META-INF/my_license.dat").getFile());
    }

    public void validate(String file) throws Exception {
        PeriodicLicenseBean license = null;

        try {
            PeriodicLicenseFileExtractor extractor = new PeriodicLicenseFileExtractor(new File(file));
            license = (PeriodicLicenseBean)extractor.doExtract(new DefaultFileExtractionAlgorithm());
        } catch (Exception var11) {
            throw new Exception("读取License文件失败!", var11);
        }

        if (license == null) {
            throw new Exception("License读取失败!");
        } else {
            String licenseInfo = license.getExtraInfo();
            String[] pairs = licenseInfo.split("&");
            Map<String, String> info = new HashMap();
            String[] var9 = pairs;
            int var8 = pairs.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                String pair = var9[var7];
                String[] kv = pair.split("=");
                info.put(kv[0], kv[1]);
            }

            System.out.println("----版本号   : V" + (String)info.get("version"));
            System.out.println("----授权给   : " + (String)info.get("company"));
            System.out.println("----MAC地址: " + (String)info.get("macAddress"));
            System.out.println("----起始日期: " + this.getDateTimeString(license.getStartDate().getTime()));
            System.out.println("----截止日期: " + this.getDateTimeString(license.getEndDate().getTime()));
            Calendar car = Calendar.getInstance();
            PeriodicLicenseValidator validator = new PeriodicLicenseValidator(car);
            if (validator.validate(license)) {
                System.out.println("----License可用!");
            } else {
                throw new Exception("License已过期!");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        com.hnjz.license.LicenseHelper helper = new com.hnjz.license.LicenseHelper();
        if (args != null && args.length > 0 && args[0] != null) {
            helper.validate(args[0]);
        } else {
            helper.validate();
        }

    }
}
