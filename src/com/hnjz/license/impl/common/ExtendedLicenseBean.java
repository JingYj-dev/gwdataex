package com.hnjz.license.impl.common;

import com.hnjz.license.api.model.Duration;
import com.hnjz.license.impl.common.PeriodicLicenseBean;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Calendar;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:36
 */
public final class ExtendedLicenseBean extends PeriodicLicenseBean {
    private static final long serialVersionUID = -2198114997383072826L;
    private String physicalAddress = "";
    private String osType = "";

    public ExtendedLicenseBean() {
    }

    public ExtendedLicenseBean(Calendar aStartDate, Duration aSustenance, String extra) {
        super(aStartDate, aSustenance, extra);
        this.physicalAddress = "";
        this.osType = "";
    }

    public ExtendedLicenseBean(Calendar aStartDate, Duration aSustenance, String extra, String osType, String physicalAddress) {
        super(aStartDate, aSustenance, extra);
        this.osType = osType;
        this.physicalAddress = physicalAddress;

        assert osType != null;

        assert physicalAddress != null;

    }

    public String getPhysicalAddress() {
        return this.physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getOsType() {
        return this.osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public boolean equals(Object aComparedObject) {
        boolean res = super.equals(aComparedObject);
        if (!res) {
            return res;
        } else {
            com.hnjz.license.impl.common.ExtendedLicenseBean aComparedLicenseBean = (com.hnjz.license.impl.common.ExtendedLicenseBean)aComparedObject;
            if (this.osType.equals(aComparedLicenseBean.getOsType()) && this.physicalAddress.equals(aComparedLicenseBean.getPhysicalAddress())) {
                res = true;
            } else {
                res = false;
            }

            return res;
        }
    }

    public int hashCode() {
        long aAssistantKeyHash = (long)super.hashCode();
        char[] aAssistantKeyChars = this.osType.toCharArray();
        int aNumberOfChars = aAssistantKeyChars.length;

        int aIndex;
        for(aIndex = 0; aIndex < aNumberOfChars; ++aIndex) {
            aAssistantKeyHash += (long)(aAssistantKeyChars[aIndex] * (aIndex + 1));
        }

        aAssistantKeyChars = this.physicalAddress.toCharArray();
        aNumberOfChars = aAssistantKeyChars.length;

        for(aIndex = 0; aIndex < aNumberOfChars; ++aIndex) {
            aAssistantKeyHash += (long)(aAssistantKeyChars[aIndex] * (aIndex + 1));
        }

        return (int)aAssistantKeyHash;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(this.osType);
        out.writeObject(this.physicalAddress);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.osType = (String)in.readObject();
        this.physicalAddress = (String)in.readObject();
    }
}
