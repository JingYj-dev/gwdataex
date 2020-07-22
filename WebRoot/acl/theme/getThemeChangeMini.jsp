<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:plugin name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
    <ul class="con" id="miniPage">
    
     <ww:iterator value="page.results" id="data" status="index">
      <li><a href="javascript:void(0);" onclick="changeTheme('<%=request.getContextPath() %>/cssui/themes/<ww:property value="code"/>');"><img src="<%=request.getContextPath() %>/cssui/themes/<ww:property value="code"/>/images/breviary.jpg" alt="<ww:property value="name"/>"><div class="txtbg"></div><span class="txt"><ww:property value="name"/></span></a></li>
     </ww:iterator>
  
    </ul>
    <div class="page">
   	<ww:if test="pageNums.size() >= 2">
    <ww:iterator value="pageNums" id="data" status="data">
    <ww:if test="page.currentPage == #data.index+1">
    <a href="javascript:void(0);" title="<ww:property value="(#data.index+1)%"/>" class="cur" onclick="nextPage(<ww:property value="#data.index+1"/>);">
    </ww:if>
    <ww:else>
    <a href="javascript:void(0);" title="<ww:property value="(#data.index+1)%"/>" onclick="nextPage(<ww:property value="#data.index+1"/>);">
    </ww:else>
    </ww:iterator>
    </ww:if>
    </div>
    

