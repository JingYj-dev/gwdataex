package com.hnjz.license.api.model;

import com.hnjz.license.api.model.Duration;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:45
 */
public abstract class AbstractDuration implements com.hnjz.license.api.model.Duration {
    protected static final int DEFAULT_LENGTH = 0;
    protected int length;

    public AbstractDuration() {
        this.length = 0;
    }

    public AbstractDuration(int aLength) {
        this.length = aLength;
    }

    public int getSustenanceInHours() {
        float aTotalHour = (float)(this.calculateUnitInHour() * this.length);
        if (aTotalHour > 2.14748365E9F) {
            throw new RuntimeException("The value for the total duration   in hours exceed the maximum value of an integer, it is recommended that use a bigger measurement unit");
        } else {
            return (int)aTotalHour;
        }
    }

    protected abstract int calculateUnitInHour();

    public int getLength() {
        return this.length;
    }

    public void setLength(int aLength) {
        this.length = aLength;
    }

    public boolean equals(Object aComparedObject) {
        if (this.getClass().isInstance(aComparedObject)) {
            com.hnjz.license.api.model.Duration aComparedSustenance = (Duration)aComparedObject;
            return this.getMeasurementUnit().equals(aComparedSustenance.getMeasurementUnit()) && this.length == aComparedSustenance.getLength() && this.getSustenanceInHours() == aComparedSustenance.getSustenanceInHours();
        } else {
            return false;
        }
    }

    public int hashCode() {
        int aMeasurementUnitHash = 0;
        char[] aMeasurementUnitChars = this.getMeasurementUnit().toCharArray();
        int aNumberOfMeasurementUnitChars = aMeasurementUnitChars.length;

        for(int aIndex = 0; aIndex < aNumberOfMeasurementUnitChars; ++aIndex) {
            aMeasurementUnitHash += Character.getNumericValue(aMeasurementUnitChars[aIndex]);
        }

        return aMeasurementUnitHash + this.length + this.calculateUnitInHour();
    }
}
