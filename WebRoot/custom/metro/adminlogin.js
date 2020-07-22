var ENTER = 13;
// 导入jquery
using('jquery',function(){
	
	// 导入 cookie控件
	using('cookie',function(){
	})
	
	// 导入验证控件
	using('validate',function(){
	  $('#login-form').validate({
	    focusInvalid: true, 
	    ignore: "",
	    onkeyup: false,

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
	})
	
	$('#name').keyup(onKeyUp)

	$('#pwd').keyup(onKeyUp);

	$('#code').keyup(onKeyUp)

	mod_950_w();

	$(window).resize(function(){
		mod_950_w();
	})
})

// 处理回车事件提交表单
function onKeyUp(event){
	if(event.keyCode==ENTER){
		if($('#name').val() && $('#pwd').val() && $('#code').val())
			$(this).parents('form').submit();
	}
}

// 计算图标位置
function mod_950_w(){
	document.getElementById("mod_950_w").style.width = document.body.clientWidth - 430 + "px";
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
		document.location.href="adminindex.jsp";
	}else if(json.result==1){
		alert(json.msg);
	}
}