<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal form-validate" id="form1" name="form1" method="post" action="<ww:if test="uuid!= null">updPost.action</ww:if><ww:else>addPost.action</ww:else>" onsubmit="return validateCallback(this, dialogAjaxDone);">
<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
  <div class="control-group">
    <label class="control-label"><span class="required">*</span>岗位名称</label>
    <div class="controls">
       <input type="text"  placeholder="不超过20个字"  class="input-xlarge required" name="name" id="name" value="<ww:property value='item.name'/>"  maxlength="20">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label"><span>&nbsp;</span>岗位描述</label>
    <div class="controls">
      <div class="textarea">
        <textarea rows="5" class="input-xlarge" name="remark" id="remark" maxlength="120"><ww:property value='item.remark'/></textarea>
      </div>
    </div>
  </div>
   <div class="set-btn" data-spy="affix" data-offset-top="200">
<%--    <button class="btn green" type="submit">保存</button>--%>
    <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  </div>
</form>