package com.hnjz.license.impl.common;

import com.hnjz.license.api.model.LicenseBean;
import com.hnjz.license.api.validator.LicenseValidator;

import java.util.Calendar;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:33
 */
public final class PeriodicLicenseValidator implements LicenseValidator {
    private Calendar benchmark;

    public PeriodicLicenseValidator() {
        this.benchmark = Calendar.getInstance();
    }

    public PeriodicLicenseValidator(Calendar aBenchmarkDate) {
        this.benchmark = aBenchmarkDate;
    }

    public boolean validate(LicenseBean aBean) {
        return this.validateByBenchmarkDate(aBean, this.benchmark);
    }

    public void setBenchmark(Calendar aBenchmark) {
        this.benchmark = aBenchmark;
    }

    public Calendar getBenchmark() {
        return this.benchmark;
    }

    private boolean validateByBenchmarkDate(LicenseBean aLicenseBean, Calendar aBenchmarkDate) {
        return aLicenseBean.getStartDate().before(aBenchmarkDate) && aBenchmarkDate.before(aLicenseBean.getEndDate());
    }
}
