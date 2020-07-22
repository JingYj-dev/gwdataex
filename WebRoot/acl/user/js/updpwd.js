    var pwdLevel = $("#pwdLevel").val();
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
    var loginStatus = 1;
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
    	if($("#newPassword").val() != ""){
    		/*var reg=/^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$/;*/
    		var reg = /^(?=.*[0-9].*)(?=.*[~`!@#$%^&*()_+\'{}<>?:,.;-=\"].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,16}$/;
    	    if(!reg.test($("#newPassword").val())){
        		$("#newPasswordShowEm").html("口令必须为大小写英文字母、数字、字符的组合，并且长度不少于8位!");
        		password4 = false;
        	}else{
        		$("#newPasswordShowEm").html("");
        		password4 = true;
    	    }
    	}
    	if($("#repeatNewPassword").val() != ""){
    		onRepeatNewPasswordMouseMove();
    	}
    	if(loginStatus == 2){
    		hiddenTable();
    	}
    	$("#hiddenTable").attr("style","");
    	if (pwd==null||pwd==''){ 
    	}else { 
    		S_level=checkStrong(pwd); 
    		switch(S_level) { 
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
    		$("#repeatPasswordShow1").attr("style","color:#ff0000;margin-top: -27px;margin-left: 290px; margin-right: 10px;");
    		password6 = false;
    	} else {
    		$("#repeatPasswordShow1").attr("style","color:#ff0000;margin-top: -27px;margin-left: 290px; margin-right: 10px;display:none;");
    		password6 = true;
        }
    	}
    }
    function submit(){
    	//debugger;
    	if(password6 == true && password1 == true && password2 == true && password3 == true && password4 == true && password5 == true && password7 == true &&
    			$("#password").val() != "" && $("#newPassword").val() != "" && $("#repeatNewPassword").val() != ""){
    			var url = "updSafesPassword.action";
    			var password = md5($("#password").val());
    			var newPassword = md5($("#newPassword").val());
    			var repeatNewPassword = md5($("#repeatNewPassword").val());
    			var parms = {'pwdLevel':pwdLevel,'loginName':$("#loginName").val(), 'password':password, 'newPassword':newPassword ,'repeatNewPassword':repeatNewPassword};
    			$css.post(url, parms,
    				function(data){
    					if(data.result!=0){ 
    						  //$("#password").val("");
    						 // $("#newPassword").val("");
    						  //$("#repeatNewPassword").val("");
    						 // $("#newPasswordShowEm").html("");
    						 // $("#PasswordShowEm").html("");
    						 // $("#repeatPasswordShow1").attr("style","color:#ff0000;margin-top: -27px;margin-left: 300px; margin-right: 10px;display:none;");
    						  $css.alert(data.msg);
    					}else{
    						alert(data.msg);
    						document.location.href="login.jsp";
    					}
    				},'json');
    			}else{
    				loginStatus = 2;
        			if($("#newPasswordShowEm").html() != ""){
    					$("#newPassword").focus();
    				}else if( $("#PasswordShowEm").html() != ""){
    					$("#Password").focus();
    				}else{
    					$("#repeatNewPassword").focus();
    				}
    			}
    }
    function reset(){
    	$("#password").val("");
    	  $("#newPassword").val("");
    	  $("#repeatNewPassword").val("");
    }
    function hiddenTable(){
    	if($("#newPassword").val().length > 16){
    		$("#newPasswordShowEm").html("密码长度是16位以内！");
    		password1 = false;
    	}else{
    		password1 = true;
    		$("#newPasswordShowEm").html("");
    		if(S_level < pwdLevel){
        		var msg = "密码强度是中以上！";
        		if(pwdLevel == 2) msg="密码强度必须是中度以上！";
        		if(pwdLevel == 3) msg="密码强度必须是强度！";
        		password5 = false;
        		$("#newPasswordShowEm").html(msg);
        	}else{
        		$("#newPasswordShowEm").html("");
        		$("#hiddenTable").hide();
        		password5 = true;
        	}
    	}
    	if($("#newPassword").val() != "" && $("#password").val() != ""){
			if($("#newPassword").val() == $("#password").val()){
				$("#newPasswordShowEm").html("新密码与原密码不能相同！");
				password3 = false;
	    	}else{
	    		password3 = true;
	    	}
			//var reg=/^[A-Za-z0-9~`!@#$%^&*()_]+$/;
		    //var reg=/^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$/;
		    var reg = /^(?=.*[0-9].*)(?=.*[~`!@#$%^&*()_+\'{}<>?:,.;-=\"].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,16}$/;
		    if(!reg.test($("#newPassword").val())){
	    		$("#newPasswordShowEm").html("口令必须为大小写英文字母、数字、字符的组合，并且长度不少于8位!");
	    		password4 = false;
	    	}else{
	    		password4 = true;
		    }
		}
    }
    function onPasswordMouseMove(){
    	if(password6 == true && password1 == true && password3 == true && password4 == true && password5 == true){
	    	$("#newPasswordShowEm").html("");
	    }
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
    	//debugger;
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
    	$("#repeatPasswordShow1").attr("style","color:#ff0000;margin-top: -27px;margin-left: 290px; margin-right: 10px;display:none;");
		password6 = true;
    }
    function passwordfocus(){
    	$('#PasswordShowEm').html("");
		password7 = true;
    }