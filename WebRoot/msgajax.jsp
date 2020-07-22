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
		<title></title>
	</head>
	<body>
		<div style="width:400px;height:130px; margin-top:6px; border:0;" class="win_info" >
		  <i class="aler"></i>
		  <span style="font-size:16px;">
			  <ww:if test="result!=null && !result.equals(\"\")">
					<ww:property value="result"/>
				</ww:if>
				<ww:elseif test="#session.message!=null && !#session.message.equals(\"\")">
					<ww:property value="#session.message"/>
				</ww:elseif>
				<ww:else>操作异常，请联系系统管理员！</ww:else>
		</span>
		</div>	
		<div class="set-btn" data-spy="affix" data-offset-top="200">
	    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">关闭</a>
	    </div>
	</body>
</html>
