package com.hnjz.core.model;

import java.util.Set;

public interface IUser {
    String getUserId();

    String getLoginName();

    String getRealName();

    String getPassword();

    String getType();

    String getPhone();

    String getOrganId();

    String getStatus();

    Set<String> getFuncActions();

    Set<String> getFunctions();
}
