package com.hnjz.webbase.plugins.privilege;

import com.hnjz.common.exception.base.CssBaseException;

public class NoPrivilegeException extends CssBaseException {
    private static String messageCode = "systemMsg.noPriviledge";

    public NoPrivilegeException(String funcCode) {
        super(messageCode, new String[]{funcCode});
    }

    public NoPrivilegeException(String funcCode, Throwable cause) {
        super(messageCode, new String[]{funcCode}, cause);
    }
}
