<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ct" value="${pageContext.request.contextPath}" />
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/js" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>节目预览</title>
<script src="${ctxStatic}/jquery-3.1.1.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/layui/css/layui.css" rel="stylesheet">
<script src="${ctxStatic}/layui/lay/dest/layui.all.js" type="text/javascript"></script>
<script src="${ctxStatic}/layui/layui.js" type="text/javascript"></script>
<script src="${ct}/program/play/PlayProgram.js" type="text/javascript"></script>
<script type="text/JavaScript">
	var height = ${height};
	var width = ${width};
</script>
</head>
<body>
	<div id="sceneBack" style="padding: 30px 50px 0px 50px;  background-color: #393D49; ">
		<h1 style="color:#fff;">预览节目：${name}</h1>
		<br>
		<div fit="true" id="sceneBackChild" style="position: relative;padding:2px 3px 3px 2px;border:2px solid white;">
			<c:forEach items="${region}" var="regions">
				<div id="${regions.id}"
					style="z-index: ${regions.zIndex}; position: absolute; left: ${regions.left}px; top: ${regions.top}px; width: ${regions.width}px; height: ${regions.height}px; margin:0px;">
					<c:forEach items="${regions.element}" var="element">
						<div id="${regions.id}${element.id}" life="${element.life}"
							style="width: ${regions.width}px;  position: absolute; height: ${regions.height}px; background: rgb(204, 204, 204); border: 3px solid rgb(187, 187, 187); margin:0px;">
			                <%-- <c:if test="${fn:contains(element.src, 'mp4')}">
								<video  autoplay="autoplay" controls="controls" loop="loop" src="${element.src}" ></video>
							</c:if>  --%>
							<c:choose>
								<c:when test="${fn:contains(element.thumbnailUrl, 'gif')}">
									<img src="${element.thumbnailUrl}">
								</c:when>
								<c:otherwise>
									<img src="${element.src}">
								</c:otherwise>
							</c:choose>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</div>
		<br> <a href="${ct}/program/programList.jsp" class="layui-btn layui-btn-normal">关闭</a>
	</div>
</body>
</html>
