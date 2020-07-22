<%@page import="com.hnjz.core.configuration.Environment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.webbase.menu.MenuFactory'" id="menuFactory" />
<link rel="shortcut icon" href="favicon.ico" />
	<!-- Site CSS -->
<link href="cssui/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
<link href="cssui/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="cssui/layout/default.css" rel="stylesheet"/>
<link href="cssui/themes/metro/style.css" rel="stylesheet"/>
<link href="cssui/layout/document.css" rel="stylesheet"/>
<div class="header navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container-fluid">
					<a class="brand" href="explorer.jsp"><img src="custom/brand.png" />电子公文处理系统</a>
					
					<ul class="nav pull-right">
						<li class="dropdown">
							<a class="user" title="清空缓存" href="javascript:clearCache();">
							<!-- <i class="icon-setting"></i> -->清空缓存
							</a>
						</li>
						<li class="dropdown user">
								<ww:property value="#session.sUser.realName" />
								<!-- <i class="icon-down"></i> -->
						</li>
						<!--  
						<li class="dropdown">
							<a href="javascript:;">
							主题
							</a>
						</li>
						-->
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
							<a href="firstPage.action" rel="home" id="fpTab" target="cssTab" title="首页" refresh=true>
							<i class="icon_home"></i> 
							<span class="title">首页</span>
							<span class="selected"></span>
							</a>
						</li>
						<li id="settingLi" style="display: none">
								<a href="getUserSettings.action" rel="F_0164" target="cssTab" title="设置" >
									<span class="title">设置</span>
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
												<i class="<ww:property value="icon" />"></i> <span class="title"><ww:property value="name" /></span>
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
														<i class="<ww:property value="icon" />"></i> <span class="title"><ww:property value="name" /></span>
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
    <script type="text/javascript" src="cssui/plugins/lhgdialog/lhgdialog.min.js?skin=add"></script>
    <script type="text/javascript" src="cssui/js/app.js"></script>
  	
    <script type="text/javascript">
    /* $('.page-sidebar').resizable({handles:'e',onResize:function(){
    }}); */
    g_user={id:'<ww:property value="#session.sUser.userId" />',name:'<ww:property value="#session.sUser.realName" />'};
    function showSetting() {
    	$("#settingLi").children().eq(0).click();
    }
    function clearCache() {
    	$.post("clearCache.action", {}, function(data) {
        	$css.tip(data.msg);
        },"json");
    }
    $(function() {
		$css.post('firstPage.action',function(data){
			$("#home").html(data);
		},'json');
    });
    </script>