package com.hnjz.webbase.plugins.privilege.impl;

import com.hnjz.core.model.IUser;
import com.hnjz.webbase.plugins.privilege.NoPrivilegeException;
import com.hnjz.webbase.plugins.privilege.impl.DefaultPriviledeChecker;

import java.util.Set;

public class ExPriviledeChecker extends DefaultPriviledeChecker {
    public ExPriviledeChecker() {
    }

    public void checkPrivilege(IUser user, String funcCode) throws NoPrivilegeException {
        Set<String> funcCodes = user.getFunctions();
        if (funcCodes == null || !funcCodes.contains(funcCode)) {
            super.checkPrivilege(user, funcCode);
        }
    }
}
