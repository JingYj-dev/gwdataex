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
        $ul.find('a').eq(ptabid).click();
        $more.next().find('li').eq(ptabid).addClass('active');
        $ul.find('a').eq(tabid).parent().remove();
        $content.find('.tab-pane').eq(tabid).remove();
        $more.next().find('li').eq(tabid).remove();
      },
      closeCurrent: function() {
          //module.close($ul.find('.active a').attr('tabid'));
    	  $ul.find('.active a .remove').click();
      },
      onCloseCurrent:function(callback){
      	$ul.find('.active a .remove').on('click',function(){
      		module.event.close;
      		if(typeof(callback)==="function"){
	        	callback($ul.find('.active a').attr('tabid'));
	        }
      	});
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
        return $module.width()-$tabBtn.width()-1;
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
            var $target = $($this.attr('href'));
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
                $target.load(url, function(data) {
                  $target.initUI();
                  unblockUI($module);
                  //$target.find('.set-btn').width($target.width()-10);
                  $target.find('[target=closeTab]').off().on('click',function(){
                    module.closeCurrent();
                  })
                });
              }
              $this.data('loaded', 1);
            }
          }
        },
        close: function(e) {
          e.stopPropagation();
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