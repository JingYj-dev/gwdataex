<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem'" id="dirItem" />
<form class="form-horizontal table-form" name="dirDataexSysTransLog_form" id="dirDataexSysTransLog_form" action="dirDataexSysTransAccount.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<table class="table table-bordered">
		<thead>
			<tr>
				<%-- <th width="25px" >序号</th> --%>
				<th width="150px" order-field="sendId" clas="order">发送方</th>
				<%-- <th order-field="serverName" class="order">服务器名称</th> --%>
				<th width="150px" order-field="serverName" class="order">接收方</th>
				<th order-field="msgType" class="order">报文类型</th>
				<th order-field="opType" class="order">操作类型</th>
				<th width="150px" order-field="opTime" class="order">操作时间</th>
				<%--<th order-field="opStatus" class="order">发送状态</th> --%>
				<th order-field="opStatus" class="order">操作状态</th>
				<th width="30px">备注</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index"> 
			<tr ondblclick="javascript:$('#showMemo<ww:property value="#index.getIndex()"/>').click();">
				<%--<td class="text-center"><ww:property value="#index.index+1"/></td> --%>
				<td class="text-left"><ww:property value="sendId" /></td>
			    <%-- <td class="text-left"><ww:property value="serverName" /></td> --%>
			    <td class="text-center"><ww:property value="serverName" /></td>
			    <td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_msgType', msgType).name"/></td>
			    <td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_opType', opType).name"/></td>
			    <td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(opTime)" /></td>
			    <%--<td class="text-center"><ww:property value="#dictID.getDictType('dataex_d_sendstate', opStatus).name"/></td>--%>
			    <td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_opstatus', opStatus).name" /></td>
			<td class="text-center iconspage"><ww:if test='memo == "" or memo == null'><span>--</span></ww:if>
				<ww:else><span class="expand"><i id="showMemo<ww:property value="#index.getIndex()"/>" class="icon_arrow_down"></i></span></ww:else></td>
			</tr>
			<tr class="sub">
			<td colspan="10" ><ww:property value="memo"/></td>
			</tr>
		  </ww:iterator>
		</tbody>
	</table>
	<div class="pagination"><ww:property value="page.pageSplit"/></div>
</form>
<div class="set-btn" data-spy="affix" data-offset-top="200">
	<a class="btn" href="javascript:;" onclick="$css.closeDialog();">关闭</a>
</div>
<script type="text/javascript">
using('datepicker');
$.cssTab.focus().find('tbody tr').click(function (e) {
	/**单击选中行事件*/
	var $tab = $.cssTab.focus();
	$.cssTab.focus().find('tbody tr').click(function () {
		$.cssTab.focus().find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
	});
});
function resetTransLogForm(){
	var $tab = $.cssTab.focus();
	$tab.find(':input','#dirDataexSysTransLog_form').not(':button, :submit,:radio, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
	$tab.find('input[type=radio][value=or]').attr('checked','checked');
}
</script>