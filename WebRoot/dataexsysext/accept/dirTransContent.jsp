<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem'" id="dirItem" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.util.ServiceUtil'" id="ServiceUtil" />
<form class="form-horizontal table-form" name="form1"  id="form1" action="dirAcceptDoc.action"  onsubmit="return navTabSearch(this)">

    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><input class="input-medium" type="text" id="docTitle" name="docTitle" placeholder="公文标题" value="<ww:property value='docTitle'/>"></li>
			    <li>接收时间：<input class="input-small Wdate" id="startRecvTime" name="startRecvTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endRecvTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(startRecvTime)"/>'> 
			    	至 <input class="input-small Wdate" id="endRecvTime" name="endRecvTime" readonly="true" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startRecvTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(endRecvTime)"/>'></li>
			    <li>
			       <ww:button css="btn green" caption="查询" funcode="oa_acceptdoc/dirAcceptDoc" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
<%-- 			<div class="btn-group">
			    <ww:link funcode="oa_acceptdoc/acceptDoc" css="btn" caption="批量签收"  onclick="$.cssTable.act('batch',{url:'acceptDoc.action',title:'你确定要签收这些公文吗？'});"></ww:link>
			</div> --%>
<%-- 		    <div class="btn-group btnTab" style="float:right;">
		    	<input type="hidden" name="acceptStatus" id="acceptStatus" value="<ww:property value='acceptStatus'/>">
				<a class="btn" value="1" href="javascript:;">未接收</a>
				<a class="btn" value="2" href="javascript:;">已接收</a>
			</div> --%>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th style="width: 25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
<!-- 				<th style="width: 80px">操作</th> -->
				<th order-field="b.docTitle" class="order">公文标题</th>
				<th >发文机关</th>
				<th >收文机关</th>
				<th style="width: 80px" order-field="b.docSecurity" class="order">密级</th>
				<th style="width: 80px" order-field="b.docEmergency" class="order">紧急程度</th>
				<th style="width: 120px" order-field="b.recvTime" class="order">接收时间</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data"> 
			<tr  rel="<ww:property value='uuid' />">
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="sendId" />"/></td>
<%-- 				<td class="text-center" nowrap="nowrap">
					<ww:if test="acceptStatus!=null && acceptStatus.equals(\"2\")">
						<a class="fileSection" href="downloadDocOfd.action?uuid=<ww:property value="uuid"/>" >下载</a>
					</ww:if>
					<ww:else>
					    <ww:link funcode="oa_acceptdoc/acceptDoc" caption="签收" onclick="$.cssTable.act('del',{url:'acceptDoc.action?ids=%{sendId}&status=2',title:'你确定要签收这个公文吗？'})" href="javascript:;"></ww:link>
					    
					    <ww:link funcode="oa_acceptdoc/acceptDoc" caption="退回" onclick="$.cssTable.act('del',{url:'acceptDoc.action?ids=%{sendId}&status=1',title:'你确定要退回这个公文吗？'})" href="javascript:;"></ww:link>
					    
					</ww:else>
				</td> --%>
				<td title="<ww:property value="docTitle"/>"><ww:property value="#ServiceUtil.getTitle(docTitle)"/></td>
				<td class="text-center"><ww:property value="#dirItem.getDirByDirOrg(sendOrg).dirName"/></td>
				<td class="text-center"><ww:property value='#dirItem.getDirNamesByContentId(uuid)'/></td>
				<td class="text-center"><ww:property value="docSecurity"/></td>
				<td class="text-center"><ww:property value="docEmergency"/></td>
				<td class="text-center" ><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm').format(recvTime)"/></td>
			</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
</script>
