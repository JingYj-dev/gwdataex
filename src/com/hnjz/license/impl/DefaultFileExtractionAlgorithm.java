package com.hnjz.license.impl;

import com.hnjz.license.api.algorithm.ExtractionAlgorithm;
import com.hnjz.license.api.model.LicenseBean;
import com.hnjz.license.impl.common.PeriodicLicenseBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:31
 */
public class DefaultFileExtractionAlgorithm implements ExtractionAlgorithm {
    private Log log = LogFactory.getLog(DefaultFileExtractionAlgorithm.class);
    private static final String MUST_BE_FILE_TYPE = "指定的输出对象必须是文件!";

    public DefaultFileExtractionAlgorithm() {
    }

    public LicenseBean doExtract(Object aLicenseObject) throws FileNotFoundException, IOException, ClassNotFoundException {
        File aSource = this.______castToFile(aLicenseObject);
        return this.______extractLicenseBeanFromCompressedFile(aSource);
    }

    private File ______castToFile(Object aOutput) {
        if (!File.class.isInstance(aOutput)) {
            throw new IllegalArgumentException("指定的输出对象必须是文件!");
        } else {
            return (File) aOutput;
        }
    }

    private LicenseBean ______extractLicenseBeanFromCompressedFile(File aSource) throws FileNotFoundException, IOException, ClassNotFoundException {
        log.debug(aSource);
        InputStream inputStream = new FileInputStream(aSource);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
//        ObjectInputStream aObjectInputStream = new ObjectInputStream(new GZIPInputStream(new FileInputStream(aSource)));
        Object obj = objectInputStream.readObject();
        LicenseBean licenseBean = (LicenseBean) obj;
        LicenseBean licenseBean1 = (PeriodicLicenseBean) obj;
        objectInputStream.close();
        return licenseBean;
    }
}
