<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:if test="item.exnodeId!= null">
<div class="page-header">
  <h5>修改交换节点</h5>
</div>
<form class="form-horizontal form-validate" id="updDataexSysNode_form" method="post" action="updDataexSysNode.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:if>
<ww:else>
<div class="page-header">
  <h5>添加交换节点</h5>
</div>
<form class="form-horizontal form-validate" id="addDataexSysNode_form" method="post" action="addDataexSysNode.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
</ww:else>
	<div layoutH="84">
	<div style="float:left;">
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>节点名称</label>
		<div class="controls">
			<ww:if test="item.exnodeId == null">
	       		<input type="text" placeholder="请输入节点名称" class="input-large required " name="exnodeName" id="exnodeName" value="<ww:property value='item.exnodeName'/>" maxlength="50"> 
	    	</ww:if>
			<ww:else>
				<input type="hidden" placeholder=""  class="input-large " name="exnodeId" id="exnodeId" value="<ww:property value='item.exnodeId'/>"  maxlength="50">
				<span>&nbsp;<ww:property value='item.exnodeName'/></span>
			</ww:else>			
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>节点类型</label>
		<div class="controls">
			<ww:select  attributes="class='input-large required'"   name="'exnodeType'" id="exnodeType" value="item.exnodeType" list="#dictID.getDictType('dataex_sys_dirType')" listKey="code" listValue="name" hint="-请选择-"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>节点状态</label>
		<div class="controls">
			<ww:select  attributes="class='input-large required'"   name="'exnodeStatus'" id="exnodeStatus" value="item.exnodeStatus" list="#dictID.getDictType('d_openflag')" listKey="code" listValue="name" hint="-请选择-"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01">签名</label>
		<div class="controls">
			<ww:select  attributes="class='input-large'" name="'signature'" id="signature" value="item.signature" list="#dictID.getDictType('dataex_sys_signature')" listKey="code" listValue="name" hint="-请选择-"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01">加密</label>
		<div class="controls">
			<ww:select  attributes="class='input-large'" name="'encryption'" id="encryption" value="item.encryption" list="#dictID.getDictType('dataex_sys_encryption')" listKey="code" listValue="name" hint="-请选择-"/>
		</div>
	</div>
    </div>
    </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn save" type="button" onclick="$(this).submit();">保存</button>
		<a class="btn" href="javascript:;" onclick="$css.closeTab();">取消</a>
	</div>
</form>
