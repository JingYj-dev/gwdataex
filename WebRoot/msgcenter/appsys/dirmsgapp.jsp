<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="form1" id="form1" method="post" action="dirMsgApp.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="appCode" name="appCode" maxlength="10" placeholder="请输入应用编码" value="<ww:property value='appCode'/>"></li>
				<li><input class="input-medium" type="text" id="appName" name="appName" maxlength="50" placeholder="请输入应用名称" value="<ww:property value='appName'/>"></li>
			    <li>开启状态：<ww:select  attributes="class='input-small required'"   name="'openFlag'" id="openFlag" value="openFlag" list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="msgcenter_msgapp/dirMsgApp" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="msgcenter_msgapp/addMsgApp" css="btn" caption="添加" title="添加应用" target="cssTab" rel="addMsgApp" href="getMsgApp.action"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="msgcenter_msgapp/delMsgApp" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delMsgApp.action');"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:dropdown funcode="msgcenter_msgapp/updMsgAppStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updMsgAppStatus.action?openFlag=%{#model.code}')" />  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="80px">操作</th>
				<th width="150px" order-field="appCode" class="order">应用编码</th>
				<th width="150px">应用名称</th>
				<th width="150px">绑定IP</th>
				<th width="50px">开启状态</th>
				<th width="150px">授权码</th>
				<th width="150px" order-field="createdTime" class="order">创建时间</th>
				<th >模块描述</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="appId" />"/></td>
				<td class="text-center">
					<ww:link funcode="msgcenter_msgapp/updMsgApp" caption="修改" title="修改应用" target="cssTab" rel="getDict" href="getMsgApp.action?appId=%{appId}" ></ww:link>&nbsp;
					<ww:link funcode="msgcenter_msgapp/delMsgApp" caption="删除" onclick="$.cssTable.act('del','delMsgApp.action?ids=%{appId}')" href="javascript:;"></ww:link>&nbsp;
				</td>
				<td class="text-center"><ww:property value="appCode"/></td>
				<td><ww:property value="appName"/></td>
				<td><ww:property value="appSysIp"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag', openFlag).name"/></td>
				<td><ww:property value="licenseCode"/></td>
				<td><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(createdTime)"/></td>
				<td class="text-center iconspage"><ww:if test='memo == "" or memo == null'><span>--</span></ww:if>
				<ww:else><span class="expand"><i id="showIn<ww:property value="#data.getIndex()"/>" class="icon_arrow_down"></i></span></ww:else></td>
			</tr>
			<tr class="sub">
			<td colspan="9" ><ww:property value="memo"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
