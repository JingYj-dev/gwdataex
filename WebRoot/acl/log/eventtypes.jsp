<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:set name="types" value="#dictID.getDictListQuery('d_eventtype')"/>

<form class="form-horizontal table-form" style="width:320px" name="send_form"  id="send_form">
	<table class="table table-bordered">
		<thead>
			<tr>
				<th width="25px"><input group="ids" class="cleck-all" type="checkbox"/></th>
				<th width="100px">事件类型</th>
			</tr>
		</thead>
		<tbody>
		  <ww:iterator value="#types" status="index" id="data"> 
			<ww:if test="#index.index!=0">
			<tr>
				<td class="text-center"><input type="checkbox" class="choose" name="ids" id="ids" value="<ww:property value="#types.get(#index.index).getName()"/>,<ww:property value="#types.get(#index.index).getCode()"/>"/></td>
				<td ><ww:property value="#types.get(#index.index).getName()"/></td>
			</tr>
			</ww:if>
			</ww:iterator>
		</tbody>
	</table>
	<div class="set-btn" data-spy="affix" data-offset-top="200">
		<input class="btn green" type="button" value="选中" id="funCheck" />
		<input class="btn green" type="button" value="取消" onclick="$css.closeDialog()"/>
	</div>
</form>
<script type="text/javascript">
 $("#funCheck").click(function(){
     var types="";
     var codes="";
	    $.each($(".choose"),function(i,d){
				if(d.checked==true){
				  types += d.value.split(",")[0]+",";
				  codes += d.value.split(",")[1]+",";
				}
			});
			if($.trim(types)==""){
				$css.alert(titleMap.alertBatch);
			     return false;
			}
			if (types.length > 0 ) types = types.substring(0, types.length-1);
			if (codes.length > 0 ) codes = codes.substring(0, codes.length-1);
			var $tab = $.cssTab.focus();
			$("#eventTypes",$tab).val(types);
			$("#eventCodes",$tab).val(codes);
			$css.closeDialog();
			return false;
			
	});
</script>