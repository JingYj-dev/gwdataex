package com.hnjz.webbase.plugins.privilege.impl;

import com.hnjz.common.plugins.impl.AbstractPlugin;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.model.IUser;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.plugins.privilege.IPrivilegeChecker;
import com.hnjz.webbase.plugins.privilege.NoPrivilegeException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class DefaultPriviledeChecker extends AbstractPlugin implements IPrivilegeChecker {
    private static boolean isSecured = ConfigurationManager.isSecured();
    private static Set<String> excludesActions = null;

    public DefaultPriviledeChecker() {
    }

    private Set<String> getExcludesActions() {
        if (excludesActions == null) {
            String excludes = ConfigurationManager.getConfigurationManager().getSysConfigure("app.popedom.excludes", "");
            excludesActions = new HashSet(Arrays.asList(excludes.split(",")));
        }

        return excludesActions;
    }

    public void checkPrivilege(IUser user, String funcCode) throws NoPrivilegeException {
        if (isSecured) {
            if (!StringHelper.isEmpty(funcCode) && !this.getExcludesActions().contains(funcCode)) {
                Set<String> ownedFuncCodes = user.getFuncActions();
                if (ownedFuncCodes == null || !ownedFuncCodes.contains(funcCode)) {
                    throw new NoPrivilegeException(funcCode);
                }
            }
        }
    }
}
