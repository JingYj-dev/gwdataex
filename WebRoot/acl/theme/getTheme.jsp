<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal form-validate" method="post"  action="<ww:if test="item.uuid != null">updTheme.action</ww:if><ww:else>addTheme.action</ww:else>" onsubmit="return validateCallback(this, dialogAjaxDone);">
  <input type="hidden" name="uuid" id="uuid" value="<ww:property value='item.uuid'/>">
  <div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>主题编码</label>
		<div class="controls">
      	<input type="text" maxlength="20"  placeholder="请输入主题编码"  class="input-large required" name="code" id="code"  value="<ww:property value='item.code'/>">
    	</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>主题名称</label>
		<div class="controls">
			<input type="text" placeholder="请输入主题名称" class="input-large required" name="name" id="name" value="<ww:property value='item.name'/>" maxlength="50">
		</div>
	</div>
	<!--  
	<div class="control-group">
		<label class="control-label" for="input01">图片地址</label>
		<div class="controls">
			<input type="text" placeholder="请输入图片地址" class="input-large " name="picUrl" id="picUrl" value="<ww:property value='item.picUrl'/>" maxlength="255">
		</div>
	</div>
	-->
	 <div class="control-group">
			<label class="control-label">所属类别<span class="required"></span></label>
			<div class="controls">
		            <select name="typeId" id="typeId"  class="input-large required">
		        		<ww:iterator value="#dictID.getDictType('theme_type')">
		        			<option value="<ww:property value="code"/>" 
		        				<ww:if test="code == item.typeId">selected</ww:if>><ww:property value="name"/></option>
		        		</ww:iterator>
				  </select>
			</div>
      </div>
	<div class="control-group">
		<label class="control-label" for="input01">开启状态</label>
		<div class="controls">
			<label class="radio inline"><input type="radio" name="openFlag" value="1" <ww:if test='item.openFlag == "1" or item.openFlag == null '>checked</ww:if>/>开启</label>
	        <label class="radio inline"><input type="radio" name="openFlag" value="2" <ww:if test='item.openFlag == "2"'>checked</ww:if>/>关闭</label>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01">主题序号</label>
		<div class="controls">
      	<input type="text" maxlength="10"  placeholder="请输入数字"  class="input-large" name="orderNum" id="orderNum"  value="<ww:property value='item.orderNum'/>">
    	</div>
	</div>
	<div class="control-group">
      <label class="control-label">备注<span class="required"></span></label>
      <div class="controls">
        <div class="textarea">
          <textarea name="remark" id="remark" class="input-large" maxlength="20"><ww:property value='item.remark'/></textarea>
        </div>
      </div>
    </div>
  <div class="set-btn" data-spy="affix" data-offset-top="200">
   <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  </div>
</form>