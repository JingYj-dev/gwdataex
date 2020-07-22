function resetInputTree(that){
	$(that).parent().find('input').each(function(){
		$(this).val("");
	});
	//$("#getmenu_selSys").value("");
	var rtree = $.fn.zTree.getZTreeObj("getmenu_tree");
	if(rtree){
		//rtree.cancelSelectedNode();
	    var nodes = rtree.getCheckedNodes(true);
	    for (var i=0, l=nodes.length; i<l; i++) {
			rtree.checkNode(nodes[i], false, true, true);
		}
	}
}
var funcId = $("#getmenu_selFunc").val();
var getmenu_setting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		async: {
			enable: true,
			url:"getFuncTree.action",
			autoParam:["id=id"],
			otherParam: ["funcId", funcId],
			type:"post"
		},
		callback: {
			beforeClick : getmenu_beforeClick,
			onCheck: getmenu_onCheck,
			onAsyncSuccess : function(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if(treeObj.getSelectedNodes().length==0){
				var nodes = treeObj.getNodes();
//				treeObj.expandAll(nodes[0], true);
				treeObj.expandNode(nodes[0], true);
			}
		}
		}
	};

	var getmenu_zNodes = null;
	function getmenu_beforeClick(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("getmenu_tree");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	function getmenu_onCheck(e, treeId, treeNode) {
		//debugger;
		var zTree = $.fn.zTree.getZTreeObj("getmenu_tree"),
		nodes = zTree.getCheckedNodes(true),
		//v = "",
		v2 = "";
		v3 = "";
		v4 = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			//v += nodes[i].name + ",";
			v2 += nodes[i].sysId + ",";
			v3 += nodes[i].url + ",";
			v4 += nodes[i].funcId + ",";
		}
		//if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (v2.length > 0 ) v2 = v2.substring(0, v2.length-1);
		if (v3.length > 0 ) v3 = v3.substring(0, v3.length-1);
		if (v4.length > 0 ) v4 = v4.substring(0, v4.length-1);
		//var cityObj = $("#getmenu_sel");
		var cityObj2 = $("#getmenu_selSys");
		var cityObj3 = $("#url");
		var cityObj4 = $("#getmenu_selFunc");
		//cityObj.attr("value", v);
		cityObj2.val(v2);
		//cityObj3.attr("value","");
		cityObj4.val(v4);
		//if (v3 != "undefined" ) cityObj3.attr("value", v3);
		getmenu_hideMenu();
		if(isnull($("#getmenu_selFunc").val())){
			$("#removeTree").hide();
		}else{
			$("#removeTree").show();
		}
	}
	function getmenu_showMenu() {
		var top = document.getElementById("getmenu_selFunc").offsetTop + 62;
		document.getElementById("getmenu_menuContent").style.top = top + "px";
		$("#getmenu_menuContent").slideDown("fast");
		$("#add_menu").bind("mousedown", getmenu_onBodyDown);	
	}
	function getmenu_hideMenu() {
		$("#getmenu_menuContent").fadeOut("fast");
		$("#add_menu").unbind("mousedown", getmenu_onBodyDown);
	}
	function getmenu_onBodyDown(event) {
		if (!(event.target.id == "getmenu_menuBtn" || event.target.id == "getmenu_sel" 
				|| event.target.id == "getmenu_menuContent" || $(event.target).parents("#getmenu_menuContent").length>0)) {
			getmenu_hideMenu();
		}
	}
	using('tree',function(){
	    $.fn.zTree.init($("#getmenu_tree"), getmenu_setting,getmenu_zNodes);    
	})
	
	function addMenu(data,tabid){
	   dialogAjaxDone(data);
       if(data.result==0){
    	   var newNodes = [{"id":data.info.menuId,"name":data.info.menuName,"pId":data.info.parentId}];
           var node = rf_tree.getNodeByParam("id",$("#parentId").val(), null);
	       rf_tree.addNodes(node, newNodes);
	       var $form=$('#div_menu').find("#menu_form");
	       $form.submit();	
       }
       if(data.result==1){
			$css.alert(data.msg);
		}
	}
	
	function updMenu(data,tabid){
		dialogAjaxDone(data);
	    var oldNode = rf_tree.getNodeByParam("id",data.info.menuId, null);
	    oldNode.name = data.info.menuName;
	    rf_tree.updateNode(oldNode);
	    var $form=$('#div_menu').find("#menu_form");
       $form.submit();
	}
	
	$("#choose_icon_link").on("click",function(){
		//$css.openDialog('acl/menu/icon.jsp');
		var a={
            	id:'__chose_icon',
                title:'选择图标',
                lock:true,
                max:false,
                min:false,
                padding: 50,
                width: 800,
                height: 500
            }
            var api = $.dialog(a);
            $.ajax({
                url:'acl/menu/icon.jsp',
                success:function(data){
                    api.content(data);
                    $(api.DOM.content[0]).initUI();
                    //$(".ui_content",$.dialog.focus).attr("style","height: 490px;overflow-x: auto;width: 820px;");
                },
                cache:false
            });
	});