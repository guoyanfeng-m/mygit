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

<title>素材管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="js/easyui.css">
<link rel="stylesheet" type="text/css" href="js/icon1.css">
<link rel="stylesheet" type="text/css" href="js/demo.css">
<link rel="stylesheet" type="text/css" href="js/style.css">
<link rel="stylesheet" href="js/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="element/css/strip/strip.css" />
<script type="text/javascript" src="js/check.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="element/js/strip/strip.js"></script>
<script type="text/javascript" src="js/msgbox.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script> 
<script type="text/javascript" src="element/js/jquery.colorpicker.js"></script>
		<script type="text/javascript">
		var viewModel = <%=request.getParameter("viewModel")%>

		if(viewModel==2||viewModel=="2"){
				document.write(" <script  type=\"text/javascript\"  src='element/js/elementList.js' > <\/script>");
			}else{
				viewModel=1;
			document.write(" <script  type=\"text/javascript\"  src='element/js/element.js' > <\/script>");
			}
		</script>

<style type="text/css">
.panel-title {
	font-size: 13px;
	font-weight: bold;
	color: black;
	height: 16px;
	line-height: 16px;
}

#selectable .ui-selecting {
	background: #D4EBFF;
}

#selectable .ui-selected {
	background: #FECA40;
	color: white;
}

#selectable {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 100%;
}

#selectable li {
	margin-left: 2.3%;
	margin-top: 2%;
	float: left;
	width: 182px;
	height: 205px;
	cursor: pointer;
	font-size: 1em;
	text-align: center;
}

#selectable li img {
	padding: 4%;
	margin: 1%;
}
#selectable div {
	position:relative;
	margin-top: 10px;
}

#classify li {
	margin-left: 1%;
	margin-top: 1%;
	float: left;
	cursor: pointer;
	width: 120px;
	height: 110px;
	font-size: 1em;
	text-align: center;
}

#classify {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 100%;
}

#classify li img {
	padding: 6%;
	margin: 1%;
	height: 65%;
	width: 65%;
}

#classify .ui-selecting {
	background: #FECA40;
}

#classify .ui-selected {
	background: #4C9EE6;
	color: white;
}
</style>
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
<script type="text/javascript">
<%UserBean u = (UserBean) request.getSession().getAttribute("user");%>
	var currentuserId = <%=u.getUser_id()%>;
<%--<% request.getSession().setAttribute("viewModel","1"); %>--%>
/* var tid = '${sessionScope.user.user_id}'; */
<%--<% Object viewModel2 = request.getSession().getAttribute("viewModel"); %>--%>
<%--var viewModel2 = <%=viewModel2%>;--%>
<%----%>


function changeModel(rec){
	var jumppath = "element/element.jsp";
	var name = navigator.appName;
	if ("ActiveXObject" in window){
		jumppath = "element.jsp";
	}
	
	if(rec.value=='1'|| rec.value==1){
		jumppath+="?viewModel=1"
		}else{
			jumppath+="?viewModel=2"
			}
	window.location.href = jumppath;
}
	function buttonover(event) {
		event = event ? event : window.event;
		var temp = event.currentTarget;
		$(temp).removeClass("button_div_border");
		$(temp).addClass("button_mouseover");
	}
	var strCookie = document.cookie;
	function buttonout(event) {
		event = event ? event : window.event;
		var temp = event.currentTarget;
		$(temp).removeClass("button_mouseover");
		$(temp).addClass("button_div_border");
	}
</script>
<script type="text/javascript">

</script>

</head>

<body class="easyui-layout" onload="initLoad()" bgcolor="#D4EBFF">
<div style="height:7%; background-color:transparent;">
		<img src="img/elementtop.png" width="100%" />
	</div>
	<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
	<div style="height:7%;position: relative;">
		<div class="button_div button_div_border" 
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="eleadd">
			<img onclick="addElemt()" title="添加素材" class="button_img"
				src="img/add.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="eleedit">
			<img onclick="showMessage()" title="编辑素材" class="button_img"
				src="img/bj.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="eledele">
			<img onclick="deleteElem()" title="删除素材" class="button_img"
				src="img/sc.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="eleaudit">
			<img onclick="auditE()" title="审核素材" class="button_img"
				src="img/shh.png" />
		</div>
		<div id="previewDiv" class="button_div button_div_border" style="display: none;"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img onclick="preView()" title="预览素材" class="button_img"
				src="img/preview.png" />
		</div>
		<script>
		if(viewModel=="1"){
			document.getElementById("previewDiv").style.display="block";
			}
		</script>		
		<div style="float: left;margin-left: 10px;" class="button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img onclick="reFresh()" title="刷新" class="button_img"
				src="img/sx.png" />
		</div>
		<div style="float: right;">
			<div style=" margin: 6 0 0 3; position:absolute; ">
				<input id="searchName" class="easyui-textbox"
					style="width:150px; height:25px;" data-options="prompt:'请输入素材名称'">
			</div>
			<img onclick="searchName()" class="bgimage" src="img/search2.png"
				style="cursor:pointer; height:36px; width:200px;" align="middle" />
		</div>
	</div>
</div>
		<div style="position: absolute;width:95px;height:20px;margin-top: 3%; margin-left: 3%;">
			 <textarea id="compare" style="height:0px;width:0px;overflow:scroll;white-space:nowrap;resize:none"></textarea>
			<select id="cc" class="easyui-combobox" name="showmodel" style="width:95px;" panelHeight="50"data-options="value:viewModel, onSelect: function(rec){    
            changeModel(rec);
       		 }"    ">   
			    <option value="1">缩略图显示</option>   
			    <option value="2">列表显示</option>   
			</select> 
		</div>
		<div style="position: absolute; margin-left: 3%; margin-top: 5%;">
			<table>
				<tr>
					<td><img class="imgs" onclick="newClassify()" id="newFile"
						width="35px" height="25px" src="img/addclassify.png"  title="新建分类" />
						<img class="imgs" onclick="deleteClassify()" id="delElement" width="35px"
						height="25px" src="img/delsta.png"  title="删除分类" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_picture" title="图片"
						onClick="queryBytype('3')" src="img/e_picture.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_movie" title="视频"
						onClick="queryBytype('4')" src="img/e_movie.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_vedio" title="音频"
						onClick="queryBytype('2')" src="img/e_vedio.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_Flash" title="FLASH"
						onClick="queryBytype('6')" src="img/e_Flash.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_office" title="OFFICE"
						onClick="queryBytype('7')" src="img/e_office.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_html" title="网页"
						onClick="queryBytype('5')" src="img/e_html.png"
						style="cursor: pointer; width: 70%;margin-left:10%;display: inline;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_text" title="文本"
						onClick="queryBytype('1')" src="img/e_text.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>
				<tr>
					<td><img class="imgs" id="e_stream" title="流媒体"
						onClick="queryBytype('8')" src="img/e_stream.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>		
				<tr>
					<td><img class="imgs" id="e_other" title="其它"
						onClick="queryBytype('9')" src="img/e_other.png"
						style="cursor: pointer; width: 70%;margin-left:10%;" /></td>
				</tr>			
			</table>
		</div>

		<div
			style="border: 5px solid #dedede; -moz-border-radius: 15px; -webkit-border-radius: 15px; border-radius: 15px; overflow: auto; position: absolute; margin-top: 2%; margin-left: 12%; width: 86%; height: 75%">
			<div class="easyui-accordion"
				data-options="collapsed:false,onSelect:function(){showClassifyDiv()}, "
				style="width:98%;">
				<div title="素材分类" id="classifyDiv"
					style="width:90%; overflow: auto; position: relative; margin-top:0.1%; margin-left:0.3%;height: 150px;">
					<ol id="classify">
					</ol>
				</div>
			</div>
			<ol id="selectable">
			</ol>
<!-- 主页数据列表 -->
		<div id="elementlistDiv" style="display:none;height:89%;width: 100%;float: left;">
			<table style="width:100%;height:100%" id="elementlist"></table>
		</div>			
		<script>
		if(viewModel=="2"){
			document.getElementById("elementlistDiv").style.display="block";
			}
		</script>
		</div>
		<img src="img/x_u26.png" width="100%" height="84%"
			style="margin-top: 10px;"></img>
		<div id="panDiv" style="display:none; position: absolute; width: 100%; height: 10%;top:95%;">
			<div class="easyui-pagination" id="pagInationT" style="width: 100%;"
				data-options="showPageList:false,beforePageText: '第',
        afterPageText: '页    共 {pages} 页', 
        showRefresh:false,
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', "></div>
		</div>
		<script>
		if(viewModel=="1"){
			document.getElementById("panDiv").style.display="block";
			}
		</script>
		
		<p id="priview"></p>
		<div id="menu" class="easyui-menu" style="width: 30px; display: none;">
	      			<div onclick="showMessage()" data-options="iconCls:'icon-sxedit'"
				id="regionMenu_edit">编辑</div>
			<div class="menu-sep"></div>
			<div onclick="auditE()" data-options="iconCls:'icon-sxaudit'"
				id="regionMenu_audit">审核</div>
			<div class="menu-sep"></div>
			<div onclick="deleteElem()" data-options="iconCls:'icon-delete'"
				id="regionMenu_delete" >删除</div>
			<div class="menu-sep"></div>
			<div onclick="sendTo()" data-options="iconCls:'icon-sxclassify'"
				id="regionMenu_classify">发送到</div>
			<div id="regionMenu_moveclassify_1" class="menu-sep"
				style="display: none"></div>
			<div onclick="moveFromClassify()"
				data-options="iconCls:'icon-moveclassify'"
				id="regionMenu_moveclassify" style="display: none">从分类中移除</div>
 		</div>
		<!-- ol便签右键事件menu -->
		<div id=regionMenu class="easyui-menu" style="width:120px;">
			<div  onclick="showMessage()" data-options="iconCls:'icon-sxedit'"
				 id="regionMenu_edit1" style="display: none;">编辑</div>
			<div id="regionMenu_edit11" class="menu-sep" style="display: none;" ></div>
			<div onclick="auditE()" data-options="iconCls:'icon-sxaudit'"
				id="regionMenu_audit1"  style="display: none;">审核</div>
			<div id="regionMenu_audit11" class="menu-sep" style="display: none;"></div>
			<div onclick="deleteElem()" data-options="iconCls:'icon-delete'"
				id="regionMenu_delete1"  style="display: none;">删除</div>
			<div id="regionMenu_delete11" class="menu-sep" style="display: none;"></div>
			<div onclick="sendTo()" data-options="iconCls:'icon-sxclassify'"
				id="regionMenu_classify1">发送到</div>
			<div id="regionMenu_moveclassify_11" class="menu-sep" style="display: none"></div>
			<div onclick="moveFromClassify()"
				data-options="iconCls:'icon-moveclassify'"
				id="regionMenu_moveclassify1" style="display: none">从分类中移除</div>
			<div class="menu-sep"></div>
			<div onclick="elementProperty()" data-options="iconCls:'icon-copy'"
				id="regionMenu_property1">属性</div>
		</div>
		<!-- end -->
		<!-- 素材分类文件夹右键事件menu -->
		<div id=cregionMenu class="easyui-menu" style="width:120px;">

			<div onclick="deleteClassify()" data-options="iconCls:'icon-delete'"
				id="cregionMenu_delete">删除</div>
			<div class="menu-sep"></div>
			<div onclick="reNameClassify()" data-options="iconCls:'icon-copy'"
				id="cregionMenu_classify">属性更改</div>
 
		</div>
		<!-- end -->
		<!-- new素材添加 -->
		<div class="easyui-window" id="zj" title="添加素材" resizable="false"
			minimizable="false"
			data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false, modal:true,closed:true"
			style="width: 260px; height: 310px; padding: 5px; background: #fafafa;">

			<div region="center" border="false" border="false">
				<div>
					<a id="add"
						style="height: 40px; width: 150px;margin-left: 50px; margin-top: 20px;"
						class="easyui-linkbutton" data-options="iconCls:'icon-add'"
						onclick="addElem()">添加多媒体文件</a> <br /> <a id="adds"
						style="height: 40px; width: 150px;margin-left: 50px; margin-top: 20px;"
						class="easyui-linkbutton" data-options="iconCls:'icon-add'"
						onclick="addText()">添加文本文件</a><br><a id="add_web"
						style="height: 40px; width: 150px;margin-left: 50px; margin-top: 20px;"
						class="easyui-linkbutton" data-options="iconCls:'icon-add'"
						onclick="addWeb()">添加网页</a>
						<br><a id="add_stream"
						style="height: 40px; width: 150px;margin-left: 50px; margin-top: 20px;"
						class="easyui-linkbutton" data-options="iconCls:'icon-add'"
						onclick="addStream()">添加流媒体</a>
				</div>
			</div>

		</div>

		<!--  -->
		<!-- 多媒体文件素材增加窗口 -->
		<div class="easyui-window" id="z" title="添加素材" resizable="false"
			minimizable="false"
			data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false, modal:true,closed:true"
			style="width: 650px; height: 450px; padding: 5px; background: #fafafa;">
			<div class="easyui-layout" fit="true">

				<div region="center" border="false" border="false">
					<div class="easyui-tabs" fit="true">
						<div id="addCode" title="媒体文件上传" style="padding: 10px;"></div>
						<%--							<div title="网页上传">--%>
						<%--								<table style="width: 100%; height: 250px" border="0">--%>
						<%--									 <tr align="left">--%>
						<%--										<td style="color: #666666; font-size: 14px;">--%>
						<%--											网&nbsp;&nbsp;页&nbsp;&nbsp;名&nbsp;&nbsp;称：--%>
						<%--										</td>--%>
						<%--										<td>--%>
						<%--											<input style="width: 90%" id="htmlName"--%>
						<%--												class="easyui-textbox" value="">--%>
						<%--										</td>--%>
						<%--									</tr>--%>
						<%--									<tr align="left">--%>
						<%--										<td style="color: #666666; font-size: 14px;">--%>
						<%--											网&nbsp;&nbsp;页&nbsp;&nbsp;地&nbsp;&nbsp;址：--%>
						<%--										</td>--%>
						<%--										<td>--%>
						<%--											<input style="width: 90%;height:40%" id="htmladdress"--%>
						<%--												class="easyui-textbox" value="">--%>
						<%--										</td>--%>
						<%--									</tr>--%>
						<%--															--%>
						<%--									<tr align="center">--%>
						<%--										<td>--%>
						<%----%>
						<%--										</td>--%>
						<%--										<td>--%>
						<%--											<input style="width: 70px;height:30px;" type="button" onclick="reset()" value="重置">--%>
						<%--											<input style="width: 70px;height: 30px;" type="button" onclick="submintHtml()" value="提交">--%>
						<%--										</td>--%>
						<%--									</tr>									--%>
						<%--								</table>--%>
						<%--							</div>--%>

					</div>
				</div>
				<div region="south" border="false"
					style="text-align: right; height: 30px; line-height: 25px;">
					<!--	<a class="easyui-linkbutton" icon="icon-ok"
							href="javascript:void(0)">Ok</a>-->
					<a class="easyui-linkbutton" icon="icon-cancel"
						onclick="$('#z').window('close');" href="javascript:void(0)">关闭</a>
				</div>
			</div>
		</div>
		<!-- end -->
		<!-- 文本文件素材增加窗口 -->
		<div class="easyui-window" id="wb" title="添加素材" resizable="false"
			minimizable="false"
			data-options="resizable:false,draggable:false,collapsible:false,closable:true,minimizable:false,maximizable:false, modal:true,closed:true"
			style="width: 1130px; height: 655px; padding: 5px; background: #fafafa;">
			<div style="width:100%;height:60px;">
					<table style="width: 100%; height: 50%" border="0">
						<tr align="left" style="width: 100%;height: 20px;">
							<td style="color: #666666; font-size: 14px; width: 40px;">
								标题:</td>
							<td style="width: 100px;"><input id="textId"
								style="width: 100px;display: none;"> <input
								id="textName" class="easyui-textbox" style="width: 100px;">
							</td>
							<td style="color: #666666; font-size: 14px; width: 60px;padding-left:5px;">
								字体大小:</td>
							<td style="width:60px;"><input id="textSize"
								class="easyui-numberspinner"
								data-options="missingMessage:'此为必填项!',min:12,max:300,required:true,value:12,onChange:function(){changeSize()}"
								style="width:60px;"></td>
							<td style="width: 85px;padding-left:5px;"><select id="textFont"
								name="textFont" editable="editable" style="width:85px;">
									
							</select></td>
							<td style="color: #666666; font-size: 14px; width: 60px;padding-left:5px;">滚动方向:</td>
							<td style="width: 85px;"><input id="direction" class="easyui-combobox"
										name="direction" editable="editable" style="width:85px;">
					         </td>
							<td style="color: #666666; font-size: 14px; width: 60px;padding-left:5px;">滚动速度:</td>
							<td style="width:60px;"><input id="speed"
								class="easyui-numberspinner"
								data-options="missingMessage:'此为必填项!',min:1,max:100,required:true,value:12,onChange:function(){changeSize()}"
								style="width:60px;"></td>
							<td style="color: #666666; font-size: 14px; width: 50px;padding-left:10px;">
							分辨率:</td>
							<td style="width: 125px;">
							<input id="resolution" class="easyui-textbox" style="padding-right:5px;font-size:14px;width:50px;"/>
							<h style="font-size:14px;padding-right:5px; display : inline;">*</h><input id="resolution1" class="easyui-textbox" style="font-size:14px;width:50px;">
							</td>
							<td style="color: #666666; font-size: 14px; width: 90px;padding-left:5px;">
							终端居中设置:</td>
							<td style="width: 100px;padding-left:5px;" id = "centerUp">
								上下<input type="checkbox" onclick="Obox()" name="cen" value="01" />
								左右<input type="checkbox" onclick="Obox()" name="cen" value="02" />
							</td>
						</tr>
					</table>
					<table style="width: 30%; height: 50%"  align="right" border="0">
							<tr align="right" style="width: 100%;height: 20px;">
								<td style="color: #666666; font-size: 14px; width: 50px;padding-left:5px;">
									字体颜色:</td>
								<td style="width:15px;"><input id="textcolor" style="display: none"> 
								<img
									src="scrollingnews/colorpicker.png" id="cp3"
									style="cursor:pointer" /></td>
								<td style="color: #666666; font-size: 14px; width: 50px;padding-left:5px;">
									背景颜色:</td>
								<td style="width:15px;"><input id="textareacolor" style="display: none;">
									<img src="scrollingnews/colorpicker.png" id="cp4"
									style="cursor:pointer" /></td>
								<td style="color: #666666; font-size: 14px; width: 50px;padding-left:5px;">
									背景透明:</td>
								<td style="width:15px;"><input type="checkbox" id="bjtm" value="#00000000" onclick="setbacktran()"/></td>
							</tr>
					</table>
			 </div>
			 <div style="width:100%;margin-top:10px;height:80%;position: relative;">
				    <div id="textarea"  style="background-color: #EBEBE4;resize:none;width: 100%;height: 100%;font-size:12px; font-family: 黑体;overflow-y:auto;position: absolute;">
				    	<span></span>
				    </div>
			 </div>
			 <div style="width:100%;height:8%;margin-top:10px;">
			 <span id="msg" color=""></span>
			 <table align="right">
			 <tr>
			     <td><input style="width: 50px;height:30px;" type="button" onclick="resetText()" value="重置"></td>
				<td></td>
				<td><input style="width: 50px;height: 30px;" type="button"
					onclick="submintText()" value="提交"></td>
				<td><input style="width: 50px;height: 30px;" type="button"
				onclick="$('#wb').window('close');times++;testTimes();resetText();" href="javascript:void(0)" value="关闭"></td>
			</table>
			</tr>
			 </div>
		</div>
		<!-- end -->
      <div class="easyui-window" id="web" title="添加素材" resizable="false"
			minimizable="false"
			data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false, modal:true,closed:true"
			style="width: 650px; height: 450px; padding: 5px; background: #fafafa;">
			<div class="easyui-layout" fit="true">

				<div region="center" border="false" border="false">
					<div class="easyui-tabs" fit="true">

													<div title="网页上传">
														<table style="width: 100%; height: 250px" border="0">
															 <tr align="left">
																<td style="color: #666666; font-size: 14px;">
																	网&nbsp;&nbsp;页&nbsp;&nbsp;名&nbsp;&nbsp;称：
																</td>
																<td>
																	<input style="width: 90%" id="htmlName"
																		class="easyui-textbox" value="">
																</td>
															</tr>
															<tr align="left">
																<td style="color: #666666; font-size: 14px;">
																	网&nbsp;&nbsp;页&nbsp;&nbsp;地&nbsp;&nbsp;址：
																</td>
																<td>
																	<input style="width: 90%;height:40%" id="htmladdress"
																		class="easyui-textbox" value=""><br>
																		<p style="color: red;">*网页地址格式:http://www.baidu.com</p>
																</td>
															</tr>
																					
															<tr align="center">
																<td>
						
																</td>
																<td>
																	<input style="width: 70px;height:30px;" type="button" onclick="reset()" value="重置">
																	<input style="width: 70px;height: 30px;" type="button" onclick="submintHtml()" value="提交">
																</td>
															</tr>									
														</table>
													</div>
					</div>
				</div>
			</div>
		</div>
				<!-- 流媒体 -->
<div class="easyui-window" id="stream" title="添加素材" resizable="false"
			minimizable="false"
			data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false, modal:true,closed:true"
			style="width: 650px; height: 450px; padding: 5px; background: #fafafa;">
			<div class="easyui-layout" fit="true">

				<div region="center" border="false" border="false">
					<div class="easyui-tabs" fit="true">

													<div title="流媒体上传">
														<table style="width: 100%; height: 250px" border="0">
															 <tr align="left">
																<td style="color: #666666; font-size: 14px;">
																	流&nbsp;&nbsp;媒&nbsp;&nbsp;体&nbsp;&nbsp;名&nbsp;&nbsp;称：
																</td>
																<td>
																	<input style="width: 90%" id="streamName"
																		class="easyui-textbox" value="">
																</td>
															</tr>
															<tr align="left">
																<td style="color: #666666; font-size: 14px;">
																	流&nbsp;&nbsp;媒&nbsp;&nbsp;体&nbsp;&nbsp;地&nbsp;&nbsp;址：
																</td>
																<td>
																	<input style="width: 90%;height:40%" id="streamaddress"
																		class="easyui-textbox" value="">
																</td>
															</tr>
																					
															<tr align="center">
																<td>
						
																</td>
																<td>
																	<input style="width: 70px;height:30px;" type="button" onclick="resetStream()" value="重置">
																	<input style="width: 70px;height: 30px;" type="button" onclick="submintStream()" value="提交">
																</td>
															</tr>									
														</table>
													</div>
					</div>
				</div>
		<!-- 编辑素材窗口 -->
		<div id="b" class="easyui-window" title="素材编辑" minimizable="false"
			data-options="modal:true,closed:true,resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,"
			style="width: 400px; height: 350px; padding: 10px;">
			<div
				style="padding-top: 20px; padding-left: 20px; width: 100%; height: 100%; vertical-align: middle; position: absolute;">
				<table style="width: 350px; height: 250px" border="0">
					<tr align="right" style="display: none">
						<td style="color: #666666; font-size: 14px;">素材ID</td>
						<td><input id="elementId" class="easyui-textbox"></td>
					</tr>
					<tr align="right" style="display: none">
						<td style="color: #666666; font-size: 14px;">素材扩展名</td>
						<td><input id="elementprifix" class="easyui-textbox">
						</td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;名&nbsp;&nbsp;称：</td>
						<td><input style="width: 90%" id="elementName"
							class="easyui-textbox" value=""></td>
					</tr>
					
					<tr align="left"  id="elementAddressTR"   >
						<td style="color: #666666; font-size: 14px;">
							网&nbsp;&nbsp;页&nbsp;&nbsp;地&nbsp;&nbsp;址：</td>
					<td><input style="width: 90%" id="elementAddress"
							class="easyui-textbox" value=""></td>
					</tr>	
					
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;描&nbsp;&nbsp;述：</td>
						<td><textarea id="description" cols="25" rows="10"
								style="resize:none;"></textarea></td>
					</tr>

					<tr>
						<td align="right">&nbsp;</td>
						<td align="center">&nbsp;</td>
					</tr>
					<tr>
						<td align="right"><a id="windowButton"
							onclick="editElement()"
							style="cursor: pointer; color: #0E4876; font-size: 14px;">确定</a>
						</td>
						<td align="center"><a onclick="$('#b').window('close')"
							style="cursor: pointer; color: #0E4876; font-size: 14px;">取消</a>
						</td>
					</tr>
				</table>
			</div>
			<img class="bgimage" src="img/u106.png"
				style="height: 100%; width: 100%;" align="middle" />
		</div>
		<!--end  -->
		<!-- 素材审核窗口 -->
		<div id="s" class="easyui-window" title="素材审核" minimizable="false"
			data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,"
			style="width: 400px; height: 350px; padding: 10px;">
			<div
				style="padding-top: 20px; padding-left: 20px; width: 100%; height: 100%; vertical-align: middle; position: absolute;">
				<table style="width: 350px; height: 250px" border="0">
					<tr align="right" style="display: none">
						<td style="color: #666666; font-size: 14px;">素材ID</td>
						<td><input id="auditelementId" class="easyui-textbox">
						</td>
					</tr>
					<tr>
						<td align="right"><a id="windowButton"
							onclick="auditElement()"
							style="cursor: pointer; color: #0E4876; font-size: 14px;">确定</a>
						</td>
						<td align="center"><a onclick="$('#s').window('close')"
							style="cursor: pointer; color: #0E4876; font-size: 14px;">取消</a>
						</td>
					</tr>
				</table>
			</div>
			<img class="bgimage" src="img/u106.png"
				style="height: 100%; width: 100%;" align="middle" />
		</div>
		<!-- end -->
		<!-- 素材属性窗口 -->
		<div id="sx" class="easyui-window" title="素材属性" minimizable="false"
			data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,"
			style="width: 400px; height: 400px; padding: 10px;">
			<div
				style="padding-top: 20px; padding-left: 20px; width: 100%; height: 100%; vertical-align: middle; position: absolute;">
				<table style="width: 350px; height: 350px" border="0">
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;名&nbsp;&nbsp;称：</td>
						<td><input style="width: 90%" id="sxelemName"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							&nbsp;&nbsp;创&nbsp;&nbsp;建&nbsp;&nbsp;人：</td>
						<td><input style="width: 90%" id="sxcreater"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;大&nbsp;&nbsp;小：</td>
						<td><input style="width: 90%" id="sxelemSize"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;类&nbsp;&nbsp;型：</td>
						<td><input style="width: 90%" id="sxelemType"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;分&nbsp;&nbsp;辨&nbsp;&nbsp;率：</td>
						<td><input style="width: 90%" id="sxelemResolution"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;MD5&nbsp;&nbsp;值：</td>
						<td><input style="width: 90%" id="sxelemMD5"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							上&nbsp;&nbsp;传&nbsp;&nbsp;时&nbsp;&nbsp;间：</td>
						<td><input style="width: 90%" id="sxelemUploadtime"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">最后修改时间：</td>
						<td><input style="width: 90%" id="sxelemEdittime"
							class="easyui-textbox" value="" disabled="disabled"
							readonly="readonly"></td>
					</tr>
					<tr align="left">
						<td style="color: #666666; font-size: 14px;">
							素&nbsp;&nbsp;材&nbsp;&nbsp;描&nbsp;&nbsp;述：</td>
						<td><textarea style="width: 90%;resize:none;"
								id="sxelemDescription" cols="30" rows="6" disabled="disabled"
								readonly="readonly"></textarea></td>
					</tr>

					<tr>
						<td align="right">&nbsp;</td>
						<td align="center">&nbsp;</td>
					</tr>
				</table>
			</div>
			<img class="bgimage" src="img/u106.png"
				style="height: 100%; width: 100%;" align="middle" />
		</div>
		<!--end  -->
		<!--新建分类窗口  -->
		<div id="fl" class="easyui-window" title="素材分类" minimizable="false"
			data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,"
			style="width: 320px; height: 150px; padding: 10px;">
			<table>
				<tr align="left">
					<td style="color: #666666; font-size: 14px;">
						分&nbsp;&nbsp;类&nbsp;&nbsp;名&nbsp;&nbsp;称：</td>
					<td><input style="" id="flelemName" class="easyui-textbox"
						value=""></td>
				</tr>
					<tr align="left">
					<td style="color: #666666; font-size: 14px;">
						文&nbsp;&nbsp;件&nbsp;&nbsp;夹&nbsp;&nbsp;属&nbsp;&nbsp;性：</td>
					<td>
						<select  style="width: 150px;" id="folderproperty" class="easyui-combobox">
							    <option value="0" selected="selected">私有</option>   
    							<option value="1">共享</option> 
    					</select>
						</td>
				</tr>				
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr>
					<td></td>
					<td align="right"><a id="flsureButton"
						onclick="subClassifyName()"
						style="cursor: pointer; color: #0E4876; font-size: 14px;">确定</a></td>
				</tr>
			</table>
		</div>
		<!-- end -->
		<!--重命名分类窗口  -->
		<div id="cmm" class="easyui-window" title="分类属性" minimizable="false"
			data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,"
			style="width: 320px; height: 150px; padding: 10px;">
			<table>
				<tr align="left">
					<td style="color: #666666; font-size: 14px;">
						分&nbsp;&nbsp;类&nbsp;&nbsp;名&nbsp;&nbsp;称：</td>
					<td><input style="" id="cmmflelemName" class="easyui-textbox"
						value=""></td>
				</tr>
				<tr align="left">
					<td style="color: #666666; font-size: 14px;">
						文&nbsp;&nbsp;件&nbsp;&nbsp;夹&nbsp;&nbsp;属&nbsp;&nbsp;性：</td>
					<td>
						<select  style="width: 150px;" id="cmmfolderproperty" class="easyui-combobox">
							    <option value="0">私有</option>   
    							<option value="1">共享</option> 
    					</select>
					</td>
				</tr>					
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr>
					<td></td>
					<td align="right"><a id="cmmflsureButton"
						onclick="subcmmClassidy()"
						style="cursor: pointer; color: #0E4876; font-size: 14px;">确定</a></td>
				</tr>
			</table>
		</div>
		<!-- end -->
		<!-- 素材分类目录 -->
		<div id="ml" class="easyui-window" title="素材分类目录" minimizable="false"
			data-options="modal:true,closed:true,draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,"
			style="width: 300px; height: 120px; padding: 10px;">
			<table>
				<tr align="left">
					<td style="color: #666666; font-size: 14px;">
						选&nbsp;&nbsp;择&nbsp;&nbsp;分&nbsp;&nbsp;类：</td>
					<td><select class="easyui-combobox" style="width: 150px;"
						id="classifyName">



					</select></td>
				</tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr>
					<td></td>
					<td align="right"><a id="subclassify" onclick="subclassify()"
						style="cursor: pointer; color: #0E4876; font-size: 14px;">确定</a></td>
				</tr>
			</table>
		</div>
</body>
</html>
