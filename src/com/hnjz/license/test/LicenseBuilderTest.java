package com.hnjz.license.test;

import com.hnjz.license.api.generator.LicenseGenerator;
import com.hnjz.license.impl.DefaultFileExtractionAlgorithm;
import com.hnjz.license.impl.DefaultFileGenerationAlgorithm;
import com.hnjz.license.impl.PeriodicLicenseFileExtractor;
import com.hnjz.license.impl.common.DayDuration;
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
 * @date 2020/6/17 11:29
 */
public class LicenseBuilderTest {
    public LicenseBuilderTest() {
    }

    public static String getDateTimeString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            date = new Date();
        }

        String DateTimeString = df.format(date);
        return DateTimeString;
    }

//    public void testGengerate() throws Exception {
//        DayDuration ds = new DayDuration();
//        ds.setLength(25);
//        String extraInfo = "version=1.0&company=中科九洲科技股份有限公司&macAddress=F0-DE-F1-46-22-82";
//        PeriodicLicenseBean licenseBean = new PeriodicLicenseBean(Calendar.getInstance(), ds, extraInfo);
//        LicenseGenerator generator = new PeriodicLicenseFileGenerator(licenseBean, new File("d:/my_license.dat"));
//        generator.generate(new DefaultFileGenerationAlgorithm());
//    }

    public void testExtract() throws Exception {
        PeriodicLicenseFileExtractor extractor = new PeriodicLicenseFileExtractor(new File(PeriodicLicenseFileExtractor.class.getResource("/META-INF/my_license.dat").getFile()));
        PeriodicLicenseBean license = (PeriodicLicenseBean)extractor.doExtract(new DefaultFileExtractionAlgorithm());
        if (license != null) {
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

            System.out.println("----版本号   :V" + (String)info.get("version"));
            System.out.println("----授权给   :" + (String)info.get("company"));
            System.out.println("----MAC地址:" + (String)info.get("macAddress"));
            System.out.println("----起始时间:" + getDateTimeString(license.getStartDate().getTime()));
            System.out.println("----截止时间:" + getDateTimeString(license.getEndDate().getTime()));
        }

    }

    public void testValidate() throws Exception {
        PeriodicLicenseFileExtractor extractor = new PeriodicLicenseFileExtractor(new File("d:/my_license.dat"));
        PeriodicLicenseBean license = (PeriodicLicenseBean)extractor.doExtract(new DefaultFileExtractionAlgorithm());
        if (license != null) {
            Calendar car = Calendar.getInstance();
            PeriodicLicenseValidator validator = new PeriodicLicenseValidator(car);
            if (validator.validate(license)) {
                System.out.println("License可用!");
            } else {
                System.err.println("License已过期!");
            }
        } else {
            System.err.println("License读取失败!");
        }

    }

//    public static void main(String[] args) throws Exception {
//        com.hnjz.license.test.LicenseBuilderTest builder = new com.hnjz.license.test.LicenseBuilderTest();
//        builder.testGengerate();
//        builder.testExtract();
//    }
}
