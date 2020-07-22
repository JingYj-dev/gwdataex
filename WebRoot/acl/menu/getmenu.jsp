<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal form-validate" id="add_menu" name="add_menu" method="post" action="<ww:if test="item.menuId!= null">updMenu.action</ww:if><ww:else>addMenu.action</ww:else>" onsubmit="<ww:if test="item.menuId!= null">return validateCallback(this, updMenu);</ww:if><ww:else>return validateCallback(this, addMenu);</ww:else>">
   
   <input type="hidden" id="parentId" name="parentId" value="<ww:property value='item.parentId'/>"/>
   <input type="hidden" id="menuId" name="menuId" value="<ww:property value='item.menuId'/>"/>
   <div class="control-group">
    <label class="control-label"><span class="required">*</span>菜单名称</label>
    <div class="controls">
       <input type="text"  class="input-xlarge required" placeholder="请输入菜单名称" name="menuName" id="menuName" value="<ww:property value='item.menuName'/>"  maxlength="20">
    </div>
  </div>
   <div class="control-group">
		<label class="control-label" for="input01">功能编码</label>
		<div class="controls">
			<%--<input type="hidden"  name="getmenu_selFunc" id="getmenu_selFunc" value="<ww:property value="item.funcId"/>" />
			--%><input type="hidden"  name="getmenu_selSys" id="getmenu_selSys" value="<ww:property value="item.sysId"/>" />
			<input type="text" placeholder="请选择功能编码" name="getmenu_selFunc" class="input-large" id="getmenu_selFunc" value="<ww:property value='item.funcId'/>"  onclick="getmenu_showMenu();"
				 readonly style="cursor:pointer;width:270px; background-color:white;"/>
				
				 <i id="removeTree" <ww:if test="item.funcId == null">style="cursor:pointer;margin-left: -20px; margin-right: 9px;margin-left: -25px\0;margin-right:0px\0;background-image:url(cssui/plugins/bootstrap/img/input-closeicon.png)\0;display: none;"</ww:if>
				 <ww:else>style="cursor:pointer;margin-left: -20px; margin-right: 9px;margin-left: -25px\0;margin-right:0px\0;background-image:url(cssui/plugins/bootstrap/img/input-closeicon.png)\0;"</ww:else> title="清空" class="icon-remove" onclick="resetInputTree(this)" for="getmenu_selFunc"></i>
			<a id="getmenu_menuBtn" href="#" onclick="getuser_showMenu(); return false;"></a>
				<div id="getmenu_menuContent" class="menuContent" style="display:none; position: absolute;">
				<ul id="getmenu_tree" class="ztree" style="margin-top:0; width:180px; height: 260px;background:white;border:#e5e7ee solid 1px;overflow:scroll;"></ul>
			</div>
		</div>
	</div>
  <div class="control-group">
    <label class="control-label">菜单地址</label>
    <div class="controls">
       <input type="text"  class="input-xlarge"  placeholder="请输入菜单地址" name="url" id="url" value="<ww:property value='item.url'/>"  maxlength="150">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label"><span class="required">*</span>菜单图标</label>
    <div class="controls iconspage">
    <i  id="icon-i-setting" class="<ww:property value='item.icon'/>" />&nbsp;&nbsp;<span id="icon-span"><ww:property value='item.icon'/></span>
    <input type="hidden" class="input-xlarge required" id="icon" name="icon" value="<ww:property value='item.icon'/>"/>
    &nbsp;&nbsp;<a   css="btn" href="javascript:;"  id="choose_icon_link">选择</a>
    <%-- <input type="text"  class="input-xlarge required"  placeholder="请输入菜单图标" name="icon" id="icon" value="<ww:property value='item.icon'/>"  maxlength="20">--%>    
 </div>
  </div>
  <div class="control-group">
      <label class="control-label" ><span class="required">*</span>设置状态</label>
      <div class="controls">
	      <ww:iterator value="#dictID.getDictType('d_openflag')">
	           <label class="radio inline">
	          <input type="radio"  value="<ww:property value='code'/>" <ww:if test="item.openFlag==code">checked="checked"</ww:if> id="openFlag" name="openFlag"><ww:property value="name"/></label>
	      </ww:iterator>
      </div>
    </div>
    <div class="control-group">
	    <label class="control-label"><span class="required">*</span>菜单序号</label>
		    <div class="controls">
		       <input type="text"  class="input-xlarge required digits" onkeyup="value=this.value.replace(/\D+/g,'')"  placeholder="请输入数字" name="orderNum" id="orderNum" value="<ww:property value='item.orderNum'/>"  maxlength="150">
	    </div>
  </div>
   <div class="set-btn" data-spy="affix" data-offset-top="200">
    <button class="btn green" type="button" onclick="$(this).submit();">保存</button>
    <a class="btn" href="javascript:;" onclick="$css.closeDialog()">取消</a>
  </div>
</form>
<script type="text/javascript" src="acl/menu/js/menu.js"></script>
<script>

</script>