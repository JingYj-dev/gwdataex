<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal table-form" name="form1" id="form1" method="post" action="dirDict.action?table=<ww:property value='table'/>"  onsubmit="return navTabSearch(this,'dirDict<ww:property value="uuid"/>')">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<input type="hidden" name="parentId" value="<ww:property value='parentId'/>">
	
	<div class="table-header">
		<div class="table-search">
			<ul>
<%--				<li><input class="input-medium" type="text" id="dictCode" name="dictCode" placeholder="请输入字典编码" value="<ww:property value='dictCode'/>"></li>--%>
<%--				<li><input class="input-medium" type="text" id="dictName" name="dictName" placeholder="请输入字典名称" value="<ww:property value='dictName'/>"></li>--%>
				<li><input class="input-medium" type="text" id="name1" name="name" maxlength="50"  placeholder="请输入字典编码或名称" value="<ww:property value='name'/>"></li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="acl_dict/dirDict" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_dict/addDict" css="btn" caption="添加" title="添加字典" target="cssDialog" rel="getDict" href="getDict.action?parentId=%{parentId}"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="acl_dict/delDict" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delDict.action');"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="150px">操作</th>
				<th width="150px" order-field="code" class="order">字典编码</th>
				<th width="35%" order-field="name" class="order">字典名称</th>
				<th >字典描述</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
					<ww:link funcode="acl_dict/updDict" caption="修改"  title="修改字典"  target="cssDialog" rel="getDict" href="getDict.action?uuid=%{uuid}" ></ww:link>&nbsp;
					<ww:link funcode="acl_dict/delDict" caption="删除" onclick="$.cssTable.act('del','delDict.action?ids=%{uuid}')" href="javascript:;"></ww:link>&nbsp;
					<ww:link funcode="acl_dict/dirDict" caption="字典项" target="cssTab" rel="dirDict%{uuid}"  href="dirDict.action?parentId=%{uuid}" title="%{name}-字典项管理"></ww:link>
				</td>
				<td><ww:property value="code"/></td>
				<td><ww:property value="name"/></td>
				<td><ww:property value="remark"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
