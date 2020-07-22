<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="form1" id="form1" method="post" action="dirDataexSysNode.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="exnodeName" name="exnodeName" maxlength="10" placeholder="请输入节点名称" value="<ww:property value='exnodeName'/>"></li>
				<li>节点类型：<ww:select  attributes="class='input-medium required'"   name="'exnodeType'" id="exnodeType" value="exnodeType" list="#dictID.getDictListQuery('dataex_sys_dirType')" listKey="code" listValue="name"></ww:select> </li>
			    <li>节点状态：<ww:select  attributes="class='input-small required'"   name="'openFlag'" id="openFlag" value="openFlag" list="#dictID.getDictListQuery('d_openflag')" listKey="code" listValue="name"></ww:select> </li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="oa_dataexsysnode/dirDataexSysNode" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="oa_dataexsysnode/addDataexSysNode" css="btn" caption="添加" title="添加交换节点" target="cssTab" rel="addDataexSysNode" href="getDataexSysNode.action"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="oa_dataexsysnode/delDataexSysNode" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delDataexSysNode.action');"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:dropdown funcode="oa_dataexsysnode/updDataexSysNodeStatus" value="model" list="#dictID.getDictType('d_openflag')" listKey="name" caption="设置状态" onclick="$.cssTable.act('batch','updDataexSysNodeStatus.action?openFlag=%{#model.code}')" />  
		    </div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="150px">操作</th>
				<th width="150px" order-field="exnodeName" class="order">节点名称</th>
				<th width="150px" order-field="exnodeType" class="order">节点类型</th>
				<th width="150px" order-field="exnodeStatus" class="order">节点状态</th>
				<th width="150px" order-field="createdTime" class="order">创建时间</th>
				<%--
				
				<th >证书</th>
				 --%>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="exnodeId" />"/></td>
				<td class="text-center">
					<ww:link funcode="oa_dataexsysnode/updDataexSysNode" caption="修改" title="%{exnodeName}" target="cssTab" rel="getDataexSysNode-%{exnodeId}" href="getDataexSysNode.action?exnodeId=%{exnodeId}" ></ww:link>&nbsp;
					<ww:link funcode="oa_dataexsysnode/delDataexSysNode" caption="删除" onclick="$.cssTable.act('del','delDataexSysNode.action?ids=%{exnodeId}')" href="javascript:;"></ww:link>&nbsp;
					<ww:link caption="接口" title="%{exnodeName}-接口" funcode="oa_dataexsysnode/dirDataexSysDirWs" target="cssTab" rel="dirDataexSysDirWs-%{exnodeId}" href="dirDataexSysDirWs.action?uuid=%{exnodeId}"></ww:link>
				</td>
				<td class="text-left"><ww:property value="exnodeName"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_dirType', exnodeType).name"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('d_openflag', exnodeStatus).name"/></td>
				<td><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(createdTime)"/></td>
				<%--
				<td class="text-center">
					<div class="uploadCertFlag" id="head<ww:property value='#index.index+1'/>">
						<input type="hidden" name="uuid" value="<ww:property value="exnodeId" />" /> 
						<ww:if test="certPath == null or certPath == ''">
		   			 	<a class="fileUpload">上传</a>
		   			 	<a class="fileDownload" href="downloadDirCert.action?uuid=<ww:property value='exnodeId'/>&attach=head" style="display:none;">下载</a>							
						<a class="fileDel" id="fileDel<ww:property value='#index.index+1'/>" href="javascript:delAttach('fileDel<ww:property value='#index.index+1'/>','<ww:property value="exnodeId"/>','delDirCert.action?attach=head','head')" style="display:none;">删除</a>
		   			 	</ww:if>	  
		   			 	<ww:else>
		   			 	<a class="fileUpload" style="display:none">上传</a>
		   			 	<a class="fileDownload" href="downloadDirCert.action?uuid=<ww:property value='exnodeId'/>&attach=head">下载</a>							
						<a class="fileDel" id="fileDel<ww:property value='#index.index+1'/>" href="javascript:delAttach('fileDel<ww:property value='#index.index+1'/>','<ww:property value="exnodeId"/>','delDirCert.action?attach=head','head')">删除</a>
		   			 	</ww:else>		   
	   		 		</div>
				</td>
				
				 --%>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
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
