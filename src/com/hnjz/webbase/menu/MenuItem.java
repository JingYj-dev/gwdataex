package com.hnjz.webbase.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Serializable {
    private static final long serialVersionUID = -7130564753291455733L;
    private String id;
    private String name;
    private String path;
    private String icon;
    private String openIcon;
    private String funcode;
    private Boolean visible;
    private Boolean isLast;
    private String parentId;
    private int level;
    private List<com.hnjz.webbase.menu.MenuItem> menus = new ArrayList();

    public MenuItem() {
    }

    public MenuItem(String id, String name, String path, String icon, String openIcon, String funcode, Boolean visible, Boolean isLast, String parentId, int level, List<com.hnjz.webbase.menu.MenuItem> menus) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.icon = icon;
        this.openIcon = openIcon;
        this.funcode = funcode;
        this.visible = visible;
        this.isLast = isLast;
        this.parentId = parentId;
        this.level = level;
        this.menus = menus;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpenIcon() {
        return this.openIcon;
    }

    public void setOpenIcon(String openIcon) {
        this.openIcon = openIcon;
    }

    public String getFuncode() {
        return this.funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getIsLast() {
        return this.isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<com.hnjz.webbase.menu.MenuItem> getMenus() {
        return this.menus;
    }

    public void setMenus(List<com.hnjz.webbase.menu.MenuItem> menus) {
        this.menus = menus;
    }
}
