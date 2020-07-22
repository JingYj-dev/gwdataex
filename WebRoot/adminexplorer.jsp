<%@page import="com.hnjz.core.configuration.Environment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.webbase.menu.MenuFactory'" id="menuFactory" />
<% 
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
 		<!-- Site CSS -->
  		<link href="cssui/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
  		<link href="cssui/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
  		<link href="cssui/layout/default.css" rel="stylesheet"/>
  		<link href="cssui/layout/document.css" rel="stylesheet"/>
  		<link href="cssui/themes/metro/style.css" rel="stylesheet"/>
  		<script type="text/javascript" src="lib/ajax-pushlet-client.js"></script>
  	</head>
	<body class="page-header-fixed">
    	<div class="header navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container-fluid">
					<a class="brand" href="explorer.jsp"><img src="custom/brand.png"/></a>
					<a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
					<img src="cssui/images/menu-toggler.png" alt="">
					</a>             
					<ul class="nav pull-right">
						<li class="dropdown">
							<a class="user" href="indexMan.action">
								<span><ww:property value="#session.sUser.realName" /></span>
								<i class="icon-down"></i>
							</a>
						</li>
						<li class="dropdown">
							<a href="javascript:;">
							<i class="icon-setting"></i>
							</a>
						</li>
						<li class="dropdown">
							<a href="adminQuit.action">
							<i class="icon-quit"></i>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
    	<div class="page-container">
				<div class="page-sidebar" role="main">
					<div class="folder"><a></a></div>
					<ul class="page-sidebar-menu">
						<li class="start active ">
							<a href="index.html" title="首页">
							<i class="icon-home"></i> 
							<span class="title">首页</span>
							<span class="selected"></span>
							</a>
						</li>
						<ww:iterator value="#menuFactory.getUserMenu()" id="data1" status="data1">
							<li>
								<ww:if test="isLast">
									<a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" >
										<i class="<ww:property value="icon" />"></i> 
										<span class="title"><ww:property value="name" /></span>
									</a>
								</ww:if><ww:else>
									<a href="javascript:;"><i class="<ww:property value="icon" />"></i><span class="title"><ww:property value="name" /></span><span class="arrow "></span></a>
									<ul class="sub-menu">
										<ww:iterator value="menus" id="data2" status="data2">
										<li>
											<a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" >
												<i class="<ww:property value="icon" />"></i> 
												<span class="title"><ww:property value="name" /></span>
											</a>
										</li>
										</ww:iterator>
									</ul>
								</ww:else>
							</li>
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
						<ul class="nav nav-tabs" context-menu="menu1" style="margin-right:31px">
							<li class="active ">
								<a tabid="home" href="#home">首页</a>
							</li>
						</ul>
						<div class="tab-btn">
							<a href="javascript:;" data-toggle="dropdown" class="dropdown-toggle more"><i></i></a>
							<ul class="dropdown-menu">
							</ul>
						</div>
						<div class="tab-content layoutBox">
							<div class="tab-pane active" id="home">
								<div class="row-fluid column-seperation text-center"></div>
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
    <script type="text/javascript" src="cssui/plugins/jquery.history.js"></script>
    <script type="text/javascript" src="cssui/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="cssui/js/cssui.js"></script>
    <script type="text/javascript" src="cssui/js/app.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.validate.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.blockui.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/lhgdialog/lhgdialog.min.js?skin=add""></script>
    <script type="text/javascript" src="cssui/plugins/ajaxupload/ajaxupload.3.6.min.js"></script>
  	<script type="text/javascript" src='oa/attach/js/fwattach.js'></script>
  	<script type="text/javascript" src='oa/review/js/fwreview.js'></script>
  	<script type="text/javascript" src='oa/attach/js/swattach.js'></script>
  	<script type="text/javascript" src='oa/review/js/swreview.js'></script>
  	<script type="text/javascript" src='oa/review/js/userreview.js'></script>
  	<script type="text/javascript" src='oa/sw/js/swzhengwen.js'></script>
  	<script type="text/javascript" src='oa/fw/js/fwform.js'></script>
  	<script type="text/javascript" src='oa/fwzhengwen/fwzhengwen.js'></script>
       <script type="text/javascript">
    $('.page-sidebar').resizable({handles:'e',onResize:function(){
    }});
    g_user={id:'<ww:property value="#session.sUser.userId" />',name:'<ww:property value="#session.sUser.realName" />'};
    function showSetting() {
    	$("#settingLi").children().eq(0).click();
    }
    </script>
  </body>
</html>