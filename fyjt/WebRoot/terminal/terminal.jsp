<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="terminal/terminal.js"></script>
	<script type="text/javascript" src="js/msgbox.js"></script>
	<style type="text/css">
		.button_div {
			float: left;
			margin-left: 10px;
			margin-top: 3px;
			display: none;
		}
		.button_div_min {
			float: left;
			margin-left: 3px;
			margin-top: 5px;
			display: none;
		}
		.button_div_border {
			border: solid 1px #D4EBFF;
		}
		.button_img {
			width: 60px;
			cursor: pointer;
			margin: 3 3 3 3;
		}
		.button_img_min {
			width: 50px;
			cursor: pointer;
			margin: 3 3 3 3;
		}
		.button_mouseover {
			background-color: #ffffff;
			border: solid 1px #1abc9c;
		}
		.add_terminal_font_style{
		  font-size:14px;
		  font-family: 微软雅黑;
		}
		.add_terminal_table_tr{
		  margin-top: 20px;
		}
		.add_terminal_table_tr td input{
		  width:300px;
		  font-size: 14px;
		  font-family: 微软雅黑;
		}
	</style>
	<script type="text/javascript">
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
	</script>
  </head>
<body class="easyui-layout" >
	<div data-options="region:'north',border:true" style="height:7%; background-color:transparent;"> 
		<img src="img/zdgltop.bmp" width="100%" />
	</div>
	<div data-options="region:'west',border:true" title="终端树"  style="width:16%; ">
		<div style="width: 100%;height:38px;border: none;background-color:#D4EBFF;">
			<div style="position: relative;">
				<div class="button_div_min button_div_border" 
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="addGroup">
					<img onclick="$('#z').window('open');addGroup()" title="添加" class="button_img_min"
						src="img/add.png" />
				</div>
				<div class="button_div_min button_div_border" 
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="editGroup">
					<img onclick="editGroup()" title="编辑" class="button_img_min"
						src="img/bj.png" />
				</div>
				<div class="button_div_min button_div_border" 
					onmouseover="buttonover(event)" onmouseout="buttonout(event)"
					id="delGroup">
					<img onclick="deleteTerminalGroup()" title="删除" class="button_img_min"
						src="img/sc.png" />
				</div>
			</div>
		</div>
	<div style=" width:100%;">
		<ul id="tt" class="easyui-tree"></ul>
	</div>
		
	</div>
	<!-- 
	<div  data-options="region:'east',border:true" title="被动添加终端" style="width:300px;overflow:hidden">
	<table width="100%"  height="6%" border="0"  style="background-color:#D4EBFF;padding-bottom:5px;padding-top:5px;">
		  <tr>
			<td align="center" style="width:80px"><img onclick="bdtj()" title="添加" style="height:20px; width:50px;cursor:pointer;" src="img/add.png"/></td>
			<td align="center" style="width:80px"><img onclick="deteleBdtj()" title="删除" style="height:18px; width:40px;cursor:pointer;" src="img/sc.png"/></td>
		  	<td>&nbsp;</td>
		  </tr>
		</table>
		<table style="height:94%" id="bdtj"></table>  
	</div> -->
	<div data-options="region:'center'" title="终端列表" style="border:thin;background-color:#D4EBFF;overflow:hidden" id="mains">
<!-- 终端列表头开始 -->
<div style="height:6%;position: relative;">
		<div class="button_div button_div_border" 
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="addtd">
			<img onclick="$('#w').window('open');adds()" id="add" class="button_img"
				src="img/add.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="edittd">
			<img onclick="edits()" id="edit"  class="button_img"
				src="img/bj.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="deltd">
			<img  id="del" onclick="deleteTerminal()" class="button_img"
				src="img/sc.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="audtd">
			<img   id="aud" onclick="audit_terminal()" class="button_img"
				src="img/shh.png" />
		</div>
		<div style="float: left;margin-left: 10px;margin-top: 3px;" class="button_div_border" id="reftd"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img id="ref" onclick="datarefresh()"  class="button_img"
				src="img/sx.png" />
		</div>
		<div style="float: left;margin-left: 10px;margin-top: 3px;" class="button_div_border" id="bdtd" onclick="$('#bd').window('open');serchBds()"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img id="serchBd" src="img/bdtj.png"  class="button_img" style="width:95px;" />
			<img id="serchBd1" src="img/bdtj1.png"  class="button_img" style="width:95px;"/>
		</div>
		
		<div style="float: right;">
			<div style=" margin: 6 0 0 3; position:absolute; ">
				<input id="searchName" class="easyui-textbox"
					style="width:150px; height:25px;" data-options="prompt:'请输入终端名称'">
			</div>
			<img onclick="searchTerminal()" class="bgimage" src="img/search2.png"
				style="cursor:pointer; height:36px; width:200px;" align="middle" />
		</div>
	</div>
     <!-- 终端列表头结束-->
    <table style="width:100%;height:94%" id="cxdm"></table>  
	</div>
	<div  id="w" class="easyui-window" title="添 加 终 端" 
	      resizable="false" collapsible="false" maximizable="true" minimizable="false"  
	      data-options="modal:true,closed:true,iconCls:'icon-save'" 
	      style="width:800px;height:95%;padding:5px;">
			<div align="center" style=" width:96%; height:85%; position:absolute;">
				<table align="center" style="margin-top:50px; " >
				    <tr style="display:none">
						<td style="color:#666666; font-size:14px;">
							终端ID
						</td>
						<td align="left">
						   <input  id="terminalId" class="easyui-textbox" >
						</td>
					</tr>
					<tr class="add_terminal_table_tr">
						<td  align="right" class="add_terminal_font_style" style="">
							终&nbsp;端&nbsp;名&nbsp;称：
						</td>
						<td id="terminalNameTd"  align="left">
						  <input id="terminalName" class="easyui-textbox" data-options="prompt:'请输入终端名称'">
						</td>
					</tr>
					<tr class="add_terminal_table_tr">
						<td align="right" class="add_terminal_font_style" style="">
							终&nbsp;端&nbsp;标&nbsp;识：
						</td>
						<td align="left">
							<input  id="MAC" class="easyui-textbox" data-options="prompt:'请输入终端标识'">
						</td>
					</tr>
					<tr class="add_terminal_table_tr">
						<td  align="right" class="add_terminal_font_style" style="">
							终&nbsp;端&nbsp;IP&nbsp;地&nbsp;址：
						</td>
						<td align="left">
						<input  id="IP" class="easyui-textbox"  data-options="prompt:'请输入IP地址'">
						</td>
					</tr>
					<tr class="add_terminal_table_tr">
						<td align="right" class="add_terminal_font_style" style="">
							所&nbsp;属&nbsp;终&nbsp;端&nbsp;组：
						</td>
						<td align="left" >
						        <div style="width:100%;height:25px;border:1px solid #95B8E7;background-color:#a0c6e7; margin-top: 5px;border:1px solid #7fb7ea;-moz-border-radius: 5px; -webkit-border-radius:5px;border-radius:5px; " align="center">
						            <font style="font-size: 14px ;font-family: 微软雅黑;">只可以选择一个终端组</font>
						        </div>
						        <div style="height:350px;width:100%;padding-top :5px; border:1px solid #95B8E7;background-color:;overflow: auto;-moz-border-radius: 5px; -webkit-border-radius:5px;border-radius:5px;">
						            <ul id="terminalGroup" ></ul>
						        </div>
						</td>
					</tr>
				</table>
			    <div style="width:400px;;height:25px;position: relative;margin-top: 60px; " align="center">
					<button id="windowButton"  onclick="insertTerminal()" style="width:70px;height:100%;position: relative;float: left;cursor:pointer;margin-left:150px;font-size:14px;font-family: 微软雅黑;">
						确定
					</button>
					<button onclick="$('#w').window('close')" style="width:70px;height:100%;position: relative;float: left;margin-left: 50px;cursor:pointer;font-size:14px;font-family: 微软雅黑;">
						取消
					</button>
			    </div>
			</div>
		 <img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
		</div>
		<div id="z" class="easyui-window" title="添加终端组" resizable="false" collapsible="false" maximizable="false" minimizable="false"  data-options="modal:true,closed:true,iconCls:'icon-save'" style="width:350px;height:300px;padding:10px;">
		<div style="padding-top:50px; width:100%; height:100%;vertical-align:middle; position:absolute; ">
			<table align="center" >
			  <tr align="right" style="display:none" >
					<td style="color:#666666; font-size:14px;">
						终端ID
					</td>
					<td>
					<input  id="terminalGroupId" class="easyui-textbox" >
						
					</td>
				</tr>
				<tr align="right" >
					<td style="color:#666666; font-size:14px;">
						终&nbsp;端&nbsp;组&nbsp;名&nbsp;称：
					</td>
					<td>
					<input  id="terminalGroupName" class="easyui-textbox" data-options="prompt:'请输入终端组名称'">
					
					</td>
				</tr>
				<tr id="texts" align="right" >
					<td style="color:#666666; font-size:14px;">
						所&nbsp;属&nbsp;终&nbsp;端&nbsp;组：
					</td>
					<td>
					<input id="terminalGroupGroup" class="easyui-combobox" name="language">
					</td>
				</tr>
				<tr align="right">
					<td style="color:#666666; font-size:14px;">
						终&nbsp;端&nbsp;组&nbsp;描&nbsp;述：
					</td>
					<td>
					<textarea style="height:50px; width:130px;resize: none;" id="terminalGroupDescription"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">
						&nbsp;
					</td>
					<td align="center">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="right">
						<a id="groupWindowButton" onclick="insertTerminalGroup()" style="cursor:pointer;color:#0E4876; font-size:14px;">确定</a>
					</td>
					<td align="center">
						<a onclick="$('#z').window('close')" style="cursor:pointer;color:#0E4876; font-size:14px;">取消</a>
					</td>
				</tr>
			</table>
		</div>
		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
	</div>
	<div id="bd" class="easyui-window" title="查看被动添加终端" resizable="false" collapsible="false" maximizable="true" minimizable="false"  data-options="modal:true,closed:true,iconCls:'icon-save'" 
	             style="width:500px;height:480px;padding:5px;">
			<table style="height:100%" id="bdtj"></table>  
	</div>
	<div id="tbs" style="padding:5px;height:auto">    
	    <div>
	        <a class="easyui-linkbutton" onclick="bdtjs()">添加</a>    
	        <a class="easyui-linkbutton" onclick="deteleBdtj()">删除</a>    
	    	<a class="easyui-linkbutton" onclick="refreshBdtj()">刷新</a> 
	    </div>    
	</div>
	<div id="ShowTerminalGroup" class="easyui-panel" title="终端组列表" style="width:500px;height:150px;padding:10px;background:#fafafa;"   
	        data-options="closable:true,maximizable:true,closed:true">  
	</div>  
</body>
</html>
