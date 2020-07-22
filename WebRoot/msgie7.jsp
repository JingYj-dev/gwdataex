<%@ page contentType="text/html; charset=UTF-8"%>
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
	<body id="Body">
	<script>
	if(!! navigator.userAgent.match(/MSIE 8/)
			||!! navigator.userAgent.match(/MSIE 9/)
			||!! navigator.userAgent.match(/MSIE 10/)) {
		document.location.href = "index.jsp";
	}
	</script>
		<div style="padding:0; margin:20px auto 0; width:642px; height:282px;">
			<div id="Win" class="error">
				<div style="margin-left :90px; text-align:left">
					<span class="er">信息提示：</span>
					<p style="margin-left:30px; width:460px;">
						暂不支持ie6，ie7，请使用ie8或者以上版本。
					</p>
				</div>
			</div>
		</div>
	</body>
</html>
