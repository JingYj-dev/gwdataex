<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" name="form1" id="form1" method="get">
	<div style="width: 400px;height: 250px; overflow: auto;">
		<ul id="tree" class="ztree"></ul>
	</div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="submit">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
	</div>
</form>
<script>
(function(){
	var no = '<ww:property value="no"/>';
	var $tab = $.cssTab.focus();
	var $dialog = $css.focusDialog();
	var treeId = "tree_" +new Date().getTime();//zTree里面的树必须具有惟一标识treeId，否则map不到。
	$("#tree", $dialog).attr("id",treeId);
	$("#form1",$dialog).submit(add);
	var setting = {	
			check: {
				enable: true
			},
			data : {
				simpleData : {
					enable : true
				}
			}
		};
		var zNodes =<ww:property value="result"/>;
		var tree  = null;
		using('tree',function(){
		    tree = $.fn.zTree.init($("#"+treeId), setting, zNodes);
		    var nodes = tree.getNodesByParam(setting);
		    var ids = $("#ids" + (no==''?'':no),$tab).val()+"";
			for (var i=0, l=nodes.length; i<l; i++) {
				if(ids.match(nodes[i].id))		
					nodes[i].checked = true;
			}
		})
		function add(){
			var nodes = tree.getCheckedNodes(true);
			var ids ="";
			var names ="";
			for(var i in nodes){
				if(!(nodes[i].isParent)) {
					ids += nodes[i].id;
					names +=nodes[i].name;
					if(i<nodes.length-1) {
						ids+=",";
						names+=",";
					}
				}
			}
			$("#ids" + (no==''?'':no),$tab).val(ids);
			$("#names" + (no==''?'':no),$tab).val(names);
			$css.closeDialog();
			return false;
		}
		/* function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			var nodes = tree.getNodesByParam(setting, null, "pId", treeNode.id);
			var ids = $("#ids",$tab).val()+"";
			console.log(111111)
			console.log(treeNode.id)
			console.log(222222)
			console.log(nodes.length)
			for (var i=0, l=nodes.length; i<l; i++) {
				console.log(i)
				console.log(ids.indexOf("asd"))
				console.log(ids.indexOf("asd"))
				console.log(ids.indexOf("asd"))
				console.log(ids.indexOf("asd"))
				ids.match(nodes[i].id);		
				nodes[i].checked = true;
			}
		}; */
		
		
})()
	
</script>