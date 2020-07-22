package com.hnjz.common.exception;

import com.hnjz.common.exception.base.CssBaseException;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:06
 */
public class ExceptionFactory {
    public ExceptionFactory() {
    }

    public static CssBaseException makeBaseException(String messageCode) {
        return new CssBaseException(messageCode);
    }

    public static CssBaseException makeBaseException(String messageCode, Throwable e) {
        return new CssBaseException(messageCode, e);
    }

    public static CssBaseException makeBaseException(String messageCode, String[] params) {
        return new CssBaseException(messageCode, params);
    }

    public static CssBaseException makeBaseException(String messageCode, String[] params, Throwable e) {
        return new CssBaseException(messageCode, params, e);
    }
}
