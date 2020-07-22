<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.sys.action.SysItem'" id="sysItemID" />
<form class="form-horizontal table-form"  name="role_form"  id="role_form" action="dirRole.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text" id="roleName" name="roleName"  class="input-medium" maxlength="20"  placeholder="请输入角色名称" value="<ww:property value='roleName'/>"></li>
				<li>所属系统：<ww:select attributes="class='input-medium'" id="sysId" name="sysId" value="sysId" list="#sysItemID.getOpenSystems()" listKey="uuid" listValue="name" hint="全部" />
				</li>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
				<li>角色类型：<ww:select attributes="class='input-medium'" id="roleType" name="roleType" value="roleType" list="#dictID.getDictListQuery('d_roletype')" listKey="code" listValue="name" />
				</li>
				</ww:if>
				<li>状态：<ww:select  attributes="class='input-small'" name="'openFlag'" id="openFlag"  value="openFlag" list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
				<li>
				 <ww:button css="btn green" caption="查询" funcode="acl_role/dirRole" type="submit"></ww:button>
				</li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_role/addRole"  target="cssDialog" css="btn" caption="添加" title="添加角色" rel="addRole" href="getRole.action?parentId=%{parentId}"></ww:link>
			</div>
            <div class="btn-group">
				 <ww:link funcode="acl_role/delRole" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch','delRole.action');"></ww:link>
            </div>
		    <div class="btn-group">
			    <ww:dropdown funcode="acl_role/updRoleStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updRoleStatus.action?openStatus=%{#model.code}')" />  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="140px">操作</th>
				<th width="180px" order-field="name" class="order">角色名称</th>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<th width="100px" order-field="roleType" class="order">角色类型</th>
				</ww:if>
				<th width="160px" order-field="sysId" class="order">所属系统</th>
				<th >角色描述</th>
				<th width="30px" order-field="openFlag" class="order">状态</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data"> 
			<tr rel="<ww:property value='uuid' />">
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value='uuid' />"/></td>
				<td class="text-center">
					 <ww:link funcode="acl_role/updRole" caption="修改" title="修改角色" target="cssDialog"  rel="getRole" href="getRole.action?uuid=%{uuid}" ></ww:link>&nbsp;
					 <ww:link funcode="acl_role/delRole" caption="删除" onclick="$.cssTable.act('del',{url:'delRole.action?ids=%{uuid}',title:'将删除岗位以及角色关联，确认删除?'})"   href="javascript:;"></ww:link>&nbsp;
					 <ww:link funcode="acl_role/addRoleFunc"  target="cssDialog" rel="getRoleFuncTree" caption="分配功能" title="分配功能" href="getRoleFuncTree.action?roleId=%{uuid}" ></ww:link>&nbsp;
			    </td>
				<td ><ww:property value="name"/></td>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<td class="text-center"><ww:property value="#dictID.getDictType('d_roletype',roleType).name" /></td>
				</ww:if>
				<td ><ww:property value="#sysItemID.getSystem(sysId).name" /></td>
				<td ><ww:property value="remark"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>