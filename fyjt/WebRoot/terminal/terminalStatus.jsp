<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>terminalStatus</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<link rel="stylesheet" type="text/css" href="js/easyui.css">
<link rel="stylesheet" type="text/css" href="js/demo.css">
<link rel="stylesheet" type="text/css" href="js/icon1.css">
<link rel="stylesheet" type="text/css" href="js/style.css">
<link rel="stylesheet" type="text/css" href="terminal/js/css/strip/strip.css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="terminal/terminalStatus.js"></script>
<script type="text/javascript" src="terminal/js/strip.js"></script>
<script type="text/javascript" src="js/msgbox.js"></script>
<style type="text/css">
.button_div {
	float: left;
	margin-left: 10px;
	margin-top: 3px;
	display: inline;
}
.button_div_border {
	border: solid 1px #D4EBFF;
}

.button_img {
	height: 18px;
	cursor: pointer;
	margin:  3px 0 0 0;
	padding:1px 15px 4px 4px;
}

.button_mouseover {
	background-color: #ffffff;
	border: solid 1px #1abc9c;
	
}
.t_operation{
    left:609px;
    top:31px;
	display: none;
	width:120px;
	height:185px;
	position: absolute;
	z-index: 123123;
	background-color: #FFFFFF;
	border:1px solid #5CACEE;
}
.t_operation_c{
   margin: 2px  ;
}
</style>
<script type="text/javascript">
	document.onreadystatechange = subSomething;//当页面加载状态改变的时候执行这个方法.
	function subSomething() {
 		if(document.readyState=="complete"){
			var target =  document.getElementById('t_operation');
			document.onclick = function(){
				var flag = $("#t_operation")[0].style.display;
				$("#t_operation").fadeOut(500);
				flag="none";
			};
			target.onclick = function(){
				if(document.all){
					window.event.cancelBubble = true;
			 	}else{
					event.stopPropagation(); 
				}
			}
	 	}
	}
	var stopRefreshTerminalGrid = '';var stopRefreshDetailGrid = '';
	function buttonover(event) {
		event = event ? event : window.event;
		var temp = event.currentTarget;
		$(temp).removeClass("button_div_border");
		$(temp).addClass("button_mouseover");
	}
	function buttonout(event) {
		event = event ? event : window.event;
		var temp = event.currentTarget;
		$(temp).removeClass("button_mouseover");
		$(temp).addClass("button_div_border");
	}
	function init(){
		$.ajax({ 
	    	url: "modulepower/queryModulePowerID.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    	async: false,
	    	success: function(result){
				for(var i=0;i<result.moduleList.length;i++){
					if(result.moduleList[i]==31){
						$("#screenView_b").show();
					}
					if(result.moduleList[i]==32){
						$("#showDown_b").show();
						$("#showTask_b").show();
						$("#setClose_b").show();
						$("#controlOpen_b").show();
						$("#controlClose_b").show();
						$("#controlRestart_b").show();
						$("#soundSet_b").show();
					}
					if(result.moduleList[i]==33){
						$("#controlUpdate_b").show();
					}
				}
	      	}
	      });
	}
	function openclear() {
		$('#open_time').timespinner('setValue', ''); 
	}
	function closeclear() {
		$('#close_time').timespinner('setValue', ''); 
	}
	function edopenclear() {
		$('#edopen_time').timespinner('setValue', ''); 
	}
	function edcloseclear() {
		$('#edclose_time').timespinner('setValue', ''); 
	}
</script>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->



</head>

<body onload="init()" class="easyui-layout">
	<!-- top start -->
	<div data-options="region:'north',border:true"
		style="height:7%; border-color:#D4EBFF; background-color:transparent;">
		<img src="img/zdjktop.bmp" width="100%" />
	</div>
	<!-- top end -->

	<!-- tree start -->
	<div data-options="region:'west',border:true" title="终端树"
		style="width:15%;height:100%; border:'thin';">
		<div style=" width:100%;">
			<ul id="tt" class="easyui-tree"></ul>
		</div>
	</div>
	<!-- tree end -->

	<!-- datagrid start -->
	<div data-options="region:'center',border:true" title="终端列表"
		style="border:'thin';height:100%;background-color:#D4EBFF;"
		id="mains">
		<div style="height:6%;width:100%;position: relative;display: block;">
			<div class="button_div" style="">
				<select id="sysCondition" class="easyui-combobox" name="sysCondition" style="height:25px;width:88px;"  
					panelHeight="auto" data-options="editable:false">
					<option selected="selected" value="10">全部</option>
					<option value="1">Windows 终端</option>
					<option value="2">Android 终端</option>
				</select>
			</div>
			<div  class="button_div" style="">
				<select id="filterCondition" class="easyui-combobox" name="filterCondition" style="height:25px;width:60px;"  
					panelHeight="auto" data-options="editable:false">
					<option selected="selected" value="10">全部</option>
					<option value="1">在线</option>
					<option value="0">离线</option>
					<option value="2">异常</option>
				</select>
			</div>
			
			<div class="button_div button_div_border "
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="screenView_b">
				<img onclick="screenView()" class="button_img" src="img/screen_view.png" />
			</div>
			<div class="button_div button_div_border "
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="showDown_b">
				<img onclick="showTaskDownLoad()" class="button_img"
					src="img/down_status.png" />
			</div>
			<div class="button_div button_div_border "
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="showTask_b">
				<img onclick="showTerminalTask()" class="button_img"
					src="img/tastList.png" />
			</div>
			<div  class="button_div button_div_border" style="width:57px" 
				onmouseover="buttonover(event)" onmouseout="buttonout(event)">
				<img onclick="refresh()" class="button_img"
					src="img/shuaxin.png" />
		   </div>
			<div  class="button_div button_div_border" style="150px" 
				onmouseover="buttonover(event)" onmouseout="buttonout(event)">
				<img onclick="t_operation()" class="button_img"
					src="img/terminal_operation.png"  id="t_operationid"/>
		   </div>
		   <!-- 终端操作 -->
		   <div id="t_operation"  class="t_operation"style="">
			   <div class=" button_div_border t_operation_c" style=""
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="setClose_b">
					<img onclick="setCloseTime();$('#t_operation').fadeOut(500);" class="button_img" src="img/set_closetime.png" />
				</div>
				<div class=" button_div_border t_operation_c" id="controlOpen_b"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)">
					<img onclick="control('open','0');$('#t_operation').fadeOut(500);" class="button_img"
						src="img/control_open.png" />
				</div>
				<div class=" button_div_border t_operation_c" id="controlClose_b"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)">
					<img onclick="control('close','0');$('#t_operation').fadeOut(500);" class="button_img"
						src="img/control_close.png" />
				</div>
				<div class=" button_div_border t_operation_c" id="controlRestart_b"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)">
					<img onclick="control('restart','0');$('#t_operation').fadeOut(500);" class="button_img"
						src="img/control_restart.png" />
				</div>
				<div class=" button_div_border t_operation_c " id="soundSet_b"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)">
					<img onclick="$('#yl').window('open');$('#t_operation').fadeOut(500);" class="button_img"
						src="img/sound_set.png" />
				</div>
				<div class="button_div_border t_operation_c" id="controlUpdate_b"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)">
					<img onclick="control('updateApp','0');$('#t_operation').fadeOut(500);" class="button_img"
						src="img/updatesoft.png" />
				</div>
		   </div>
		   <!-- 定时下载设置 -->
		   <div class="button_div button_div_border "
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="timingset" style="margin-left:0px;">
				<img onclick="showTerminalTimingWin()" class="button_img"
					src="img/timingset.png" />
		   </div>
		</div>
		<table style="height:94%;width:100%" id="zdjk"></table>
	</div>
	<!-- datagrid end --><!--
	<div id="downtbs" style="padding:0px;height:auto">
		<div>
			<a id="downing" class="easyui-linkbutton" style="display:none;"
				onclick="downing()" iconCls="icon-ckpm" plain="true">查看下载中任务</a> <a
				id="downed" class="easyui-linkbutton" style="display:none;"
				onclick="downed()" iconCls="icon-ckrw" plain="true">查看已完成任务</a> <a
				id="downdel" class="easyui-linkbutton" style="display:none;"
				onclick="downdel()" iconCls="icon-close" plain="true">删除</a>
		</div>
	</div>

	--><div id="pt" style="padding:0px;height:auto">
		<div>
			<a class="easyui-linkbutton" onclick="stopProgram()"
				iconCls="icon-ckpm" plain="true">停止任务</a>
		</div>
	</div>
	<!-- ToolBar end -->
	<!-- Window start -->
	<div id="yl" class="easyui-window" title="音量设置" resizable="false"
		collapsible="false" maximizable="false" minimizable="false"
		data-options="modal:true,closed:true"
		style="overflow: hidden;width:250px;height:150px;padding:30px;">
		<div id="sld">
			<input id="sliders" class="easyui-slider" value="50"
				style="width:200px"
				data-options="showTip:true,rule:[0,'|',25,'|',50,'|',75,'|',100]" />
		</div>
		<input type="button" value="确定" onclick="setVolum()"
			style="margin-top:35px;margin-left:80px;">
	</div>
	<div id="screen" class="easyui-window" title="查看屏幕画面"
		minimizable="false"
		data-options="draggable:false,modal:true,closed:true,onClose:function (){ clearInterval(task2);}"
		style="width:570px;height:500px;padding:10px;">
	</div>

	<div id="taskDownLoad" class="easyui-window" title="查看任务下载状态"
		resizable="false" collapsible="false" maximizable="false"
		minimizable="false"
		data-options="modal:true,closed:true,onClose:function (){stopRefresh(stopRefreshTerminalGrid);}"
		style="width:600px;height:400px;padding:10px;">
		<table style="height:100%" id="rwxz"></table>
	</div>

	<div id="program_terminal" class="easyui-window" title="查看终端任务"
		resizable="false" collapsible="false" maximizable="false"
		minimizable="false" data-options="modal:true,closed:true"
		style="width:400px;height:400px;padding:10px;">
		<table style="height:100%" id="zdrw"></table>
	</div>


	<div id="closeTime" class="easyui-window" title="设置终端自动开关机时间"
		resizable="false" collapsible="false" maximizable="false"
		minimizable="false" data-options="modal:true,closed:true"
		style="width:400px;height:340px;padding:2px;">
		<div
			style="padding-top:20px; width:100%; height:100%;vertical-align:middle; position:absolute; ">
			<div style="margin-left: 7px;">
			<table id="timesettable" class="easyui-datagrid" style="width:380px;height:100%;font-size: 10px;margin-top: 28px" data-options="rowStyler: function(index,row){
				return 'height:25px;'},url:'',autoRowHeight:false,fitColumns:true,singleSelect:false,remoteSort : false,
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
			      iconCls:'icon-delete',
			    	 handler:function(value,row,index){
			    	 	delloop();}
			    }]">   
			    <thead>   
			        <tr> 
			        	<th data-options="field:'terminalid',align:'center',width:70,hidden:true">终端ID</th>   
			            <th data-options="field:'terminalname',align:'center',width:70">终端名称</th>
			            <th data-options="field:'terminalmac',align:'center',width:70,hidden:true">终端MAC</th>    
			            <th data-options="field:'opentime',align:'center',width:70">开机时间</th>   
			            <th data-options="field:'closetime',align:'center',width:70">关机时间</th>
			            <th data-options="field:'days',align:'center',width:115">执行周期</th>
			        </tr>   
			    </thead>   
			</table>
			</div> 
			<table align="center">
				<tr align="right" style="display:none">
					<td style="color:#666666; font-size:14px;">终端ID</td>
					<td><input id="terminalId" class="easyui-textbox"></td>
				</tr>
				<tr align="right" style="display:none">
					<td style="color:#666666; font-size:14px;">开机时间：</td>
					<td><input id="startTime" class="easyui-timespinner"
						style="width:80px;" 
						data-options="showSeconds:false,missingMessage:'输入有误'" /></td>
				</tr>
				<tr align="right" style="display:none">
					<td style="color:#666666; font-size:14px;">关机时间：</td>
					<td><input id="endTime" class="easyui-timespinner"
						style="width:80px;" 
						data-options="showSeconds:false,missingMessage:'输入有误'" /></td>
				</tr>
				<tr>
					<td align="right">&nbsp;</td>
					<td align="center">&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><a id="groupWindowButton"
						onclick="updateCloseTime()"
						style="cursor:pointer;color:#0E4876; font-size:14px;">确定</a><a id="delAllTimeButton"
						onclick="delAllCloseTime()"
						style="cursor:pointer;color:#0E4876; margin-left:10px;font-size:14px;">清空</a><a
						onclick="$('#closeTime').window('close')"
						style="cursor:pointer;color:#0E4876;margin-left:10px; font-size:14px;">取消</a>
					</td>
				</tr>
			</table>
		</div>
		<img class="bgimage" src="img/u106.png"
			style=" height:100%; width:100%;" align="middle" />
	</div>
	<!-- Window end -->
		<div id="addloopWin" class="easyui-window" title="设置循环时间"
		minimizable="false" data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true"
		style="width:280px;height:260px;">
		<div style="margin-left: 30px;">
				<div style="margin-top: 17px;">
					<b>时间设置</b>
				</div>
				<div style="margin-top: 17px;">
					开机时间：<input id="open_time" name="open_time"
						class="easyui-timespinner" style="width:85px;"
						data-options="showSeconds:false"
						value="00:00" /><span id="startbutton" style="cursor:pointer;color:#0E4876; font-size:12px;"
						onclick="openclear()">&nbsp;&nbsp;取消设置</span>
				</div>
				<div style="margin-top: 17px;">
					关机时间：<input id="close_time" name="close_time"
						class="easyui-timespinner" style="width:85px;"
						data-options="showSeconds:false"
						value="23:59" /><span id="closebutton" style="cursor:pointer;color:#0E4876; font-size:12px;" 
						onclick="closeclear()">&nbsp;&nbsp;取消设置</span>
				</div>
				<div style="margin-top: 17px;">
					<b>周循环</b>
				</div>
				<div style="margin-top: 17px;">
					<div style="float: left;">
						<span style="margin-left: 8px;">一</span><br> <input
							name="loopdays" value="Monday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">二</span><br> <input
							name="loopdays" value="Tuesday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">三</span><br> <input
							name="loopdays" value="Wednesday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">四</span><br> <input
							name="loopdays" value="Thursday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">五</span><br> <input
							name="loopdays" value="Friday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">六</span><br> <input
							name="loopdays" value="Saturday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">日</span><br> <input
							name="loopdays" value="Sunday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
				</div>
		</div>
		<div id="loopsavebutton"><span style="position: absolute;top: 235px;left:220px;margin-bottom: -20px;margin-right: 15px;
			color: blue;cursor:pointer; font-size: 14px;" onclick="loopsave()">确定</span>
		</div>
	</div>
	<div id="editloopWin" class="easyui-window" title="编辑循环时间"
		minimizable="false" data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true"
		style="width:280px;height:260px;">
		<div style="margin-left: 52px;">
				<div style="margin-top: 17px;">
					<b>日循环</b>
				</div>
				<div style="margin-top: 17px;">
					开机时间：<input id="edopen_time" name="edopen_time"
						class="easyui-timespinner" style="width:85px;"
						data-options="showSeconds:false"
						value="00:00:00" /><span id="startbutton" style="cursor:pointer;color:#0E4876; font-size:12px;"
						onclick="edopenclear()">&nbsp;&nbsp;取消设置</span>
				</div>
				<div style="margin-top: 17px;">
					关机时间：<input id="edclose_time" name="close_time"
						class="easyui-timespinner" style="width:85px;"
						data-options="showSeconds:false"
						value="23:59:59" /><span id="closebutton" style="cursor:pointer;color:#0E4876; font-size:12px;" 
						onclick="edcloseclear()">&nbsp;&nbsp;取消设置</span>
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
		<div id="showDetailWin" class="easyui-window" title="素材详情"
		minimizable="false"
		data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save',
			onClose:function(){
				$('#jmgl').datagrid('reload');
				stopRefresh(stopRefreshDetailGrid);
			}"
		style="width:450px;height:440px;">
		<div id="numdiv" style="height:5%;font-size: 12px;">
		</div>
		<table class="easyui-datagrid" style="width:100%;height:95%"
			id="showDetailGrid"
			data-options="url:'program/queryMaterialDownload.do',fitColumns:true,singleSelect:false,remoteSort : false,loadMsg:null,
		     rownumbers : true">
			<thead>
				<tr>
					<th data-options="field:'element_id',hidden:true">素材id</th>
					<th data-options="field:'element_type',align:'center',width:'16%',
						formatter : function(value) {
							if(value == 1){
								return '文本';
							}else if(value == 2){
								return '音频';
							}else if(value == 3){
								return '图片';
							}else if(value == 4){
								return '视频';
							}else if(value == 5){
								return '网页';
							}else if(value == 6){
								return 'flash';
							}else if(value == 7){
								return 'office文件';
							}else if(value == 8){
								return '流媒体';
							}else if(value == 9){
								return '其他';
							}
						}"
					>素材类型</th>
					<th data-options="field:'element_name',align:'center',width:'25%'">素材名称</th>
					<th data-options="field:'element_size',align:'center',width:'30%',formatter : 
						function(value) {
							if(value < 1024*1024){
								var num = parseFloat(value)/1024;
								return num.toFixed(2)+'KB';
							}else if(value >= 1024*1024 && value < 1024*1024*1024){
								var num = parseFloat(value)/(1024*1024);
								return num.toFixed(2)+'MB';
							}else{
								var num = parseFloat(value)/(1024*1024*1024);
								return num.toFixed(2)+'GB';
							}
						}">素材大小</th>
					<th data-options="field:'element_status',align:'center',width:'20%',formatter:function(value,row,index){
						if(value == null){
							return '';
						}
						var statustr = ['下载失败','待下载','','下载成功','下载中断'];
						return value.indexOf('\%')>-1?value:statustr[value];
					}">素材状态</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="terminalTimingWin" class="easyui-window" title="定时下载设置"
		resizable="false" collapsible="false" maximizable="false"
		minimizable="false" data-options="modal:true,closed:true,iconCls:'icon-save'"
		style="width:450px;height:400px;padding:10px;">
		<div>
			<a id="downing"
						style=""
						class="easyui-linkbutton" data-options="iconCls:'icon-add'"
						onclick="addTimingSet()">添加下载设置</a>
			<a id="downdel"
						style=""
						class="easyui-linkbutton" data-options="iconCls:'icon-close'"
						onclick="delTimingSet()">删除下载设置</a>
			<%-- 
			     <a id="downing" class="easyui-linkbutton" style=""
				onclick="addTimingSet()" iconCls="icon-add" plain="true">添加下载设置</a> <a
				id="downdel" class="easyui-linkbutton" style=""
				onclick="delTimingSet()" iconCls="icon-close" plain="true">删除下载设置</a>
				--%>
		</div>
		<table style="height:85%" id="dsxz"></table>
		<div>
			<a id="ensuredownloadset" class="easyui-linkbutton" style="float:right;"
				onclick="saveDownloadSet()" iconCls="icon-ok" plain="true">确定</a> 
		</div>
	</div>
	<div id="adddownloadsetWin" class="easyui-window" title="设置循环时间"
		minimizable="false" data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true"
		style="width:280px;height:260px;">
		<div style="margin-left: 30px;">
				<div style="margin-top: 17px;">
					<b>时间设置</b>
				</div>
				<div style="margin-top: 17px;">
					开始下载时间：<input id="start_download_time" name="start_download_time"
						class="easyui-timespinner" style="width:85px;"
						data-options="showSeconds:false"
						value="00:00" />
				</div>
				<div style="margin-top: 17px;">
					结束下载时间：<input id="end_download_time" name="end_download_time"
						class="easyui-timespinner" style="width:85px;"
						data-options="showSeconds:false"
						value="23:59" />
				</div>
				<div style="margin-top: 17px;">
					<b>周循环</b>
				</div>
				<div style="margin-top: 17px;">
					<div style="float: left;">
						<span style="margin-left: 8px;">一</span><br> <input
							name="loopdownloaddays" value="Monday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">二</span><br> <input
							name="loopdownloaddays" value="Tuesday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">三</span><br> <input
							name="loopdownloaddays" value="Wednesday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">四</span><br> <input
							name="loopdownloaddays" value="Thursday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">五</span><br> <input
							name="loopdownloaddays" value="Friday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">六</span><br> <input
							name="loopdownloaddays" value="Saturday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
					<div style="float: left;">
						<span style="margin-left: 8px;">日</span><br> <input
							name="loopdownloaddays" value="Sunday" checked="checked"
							style="margin-left: 7px;" type="checkbox">
					</div>
				</div>
		</div>
		<div id="downloadsavebutton"><span style="position: absolute;top: 235px;left:220px;margin-bottom: -20px;margin-right: 15px;
			color: blue;cursor:pointer; font-size: 14px;" onclick="downloadsetsave()">确定</span>
		</div>
	</div>
</body>
</html>
