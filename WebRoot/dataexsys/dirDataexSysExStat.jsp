<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem'" id="dirItem" />
<form class="form-horizontal table-form" name="exStat_form" id="exStat_form" action="dirDataexSysExStat.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<input type="hidden" id="checkedId" value=""/>
	<input type="hidden" id="transmitFlag" value=""/>
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text" id="dirName" name="dirName"  class="input-medium"  placeholder="请输入机关名称" value="<ww:property value='dirName'/>" maxlength="30"></li>
			    <input value="查询" class="btn green" title="查询" type="submit">
				<li><ww:button css="btn reset" funcode="oa_dataex/dirDataexInbox" caption="清空" type="button" onclick="resetInboxForm();">清空</ww:button></li>
		    </ul>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th rowspan="2"  style="vertical-align:middle"><b>机关名称</b></th>
				<th colspan="5"><b>发文情况</b></th>
				<th colspan="5"><b>收文情况</b></th>
			</tr>
			<tr>
				<th><b>非密</b></th>
				<th><b>秘密</b></th>
				<th><b>机密</b></th>
				<th><b>绝密</b></th>
				<th><b>合计</b></th>
				<th><b>非密</b></th>
				<th><b>秘密</b></th>
				<th><b>机密</b></th>
				<th><b>绝密</b></th>
				<th><b>合计</b></th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="objList" id="data" status="rowStatus"> 
		    <ww:set name="arr" value="objList[#rowStatus.index]"/>
			<tr>
				<td class="text-center"><ww:property value="#dirItem.getDirByDirOrg(#arr[0]).dirName" /></td>
				<td class="text-center"><ww:property value="#arr[1]" /></td>
				<td class="text-center"><ww:property value="#arr[2]" /></td>
				<td class="text-center"><ww:property value="#arr[3]" /></td>
				<td class="text-center"><ww:property value="#arr[4]" /></td>
			    <td class="text-center"><ww:property value="#arr[5]" /></td>
			    <td class="text-center"><ww:property value="#arr[6]" /></td>
				<td class="text-center"><ww:property value="#arr[7]" /></td>
				<td class="text-center"><ww:property value="#arr[8]" /></td>
				<td class="text-center"><ww:property value="#arr[9]" /></td>
				<td class="text-center"><ww:property value="#arr[10]" /></td>
			</tr>
		  </ww:iterator>
		</tbody>
	</table>
	<div class="pagination"><ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
//单击选中单条记录并为隐藏变量赋值
jQuery(function($) {
	/**添加绑定事件:单击选中行*/
    var $tab = $.cssTab.focus();
    $tab.find('tbody tr').click(function() {
		$tab.find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
    });
});

function resetInboxForm(){
	var $tab = $.cssTab.focus();
	$tab.find(':input','#dirDataexInbox_form').not(':button, :submit,:radio, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
	$tab.find('input[type=radio][value=or]').attr('checked','checked');
}
</script>