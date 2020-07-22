<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:if test="item.uuid != null">
<div class="page-header">
  <h5>修改交换APPID</h5>
</div>
<form class="form-horizontal form-validate" id="updDataexSysAppid_form" method="post" action="updDataexSysAppid.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:if>
<ww:else>
<div class="page-header">
  <h5>添加交换APPID</h5>
</div>
<form class="form-horizontal form-validate" id="addDataexSysAppid_form" method="post" action="addDataexSysAppid.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:else>
	<div layoutH="84">
	<div style="float:left; margin-left: 23%">
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>名称</label>
		<div class="controls">
			<ww:if test="item.uuid == null">
	       		<input type="text" placeholder="请输入Appid名称" class="input-large required " name="appidName" id="appidName" value="<ww:property value='item.appidName'/>" maxlength="50"> 
	    	</ww:if>
			<ww:else>
				<input type="hidden" placeholder=""  class="input-large " name="uuid" id="uuid" value="<ww:property value='item.uuid'/>"  maxlength="50">
				<span>&nbsp;<ww:property value='item.appidName'/></span>
			</ww:else>			
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>编码</label>
		<div class="controls">
	       	<input type="text" placeholder="请输入Appid编码" class="input-large required " name="appidCode" id="appidCode" value="<ww:property value='item.appidCode'/>" maxlength="50"> 
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01">备注</label>
		<div class="controls">
	       	<input type="text"  class="input-large" name="remark" id="remark" value="<ww:property value='item.remark'/>" maxlength="50"> 
		</div>
	</div>
    </div>
    </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200" style="margin-left: 40%;>
		<button class="btn save" type="button" onclick="$(this).submit();">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeTab();">取消</a>
	</div>
</form>
