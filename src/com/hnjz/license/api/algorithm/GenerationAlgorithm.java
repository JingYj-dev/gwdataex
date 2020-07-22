package com.hnjz.license.api.algorithm;

import com.hnjz.license.api.model.LicenseBean;

public interface GenerationAlgorithm {
    Object doGenerate(LicenseBean var1, Object var2) throws Exception;

}
