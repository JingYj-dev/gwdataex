<%@page import="com.hnjz.util.StringHelper"%>
<%@page import="com.hnjz.webbase.util.CookieUtil"%>
<%@page import="com.hnjz.core.configuration.Environment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(session.getAttribute(Environment.SESSION_LOGIN_KEY)!=null){
	response.sendRedirect("explorer.jsp");
}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="cssui/plugins/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="cssui/plugins/jquery.idauth.min.js"></script>
	<meta http-equiv="X-UA-Compatible" content="IE=8"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title></title>
			
</head>

<!--display:none;  onload="loginReady();"  -->
<body  style="height:100%;overflow:hidden;">
<object id="authClientCtrl" classid="clsid:C966EBD9-49E9-4E9C-B854-270861C58382" style="display: none;" type="application/x-oleobject"></object>

<script type="text/javascript">
	$(function(){
		$.idauth({
            challengeUrl: '<%=path%>/user/getIdentityVerifyChallenge',
            idauthUrl: '<%=path%>/user/loginViaIdentityVerifyService',		                    
            controlId: 'authClientCtrl',
            onSuccess: function () {
            	window.location.href = 'sso.action';
            }
            
        });
	})		
</script>

</body>
</html>