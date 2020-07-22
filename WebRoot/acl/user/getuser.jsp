<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.org.action.OrgItem'" id="orgItem" />
<style>
.table-bordered tbody tr:nth-child(even) {
 background: none; 
}
</style>
<div class="page-header">
  <h5><ww:if test="uuid!= null">修改用户</ww:if><ww:else>添加用户</ww:else></h5>
</div>
<form class="form-horizontal form-validate" id="user_add_form" method="post" action="<ww:if test="item.uuid!= null">updUser.action</ww:if><ww:else>addUser.action</ww:else>"  onsubmit="return validateCallback(this, navTabAjaxQuery, 'YHGL');">
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
	<input type="hidden" id="openFlag" name="openFlag" value="<ww:property value='item.openFlag'/>"/>
	<div layoutH="84">
	<div style="float:left;">
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>登录名</label><%--
		<div class="controls">
			<input type="text" placeholder="不超过20个字" class="input-large required alphanumeric" name="loginName" id="loginName" value="<ww:property value='item.loginName'/>" maxlength="20">
		</div>
		--%><div class="controls">
		<ww:if test="item.uuid != null">
		<input type="text" maxlength="20"  readonly  placeholder="不超过20个字"  class="input-large required alphanumeric" name="loginName" id="loginName" value="<ww:property value='item.loginName'/>" >
		</ww:if> 
		<ww:else>
      <input type="text" maxlength="20"    placeholder="不超过20个字"  class="input-large required dictcode" name="loginName" id="loginName"  >
		</ww:else>
    </div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>姓名</label>
		<div class="controls">
			<input type="text" placeholder="不超过20个字" class="input-large required" name="realName" id="realName" value="<ww:property value='item.realName'/>" maxlength="20">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01"><span class="required">*</span>机构名称</label>
		<div class="controls">
			<input type="hidden" name="orgId" id="getuser_selId" value="<ww:property value="orgId"/>" />
			<input type="text" name="getuser_sel" class="input-large required" id="getuser_sel" value="<ww:property value="#orgItem.getOrg(orgId).name"/>" onclick="getuser_showMenu();"
				readonly style="cursor:pointer;"/>
			<a id="getuser_menuBtn" href="#" onclick="getuser_showMenu(); return false;"></a><br/>
			<div id="getuser_menuContent" class="menuContent" style="display:none; position: absolute;">
				<ul id="getuser_tree" class="ztree" style="margin-top:0; width:180px; height: 260px;background:white;border:#e5e7ee solid 1px;overflow:scroll;"></ul>
			</div>
		</div>
	</div>
	<!-- 
	<div class="control-group">
		<label class="control-label"><span class="required">*</span>状态</label>
		<div class="controls">
			<ww:iterator value="#dictID.getDictType('d_security_Flag')">
				<label class="radio inline"> <input type="radio" value="<ww:property value='code'/>" <ww:if test="item.openFlag==code">checked="checked"</ww:if> id="openFlag" name="openFlag"><ww:property value="name" /></label>
			</ww:iterator>
		</div>
	</div>
	 -->
	 	<div class="control-group">
			<label class="control-label">安全级别<span class="required"></span></label>
			<div class="controls">
		            <select name="secLevel" id="secLevel"  class="input-small required">
		        		<ww:iterator value="#dictID.getDictType('fw_d_security_level')">
		        			<option value="<ww:property value="code"/>" 
		        				<ww:if test="code == item.secLevel">selected</ww:if>><ww:property value="name"/></option>
		        		</ww:iterator>
				  </select>
			</div>
      </div>
	<div class="control-group">
		<label class="control-label">性别<span class="required"></span></label>
		<div class="controls">
			<ww:iterator value="#dictID.getDictType('d_sex')">
				<label class="radio inline"> <input type="radio" value="<ww:property value='code'/>" <ww:if test="item.sex==code">checked="checked"</ww:if> id="sex" name="sex"><ww:property value="name" /></label>
			</ww:iterator>
		</div>
	</div>

	<ww:if test="@com.hnjz.core.configuration.ConfigurationManager@isAdminMode()">
		<div class="control-group">
			<label class="control-label">用户类型<span class="required"></span></label>
			<div class="controls">
				<ww:iterator value="#dictID.getDictType('d_usertype')">
					<label class="radio inline"> <input type="radio" value="<ww:property value='code'/>" <ww:if test="item.userType==code">checked="checked"</ww:if> id="userType" name="userType"><ww:property value="name" /></label>
				</ww:iterator>
			</div>
		</div>
	</ww:if>
	<ww:else>
		<input type="hidden" id="userType" name="userType" value="<ww:property value='item.userType'/>"/>
	</ww:else><%--
	<div class="control-group">
		<label class="control-label" for="input01">排序号</label>
		<div class="controls">
			<input type="text" placeholder="请输入排序号 "  class="input-large digits "  min="1" name="orderNum" id="orderNum" value="<ww:property value='item.orderNum'/>">
		</div>
	</div>
	--%><div class="control-group">
		<label class="control-label" for="input01">邮箱<span class="required"></span></label>
		<div class="controls">
			<input type="text" placeholder="例如：test.@163.com" maxlength="50" class="input-large email" name="email" id="email" value="<ww:property value='item.email'/>">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01">电话<span class="required"></span></label>
		<div class="controls">
			<input type="text" placeholder="例如：010-1234567 " maxlength="50" class="input-large phone" name="phone" id="phone" value="<ww:property value='item.phone'/>">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="input01">手机<span class="required"></span></label>
		<div class="controls">
			<input type="text" placeholder="例如：12345678900" class="input-large mobile"  maxlength ="50"  name="mobile" id="mobile" value="<ww:property value='item.mobile'/>">
		</div>
	</div>
	<div class="control-group">
      <label class="control-label">备注<span class="required"></span></label>
      <div class="controls">
        <div class="textarea">
          <textarea name="remark" id="remark" class="input-large"><ww:property value='item.remark'/></textarea>
        </div>
      </div>
    </div>
    </div>
    </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="button" onclick="$(this).submit();">保存</button>
		<a class="btn" href="javascript:;" target="closeTab">取消</a>
	</div>
</form>
<script>
var getuser_setting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		<%--async: {
			enable: true,
			url:"getOrgRadioTree.action",
			autoParam:["id=id"],
			otherParam: ["parentId", '<ww:property value="orgId"/>'],
			type:"post"
		},--%>
		callback: {
			beforeClick : getuser_beforeClick,
			onCheck: getuser_onCheck
		}
	};

	var getuser_zNodes =<ww:property value="tree"/>;
	function getuser_beforeClick(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("getuser_tree");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	function getuser_onCheck(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("getuser_tree"),
		nodes = zTree.getCheckedNodes(true),
		v = "",
		v1 = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].name + ",";
			v1 += nodes[i].id + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (v1.length > 0 ) v1 = v1.substring(0, v1.length-1);
		var cityObj = $("#getuser_sel");
		var cityObj1 = $("#getuser_selId");
		//cityObj.attr("value", v);
		//cityObj1.attr("value", v1);
		cityObj.val(v);
		cityObj1.val(v1);
		getuser_hideMenu();
	}
	function getuser_showMenu() {
		$("#getuser_menuContent").slideDown("fast");
		$("#user_add_form").bind("mousedown", getuser_onBodyDown);	
	}
	function getuser_hideMenu() {
		$("#getuser_menuContent").fadeOut("fast");
		$("#user_add_form").unbind("mousedown", getuser_onBodyDown);
	}
	function getuser_onBodyDown(event) {
		if (!(event.target.id == "getuser_menuBtn" || event.target.id == "getuser_sel" 
				|| event.target.id == "getuser_menuContent" || $(event.target).parents("#getuser_menuContent").length>0)) {
			getuser_hideMenu();
		}
	}
	using('tree',function(){
	    $.fn.zTree.init($("#getuser_tree"), getuser_setting,getuser_zNodes);    
	})
</script>
