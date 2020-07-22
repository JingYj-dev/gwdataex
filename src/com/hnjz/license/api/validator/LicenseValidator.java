package com.hnjz.license.api.validator;

import com.hnjz.license.api.model.LicenseBean;

public interface LicenseValidator {
    boolean validate(LicenseBean var1) throws Exception;

}
