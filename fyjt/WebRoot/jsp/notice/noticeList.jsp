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
<link rel="shortcut icon" type="image/x-icon" href="/static/common/img/favicon.ico" />
<meta charset="UTF-8">
<title>峰岩集团-内部公告</title>
<script src="js/jquery-1.11.0.min.js"></script>
<link rel="stylesheet" href="js/css/style.css">
<link rel="stylesheet" href="js/css/common.css" type="text/css">
<link rel="stylesheet" href="js/css/reset.css"/>
<link rel="stylesheet" href="js/css/boundHotel.css"/>
<link rel="stylesheet" href="js/css/active.css"/>
<link rel="stylesheet" href="js/css/regulitions.css"/>
<style type="text/css">
	.gohome{
		background:url("js/img/home.png") no-repeat;
	}
	.gohome:hover{
		background:url("js/img/home_1.png") no-repeat;
	}
</style>
</head>
	<%@include file="../header.jsp"%>
<body>
	<style type="text/css">
		.xcConfirm .popBox{position: fixed; left: 50%; top: 50%; background-color: #ffffff; z-index: 2147000001; width: 440px; height: 200px; margin-left: -220px; margin-top: -120px; border-radius: 5px; font-weight: bold; color: #535e66;}
	</style>
	<div class="con">
		<div class="right">
			<h2><a class="gohome" href="#">&nbsp;&nbsp;&nbsp;&nbsp;</a> > 内部公告</h2>
			<div class="tabbedPanels">
				<ul class="tabGroup"><li class="tab selectedTab">内部公告</li></ul>
				<div class="contentGroup">
					<div class="content selectedContent">
						<ul class="dyna-ul">
							<c:forEach items="${pageEnt.listPages}" var="ent">
								<li>
									<%-- <h2><img src="js/img/hire.png" alt="" />
										<a href="notice/noticeDetail/${ent.id}">${ent.title}</a>
									</h2> --%>
									<div class="dyna-ul-word">
										<p><a >${ent.title}</a></p>
										<span class="time"><fmt:formatDate value="${ent.update_time}" pattern="yyyy-MM-dd"/></span>
										<span class="detail">
											<a href="notice/noticeDetail/${ent.id}">
												<img src="js/img/detail1.png" alt="" />
											</a>
										</span>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
     			<form action="notice/noticeList" id="pagelistform" method="post">
	  			<input name="pageNo" type="hidden" value="${pageEnt.pageNo}">
	  		</form>
			<div class="mainfoot-1" style="padding-top:50px">
        		 <p:newPageTag name="pageEnt" formId="pagelistform" style="bigPage"/>
			</div>
		</div>
	</div>
	<%@include file="../footer.jsp"%>
</body>
</html>