<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<!--  <div class="page-header">
 <h5><ww:if test="uuid!= null">修改系统</ww:if><ww:else>添加系统</ww:else></h5> 
</div>
-->
<ww:if test="item.uuid!= null">
<form class="form-horizontal form-validate" id="form1" method="post" name="form1" action="updSys.action" onsubmit="return validateCallback(this, dialogAjaxDone);">
</ww:if>
<ww:else>
<form class="form-horizontal form-validate" id="form1" method="post" name="form1" action="addSys.action" onsubmit="return validateCallback(this, dialogAjaxDone);">
</ww:else>
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
	<div class="control-group">
	    <label class="control-label"><span class="required">*</span>系统编码</label>
	    <div class="controls">
	       <input type="text" placeholder="不超过10个字"  class="input-xlarge required" name="sysId" id="sysId" pattern="[0-9]{1,}" title="请输入数字" value="<ww:property value='item.sysId'/>"  maxlength="10"  <ww:if test="sysId!= null"></ww:if>> 
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label"><span class="required">*</span>系统名称</label>
	    <div class="controls">
	       <input type="text" placeholder="不超过20个字"  class="input-xlarge required" name="name" id="name" value="<ww:property value='item.name'/>"  maxlength="20">
	  
	    </div>
	  </div>
	  <div class="control-group">
	    <label class="control-label"><span class="required">*</span>系统地址</label>
	    <div class="controls">
	      <input type="text" placeholder="请输入URL"  class="input-xlarge required url"  name="url" id="url"   maxlength="250" <ww:if test="uuid!= null"> value="<ww:property value='item.url'/>" </ww:if> <ww:else>value="http://"</ww:else> >
	    </div>
	  </div>
	  
	  
	  <div class="control-group">
	      <label class="control-label"><span class="required">*</span>系统状态</label>
	      <div class="controls">
		      <ww:iterator value="#dictID.getDictType('d_openflag')">
		           <label class="radio inline">
		          <input type="radio"  value="<ww:property value='code'/>" <ww:if test="item.openFlag==code">checked="checked"</ww:if> id="openFlag" name="openFlag"><ww:property value="name"/></label>
		      </ww:iterator>
	      </div>
	    </div>
	    
	    
		 <div class="control-group">
		    <label class="control-label"><span>&nbsp;</span><span>&nbsp;</span>系统描述</label>
		    <div class="controls">
	          <div class="textarea">
		        <textarea   class="input-xlarge" name="remark" id="remark"   maxlength="120"><ww:property value='item.remark'/></textarea>
		      </div>
		    </div>
		  </div>
		  
		  
		<div class="set-btn" data-spy="affix" data-offset-top="200">
<%--			<button class="btn green" type="submit" >保存</button>--%>
			<button class="btn green" type="button" onclick="$(this).submit();">保存</button>
			<a class="btn" href="javascript:;" onclick="$css.closeDialog()" >取消</a>
		</div>
</form>