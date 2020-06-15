<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>峰岩信息发布系统</title>

<link link rel="shortcut icon"  href="favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="../js/easyui.css">
<link rel="stylesheet" type="text/css" href="../js/demo.css">
<link rel="stylesheet" type="text/css" href="../js/style.css">
<script type="text/javascript" src="../js/msgbox.js"></script>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
<style type="text/css">
body {
	width: 100%;
	height: 100%;
	padding: 0;
	margin: 0;
	overflow: auto;
	min-width: 1500px;
	min-height: 760px;
	background-image: url(../img/beijing1_u0.jpg);
}

.bgimage {
	z-index: -1;
}

iframe {
	width: 100%;
	height: 99.8%;
	border: 0;
	padding: 0;
	margin: 0;
	overflow: hidden;
}

.iframehide {
	display: none;
}

.iframeshow {
	display: inline;
}
</style>
<script type="text/javascript">
function setProStatus(status){
	$.ajax({
		url : "../proStatus/setStatus.do",
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data:{editProgram:status}
	});
}
 $(document).ready(function(){  
 $('#updatepass').window({
    	onClose: function () { 
    	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
    	if(isChrome){
    		location.href = "index.html"
    	}else{
    		location.href = "index.html"
    	}
    	}
    	});
});
var unames;	
	function init(){
		setProStatus(false);
		$.ajax({ 
	    	url: "../modulepower/queryModulePowerID.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    	async: false,
	    	success: function(result){
	    	unames=result.username;
	    	$('#uname').html(unames);
				for(var i=0;i<result.moduleList.length;i++){
					if(result.moduleList[i]<1000){
						$("#"+result.moduleList[i]).show();
					}
				}
	      	}
	      });
	}
	function exit(){
		$.messager.defaults = { ok: "是", cancel: "否" };
		 $.messager.confirm("操作提示", "您确定要执行操作吗?", function (data) {
	           if (data) {
	           		//var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						    //	if(isChrome){
						    		location.href = "../admin/login/login.jsp"
						    //	}else{
						    	//	location.href = "../login.jsp"
						    	//}
					$.ajax({ 
				    	url: "../login/userloginout.do", 
				    	type: "POST",
				    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
				    	async: false,
				    	success: function(){
				    	
				      	}
				      });
	           }else{
	        	   return;
	           }
		 });
	}
	var editId = "shouye";
	var iframedata={};
	function redirectTo(path,id){
		var src = $('#img_'+id).attr("src");
		var src1 = $('#img_'+id).attr("src1");
		$('#img_'+id).attr("src",src1);
		$('#img_'+id).attr("src1",src);
		var editsrc = $('#img_'+editId).attr("src");
		var editsrc1 = $('#img_'+editId).attr("src1");
		$('#img_'+editId).attr("src1",editsrc);
		$('#img_'+editId).attr("src",editsrc1);
		editId = id;
		if(iframedata[id]==undefined){
			iframedata[id] = id;
			var iframetemp = document.createElement("iframe");
			iframetemp.id="iframe_"+id;
			iframetemp.scrolling="no";
			iframetemp.frameborder="0";
			iframetemp.src=path;
			$("#mains").append(iframetemp);
		}
		$(".iframeshow").removeClass("iframeshow").addClass("iframehide");
		$("#iframe_"+id).addClass("iframeshow")
	}
	function changepass(){
		$('#updatepass').window('open');
	}
	function comsss(id,value){
	$('#'+id).css({"color":"black"})
	 if(value=="请输入密码"){
		$("input[id='password']").attr('type','password');
		$('#'+id).val("");
	}else if(value=="请输入确认密码"){
		$("input[id='comfirmpass']").attr('type','password');
		$('#'+id).val("");
	}else if(value=="请输入原密码"){
		$("input[id='oldpassword']").attr('type','password');
		$('#'+id).val("");
	}
}

function comddd(id){

	if($('#'+id).val()==''){
		$('#'+id) .css({"color":"#aaaaaa"})
		 if(id=="password"){
			$("input[id='password']").attr('type','text');
			$('#'+id).val("请输入密码");
		}else if(id=="comfirmpass"){
			$("input[id='comfirmpass']").attr('type','text');
			$('#'+id).val("请输入确认密码");
		}else if(id=="oldpassword"){
			$("input[id='oldpassword']").attr('type','text');
			$('#'+id).val("请输入原密码");
		}
	}
	
}
function saveuser(){
		var oldpasswordstr=$('#oldpassword').val();
		var passwordstr=$('#password').val();
		var comfirmpassstr=$('#comfirmpass').val();
		if(oldpasswordstr=="请输入原密码"){
			oldpasswordstr="";
		}
		if(passwordstr=="请输入密码"){
			passwordstr="";
		}
		if(comfirmpassstr=="请输入确认密码"){
			comfirmpassstr="";
		}
		
		var datas = {"password":passwordstr};
		var datas1 = {"oldpassword":oldpasswordstr};
		var flag=0;
			$.ajax({ 
				    	url: "../user/UserPassVerify.do", 
				    	type: "POST",
				    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
				    	data: datas1,
				    	async: false,
				    	success: function(result){
				    		if(result.success==0){
				    			flag=0;
				    		}else{
				    			flag=1;
				    		}
				      	}
				      });
		
		if(flag==0){
			ZENG.msgbox.show("原始密码不正确", 3, 2000); 
		}else{
			if(passwordstr.length<5||passwordstr.length==0){
			ZENG.msgbox.show("密码长度不能小于5", 3, 2000); 
			}else{
				if(passwordstr==comfirmpassstr){
						$.ajax({ 
					    	url: "../user/updateUserPass.do", 
					    	type: "POST",
					    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
					    	data: datas,
					    	success: function(){
								ZENG.msgbox.show("修改成功", 4, 2000);
								setTimeout(function(){
									$('#updatepass').window('close');
								 },300);
					      	}
					      });
					} else{
						ZENG.msgbox.show("两次密码不一样", 3, 2000); 
					}
			}
		}
}

function cancel(){
$('#updatepass').window('close');
    	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
    	if(isChrome){
    		location.href = "index.html"
    	}else{
    		location.href = "index.html"
    	}
}
$.extend($.fn.validatebox.defaults.rules, {
    	passValid: {
        	validator: function(value){
        	if(value=="请输入密码"||value=="请输入确认密码"||value=="请输入原密码"){
        		return true;
        	}
	    		var passValid= /^[A-Za-z0-9]+$/;
	    		var flag1;var flag2;
	    		if(value.length<5){
	 	    		flag1=0;
	    		 }
	    		else if(passValid.test(value)==false){
		    		 flag2=0;
 	    		}
	    		if(flag1==0){
	    			$.fn.validatebox.defaults.rules.passValid.message = "长度不能小于5"; 
	    			return false;
	    		}else if(flag2==0){
	    			$.fn.validatebox.defaults.rules.passValid.message = "密码由数字和英文组成";
	    			return false;
	    		}else{
	    			return true;
	    		}
        	}
        }
       });
</script>
</head>
<body onload="init()" onresize="init()">
	<div class="easyui-layout" style="width: 100%;height: 105.5%;">
		<div data-options="region:'north',border:false" style="height:8%; padding-left:10px; padding-right:10px; background-color:transparent;">
			<div class="easyui-layout" fit="true" style="width:100%;height:100%;background-color:transparent; border:none">
				<div region="north" border="true" split="false" style=" height:100%; background-color:transparent; border:none">
					<div style="padding-top:15px;padding-left:82%;position:absolute;">
						<font style="color:white; font-size:14px;">欢迎</font>&nbsp<label id="uname" style="color:white; font-size:14px;"></label>&nbsp&nbsp&nbsp&nbsp&nbsp
					</div>
					<div style="padding-top:15px;padding-left:90%;position:absolute;">
						<a onclick="changepass()" style="cursor:pointer;color:#ffffff; font-size:14px;">&nbsp&nbsp修改密码&nbsp</a> <a onclick="exit()"
							style="cursor:pointer;color:#ffffff; font-size:14px;">退出</a>
					</div>
					<img src="../img/dingbubeijing3_u2.png" name="u2_img" width="100%" class="img" id="u2_img" /> 
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false" style="padding-left:10px; padding-bottom:10px; padding-right:10px;background-color:transparent;">
			<div class="easyui-layout" fit="true" style="width:100%;height:100%;  background-image:url(../img/dibeijing4_u4.png);border:none;">
				<div data-options="region:'west',border:false,split:false" style=" padding-left:5px; padding-top:5px;  width:17%; background-color:transparent;">
					<div class="easyui-layout" fit="true" style="width:100%;height:100%; padding-bottom:5px;background-color:transparent; border:none">

						<div style=" height:100%; border:none;">
							<div style="position:absolute;height:100%; width:100%;" align="center">

								<div id="liebiao" style="width:90%; padding-top:10px;height:75%;" align="center">
									<!-- 角色管理-->
									<div id="53" style="height:11%;display:none;">
										<img id="img_53" onClick="redirectTo('../admin/role/role.jsp','53')" src1="../img/jsgl1.png" src="../img/jsgl.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									<!-- 用户管理-->
									<div id="52" style="height:11%;display:none;">
										<img id="img_52" onClick="redirectTo('../admin/user/user.jsp','52')" src1="../img/yhgl1.png" src="../img/yhgl.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									<!--导航设置 -->
									<div id="54" style="height:11%;display:none;">
										<img id="img_54" onClick="redirectTo('../admin/nav/navList.jsp','54')" src1="../img/dhsz1.png" src="../img/dhsz.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									<!--轮播图片 -->
									<div id="55" style="height:11%;display:none;">
										<img id="img_55" onClick="redirectTo('../admin/carousel/carousel.jsp','55')" src1="../img/lbgls1.png" src="../img/lbgls.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									<!--公告管理 -->
									<div id="56" style="height:11%;display:none;">
										<img id="img_56" onClick="redirectTo('../admin/notice/notice.jsp','56')" src1="../img/gggl1.png" src="../img/gggl.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									
									<!--日志管理 -->
									<div id="57" style="height:11%;display:none;">
										<img id="img_57" onClick="redirectTo('../operationlog/operationlog.jsp','57')" src1="../img/rzgl1.png" src="../img/rzgl.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									<!-- 终端管理-->
									<div id="58" style="height:11%;display:none;">
										<img id="img_58" onClick="redirectTo('../terminal/terminal.jsp','58')" src1="../img/zdgl1.png" src="../img/zdgl.png"
											style="cursor:pointer;  width:100%;" />
									</div>
									<!-- 终端监控-->
									<div id="59" style="height:11%;display:none;">
										<img id="img_59" onClick="redirectTo('../terminal/terminalStatus.jsp','59')" src1="../img/zdjk1.png" src="../img/zdjk.png"
											style="cursor:pointer;  width:100%;" />
									</div>
									<!--系统设置 -->
									<div id="60" style="height:11%;display:none;">
										<img id="img_60" onClick="redirectTo('../admin/system/system.jsp','60')" src1="../img/xtsz1.png" src="../img/xtsz.png"
											style="cursor:pointer;  width:100%; " />
									</div>
									
									<!--节目管理 -->
									<div id="61" style="height:11%;display:none;">
										<img id="img_61" onClick="redirectTo('../program/programList.jsp','61')" src1="../img/jmgl1.png" flag="0" src="../img/jmgl.png"
											style="cursor:pointer;  width:100%;" />
									</div>
								</div>

							</div>
							<img class="bgimage" src="../img/zuobeijing2_u6.png" style=" height:100%; width:100%;" align="middle" />
						</div>
					</div>
				</div>
				<div id="mains" data-options="region:'center',border:false,"
					style="background-color:transparent;  padding-left:20px; padding-top:5px; padding-bottom:5px; padding-right:5px;">
					<!--<img class="bgimage" src="../img/u106.png" style=" height:100%; width:100%;" align="middle"/>-->
				</div>
			</div>
		</div>
		<div id="updatepass" class="easyui-window" title="密码修改" minimizable="false"
			data-options="resizable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
			style="width:400px;height:260px;padding:10px;">
			<table>
				<tr>
					<td style="color:#666666; font-size:15px; padding-left:25px;padding-top:10px;">原密码：</td>
					<td style="padding-top:10px; "><input id="oldpassword" onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
						style="width:220px;height:30px;line-height:30px;color:#aaaaaa;border:1px solid #7fb7ea; padding-left:10px" class="easyui-validatebox"
						data-options="validType:'passValid'" value="请输入原密码"></td>
				</tr>
				<tr>
					<td style="color:#666666; font-size:15px; padding-left:25px;padding-top:10px;">密&nbsp&nbsp&nbsp&nbsp&nbsp码：</td>
					<td style="padding-top:10px; "><input id="password" onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
						style="width:220px;height:30px;line-height:30px;color:#aaaaaa;border:1px solid #7fb7ea; padding-left:10px" class="easyui-validatebox"
						data-options="validType:'passValid'" value="请输入密码"></td>
				</tr>
				<tr>
					<td style="color:#666666; font-size:15px; padding-left:25px;padding-top:10px;">确认密码：</td>
					<td style="padding-top:10px;"><input id="comfirmpass" value="请输入确认密码" onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
						style="width:220px;height:30px;line-height:30px;color:#aaaaaa;border:1px solid #7fb7ea; padding-left:10px" class="easyui-validatebox"
						data-options="validType:'passValid'"></td>
				</tr>
			</table>
			<div style="padding-top:25px;padding-left:250px;">
				<a onclick="saveuser()" style="cursor:pointer;color:#0E4876; font-size:17px;">确定</a> <a onclick="cancel();"
					style="cursor:pointer;color:#0E4876; font-size:17px;padding-left:10px;">取消</a>
			</div>
		</div>
	</div>
</body>
</html>