<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:if test="item.appId!= null">
<div class="page-header">
  <h5>修改应用</h5>
</div>
<form class="form-horizontal form-validate" id="updMsgApp_form" method="post" action="updMsgApp.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:if>
<ww:else>
<div class="page-header">
  <h5>添加应用</h5>
</div>
<form class="form-horizontal form-validate" id="addMsgApp_form" method="post" action="addMsgApp.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:else>
	<div layoutH="84">
	<div style="float:left;">
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>应用编码</label>
		<div class="controls">
			<ww:if test="item.appId == null">
	       		<input type="text" placeholder="请输入应用编码" class="input-large required" name="appCode" id="appCode" value="<ww:property value='item.appCode'/>" maxlength="20"> 
	    	</ww:if>
			<ww:else>
				<input type="hidden" placeholder=""  class="input-xlarge required" name="appId" id="appId" value="<ww:property value='item.appId'/>">
				<input type="hidden" placeholder=""  class="input-xlarge required" name="appCode" id="appCode" value="<ww:property value='item.appCode'/>">
				<span>&nbsp;<ww:property value='item.appCode'/></span>
			</ww:else>
			
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>应用名称</label>
		<div class="controls">
			<input type="text" placeholder="请输入应用名称" class="input-large required" name="appName" id="appName" value="<ww:property value='item.appName'/>" maxlength="50">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>IP地址</label>
		<div class="controls">
			<input type="text" placeholder="请输入IP地址" class="input-large required ipaddr" name="appSysIp" id="appSysIp" value="<ww:property value='item.appSysIp'/>" maxlength="15">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>授权码</label>
		<div class="controls">
			<input type="text" placeholder="请输入授权码" class="input-large required" name="licenseCode" id="licenseCode" value="<ww:property value='item.licenseCode'/>" maxlength="100">
		</div>
	</div>
	<div class="control-group">
      <label class="control-label">模块描述<span class="required"></span></label>
      <div class="controls">
        <div class="textarea">
          <textarea name="memo" id="memo" class="input-large" maxlength="500" style="width: 224px;"><ww:property value='item.memo'/></textarea>
        </div>
      </div>
    </div>
    </div>
    </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn save" type="button" onclick="$(this).submit();">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeTab();">取消</a>
	</div>
</form>
