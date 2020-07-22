package com.hnjz.apps.oa.dataexsys.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostInfo {
	public static InetAddress getInetAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * @Title: getHostIP
	 * @Description: TODO(描述方法功能:获取主机ip)
	 * @param netAddress
	 * @return
	 */
	public static String getHostIP(InetAddress netAddress) {
		if (netAddress == null) return null;
		return netAddress.getHostAddress();
	}
	/**
	 * @Title: getHostName
	 * @Description: TODO(描述方法功能:获取主机name)
	 * @param netAddress
	 * @return
	 */
	public static String getHostName(InetAddress netAddress) {
		if (netAddress == null) return null;
		return netAddress.getHostName();
	}

	/**
	 * @Title: main
	 * @Description: TODO(描述方法功能:)
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getHostIP(getInetAddress()));
		System.out.println(getHostName(getInetAddress()));
	}

}
