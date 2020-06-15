<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<title>节目管理</title>
<link rel="stylesheet" type="text/css" href="js/easyui.css">
<link rel="stylesheet" type="text/css" href="js/icon1.css">
<link rel="stylesheet" type="text/css" href="js/demo.css">
<link rel="stylesheet" type="text/css" href="js/style.css">
<script type="text/javascript" src="js/jquery-1.7.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery.sortable.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/msgbox.js"></script>
<script type="text/javascript" src="js/check.js"></script>
<script type="text/javascript" src="program/js/programVar.js"></script>
<script type="text/javascript" src="program/js/programControl.js"></script>
<script type="text/javascript" src="program/js/programPlug.js"></script>
<script type="text/javascript" src="program/js/buttonSelect.js"></script>
<script type="text/javascript" src="element/js/jquery.colorpicker.js"></script>
<style type="text/css">
	#feedback {
		font-size: 1.4em;
	}
	
	#selectable .ui-selecting {
		background: #7EB9EC;
	}
	
	#selectable .ui-selected {
		background: #7EB9EC;
		color: white;
	}
	
	#selectable {
		list-style-type: none;
		margin: 0;
		padding: 0;
		width: 100%;
	}
	
	#selectable li {
		margin: 3px;
		border: 0;
		text-align: center;
		padding: 0.4em;
		font-size: 1em;
		height: 15px;
	}
	
	#selectable_screen .ui-selecting {
		background: #7EB9EC;
	}
	
	#selectable_screen .ui-selected {
		background: #7EB9EC;
		color: white;
	}
	
	#selectable_screen {
		list-style-type: none;
		margin: 0;
		padding: 0;
		width: 100%;
	}
	
	#selectable_screen li {
		margin: 3px;
		border: 0;
		text-align: center;
		padding: 0.4em;
		font-size: 1em;
		height: 23px;
	}
	.select_td {
		cursor: pointer;
		width: 100%;
		text-align: center;
		font-family: 微软雅黑;
		font-size: 15px;
		color: white;
	}
	
	.buttonGroup_td {
		cursor: pointer;
		width: 50%;
		text-align: center;
		font-family: 微软雅黑;
		font-size: 15px;
		color: white;
	}
	
	.combobox-item-selected {
		background-color: #7EB9EC;
		color: #000000;
	}
	
	.elemdiv {
		float: left;
		width: 160px;
		height: 60px;
		margin-left: 20px;
		cursor: pointer;
		margin-top: 5px;
	}
	
	.elemdivUnSelect {
		background-color: #E7F2FC;
	}
	
	.elemdivSelect {
		background-color: #7EB9EC;
	}
	
	.elemImg {
		margin-top: 5px;
		margin-left: 5px;
	}
	
	#regionElemDiv li {
		float: left;
		width: 150px;
		height: 60px;
		padding-left: 15px;
		background: #ffffff;
		cursor: pointer;
		padding-top: 5px;
	}
	
	.r_elemdiv {
		float: left;
		width: 160px;
		height: 60px;
		padding-left: 15px;
		cursor: pointer;
		padding-top: 5px;
	}
	
	.media_button_div {
		position: absolute;
		width: 100%;
		height: 100%;
		z-index: 8671;
		left: 0px;
		top: 0px;
		margin-top: 2px;
	}
	
	.media_button_img {
		height: 25px;
		width: 50px;
		z-index: 8669;
	}
	
	.touch_button_div {
		position: absolute;
		width: 100%;
		height: 100%;
		z-index: 8671;
		left: 0px;
		top: 0px;
		margin-top: 2px;
	}
	
	.touch_button_img {
		height: 25px;
		width: 75px;
		z-index: 8669;
	}
	
	.r_elemdivUnSelect {
		background-color: #ffffff;
	}
	
	.r_elemdivSelect {
		background-color: #7EB9EC;
	}
	.sendProgramWinDiv{
		width:30%;
		height:100%;
		border:1px solid green;
		float: left;
		padding:15px;
	}
	.buttonStyle{
		margin-top:10px;
		width:120px;
		height:25px;
		border:2px solid gray;
		background-color: #4c9ee6;
		-moz-border-radius: 5px; 
		-webkit-border-radius:5px;
		border-radius:5px; 
		cursor: pointer;
		font: 14px;
		font-family: 微软雅黑;
		padding: 5px;
		color: white;
	}
</style>
<script type="text/javascript">
	var index = 0; var isedit = "";var elemarr = [];
	function setProStatus(status) {
		$.ajax({
			url : "proStatus/setStatus.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			data : {
				editProgram : status
			}
		});
	}
	setProStatus(true);
	var programId =<%=request.getParameter("id")%>;
	if (!checkNull(programId)) {
		program.id = programId;
	}
	function mouseDowns(id) {
		$("#" + id).attr("src", "img/" + id + "1.png");
	}
	function mouseUps(id) {
		$("#" + id).attr("src", "img/" + id + ".png");
	}
	function returnList() {
		jQuery.messager.confirm('提示:', '节目编辑中,确定已经保存了吗?', function(event) {
			if (event) {
				setProStatus(false);
				var jumppath = "program/programList.jsp";
				var name = navigator.appName;
				if ("ActiveXObject" in window){
					jumppath = "programList.jsp";
				}
				window.location.href = jumppath;
			} else {
				return false;
			}
		});
	}
	function closeWin(winid) {
		$("#" + winid).dialog("close");
	}
</script>
</head>
<body class="easyui-layout" scroll="no" onload="initAll()"style=" height:98%; width:100%;padding:0;margin:0;">
	<div style="position: absolute;width:88%;height:7%;border:0px solid #7EB9EC;overflow: visible;left: 13%;top:1%;z-index: 7889;">
		<div style="width: 10%;float: left;" id="programDiv">
			<img onclick="programSelect()" class="menuImg"
				src="img/program_select.png" id="program_select"
				style="width:100%;cursor:pointer;" />
			<div style="width: 169px;height:90px;display:none;" id="program_td">
				<div style="position: absolute;">
					<table style="width: 100%;heigth:100px;" id="program_table">
						<tr>
							<td class="select_td" onclick="saveProgramWin()">保&nbsp;&nbsp;&nbsp;&nbsp;存</td>
						</tr>
						<tr>
							<td><img src="img/line.png" width="100%" height="1px;"></td>
						</tr>
						<tr>
							<td id="sendButton_td" class="select_td"
								onclick="sendProgramWin()">发&nbsp;&nbsp;&nbsp;&nbsp;布</td>
						</tr>
						<tr>
							<td id="sendButton_td_img"><img src="img/line.png"
								width="100%" height="1px;"></td>
						</tr>
						<tr>
							<td class="select_td" onclick="returnList()">返&nbsp;&nbsp;&nbsp;&nbsp;回</td>
						</tr>
					</table>
				</div>
				<img src="img/select_bg.png" style=" height:100%; width:100%;"
					align="middle" />
			</div>
		</div>
		<div style="width: 10%;float: left;margin-left: 2%;"
			id="resolutionDiv">
			<img class="menuImg" src="img/resolution_select.png"
				onclick="resolutionSelect()" id="resolution_select"
				style="cursor:pointer;width:100%" />
			<div style="width: 169px;height:69px;display:none;"
				id="resolution_td">
				<div style="position: absolute;">
					<table style="width: 100%;heigth:100px;" id="resolution_table">
						<tr style="height: 30px;">
							<td class="select_td"><select id="resolution"
								style="width:85%;height:100%;" onchange="resolutionChange()">
									<option>1920*1080</option>
									<option>1080*1920</option>
									<option>1600*900</option>
									<option>1366*768</option>
									<option>1024*768</option>
							</select></td>
						</tr>
						<tr>
							<td><img src="img/line.png" width="100%" height="1px;"></td>
						</tr>
						<tr>
							<td class="select_td"
								onclick="$('#newResolutionWin').dialog('open');">自&nbsp;定&nbsp;义</td>
						</tr>
					</table>
				</div>
				<img src="img/select_bg.png" style=" height:100%; width:100%;"
					align="middle" />
			</div>
		</div>
		<div style="width: 10%;float: left;margin-left: 2%;" id="templateDiv">
			<img class="menuImg" src="img/template_select.png"
				onclick="templateSelect()" id="template_select"
				style="cursor:pointer;width:100%" />
			<div style="width: 169px;height:100px;display:none;" id="template_td">
				<div style="position: absolute;">
					<table style="width: 100%;heigth:90px;" id="template_table">
						<tr>
							<td class="select_td" onclick="selectTemplateWin()">打&nbsp;开&nbsp;模&nbsp;板</td>
						</tr>
						<tr>
							<td><img src="img/line.png" width="100%" height="1px;"></td>
						</tr>
						<tr>
							<td class="select_td" onclick="saveTemplateWin()">保&nbsp;存&nbsp;模&nbsp;板</td>
						</tr>
						<tr>
							<td><img src="img/line.png" width="100%" height="1px;"></td>
						</tr>
						<tr>
							<td class="select_td" onclick="sceneBackWin(3)">背&nbsp;景&nbsp;图&nbsp;片</td>
						</tr>
					</table>
				</div>
				<img src="img/select_bg.png" style=" height:100%; width:100%;"
					align="middle" />
			</div>
		</div>
		<div style="width: 10%;float: left;margin-left: 2%;" id="radioDiv">
			<img class="menuImg" src="img/radio_select.png" id="radio_select"
				onclick="radioSelect()" style="cursor:pointer;width:100%" />
			<div id="radioGroup" fit="true" onmousedown="clickgroup('radio')"
				style="display:none;z-index:8665;opacity : 0.8;filter:alpha(opacity=80);background:#6691AB;border:0px solid #bbb;margin:0;width:220px;height:120px;">
				<div style="position: relative;width: 100%;height: 15px;">
					<div style="float: right;margin: 2 2 0 0;cursor: pointer;"
						onclick="radioSelect()">
						<img src="js/icons/close.png">
					</div>
				</div>
				<table style="width: 100%;height: 90%;margin-top: 5px;">
					<tr>
						<td class="buttonGroup_td" onclick="newRegion(4)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">视频</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newRegion(3)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">图片</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newRegion(2)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">音频</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newRegion(5)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">网页</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
					</tr>
					<tr>
						<td class="buttonGroup_td" onclick="newRegion(7)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">office</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div" onclick="newRegion(1)">文本</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newPlug(6)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">天气</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newPlug(5)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">时钟</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						
					</tr>
					<tr>
					<td id="flash_button" class="buttonGroup_td" onclick="newRegion(6)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">flash</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
					<td class="buttonGroup_td" onclick="newRegion(0)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">混播</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
					<td class="buttonGroup_td" onclick="newPlug(9)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">倒计时</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
					<td class="buttonGroup_td" onclick="newPlug(10)" style="display:none;"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div" >unity3d</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newPlug(11)" style="display:none;"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">插件</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newRegion(8)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="media_button_div">流媒体</div>
								<img src="img/regionbutton.png" class="media_button_img"
									align="middle" />
							</div></td>
					</tr>
					<tr>
					</tr>
				</table>
			</div>
		</div>
		<div style="width: 10%;float: left;margin-left: 2%;" id="touchDiv">
			<img class="menuImg" src="img/touch_select.png" id="touch_select"
				onclick="touchSelect()" style="cursor:pointer;width:100%" />
			<div id="touchGroup" fit="true" onmousedown="clickgroup('touch')"
				style="display:none;z-index:8669;opacity : 0.8;filter:alpha(opacity=80);background:#6691AB;border:0px solid #bbb;margin:0;width:240px;height:65px;">
				<div style="position: relative;width: 100%;height: 15px;">
					<div style="float: right;margin: 2 2 0 0;cursor: pointer;"
						onclick="touchSelect()">
						<img src="js/icons/close.png">
					</div>
				</div>
				<table style="width: 100%;height: 90%;margin-top: 5px;">
					<tr>
						<td class="buttonGroup_td" onclick="newButton(1)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="touch_button_div">互动按钮</div>
								<img src="img/regionbutton.png" class="touch_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newPlug(1)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="touch_button_div">互动相册</div>
								<img src="img/regionbutton.png" class="touch_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newPlug(7)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="touch_button_div">互动office</div>
								<img src="img/regionbutton.png" class="touch_button_img"
									align="middle" />
							</div></td>
					</tr>
					<tr>
						<td class="buttonGroup_td" onclick="newPlug(8)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="touch_button_div">互动视频</div>
								<img src="img/regionbutton.png" class="touch_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newButton(3)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="touch_button_div">首页</div>
								<img src="img/regionbutton.png" class="touch_button_img"
									align="middle" />
							</div></td>
						<td class="buttonGroup_td" onclick="newButton(2)"><div
								style="position: relative;width: 100%;height: 100%;margin-top:auto;">
								<div class="touch_button_div">返回</div>
								<img src="img/regionbutton.png" class="touch_button_img"
									align="middle" />
							</div></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div fit="true" style="position: absolute;top:8%;left:1%;border:0px solid #888888;width: 98%;height: 91%;">
		<div style="float: left;height: 70%;width: 10%;">
			<table height="100%" width="100%" border="0">
				<tr id="screenbutton" style=" height:8%; ">
					<td colspan="2"><img id="pingbaoanniu" src="img/pingbaoanniu_1.png"
						style="width:100%;cursor: pointer;" onclick="addScreen()"/></td>
				</tr>
				<tr id="screenbutton1" style=" height:8%; ">
					<td colspan="2"><img id="pingbaoanniu1" src="img/pingbaoanniu_2.png"
						style="width:100%;cursor: pointer;display:none;" onclick=""/></td>
				</tr>
				<tr id="screenlabel">
					<td colspan="2" id="screenTr">
					<div style="height:40px; overflow:auto;border:1px solid #7EB9EC ;position: relative;"
							id='screenList'>
							<ul id="selectable_screen">
								<li id="screenid"  class='ui-widget-content'>
									<div onclick="screenSelect()" style="font-size:18px;cursor:pointer;font-family: 微软雅黑;" 
										title ="屏保" oncontextmenu="showScreenSceneMenu();return false;">屏保
									</div>
								</li>
							</ul>
						</div>
					</td>
				</tr>
				<tr id="screendelete" style="height:10%; width:10%">
					<td><img onclick="deleteScreen()" title="删除屏保"
						style="cursor:pointer; display:inline;" id="aaa"
						src="img/shanchuchangjing_u44.png" width="90%" /></td>
				</tr>
				<tr style=" height:8%; ">
					<td colspan="2"><img src="img/changjinganniu_u40.png"
						style="width:100%;" /></td>
				</tr>
				<tr>
					<td colspan="2" id="sceneTr">
						<div style="height:200px; overflow:auto;border:1px solid #7EB9EC ;position: relative;"
							id='sceneList'>
							<div style="position:absolute;width:20px;height:20px;top:5px;left:5px;">
								<img title="主场景" width="100%" height="100%" src="img/home.png"/>
							</div>
							<ul id="selectable">
							</ul>
						</div>
					</td>
				</tr>
				<tr style=" height:10%; width:10%">
					<td><img onclick="deleteScene()" title="删除场景"
						style="cursor:pointer; display:inline;" id="aaa"
						src="img/shanchuchangjing_u44.png" width="90%" /></td>
					<td><img onclick="showAddScene()" title="新建场景"
						style="cursor:pointer; display:inline" id="aaa"
						src="img/baocunchangjing_u46.png" width="90%" /></td>
				</tr>
				<tr align="center" style=" height:10%; width:10%">
					<td colspan="2" align="right" style="padding-right:2%">
						<p>
							X:&nbsp;<input onblur="regionAttrCheck();setRegionAttr=''"
								onfocus="setRegionAttr='regionAttrX'" id="regionAttrX"
								style="width:50%;" type="text">
						</p>
						<p>
							Y:&nbsp;<input onblur="regionAttrCheck();setRegionAttr=''"
								onfocus="setRegionAttr='regionAttrY'" id="regionAttrY"
								style="width:50%;" type="text">
						</p>
						<p>
							W:&nbsp;<input onblur="regionAttrCheck();setRegionAttr=''"
								onfocus="setRegionAttr='regionAttrW'" id="regionAttrW"
								style="width:50%;" type="text">
						</p>
						<p>
							H:&nbsp;<input onblur="regionAttrCheck();setRegionAttr=''"
								onfocus="setRegionAttr='regionAttrH'" id="regionAttrH"
								style="width:50%;" type="text">
						</p>
					</td>
				</tr>
				<tr style=" width:10%">
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</div>
		<div
			style="float: left;height: 100%;width: 88%;background-color: #EBEBE4;margin-left: 5px;">
			<div id="sceneBack" fit="true"
				style="position:relative;height: 100%;width: 100%;margin-left: auto;margin-right: auto;padding: 0;">
			</div>
		</div>
	</div>
	<img src="img/x_u26.png" style=" height:100%; width:100%;"
		align="middle" />
	<div id="newSceneWin" class="easyui-window" title="&nbsp;新建场景"
		style="width:300px;height:150px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin-top: 30px;margin-left: 40px;">
			场景名称：&nbsp;<input type="text" id="newSceneName"></input> <a
				style="margin-top: 30px;margin-left: 180px;" onclick="subNewScene()"
				class="easyui-linkbutton">确&nbsp;定</a>
		</div>
	</div>
	<div id="sceneRenameWin" class="easyui-window" title="&nbsp;场景属性设置"
		style="width:300px;height:180px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin-top: 15px;margin-left: 40px;">
			场景名称：&nbsp;<input type="text" id="sceneName_input" class="easyui-textbox"></input><br/><br/>
			<div id="scenelifediv">播放时长：&nbsp;<input type="text" id="scene_life" class="easyui-numberbox" data-options="min:1,precision:0"/></div>
			
			</div>
			 <a style="margin-top: 7px;margin-left: 185px;"
				onclick="subSceneSet()" class="easyui-linkbutton">确&nbsp;定</a>
		</div>
	</div>
	<div id="screenSceneWin" class="easyui-window" title="&nbsp;屏保属性设置"
		style="width:300px;height:180px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin-top: 15px;margin-left: 40px;">
			<div id="screensaverlifediv">屏保跳转时长（秒）：<input type="text" id="screensaverlife" class="easyui-numberbox" data-options="min:1,precision:0,width:80"/></div>			
			</div>
			 <a style="margin-top: 7px;margin-left: 185px;"
				onclick="subScreenSceneSet()" class="easyui-linkbutton">确&nbsp;定</a>
		</div>
	</div>
	<div id="selectSceneBack" class="easyui-window" title="&nbsp;选择背景图片"
		style="width:410px;height:530px;"
		data-options="modal:true,closed:true,resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'" style="border:0;">
				<div style=" position: absolute;margin-left: 8px;margin-top: 4px;">
					<input id="searchName_back" class="easyui-textbox"
						style="width: 140px; height: 26px" data-options="prompt:'请输入素材名称'"
						value="">
				</div>
				<img width="180px;" height="33px" id="searchimg_back"
					onclick="searchNameBack()" src="img/search2.png"
					style="cursor: pointer;" align="middle" />
				<div id="elemListDiv_back"
					style="overflow:auto;height: 330px;border:1px solid #7EB9EC;margin: 5px;"></div>
				<div class="easyui-pagination" id="pagInationElem_back"
					style="width: 390px;margin: 5px;"
					data-options="showPageList:false,beforePageText: '第',
        afterPageText: '页 共 {pages} 页', 
        displayMsg: '共 {total} 条记录' "></div>
				<table width="230px" align="right" style="margin-top: 5px;">
					<tr>
						<td style="width: 180px;height: 30px;" id="backImgLeftBottom"><p
								style="font-size: 16;font-family: 微软雅黑;">
								<input type="checkbox" id="isAllSceneBack" value="true">&nbsp;应用到所有场景
							</p></td>
						<td style="width: 50px;height: 30px;" id="backImgRightBottom"><a
							onclick="subBackImg()" class="easyui-linkbutton">确&nbsp;定</a></td>
					</tr>
				</table>
			</div>
		</div>

	</div>
	<div id=regionMenu class="easyui-menu" style="width:120px;">
		<div onclick="deleteSRegion()" data-options="iconCls:'icon-delete'"
			id="regionMenu_delete">删除</div>
		<div class="menu-sep"></div>
		<!-- <div data-options="iconCls:'icon-copy'" id="regionMenu_cope">
			<span>复制</span>   
        <div style="width:120px;">   
            <div onclick="copySRegion()" data-options="iconCls:'icon-copy'" id="regionMenu_cope_new">至当前场景</div>   
        </div>
		</div> -->
		<div onclick="copySRegion()" data-options="iconCls:'icon-copy'"
			id="regionMenu_cope_new">复制</div>
		<div class="menu-sep"></div>
		<div onclick="upAllRegionZIndex()" data-options="iconCls:'icon-upAll'"
			id="regionMenu_upAll">至于顶层</div>
		<div class="menu-sep hideorshow"></div>
		<div onclick="downAllRegionZIndex()"
			data-options="iconCls:'icon-downAll'" id="regionMenu_downAll">至于底层</div>
		<div class="menu-sep hideorshow"></div>
		<div onclick="upRegionZIndex()" data-options="iconCls:'icon-up'"
			id="regionMenu_up">上移一层</div>
		<div class="menu-sep hideorshow"></div>
		<div onclick="downRegionZIndex()" data-options="iconCls:'icon-down'"
			id="regionMenu_down">下移一层</div>
		<div class="menu-sep hideorshow"></div>
		<div onclick="showRegionAttrWin()" data-options="iconCls:'icon-down'"
			id="regionAttr_menu">属性设置</div>
		<div onclick="setRegionToStatic()" data-options="iconCls:'icon-set'"
			id="regionAttr_static">设为全局</div>
		<div onclick="setRegionToUnstatic()" data-options="iconCls:'icon-set'"
			id="regionAttr_unstatic">取消全局</div>
	</div>
	<div id=sceneMenu class="easyui-menu" style="width:120px;">
		<div onclick="deleteScene()" data-options="iconCls:'icon-delete'"
			id="sceneMenu_delete">删除</div>
		<div class="menu-sep"></div>
		<div onclick="copyScene()" data-options="iconCls:'icon-copy'"
			id="sceneMenu_cope_new">复制</div>
		<div class="menu-sep"></div>
		<div onclick="sceneRenameWin()" data-options="iconCls:'icon-down'"
			id="sceneMenu_rename">属性</div>
	</div>
	<div id="screenSceneMenu" class="easyui-menu" style="width:120px;">
		<div onclick="setScreenWin()" data-options="iconCls:'icon-down'"
			id="screenSceneMenu-set">属性</div>
	</div>
	<div id="regionManagerWin" class="easyui-window" title="&nbsp;内容编辑"
		style="width:800px;height:500px"
		data-options="modal:true,closed:true,resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,onBeforeClose:function(){$('#elemTypeSelect').combobox('setValue','');$('#searchNameElem').textbox('setValue','')}">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div style="position: absolute;width:100%;height:100%;z-index: -1;">
				<img src="img/elem_winBack.png" width="100%" height="100%"
					align="middle">
			</div>
			<div
				style="position: absolute;width:180px;height:33px;left: 30px;top:10px;cursor:pointer;"
				onclick="searchNameElem()">
				<img src="img/search2.png" width="100%" height="100%" align="middle">
			</div>
			<div style=" position: absolute;left: 38px;top: 14px;">
				<input id="searchNameElem" class="easyui-textbox"
					style="width: 140px; height: 26px" data-options="prompt:'请输入素材名称'"
					value="">
			</div>
			<div style="position: absolute;width:150px;height:33px;left: 258px;top: 14px;" id="reso" display="none">
				<select id="resolutionSize" class="easyui-combobox" style="border-radius:5px;width:90px;left: 108px;top: 14px;"  editable="editable">
				</select>
			</div>
			<div style="float: left;width: 50%;margin-top: 40px;">
				<!-- <div style=" position: absolute;left:200px;top:10px;height: 26px">
					<input style="width:120px;" id="elemTypeSelect">
				</div> -->
				<div class="easyui-pagination" id="pagInationElem"
					style="width: 380px;position: absolute;margin-left: 3px;margin-top: 380px;"
					data-options="showPageList:false,beforePageText: '第',
        afterPageText: '页 共 {pages} 页', 
        displayMsg: '共 {total} 条记录'"></div>
				<div
					style="width: 95%;height: 380px; border:0px solid #7EB9EC;margin-top: 2px;margin-left: 4px;">
					<div style="margin-top: 10px;margin-bottom: 5px;">
						<span onclick="elemAllSelect()"
							style="cursor:pointer;font-size: 14px;color: white;padding: 5px;margin-left: 30px;"><b>全选</b></span><span
							onclick="elemAllUnSelect()"
							style="cursor:pointer;font-size: 14px;color: white;padding: 5px;margin-left: 43px;"><b>全不选</b></span>
						<span style="margin-left: 25px;"><input
							style="width:95px;margin-left: 35px;height:23px;"
							id="elemTypeSelect"></span> <span onclick="addElemToRegion()"
							style="cursor:pointer;font-size: 14px;color: white;padding: 5px;margin-left: 17px; "><b>添加</b></span>
					</div>
					<div id="elemListDiv"
						style="overflow:auto;height: 330px;margin-top: 12px;"></div>
				</div>
			</div>
			<div style="float: left;width: 50%">
				<div
					style="width: 95%;height: 400px; border:0px solid #7EB9EC;margin-top: 38px;margin-left: 4px;">
					<div style="margin-top: 9px;margin-bottom: 25px;">
						<span onclick="deleteElem()"
							style="cursor:pointer;font-size: 14px;color: white;padding: 5px;margin-left: 95px;"><b>删除</b></span>
						<span onclick="rElemAllSelect()"
							style="cursor:pointer;font-size: 14px;color: white;padding: 5px;margin-left: 33px;"><b>全选</b></span>
						<span onclick="rElemSet()"
							style="cursor:pointer;font-size: 14px;color: white;padding: 5px;margin-left: 33px;"><b>属性</b></span>
					</div>
					<ul id="regionElemDiv"
						style="overflow:auto;height: 360px;list-style: none;padding:0;margin:0;">

					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 场景跳转块 -->
	<div id="buttonManagerWin" class="easyui-window"
		style="width:260px;height:300px"
		data-options="noheader:true,draggable:false,closable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'"
				style="width:100%;height:260px;border:0;">
				<div id="buttonEvent_tabs" class="easyui-tabs"
					style="width:100%;height:260px;">
					<div id="sceneJump_tab" title="场景跳转"
						style="padding:1px;height:100%;">
						<div style="margin: 20 0 0 30;float: left;">
							跳转场景：<input style="width:120px;" id="jumpSceneSelect">
						</div>
					
						<div style="margin: 20 0 0 30;float: left;">
							<div style="float: left;width:45%; margin:0 10 0 0"><input name="select" id="select" type="button" value="点击图片" onclick="sceneBackWin(1)">
							<div id="selectimg" ><img src="" style="width:100px;"></div></div>
				
	
							<div style="float: left;width:45%;margin:0 10 0 0"><input name="back" id="back" type="button" value="背景图片" onclick="sceneBackWin(2)">
							<div id="backimg" ><img src="" style=" width:100px;"></div></div>
			
					</div>
					<!-- <div id="buttonStyle_tab" title="按钮属性"
						style="padding:1px;height:100%;">
						<div style="margin: 20 0 10 70">
							<input type="radio" value="0" name="buttonStyle"
								checked="checked" onclick="buttonStyleChange(0)">透明&nbsp;&nbsp;<input
								type="radio" value="2" name="buttonStyle"
								onclick="buttonStyleChange(2)">内置&nbsp;&nbsp;

							<input
								value="1" type="radio" name="buttonStyle"
								onclick="buttonStyleChange(1)"> 自定义&nbsp;&nbsp;
						</div>
						<div id="buttonStyle2" style="display: none;margin: 10 0 0 30">
							样式选择：<input style="width:120px;" id="buttonStyleSelect"><br>
							<div style="margin-left: 30px;margin-top: 10px;">
								按钮文字：<input type="text" style="width:120px;" id="buttonText"><br>
								字体选择：<select id="button_font_family"
								style="width:75%;height:100%;">
									<option>宋体</option>
									<option>黑体</option>
									<option>楷体</option>
									<option>微软雅黑</option>
							</select><br>
								文字大小：<input type="text" style="width:120px;" id="button_font_size"><br>
								<input  id="buttonfontcolor" style="display: none">
										      <img src="scrollingnews/colorpicker.png" id="buttonfontcolor_select" style="cursor:pointer"/>
							</div>
							<div id="buttonStyleImgDiv"
								style="margin: 20 0 0 70;float: left;border: null;">
								<img width="120px" height="50px" id="buttonStyleImg" src="">
							</div>
						</div>
					</div> -->
					<!-- <div id="otherJump_tab" title="第三方程序"
							style="padding:10px;height:100%;"></div> -->
				</div>
			</div>
			<div data-options="region:'south'" style="height:30px; border:0;">
				<table width="80px" align="right" style="">
					<tr>
						<td width="40px "><div>
								<p onclick="subButtonEvent()"
									style="cursor:pointer;font-size: 16px;color: blue;"
									align="center">确定</p>
							</div></td>
						<td width="40px "><div>
								<p onclick="closebuttonmanagerwin()"
									style="cursor:pointer;font-size: 16px;color: blue;"
									align="center">取消</p>
							</div></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div id="saveProgramWin" class="easyui-window" title="&nbsp;保存节目" style="width:1200px;height:700px;padding:20px;"
		data-options="modal:true,closed:true,resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:true">
		<div class="easyui-layout" style="width:99%;height:99%;background-color: #e7f2fc;border: 1px solid #9ec8ee;padding: 30px;font-family: 微软雅黑;">
			<div style="position:relative;width:100%;height:88%; ">
				<form id="programForm" method="post" style="width:100%;height:92%;">
				<div class="sendProgramWinDiv">
					节目名称：<input id="programName" type="text" name="p_name" style="margin-top: 20px;width: 170px;height:25px; -moz-border-radius: 5px; -webkit-border-radius:5px;border-radius:5px; ">
					<div style="margin-top: 15px;">
					开始时间：<input class="easyui-datetimebox" name="startdate" id="startTime"
							data-options="editable:false,required:true,showSeconds:true,
											formatter:function(date){
											    var y = date.getFullYear();
												var m = date.getMonth()+1;
												var d = date.getDate();
												var o = date.getHours();
												var min = date.getMinutes();
												var s = date.getSeconds();
												if(o<10){
													o = '0'+o;
												}
												if(m<10){
													m = '0'+m;
												}
												if(d<10){
													d = '0'+d;
												}
												if(min<10){
													min = '0'+min;
												}
												if(s<10){
													s = '0'+s;
												}
												return y+'-'+m+'-'+d+' '+o+':'+min+':'+s;
										   }"
							value="3/4/2010 0:0:0" style="width:170px;">
					</div>
					<div style="margin-top: 15px;">
					结束时间：<input class="easyui-datetimebox" name="stopdate" id="endTime"
							data-options="editable:false,required:true,showSeconds:true,
											formatter:function(date){
											    var y = date.getFullYear();
												var m = date.getMonth()+1;
												var d = date.getDate();
												var o = date.getHours();
												var min = date.getMinutes();
												var s = date.getSeconds();
												if(o<10){
													o = '0'+o;
												}
												if(m<10){
													m = '0'+m;
												}
												if(d<10){
													d = '0'+d;
												}
												if(min<10){
													min = '0'+min;
												}
												if(s<10){
													s = '0'+s;
												}
												return y+'-'+m+'-'+d+' '+o+':'+min+':'+s;
											  }"
							value="3/4/2010 23:59:59" style="width:170px;">
					</div>
					<div style="margin-top: 15px;">
					      播出单发布权限：<select id="pubpower" style="width:80px;height:23px;color:#aaaaaa; padding-left:10px;border:1px solid #7fb7ea;-moz-border-radius: 5px; -webkit-border-radius:5px;border-radius:5px;"></select>
					</div>
					<div style="margin-top: 15px;border-top: 2px solid  gray">
							<div style="margin-top: 17px;">
									<input id="istiming"
										name="istiming" value="istiming1"
										style="margin-left: 15px;" type="checkbox">
									<b>下载时间</b>
							</div>
							<div style="margin-top: 17px;">
									开始时间：<input id="download_startTime" name="download_startTime"
										class="easyui-timespinner" style="width:170px;" 
										required="required" data-options="disabled:true"
										value="" />
							</div>
							<div style="margin-top: 17px;">
									结束时间：<input id="download_endTime" name="download_endTime"
										class="easyui-timespinner" style="width:170px;"
										required="required" data-options="disabled:true"
										value="" />
							</div>
					</div>
				</div>
				<div id="sendTreeDiv" class="sendProgramWinDiv" style="margin-left: 3px;">
					<div style="margin-top: 17px;">
						<b>发布终端</b>
					</div>
					<div style="margin-top: 17px;overflow:auto;height: 300px;">
						<ul id="sendtree"></ul>
					</div>
				</div>
				<div class="sendProgramWinDiv" style="margin-left: 3px;">
				    <div style="position: relative;width:100%;height:80%;">
						<div id="lowgradeview" style="width: 100%;">
							<div style="margin-top: 17px;">
								<b>日循环</b>
							</div>
							<div style="margin-top: 17px;">
								开始时间：<input id="day_startTime" name="loopstarttime"
									class="easyui-timespinner" style="width:170px;"
									required="required"
									data-options="showSeconds:true,missingMessage:'开始时间不能为空'"
									value="00:00:00" />
							</div>
							<div style="margin-top: 17px;">
								结束时间：<input id="day_endTime" name="loopstoptime"
									class="easyui-timespinner" style="width:170px;"
									required="required"
									data-options="showSeconds:true,missingMessage:'结束时间不能为空'"
									value="23:59:59" />
							</div>
							<div style="margin-top: 17px;">
								<b>周循环</b>
							</div>
							<div style="margin-top: 17px;">
								<div style="float: left;">
									<span style="margin-left: 15px;">一</span><br> <input
										name="days" value="Monday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
								<div style="float: left;">
									<span style="margin-left:15px;">二</span><br> <input
										name="days" value="Tuesday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
								<div style="float: left;">
									<span style="margin-left: 15px;">三</span><br> <input
										name="days" value="Wednesday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
								<div style="float: left;">
									<span style="margin-left: 15px;">四</span><br> <input
										name="days" value="Thursday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
								<div style="float: left;">
									<span style="margin-left: 15px;">五</span><br> <input
										name="days" value="Friday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
								<div style="float: left;">
									<span style="margin-left: 15px;">六</span><br> <input
										name="days" value="Saturday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
								<div style="float: left;">
									<span style="margin-left: 15px;">日</span><br> <input
										name="days" value="Sunday" checked="checked"
										style="margin-left: 15px;" type="checkbox">
								</div>
							</div>
						</div>
						<div id="highgradegrid" style="width: 310px;height:300px;margin-top: 25px;position: relative;font-size: 10px;">
							<div><b>循环周期</b></div>
							<div style="margin-top: 10px;width:100%;height:100%;">
								<table id="highgradegridtable" class="easyui-datagrid" style="width:310px;height:300px;font-size: 10px;margin-top: 10px;border:1px solid gray" 
										data-options="rowStyler: function(index,row){return 'height:25px;'},
												url:'',
												fitColumns:true,
											     rownumbers : true,toolbar:[{
											    	 iconCls:'icon-add',
											    	 handler:function(value,row,index){
											    	 	addloop();
											    	 }
											     },{
											    	 iconCls:'icon-edit',
											    	 handler:function(value,row,index){
											    	 	editloop();
											    	 }
											     },{
											      iconCls:'icon-remove',
											      handler:function(value,row,index){
											    	 	delloop();}
											    }]">   
										    <thead>   
										        <tr>   
										            <th data-options="field:'starttime',align:'center',width:70">开始时间</th>   
										            <th data-options="field:'endtime',align:'center',width:70">结束时间</th>   
										            <th data-options="field:'circleweek',align:'center',width:150">周循环</th>   
										        </tr>   
										    </thead>   
								 </table> 
							 </div>
						  </div>
					    </div>
							<div style="position: relative;width:100%;height:20px;margin-top: 20px;">
									<span id="highlevelButton" class="buttonStyle" onclick="highset()">高级设置</span>
							        <span id="lowlevelButton"  class="buttonStyle" onclick="lowset()">简易设置</span>
							</div>
					  </div>
					<div>
				</form>
			</div>
			<div style="width:300px;height:50px;position: relative;margin-top: 50px;text-align: center;padding:2px;margin-left:400px; text-align: center;">
					<span id="saveProgramButton" style=""class="buttonStyle" onclick="saveProgram()">保存</span>
					<span style="" id="sendProgramButton"class="buttonStyle" onclick="sendProgram()">发布</span>
					<span style="margin-left: 50px;" class="buttonStyle" onclick="$('#saveProgramWin').dialog('close');">取消</span>
			</div>
		</div>
	</div>
	<div id="addloopWin" class="easyui-window" title="设置循环时间"
		minimizable="false" data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true,onClose:function(){$('#jmgl').datagrid('reload');}"
		style="width:400px;height:400px;padding:20px;">
		<div style="width:100%;height:65%;position: relative;">
				<div style="margin-top: 17px;">
					<b>日循环</b>
				</div>
				<div style="margin-top: 17px;">
					开始时间：<input id="loop_days" name="loop_days"
						class="easyui-timespinner" style="width:170px;"
						required="required" data-options="showSeconds:true"
						value="00:00:00" />
				</div>
				<div style="margin-top: 17px;">
					结束时间：<input id="loop_daye" name="loop_daye"
						class="easyui-timespinner" style="width:170px;"
						required="required" data-options="showSeconds:true"
						value="23:59:59" />
				</div>
				<div style="margin-top: 17px;">
					<b>周循环</b>
				</div>
				<div style="margin-top: 17px;">
					<div style="float: left;">
						<span style="margin-left: 15px;">一</span><br> <input
							name="loopdays" value="Monday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 15px;">二</span><br> <input
							name="loopdays" value="Tuesday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 15px;">三</span><br> <input
							name="loopdays" value="Wednesday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 15px;">四</span><br> <input
							name="loopdays" value="Thursday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 15px;">五</span><br> 
						<input name="loopdays" value="Friday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 15px;">六</span><br> <input
							name="loopdays" value="Saturday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 15px;">日</span><br> <input
							name="loopdays" value="Sunday" checked="checked"
							style="margin-left: 15px;" type="checkbox">
					</div>
				</div>
		</div>
		<div  id="loopsavebutton"  style="width:95%;height:50px;position: relative;margin-top: 20px;text-align: center;padding:2px; text-align: center;">
                <span class="buttonStyle" onclick="loopsave()">确定</span>
		</div>
	</div>
	<div id="editloopWin" class="easyui-window" title="编辑循环时间"
		minimizable="false" data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true,onClose:function(){$('#jmgl').datagrid('reload');}"
		style="width:280px;height:260px;">
		<div style="margin-left: 52px;">
				<div style="margin-top: 17px;">
					<b>日循环</b>
				</div>
				<div style="margin-top: 17px;">
					开始时间：<input id="edloop_days" name="edloop_days"
						class="easyui-timespinner" style="width:85px;"
						required="required" data-options="showSeconds:true"
						value="00:00:00" />
				</div>
				<div style="margin-top: 17px;">
					结束时间：<input id="edloop_daye" name="edloop_daye"
						class="easyui-timespinner" style="width:85px;"
						required="required" data-options="showSeconds:true"
						value="23:59:59" />
				</div>
				<div style="margin-top: 17px;">
					<b>周循环</b>
				</div>
				<div style="margin-top: 17px;">
					<div style="float: left;">
						<span style="margin-left: 8px;">一</span><br> <input
							name="edloopdays" value="Monday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">二</span><br> <input
							name="edloopdays" value="Tuesday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">三</span><br> <input
							name="edloopdays" value="Wednesday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">四</span><br> <input
							name="edloopdays" value="Thursday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">五</span><br> <input
							name="edloopdays" value="Friday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">六</span><br> <input
							name="edloopdays" value="Saturday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">日</span><br> <input
							name="edloopdays" value="Sunday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
				</div>
		</div>
		<div id="loopsavebutton"><span style="position: absolute;top: 235px;left:220px;margin-bottom: -20px;margin-right: 15px;
			color: blue;cursor:pointer; font-size: 14px;" onclick="edloopsave()">确定</span>
		</div>
	</div>
	<div id="addTemplateWin" class="easyui-window" title="保存模板"
		style="width:280px;height:260px"
		data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div
			style="width: 240px;height: 200px;margin-left: 20px;margin-top: 15px;">
			模板名称：<input id="saveTemplate_name" type="text"
				style="width:160px;margin-top: 5px;"><br>
			<div style="margin-bottom: 10px;height: 130px;">
				<div style="float: left; margin-top: 25px;">模板描述：</div>
				<div style="float: left;">
					<textarea id="saveTemplate_des"
						style="width:160px;height:100px;margin-top: 20px;"></textarea>
				</div>
			</div>
			<div class="messager-button">
				<a href="javascript:saveTemplate()" class="l-btn l-btn-small"
					group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
					href="javascript:closeWin('addTemplateWin');"
					class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
			</div>
		</div>
	</div>
	<div id="selectTemplateWin" class="easyui-window" title="&nbsp;选择模板"
		style="width:562px;height:480px"
		data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'" style="border:0;">
				<div style=" position: absolute;margin-left: 8px;margin-top: 4px;">
					<input id="searchName_template" class="easyui-textbox"
						style="width: 140px; height: 26px" data-options="prompt:'请输入模板名称'"
						value="">
				</div>
				<img width="180px;" height="33px" id="searchimg_back"
					onclick="searchTemplate()" src="img/search2.png"
					style="cursor: pointer;" align="middle" />
				<table id="templateList" style="width:99%;height:90%;">
				</table>
			</div>
		</div>
	</div>
	<div id="regionAttrSetWin" class="easyui-window" title="属性设置"
		style="width:350px;height:370px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'"
				style="width:100%;height:260px;border:0;">
				<div style="margin: 10 0 0 20;">
					样式选择：<select id="albumStyleType" style="width:220px;height:30px;"
						onchange="showAlbumImg()">
						<option value="1">普通</option>
						<option value="2">水平滑动</option>
						<option value="101">列表无限滚动(windows)</option>
						<option value="102">水平滑动+轮播(windows)</option>
						<option value="103">旋转墙(windows)</option>
						<option value="105">翻书效果(windows)</option>
						<option value="106">放大缩小相册(windows)</option>
						<option value="108">瀑布流(windows)</option>
						<option value="201">立体选择(Android)</option>
					</select>
				</div>
				<div style="margin-left: 24px;margin-top: 15px;">
					<img id="albumBackimg" src="" width="300" height="200">
				</div>
			</div>
			<div data-options="region:'south'" style="height:50px; border:0;">
				<div class="messager-button">
					<a href="javascript:subRegionAttrSet()" class="l-btn l-btn-small"
						group="" id="" style="margin-left: 10px;"><span
						class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
						href="javascript:closeWin('regionAttrSetWin');"
						class="l-btn l-btn-small" group="" id=""
						style="margin-left: 10px;"><span class="l-btn-left"><span
							class="l-btn-text">取消</span></span></a>
				</div>
			</div>
		</div>
	</div>
	<div id="weatherAttrSetWin" class="easyui-window" title="属性设置"
		style="width:270px;height:320px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'"
				style="width:100%;height:260px;border:0;">
				<div style="margin: 5 0 0 40;">
					样式选择：<select id="weatherStyleType" class="easyui-combobox"
						style="width:120px;height:30px;"
						data-options="    
		missingMessage:'请选择',
        valueField: 'style',    
        textField: 'name',      
        onSelect: function(rec){
            	showWeatherImg();
        }">
					</select>
				</div>
				<div style="margin: 5 0 0 5;">
					<img width="260px" height="160px" id="weatherImg" src="">
				</div>
			</div>
			<div data-options="region:'south'" style="height:50px; border:0;">
				<div class="messager-button">
					<a href="javascript:subWeatherAttrSet()" class="l-btn l-btn-small"
						group="" id="" style="margin-left: 10px;"><span
						class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
						href="javascript:closeWin('weatherAttrSetWin');"
						class="l-btn l-btn-small" group="" id=""
						style="margin-left: 10px;"><span class="l-btn-left"><span
							class="l-btn-text">取消</span></span></a>
				</div>
			</div>
		</div>
	</div>
	<div id="countAttrSetWin" class="easyui-window" title="属性设置"
		style="width:300px;height:380px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'"
				style="width:100%;height:260px;border:0;">
				<div style="margin: 5 0 0 40;">
				显示名称：<input id="countAttr_text"
				class="easyui-textbox" style="width:150px;"></input><br><br>
				到期时间：<input class="easyui-datetimebox" style="width:150px;"
							id="countAttr_time"
							data-options="editable:false,required:true,showSeconds:true,formatter:function(date){
				    var y = date.getFullYear();
					var m = date.getMonth()+1;
					var d = date.getDate();
					var o = date.getHours();
					var min = date.getMinutes();
					var s = date.getSeconds();
					if(o<10){
						o = '0'+o;
					}
					if(m<10){
						m = '0'+m;
					}
					if(d<10){
						d = '0'+d;
					}
					if(min<10){
						min = '0'+min;
					}
					if(s<10){
						s = '0'+s;
					}
					return y+'-'+m+'-'+d+' '+o+':'+min+':'+s;
				        }"
							value="3/4/2010 0:0:0" style="width:120px;"><br><br>
					选择样式：<select id="countStyleType" class="easyui-combobox"
						style="width:150px;"
						data-options="    
		missingMessage:'请选择',
        valueField: 'style',    
        textField: 'name',      
        onSelect: function(rec){
            	showCountImg();
        }">
					</select>
				</div>
				<div style="margin: 15 5;">
					<img width="290px" height="160px" id="countImg" src="">
				</div>
			</div>
			<div data-options="region:'south'" style="height:50px; border:0;">
				<div class="messager-button">
					<a href="javascript:subCountAttrSet()" class="l-btn l-btn-small"
						group="" id="" style="margin-left: 10px;"><span
						class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
						href="javascript:closeWin('countAttrSetWin');"
						class="l-btn l-btn-small" group="" id=""
						style="margin-left: 10px;"><span class="l-btn-left"><span
							class="l-btn-text">取消</span></span></a>
				</div>
			</div>
		</div>
	</div>
	<div id="timeAttrSetWin" class="easyui-window" title="属性设置"
		style="width:350px;height:250px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'"
				style="width:100%;height:260px;border:0;background-color: #cccccc">
				<div style="margin: 5 0 0 40;">
					样式选择：<select id="timeStyleType" class="easyui-combobox"
						style="width:120px;height:30px;"
						data-options="    
		missingMessage:'请选择',
        valueField: 'clockstyle',
        textField: 'name',
        onSelect: function(rec){    
            	showTimeImg(); 
        }">
					</select>
				</div>
				<div style="margin: 5 0 0 5;">
					<img width="300px" height="50px" id="timeImg" src="">
				</div>
			</div>
			<div data-options="region:'south'" style="height:50px; border:0;">
				<div class="messager-button">
					<a href="javascript:subTimeAttrSet()" class="l-btn l-btn-small"
						group="" id="" style="margin-left: 10px;"><span
						class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
						href="javascript:closeWin('timeAttrSetWin');"
						class="l-btn l-btn-small" group="" id=""
						style="margin-left: 10px;"><span class="l-btn-left"><span
							class="l-btn-text">取消</span></span></a>
				</div>
			</div>
		</div>
	</div>
	<div id="copySceneWin" class="easyui-window" title="复制场景"
		style="width:300px;height:180px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 5 0 0 20;">
			<input type="radio" name="copyAttr" checked="checked" value="copy"
				id="copyandnewscene">新建&nbsp;&nbsp;场景名称：<input type="text"
				id=sceneName_copy><br> <input type="radio"
				name="copyAttr" value="replace" id="copyreplacescene">替换&nbsp;&nbsp;选择场景：<input
				style="width:120px;" id="copySceneSelect">
			<div style="margin: 5 0 0 30;">
				<input type="checkbox" checked="checked" id="copyBackimg_box">&nbsp;复制场景背景<br>
				<input type="checkbox" checked="checked" id="copySize_box">&nbsp;复制排版布局<br>
			</div>
			<div class="messager-button">
				<a href="javascript:subCopyScene()" class="l-btn l-btn-small"
					group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
					href="javascript:closeWin('copySceneWin');"
					class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
			</div>
		</div>
	</div>
	<div id="newResolutionWin" class="easyui-window" title="自定义分辨率"
		style="width:240px;height:160px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 5 0 0 40;">
			<br> 宽：<input id="newResolution_width" type="text"
				class="easyui-numberbox" data-options="min:200,max:10000,precision:0"></input>
			<br> <br> 高：<input id="newResolution_height" type="text"
				class="easyui-numberbox" data-options="min:200,max:10000,precision:0"></input>

		</div>
		<div class="messager-button">
			<a href="javascript:subNewResolution()" class="l-btn l-btn-small"
				group="" id="" style="margin-left: 10px;"><span
				class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
				href="javascript:closeWin('newResolutionWin');"
				class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
				class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
		</div>
	</div>
	<div id="initnewResolutionWin" class="easyui-window" title="自定义分辨率"
		style="width:240px;height:160px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 5 0 0 40;">
			<br> 宽：<input id="initnewResolution_width" type="text"
				class="easyui-numberbox" data-options="min:200,max:10000,precision:0"></input>
			<br> <br> 高：<input id="initnewResolution_height" type="text"
				class="easyui-numberbox" data-options="min:200,max:10000,precision:0"></input>

		</div>
		<div class="messager-button">
			<a href="javascript:subinitNewResolution()" class="l-btn l-btn-small"
				group="" id="" style="margin-left: 10px;"><span
				class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
				href="javascript:closeWin('initnewResolutionWin');"
				class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
				class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
		</div>
	</div>	
	<div id="previewWin" class="easyui-window" title="节目预览"
		style="width:100%;height:100%"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div
			style="float: left;height: 100%;width: 100%;background-color: #EBEBE4;">
			<div id="pre_sceneBack" fit="true"
				style="position:relative;height: 100%;width: 100%;margin-left: auto;margin-right: auto;padding: 0;">
			</div>
		</div>
	</div>
	<div id="elemPlayAttrWin" class="easyui-window" title="区域播放属性"
		style="width:300px;height:190px;"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 5 0 0 20;">
			<br>播放时长(秒)：<input id="elemAttrSet" type="text"
				class="easyui-numberbox"
				data-options="min:1,max:1000000,precision:0"></input>
			<p style="padding: 0;color: red;">注：设置该区域每个素材的播放时长</p>
			<div id="piceffectdiv">切换效果：<input id="piceffect" class="easyui-combobox" editable="false" data-options="
				valueField: 'value',
				textField: 'label',
				panelHeight:120,
				width:100,
				data: [{
					label: '无',
					value: '0',
					selected:true   
					
				},{
					label: '向左平移',
					value: '1'
				},{
					label: '向右平移',
					value: '2'
				},{
					label: '淡入淡出',
					value: '3'
				}]" /></div>
			<div class="messager-button">
				<a href="javascript:subElemAttrSet()" class="l-btn l-btn-small"
					group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
					href="javascript:closeWin('elemPlayAttrWin');"
					class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
			</div>
		</div>
	</div>
	<div id="oneelemPlayAttrWin" class="easyui-window" title="素材播放属性"
		style="width:280px;height:150px;"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 5 0 0 20;">
			<input type="text" id="setelemId" style="display: none;">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;素材名称：<input id="elemAttrElemName"
				readonly="readonly" type="text" class="easyui-validatebox"></input><br>
			<br>播放时长(秒)：<input id="oneelemAttrSet" type="text"
				class="easyui-numberbox"
				data-options="min:1,max:1000000,precision:0"></input>
			<div class="messager-button">
				<a href="javascript:subOneElemAttrSet()" class="l-btn l-btn-small"
					group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
					href="javascript:closeWin('oneelemPlayAttrWin');"
					class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
			</div>
		</div>
	</div>
	<div id="prodefsetwin" class="easyui-window" title="节目属性设置"
		style="width:320px;height:170px;"
		data-options="closable:false,draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 15 0 0 30;">分辨率：
			<input id="proxy" class="easyui-combobox" data-options="
				valueField: 'label',
				textField: 'value',
				panelHeight:120,
				width:100,
				data: [{
					label: '1920*1080',
					value: '1920*1080',
					selected:true   
					
				},{
					label: '1600*900',
					value: '1600*900'
				},{
					label: '1366*768',
					value: '1366*768'
				},{
					label: '1024*768',
					value: '1024*768'
				}]" />
		<input type="button" value="自定义分辨率" onclick="zdyResolution()"/> 
		</div>
		<div style="margin: 10 0 0 30;">
			类型：<input type="radio" value="true" name="protype" 
				checked="checked" onclick="buttonStyleChange(0)">互动节目&nbsp;&nbsp;
				<input type="radio" value="false" name="protype" 
				onclick="buttonStyleChange(2)">普通节目
		</div>
			<div class="messager-button">
				<a href="javascript:initProDef()" class="l-btn l-btn-small"
					group="" id="" style="margin-left: 10px;"><span
					class="l-btn-left"><span class="l-btn-text">确定</span></span></a>
			</div>
		</div>
		<div id="htmlattrWin" class="easyui-window" title="网页属性设置"
		style="width:240px;height:140px"
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
		<div style="margin: 5 0 0 40;">
			<br><input type="checkbox" id="showtoolbarbox" value="true" checked="checked"></input>显示工具栏<br><br>
		</div>
		<div class="messager-button">
			<a href="javascript:subhtmlattr()" class="l-btn l-btn-small"
				group="" id="" style="margin-left: 10px;"><span
				class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a
				href="javascript:closeWin('htmlattrWin');"
				class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span
				class="l-btn-left"><span class="l-btn-text">取消</span></span></a>
		</div>
	</div>
</body>
</html>
