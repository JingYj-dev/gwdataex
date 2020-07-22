<%@ page language="java" contentType="text/html; charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@page import="com.hnjz.util.StringHelper"%>
<%@ taglib prefix="ww" uri="webwork"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title></title>

	
	<script type="text/javascript" src="cssui/js/easyloader.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.cookie.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.history.js"></script>
    <script type="text/javascript" src="cssui/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.validate.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.blockui.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/lhgdialog/lhgdialog.min.js?skin=add"></script>
    <script type="text/javascript" src="cssui/plugins/ajaxupload/ajaxupload.3.6.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/encrypt/md5.js"></script>
    <script type="text/javascript" src="cssui/js/cssui.js"></script>
    <script type="text/javascript" src="cssui/js/app.js"></script>
    <script type="text/javascript" src='flowbase/js/flowbase.js'></script>
  	<script type="text/javascript" src='oa/attach/js/fwattach.js'></script>
  	<script type="text/javascript" src='oa/review/js/fwreview.js'></script>
  	<script type="text/javascript" src='oa/attach/js/swattach.js'></script>
  	<script type="text/javascript" src='oa/review/js/swreview.js'></script>
  	<script type="text/javascript" src='oa/review/js/userreview.js'></script>
  	<script type="text/javascript" src='oa/sw/js/swzhengwen.js'></script>
  	<script type="text/javascript" src='oa/fw/js/fwform.js'></script>
  	<script type="text/javascript" src='oa/fwzhengwen/js/fwzhengwen.js'></script>
  	<script type="text/javascript" src='oa/gd/js/gd.js'></script>
  	<script type="text/javascript" src='oa/sw/js/swform.js'></script>
  	
  	<link rel="stylesheet" type="text/css" href="custom/style.css" />
  	<script type="text/javascript" src="custom/login.js"></script>
  	
	<script language="javascript">
	function loginCallback(json){
		if(json.result==0){
			loadMain();
		}else if(json.result==1){
			alert(json.msg);
		}else if(json.result==2){
			window.location="getUpdPassword.action";
		}
	}
	function loadMain(){
		$.ajax({
			  url: 'explorera.jsp',
		      type: 'POST',
			  dataType: 'html',
			  success: function(data){
				$(window).off("resize");
				$("body").hide();
				$("body").attr("class","page-header-fixed");
				$("link[href='custom/style.css']").remove();
				$("body").html(data);
				$("body").show();
			  }
		});
	}
	</script>
</head>
<body style="display:none;" onload="<ww:if test="#session.sUser!=null">loadMain();</ww:if><ww:else>loginReady();</ww:else>">
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
				<div class="title">电子公文处理系统</div>
				<div class="wel_txt">请登录</div>
					<p class="account">
						<span class="type"></span>
						<input type="text" class="required csspwd" title="用户名不能为空，且不包含空格！" name="sUser.loginName" id="name" value=""/>
						<i class=""></i>
					</p>
					<p class="password">
						<span class="type"></span>
						<input type="password" class="required csspwd" title="密码不能为空，且不包含空格！" name="password" id="password" value=""/>
						<i class=""></i>
					</p>
					<p class="auth">
						<span class="type"></span>
						<input type="text" class="required" maxlength="4" title="请输入验证码！" name="checkCode" id="code" />
						<span class="code"><img src="<%=basePath%>rand.jsp" alt="验证码" 
			            		id="checkCodeImg" width="55px" style="border: 1px solid #000;"/></span>
						<i class="refresh" onclick="newImg();"></i>
					</p>
					<div class="btn"><a herf="#" class="submit" onclick="$(this).parents('form').submit();">登 录</a>
					<a herf="#" class="reset" onclick='loginReset();'>取 消</a></div>
				</form>
		</div>
		<div style="clear:both;"></div>
	</div>
</body>
</html>