<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="opForm" id="opForm" action="dirOpLog.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text" id="operName" name="operName"  class="input-small"  placeholder="请输入操作人" value="<ww:property value='operName'/>"></li>
			    <li>操作时间：<input class="input-small Wdate" id="opBeginDate" name="opBeginDate" readonly="true" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'opEndDate\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(opBeginDate)"/>'> 
			    至 <input class="input-small Wdate" id="opEndDate" name="opEndDate" readonly="true" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'opBeginDate\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(opEndDate)"/>'></li>
				<li>操作类型：<ww:select attributes="class='input-small'" name="'operType'" id="operType" value="operType"  list="#dictID.getDictListQuery('d_opertype')" listKey="code" listValue="name"></ww:select> </li>
				<li>操作对象类型：<ww:select attributes="class='input-medium'" name="'opObjType'" id="opObjType" value="opObjType"  list="#dictID.getDictListQuery('d_objtype')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			      <ww:button css="btn green" caption="查询" funcode="acl_log/dirOpLog" type="submit"></ww:button>
			    </li>
			    <li>
			     <ww:button css="btn green" caption="导出" funcode="acl_log/opLogExport" type="button" onclick="opLogExport();"></ww:button>
			    </li>
			</ul>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
			    
				<th width="120px;" order-field="funcId" class="order">功能名称</th>
				<th width="95px;" order-field="opName" class="order">操作人</th>
				<th width="114px;" order-field="opIp" class="order">操作人IP</th>
				<th width="153px;" order-field="opTime" class="order">操作时间</th>
				<th width="77px;" order-field="opType" class="order">操作类型</th>
				<th width="113px;" order-field="opObjType" class="order">操作对象类型</th>
				<th width="*" order-field="serverName" class="order">服务器名称</th>
				<th width="100px;" order-field="serverIp" class="order">服务器IP</th>
				<th width="78px;" order-field="result" class="order">操作结果</th>
				<th width="28px;">内容</th>
				
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index"> 
			<tr ondblclick="javascript:$('#showLogData<ww:property value="#index.getIndex()"/>3').click();">
				<td class="text-center"><ww:property value="@com.hnjz.webbase.FunctionManager@getFuncActionDesc(funcId)"/></td>
				<td class="text-center"><ww:property value="opName"/></td>
				<td class="text-center"><ww:property value="opIp"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm').format(opTime)"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_opertype',opType).name" /></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_objtype',opObjType).name" /></td>
				<td class="text-center"><ww:property value="serverName"/></td>
				<td class="text-center"><ww:property value="serverIp"/></td>
				<td class="text-center">
					<ww:if test="result=='success'">成功</ww:if>
					<ww:if test="result=='error'">失败</ww:if>
				</td>
				<ww:if test="logData==null or logData==''">
					<td class="text-center">--</td>
				</ww:if>
				<ww:else>
					<td class="text-center iconspage"><span class="expand"><i id="showLogData<ww:property value="#index.getIndex()"/>3" class="icon_arrow_down"></i></span></td>
				</ww:else>
			</tr>
			<tr class="sub">
				<td colspan="10" width="1px;" id="poDate">
					<dir  style="max-width: 1100px">
						<ww:property value="logData"/>
					</dir>
				</td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
using('datepicker');
function opLogExport() {
	var $tab = $.cssTab.focus(); 
	var beginDate = $("#opBeginDate", $tab).val();
	var endDate = $("#opEndDate", $tab).val();
	var operName = $("#operName", $tab).val();
	var operType = $("#operType",$tab).val();
	var opObjType = $("#opObjType",$tab).val();
	var val = "";
	if (!beginDate && !endDate && !operName && !operType && !operType && !opObjType) {
		val = "操作日志";
		val = "确定要导出所有" + val + "吗？这可能需要很长时间！";
	} else {
		val = "确定要导出日志吗？";
	}
	$css.confirm(val,function() {
		document.location="opLogExport.action?"
			+ "&beginDate=" + beginDate
			+ "&endDate=" + endDate
			+ "&operName=" + operName
			+ "&operType=" + operType
			+ "&opObjType=" + opObjType;
	});
}

$.cssTab.focus().find('tbody tr').click(function (e) {
	 /**单击选中行事件*/
    var $tab = $.cssTab.focus();
    $tab.find('tbody tr').click(function() {
		$tab.find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
    });
});

function showOpDate(){
	
}
</script>