<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" method="post" action="updPassword.action" onsubmit="return validateCallback(this, dialogAjaxDone);"
	name="user_password" id="user_password">
  <input type="hidden" name="ids" id="ids" value="<%=request.getParameter("ids")%>">
  <input type="hidden" name="userPassword" id="userPassword" value=""/>
  <div class="control-group">
    <label class="control-label"><span class="required">*</span>新密码</label>
    <div class="controls">
      <input type="password" maxlength="50" placeholder="请输入新密码" class="input-xlarge required"  id="password1" >
    </div>
  </div>
  <div class="set-btn" data-spy="affix" data-offset-top="200">
    <button class="btn green" type="button" onclick="pwd(this);">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  </div>
</form>
<script>
function pwd(obj) {
	var pwd1 = md5($("#password1").val());
	$("#userPassword").val(pwd1);
	$(obj).submit();
}

</script>