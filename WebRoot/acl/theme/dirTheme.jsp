<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:plugin name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="theme_form"  id="theme_form" action="dirTheme.action"  onsubmit="return navTabSearch(this)">

    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="code" name="code" placeholder="请输入编码" value="<ww:property value='item.code'/>"></li>
				<li><input class="input-medium" type="text" id="name" name="name" placeholder="主题名称"  value="<ww:property value='item.name'/>"></li>
				<li><ww:select attributes="class='input-medium'" name="'typeId'" id="typeId" value="<ww:property value='item.typeId'/>"  list="#dictID.getDictList('theme_type','主题类别')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
				<li><ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictList('d_openflag','状态')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			       <ww:button css="btn green" caption="查询" funcode="acl_theme/dirTheme" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group"> 
				<ww:link funcode="acl_theme/addTheme"  target="cssDialog" css="btn" caption="添加" title="添加主题" href="getTheme.action"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:link funcode="acl_theme/delTheme" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch',{url:'delTheme.action'});"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:dropdown funcode="acl_theme/updThemeOpenStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updThemeOpenStatus.action?openFlag=%{#model.code}')" />  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th style="width: 25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th style="width: 40px">操作</th>
				<th order-field="code" style="width:100px" class="order">主题编码</th>
				<th style="width:90px;">主题名称</th>
				<th style="width: 90px" >主题效果图</th>
				<th style="width: 60px" order-field="typeId" class="order">所属分类</th>
				<th style="width: 40px" order-field="openFlag" class="order">开启状态</th>
				<th>备注</th>			
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index"> 
			<tr  rel="<ww:property value='uuid' />">
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center" nowrap="nowrap">
			    	<ww:link funcode="acl_theme/updTheme" caption="修改" title="修改主题" rel="updTheme" target="cssDialog" href="getTheme.action?uuid=%{uuid}" ></ww:link>&nbsp;&nbsp;
				    <ww:link funcode="acl_theme/delTheme" caption="删除" onclick="$.cssTable.act('del',{url:'delTheme.action?ids=%{uuid}'})" href="javascript:;"></ww:link>
				</td>
				<td ><ww:property value="code"/></td>
				<td class="text-center"><ww:property value="name"/></td>
				<td class="text-center"><img style="width:80px;height:40px;" src="<%=request.getContextPath() %>/cssui/themes/<ww:property value="code"/>/images/breviary.jpg"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('theme_type',typeId).name"/></td>
				<td class="text-center" ><ww:property value="#dictID.getDictType('d_openflag',openFlag).name"/></td>
				<td class="text-center iconspage"><ww:if test='remark == "" or remark == null'><span>--</span></ww:if>
				<ww:else><ww:property value="remark"/></ww:else></td>
			</tr>
			<!--  
			<tr class="sub">	
			<td colspan="10" ><ww:property value="remark"/></td>
			</tr>
			-->
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
