package com.hnjz.core.plugins.base;

import com.hnjz.core.model.IOrg;
import com.hnjz.core.model.IPost;
import com.hnjz.core.model.IUser;

import java.util.List;

public interface IOrgProvider {
    String ROLE = com.hnjz.core.plugins.base.IOrgProvider.class.getName();

    List<IUser> queryOrgUserList(String var1);

    List<IPost> queryPost(String var1);

    List<IOrg> querySubOrgList(String var1);

    IOrg getOrg(String var1);
}
