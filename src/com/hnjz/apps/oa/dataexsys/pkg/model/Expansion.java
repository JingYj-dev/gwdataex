package com.hnjz.apps.oa.dataexsys.pkg.model;

import com.hnjz.util.ReflectionUtil;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 自定义属性
 * @author fangwq
 *
 */
public class Expansion extends PkgData {
	protected String xml;

	public Expansion(){}
	public Expansion(String xml){
		load(xml);
	}
	public Expansion(Object o){
		load(xml);
	}
	
	public void load(String xml){
		this.xml = xml;
	}
	public void load(Object o){
		this.xml = asXML(o);
	}
	
	public String asXML() {
		if(xml==null)return "";
		return xml;
	}
	
	public Node xmlToNode() throws DocumentException{
		return xmlToNode(asXML());
	}
	
	
	public static String asXML(Object o){
		StringBuffer sb = new StringBuffer();
		if(o!=null){
			if(o instanceof String){
				sb.append(o);
			}else if(o instanceof Map){
				for(Entry<String, Object> entry : ((Map<String,Object>)o).entrySet()){
					if(null!=entry.getValue()){
						sb.append("<"+entry.getKey()+">");
						sb.append(asXML(entry.getValue()));
						sb.append("</"+entry.getKey()+">");
					}else{
						
						sb.append("<"+entry.getKey()+"/>");
					}
				}
			}else if(o instanceof List){
				for(Object obj : (List)o){
					if(obj!=null){
						sb.append(asXML(obj));
					}
				}
			}else{
				try {
					Object value = ReflectionUtil.invokeMethod(o, "asXML", new Object[]{});
					if(value!=null && value instanceof String){
						sb.append(value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
}
