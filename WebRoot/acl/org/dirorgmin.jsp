<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.post.service.SPostItem'" id="postItem" />
<ww:bean name="'com.hnjz.apps.base.common.OrgProvider'" id="orgID"/>
<ww:bean name="'com.hnjz.apps.base.post.service.SPostItem'" id="postID" />
<form class="form-horizontal table-form"  name="org_form" id="org_form" action="dirOrgMin.action"  onsubmit="return divSearch(this,'div_org')">
    <input type="hidden" name="parentId" id="parentId" value="<ww:property value='parentId'/>">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text" id="name1" name="name" maxlength="20" class="input-medium" placeholder="请输入组织名称或编码" value="<ww:property value='name'/>"></li>
				<li>状态：<ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
               	<li>岗位：
				<select id="postIdsea" name="postIdsea" class="input-medium">
					<option value="" >全部</option>
					<ww:if test="uuid != null && uuid != ''">
						<ww:iterator value="#orgID.queryPost(uuid)">
							<option value="<ww:property value='uuid'/>" <ww:if test="uuid == postIdsea">selected</ww:if> >
							<ww:property value="name"/>
							</option>
						</ww:iterator>
					</ww:if>
					<ww:else >
						<ww:iterator value="#postID.getPosts()">
							<option value="<ww:property value='uuid'/>" <ww:if test="uuid == postIdsea">selected</ww:if> >
							<ww:property value="name"/>
							</option>
						</ww:iterator>
					</ww:else>
				</select>
				</li>
				<li>
				  <ww:button css="btn green" caption="查询" funcode="acl_org/dirOrg" type="submit"></ww:button>
				</li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_org/addOrg" css="btn" caption="添加" title="添加组织机构" target="cssDialog" rel="addorg" href="getOrg.action?parentId=%{parentId}"></ww:link>
			</div>
			<div class="btn-group">
				 <ww:link funcode="acl_org/delOrg" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch',{callback:delOrg,url:'delOrg.action?parentId=%{parentId}'});"  href="javascript:;"></ww:link>
			</div>
			 <div class="btn-group">
			    <ww:dropdown funcode="acl_org/updOrgStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updOrgStatus.action?openStatus=%{#model.code}')" />  
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
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="190px">操作</th>
				<th  order-field="name" class="order">组织名称</th>
				<th width="80px" order-field="code" class="order">组织编码</th>
				<th width="30px" order-field="openFlag" class="order">状态</th>
				<th width="240px" >岗位</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
					 <ww:link funcode="acl_org/updOrg" caption="修改" title="修改组织机构"  target="cssDialog"  rel="editOrg"  href="getOrg.action?uuid=%{uuid}&parentId=%{parentId}" ></ww:link>&nbsp;
					 <ww:link funcode="acl_org/delOrg" caption="删除"  onclick="$.cssTable.act('del',{callback:delOrg,url:'delOrg.action?ids=%{uuid}&parentId=%{parentId}'});"  href="javascript:;"></ww:link>&nbsp;
				     <ww:link funcode="acl_org/dirOrgPost" caption="岗位维护" target="cssTab" title="%{name}—岗位维护" href="dirOrgPost.action?orgId=%{uuid}" ></ww:link>&nbsp;
					<%--  <ww:link funcode="acl_org/dirOrgUser" caption="添加用户" target="cssTab" rel="dirorguser%{uuid}" title='%{name}-添加用户' href="dirOrgUser.action?parentId=%{uuid}"  ></ww:link>&nbsp; --%>
					</td>
				<td><ww:property value="name"/></td>
				<td><ww:property value="code"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
				<ww:if test='#postItem.getOrgPostName(uuid) == ""'><td ><span>无</span></td></ww:if>
				<ww:else>
				<td ><ww:property value="#postItem.getOrgPostName(uuid)"/></td>
				</ww:else>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
function delOrg(data){
	if(data.result==0){
	  for( var i=0;i<data.info.length;i++)
	  { var delnode = rf_tree.getNodeByParam("id",data.info[i], null);
	    rf_tree.removeNode(delnode);
	  }
	   $css.tip(data.msg);
	   var $form=$('#div_org').find("#org_form");
       $form.submit();	 
	}else{
	   $css.tip(data.msg);
	}
}
</script>