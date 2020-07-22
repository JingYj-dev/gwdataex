package com.hnjz.webbase.menu;

import com.hnjz.common.exception.ExceptionFactory;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.core.model.IUser;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.menu.IMenuProvider;
import com.hnjz.webbase.menu.MenuFactory;
import com.hnjz.webbase.menu.MenuItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DeafultMenuProvider extends AbstractConfigurablePlugin implements IMenuProvider {
    private static Log log = LogFactory.getLog(com.hnjz.webbase.menu.DeafultMenuProvider.class);
    private String menuFileName = "/css-menu.xml";

    public DeafultMenuProvider() {
    }

    protected void doConfig(Map<String, String> configMap) {
        String menuFile = (String)configMap.get("menu-file");
        if (StringHelper.isNotEmptyByTrim(menuFile)) {
            this.menuFileName = menuFile;
        }

    }

    public List<MenuItem> getAllMenu(IUser user) {
        List<MenuItem> menus = new ArrayList();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        InputStream ins = null;

        try {
            ins = MenuFactory.class.getResourceAsStream(this.menuFileName);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            if (ins == null) {
                throw ExceptionFactory.makeBaseException("systemMsg.fileNoExists");
            }

            Document doc = docBuilder.parse(ins);
            NodeList list = doc.getDocumentElement().getChildNodes();

            for(int i = 0; i < list.getLength(); ++i) {
                Node node = list.item(i);
                if (node.getNodeType() == 1) {
                    Element element = (Element)list.item(i);
                    MenuItem item = this.changeNodeToDto(element);
                    if (item.getVisible()) {
                        MenuItem subItem = this.getSubItem(item, element, user);
                        if (WebBaseUtil.hasPrivilege(user, subItem.getFuncode())) {
                            menus.add(subItem);
                        }
                    }
                }
            }
        } catch (Exception var20) {
            log.error("系统异常信息：", var20);
            throw ExceptionFactory.makeBaseException("systemMsg.loadMenuError", var20);
        } finally {
            try {
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException var19) {
                var19.printStackTrace();
            }

        }

        return menus;
    }

    private MenuItem changeNodeToDto(Element element) {
        MenuItem item = new MenuItem();
        item.setId(element.getAttribute("id"));
        item.setName(element.getAttribute("name"));
        item.setFuncode(element.getAttribute("funcode"));
        item.setPath(element.getAttribute("path"));
        String parentId = ((Element)element.getParentNode()).getAttribute("id");
        parentId = StringHelper.isNotEmpty(parentId) ? parentId : "-1";
        item.setParentId(parentId);
        int level = Integer.parseInt(element.getAttribute("level"));
        item.setLevel(level);
        if (element.hasAttribute("visible")) {
            item.setVisible(Boolean.parseBoolean(element.getAttribute("visible")));
        } else {
            item.setVisible(true);
        }

        if (element.hasAttribute("icon")) {
            item.setIcon(element.getAttribute("icon"));
        }

        if (element.hasAttribute("openIcon")) {
            item.setOpenIcon(element.getAttribute("openIcon"));
        }

        item.setIsLast(this.checkIsLast(element));
        if (StringHelper.isEmpty(item.getFuncode())) {
            item.setFuncode(item.getId());
        }

        return item;
    }

    private MenuItem getSubItem(MenuItem item, Element element, IUser user) {
        if (element.hasChildNodes()) {
            NodeList nl = element.getChildNodes();

            for(int i = 0; i < nl.getLength(); ++i) {
                Node node = nl.item(i);
                if (node.getNodeType() == 1) {
                    Element tElement = (Element)node;
                    MenuItem tItem = this.changeNodeToDto(tElement);
                    if (WebBaseUtil.hasPrivilege(user, tItem.getFuncode()) && tItem.getVisible()) {
                        item.getMenus().add(tItem);
                        this.getSubItem(tItem, tElement, user);
                    }
                }
            }
        }

        return item;
    }

    private Boolean checkIsLast(Element element) {
        if (element != null && element.hasChildNodes()) {
            NodeList list = element.getChildNodes();

            for(int i = 0; i < list.getLength(); ++i) {
                Node node = list.item(i);
                if (node.getNodeType() == 1) {
                    return false;
                }
            }
        }

        return true;
    }
}
