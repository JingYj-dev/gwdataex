package com.hnjz.core.model;

import com.hnjz.core.model.IJson;

import java.io.Serializable;

public interface IServiceResult <T> extends Serializable, IJson {
    int RESULT_OK = 0;
    int RESULT_FAILED = 1;
    int RESULT_ERROR = 2;
    int RESULT_UNLOGON = 3;
    int RESULT_UNAUTHORIZED = 4;
    int RESULT_UNKNOWN = 9;

    int getResultCode();

    String getResultDesc();

    T getResultObject();

    String toActionResult();
}
