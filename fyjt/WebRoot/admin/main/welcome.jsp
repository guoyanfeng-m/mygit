<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'welcome.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <script type="text/javascript">
  function init(){
  	$('#bktp').css("width",document.body.clientWidth+"px");
   	$('#bktp').css("height",document.body.clientHeight+"px");
  }
  </script>
  
  <body onload="init()" scroll=no  style="margin: 0px">
  <div style="height:100%;width:100%; ">
	  	<div align="right" style="position: absolute; width:100%">
	  		<img height="100px" width="180px" src="img/hdllogo.jpg"/>
	  		
	  	</div>
	  	<div  id="box" align="center" style="position: absolute;width:100%;top:30%; padding-left: 50px;padding-right: 50px">
	  		<a  style="top:500px; left: 50%; color: white; font-size:100px">
	  
	  		</a>
	  		
	  	</div>
	  	
	  	
	    <img id="bktp" src="img/hdlbj.png"/>
   </div>
  </body>
</html>
