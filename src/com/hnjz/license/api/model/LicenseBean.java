package com.hnjz.license.api.model;

import com.hnjz.license.api.model.Duration;

import java.io.Externalizable;
import java.util.Calendar;

public interface LicenseBean extends Externalizable {
    Duration getDuration();

    Calendar getStartDate();

    Calendar getEndDate();

    String getExtraInfo();
}
