package com.hnjz.apps.base.user.action.login;

import com.westone.middleware.toolkit.trustService.TrustServiceException;
import com.westone.middleware.toolkit.trustService.identity.IdentityVerifyServiceClient;
import com.westone.web.mvc.JsonResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChallengeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569067299664278187L;
	
	private String identityVerifyServiceEndpoint;
	
	private String timeout;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		Properties p=new Properties();
    	InputStream in;
			try {
				in=this.getClass().getClassLoader().getResourceAsStream("wst.properties");
				p.load(in);
				identityVerifyServiceEndpoint = p.getProperty("identityVerifyServiceEndpoint");
				timeout = p.getProperty("timeout");
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonResult result = new JsonResult();
		HttpSession session = request.getSession();
		try {
			String challenge = getIdentityVerifyChallenge();
			System.out.println(challenge);
			if(!challenge.equals("")){
				session.setAttribute("challenge", challenge);
				result.setCode(0);
				result.setMessage(challenge);
			}else{
				result.setCode(-1);
				result.setMessage("申请随机数失败");
			}
			
		} catch (TrustServiceException e) {
			result.setCode(-1);
			result.setMessage(e.getMessage());
		}
		
		try {
			response.setContentType("text/html;charset=UTF-8");
			System.out.println(result.toString());
			response.getWriter().write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getIdentityVerifyChallenge() throws TrustServiceException {
		IdentityVerifyServiceClient ivsc = new IdentityVerifyServiceClient(identityVerifyServiceEndpoint);
		return ivsc.generatorChallenge(Integer.parseInt(timeout));

	}
	
	
}
