<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal form-validate" style="width: 400px;height: 110px;" id="officialdocpack_store_form" method="post" name="officialdocpack_store_form" action="addOfficialDocPackSys.action" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<div class="control-group" style="height: 60px;">
	    <label class="control-label" for="input01" style="width: 100px;"><span class="required">*</span>选择公文包</label>
		<div class="controls" id="uploadHead">
		<%--
	       <a class="fileDownload" href="downloadFwDocTypeAttach.action?uuid=<ww:property value="item.uuid"/>&attach=head" ><ww:if test="item.headTmp!=null && !item.headTmp.equals(\"\")">下载</ww:if></a>
	     --%>
		   <a class="fileUpload">上传</a><span><input type="hidden" id="attachmentid" name="attachmentid" value="" class='required'/></span>
		   <a class="fileDel" href="" style="display: none">删除</a>
	    </div>
	    <br/>
	    <div id="gwNameDivId" style="display:none;">
		    <label class="control-label" for="input01" style="width: 100px;">公文包名称</label>
			<div class="controls" >
			   <span id="gwName"></span>
		    </div>	  
	    </div>
		    
	</div>
	
	
	
	<div class="set-btn">
		<div>
			<button class="btn green" type="button" onclick="$(this).submit();">导入</button>
			<a class="btn" href="javascript:void(0);" onclick="$css.closeDialog()" >取消</a>
		</div>
	</div>
</form>
<script type="text/javascript">
	function delAttach(uuid, filePath) {
		$css.confirm("确定要删除吗？", function() {
			$css.post('delOfficialDocSys.action', {'uuid':uuid, 'filePath':filePath}, function(data) {
				if (data.result == 0) {
					$.dialog.tips(data.msg, 1, 'success.gif');
					$("#uploadHead .fileUpload").show();
					$("#uploadHead .fileDel").hide();
					$("#attachmentid").val("");
					$("#gwNameDivId").hide();
					//$("#gwpack_name").attr("style","display: none;");
				} else {
					$.dialog.tips(data.msg, 1, 'error.gif');
				}
			}, 'json');
		});
	}

	fileUpload('uploadHead', null, 'uploadOfficialDocSys.action',
		function(data) {
			if (data.result == 0) {
				$.dialog.tips('文件上传成功！', 1, 'success.gif');
				var uuid = data.sid;
				var filePath = data.path;
				$("#uploadHead .fileUpload").hide();
				$("#uploadHead .fileDel").show();
				$("#uploadHead .fileDel").attr("href","javascript:delAttach('" + uuid + "','" + filePath + "')");
				$("#uploadHead .fileDel").text('删除');
				$("#attachmentid").val(uuid);
				$("#gwNameDivId").show();
				//$("#gwpack_name").attr("style","display: inline;width: 200px;");
				$("#gwName").html(data.description);
				//$.cssTable.focus().submit();
			} else {
				$.dialog.tips(data.msg, 1, 'error.gif');
			}
	});
	
</script>