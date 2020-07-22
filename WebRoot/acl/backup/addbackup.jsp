<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" name="add_bak_form"  id="add_bak_form" method="post" action="addDataBackup.action" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<div class="control-group">
	    <label class="control-label"><span class="required">*</span>备份名称：</label>
	    <div class="controls" style="margin-left: 90px;">
	       <input type="text"  class="input-large required backupname" name="backupName" id="backupName"  maxlength="30">
	    </div>
  	</div>
  	
  	<div class="control-group">
	    <label class="control-label"><span class="required">*</span>是否压缩：</label>
	    <div class="controls" style="margin-left: 90px;">
	       <label class="radio inline"><input type="radio" name="zipMark" value="1" checked="checked">是</label>
	       <label class="radio inline"><input type="radio" name="zipMark" value="2">否</label>
	    </div>
  	</div>
  	
  	<div class="set-btn" data-spy="affix" data-offset-top="200" style="padding: 5px 20px;">
	    <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
	    <a class="btn" href="javascript:;" onclick="$css.closeDialog()" >取消</a>
	</div>
</form>
