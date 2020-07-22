<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<ww:bean name="'com.hnjz.apps.base.dict.service.DictMan'" id="dictID" />
<ww:bean name="'com.hnjz.apps.base.org.action.OrgItem'" id="orgItem" />
	<input type="hidden" id="pwdLevelJs" name="pwdLevelJs" value="<ww:property value='pwdLevel'/>"/>
	<input type="hidden" id="realpwd" name="realpwd" value="<ww:property value='#session.sUser.password'/>"/>
	<input type="hidden" id="realreviewpwd" name="realreviewpwd" value="<ww:property value='#session.sUser.reviewPwd'/>"/>
	<div layoutH="84">
		<div style="float:center;">
			<div id="accountSafeDiv">
				<div class="page-header" onclick="hideDivloginPwdDiv();">
					<a title="修改登录密码" href="javascript:;" style="text-decoration: inherit;color: #000000;">修改登录密码</a>
				</div>
				<div id="loginPwdDiv" style="display:none;">
				<form class="form-horizontal form-validate" id="updPasswordSetting_form" method="post" action="updPasswordSetting.action"  onsubmit="return validateCallback(this, updPassword);">
					<input type="hidden" id="pwdLevel" name="pwdLevel" value="<ww:property value='pwdLevel'/>"/>
					<input type="hidden" id="newPasswordTr" name="newPasswordTr" />
					<input type="hidden" id="repeatNewPasswordTr" name="repeatNewPasswordTr" />
					<input type="hidden" id="passwordTr" name="passwordTr" />
					<div class="control-group">
						<label class="control-label" ><span class="required">*</span>原密码</label>
						<div class="controls">
							<input type="password" placeholder="请输入原密码"  class="input-large required" onfocus="passwordfocus()" onblur="passwordblur()" id="password" name="password" value="" onKeyUp="onPasswordMouseMove();" maxlength="50"/>
							<font id="PasswordShowEm" color="red"></font>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="required">*</span>新密码</label>
						<div class="controls">
							<input type="password" placeholder="请输入6-16位新密码" value="" class="input-large required" id="newPassword" name="newPassword" maxlength="50"
                          			onkeyup="pwStrength(this.value)" onblur="hiddenTable()" onfocus="pwStrength(this.value)"> 
                          			<font id="newPasswordShowEm" color="red" style="left: 16px;position: relative;"></font>
                            <table  style="display:none" id="hiddenTable" border="0" cellpadding="0" cellspacing="0" class="passmsg">
                              <tr> 
                                <td id="strength_L" class="l">弱</td>
                                <td id="strength_M" class="m">中</td> 
                                <td id="strength_H" class="h">强</td> 
                              </tr>
                            </table>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="required">*</span>确认密码</label>
						<div class="controls">
							<input type="password" placeholder="请再次输入新密码" class="input-large required" value="" name="repeatNewPassword" maxlength="50"
											 id="repeatNewPassword" onKeyUp="hiddenTable()" onblur="onRepeatNewPasswordMouseMove();" onfocus="repeatNewPasswordfocus()">
											&nbsp;&nbsp;&nbsp;&nbsp;<font id="repeatPasswordShow1" color="red"></font>
											<%--<div id="repeatPasswordShow1" style="color:#ff0000;margin-top: -25px;margin-left: 240px; margin-right: 10px;">
                          						</div>
						--%></div>
					</div>
					 
					<div class="set-btn" data-spy="affix" data-offset-top="200">
						<%--<button id="saveButton" class="btn save" type="submit">保存</button>
						--%>
						<a class="save btn" href="javascript:;" onclick="submitPwd()" style="border: 0;background-color: #317caf;height: 21px;width: 26px">保存</a>
						
						
					</div>
					</form>
				</div>
				
				<!-- 这是设置签章密码的领域  --start--- -->
				<div class="page-header" style="display:none;" onclick="hideDivreviewPwdDiv();">
					<a title="修改签章密码" href="javascript:;" style="text-decoration: inherit;color: #000000;">修改签章密码</a>
				</div>
				<div id="reviewPwdDiv" style="display:none;">
				<form class="form-horizontal form-validateRe" id="updReviewPwdSetting_form" method="post" action="updReviewPwdSetting.action"  onsubmit="return validateCallback(this, updReviewPwd)">
					<input type="hidden" id="pwdLevel" name="pwdLevel" value="<ww:property value='pwdLevel'/>"/>
					<input type="hidden" id="reviewPwdHidden" name="reviewPwdHidden" value="<ww:property value='item.reviewPwd'/>" />
					<input type="hidden" id="reviewPwdTr" name="reviewPwdTr" />
					<input type="hidden" id="newReviewPwdTr" name="newReviewPwdTr" />
					<input type="hidden" id="repeatNewReviewPwdTr" name="repeatNewReviewPwdTr" />
					<div class="control-group">
						<label class="control-label" ><span class="required">*</span>原密码</label>
						<div class="controls">
							<input type="password" placeholder="请输入原密码" class="input-large required" onfocus="reviewpasswordfocus()" onblur="reviewpasswordblur()" name="reviewPwd" id="reviewPwd" value="" maxlength="50" onKeyUp="onNewReviewPwdMouseMove();"/><!--  class="input-large required" -->
							<font id="reviewPasswordShowEm" color="red"></font>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="required">*</span>新密码</label>
						<div class="controls">
							<input type="password" placeholder="请输入6-16位新密码" name="newReviewPwd"  class="input-large required" maxlength="50"
								id="newReviewPwd" value="" onblur="hiddenReviewTable()" onkeyup="pwReviewStrength(this.value)"
								onfocus="pwReviewStrength(this.value)">
								<font id="newReviewPasswordShowEm" color="red" style="left: 16px;position: relative;"></font>
								<%--<div class="editpass_n flo_l"><em class="err" id="newReviewPasswordShowEm"></em></div>
								<div class="clear"></div>
							--%><%--<div id="newReviewPasswordShowEm"></div>
                            --%><table  style="display:none" id="hiddenReviewTable" border="0" cellpadding="0" cellspacing="0" class="passmsg"> 
                              <tr> 
                                <td id="strength_L1" class="l">弱</td>
                                <td id="strength_M1" class="m">中</td> 
                                <td id="strength_H1" class="h">强</td> 
                              </tr>
                            </table>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="required">*</span>确认密码</label>
						<div class="controls">
							<input type="password" placeholder="请再次输入新密码" class="input-large required" name="repeatNewReviewPwd" maxlength="50"
								id="repeatNewReviewPwd" value="" onKeyUp="hiddenReviewTable()" onblur="onRepeatNewReviewPwdMouseMove();" onfocus="repeatNewReviewPasswordfocus()">
								&nbsp;&nbsp;&nbsp;&nbsp;<font id="repeatNewReviewPwdFont" color="red"></font>
								<%--<div id="repeatNewReviewPwdFont" style="color:#ff0000;margin-top: -25px;margin-left: 240px; margin-right: 10px;">
                          						</div>
						--%></div>
					</div>
					<div class="set-btn" data-spy="affix" data-offset-top="200">
						<%--<button id="saveButton" class="btn save" type="submit">保存</button>
						--%>
						<a class="save btn" href="javascript:;" onclick="submitRe()" style="border: 0;background-color: #317caf;height: 21px;width: 26px">保存</a>
					</div>
					</form>
				</div>
				
				<div id="setReviewPwdDiv" style="display:none">
				<form class="form-horizontal form-validateNre" id="updReviewPwdSetting_form" method="post" action="updReviewPwdSetting.action"  onsubmit="return validateCallback(this, updReviewPwd)">
					<input type="hidden" id="reviewPwdHidden" name="reviewPwdHidden" value="<ww:property value='item.reviewPwd'/>" />
					<input type="hidden" id="pwdLevel" name="pwdLevel" value="<ww:property value='pwdLevel'/>"/>
					<input type="hidden" id="newReviewPwdTr" name="newReviewPwdTr" />
					<input type="hidden" id="repeatNewReviewPwdTr" name="repeatNewReviewPwdTr" />
					<div class="control-group">
						<label class="control-label" for="input01"><span class="required">*</span>设置密码</label>
						<div class="controls">
							<%--<input type="password" placeholder="请输入6-16位新密码" name="newReviewPwd" class="input-large" id="newReviewPwd" maxlength="50"
                          			onkeyup="pwStrength(this.value)" onblur="hiddenTable()"onfocus="pwStrength(this.value)">
							--%><input type="password" placeholder="请输入6-16位新密码" name="newReviewPwd" class="input-large required" maxlength="50"
								id="newReviewPwd" value="" onblur="hiddenReviewTable()" onkeyup="pwReviewStrength(this.value)"
								onfocus="pwReviewStrength(this.value)">
								<font id="newReviewPasswordShowEm" color="red" style="left: 16px;position: relative;"></font>
								<%--<div class="editpass_n flo_l"><em class="err" id="newReviewPasswordShowEm"></em></div>
								<div class="clear"></div>
							--%><%--<div id="newReviewPasswordShowEm"></div>
                            --%><table  style="display:none" id="hiddenReviewTable" border="0" cellpadding="0" cellspacing="0" class="passmsg"> 
                              <tr> 
                                <td id="strength_L1" class="l">弱</td>
                                <td id="strength_M1" class="m">中</td> 
                                <td id="strength_H1" class="h">强</td> 
                              </tr>
                            </table>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="input01"><span class="required">*</span>确认密码</label>
						<div class="controls">
							<input type="password" placeholder="请再次输入新密码" name="repeatNewReviewPwd" class="input-large required" maxlength="50"
								id="repeatNewReviewPwd" value="" onblur="hiddenReviewTable()" onKeyUp="onRepeatNewReviewPwdMouseMove();">&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;<font id="repeatNewReviewPwdFont" color="red"></font>
								<%--<div id="repeatNewReviewPwdFont" style="color:#ff0000;margin-top: -25px;margin-left: 240px; margin-right: 10px;">
                          						</div>
						--%></div>
					</div>
					 
					<div class="set-btn" data-spy="affix" data-offset-top="200">
						<%--<button id="saveButton" class="btn save" type="submit">保存</button>
						--%>
						<a class="save btn" href="javascript:;" onclick="submitRe()" style="border: 0;background-color: #317caf;height: 21px;width: 26px">保存</a>
					</div>
					</form>
				</div>
				<!-- 这是设置签章密码的领域  --end--- -->
			</div>
		</div>
		<div class="page-header" onclick="hideDivpersonalData();">
			<a title="修改个人资料" href="javascript:;" style="text-decoration: inherit;color: #000000;">修改个人资料</a>
		</div>
		<form class="form-horizontal form-validatePe" id="updPersonalSetting_form" method="post" action="updPersonalSetting.action"  onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div id="personalData" style="display:;">
			<div class="control-group">
				<label class="control-label" for="input01">真实姓名</label>
				<div class="controls">
					<tr><td><ww:property value='item.realName'/></td></tr>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01">机构名称</label>
				<div class="controls">
					<table>
					<tr><td><ww:property value="#orgItem.getOrg(item.orgId).name"/></td></tr>
					</table>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01">安全级别</label>
				<div class="controls">
					<table>
						<tr><td>
						  <ww:iterator value="#dictID.getDictType('fw_d_security_level')">
				        		<ww:if test="code == item.secLevel">
				        			<ww:property value="name"/>
				        		</ww:if>
				          </ww:iterator>
						</td></tr>
					</table>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01">手机</label>
				<div class="controls">
					<input type="text" placeholder="请输入手机号" class="input-large mobile" name="mobile" maxlength="50"
						id="mobile" value="<ww:property value='item.mobile'/>" onKeyUp="onMobileMouseMove();"/><font id="mobileFont" color="red"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01">电话</label>
				<div class="controls">
					<input type="text" placeholder="请输入电话号码" class="input-large tel" name="phone" maxlength="50"
						id="phone" value="<ww:property value='item.phone'/>" onKeyUp="onPhoneMouseMove();"/><font id="phoneFont" color="red"></font>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="input01">邮箱</label>
				<div class="controls">
					<input type="text" placeholder="请输入邮箱" class="input-large email" name="email" maxlength="50"
						id="email" value="<ww:property value='item.email'/>" onKeyUp="onEmailMouseMove();"/><font id="emailFont" color="red"></font>
				</div>
			</div>
			<div class="set-btn" data-spy="affix" data-offset-top="200">
				<%--<button id="saveButton" class="btn save" type="submit">保存</button>
				--%>
				<a class="save btn" href="javascript:;" onclick="submitPe()" style="border: 0;background-color: #317caf;height: 21px;width: 26px">保存</a>
				
				
			</div>
	    </div>
	   </form>
    </div>
     <%--<div class="set-btn" data-spy="affix" data-offset-top="200">
						<button id="saveButton" class="btn save" type="submit">保存</button>
						
			<a class="save btn" href="javascript:;" onclick="submit1()" style="border: 0;background-color: #317caf;height: 21px;width: 26px">保存</a>
			<a class="btn" href="javascript:;" target="closeTab">取消</a>
						
		</div>
	--%>
<script>
var pwdLevel = $("#pwdLevelJs").val();
if(pwdLevel > 3 || pwdLevel == null || pwdLevel == ''){
	pwdLevel = 3;
}
var password1 = true;
var password2 = true;
var password3 = true;
var password4 = true;
var password5 = true;
var password6 = true;
var password7 = true;
var loginStatusPwd = 1;
var loginStatusRe = 1;
var loginStatusPe = 1;

$(document).ready(function() { 
	$("#loginPwdDivTab").attr("class","active");
    $("#reviewPwdDivTab").attr("class","");
    $("#personalDataTab").attr("class","");
}); 

function updReviewPwd(data){
	//debugger;
	navTabAjaxDone(data);
	if(data.result!=0){ 
		  $("#reviewPwd").val("");
		  $("#newReviewPwd").val("");
		  $("#repeatNewReviewPwd").val("");
		  $("#newReviewPasswordShowEm").html("");
		  $("#repeatNewReviewPwdFont").html("");
		  $css.alert(data.msg);
	}else{
		$css.tip(data.msg);
		$.cssTab.focus().parent().parent().cssTab('closeCurrent');
	}
}
function updPassword(data){
	navTabAjaxDone(data);
	if(data.result!=0){ 
		  $("#password").val("");
		  $("#newPassword").val("");
		  $("#repeatNewPassword").val("");
		  $("#newPasswordShowEm").html("");
		  $("#repeatPasswordShow1").html("");
		  $css.alert(data.msg);
		  
	}else{
		$css.tip(data.msg);
		$.cssTab.focus().parent().parent().cssTab('closeCurrent');
	}
}
function submitPwd(){
	if(password6 == true && password1 == true && password2 == true && password3 == true && password4 == true && password5 == true && password7 == true &&
			$("#password").val() != "" && $("#newPassword").val() != "" && $("#repeatNewPassword").val() != ""){
		var passwordTr = md5($("#password").val());
		var newPasswordTr = md5($("#newPassword").val());
		var repeatNewPasswordTr = md5($("#repeatNewPassword").val());
		var parms = {'pwdLevel':pwdLevel,'passwordTr':passwordTr, 'newPasswordTr':newPasswordTr, 'repeatNewPasswordTr':repeatNewPasswordTr};
		$css.post('updPasswordSetting.action', parms,
			function(data){
				if(data.result!=0){ 
					  $css.alert(data.msg);
				}else{
					alert(data.msg);
					document.location.href="login.jsp";
				}
			},'json');
	}else{
		loginStatusPwd = 2;
		if($("#newPasswordShowEm").html() != ""){
			$("#newPassword").focus();
		}else if($('#PasswordShowEm').html() != ""){
			$("#password").focus();
		}else if($("#repeatPasswordShow1").html() != ""){
			$("#repeatNewPassword").focus();
		}
	}
}

function CharMode(iN){
	if (iN>=48 && iN <=57) return 1; 
	if (iN>=65 && iN <=90) return 2; 
	if (iN>=97 && iN <=122) return 4; 
} 
function bitTotal(num){ 
	modes=0; 
	for (i=0;i<4;i++){
		if (num & 1) modes++; 
		num>>>=1; 
	} 
	return modes; 
} 
function checkStrong(sPW){ 
	if (sPW.length<=9) return 1; 
	Modes=1; 
	for (i=0;i<sPW.length;i++){ 
		Modes|=CharMode(sPW.charCodeAt(i)); 
	} 
	return bitTotal(Modes); 
} 
var  S_level;
function pwStrength(pwd){
	//debugger;
	if($("#newPassword").val() != ""){
		var reg=/^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$/;
	    if(!reg.test($("#newPassword").val())){
    		$("#newPasswordShowEm").html("密码不允许使用空格！");
    		password4 = false;
    	}else{
    		$("#newPasswordShowEm").html("");
    		password4 = true;
	    }
	}
	if($("#repeatNewPassword").val() != ""){
		onRepeatNewPasswordMouseMove();
	}
	if(loginStatusPwd == 2){
		hiddenTable();
	}
	$("#hiddenTable").attr("style","");
	if (pwd==null||pwd==''){ 
		S_level = 0;
	}else {
		S_level=checkStrong(pwd); 
		//alert(S_level); 
		switch(S_level) { 
		case 0: ; 
		case 1: $("#strength_L").addClass("cur"); $("#strength_M").attr("class","m");$("#strength_H").attr("class","h"); 
		break; 
		case 2: $("#strength_M").addClass("cur"); $("#strength_L").attr("class","l");$("#strength_H").attr("class","h"); 
		break; 
		default: 
			$("#strength_H").addClass("cur"); $("#strength_M").attr("class","m");$("#strength_L").attr("class","l"); 
		} 
	}
	return ; 
} 

//新确密码相同提示
function onRepeatNewPasswordMouseMove() {
	if($("#newPassword").val() != "" && $("#repeatNewPassword").val() != ""){
	if ($("#newPassword").val() != $("#repeatNewPassword").val()) {
		$("#repeatPasswordShow1").html("新密码与确认密码不一致");
		password6 = false;
	} else {
		$("#repeatPasswordShow1").html("");
		password6 = true;
	}
	}
}
function hiddenTable(){
	if($("#newPassword").val().length > 16 ){
		$("#newPasswordShowEm").html("密码长度是16位以内！");
		password1 = false;
	}else{
		password1 = true;
		$("#newReviewPasswordShowEm").html("");
		if(S_level < pwdLevel){
    		var msg = "";
    		if(pwdLevel == 2) msg="密码强度是中以上！";
    		if(pwdLevel == 3) msg="密码强度是强以上！";
    		$("#newPasswordShowEm").html(msg);
    		password5 = false;
    	}else{
    		$("#newPasswordShowEm").html("");
    		$("#hiddenTable").hide();
    		password5 = true;
    	}
	}
	if($("#newPassword").val() != ""){
		if($("#newPassword").val() == $("#password").val()){
			$("#newPasswordShowEm").html("新密码与原密码不能相同！");
			password3 = false;
    	}else{
    		password3 = true;
    	}
		var reg=/^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$/;
	    if(!reg.test($("#newPassword").val())){
    		$("#newPasswordShowEm").html("密码不允许使用空格！");
    		password4 = false;
    	}else{
    		password4 = true;
	    }   
	}
}
function onPasswordMouseMove(){
    if($("#password").val() != ""){
    	if($("#newPassword").val() == $("#password").val()){
    		$("#newPasswordShowEm").html("新密码与原密码不能相同！");
    		password2 = false;
    	}else{
    		$("#newPasswordShowEm").html("");
    		password2 = true;
    	}
    }
}

function passwordblur(){
	if($("#password").val() != ""){
	var realpwd = $('#realpwd').val();
	var password = md5(md5($("#password").val()));
	if(realpwd != password){
		$('#PasswordShowEm').html("原密码输入错误！");
		password7 = false;
	}else{
		$('#PasswordShowEm').html("");
		password7 = true;
	}
	}
}
function repeatNewPasswordfocus(){
	$("#repeatPasswordShow1").html("");
	password6 = true;
}
function passwordfocus(){
	$('#PasswordShowEm').html("");
	password7 = true;
}
// 校验登录密码




function submitRe(){
	//debugger;
	if($("#newReviewPasswordShowEm").html() == "" && $("#repeatNewReviewPwdFont").html() == ""){
		if($("#reviewPwdHidden").val() != "") $("#reviewPwdTr").val(md5($("#reviewPwd").val()));
		$("#newReviewPwdTr").val(md5($("#newReviewPwd").val()));
		$("#repeatNewReviewPwdTr").val(md5($("#repeatNewReviewPwd").val()));
		$("#updReviewPwdSetting_form").submit();
	}else{
		loginStatusRe = 2;
		if($("#newReviewPasswordShowEm").html() != ""){
			$("#newReviewPwd").focus();
		}else if($("#repeatNewReviewPwdFont").html() != ""){
			$("#repeatNewReviewPwd").focus();
		}
	}
}
function submitPe(){
	if($("#mobileFont").html() == "" && $("#phoneFont").html() == "" && $("#emailFont").html() == ""){
		$("#updPersonalSetting_form").submit();
	}else{
		loginStatusPe = 2;
		if($("#mobileFont").html() != ""){
			$("#mobile").focus();
		}else if($("#phoneFont").html() != ""){
			$("#phone").focus();
		}else if($("#emailFont").html() != ""){
			$("#email").focus();
		}
	}
}


// 校验登录密码

//校验签章密码
var  S_levelReview;
function pwReviewStrength(pwd){
	if($("#newReviewPwd").val() != ""){
		var reg=/^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$/;
	    if(!reg.test($("#newReviewPwd").val())){
    		$("#newReviewPasswordShowEm").html("密码不允许使用空格！");
    	}else{
    		$("#newReviewPasswordShowEm").html("");
	    }
	}
	if($("#repeatNewReviewPwd").val() != ""){
		onRepeatNewReviewPwdMouseMove();
	}
	if(loginStatusRe == 2){
		hiddenReviewTable();
	}
	$("#hiddenReviewTable").attr("style","");
	if (pwd==null||pwd==''){ 
		S_levelReview = 0;
	}else {
		S_levelReview=checkStrong(pwd); 
		switch(S_levelReview) { 
		case 0:; 
		case 1: $("#strength_L1").addClass("cur"); $("#strength_M1").attr("class","m");$("#strength_H1").attr("class","h"); 
		break; 
		case 2: $("#strength_M1").addClass("cur"); $("#strength_L1").attr("class","l");$("#strength_H1").attr("class","h"); 
		break; 
		default: 
			$("#strength_H1").addClass("cur"); $("#strength_M1").attr("class","m");$("#strength_L1").attr("class","l"); 
		} 
	}
	return ; 
}
function onRepeatNewReviewPwdMouseMove() {
	if($("#newReviewPwd").val() != "" && $("#repeatNewReviewPwd").val() != ""){
	if ($("#newReviewPwd").val() != $("#repeatNewReviewPwd").val()) {
		$("#repeatNewReviewPwdFont").html("新密码与确认密码不一致");
	} else {
		$("#repeatNewReviewPwdFont").html("");
	}
	}
}
function hiddenReviewTable(){
	if($("#newReviewPwd").val().length > 16){
		$("#newReviewPasswordShowEm").html("密码长度是16位以内！");
	}else{
		$("#newReviewPasswordShowEm").html("");
		if(S_levelReview < pwdLevel){
    		var msg = "";
    		if(pwdLevel == 2) msg="密码强度是中以上！";
    		if(pwdLevel == 3) msg="密码强度是强以上！";
    		$("#newReviewPasswordShowEm").html(msg);
    	}else{
    		$("#newReviewPasswordShowEm").html("");
    		$("#hiddenReviewTable").hide();
    	}
	}
	if($("#newReviewPwd").val() != ""){
		if($("#newReviewPwd").val() == $("#reviewPwd").val()){
			$("#newReviewPasswordShowEm").html("新密码与原密码不能相同！");
    	}
		var reg=/^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$/;
	    if(!reg.test($("#newReviewPwd").val())){
			$("#newReviewPasswordShowEm").html("密码不允许使用空格！");
		}
	}
	
}
function onNewReviewPwdMouseMove(){
    if($("#newReviewPwd").val() != ""){
    	if($("#newReviewPwd").val() == $("#reviewPwd").val()){
    		$("#newReviewPasswordShowEm").html("新密码与原密码不能相同！");
    	}else{
    		$("#newReviewPasswordShowEm").html("");
    	}
    }
}

function reviewpasswordblur(){
	//debugger;
	if($("#reviewPwd").val() != ""){
	var realreviewpwd = $('#realreviewpwd').val();
	var reviewPwd = md5(md5($("#reviewPwd").val()));
	if(realreviewpwd != reviewPwd){
		$('#reviewPasswordShowEm').html("原密码输入错误！");
	}else{
		$('#reviewPasswordShowEm').html("");
	}
	}
}
function repeatNewReviewPasswordfocus(){
	$("#repeatNewReviewPwdFont").html("");
}
function reviewpasswordfocus(){
	$('#reviewPasswordShowEm').html("");
}

function hideDivloginPwdDiv(){
	 var temp= $("#loginPwdDiv").is(":hidden");//是否隐藏
	    if (temp) {
	    	$("#reviewPwdDiv").hide("fast");
	    	$("#personalData").hide("fast");
	    	$("#loginPwdDiv").show("slow");
	    	//$("#loginPwdDiv").find(":input").addClass("required");
	    	//$("#reviewPwdDiv").find(":input").removeClass("required");
	    } else {
	    	//$("#loginPwdDiv").find(":input").removeClass("required");
	    	$("#loginPwdDiv").hide("fast");
	    }
}
function hideDivpersonalData() {
    var temp= $("#personalData").is(":hidden");//是否隐藏
    if (temp) {
    	$("#reviewPwdDiv").hide("fast");
    	$("#loginPwdDiv").hide("fast");
    	$("#personalData").show("slow");
    	//$("#personalData").find(":input").addClass("required");
    } else {
    	//$("#personalData").find(":input").removeClass("required");
    	$("#personalData").hide("fast");
    }
}
function hideDivreviewPwdDiv(){
	 var temp= $("#reviewPwdDiv").is(":hidden");//是否隐藏
	    if (temp) {
	    	$("#personalData").hide("fast");
	    	$("#loginPwdDiv").hide("fast");
	    	$("#reviewPwdDiv").show("slow");
	    	//$("#loginPwdDiv").find(":input").removeClass("required");
	    	//$("#reviewPwdDiv").find(":input").addClass("required");
	    } else {
	    	//$("#reviewPwdDiv").find(":input").removeClass("required");
	    	$("#reviewPwdDiv").hide("fast");
	    }
}
/* function changeDivContent() {
	if ($("#reviewPwdHidden").val() == "") {
		$("#reviewPwdDiv").html($("#setReviewPwdDiv").html());
		$("#setReviewPwdDiv").html("");
	}
}
changeDivContent(); */
</script>
