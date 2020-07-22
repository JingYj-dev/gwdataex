<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<div style="width:750px;">
<form id="funcpoint_form" name="funcpoint_form" class="form-horizontal table-form long" method="post" action="acl_func/dirFuncPointMini.action" onsubmit="return divSearch(this,'include')">
	<input type="hidden" id="sysId" name="sysId" value="<ww:property value='item.sysId'/>"/>
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
	<input type="hidden" name="funcCode" id="funcCode" value="<ww:property value='item.uuid'/>" />
	<input type="hidden" id="actionCodes" name="actionCodes"/>
	<input type="hidden" id="actionNames" name="actionNames"/>
    <div class="control-group">
		<div id="include"></div> 
    </div>
    <div class="set-btn" data-spy="affix" data-offset-top="200">
	    <button class="btn green" type="button" onclick="save()">保存</button>
	    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  	</div>
</form>
</div>
<script type="text/javascript">
	function myCheckedVal(name, parent, split){
        var s=[];
        $('input[name="ids"]:checked',$(parent || document)).each(function(i,t){
        	//获取checked的tr下为name的隐藏框
			var target = $(t).parent().siblings().find('input[name="'+name+'"]:hidden');
            s.push(target.val());
        });
        return s.join(split);
    }
	function save(){
		var $form = $('#funcpoint_form');
		var actionCodes = myCheckedVal('actionCode',$form);
		var actionNames = myCheckedVal('actionName',$form);
		$('#actionCodes').val(actionCodes);
		$('#actionNames').val(actionNames);
		//提交
		$form.attr('action','acl_func/addFuncPoint.action');
		validateCallback($form.get(0), dialogAjaxDone);
		//重置
		$form.attr('action','acl_func/dirFuncPointMini.action');
		$('#actionCodes').val('');
		$('#actionNames').val('');
	}
	function search(){
		$('#funcpoint_form').find('.page-current').val(1);
		$('#funcpoint_form').attr('action','acl_func/dirFuncPointMini.action');
		var parms = $("#funcpoint_form").serialize();
		var toUrl = "acl_func/dirFuncPointMini.action";
		$.ajax({
			type : "POST", // 用POST方式传输
			url : toUrl,
			data : parms,
			beforeSend : function() {}, 
			complete : function() {}, 
			error : function(XMLHttpRequest, textStatus, errorThrown) {},
			success : function(data) {
				$('#include').html(data);
			},
			cache : false
		});
		//$('#funcpoint_form').attr('ation','acl_func/dirFuncPointMini.action');
		//divSearch($("#funcpoint_form").get(0),'include')
	}
	search();
</script>