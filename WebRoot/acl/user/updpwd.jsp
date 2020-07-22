<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<!DOCTYPE html>
<!--[if IE 7]> <html lang="en" class="ie7 no-js"> <![endif]-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="favicon.ico" />
    <title>电子公文交换平台</title>
    <!-- Site CSS -->
      <link href="cssui/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
      <link href="cssui/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
      <link href="cssui/layout/default.css" rel="stylesheet"/>
      <link href="cssui/layout/document.css" rel="stylesheet"/>
      	<ww:if test="#session.updpwd.skinId == null or ''.equals(#session.updpwd.skinId)">
  		<link class="current" href="cssui/themes/default/style.css" rel="stylesheet"/>
  		</ww:if>
  		<ww:else>
  		<link class="current" href="cssui/themes/<ww:property value="#session.updpwd.skinId"/>/style.css" rel="stylesheet"/>
  		</ww:else>
      <script type="text/javascript" src="lib/ajax-pushlet-client.js"></script>
    </head>
  <body class="page-header-fixed">
      <div class="header navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
        <a class="brand" href="explorer.jsp"><!-- <img src="../custom/brand.png" /> -->电子公文交换平台</a>
		<a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
		<img src="../cssui/images/menu-toggler.png" alt="logo">
		</a>
          <ul class="nav pull-right">
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
        <div class="page-content">
          <div id="tab-01">
            <div class="tab-content layoutBox">
              <div class="tab-pane active" id="home">
                <div class="row-fluid column-seperation">
                  <div class="editpass">
                    <div class="title"><ww:if test="#session.updpwd.activeStatus == 2">账号未被激活，请修改密码成功后，即完成激活！</ww:if>
                    					<ww:elseif test="#session.updpwd.editPwdTime == null">账号未更改初始密码，请修改密码！</ww:elseif>
                    					<ww:else>账号密码已过期，请修改密码！</ww:else></div>
                    <div class="con">
                    <input type="hidden" id="pwdLevel" name="pwdLevel" value="<ww:property value='pwdLevel'/>"/>
                    <input type="hidden" id="realpwd" name="realpwd" value="<ww:property value='#session.updpwd.password'/>"/>
                        <ul>
                          <li><span>用<em></em>户<em></em>名：</span><ww:property value="#session.updpwd.loginName"/>
                          <input type="hidden" class="input-large required" name="loginName" id="loginName" value="<ww:property value="#session.updpwd.loginName"/>" maxlength="20"></li>
                          <li><span>姓<em></em><em></em><em></em><em></em>名：</span><ww:property value="#session.updpwd.realName"/></li>
                          <li><span>原<em></em>密<em></em>码：</span>
                          		<input type="password" placeholder="请输入原密码" name="password" onfocus="passwordfocus()" onblur="passwordblur()" id="password" value="" onKeyUp="onPasswordMouseMove();" class="input-large required" maxlength="50"/>
                          		<em style="color:" class="err" id="PasswordShowEm"></em></li>
                          <li><span>新<em></em>密<em></em>码：</span>
                          		<input type="password" placeholder="请输入6-16位新密码" name="newPassword" class="input-large" id="newPassword" maxlength="50"
                          			onkeyup="pwStrength(this.value)" onblur="hiddenTable()"onfocus="pwStrength(this.value)">
                          			<em style="color:" class="err" id="newPasswordShowEm"></em>
                          			</li>
                          <li class="strength">
                            <table style="display:none" id="hiddenTable" border="0" cellpadding="0" cellspacing="0"> 
                              <tr> 
                                <td id="strength_L" class="l">弱</td>
                                <td id="strength_M" class="m">中</td> 
                                <td id="strength_H" class="h">强</td> 
                              </tr>
                            </table>
                          </li>
                          <li><span>确认密码：</span>
                          		<input type="password" placeholder="请再次输入新密码" class="input-large required" maxlength="50"
											name="repeatNewPassword" onKeyUp="hiddenTable()" id="repeatNewPassword" onblur="onRepeatNewPasswordMouseMove();" onfocus="repeatNewPasswordfocus()">
											<div id="repeatPasswordShow1" style="color:#ff0000;display:none;margin-top: -27px;margin-left: 290px; margin-right: 10px;">
                          						新密码与确认密码不一致</div></li>
                        </ul>
                      <div class="win_btn">
                      	<a href="#" class="submit" onclick="submit()">保存</a><a href="#" class="reset" onclick="reset()">取消</a>
                      </div>
                    </div>
                  </div> 
                </div>
              </div>
            </div>
          </div>
        </div>
        </div>
    <script type="text/javascript" src="cssui/js/easyloader.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.history.js"></script>
    <script type="text/javascript" src="cssui/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.validate.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/jquery.blockui.min.js"></script>
    <script type="text/javascript" src="cssui/plugins/lhgdialog/lhgdialog.min.js?skin=add"></script>
    <script type="text/javascript" src="cssui/plugins/ajaxupload/ajaxupload.3.6.min.js"></script>
    <script type="text/javascript" src="cssui/js/cssui.js"></script>
    <script type="text/javascript" src="cssui/js/app.js"></script>
    <script type="text/javascript" src="acl/user/js/updpwd.js"></script>
    <script type="text/javascript" src="custom/login.js"></script>
    <script type="text/javascript" src="cssui/plugins/encrypt/md5.js"></script>
    <script type="text/javascript">
    </script>
  </body>
</html> 