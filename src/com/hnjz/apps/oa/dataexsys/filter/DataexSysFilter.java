package com.hnjz.apps.oa.dataexsys.filter;

import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.db.hibernate.ThreadLocalMap;

import javax.servlet.*;
import java.io.IOException;
import java.util.Calendar;

public class DataexSysFilter implements Filter {

	@Override
	public void destroy() {
		

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setRequest(request);
		clientInfo.setStartTime(Calendar.getInstance().getTime());
		ThreadLocalMap.put(Constants.KEY, clientInfo);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
