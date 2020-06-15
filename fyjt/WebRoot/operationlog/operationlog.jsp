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
	<script type="text/javascript" src="operationlog/jquery.js"></script>
	<script type="text/javascript" src="operationlog/ajaxfileupload/orderajaxfileupload.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/msgbox.js"></script> 
	<script type="text/javascript" src="js/check.js"></script> 
	<script type="text/javascript" src="operationlog/operationlog.js"></script> 
	<style type="text/css"> 
	 .bgimage{  
        z-index:-1;
    } 
	body{
	font-family:"微软雅黑";
	}
	font{
	  font-size: 15px;
	  color:white;
	}
	input{
	  width:100px;
	  height:25px;
	}
	.pagination .pagination-num {
	border-width: 1px;
	border-style: solid;
	margin: 0 2px;
	padding: 2px;
	width: 3em;
	height: auto;
	}
	.datagrid-header-rownumber, .datagrid-cell-rownumber {
	width: 70px;
	text-align: center;
	margin: 0;
	padding: 0;
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
    </style> 
    <script type="text/javascript">
     	var i;
    </script>
</head>
<body class="easyui-layout" onload="init()">
<div  style="width:100% ;height:99%; left: 0px;top: 0px;" scroll="yes">
        <div id="logDiv" style="width:100%;height:5%;">
          <select id="log" name="log" style="height:88%;width:9%;" onchange="change(this.value)">
             <option value="0">操作日志</option>
             <option value="1">统计日志</option>
             <option value="10">播放日志</option>
             <option value="11">故障日志</option>
          </select>
        </div>
        <!-- 播放日志 -->
		<div id="programlogTbDiv" style="padding-top:5px;height:8%;width:100%; display: none;">
		   <div  style="width:1300px; height:80px; background:url(img/rizigbcolor.png) no-repeat;">
			<table style="padding-top:10px;padding-left:20px;">
			  <tr> 
			    <td rowspan="2"><font>终端名称：</font></td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="proterminalName"/></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" align="right" style="color:white;font-size:15px;">节目名称：</td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="proName"/></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			    <td><font>开始时间：</font></td>
			    <td><input class="easyui-datetimebox" id="prostarttime" style="width:150px;height:25px;" editable="false"/></td>
			    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td rowspan="2" width="5%"><img src="img/searchlog.png"  title="查询" style="cursor: pointer;" onclick="change('12')"/></td>
			    <td rowspan="2" width="5%"><img src="img/chongzhi.png" title="重置查询" style="cursor:pointer;" onclick="prostatisticsReset()"/></td>
			    <td rowspan="2" width="5%"><img src="img/importProgramlog.png" title="导入离线播放统计" style="cursor:pointer;" onclick="uploadProData()"/></td>
			    <td rowspan="2" width="5%" style="display: none;" id="importLog"><img src="img/importplaylog.png" title="导出excel表格" style="cursor:pointer;" onclick="exportProStatisticsTJData()"/></td>
			  </tr>
			  <tr>
			    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			    <td>&nbsp;</td>
			    <td><font>结束时间：</font></td>
			    <td><input class="easyui-datetimebox" id="proendtime" editable="false" style="width:150px;height:25px;"/></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			  </tr>
			</table>
			</div>
		</div>  
		<!-- 播放日志列表 -->
		<div id="programlogBgDiv" style="height:82%;margin-top:35px; display: none;">
		  <table id="programlogdg" style="width:auto;width:100%;height:100%;"></table>
		</div>
		<!-- 导入节目播放统计 -->
		<div class="easyui-window" id="zj" title="导入播放统计" resizable="false"
			minimizable="false"
			data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false, modal:true,closed:true"
			style="width: 320px; height: 120px; padding: 5px; background: #fafafa;">
			<div region="center" border="false" border="false">
				<div style="margin-top: 15px;">
					<span style="font-size: 12px; color: #ff9966;">*文件格式示例：</span>
					<span style="font-size: 12px; color: #ff9966;" title="2016-01-01@00-00-00-00-00-00.txt">2016-01-01@00-...txt</span>
				</div>
				<input id="profileurl" name="profileurl" type="file" multiple style="width: 150px; float: left; margin-top: 10px;" title="离线播放日志"
				onchange="PreviewImage(this)"/>
				<input type="button" value="上传文件" style="float: left; margin-left: 54px; margin-top: 10px;" onclick="uploadProFile()"/><br><br/>
			</div>
			<div id="filePreview"></div>
		</div>
		<!-- 故障日志 -->
		<div id="errorlogTbDiv" style="padding-top:5px;height:8%;width:100%; display: none;">
		   <div  style="width:1300px; height:80px; background:url(img/rizigbcolor.png) no-repeat;">
			<table style="padding-top:10px;padding-left:20px;">
			  <tr> 
			    <td rowspan="2" style="display:none;"><font>故障模块：</font></td>
			    <td rowspan="2" style="display:none;">
			    	<select class="easyui-combobox" id="errorModule" editable="false" style="width:120px;height:25px;">
                          <option value="1">所有模块</option>
                          <option value="2">用户管理</option>
                          <option value="3">角色管理</option>
                          <option value="4">素材管理</option>
                          <option value="5">节目管理</option>
                          <option value="6">滚动消息</option>
                          <option value="7">日志管理</option>
                          <option value="8">终端管理</option>
                          <option value="9">终端监控</option>
                          <option value="10">系统设置</option>
                      </select>
                </td>
				<td style="display:none;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" align="right" style="color:white;font-size:15px;">故障描述：</td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="errordetail"/></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			    <td rowspan="2" ><font>开始时间：</font></td>
			    <td><input class="easyui-datetimebox" id="errorstarttime" style="width:150px;height:25px;" editable="false"/></td>
			     <td rowspan="2" ><font> 结束时间：</font></td>
			    <td><input class="easyui-datetimebox" id="errorendtime" editable="false" style="width:150px;height:25px;"/></td>
			    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td rowspan="2" width="5%"><img src="img/searchlog.png"  title="查询" style="cursor: pointer;" onclick="change('11')"/></td>
			    <td rowspan="2" width="5%"><img src="img/chongzhi.png" title="重置查询" style="cursor:pointer;" onclick="errorReset()"/></td>
			    <td rowspan="2" width="5%" style="display: none;" id="importErrorLog"><img src="img/importplaylog.png" title="导出excel表格" style="cursor:pointer;" onclick="exportProStatisticsTJData()"/></td>
			  </tr>
			</table>
			</div>
		</div>
		<!-- 故障日志列表 -->
		<div id="errorlogBgDiv" style="height:82%;margin-top:35px; display: none;">
		  <table id="errorlogdg" style="width:auto;width:100%;height:100%;"></table>
		</div>  
		<!-- 操作日志 -->      
		<div id="operalogTbDiv" style="padding-top:5px;height:8%;width:100%;">
		   <div  style="width:1300px; height:80px; background:url(img/rizigbcolor.png) no-repeat;">
			<table style="padding-top:10px;padding-left:20px;">
			  <tr> 
			    <td rowspan="2"><font>操作人名称：</font></td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="userName"/></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" align="right" style="color:white;font-size:15px;">元素名称：</td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="operationName"/></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			    <td><font>开始时间：</font></td>
			    <td><input class="easyui-datetimebox" id="starttime" style="width:150px;height:25px;" editable="false"/></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
			    <td><font>所有模块：</font></td>
			    <td> 
                    <select class="easyui-combobox" id="allModule" editable="false" style="width:120px;height:25px;">
                          <option value="">----请选择----</option>
                          <option value="1">所有模块</option>
                          <option value="2">用户管理</option>
                          <option value="3">角色管理</option>
                          <option value="4">素材管理</option>
                          <option value="5">节目管理</option>
                          <option value="6">滚动消息</option>
                          <option value="7">日志管理</option>
                          <option value="8">终端管理</option>
                          <option value="9">终端监控</option>
                          <option value="10">系统设置</option>
                          <option value="11">统计</option>
                      </select>
                </td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                 <td rowspan="2" width="5%"><img src="img/searchlog.png"  title="查询" style="cursor: pointer;" onclick="search()"/></td>
			     <td rowspan="2" width="5%"><img src="img/chongzhi.png" title="重置查询" style="cursor:pointer;" onclick="reset()"/></td>
			     <td rowspan="2" width="5%" style="display: none;" id="importLog"><img src="img/importplaylog.png" title="导出excel表格" style="cursor:pointer;" onclick="importExcel()"/></td>
			  </tr>
			  
			  <tr>
			   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			    <td>&nbsp;</td>
			    <td><font>结束时间：</font></td>
			    <td><input class="easyui-datetimebox" id="endtime" editable="false" style="width:150px;height:25px;"/></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td><font>所有操作：</font></td>
                <td>
                  <select class="easyui-combobox" id="allOpera" editable="false" style="width:120px;height:25px;">
                    <option value="">----请选择----</option>
                    <option value="0">所有操作</option>
                    <option value="1">增加操作</option>
                    <option value="2">删除操作</option>
                    <option value="3">修改操作</option>
                    <option value="4">审核操作</option>
                    <option value="5">发布操作</option>
                    <option value="6">停止操作</option>
                    <option value="7">关闭终端操作</option>
                    <option value="8">重启终端操作</option>
                    <option value="9">更新终端操作</option>
                    <option value="10">导入Licens操作</option>
                    <option value="11">导出操作</option>
                    <option value="12">音量设置操作</option>
                    <option value="13">登陆</option>
                    <option value="14">开机</option>
                   </select>
                 </td>
			  </tr>
			</table>
			</div>
		</div>
		<!-- 操作日志列表 -->
		<div id="operalogBgDiv" style="height:82%;margin-top:35px;">
		  <table id="logdg" style="width:auto;width:100%;height:100%;"></table>
		</div>
		<!-- 查询素材信息 -->
		<div id="staticlogBgDiv" style="padding-top:5px;height:8%;width:100%;display: none;margin-bottom:35px;">
		   <div  style="width:1300px; height:80px; background:url(img/rizigbcolor.png) no-repeat;">
			<table style="padding-top:10px;padding-left:20px;">
			  <tr> 
			    <td rowspan="2"><font>素材名称：</font></td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="eleName"/></td>
				<td rowspan="2" align="left" style="color:white;font-size:15px;padding-left:8px;">终端名称：</td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="terminalName"/></td>
			    <td style="padding-left:10px;"><font>开始时间：</font></td>
			    <td rowspan="2">&nbsp;&nbsp;</td>
			    <td><input class="easyui-datetimebox" id="statictisstarttime" style="width:150px;height:25px;" editable="false"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/searchsc.png"  title="素材查询" style="cursor: pointer;" onclick="change('2')"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/searchSucai.png"  title="素材统计" style="cursor: pointer;" onclick="change('3')"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/cmsearch.png"  title="触摸信息查询" style="cursor: pointer;" onclick="change('4')"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/cmtj.png"  title="触摸信息统计查询" style="cursor: pointer;" onclick="change('5')"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/chongzhi.png" title="重置查询" style="cursor:pointer;" onclick="statisticsReset()"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;" id="delTD"><img src="img/del.png" title="删除" style="cursor:pointer;" onclick="statisticsDel()"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;" id="importtj"><img src="img/importplaylog.png" title="导出统计Excel表" style="cursor:pointer;" onclick="exportEleData()"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;" id="importsc"><img src="img/importplaylog.png" title="导出素材统计信息Excel表" style="cursor:pointer;" onclick="exportStatisticsTJData()"/></td><!--
			     <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/playduring.png" title="终端播放时长统计" style="cursor:pointer;" onclick="alert('功能尚未实现')"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/number.png" title="触摸点击次数统计" style="cursor:pointer;" onclick="alert('功能尚未实现')"/></td>
			  --></tr>
			  <tr>
			    <td style="padding-left:10px;"><font>结束时间：</font></td>
			    <td><input class="easyui-datetimebox" id="statictisendtime" editable="false" style="width:150px;height:25px;"/></td>
			  </tr>
			</table>
			</div>
		</div>
		<!-- 素材信息列表 -->
		<div id="staticlogTbDiv" style="height:82%;margin-top:35px;display: none;">
		  <table id="staticdg" style="width:auto;width:100%;height:100%;"></table>
		</div>
		
		<!-- 统计日志列表 -->
		<div id="staticlogTbTJ" style="height:82%;margin-top:35px;display: none;">
		  <table id="statictj" style="width:auto;width:100%;height:100%;"></table>
		</div>
		
		<!-- 触摸统计 -->
		<div id="CMBGDiv" style="padding-top:5px;height:8%;width:100%;display: none;margin-bottom:35px;">
		   <div  style="width:1300px; height:80px; background:url(img/rizigbcolor.png) no-repeat;">
			<table style="padding-top:10px;padding-left:20px;">
			  <tr> 
			    <td rowspan="2"><font>场景名称：</font></td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="sceneNameofButton"/></td>
			    <td rowspan="2"><font>跳转场景名称：</font></td>
			    <td rowspan="2"><input type="text" class="easyui-validatebox" id="sceneNameofJumpbutton"/></td>
				<td rowspan="2" align="left" style="color:white;font-size:15px;padding-left:8px;">按钮类型：</td>
			    <td rowspan="2">
			    <input id="buttonType"  class="easyui-combobox" style="width:135px;height:25px;"  data-options="url:'operationlog/buttontype.json',valueField: 'id',textField: 'text'"></td>
			    <td style="padding-left:10px;"><font>开始时间：</font></td>
			    <td rowspan="2">&nbsp;&nbsp;</td>
			    <td><input class="easyui-datetimebox" id="clickStarttime" style="width:150px;height:25px;" editable="false"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/searchsc.png"  title="素材信息查询查询" style="cursor: pointer;" onclick="change('2')"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/searchSucai.png"  title="素材统计查询"  style="cursor: pointer;" onclick="change('3')"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/cmsearch.png"  title="触摸信息查询" style="cursor: pointer;" onclick="change('4')"/></td>
                 <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/cmtj.png"  title="触摸信息统计查询" style="cursor: pointer;" onclick="change('5')"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;"><img src="img/chongzhi.png" title="重置查询" style="cursor:pointer;" onclick="statisticsReset()"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;" id="importCMCX"><img src="img/importplaylog.png" title="导出触摸信息Excel表" style="cursor:pointer;" onclick="exportCMExcel()"/></td>
			     <td rowspan="2" width="5%" style="padding-left:10px;" id="importCMTJ"><img src="img/importplaylog.png" title="导出触摸统计信息Excel表" style="cursor:pointer;" onclick="exportCMTJExcel()"/></td>
			  </tr>
			  <tr>
			    <td style="padding-left:10px;"><font>结束时间：</font></td>
			    <td><input class="easyui-datetimebox" id="clickEndtime" editable="false" style="width:150px;height:25px;"/></td>
			  </tr>
			</table>
			</div>
		</div>
		<!-- 触摸统计列表 -->
		<div id="CMDiv" style="height:82%;margin-top:35px;display: none;">
		  <table id="CMTable" style="width:auto;width:100%;height:100%;"></table>
		</div>
	</div>
	<!-- 素材下载窗体 -->
	<div id="downWin" class="easyui-window" title="导出Excel" data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:440px;height:380px;padding:10px;">
	    <table id="downTB" style="width:auto;width:100%;height:100%;"></table>
	</div>
	<!-- 触摸下载窗体 -->
	<div id="downWin2" class="easyui-window" title="导出Excel" data-options="draggable:false,modal:true,closed:true,resizable:false,collapsible:false,minimizable:false,maximizable:false,iconCls:'icon-save'" style="width:440px;height:380px;padding:10px;">
	    <table id="downTB2" style="width:auto;width:100%;height:100%;"></table>
	</div>
</body>
</html>