<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<div class="tree-panel" layoutH="5">
	<ul id="userAdminTree" class="ztree"></ul>
</div>
<div class="trigger close" onclick="$('.tree-panel',$.cssTab.focus()).toggle();$(this).toggleClass('close');"></div>
<div layoutH="5" id="div_userAdmin" class="table-div">
	<jsp:include page="dirusermini.jsp"></jsp:include>
</div>
<script type="text/javascript">
var user_setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		async: {
			enable: true,
			url:"getOrgTree.action",
			autoParam:["id=id"],
			type:"post"
		},
		callback : {
			onClick : user_onClick,
			onAsyncSuccess : function(event, treeId, treeNode, msg){
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				if(treeObj.getSelectedNodes().length==0){
					var nodes = treeObj.getNodes();
					treeObj.selectNode(nodes[0]);
				}
			}
		}
	};
function user_onClick(e, treeId, treeNode) {
	var $form=$('#div_userAdmin').find("#userAdmin_form");
	$("#orgId",$form).val(treeNode ? treeNode.id : "0");
	$form.submit();	
}
<%--var user_zNodes = <ww:property value="tree"/>;--%>
var user_tree = null;
using('tree',function(){
	user_tree = $.fn.zTree.init($("#userAdminTree"),user_setting);    
})
</script>