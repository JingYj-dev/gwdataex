<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.log.service.TjNumberFormat'" id="tjNumberFormat" />
<style type="text/css">
<!--
.schdown {
background-image: url(cssui/themes/metro/images/new_icon.png);
background-repeat: no-repeat;
background-position: 0px -73px;
display: inline-block;
width: 18px;
height: 18px;
}
-->
</style>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal  " name="fxForm" id="fxForm" action="logFx.action"  onsubmit="return navTabSearch(this)">
	<div class="table-header">
		<div class="table-search">
			<ul>
			    <li>统计年度：<input class="input-small Wdate" id="fxDate" name="fxDate" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" value='<ww:property value="fxDate"/>'> </li>
			    <li>事件范围：
			    	<input class="input-xxlarge" title="" type="text" name="eventTypes" id="eventTypes" value="<ww:property value="eventTypes"/>" onclick="getBriefcaseList()">
			    	<input type="hidden" name="eventCodes" id="eventCodes" value="<ww:property value="eventCodes"/>"/>
			    	<a href="acl/log/eventtypes.jsp" target="cssDialog" rel="EventTypes" title="事件范围" class="schdown" style=""></a>
			    </li>
			    <li>
			      <ww:button css="btn green" caption="查询" funcode="" onclick="fxFormSubmit()"></ww:button>
			    </li>
			    <li>
			     <ww:button css="btn green" caption="导出" funcode="" type="button" onclick="logFxExport();"></ww:button>
			    </li>
			</ul>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="10%">事件类型</th>
				<th width="6%">一月</th>
				<th width="6%">二月</th>
				<th width="6%">三月</th>
				<th width="6%">四月</th>
				<th width="6%">五月</th>
				<th width="6%">六月</th>
				<th width="6%">七月</th>
				<th width="6%">八月</th>
				<th width="6%">九月</th>
				<th width="6%">十月</th>
				<th width="6%">十一月</th>
				<th width="6%">十二月</th>
				<th width="6%">平均</th>
				<th width="6%">合计</th>
			</tr>
		</thead>
		<tbody>
			<ww:set name="sum1" value="0"/>
			<ww:set name="sum2" value="0"/>
			<ww:set name="sum3" value="0"/>
			<ww:set name="sum4" value="0"/>
			<ww:set name="sum5" value="0"/>
			<ww:set name="sum6" value="0"/>
			<ww:set name="sum7" value="0"/>
			<ww:set name="sum8" value="0"/>
			<ww:set name="sum9" value="0"/>
			<ww:set name="sum10" value="0"/>
			<ww:set name="sum11" value="0"/>
			<ww:set name="sum12" value="0"/>
			<ww:set name="sum13" value="0"/>
			<ww:iterator value="tjList" status="index">
				<ww:set name="obj" value="tjList[#index.index]"/>
				<tr>
					<td class="text-center"><ww:property value="#dictID.getDictType('d_eventtype',#obj[0]).name"/></td>
					<td class="text-center"><ww:property value="#obj[1]"/></td>
					<ww:set name="sum1" value="#sum1+#obj[1]"/>
					<td class="text-center"><ww:property value="#obj[2]"/></td>
					<ww:set name="sum2" value="#sum2+#obj[2]"/>
					<td class="text-center"><ww:property value="#obj[3]"/></td>
					<ww:set name="sum3" value="#sum3+#obj[3]"/>
					<td class="text-center"><ww:property value="#obj[4]"/></td>
					<ww:set name="sum4" value="#sum4+#obj[4]"/>
					<td class="text-center"><ww:property value="#obj[5]"/></td>
					<ww:set name="sum5" value="#sum5+#obj[5]"/>
					<td class="text-center"><ww:property value="#obj[6]"/></td>
					<ww:set name="sum6" value="#sum6+#obj[6]"/>
					<td class="text-center"><ww:property value="#obj[7]"/></td>
					<ww:set name="sum7" value="#sum7+#obj[7]"/>
					<td class="text-center"><ww:property value="#obj[8]"/></td>
					<ww:set name="sum8" value="#sum8+#obj[8]"/>
					<td class="text-center"><ww:property value="#obj[9]"/></td>
					<ww:set name="sum9" value="#sum9+#obj[9]"/>
					<td class="text-center"><ww:property value="#obj[10]"/></td>
					<ww:set name="sum10" value="#sum10+#obj[10]"/>
					<td class="text-center"><ww:property value="#obj[11]"/></td>
					<ww:set name="sum11" value="#sum11+#obj[11]"/>
					<td class="text-center"><ww:property value="#obj[12]"/></td>
					<ww:set name="sum12" value="#sum12+#obj[12]"/>
					<td class="text-center"><ww:property value="#tjNumberFormat.format(#obj[13])"/></td>
					<td class="text-center"><ww:property value="#obj[14]"/></td>
					<ww:set name="sum13" value="#sum13+#obj[14]"/>
				</tr>
			</ww:iterator>
			<tr>
				<td class="text-center">合计</td>
				<td class="text-center"><ww:property value="#sum1"/></td>
				<td class="text-center"><ww:property value="#sum2"/></td>
				<td class="text-center"><ww:property value="#sum3"/></td>
				<td class="text-center"><ww:property value="#sum4"/></td>
				<td class="text-center"><ww:property value="#sum5"/></td>
				<td class="text-center"><ww:property value="#sum6"/></td>
				<td class="text-center"><ww:property value="#sum7"/></td>
				<td class="text-center"><ww:property value="#sum8"/></td>
				<td class="text-center"><ww:property value="#sum9"/></td>
				<td class="text-center"><ww:property value="#sum10"/></td>
				<td class="text-center"><ww:property value="#sum11"/></td>
				<td class="text-center"><ww:property value="#sum12"/></td>
				<td class="text-center"><ww:property value="#tjNumberFormat.format(#sum13*1.0/12)"/></td>
				<td class="text-center"><ww:property value="#sum13"/></td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
using('datepicker');
function getBriefcaseList() {
	$css.openDialog({
		title:"选择公文",
		url:"acl/log/eventtypes.jsp",
		lock:true
	});
}

function logFxExport() {
	var $tab = $.cssTab.focus(); 
	var fxDate = $("#fxDate", $tab).val();
	var eventCodes = $("#eventCodes", $tab).val();
	var val = "";
	if (!fxDate) {
		val = "请输入统计年份";
		$css.alert(val);
		return false;
	} else if(!eventCodes){
		val = "请输入事件范围";
		$css.alert(val);
		return false;
	}else {
		val = "确定要导出吗？";
	}
	$css.confirm(val,function() {
		document.location="logFxExport.action?"
			+ "&fxDate=" + fxDate
			+ "&eventCodes=" + eventCodes;
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


function fxFormSubmit(){
	var fxDate = $("#fxDate").val();
	var eventTypes = $("#eventTypes").val();
	if(fxDate == ""){
		$css.alert('统计年份不能为空');
		return false;
	}
	if(eventTypes == ""){
		$css.alert('事件范围不能为空');
		return false;
	}
	$("#fxForm").submit();
}
</script>
