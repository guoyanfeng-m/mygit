<%@ page language="java"  import="java.util.Properties,java.io.FileInputStream,java.io.FileNotFoundException,java.io.IOException" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<%
  String version = "";
  
 Properties pro = new Properties(); 
 String realpath = request.getRealPath("/WEB-INF/classes"); 
 try{  
 //读取配置文件
 FileInputStream in = new FileInputStream(realpath+"/version.properties"); 
 pro.load(in); 
 } 
 catch(FileNotFoundException e){ 
     out.println(e); 
 } 
 catch(IOException e){out.println(e);} 

//通过key获取配置文件
 version = pro.getProperty("version");
 
 //byte b[]=title.getBytes("utf-8");
 //title=new String(b);
 
%>
<head>
<meta http-equiv="Content-Type" content="text/html;"> 
  <base href="<%=basePath%>">
	<link rel="stylesheet" type="text/css" href="admin/system/easyui/easyui.css">
	<link rel="stylesheet" type="text/css" href="admin/system/easyui/icon.css">
	<link rel="stylesheet" type="text/css" href="js/demo.css">
	<link rel="stylesheet" type="text/css" href="js/style.css">
	<script type="text/javascript" src="js/msgbox.js"></script> 
	<script type="text/javascript" src="js/jsp/jquery.js"></script>
	<script type="text/javascript" src="admin/system/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/check.js"></script>
	<script type="text/javascript" src="admin/system/system.js"></script>
	
<style type="text/css"> 
    .bgimage{  
        z-index:-1;
    } 
	body{
	font-family:"微软雅黑";
	}
	p{
	font-size:15px;
	}
</style> 
<!-- 上传进度条 -->
<script type="text/javascript">
		function start() {
			var value = $('#p').progressbar('getValue');
			if (value < 100) {
				value += Math.floor(Math.random() * 10);
				$('#p').progressbar('setValue', value);
			    setTimeout(arguments.callee, 200);
				if (value >= 100) {
					//$('#button').linkbutton('disable');//文件上传成功之后禁用按钮
					//$('#p').progressbar('disable');//文件上传成功之后禁用进度条
				}
			}
		}
	   function begin() {
			var value = $('#pp').progressbar('getValue');
			if (value < 100) {
				value += Math.floor(Math.random() * 10);
				$('#pp').progressbar('setValue', value);
			    setTimeout(arguments.callee, 200);
				if (value >= 100) {
					//$('#button').linkbutton('disable');//文件上传成功之后禁用按钮
					//$('#p').progressbar('disable');//文件上传成功之后禁用进度条
				}
			}
		}
</script>
</head>
<body>
<div style="height:100%;width:100%;" class="easyui-layout" scroll="no" fit="true">
<div region="north" style="width:10%;height:47px;;border:0px;" scroll="no">
	<img src="img/xtsz0.PNG" width="100%" border="0" style="border:0px;"></img>
</div>
	<div region="center" style="top:15px;width:100%;height:100%;" scroll="yes">
	<div id="aa" style="padding-top:0px;" class="easyui-accordion" data-options="multiple:true">
		<!--<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;终端License导入"  style="overflow:auto;padding:10px;">
			<div>
			  <label><span style="color:green">请选择：</span></label>
			   <input id="import"  type="file" name="file" style="width:230px;height:25px">
		       <input type="button" id="importin" value="导入" style="width:40px;height:25px;"/>
			   <input type="button" id="importout" value="导出" style="width:40px;height:25px;"/>
			  </div>
		</div>
		-->
		
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务器版本查看" style="overflow:auto;padding:10px;width:100%;">
			<label><span style="color:green">服务器版本：<%=version %></span></label>
		</div>
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元素权限设置" style="overflow:auto;padding:10px;width:100%;">
			      <input type="radio" name="radio" id="radio" value="0" onclick="saveelepower(0)"/>
        				所有人可见
          		 <input type="radio" name="radio" id="radio1" value="1" onclick="saveelepower(1)"/>
      					仅创建者
      			  <input type="radio" name="radio" id="radio2" value="2" onclick="saveelepower(2)"/>
      					角色共享
		</div>		
		<div  id="a1" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WEB服务器设置"  style="overflow:auto;padding:10px;width:100%;">
			   <label><span style="color:green">WEBURL：</span></label>
			   <input id="webServerUrl"  name="webServerUrl" style="width:250px;height:25px;">&nbsp;&nbsp;&nbsp;
			   <label><span style="color:green">内网WEBIP：</span></label>
			   <input id="webServerIp"  name="webServerIp" style="width:200px;height:25px;">
			   <label><span style="color:green">外网WEBIP：</span></label>
			   <input id="outWebServerIp"  name="outWebServerIp" style="width:200px;height:25px;">
		       <input type="button" id="save" value="保存" onclick="saveweb(0)" style="width:40px;height:25px;"/>
		</div>
		<div  id="a2" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系方式"  style="overflow:auto;padding:10px;width:100%;">
			   <label><span style="color:green">联系电话：</span></label>
			   <input id="contactPhone"  name="contactPhone" style="width:250px;height:25px;">&nbsp;&nbsp;&nbsp;
			   <label><span style="color:green">联系传真：</span></label>
			   <input id="contactFax"  name="contactFax" style="width:200px;height:25px;">
			   <label><span style="color:green">联系邮箱：</span></label>
			   <input id="contactEmail"  name="contactEmail" style="width:200px;height:25px;">
		       <input type="button" id="saveCantac" value="保存" onclick="saveContact()" style="width:40px;height:25px;"/>
		</div>

		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;自动删除日志设置"  style="overflow:auto;padding:10px;">
			   <label><span style="color:green">统计日志保留天数：</span></label>
			   <input id="statisticsDelete"  class="easyui-validatabel" name="statisticsDelete" style="width:100px;height:25px;">&nbsp;&nbsp;&nbsp;
			   <label><span style="color:green">操作日志保留天数：</span></label>
			   <input id="logDelete"  class="easyui-validatabel" name="logDelete" style="width:100px;height:25px;">
		       <input type="button" id="save" value="保存" onclick="saveweb(2)" style="width:40px;height:25px;"/>
		       <input type="button"  value="查看当前" onclick="$('#logWin').window('open')" style="width:65px;height:25px;"/>
		</div>
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手动删除操作日志设置"  style="overflow:auto;padding:10px;">
			 <label style="color:green">开始日期：</label> &nbsp;&nbsp;&nbsp;&nbsp;
             <input id="starttime" name="starttime" class="easyui-datebox" editable="false" style="width:130px;height:25px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 <label style="color:green">结束日期：</label> &nbsp;&nbsp;&nbsp;&nbsp;
             <input id="endtime" name="endtime" class="easyui-datebox"  editable="false" style="width:130px;height:25px">&nbsp;&nbsp;
             <input type="button" id="dellog" value="删除" onclick="delOpearLog()" style="width:40px;height:25px;"/>
		</div>
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手动删除统计日志设置"  style="overflow:auto;padding:10px;">
			 <label style="color:green">开始日期：</label> &nbsp;&nbsp;&nbsp;&nbsp;
             <input id="starttime2" name="starttime" class="easyui-datebox" editable="false" style="width:130px;height:25px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 <label style="color:green">结束日期：</label> &nbsp;&nbsp;&nbsp;&nbsp;
             <input id="endtime2" name="endtime" class="easyui-datebox" editable="false" style="width:130px;height:25px;">&nbsp;&nbsp;
             <input type="button" id="delstatic" value="删除" onclick="delstatis()" style="width:40px;height:25px;"/>
		</div>
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;终端更新上传"  style="overflow:auto;padding:10px;">
		<form id="upload" name="formname" enctype="multipart/form-data" method="post">
		    <label style="color:green">请选择：</label> &nbsp;&nbsp;&nbsp;
		    <select  name="version" style="width:100px;height:25px;" id="version">
		        <option value="1">windows版本</option>
		        <option value="2">android版本</option>
		    </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    <label><span style="color:green">请选择：</span></label>
		    <input id="import" type="file" name="myfiles" style="width:230px;height:25px">&nbsp;&nbsp;&nbsp;
		    <label style="color:green">版本号：</label> &nbsp;&nbsp;&nbsp;&nbsp;
		    <input type="text"  name="versionnumber" id="versionNumber" style="height:25px;"/>
		    <input type="button"  value="上传" onclick="uploadFile()" style="width:40px;height:25px;"/>
		    <input type="button"  value="查看当前版本" onclick="$('#terminalWin').window('open')" style="width:90px;height:25px;"/>
		  </form>
		    <div><div id="p" class="easyui-progressbar" data-options="value:0" style="width:200px;display: none;left:550px;"></div>
		    <script>
		       $('#p').progressbar({
			      text: '文件上传{value}%',
		       });
	        </script>
		    </div>
		</div>

	    <div  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报警消息提示设置"  style="overflow:auto;padding:10px;">
			<label style="color:green">发送邮箱：</label> &nbsp;&nbsp;&nbsp;&nbsp;
		    <input type="text" id="monitorSenderMail" style="width:200px;height:25px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
		    <label style="color:green">邮箱密码：</label> &nbsp;&nbsp;&nbsp;&nbsp;
		    <input type="password" id="monitorSenderPass" style="width:200px;height:25px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
		    <label style="color:green">接收邮箱：</label> &nbsp;&nbsp;&nbsp;&nbsp;
		    <input type="text" id="monitorRecieverMail" style="width:200px;height:25px;"/>
		    <input type="button" id="" value="保存" style="width:40px;height:25px;" onclick="sendEmail()"/>
		    <input type="button"  value="查看当前" onclick="$('#sendEmailWin').window('open')" style="width:65px;height:25px;"/>
		</div>
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FTP服务器设置"  style="overflow:auto;padding:10px;">
			<div>
			    &nbsp;&nbsp;&nbsp;&nbsp;<label style="color:green">外网FTPIP：</label> &nbsp;&nbsp;&nbsp;&nbsp;
			    <input id="outFtpServerIp" type="text" style="width:200px;height:25px;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			     <label style="color:green">FTP上传端口：</label> &nbsp;&nbsp;&nbsp;&nbsp;
			    <input id="uploadPort" type="text" style="width:200px;height:25px;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    <label style="color:green">FTPURL：</label> &nbsp;&nbsp;&nbsp;&nbsp;
			    <input id="ftpMappingUrl" type="text" style="width:200px;height:25px;"/>
		    </div><br/>
		    <div>
                &nbsp;&nbsp;&nbsp;&nbsp;<label style="color:green">内网FTPIP：</label> &nbsp;&nbsp;&nbsp;
			    &nbsp;&nbsp;&nbsp;&nbsp;<input id="ftpServerIp" type="text" style="width:200px;height:25px;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    &nbsp;&nbsp;&nbsp;&nbsp;<label style="color:green">FTP下载端口：</label> &nbsp;&nbsp;&nbsp;&nbsp;
			    <input id="downloadPort" type="text" style="width:200px;height:25px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
			    <input type="button"  value="保存" onclick="saveweb(1)" style="width:40px;height:25px;"/>
		    </div>
		</div>
		<!-- <div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;审核模块"  style="overflow:auto;padding:10px;">
			 <div>
			   <input type="checkbox" id="ckall"><label style="color:green">全选</label>&nbsp;
			   <input type="checkbox" id="rmall" style="display: none"><label style="color:green"></label>&nbsp;
			   <input type="button" value="保存" onclick="checkSave()"/>
			 </div><br/>
			 <div>
			   <label style="color:green">请选择：</label> &nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox1" id="c8" value="8"  onchange="SHcheck(this.value)"><span id="s8">终端管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox1" id="c4" value="4" onchange="SHcheck(this.value)"><span id="s4">素材管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox1" id="c5" value="5" onchange="SHcheck(this.value)"><span id="s5">节目管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox1" id="c6" value="6" onchange="SHcheck(this.value)"><span id="s6">滚动消息</span>
			 </div>
		</div> -->
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;异常监听设置" style="overflow:auto;padding:10px;width:100%;">
			      <input type="radio" name="listenradio" id="listenradio" value="0" onclick="savelisten(0)"/>
        				关闭
          		 <input type="radio" name="listenradio" id="listenradio1" value="1" onclick="savelisten(1)"/>
      					开启
		</div>
		<!-- <div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作日志设置"  style="overflow:auto;padding:10px;">
			<div>
			   <input type="checkbox" id="logckall"><label style="color:green">全选</label>&nbsp;
			   <input type="checkbox" id="logrmall" style="display: none"><label style="color:green"></label>&nbsp;
			   <input type="button" value="保存" onclick="opearLogSave()"/>
			 </div><br/>
             <div>
			   <label style="color:green">请选择：</label> &nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch2" value="2" onchange="checklog(this.value)"><span id="cs2">用户管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch3" value="3" onchange="checklog(this.value)"><span id="cs3">角色管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch4" value="4" onchange="checklog(this.value)"><span id="cs4">素材管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch5" value="5" onchange="checklog(this.value)"><span id="cs5">节目管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch6" value="6" onchange="checklog(this.value)"><span id="cs6">滚动消息</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch7" value="7" onchange="checklog(this.value)"><span id="cs7">日志管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch8" value="8" onchange="checklog(this.value)"><span id="cs8">终端管理</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch9" value="9" onchange="checklog(this.value)"><span id="cs9">终端监控</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   <input type="checkbox" name="checkbox2" id="ch10" value="10" onchange="checklog(this.value)"><span id="cs10">系统设置</span>
			 </div>
		</div> -->
	    <div  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务器备份与还原"  style="overflow:auto;padding:10px;">
			 <form id="uploadserver" name="upserverForm" enctype="multipart/form-data" method="post">
			    <input type="button" id="" value="服务器备份" style="width:150px;height:25px;" onclick="bakupServer()"/>
			    <input type="button" id="" value="服务器备份文件下载" style="width:150px;height:25px;" onclick="downServer()"/>
			    <label style="color:green">服务器还原：</label>
			    <input id="severfiles" type="file" name="severfiles" style="width:150px;height:25px">&nbsp;&nbsp;&nbsp;
		 		<input type="button"  value="上传服务器备份文件" onclick="uploadserverFile()" style="width:180px;height:25px;"/>
		    </form>
		</div>		
		<!-- -license操作 -->
		<div title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;license管理"  style="overflow:auto;padding:10px;">
		<div style="padding-left: 20px;height: 20px;margin-bottom:10px; border-bottom: 2px #CDCDCD solid;">
		    <label style="color:green;">终端个数：<span id="terminalTotal" style="color:red"></span></label>
		   <label style="color:green;margin-left: 100px;">已注册个数：<span id="terminalHad" style="color:red"></span></label>&nbsp;&nbsp;</label>          </div><br/>
          <div style="width:70%;float:left;">
		   <label style="color:green">用户名：</label> &nbsp;&nbsp;
		   <input type="text" name="loginname" id="loginname" style="height:25px;width:120px;">&nbsp;&nbsp;&nbsp;
		   <label style="color:green">密码：</label> &nbsp;&nbsp;&nbsp;&nbsp;
		   <input type="password" name="password" id="password" style="height:25px;width:120px;">&nbsp;&nbsp;&nbsp;
		   <label style="color:green">license服务器地址：</label> &nbsp;&nbsp;
		   <input type="text" name="url" id="url" style="height:25px;width:200px;">&nbsp;&nbsp;&nbsp;
		   <input type="button" id="getLicense" value="获取license信息" style="height:25px" onclick="getLicense()">
		   </div>
		   <div style="float:left;">
		   <form id="uploadform" name="uploadform" enctype="multipart/form-data" method="post">
		    <label><span style="color:green">请选择：</span></label>
		    <input  type="file" name="fileData" style="width:130px;height:25px">
		    <input  type="button" style="width:50px;height:25px" value="上传" onclick="upload()"/>
		    </form>
		    <div><div id="pp" class="easyui-progressbar" data-options="value:0" style="width:200px;display: none;left:10%;"></div>
		    <script>
		       $('#pp').progressbar({
			      text: '文件上传{value}%',
		       });
	        </script>
	        </div>
		 </div>
	        <div style="float:left;">
	        <input type="button"  onclick="exportLicense()" value="导出licenses">
	        </div>
		</div>
		</div>
	</div>
	</div>

	<!-- web服务器设置窗体 -->
	<div id="webMessage" class="easyui-window" title="web服务器信息" resizable="false" collapsible="false" maximizable="false" minimizable="false"  data-options="draggable:false,modal:true,closed:true,iconCls:'icon-save'" style="width:350px;height:300px;padding:10px;">
		<div align="center" style="padding-top:30px; padding-left:30px;width:100%; height:100%; position:absolute;color:green;text-align:left;">
		   <p>当前配置：</p>
	       <p>WebUrl：<span id="weburl" style="color:green;font-size:10px"></span></p>
	       <p>WebIP：<span id="webip" style="color:green;font-size:10px"/></span></p>
	       <p>WebPort：<span id="webport" style="color:green;font-size:10px"/></span></p>
		</div>
		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
	</div>
		<!--自动删除日志设置窗体 -->
	<div id="logWin" class="easyui-window" title="自动删除日志设置" resizable="false" collapsible="false" maximizable="false" minimizable="false"  data-options="draggable:false,modal:true,closed:true,iconCls:'icon-save'" style="width:250px;height:250px;padding:10px;">
		<div align="center" style="padding-top:30px; padding-left:30px;width:100%; height:100%; position:absolute;color:green;text-align:left;">
		   <p>当前配置：</p>
	       <p>统计日志保留天数：<span id="statisticsDeleteWin" style="color:green;font-size:10px"></span></p>
	       <p>操作日志保留天数：<span id="logDeleteWin" style="color:green;font-size:10px"/></span></p>
		</div>
		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
	</div>
	<!--终端更新上传窗体 -->
	<div id="terminalWin" class="easyui-window" title="终端更新上传" resizable="false" collapsible="false" maximizable="false" minimizable="false"  data-options="draggable:false,modal:true,closed:true,iconCls:'icon-save'" style="width:250px;height:250px;padding:10px;">
		<div align="center" style="padding-top:20px; padding-left:30px;width:100%; height:100%; position:absolute;color:green;text-align:left;">
		   <p>当前版本：</p>
	       <p>windows版本：<span id="lastTerminalAppVer" style="color:green;font-size:10px"></span></p>
	       <p>android版本：<span id="lastAndroidTerminalAppVer" style="color:green;font-size:10px"/></span></p>
		</div>
		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
	</div>
		<!--邮箱显示窗体 -->
	<div id="sendEmailWin" class="easyui-window" title="邮箱信息" resizable="false" collapsible="false" maximizable="false" minimizable="false"  data-options="draggable:false,modal:true,closed:true,iconCls:'icon-save'" style="width:300px;height:250px;padding:10px;">
		<div align="center" style="padding-top:20px; padding-left:30px;width:100%; height:100%; position:absolute;color:green;text-align:left;">
		   <p>当前配置：</p>
	       <p>发送邮箱：<span id="monitorSenderMailWin" style="color:green;font-size:10px"></span></p>
	       <p>接收邮箱：<span id="monitorRecieverMailWin" style="color:green;font-size:10px"/></span></p>
		</div>
		<img class="bgimage" src="img/u106.png" style=" height:100%; width:100%;" align="middle"/>
	</div>
</body>
</html>