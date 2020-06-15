<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>terminal</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="js/easyui.css">
<link rel="stylesheet" type="text/css" href="js/demo.css">
<link rel="stylesheet" type="text/css" href="js/style.css">
<link rel="stylesheet" type="text/css" href="js/icon1.css">
<link rel="stylesheet" type="text/css" href="js/uplodify/uploadify.css">
<script type="text/javascript" src="js/jquery-1.7.min.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/msgbox.js"></script>
<script type="text/javascript" src="js/check.js"></script>
<script type="text/javascript" src="js/uplodify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="program/js/programList.js"></script>
<style type="text/css">
	.button_mouseover {
		background-color: #ffffff;
		border: solid 1px #1abc9c;
	}
	
	.datagrid-header-rownumber,.datagrid-cell-rownumber {
		width: 50px;
		text-align: center;
		margin: 0;
		padding: 0;
	}
	
	.button_div {
		float: left;
		margin-left: 10px;
		display: none;
	}
	
	.button_div_border {
		border: solid 1px #D4EBFF;
	}
	
	.button_img {
		width: 65px;
		cursor: pointer;
		margin: 5 5 5 5;
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
</head>
	<body class="easyui-layout">
		<div style="height:7%; background-color:transparent;">
			<img src="img/jmgltop.bmp" width="100%" />
		</div>
		<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
			<div style="height:7%;position: relative;">
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="addButton">
					<img onclick="addProgram()" title="新建节目" class="button_img"
						src="img/add.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="editButton">
					<img onclick="editProgram()" title="编辑节目" class="button_img"
						src="img/bj.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="deleteButton">
					<img onclick="deleteProgramCheck()" title="删除节目" class="button_img"
						src="img/sc.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="sendButton">
					<img onclick="publish()" title="发布节目" class="button_img"
						src="img/publish.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="stopButton">
					<img onclick="showStopWin()" title="停止节目命令"
						style="width:90px; cursor:pointer;margin: 5 5 5 5;"
						class="button_img" src="img/stop.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="auditButton">
					<img onclick="shoAuditWin()" title="审核节目" class="button_img"
						src="img/shh.png" />
				</div>
				<div style="float: left;margin-left: 10px;" class="button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)">
					<img onclick="datarefresh()" title="刷新节目" class="button_img"
						src="img/sx.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="exportButton">
					<img onclick="exportProgram()" title="导出节目" class="button_img"
						src="img/jmdc.png" />
				</div>
				<div class="button_div button_div_border"
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="exportButton1">
					<img onclick="insertProgramType()" title="导入节目" class="button_img"
						src="img/jmdx.png" />
				</div>
				<div style="float: right;">
					<div style=" margin: 6 0 0 3; position:absolute; ">
						<input id="searchName" class="easyui-textbox"
							style="width:150px; height:25px;" data-options="prompt:'请输入节目名称'">
					</div>
					<img onclick="searchProgram()" class="bgimage" src="img/search2.png"
						style="cursor:pointer; height:36px; width:200px;" align="middle" />
				</div>
			</div>
			<!-- <div style="height:7%;width: 100%;float: left;">
				<table border="0"
					style="padding-bottom:1px;padding-top:1px;">
					<tr>
						<td align="right" style="display:none;margin-left: 10px;;" id="addButton"><img onclick="addProgram()" title="新建节目"
							style="width:60px; cursor:pointer;" src="img/add.png" /></td>
						<td align="right" style="display:none;margin-left: 10px;;" id="editButton"><img title="编辑" onclick="editProgram()"
							style="width:60px;cursor:pointer;" src="img/bj.png" /></td>
						<td align="right" style="display:none;margin-left: 10px;;" id="deleteButton"><img onclick="deleteProgramCheck()"
							title="删除" style="width:60px;cursor:pointer;" src="img/sc.png" /></td>
						<td align="right" style="display:none;margin-left: 10px;;" id="sendButton"><img onclick="publish()" title="发布"
							style="width:60px;cursor:pointer;" src="img/publish.png" /></td>
						<td align="right" style="display:none;margin-left: 10px;;" id="stopButton"><img onclick="showStopWin()" title="停止"
							style="width:60px;cursor:pointer;" src="img/stop.png" /></td>
						<td align="right" style="display:none;margin-left: 10px;;" id="auditButton"><img title="审核" onclick="shoAuditWin()"
							style="width:60px;cursor:pointer;" src="img/shh.png" /></td>
						<td align="right"style="margin-left: 100px;;"><img onclick="datarefresh()" title="刷新"
							style="width:60px;cursor:pointer;" src="img/sx.png" /></td>
						<td align="right" style="display:none;margin-left: 10px;;" id="exportButton"><img onclick="exportProgram()" title="导出"
							style="width:60px;cursor:pointer;" src="img/jmdc.png" /></td>
						<td valign="middle" style="width:20%;">&nbsp;</td>
						<td align="right" style="width:180px;">
							<div style=" padding-top:1px; position:absolute; ">
								<input id="searchName" class="easyui-textbox"
									style="width:150px; height:25px" data-options="prompt:'请输入节目名称'">
							</div> <img onclick="searchProgram()" class="bgimage"
							src="img/search2.png"
							style="cursor:pointer; height:27px; width:180px;" align="middle" />
						</td>
						<td valign="middle" style="width:2%;">&nbsp;</td>
					</tr>
				</table>
			</div> -->
			<div style="height:85%;width: 100%;float: left;">
				<table style="width:100%;height:100%" id="jmgl"></table>
			</div>
		</div>
		<div id="sendProgramWin" class="easyui-window" title="发布节目" style="width:1200px;height:600px;padding:20px;"
			data-options="modal:true,closed:true,resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:true">
			<div class="easyui-layout" style="width:99%;height:99%;background-color: #e7f2fc;border: 1px solid #9ec8ee;padding: 30px;font-family: 微软雅黑;">
				<div style="position:relative;width:100%;height:88%; ">
					<form id="programForm" method="post" style="width:100%;height:92%;">
					<div class="sendProgramWinDiv">
								 <input style="display: none;" id="program_id" name="program_id">
						节目名称：<input id="programName" type="easyui-text" name="name"style="margin-top: 20px;width: 170px;height:25px; -moz-border-radius: 5px; -webkit-border-radius:5px;border-radius:5px; ">
						<div style="margin-top: 15px;">
							开始时间：<input class="easyui-datetimebox" id="startdate"name="startdate"
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
												if(min<10){
													min = '0'+min;
												}
												if(s<10){
													s = '0'+s;
												}
												if(m<10){
													m = '0'+m;
												}
												if(d<10){
													d = '0'+d;
												}
												return y+'-'+m+'-'+d+' '+o+':'+min+':'+s;
											  }"
								value="3/4/2010 00:00:00" style="width:170px;">
						</div>
						<div style="margin-top: 15px;">
							结束时间：<input class="easyui-datetimebox" id="stopdate" name="stopdate"
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
										if(min<10){
											min = '0'+min;
										}
										if(s<10){
											s = '0'+s;
										}
										if(m<10){
											m = '0'+m;
										}
										if(d<10){
											d = '0'+d;
										}
										return y+'-'+m+'-'+d+' '+o+':'+min+':'+s;
									        }"
								value="3/4/2010 23:59:59" style="width:170px;">
						</div>
						<div style="margin-top: 15px;">
						    <span style="">播出单发布权限：</span>
						    <select id="pubpower" style="width:80px;height:23px;color:#aaaaaa; padding-left:10px;border:1px solid #7fb7ea;-moz-border-radius: 5px; -webkit-border-radius:5px;border-radius:5px;"></select>
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
					<div id="sendTreeDiv" class="sendProgramWinDiv" style="margin-left: 5px;">
						<div style="margin-top: 17px;">
							<b>发布终端</b>
						</div>
						<div style="margin-top: 17px;overflow:auto;height: 300px;">
							<ul id="sendtree"></ul>
						</div>
					</div>
					<div class="sendProgramWinDiv" style="margin-left: 5px;">
					    <div style="position: relative;width:100%;height:80%;">
							<div id="lowgradeview" style="width: 100%;">
								<div style="margin-top: 17px;">
									<b>日循环</b>
								</div>
								<div style="margin-top: 17px;">
									开始时间：<input id="day_startTime" name="day_startTime"
										class="easyui-timespinner" style="width:170px;"
										required="required" data-options="showSeconds:true"
										value="00:00:00" />
								</div>
								<div style="margin-top: 17px;">
									结束时间：<input id="day_endTime" name="day_endTime"
										class="easyui-timespinner" style="width:170px;"
										required="required" data-options="showSeconds:true"
										value="23:59:59" />
								</div>
								<div style="margin-top: 60px;">
									<b>周循环</b>
								</div>
								<div style="margin-top: 17px;">
									<div style="float: left;">
										<span style="margin-left: 15px;">一</span><br> <input
											name="days" value="Monday" checked="checked"
											style="margin-left: 15px;" type="checkbox">
									</div>
									<div style="float: left;">
										<span style="margin-left: 15px;">二</span><br> <input
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
										<span style="margin-left:15px;">五</span><br> <input
											name="days" value="Friday" checked="checked"
											style="margin-left: 15px;" type="checkbox">
									</div>
									<div style="float: left;">
										<span style="margin-left: 15px;">六</span><br> <input
											name="days" value="Saturday" checked="checked"
											style="margin-left:15px;" type="checkbox">
									</div>
									<div style="float: left;">
										<span style="margin-left: 15px;">日</span><br> <input
											name="days" value="Sunday" checked="checked"
											style="margin-left: 15px;" type="checkbox">
									</div>
								</div>
							</div>
							<div id="highgradegrid" style="width: 100%;height:230px;margin-top: 17px;position: relative;font-size: 10px;">
									<div><b>循环周期</b></div>
									<div style="margin-top: 8px;">
									<table id="highgradegridtable" class="easyui-datagrid" style="width:330px;height:100%;font-size: 10px;margin-top: 28px" 
									data-options="rowStyler: function(index,row){
										return 'height:25px;'},
										url:'',
										autoRowHeight:false,
										fitColumns:true,
										singleSelect:false,
										remoteSort : false,
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
									            <th data-options="field:'circleweek',align:'center',width:115">周循环</th>   
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
					</form>
				</div>
				<div style="width:300px;height:50px;position: relative;margin-top: 20px;text-align: center;padding:2px;margin-left:400px; text-align: center;">
						<span style="" id="copyProgramButton" class="buttonStyle" onclick="copy()">确定</span>
						<span style="" id="sendProgramButton" class="buttonStyle"  onclick="send()">发布</span>
						<span onclick="$('#sendProgramWin').dialog('close');"  class="buttonStyle" style="margin-left: 50px;">取消</span>
			   </div>
		</div>
		</div>
		<!-- 	<div id="auditWin" class="easyui-dialog" title="审批节目单"
			style="width:240px;height:110px"
			data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false">
			<div style="position: absolute;top: 60px;left: 23px;">
				<a href="javascript:void(0)" class="l-btn l-btn-small"
					style="margin-left: 10px;width: 80px;" onclick="audit(1)"><span
					class="l-btn-left"><span class="l-btn-text">审批通过</span></span></a> <a
					href="javascript:void(0)" class="l-btn l-btn-small"
					style="margin-left: 10px;width: 80px;" onclick="audit(2)"><span
					class="l-btn-left"><span class="l-btn-text">审批不通过</span></span></a>
			</div>
		</div> -->
		<div id="exportWin" class="easyui-window" title="导出节目单"
			minimizable="false"
			data-options="resizable:false,collapsible:false,minimizable:false,draggable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
			style="width:628px;height:400px;padding:10px;">
			<div id="exportdiv" title="节目导出"></div>
		</div>
		<div id="exportWin1" class="easyui-window" title="导入节目单"
			minimizable="false"
			data-options="resizable:false,collapsible:false,minimizable:false,draggable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
			style="width:400px;height:400px;padding:10px;">
				<div id="exportdiv1" title="节目导出"></div>
				<div id="uploadfileQueue"></div>
				<input type="file" id="file_upload" value="请选择文件">
				<input type="button" value="上传" onclick="javascript:$('#file_upload').uploadify('upload','*');" />
				<input type="button" value="取消上传" onclick="javascript:$('#file_upload').uploadify('stop')"/>
		</div>
		<div id="sendTerminalWin" class="easyui-window" title="发布终端列表"
			minimizable="false"
			data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save',
				onClose:function(){
					$('#jmgl').datagrid('reload');
					stopRefresh(stopRefreshTerminalGrid);
				}"
			style="width:700px;height:440px;">
			<table class="easyui-datagrid" style="width:100%;height:100%"
				id="sendTerminalGrid"
				data-options="url:'program/querySendP_T.do',fitColumns:true,singleSelect:false,remoteSort : false,loadMsg:null,
			     frozenColumns : [[{
			         field : 'ck',
			         checkbox : true
			     }]],
			     rownumbers : true,toolbar:[{
			    	 text:'停止命令',
			    	 iconCls:'icon-cancel',
			    	 handler:function(value,row,index){
			    	 	stopTerminal();
			    	 }
			     }]">
				<thead>
					<tr>
						<th data-options="field:'terminal_name',width:110">终端名称</th>
						<th data-options="field:'program_id',hidden:true">节目标示</th>
						<th data-options="field:'terminal_ip',width:150">终端IP</th>
						<th data-options="field:'mac',width:160,hidden:true">终端MAC</th>
						<th data-options="field:'type',width:90,formatter:function(value,row,index){
											return value==0?'普通':'互动';}">节目类型</th>
						<th data-options="field:'program_status',width:110,formatter:function(value,row,index){
											if(value == null){
												return '';
											}
											var statustr = ['下载失败','待下载','','下载成功','下载中断'];
											return value.indexOf('\%')>-1?value:statustr[value];}">
						下载状态</th>
						<th data-options="field:'schedulelevel',width:60">优先级</th>
						<th data-options="field:'group_name',width:110">所属终端组</th>
						<th data-options="field:'materials',
											formatter : function(value, row, index) {
												return doDiv(value, row, index);
											}">
						素材</th>
											
					</tr>
				</thead>
			</table>
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
							function(value, row, index) {
								if(row.element_type != 7){
									if(parseInt(value) < 1024*1024){
										var num = parseFloat(value)/1024;
										return num.toFixed(2)+'KB';
									}else if(parseInt(value) >= 1024*1024 && value < 1024*1024*1024){
										var num = parseFloat(value)/(1024*1024);
										return num.toFixed(2)+'MB';
									}else{
										var num = parseFloat(value)/(1024*1024*1024);
										return num.toFixed(2)+'GB';
									}
								}else{
									return '实际转换图片'+value+'个';
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
		<div id="addloopWin" class="easyui-window" title="设置循环时间"
			minimizable="false" data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true"
			style="width:280px;height:260px;">
			<div style="margin-left: 52px;">
					<div style="margin-top: 17px;">
						<b>日循环</b>
					</div>
					<div style="margin-top: 17px;">
						开始时间：<input id="loop_days" name="loop_days"
							class="easyui-timespinner" style="width:85px;"
							required="required" data-options="showSeconds:true"
							value="00:00:00" />
					</div>
					<div style="margin-top: 17px;">
						结束时间：<input id="loop_daye" name="loop_daye"
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
		<div id="showProgramLogWin" class="easyui-window" title="节目操作日志"
		minimizable="false"
		data-options="resizable:false,collapsible:false,draggable:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save',
			onClose:function(){
				$('#pjmgl').datagrid('reload');
			}"
		style="width:450px;height:440px;">
		<div id="numdiv" style="height:5%;font-size: 12px;"></div>
		<table class="easyui-datagrid" style="width:100%;height:95%"
			id="showProgramLogGrid"
			data-options="url:'program/queryProgramLog.do?',fitColumns:true,singleSelect:false,remoteSort : false,loadMsg:null,
		     rownumbers : true,pagination:true">
			<thead>
				<tr>
					<th data-options="field:'logID',hidden:true">日志id</th>
					<th data-options="field:'operationType',align:'center',width:'20%',
						formatter : function(value) {
							if(value == 0){
								return '创建';
							}else if(value == 1){
								return '编辑';
							}else if(value == 2){
								return '发布';
							}else if(value == 3){
								return '停止发布';
							}else if(value == 4){
								return '保存';
							}else if(value == 5){
								return '审核';
							}
						}">操作类型</th>
					<th data-options="field:'userName',align:'center',width:'25%'">操作人</th>
					<th data-options="field:'createTime',align:'center',width:'45%',
						formatter : function(value) {
							return new Date(value).toLocaleString();
						}">操作时间</th>
				</tr>
			</thead>
		</table>
	</div>
	</body>
</html>