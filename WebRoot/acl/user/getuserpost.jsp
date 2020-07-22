<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.post.model.SPost'" id="dictPost" />
<ww:bean name="'com.hnjz.apps.base.org.model.SUserOrg'" id="orgID" />
<ww:bean name="'com.hnjz.apps.base.post.service.SPostItem'" id="postID" />
<ww:bean name="'com.hnjz.apps.base.role.action.RoleItem'" id="roleItem"></ww:bean>
<div id="userPostDiv">
<form class="form-horizontal table-form" id="form1" method="post" name="form1" action="getUserPost.action" onsubmit="return divSearch(this, 'userPostDiv')">
<input type="hidden" id="userId" name="userId" value="<ww:property value='userId'/>"/>
<input type="hidden" id="delRoleIds" name="delRoleIds" value="<ww:property value='delRoleIds'/>"/>
<input type="hidden" name="page.orderFlag" id="page.orderFlag" value="<ww:property value='page.orderFlag'/>" class="order-flag"> 
<input type="hidden" name="page.orderString" id="page.orderString" value="<ww:property value='page.orderString'/>" class="order-string">
<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
<div class="table-header fwin_line" style="padding:15px;overflow-y:auto;width:710px;height:280px;" >
<table class="table table-bordered">
	<thead>
	<tr/>
		<tr>
			<th width="20px"><input group="ids" type="checkbox" class="cleck-all" name="checkdelall" id="checkdelall"/></th>
			<th width="150px">岗位名称</th>
			<th width="250px" >关联角色</th>
<%--			<th width="250px" >岗位描述</th>--%>
		</tr>
	</thead>
	<tbody>
			
		    <ww:iterator value="page.results" id="data" status="status"> 
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid"/>"/></td>
				<td><ww:property value="name"/></td>
				<td>
					<ww:iterator value="#postID.getRoleList(uuid)" id="dataRole" status="statusRole">
<%--						   <ww:set name="record" value="#postID.getRoleList(uuid)[#status.count]"/>--%>
						    <span id='span<ww:property value="uuid"/><ww:property value="#status.count"/>'>
						       <ww:property value="name"/>
						       <ww:if test="#roleItem.getRoleUseFlag(userId,uuid) == false">
						       <a href="javascript:delPostRole('<ww:property value="uuid"/>','<ww:property value="#status.count"/>')"><img src="portal/images/icon/btn_del.png"></a>
						       </ww:if>
						       <ww:else>;</ww:else>
						    </span>
					</ww:iterator>
				</td>
			</tr>
			</ww:iterator>
	</tbody>
</table>
<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
			<button class="btn green" type="button" onclick="addUserPost()">保存</button>
			<a class="btn" href="javascript:;" onclick="$css.closeDialog()" >取消</a>
	</div>
</form>
</div>

<script>
function addUserPost() {
	var url='addUserPost.action',
		pars = $("#form1").serialize();
	$.post(url,pars,function(data){
		switch(data.result){
			case 0:
				$css.closeDialog();
				$css.tip(data.msg);
				$.cssTable.focus().submit();
			   break;
			default:
				$css.alert(data.msg);
				break;
		}
	},'json');
} 
function delPostRole(id,index) {
	if($("#delRoleIds").val() == ""){
	 $("#delRoleIds").val(id);
	}else{
		$("#delRoleIds").val($("#delRoleIds").val()+","+id);
	}
	 $("#span"+id+index).remove(); 
}
</script>
