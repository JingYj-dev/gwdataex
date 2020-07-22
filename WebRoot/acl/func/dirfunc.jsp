<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<div class="tree-panel" layoutH="5">
	<ul id="funcTree" class="ztree"></ul>
</div>
<div class="trigger close" onclick="$('.tree-panel').toggle();$(this).toggleClass('close');"></div>
<div layoutH="5" id="div_func" class="table-div">
	<jsp:include page="dirfuncmini.jsp"></jsp:include>
</div>
<script type="text/javascript">
var func_setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		async: {
			enable: true,
			url:"getFuncTree.action",
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
			onClick : func_onClick,
			onAsyncSuccess : function(event, treeId, treeNode, msg){
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				if(treeObj.getSelectedNodes().length==0){
					var nodes = treeObj.getNodes();
					treeObj.selectNode(nodes[0]);
					treeObj.expandAll(nodes[0], true);
					treeObj.expandNode(nodes[0], true, true, true);
					func_onClick(event, treeId, nodes[0]);
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
	var parentNode = func_tree.getNodeByParam("id",newParentId, null);
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
			$css.tip('不能存在名称相同的同级功能，拖拽失败！');
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
		$css.post('dragFuncTree.action',{'targetId':targetNode.id , 'moveType':moveType ,'nodeIds':nodes},function(data){
		       var $form=$('#div_func').find("#func_form");
		       $form.submit();	 
		},'json');
	}
}
function func_onClick(e, treeId, treeNode) {
	var $form=$('#div_func').find("#func_form");
	$("#parentId",$form).val(treeNode ? treeNode.id : "0");
	$("#sysId",$form).val(treeNode ? treeNode.sysId : "0");
	$form.submit();	 
}
var func_tree = null;
using('tree',function(){
    func_tree = $.fn.zTree.init($("#funcTree"), func_setting);    
})
</script>