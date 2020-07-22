<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal form-validate" method="post"  <ww:if test="dict.uuid != null">action="updDict.action"</ww:if><ww:else>action="addDict.action"</ww:else> onsubmit="return validateCallback(this, dialogAjaxDone);">
  <input type="hidden" name="uuid" id="uuid" value="<ww:property value='dict.uuid'/>">
  <input type="hidden" name="parentId" id="parentId" value="<ww:property value='dict.parentId'/>">
  <input type="hidden" name="tableName" id="tableName" value="<ww:property value='dict.tableName'/>">
  <div class="control-group" >
    <label class="control-label"><span class="required">*</span>字典编码</label>
    <div class="controls">
      <input type="text" maxlength="100" <ww:if test="dict.uuid != null && \"d_root\".equals(dict.tableName)">readonly</ww:if>  placeholder="请输入字典编码"  class="input-large required dictcode" name="code" id="code" value="<ww:property value='dict.code'/>" >
    </div>
  </div>
  <div class="control-group">
    <label class="control-label"><span class="required">*</span>字典名称</label>
    <div class="controls">
      <input type="text" maxlength="100" placeholder="请输入字典名称" class="input-large required" name="name" id="name" value="<ww:property value='dict.name'/>" >
    </div>
  </div>
  <div class="control-group">
    <label class="control-label"><span>&nbsp;</span><span>&nbsp;</span>字典描述</label>
    <div class="controls">
      <div class="textarea">
        <textarea rows="5" maxlength="150" name="remark" id="remark" class="input-large"><ww:property value='dict.remark'/></textarea>
      </div>
    </div>
  </div>
  <div class="set-btn" data-spy="affix" data-offset-top="200">
   <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  </div>
</form>