var ENTER = 13;
function loginReady(){
	var flag = $("#login_skin").attr("href");
	var	theme_name_pre = flag.substring(0,flag.lastIndexOf("/"));
	var theme_name = theme_name_pre.substring(theme_name_pre.lastIndexOf("/")+1);
	if(theme_name != "metro"){
	if(theme_name == "skyblue"){
			wh_skyblue();
			$(window).resize(function(){
				wh_skyblue();
			})
		}else if(theme_name == "chinastyle"){
			wh_chinastyle();
			$(window).resize(function(){
				wh_chinastyle();
			})
		}else if(theme_name == "crystal"){
			wh_crystal();
			$(window).resize(function(){
				wh_crystal();
			})
		}
		$('#login-form').validate({
		focusInvalid: true, 
		ignore: "",
//	   		 onkeyup: false,
	
		errorPlacement: function (label, element) { 
		$('<div class="msg"></div>').insertAfter(element.parent()).append(label);
		$(element).next('i').attr('class','invalid');
		},
	
		success: function (label, element) {
			$(element).next('i').attr('class','valid');
		},
	
		submitHandler: function (form) {
		}
	
		}); 
	
		$('#name').keyup(onKeyUp)
	
		$('#password').keyup(onKeyUp);
	
		$('#code').keyup(onKeyUp);
	}else{
	//	document.body.style = "height:100%;overflow:hidden;display:none;";
		 mod_950_w();
		
		$(window).resize(function(){
			mod_950_w();
		})
		$('#login-form').validate({
			focusInvalid: true, 
			ignore: "",
//		    onkeyup: false,
			
			errorPlacement: function (label, element) { 
			$('<div class="msg"></div>').insertAfter(element.parent()).append(label);
			$(element).next('i').attr('class','invalid');
		},
		
		success: function (label, element) {
			$(element).next('i').attr('class','valid');
		},
		
		submitHandler: function (form) {
		}
		
		}); 
		
		$('#name').keyup(onKeyUp)
		
		$('#password').keyup(onKeyUp);
		
		$('#code').keyup(onKeyUp);
	}
	
	
	
}
function wh_chinastyle(){
	document.getElementById("main").style.marginTop = ( document.documentElement.clientHeight - 650 ) / 2 + "px";
}
function wh_skyblue(){
	document.getElementById("main").style.marginTop = ( document.documentElement.clientHeight - 530 ) / 2 + "px";
}
function wh_crystal(){
	document.getElementById("main").style.marginTop = ( document.documentElement.clientHeight - 760 ) / 2 + "px";
}
// 处理回车事件提交表单
function onKeyUp(event){
	if(event.keyCode==ENTER){
		if($('#name').val() && $('#password').val() && $('#code').val())
			{				 
				$(this).parents('form').submit();
			}
	}
}

// 计算图标位置
function mod_950_w(){
	
	setTimeout(function(){
		var isIE = false;
		isIE = !! navigator.userAgent.match(/MSIE/);
		var h,w;
		if(isIE) {
			h = document.documentElement.clientHeight;
			w = document.documentElement.clientWidth;
		} else {
			h = window.innerHeight;
			w = window.innerWidth;
		}
		document.getElementById("mod_l").style.width = 400 + "px";
		document.getElementById("mod_r").style.width = w -400 + "px";
		document.getElementById("mod_l").style.height = "500px";
		document.getElementById("mod_r").style.height = "500px";
		document.getElementById("main").style.padding = ( h -500 ) / 2 + "px 0";
		document.body.style.display = "block";
	},0);
}

// 刷新验证码
function newImg(){
	$('#checkCodeImg')[0].src='rand.jsp?tSessionId='+new Date().valueOf();
}

function validateCallback(form, callback) {
	var $form = $(form);
	if (!$form.valid()) {
		return false;
	}
	$('#pwd').val(md5($('#password').val()));
	$.ajax({
		type: form.method || 'POST',
		url:$form.attr("action"),
		data:$form.serializeArray(),
		dataType:"json",
		cache: false,
		success: callback
	});
	return false;
}

function loginCallback(json){
	if(json.result==0){
		document.location.href="explorer.jsp";
	}else if(json.result==1){
		alert(json.msg);
	}else if(json.result==2){
		window.location="getUpdPassword.action";
	}
}
function loginReset(){
	$('#login-form input').val('');
}