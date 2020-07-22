<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysHelper'" id="exnodeHelper" />
<form class="form-horizontal table-form"  name="dataexSysDir_form" id="dataexSysDir_form" action="dirDataexSysDirMini.action"  onsubmit="return divSearch(this,'div_dataexSysDir')">
    <input type="hidden" name="dataexSysDirId" id="dataexSysDirId" value="<ww:property value='dataexSysDirId'/>">
	<input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">	
			<ul>
				<li><input type="text" id="dirName" name="dirName" class="input-medium" placeholder="请输入机构名称" value="<ww:property value='dirName'/>" maxlength="30"></li>
				<%--
				<li><input type="text" id="dirIp" name="dirIp" class="input-medium" placeholder="请输入节点IP" value="<ww:property value='dirIp'/>" maxlength="15"></li>
				<li><input type="text" id="dirProvider" name="dirProvider" class="input-medium" placeholder="请输入提供商名称" value="<ww:property value='dirProvider'/>" maxlength="30"></li>
				<li>类型：<ww:select attributes="class='input-small'" name="'dirType'" id="dirType" value="dirType"  list="#dictID.getDictListQuery('dataex_sys_dirType')" listKey="code" listValue="name"></ww:select> </li>
				 --%>
				<li>状态：<ww:select attributes="class='input-small'" name="'dirStatus'" id="dirStatus" value="dirStatus"  list="#dictID.getDictListQuery('dataex_sys_dirStatus')" listKey="code" listValue="name"></ww:select> </li>
                <li>创建时间：<input class="input-small Wdate" id="beginCreatedTime" name="beginCreatedTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endCreatedTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(beginCreatedTime)"/>'> 至 <input class="input-small Wdate" id="endCreatedTime" name="endCreatedTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginCreatedTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endCreatedTime)"/>'></li>
				<li><ww:button css="btn green" caption="查询" funcode="oa_dataexsys/dirDataexSysDirMini" type="submit"></ww:button></li>
                <li><ww:button css="btn reset" caption="清空" funcode="oa_dataexsys/dirDataexSysDirMini" type="button" onclick="resetDirForm();"></ww:button></li>
			</ul>
			<%--
			<br/>
			<ul>
				<li>签名：<ww:select attributes="class='input-small'" name="'signature'" id="signature" value="signature"  list="#dictID.getDictListQuery('dataex_sys_signature')" listKey="code" listValue="name"></ww:select> </li>
				<li>加密：<ww:select attributes="class='input-small'" name="'encryption'" id="encryption" value="encryption"  list="#dictID.getDictListQuery('dataex_sys_encryption')" listKey="code" listValue="name"></ww:select> </li>
                
			</ul>
			 --%>
		</div>
	    <div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="oa_dataexsys/addDataexSysDir" css="btn" caption="添加" title="添加节点" target="cssTab" rel="addDataexSysDir" href="getDataexSysDir.action?defaultPId=%{dataexSysDirId}"></ww:link>
			</div>
			<div class="btn-group">
				 <ww:link funcode="oa_dataexsys/delDataexSysDir" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch',{callback:delDataexSysDir,url:'delDataexSysDir.action'});"  href="javascript:;"></ww:link>
			</div>
			 <div class="btn-group">
			    <ww:dropdown funcode="oa_dataexsys/updDirStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updDirStatus.action?openStatus=%{#model.code}')" />  
		    </div>
			<div class="btn-group btnTab" style="float:right;">
				<input type="hidden" name="includeFlag" id="includeFlag" value="<ww:property value='includeFlag'/>">
				<a class="btn" value="1" href="javascript:;">当前组织</a>
				<a class="btn" value="2" href="javascript:;">包含下级</a>
			</div> 
		</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="100px">操作</th>  
				<th order-field="dirName" class="order">机构名称</th>
				<th width="100px" order-field="dirOrg" class="order">机构编码</th>
				<%--<th width="100px" order-field="dirIp" class="order">节点IP</th>
				<th order-field="dirProvider" class="order">提供商名称</th> --%>
				<%--<th order-field="dirOrg" class="order">所属机构</th> 
				<th width="140px" order-field="createdTime" class="order">创建时间</th>--%>
				<%--<th width="30px" order-field="signature" class="order">签名</th>
				<th width="30px" order-field="encryption" class="order">加密</th>--%>
				<th width="150px" >关联交换节点</th>
				<th width="150px" >关联交换APPID</th>
				<%--<th width="70px" order-field="dirType" class="order">交换节点类型</th> --%>
				<!-- <th width="150px" order-field="createdTime" class="order">创建时间</th> -->
				<th width="50px" order-field="dirStatus" class="order">状态</th>
				<%--<th width="90px">证书</th> --%>
			</tr>
		</thead>
		<tbody>
		    <ww:iterator value="page.results" id="data"  status="index"> 
			<tr>
				<td class="text-center"><input type="checkbox" name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
					<ww:link funcode="oa_dataexsys/getDataexSysDir" caption="修改" title="%{dirName}" rel="getDataexSysDir-%{uuid}" target="cssTab" href="getDataexSysDir.action?uuid=%{uuid}"
						 style="display:inline-block;"></ww:link>&nbsp;
						 <ww:if test='parentId == null || parentId == ""'><span style="visibility:hidden;">删除</span></ww:if>
				<ww:else><ww:link caption="删除" funcode="oa_dataexsys/delDataexSysDir" onclick="$.cssTable.act('del',{callback:delDataexSysDir,url:'delDataexSysDir.action?ids=%{uuid}'})"  href="javascript:void(0);"></ww:link></ww:else>&nbsp;
					<%-- <ww:link caption="接口" funcode="oa_dataexsys/dirDataexSysDirWs" target="cssTab" href="dirDataexSysDirWs.action?uuid=%{uuid}"></ww:link> --%>
				</td>
				<td class="text-left"><ww:property value="dirName"/></td>
				<td class="text-center"><ww:property value="dirOrg"/></td>
				<%--<td class="text-center"><ww:property value="dirIp" /></td>
				<td class="text-left"><ww:property value="dirProvider"/></td>--%>
				<%--<td class="text-left"><ww:property value="dirOrg"/></td>
				<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(createdTime)"/></td>--%>
				<%--<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_signature', signature).name"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_encryption', encryption).name"/></td>--%>
				<%--
				<td class="text-center">
					<div class="uploadCertFlag" id="head<ww:property value='#index.index+1'/>">
						<input type="hidden" name="uuid" value="<ww:property value="uuid" />" /> 
						<ww:if test="certPath == null or certPath == ''">
		   			 	<a class="fileUpload">上传</a>
		   			 	<a class="fileDownload" href="downloadDirCert.action?uuid=<ww:property value='uuid'/>&attach=head" style="display:none;">下载</a>							
						<a class="fileDel" id="fileDel<ww:property value='#index.index+1'/>" href="javascript:delAttach('fileDel<ww:property value='#index.index+1'/>','<ww:property value="uuid"/>','delDirCert.action?attach=head','head')" style="display:none;">删除</a>
		   			 	</ww:if>	  
		   			 	<ww:else>
		   			 	<a class="fileUpload" style="display:none">上传</a>
		   			 	<a class="fileDownload" href="downloadDirCert.action?uuid=<ww:property value='uuid'/>&attach=head">下载</a>							
						<a class="fileDel" id="fileDel<ww:property value='#index.index+1'/>" href="javascript:delAttach('fileDel<ww:property value='#index.index+1'/>','<ww:property value="uuid"/>','delDirCert.action?attach=head','head')">删除</a>
		   			 	</ww:else>		   
	   		 		</div>
				</td>
				 --%>
				<td class="text-center"><ww:property value="#exnodeHelper.getExNodeName(exnodeId)"/></td>
				<td class="text-center">
					<ww:property value="#exnodeHelper.getExAppidCode(appid)"/>
				<%-- <ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(createdTime)"/> --%>
				</td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_dirStatus', dirStatus).name"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
	</div>
</form>
<script type="text/javascript" src="acl/common/upload/js/upload.js"></script>
<script>
//单击选中单条记录并为隐藏变量赋值
jQuery(function($) {
	/**添加绑定事件:单击选中行*/
    var $tab = $.cssTab.focus();
    $tab.find('tbody tr').click(function() {
		$tab.find('.listsel').removeClass('listsel');
		$(this).addClass('listsel');
    }); 

    $(".uploadCertFlag").each(function(){
    	var head = $(this,$.cssTab.focus()).attr('id');
    	uploadCert(this,head,$(this).children('input').val());
     });
    
});

function delDataexSysDir(data) {
	if (data.result == 0) {
		for (var i = 0; i < data.info.length; i ++) { 
			var delNode = dir_tree.getNodeByParam("id", data.info[i], null);
			dir_tree.removeNode(delNode);
		}
		$css.tip(data.msg);
		var $form = $('#div_dataexSysDir').find("#dataexSysDir_form");
		$form.submit();
	} else {
		$.dialog.tips(data.msg, 1, 'error.gif');
		//$css.tip(data.msg);
	}
}
function resetDirForm(){
	var $tab = $.cssTab.focus();
	$tab.find(':input','#dataexSysDir_form').not(':button, :submit,:radio, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
	$tab.find('input[type=radio][value=or]').attr('checked','checked');
}

//上传证书
function uploadCert(obj,head,uuid){
	fileUpload(head,uuid,'uploadDirCert.action?attach=head',
			function(data) {
				var tar = head;
				var toUrl='uploadDirCert.action?attach=head';
				var uuid="";
				$css.tip("证书上传成功");
				var aObj = $(obj).children('a:first');
				aObj.hide();
				aObj.next().show();
				aObj.next().next().show();
				//$.cssTab.focus().parent().parent().cssTab('reload',{id:'MLGL'})
				//navTabAjaxQuery(data);
	});
}

function delAttach(obj,uuid,url) {
	delFile(uuid, url, function() {
		$css.tip("证书删除成功");
		$('#'+obj).hide();
		$('#'+obj).prev().hide();
		$('#'+obj).prev().prev().show();
		//navTabAjaxQuery(data);
		//$.cssTab.focus().parent().parent().cssTab('reload',{id:'MLGL'})
	});

}
</script>