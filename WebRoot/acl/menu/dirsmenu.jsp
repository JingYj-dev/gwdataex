<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<div class="tree-panel" layouth="5">
	<ul id="smenuTree" class="ztree"></ul>
</div>
<div class="trigger close" onclick="$('.tree-panel').toggle();$(this).toggleClass('close');"></div>
<div layoutH="5" id="div_menu" class="table-div">
	<jsp:include page="dirsmenumin.jsp"></jsp:include>
</div>
<script type="text/javascript">
var menu_setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		async: {
			enable: true,
			url:"getMenuTree.action",
			autoParam:["id=id"],
			type:"post"
		},
		callback : {
			onClick : menu_onClick,
			onAsyncSuccess : function(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if(treeObj.getSelectedNodes().length==0){
				var nodes = treeObj.getNodes();
				treeObj.selectNode(nodes[0]);
				treeObj.expandAll(nodes[0], true);
				treeObj.expandNode(nodes[0], true, true, true);
				menu_onClick(event, treeId, nodes[0]);
			}
		}
		}
	};
function menu_onClick(e, treeId, treeNode) {
	var $form=$('#div_menu').find("#menu_form");
	$("#parentId",$form).val(treeNode ? treeNode.id : "0");
	$("#sysId",$form).val(treeNode ? treeNode.sysId : "0");
	$form.submit();	 
}
<%--var menu_zNodes =<ww:property value="result"/>;--%>
var rf_tree  = null;
using('tree',function(){
   rf_tree =  $.fn.zTree.init($("#smenuTree"), menu_setting);    
})
</script>