<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:if test="item.uuid!= null">
<div class="page-header">
  <h5>修改通道</h5>
</div>
<form class="form-horizontal form-validate" id="updMsgChannel_form" method="post" action="updMsgChannel.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:if>
<ww:else>
<div class="page-header">
  <h5>添加通道</h5>
</div>
<form class="form-horizontal form-validate" id="addMsgChannel_form" method="post" action="addMsgChannel.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:else>
	<div layoutH="84">
	<div style="float:left;">
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>通道编号</label>
		<div class="controls">
			<ww:if test="item.uuid == null">
	       		<input type="text" placeholder="请输入通道编号" class="input-large required digits" name="uuid" id="uuid" value="<ww:property value='item.uuid'/>" maxlength="5"> 
	    	</ww:if>
			<ww:else>
				<input type="hidden" placeholder=""  class="input-xlarge required" name="uuid" id="uuid" value="<ww:property value='item.uuid'/>"  maxlength="50">
				<span>&nbsp;<ww:property value='item.uuid'/></span>
			</ww:else>
			
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>通道名称</label>
		<div class="controls">
			<input type="text" placeholder="请输入通道名称" class="input-large required" name="channelName" id="channelName" value="<ww:property value='item.channelName'/>" maxlength="50">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>通道类型</label>
		<div class="controls">
			<ww:select  attributes="class='input-large required'"   name="'channelType'" id="channelType" value="item.channelType" list="#dictID.getDictType('msgcenter_d_channeltype')" listKey="code" listValue="name" hint="-请选择-"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>IP地址</label>
		<div class="controls">
			<input type="text" placeholder="请输入IP地址" class="input-large required ipaddr" name="serverIp" id="serverIp" value="<ww:property value='item.serverIp'/>" maxlength="15">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>端口</label>
		<div class="controls">
			<input type="text" placeholder="请输入端口" class="input-large required digits" name="port" id="port" value="<ww:property value='item.port'/>" maxlength="5">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>服务地址</label>
		<div class="controls">
			<input type="text" placeholder="请输入服务地址" class="input-large required" name="serverUrl" id="serverUrl" value="<ww:property value='item.serverUrl'/>" maxlength="100">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>操作方法</label>
		<div class="controls">
			<input type="text" placeholder="请输入操作方法" class="input-large required" name="operationName" id="operationName" value="<ww:property value='item.operationName'/>" maxlength="100">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>访问账号</label>
		<div class="controls">
			<input type="text" placeholder="请输入访问账号" class="input-large required" name="imAccount" id="imAccount" value="<ww:property value='item.imAccount'/>" maxlength="50">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>访问口令</label>
		<div class="controls">
			<input type="text" placeholder="请输入访问口令" class="input-large required" name="imPassword" id="imPassword" value="<ww:property value='item.imPassword'/>" maxlength="50">
		</div>
	</div>
	<div class="control-group">
      <label class="control-label">通道描述<span class="required"></span></label>
      <div class="controls">
        <div class="textarea">
          <textarea name="remark" id="remark" class="input-large" maxlength="300" style="width: 224px;"><ww:property value='item.remark'/></textarea>
        </div>
      </div>
    </div>
	<div class="control-group">
      <label class="control-label">扩展配置<span class="required"></span></label>
      <div class="controls">
        <div class="textarea">
          <textarea name="confData" id="confData" class="input-large" maxlength="300" style="width: 224px;"><ww:property value='item.confData'/></textarea>
        </div>
      </div>
    </div>
    </div>
    </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<!--<button class="btn green" type="button" onclick="$(this).submit();">保存</button> gdSubmit(this, $('#addMsgChannel_form')); -->
		<button class="btn save" type="button" onclick="$(this).submit();">保存</button>
		<!-- <a class="btn" onclick="$css.closeTab();" href="javascript:;">取消</a> -->
		<a class="btn" href="javascript:;" onclick="$css.closeTab();">取消</a>
	</div>
</form>
