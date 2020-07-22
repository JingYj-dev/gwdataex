<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="sys_form"  id="sys_form" action="dirSys.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	
	<div class="table-header">
		<div class="table-search">
			<ul>
			<li>
			<input class="input-medium" type="text" id="name1" name="name" placeholder="请输入系统编码或名称" value="<ww:property value='name'/>"  maxlength="10" > 
			</li>
				<li>状态：<ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			       <ww:button css="btn green" caption="查询" funcode="acl_sys/dirSys" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_sys/addSys"  target="cssDialog" css="btn" caption="添加" title="添加系统" rel="addsys" href="getSys.action?parentId=%{parentId}"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:link funcode="acl_sys/delSys" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch','delSys.action');"></ww:link>
			</div>
			 <div class="btn-group">
			    <ww:dropdown funcode="acl_sys/updSys" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updSysStatus.action?openStatus=%{#model.code}')" />  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="80px">操作</th>
				<th width="20%" order-field="uuid" class="order">系统编码</th>
				<th width="20%" order-field="name" class="order">系统名称</th>
				<th>系统地址</th>
				<th>系统描述</th>
				<th width="30px" order-field="openFlag" class="order">状态</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data"> 
			<tr  rel="<ww:property value='uuid' />">
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
				    <ww:link funcode="acl_sys/updSys" caption="修改"  title="修改系统" target="cssDialog"  rel="getSys" href="getSys.action?uuid=%{uuid}" ></ww:link>&nbsp;
				    <ww:link funcode="acl_sys/delSys" caption="删除" onclick="$.cssTable.act('del','delSys.action?ids=%{uuid}')" href="javascript:;"></ww:link>&nbsp;
				</td>
				<td ><ww:property value="sysId"/></td>
				<td ><ww:property value="name"/></td>
				<td >
				<ww:if test="url!=null&&url.length()>20">
							<a target="cssTab" css="btn" href="%{url}"><ww:property value='" "+url.substring(0,20)+"..."'/> </a>
				</ww:if>
				<ww:else>
						<a target="cssTab" css="btn" href="%{url}"><ww:property value="url"/></a>
				</ww:else>
				</td>
				<td ><ww:property value="remark"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>