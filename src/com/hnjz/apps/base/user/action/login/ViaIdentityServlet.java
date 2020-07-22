package com.hnjz.apps.base.user.action.login;

import com.hnjz.core.configuration.Environment;
import com.hnjz.apps.base.user.service.UserService;
import com.hnjz.apps.base.user.model.SUser;
import com.westone.middleware.toolkit.trustService.TrustServiceException;
import com.westone.middleware.toolkit.trustService.identity.IdentityVerifyServiceClient;
import com.westone.middleware.toolkit.trustService.identity.VerifyIdTRet;
import com.westone.middleware.toolkit.trustService.identity.VerifyIdentityTicketResult;
import com.westone.middleware.toolkit.trustService.identity.VerifyIdentityTicketResult.Userinfo;
import com.westone.utilities.XmlUtilities;
import com.westone.web.mvc.JsonResult;
import com.westone.web.security.Identity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ViaIdentityServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1151967714088520164L;

	private String identityVerifyServiceEndpoint;

	private String identityVerifyServiceAppId;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		Properties p = new Properties();
		InputStream in;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("wst.properties");
			p.load(in);
			identityVerifyServiceAppId = p.getProperty("identityVerifyServiceAppId");
			identityVerifyServiceEndpoint = p.getProperty("identityVerifyServiceEndpoint");
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonResult result = new JsonResult();
		HttpSession session = request.getSession();
		String ticket = request.getParameter("signAndToken");
		String challenge = (String) session.getAttribute("challenge");
		try {
			SUser user = loginViaIdentityVerifyService(challenge, ticket);

			if (user == null) {
				result.setCode(-1);
				result.setMessage("单点登录失败");
			} else {

				session.setAttribute(Environment.SESSION_LOGIN_KEY, user);
				result.setCode(0);
				Identity identity = new Identity(user.getUuid(), user.getLoginName(), user.getRoleList().toString(),
						null, null);
				request.getSession().setAttribute("IdentityTicket", identity);

			}

		} catch (TrustServiceException ex) {

			result.setCode(5);
			result.setMessage(ex.getMessage());
		}

		try {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 使用统一认证服务登录
	 * 
	 * @param challenge
	 *            挑战数
	 * @param signAndToken
	 *            挑战数签名和身份票据
	 * @return 用户标识
	 * @throws Exception
	 */
	public SUser loginViaIdentityVerifyService(String challenge, String signAndToken) throws TrustServiceException {

		IdentityVerifyServiceClient ivsc = new IdentityVerifyServiceClient(identityVerifyServiceEndpoint + "/VerifyIdentityTicket");
		VerifyIdTRet verifyIdTRet = ivsc.VerifyIdentityTicket(challenge, signAndToken, identityVerifyServiceAppId);
		if (verifyIdTRet == null) {
			return null;
		}
		VerifyIdentityTicketResult vitr;

		SUser user = new SUser();
		if (verifyIdTRet.getResult().length() > 1) {
			//String result = new String(Base64.decode(verifyIdTRet.getResult()));
			// System.out.println(result);
			vitr = XmlUtilities.deserialize(VerifyIdentityTicketResult.class, verifyIdTRet.getResult());
			// System.out.println(vitr);
			Userinfo userinfo = vitr.getUserinfo();
			user = UserService.getUser(userinfo.getRmsid().replaceAll("[\\pP\\p{Punct}]", ""));
			return user;
		} else {
			return null;
		}
	}
}
