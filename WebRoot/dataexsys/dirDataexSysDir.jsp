<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<div class="tree-panel" layoutH="5">
	<ul id="sysDirTree" class="ztree"></ul>
</div>
<div class="trigger close" onclick="$('.tree-panel').toggle();$(this).toggleClass('close');"></div>
<div layoutH="5" id="div_dataexSysDir" class="table-div">
	<jsp:include page="dirDataexSysDirMini.jsp"></jsp:include>
</div>
<script type="text/javascript">
var dir_setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		async: {
			enable: true,
			url:"getDataexSysDirTree.action",
			autoParam:["id=id"],
			type:"post"
		},
		callback : {
			onClick : dataexSysDir_onClick
		}
	};
function dataexSysDir_onClick(e, treeId, treeNode) {
	var $form = $('#div_dataexSysDir').find("#dataexSysDir_form");
	$("#dataexSysDirId",$form).val(treeNode ? treeNode.id : "0");
	$form.submit();	
}
//var dir_zNodes = <ww:property value="tree"/>;

var dir_tree = null;
using('tree',function(){
	//dir_tree = $.fn.zTree.init($("#sysDirTree"), dir_setting, dir_zNodes); 
	dir_tree = $.fn.zTree.init($("#sysDirTree"), dir_setting);   
})
</script>