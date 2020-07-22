<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<!--  <div style="width:400px">-->
<ww:if test="item.uuid!= null">
<form class="form-horizontal form-validate" id="func_add_form" method="post" action="updFunc.action" onsubmit="return validateCallback(this, updFunc);">
</ww:if>
<ww:else>
<form class="form-horizontal form-validate" id="func_add_form" method="post" action="addFunc.action" onsubmit="return validateCallback(this, addFunc);">
</ww:else>	
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
	<input type="hidden" id="parentId" name="parentId" value="<ww:property value='item.parentId'/>"/>
	<input type="hidden" id="sysId" name="sysId" value="<ww:property value='item.sysId'/>"/>
	<input type="hidden" id="orderNum" name="orderNum" value="<ww:property value='item.orderNum'/>"/>
    <ww:if test="item.parentId !=item.sysId">
    <div class="control-group">
      <label class="control-label">上级功能<span class="required">&nbsp;</span></label>
      <div class="controls">
           <input type="text" class="input-large" value="<ww:property value='parentFuncName'/>" readonly/>
      </div>
    </div>
    </ww:if>
    <div class="control-group">
      <label class="control-label"><span class="required">*</span>功能代码</label>
      <div class="controls">
         <input type="text" placeholder="请输入功能代码" class="input-large required" name="funcId" id="funcId1" value="<ww:property value='item.funcId'/>" maxlength="30">
      </div>
    </div>
    
    
    <div class="control-group">
      <label class="control-label" for="input01"><span class="required">*</span>功能名称</label>
      <div class="controls">
        <input type="text" placeholder="请输入功能名称" class="input-large required" name="name" id="name1" maxlength="25" value="<ww:property value='item.name'/>">
      </div>
    </div>
    
    <ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
		<div class="control-group">
			<label class="control-label">功能类型<span class="required"></span></label>
			<div class="controls">
				<ww:iterator value="#dictID.getDictType('d_functype')">
					<label class="radio inline"> <input type="radio" value="<ww:property value='code'/>" <ww:if test="item.funcType==code">checked="checked"</ww:if> id="funcType" name="funcType"><ww:property value="name" /></label>
				</ww:iterator>
			</div>
		</div>
	</ww:if>
	<ww:else>
		<input type="hidden" id="funcType" name="funcType" value="<ww:property value='item.funcType'/>"/>
	</ww:else>
   
    <!-- 
    <div class="control-group">
      <label class="control-label"><span class="required">&nbsp;</span>操作类型</label>
      <div class="controls">
         <ww:select attributes="class='input-large'" name="'operType'" id="operType" value='item.operType' list="#dictID.getDictType('d_opertype')" listKey="code" listValue="name" />
      </div>
    </div>
    
    
    <div class="control-group">
      <label class="control-label"><span class="required">&nbsp;</span>日志级别</label>
      <div class="controls">
         <ww:select attributes="class='input-large'" name="'logLevel'" id="logLevel" value='item.logLevel' list="#dictID.getDictType('d_loglevel')" listKey="code" listValue="name" />
      </div>
    </div>
     -->
    <div class="control-group">
      <label class="control-label"><span class="required">&nbsp;</span>功能描述</label>
      <div class="controls">
        <div class="textarea">
          <textarea name="remark" id="remark" class="input-large"><ww:property value='item.remark'/></textarea>
        </div>
      </div>
    </div>
    <div class="set-btn" data-spy="affix" data-offset-top="200">
	    <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
	    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  	</div>
</form>
<script>
	function addFunc(data,tabid){
	   dialogNoCloseAjaxDone(data);
       var newNodes = [{"id":data.info.uuid,"name":data.info.name,"pId":data.info.parentId}];
       var node = func_tree.getNodeByParam("id",$("#parentId").val(), null);
       func_tree.addNodes(node, newNodes);
       var $form=$('#div_func').find("#func_form");
       $form.submit();	
       $("#funcId").val("");
       $("#name").val("");
       $("#operType").val("1");
       $("#logLevel").val("1");
       $("#remark").val("");
	}
	function updFunc(data,tabid){
	   dialogAjaxDone(data);
       var updnode = func_tree.getNodeByParam("id",data.info.uuid, null);
       updnode.name=data.info.name;
       func_tree.updateNode(updnode);
       var $form=$('#div_func').find("#func_form");
       $form.submit();	
	}
</script>
