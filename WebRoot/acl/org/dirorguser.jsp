<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="user_form" id="user_form" action="dirOrgUser.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="parentId" id="parentId" value="<ww:property value='parentId'/>">
	<input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text"id="loginName" name="loginName"class="input-medium" placeholder="请输入登录名" value="<ww:property value='loginName'/>"></li>
				<li><input type="text"id="realName" name="realName"class="input-medium" placeholder="请输入真实姓名 " value="<ww:property value='realName'/>"></li>
				<li><button class="btn green" type="submit">查询</button></li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<a class="btn" onclick="$.cssTable.act('batch','addOrgUser.action?parentId=<ww:property value='parentId'/>');">批量添加</a>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="5%"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="15%" order-field="loginName" class="order">登录名</th>
				<th width="15%" order-field="realName" class="order">姓名</th>
				<th width="10%" order-field="sex" class="order">性别</th>
				<th width="15%" order-field="userType" class="order">类型</th>
				<th width="15%" order-field="openFlag" class="order">状态</th>
				<th width="10%" order-field="orderNum" class="order">序号</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"
					<ww:if test="parentId.equals(orgId)">disabled checked</ww:if>/></td>
				<td><ww:property value="loginName"/></td>
				<td><ww:property value="realName"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_sex',sex).name" /></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_usertype',userType).name" /></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
				<td><ww:property value="orderNum" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
