<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal form-validateLog" id="form1" name="form1" method="post" action="<ww:if test="item.setId!= null">updSAlertSetting.action</ww:if><ww:else>addSAlertSetting.action</ww:else>" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<input type="hidden" id="alertType" name="" value="<ww:property value='item.alertType'/>"/>
	<input type="hidden" id="setId" name="setId" value="<ww:property value='item.setId'/>"/>
	<div class="control-group">
			<label class="control-label"><span class="required">*</span>事件类型</label>
			<div class="controls">
		            <select name="eventType" id="eventType"  class="input-medium required">
		        		<ww:iterator value="#dictID.getDictType('d_eventtype')">
		        			<option value="<ww:property value="code"/>" 
		        				<ww:if test="code == item.eventType">selected</ww:if>><ww:property value="name"/></option>
		        		</ww:iterator>
				  </select>
			</div>
      </div> 
      <div class="control-group">
			<label class="control-label"><span class="required">*</span>重要程度</label>
			<div class="controls">
		            <select name="severLevel" id="severLevel"  class="input-medium required">
		        		<ww:iterator value="#dictID.getDictType('d_loglevel')">
		        			<option value="<ww:property value="code"/>" 
		        				<ww:if test="code == item.severLevel">selected</ww:if>><ww:property value="name"/></option>
		        		</ww:iterator>
				  </select>
			</div>
      </div> 
      <div class="control-group">
		<label class="control-label"><span class="required">*</span>预警方式:</label>
		<div class="controls">
			<ww:iterator value="#dictID.getDictType('d_alertType')" status="status">
				<label class="checkbox inline"> <input type="checkbox" class="input-small required" value="<ww:property value='code'/>"  id="alertType<ww:property value='code'/>" name="alertType"><ww:property value="name" /></label>
			</ww:iterator>
		</div>
	</div>
   <div class="set-btn" data-spy="affix" data-offset-top="200">
    <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  </div>
</form>
<script>
jQuery(function($) {
	var alertType = $("#alertType").val().split(",");
	for(var i = 0; i < alertType.length; i++){
		var value = alertType[i].trim();
		var val = $("#alertType" + value).val();
		if(value == val) {
			$("#alertType" + value).attr("checked","checked");
		}
	}
});
</script>
