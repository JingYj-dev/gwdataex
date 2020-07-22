<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<ww:bean name="'com.hnjz.core.dict.DictMan'" id="dictMan" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>资源共享课申报提交工具</title>
		<link href="<ww:property value="#dictMan.getDictType('d_sns_para',200001).description"/>common/hep/css/css.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<ww:property value="#dictMan.getDictType('d_sns_para',200001).description"/>common/js/cookie.js"></script>
		<script type="text/javascript" src="<ww:property value="#dictMan.getDictType('d_sns_para',200001).description"/>common/js/global.js"></script>
		<style type="text/css">
		#head {background: url(<ww:property value="#dictMan.getDictType('d_sns_para',200001).description"/>common/images/top.gif) 0 0 no-repeat; margin: 0 auto; width: 100%; height: 106px;}
		</style>
	</head>
	<body id="Body">
	<%-- 
		<div id="head"></div>
		 --%>
		<div style="padding:0; margin:20px auto 0; width:642px; height:282px;">
			<div id="Win" class="error">
				<div style="margin-left :90px; text-align:left">
					<span class="er">信息提示：</span>
					<p style="margin-left:30px; width:460px;">
						<ww:if test="#session.message!=null && !#session.message.equals(\"\")">
							<ww:property value="#session.message"/>
						</ww:if>
						<ww:else>由于网络或系统的原因，操作异常，请联系系统管理员！</ww:else>
					</p>
					<span style="margin-left:400px;">
						<a href="javascript:void(0)" onclick="gfp()">返回首页»</a>
					</span>
				</div>
			</div>
		</div>
		<script type="text/javascript">
		function gfp(){
			setFirstTag();
			var s = window;
			var p = s.parent;
			var i=100;
			while (i>0) {
				var up = null;
				try {
					up = p.location.hostname;
				} catch(e) {
					s.location = '<%=basePath%>loginEntrance.action';
					break;
				}
				var us = s.location.hostname;
				if (up==us) {
					s = p;
					p = p.parent;
				} else {
					s.location = '<%=basePath%>loginEntrance.action';
					break;
				}
				i--;
			}
			if (p.location.hostname=='<%=request.getServerName()%>') {
				s.location = '<%=basePath%>loginEntrance.action';
			}
		}
		</script>
	</body>
</html>