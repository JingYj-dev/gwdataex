<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem'" id="dirItem" />
<form class="form-horizontal table-form" name="dirDataexQueue_form" id="dirDataexQueue_form" method="post" action="dirDataexSysTransQueue.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="docTitle" name="docTitle" placeholder="请输入公文标题" maxlength="70" value="<ww:property value='docTitle'/>"></li>
				<li><input class="input-medium" type="text" id="sendOrg" name="sendOrg" placeholder="请输入发送机关" maxlength="70" value="<ww:property value='sendOrg'/>"></li>
				<li><input class="input-medium" type="text" id="recvOrg" name="recvOrg" placeholder="请输入接收机关" maxlength="70" value="<ww:property value='recvOrg'/>"></li>
				<li>提交时间：<input class="input-small Wdate" id="beginStartTime" name="beginStartTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endStartTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginStartTime)"/>'> 至 <input class="input-small Wdate" id="endStartTime" name="endStartTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginStartTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endStartTime)"/>'></li>
				<%--<li>上次更新时间：<input class="input-small Wdate" id="beginUpdateTime" name="beginUpdateTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endUpdateTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginUpdateTime)"/>'> 至 <input class="input-small Wdate" id="endUpdateTime" name="endUpdateTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginUpdateTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endUpdateTime)"/>'></li> --%>
				<li>发送状态：<ww:select attributes="class='input-small'" name="sendStatus" id="sendStatus" value="sendStatus"  list="#dictID.getDictListQuery('dataex_d_sendstate')" listKey="code" listValue="name"></ww:select> </li>
			    <li><ww:button css="btn green" caption="查询" funcode="oa_dataexsys/dirDataexSysTransQueue" type="submit"></ww:button></li>
			    <li><ww:button css="btn reset" funcode="oa_dataexsys/dirDataexSysTransQueue" caption="清空" type="button" onclick="resetQueueForm();"></ww:button></li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="oa_dataexsys/delDataexSysTransQueue" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delDataexSysTransQueue.action');"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="oa_dataexsys/startDataexSysTransQueue" css="btn" caption="启动" onclick="$.cssTable.act('batch',{url:'startDataexSysTransQueue.action',title:'你确定要启动这些任务吗?'});"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="80px">操作</th>
				<th order-field="title">公文标题</th>
				<th width="180px" order-field="sendOrg">发送机关</th>
				<th order-field="recvOrg">接收机关</th>
				<th  order-field="serverName">执行节点</th>
				<th width="140px" order-field="startTime">开始时间</th>
				<th width="140px" order-field="updateTime">上次更新时间</th>
				<th width="40px" order-field="sendStatus">发送状态</th>
				<th width="50px" order-field="sendTimes">已发送次数</th>
				<th width="30px">备注</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index">
			<tr ondblclick="javascript:$('#showM<ww:property value="#index.getIndex()"/>').click();">
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="sendId" />"/></td>
				<td class="text-center">
					<ww:if test='sendStatus == sendStatusFail && sendTimes >= maxSendTimes'><ww:link funcode="oa_dataexsys/startDataexSysTransQueue" caption="启动" onclick="$.cssTable.act('del',{url:'startDataexSysTransQueue.action?ids=%{sendId}',title:'你确定要启动此任务吗?'});" href="javascript:;"></ww:link></ww:if>
					<ww:else><span style="visibility:hidden;">启动</span></ww:else>&nbsp;
					<ww:link caption="删除" funcode="oa_dataexsys/delDataexSysTransQueue" onclick="$.cssTable.act('del','delDataexSysTransQueue.action?ids=%{sendId}')"  href="javascript:void(0);"></ww:link>
				</td>
				<td class="text-left"><ww:property value="title"/></td>
				<td class="text-left"><ww:property value="#dirItem.getDirByDirOrg(sendOrg).dirName"/></td>
				<td class="text-left"><ww:property value="#dirItem.getDirByDirOrg(recvOrg).dirName"/></td>
				<td class="text-left"><ww:property value="serverName"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(startTime)"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(updateTime)"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_d_sendstate', sendStatus).name"/></td>
				<td class="text-center"><ww:property value="sendTimes"/></td>
				<td class="text-center iconspage"><ww:if test='memo == "" or memo == null'><span>--</span></ww:if>
					<ww:else><span class="expand"><i id="showM<ww:property value="#index.getIndex()"/>" class="icon_arrow_down"></i></span></ww:else></td>
				</tr>
				<tr class="sub">
				<td colspan="11" ><ww:property value="memo"/></td>
				</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script>
//单击选中单条记录并为隐藏变量赋值
jQuery(function($) {
	/**添加绑定事件:单击选中行*/
    var $tab = $.cssTab.focus();
    $tab.find('tbody tr').click(function() {
		$tab.find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
		var tr = $(this);
    });
});

function resetQueueForm(){
	var $tab = $.cssTab.focus();
	$tab.find(':input','#dirDataexQueue_form').not(':button, :submit,:radio, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
	$tab.find('input[type=radio][value=or]').attr('checked','checked');
}
</script>
