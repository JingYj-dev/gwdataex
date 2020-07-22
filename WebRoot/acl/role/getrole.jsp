<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.sys.action.SysItem'" id="sysItemID" />
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<!--  
<div class="page-header">
  <h5><ww:if test="uuid!= null">修改角色</ww:if><ww:else>添加角色</ww:else></h5>
</div>
-->

<form class="form-horizontal form-validate" id="form1" name="form1" method="post" action="<ww:if test="uuid!= null">updRole.action</ww:if><ww:else>addRole.action</ww:else>" onsubmit="return validateCallback(this, dialogAjaxDone);">
<!--  <div layoutH="86">-->
<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
 <div style="height: 180px;">
  <div class="control-group">
    <label class="control-label"><span class="required">*</span>角色名称</label>
    <div class="controls">
      <input type="text" placeholder="不超过20个字"  class="input-large required" name="name" id="name" value="<ww:property value='item.name'/>"  maxlength="20">
<!--     <p class="help-block">最大支持20个字</p> -->   
    </div>
  </div>
  
    <ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
		<div class="control-group">
			<label class="control-label">角色类型<span class="required"></span></label>
			<div class="controls">
				<ww:iterator value="#dictID.getDictType('d_roletype')">
					<label class="radio inline"> <input type="radio" value="<ww:property value='code'/>" <ww:if test="item.roleType==code">checked="checked"</ww:if> id="roleType" name="roleType"><ww:property value="name" /></label>
				</ww:iterator>
			</div>
		</div>
	</ww:if>
	<ww:else>
		<input type="hidden" id="roleType" name="roleType" value="<ww:property value='item.roleType'/>"/>
	</ww:else>
  
  
    <div class="control-group">
      <label class="control-label"><span class="required">*</span>所属系统</label>
      <div class="controls">
            <select name="sysId" id="sysId"  class="input-large required">
            	<option value="">请选择所属系统</option>
        		<ww:iterator value="#sysItemID.getSystems()">
        			<option value="<ww:property value="uuid"/>" 
        				<ww:if test="uuid == item.sysId">selected</ww:if>><ww:property value="name"/></option>
        		</ww:iterator>
		  </select>
      </div>
    </div>
    
    
     <div class="control-group">
      <label class="control-label"><span class="required">*</span>设置状态</label>
      <div class="controls">
	      <ww:iterator value="#dictID.getDictType('d_openflag')">
	           <label class="radio inline">
	          <input type="radio" value="<ww:property value='code'/>" <ww:if test="item.openFlag==code">checked="checked"</ww:if> id="openFlag" name="openFlag"><ww:property value="name"/></label>
	      </ww:iterator>
      </div>
    </div>
    
    
    <div class="control-group">
    <label class="control-label"><span>&nbsp;</span><span>&nbsp;</span>角色描述</label>
    <div class="controls">
      <div class="textarea">
        <textarea class="input-large"  placeholder="不超过60个字" name="remark" id="remark" maxlength="120"><ww:property value='item.remark'/></textarea>
   <!--       <p class="help-block">最大支持120个字</p>  -->
      </div>
    </div>
  </div>
  
  
<!-- </div> -->
     </div>
   <div class="set-btn" data-spy="affix" data-offset-top="200">

   <div align="right">
<%--      <button class="btn green" type="submit">保存</button>--%>
      <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
     <a class="btn" href="javascript:;" onclick="$css.closeDialog()" >取消</a>
    </div>
  </div>

</form>
