<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem'" id="dirItem" />
<ww:bean name="'com.hnjz.apps.oa.dataexsys.util.ServiceUtil'" id="ServiceUtil" />
<form>
<div class="tablesld" style="border:1px solid #ffffff; padding:15px 15px 10px 15px">
  <table border="0" cellspacing="0" cellpadding="0" class="sun_t" style="width:100%;">
    <tr>
        <td align="right" style="width:100px; padding-right:15px;">公文标题</td>
        <td colspan="8"><ww:property value='#ServiceUtil.getTitle(item.docTitle)'/></td>
    </tr>
    <tr> 
        <td align="right" style="width:100px; padding-right:15px;">发文机关</td>
	    <td colspan="3" style="width:100px;"><ww:property value="#dirItem.getDirByDirOrg(item.sendOrg).dirName"/></td>
        <td align="right" style="width:100px;padding-right:15px;">收文机关</td>
	    <td colspan="3" style="width:100px;"><ww:property value='#dirItem.getDirNamesByContentId(item.uuid)'/></td>
    </tr>
    <tr> 
        <td align="right" style="width:100px; padding-right:15px;">密级</td>
	    <td colspan="3" style="width:100px;"><ww:property value='item.docSecurity'/></td>
        <td align="right" style="width:100px;padding-right:15px;">紧急程度</td>
	    <td colspan="3"><ww:property value='item.docEmergency'/></td>
    </tr>
    <tr> 
        <td align="right" style="width:100px; padding-right:15px;">报文类型</td>
	    <td colspan="3" style="width:100px;"><ww:property value='#dictID.getDictType("dataex_sys_msgType",item.packType).name'/></td>
        <td align="right" style="width:100px;padding-right:15px;">报文大小</td>
	    <td colspan="3"><ww:property value='item.packSize'/>(字节)</td>
    </tr>
    <tr> 
        <td align="right" style="width:100px; padding-right:15px;">接收开始时间</td>
	    <td colspan="3" style="width:100px;"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(item.recvStartTime)"/></td>
        <td align="right" style="width:100px;padding-right:15px;">接收完成时间</td>
	    <td colspan="3"><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(item.recvTime)"/></td>
    </tr>
	<tr>
		<td align="right" style="padding-right:15px;">发送完成时间</td>
	    <td colspan="3">
	    <ww:if test='item.sendTime == null'>--</ww:if>
	    <ww:else><ww:property value="new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(item.sendTime)" /></ww:else>&nbsp;
	    </td>
	    <td align="right" style="width:100px;padding-right:15px;">发送系统IP</td>
	    <td colspan="3"><ww:property value="item.clientIp"/></td>
	</tr>
	<tr>
        <td align="right" style="width:100px; padding-right:15px;">交换状态</td>
        <td colspan="8"><ww:property value="#dictID.getDictType('dataex_sys_transtatus', item.transStatus).name"/></td>
    </tr>
    <%--
	<ww:iterator value="docAttachmentList" id="data" status="index">
		<tr>
			<td align="right" style="padding-right:15px;">附件<ww:property value="#index.getIndex() + 1"/></td>
	    	<td colspan="8"><ww:property value="fileName"/></td>
		</tr>
	</ww:iterator>
	 --%>
  </table>
</div>  
 <div class="set-btn" data-spy="affix" data-offset-top="200">
    <a class="btn" href="javascript:;" onclick="$css.closeDialog();">关闭</a>
  </div>
</form>
