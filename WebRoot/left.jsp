<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.user.obj.GUserItem'" id="userId" />
<div class="W_main_l" id="Box_left">
	<div class="left_nav">
		<div id="pl_leftNav_common">
		
			<div class="border">
				<dl class="person_info clearfix">
					<dt>
						<div class="imgSection">
								<img src="<ww:property value="#userId.getUserImgUrl()"/>user/<ww:property value="#session.gUser.id"/>.jpg.jpg?tSessionId=<%=System.currentTimeMillis()%>" onError="logo_user(this)" class="user-imgstyle" />
						</div>
					</dt>
					<dd class="nameBox">
						<div style="padding-top:10px;text-align:center;">
						<a href="../user/getUserInfo.action" class="name" title="ScorpioSoSo"><ww:property value="#session.gUser.realName"/></a>
						<p><a href="../user/getUserInfo.action">编辑资料</a></p>
						</div>
					</dd>
				</dl>
			</div>
			
			<div class="border">
				<ww:if test="#session.gUser.isSuperAdmin()">
				<dl>
					<dd id="dirDictType">
						<a href="../admin/dirDictType.action?type=d_para_g" class="title">系统管理</a>
					</dd>
					<dd id="bbsMan">
						<a href="../bbs/dirBbsTypeAdmin.action" class="title">论坛管理</a>
					</dd>
				</dl>
				</ww:if>

				<ww:if test="#session.gUser.isSuperAdmin()">
				<dl>
					<dd id="dirUser">
						<a href="../user/dirUserAdmin.action" class="title">用户管理</a>
					</dd>
				</dl>
				<ul id="groupTree" class="ztree" style="display:none;"></ul>
				<ul id="groupOper" style="display:none;">
					<div class="border2"></div>
					<center>
					<table class="leftTable">
						<tr>
							<td id="add"><img style="margin-bottom:-3px" src="../images/icon/btn_add.png" /> 增加</td>
							<td id="edit"><img style="margin-bottom:-3px" src="../images/icon/btn_edit.png" /> 编辑</td>
							<td id="remove"><img style="margin-bottom:-3px" src="../images/icon/btn_del.png" /> 删除</td>
						</tr>
					</table>
					</center>
					<div class="border2"></div>
				</ul>
				</ww:if>
				<ww:else>
				<dl>
					<dd id="dirUser">
						<a href="../user/dirUser.action" class="title">通讯录</a>
					</dd>
				</dl>
				<ul id="groupTree" class="ztree" style="display:none;"></ul>
				</ww:else>				
				<dl>
					<dd id="portalMan">
						<a class="title"><span onclick="document.location='../portal/index.action'">个人门户</span><span style="padding-right:45px;"></span><span onclick="document.location='../portal/indexMan.action'">管理</span></a>
					</dd>
					
					<dd id="dirBbs">
						<a class="title"><span onclick="document.location='../bbs/dirBbs.action'">交流论坛</span><span style="padding-right:45px;"></span><span onclick="document.location='../bbs/dirBbsType.action'">版块</span></a>
					</dd>
				</dl>
				<ul id="bbsTree" class="ztree" style="display:none;"></ul>
				<dl>
					<dd id="dirWlog">
						<a class="title"><span onclick="document.location='../wlog/dirWlog.action'">工作日志</span><span style="padding-right:45px;"></span><span onclick="document.location='../wlog/dirWlogMonth.action'">汇总</span></a>
					</dd>
				</dl>
				<ul id="userTree" class="ztree" style="display:none;"></ul>
				<dl>
				</dl>	
			</div>
		</div>
	</div>
</div>