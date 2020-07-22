package com.hnjz.license.impl.common;

import com.hnjz.license.api.model.AbstractDuration;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:36
 */
public class DayDuration extends AbstractDuration {
    public DayDuration() {
    }

    public DayDuration(int aLength) {
        super(aLength);
    }

    protected int calculateUnitInHour() {
        return 24;
    }

    public String getMeasurementUnit() {
        return "Day";
    }
}
