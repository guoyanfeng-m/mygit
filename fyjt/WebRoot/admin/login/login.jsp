<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录</title>
	<link rel="stylesheet" type="text/css" href="js/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/icon1.css">
	<link rel="stylesheet" type="text/css" href="js/demo.css">
	<link rel="stylesheet" type="text/css" href="js/style.css">
	<script type="text/javascript" src="js/msgbox.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript">
	function init(){
	 	var msg = "${loginMsg}";
	 	if(msg!=''&&msg!=null){
		 	document.getElementById("msg").innerHTML ="<span  style='font-size:16px;'><font color=red>"+'<b>'+msg+'<b>'+"</font></span>";
	 	}else{
	 		document.getElementById("msg").innerHTML = '';
	 	}

	}

	var checkInput = function(data) {
		var value = data.value;
		var pattern =/^[\w\u4e00-\u9fa5]+$/gi;
		if (!pattern.test(value)&&value.length>0) {
			if(data.id=="usernames"){
				document.getElementById("msg").innerHTML ="<span style='font-size:16px;'><font color=red><b>用户名不能有特殊字符!</b></font></span>";
				}else if(data.id=="passwords")
				document.getElementById("msg").innerHTML ="<span style='font-size:16px;'><font color=red><b>密码不能有特殊字符!</b></font></span>";
		}else{
		document.getElementById("msg").innerHTML = '';
			}
	}

	function checkUser(){
		   var result = document.getElementById("usernames").value;
		   var password = document.getElementById("passwords").value;

		 if(result == ""){
			   document.getElementsByTagName("input")[0].focus();
			   document.getElementById("msg").innerHTML = "<span style='font-size:16px;'><font  color=red><b>用户名不能为空!</b></font></span>";	
			     return false;
		   }
		   if(password == ""  ){
			   document.getElementsByTagName("input")[1].focus();
			   document.getElementById("msg").innerHTML ="<span  style='font-size:16px;'><font color=red><b>密码不能为空!</b></font></span>";
		     	return false;
		   }
		  
		   document.getElementById("login").submit();
		}
	function enter(){
		if (event.keyCode==13) {
			checkUser();
		}
	}
	function imgSwap(imgObj){imgObj.src=('img/denglu5.png')}
	function imgSwap2(imgObj){imgObj.src=('img/denglu2.png')}
    </script>
    <style type="text/css">
    body{
    width: 100%;
	height: 100%;
	padding: 0;
	margin: 0; 
	overflow:auto;
	min-width: 1300px;
	min-height:650px;
    }
    </style>
</head>
<body  onload="init()"  onkeyup="enter()" >
	<div style="width:100%; height:100%;position: relative;" >
	<img src="img/denglu1.jpg" width="100%" height="100%">
		<div  style="width: 30%;height: 35%;top:22%;left:35%;position: absolute;">
			<form id="login" role="form" method="post" action="login/userlogin.do" style="width:100%;height: 100%;">
				<div id="d_user" style="width:0px\9;height:15%;margin-top:10%;margin-left: 20%;position: relative">
				 <div style="position: absolute; padding-top: 12px; padding-left:10px">
					<img src="img/denglu3.gif">
				 </div>
				<input   style="width: 80%;width: 300px\9;height: 100%;padding-left:40px; outline:none;border:0px;font-size:20px;line-height:37px"  autocomplate="off" onkeyup="checkInput(this)"  name="usernames" id="usernames">
				</div>
				<div id="d_pswd" style="width:0px\9;height:15%;margin-top:5%;margin-left:20%;position: relative" >
				<div style="position: absolute; padding-top: 12px; padding-left:10px">
					<img src="img/denglu4.png">
				 </div>
				<input  style="width: 80%;width: 300px\9;height: 100%; padding-left:40px; outline:none;border:0px;font-size:20px;outline:none;line-height:37px" onkeyup="checkInput(this)"   name="passwords" id="passwords"type="password">
				</div>
				<div style="width:70%;height:25%;margin-top:5%;margin-left: 15%;position: relative;">
				<div id="d_button"  style="top:15%;left:65%; position:absolute ; ">
				<img src="img/denglu2.png" onmouseover="imgSwap(this);"onmouseout="imgSwap2(this);" onclick="checkUser()">
				</div>
				<div id='msg' style="top:20%;left:10%;position:absolute ; "></div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
