package com.hnjz.core.plugins.base;

public interface ILicenser {
    String ROLE = ILicenser.class.getName();

    boolean validate() throws Exception;
}
