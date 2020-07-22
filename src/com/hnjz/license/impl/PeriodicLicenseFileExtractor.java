package com.hnjz.license.impl;

import com.hnjz.license.api.algorithm.ExtractionAlgorithm;
import com.hnjz.license.api.extractor.LicenseExtractor;
import com.hnjz.license.api.model.LicenseBean;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:30
 */
public final class PeriodicLicenseFileExtractor implements LicenseExtractor {
    private static final String SOURCE_OBJECT_NOT_FOUND = "未找到指定的文件!";
    private static final String READ_ACCESS_NOT_ALLOWED = "没有访问权限!";
    private final File licensePath;

    public PeriodicLicenseFileExtractor(File aLicenseFilePath) throws FileNotFoundException {
        if (!aLicenseFilePath.exists()) {
            throw new FileNotFoundException("未找到指定的文件!");
        } else if (!aLicenseFilePath.canRead()) {
            throw new SecurityException("没有访问权限!");
        } else {
            this.licensePath = aLicenseFilePath;
        }
    }

    public LicenseBean doExtract(ExtractionAlgorithm aExtractionAlgorithm) throws Exception {
        return aExtractionAlgorithm.doExtract(this.licensePath);
    }
}
