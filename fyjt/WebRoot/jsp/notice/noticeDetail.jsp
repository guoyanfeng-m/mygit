<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="../../WEB-INF/fyjttag.tld" prefix="p"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<title>峰岩集团-公告详情</title>
<link rel="stylesheet" href="js/css/show_more.css">
<link rel="stylesheet" href="js/css/noticeDetail.css">
<script src="js/jquery-1.11.0.min.js"></script>
<style type="text/css">
	.gohome{
		background:url("js/img/home.png") no-repeat;
	}
	.gohome:hover{
		background:url("js/img/home_1.png") no-repeat;
	}
	.goback:hover{
		color:#0185d7;
	}
	.goback{
		cursor: pointer;
		font-size: 16px;
	    /* vertical-align: middle; */
	    color: #939393;
	}
</style>
</head>
	<%@include file="../header.jsp"%>
<body>
	<div class="mid">
		<div class="mid-con">
			<h2><a class="gohome" href="#">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;首页</a> 
				>
				<a href="notice/noticeList" class="goback">内部公告</a>
				>
				 信息展示
			 </h2>
			<div class="info">
				<h2>${ent.title}</h2>
				<p class="time"><fmt:formatDate value="${ent.update_time}" pattern="yyyy-MM-dd HH:mm" /></p>
				<p class="first">${ent.description}</p>
			</div>
		</div>
	</div>
	<%@include file="../footer.jsp"%>
</body>
</html>