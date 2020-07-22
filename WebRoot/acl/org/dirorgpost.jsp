<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.org.action.OrgItem'" id="oegPostID" />
<form class="form-horizontal table-form"  name="org_form" id="org_form" action="dirOrgPost.action?"  onsubmit="return navTabSearch(this)">
	<input type="hidden" name="orgId" id="orgId" value="<ww:property value='orgId'/>">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
	<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
			<div class="table-search">
			<ul>
				<li><input type="text" id="name" name="name" class="input-medium" maxlength="20" placeholder="请输入岗位名称" value="<ww:property value='name'/>"></li>
				<li>
				  <ww:button css="btn green" caption="查询" funcode="acl_org/dirOrgPost" type="submit"></ww:button>
				</li>
			</ul>
			</div>
		<div class="table-btn">
			<div class="btn-group">
			  <ww:link funcode="acl_org/addOrgPost" target="cssDialog" title="添加岗位" css="btn" caption="添加" href="getOrgPost.action?orgId=%{orgId}"></ww:link>	
			</div>
			<div class="btn-group">
			  <ww:link funcode="acl_org/delOrgPost"  title="删除岗位" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delOrgPost.action?orgId=%{orgId}');" href="javascript:;" ></ww:link>	
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
			    <th width="25px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
			    <th width="40px">操作</th>
				<th width="240px" >岗位名称</th>
				<th >岗位描述</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="PostList" id="data" > 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid"/>"/></td>
				<td class="text-center">
			    	<ww:link funcode="acl_org/delOrgPost" caption="删除" onclick="$.cssTable.act('del','delOrgPost.action?ids=%{uuid} & orgId=%{orgId}');" href="javascript:;"></ww:link>
				</td>
				<td><ww:property value="name"/></td>
				<td><ww:property value="remark"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<%--<div class="pagination"> <ww:property value="page.pageSplit"/></div>
--%></form>