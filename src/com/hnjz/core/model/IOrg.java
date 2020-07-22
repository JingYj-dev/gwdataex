package com.hnjz.core.model;

import java.io.Serializable;

public interface IOrg extends Serializable {
    String getOrgId();

    String getParentId();

    String getOrgName();

    String getOrgCode();
}
