;using('ajaxupload');
function delImg(uuid,url,succ){
	$.dialog.confirm('你确定要删除该图片吗？', function(){
		var pars = {uuid:uuid};
		$.post(url,pars,function(data){
					switch(data.result){
						case 0:
						   if(typeof(succ)=="function")
						   {
							   succ(data);
						   }else{
					       	   $.dialog.tips("删除成功！",1,'success.gif');
							   if(typeof(succ)=="string"){
								   $("#"+succ+" .fileDownload").attr("src","./portal/images/icon/small_logo.jpg");
								   $("#"+succ+" .fileDownload").attr("title","small_logo.jpg");
								   $("#"+succ+" .fileDel").text('');
							   }
							   $.cssTable.focus().submit();
						   }
						   break;
						default:
							$.dialog.alert(data.msg);
							break;
					}
				},'json');
			}, function(){
	    return;
	 });
}
function viewImg(that){
	var $that = $(that);
    $.dialog({
        id: '_blank', 
        title:'图片预览',				
        lock:true,
        padding: 0,
        content:'<img src="' +$that.attr("src")+ '" width="500" height="400" />'
    });
}

function imgUpload(tar,uuid,toUrl,succ){
	var $tar = $('#'+tar+' .fileUpload');
	if($tar.length<=0){return;}
	new AjaxUpload($tar, {
			action: toUrl,
			name: 'uploadFile',
			data: {uuid:uuid},
			autoSubmit: true,
			responseType: 'json',
			onSubmit: function(filename, ext) {
				if (!(ext && /^(jpg|jpeg|gif|png|bmp)$/.test(ext))) {
					alert('未允许上传的文件格式!');
					return false;
				}
				$.dialog.tips('上传中，请稍候...',600,'loading.gif');
			},
			onComplete: function(filename, data) {
				var code = data.getElementsByTagName('code')[0].firstChild.data;
				var desc = data.getElementsByTagName('desc')[0].firstChild.data;
				//var code = data.result;
				//var desc = data.msg;
				switch(parseInt(code)){
						case 0:
							data=jQuery.parseJSON(desc);  
							if(typeof(succ)=="function")
							{
								succ(data);
							}else{
								//data = data.info;
								$.dialog.tips('图片上传成功！',1,'success.gif');
								$("#"+tar+" .fileDownload").attr("src",toUrl.replaceAll("upload","download")+"&uuid="+data.sid+'&_'+new Date().getTime());
								$("#"+tar+" .fileDownload").attr("title",data.fileName);
								$("#"+tar+" .fileDel").attr("href","javascript:delImg('"+data.sid+"','"+toUrl.replaceAll("upload","del")+"','"+tar+"')");
								$("#"+tar+" .fileDel").text('删除');
								$("#"+uuid).val(data.sid);
								$.cssTable.focus().submit();
							}
							break;
						default:
							$.dialog.tips(desc,0);
							$.dialog.alert(desc);
							break;
					}
			}
		});
}
function delFile(uuid,url,succ){
	$.dialog.confirm('你确定要删除该文件吗？', function(){
		var pars = {uuid:uuid};
		$.post(url,pars,function(data){
					switch(data.result){
						case 0:
						   if(typeof(succ)=="function")
						   {
							   succ(data);
						   }else{
						       $.dialog.tips("删除成功！",1,'success.gif');
							   if(typeof(succ)=="string"){
								   $("#"+succ+" .fileDownload").attr("href","javascript:;");
								   $("#"+succ+" .fileDownload").text("");
								   $("#"+succ+" .fileDel").text('');
							   }
							   $.cssTable.focus().submit();
						   }
						   break;
						default:
							$.dialog.alert(data.msg);
							break;
					}
				},'json');
			}, function(){
	    return;
	 });
}
function fileUpload(tar,uuid,toUrl,succ){
	var $tar = $('#'+tar+' .fileUpload');
	if($tar.length<=0){return;}
	new AjaxUpload($tar, {
			action: toUrl,
			name: 'uploadFile',
			data: {uuid:uuid},
			autoSubmit: true,
			responseType: 'json',
			onSubmit: function(filename, ext) {
				/**
				if (!(ext && /^(jpg|jpeg|gif|png)$/.test(ext))) {
					alert('未允许上传的文件格式!');
					return false;
				}
				 */
				$.dialog.tips('上传中，请稍候...',600,'loading.gif');
			},
			onComplete: function(filename, data) {	
				var code = data.getElementsByTagName('code')[0].firstChild.data;
				var desc = data.getElementsByTagName('desc')[0].firstChild.data;
				//var code = data.result;
				//var desc = data.msg;
				switch(parseInt(code)){
						case 0:
							data=jQuery.parseJSON(desc);
							if(typeof(succ)=="function"){
								succ(data);
							}else{
								//data = data.info;
								$.dialog.tips('文件上传成功！',1,'success.gif');
								$("#"+tar+" .fileDownload").attr("href",toUrl.replaceAll("upload","download")+"&uuid="+data.sid);
								$("#"+tar+" .fileDownload").text("下载");
								$("#"+tar+" .fileDel").attr("href","javascript:delFile('"+data.sid+"','"+toUrl.replaceAll("upload","del")+"','"+tar+"')");
								$("#"+tar+" .fileDel").text('删除');
								$("#"+uuid).val(data.sid);
								$.cssTable.focus().submit();
							}
							break;
						default:
							$.dialog.tips(desc,0);
							$.dialog.alert(desc);
							break;
					}
			}
		});
}
function logo_other(obj){
	try{
		//var obj = event.srcElement;
		obj.onerror=null;
		obj.src='./acl/common/upload/skins/small_logo.jpg';
		obj.title='未上传';
	}catch(ex){}
}