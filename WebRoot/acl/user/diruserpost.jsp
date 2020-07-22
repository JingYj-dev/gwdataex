<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal table-form"  name="user_form" id="user_form" action="dirUserPost.action?"  onsubmit="return navTabSearch(this)">
	<input type="hidden" name="userId" id="userId" value="<ww:property value='userId'/>">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
	<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-btn">
			<div class="btn-group">
			  <ww:link funcode="acl_user/addUserPost" target="cssDialog" title="添加岗位" css="btn" caption="添加" href="getUserPost.action?userId=%{userId}"></ww:link>	
			</div>
			<div class="btn-group">
			  <ww:link funcode="acl_user/delUserPost"  title="删除岗位" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delUserPost.action?userId=%{userId}');" href="javascript:;" ></ww:link>	
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
			    <th width="25px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
			    <th width="40px">操作</th>
				<th width="30%" class="order">岗位名称</th>
				<th class="order">岗位描述</th>
			</tr>
		</thead>
		<tbody>
		     <ww:iterator value="PostList" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid"/>"/></td>
				<td class="text-center">
			    	<ww:link funcode="acl_user/delUserPost" caption="删除" onclick="$.cssTable.act('del','delUserPost.action?ids=%{uuid} & userId=%{userId}');" href="javascript:;"></ww:link>
				</td>
				<td><ww:property value="name"/></td>
				<td><ww:property value="remark"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
</form>