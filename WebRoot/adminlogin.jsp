<%@page import="com.hnjz.util.StringHelper"%>
<%@page import="com.hnjz.webbase.util.CookieUtil"%>
<%@page import="com.hnjz.core.configuration.Environment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String cookieUserId = CookieUtil.GetCookies(Environment.Cookie_UserID, request);
	if(session.getAttribute(Environment.SESSION_LOGIN_KEY)!=null){
		response.sendRedirect("adminexplorer.jsp");
	}
	else if (StringHelper.isNotEmpty(cookieUserId)) {
		response.sendRedirect("adminAutoLogin.action");
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<title></title>
	<link rel="stylesheet" type="text/css" href="custom/style.css" />
</head>
<body>
	<div class="main" id="main_h">
		<div class="mod_905" id="mod_950_w">
			<div class="bg"></div>
		</div>
		<div class="mod_415">
			<div class="header">
				<div class="logo"></div>
				<div class="title">综合管理平台</div>
			</div>
			<div class="cont">
				<div class="login">
					<form id="login-form" action="adminLogin.action" method="post" onsubmit="return validateCallback(this,loginCallback)">
						<p class="account">
							<span class="type"></span>
							<input type="text" class="required" title="请输入用户名！" name="sUser.loginName" id="name" value="admin"/>
							<i class=""></i>
						</p>
						<p class="password">
							<span class="type"></span>
							<input type="password" class="required" title="请输入密码！" name="sUser.password" id="pwd" value="111111"/>
							<i class=""></i>
						</p>
						<p class="auth">
							<span class="type"></span>
							<input type="text" class="required" maxlength="4" title="请输入验证码！" name="checkCode" id="code" />
							<span class="code"><img src="<%=basePath%>rand.jsp" alt="验证码" 
				            		id="checkCodeImg" width="55px" style="border: 1px solid #000;"/></span>
							<i class="refresh" onclick="newImg();"></i>
						</p>
						<p class="rmb">
							<span><input type="checkbox" name="autoLogin" value="1" id="remember" />记住密码</span>
						</p>
						<a herf="#" class="submit" onclick="$(this).parents('form').submit();">登录</a>
					</form>
				</div>
			</div>
			<div class="footer">
				<div class="copyright">
					<p>版权所有: 中国软件与技术股份有限公司</p>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="cssui/js/easyloader.js"></script>
	<script type="text/javascript" src="custom/adminlogin.js"></script>
</body>
</html>