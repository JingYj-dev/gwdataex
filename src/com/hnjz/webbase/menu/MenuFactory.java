package com.hnjz.webbase.menu;

import com.hnjz.common.PluginBus;
import com.hnjz.common.exception.ExceptionFactory;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.model.IUser;
import com.hnjz.util.Messages;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.menu.IMenuProvider;
import com.hnjz.webbase.menu.MenuItem;
import com.hnjz.webbase.webwork.WebworkUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuFactory {
    private static Log log = LogFactory.getLog(com.hnjz.webbase.menu.MenuFactory.class);
    public static String menuCacheName = com.hnjz.webbase.menu.MenuFactory.class.getName();
    private static String sysMode = ConfigurationManager.getMode();

    public MenuFactory() {
    }

    public static List<MenuItem> getUserMenu() {
        com.hnjz.webbase.menu.IMenuProvider menuProvider = (com.hnjz.webbase.menu.IMenuProvider)PluginBus.getPlugin(IMenuProvider.class, sysMode);
        if (menuProvider == null) {
            throw ExceptionFactory.makeBaseException("systemMsg.fetchMenuFailed", new String[]{sysMode});
        } else {
            List<MenuItem> result = new ArrayList(0);
            IUser user = WebBaseUtil.getCurrentUser();
            if (user == null) {
                log.error(Messages.getString("systemMsg.sessionInvalid"));
                return (List)result;
            } else {
                Object userId = user.getUserId();
                Object tMenus = getCachedMenuRootList(userId);
                if (tMenus == null) {
                    try {
                        tMenus = menuProvider.getAllMenu(user);
                    } catch (Exception var6) {
                        log.error(Messages.getString("systemMsg.fetchMenuFailed") + ":[" + userId + "][" + user.getRealName() + "]", var6);
                    }
                }

                if (tMenus != null) {
                    result = (List)tMenus;
                    cacheMenuRootList(userId, tMenus);
                }

                return (List)result;
            }
        }
    }

    private static Object getCachedMenuRootList(Object userId) {
        return WebworkUtil.getActionContext().getSession().get(menuCacheName);
    }

    private static void cacheMenuRootList(Object userId, Object tMenus) {
        WebworkUtil.getActionContext().getSession().put(menuCacheName, tMenus);
    }

    public static void removeMenuRootList(String userId) {
        WebworkUtil.getActionContext().getSession().remove(menuCacheName);
    }

    public static MenuItem getCurrentMenuRoot(String sysMenuId) {
        Object tMaps = getUserMenu();
        if (tMaps != null) {
            List<MenuItem> items = (List)tMaps;
            Iterator var4 = items.iterator();

            while(var4.hasNext()) {
                MenuItem menuItem = (MenuItem)var4.next();
                if (menuItem.getId().equals(sysMenuId)) {
                    return menuItem;
                }
            }
        }

        return null;
    }
}
