<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:if test="item.paramId!= null">
<form class="form-horizontal form-validate" id="form1" method="post" name="form1" action="updMsgParam.action" onsubmit="return validateCallback(this, dialogAjaxDone);">
</ww:if>
<ww:else>
<form class="form-horizontal form-validate" id="form1" method="post" name="form1" action="addMsgParam.action" onsubmit="return validateCallback(this, dialogAjaxDone);">
</ww:else>
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.paramId'/>"/>
	<div class="control-group">
	    <label class="control-label"><span class="required">*</span>参数编号</label>
	    <div class="controls">
	    	<ww:if test="item.paramId == null">
	       		<input type="text" placeholder="请输入参数编号" onkeyup="value=this.value.replace(/\D+/g,'')" class="input-xlarge required digits" name="paramId" id="paramId" maxlength="5" title="请输入数字" value="<ww:property value='item.paramId'/>"  <ww:if test="paramId!= null"></ww:if>> 
	    	</ww:if>
			<ww:else>
				<input type="hidden" placeholder=""  class="input-xlarge required" name="paramId" id="paramId" value="<ww:property value='item.paramId'/>"  maxlength="50">
				<span>&nbsp;<ww:property value='item.paramId'/></span>
			</ww:else>
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label"><span class="required">*</span>参数名称</label>
	    <div class="controls">
	       <input type="text" placeholder="请输入参数名称"  class="input-xlarge required" name="paramName" id="paramName" value="<ww:property value='item.paramName'/>"  maxlength="50">
	  
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label"><span class="required">*</span>参数值</label>
	    <div class="controls">
	      <input type="text" placeholder="请输入参数值"  class="input-xlarge required "  name="paramValue" id="paramValue"  value="<ww:property value='item.paramValue'/>"  maxlength="200">
	    </div>
	  </div>
	 <div class="control-group">
		    <label class="control-label"><span>&nbsp;</span><span>&nbsp;</span>备注</label>
		    <div class="controls">
	          <div class="textarea">
		        <textarea class="input-xlarge" name="paramMemo" id="paramMemo" maxlength="120"><ww:property value='item.paramMemo'/></textarea>
		      </div>
		    </div>
		  </div>
		<div class="set-btn" data-spy="affix" data-offset-top="200">
			<button class="btn green" type="button" onclick="$(this).submit();">保存</button>
			<a class="btn" href="javascript:;" onclick="$css.closeDialog()" >取消</a>
		</div>
</form>