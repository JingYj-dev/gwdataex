<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.user.model.SUser'" id="dictPost" />
<ww:bean name="'com.hnjz.apps.base.common.OrgProvider'" id="orgID"/>
<ww:bean name="'com.hnjz.apps.base.post.service.SPostItem'" id="postID" />
<form class="form-horizontal table-form"  name="user_form" id="user_form" action="dirUserMini.action"  onsubmit="return divSearch(this,'div_user')">
    <input type="hidden" name="orgId" id="orgId" value="<ww:property value='orgId'/>">
	<input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text"id="username" name="username"class="input-medium" maxlength="20" placeholder="请输入登录名或姓名" value="<ww:property value='username'/>"></li>
				<li>状态：<ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictListQuery('d_security_Flag')" listKey="code" listValue="name"></ww:select> </li>
                <li>激活状态：<ww:select attributes="class='input-small'" name="'activeStatus'" id="activeStatus" value="activeStatus"  list="#dictID.getDictListQuery('d_active_status')" listKey="code" listValue="name"></ww:select> </li>
               	<li>岗位：
				<select id="postIdsearch" name="postIdsearch" class="input-medium">
					<option value="" >全部</option>
					<ww:if test="orgId != null && orgId != ''">
						<ww:iterator value="#orgID.queryPost(orgId)">
							<option value="<ww:property value='uuid'/>" <ww:if test="uuid == postIdsearch">selected</ww:if> >
							<ww:property value="name"/>
							</option>
						</ww:iterator>
					</ww:if>
					<ww:else >
						<ww:iterator value="#postID.getPosts()">
							<option value="<ww:property value='uuid'/>" <ww:if test="uuid == postIdsearch">selected</ww:if> >
							<ww:property value="name"/>
							</option>
						</ww:iterator>
					</ww:else>
				</select>
			</li>
                <li><ww:button css="btn green" caption="查询" funcode="acl_user/dirUserMini" type="submit"></ww:button></li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_user/addUser" css="btn" caption="添加" title="添加用户" target="cssTab" rel="adduser" href="getUser.action?orgId=%{orgId}"></ww:link>
			</div>
			<div class="btn-group btnTab" style="float:right;">
				<input type="hidden" name="includeFlag" id="includeFlag" value="<ww:property value='includeFlag'/>">
				<a class="btn" value="1" href="javascript:;">当前组织</a>
				<a class="btn" value="2" href="javascript:;">包含下级</a>
			</div> 
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
<%--				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>--%>
				<th width="200px">操作</th>
				<th  width="100px" order-field="loginName" class="order">登录名</th>
				<th  width="80px" order-field="realName" class="order">姓名</th>
				<th width="60px" order-field="activeStatus" class="order">激活状态</th>
				<th width="25px" order-field="sex" class="order">性别</th>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<th width="30px" order-field="userType" class="order">类型</th>
				</ww:if>
				<th width="25px" order-field="openFlag" class="order">状态</th>
				<th  width="35px" order-field="secLevel" class="order">安全级别</th>
				<th  >所属机构</th>
				<th >岗位</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data" status="index"> 
				<tr ondblclick="javascript:$('#showLogData<ww:property value="#index.getIndex()"/>2').click();">
<%--					<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>--%>
					<td class="text-center">
						<ww:link funcode="acl_user/updUser" rel="updUser%{uuid}" caption="修改" target="cssTab" href="getUser.action?uuid=%{uuid}"
							 style="display:inline-block;" title="用户%{realName}-修改"></ww:link>
						<ww:if test="activeStatus == 1">
							<ww:link funcode="acl_user/delUser" caption="删除" onclick="$.cssTable.act('del',{url:'delUser.action?ids=%{uuid}',title:'将删除岗位以及角色关联，确认删除?'})" 
								 style="display:inline-block;" href="javascript:;"></ww:link>
						</ww:if>
						<ww:else>
						    <ww:link funcode="acl_user/delUser" caption="删除" onclick="delUserConfirm()" 
							style="display:inline-block;" href="javascript:;"></ww:link>
						</ww:else>
					</td>
					<td ><ww:property value="loginName"/></td>
					<td ><ww:property value="realName"/></td>
					<td class="text-center"><ww:property value="#dictID.getDictType('d_active_status',activeStatus).name" /></td>
					<td class="text-center"><ww:property value="#dictID.getDictType('d_sex',sex).name" /></td>
					<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
						<td class="text-center"><ww:property value="#dictID.getDictType('d_usertype',userType).name" /></td>
					</ww:if>
					<td class="text-center"><ww:property value="#dictID.getDictType('d_security_Flag',openFlag).name" /></td>
					 <td class="text-center"><ww:property value="#dictID.getDictType('fw_d_security_level',secLevel).name" /></td>
					<td class="text-center"><ww:property value="orgName" /></td>
					<ww:if test='#dictPost.getUserPostName(uuid) == ""'><td ><span>无</span></td></ww:if>
					<ww:else>
					<td ><ww:property value="#dictPost.getUserPostName(uuid)"/></td>
					</ww:else>
				</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
function delUserConfirm(){
	 $css.alert("用户已激活或待激活，无法删除！");
}
</script>