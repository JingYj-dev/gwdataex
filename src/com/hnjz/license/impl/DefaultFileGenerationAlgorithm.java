package com.hnjz.license.impl;

import com.hnjz.license.api.algorithm.GenerationAlgorithm;
import com.hnjz.license.api.model.LicenseBean;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:30
 */
public class DefaultFileGenerationAlgorithm implements GenerationAlgorithm {
    private static final String MUST_BE_FILE_TYPE = "指定的输出对象必须是文件!";

    public DefaultFileGenerationAlgorithm() {
    }

    public Object doGenerate(LicenseBean aLicenseBean, Object aOutput) throws FileNotFoundException, IOException {
        File aDestination = this.______castToFile(aOutput);
        this.______compressLicenseBeanAndSetToFile(aLicenseBean, aDestination);
        return aDestination;
    }

    private File ______castToFile(Object aOutput) {
        if (!File.class.isInstance(aOutput)) {
            throw new IllegalArgumentException("指定的输出对象必须是文件!");
        } else {
            return (File)aOutput;
        }
    }

    private void ______compressLicenseBeanAndSetToFile(LicenseBean aLicenseBean, File aDestination) throws FileNotFoundException, IOException {
        FileOutputStream aFileOutputStream = new FileOutputStream(aDestination);
        GZIPOutputStream aGZipOutputStream = new GZIPOutputStream(aFileOutputStream);
        ObjectOutputStream aObjectOutputStream = new ObjectOutputStream(aGZipOutputStream);
        aObjectOutputStream.writeObject(aLicenseBean);
        aFileOutputStream.flush();
        aGZipOutputStream.flush();
        aObjectOutputStream.flush();
        aObjectOutputStream.close();
    }
}
