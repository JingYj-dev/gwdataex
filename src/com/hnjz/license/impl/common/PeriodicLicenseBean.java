package com.hnjz.license.impl.common;

import com.hnjz.license.api.model.Duration;
import com.hnjz.license.api.model.LicenseBean;
import com.hnjz.license.impl.common.DayDuration;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Calendar;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:34
 */
public class PeriodicLicenseBean implements LicenseBean {
   //private static final long serialVersionUID = -2198114997383072826L;
    private static final int DEFAULT_LENGTH = 30;
    private static final String DEFAULT_EXTRA_INFO = "DefaultKey=123456";
    private Duration duration;
    private Calendar startDate;
    private String extraInfo;

    public PeriodicLicenseBean() {
        this.startDate = Calendar.getInstance();
        this.setDefaultLengthToSustenance();
        this.extraInfo = "DefaultKey=123456";
    }

    public PeriodicLicenseBean(Calendar aStartDate, Duration aSustenance, String aAssistantKey) {
        this.startDate = aStartDate;
        this.duration = aSustenance;
        this.extraInfo = aAssistantKey;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Calendar getStartDate() {
        return this.startDate;
    }

    public Calendar getEndDate() {
        Calendar aEndDate = (Calendar)this.startDate.clone();
        aEndDate.add(11, this.duration.getSustenanceInHours());
        return aEndDate;
    }

    public String getExtraInfo() {
        return this.extraInfo;
    }

    private void setDefaultLengthToSustenance() {
        if (this.duration == null) {
            this.duration = new DayDuration(30);
        } else {
            this.duration.setLength(30);
        }

    }

    public boolean equals(Object aComparedObject) {
        if (!this.compareType(aComparedObject)) {
            return false;
        } else {
            com.hnjz.license.impl.common.PeriodicLicenseBean aComparedLicenseBean = (com.hnjz.license.impl.common.PeriodicLicenseBean)aComparedObject;
            if (!this.compareSustenance(aComparedLicenseBean)) {
                return false;
            } else if (!this.compareStartDate(aComparedLicenseBean)) {
                return false;
            } else {
                return this.compareExtraInfo(aComparedLicenseBean);
            }
        }
    }

    private boolean compareExtraInfo(LicenseBean lib) {
        return this.extraInfo.equals(lib.getExtraInfo());
    }

    private boolean compareStartDate(LicenseBean aComparedLicenseBean) {
        return this.startDate.equals(aComparedLicenseBean.getStartDate());
    }

    private boolean compareType(Object aComparedObject) {
        return this.getClass().isInstance(aComparedObject);
    }

    private boolean compareSustenance(LicenseBean aComparedBean) {
        return this.duration.equals(aComparedBean.getDuration());
    }

    public int hashCode() {
        long aAssistantKeyHash = 0L;
        char[] aAssistantKeyChars = this.extraInfo.toCharArray();
        int aNumberOfChars = aAssistantKeyChars.length;

        for(int aIndex = 0; aIndex < aNumberOfChars; ++aIndex) {
            aAssistantKeyHash += (long)(aAssistantKeyChars[aIndex] * (aIndex + 1));
        }

        return (int)((long)this.startDate.get(6) + aAssistantKeyHash + (long)this.duration.getLength() + (long)this.duration.getSustenanceInHours());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.startDate);
        out.writeObject(this.duration);
        out.writeUTF(this.extraInfo);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.startDate = (Calendar)in.readObject();
        this.duration = (Duration)in.readObject();
        this.extraInfo = in.readUTF();
    }
}
