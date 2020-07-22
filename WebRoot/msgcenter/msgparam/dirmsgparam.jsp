<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal table-form" name="form1" id="form1" method="post" action="dirMsgParam.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" onkeyup="value=this.value.replace(/[^0-9]+/g,'')" id="paramIdDir" name="paramId" maxlength="5" placeholder="请输入参数编号" value="<ww:property value='paramId'/>"></li>
				<li><input class="input-medium" type="text" id="paramNameDir" name="paramName" placeholder="请输入参数名称" maxlength="50" value="<ww:property value='paramName'/>"></li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="msgcenter_msgparam/dirMsgParam" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="msgcenter_msgparam/addMsgParam" css="btn" caption="添加" title="添加参数" target="cssDialog" rel="addMsgParam" href="getMsgParam.action"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="msgcenter_msgparam/delMsgParam" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delMsgParam.action');"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="80px">操作</th>
				<th width="50px" order-field="paramId" class="paramId">参数编号</th>
				<th width="180px">参数名称</th>
				<th width="200px">参数值</th>
				<th >备注</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="paramId" />"/></td>
				<td class="text-center">
					<ww:link funcode="msgcenter_msgparam/updMsgParam" caption="修改" title="修改参数"target="cssDialog" rel="getDict" href="getMsgParam.action?paramId=%{paramId}" ></ww:link>&nbsp;
					<ww:link funcode="msgcenter_msgparam/delMsgParam" caption="删除" onclick="$.cssTable.act('del','delMsgParam.action?ids=%{paramId}')" href="javascript:;"></ww:link>&nbsp;
				</td>
				<td class="text-center"><ww:property value="paramId"/></td>
				<td><ww:property value="paramName"/></td>
				<td><ww:property value="paramValue"/></td>
				<td><ww:property value="paramMemo"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
