<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:if test="item.uuid!= null">
<form class="form-horizontal form-validate" id="organ_upd_form" method="post" action="updOrg.action"  onsubmit="return validateCallback(this, updOrg);">
</ww:if>
<ww:else>
<form class="form-horizontal form-validate" id="organ_add_form" method="post" action="addOrg.action"  onsubmit="return validateCallback(this, addOrg);">
</ww:else>	
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
	<input type="hidden" id="parentId" name="parentId" value="<ww:property value='item.parentId'/>"/>
	<input type="hidden" id="orderNum" name="orderNum" value="<ww:property value='item.orderNum'/>"/>
	        <div class="control-group">
      <label class="control-label" for="input02"><span class="required">*</span>组织编码</label>
      <div class="controls">
        <input type="text" placeholder="不超过8个数字或字母"    class="input-large required alphanumeric" name="Code" id="Code" value="<ww:property value='item.Code'/>" maxlength="8">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label" for="input01"><span class="required">*</span>组织名称</label>
      <div class="controls">
        <input type="text" placeholder="不超过20个字"  class="input-large required" name="name" id="name" value="<ww:property value='item.name'/>" maxlength="20">
      </div>
    </div>
     <div class="control-group">
      <label class="control-label"><span class="required">*</span>设置状态</label>
      <div class="controls">
	      <ww:iterator value="#dictID.getDictType('d_openflag')">
	           <label class="radio inline">
	          <input type="radio"  value="<ww:property value='code'/>" <ww:if test="item.openFlag==code">checked="checked"</ww:if> id="openFlag" name="openFlag"><ww:property value="name"/></label>
	      </ww:iterator>
      </div>
    </div>
   <div class="set-btn" data-spy="affix" data-offset-top="200">
    <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
    </div>
</form> 
<script>
	function addOrg(data,tabid){
	   dialogAjaxDone(data);
	   if(data.result == 0 ){
	       var newNodes = [{"id":data.info.uuid,"name":data.info.name,"pId":data.info.parentId}];
	       var node = rf_tree.getNodeByParam("id",$("#parentId").val(), null);
	       rf_tree.addNodes(node, newNodes);
	       var $form=$('#div_org').find("#org_form");
	       $form.submit();	
	   }
	}
	function updOrg(data,tabid){
		dialogAjaxDone(data);
		if(data.result == 0){
	       var updnode = rf_tree.getNodeByParam("id",data.info.uuid, null);
	       updnode.name=data.info.name;
	       rf_tree.updateNode(updnode);
	       var $form=$('#div_org').find("#org_form");
	       $form.submit();	
		}

	}
</script>