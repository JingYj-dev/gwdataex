<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="post_form"  id="post_form" action="dirPost.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="postName" name="postName" maxlength="20" placeholder="请输入岗位名称" value="<ww:property value='postName'/>"></li>
			    <li>
			    <ww:button css="btn green" caption="查询" funcode="acl_post/dirPost" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
			   <ww:link funcode="acl_post/addPost"  rel="addpost" target="cssDialog" css="btn" caption="添加" title="添加岗位" href="getPost.action"></ww:link>	
			</div>
			<div class="btn-group">
			   <ww:link funcode="acl_post/delPost" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch','delPost.action');"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="130px">操作</th>
				<th width="200px" order-field="name" class="order">岗位名称</th>
				<th>岗位描述</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data"> 
			<tr  rel="<ww:property value='uuid' />">
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
				    <ww:link funcode="acl_post/updPost" caption="修改" title="修改岗位" target="cssDialog" rel="getPost" href="getPost.action?uuid=%{uuid}" ></ww:link>&nbsp;
				    <ww:link funcode="acl_post/delPost" caption="删除" onclick="$.cssTable.act('del','delPost.action?ids=%{uuid}')" href="javascript:;"></ww:link>&nbsp;
					<ww:link funcode="acl_post/dirPostRole" caption="授权" href="dirPostRole.action?postId=%{uuid}" target="cssTab" 
						style="display:inline-block;" rel="dirpostrrole%{uuid}"  title="机构%{name}-授权">授权</ww:link>
				</td>
				<td ><ww:property value="name"/></td>
				<td ><ww:property value="remark"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>