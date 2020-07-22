package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.user.model.SUser;
import com.westone.jseapi.JseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.cert.CertificateException;

public class CheckLoginOut extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String dn=req.getParameter("dn");
		HttpSession session=req.getSession();
		SUser suser=(SUser) session.getAttribute("sUser");
		try {
			String sDn= CaUtil.getDN(suser.getCert().toString());
			if(sDn.equals(dn)){
				resp.getWriter().write("1");//1表示uk没有拔出
			}else{
				resp.getWriter().write("0");//0表示uk拔出
				session.removeAttribute("sUser"); 
			}
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.doPost(req, resp);
	}

}
