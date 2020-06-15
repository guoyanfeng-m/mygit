var buttonIds=['program_select','resolution_select','template_select'];
function buttonSelect(id){
	var src = $("#"+id).attr("src");
	if(src.indexOf("1.")>-1){
		$("#"+id).attr("src",src.replace("1.","."));
	}else{
		$("#"+id).attr("src",src.replace(".","1."));
	}
}

function initButton(e){
	var targetId=e.target.id;
	for(var i=0;i<buttonIds.length;i++){
		var src = $("#"+buttonIds[i]).attr("src");
		if(src.indexOf("1.")>-1){
			if(targetId==buttonIds[i]||targetId=="resolution"){
				return;
			}
			$("#"+buttonIds[i]).attr("src",src.replace("1.","."));
			$("#"+buttonIds[i].replace("select", "td")).fadeOut(500);
		}
	}
}
//点击节目按钮
function programSelect(){
	buttonSelect("program_select");
	$("#program_td").css("width",$("#program_select").css("width"));
	$("#program_table").css("width",$("#program_select").css("width"));
	var src = $("#program_select").attr("src");
	if(src.indexOf("1.")>-1){
		$("#program_td").fadeIn(500);
	}else{
		$("#program_td").fadeOut(500);
	}
}
//点击分辨率设置按钮
function resolutionSelect(){
	buttonSelect("resolution_select");
	$("#resolution_td").css("width",$("#resolution_select").css("width"));
	$("#resolution_table").css("width",$("#resolution_select").css("width"));
	var src = $("#resolution_select").attr("src");
	if(src.indexOf("1.")>-1){
		$("#resolution_td").fadeIn(500);
	}else{
		$("#resolution_td").fadeOut(500);
	}
}
//点击模板操作按钮
function templateSelect(){
	buttonSelect("template_select");
	$("#template_td").css("width",$("#template_select").css("width"));
	$("#template_table").css("width",$("#template_select").css("width"));
	var src = $("#template_select").attr("src");
	if(src.indexOf("1.")>-1){
		$("#template_td").fadeIn(500);
	}else{
		$("#template_td").fadeOut(500);
	}
}
//点击多媒体元素按钮
function radioSelect(){
	buttonSelect("radio_select");
	var src = $("#radio_select").attr("src");
	if(src.indexOf("1.")>-1){
		$("#radioGroup").fadeIn(500);
	}else{
		$("#radioGroup").fadeOut(500);
	}
}
//点击触摸互动元素按钮
function touchSelect(){
	buttonSelect("touch_select");
	var src = $("#touch_select").attr("src");
	if(src.indexOf("1.")>-1){
		$("#touchGroup").fadeIn(500);
	}else{
		$("#touchGroup").fadeOut(500);
	}
}
//初始化按钮组
function initRegionGroup(){
	$("#touchGroup")
	.draggable(
			{
				scroll : false,
				delay : 0,
				isDrag : true
			});
	$("#radioGroup")
	.draggable(
			{
				scroll : false,
				delay : 0,
				isDrag : true
			});
}
//查询节目是否需要审核 如果需要则将发布按钮隐藏
function auditButtonShow(){
	$.ajax({
		url : 'program/getAuditStatus.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		success : function(data) {
			if(data.auditStatus==0){
				$("#sendButton_td").css("display","none");
				$("#sendButton_td_img").css("display","none");
				$("#program_td").css("height","69px");
			}
		}
	});
}