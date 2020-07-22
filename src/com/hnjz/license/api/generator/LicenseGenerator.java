package com.hnjz.license.api.generator;

import com.hnjz.license.api.algorithm.GenerationAlgorithm;

public interface LicenseGenerator {
    Object generate(GenerationAlgorithm var1) throws Exception;

}
