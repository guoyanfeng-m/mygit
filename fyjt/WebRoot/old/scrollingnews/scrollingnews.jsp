<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
	<link rel="stylesheet" type="text/css" href="js/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/icon1.css">
	<link rel="stylesheet" type="text/css" href="js/demo.css">
	<link rel="stylesheet" type="text/css" href="js/style.css">
	<script type="text/javascript" src="scrollingnews/jquery.js"></script> 
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="scrollingnews/jquery.colorpicker.js"></script> 
	<script type="text/javascript" src="scrollingnews/check.js"></script> 
	<script type="text/javascript" src="js/check.js"></script> 
	<script type="text/javascript" src="scrollingnews/scrollingnews.js"></script>
	<script type="text/javascript" src="js/msgbox.js"></script> 
	
	<style type="text/css"> 
    .bgimage{  
        z-index:-1;
    } 
	body{
	font-family:"微软雅黑";
	}
	h1 { 
	word-wrap: break-word; 
	letter-spacing:15px; 
	text-align:center;
	} 
   .c1{float:left;position:absolute;right:0px;}
   .pagination .pagination-num {
	border-width: 1px;
	border-style: solid;
	margin: 0 2px;
	padding: 2px;
	width: 3em;
	height: auto;
	}
	.datagrid-header-rownumber, .datagrid-cell-rownumber {
	width: 50px;
	text-align: center;
	margin: 0;
	padding: 0;
	}
	.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{  
    -o-text-overflow: ellipsis;  
    text-overflow: ellipsis;  
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
.button_mouseover {
	background-color: #ffffff;
	border: solid 1px #1abc9c;
}
</style> 
<script type="text/javascript">
var page;
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
<body class="easyui-layout" onload="init()" >
	<div style="height:7%; background-color:transparent;">
		<img src="img/gundongxxtop.PNG" width="100%" />
	</div>
<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
	<div style="height:7%;position: relative;">
		<div class="button_div button_div_border" 
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="addScroll">
			<img onclick="add()" title="新建滚动消息" class="button_img"
				src="img/add.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="updateScroll">
			<img onclick="update(1)" title="编辑滚动消息" class="button_img"
				src="img/bj.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="delScroll">
			<img onclick="del()" title="删除滚动消息" class="button_img"
				src="img/sc.png" />
		</div>
				<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="publishScroll">
			<img onclick="publishWin()" title="发布滚动消息" class="button_img"
				src="img/publish.png" />
		</div>
				<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="stopScroll">
			<img onclick="stopCommand()" title="停止滚动消息" class="button_img" style="width:90px; cursor:pointer;margin: 5 5 5 5;"
				src="img/stop.png" />
		</div>
				<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="auditScroll">
			<img onclick="audit()" title="审核滚动消息" class="button_img"
				src="img/shh.png" />
		</div>
		<div style="float: left;margin-left: 10px;" class="button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img onclick="refresh()" title="刷新" class="button_img"
				src="img/sx.png" />
		</div>
		<div style="float: right;">
			<div style=" margin: 6 0 0 3; position:absolute; ">
				<input id="searchName" class="easyui-textbox"
					style="width:150px; height:25px;" data-options="prompt:'请输入消息名称'">
			</div>
			<img onclick="doSearch()" class="bgimage" src="img/search2.png"
				style="cursor:pointer; height:36px; width:200px;" align="middle" />
		</div>
	</div>
<!-- 主页数据列表 -->
	<div style="height:85%;width: 100%;float: left;">
			<table style="width:100%;height:100%" id="dg"></table>
		</div>
</div>	
	<!-- 终端树 -->
    <div id="tree" class="easyui-window"  title="发布终端" minimizable="false"  data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:540px;height:640px;padding:10px;">
     <div style="width:95%; height:540px;position:absolute;">
        <div id="tt" class="easyui-tree" data-options="animate:true" style="top:10px;padding-left:10px;width:98%;height:530px;position:absolute;overflow:auto;">
		    <ul id="sendtree" class="easyui-tree"></ul>
		</div>
        </div>
        <div id="dlg-buttons" style="position:absolute;right:15;bottom:20;"> 
          <a href="javascript:void(0)" class="easyui-linkbutton" onclick="publish()" iconCls="icon-ok">发布</a> 
          <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#tree').dialog('close')" iconCls="icon-cancel">取消</a> 
        </div>
           <img class="bgimage" src="img/u106.png" style="height:100%; width:100%;" align="middle"/>
   </div>
   
   <!-- 滚动消息发布终端页面-->
       <div id="scrollterminal" class="easyui-window" title="滚动消息终端列表" minimizable="false"  data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:800px;height:540px;padding:10px;">
         <div style="width:97%; height:100%;position:absolute;padding-top:10px;">
           <table id="terminallist" style="width:100%;height:100%;">
            </table><!--
		  <div id="dlg-buttons" style="position:absolute;right:15;bottom:55;"> 
          <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#scrollterminal').dialog('close')" iconCls="icon-cancel">取消</a> 
         </div>
        --></div>
     </div>
 <!-- 增修改页面 -->
	<div id="dlg" class="easyui-window" title="添加滚动消息" data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:840px;height:680px;padding:10px;">
		<div style="padding-top:15px;width:100%;height:100%;vertical-align:middle; position:absolute;">
		 <div id="ceshi1">
		 <table id="fm">
		   <tr style="display:none">
		      <td>消息ID：<input id="new_id" name="new_id" /></td>
		   </tr>
		   <tr>
		     <td style="color:#666666; font-size:14px;padding-left:35px">消息名称：</td>
		     <td><input id="sname" name="sname" class="easyui-validatebox" style="width:130px;height:25px"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		     <td style="color:#666666; font-size:14px;padding-left:35px">字体大小：</td>
		     <td><input id="font_size" name="font_size" style="width:130px;height:25px"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		     <td style="color:#666666; font-size:14px;padding-left:35px">滚动方位：</td>
		     <td>
	         <select name="scroll_direction" id="scroll_direction" style="width:130px;height:25px" >
		         <option value="3">left</option>  
			     <option value="4">right</option>   
			     <option value="2">down</option>   
			     <option value="1">up</option>   
		     </select>
		     </td>
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
		   <tr>
		     <td style="color:#666666; font-size:14px;padding-left:35px">字体：</td>
		     <td>
		        <select id="font" name="font"  style="width:130px;height:25px"/> 
	              <option value="0">黑体</option>  
				  <option value="1">楷体</option>  
				  <option value="2">宋体</option>  
				  <option value="3">仿宋体</option>
				  <option value="4">隶书</option>
				  <option value="5">方正姚体</option>
				  <option value="6">楷书</option>
				  <option value="7">幼圆</option>
				  <option value="8">新宋体</option>
               </select>
		     </td>
		     <td style="color:#666666; font-size:14px;padding-left:35px">滚动速度：</td>
		     <td><input id="scroll_speed" name="scroll_speed" style="width:130px;height:25px"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		     <td style="color:#666666;font-size:14px;padding-left:35px">开始时间：</td>
		     <td><input id="start_time" name="start_time" class="easyui-datetimebox" style="width:150px;height:25px;" editable="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
		   <tr>
		     <td style="color:#666666;font-size:14px;padding-left:35px">结束时间：</td>
		     <td><input id="end_time" name="end_time" class="easyui-datetimebox"  style="width:150px;height:25px;" editable="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		     <td style="color:#666666;font-size:14px;padding-left:35px">字体颜色：</td>
		     <td>
		       <div style="width:200px;height:30px;position:absolute;right:249px;top:90">
	              <input type="hidden" id="ceshi111" /> 
				 <input type="text" id="font_color" name="font_color" disabled="disabled" style="width:150px;height:25px"/><img src="scrollingnews/colorpicker.png" id="cp3" style="cursor:pointer"/>
	          </div>
		     </td>
		   </tr>
		   <tr>
		   <td style="color:#666666;font-size:14px;padding-left:35px">消息内容：</td>
		     <td><textarea id="text" name="text"  cols="90" rows="12" style="position:absolute;resize:none;"></textarea></td>
		   </tr>
		   </table></div>
		    <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
		    <div id="yulan" class="easyui-panel" title="消息预览" style="width:818px;height:240px;position:absolute;"></div>
		    <div id="dlg-buttons" align="right" style="padding-top:230px;padding-left:700px;position:absolute;"> 
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()" iconCls="icon-ok">保存</a> 
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')" iconCls="icon-cancel">取消</a> 
    </div>
   </div> 
   <img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
    </div>
</body>
</html>