<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysHelper'" id="exnodeHelper" />
<div class="page-header">
  <h5><ww:if test="uuid != null">修改目录</ww:if><ww:else>添加目录</ww:else></h5>  
</div>

<form class="form-horizontal form-validate"  method="post" action="<ww:if test="item.uuid!=null && !item.uuid.equals(\"\")">updDataexSysDir.action</ww:if><ww:else>addDataexSysDir.action</ww:else>" onsubmit="return validateCallback(this, <ww:if test="item.uuid!=null && !item.uuid.equals(\"\")">updDir</ww:if><ww:else>addDir</ww:else>);">
	<input type="hidden" id="uuid" name="uuid" value="<ww:property value='item.uuid'/>"/>
	<input type="hidden" name="parentId" id="parentId" value="<ww:property value='item.parentId'/>" />
	<div layoutH="84">
		<div style="float:left;">
			<div class="control-group">
				<label class="control-label" for="input01"><span class="required">*</span>目录名称</label>
				<div class="controls">
					<input type="text" placeholder="不超过40个字" class="input-large required" name="dirName" id="dirName" value="<ww:property value='item.dirName'/>" maxlength="40">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01"><span class="required">*</span>机构编码</label>
				<div class="controls">	
					<input type="text" placeholder="不超过40个字" class="input-large required" name="dirOrg" id="dirOrg" value="<ww:property value='item.dirOrg'/>" maxlength="40">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01"><span class="required">*</span>交换节点</label>
				<div class="controls">
					<ww:select attributes="class='input-large required'" name="'exnodeId'" id="exnodeId" value="item.exnodeId" list="#exnodeHelper.getDataexSysNodeListQuery()" listKey="exnodeId" listValue="exnodeName"></ww:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01">交换APPID</label>
				<div class="controls">
					<ww:select attributes="class='input-large'" name="'appid'" id="appid" value="item.appid" list="#exnodeHelper.getDataexSysAppidListQuery()" listKey="uuid" listValue="appidName"></ww:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01"><span class="required">*</span>目录状态</label>
				<div class="controls">
					<ww:select  attributes="class='input-large required'" name="'dirStatus'" id="dirStatus" value="item.dirStatus" list="#dictID.getDictType('dataex_sys_dirStatus')" listKey="code" listValue="name" hint="-请选择-"/>
				</div>
			</div>
			<!-- 修改目录 -->
			<!--  
			<ww:if test="item.uuid!=null && !item.uuid.equals(\"\")">
			<div class="control-group" id="uploadCert" >
	    		<label class="control-label" for="input01">上传证书</label>
				<div class="controls certFlag" id="head">
		   			 <ww:if test="item.certPath == null or item.certPath == ''">
		   			 <a class="fileUpload">上传</a><span><input type="hidden" id="attachmentid" name="attachmentid" value=""/></span>
		   			 <a style="display:none" class="fileDel" href="javascript:delAttach('<ww:property value="item.uuid"/>','delDirCert.action?attach=head','head')">删除</a>
		   			 </ww:if>
		   			 <ww:else>
		   			 <a class="fileUpload" style="display:none;">上传</a><span><input type="hidden" id="attachmentid" name="attachmentid" value=""/></span>
		   			 <a class="fileDel" href="javascript:delAttach('<ww:property value="item.uuid"/>','delDirCert.action?attach=head','head')">删除</a>
		   			 </ww:else>		 
	   		 	</div>
	  		</div>
	  		</ww:if>
	  		-->
	  		<!-- 添加目录 -->
	  		<!--  
	  		<ww:else>
	  			<div class="control-group" id="uploadCert" style="display:none;" >
	    		<label class="control-label" for="input01">上传证书</label>
				<div class="controls certFlag" id="head">
		   			 <ww:if test="certPath == null or certPath == ''">
		   			 <a class="fileUpload">上传</a><span><input type="hidden" id="attachmentid" name="attachmentid" value=""/></span>
		   			 <a style="display:none" class="fileDel" href="javascript:delAttach('<ww:property value="item.uuid"/>','delDirCert.action?attach=head','head')">删除</a>
		   			 </ww:if>
		   			 <ww:else>
		   			 <a class="fileUpload" style="display:none;">上传</a><span><input type="hidden" id="attachmentid" name="attachmentid" value=""/></span>
		   			 <a class="fileDel" href="javascript:delAttach('<ww:property value="item.uuid"/>','delDirCert.action?attach=head','head')">删除</a>
		   			 </ww:else>		 
	   		 	</div>
	  		</div>
	  		</ww:else>
			-->
	    </div>
    </div>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<button class="btn green" type="button" onclick="$(this).submit();">保存</button>
		<a class="btn" href="javascript:;" target="closeTab">取消</a>
	</div>
</form>

<script>
/*
$(function(){
	//修改
	var id = $(".certFlag",$.cssTab.focus()).attr('id')+$.cssTab.focus().attr('id');
	$(".certFlag",$.cssTab.focus()).attr('id',id);
	var uuid = '<ww:property value="item.uuid"/>';
	var head = $(".certFlag",$.cssTab.focus()).attr("id");
	if(uuid != null || uuid == ''){
		uploadCert(head);
	}
});

function delAttach(uuid, url) {
	delFile(uuid, url, function() {
		$css.tip("证书删除成功");
		$(".fileUpload",$.cssTab.focus()).show();
		$(".fileDel",$.cssTab.focus()).hide();
		navTabAjaxQuery(data);
	});
}
*/
function addDir(data,tabid) {
	if(data.result == 0){
		$("#uuid").val(data.info.uuid);
		$("#uploadCert",$.cssTab.focus()).show();
		var head = $(".certFlag",$.cssTab.focus()).attr("id");
		var newNodes = [{"id":data.info.uuid,"name":data.info.dirName,"pId":data.info.parentId}];
	    var node = dir_tree.getNodeByParam("id",data.info.parentId, null);
	    dir_tree.addNodes(node, newNodes);	
	    //$.cssTab.focus().parent().parent().cssTab('reload',{id:'MLGL'});
	    /*
	    var url = "getDataexSysDir.action?uuid="+$("#uuid").val();
		var rel = $("#dirOrg").val()+"_getDataexSysDir";
		$css.closeTab();
		$css.openTab({id:rel, title:'修改', url:url, active:true});
	    uploadCert(head);
	    */
	    navTabAjaxQuery(data);
	} else {
		navTabAjaxQuery(data);
	}
}
/*
function uploadCert(head){

	fileUpload(head,$("#uuid").val(),'uploadDirCert.action?attach=head',
			function(data) {
			if(data.result == 0){
				var tar = 'head';
				var toUrl='uploadDirCert.action?attach=head';
				var uuid="";
				$css.tip("证书上传完成");								
				navTabAjaxQuery(data);
			}else{
				$css.tip(data.msg);
			}
				
		});
}*/
function updDir(data, tabid) {
   if(data.result == 0){
	   var updnode = dir_tree.getNodeByParam("id",data.info.uuid, null);
	   if(updnode!=null){
		    updnode.name = data.info.dirName;
		    dir_tree.updateNode(updnode);
	   }
   }
   navTabAjaxQuery(data);
}
<%--
var getDir_setting = {
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
		async: {
			enable: true,
			url:"getDataexSysDirRadioTree.action",
			autoParam:["id=id"],
			otherParam: ["parentId", '<ww:property value="parentId"/>'],
			type:"post"
		}, 
		callback: {
			beforeClick : getDir_beforeClick,
			onCheck: getDir_onCheck
		}
	};

	var getDir_zNodes = <ww:property value="tree"/>;
	
	function getDir_beforeClick(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("getDir_tree");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	function getDir_onCheck(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("getDir_tree"),
		nodes = zTree.getCheckedNodes(true), v, v1;
		if (nodes.length > 0) {
			v = nodes[0].name;
			v1 = nodes[0].id;
			var objName = $("#getDir_sel");
			var objId = $("#getDir_selId");
			objName.val(v);
			objId.val(v1);
		}
		getDir_hideMenu();
	}
	function getDir_showMenu() {
		$("#getDir_menuContent").slideDown("fast");
		$("#dir_add_form").bind("mousedown", getDir_onBodyDown);	
	}
	function getDir_hideMenu() {
		$("#getDir_menuContent").fadeOut("fast");
		$("#dir_add_form").unbind("mousedown", getDir_onBodyDown);
	}
	function getDir_onBodyDown(event) {
		if (!(event.target.id == "getDir_menuBtn" || event.target.id == "getDir_sel" 
				|| event.target.id == "getDir_menuContent" || $(event.target).parents("#getDir_menuContent").length > 0)) {
			getDir_hideMenu();
		}
	}
	using('tree', function() {
	    $.fn.zTree.init($("#getDir_tree"), getDir_setting, getDir_zNodes);    
	    getDir_onCheck(); //初始化上级目录
	})
--%>
</script>
