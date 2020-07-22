package com.hnjz.core.model;

import java.io.Serializable;

public interface IFunction extends Serializable {
    String getFuncCode();

    String getFuncName();

    String getParentCode();

    String getSystemId();

    Short getStatus();

    String getLeafMark();

    Double getShowOrder();
}

