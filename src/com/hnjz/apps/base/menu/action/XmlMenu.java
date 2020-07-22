package com.hnjz.apps.base.menu.action;

import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XmlMenu extends AdminAction {
	private static Log log = LogFactory.getLog(XmlMenu.class);

	public XmlMenu() {
		super();
	}

	@Override
	protected String adminGo() {
		try {
			StringBuffer buf = new StringBuffer();
			SMenu menu = new SMenu();
			menu.setMenuId("0");
			buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
			xml(menu, buf);
			writeFile(buf.toString(), "d:/menu.xml");
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages
					.getString("systemMsg.success"), null);
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
					Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	
	public void xml(SMenu menu, StringBuffer xml) {
		String mid = menu.getMenuId();
		QueryCache qc = new QueryCache("select a.menuId from SMenu a where a.parentId =:parentId")
				.setParameter("parentId", menu.getMenuId());
		List<String> sMenuIds1 = qc.list();
		List<SMenu> sMenuList1 = QueryCache.idToObj(SMenu.class, sMenuIds1);
		// 根节点
		if (menu.getParentId() == null) {
			xml.append("<css-menu>").append("\n");
			for (SMenu sMenu1 : sMenuList1) {
				xml(sMenu1, xml);
			}
			xml.append("</css-menu>");
		} else {
			if (sMenuList1 == null || sMenuList1.isEmpty()) {//没有子节点
				xml.append("<menu id=\"").append(menu.getFuncId()).append("\"")
						.append(" name=\"").append(menu.getMenuName())
						.append("\"").append(" icon=\"").append(menu.getIcon())
						.append("\"").append(" path=\"").append(menu.getUrl())
						.append("\"").append(" visible=\"").append("true")
						.append("\"").append(" level=\"").append("2")
						.append("\"").append("/>").append("\n");
			} else {//有子节点
				xml.append("<menu id=\"").append(menu.getFuncId()).append("\"")
						.append(" name=\"").append(menu.getMenuName())
						.append("\"").append(" icon=\"").append(menu.getIcon())
						.append("\"").append(" visible=\"").append("true")
						.append("\"").append(" level=\"").append("1")
						.append("\"").append(">").append("\n");
				for (SMenu sMenu1 : sMenuList1) {
					xml(sMenu1, xml);
				}
				xml.append("</menu>\n");
			}
		}
	}

	private void writeFile(String data, String filename) {//字符写入到xml文件里
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			bw.write(data);
			bw.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
