<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="beans.user.UserBean" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	UserBean userBean = (UserBean)session.getAttribute("user");
%>


<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'MyJsp.jsp' starting page</title>
<link rel="stylesheet" type="text/css" href="js/easyui.css">
<link rel="stylesheet" type="text/css" href="js/icon1.css">
<link rel="stylesheet" type="text/css" href="js/demo.css">
<link rel="stylesheet" type="text/css" href="js/style.css">
<style type="text/css">
.datagrid-header-rownumber,.datagrid-cell-rownumber {
	width: 40px;
}
</style>
<script type="text/javascript" src="js/msgbox.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
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
	var thisUser = '<%=userBean.getUsername()%>';
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
<script type="text/javascript" src="admin/user/user.js"></script>

</head>
<body onload="init()" class="easyui-layout" scroll="no"
	style=" height:100%; width:100%">
	<div style="height:7%; background-color:transparent;">
		<img src="img/user1.png" width="100%" />
	</div>
	<div style="border:'thin';width: 100%;margin-top: 2px;" id="mains">
	<div style="height:7%;position: relative;">
		<div class="button_div button_div_border" 
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="addusertd">
			<img onclick="newuser()" title="新建用户" class="button_img"
				src="img/add.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="updateusertd">
			<img onclick="updateuser()" title="编辑用户" class="button_img"
				src="img/bj.png" />
		</div>
		<div class="button_div button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)"
			id="deleusertd">
			<img onclick="deleteuser()" title="删除用户" class="button_img"
				src="img/sc.png" />
		</div>
		<div style="float: left;margin-left: 10px;" class="button_div_border"
			onmouseover="buttonover(event)" onmouseout="buttonout(event)">
			<img onclick="datarefresh()" title="刷新" class="button_img"
				src="img/sx.png" />
		</div>
		<div style="float: right;">
			<div style=" margin: 6 0 0 3; position:absolute; ">
				<input id="searchname" class="easyui-textbox"
					style="width:150px; height:25px;" data-options="prompt:'请输入登录名/姓名'">
			</div>
			<img onclick="searchUser()" class="bgimage" src="img/search2.png"
				style="cursor:pointer; height:36px; width:200px;" align="middle" />
		</div>
	</div>
	<%--用户列表--%>
	<div style="height:85%;width: 100%;float: left;">
			<table style="width:100%;height:100%" id="userlist"></table>
		</div>
</div>
	
	<%--新建用户--%>
	<div id="newuser" class="easyui-window" title="新建用户"
		minimizable="false"
		data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
		style="width:800px;height:500px;padding:10px;">
		<div
			style="width:100%; height:99%; background:url(img/u106.png) no-repeat;border-bottom:1px solid #7fb7ea;border-right:1px solid #7fb7ea;">
			<div id="newfuck"
				style=" float:left;  width:350px; height:100%; border-right:1px solid #7fb7ea">
				<table>
					<tr>
						<td
							style="color:#666666; font-size:14px;padding-top:16px; padding-left:15px">
							登&nbsp录&nbsp名：</td>
						<td style="padding-top:16px;"><input id="username"
							onfocus="comsss(this.id,this.value)"
							onblur="comddd(this.id),nameverify()"
							style="width:220px;height:30px;color:#aaaaaa; line-height:30px;padding-left:10px;border:1px solid #7fb7ea;"
							class="easyui-validatebox" data-options="validType:'userValid'">
						</td>
					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							密&nbsp&nbsp&nbsp&nbsp&nbsp码：</td>
						<td style="padding-top:10px; "><input id="password"
							onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
							style="width:220px;height:30px;color:#aaaaaa;border:1px solid #7fb7ea; line-height:30px;padding-left:10px"
							class="easyui-validatebox" data-options="validType:'passValid'"
							value="请输入密码"></td>

					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							确认密码：</td>
						<td style="padding-top:10px;"><input id="comfirmpass"
							value="请输入确认密码" onfocus="comsss(this.id,this.value)"
							onblur="comddd(this.id)"
							style="width:220px;height:30px;color:#aaaaaa;border:1px solid #7fb7ea;line-height:30px; padding-left:10px"
							class="easyui-validatebox" data-options="validType:'passValid'">
						</td>
					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							姓&nbsp&nbsp&nbsp&nbsp&nbsp名：</td>
						<td style="padding-top:10px;"><input id="realname"
							onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
							style="width:220px;height:30px;color:#aaaaaa;border:1px solid #7fb7ea; padding-left:10px"
							value="请输入真实姓名"></td>
					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							手&nbsp&nbsp&nbsp&nbsp&nbsp机：</td>
						<td style="padding-top:10px;"><input id="telephone"
							onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
							style="width:220px;height:30px;line-height:30px;color:#aaaaaa;border:1px solid #7fb7ea; padding-left:10px"
							class="easyui-validatebox"
							data-options="validType:'telephoneValid'" value="请输入手机号">
						</td>
					</tr>

					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							邮&nbsp&nbsp&nbsp&nbsp&nbsp箱：</td>
						<td style="padding-top:10px;"><input maxlength="25" id="email"
							onfocus="comsss(this.id,this.value)" onblur="comddd(this.id)"
							style="width:220px;height:30px;color:#aaaaaa;border:1px solid #7fb7ea; line-height:30px;padding-left:10px"
							value="请输入邮箱" class="easyui-validatebox"
							data-options="validType:'emailValid'"></td>
					</tr>
					<tr>
						<td valign="top"
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:15px;">
							描&nbsp&nbsp&nbsp&nbsp&nbsp述：</td>
						<td style="padding-top:10px;"><textarea cols="50" rows="10"
								id="userdes" onfocus="comsss(this.id,this.value)"
								onblur="comddd(this.id)"
								style="width:220px;height:100px;color:#aaaaaa;border:1px solid #7fb7ea; padding-left:10px;padding-top:5px;resize:none;">请输入描述</textarea>
						</td>
					</tr>
				</table>

			</div>

			<div
				style=" float:right;  width:427px; height:393px;padding-top:1px; ">
				<table id="rolelistone" style="width:100%; height:100%;"></table>
				<div style="padding-top:15px;padding-left:160px;">
					<a id="saveuserbut" onclick="saveuser()"
						style="cursor:pointer;color:#0E4876; font-size:14px;">确定</a> <a
						onclick="cancel();"
						style="cursor:pointer;color:#0E4876; font-size:14px;padding-left:10px;">取消</a><a
						onclick="refreshRole();"
						style="cursor:pointer;color:#0E4876; font-size:14px;padding-left:10px;font-family:微软雅黑;">刷新角色</a>
				</div>
			</div>

		</div>


	</div>

	<%--修改用户--%>

	<div id="updateuser" class="easyui-window" title="修改用户"
		minimizable="false"
		data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
		style="width:800px;height:500px;padding:10px;">
		<div
			style="width:100%; height:99%; background:url(img/u106.png) no-repeat;border-bottom:1px solid #7fb7ea;border-right:1px solid #7fb7ea;">
			<div
				style=" float:left;  width:350px; height:100%; border-right:1px solid #7fb7ea">
				<table>
					<tr>
						<td
							style="color:#666666; font-size:14px;padding-top:16px; padding-left:15px">
							登&nbsp录&nbsp名：</td>
						<td style="padding-top:16px;"><input id="updateusername"
							onfocus="comsss(this.id,this.value)"
							onblur="updatecomddd(this.id),updatenameverify()"
							style="width:220px;height:30px;color:black;line-height:30px;border:1px solid #7fb7ea; padding-left:10px;"
							value="请输入登录名称" readOnly="true"></td>
					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							密&nbsp&nbsp&nbsp&nbsp&nbsp码：</td>
						<td style="padding-top:10px; "><input id="updatepassword"
							type="password" onfocus="updatecomsss(this.id,this.value)"
							onblur="updatecomddd(this.id)"
							style="width:220px;height:30px;line-height:30px;border:1px solid #7fb7ea; padding-left:10px"
							value="!@#$%^&*" class="easyui-validatebox"
							data-options="validType:'passValid'"></td>

					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							确认密码：</td>
						<td style="padding-top:10px;"><input
							id="updatecomfirmpassword" type="password" value="!@#$%^&*"
							onfocus="updatecomsss(this.id,this.value)"
							onblur="updatecomddd(this.id)"
							style="width:220px;height:30px;line-height:30px;border:1px solid #7fb7ea; padding-left:10px"
							class="easyui-validatebox" data-options="validType:'passValid'">
						</td>
					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							姓&nbsp&nbsp&nbsp&nbsp&nbsp名：</td>
						<td style="padding-top:10px;"><input id="updaterealname"
							onfocus="comsss(this.id,this.value)"
							onblur="updatecomddd(this.id)"
							style="width:220px;height:30px;color:black;border:1px solid #7fb7ea;line-height:30px; padding-left:10px"
							value="请输入真实姓名"></td>
					</tr>
					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							手&nbsp&nbsp&nbsp&nbsp&nbsp机：</td>
						<td style="padding-top:10px;"><input id="updatetelephone"
							onfocus="comsss(this.id,this.value)"
							onblur="updatecomddd(this.id)"
							style="width:220px;height:30px;color:black;border:1px solid #7fb7ea;line-height:30px; padding-left:10px"
							value="请输入手机号" class="easyui-validatebox"
							data-options="validType:'telephoneValid'"></td>
					</tr>

					<tr>
						<td
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:10px;">
							邮&nbsp&nbsp&nbsp&nbsp&nbsp箱：</td>
						<td style="padding-top:10px;"><input id="updateemail" maxlength="25" 
							onfocus="comsss(this.id,this.value)"
							onblur="updatecomddd(this.id)"
							style="width:220px;height:30px;color:black;border:1px solid #7fb7ea;line-height:30px; padding-left:10px"
							value="请输入邮箱" class="easyui-validatebox"
							data-options="validType:'emailValid'"></td>
					</tr>
					<tr>
						<td valign="top"
							style="color:#666666; font-size:14px; padding-left:15px;padding-top:15px;">
							描&nbsp&nbsp&nbsp&nbsp&nbsp述：</td>
						<td style="padding-top:10px;"><textarea cols="50" rows="10"
								id="updateuserdes" onfocus="comsss(this.id,this.value)"
								onblur="updatecomddd(this.id)"
								style="width:220px;height:100px;color:black;border:1px solid #7fb7ea; padding-left:10px;padding-top:5px;resize:none;">请输入描述</textarea>
						</td>
					</tr>
				</table>
			</div>

			<div
				style=" float:right;  width:427px; height:393px;padding-top:1px; ">

				<table id="updaterolelistone" style="width:100%; height:100%;"></table>
				<div style="padding-top:15px;padding-left:160px;">
					<a id="updateuserbut" onclick="updatesaveuser()"
						style="cursor:pointer;color:#0E4876; font-size:14px;">确定</a> <a
						onclick="updatecancel()"
						style="cursor:pointer;color:#0E4876; font-size:14px;padding-left:15px;">取消</a> <a
						onclick="updaterefreshRole()"
						style="cursor:pointer;color:#0E4876; font-size:14px;padding-left:15px;">刷新角色</a>
				</div>
			</div>

		</div>


	</div>


	<div id="showpower" class="easyui-window" title="查看详细"
		minimizable="false"
		data-options="draggable:false,resizable:false,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,iconCls:'icon-save'"
		style="width:600px;height:520px;padding:10px;">
		<div style=" float:left;  width:48%;">
			<div style=" height:10%;">
				<img style="padding-top:1px;width:100%;" src="img/role8.png">
			</div>
			<div style=" height:400px;border:1px solid #7fb7ea;overflow:auto; ">
				<ul id="rolepowertree" class="easyui-tree"></ul>
			</div>
		</div>
		<div style=" float:right; width:48%;">
			<div style=" height:10%;">
				<img style="padding-top:1px;width:100%;" src="img/role9.png">
			</div>
			<div style=" height:400px;border:1px solid #7fb7ea;overflow:auto;">
				<ul id="roleterminaltree" class="easyui-tree"></ul>
			</div>
		</div>
	</div>
</body>
</html>
