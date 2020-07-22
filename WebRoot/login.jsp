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
	<link href="newLogin/css/resetCSS.css" rel="stylesheet" type="text/css" />
	<link href="newLogin/css/login.css" rel="stylesheet" type="text/css" />
	<title>中科九洲电子公文交换系统</title>
	<%if(login_skin !=null && !"".equals(login_skin)) {%>
	<link rel="stylesheet" type="text/css" href="custom/<%=login_skin %>/style.css" id="login_skin"/>
	<%}else{ %>
	<link rel="stylesheet" type="text/css" href="custom/metro/style.css" id="login_skin"/>
	<%} %>
	
</head>
<!--display:none;  onload="loginReady();"  -->
<body  class="loginBody">
<script>
if(!! navigator.userAgent.match(/MSIE 7/)) {
	document.location.href = "msgie7.jsp";
}
</script>
<div class="gwloginBody">
	<div class="nullMargin"></div>
    <div class="login">
 	<div class="title">中科九洲电子公文交换系统</div>
      <div class="login_contentBg"></div>
      <h1 class="tit"></h1>
      
      <div class="ulCon" id="uldiv">
		  <form id="login-form" action="login.action" method="post"
				onsubmit="return validateCallback(this,loginCallback)">
<%--			<form id="login-form" action="j_spring_security_check" method="post" onsubmit="return validateCallback(this,loginCallback)">--%>
			<input type="hidden" name="sUser.password" id="pwd"/>
				<ul>
		            <li class="li1 clearfix"><span>用户名</span>
		              <input type="text" class="txt"  name="sUser.loginName" id="name" placeholder="用户名" value=""/>
		            </li>
		            <li class="li2 clearfix"><span>密　码</span>
		              <input type="password" class="txt" id="password" placeholder="密　码" value="" autocomplete="off"/>
		            </li>
		            <li class="li3">
		              <input type="button" class="loginS" onclick="$(this).parents('form').submit();" id="login" name="login" value="登 录"/>
		            </li>
		          </ul>
			</form>
		</div>
		<embed width="0" height="0" type="application/browser-username-extension"  id="pluginId">
    </div>
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
		
		var winH;
		 var mTop;
		 function initH(){
			 winH=$(window).height();
			 $(".gwloginBody").css({"height":winH+"px"});
			 if(winH<425){
			   mTop=0; 
			 }else{
			   mTop=(winH-425)*0.377;
			 }
			 $(".nullMargin").css({"height":mTop+"px"});
		 }
		 initH();
		 $(window).resize(function(){ initH();});
		 
		 $(document).ready(function(){
			 $('#uldiv').keydown(function(e){
				 if(e.keyCode==13){
					 $('#login-form').submit();
				 }
			 })
		 });
	</script>
</body>
</html>