<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<%--<div class="tree-panel" layouth="5">
--%><%--<input type="hidden" name="dragFlag" id="dragFlag" class="order-flag">
	<ul id="orgTree" class="ztree"></ul>
	<jsp:include page="saveDragTree.jsp"></jsp:include>
</div>
--%>
<div class="tree-panel" layouth="5">
<ul id="orgTree" class="ztree"></ul>
</div>
<div class="trigger close" onclick="$('.tree-panel').toggle();$(this).toggleClass('close');"></div>

<div layoutH="5" id="div_org" class="table-div">
	
	<jsp:include page="dirorgmin.jsp"></jsp:include>
</div>
<script type="text/javascript">
var org_setting = {
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
		edit: {
			enable: true,
			showRemoveBtn: false,
			showRenameBtn: false,
			drag: {
				isCopy: false,
				isMove: true
			}
		},			
		callback : {
			beforeDrop: zTreeBeforeDrop,
			onDrop: zTreeOnDrop,
			onClick : org_onClick,
			onAsyncSuccess : function(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if(treeObj.getSelectedNodes().length==0){
				var nodes = treeObj.getNodes();
				treeObj.selectNode(nodes[0]);
			}
		}
		}
	};
function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) { //禁止成为根节点，禁止相同名称的组织机构成为兄弟节点
	var flag = 0;
	var newParentId = targetNode.id;
	if(moveType == "prev" || moveType == "next"){
		newParentId = targetNode.getParentNode().id;
	}
	var parentNode = rf_tree.getNodeByParam("id",newParentId, null);
	if(parentNode.isParent){
		var nodes = parentNode.children;
		for(var i=0;i<treeNodes.length;i++){
			for(var j=0;j<nodes.length;j++){
				if(treeNodes[i].id != nodes[j].id && treeNodes[i].name == nodes[j].name){
					flag = 1;
					break;
				}
			};
		};
		if(flag == 1){
			$css.tip('不能存在名称相同的同级机构，拖拽失败！');
		}
	}
    return !(targetNode == null || (moveType != "inner" && !targetNode.parentTId) || (flag == 1));
}
function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
	if(moveType !=null && treeNodes !=null && targetNode!=null){
		var nodes=treeNodes[0].id;
		if(treeNodes.length>1){
			for(var i=1;i<treeNodes.length;i++){
				nodes +=',';
				nodes +=treeNodes[i].id;
			};
		}
		$css.post('dragOrgTree.action',{'targetId':targetNode.id , 'moveType':moveType ,'nodeIds':nodes},function(data){
		       var $form=$('#div_org').find("#org_form");
		       $form.submit();	 
		},'json');
	}
}
function org_onClick(e, treeId, treeNode) {
	var $form=$('#div_org').find("#org_form");
	$("#parentId",$form).val(treeNode ? treeNode.id : "");
	$form.submit();	 
}
<%--var org_zNodes =<ww:property value="result"/>;--%>
var rf_tree  = null;
using('tree',function(){
   rf_tree =  $.fn.zTree.init($("#orgTree"), org_setting);    
})
</script>