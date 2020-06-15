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
	<script type="text/javascript" src="js/jquery.js"></script> 
	<script type="text/javascript" src="js/ajaxfileupload.js"></script> 
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/check.js"></script> 
	<script type="text/javascript" src="admin/carousel/carousel.js"></script>
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
		<img src="img/lbtpTop.png" width="100%" />
	</div>
<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
	<div style="height:7%;position: relative;">
		<div class="button_div button_div_border" 
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="addCarousel">
			<img onclick="add()" title="新增轮播图" class="button_img"
				src="img/add.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="updateCarousel">
			<img onclick="update(1)" title="编辑轮播图" class="button_img"
				src="img/bj.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="delCarousel">
			<img onclick="del()" title="删除轮播图" class="button_img"
				src="img/sc.png" />
		</div>
		<div style="float: left;margin-left: 10px;" class="button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img onclick="refresh()" title="刷新" class="button_img"
				src="img/sx.png" />
		</div>
	</div>
</div>	
<!-- 主页数据列表 -->
<div style="height:85%;width: 100%;float: left;">
	<table style="width:100%;height:100%" id="dg"></table>
</div>
   
   <!-- 查看轮播图-->
   	<div id="showUrl" class="easyui-window" title="查看轮播图" data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:450px;height:300px;padding:10px;">
        <img id="preview-img" style="width:95%; height:75%;position:absolute;padding-top:5px;" align="center" >
    </div>
 	<!-- 增修改页面 -->
	<div id="dlg" class="easyui-window" title="添加滚动消息" data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:450px;height:400px;padding:10px;">
		<div style="padding-top:15px;width:100%;height:100%;vertical-align:middle; position:absolute;">
		 <table id="fm" >
		   <tr>
		     <td style="color:#666666; font-size:14px;padding-left:35px">图片排序：</td>
		     <td><input id="isort" name="isort" class="easyui-validatebox" style="width:130px;height:25px"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
		   <tr>
		   	 <td style="color:#666666; font-size:14px;padding-left:35px">图片路径：</td>
		     <td>
		     <input id="imgurl" name="imgurl" type="file" accept="image/*"  style="width:200px;height:25px" />
		     <input id="carousel_id" name="carousel_id" style="display:none"/>
		     <input id="imgurl_old" name="imgurl_old"  style="display:none"/>
		     &nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" id="sub">上传</button>
		     </td>
		   
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
		   <tr>
		   	 <td style="color:#666666; font-size:14px;padding-left:35px">图片预览：</td>
		     <td><div style="margin:10px 10px -20px 10px;">
			    <img id="show-img"  width="200" height="200" style="border:0.5px solid #ccc;">
		    </div></td>
		   
		   </tr><tr></tr><tr></tr><tr></tr><tr></tr>
		   </table>
		    <div id="dlg-buttons" align="right" style="padding-top:20px;padding-left:300px;position:absolute;"> 
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()" iconCls="icon-ok">保存</a> 
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')" iconCls="icon-cancel">取消</a> 
    </div>
   </div> 
   		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
    </div>
</body>
</html>