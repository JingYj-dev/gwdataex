<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="post_form"  id="post_form" action="dirSAlertSetting.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li>事件类型：<ww:select attributes="class='input-medium'" name="'eventType'" id="eventType" value="eventType"  list="#dictID.getDictListQuery('d_eventtype')" listKey="code" listValue="name"></ww:select> </li>
			    <li>重要程度：<ww:select attributes="class='input-medium'" name="'severLevel'" id="severLevel" value="severLevel"  list="#dictID.getDictListQuery('d_loglevel')" listKey="code" listValue="name"></ww:select> </li>
			    <li>预警方式：<ww:select attributes="class='input-medium'" name="'alertType'" id="alertType" value="alertType"  list="#dictID.getDictListQuery('d_alertType')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
			   <ww:link funcode="acl_log/addSAlertSetting"  rel="addSAlertSetting" target="cssDialog" css="btn" caption="添加" title="添加日志预警" href="getSAlertSetting.action"></ww:link>	
			</div>
			<div class="btn-group">
			   <ww:link funcode="acl_log/delSAlertSetting" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch','delSAlertSetting.action');"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="100px">操作</th>
				<th >事件类型</th>
				<th width="100px">重要程度</th>
				<th >预警方式</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="status"> 
		  	<%--<input type="hidden" id="alertType<ww:property value='#status.count'/>" name="" value="<ww:property value='alertType'/>"/>
			--%><tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="setId" />"/></td>
				<td class="text-center">
				    <ww:link funcode="acl_log/updSAlertSetting" caption="编辑"  title="编辑日志预警" target="cssDialog" rel="updSAlertSetting" href="getSAlertSetting.action?setId=%{setId}" ></ww:link>&nbsp;
				    <ww:link funcode="acl_log/delSAlertSetting" caption="删除" onclick="$.cssTable.act('del','delSAlertSetting.action?ids=%{setId}')" href="javascript:;"></ww:link>&nbsp;
				</td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_eventtype',eventType).name" /></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_loglevel',severLevel).name" /></td>
				<td class="text-center" id="td<ww:property value='#status.count'/>">
				<%--<script>
					var alertType = $("#alertType" + '<ww:property value="#status.count"/>').val().split(",");
					var alertTypeVal = "";
					//alert('<ww:property value="#dictID.getDictType('d_alertType', '1').name"/>');
					for(var i = 0; i < alertType.length; i++){
						var val = alertType[i];
						alertTypeVal += '<ww:property value="#dictID.getDictType('d_alertType','1').name"/>' + ",";
					}
					 $("#td" + '<ww:property value="#status.count"/>').html(alertTypeVal);
				</script>
						--%><ww:property value="@com.hnjz.apps.base.log.model.LogDict@getDictType('d_alertType',alertType)" />
				</td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
