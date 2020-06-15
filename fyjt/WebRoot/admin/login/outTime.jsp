<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <TITLE>用户登录</TITLE>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<style type="text/css">
	.root {
				background: url('admin/login/img/outimegb.png');
				height: 100%;
				width: 100%;
				background-size: 100% 100%;
				margin: 0px auto;
				position: fixed;
				top: 0;
				left: 0;
				text-align: center;
			}
	</style>
	<script type="text/javascript">
	var c=10;
	window.onload=function(){ 
		timer();
	}
	function jumper(){
		var path="<%=basePath%>";
		window.top.location=path+"admin/login/login.jsp";
	}
	function timer(){
		document.getElementById('data').innerHTML = c
		c-=1;
		if(c==0){
				   jumper();
				}
		window.setTimeout('timer()',1000);
	}
	function over(){
		document.getElementById('rebtn').src="admin/login/img/outtimebutton_2.png";
	}
	function out(){
		document.getElementById('rebtn').src="admin/login/img/outtimebutton_1.png";
	}
	</script>
  </head>
  <body >
	  <div class="root">
	  	<div  style="margin-top: 40px">
	  	    <p style="font-size: 50px;color:#FFFFFF">用户异常退出，请重新登录</p>
	  	</div>
	  	<div  style=" margin-top: 60px">
	  	     <p style="font-size: 50px;color:#FFFFFF">页面将在<span id="data"></span>秒后跳转至登录页面</p>
	  	</div>
	  	<div style="margin-top: 100px ">
	  	     <img  id="rebtn" src="admin/login/img/outtimebutton_1.png" onMouseOver="over()" onMouseOut="out()" onclick="jumper()">
	  	</div>
	 	<div>
	 	     <p style="font-size: 20px;color:#FFFFFF">点此直接跳转</p>
	 	</div>
	 	<div style="display: none;">fyjtlogout</div>
	  </div>
  </body>
</html>
