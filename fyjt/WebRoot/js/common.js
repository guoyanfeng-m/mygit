/**
 *设为首页和加入收藏
 * 
 */
//加入收藏
function AddFavorite(sURL, sTitle) {
	sURL = encodeURI(sURL);
	try {
		window.external.addFavorite(sURL, sTitle);
	} catch (e) {
		try {
		    window.sidebar.addPanel(sTitle, sURL, "");
		} catch (e) {
		    alert("加入收藏失败,请使用Ctrl+D进行添加,或手动在浏览器里进行设置.");
		}
	}
}		
//设为首页
function SetHome(url) {
	if (document.all) {
		document.body.style.behavior = 'url(#default#homepage)';
		document.body.setHomePage(url);
	} else {
		alert("您好,您的浏览器不支持自动设置页面为首页功能,请您手动在浏览器里设置该页面为首页!");
	}
}

/**
 * 页面效果
 * 
 */

// 内部菜单切换
jQuery(".sec-nav li").click(function(){
	var idx = jQuery(this).index();
	jQuery(this).addClass("cur").siblings("li").removeClass("cur");
	jQuery(this).parent().siblings(".aboutinfo").eq(idx).addClass("cur").siblings(".aboutinfo").removeClass("cur");
	return false;
})
function uploadFile(fileId, hiddenId, imgId, uploadUrl, fileName) {
	// 图片上传
	jQuery(document).on(
			"change",
			"#" + fileId,
			function() {
				var $this = jQuery(this);
				jQuery.ajaxFileUpload({
					url : uploadUrl,
					secureuri : false,
					fileElementId : $this.attr("id"),
					dataType : 'json',
					success : function(data, status) {
						if (data.code == 0) {

							var url = data.data.uri;
							jQuery("#" + hiddenId).val(url);
							if (imgId != '') {
								jQuery("#" + imgId).attr("src", url);
								jQuery("#" + imgId).show();
							}
							if (fileName) {
								jQuery('#' + fileName).html(data.data.title);
							}
						} else {
							alert(data.msg);
						}
					},
					error : function(data, status, e) {
						alert(e);
					}
				});
			});
}
/**
 * 
 * @param delurl
 * @param id
 * ajax删除每一项表单的通用方法
 */
function delinfo(delurl,id){
	jQuery.messager.confirm('提示','确定要删除该记录吗?',function(r){  
	      if (r){   
	       progressShow();
	       jQuery.post(delurl,{"ids":id} ,function(data) {
	  			 progressClose(); 
	  			jQuery('#role_list').datagrid('reload');
	  			 if(data.code==0){
	  				 jQuery.messager.alert('提示',"成功！");
	  			 }else{
	  				 jQuery.messager.alert('提示',data.msg);
	  			 }
	  		},"json");
	      }   
	});  
}