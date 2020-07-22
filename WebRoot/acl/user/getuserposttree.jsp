<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" name="up_form" id="up_form" method="get" action="addUserPost.action" onsubmit="return up_add(this, dialogAjaxDone);">
	<input type="hidden" id="userId" name="userId" value="<ww:property value='userId'/>"> 
	<input type="hidden" id="ids" name="ids" value="">
	<div style="width: 400px;height: 250px; overflow: auto;">
		<ul id="up_tree" class="ztree"></ul>
	</div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="submit">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
	</div>
</form>
<script>
	var up_setting = {	
			check: {
				enable: true
			},
			data : {
				simpleData : {
					enable : true
				}
			},
		};
		function up_add(form,callback){
			var nodes = up_tree.getCheckedNodes(true);
			var pars ="";
			for(var i in nodes){
				if(nodes[i].use){
					pars += nodes[i].id;
					if(i<nodes.length-1)
					 pars+=",";
				}
			}
			$("#ids").val(pars);
			validateCallback(form, callback);
			return false;
		}
		var up_zNodes =<ww:property value="result"/>;
		var up_tree  = null;
		using('tree',function(){
		    up_tree = $.fn.zTree.init($("#up_tree"), up_setting, up_zNodes);    
		})
</script>