<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="post_form" id="post_form" action="dirPostRole.action"  onsubmit="return navTabSearch(this)">
	<input type="hidden" name="postId" id="postId" value="<ww:property value='postId'/>">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
	<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-btn">
			<div class="btn-group">
			  <ww:link funcode="acl_post/addPostRole" target="cssDialog" title="添加授权" css="btn" caption="添加" href="getPostRole.action?postId=%{postId}  & Flag=%{Flag}"></ww:link>	
			</div>
			<div class="btn-group">
			  <ww:link funcode="acl_post/delPostRole"  title="删除授权" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delPostRole.action?postId=%{postId}');" href="javascript:;" ></ww:link>	
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
			    <th width="25px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
			    <th width="40px">操作</th>
				<th  >角色名称</th>
				<th  >角色描述</th>
				<th width="40px" >状态</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="RoleList" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid"/>"/></td>
				<td class="text-center">
			    	<ww:link funcode="acl_post/delPostRole" caption="删除" onclick="$.cssTable.act('del','delPostRole.action?ids=%{uuid} & postId=%{postId}');" href="javascript:;"></ww:link>
				</td>
				<td><ww:property value="name"/></td>
				<td><ww:property value="remark"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
</form>