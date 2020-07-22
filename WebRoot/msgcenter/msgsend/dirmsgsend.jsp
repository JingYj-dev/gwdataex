<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.msgcenter.spi.service.MsgHelper'" id="msgHelper" />
<form class="form-horizontal table-form" name="form1" id="form1" method="post" action="dirMsgSend.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li>应用系统：<ww:select  attributes="class='input-small required'"   name="'appId'" id="appId" value="appId" list="#msgHelper.getAppListQuery()" listKey="appId" listValue="appName"></ww:select> </li>
				<li>消息通道：<ww:select  attributes="class='input-small required'"   name="'channelName'" id="channelName" value="channelName" list="#msgHelper.getChannelListQuery()" listKey="uuid" listValue="channelName"></ww:select> </li>
			    <li>消息状态：<ww:select  attributes="class='input-small required'"   name="'msgStatus'" id="msgStatus" value="msgStatus" list="#dictID.getDictListQuery('msgcenter_d_msgstatus')" listKey="code" listValue="name"></ww:select> </li>
			    <li>接收时间：<input id="d1" class="input-medium Wdate" id="beginTime" name="beginTime" readonly="true" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'d2\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginTime)"/>'> 至 <input id="d2" class="input-medium Wdate" id="endTime" name="endTime" readonly="true" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'d1\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endTime)"/>'></li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="msgcenter_msgspi/dirMsgSend" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px">消息ID</th>
				<th width="150px">发送系统</th>
				<th width="150px">接收方</th>
				<th width="150px">发送通道</th>
				<th width="100px" order-field=msgStatus class="order">状态</th>
				<th width="150px">接收时间</th>
				<th width="150px">发送时间</th>
				<th width="50px" order-field=sendTimes class="order">发送次数</th>
				<th >内容</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><ww:property value="#data.index+1"/></td>
				<td class="text-center"><ww:property value="appId"/></td>
				<td class="text-center"><ww:property value="targetId"/></td>
				<td><ww:property value="channelName"/></td>
				<td><ww:property value="#dictID.getDictType('msgcenter_d_msgstatus', msgStatus).name"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(receiveTime)"/></td>
				<td><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(finishTime)"/></td>
				<td><ww:property value="sendTimes"/></td>
				<td class="text-center iconspage"><ww:if test='msgContent == "" or msgContent == null'><span>--</span></ww:if>
				<ww:else><span class="expand"><i id="showIn<ww:property value="#data.getIndex()"/>" class="icon_arrow_down"></i></span></ww:else></td>
			</tr>
			<tr class="sub">
			<td colspan="9" ><ww:property value="msgContent"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
