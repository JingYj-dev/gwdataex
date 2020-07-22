<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="statForm" id="statForm" action="logStat.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
			    <li>统计月份：<input class="input-small Wdate" id="statBeginDate" name="statBeginDate" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'#F{$dp.$D(\'statEndDate\')}'})" value='<ww:property value="statBeginDate"/>'> 
			    至 <input class="input-small Wdate" id="statEndDate" name="statEndDate" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'statBeginDate\')}'})" value='<ww:property value="statEndDate"/>'></li>
			    <li>
			      <ww:button css="btn green" caption="查询" funcode="acl_log/logStat" onclick="statFormSubmit()"></ww:button>
			    </li>
			    <li>
			     <ww:button css="btn green" caption="导出" funcode="acl_log/logStatExport" type="button" onclick="logStatExport();"></ww:button>
			    </li>
			</ul>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="16%"  style="vertical-align:middle" rowspan="2">月份</th>
				<th width="20%"  style="vertical-align:middle" rowspan="2">事件类型</th>
				<th width="54%" colspan="3">数量分布</th>
				<th width="16%" style="vertical-align:middle" rowspan="2">合计</th>
			</tr>
			<tr>
				<th >一般</th>
				<th >重要</th>
				<th >严重</th>
			</tr>
		</thead>
		<tbody>
			<ww:iterator value="page.results" id="data" status="rowstatus">
				<tr>
					<ww:set name="obj" value="page.results[#rowstatus.index]"/>
					<td class="text-center" rowspan="<ww:property value="#obj[1].size()+2"/>" style="vertical-align: middle"><ww:property value="#obj[0]"/></td>
					<ww:set name="s1" value="0"/>
					<ww:set name="s2" value="0"/>
					<ww:set name="s3" value="0"/>
					<ww:iterator value="#obj[1]" id="data" status="index">
						<tr>
							<ww:set name="obj1" value="#obj[1][#index.index]"/>
							<td class="text-center"><ww:property value="#dictID.getDictType('d_eventtype',#obj1[0]).name"/></td>
							<td class="text-center"><ww:property value="#obj1[1]"/></td>
							<td class="text-center"><ww:property value="#obj1[2]"/></td>
							<td class="text-center"><ww:property value="#obj1[3]"/></td>
							<td class="text-center"><ww:property value="#obj1[1]+#obj1[2]+#obj1[3]"/></td>
							<ww:set name="s1" value="#s1+#obj1[1]"/>
							<ww:set name="s2" value="#s2+#obj1[2]"/>
							<ww:set name="s3" value="#s3+#obj1[3]"/>
						</tr>
					</ww:iterator>
					<tr>
						<td class="text-center">合计</td>
						<td class="text-center"><ww:property value="#s1"/></td>
						<td class="text-center"><ww:property value="#s2"/></td>
						<td class="text-center"><ww:property value="#s3"/></td>
						<td class="text-center"><ww:property value="#s1+#s2+#s3"/></td>
					</tr>
				</tr>
			</ww:iterator>
		</tbody>
	</table>
</form>
<script type="text/javascript">
using('datepicker');
function logStatExport() {
	var $tab = $.cssTab.focus(); 
	var beginDate = $("#statBeginDate", $tab).val();
	var endDate = $("#statEndDate", $tab).val();
	var val = "";
	if (!beginDate || !endDate) {
		val = "统计月份的起始月和终止月不能为空";
		$css.alert(val);
		return false;
	} else {
		val = "确定要导出日志吗？";
	}
	$css.confirm(val,function() {
		document.location="logStatExport.action?"
			+ "&beginDate=" + beginDate
			+ "&endDate=" + endDate;
	});
}

$.cssTab.focus().find('tbody tr').click(function (e) {
	 /**单击选中行事件*/
    var $tab = $.cssTab.focus();
    $tab.find('tbody tr').click(function() {
		$tab.find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
		var tr = $(this);
		$('#workitemId', $tab).val(tr.attr("rel"));
    });
});

function statFormSubmit(){
	var beginDate = $("#statBeginDate").val();
	var endDate = $("#statEndDate").val();
	if(beginDate == "" || endDate == ""){
		$css.alert('统计月份的起始月和终止月不能为空');
		return false;
	}else{
		$("#statForm").submit();
	}
}
</script>
