<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form"  name="func_form" id="func_form" action="dirFuncMini.action"  onsubmit="return divSearch(this,'div_func')">
    <input type="hidden" name="parentId" id="parentId" value="<ww:property value='parentId'/>">
    <input type="hidden" name="sysId" id="sysId" value="<ww:property value='sysId'/>">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
	<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<input type="hidden" id="delIds" value="<ww:property value="uuid" />" /> 
	<div class="table-header">
		<div class="table-search">
			<ul>
<%--				<li><input type="text" id="funcId" name="funcId"class="input-medium" placeholder="请输入功能代码" value="<ww:property value='funcId'/>"></li>--%>
<%--				<li><input type="text" id="funcName" name="funcName"class="input-medium" placeholder="请输入功能名称" value="<ww:property value='funcName'/>"></li>--%>
				<li><input type="text" id="name" name="name"class="input-medium" placeholder="请输入功能代码或名称" maxlength="25" value="<ww:property value='name'/>"></li>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
				<li>功能类型：<ww:select attributes="class='input-small'" name="'funcType'" id="funcType" value="funcType"  list="#dictID.getDictListQuery('d_functype')" listKey="code" listValue="name"></ww:select> </li>
				</ww:if>
				<li>状态：<ww:select attributes="class='input-small'" name="'openFlag'" id="openFlag" value="openFlag"  list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
				<li>
				 <ww:button css="btn green" caption="查询" funcode="acl_func/dirFunc" type="submit"></ww:button>
				</li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
			  <ww:link funcode="acl_func/addFunc" target="cssDialog" rel="addfunc" title="添加功能" css="btn" caption="添加" href="getFunc.action?parentId=%{parentId}&sysId=%{sysId}"></ww:link>	
			</div>
			<div class="btn-group">
		      <ww:link funcode="acl_func/delFuncConFirm" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch',{callback:delFunc,url:'delFuncConFirm.action'});"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:dropdown funcode="acl_func/UpdateFuncStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','UpdateFuncStatus.action?openStatus=%{#model.code}')" />  
		    </div>
			<div class="btn-group btnTab" style="float:right;">
		    	<input type="hidden" name="includeFlag" id="includeFlag" value="<ww:property value='includeFlag'/>">
				<a class="btn" value="1" href="javascript:;">当前功能</a>
				<a class="btn" value="2" href="javascript:;">包含下级</a>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
				<th width="120px">操作</th>
				<th width="120px" order-field="funcId" class="order">功能代码</th>
				<th width="100px" order-field="name" class="order">功能名称</th>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<th width="100px" order-field="funcType" class="order">功能类型</th>
				</ww:if>
				<!-- 
				<th  width="40px"order-field="operType" class="order">操作类型</th>
				<th  width="40px"order-field="logLevel" class="order">日志级别</th>
				 -->
				<th order-field="remark" class="order">功能描述</th>
				<th width="30px"order-field="openFlag" class="order">状态</th>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
				   <ww:link funcode="acl_func/updFunc" caption="修改" title="修改功能" target="cssDialog" rel="editfunc"  href="getFunc.action?uuid=%{uuid}&parentId=%{parentId}" ></ww:link>&nbsp;
				   <ww:link funcode="acl_func/delFuncConFirm" caption="删除" onclick="$.cssTable.act('del',{callback:delFunc,url:'delFuncConFirm.action?ids=%{uuid}'});" href="javascript:;"></ww:link>&nbsp;
				   <ww:link funcode="acl_func/dirFuncPoint" caption="功能项" target="cssTab" rel="dirFuncPoint%{uuid}"  href="dirFuncPoint.action?uuid=%{uuid}" title="%{name}-功能项管理"></ww:link>
				</td>
				<td><ww:property value="funcId"/></td>
				<td><ww:property value="name"/></td>
				<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
					<td class="text-center"><ww:property value="#dictID.getDictType('d_functype',funcType).name" /></td>
				</ww:if>
				<!-- 
				<td class="text-center"><ww:property value="#dictID.getDictType('d_opertype',operType).name" /></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_loglevel',logLevel).name" /></td>
				 -->
				<td><ww:property value="remark"/></td>	
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag',openFlag).name" /></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
function delFunc(data){
	var info = data.info.split(";");
	var $tab = $.cssTab.focus(); 
	if(info[1] == "false"){//存在角色功能关联
		$css.confirm("确定删除功能以及角色-功能关联吗？",function(){
			$css.post('delFunc.action',{'ids':info[0]},function(data){
				if(data.result==0){  
					  for( var i=0;i<data.info.length;i++) { 
						  var delnode = func_tree.getNodeByParam("id",data.info[i], null);
					      func_tree.removeNode(delnode);
					  }
					  var $form=$('#div_func').find("#func_form");
				      $form.submit();            
				   $css.tip(data.msg);
				}else{
				   $css.alert(data.msg);
				}
			},'json');
		});	
		//	$.cssTable.act('del',{url:'delFunc.action?ids=' + info[0]});
		//	document.location="delFunc.action?"+ "ids=" + info[0];
	}else{
		$css.post('delFunc.action',{'ids':info[0]},function(data){
			if(data.result==0){  
				  for( var i=0;i<data.info.length;i++) { 
					  var delnode = func_tree.getNodeByParam("id",data.info[i], null);
				      func_tree.removeNode(delnode);
				  }
				  var $form=$('#div_func').find("#func_form");
			      $form.submit();           
			   $css.tip(data.msg);
			}else{
			   $css.alert(data.msg);
			}
		},'json');

		//document.location="delFunc.action?"+ "ids=" + info[0];
	}

}
</script>

