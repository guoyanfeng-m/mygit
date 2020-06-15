<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>" />
<title>节目编排</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" href="js/jquery-ui.min.css"/>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript">
	var regionCount = 1;
	var editScene = 'scene1';
	var sceneW = 1000;
	var sceneH = 600;
		$(document).ready(function() {
			$("#newRegion").click(function() {
				var regionId = editScene+'_region_'+regionCount;
				regionCount = regionCount+1;
				var str='<div id="'+regionId+'" style="width:150px;height:150px;background:#888;opacity : 0.5;filter:alpha(opacity=50); "><div id="'+regionId+'_title" style="margin-left: auto;margin-top: auto;margin-right: auto;margin-bottom: auto;width:90%;height:90%;">title</div></div>';
				$("#editDiv").append(str);
				var regionDiv =  $('#'+regionId);
				 regionDiv.draggable({
					handle : '#'+regionId+'_title',
					containment:'parent'
				}); 
				regionDiv.resizable({
				      containment: "parent"
			    });
			});
		});
</script>
<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0px;
	padding: 0px;
	background-color: #FFF5E0;
}
</style>
</head>
	<body scroll="no">
		<button id="newRegion">区域</button>&nbsp;&nbsp;&nbsp;&nbsp;
		X:<input disabled="disabled" type="text" name="left" id="left" style="width:40px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
		Y:<input disabled="disabled" type="text" name="top" id="top" style="width:40px;"/>
		<div id="editDiv" align="center" style="width: 80%;height:90%;background-color: #DBDBDB;margin-left: auto;margin-right: auto;position: relative;"></div>
	</body>
</html>