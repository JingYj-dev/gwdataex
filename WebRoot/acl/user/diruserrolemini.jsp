<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.sys.action.SysItem'" id="sysID" />
<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
<div class="table-header" style="padding:3px 0;border-bottom: 1px solid #DDDDDD;">
				<select id="sysIdsearch" name="sysIdsearch" class="input-medium" onchange="search()">
					<option value="" >--请选择系统--</option>
					<ww:iterator value="#sysID.getOpenSystems()">
						<option value="<ww:property value='uuid'/>" <ww:if test="uuid == sysIdsearch">selected</ww:if> >
						<ww:property value="name"/>
						</option>
					</ww:iterator>
				</select>
</div>
<div style="overflow-y:auto;height:240px;">
<table class="table table-bordered">
	<thead>
		<tr>
			<th width="20px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
			<th width="150px">角色名称</th>
			<th  >角色描述</th>
		</tr>
	</thead>
	<tbody>
	    <ww:iterator value="page.results" id="data"> 
		<tr>
			<td class="text-center"><input type="checkbox" name="ids" id="ids" /></td>
			<td class="text-center"><ww:property value="name"/><input type="hidden"  name="roleid" value="<ww:property value='uuid'/>"/></td>
			<td class="text-center"><ww:property value="remark"/><input type="hidden" name="sysid" value="<ww:property value='sysId'/>"/></td>
		</tr>
		</ww:iterator>
	</tbody>
</table>
</div>
<div class="pagination"> <ww:property value="page.pageSplit"/></div>

