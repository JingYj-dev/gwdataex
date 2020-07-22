<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.service.DataexService'" id="dataexService" />
<form class="form-horizontal table-form" name="dataexSysDirWs_form" id="dataexSysDirWs_form" action="dirDataexSysDirWs.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="uuid" id="uuid" value="<ww:property value="uuid"/>"/>
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<input type="hidden" id="checkedId" value=""/>
	<input type="hidden" id="transmitFlag" value=""/>
	<div class="table-header">
	 <div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="oa_dataexsys/addDataexSysDirWs" css="btn" caption="添加" title="添加接口" target="cssDialog"  href="getDataexSysDirWs.action?dirId=%{uuid}">添加</ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="oa_dataexsys/delDataexSysDirWs" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delDataexSysDirWs.action');"></ww:link>
			</div>
	 </div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="80px" order-field="recvTime" class="order">操作</th>
				
				<th order-field="sourceType" class="order">方法名</th>
				<th order-field="sendSysType" class="order">数据服务地址</th>
				<th order-field="recvTime" class="order">接口类型</th>
				<th order-field="sendTime" class="order">备注</th>			
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="index"> 
		  <!-- rel="<ww:property value='uuid'/>" transmitFlag="<ww:property value="transmitType"/>"  -->
			<tr ondblclick="javascript:$('#showD<ww:property value="#index.getIndex()"/>').click();">
				<td class="text-center"><input type="checkbox"  name="ids" value="<ww:property value="wsId" />"/></td>
				<td class="text-center">
					<ww:link funcode="oa_dataexsys/updDataexSysDirWs" caption="修改" target="cssDialog" href="getDataexSysDirWs.action?wsId=%{wsId}"
						 style="display:inline-block;"></ww:link>&nbsp;
					<ww:link caption="删除" funcode="oa_dataexsys/delDataexSysDirWs" onclick="$.cssTable.act('del','delDataexSysDirWs.action?ids=%{wsId}')"  href="javascript:void(0);"></ww:link>
				</td>
			    <td class="text-left"><ww:property value="methodName"/></td>
			    <td class="text-center"><ww:property value="dataServiceUrl"/></td>
				<td class="text-center"><ww:property value="#dictID.getDictType('dataex_sys_wsType', wsType).name" /></td>
				<td class="text-center iconspage"><ww:if test='memo == "" or memo == null'><span>--</span></ww:if>
				<ww:else><span class="expand"><i id="showD<ww:property value="#index.getIndex()"/>" class="icon_arrow_down"></i></span></ww:else></td>
			</tr>
			<tr class="sub">
			<td colspan="10" ><ww:property value="memo"/></td>
			</tr>
		  </ww:iterator>
		</tbody>
	</table>
	<div class="pagination"><ww:property value="page.pageSplit"/></div>
</form>
<script type="text/javascript">
</script>