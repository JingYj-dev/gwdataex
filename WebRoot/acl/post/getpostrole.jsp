<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<div style="width:710px;height:370px;">
<form id="postrole_form" name="postrole_form" class="form-horizontal table-form long" method="post" action="acl_post/dirPostRoleMini.action" onsubmit="return divSearch(this,'include')">
	<input type="hidden" name="postId" id="postId" value="<ww:property value='item.uuid'/>" />
	<input type="hidden" name="Flag" id="Flag" />
	<input type="hidden" id="roleids" name="roleids"/>
	<input type="hidden" id="sysids" name="sysids"/>
    <div class="control-group">
		<div id="include"></div> 
    </div>
    
    <div class="set-btn" data-spy="affix" data-offset-top="200">
<%--	    <button class="btn green" onclick="save()">保存</button>--%>
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
		var $form = $('#postrole_form');
		var roleid = myCheckedVal('roleid',$form);
		var sysid = myCheckedVal('sysid',$form);
		$('#roleids').val(roleid);
		$('#sysids').val(sysid);
		//提交
		$form.attr('action','acl_post/addPostRole.action');
		validateCallback($form.get(0), dialogAjaxDone);
		//重置
		$form.attr('action','acl_post/dirPostRoleMini.action');
		$('#roleids').val('');
		$('#sysids').val('');
	}
	function search(){
		$('#postrole_form').attr('action','acl_post/dirPostRoleMini.action');
		var parms = $("#postrole_form").serialize();
		var toUrl = "acl_post/dirPostRoleMini.action";
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
	}
	search();
</script>