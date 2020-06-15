<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="beans.user.UserBean" %>
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
	<link rel="stylesheet" type="text/css" href="js/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/icon1.css">
	<link rel="stylesheet" type="text/css" href="js/demo.css">
	<link rel="stylesheet" type="text/css" href="js/style.css">
	<script type="text/javascript" src="js/jquery.js"></script> 
	<script type="text/javascript" src="js/ajaxfileupload.js"></script> 
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/check.js"></script> 
	<script type="text/javascript" src="js/msgbox.js"></script> 
	 <!--引入引入kindeditor编辑器相关文件-->
    <link rel="stylesheet" href="js/kindeditor/themes/default/default.css"/>
    <script charset="utf-8" src="js/kindeditor/kindeditor-all.js"></script>
	<script type="text/javascript" src="admin/notice/notice.js"></script>

	<style type="text/css">
		.button_div {
			float: left;
			margin-left: 10px;
			display: none;
		}
		.knowsky{
		display:inline-block;
		vertical-align:middle;
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
</head>

<body class="easyui-layout" onload="init()" >
	<div style="height:7%; background-color:transparent;">
		<img src="img/elementtop.png" width="100%" />
	</div>
	<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
		<div style="height:7%;position: relative;">
			<div class="button_div button_div_border" 
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="addNotice">
				<img onclick="add()" title="添加公告" class="button_img"
					src="img/add.png" />
			</div>
			<div class="button_div button_div_border"
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="updateNotice">
				<img onclick="update()" title="编辑公告" class="button_img"
					src="img/bj.png" />
			</div>
			<div class="button_div button_div_border"
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="delNotice">
				<img onclick="del()" title="删除公告" class="button_img"
					src="img/sc.png" />
			</div>
			<div class="button_div button_div_border"
				onmouseover="buttonover(event)" onmouseout="buttonout(event)"
				id="editNotice">
				<img onclick="editNotice()" title="审核公告" class="button_img"
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
						style="width:150px; height:25px;" data-options="prompt:'请输入公告标题'">
				</div>
				<img onclick="searchName()" class="bgimage" src="img/search2.png"
					style="cursor:pointer; height:36px; width:200px;" align="middle" />
			</div>
		</div>
	</div>
	
	<!-- 主页数据列表 -->
	<div id="noticelistDiv" style="height:85%;width: 100%;float: left;">
		<table style="width:100%;height:100%" id="noticelist"></table>
	</div>	
	
	<!-- 增修改页面 -->
	<div id="dlg" class="easyui-window" title="添加滚动消息" 
		data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" 
		style="width:1100px;height:700px;padding:10px;">
		<div style="padding-top:15px;width:100%;height:100%;vertical-align:middle; position:absolute;">
	 	<table id="fm" >
			<tr>
		     <td style="color:#666666; font-size:18px;padding-left:35px">标题：</td>
		     <td>
		     <input id="title" name="title" class="easyui-validatebox" style="width:550px;height:36px"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		     <input id="id" name="id" style="display:none"/>
		     </td>
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
		   <tr>
		     <td style="color:#666666; font-size:16px;padding-left:35px">详细内容：</td>
		     <td>
		     	<textarea id="description" style="width:900px;height:500px;visibility:hidden;"></textarea>
		     </td>
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
	   	</table>
	    <div id="dlg-buttons" align="right" style="padding-top:20px;padding-left:300px;position:absolute;"> 
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()" iconCls="icon-ok">保存</a> 
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close');KindEditor.instances[0].html('');" iconCls="icon-cancel">取消</a> 
    	</div>
  		</div> 
  		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
   </div>
    
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
		<%UserBean u = (UserBean) request.getSession().getAttribute("user");%>
		var currentuserId = <%=u.getUser_id()%>;
	</script>	
</body>
</html>
