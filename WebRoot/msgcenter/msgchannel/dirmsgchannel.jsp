<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="form1" id="form1" method="post" action="dirMsgChannel.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="channelNameDir" name="channelName" maxlength="50" placeholder="请输入通道名称" value="<ww:property value='channelName'/>"></li>
			    <li>通道类型：<ww:select  attributes="class='input-medium required'"   name="'channelType'" id="channelType" value="channelType" list="#dictID.getDictListQuery('msgcenter_d_channeltype')" listKey="code" listValue="name"></ww:select> </li>
			    <li>开启状态：<ww:select  attributes="class='input-small required'"   name="'openFlag'" id="openFlag" value="openFlag" list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="msgcenter_msgchannel/dirMsgChannel" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="msgcenter_msgchannel/addMsgChannel" css="btn" caption="添加" title="添加通道" target="cssTab" rel="addMsgChannel" href="getMsgChannel.action"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="msgcenter_msgchannel/delMsgChannel" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delMsgChannel.action');"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:dropdown funcode="msgcenter_msgchannel/updMsgChannelStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updMsgChannelStatus.action?openFlag=%{#model.code}')" />  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="80px">操作</th>
				<th width="50px" order-field="uuid" class="order">通道编号</th>
				<th width="50px" order-field="channelName" class="order">通道名称</th>
				<th width="80px">通道类型</th>
				<th width="100px">服务器IP</th>
				<th width="50px">服务端口</th>
				<th width="300px">服务地址</th>
				<th width="80px">访问账号</th>
				<th width="80px">口令</th>
				<th width="20px" order-field="openFlag" class="order">开启状态</th>
				<th >扩展配置</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><input type="checkbox" name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
					<ww:link funcode="msgcenter_msgchannel/updMsgChannel" caption="修改" title="修改通道" target="cssTab" rel="getDict" href="getMsgChannel.action?uuid=%{uuid}" ></ww:link>&nbsp;
					<ww:link funcode="msgcenter_msgchannel/delMsgChannel" caption="删除" onclick="$.cssTable.act('del','delMsgChannel.action?ids=%{uuid}')" href="javascript:;"></ww:link>&nbsp;
				</td>
				<td class="text-center"><ww:property value="uuid"/></td>
				<td class="text-center"><ww:property value="channelName"/></td>
				<td><ww:property value="#dictID.getDictType('msgcenter_d_channeltype', channelType).name"/></td>
				<td><ww:property value="serverIp"/></td>
				<td class="text-center"><ww:property value="port"/></td>
				<td><ww:property value="serverUrl"/></td>
				<td><ww:property value="imAccount"/></td>
				<td class="text-center"><ww:property value="imPassword"/></td>
				<td><ww:property value="#dictID.getDictType('d_openflag', openFlag).name"/></td>
				<td class="text-center iconspage"><ww:if test='confData == "" or confData == null'><span>--</span></ww:if>
				<ww:else><span class="expand"><i id="showIn<ww:property value="#data.getIndex()"/>" class="icon_arrow_down"></i></span></ww:else></td>
			</tr>
			<tr class="sub">
			<td colspan="12" ><ww:property value="confData"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
