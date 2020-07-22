<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.func.action.FuncPointItem'" id="funcPointID" />
<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
<div class="table-header" style="padding:3px 0;border-bottom: 1px solid #DDDDDD;">
<select id="packageId" name="packageId" class="input-medium" onchange="search()">
					<option value="" >--请选择功能包--</option>
					<ww:iterator value="#funcPointID.getPackages(item.sysId)">
						<option value="<ww:property value='itemCode'/>" <ww:if test="itemCode == packageId">selected</ww:if> >
							<ww:property value="itemName"/>
						</option>
					</ww:iterator>
				</select>
</div>
<table class="table table-bordered">
	<thead>
		<tr>
			<th width="20px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
			<th width="200px">功能项名称</th>
			<th  >功能项代码</th>
		</tr>
	</thead>
	<tbody>
	    <ww:iterator value="page.results" id="data"> 
		<tr>
			<td class="text-center"><input type="checkbox" name="ids" id="ids"/></td>
			<td class="text-center"><ww:property value="actionDesc"/><input type="hidden" id="actionName" name="actionName" value="<ww:property value='actionDesc'/>"/></td>
			<td class="text-center"  style="word-break:break-all; word-wrap:break-all;"><ww:property value="actionId"/><input type="hidden" id="actionCode" name="actionCode" value="<ww:property value='actionId'/>"/></td>
		</tr>
		</ww:iterator>
	</tbody>
</table>
<div id="pageid" class="pagination"> <ww:property value="page.pageSplit"/></div>
