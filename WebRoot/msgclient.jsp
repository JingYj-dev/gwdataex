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
	.ui_dialog #content{
		padding:100px 30px 200px;
	}
</style>
<body>
<div id="content">
  <div id="mod">
    <div class="licon"></div>
	<div class="txt">
	  <div class="title">抱歉，您访问的页面出错了！</div>
	  <div class="info">
	    <ww:if test="#session.message!=null && !#session.message.equals(\"\")">
		  <ww:property value="#session.message"/>
	    </ww:if>
		<ww:else>由于网络或系统的原因，操作异常，请联系系统管理员！</ww:else>
	  </div>
	</div>		
  </div>
</div>
</body>
</html>
