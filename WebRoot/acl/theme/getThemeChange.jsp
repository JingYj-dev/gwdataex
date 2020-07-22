<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:plugin name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.theme.service.ThemeService'" id="themeService" />
    
<div class="float_win" style="width:516px;">
 <div class="skin_con">
  <form class="form-horizontal table-form" name="themeChange_form" id="themeChange_form" action="changeTheme.action"  onsubmit="return validateCallback(this, dialogAjaxDone);">
    <input type="hidden" class="cssPath" value=""/>
    <input type="hidden" class="cssPath" value=""/>
    <input type="hidden" id="code" name="code" value="<ww:property value="code"/>">
     <input type="hidden" id="typeId" name="typeId" value="<ww:property value="typeId"/>">
	<input type="hidden" name="page.totalPages" class="page-count" value="<ww:property value='page.totalPages'/>">
   	<ww:if test="#dictID.getDictType('theme_type').size() > 0">
    <div class="table_list_tab">
   	<ww:iterator value="#dictID.getDictType('theme_type')" id="data" status="data">
   	<a href="javascript:void(0);" onclick="obtainTheme(<ww:property value="code"/>,this);" class="<ww:if test="code.equals(theme.typeId)">active</ww:if><ww:else></ww:else>"><ww:property value="name"/></a>
  	 </ww:iterator>
  	</div>
  	</ww:if>
  <div class="breviary" style="height:250px">
   	<ww:include page="getThemeChangeMini.jsp"/>
  </div>
  <div class="set-btn" data-spy="affix" data-offset-top="200">
   <button class="btn green" id="saveT" type="button" onclick="saveTheme();">保存</button>
    <a class="btn" href="javascript:;" onclick="cancleTheme();">取消</a>
  </div>
  </form>
  </div>
</div>
<script>
	/*$('.ui_close',$($css.focusDialog()).parent().parent()).click(function(){
	    cancleTheme();
	})*/
	$(document).ready(function(){
		var len = <ww:property value="#dictID.getDictType('theme_type').size()"/>;
		var len2 = <ww:property value="#themeService.getThemeCount()"/>
		if(len == 0 && len2 == 0){
			$('#saveT').css('display','none');
		}
	})
	if($.dialog.focus){
		$($.dialog.focus.DOM.close).click(function(){
		    cancleTheme();
		});
	}
	function obtainTheme(typeId,item){
		$("#typeId").val(typeId);
		$(item).parent().children().each(function(index,t){
			$(t).removeClass("active");
		})
		$(item).addClass("active");
		$css.post('getThemeChange.action',{'typeId':typeId},function(data){
			$(".breviary").html(data);
		},'html');
		//$(item).addClass("active");
	}
	function changeTheme(url){
		$("#code").val(url.substring(url.lastIndexOf('/')+1));
		if($(".current").length){
			var str = $(".current")[0].href;
			if(str.substring(0,str.lastIndexOf('/')) == url){
				$(".current")[0].className = "temp";
				$(".current")[0].className = "temp";
				return;
			}else{
				$(".cssPath")[0].value = $(".current")[0].href;
				$(".cssPath")[1].value = $(".current")[1].href;
				$(".current")[0].href = url+"/icons.css";
				$(".current")[1].href = url+"/style.css";
				$(".current")[0].className = "temp";
				$(".current")[0].className = "temp";
			}
		}else{
			$(".temp")[0].href = url+"/icons.css";
			$(".temp")[1].href = url+"/style.css";
		}
	}
	function cancleTheme(){
		if($(".temp").length){
			$(".temp")[0].href = $(".cssPath")[0].value;
			$(".temp")[1].href = $(".cssPath")[1].value;
			$(".temp")[0].className = "current";
			$(".temp")[0].className = "current";
		}
		$css.closeDialog();
	}
	function saveTheme(){
		if($(".temp").length){
			$(".temp")[0].className = "current";
			$(".temp")[0].className = "current";
		}
		$("form").submit()
	}
	function nextPage(currentPage){
          $css.post('getThemeChange.action',{'page.currentPage':currentPage,'typeId':$("#typeId").val(),'flag':'flgSplit'},function(data){
  			$(".breviary").html(data);
  		},'html');
	}
</script>
