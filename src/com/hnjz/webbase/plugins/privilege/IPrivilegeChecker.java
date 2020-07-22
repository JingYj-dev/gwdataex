package com.hnjz.webbase.plugins.privilege;

import com.hnjz.core.model.IUser;
import com.hnjz.webbase.plugins.privilege.NoPrivilegeException;

public interface IPrivilegeChecker {
    void checkPrivilege(IUser var1, String var2) throws NoPrivilegeException;

}
