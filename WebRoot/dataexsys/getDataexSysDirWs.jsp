<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal form-validate" id="DataexSysDirWs_form" method="post" name="DataexSysDirWs_form" action="<ww:if test="item.wsId != null">updDataexSysDirWs.action</ww:if><ww:else>addDataexSysDirWs.action</ww:else>" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<input type="hidden" id="wsId" name="wsId" value="<ww:property value='item.wsId'/>"/>
	<input type="hidden" id="dirId" name="dirId" value="<ww:property value='item.dirId'/>"/>
	<div class="control-group">
	    <label class="control-label"><span class="required">*</span>方法名称</label>
	    <div class="controls">
				<input type="text" placeholder="请输入方法名称"  class="input-xlarge required" name="methodName" id="methodName" value="<ww:property value='item.methodName'/>"  maxlength="50">			
	    </div>
	</div>
	  <div class="control-group">
	    <label class="control-label"><span class="required">*</span>服务地址</label>
	    <div class="controls">
	       <input type="text" placeholder="请输入数据服务地址"  class="input-xlarge required url" name="dataServiceUrl" id="dataServiceUrl" value="<ww:property value='item.dataServiceUrl'/>"  maxlength="300">
	    </div>
	  </div>
	<div class="control-group">
	    <label class="control-label">
	    	<span class="required">*</span>接口类型
	    </label>

   			<div class="controls">
		            <select name="wsType" id="wsType"  class="input-small required">
		        		<ww:iterator value="#dictID.getDictType('dataex_sys_wsType')">
		        			<option value="<ww:property value="code"/>" 
		        				<ww:if test="code == item.wsType">selected</ww:if>><ww:property value="name"/></option>
		        		</ww:iterator>
				  </select>
			</div>
 	 </div>
	 <div class="control-group">
		    <label class="control-label"><span>&nbsp;</span><span>&nbsp;</span>备注</label>
		    <div class="controls">
	          <div class="textarea">
		        <textarea class="input-xlarge" name="memo" id="memo" maxlength="120"><ww:property value='item.memo'/></textarea>
		      </div>
		    </div>
	 </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
			<button class="btn green" type="button" onclick="$(this).submit();">保存</button>
			<a class="btn" href="javascript:;" onclick="$css.closeDialog()" >取消</a>
	</div>
</form>