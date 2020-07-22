package com.hnjz.apps.oa.dataexsys.service.ws;

import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.db.hibernate.ThreadLocalMap;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import java.util.Calendar;

public class DataexReqHandler extends BasicHandler {
	
	private static final Log _logger = LogFactory.getLog(DataexReqHandler.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public void invoke(MessageContext mc) throws AxisFault {
//		String status = (String) this.getOption("status");
		/*HttpServletRequest request = (HttpServletRequest) mc.getProperty(org.apache.axis.transport.http.HTTPConstants.MC_HTTP_SERVLETREQUEST);
		_logger.info(request.getRemoteAddr());*/
		ClientInfo clientInfo = (ClientInfo) ThreadLocalMap.get(Constants.KEY);
		//TODO XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		_logger.info(Calendar.getInstance().getTime().getTime() - clientInfo.getStartTime().getTime() + "\n" + getIP(clientInfo.getRequest()));
//		ThreadLocalMap.put(Constants.KEY, mc);
//		System.out.println("HelloWorldHandler's status is: " + status + "\nsoapURI:" + mc.getSOAPActionURI());
	}
	
	public String getIP(ServletRequest request){
		String ip = "127.0.0.1";
		if(request==null)return ip;
		try {
			String addr = request.getRemoteAddr();
			if(addr!=null)return addr;
		} catch (Exception e) {
			e.printStackTrace();
			return ip;
		}
		return ip;
	}

}
