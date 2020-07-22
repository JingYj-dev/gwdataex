<br><%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.webbase.menu.MenuFactory'" id="menuFactory" />
	  <div class="welcome text-center">
	  <div class="txt">欢迎使用电子公文交换系统</div>
	  <div class="welcome_bg"></div>
	  <div class="db_word">
	      <ul>
	      <ww:iterator value="#menuFactory.getUserMenu()" id="data1" status="data1">
			<ww:if test="isLast">
			</ww:if><ww:else>
				<ww:iterator value="menus" id="data2" status="data2">
				<ww:if test="isLast">
					<ww:if test="name == '待办工作'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num1"></i>
				            <span class="txt">待办工作</span>
				          </a> <em id="dbCounts" class="msg"><ww:property value="dbCounts"/></em>
				        </li>
					</ww:if><ww:elseif test="name == '已办工作'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num2"></i>
				            <span class="txt">已办工作</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '我收到的催办'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num3"></i>
				            <span class="txt">催办事项</span>
				          </a>
				          <em class="msg" id="cbCounts"><ww:property value="cbCounts"/></em>
				        </li>
					</ww:elseif><ww:elseif test="name == '发文起草'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num4"></i>
				            <span class="txt">发文起草</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '收文登记'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num5"></i>
				            <span class="txt">收文登记</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '待归档公文'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num7"></i>
				            <span class="txt">公文归档</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '发送公文'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num6"></i>
				            <span class="txt">公文交换</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '发文查询'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num8"></i>
				            <span class="txt">发文查询</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '收文查询'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num9"></i>
				            <span class="txt">收文查询</span>
				          </a>
				        </li>
					</ww:elseif><ww:elseif test="name == '已归档公文'">
						<li>
				          <a href="<ww:property value="path" />" rel="<ww:property value="id" />" target="cssTab" title="<ww:property value="name" />" refresh="true">
				            <i class="num10"></i>
				            <span class="txt">档案查询</span>
				          </a>
				        </li>
					</ww:elseif>
				</ww:if>
				</ww:iterator>
			</ww:else>
		 </ww:iterator>
		 <%--<ww:pc funcode="acl_log/logSizeOut">
		 	<ww:if test="@com.hnjz.apps.base.log.action.LogSizeOut@logSizeOut()">
			<li>
	          <a href="javascript:void(0);"  title="日志预警">
	            <i class="num1"></i>
	            <span class="txt">日志预警</span>
	          </a>
	          <em class="msg">><ww:property value="@com.hnjz.apps.base.log.action.LogSizeOut@warnNum()"/>%</em>
	        </li>
		 	</ww:if>
		 </ww:pc>
		 --%>
	     </ul>
	     <div class="clear"></div>
	  </div>
	  </div>
	  <script type="text/javascript">
		$("#home").initUI();
	  </script>
