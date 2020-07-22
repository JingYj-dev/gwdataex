<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="userAdmin_form" id="userAdmin_form" action="dirUserAdminMini.action"  onsubmit="return divSearch(this,'div_userAdmin')">
    <input type="hidden" name="orgId" id="orgId" value="<ww:property value='orgId'/>">
	<input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text"id="name" name="name"class="input-medium" maxlength="20" placeholder="请输入登录名或姓名" value="<ww:property value='name'/>" maxlength="20" ></li>
<%--				<li><input type="text"id="loginName" name="loginName"class="input-medium" placeholder="请输入登录名" value="<ww:property value='loginName'/>"></li>--%>
<%--				<li><input type="text"id="realName" name="realName"class="input-medium" placeholder="请输入姓名 " value="<ww:property value='realName'/>"></li>--%>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
				<li>类型：<ww:select attributes="class='input-medium'" name="'userType'" id="userType" value="userType"  list="#dictID.getDictListQuery('d_usertype')" listKey="code" listValue="name"></ww:select> </li>
				</ww:if>
				<li>状态：<ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictListQuery('d_security_Flag')" listKey="code" listValue="name"></ww:select> </li>
                <li>激活状态：<ww:select attributes="class='input-small'" name="'activeStatus'" id="activeStatus" value="activeStatus"  list="#dictID.getDictListQuery('d_active_status')" listKey="code" listValue="name"></ww:select> </li>
                <li><ww:button css="btn green" caption="查询" funcode="acl_user/dirUserAdminMini" type="submit"></ww:button></li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
			    <ww:dropdown funcode="acl_user/updUserStatus" value="model" list="#dictID.getDictType('d_security_Flag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updUserStatus.action?openFlag=%{#model.code}')" />  
		    </div>
			<div class="btn-group">
				<ww:link funcode="acl_user/updPassword" css="btn" caption="重置密码" title="重置密码"
					onclick="$.cssTable.act('updBatch',{url:'acl/user/getpassword.jsp',title:'重置密码'})"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="acl_user/perUserActiveStatus" css="btn" caption="允许激活" onclick="$.cssTable.act('batch','perUserActiveStatus.action');"></ww:link>
			</div>
			<div class="btn-group btnTab" style="float:right;">
				<input type="hidden" name="includeFlag" id="includeFlag" value="<ww:property value='includeFlag'/>">
				<a class="btn" value="1" href="javascript:;">当前组织</a>
				<a class="btn" value="2" href="javascript:;">包含下级</a>
			</div> 
		</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px" ><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="120px">操作</th>
				<th width="100px" order-field="loginName" class="order">登录名</th>
				<th width="80px" order-field="realName" class="order">姓名</th>
				<th width="80px" order-field="activeStatus" class="order">激活状态</th>
				<th width="30px" order-field="sex" class="order">性别</th>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<th width="30px" order-field="userType" class="order">类型</th>
				</ww:if>
				<th width="30px" order-field="openFlag" class="order">状态</th>
				<%--<th width="30px" order-field="orderNum" class="order">排序号</th>
				--%><th    >所属机构</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
					<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode() || !admin">
					<ww:link funcode="acl_user/dirUserRole" caption="授权" href="dirUserRole.action?userId=%{uuid}" target="cssTab" 
						style="display:inline-block;" rel="diruserrole%{uuid}"  title="用户%{realName}-授权">授权</ww:link>
					</ww:if>
					<ww:link funcode="acl_user/dirUserPost" caption="岗位设置" href="dirUserPost.action?userId=%{uuid}" target="cssTab" 
						style="display:inline-block;" rel="diruserpost%{uuid}"  title="用户%{realName}-岗位设置">岗位设置</ww:link>
				</td>
				<td ><ww:property value="loginName"/></td>
				<td><ww:property value="realName"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_active_status',activeStatus).name" /></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_sex',sex).name" /></td>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<td class="text-center"><ww:property value="#dictID.getDictType('d_usertype',userType).name" /></td>
				</ww:if>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_security_Flag',openFlag).name" /></td>
				<%--<td><ww:property value="orderNum" /></td>
				--%><td ><ww:property value="orgName" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
