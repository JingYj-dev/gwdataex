<%@page import="com.hnjz.core.configuration.Environment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.webbase.menu.MenuFactory'" id="menuFactory" />
<% 
    String path = request.getContextPath();
	if(session.getAttribute(Environment.SESSION_LOGIN_KEY)==null){
		response.sendRedirect("login.jsp");
	}
%>
<!DOCTYPE html>
<!--[if IE 7]> <html lang="en" class="ie7 no-js"> <![endif]-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<link rel="shortcut icon" href="favicon.ico" />
		<title>电子公文交换平台</title>
 		<!-- Site CSS -->
  		<link href="cssui/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
  		<link href="cssui/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
  		<link href="cssui/layout/default.css" rel="stylesheet"/>
  		<link href="cssui/layout/document.css" rel="stylesheet"/>
  		<ww:if test="#session.sUser.skinId == null or ''.equals(#session.sUser.skinId)">
  		<link class="current" href="cssui/themes/metro/icons.css" rel="stylesheet" />
  		<link class="current" href="cssui/themes/metro/style.css" rel="stylesheet"/>
  		</ww:if>
  		<ww:else>
  		<link class="current" href="cssui/themes/<ww:property value="#session.sUser.skinId"/>/icons.css" rel="stylesheet" />
  		<link class="current" href="cssui/themes/<ww:property value="#session.sUser.skinId"/>/style.css" rel="stylesheet"/>
  		</ww:else>
  	</head>
	<body class="page-header-fixed">
	<script>
	if(!! navigator.userAgent.match(/MSIE 7/)) {
		document.location.href = "msgie7.jsp";
	}
	</script>
    	<div class="header navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container-fluid">
					<a class="brand" href="explorer.jsp"><img src="custom/metro/brand.png" />电子公文交换平台</a>
					
					<ul class="nav pull-right">
						<li class="dropdown">
						   <ww:if test="#session.sUser.loginName == 'devadmin'">
							<a class="user" title="清空缓存" href="javascript:clearCache();">
							<!-- <i class="icon-setting"></i> -->清空缓存
							</a>
							</ww:if>
						</li>
						<li class="dropdown user">
							<ww:property value="#session.sUser.realName" />
						</li>
						
						<!--
						<li class="dropdown">
							<a class="dropdown" title="主题" href="javascript:showTheme();">
							 <i class="icon-setting"></i>主题
							</a>
						</li>-->
						  
						<li class="dropdown">
							<a class="user" title="设置" href="javascript:showSetting();">
							<!-- <i class="icon-setting"></i> -->设置
							</a>
						</li>
						
						<li class="dropdown">
							<a class="user" title="退出" href="quit.action">
							<!-- <i class="icon-quit"></i> -->退出
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
    	<div class="page-container">
				<div class="page-sidebar" role="main" style="overflow: auto;">
					<div class="folder"><a></a></div>
					<ul class="page-sidebar-menu">
						<li class="start active ">
							<a href="firstPage.action" rel="home" id="fpTab" target="cssTab" title="首页" refresh="true">
							<i class="icon_home"></i> 
							<span class="title">首&nbsp;&nbsp;&nbsp;&nbsp;页</span>
							<span class="selected"></span>
							</a>
						</li>
					    <li style="display:none;">
							<a href="getIndex.action?portalType=2"  rel="customizeportal"  target="cssTab" title="首页门户" refresh="true">
							<i class="icon_home"></i> 
							<span class="title">首&nbsp;页&nbsp;门&nbsp;户</span>
							<span class="selected"></span>
							</a>
						</li>
						<ww:iterator value="#menuFactory.getUserMenu()" id="data1" status="data1">
							<ww:if test="isLast">
							<li>
								<a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh=true>
									<i class="<ww:property value="icon" />"></i> 
									<span class="title"><ww:property value="name" /></span>
								</a>
							</li>
							</ww:if><ww:else>
							<li>
								<a href="javascript:;">
								<i class="<ww:property value="icon" />"></i>
								<span class="title"><ww:property value="name" /></span>
								<span class="arrow "></span></a>
							<%-- <li class="active">
								<a href="javascript:;"><i class="<ww:property value="icon" />"></i><span class="title"><ww:property value="name" /></span><span class="arrow open"></span></a> --%>
								<ul class="sub-menu">
									<ww:iterator value="menus" id="data2" status="data2">
									<ww:if test="isLast">
									<li>
										<a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh=true>
												<%--<i class="<ww:property value="icon" />"></i> --%><span class="title"><ww:property value="name" /></span>
										</a>
									</li>
									</ww:if><ww:else>
									<li>
										<a href="javascript:;"><span class="title"><ww:property value="name" /></span><span class="arrow "></span></a>
									<%-- <li class="active">
										<a href="javascript:;"><span class="title"><ww:property value="name" /></span><span class="arrow open"></span></a> --%>
										<ul class="sub-menu">
											<ww:iterator value="menus" id="data3" status="data3">
											<li>
												<a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" >
														<%--<i class="<ww:property value="icon" />"></i> --%><span class="title"><ww:property value="name" /></span>
												</a>
											</li>
											</ww:iterator>
										</ul>
									</li>
									</ww:else>
									</ww:iterator>
								</ul>
							</li>
							</ww:else>
						</ww:iterator>
					</ul>
      			</div>
				<div class="page-content">
					<div id="tab-01">
						<ul class="dropdown-menu" id="menu1">
							<li>
								<a href="javascript:;" rel="reload">刷新标签页</a>
							</li>
							<li>
								<a href="javascript:;" rel="close">关闭标签页</a>
							</li>
							<li>
								<a href="javascript:;" rel="closeOther">关闭其他标签页</a>
							</li>
							<li>
								<a href="javascript:;" rel="closeAll">关闭全部标签页</a>
							</li>
						</ul>
						<ul class="nav nav-tabs" context-menu="menu1" style="margin-right:48px">
							<li class="active ">
								<a ptabid="home" tabid="home" href="#home" data-href="firstPage.action" style="text-align:center">首&nbsp;&nbsp;&nbsp;&nbsp;页</a>
							</li>
						</ul>
						<div class="tab-btn">
							<a href="javascript:;" data-toggle="dropdown" class="dropdown-toggle more"><i></i></a>
							<ul class="dropdown-menu">
							</ul>
						</div>
						<div class="tab-content layoutBox">
							<div class="tab-pane active" id="home">
							</div>
						</div>
					</div>
				</div>
    		</div>
    	<!--
    	<footer class="footer text-center">
    		<div class="footer-inner">
				2013 © MMMMMM by LLLLL.
			</div>
    	</footer>	
    	-->
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
    <script type="text/javascript">
    /* $('.page-sidebar').resizable({handles:'e',onResize:function(){
    }}); */
    
  //初始化加载
    $(function (){
  	 
      window.setInterval(funGetCertDN,10000);
    });
  
  /*单点登录退出模块funGetCertDN
  */
  function funGetCertDN() 
  {
      var provider, container, pin, outdata, certtype, certdata;
      provider = this.document.getElementById("textProvider").value;
      container = this.document.getElementById("textContainer").value;
      pin = this.document.getElementById("textPin").value;
		certtype=parseInt(0);
      outdata = this.document.getElementById("usbControl").GetCertDN(provider, container, pin, certtype);
      $.ajax({
    		url:'<%=path%>/user/checkLoginOut',
    		type:"POST",
    		data:{"dn":outdata},
    		success:function(result){
    			if(result=='0'){
    				alert("当前用户usb-key已拔出,请重新插入usb-key访问本系统");
    				window.opener=null;
    				window.open('','_self');
    				window.close();
    			}
    		}
    		
    	}); 
  }
    
    g_user={id:'<ww:property value="#session.sUser.userId" />',name:'<ww:property value="#session.sUser.realName" />'};
    function showSetting() {
    	//$("#settingLi").children().eq(0).click();
    	$('#tab-01').cssTab('addTab',{
            id:'F_0169',
            title:'设置',
            url:'getUserSettings.action',
            active:true
        });
    }
    function showTheme(){
    	$css.openDialog({
    		title:"选择主题风格",
    		url:"getThemeChange.action",
    		lock:false
    	});
    }
    function clearCache() {
    	$.post("clearCache.action", {}, function(data) {
        	$css.tip(data.msg);
        },"json");
    }
    
    $(function() {
		$css.post('firstPage.action',function(data){
			$("#home").html(data);
			$('[href="#home"]').data("loaded",1);
		},'json');
    });
    </script>
  </body>
</html>