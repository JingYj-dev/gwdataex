<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.service.QueryService'" id="queryService" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.util.ServiceUtil'" id="ServiceUtil" />
<form class="form-horizontal table-form" name="dataexSysTrans_form" id="dataexSysTrans_form" action="dirDataexSysTrans.action"  onsubmit="return navTabSearch(this)">
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
				<li><input type="text" id="docTitle" name="docTitle"  class="input-medium"  placeholder="请输入公文标题" value="<ww:property value='docTitle'/>"  maxlength="25"></li>
				<li>发送系统类型：<ww:select attributes="class='input-small'" name="sendSysType" id="sendSysType" value="sendSysType"  list="#dictID.getDictListQuery('dataex_sys_dirType')" listKey="code" listValue="name"></ww:select> </li>
				<li>交易状态：<ww:select attributes="class='input-small'" name="transStatus" id="transStatus" value="transStatus"  list="#dictID.getDictListQuery('dataexsys_d_transtatus')" listKey="code" listValue="name"></ww:select> </li>
			    <li>来源类型：<ww:select attributes="class='input-small'" name="sourceType" id="sourceType" value="sourceType"  list="#dictID.getDictListQuery('dataex_d_fromtype')" listKey="code" listValue="name"></ww:select> </li><br />
			    <li>接收完成时间：<input class="input-small Wdate" id="beginRecvTime" name="beginRecvTime" readonly="readonly" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endRecvTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginRecvTime)"/>'> 至 <input class="input-small Wdate" id="endRecvTime" name="endRecvTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginRecvTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endRecvTime)"/>'></li>
			    <li>发送完成时间：<input class="input-small Wdate" id="beginSendTime" name="beginSendTime" readonly="readonly" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endSendTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginSendTime)"/>'> 至 <input class="input-small Wdate" id="endSendTime" name="endSendTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginSendTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endSendTime)"/>'></li>
			    <li><ww:button css="btn green" funcode="oa_dataexsys/dirDataexSysTrans" caption="查询" type="submit"></ww:button></li>
			    <li><ww:button css="btn reset" funcode="oa_dataexsys/dirDataexSysTrans" caption="清空" type="button" onclick="resetTransForm();"></ww:button></li>
			</ul>
		</div>
<%-- 		<div class="table-btn">
		    <div class="btn-group">
		      <ww:link css="btn" caption="导入公文包" funcode="oa_dataexsys/impOfficialDocPackSys" target="cssDialog" title="导入公文包" rel="impOfficialDocPackSys" href="impOfficialDocPackSys.action"></ww:link>
		    </div>
		    <div class="btn-group">
		      <ww:link css="btn" caption="导出公文包" funcode="oa_dataexsys/exportOfficialDocPackSys" onclick="exportOfficialPackSys();"></ww:link>
		    </div>
		</div>--%>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<%-- <th width="25px" >序号</th> --%>
				<th width="150px" class="order">操作</th>
				<%-- <th width="40px" order-field="recvTime" class="order">操作</th> --%>
				<th class="order">公文标题</th>
				<th width="60px" order-field="sourceType" class="order">来源类型</th>
				<th width="85px" order-field="sendSysType" class="order">发送系统类型</th>
<!-- 				<th width="85px" order-field="clientIp" class="order">发送系统IP</th>
 -->				<th width="135px" order-field="recvStartTime" class="order">接收开始时间</th>
				<th width="135px" order-field="recvTime" class="order">接收完成时间</th>
				<th width="135px" order-field="sendTime" class="order">发送完成时间</th>
				<th width="40px" order-field="transStatus" class="order">交易状态</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index"> 
			<tr rel="<ww:property value='uuid'/>" transmitFlag="<ww:property value="transmitType"/>" ondblclick="javascript:$('#showDesc<ww:property value="#index.getIndex()"/>').click();">
				<%--<td class="text-center"><ww:property value="#index.index+1"/></td> --%>
				<td class="text-center">
					<ww:link funcode="oa_dataexsys/dirDataexSysTransLog" caption="查看日志" title="查看日志" target="cssDialog" rel="dirDataexSysTransLog" href="dirDataexSysTransLog.action?transId=%{uuid}" ></ww:link>&nbsp;
					<ww:link funcode="oa_dataexsys/getTransContent" caption="查看详情" title="查看详情" target="cssDialog" href="getTransContent.action?transId=%{uuid}"></ww:link>
				</td>
			    <td class="text-left"><ww:property value="#queryService.getContentByTransId(uuid).docTitle"/></td>
			    <td class="text-center"><ww:property value="#dictID.getDictType('dataex_d_fromtype', sourceType).name"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_dirType', sendSysType).name" /></td>
<%-- 			    <td class="text-center"><ww:property value="clientIp"/></td>
 --%>				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(recvStartTime)"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(recvTime)"/></td>
				<td class="text-center">
					<ww:if test='sendTime == null'>--</ww:if>
		    		<ww:else><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(sendTime)" /></ww:else>&nbsp;
				</td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_transtatus', transStatus).name"/></td>
			</tr>
		  </ww:iterator>
		</tbody>
	</table>
	<div class="pagination"><ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
var exTitleMap = {
	transmitBatch:'选择收文部门',
	transmittedOfficialDoc:'此公文包已转收文！',
	alertBatch:'请选中一条数据！'
};
function getOrgTree(url){
	//debugger;
    var $tab = $.cssTab.focus();
    var checkedId = $('#checkedId',$tab)[0].value;
    var transmitFlag = $('#transmitFlag',$tab)[0].value;
    if(checkedId.length == 0){
       $.dialog.alert(exTitleMap.alertBatch);
       return false;
    }
    if (transmitFlag=='1') {
		$.dialog.alert(exTitleMap.transmittedOfficialDoc);
		return false;
    }
    if(url.indexOf('?')>0)
		url+="&ids="+checkedId+"&transmitFlag="+transmitFlag;
	else
		url+="?ids="+checkedId+"&transmitFlag="+transmitFlag;
    $css.openDialog({
		title: exTitleMap.transmitBatch,
		url: url,
		lock: true
	});
}

//单击选中单条记录并为隐藏变量赋值
jQuery(function($) {
	/**添加绑定事件:单击选中行*/
    var $tab = $.cssTab.focus();
    $tab.find('tbody tr').click(function() {
		$tab.find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
		var tr = $(this);
		$('#checkedId', $tab).val(tr.attr("rel"));
		$('#transmitFlag', $tab).val(tr.attr("transmitFlag"));
    });
});

function exportOfficialPackSys() {
	var $tab = $.cssTab.focus();
    var checkedId = $('#checkedId',$tab)[0].value;
    if(checkedId.length == 0){
       $.dialog.alert(exTitleMap.alertBatch);
       return false;
    }
    var url = "exportOfficialDocPackSys.action?ids=" + checkedId;
	document.location = url;
}
function resetTransForm(){
	var $tab = $.cssTab.focus();
	$tab.find(':input','#dataexSysTrans_form').not(':button, :submit,:radio, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
	$tab.find('input[type=radio][value=or]').attr('checked','checked');
}
using('upload');
</script>