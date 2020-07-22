<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" name="orgpost_form" id="orgpost_form" method="get" action="addOrgPost.action" onsubmit="return orgpost_add(this, dialogAjaxDone);">
	<input type="hidden" id="orgId" name="orgId" value="<ww:property value='orgId'/>"> 
	<input type="hidden" id="ids" name="ids" value="">
	<div style="width: 400px;height: 250px; overflow: auto;">
		<ul id="orgpost_tree" class="ztree"></ul>
	</div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="submit">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
	</div>
</form>
<script>
	var  orgpost_setting = {	
			check: {
				enable: true
			},
			data : {
				simpleData : {
					enable : true
				}
			},
		};
		function orgpost_add(form,callback){
			var nodes = orgpost_tree.getCheckedNodes(true);
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
		var orgpost_zNodes =<ww:property value="result"/>;
		var orgpost_tree  = null;
		using('tree',function(){
		    orgpost_tree = $.fn.zTree.init($("#orgpost_tree"), orgpost_setting, orgpost_zNodes);    
		})
</script>