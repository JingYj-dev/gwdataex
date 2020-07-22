package com.hnjz.license.api.model;

import java.io.Serializable;

public interface Duration extends Serializable {
    int getSustenanceInHours();

    void setLength(int var1);

    int getLength();

    String getMeasurementUnit();
}
