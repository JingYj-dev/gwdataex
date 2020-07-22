package com.hnjz.license.api.extractor;

import com.hnjz.license.api.algorithm.ExtractionAlgorithm;
import com.hnjz.license.api.model.LicenseBean;

public interface LicenseExtractor {
    LicenseBean doExtract(ExtractionAlgorithm var1) throws Exception;

}
