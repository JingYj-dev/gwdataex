<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="menu_form" id="menu_form" action="dirmenuMin.action" method="post" onsubmit="return divSearch(this,'div_menu')">
    <input type="hidden" name="parentId" id="parentId" value="<ww:property value='parentId'/>">
	<input type="hidden" name="menuId" id="menuId" value="<ww:property value='menuId'/>">
	<input type="hidden" name="sysId" id="sysId" value="<ww:property value='sysId'/>">
	<input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input type="text"id="menuNameOrFuncId" name="menuNameOrFuncId"class="input-large" placeholder="请输入功能编码或菜单名称" value="<ww:property value='menuNameOrFuncId'/>"></li><%--
				<li><input type="text"id="menuName" name="menuName"class="input-medium" placeholder="请输入菜单名称" value="<ww:property value='menuName'/>"></li>
				--%><li>状态：<ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
				<li>
				  <ww:button css="btn green" caption="查询" funcode="acl_menu/dirmenuMin" type="submit"></ww:button>
				</li>
				<!-- <li><input type="button" value="生成菜单栏XML" onclick="batch('xmlMenu.action')"></input></li> -->
				<!--<li><a href="javascript:;" onclick="batch('xmlMenu.action')" title="XML" ><font size='3' color='black'>生成菜单栏XML</font></a></li>
			--></ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_menu/addMenu" css="btn" caption="添加" title="添加菜单" target="cssDialog" rel="addMenu" href="getMenu.action?parentId=%{parentId}"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:link funcode="acl_menu/delMenu" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch',{callback:delMenu,url:'delMenu.action'});"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:dropdown funcode="acl_menu/updMenuStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updMenuStatus.action?openStatus=%{#model.code}')" />  
		    </div>
		    <div class="btn-group btnTab" style="float:right;">
				<input type="hidden" name="includeFlag" id="includeFlag" value="<ww:property value='includeFlag'/>">
				<a class="btn" value="1" href="javascript:;">当前级别</a>
				<a class="btn" value="2" href="javascript:;">包含下级</a>
			</div> 
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="120px">操作</th>
				<th width="100px" order-field="menuName" class="order">菜单名称</th>
				<th width="120px" order-field="funcId" class="order">功能编码</th>
				<th width="300px" order-field="url" class="order">菜单地址</th>
				<th width="200px" >菜单图标</th>
				<th width="25px" align="center" order-field="orderNum" class="order">菜单序号</th>
				<th width="50px"order-field="openFlag" class="order">状态</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="menuId" />"/></td>
				<td class="text-center">
					 <ww:link funcode="acl_menu/updMenu" caption="修改" title="修改菜单"  target="cssDialog"  rel="updMenu" href="getMenu.action?menuId=%{menuId}" ></ww:link>&nbsp;
					 <ww:link funcode="acl_menu/delMenu" caption="删除"  onclick="$.cssTable.act('del',{callback:delMenu,url:'delMenu.action?ids=%{menuId}'});"   href="javascript:;"></ww:link>&nbsp;
					</td>
				<td><ww:property value="menuName"/></td>
				<td><ww:property value="funcId"/></td>
				<td><ww:property value="url"/></td>
				<td class=" iconspage"><i class="<ww:property value='icon'/>" />&nbsp;&nbsp;<ww:property value='icon'/></td>
				<td  class="text-center"><ww:property value="orderNum"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
	function delMenu(data){
		if(data.result==0){
		  for( var i=0;i<data.info.length;i++)
		  { var delnode = rf_tree.getNodeByParam("id",data.info[i], null);
		    rf_tree.removeNode(delnode);
		  }
		   $css.tip(data.msg);
		   var $form=$('#div_menu').find("#menu_form");
	       $form.submit();	 
		}
		if(data.result==1){
			$css.alert(data.msg);
		}
	}

	function batch(url,callback){
	}
	function batch(url,callback){
	if(confirm('确认生成XML？')){
		$css.post(url,{'ids':"0"},function(data){
			if(data.result==0){              
			   $css.tip(data.msg);
			}else{
			   $css.alert(data.msg);
			}
		},'json');
		return false;
		}
	}
</script>