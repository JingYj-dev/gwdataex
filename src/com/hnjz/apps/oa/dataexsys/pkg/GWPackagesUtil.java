package com.hnjz.apps.oa.dataexsys.pkg;

import com.hnjz.util.StringHelper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

/**
 * 公文封包与解包
 * 注释掉正文转base64步骤
 * @author LinZhanbo
 *
 */
public class GWPackagesUtil {
	
	/*public String getXmlFromFile(String filePath) throws UnsupportedEncodingException {
		return new String(FileUtil.getBytesFromFile(filePath), "UTF-8");
	}
	public String getXmlFromFile(Integer serverId,String realPath) throws UnsupportedEncodingException {
		String filePath = AttachItem.filePath(serverId)+realPath;
		return getXmlFromFile(filePath);
	}*/

	public static Document parsePackage(String xml) throws Exception{
		Document doc = null;
		//检查报文是否为空
		if (StringHelper.isEmptyByTrim(xml)) {
			throw new Exception("[request data is empty. ]");
		}
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (Exception e) {
			try {
				int beginMark = xml.indexOf("<内容>");
				int endMark = xml.indexOf("</内容>", beginMark);
				StringBuilder sb = new StringBuilder();
				sb.append(xml.substring(0, beginMark) + "<内容><![CDATA[" + xml.substring(beginMark + 4, endMark) + "]]>" + xml.substring(endMark));
				doc = DocumentHelper.parseText(sb.toString());
			} catch (Exception e1) {
				throw e1;
			}
		}
		return doc;
	}



}
