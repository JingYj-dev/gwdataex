/*
 * cssSlider v1.0 Copyright(c) 2012 by CSS WeidongWang
 */
;(function($){
	$.fn.cssSlider = function(options) {
		var defaults = {
			speed:3000
		},
		options = $.extend(defaults,options);
		return this.each(function() {
			var $this = $(this),
			_running = false,
			_paused = false,
			_idx = 0,
			_timer = '',
			$img = $this.find('img'),
			_len = $img.length,
			$nav = $('<div class="css-nav"></div>'),
			$href = $('<div class="css-href"></div>'),
			init = function(){
	      $this.append($nav);
				$this.append($href);
				$this.append('<div style="clear:both;"></div>');
				for(var i=0; i<_len; i++)
	      	$nav.append('<a rel="'+i+'">'+(i+1)+'</a>');
	      $('a:eq('+ _idx +')', $nav).addClass('active');
	      $('a', $nav).bind('click', function(){
	       	if(_running) return false;
	        if($(this).hasClass('active')) return false;
	        _running=true;
	      	stop();
	        to($(this).attr('rel'));
	      });
	      $img.bind('click', function(e){
	      	if(_running) return false;
	      	var rel=(parseInt(_idx)+1)%_len;
	      	try{
	      		var posX=e.clientX-$(this).offset().left;
	      		if(posX*2<$(this).width())
	      			rel=(_idx==0?_len-1:parseInt(_idx)-1);
	      	}catch(ex){}
	      	$('a:eq('+ rel +')', $nav).click();
				}); 
				$this.hover(function(){
					_paused=true;
	       	stop();
	      }, function(){
	        _paused=false;
	        start();
	      });
	      to(0);
			},
			to = function(rel){
				$('a:eq('+ _idx +')', $nav).removeClass('active');
	      $img.eq(_idx).fadeOut(200,function(){
					_idx =rel;
					$('a:eq('+ _idx +')', $nav).addClass('active');
					var w=parseInt(($img.eq(_idx).parent().width()-$nav.width()-20)*2/12);
					$href.html('<a target="_blank" '+($img.eq(_idx).attr('rel')==''?'':'href="'+$img.eq(_idx).attr('rel')+'"')+'>'+substr($img.eq(_idx).attr('title'),w)+'</a>');
	        $img.eq(_idx).fadeIn(200,start());
	      });
			},
			stop = function(){
				clearInterval(_timer);
	      _timer = '';
			},
			start = function(){
				_running=false;
				if(!_paused){
					clearInterval(_timer);
					_timer = setInterval(function(){
						$('img:eq('+ _idx +')', $this).click();
						},options.speed);
				}
			};
			init();
			start();
		});
	};
	function substr(str, len) {
		if(!str || !len) { return ''; }
		var a = 0;
		var temp = '';
		for (var i=0;i<str.length;i++){
			a+=str.charCodeAt(i)>255?2:1;
			if(a >len)return temp;
			temp += str.charAt(i);
		}
		return str;
	};
})(jQuery)