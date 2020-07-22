<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.func.action.FuncItem'" id="funcItem" />
<form class="form-horizontal table-form" name="form1" id="form1" action="dirLogExt.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<input type="hidden" name="logType" id="logType" value="<ww:property value='logType'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text" id="opName" name="opName"  class="input-small"  placeholder="请输入操作人" value="<ww:property value='opName'/>"></li>
				<!-- <li>选择功能：<ww:select attributes="class='input-small'" name="'funcId'" id="funcId" value="funcId"  list="#funcItem.getSFuncIdList(userId,'1001')" listKey="funcId" listValue="name"></ww:select> </li> -->
			    <li>起止时间：<input class="input-small Wdate" id="beginDate" name="beginDate" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginDate)"/>'> 至 <input class="input-small Wdate" id="endDate" name="endDate" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginDate\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endDate)"/>'></li>
				<li>操作类型：<ww:select attributes="class='input-small'" name="'opType'" id="opType" value="opType"  list="#dictID.getDictListQuery('d_opertype')" listKey="code" listValue="name"></ww:select> </li>
				<!-- <li>日志类型：<ww:select attributes="class='input-small'" name="'logType'" id="logType" value="logType"  list="#dictID.getDictListQuery('d_logtype')" listKey="code" listValue="name"></ww:select> </li> -->
				<li>日志级别：<ww:select attributes="class='input-small'" name="'logLevel'" id="logLevel" value="logLevel"  list="#dictID.getDictListQuery('d_loglevel')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			      <ww:button css="btn green" caption="查询" type="submit"></ww:button>
			    </li>
			    <li>
			     <ww:button css="btn green" caption="日志导出" funcode="acl_log/dirLogExt" type="button" onclick="logExport();"></ww:button>
			    </li>
			</ul>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="14%" order-field="opTime" class="order">功能ID</th>
				<th width="14%" order-field="opType" class="order">操作类型</th>
				<th width="14%" order-field="opName" class="order">操作人</th>
				<th width="14%" order-field="opIp" class="order">操作IP</th>
				<th width="14%" order-field="opResult" class="order">操作结果</th>
				<th width="14%" order-field="logLevel" class="order">日志级别</th>
				<th width="25%" order-field="opTime" class="order">日志时间</th>
				<th width="6%">内容</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index"> 
			<tr ondblclick="javascript:$('#showLogData<ww:property value="#index.getIndex()"/>').click();">
				<td class="text-center"><ww:property value="funcId"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_funcType',opType).name" /></td>
				<td class="text-center"><ww:property value="opName"/></td>
				<td class="text-center"><ww:property value="opIp"/></td>
				<td class="text-center"><ww:property value="opResult"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_loglevel', logLevel).name" /></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(opTime)"/></td>
				<td class="text-center"><span class="expand"><i id="showLogData<ww:property value="#index.getIndex()"/>"  class="icon-chevron-down"></i></span></td>
			</tr>
			<tr class="sub">
				<td colspan="8" ><ww:property value="logData"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"><ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
using('datepicker');
function logExport() {
	var $tab = $.cssTab.focus(); 
	var logType = $("#logType", $tab).val();
	var beginDate = $("#beginDate", $tab).val();
	var endDate = $("#endDate", $tab).val();
	var opName = $("#opName", $tab).val();
	var opType = $("#opType",$tab).val();
	var logLevel = $("#logLevel",$tab).val();
	var val = "";
	if (!beginDate && !endDate && !opName && !opType && !logLevel) {
		if ("1" == logType) {
			val = "操作日志";
		} else if ("2" == logType) {
			val = "系统日志";
		} else {
			val = "安全日志";
		}
		val = "确定要导出所有" + val + "吗？这可能需要很长时间！";
	} else {
		val = "确定要导出日志吗？";
	}
	if (confirm(val)) {
		document.location="logExport.action?"
			+ "logType=" + logType 
			+ "&beginDate=" + beginDate
			+ "&endDate=" + endDate
			+ "&opName=" + opName
			+ "&opType=" + opType
			+ "&logLevel=" + logLevel;
	}
}
$.cssTab.focus().find('tbody tr').click(function (e) {
	/**单击选中行事件*/
	var $tab = $.cssTab.focus();
	$.cssTab.focus().find('tbody tr').click(function () {
		$.cssTab.focus().find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
	});
});
</script>