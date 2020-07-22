<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<form class="form-horizontal table-form" name="dir_backup_form"  id="dir_backup_form" action="dirDataBackup.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li>
					<input class="input-medium" placeholder="请输入备份名称" type="text" id="backupName" name="backupName" value="<ww:property value='backupName'/>" maxlength="50" />
				</li>
				<li>开始时间：
					<input class="input-small Wdate" id="backupStartTime" name="backupStartTime" readonly="true" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'backupEndTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(backupStartTime)"/>'>
				</li>
				<li>结束时间：
					<input class="input-small Wdate" id="backupEndTime" name="backupEndTime" readonly="true" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'backupStartTime\')}'})" value='<ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd').format(backupEndTime)"/>'>
				</li>
				<li>
			      <ww:button css="btn green" caption="查询" funcode="acl_data_backup/dirDataBackup" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="acl_data_backup/addDataBackup"  target="cssDialog" css="btn" caption="添加" title="添加备份" rel="addbackup" href="acl/backup/addbackup.jsp"></ww:link>
			</div>
			<div class="btn-group">
			    <ww:link funcode="acl_data_backup/delDataBackup" css="btn" caption="批量删除"  onclick="$.cssTable.act('delBatch','delDataBackup.action');"></ww:link>
			</div>
		</div>
	</div>
	
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="50px;"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="70px;">操作</th>
				<th width="150px;" order-field="backupName" class="order">备份名称</th>
				<th width="*" order-field="dataBakPath" class="order">数据备份路径</th>
				<th width="90px;">是否压缩</th>
				<th width="90px;" order-field="backupStatus" class="order">备份状态</th>
				<th width="190px;" order-field="backupStartTime" class="order">开始时间</th>
				<th width="190px;" order-field="backupEndTime" class="order">结束时间</th>
			</tr>
		</thead>
		
		<tbody>
			<ww:iterator value="page.results" id="data"> 
				<tr  rel="<ww:property value='backupId' />">
					<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="backupId" />"/></td>
					<td class="text-center">
					    <ww:link funcode="acl_data_backup/delDataBackup" caption="删除" onclick="$.cssTable.act('del','delDataBackup.action?ids=%{backupId}')" href="javascript:;"></ww:link>&nbsp;
					</td>
					<td class="text-center"><ww:property value="backupName"/></td>
					<td class="text-center"><ww:property value="dataBakPath"/></td>
					<td class="text-center"><ww:property value="#dictID.getDictType('d_truefalse',zipMark).name"/></td>
					<td class="text-center"><ww:property value="#dictID.getDictType('d_backup_status',backupStatus).name"/></td>
					<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(backupStartTime)"/></td>
					<td class="text-center"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(backupEndTime)"/></td>
				</tr>
			</ww:iterator>
		</tbody>
	</table>
	<div class="pagination"> <ww:property value="page.pageSplit"/></div>
</form>
