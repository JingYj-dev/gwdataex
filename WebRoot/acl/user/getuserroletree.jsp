<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" name="ur_form" id="ur_form" method="get" action="addUserRole.action" onsubmit="return ur_add(this, dialogAjaxDone);">
	<input type="hidden" id="userId" name="userId" value="<ww:property value='userId'/>"> 
	<input type="hidden" id="ids" name="ids" value="">
	<div style="width: 400px;height: 250px; overflow: auto;">
		<ul id="ur_tree" class="ztree"></ul>
	</div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="submit">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
	</div>
</form>
<script>
	var ur_setting = {	
			check: {
				enable: true
			},
			data : {
				simpleData : {
					enable : true
				}
			},
		};
		function ur_add(form,callback){
			var nodes = ur_tree.getCheckedNodes(true);
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
		var ur_zNodes =<ww:property value="result"/>;
		var ur_tree  = null;
		using('tree',function(){
		    ur_tree = $.fn.zTree.init($("#ur_tree"), ur_setting, ur_zNodes);    
		})
</script>