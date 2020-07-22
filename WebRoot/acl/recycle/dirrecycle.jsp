<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="recycle_form" id="recycle_form" action="dirRecycle.action"  onsubmit="return navTabSearch(this)">
	<input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li>操作人：<input type="text" maxlength="50" id="operatorName" name="operatorName"class="input-medium" placeholder="请输入真实姓名" value="<ww:property value='operatorName'/>"></li>
				<li>业务类型：<ww:select attributes="class='input-small'" name="'funcName'" id="funcName" value="funcName"  list="#dictID.getDictListQuery('dataex_d_gwtype')" listKey="code" listValue="name"></ww:select></li>
                <li>删除时间：<input id="d1" class="input-medium Wdate" id="beginCollectTime" name="beginCollectTime" readonly="true" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'d2\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginCollectTime)"/>'> 至 <input id="d2" class="input-medium Wdate" id="endCollectTime" name="endCollectTime" readonly="true" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'d1\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endCollectTime)"/>'></li>
                <li><ww:button css="btn green" caption="查询" funcode="acl_recycle/dirRecycle" type="submit"></ww:button></li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_recycle/recoverRecycle" css="btn" caption="批量还原" onclick="$.cssTable.act('recoverBatch','recoverRecycle.action');"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="acl_recycle/delRecycle" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delRecycle.action');"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:link funcode="acl_recycle/delAllRecycle" css="btn" caption="清空回收站" onclick="$.cssTable.act('delAll','delAllRecycle.action');"></ww:link>  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="100px">操作</th>
				<th width="160px" order-field="topic" class="order">标题</th>
				<th width="180px" order-field="funcName" class="order">业务类型</th>
				<th width="160px" order-field="createdTime" class="order">删除时间</th>
				<th width="80px" order-field="opName" class="order">操作人</th>
				<th order-field="delReason" class="order">删除原因</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="entityId" />"/></td>
				<td class="text-center">
					<ww:link funcode="acl_recycle/recoverRecycle" caption="还原" onclick="$.cssTable.act('recover','recoverRecycle.action?ids=%{entityId}')" 
						 style="display:inline-block;" href="javascript:;"></ww:link>&nbsp;
					<ww:link funcode="acl_recycle/delRecycle" caption="删除" onclick="$.cssTable.act('del','delRecycle.action?ids=%{entityId}')" 
						 style="display:inline-block;" href="javascript:;"></ww:link>&nbsp;
					</td>
				<td ><ww:property value="topic"/></td>
				<td class="text-center"><ww:property value="funcName"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(createdTime)" /></td>
				<td class="text-center"><ww:property value="opName" /></td>
				<td class="text-center"><ww:property value="delReason"/></td>
			</tr>
			<!--<tr class="sub">
			<td colspan="7" ><ww:property value="delReason"/></td>
			
			</tr>
			--></ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
