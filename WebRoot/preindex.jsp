<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>信息提示</title>
	</head>
	<style type="text/css">
	   *{
			padding:0px;
			margin:0px;
		}
		body{
			background-color: #f8f8f8;
		}
		#content{
			margin:0 auto; 
			position:relative;
			width: 435px;
			padding-top: 220px;
		}
		.ui_dialog #content{
			padding:100px 30px 200px;
		}
		#content #mod{
			padding-left: 0px;
		}
		#content .licon{
			float: left;
			background-image: url(cssui/images/error_icon.png);
			width: 80px;
			height: 80px;
			margin-right: 15px;
		}
		#content .txt{
			float: left;
			font-family:"Microsoft YaHei";
			font-size: 16px;
			color: #317caf;
			margin-top:10px;
		}
		#content .txt .title{
			font-size: 26px;
			padding-bottom: 5px;
		}
	</style>
<body id="Body">
<div id="content">
  <div id="mod">
    <div class="licon"></div>
	<div class="txt">
	  <div class="title">页面超时，请重新登录！</div>
	  <br/>
	  <div class="info">
	    <span id="timeSpan" style="color:red;"></span>秒后返回登录页面
	  </div>
	</div>		
  </div>
</div>
<script>
	i=3;
	function gfp(){
		if(i == 0)
			location = 'index.jsp';
		document.getElementById('timeSpan').innerHTML = i--;
	}
	gfp();
	setInterval(gfp, 1000);
</script>
</body>
</html>
		


