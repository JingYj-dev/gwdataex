package com.hnjz.license.api.algorithm;

import com.hnjz.license.api.model.LicenseBean;

public interface ExtractionAlgorithm {
    LicenseBean doExtract(Object var1) throws Exception;

}
