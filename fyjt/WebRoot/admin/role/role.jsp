<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>


<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'MyJsp.jsp' starting page</title>



<link rel="stylesheet" type="text/css" href="js/easyui.css">
<link rel="stylesheet" type="text/css" href="js/icon1.css">
<link rel="stylesheet" type="text/css" href="js/demo.css">
<link rel="stylesheet" type="text/css" href="js/style.css">
<script type="text/javascript" src="js/msgbox.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="admin/role/role.js"></script>
<script type="text/javascript" src="js/check.js"></script>
<style type="text/css">
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
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>
<body onload="init()" class="easyui-layout" scroll="no"
	style=" height:100%; width:100%">
	<div style="height:7%; background-color:transparent;">
		<img src="img/role.png" width="100%" />
	</div>
	<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
	<div style="height:7%;position: relative;">
		<div class="button_div button_div_border" 
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="addroletd">
			<img onclick="newrole()" title="新建角色" class="button_img"
				src="img/add.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="updateroletd">
			<img onclick="updaterole()" title="编辑角色" class="button_img"
				src="img/bj.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="deleroletd">
			<img onclick="deleteRole()" title="删除角色" class="button_img"
				src="img/sc.png" />
		</div>
		<div style="float: left;margin-left: 10px;" class="button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img onclick="datarefresh()" title="刷新节目" class="button_img"
				src="img/sx.png" />
		</div>
		<div style="float: right;">
			<div style=" margin: 6 0 0 3; position:absolute; ">
				<input id="searchname" class="easyui-textbox"
					style="width:150px; height:25px;" data-options="prompt:'请输入角色名称'">
			</div>
			<img onclick="searchRole()" class="bgimage" src="img/search2.png"
				style="cursor:pointer; height:36px; width:200px;" align="middle" />
		</div>
	</div>
	<div style="height:85%;width: 100%;float: left;">
			<table style="width:100%;height:100%" id="rolelist"></table>
		</div>
</div>
	<!-- <table width="100%" height="8%" border="0">
		<tr>
			<td width="10%" align="right" style="display:none;" id="addroletd"><img
				id="addroleimg" src="img/add.png" onclick="newrole()"
				style="cursor:pointer" /></td>
			<td width="10%" align="right" style="display:none;" id="updateroletd"><img
				id="updateroleimg" src="img/bj.png" onclick="updaterole()"
				style="cursor:pointer" /></td>
			<td width="10%" align="right" style="display:none;" id="deleroletd"><img
				id="deleteroleimg" src="img/sc.png" onclick="deleteRole()"
				style="cursor:pointer" /></td>
			<td width="10%" align="right"><img onclick="datarefresh()"
				title="刷新" src="img/sx.png" style="cursor:pointer" /></td>
			<td valign="middle">&nbsp;</td>
			<td align="right" style="width:300px;">
				<div style=" padding-top:6px; padding-left:1%; position:absolute; ">
					<input id="searchname" class="easyui-textbox"
						style="width:230px; height:30px ;line-height:30px;"
						data-options="prompt:'请输入角色名称'">
				</div> <img id="searchimg" onclick=" searchRole()"
				onmousedown="changeImg()" onmouseup="changeImg2()"
				src="img/search2.png" style="cursor:pointer; " align="middle" />
			</td>
		</tr>
	</table> -->



	
	<div id="newrole" class="easyui-window" title="新建角色"
		minimizable="false"
		data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
		style="width:1000px;height:670px;padding:10px;">
		<div
			style=" float:left;  width:100%; height:99%; background:url(img/u106.png) no-repeat;border-bottom:1px solid #7fb7ea;border-right:1px solid #7fb7ea;">
			<div style=" float:left;  width:65%; height:100%;">
				<div style="height:7%;width:100%">
					<img
						style="padding-top:2px;padding-left:1px;height:100%;width:100%"
						src="img/role1.png">
				</div>
				<div
					style=" margin:1px 0px 0px 1px;padding-left:10px; width:98%; height:15%; background:#FFFFFF; color:#FFFFFF;border:1px solid #7fb7ea;">
					<table>
						<tr>
							<td
								style="color:#666666; font-size:14px;font-weight:bold;padding-top:16px;">
								角色名称：</td>
							<td style="padding-top:16px;"><input id="rolename"
								onfocus="onfocusrole(this.id,this.value)"
								onblur="blurrole(this.id),nameverify()"
								style="width:170px;height:30px;color:#aaaaaa; padding-left:10px;border:1px solid #7fb7ea;"
								class="easyui-validatebox" data-options="validType:'roleValid'">
							</td>
							<td
								style="color:#666666;font-weight:bold; font-size:14px; padding-left:20px;padding-top:16px;">
								角色描述：</td>
							<td style="padding-top:16px;"><input id="roledes"
								onfocus="onfocusrole(this.id,this.value)"
								onblur="blurrole(this.id)"
								style="width:180px;height:30px;color:#aaaaaa; padding-left:10px;border:1px solid #7fb7ea;">
							</td>
						</tr>
						<tr>
							<td
								style="color:#666666;font-weight:bold; font-size:14px;padding-top:2px;">
								发布权限：</td>
							<td style="padding-top:2px;"><select id="pubpower" 
								onfocus="onfocusrole(this.id,this.value)"
								onblur="blurrole(this.id)"
								style="width:50px;height:30px;color:#aaaaaa; padding-left:10px;border:1px solid #7fb7ea;"></select>
							</td>							
						</tr>
					</table>
				</div>
				<div style="height:2%"></div>
				<div style="height:7%;padding-left:2px;">
					<img style="padding-top:1px;width:100%;height:100%"
						src="img/role3.png">
				</div>
				<div
					style=" margin:0px 0px 0px 2px;padding-bottom:10px; width:100%; height:68%; overflow-y: auto;">
					<div id="selectmodule" data-options="iconCls:'icon-ok'"></div>
				</div>
			</div>
			<div style=" float:right;width:32%; height:98%;">
				<div style=" height:7.5%;">
					<img style="padding-top:1px;width:100%;height:100%"
						src="img/role4.png">
				</div>
				<div
					style=" height:87%;border-left:1px solid #7fb7ea;overflow:auto;  border-bottom:1px solid #7fb7ea;background:#FFFFFF;">

					<ul id="roletree" class="easyui-tree"></ul>


				</div>
				<div style="padding-top:15px;padding-left:150px;">
					<a id="savebut" onclick="saveRole()"
						style="cursor:pointer;color:#0E4876; font-size:14px;">保存</a> <a
						onclick="$('#newrole').window('close');"
						style="cursor:pointer;color:#0E4876; font-size:14px;padding-left:10px;">取消</a>
				</div>
			</div>
		</div>

	</div>


	<%--	<div id="updaterole"  class="easyui-window" title="修改角色" minimizable="false"  data-options="modal:true,closed:true,iconCls:'icon-save'" style="width:900px;height:600px;padding:10px;">--%>
	<%--		<div  style=" float:left;  width:65%; height:100%;">--%>
	<%--			<div  style=" padding-left:10px; width:100%; height:10%; background:url(img/u106.png) no-repeat;">--%>
	<%--				<table>--%>
	<%--					<tr align="right" >--%>
	<%--						<td style="color:#666666; font-size:14px; padding-top:20px;">--%>
	<%--							角色名称：--%>
	<%--						</td>--%>
	<%--						<td style="padding-top:20px;">--%>
	<%--						<input  id="updaterolename" class="easyui-textbox" data-options="prompt:'请输入角色名称'">--%>
	<%--						</td>--%>
	<%--						<td style="color:#666666; font-size:14px; padding-left:10px; padding-top:20px;">--%>
	<%--							角色描述：--%>
	<%--						</td>--%>
	<%--						<td style="padding-top:20px;">--%>
	<%--						<input  id="updateroledes" class="easyui-textbox" style="width:250px" data-options="prompt:'请输入角色描述'">--%>
	<%--						</td>--%>
	<%--					</tr>--%>
	<%--				</table>--%>
	<%--			</div>--%>
	<%--			--%>
	<%--			--%>
	<%--			<div style=" margin:15px 0;padding-bottom:10px; padding-left:10px; width:100%; height:84%; background:url(img/u106.png) no-repeat;">--%>
	<%--				<div id="updateselectmodule" data-options="iconCls:'icon-ok'" > </div> --%>
	<%--			</div>--%>
	<%--		</div>--%>
	<%--		<div  style=" float:right;width:32%; height:98%; background:url(img/u106.png) no-repeat;">--%>
	<%--			<div style=" height:95%;">--%>
	<%--			    --%>
	<%--			<ul id="updateroletree" class="easyui-tree"></ul>--%>
	<%--		--%>
	<%--				--%>
	<%--			</div>--%>
	<%--			<div  align="right"> --%>
	<%--       			 <a  class="easyui-linkbutton" onclick="updatesaveRole()" iconCls="icon-ok">保存</a> --%>
	<%--       			 <a  class="easyui-linkbutton" onclick="" iconCls="icon-cancel">取消</a> --%>
	<%--    		</div>--%>
	<%--		</div>--%>
	<%--		--%>
	<%--	</div>--%>

	<div id="updaterole" class="easyui-window" title="编辑角色"
		minimizable="false"
		data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
		style="width:1000px;height:670px;padding:10px;">
		<div
			style=" float:left;  width:100%; height:99%; background:url(img/u106.png) no-repeat;border-bottom:1px solid #7fb7ea;border-right:1px solid #7fb7ea;">
			<div style=" float:left;  width:65%; height:100%;">
				<div style="height:7%;width:100%">
					<img
						style="padding-top:2px;padding-left:1px;height:100%;width:100%"
						src="img/role1.png">
				</div>
				<div
					style=" margin:1px 0px 0px 1px;padding-left:10px; width:98%; height:15%; background:#FFFFFF; color:#FFFFFF;border:1px solid #7fb7ea;">
					<table>
						<tr>
							<td style="color:#666666; font-weight:bold;font-size:14px;padding-top:16px;">
								角色名称：</td>
							<td style="padding-top:16px;"><input id="updaterolename"
								onfocus="onfocusrole(this.id,this.value)"
								onblur="updateblurrole(this.id),updatenameverify()"
								style="width:170px;height:30px;color:black; padding-left:10px;border:1px solid #7fb7ea;"
								class="easyui-validatebox" data-options="validType:'roleValid'">
							</td>
							<td
								style="color:#666666; font-size:14px;font-weight:bold; padding-left:10px;padding-top:16px;">
								角色描述：</td>
							<td style="padding-top:16px;"><input id="updateroledes"
								onfocus="onfocusrole(this.id,this.value)"
								onblur="updateblurrole(this.id)"
								style="width:170px;height:30px;color:black; padding-left:10px;border:1px solid #7fb7ea;">
							</td>
						</tr>
						<tr>
							<td
								style="color:#666666;font-weight:bold; font-size:14px;padding-top:2px;">
								发布权限：</td>
							<td style="padding-top:2px;"><select id="updatepubpower" 
								onfocus="onfocusrole(this.id,this.value)"
								onblur="blurrole(this.id)"
								style="width:50px;height:30px;color:#aaaaaa; padding-left:10px;border:1px solid #7fb7ea;"></select>
							</td>							
						</tr>
					</table>
				</div>
				<div style="height:2%"></div>
				<div style="height:7%;padding-left:2px;">
					<img style="padding-top:1px;width:100%;height:100%"
						src="img/role3.png">
				</div>
				<div
					style=" margin:0px 0px 0px 2px;padding-bottom:10px; width:100%; height:68%; overflow-y: auto;">
					<%--	border:1px solid #F00;			<table id="selectmodule" class="easyui-datagrid"  style="width:100%;height:80%"></table>--%>
					<div id="updateselectmodule" data-options="iconCls:'icon-ok'">

					</div>
				</div>
			</div>
			<div style=" float:right;width:32%; height:98%; ">
				<div style=" height:7.5%;">
					<img style="padding-top:1px;width:100%;height:100%"
						src="img/role4.png">
				</div>
				<div
					style=" height:87%;border-left:1px solid #7fb7ea; overflow:auto; border-bottom:1px solid #7fb7ea;background:#FFFFFF;">
					<ul id="updateroletree" class="easyui-tree"></ul>

				</div>
				<div style="padding-top:15px;padding-left:150px;">
					<a id="updaterolebut" onclick="updatesaveRole()"
						style="cursor:pointer;color:#0E4876; font-size:14px;">保存</a> <a
						onclick="$('#updaterole').window('close');"
						style="cursor:pointer;color:#0E4876; font-size:14px;padding-left:10px;">取消</a>
				</div>
			</div>
		</div>

	</div>

	<div id="showpower" class="easyui-window" title="查看详细"
		minimizable="false"
		data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
		style="width:600px;height:510px;padding:10px;">
		<div style=" float:left;  width:48%; ">
			<div style=" height:10%;">
				<img style="padding-top:1px;width:100%;" src="img/role8.png">
			</div>
			<div style=" height:400px;border:1px solid #7fb7ea; overflow:auto;">
				<ul id="rolepowertree" class="easyui-tree"></ul>
			</div>
		</div>
		<div style=" float:right; width:48%;">
			<div style=" height:10%;">
				<img style="padding-top:1px;width:100%;" src="img/role9.png">
			</div>
			<div style=" height:400px;border:1px solid #7fb7ea; overflow:auto;">
				<ul id="roleterminaltree" class="easyui-tree"></ul>
			</div>
		</div>
	</div>
</body>
</html>
