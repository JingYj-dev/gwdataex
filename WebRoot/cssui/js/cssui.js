function lily_beforeClose(){
	return true;
}
$.extend(String.prototype, {
	isPositiveInteger: function() {
		return (new RegExp(/^[1-9]\d*$/).test(this));
	},
	isInteger: function() {
		return (new RegExp(/^\d+$/).test(this));
	},
	isNumber: function(value, element) {
		return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
	},
	trim: function() {
		return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
	},
	startsWith: function(pattern) {
		return this.indexOf(pattern) === 0;
	},
	endsWith: function(pattern) {
		var d = this.length - pattern.length;
		return d >= 0 && this.lastIndexOf(pattern) === d;
	},
	replaceSuffix: function(index) {
		return this.replace(/\[[0-9]+\]/, '[' + index + ']').replace('#index#', index);
	},
	trans: function() {
		return this.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&quot;/g, '"');
	},
	replaceAll: function(os, ns) {
		return this.replace(new RegExp(os, "gm"), ns);
	},
	replaceTm: function($data) {
		if (!$data) return this;
		return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
			return $data[$1.replace(/[{}]+/g, "")];
		});
	},
	replaceTmById: function(_box) {
		var $parent = _box || $(document);
		return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
			var $input = $parent.find("#" + $1.replace(/[{}]+/g, ""));
			return $input.val() ? $input.val() : $1;
		});
	},
	isFinishedTm: function() {
		return !(new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this));
	},
	skipChar: function(ch) {
		if (!this || this.length === 0) {
			return '';
		}
		if (this.charAt(0) === ch) {
			return this.substring(1).skipChar(ch);
		}
		return this;
	},
	isValidPwd: function() {
		return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this));
	},
	isValidMail: function() {
		return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
	},
	isSpaces: function() {
		for (var i = 0; i < this.length; i += 1) {
			var ch = this.charAt(i);
			if (ch != ' ' && ch != "\n" && ch != "\t" && ch != "\r") {
				return false;
			}
		}
		return true;
	},
	isPhone: function() {
		return (new RegExp(/(^([0-9]{3,4}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0-9]{3,4}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/).test(this));
	},
	isUrl: function() {
		return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));
	},
	isExternalUrl: function() {
		return this.isUrl() && this.indexOf("://" + document.domain) == -1;
	}
});
(function($, window, document, undefined) {

  $.fn.cssTab = function(parameters) {

    var settings = $.extend(true, {}, $.fn.cssTab.settings, parameters),

      $module = $(this),

      element = this,
      performance = [],

      className = settings.className,
      metadata = settings.metadata,
      selector = settings.selector,
      error = settings.error,

      eventNamespace = '.' + settings.namespace,
      moduleNamespace = 'module-' + settings.namespace,

      instance = $module.data(moduleNamespace),

      $ul = $module.find('.nav-tabs'),
      $content = $module.find('.tab-content'),
      $prev = $module.find(settings.selector.prev),
      $next = $module.find(settings.selector.next),
      $more = $module.find(settings.selector.more),
      $tabBtn = $module.find(settings.selector.tabBtn),

      query = arguments[0],
      methodInvoked = (instance !== undefined && typeof query == 'string'),
      queryArguments = [].slice.call(arguments, 1),

      module,
      returnedValue;

    module = {
      addTab: function(params) {
        if (!params.id)
          params.id = settings.namespace + '_' + Math.round(Math.random() * 10000);
        if ($('#' + params.id)[0]) {
        	if(params.refresh+''=='true') {
        		$ul.find('[href="#' + params.id + '"]').data('loaded', 0);
        	}
          $ul.find('[href="#' + params.id + '"]').click();
          return;
        }
        $li = $('<li><a ptabid="' +($ul.find('.active a').attr('tabid')||'home')+ '" tabid="' + params.id + '" href="#' + params.id + '" data-href="' + params.url + '"><span>' + params.title + '</span><i class="remove"></i></a></li>');
        $ul.append($li);
        $li.find('a').on('click', module.event.click);
        $li.find('a .remove').on('click', module.event.close);
        $content.append('<div id="' + params.id + '" class="tab-pane"></div>');
        $more.next().append('<li><a href="javascript:;">'+params.title+'</a></li>');
        module._contextMenu($ul.find('a'));
        if (params.active){
          $li.find('a').click();
        }
      },
      reload:function(params){
        module._reload(params.id,params.url,params);
      },
      reloadCurrent: function() {
        module.reload($ul.find('.active a').attr('tabid'));
      },
      currentTab: function(){
        return module._getPanels().eq(module._currentIndex);
      },
      close: function(tabid) {
        if (typeof(tabid) === 'string')
          tabid = module._indexTabId(tabid);
        /**close currentTab,open preTab*/
        //$ul.find('a').eq(tabid - 1).click();
        //$more.next().find('li').eq(tabid - 1).addClass('active');
        /**close currentTab,open parentTab*/
        var ptabid = module._indexTabId($ul.find('a').eq(tabid).attr('ptabid'));
        if(ptabid==-1){
        	ptabid=tabid-1;
        }
        $ul.find('a').eq(tabid).parent().remove();
        $content.find('.tab-pane').eq(tabid).remove();
        $more.next().find('li').eq(tabid).remove();
        
        $ul.find('a').eq(ptabid).click();
        $more.next().find('li').eq(ptabid).addClass('active');
      },
      onCloseCurrent:function(callback){
      	$ul.find('.active a .remove').on('click',function(){
      		module.event.close;
      		if(typeof(callback)==="function"){
	        	callback($ul.find('.active a').attr('tabid'));
	        }
      	});
      },
      closeCurrent: function() {
          //module.close($ul.find('.active a').attr('tabid'));
    	  $ul.find('.active a .remove').click();
      },
      closeOther: function(tabid) {
        if (typeof(tabid) === 'string')
          tabid = module._indexTabId(tabid);
        module._getTabs().each(function(i,t){
          $this=$(t);
          if($this.find('.remove').length>0 && i!=tabid){
            $this.parent().addClass('to-be-del');
            module._getPanels().eq(i).addClass('to-be-del');
            $more.next().find('li').eq(i).addClass('to-be-del');
          }
        })
        $('.to-be-del').remove();
        $prev.hide();
        $next.hide();
        if(module._getTabs().length>0){
          module._getTabs().eq(module._getTabs().length-1).click();
        }
      },
      closeAll: function() {
        module._getTabs().each(function(i,t){
          $this=$(t);
          if($this.find('.remove').length>0){
            $this.parent().addClass('to-be-del');
            module._getPanels().eq(i).addClass('to-be-del');
            $more.next().find('li').eq(i).addClass('to-be-del');
          }
        })
        $('.to-be-del').remove();
        $prev.hide();
        $next.hide();
        if(module._getTabs().length>0){
          module._getTabs().eq(module._getTabs().length-1).click();
        }
      },
      _reload: function(tabid, url, params) {
        if (!tabid)
          tabid=$ul.find('.active a').attr('tabid');
        //else if (typeof(tabid) === 'string')
        tabid = module._indexTabId(tabid);
        if (url)
          module._getTabs().eq(tabid).attr('data-href', url);
          /** reload title   start */
          if(params){
	          var title =  module._getTabs().eq(tabid).find('span');
	          title.text(params.title||title.text());
		      $more.next().find('li>a').eq(tabid).text(params.title||title.text());
          }
          /** reload title   end */
          module._getTabs().eq(tabid).data('loaded', 0).click();
      },
      _init: function() {
        $ul.find('a').on('click', module.event.click);
        $ul.find('a .remove').on('click', module.event.close);
        $prev.off().on('click', module._scrollPrev);
        $next.off().on('click', module._scrollNext);
        $more.off().on('click', module.event.more);
        for(var i=0;i<module._getTabs().length;i++){
          $more.next().append('<li><a href="javascript:;">'+module._getTabs().eq(i).html()+'</a></li>');
        }
        $more.next().on('click','li',function(){
          var index=$more.next().find('li').index(this);
          module._getTabs().eq(index).click();
        })
        if ($ul.attr('context-menu')) {
          module._contextMenu($ul.find('a'));
        }
      },
      _getTabs: function() {
        return $ul.find('> li a');
      },
      _getPanels: function() {
        return $content.find('.tab-pane');
      },
      _indexTabId: function(tabid) {
        if (!tabid) return -1;
        var iOpenIndex = -1;
        $ul.find('a').each(function(index) {
          if ($(this).attr("tabid") == tabid) {
            iOpenIndex = index;
            return;
          }
        });
        return iOpenIndex;
      },
      _tabsW:function($tabs){
          var iW = 0;
          $tabs.each(function(){
            iW += $(this).outerWidth(true);
          });
          return iW;
        },
      _getTabsW: function(iStart, iEnd){
        return module._tabsW(module._getTabs().slice(iStart, iEnd));
      },
      _getLeft: function(){
        return $ul.position().left;
      },
      _getScrollBarW: function(){
        return $module.width()-$tabBtn.width()-33;
      },
      _visibleStart: function(){
        var iLeft = parseInt($ul.css('marginLeft').replaceAll('px','')), iW = 0;
        var $tabs = module._getTabs();
        for (var i=0; i<$tabs.size(); i++){
          if (iW + iLeft >= 0) {
            return i;
          }
          iW += $tabs.eq(i).outerWidth(true);
        }
        return 0;
      },
      _visibleEnd: function(){
        var iLeft = parseInt($ul.css('marginLeft').replaceAll('px','')), iW = 0;
        var $tabs = module._getTabs();
        for (var i=0; i<$tabs.size(); i++){
          iW += $tabs.eq(i).outerWidth(true);
          if (iW + iLeft > module._getScrollBarW()) {
            return i;
          }
        }
        return $tabs.size();
      },
      _scrollPrev: function(){
        var iStart = module._visibleStart();
        if (iStart > 0){
          module._scrollTab(-module._getTabsW(0, iStart-1));
        }
      },
      _scrollNext: function(){
        var iEnd = module._visibleEnd();
        if (iEnd < module._getTabs().size()){
          module._scrollTab(-module._getTabsW(0, iEnd+1) + module._getScrollBarW());
        } 
      },
      _scrollTab: function(iLeft, isNext){
        //var $this = this;
        $ul.animate({ marginLeft: iLeft+'px' }, 200, function(){
          //$this._ctrlScrollBut();
        });
      },
      _scrollCurrent: function(){ // auto scroll current tab
        var iW = module._tabsW(module._getTabs());
        if (iW <= module._getScrollBarW()){
          module._scrollTab(0);
          $prev.hide();
          $next.hide();
        } else if (module._getLeft() < module._getScrollBarW() - iW){
          module._scrollTab(module._getScrollBarW()-iW);
        } else if (module._currentIndex < module._visibleStart()) {
          module._scrollTab(-module._getTabsW(0, module._currentIndex));
        } else if (module._currentIndex >= module._visibleEnd()) {
          module._scrollTab(module._getScrollBarW() - module._getTabs().eq(module._currentIndex).outerWidth(true) - module._getTabsW(0, module._currentIndex));
          $prev.show();
          $next.show();
        }
      },
      _contextMenu: function($obj) {
        $obj.cssMenu({
          selector: '#' + $ul.attr('context-menu'),
          callback: {
            close: function(t) {
              module.close(t.attr('tabid'));
            },
            closeOther: function(t) {
              module.closeOther(t.attr('tabid'));
            },
            closeAll: function(t) {
              module.closeAll();
            },
            reload: function(t) {
              module._reload(t.attr('tabid'));
            }
          }
        });
      },
      event: {
        click: function(e) {
          e.preventDefault();
          $this = $(this);
          $this.tab('show');
          module._currentIndex=module._indexTabId($this.attr('tabid'));
          module._scrollCurrent();

          $more.next().find('.active').removeClass('active');
          $more.next().find('li').eq(module._currentIndex).addClass('active');
          if ($this.attr('data-href')) {
            //var $target = $($this.attr('href'));
            var $target = $('#'+$this.attr('tabid'));
            $('.tab-focus').removeClass('tab-focus');
            $target.addClass('tab-focus');
            if ($this.data('loaded') != 1) {
              var url = $this.attr('data-href');
              if (url.isExternalUrl()) {
                var ih = $target.parent('.tab-content').height() - 5;
                $target.css({
                  'margin': 0,
                  'padding': 0,
                  'overflow': 'hidden'
                });
                $target.html(settings.iframeStr.replaceAll("{url}", url).replaceAll("{height}", ih + "px"));
              } else {
                blockUI($module);
                if(url.indexOf('?')>0){
                	url=url+"&rt="+(+new Date());
                }else{
                	url=url+"?rt="+(+new Date());
                }
                $target.load(url, function(data) {
                  $target.initUI();
                  unblockUI($module);
                  //$target.find('.set-btn').width($target.width()-10);
                  $target.find('[target=closeTab]').off().on('click',function(){
                    module.closeCurrent();
                    return false;
                  })
                });
              }
              $this.data('loaded', 1);
            }
          }
        },
        close: function(e) {
          e.stopPropagation();
          if(lily_beforeClose())
        	  module.close($(this).parent().attr('tabid'));
          // alert($(this).parent().attr('tabid'));
          // $(this).parents('li').prev().find('a').tab('show');
          // $(this).parents('li').remove();
          // $content.find($(this).parent().attr('href')).remove();
          // module._scrollCurrent();
        }
      },
      initialize: function() {
        if (settings.history) {
          if ($.address === undefined) {
            module.error(error.state);
            return false;
          } else {
            if (settings.historyType == 'html5') {
              if (settings.path !== false) {
                $.address
                  .history(true)
                  .state(settings.path);
              } else {
                module.error(error.path);
                return false;
              }
            }
          }
        }

        module.instantiate();
        module._init();
      },
      instantiate: function() {
        $module
          .data(moduleNamespace, module);
      },

      destroy: function() {
        $module
          .removeData(moduleNamespace)
          .off(eventNamespace);
      },

      error: function() {
        module.error = Function.prototype.bind.call(console.error, console, settings.name + ':');
        module.error.apply(console, arguments);
      },

      invoke: function(query, passedArguments, context) {
        var
        maxDepth,
          found,
          response;
        passedArguments = passedArguments || queryArguments;
        context = element || context;
        if (typeof query == 'string' && instance !== undefined) {
          query = query.split(/[\. ]/);
          maxDepth = query.length - 1;
          $.each(query, function(depth, value) {
            var camelCaseValue = (depth != maxDepth) ? value + query[depth + 1].charAt(0).toUpperCase() + query[depth + 1].slice(1) : query;
            if ($.isPlainObject(instance[value]) && (depth != maxDepth)) {
              instance = instance[value];
            } else if ($.isPlainObject(instance[camelCaseValue]) && (depth != maxDepth)) {
              instance = instance[camelCaseValue];
            } else if (instance[value] !== undefined) {
              found = instance[value];
              return false;
            } else if (instance[camelCaseValue] !== undefined) {
              found = instance[camelCaseValue];
              return false;
            } else {
              module.error(error.method, query);
              return false;
            }
          });
        }

        if ($.isFunction(found)) {
          response = found.apply(context, passedArguments);
        } else if (found !== undefined) {
          response = found;
        }
        if ($.isArray(returnedValue)) {
          returnedValue.push(response);
        } else if (returnedValue !== undefined) {
          returnedValue = [returnedValue, response];
        } else if (response !== undefined) {
          returnedValue = response;
        }
        return found;
      }
    };

    if (methodInvoked) {
      if (instance === undefined) {
        module.initialize();
      }
      module.invoke(query);
    } else {
      if (instance !== undefined) {
        module.destroy();
      }
      module.initialize();
    }

    return (returnedValue !== undefined) ? returnedValue : this;

  };

  $.cssTab = {
    focus:function(){
      return $('.tab-focus');
    }
  };

  $.fn.cssTab.settings = {

    name: 'Tab',
    verbose: true,
    debug: true,
    performance: true,
    namespace: 'tab',
    iframeStr: '<iframe src="{url}" style="width:100%;height:{height};" frameborder="no" border="0" marginwidth="0" marginheight="0"></iframe>',

    // only called first time a tab's content is loaded (when remote source)
    onTabInit: function(tabPath, parameterArray, historyEvent) {},
    // called on every load
    onTabLoad: function(tabPath, parameterArray, historyEvent) {},

    templates: {
      determineTitle: function(tabArray) {}
    },

    // uses pjax style endpoints fetching content from same url with remote-content headers
    auto: false,
    history: true,
    historyType: 'hash',
    path: false,

    context: 'body',

    // max depth a tab can be nested
    maxDepth: 25,
    // dont load content on first load
    ignoreFirstLoad: false,
    // load tab content new every tab click
    alwaysRefresh: false,
    // cache the content requests to pull locally
    cache: true,
    // settings for api call
    apiSettings: false,

    error: {
      api: 'You attempted to load content without API module',
      method: 'The method you called is not defined',
      missingTab: 'Tab cannot be found',
      noContent: 'The tab you specified is missing a content url.',
      path: 'History enabled, but no path was specified',
      recursion: 'Max recursive depth reached',
      state: 'The state library has not been initialized'
    },

    metadata: {
      tab: 'tab',
      loaded: 'loaded',
      promise: 'promise'
    },

    className: {
      loading: 'loading',
      active: 'active'
    },

    selector: {
      tabs: '.ui.tab',
      tabBtn: '.tab-btn',
      panel: '',
      prev: '.left',
      next: '.right',
      more: '.more'
    }

  };

})(jQuery, window, document);
(function($, window, document, undefined) {

  $.fn.cssMenu = function(parameters) {

    var settings = $.extend(true, {}, $.fn.cssMenu.settings, parameters),
      $module = $(this),
      element = this,
      selector = settings.selector,
      $target = $(settings.selector),

      eventNamespace = '.' + settings.namespace,
      moduleNamespace = 'module-' + settings.namespace,

      instance = $module.data(moduleNamespace),

      query = arguments[0],
      methodInvoked = (instance !== undefined && typeof query == 'string'),
      queryArguments = [].slice.call(arguments, 1),

      module,
      returnedValue;

    module = {
      _init: function() {
        if (!$target.parent()[0].nodeName === 'BODY'); {
          $('body').append($target.clone());
          var $clone = $target.clone();
          $target.remove();
          $clone.append('body').hide();
          $target = $(settings.selector);
        }
        // $(selector).find('li').click(function(){
        //   settings.callback(module.target);
        // });
        $module.contextmenu(function(e) {
          var a=this;
          var posX = e.pageX;
          var posY = e.pageY;
          if ($(window).width() < posX + $(this).width()) posX -= $(this).width();
          if ($(window).height() < posY + $(this).height()) posY -= $(this).height();
          $target.css({
            'position': 'absolute',
            'left': posX,
            'top': posY
          }).show();
          $.each(settings.callback,function(id,func){
            $target.find('[rel='+id+']').off().on('click',function(){
              func($(a));
            })
          });
          $(document).one('click', function() {
            $target.hide();
          });
          return false;
        });
      },
      event: {},
      initialize: function() {
        module.instantiate();
        module._init();
      },
      instantiate: function() {
        $module
          .data(moduleNamespace, module);
      },

      destroy: function() {
        $module
          .removeData(moduleNamespace)
          .off(eventNamespace);
      },

      error: function() {
        module.error = Function.prototype.bind.call(console.error, console, settings.name + ':');
        module.error.apply(console, arguments);
      },

      invoke: function(query, passedArguments, context) {
        var
        maxDepth,
          found,
          response;
        passedArguments = passedArguments || queryArguments;
        context = element || context;
        if (typeof query == 'string' && instance !== undefined) {
          query = query.split(/[\. ]/);
          maxDepth = query.length - 1;
          $.each(query, function(depth, value) {
            var camelCaseValue = (depth != maxDepth) ? value + query[depth + 1].charAt(0).toUpperCase() + query[depth + 1].slice(1) : query;
            if ($.isPlainObject(instance[value]) && (depth != maxDepth)) {
              instance = instance[value];
            } else if ($.isPlainObject(instance[camelCaseValue]) && (depth != maxDepth)) {
              instance = instance[camelCaseValue];
            } else if (instance[value] !== undefined) {
              found = instance[value];
              return false;
            } else if (instance[camelCaseValue] !== undefined) {
              found = instance[camelCaseValue];
              return false;
            } else {
              module.error(error.method, query);
              return false;
            }
          });
        }

        if ($.isFunction(found)) {
          response = found.apply(context, passedArguments);
        } else if (found !== undefined) {
          response = found;
        }
        if ($.isArray(returnedValue)) {
          returnedValue.push(response);
        } else if (returnedValue !== undefined) {
          returnedValue = [returnedValue, response];
        } else if (response !== undefined) {
          returnedValue = response;
        }
        return found;
      }
    };
    if (methodInvoked) {
      if (instance === undefined) {
        module.initialize();
      }
      module.invoke(query);
    } else {
      if (instance !== undefined) {
        module.destroy();
      }
      module.initialize();
    }
    return (returnedValue !== undefined) ? returnedValue : this;
  };

  $.fn.cssMenu.settings = {
    name: 'cssMenu',
    namespace: 'cssMenu',
    selector: '#contextmenu',

    menuStr: '<div id="contextmenu"></div>'
  };
})(jQuery, window, document);
(function($, window, document, undefined) {

  $.fn.cssTable = function(parameters) {

    var settings = $.extend(true, {}, $.fn.cssTable.settings, parameters),

      $module = $(this),

      element = this,
      performance = [],

      className = settings.className,
      metadata = settings.metadata,
      selector = settings.selector,
      error = settings.error,

      eventNamespace = '.' + settings.namespace,
      moduleNamespace = 'module-' + settings.namespace,

      instance = $module.data(moduleNamespace),

      query = arguments[0],
      methodInvoked = (instance !== undefined && typeof query == 'string'),
      queryArguments = [].slice.call(arguments, 1),

      module,
      returnedValue;
	  
	titleMap = {
		del : '你确定要删除这条记录吗？',
		delBatch : '你确定要删除这些记录吗？',
		delAll : '你确定要清空所有记录吗？',
		batch : '你确定要操作这些记录吗？',
		recover : '你确定要还原这条记录吗？',
		recoverBatch : '你确定要还原这些记录吗？',
		exportBatch : '你确定要公文包吗？',
		transmitBatch : '你确定要转收文吗？',
		alertBatch:'请选中一条数据！',
		setJsonBatch:'你确定要设置班制吗？',
		release : '你确定要发布这条日程吗？',
		releaseBatch : '你确定要发布这些日程吗？',
		cancelRelease : '你确定要取消发布这条日程吗？',
		cancelReleaseBatch : '你确定要取消发布这些日程吗？',
		sub : '你确定要提交这条记录吗？',
		subBatch : '你确定要提交这些记录吗？'
	};
    module = {
      getCheck:function(){
		return $css.checkedVal('ids',$module);
      },
      batch:function(params){
		var ids=$css.checkedVal('ids',$module);
		if(ids.length>0){
			var url,title = titleMap.batch;
			if (typeof(params) === 'string')
			  url=params;
			else{
			  url=params.url;
			  if(params.title)
				  title=params.title;
			}
			$css.confirm(title,function(){
			  $css.post(url,{'ids':ids},function(data){
				if(params.callback){
				  params.callback(data);
				}
				else if(data.result==0){              
				  $css.tip(data.msg);
				  $module.submit();
				}else{
				  $css.alert(data.msg);
				}
			  },'json');
			});
			return false;
		} else {
		   $css.alert(titleMap.alertBatch);
		}
		return false;
      },
      order:function(params){	
    	  var url=params;
    	  $css.post(url,'',function(data){
			if(data.result==0){              
			  $module.submit();
			}else{
			  $css.alert(data.msg);
			}
		  },'json');
    	  return false;
      },
      updBatch:function(params){		
	    var ids=$css.checkedVal('ids',$module);
		if(ids.length>0){
			if(params.url.indexOf('?')==-1){params.url += "?";}
			params.url += "&ids="+ids;
			$css.openDialog(params);
			return false;
		} else {
		   $css.alert(titleMap.alertBatch);
		}
        return false;
      },
      dlgBatch:function(params){		
    	    var ids=$css.checkedVal('ids',$module);
    		if(ids.length>0){  			 
    			var url='';
    			if (typeof(params) === 'string')
    			  url=params;
    			else{
    			  url=params.url;  			   
    			}
    			
    			if(url.indexOf('?')>0)
    				url+="&ids="+ids;
    			else
    				url+="?ids="+ids;
    			 
    			if (typeof(params) === 'string')
    					params=url;
      			else{
      			   params.url=url;  			   
      		}  			 
    			$css.openDialog(params);
    			return false;
    		} else {
    		   $css.alert(titleMap.alertBatch);
    		}
            return false;
          },
      delBatch:function(params){
		var ids=$css.checkedVal('ids',$module);
		if(ids.length>0){
		   var url,title = titleMap.delBatch;
			if (typeof(params) === 'string')
			  url=params;
			else{
			  url=params.url;
			  if(params.title)
				  title=params.title;
			}
			$css.confirm(title,function(){
			  $css.post(url,{'ids':ids},function(data){
				if(params.callback){
				  params.callback(data);
				}
				else if(data.result==0){              
				  $css.tip(data.msg);
				  $module.submit();
				}else{
				  $css.alert(data.msg);
				}
			  },'json');
			});
		} else {
		   $css.alert(titleMap.alertBatch);
		}
		return false;   	
      },
      releaseBatch:function(params){
  		var ids=$css.checkedVal('ids',$module);
  		if(ids.length>0){
  		   var url,title = titleMap.releaseBatch;
  			if (typeof(params) === 'string')
  			  url=params;
  			else{
  			  url=params.url;
  			  if(params.title)
  				  title=params.title;
  			}
  			$css.confirm(title,function(){
  			  $css.post(url,{'ids':ids},function(data){
  				if(params.callback){
  				  params.callback(data);
  				}
  				else if(data.result==0){              
  				  $css.tip(data.msg);
  				  $module.submit();
  				}else{
  				  $css.alert(data.msg);
  				}
  			  },'json');
  			});
  		} else {
  		   $css.alert(titleMap.alertBatch);
  		}
  		return false;   	
        },
        cancelReleaseBatch:function(params){
      		var ids=$css.checkedVal('ids',$module);
      		if(ids.length>0){
      		   var url,title = titleMap.cancelReleaseBatch;
      			if (typeof(params) === 'string')
      			  url=params;
      			else{
      			  url=params.url;
      			  if(params.title)
      				  title=params.title;
      			}
      			$css.confirm(title,function(){
      			  $css.post(url,{'ids':ids},function(data){
      				if(params.callback){
      				  params.callback(data);
      				}
      				else if(data.result==0){              
      				  $css.tip(data.msg);
      				  $module.submit();
      				}else{
      				  $css.alert(data.msg);
      				}
      			  },'json');
      			});
      		} else {
      		   $css.alert(titleMap.alertBatch);
      		}
      		return false;   	
            },
        subBatch:function(params){
      		var ids=$css.checkedVal('ids',$module);
      		if(ids.length>0){
      		   var url,title = titleMap.subBatch;
      			if (typeof(params) === 'string')
      			  url=params;
      			else{
      			  url=params.url;
      			  if(params.title)
      				  title=params.title;
      			}
      			$css.confirm(title,function(){
      			  $css.post(url,{'ids':ids},function(data){
      				if(params.callback){
      				  params.callback(data);
      				}
      				else if(data.result==0){              
      				  $css.tip(data.msg);
      				  $module.submit();
      				}else{
      				  $css.alert(data.msg);
      				}
      			  },'json');
      			});
      		} else {
      		   $css.alert(titleMap.alertBatch);
      		}
      		return false;   	
            },
      delAll:function(params){
  		var url,title = titleMap.delAll;
  		if (typeof(params) === 'string')
  		  url=params;
  		else{
  		  url=params.url;
  		  if(params.title)
  			  title=params.title;
  		}
  		$css.confirm(title,function(){
  		  $css.post(url,{},function(data){
  			if(params.callback){
  			  params.callback(data);
  			}
  			else if(data.result==0){              
  			  $css.tip(data.msg);
  			  $module.submit();
  			}else{
  			  $css.alert(data.msg);
  			}
  		  },'json');
  		});
      },
      del:function(params){
		var url,title = titleMap.del;
		if (typeof(params) === 'string')
		  url=params;
		else{
		  url=params.url;
		  if(params.title)
			  title=params.title;
		}
		$css.confirm(title,function(){
		  $css.post(url,{},function(data){
			if(params.callback){
			  params.callback(data);
			}
			else if(data.result==0){              
			  $css.tip(data.msg);
			  $module.submit();
			}else{
			  $css.alert(data.msg);
			}
		  },'json');
		});
      },
      release:function(params){
  		var url,title = titleMap.release;
  		if (typeof(params) === 'string')
  		  url=params;
  		else{
  		  url=params.url;
  		  if(params.title)
  			  title=params.title;
  		}
  		$css.confirm(title,function(){
  		  $css.post(url,{},function(data){
  			if(params.callback){
  			  params.callback(data);
  			}
  			else if(data.result==0){              
  			  $css.tip(data.msg);
  			  $module.submit();
  			}else{
  			  $css.alert(data.msg);
  			}
  		  },'json');
  		});
        },
        cancelRelease:function(params){
      		var url,title = titleMap.cancelRelease;
      		if (typeof(params) === 'string')
      		  url=params;
      		else{
      		  url=params.url;
      		  if(params.title)
      			  title=params.title;
      		}
      		$css.confirm(title,function(){
      		  $css.post(url,{},function(data){
      			if(params.callback){
      			  params.callback(data);
      			}
      			else if(data.result==0){              
      			  $css.tip(data.msg);
      			  $module.submit();
      			}else{
      			  $css.alert(data.msg);
      			}
      		  },'json');
      		});
            },
        sub:function(params){
      		var url,title = titleMap.sub;
      		if (typeof(params) === 'string')
      		  url=params;
      		else{
      		  url=params.url;
      		  if(params.title)
      			  title=params.title;
      		}
      		$css.confirm(title,function(){
      		  $css.post(url,{},function(data){
      			if(params.callback){
      			  params.callback(data);
      			}
      			else if(data.result==0){              
      			  $css.tip(data.msg);
      			  $module.submit();
      			}else{
      			  $css.alert(data.msg);
      			}
      		  },'json');
      		});
            },
      setJsonBatch:function(params){
		var ids=$css.checkedVal('ids',$module);
		if(ids.length>0){
		   var url,title = titleMap.delBatch;
			if (typeof(params) === 'string')
			  url=params;
			else{
			  url=params.url;
			  if(params.title)
				  title=params.title;
			}
			$css.confirm(title,function(){
			  $css.post(url,{'ids':ids},function(data){
				if(params.callback){
				  params.callback(data);
				}
				else if(data.result==0){              
				  $css.tip(data.msg);
				  $module.submit();
				}else{
				  $css.alert(data.msg);
				}
			  },'json');
			});
		} else {
		   $css.alert(titleMap.alertBatch);
		}
		return false;   	
      },
      recover:function(params){
  		var url,title = titleMap.recover;
  		if (typeof(params) === 'string')
  		  url=params;
  		else{
  		  url=params.url;
  		  if(params.title)
  			  title=params.title;
  		}
  		$css.confirm(title,function(){
  		  $css.post(url,{},function(data){
  			if(params.callback){
  			  params.callback(data);
  			}
  			else if(data.result==0){              
  			  $css.tip(data.msg);
  			  $module.submit();
  			}else{
  			  $css.alert(data.msg);
  			}
  		  },'json');
  		});
      },
      recoverBatch:function(params){
  		var ids=$css.checkedVal('ids',$module);
  		if(ids.length>0){
  		   var url,title = titleMap.recoverBatch;
  			if (typeof(params) === 'string')
  			  url=params;
  			else{
  			  url=params.url;
  			  if(params.title)
  				  title=params.title;
  			}
  			$css.confirm(title,function(){
  			  $css.post(url,{'ids':ids},function(data){
  				if(params.callback){
  				  params.callback(data);
  				}
  				else if(data.result==0){              
  				  $css.tip(data.msg);
  				  $module.submit();
  				}else{
  				  $css.alert(data.msg);
  				}
  			  },'json');
  			});
  		} else {
  		   $css.alert(titleMap.alertBatch);
  		}
  		return false;   	
      },
      exportBatch:function(params){
    		var ids=$css.checkedVal('ids',$module);
    		if(ids.length>0){
    		   var url,title = titleMap.exportBatch;
    			if (typeof(params) === 'string')
    			  url=params;
    			else{
    			  url=params.url;
    			  if(params.title)
    				  title=params.title;
    			}
    			$css.confirm(title,function(){
    			  $css.post(url,{'ids':ids},function(data){
    				if(params.callback){
    				  params.callback(data);
    				}
    				else if(data.result==0){              
    				  $css.tip(data.msg);
    				  $module.submit();
    				}else{
    				  $css.alert(data.msg);
    				}
    			  },'json');
    			});
    		} else {
    		   $css.alert(titleMap.alertBatch);
    		}
    		return false;   	
      },
      transmitBatch:function(params){
    		var ids=$css.checkedVal('ids',$module);
    		if(ids.length>0){
//    			$css.alert(params);
//    			$css.alert(ids);
//    			$css.alert(ids.length);
//    			$css.alert(typeof ids);
//    			$css.alert(typeof params);
    		   var url,title = titleMap.transmitBatch;
    			if (typeof(params) === 'string')
    			  url=params;
    			else{
    			  url=params.url;
    			  if(params.title)
    				  title=params.title;
    			}
    			if(url.indexOf('?')>0)
    				url+="&ids="+ids;
    			else
    				url+="?ids="+ids;
    			 
//    			if (typeof(params) === 'string')
//    					params=url;
//      			else{
//      			   params.url=url;
      			   
//    			$css.openDialog({
//    				title:'',
//    				url:'',
//    				rel:'',
//    				lock:true
//    			}
//    			$css.confirm(title,function(){
//    			  $css.post(url,{'ids':ids},function(data){
//    				if(params.callback){
//    				  params.callback(data);
//    				}
//    				else if(data.result==0){              
//    				  $css.tip(data.msg);
//    				  $module.submit();
//    				}else{
//    				  $css.alert(data.msg);
//    				}
//    			  },'json');
//    			}
//    			);
//      			}
//    			params.url = url;
//    			params.title = title;
//    			$css.alert(url);
    			$css.openDialog({
    				title:title,
    				url:url,
    				rel:'transmitFW',
    				lock:true
    			});
    			return false;
    		} else {
    		   $css.alert(titleMap.alertBatch);
    		}
    		return false;   	
      },
      addData:function(){

      },
      editData:function(){

      },
      commonBatch:function(params){
  		var ids=$css.checkedVal('ids',$module);
  		if(ids.length>0){
  			var url,title = titleMap.batch;
  			if (typeof(params) === 'string')
  			  url=params;
  			else{
  			  url=params.url;
  			  if(params.title)
  				  title=params.title;
  			}
  			//$css.confirm(title,function(){
  			  $css.post(url,{'ids':ids},function(data){
  				if(params.callback){
  				  params.callback(data);
  				}
  				else if(data.result==0){              
  				  $css.tip(data.msg);
  				  $module.submit();
  				}else{
  				  $css.alert(data.msg);
  				}
  			  },'json');
  			//});
  			return false;
  		} else {
  		   $css.alert(titleMap.alertBatch);
  		}
  		return false;
      },
      _init:function(){
        $module.find('.expand').click(function(){
          var $this=$(this),
              $i=$this.find('i'),
              $sub=$this.parents('tr').next('.sub');
              cla=$i.attr('class');
          if(cla=="icon_arrow_up"){
            $i.attr('class','icon_arrow_down');
            $sub.hide();
          }else{
            if(!$sub[0]){
              $sub=$('<tr class="sub"><td colspan="'+$this.parents('tr').find('td').length+'">详细内容</td></tr>');
              $sub.insertAfter($(this).parents('tr'));
            }
            $i.attr('class','icon_arrow_up');
            $sub.show();
          }
        });
        $module.find('.btnTab a').click(function(){
          var $this=$(this);
          $this.parent().find('input').val($this.attr('value'));
          $module.find('.page-current').val(1);
		  $module.submit();
        });
        $(".btnTab a[value='"+$('.btnTab input',$module).val()+"']",$module).addClass('active');
        var os = $('.order-string',$module).val();
        if(os) 
        	os = os.replace('.','\\\.');
        $('[order-field='+os+']',$module).attr('class',$('.order-flag',$module).val()==1?'asc':'desc');
      },
      event: {
      },
      initialize: function() {
        module.instantiate();
        module._init();
      },
      instantiate: function() {
        $module
          .data(moduleNamespace, module);
      },

      destroy: function() {
        $module
          .removeData(moduleNamespace)
          .off(eventNamespace);
      },

      error: function() {
        module.error = Function.prototype.bind.call(console.error, console, settings.name + ':');
        module.error.apply(console, arguments);
      },

      invoke: function(query, passedArguments, context) {
        var
        maxDepth,
          found,
          response;
        passedArguments = passedArguments || queryArguments;
        context = element || context;
        if (typeof query == 'string' && instance !== undefined) {
          query = query.split(/[\. ]/);
          maxDepth = query.length - 1;
          $.each(query, function(depth, value) {
            var camelCaseValue = (depth != maxDepth) ? value + query[depth + 1].charAt(0).toUpperCase() + query[depth + 1].slice(1) : query;
            if ($.isPlainObject(instance[value]) && (depth != maxDepth)) {
              instance = instance[value];
            } else if ($.isPlainObject(instance[camelCaseValue]) && (depth != maxDepth)) {
              instance = instance[camelCaseValue];
            } else if (instance[value] !== undefined) {
              found = instance[value];
              return false;
            } else if (instance[camelCaseValue] !== undefined) {
              found = instance[camelCaseValue];
              return false;
            } else {
              module.error(error.method, query);
              return false;
            }
          });
        }

        if ($.isFunction(found)) {
          response = found.apply(context, passedArguments);
        } else if (found !== undefined) {
          response = found;
        }
        if ($.isArray(returnedValue)) {
          returnedValue.push(response);
        } else if (returnedValue !== undefined) {
          returnedValue = [returnedValue, response];
        } else if (response !== undefined) {
          returnedValue = response;
        }
        return found;
      }
    };

    if (methodInvoked) {
      if (instance === undefined) {
        module.initialize();
      }
      module.invoke(query);
    } else {
      if (instance !== undefined) {
        module.destroy();
      }
      module.initialize();
    }

    return (returnedValue !== undefined) ? returnedValue : this;

  };

  $.cssTable = {
    focus:function(){
      return $.cssTab.focus().find('.table-form');
    },
  	act:function(a,b){
      return $.cssTable.focus().cssTable(a,b);
    }
  };

  $.fn.cssTable.settings = {

    name: 'Table',
    verbose: true,
    debug: true,
    performance: true,
    namespace: 'table',

    error: {
    },

    metadata: {
      tab: 'tab',
      loaded: 'loaded',
      promise: 'promise'
    },

    className: {
    },

    selector: {
    }

  };

})(jQuery, window, document);
(function($) {
	function _1(_2) {
		$(_2).addClass("droppable");
		$(_2).bind("_dragenter", function(e, _3) {
			$.data(_2, "droppable").options.onDragEnter.apply(_2, [e, _3]);
		});
		$(_2).bind("_dragleave", function(e, _4) {
			$.data(_2, "droppable").options.onDragLeave.apply(_2, [e, _4]);
		});
		$(_2).bind("_dragover", function(e, _5) {
			$.data(_2, "droppable").options.onDragOver.apply(_2, [e, _5]);
		});
		$(_2).bind("_drop", function(e, _6) {
			$.data(_2, "droppable").options.onDrop.apply(_2, [e, _6]);
		});
	};
	$.fn.droppable = function(_7, _8) {
		if (typeof _7 == "string") {
			return $.fn.droppable.methods[_7](this, _8);
		}
		_7 = _7 || {};
		return this.each(function() {
			var _9 = $.data(this, "droppable");
			if (_9) {
				$.extend(_9.options, _7);
			} else {
				_1(this);
				$.data(this, "droppable", {
					options: $.extend({}, $.fn.droppable.defaults, $.fn.droppable.parseOptions(this), _7)
				});
			}
		});
	};
	$.fn.droppable.methods = {
		options: function(jq) {
			return $.data(jq[0], "droppable").options;
		},
		enable: function(jq) {
			return jq.each(function() {
				$(this).droppable({
					disabled: false
				});
			});
		},
		disable: function(jq) {
			return jq.each(function() {
				$(this).droppable({
					disabled: true
				});
			});
		}
	};
	$.fn.droppable.parseOptions = function(_a) {
		var t = $(_a);
		return $.extend({}, $.parser.parseOptions(_a, ["accept"]), {
			disabled: (t.attr("disabled") ? true : undefined)
		});
	};
	$.fn.droppable.defaults = {
		accept: null,
		disabled: false,
		onDragEnter: function(e, _b) {},
		onDragOver: function(e, _c) {},
		onDragLeave: function(e, _d) {},
		onDrop: function(e, _e) {}
	};
})(jQuery);
(function($) {
	function _1(e) {
		var _2 = $.data(e.data.target, "draggable");
		var _3 = _2.options;
		var _4 = _2.proxy;
		var _5 = e.data;
		var _6 = _5.startLeft + e.pageX - _5.startX;
		var _7 = _5.startTop + e.pageY - _5.startY;
		if (_4) {
			if (_4.parent()[0] == document.body) {
				if (_3.deltaX != null && _3.deltaX != undefined) {
					_6 = e.pageX + _3.deltaX;
				} else {
					_6 = e.pageX - e.data.offsetWidth;
				}
				if (_3.deltaY != null && _3.deltaY != undefined) {
					_7 = e.pageY + _3.deltaY;
				} else {
					_7 = e.pageY - e.data.offsetHeight;
				}
			} else {
				if (_3.deltaX != null && _3.deltaX != undefined) {
					_6 += e.data.offsetWidth + _3.deltaX;
				}
				if (_3.deltaY != null && _3.deltaY != undefined) {
					_7 += e.data.offsetHeight + _3.deltaY;
				}
			}
		}
		if (e.data.parent != document.body) {
			_6 += $(e.data.parent).scrollLeft();
			_7 += $(e.data.parent).scrollTop();
		}
		if (_3.axis == "h") {
			_5.left = _6;
		} else {
			if (_3.axis == "v") {
				_5.top = _7;
			} else {
				_5.left = _6;
				_5.top = _7;
			}
		}
	};

	function _8(e) {
		var _9 = $.data(e.data.target, "draggable");
		var _a = _9.options;
		var _b = _9.proxy;
		if (!_b) {
			_b = $(e.data.target);
		}
		_b.css({
			left: e.data.left,
			top: e.data.top
		});
		$("body").css("cursor", _a.cursor);
	};

	function _c(e) {
		$.fn.draggable.isDragging = true;
		var _d = $.data(e.data.target, "draggable");
		var _e = _d.options;
		var _f = $(".droppable").filter(function() {
			return e.data.target != this;
		}).filter(function() {
			var _10 = $.data(this, "droppable").options.accept;
			if (_10) {
				return $(_10).filter(function() {
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});
		_d.droppables = _f;
		var _11 = _d.proxy;
		if (!_11) {
			if (_e.proxy) {
				if (_e.proxy == "clone") {
					_11 = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					_11 = _e.proxy.call(e.data.target, e.data.target);
				}
				_d.proxy = _11;
			} else {
				_11 = $(e.data.target);
			}
		}
		_11.css("position", "absolute");
		_1(e);
		_8(e);
		_e.onStartDrag.call(e.data.target, e);
		return false;
	};

	function _12(e) {
		var _13 = $.data(e.data.target, "draggable");
		_1(e);
		if (_13.options.onDrag.call(e.data.target, e) != false) {
			_8(e);
		}
		var _14 = e.data.target;
		_13.droppables.each(function() {
			var _15 = $(this);
			if (_15.droppable("options").disabled) {
				return;
			}
			var p2 = _15.offset();
			if (e.pageX > p2.left && e.pageX < p2.left + _15.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + _15.outerHeight()) {
				if (!this.entered) {
					$(this).trigger("_dragenter", [_14]);
					this.entered = true;
				}
				$(this).trigger("_dragover", [_14]);
			} else {
				if (this.entered) {
					$(this).trigger("_dragleave", [_14]);
					this.entered = false;
				}
			}
		});
		return false;
	};

	function _16(e) {
		$.fn.draggable.isDragging = false;
		_12(e);
		var _17 = $.data(e.data.target, "draggable");
		var _18 = _17.proxy;
		var _19 = _17.options;
		if (_19.revert) {
			if (_1a() == true) {
				$(e.data.target).css({
					position: e.data.startPosition,
					left: e.data.startLeft,
					top: e.data.startTop
				});
			} else {
				if (_18) {
					var _1b, top;
					if (_18.parent()[0] == document.body) {
						_1b = e.data.startX - e.data.offsetWidth;
						top = e.data.startY - e.data.offsetHeight;
					} else {
						_1b = e.data.startLeft;
						top = e.data.startTop;
					}
					_18.animate({
						left: _1b,
						top: top
					}, function() {
						_1c();
					});
				} else {
					$(e.data.target).animate({
						left: e.data.startLeft,
						top: e.data.startTop
					}, function() {
						$(e.data.target).css("position", e.data.startPosition);
					});
				}
			}
		} else {
			$(e.data.target).css({
				position: "absolute",
				left: e.data.left,
				top: e.data.top
			});
			_1a();
		}
		_19.onStopDrag.call(e.data.target, e);
		$(document).unbind(".draggable");
		setTimeout(function() {
			$("body").css("cursor", "");
		}, 100);

		function _1c() {
			if (_18) {
				_18.remove();
			}
			_17.proxy = null;
		};

		function _1a() {
			var _1d = false;
			_17.droppables.each(function() {
				var _1e = $(this);
				if (_1e.droppable("options").disabled) {
					return;
				}
				var p2 = _1e.offset();
				if (e.pageX > p2.left && e.pageX < p2.left + _1e.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + _1e.outerHeight()) {
					if (_19.revert) {
						$(e.data.target).css({
							position: e.data.startPosition,
							left: e.data.startLeft,
							top: e.data.startTop
						});
					}
					$(this).trigger("_drop", [e.data.target]);
					_1c();
					_1d = true;
					this.entered = false;
					return false;
				}
			});
			if (!_1d && !_19.revert) {
				_1c();
			}
			return _1d;
		};
		return false;
	};
	$.fn.draggable = function(_1f, _20) {
		if (typeof _1f == "string") {
			return $.fn.draggable.methods[_1f](this, _20);
		}
		return this.each(function() {
			var _21;
			var _22 = $.data(this, "draggable");
			if (_22) {
				_22.handle.unbind(".draggable");
				_21 = $.extend(_22.options, _1f);
			} else {
				_21 = $.extend({}, $.fn.draggable.defaults, $.fn.draggable.parseOptions(this), _1f || {});
			}
			var _23 = _21.handle ? (typeof _21.handle == "string" ? $(_21.handle, this) : _21.handle) : $(this);
			$.data(this, "draggable", {
				options: _21,
				handle: _23
			});
			if (_21.disabled) {
				$(this).css("cursor", "");
				return;
			}
			_23.unbind(".draggable").bind("mousemove.draggable", {
				target: this
			}, function(e) {
				if ($.fn.draggable.isDragging) {
					return;
				}
				var _24 = $.data(e.data.target, "draggable").options;
				if (_25(e)) {
					$(this).css("cursor", _24.cursor);
				} else {
					$(this).css("cursor", "");
				}
			}).bind("mouseleave.draggable", {
				target: this
			}, function(e) {
				$(this).css("cursor", "");
			}).bind("mousedown.draggable", {
				target: this
			}, function(e) {
				if (_25(e) == false) {
					return;
				}
				$(this).css("cursor", "");
				var _26 = $(e.data.target).position();
				var _27 = $(e.data.target).offset();
				var _28 = {
					startPosition: $(e.data.target).css("position"),
					startLeft: _26.left,
					startTop: _26.top,
					left: _26.left,
					top: _26.top,
					startX: e.pageX,
					startY: e.pageY,
					offsetWidth: (e.pageX - _27.left),
					offsetHeight: (e.pageY - _27.top),
					target: e.data.target,
					parent: $(e.data.target).parent()[0]
				};
				$.extend(e.data, _28);
				var _29 = $.data(e.data.target, "draggable").options;
				if (_29.onBeforeDrag.call(e.data.target, e) == false) {
					return;
				}
				$(document).bind("mousedown.draggable", e.data, _c);
				$(document).bind("mousemove.draggable", e.data, _12);
				$(document).bind("mouseup.draggable", e.data, _16);
			});

			function _25(e) {
				var _2a = $.data(e.data.target, "draggable");
				var _2b = _2a.handle;
				var _2c = $(_2b).offset();
				var _2d = $(_2b).outerWidth();
				var _2e = $(_2b).outerHeight();
				var t = e.pageY - _2c.top;
				var r = _2c.left + _2d - e.pageX;
				var b = _2c.top + _2e - e.pageY;
				var l = e.pageX - _2c.left;
				return Math.min(t, r, b, l) > _2a.options.edge;
			};
		});
	};
	$.fn.draggable.methods = {
		options: function(jq) {
			return $.data(jq[0], "draggable").options;
		},
		proxy: function(jq) {
			return $.data(jq[0], "draggable").proxy;
		},
		enable: function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled: false
				});
			});
		},
		disable: function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled: true
				});
			});
		}
	};
	$.fn.draggable.parseOptions = function(_2f) {
		var t = $(_2f);
		return $.extend({}, $.parser.parseOptions(_2f, ["cursor", "handle", "axis", {
			"revert": "boolean",
			"deltaX": "number",
			"deltaY": "number",
			"edge": "number"
		}]), {
			disabled: (t.attr("disabled") ? true : undefined)
		});
	};
	$.fn.draggable.defaults = {
		proxy: null,
		revert: false,
		cursor: "move",
		deltaX: null,
		deltaY: null,
		handle: null,
		disabled: false,
		edge: 0,
		axis: null,
		onBeforeDrag: function(e) {},
		onStartDrag: function(e) {},
		onDrag: function(e) {},
		onStopDrag: function(e) {}
	};
	$.fn.draggable.isDragging = false;
})(jQuery);
(function($) {
	$.parser = {
		auto: true,
		onComplete: function(_1) {},
		plugins: ["draggable", "droppable", "resizable", "pagination", "tooltip", "linkbutton", "menu", "menubutton", "splitbutton", "progressbar", "tree", "combobox", "combotree", "combogrid", "numberbox", "validatebox", "searchbox", "numberspinner", "timespinner", "calendar", "datebox", "datetimebox", "slider", "layout", "panel", "datagrid", "propertygrid", "treegrid", "tabs", "accordion", "window", "dialog"],
		parse: function(_2) {
			var aa = [];
			for (var i = 0; i < $.parser.plugins.length; i++) {
				var _3 = $.parser.plugins[i];
				var r = $(".easyui-" + _3, _2);
				if (r.length) {
					if (r[_3]) {
						r[_3]();
					} else {
						aa.push({
							name: _3,
							jq: r
						});
					}
				}
			}
			if (aa.length && window.easyloader) {
				var _4 = [];
				for (var i = 0; i < aa.length; i++) {
					_4.push(aa[i].name);
				}
				easyloader.load(_4, function() {
					for (var i = 0; i < aa.length; i++) {
						var _5 = aa[i].name;
						var jq = aa[i].jq;
						jq[_5]();
					}
					$.parser.onComplete.call($.parser, _2);
				});
			} else {
				$.parser.onComplete.call($.parser, _2);
			}
		},
		parseOptions: function(_6, _7) {
			var t = $(_6);
			var _8 = {};
			var s = $.trim(t.attr("data-options"));
			if (s) {
				if (s.substring(0, 1) != "{") {
					s = "{" + s + "}";
				}
				_8 = (new Function("return " + s))();
			}
			if (_7) {
				var _9 = {};
				for (var i = 0; i < _7.length; i++) {
					var pp = _7[i];
					if (typeof pp == "string") {
						if (pp == "width" || pp == "height" || pp == "left" || pp == "top") {
							_9[pp] = parseInt(_6.style[pp]) || undefined;
						} else {
							_9[pp] = t.attr(pp);
						}
					} else {
						for (var _a in pp) {
							var _b = pp[_a];
							if (_b == "boolean") {
								_9[_a] = t.attr(_a) ? (t.attr(_a) == "true") : undefined;
							} else {
								if (_b == "number") {
									_9[_a] = t.attr(_a) == "0" ? 0 : parseFloat(t.attr(_a)) || undefined;
								}
							}
						}
					}
				}
				$.extend(_8, _9);
			}
			return _8;
		}
	};
	$(function() {
		var d = $("<div style=\"position:absolute;top:-1000px;width:100px;height:100px;padding:5px\"></div>").appendTo("body");
		d.width(100);
		$._boxModel = parseInt(d.width()) == 100;
		d.remove();
		if (!window.easyloader && $.parser.auto) {
			$.parser.parse();
		}
	});
	$.fn._outerWidth = function(_c) {
		if (_c == undefined) {
			if (this[0] == window) {
				return this.width() || document.body.clientWidth;
			}
			return this.outerWidth() || 0;
		}
		return this.each(function() {
			if ($._boxModel) {
				$(this).width(_c - ($(this).outerWidth() - $(this).width()));
			} else {
				$(this).width(_c);
			}
		});
	};
	$.fn._outerHeight = function(_d) {
		if (_d == undefined) {
			if (this[0] == window) {
				return this.height() || document.body.clientHeight;
			}
			return this.outerHeight() || 0;
		}
		return this.each(function() {
			if ($._boxModel) {
				$(this).height(_d - ($(this).outerHeight() - $(this).height()));
			} else {
				$(this).height(_d);
			}
		});
	};
	$.fn._scrollLeft = function(_e) {
		if (_e == undefined) {
			return this.scrollLeft();
		} else {
			return this.each(function() {
				$(this).scrollLeft(_e);
			});
		}
	};
	$.fn._propAttr = $.fn.prop || $.fn.attr;
	$.fn._fit = function(_f) {
		_f = _f == undefined ? true : _f;
		var t = this[0];
		var p = (t.tagName == "BODY" ? t : this.parent()[0]);
		var _10 = p.fcount || 0;
		if (_f) {
			if (!t.fitted) {
				t.fitted = true;
				p.fcount = _10 + 1;
				$(p).addClass("panel-noscroll");
				if (p.tagName == "BODY") {
					$("html").addClass("panel-fit");
				}
			}
		} else {
			if (t.fitted) {
				t.fitted = false;
				p.fcount = _10 - 1;
				if (p.fcount == 0) {
					$(p).removeClass("panel-noscroll");
					if (p.tagName == "BODY") {
						$("html").removeClass("panel-fit");
					}
				}
			}
		}
		return {
			width: $(p).width(),
			height: $(p).height()
		};
	};
})(jQuery);
(function($) {
	var _11 = null;
	var _12 = null;
	var _13 = false;

	function _14(e) {
		if (e.touches.length != 1) {
			return;
		}
		if (!_13) {
			_13 = true;
			dblClickTimer = setTimeout(function() {
				_13 = false;
			}, 500);
		} else {
			clearTimeout(dblClickTimer);
			_13 = false;
			_15(e, "dblclick");
		}
		_11 = setTimeout(function() {
			_15(e, "contextmenu", 3);
		}, 1000);
		_15(e, "mousedown");
		if ($.fn.draggable.isDragging || $.fn.resizable.isResizing) {
			e.preventDefault();
		}
	};

	function _16(e) {
		if (e.touches.length != 1) {
			return;
		}
		if (_11) {
			clearTimeout(_11);
		}
		_15(e, "mousemove");
		if ($.fn.draggable.isDragging || $.fn.resizable.isResizing) {
			e.preventDefault();
		}
	};

	function _17(e) {
		if (_11) {
			clearTimeout(_11);
		}
		_15(e, "mouseup");
		if ($.fn.draggable.isDragging || $.fn.resizable.isResizing) {
			e.preventDefault();
		}
	};

	function _15(e, _18, _19) {
		var _1a = new $.Event(_18);
		_1a.pageX = e.changedTouches[0].pageX;
		_1a.pageY = e.changedTouches[0].pageY;
		_1a.which = _19 || 1;
		$(e.target).trigger(_1a);
	};
	if (document.addEventListener) {
		document.addEventListener("touchstart", _14, true);
		document.addEventListener("touchmove", _16, true);
		document.addEventListener("touchend", _17, true);
	}
})(jQuery);
(function($) {
	$.fn.resizable = function(_1, _2) {
		if (typeof _1 == "string") {
			return $.fn.resizable.methods[_1](this, _2);
		}

		function _3(e) {
			var _4 = e.data;
			var _5 = $.data(_4.target, "resizable").options;
			if (_4.dir.indexOf("e") != -1) {
				var _6 = _4.startWidth + e.pageX - _4.startX;
				_6 = Math.min(Math.max(_6, _5.minWidth), _5.maxWidth);
				_4.width = _6;
			}
			if (_4.dir.indexOf("s") != -1) {
				var _7 = _4.startHeight + e.pageY - _4.startY;
				_7 = Math.min(Math.max(_7, _5.minHeight), _5.maxHeight);
				_4.height = _7;
			}
			if (_4.dir.indexOf("w") != -1) {
				var _6 = _4.startWidth - e.pageX + _4.startX;
				_6 = Math.min(Math.max(_6, _5.minWidth), _5.maxWidth);
				_4.width = _6;
				_4.left = _4.startLeft + _4.startWidth - _4.width;
			}
			if (_4.dir.indexOf("n") != -1) {
				var _7 = _4.startHeight - e.pageY + _4.startY;
				_7 = Math.min(Math.max(_7, _5.minHeight), _5.maxHeight);
				_4.height = _7;
				_4.top = _4.startTop + _4.startHeight - _4.height;
			}
		};

		function _8(e) {
			var _9 = e.data;
			var t = $(_9.target);
			t.css({
				left: _9.left,
				top: _9.top
			});
			if (t.outerWidth() != _9.width) {
				t._outerWidth(_9.width);
			}
			if (t.outerHeight() != _9.height) {
				t._outerHeight(_9.height);
			}
		};

		function _a(e) {
			$.fn.resizable.isResizing = true;
			$.data(e.data.target, "resizable").options.onStartResize.call(e.data.target, e);
			return false;
		};

		function _b(e) {
			_3(e);
			if ($.data(e.data.target, "resizable").options.onResize.call(e.data.target, e) != false) {
				_8(e);
			}
			return false;
		};

		function _c(e) {
			$.fn.resizable.isResizing = false;
			_3(e, true);
			_8(e);
			$.data(e.data.target, "resizable").options.onStopResize.call(e.data.target, e);
			$(document).unbind(".resizable");
			$("body").css("cursor", "");
			return false;
		};
		return this.each(function() {
			var _d = null;
			var _e = $.data(this, "resizable");
			if (_e) {
				$(this).unbind(".resizable");
				_d = $.extend(_e.options, _1 || {});
			} else {
				_d = $.extend({}, $.fn.resizable.defaults, $.fn.resizable.parseOptions(this), _1 || {});
				$.data(this, "resizable", {
					options: _d
				});
			}
			if (_d.disabled == true) {
				return;
			}
			$(this).bind("mousemove.resizable", {
				target: this
			}, function(e) {
				if ($.fn.resizable.isResizing) {
					return;
				}
				var _f = _10(e);
				if (_f == "") {
					$(e.data.target).css("cursor", "");
				} else {
					$(e.data.target).css("cursor", _f + "-resize");
				}
			}).bind("mouseleave.resizable", {
				target: this
			}, function(e) {
				$(e.data.target).css("cursor", "");
			}).bind("mousedown.resizable", {
				target: this
			}, function(e) {
				var dir = _10(e);
				if (dir == "") {
					return;
				}

				function _11(css) {
					var val = parseInt($(e.data.target).css(css));
					if (isNaN(val)) {
						return 0;
					} else {
						return val;
					}
				};
				var _12 = {
					target: e.data.target,
					dir: dir,
					startLeft: _11("left"),
					startTop: _11("top"),
					left: _11("left"),
					top: _11("top"),
					startX: e.pageX,
					startY: e.pageY,
					startWidth: $(e.data.target).outerWidth(),
					startHeight: $(e.data.target).outerHeight(),
					width: $(e.data.target).outerWidth(),
					height: $(e.data.target).outerHeight(),
					deltaWidth: $(e.data.target).outerWidth() - $(e.data.target).width(),
					deltaHeight: $(e.data.target).outerHeight() - $(e.data.target).height()
				};
				$(document).bind("mousedown.resizable", _12, _a);
				$(document).bind("mousemove.resizable", _12, _b);
				$(document).bind("mouseup.resizable", _12, _c);
				$("body").css("cursor", dir + "-resize");
			});

			function _10(e) {
				var tt = $(e.data.target);
				var dir = "";
				var _13 = tt.offset();
				var _14 = tt.outerWidth();
				var _15 = tt.outerHeight();
				var _16 = _d.edge;
				if (e.pageY > _13.top && e.pageY < _13.top + _16) {
					dir += "n";
				} else {
					if (e.pageY < _13.top + _15 && e.pageY > _13.top + _15 - _16) {
						dir += "s";
					}
				}
				if (e.pageX > _13.left && e.pageX < _13.left + _16) {
					dir += "w";
				} else {
					if (e.pageX < _13.left + _14 && e.pageX > _13.left + _14 - _16) {
						dir += "e";
					}
				}
				var _17 = _d.handles.split(",");
				for (var i = 0; i < _17.length; i++) {
					var _18 = _17[i].replace(/(^\s*)|(\s*$)/g, "");
					if (_18 == "all" || _18 == dir) {
						return dir;
					}
				}
				return "";
			};
		});
	};
	$.fn.resizable.methods = {
		options: function(jq) {
			return $.data(jq[0], "resizable").options;
		},
		enable: function(jq) {
			return jq.each(function() {
				$(this).resizable({
					disabled: false
				});
			});
		},
		disable: function(jq) {
			return jq.each(function() {
				$(this).resizable({
					disabled: true
				});
			});
		}
	};
	$.fn.resizable.parseOptions = function(_19) {
		var t = $(_19);
		return $.extend({}, $.parser.parseOptions(_19, ["handles", {
			minWidth: "number",
			minHeight: "number",
			maxWidth: "number",
			maxHeight: "number",
			edge: "number"
		}]), {
			disabled: (t.attr("disabled") ? true : undefined)
		});
	};
	$.fn.resizable.defaults = {
		disabled: false,
		handles: "n, e, s, w, ne, se, sw, nw, all",
		minWidth: 10,
		minHeight: 10,
		maxWidth: 10000,
		maxHeight: 10000,
		edge: 5,
		onStartResize: function(e) {},
		onResize: function(e) {},
		onStopResize: function(e) {}
	};
	$.fn.resizable.isResizing = false;
})(jQuery);