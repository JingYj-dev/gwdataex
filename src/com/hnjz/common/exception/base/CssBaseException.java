package com.hnjz.common.exception.base;

import com.hnjz.common.exception.base.ExceptionConfig;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:07
 */
public class CssBaseException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CssBaseException(String messageCode) {
        super(getErrorMessage(messageCode));
    }

    public CssBaseException(String messageCode, String[] params) {
        super(getErrorMessage(messageCode, params));
    }

    public CssBaseException(Throwable cause) {
        super(cause);
    }

    public CssBaseException() {
    }

    public CssBaseException(String messageCode, Throwable cause) {
        super(getErrorMessage(messageCode), cause);
    }

    public CssBaseException(String messageCode, String[] params, Throwable cause) {
        super(getErrorMessage(messageCode, params), cause);
    }

    private static String getErrorMessage(String key, String[] paras) {
        String res = com.hnjz.common.exception.base.ExceptionConfig.getString(key, paras);
        return res == null ? key : res;
    }

    private static String getErrorMessage(String key) {
        String res = ExceptionConfig.getString(key);
        return res == null ? key : res;
    }
}
