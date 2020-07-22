<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<form class="form-horizontal table-form" name="appidform" id="appidform" method="post" action="dirDataexSysAppid.action"  onsubmit="return navTabSearch(this)">
    <input type="hidden" name="page.orderFlag" id="page.orderFlag" class="order-flag" value="<ww:property value='page.orderFlag'/>"> 
	<input type="hidden" name="page.orderString" id="page.orderString" class="order-string" value="<ww:property value='page.orderString'/>">
	<input type="hidden" name="page.pageSize" class="page-size" value="<ww:property value='page.pageSize'/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
	<input type="hidden" name="page.currentPage" class="page-current" value="<ww:property value='page.currentPage'/>">
	<div class="table-header">
		<div class="table-search">
			<ul>
				<li><span>名称：</span></span><input class="input-medium" type="text" id="appidName" name="appidName" maxlength="20" placeholder="请输入appid名称" value="<ww:property value='appidName'/>"></li>
				<li><span>&nbsp;&nbsp;编码：</span><input class="input-medium" type="text" id="appidCode" name="appidCode" maxlength="20" placeholder="请输入appid编码" value="<ww:property value='appidCode'/>"></li>
			    <li>
			    	<ww:button css="btn green" caption="查询" funcode="oa_dataexsysnode/dirDataexSysAppid" type="submit"></ww:button>
			    </li>
			</ul>
		</div>
		<div class="table-btn">
			<div class="btn-group">
				<ww:link funcode="oa_dataexsysnode/addDataexSysAppid" css="btn" caption="添加" title="添加交换appid" target="cssTab" rel="addDataexSysAppid" href="getDataexSysAppid.action"></ww:link>
			</div>
			<div class="btn-group">
				<ww:link funcode="oa_dataexsysnode/delDataexSysAppid" css="btn" caption="批量删除" onclick="$.cssTable.act('delBatch','delDataexSysAppid.action');"></ww:link>
			</div>
		</div>
	</div>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="100px">操作</th>
				<th width="150px" order-field="appidName" class="order">名称</th>
				<th width="80px" order-field="appidCode" class="order">编码</th>
				<th width="200px" order-field="remark" class="order">备注</th>
				<th width="150px" order-field="createdTime" class="order">创建时间</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="page.results" id="data" status="data">
			<tr>
				<td class="text-center"><input type="checkbox"  name="ids" id="ids" value="<ww:property value="uuid" />"/></td>
				<td class="text-center">
					<ww:link funcode="oa_dataexsysnode/updDataexSysAppid" caption="修改" title="%{appidName}" target="cssTab" rel="getDataexSysAppid-%{uuid}" href="getDataexSysAppid.action?uuid=%{uuid}" ></ww:link>&nbsp;
					<ww:link funcode="oa_dataexsysnode/delDataexSysAppid" caption="删除" onclick="$.cssTable.act('del','delDataexSysAppid.action?ids=%{uuid}')" href="javascript:;"></ww:link>&nbsp;
				</td>
				<td class="text-left"><ww:property value="appidName"/></td>
				<td class="text-left"><ww:property value="appidCode"/></td>
				<td class="text-left"><ww:property value="remark"/></td>
				<td><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(createdTime)"/></td>
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

});
</script>
