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