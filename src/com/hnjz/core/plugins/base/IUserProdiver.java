package com.hnjz.core.plugins.base;

import com.hnjz.core.model.IFunction;
import com.hnjz.core.model.IPost;
import com.hnjz.core.model.IRole;
import com.hnjz.core.model.IUser;

import java.util.List;
import java.util.Set;

public interface IUserProdiver {
    String ROLE = com.hnjz.core.plugins.base.IUserProdiver.class.getName();

    IUser getUser(String var1);

    List<IPost> getPostList(String var1);

    List<IRole> getRoleList(String var1);

    List<IFunction> getFuncList(String var1);

    Set<String> getFuncPointList(String var1);
}
