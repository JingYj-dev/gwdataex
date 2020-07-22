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
		batch : '你确定要操作这些记录吗？',
		alertBatch:'请选中一条数据！'
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
      updBatch:function(params){		
	    var ids=$css.checkedVal('ids',$module);
		if(ids.length>0){
			if(!params.url.contains('?')){params.url += "?";}
			params.url += "&ids="+ids;
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
      addData:function(){

      },
      editData:function(){

      },commonBatch:function(params){
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
          if(cla=="icon-chevron-up"){
            $i.attr('class','icon-chevron-down');
            $sub.hide();
          }else{
            if(!$sub[0]){
              $sub=$('<tr class="sub"><td colspan="'+$this.parents('tr').find('td').length+'">详细内容</td></tr>');
              $sub.insertAfter($(this).parents('tr'));
            }
            $i.attr('class','icon-chevron-up');
            $sub.show();
          }
        });
        $module.find('.btnTab a').click(function(){
          var $this=$(this);
          $this.parent().find('input').val($this.attr('value'));
		  $module.submit();
        });
        $(".btnTab a[value='"+$('.btnTab input',$module).val()+"']",$module).addClass('active');
        $('[order-field='+$('.order-string',$module).val()+']',$module).attr('class',$('.order-flag',$module).val()==1?'asc':'desc');
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