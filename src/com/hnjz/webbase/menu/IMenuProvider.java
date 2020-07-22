package com.hnjz.webbase.menu;

import com.hnjz.core.model.IUser;
import com.hnjz.webbase.menu.MenuItem;

import java.util.List;

public interface IMenuProvider {
    List<MenuItem> getAllMenu(IUser var1);

}
