function isnull(str){if(str==null||str==""||str=="undefine")return true;}

function initLayout() {
	
    // var iContentW = $(window).width() - (DWZ.ui.sbar ? $("#sidebar").width() + 10 : 34) - 5;
    // var iContentH = $(window).height() - $("#header").height() - 34;

    // $("#container").width(iContentW);
    // $("#container .tabsPageContent").height(iContentH - 34).find("[layoutH]").layoutH();
    // $("#sidebar, #sidebar_s .collapse, #splitBar, #splitBarProxy").height(iContentH - 5);
    // $("#taskbar").css({top: iContentH + $("#header").height() + 5, width:$(window).width()});
    var iContentW = $(window).width() - $("#sidebar").width() + 10 - 5;
    var iContentH = $(window).height() - $(".navbar-inverse").height() - 34;
    //$("#main").width(iContentW);
    $('[role=main]').height(iContentH+30);
    $('.tab-content').height(iContentH - $('.nav-tabs').height() - $('.footer').height() + 29);
    $("[layoutH]").layoutH();
}



var handleSidebarMenu = function() {
    jQuery('.page-sidebar').on('click', 'li > a', function(e) {
        if ($(this).next().hasClass('sub-menu') == false) {
            if ($('.btn-navbar').hasClass('collapsed') == false) {
                $('.btn-navbar').click();
            }
            return;
        }

        var parent = $(this).parent().parent();

        parent.children('li.open').children('a').children('.arrow').removeClass('open');
        parent.children('li.open').children('.sub-menu').slideUp(200);
        parent.children('li.open').removeClass('open');

        var sub = jQuery(this).next();
        if (sub.is(":visible")) {
            jQuery('.arrow', jQuery(this)).removeClass("open");
            jQuery(this).parent().removeClass("open");
            sub.slideUp(200, function() {
                //handleSidebarAndContentHeight();
            });
        } else {
            jQuery('.arrow', jQuery(this)).addClass("open");
            jQuery(this).parent().addClass("open");
            sub.slideDown(200, function() {
                //handleSidebarAndContentHeight();
            });
        }

        e.preventDefault();
    });

    // handle ajax links
    jQuery('.page-sidebar').on('click', ' li > a.ajaxify', function(e) {
        e.preventDefault();
        App.scrollTop();

        var url = $(this).attr("href");
        var menuContainer = jQuery('.page-sidebar ul');
        var pageContent = $('.page-content');
        var pageContentBody = $('.page-content .page-content-body');

        menuContainer.children('li.active').removeClass('active');
        menuContainer.children('arrow.open').removeClass('open');

        $(this).parents('li').each(function() {
            $(this).addClass('active');
            $(this).children('a > span.arrow').addClass('open');
        });
        $(this).parents('li').addClass('active');

        App.blockUI(pageContent, false);

        $.post(url, {}, function(res) {
            App.unblockUI(pageContent);
            pageContentBody.html(res);
            App.fixContentHeight(); // fix content height
            App.initUniform(); // initialize uniform elements
        });
    });
}

var handleSidebarToggler = function() {
    // handle sidebar show/hide
    //$('.page-sidebar').on('click', '.sidebar-toggler', function(e) {
    $('.page-sidebar').on('click', '.folder', function(e) {
        var body = $('body');
        var sidebar = $('.page-sidebar');

        if ((body.hasClass("page-sidebar-hover-on") && body.hasClass('page-sidebar-fixed')) || sidebar.hasClass('page-sidebar-hovering')) {
            body.removeClass('page-sidebar-hover-on');
            sidebar.css('width', '').hide().show();
            e.stopPropagation();
//            runResponsiveHandlers();
            return;
        }

        $(".sidebar-search", sidebar).removeClass("open");

        if (body.hasClass("page-sidebar-closed")) {
            body.removeClass("page-sidebar-closed");
            if (body.hasClass('page-sidebar-fixed')) {
                sidebar.css('width', '');
            }
        } else {
            body.addClass("page-sidebar-closed");
        }
//        runResponsiveHandlers();
    });

    // handle the search bar close
    $('.page-sidebar').on('click', '.sidebar-search .remove', function(e) {
        e.preventDefault();
        $('.sidebar-search').removeClass("open");
    });

    // handle the search query submit on enter press
    $('.page-sidebar').on('keypress', '.sidebar-search input', function(e) {
        if (e.which == 13) {
            window.location.href = "extra_search.html";
            return false; //<---- Add this line
        }
    });
    // handle the search submit
    $('.sidebar-search .submit').on('click', function(e) {
        e.preventDefault();

        if ($('body').hasClass("page-sidebar-closed")) {
            if ($('.sidebar-search').hasClass('open') == false) {
                if ($('.page-sidebar-fixed').size() === 1) {
                    $('.page-sidebar .sidebar-toggler').click(); //trigger sidebar toggle button
                }
                $('.sidebar-search').addClass("open");
            } else {
                window.location.href = "extra_search.html";
            }
        } else {
            window.location.href = "extra_search.html";
        }
    });
    }
var isIE8 = false;
var isIE9 = false;
var isIE10 = false;
isIE8 = !! navigator.userAgent.match(/MSIE 8.0/);
isIE9 = !! navigator.userAgent.match(/MSIE 9.0/);
isIE10 = !! navigator.userAgent.match(/MSIE 10/);
$.fn.placeholder = function() {
	return this.each(function() {

		var $this = $(this);
		function getAltBox() {
			return $this.parent().find("label.alt");
		}
		function altBoxCss(opacity) {
			var position = $this.position();
			return {
				width : $this.width(),
				top : -position.top + 'px',
				left : position.left + 'px',
				lineHeight : '19px',
				opacity : opacity || 1,
				marginTop : '-' + ($this.height() + GetStringNumValue($this.css('paddingBottom')) + GetStringNumValue($this.css('marginBottom'))) + 'px',
				marginLeft : GetStringNumValue($this.css('paddingLeft')) + ($this.offset().left - $this.parent().offset().left) + GetStringNumValue($this.css('marginLeft') + 3) + 'px'
			}
		}
		function GetStringNumValue(el) {
			var el = !el ? '' : el.replace('px', '');
			return parseInt(el);
		}
		if ($this.attr("placeholder") && getAltBox().size() < 1) {
			if (!$this.attr("id"))
				$this.attr("id", $this.attr("name") + "_"
								+ Math.round(Math.random() * 10000));
			var $target = $this.siblings('[for='+$this.attr("id")+']:last');
			if($target.length==0){$target=$this;}
			$('<label class="alt" for="' + $this.attr("id") + '">' + $this.attr("placeholder") + '</label>')
			.insertAfter($target)
			.css(altBoxCss(1))
			.hide()
			.click(function(){$this.focus().click();return false;});
			if (!$(this).val())
				getAltBox().show().css("opacity", 1);
		}
		$this.focus(function() {
			getAltBox().css(altBoxCss(0.6));
		}).blur(function() {
			if (!$this.val() || $this.val()=='')
				getAltBox().show().css("opacity", 1);
			else
				getAltBox().hide();
		}).bind('keyup input propertychange',function() {
			if (!$this.val() || $this.val()=='')
				getAltBox().show().css("opacity", 0.6);
			else
				getAltBox().hide();
		}).bind('reset',function(){
			getAltBox().css({
				marginTop : '-' + ($this.height() + GetStringNumValue($this.css('paddingBottom')) + GetStringNumValue($this.css('marginBottom'))) + 'px',
				marginLeft : GetStringNumValue($this.css('paddingLeft')) + ($this.offset().left - $this.parent().offset().left) + GetStringNumValue($this.css('marginLeft') + 3) + 'px'
			});
			if (!$this.val() || $this.val()=='')
				getAltBox().show().css("opacity", 0.6);
			else
				getAltBox().hide();
		});

	});

};

var handleFixInputPlaceholderForIE = function () {
    if (!('placeholder' in document.createElement('input'))) {
    		$('input[placeholder]:not(.placeholder-no-fix), textarea[placeholder]:not(.placeholder-no-fix)').placeholder();
    }
}
function chat_init(){
	if(window.p_join_listen){
		p_join_listen(window.g_user?g_user.id:"");
	}
}    
function onData(event){
	var msg = event.get("msg");
	var type = event.get("type");
	var content;
	if(type == '1')
		content = '<div style="min-width:230px;text-align:center;"><a href="javascript:;"' +
			'onclick=\'$css.openTab({id:"CBGL-HF",title:"我收到的催办",url:"dirReceiveUrgency.action",active:true,refresh:true});\'>您新收到<span style="color:red;">'
			+ msg +'</span>条催办！</a></div>';
	else
		content = '<div style="min-width:230px;text-align:center;"><a href="javascript:;"' +
			'onclick=\'$css.openTab({id:"CBGL-CK",title:"我发起的催办",url:"dirSendUrgency.action?tab=3",active:true,refresh:true});\'>您新收到<span style="color:red;">'
			+ msg +'</span>条催办回复！</a></div>';
	setTimeout(function(){
		$.dialog.tip(content,1000);
	},3000);
}
jQuery(document).ready(function() {
	//alert(1);
    //chat_init();
    /*
    $.post('dragUrgency.action','', function(data){
    	if(data.result==0) {
    		if(data.msg != '0') {
	    		content = '<div style="min-width:230px;text-align:center;"><a href="javascript:;"' +
				'onclick=\'$css.openTab({id:"CBGL-HF",title:"我收到的催办",url:"dirReceiveUrgency.action",active:true,refresh:true});\'>您有<span style="color:red;">'
				+ data.msg +'</span>条催办未处理！</a></div>';
				$.dialog.tip(content,1000);
    		}
    	}
	},'json');
	*/
    using('datepicker');
    using('upload');

    $('#tab-01').cssTab();

    $('body').on('click','[target=cssDialog]',function(){
	    return $css.openDialog($(this));
    })

    $('body').on('click','.pagination .page-prev',function(){
        var $form=$(this).parents('.table-form'),
            prevPage=parseInt($form.find('.page-current').val())-1;
        if(prevPage==0)
            prevPage=1;     
        $form.find('.page-current').val(prevPage);
        $form.find('.page-size').val($form.find('.page-num').val());
        $form.submit();
    })
    $('body').on('click','.pagination .page-next',function(){
        var $form=$(this).parents('.table-form'),
            pageCount=parseInt($form.find('.page-count').val()),
            nextPage=parseInt($form.find('.page-current').val())+1;
        if(nextPage>pageCount)
            nextPage=pageCount;     
        $form.find('.page-current').val(nextPage);
        $form.find('.page-size').val($form.find('.page-num').val());
        $form.submit();
    })
    $('body').on('click','.pagination .page-first',function(){
        $form=$(this).parents('.table-form');
        $form.find('.page-current').val(1);
        $form.find('.page-size').val($form.find('.page-num').val());
        $form.submit();
    })
    $('body').on('click','.pagination .page-last',function(){
        $form=$(this).parents('.table-form');
        $form.find('.page-current').val($form.find('.page-count').val());
        $form.find('.page-size').val($form.find('.page-num').val());
        $form.submit();
    })
    $('body').on('click','.pagination .page-go',function(){
        var $form=$(this).parents('.table-form'),
            pageNum=parseInt($form.find('.page-jump').val());
            pageCount=parseInt($form.find('.page-count').val());
        if(pageNum>pageCount)
            pageNum=pageCount;  
        if(pageNum==0)
            pageNum=1;      
        $form=$(this).parents('.table-form');
        $form.find('.page-current').val(pageNum);
        $form.find('.page-size').val($form.find('.page-num').val());
        $form.submit();
    })
    $('body').on('keydown','.pagination .page-jump',function(event){
    	var ENTER = 13;
    	if(event.keyCode==ENTER){
    		$form=$(this).parents('.table-form');
    		$form.find('.page-current').val($form.find('.page-jump').val());
            $form.submit();
    	}
    })
    $('body').on('keydown','.pagination .page-num',function(){
    	var ENTER = 13;
    	if(event.keyCode==ENTER){
    		$form=$(this).parents('.table-form');
    		$form.find('.page-size').val($form.find('.page-num').val());
            $form.submit();
    	}
    })
    $('body').on('click','th[order-field]',function(){
        var $this=$(this),
            $form=$this.parents('.table-form');
        if(!$this.attr('class') || $this.attr('class')=='asc'){
            $this.attr('class','desc');
        }else{
            $this.attr('class','asc');
        }
        $form.find('.order-flag').val(($this.attr('class')=='asc'?"1":"0"));
        $form.find('.order-string').val($this.attr('order-field'));
        $form.submit();
    })

    $('body').on('click','.cleck-all',function(){
        var $this=$(this),
            $form=$this.parents('.table-form');
            group=$this.attr('group');
        $form.find('input[name="'+group+'"]').prop('checked',$this.prop("checked"));
    })
    
});

_path = (function( script, i, me )
{
    var l = script.length;
    
    for( ; i < l; i++ )
    {
        me = !!document.querySelector ?
            script[i].src : script[i].getAttribute('src',4);

        if( me.substr(me.lastIndexOf('/')).indexOf('lhgdialog') !== -1 )
            break;
    }
    
    me = me.split('?'); _args = me[1];
    
    return me[0].substr( 0, me[0].lastIndexOf('/') + 1 );
})(document.getElementsByTagName('script'),0);

_webroot = (function( script, i, me )
{
    for (var i = 0; i < script.length; ++i) {
		var src = script.item(i).src;
		if (src) {
			var index = src.indexOf("jquery");
			if (index >= 0) {
				index = src.indexOf("cssui");
				return src.substring(0, index);
			}
		}
	}
	return;
})(document.getElementsByTagName('script'),0);

function blockUI(el, centerY) {
    var el = jQuery(el);
    el.block({
        message: '<img src="'+_webroot+'cssui/images/ajax-loading.gif" align="">正在努力的加载,请稍等......',
        centerY: centerY != undefined ? centerY : true,
        css: {
            top: '10%',
            border: 'none',
            padding: '2px',
            backgroundColor: 'none'
        },
        overlayCSS: {
            backgroundColor: '#000',
            opacity: 0.05,
            cursor: 'wait'
        }
    });
}

function unblockUI(el) {
    jQuery(el).unblock({
        onUnblock: function() {
            jQuery(el).removeAttr("style");
        }
    });
}

function divSearch(form, rel){
    var $form = $(form),
        $el = $form.parents('.tab-pane');
    if (!('placeholder' in document.createElement('input'))) {
		$form.find("input.placeholder, textarea.placeholder").each(function () {
    		$(this).val('');
    	});
    }
    if (rel) {
        var $box = $("#" + rel);
        blockUI($el);
        $css.load($box,$form.attr("action"),$form.serialize(),function(){
            $box.find("[layoutH]").layoutH();
            unblockUI($el);
            $el.initUI();
            //$('[order-field='+$('.order-string').val()+']',$el).attr('class',$('.order-flag').val()==1?'asc':'desc');
        })
    }
    return false;
}

function navTabSearch(form, navTabId){
    var $form = $(form),
    	$el = $form.parents('.tab-pane');
    if (!('placeholder' in document.createElement('input'))) {
		$form.find("input.placeholder, textarea.placeholder").each(function () {
    		$(this).val('');
    	});
    }
	if($el[0]){
		blockUI($el.parent().parent());
		$.post($form.attr('action'),$form.serialize(),function(data){
            unblockUI($el.parent().parent());
            $el.html(data);
			$el.initUI();
			//$('[order-field='+$('.order-string').val()+']',$el).attr('class',$('.order-flag').val()==1?'asc':'desc');     
		})
	}
//    $('#tab-01').cssTab('reload',{'url':($form.attr('action')+($form.attr('action').indexOf('?')>0?'&':'?')+$form.serialize())});
//    setTimeout(function(){
//        $('[order-field='+$('.order-string').val()+']').attr('class',$('.order-flag').val()==1?'asc':'desc');    
//    },1000);
//    $.post()
    return false;
}

function validateCallback(form, callback, tabId) {
//	debugger;
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
        success: function(data){
//            $form[0].reset();
            callback(data,tabId);
        }
    });
    return false;
}


function navTabAjaxDone(json,tabId){
    if (json.result == 0){
        $css.tip(json.msg);
        $.cssTab.focus().parent().parent().cssTab('closeCurrent');
        if(tabId){
            $.cssTab.focus().parent().parent().cssTab('reload',{id:tabId});
        }
        else{
            $.cssTab.focus().parent().parent().cssTab('reloadCurrent');
        }
    }else{
        $css.alert(json.msg);
    }
}

function navTabAjaxQuery(json,tabId){
    if (json.result == 0){
        $css.tip(json.msg);
        $.cssTab.focus().parent().parent().cssTab('closeCurrent');
        if(tabId)
        	$css.openTab({id:tabId});
        $("form:eq(0)",$.cssTab.focus()).submit();
    }else{
        $css.alert(json.msg);
    }
}
function navTabNoCloseAjaxDone(json){
    if (json.result == 0){
        $css.tip(json.msg);
    }else{
        $css.alert(json.msg);
    }
}

function dialogAjaxDone(json){
    if (json.result == 0){
        $.dialog.focus.close();
        $css.tip(json.msg);
        $.cssTable.focus().submit();
    }else{
        $css.alert(json.msg);
    }
}

function dialogNoReloadAjaxDone(json){
    if (json.result == 0){
        $.dialog.focus.close();
        $css.tip(json.msg);
    }else{
        $css.alert(json.msg);
    }
}

function dialogNoCloseAjaxDone(json){
    if (json.result == 0){
        $css.tip(json.msg);
    }else{
        $css.alert(json.msg);
    }
}

$css={
    openTab:function(params){
        $('#tab-01').cssTab('addTab',params);
    },
    closeTab:function(){
    	$.cssTab.focus().parent().parent().cssTab('closeCurrent');
    },
    onCloseTab:function(func){
    	$.cssTab.focus().parent().parent().cssTab('onCloseCurrent',func);
    },
    onCloseTabRefresh:function(){
    	$.cssTab.focus().parent().parent().cssTab('onCloseCurrent',function(){$.cssTable.focus().submit();});
    },
    openDialog:function(params){
    	//{url:"",title:""}
    	//title
    	//$obj href="http://...." title="abc"
    	var url, title, rel,lock;
    	if(params instanceof jQuery) {
    		var $target = params,
    		url=$target.attr('href')?$target.attr('href'):'',
    		title=$target.attr('title')?$target.attr('title'):'',
    		rel = $target.attr('rel')?$target.attr('rel'):'',
    		lock = $target.attr('lock')?$target.attr('lock'):true;
    	} else {
    		if (typeof(params) === 'string') {
				url=params,
				title='',
				rel='',
				lock=true;
			}
			else{
			  url=params.url?params.url:'',
			  title=params.title?params.title:'',
			  rel=params.rel?params.rel:'',
			  lock=params.lock==null?true:params.lock;
			}
    	}
        if(url.isExternalUrl()){
            $.dialog({
            	id:rel,
                title:title,				
                lock:true,
				width:500,
				height:300,
                content:'url:'+url
            });
        }else{
            var a={
            	id:rel,
                title:title,
                lock:lock,
                resize: false,
                max:false,
                min:false,
                padding: 0
            }
            var api = $.dialog(a);
            /* jQuery ajax */
            $.ajax({
                url:url,
                success:function(data){
                    api.content(data);
                    $(api.DOM.content[0]).initUI();
                },
                cache:false
            });
        }
        return false;
    },
    focusDialog:function(){
        return $.dialog.focus.DOM.main[0];
    },
    closeDialog:function(){
        $.dialog.focus.close();
    },
    tip:function(content){
        $.dialog.tips(content,2,'success.gif');
    },
    alert:function(content,callback){
    	   $.dialog.alert('<div style="min-width:300px;">'+content+'</div>',callback);
    },
    confirm:function(content,callback1,callback2){
        $.dialog.confirm('<div style="min-width:300px;">'+content+'</div>', callback1, callback2);
    },
    login:function(){

    },
    timestampUrl:function(url){
    	if(url.indexOf('?')<0){
    		url += '?';
    	}else{
    		url += '&';
    	}
    	url += '_='+new Date().getTime();
    	return url;
    },
    post:function(url, data, callback, type){
        if ( jQuery.isFunction( data ) ) {
            callback = data;
            data = {};
        }

        return jQuery.ajax({
            type: "POST",
            url: $css.timestampUrl(url),
            data: data,
            success: function(data){
                if(jQuery.isFunction(callback))
                    callback(data);
            },
            dataType: type,
            error:function(){

            }
        });
    },
    get:function(url, data, callback, type){
        if ( jQuery.isFunction( data ) ) {
            callback = data;
            data = null;
        }
        
        return jQuery.ajax({
            type: "GET",
            url: $css.timestampUrl(url),
            data: data,
            success: function(data){
                if(jQuery.isFunction(callback))
                    callback(data);
            },
            dataType: type,
            error:function(){

            }
        });
    },
    load:function(selector, url, data, callback, type){
        //jQuery(selector).load(url,data,callback,type);
        $.post(url,data,function(m){
            $(selector).html(m);
            callback();
        })
    },
    checkedVal:function(name, parent, split){
        var s=[];
        $('input[name="'+name+'"]:checked',$(parent || document)).each(function(i,t){
            s.push($(t).val());
        })
        return s.join(split);
    },
    parseUrl: function(b, a) {
        var c = b ? b: document.location.href;
        b = {};
        a = a || "?";
        if (!c.indexOf(a)) return b;
        a = c.split(a)[1].split("&");
        for (c = 0; c < a.length; c++) {
            var e = a[c].replace(/#.*$/g, "").split("=");
            e[1] || (e[1] = "");
            //b[e[0]] = UI.B.ie ? e[1] : UI.decode(e[1])
            b[e[0]] = e[1];

        }
        return b
    }
}


$.fn.extend({
    initUI: function() {
        return this.each(function() {
            var $el=$(this || document);
            using('validate',function(){
              $('.form-validate',$el).validate({
                focusInvalid: true, 
                ignore: "",
                onkeyup: false,
                rules: {
                },
                errorPlacement: function (label, element) {
                  if($(element).hasClass("novalidate")){return;}
                  var $target = $(element).siblings('[for='+$(element).attr("id")+']:last');
        		  if($target.length==0){$target=$(element);}
                  $('<span class="help-inline"></span>').insertAfter($target.get(0)).append(label);
                  var parent = $(element).parents('.control-group');
                  parent.addClass('error');  
                },
                success: function (label, element) {
                  var parent = $(element).parents('.control-group');
                  parent.removeClass('error'); 
                }
              });
              $('.form-validateLog',$el).validate({
                  focusInvalid: true, 
                  ignore: "",
                  onkeyup: false,
                  rules: {
                  },
                  errorPlacement: function (label, element) {
                    if($(element).hasClass("novalidate")){return;}
                    var $target = $(element).siblings('[for='+$(element).attr("id")+']:last');
          		  if($target.length==0){$target=$(element);}
                    var parent = $(element).parents('.control-group');
                    parent.addClass('error');  
                  },
                  success: function (label, element) {
                    var parent = $(element).parents('.control-group');
                    parent.removeClass('error'); 
                  }
                });
                  $('.form-validateRe',$el).validate({
                    focusInvalid: true, 
                    ignore: "",
                    onkeyup: false,
                    rules: {
                    },
                    errorPlacement: function (label, element) {
                      if($(element).hasClass("novalidate")){return;}
                      var $target = $(element).siblings('[for='+$(element).attr("id")+']:last');
            		  if($target.length==0){$target=$(element);}
                      $('<span class="help-inline"></span>').insertAfter($target.get(0)).append(label);
                      var parent = $(element).parents('.control-group');
                      parent.addClass('error');  
                    },
                    success: function (label, element) {
                      var parent = $(element).parents('.control-group');
                      parent.removeClass('error'); 
                    }
                  });
                  $('.form-validateNre',$el).validate({
                      focusInvalid: true, 
                      ignore: "",
                      onkeyup: false,
                      rules: {
                      },
                      errorPlacement: function (label, element) {
                        if($(element).hasClass("novalidate")){return;}
                        var $target = $(element).siblings('[for='+$(element).attr("id")+']:last');
              		  if($target.length==0){$target=$(element);}
                        $('<span class="help-inline"></span>').insertAfter($target.get(0)).append(label);
                        var parent = $(element).parents('.control-group');
                        parent.addClass('error');  
                      },
                      success: function (label, element) {
                        var parent = $(element).parents('.control-group');
                        parent.removeClass('error'); 
                      }
                    });
                  $('.form-validatePe',$el).validate({
                      focusInvalid: true, 
                      ignore: "",
                      onkeyup: false,
                      rules: {
                      },
                      errorPlacement: function (label, element) {
                        if($(element).hasClass("novalidate")){return;}
                        var $target = $(element).siblings('[for='+$(element).attr("id")+']:last');
              		  if($target.length==0){$target=$(element);}
                        $('<span class="help-inline"></span>').insertAfter($target.get(0)).append(label);
                        var parent = $(element).parents('.control-group');
                        parent.addClass('error');  
                      },
                      success: function (label, element) {
                        var parent = $(element).parents('.control-group');
                        parent.removeClass('error'); 
                      }
                    });
              $('.doc-form-validate',$el).validate({
        	    focusInvalid: true, 
        	    ignore: "",
        	    onkeyup: false,
        	    rules: {
        	    },
        	    errorPlacement: function (label, element) { 
        	      if($(element).hasClass("novalidate")){return;}
        	      $(element).popover({
        	        content:label.html(),
        	        placement:'top',
        	        trigger:'manual',
        	        container:$(element).parent()
        	      }).popover('show');
        	    },
        	    success: function (label, element) {
        	      $(element).popover('destroy');
        	    }
        	  }); 
            })

            $('.table-form',$el).cssTable();

            $("[layoutH]",$el).layoutH();

            $('[target=cssTab]',$el).click(function(){
                var $target=$(this);
                $('#tab-01').cssTab('addTab',{
                    id:$target.attr('rel'),
                    title:$target.attr('title')?$target.attr('title'):$target.html(),
                    url:$target.attr('href'),
                    refresh:$target.attr('refresh')==null?'false':$target.attr('refresh')+'',
                    active:true
                });
                return false;
            });
            handleFixInputPlaceholderForIE();
        
        });
	},
    isTag:function(tn) {
        if(!tn) return false;
            return $(this)[0].tagName.toLowerCase() == tn?true:false;
    },
    layoutH: function($refBox){
        return this.each(function(){
            var $this = $(this);
            if (! $refBox) $refBox = $this.parents("div.layoutBox:first");
            var iRefH = $refBox.height();
            var iLayoutH = parseInt($this.attr("layoutH"));
            var iH = iRefH - iLayoutH > 50 ? iRefH - iLayoutH : 50;
            
            if ($this.isTag("table")) {
                $this.removeAttr("layoutH").wrap('<div layoutH="'+iLayoutH+'" style="overflow:auto;height:'+iH+'px"></div>');
            } else {
                $this.height(iH).css("overflow","auto");
            }
        });
    }
})

handleSidebarMenu();
handleSidebarToggler();
initLayout();
$(document).initUI();
$(window).resize(function(){
    initLayout();
});