<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" name="rolefuncform" id="rolefuncform" method="post" action="addRoleFunc.action" onsubmit="return rf_add(this, dialogAjaxDone);">
	<input type="hidden" id="roleId" name="roleId" value="<ww:property value='roleId'/>">
	<input type="hidden" id="ids" name="ids" value="">
	<div style="width: 500px;height: 300px; overflow: auto;">
		<ul id="rf_tree" class="ztree"></ul>
	</div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="button" onclick="$(this).submit();">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
	</div>
</form>
<script>
	var rolefunc_setting = {	
			check: {
				enable: true
			},
			data : {
				simpleData : {
					enable : true
				}
			},
		};
		function rf_add(form,callback){
			var nodes = rf_tree.getCheckedNodes(true);
			var pars ="";
			for(var i in nodes){
				pars += nodes[i].id;
				if(i<nodes.length-1)
				 pars+=",";
				
			}
			$("#ids").val(pars);
			validateCallback(form, callback);
			return false;
		}
		var rf_zNodes =<ww:property value="result"/>;
		var rf_tree  = null;
		using('tree',function(){
		    rf_tree = $.fn.zTree.init($("#rf_tree"), rolefunc_setting, rf_zNodes);    
		})
</script>