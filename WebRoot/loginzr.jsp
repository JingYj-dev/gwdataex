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
		response.sendRedirect("explorer.jsp");
	}else if (StringHelper.isNotEmpty(cookieUserId)) {
		response.sendRedirect("autoLogin.action");
	}
	String login_skin  = CookieUtil.GetCookies("skin",request);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title></title>
	<%if(login_skin !=null && !"".equals(login_skin)) {%>
	<link rel="stylesheet" type="text/css" href="custom/<%=login_skin %>/style.css" id="login_skin"/>
	<%}else{ %>
	<link rel="stylesheet" type="text/css" href="custom/metro/style.css" id="login_skin"/>
	<%} %>
	
</head>
<!--display:none;  onload="loginReady();"  -->
<body  style="height:100%;overflow:hidden;" onload="loginReady();">
<script>
if(!! navigator.userAgent.match(/MSIE 7/)) {
	document.location.href = "msgie7.jsp";
}
</script>
	<div class="main" id="main">
		<div class="mod_l" id="mod_l">
			<i class="user_icon"></i>
		</div>
		<div class="mod_r" id="mod_r">
			<form id="login-form" action="login.action" method="post" onsubmit="return validateCallback(this,loginCallback)">
			<input type="hidden" name="sUser.password" id="pwd"/>
				<div class="title">电子公文交换平台</div>
				<div class="wel_txt">请登录</div>
					<p class="account">
						<span class="type"></span>
						<input type="text" class="required csspwd" title="用户名不能为空，且不包含空格！" name="sUser.loginName" id="name" value="sjjhadmin"/>
						<i class=""></i>
					</p>
					<p class="password">
						<span class="type"></span>
						<input type="password" class="required csspwd" title="密码不能为空，且不包含空格！" name="password" id="password" value="admin111111"/>
						<i class=""></i>
					</p>
					<p class="auth">
						<span class="type"></span>
						<input type="text" class="required" maxlength="4" title="请输入验证码！" name="checkCode" id="code" value=""/>
						<span class="code"><img src="<%=basePath%>rand.jsp" alt="验证码" 
			            		id="checkCodeImg" width="55px" style="border: 1px solid #000;"/></span>
						<i class="refresh" onclick="newImg();"></i>
					</p>
					<div class="btn"><a herf="#" class="submit" onclick="$(this).parents('form').submit();">登 录</a>
					<a herf="#" class="reset" onclick='loginReset();'>取 消</a></div>
				</form>
		</div>
		<div style="clear:both;"></div>
		<embed width="0" height="0" type="application/browser-username-extension"  id="pluginId">
	</div>
	<script type="text/javascript" src="cssui/plugins/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="cssui/plugins/jquery.validate.min.js"></script>
	<script type="text/javascript" src="cssui/plugins/encrypt/md5.js"></script>
	<script type="text/javascript" src="custom/metro/login.js"></script>
	<script type="text/javascript">
		var userNameCount = 0;
		function getUserName() {
			//if(!"id" in document.createElement('embed')){return;}
			if(!!navigator.userAgent.match(/Firefox/)){
				var plugin = document.getElementById("pluginId");
				if(!plugin){
					$('body').append('<embed width="0" height="0" type="application/browser-username-extension"  id="pluginId">');
					plugin = document.getElementById("pluginId");
				}
				if('getUserName' in plugin){
					//获取插件对象
					var userInput=document.getElementById("name");
					//调用插件方法获取用户名
					var userName=plugin.getUserName();
					if(userName && userName!=""){
						$('#name').val(userName)
							//.attr('disabled',true)
							.css('cursor','not-allowed');
					}
					//alert(plugin.exec());
				}else{
					userNameCount++;
					if(userNameCount<3){
						setTimeout(getUserName,300);
					}
				}
			}
		}
		$(document).ready(function(){
			$.ajax({
				type: 'POST',
				url:'getLoginStyle.action',
				//data:$form.serializeArray(),
				dataType:"json",
				cache: false,
				success: function(data){
					if(data.result==0){
						getUserName();
					}
				}
			});
		});
	</script>
</body>
</html>