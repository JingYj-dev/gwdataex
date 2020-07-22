<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="ww" uri="webwork"%>
<%
	String path = request.getContextPath();
	String basePath = path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link href="<%=basePath%>resources/css/common.css" rel="stylesheet"
			type="text/css" />
		<script language="javascript"
			src="<%=basePath%>resources/js/jquery/jquery.js"></script>
		<script src="<%=basePath%>resources/js/common/common.js"></script>
		<script src="<%=basePath%>resources/js/common/formutil.js"></script>
	</head>
	<body>
		<form name="form1" id="form1" method="post" action="<%=basePath%>/param/dirSParam.action">
			<div style="padding-top: 0px;"></div>
			<div class="tab_section">
				<div>
					<table border="0" cellspacing="0" cellpadding="0" id="label">
						<tr>
							<td width="10">
								<img src="<%=basePath%>/admin/images/label_left.gif" />
							</td>
							<td nowrap="nowrap"
								background="<%=basePath%>/admin/images/label_bg.gif">
								参数管理
							</td>
							<td>
								<img src="<%=basePath%>/admin/images/label_right.gif" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id=formTable>
				<table style="width: 100%">
					<tr>
						<th>
							<span class="nNull">*</span> 参数名称:
						</th>
						<td>
							<input class="tdTxt tdTxtWid3" type="text" name="name" id="name"
								onblur="Validator.ValidateOne(this,3)" datatype="Requre"
								okmsg="√" msg="请输入参数名称:!" require="true" />

						</td>
						<th>
							系统名称:
						</th>
						<td>
						 <%--  <ww:select name="sysid"  value="sysid" list="@com.hnjz.admin.sys.service.SystemService@getAvailabeSystems().resultObject" listKey="systemId" listValue="sysname" hint="----请选择----"/>
						  --%>
						  <ww:panelCheck id="sysid"  value="sysid" list="@com.hnjz.admin.sys.service.SystemService@getAvailabeSystems().resultObject" listKey="systemId" listValue="sysname" cols="2"/>
						</td>
						</tr>
						
						<tr>
						<th>
							状态:
						</th>
						<td>
							<select id="status" name="status">
								<option value="">--请选择--</option>
								<option value="2">关闭</option>
								<option value="1">开启</option>
							</select>
						</td>
						<td>
							<center id="submitId">
								<input class="cssBtn" type="button" value="查询 " onclick="
								javascript: document.getElementById('form1').submit();"
									name="submitQuery" />
								<input class="cssBtn" type="button" onclick="add('${123}');" value="新增" />
								<input class="cssBtn" type="button" onclick="del_param();"
									value="删除" />
								<input type="submit" name="conSubmit"
									style="position: absolute; top: -10000px; left: -10000px; index: 0" />
								<ww:button name="btnTest" funcode="forwardMain" caption="主页" onclick="" type="submit"/>
								<ww:link name="lnkA" funcode="forwardMain" caption="GO" href="javascript:void(0)" />
																								
								<input type="hidden" name="page.orderFlag" id="page.orderFlag"		value="<ww:property value="page.orderFlag"/>" />
								<input type="hidden" name="page.orderString" id="page.orderString" 	value="<ww:property value="page.orderString"/>" />
								<input type="hidden" name="paramname" id="paramname" />					
							</center>
						</td>
						</tr>
				</table>
			</div>
			<div id=list>
				<table>
					<!-- ================table header========================= -->
					<tr>
						<th align="center" width=5%>
							<input type="checkbox" name="checkdelall"
								id="checkdelall" onclick="funCheck('checkdel')" />
						</th>
						<th align="center" width=10%>
							操作
						</th>
						<th align="center" width=10%>
							<a href="javascript:SetOrder('name')">参数名称</a>
						</th>
						<th align="center" width=16%>
							<a href="javascript:SetOrder('sysid')">系统名称</a>
						</th>
						<th align="center" prop="value">
							<a href="javascript:SetOrder('value')">参数值</a>
						</th>
						<th align="center" width=10% prop="memo">
							<a href="javascript:SetOrder('memo')">描述</a>
						</th>
					</tr>
					<!-- ================end table header========================= -->
					<!-- ================table rows========================= -->
					<ww:iterator value="page.results">
						<tr>
							<td align="center" width="5%">
								<input type="checkbox" name="checkdel"
									value="<ww:property value="name"/>" />
							</td>
							<td align="center">
								<a
									href="javascript:op_submit_form('form1','<%=basePath%>param/showSParam.action',{'paramname':'<ww:property value="name"/>'})">查看</a>&nbsp;
								<a
									href="javascript:op_submit_form('form1','<%=basePath%>param/getSParam.action',{'paramname':'<ww:property value="name"/>'})">修改</a>
							</td>
							<td align="left">
								<ww:property value="name" />
							</td>
							<td align="center">
								<ww:property value="@com.hnjz.admin.sys.service.SystemService@getSystem(sysid).resultObject.sysname()"/>
							</td>
							<td align="left">
								<ww:property value="value" />
							</td>
							<td align="left">
								<ww:property value="memo" />
							</td>
						</tr>
					</ww:iterator>
					<tr>
						<td align="center" colspan="6">
							<ww:property value="page.pageSplit" />
						</td>
					</tr>
				</table>
				<!-- ================table end========================= -->
			</div>
		</form>
	</body>
</html>
