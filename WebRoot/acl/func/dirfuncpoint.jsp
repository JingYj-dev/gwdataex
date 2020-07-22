<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="func_form" id="func_form" action="dirFuncPoint.action?uuid=<ww:property value='uuid'/>"  onsubmit="return navTabSearch(this,'dirFuncPoint<ww:property value="uuid"/>')">
    <input type="hidden" name="parentId" id="parentId" value="<ww:property value='parentId'/>">
    <input type="hidden" name="sysId" id="sysId" value="<ww:property value='sysId'/>">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
	<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	
	<div class="table-header">
		<div class="table-btn">
			<div class="btn-group">
			  <ww:link funcode="acl_func/addFuncPoint" target="cssDialog" title="添加功能" css="btn" caption="添加" href="getFuncPoint.action?uuid=%{uuid}"></ww:link>	
			</div>
			<div class="btn-group">
		      <ww:link funcode="acl_func/delFuncPoint" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delFuncPoint.action');"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
				<th width="60px">操作</th>
				<th   >功能项代码</th>
				<th   >功能项名称</th>
				<th width="30px" >状态</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
				   <ww:link funcode="acl_func/delFuncPoint" caption="删除" onclick="$.cssTable.act('del','delFuncPoint.action?ids=%{uuid}');" href="javascript:;"></ww:link>
				</td>
				<td><ww:property value="actionCode"/></td>
				<td><ww:property value="actionName"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
