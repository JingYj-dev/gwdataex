<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.post.model.SPost'" id="postID" />
<form class="form-horizontal table-form" name="orgpostmini_form"  id="orgpostmini_form" action="dirOrgPostMini.action"  onsubmit="return divSearch(this,'include_org_postmini')">
	<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
	<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<input type="hidden" id="orgId" name="orgId" value="<ww:property value='orgId'/>"/>
	<div class="table-header" >
		<div class="table-search">
				<ul>
					<li><input type="text" id="postname" name="postname" class="input-medium" maxlength="20" placeholder="请输入岗位名称" value="<ww:property value='postname'/>"></li>
					<li>
					  <ww:button css="btn green" caption="查询" funcode="acl_org/dirOrgPostMini" type="submit"></ww:button>
					</li>
				</ul>
			</div>
	</div>
<div style="overflow-y:auto;height:240px;">
	<table class="table table-bordered">
		<thead>
				<tr>
				<th width="20px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
				<th width="150px">岗位名称</th>
				<th width="250px">岗位描述</th>
			</tr>
		</thead>
		<tbody>
			    <ww:iterator value="page.results" id="data"> 
				<tr>
					<td class="text-center"><input type="checkbox" class="choose" name="ids" value="<ww:property value="uuid"/>" /></td>
					<td><ww:property value="name"/><input type="hidden" id="name" name="name" value="<ww:property value='name'/>"/></td>
					<td><ww:property value="remark"/><input type="hidden" id="remark" name="remark" value="<ww:property value='remark'/>"/></td>
				</tr>
				</ww:iterator>
		</tbody>
	</table>
</div>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
	    <div class="set-btn" data-spy="affix" data-offset-top="200">
	    <button class="btn green" type="button" onclick="savePost()">保存</button>
	    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  	</div>
</form>
<script type="text/javascript">
	$.cssTab.focus().initUI();
	function savePost() {
		var j = 0;
		$.each($(".choose"),function(i,d){
			if(d.checked == true){
				j ++;
			}				
		});
		if(j == 0){
			$css.tip("请选择一条记录");
			return false;
		}


		
		var $form = $('#orgpostmini_form');
		//提交
		$form.attr('action','acl_org/addOrgPost.action');
		validateCallback($form.get(0), dialogAjaxDone);
		//重置
		$form.attr('action','acl_org/dirOrgPostMini.action');
	}
</script>