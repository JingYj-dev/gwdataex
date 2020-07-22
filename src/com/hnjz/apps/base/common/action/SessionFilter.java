package com.hnjz.apps.base.common.action;

import com.hnjz.apps.base.common.Constants;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.IUser;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.opensymphony.webwork.dispatcher.FilterDispatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class SessionFilter extends FilterDispatcher{
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		if(request.getSession().getAttribute(Environment.SESSION_LOGIN_KEY)!=null){
			if(sessionTimeout(request,response)){
				IUser user = (IUser) request.getSession().getAttribute(Environment.SESSION_LOGIN_KEY);
				if(user!=null){
					request.getSession().setAttribute(user.getUserId(),null);
					MemCachedFactory.delete(Constants.ACCOUNT_LOGON+user.getUserId());
					request.getSession().setAttribute(Environment.SESSION_LOGIN_KEY,null);
				}
				//throw new ServletException(Messages.getString("systemMsg.sessionInvalid"));
				//response.sendRedirect("doError.action");
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
	private boolean sessionTimeout(HttpServletRequest request,HttpServletResponse response){
		String timeoutStr = SecurityService.getParamValue(SecurityParam.SESSION_TIMEOUT);
		try {
			long timeout = SecurityParam.DEFAULT_SESSION_TIMEOUT;
			if(StringHelper.isNotEmpty(timeoutStr)){
				timeout = Integer.parseInt(timeoutStr);
			}
			timeout = timeout * 1000;
			long exp = timeout + request.getSession().getLastAccessedTime();
			if (exp < new Date().getTime()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
