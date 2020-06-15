$(document).ready(function() {
	loadGrid();
	powerButtonShow();
	$.ajax({
		url : "proStatus/setStatus.do",
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			editProgram : false
		}
	});
	$("#istiming").bind('change',function(){
		if($('#istiming').is(':checked')) {
			$("#download_startTime").timespinner({'disabled':false,'value':'00:00:00'});
			$("#download_endTime").timespinner({'disabled':false,'value':'23:59:59'});
		}else{
			$("#download_startTime").timespinner({'disabled':true,'value':''});
			$("#download_endTime").timespinner({'disabled':true,'value':''});
		}
	});
});
var sendDate;
var index = 0; 
var action = '';
var stopRefreshTerminalGrid = '';
var stopRefreshDetailGrid = '';
var hasPass = false;	//ie走不动判断
function powerButtonShow() {
	$.ajax({
		url : "modulepower/queryModulePowerID.do",
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		async : false,
		success : function(result) {
			for (var i = 0; i < result.moduleList.length; i++) {
				if (result.moduleList[i] == 11) {
					$("#addButton").show();
				}
				if (result.moduleList[i] == 12) {
					$("#editButton").show();
				}
				if (result.moduleList[i] == 13) {
					$("#deleteButton").show();
				}
				if (result.moduleList[i] == 24) {
					$("#auditButton").show();
				}
				if (result.moduleList[i] == 27) {
					$("#sendButton").show();
					$("#stopButton").show();
				}
				if (result.moduleList[i] == 28) {
					$("#exportButton").show();
				}
				if (result.moduleList[i] == 34) {
					$("#exportButton1").show();
				}
			}
		}
	});
}
function stopTerminal() {
	var rows = $("#sendTerminalGrid").datagrid("getSelections");
	if (rows.length == 0) {
		ZENG.msgbox.show("请选择要停止终端!", 3, 1000);
		// $.messager.alert("提示信息","请选择要停止终端");
		return;
	}
	var programId = rows[0].program_id;
	var ids = new Array();
	for (i in rows) {
		ids.push(rows[i].terminal_id);
	}
	$.messager.defaults = { ok: "确定", cancel: "取消" };
	$.messager.confirm('确认对话框', '确定停止选择终端？', function(r) {
		if (r) {
			$.ajax({
				url : 'program/stopProgramTerminal.do',
				method : "POST",
				data : {
					"program_id" : programId,
					"terminal_id" : ids
				},
				dataType : 'json',
				success : function(data) {
					var count = $("#sendTerminalGrid").datagrid("getRows");
					if (!data.success) {
						ZENG.msgbox.show("操作失败，请联系系统管理员！", 5, 2000);
					} else {
						ZENG.msgbox.show("操作成功！", 4, 800);
						$("#sendTerminalGrid").datagrid("reload");
						if(count.length == 1 || rows.length == count.length){
							$("#sendTerminalWin").window("close");
							datarefresh();
						}
//						datarefresh();
//						var a = $("#sendTerminalGrid").datagrid("getRows");
//						var alength = a.length;
//						for ( var i = 0; i < alength; i++) {
//						var index = $('#sendTerminalGrid').datagrid('getRowIndex', a[i]);
//						$('#sendTerminalGrid').datagrid('deleteRow', index);
//						}
//						$("#sendTerminalGrid").datagrid("reload");
					}
				}
			});
			/* $.ajax({
							url : 'program/stopProgramTerminal.do',
							type : 'post',
							contentType : "application/x-www-form-urlencoded;charset=UTF-8",
							data : {
								"program_id" : program_id,
								"ids" : ids
							},
							success : function(data) {
								if (!data.success) {
									ZENG.msgbox.show(
											"操作失败，请联系系统管理员！",
											5, 2000);
								} else {
									$("#sendTerminalGrid")
											.datagrid("reload");
									ZENG.msgbox.show("操作成功！",
											4, 800);
								}
							}
						}); */
		}
	});
}

function dateToString(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	var o = date.getHours();
	var min = date.getMinutes();
	var s = date.getSeconds();
	if (o < 10) {
		o = '0' + o;
	}
	if (m < 10) {
		m = '0' + m;
	}
	if (d < 10) {
		d = '0' + d;
	}
	if (min < 10) {
		min = '0' + min;
	}
	if (s < 10) {
		s = '0' + s;
	}
	return y + '-' + m + '-' + d + ' ' + o + ':' + min + ':' + s;
}
var tid = '${sessionScope.user.user_id}';
function loadGrid() {
	//加载数据  
	$('#jmgl')
	.datagrid(
			{
				width : 'auto',
				striped : true,
				singleSelect : false,
				url : 'program/queryProgram.do',
				loadMsg : '数据加载中请稍后……',
				pagination : true,
				pageSize : 10,
				queryParams : {
					page : 1,
					rows : 10
				},
				fitColumns : true,
				method : 'POST',
				rownumbers : true,
				columns : [ [
				             {
				            	 field : '',
				            	 checkbox : true,
				            	 align : 'center',
				            	 title : ''
				             },
				             {
				            	 field : 'program_id',
				            	 //width : '100px',
				            	 hidden : "true",
				            	 align : 'center',
				            	 title : '终端ID'
				             },
				             {
				            	 field : 'name',
				            	 width : '15%',
				            	 align : 'center',
				            	 title : '节目名称',
				            	 formatter : function(value, row, index) {
				            		 var text = value;
				            		 if (value.length > 12) {
				            			 text = value.substring(0, 12)
				            			 + "...";
				            		 }
				            		 var htmlstr = "<p class='easyui-tooltip' title='"+value+"'>"
				            		 + text + "</p>"
				            		 return htmlstr;
				            	 }
				             },{
				            	 field : 'type',
				            	 align : 'center',
				            	 title : '节目类型',
				            	 formatter : function(value, row, index) {
				            		 return value=="0"?"普通":"互动";
				            	 }
				             },
				             {
				            	 field : 'startTime',
				            	 align : 'center',
				            	 title : '开始时间',
				            	 formatter : function(value, row, index) {
				            		 var date = new Date(value);
				            		 return dateToString(date);
				            	 }
				             },
				             {
				            	 field : 'endTime',
				            	 align : 'center',
				            	 title : '结束时间',
				            	 formatter : function(value, row, index) {
				            		 var date = new Date(value);
				            		 return dateToString(date);
				            	 }
				             },
				             {
				            	 field : 'audit_status',
				            	 //width : '6%',
				            	 align : 'center',
				            	 title : '审核状态',
				            	 formatter : function(value, row, index) {
				            		 if (value == 0) {
				            			 return "未审核";
				            		 } else if (value == 1) {
				            			 return "审核通过";
				            		 } else {
				            			 return "审核未通过";
				            		 }

				            	 }
				             },
				             {
				            	 field : 'isSend',
				            	 //width : '6%',
				            	 align : 'center',
				            	 title : '发布状态',
				            	 formatter : function(value, row, index) {
				            		 if (value == 1) {
				            			 return "已发布";
				            		 } else {
				            			 return "未发布";
				            		 }

				            	 }
				             },
				             {
				            	 field : 'updateTime',
				            	 //width : '15%',
				            	 align : 'center',
				            	 title : '更新日期',
				            	 formatter : function(value, row, index) {
				            		 var date = new Date(value);
				            		 return dateToString(date);
				            	 }
				             },
				             {
				            	 field : 'realname',
				            	 //width : '10%',
				            	 align : 'center',
				            	 title : '创建者名称'
				             },
				             /* {
										field : 'yulan',
										//width : '6%',
										align : 'center',
										title : '预览',
										formatter : function(value, row, index) {
											return '<a style="cursor:pointer;" onclick="showProgram('
													+ row.program_id
													+ ')">预览</a>';
										}
									}, */
				             {
				            	 field : 'terminal',
				            	 width : '100px',
				            	 align : 'center',
				            	 title : '发布终端',
				            	 formatter : function(value, row, index) {
				            		 var btn = '<a class="editcls" onclick="showProgram('
				            			 + row.program_id
				            			 + ','
				            			 + row.isSend
				            			 + ')" href="javascript:void(0)">查询终端</a>';
				            		 return btn;
				            	 }
				             },
				             {
				            	 field : 'url',
				            	 width : '150px',
				            	 align : 'center',
				            	 title : '操作',
				            	 formatter : function(value, row, index) {
				            		 var copyProgram = '<a class="copyprogram" onclick="copyProgram('+ row.program_id+ ')" href="javascript:void(0)">复制节目</a>';
				            		 var playProgram='<a class="copyprogram" onclick="playProgram('+ row.program_id +')" href="javascript:void(0)">预览</a>';
				            		 return copyProgram;
//				            		 return copyProgram+playProgram;
				            	 }
				             },
				             {
				            	 field : 'schedulelevel',
				            	 width : '100px',
				            	 align : 'center',
				            	 title : '发布权限',
				            	 hidden : true
				             } ] ],
				             onLoadSuccess : function(data) {
				            	 $('.editcls').linkbutton({
				            		 plain : true,
				            		 iconCls : 'icon-search'
				            	 });
				            	 $('.copyprogram').linkbutton({
				            		 plain : true,
				            		 iconCls : 'icon-copy'
				            	 });
				             }
			});
	var p = $('#jmgl').datagrid('getPager');
	$(p).pagination({
		pageList : [ 5, 10, 15 ],
		beforePageText : '第',//页数文本框前显示的汉字 
		afterPageText : '页    共 {pages} 页',
		showRefresh : false,
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}
//节目预览
function playProgram(programId){
	window.location.href="program/playProgram/"+programId;
}
function searchProgram() {
	var grid = $('#jmgl');
	var options = grid.datagrid('getPager').data("pagination").options;
	var curr = options.pageNumber;
	var programName = $('#searchName').val();
	programName = $.trim(programName);
	$("#jmgl").datagrid('load', {
		name : programName,
		page : 1,
		rows : 10
	});
	var p = $('#jmgl').datagrid('getPager');
	$(p).pagination({
		pageNumber : 1
	});
}
function showProgram(values, isSend) {
	if (isSend != 1) {
		ZENG.msgbox.show("选中的节目单未发布!", 5, 2000);
		return;
	}
	$("#sendTerminalWin").dialog("open");
	$('#sendTerminalGrid').datagrid('load', {
		"program_id" : values
	});
	stopRefreshTerminalGrid = setInterval("refreshTerminalGrid()", 5000);  
}
function refreshTerminalGrid(){
	var selections = $("#sendTerminalGrid").datagrid("getSelections");
	var rows = [];
	for ( var i = 0; i < selections.length; i++) {
		rows[i] = $('#sendTerminalGrid').datagrid('getRowIndex', selections[i]);
	}
	$("#sendTerminalGrid").datagrid("load");
	$("#sendTerminalGrid").datagrid({
		onLoadSuccess:function(data){
			for ( var i = 0; i < selections.length; i++) {
				$("#sendTerminalGrid").datagrid('selectRow',rows[i]);
			}
		}
	});
}
function datarefresh() {
	$('#searchName').textbox('setValue',"");
	$("#jmgl").datagrid('reload',{
		name : ""
	});
}
function stopRefresh(gridname){
	clearInterval(gridname);
}
//wangyulin 2014/12/18  program export
function exportProgram() {
	var urls = new Array();
	var selections = $('#jmgl').datagrid('getSelections');
	if (selections.length == 0) {
		ZENG.msgbox.show("请选择一个节目!", 3, 2000);
		return;
	} else if (selections.length > 10){
		ZENG.msgbox.show("只能一次导出10个节目单!", 3, 2000);
		return;
	} else {
		for (var i = 0; i < selections.length; i++) {
			if (selections[i].audit_status != 1) {
				ZENG.msgbox.show("包含未审核或审核不通过项，确认后重新导出!", 5, 2000);
				return;
			}
			urls[i] = selections[i].url;
		}

		$.ajax({
			url : 'program/isExitProgram.do',
			method : "POST",
			data : {
				"urls" : urls
			},
			dataType : 'json',
			success : function(data) {
				if (data.haveFile) {
					$('#exportWin').window({
						onClose : function() {
							datarefresh();
						}
					});
					$('#exportWin').window('open');
					var ids = [];
					for (var i = 0; i < selections.length; i++) {
						ids[i] = parseInt(selections[i].program_id);
					}
					$.ajax({
						url : 'element/queryFtpHttpMessage.do',
						method : "POST",
						dataType : 'json',
						success : function(data) {
							var info = window.location.hostname;
							var ftpip = data.configBeanList[1].config_value;
							var httpip = data.configBeanList[2].config_value;
							if(info == data.configBeanList[4].config_value){
								ftpip = data.configBeanList[4].config_value;
								httpip = data.configBeanList[5].config_value;
							}
							var temphtml = '<object id="plugin0" type="application/x-mmstools" width="600" height="300">'
								+ '<param name="type" value="exportPlayList" />'
								+ '<param name="proj" value="fyjt" />'
								+ '<param name="lang" value="ch" />'
								+ '<param name="debug" value="false" />'
								+ '<param name="ftpip" value="'+ftpip+'" />'
								+ '<param name="uploadport" value="'+data.configBeanList[6].config_value+'" />'
								+ '<param name="downloadport" value="'+data.configBeanList[0].config_value+'" />'
								+ '<param name="httpip" value="'+httpip+'" />'
								+ '<param name="httpport" value="'+data.configBeanList[3].config_value+'" />'
								+ '<param name="uploadpath" value="/" />'
								+ '<param name="playListIds" value="'+ids+'" />'
								+ '<param name="userid" value="'+tid+'" />'
								+ '</object>';
							setTimeout("downloadTool()",1500);                 //ie走不动
							$('#exportdiv').html(temphtml);
							hasPass = true;
							var temp = document.getElementById("plugin0");
							if(temp.version!="3.0.0.0"){
								$('#exportdiv').html("");
								$('#exportWin').window('close');
								$.messager.defaults = {
										ok : "是",
										cancel : "否"
								};
								$.messager
								.confirm(
										"信息提示",
										"检测到上传插件已更新,请点击是进行下载!安装过程请关闭杀毒软件，如遇阻止请点击允许!",
										function(data) {
											if (data) {
												var jumppath = "element/downLoadMmstools.do";
												var name = navigator.appName;
												if (checkBrowserVision() == 2)
													jumppath = "downLoadMmstools.do";
												window
												.open(jumppath);
											} else {
												return;
											}
										});
							}
							else{
								// $(".easyui-tabs").tabs('select', '媒体文件上传');
								setTimeout(
										function() {
											if (document.getElementById('plugin0') != undefined
													&& document
													.getElementById('plugin0').valid) {
											} else {
												$.messager.defaults = {
														ok : "是",
														cancel : "否"
												};
												$.messager
												.confirm(
														"信息提示",
														"检测到您浏览器未安装上传控件,请点击是下载!安装过程请关闭杀毒软件，如遇阻止请点击允许!",
														function(data) {
															if (data) {
																var jumppath = "element/downLoadMmstools.do";
																var name = navigator.appName;
																if (checkBrowserVision() == 2){
																	jumppath = "downLoadMmstools.do";
																}
																window
																.open(jumppath);
															} else {
																return;
															}
														});
											}
										}, 500)};
						}
					});
				} else {
					var names = data.fileName;
					var str = '';
					for (var i = 0; i < names.length; i++) {
						str = str + names[i];
						if (i != names.length - 1) {
							str = str + ',';
						}
					}
					ZENG.msgbox.show("播放单丢失文件或素材，请确认后重新导出！", 5,
							2000);
				}
			}
		});
	}
}

/**
 * 节目导入窗口
 */
function insertProgramType(){
	$("#exportWin1").window("open");
	/**
	 * 上传节目插件
	 */
	$(document).ready(function() {
		$("#file_upload").uploadify({

			//是否自动上传 true or false
			'auto':false,

			//超时时间上传成功后，将等待服务器的响应时间。
			//在此时间后，若服务器未响应，则默认为成功(因为已经上传,等待服务器的响应) 单位：秒
			'successTimeout':90,

			//附带值 JSON对象数据，将与每个文件一起发送至服务器端。
			//如果为动态值，请在onUploadStart()中使用settings()方法更改该JSON值
			'formData':{       //可以不写
				'userid':tid
			},  
			'onUploadStart': function(file) { 
				var userid=$('#userid').val();
				$("#file_upload").uploadify(
						"settings", 
						"formData", 
						{'userid':userid});
			}, 
			//flash
			'swf': "js/uplodify/uploadify.swf",

			//上传按钮名称
			'buttonText' : "选择文件",

			//文件选择后的容器div的id值 
			'queueID':'uploadfileQueue',

			//将要上传的文件对象的名称 必须与后台controller中抓取的文件名保持一致    
			'fileObjName':'pic',

			//上传地址
			'uploader':'../program/uploadFile.do',

			//浏览按钮的宽度
			'width':'100',

			//浏览按钮的高度
			'height':'32',

			//在浏览窗口底部的文件类型下拉菜单中显示的文本
			'fileTypeDesc':'支持的格式：',

			//允许上传的文件后缀
			'fileTypeExts':'*.zip',

			/*上传文件的大小限制允许上传文件的最大 大小。 这个值可以是一个数字或字 符串。
		              如果它是一个字符串，它接受一个单位(B, KB, MB, or GB)。
		              默认单位为KB您可以将此值设置为0 ，没有限制, 
		              单个文件不允许超过所设置的值 如果超过 onSelectError时间被触发*/
			'fileSizeLimit':'100MB',

			//允许上传的文件的最大数量。当达到或超过这个数字，onSelectError事件被触发。
			'queueSizeLimit' : 3,

			//选择上传文件后调用
			'onSelect' : function(file) {
				ZENG.msgbox.show("文件加载成功！", 1,
						3000);
			},

			'onFallback' : function() {//检测FLASH失败调用  
				ZENG.msgbox.show("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。", 5,
						3000); 
			},
			//返回一个错误，选择文件的时候触发
			'onSelectError':function(file, errorCode, errorMsg){
				switch(errorCode) {
				case -100:
					ZENG.msgbox.show("上传的文件数量已经超出系统限制的"
							+$('#file_upload').uploadify('settings','queueSizeLimit')+"个文件！" ,5,
							3000);
					break;

				case -110:
					ZENG.msgbox.show("文件 ["+file.name+"] 大小超出系统限制的"
							+$('#file_upload').uploadify('settings','fileSizeLimit')+"大小！" ,5,
							3000);
					break;

				case -120:
					ZENG.msgbox.show("文件 ["+file.name+"] 大小异常！" ,5,
							3000);
					break;

				case -130:
					ZENG.msgbox.show("文件 ["+file.name+"] 类型不正确！",5,
							3000);
					break;
				}
			},

			//上传到服务器，服务器返回相应信息到data里
			'onUploadSuccess':function(file, data, response){
				ZENG.msgbox.show(data,1,2000);
				//刷新节目
				datarefresh();
			},

			//当单个文件上传出错时触发
			'onUploadError': function (file, errorCode, errorMsg, errorString) { 
				ZENG.msgbox.show("上传失败",5,3000);
			} 
		});
	});
}
function addProgram() {
	var jumppath = "program/addProgram.jsp";
	var name = navigator.appName;
	if ("ActiveXObject" in window){
		jumppath = "addProgram.jsp";
	}
	window.location.href = jumppath;
}
function editProgram() {
	var row = $("#jmgl").datagrid("getSelections");
	if (row.length == 0) {
		ZENG.msgbox.show("请选择一个节目", 5, 2000);
		return;
	} else if (row.length > 1) {
		ZENG.msgbox.show("只能选择一个节目", 5, 2000);
		return;
	}
	var programId = row[0].program_id;
	$.ajax({
		url : 'program/checkProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"program_id" : programId
		},
		success : function(data) {
			if (!data.success) {
				ZENG.msgbox.show(data.msg, 5, 2000);
				return;
			}
			var jumppath = "program/addProgram.jsp?id=" + programId;
			var name = navigator.appName;
			if ("ActiveXObject" in window){
				jumppath = "addProgram.jsp?id=" + programId;
			}
			window.location.href = jumppath;
		}
	});

}
function deleteProgram() {
	var row = $("#jmgl").datagrid("getSelections");
	if (row.length == 0) {
		ZENG.msgbox.show("请先选择节目", 5, 2000);
		return;
	}
	var ids = new Array();
	for (var i = 0; i < row.length; i++) {
		ids[i] = row[i].program_id;
	}
	$.ajax({
		url : 'program/deleteProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"ids" : ids
		},
		success : function() {
			ZENG.msgbox.show("删除成功!", 4, 2000);
			$("#jmgl").datagrid('reload');
		}
	});
}
function checkNull(str) {
	if (str == null || str == undefined || str == "null" || str == ""
		|| str == undefined)
		return true;
	return false;
}
function convertToDateTime(s) {
	s = s.replace("-/g", "/");
	return new Date(s)
}
function checkSendData(postData) {
	postData.program_id = $("#program_id").val();
	postData.name = $("#programName").val();
	postData.startdate = $("#startdate").combo("getValue");
	postData.stopdate = $("#stopdate").combo("getValue");
	postData.isloop = true;
	postData.loops = new Array();
	postData.loops[0]=new Object();
	postData.loops[0].loop = new Array();
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	for ( var i = 0; i < rowcount.length; i++) {
		var days = "";
		var circleweek = (rowcount[i].circleweek).split(",");
		for ( var j = 0; j < circleweek.length; j++) {
			switch (circleweek[j]){ 
			case '一' : temp = 'Monday';  break;
			case '二' : temp = 'Tuesday';  break;
			case '三' : temp = 'Wednesday';  break;
			case '四' : temp = 'Thursday';  break;
			case '五' : temp = 'Friday';  break;
			case '六' : temp = 'Saturday';  break;
			case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		var temp = {
				'starttime':rowcount[i].starttime,
				'endtime':rowcount[i].endtime,
				'days':days.substring(0, days.length - 1)
		};
		postData.loops[0].loop.push(temp);
	}
	var tempstart = convertToDateTime(postData.startdate.replace("-","/").replace("-","/"));
	var tempend = convertToDateTime(postData.stopdate.replace("-","/").replace("-","/"));
	var nowDate = new Date();
	if (nowDate > tempend) {
		ZENG.msgbox.show("该节目单已经过期，如需发布请重新设置结束时间！", 3, 2000);
		return false;
	}
	if (tempstart > tempend) {
		ZENG.msgbox.show("开始时间不能早于结束时间！", 3, 2000);
		return false;
	}
	postData.loopstarttime = $("#day_startTime").val();
	postData.loopstoptime = $("#day_endTime").val();
	if (checkNull(postData.name)) {
		ZENG.msgbox.show("节目名称不能为空!", 5, 1000);
		return false;
	} else if (!checkProgramname(postData.name)) {
		ZENG.msgbox.show('节目名称不能包含‘\’,‘/’,‘:’,‘*’,‘?’,‘"’,‘<’,‘>’字符!',5, 1000);
		return false;	
	} else if (checkNull(postData.startdate)) {
		ZENG.msgbox.show("开始时间不能为空!", 5, 1000);
		return false;
	} else if (checkNull(postData.stopdate)) {
		ZENG.msgbox.show("结束时间不能为空!", 5, 1000);
		return false;
	} else if (checkNull(postData.loopstarttime)) {
		ZENG.msgbox.show("日循环开始时间不能为空!", 5, 1000);
		return false;
	} else if (checkNull(postData.loopstoptime)) {
		ZENG.msgbox.show("日循环结束时间不能为空!", 5, 1000);
		return false;
	}
	return true;
}
function checkProgramname(programName){
	var reg = new RegExp('^[^\\\\\\/:*?\\"<>|]+$');// 转义 \ 符号也不行
	if(reg.test(programName))
		return true;
	else
		return false;
}
function checkCopyData(postData) {
	postData.program_id = $("#program_id").val();
	postData.name = $("#programName").val();
	postData.startdate = $("#startdate").combo("getValue");
	postData.stopdate = $("#stopdate").combo("getValue");
	postData.isloop = true;
	postData.loops = new Array();
	postData.loops[0]=new Object();
	postData.loops[0].loop = new Array();
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	for ( var i = 0; i < rowcount.length; i++) {
		var days = "";
		var circleweek = (rowcount[i].circleweek).split(",");
		for ( var j = 0; j < circleweek.length; j++) {
			switch (circleweek[j]){ 
			case '一' : temp = 'Monday';  break;
			case '二' : temp = 'Tuesday';  break;
			case '三' : temp = 'Wednesday';  break;
			case '四' : temp = 'Thursday';  break;
			case '五' : temp = 'Friday';  break;
			case '六' : temp = 'Saturday';  break;
			case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		var temp = {
				'starttime':rowcount[i].starttime,
				'endtime':rowcount[i].endtime,
				'days':days.substring(0, days.length - 1)
		};
		postData.loops[0].loop.push(temp);
	}
	var tempstart = convertToDateTime(postData.startdate.replace("-","/").replace("-","/"));
	var tempend = convertToDateTime(postData.stopdate.replace("-","/").replace("-","/"));
	var nowDate = new Date();
//	if (checkZIFU(postData.name)) {
//	return false;
//	}
	if (checkNull(postData.name)) {
		ZENG.msgbox.show("节目名称不能为空！", 3, 2000);
		return false;
	}
	if (postData.name.length > 40) {
		ZENG.msgbox.show("节目名称不能大于40个字符", 3, 2000);
		return false;
	}
	if (tempstart > tempend) {
		ZENG.msgbox.show("开始时间不能早于结束时间！", 3, 2000);
		return false;
	}
	postData.loopstarttime = $("#day_startTime").val();
	postData.loopstoptime = $("#day_endTime").val();
	if (checkNull(postData.name)) {
		ZENG.msgbox.show("节目名称不能为空!", 5, 1000);
		return false;
	} else if (!checkProgramname(postData.name)) {
		ZENG.msgbox.show('节目名称不能包含‘\’,‘/’,‘:’,‘*’,‘?’,‘"’,‘<’,‘>’字符!',5, 1000);
		return false;	
	}  else if (checkNull(postData.startdate)) {
		ZENG.msgbox.show("开始时间不能为空!", 5, 1000);
		return false;
	} else if (checkNull(postData.stopdate)) {
		ZENG.msgbox.show("结束时间不能为空!", 5, 1000);
		return false;
	} else if (checkNull(postData.loopstarttime)) {
		ZENG.msgbox.show("日循环开始时间不能为空!", 5, 1000);
		return false;
	} else if (checkNull(postData.loopstoptime)) {
		ZENG.msgbox.show("日循环结束时间不能为空!", 5, 1000);
		return false;
	}
	postData.name = postData.name.replace("%", "\%").replace(" ", "");
	return true;
}
/*
 * 节目发送
 */
function send() {
	var postData = {};
	if (!checkSendData(postData)) {
		return;
	}
	var terminalIds = new Array();
	var checked = $("#sendtree").tree("getChecked"); // 得到选择的终端
	var ids = "";
	for (var i = 0; i < checked.length; i++) {
		if (checked[i].id < 40000) { // 去除终端组
			continue;
		} else {
			if (terminalIds.toString().indexOf(checked[i].id) > -1) { // 去掉重复的终端ID
				continue;
			} else {
				terminalIds.push(checked[i].id);
			}
		}
	}
	if (terminalIds.length == 0) {
		ZENG.msgbox.show("请先选择终端!", 3, 2000);
		return;
	}
	for (var i = 0; i < terminalIds.length; i++) {
		ids = ids + terminalIds[i] + ",";
	}
	if($("#download_startTime").val() != ""){
		postData.downloadStartTime=$("#download_startTime").val() + ":00";
		postData.downloadEndTime=$("#download_endTime").val() + ":00";
	}else{
		postData.downloadStartTime=$("#download_startTime").val();
		postData.downloadEndTime=$("#download_endTime").val();
	}
	var flag=validationTime(postData.downloadStartTime,postData.downloadEndTime);
	if (!flag) {
		ZENG.msgbox.show("节目下载开始结束时间不能等于或者小于开始时间！！",5,2000);
		return;
	}
	postData.ids = ids.substring(0, ids.length - 1);
	var daysEl = document.getElementsByName("days");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			switch (daysEl[i].value){ 
			case '一' : daysEl[i].value = 'Monday';  break;
			case '二' : daysEl[i].value = 'Tuesday';  break;
			case '三' : daysEl[i].value = 'Wednesday';  break;
			case '四' : daysEl[i].value = 'Thursday';  break;
			case '五' : daysEl[i].value = 'Friday';  break;
			case '六' : daysEl[i].value = 'Saturday';  break;
			case '日' : daysEl[i].value = 'Sunday';  break;
			} 
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if (checkNull(days)&&index != 1) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	postData.days = days;
	postData.pubpower = document.getElementById("pubpower").value;
	if(postData.loops[0].loop.length > 1){index = 1;}
	if(index == 1) {
		var rowcount = $('#highgradegridtable').datagrid('getRows');
		var circleweek = (rowcount[0].circleweek).split(",");
		days = "";
		for ( var i = 0; i < circleweek.length; i++) {
			switch (circleweek[i]){ 
			case '一' : temp = 'Monday';  break;
			case '二' : temp = 'Tuesday';  break;
			case '三' : temp = 'Wednesday';  break;
			case '四' : temp = 'Thursday';  break;
			case '五' : temp = 'Friday';  break;
			case '六' : temp = 'Saturday';  break;
			case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		postData.loopstarttime = rowcount[0].starttime;
		postData.loopstoptime = rowcount[0].endtime;
		postData.days = days.substring(0, days.length - 1);
	}else if(index == 0) {
		var temp = {
				'starttime':$("#day_startTime").val(),
				'endtime':$("#day_endTime").val(),
				'days':days
		};
		postData.loops[0].loop = [];
		postData.loops[0].loop.push(temp);
	}
	postData.hasChange = getChange(postData);
	if (postData.hasChange) {
		for ( var i = 0; i < postData.loops[0].loop.length; i++) {
			var tempe = postData.loops[0].loop[i].endtime.split(":");
			var temps = postData.loops[0].loop[i].starttime.split(":");
			var st = temps[0]*3600 + temps[1]*60 + temps[2];
			var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
			if (parseInt(st) > parseInt(se)){
				ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
				return;
			}
			if (postData.loops[0].loop[i].days == "" || postData.loops[0].loop[i].days == "undefined") {
				ZENG.msgbox.show("周循环不能为空!", 3, 2000);
				return;		
			}
		}
	}
	var timearr = [];
	for (var i = 0; i < postData.loops[0].loop.length; i++) {
		var ttemp = postData.loops[0].loop[i].starttime+"@"+postData.loops[0].loop[i].endtime+"@"+postData.loops[0].loop[i].days;
		timearr.push(ttemp);
	}
	var rows = $("#jmgl").datagrid("getSelections");
	var istouch = rows[0].type;
	if(istouch == 0)
		istouch = "false";
	else
		istouch = "true";
	$.ajax({
		url : 'program/queryIsConflict.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"program_id" : postData.program_id,
			"startdate" : postData.startdate,
			"stopdate" : postData.stopdate,
			"timearr" : timearr,
			"terminalIds" : terminalIds,
			"pubpower" : postData.pubpower,
			"istouch" : istouch
		},
		success : function(result) {
			if (!result.success) {
				ZENG.msgbox.show("查询节目冲突失败", 3, 2000);
				return;
			}
			if (result.isconflict) {
				$.messager.defaults = { ok: "确定", cancel: "取消" };
				$.messager.confirm('确认','以下终端：<br>'+result.conflictterminals+'<br>在该时间段已有节目单发布,需排队等候!是否继续发布?',function(r){    
					if (!r){    
						return;  
					} else {
						if(!$('#istiming').is(':checked')){
							isTerminalTiming(terminalIds,postData);
						}else{
							insertProgram(postData);
						}
					}   
				});  
			} else {
				if(!$('#istiming').is(':checked')){
					isTerminalTiming(terminalIds,postData);
				}else{
					insertProgram(postData);
				}
			}
		}
	});
}
function copy() {
	var postData = {};
	if (!checkCopyData(postData)) {
		return;
	}
	var daysEl = document.getElementsByName("days");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if (checkNull(days)) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	var daysEl = document.getElementsByName("days");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			switch (daysEl[i].value){ 
			case '一' : daysEl[i].value = 'Monday';  break;
			case '二' : daysEl[i].value = 'Tuesday';  break;
			case '三' : daysEl[i].value = 'Wednesday';  break;
			case '四' : daysEl[i].value = 'Thursday';  break;
			case '五' : daysEl[i].value = 'Friday';  break;
			case '六' : daysEl[i].value = 'Saturday';  break;
			case '日' : daysEl[i].value = 'Sunday';  break;
			} 
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if (checkNull(days)&&index != 1) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	postData.days = days;

	postData.pubpower = document.getElementById("pubpower").value;
	if(index == 1) {
		var rowcount = $('#highgradegridtable').datagrid('getRows');
		var circleweek = (rowcount[0].circleweek).split(",");
		days = "";
		for ( var i = 0; i < circleweek.length; i++) {
			switch (circleweek[i]){ 
			case '一' : temp = 'Monday';  break;
			case '二' : temp = 'Tuesday';  break;
			case '三' : temp = 'Wednesday';  break;
			case '四' : temp = 'Thursday';  break;
			case '五' : temp = 'Friday';  break;
			case '六' : temp = 'Saturday';  break;
			case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		postData.loopstarttime = rowcount[0].starttime;
		postData.loopstoptime = rowcount[0].endtime;
		postData.days = days.substring(0, days.length - 1);
	}else if(index == 0) {
		var temp = {
				'starttime':$("#day_startTime").val(),
				'endtime':$("#day_endTime").val(),
				'days':days
		};
		postData.loops[0].loop = [];
		postData.loops[0].loop.push(temp);
	}
	postData.hasChange = getChange(postData);
	if (postData.hasChange) {
		for ( var i = 0; i < postData.loops[0].loop.length; i++) {
			var tempe = postData.loops[0].loop[i].endtime.split(":");
			var temps = postData.loops[0].loop[i].starttime.split(":");
			var st = temps[0]*3600 + temps[1]*60 + temps[2];
			var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
			if (parseInt(st) > parseInt(se)){
				ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
				return;
			}
			if (postData.loops[0].loop[i].days == "" || postData.loops[0].loop[i].days == "undefined") {
				ZENG.msgbox.show("周循环不能为空!", 3, 2000);
				return;		
			}
		}
	}
	var jsonStr = JSON.stringify(postData);
	$.ajax({
		url : 'program/copyProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data :{
			"programJson" : jsonStr,
		},
		success : function(data) {
			if (!data.success) {
				ZENG.msgbox.show(data.msg, 3, 2000);
				return;
			}
			$('#sendProgramWin').dialog('close');
			ZENG.msgbox.show("拷贝成功!", 4, 2000);
			$("#jmgl").datagrid('reload');
		}
	});
}
function getChange(postData) {
	if (postData.name != sendDate.name) {
		return true;
	} else if (postData.startdate != sendDate.startdate) {
		return true;
	} else if (postData.stopdate != sendDate.stopdate) {
		return true;
	} else if (postData.loopstarttime != sendDate.loopstarttime) {
		return true;
	} else if (postData.loopstoptime != sendDate.loopstoptime) {
		return true;
	} else if (postData.days != sendDate.days) {
		return true;
	} else if (postData.loops[0].loop.length != sendDate.loops[0].loop.length) {
		return true;
	} else if(postData.downloadStartTime != sendDate.downloadStartTime){
		return true;
	} else if(postData.downloadEndTime != sendDate.downloadEndTime){
		return true;
	}  else if (postData.loops[0].loop.length == sendDate.loops[0].loop.length){
		for ( var i = 0; i < postData.loops[0].loop.length; i++) {
			if (postData.loops[0].loop[i].starttime !=  sendDate.loops[0].loop[i].starttime 
					|| postData.loops[0].loop[i].endtime !=  sendDate.loops[0].loop[i].endtime 
					|| postData.loops[0].loop[i].days !=  sendDate.loops[0].loop[i].days) {
				return true;
			}
		}
	}
	return false;
}
function copyProgram(program_id){
	$
	.ajax({
		url : 'program/getProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"program_id" : program_id
		},
		success : function(data) {
			var succ = data.success;
			if (!succ) {
				ZENG.msgbox.show(data.msg, 3, 2000);
				return;
			}
			var pro = data.programMap.twinflag.program[0];
			pro.name="";
			$("#programForm").form("load", pro);
			var days = pro.loopdesc.split(",");
			var tempJson = {
					days : days,
					program_id:program_id
			};
			sendDate = {
					name : pro.name,
					startdate : pro.startdate,
					stopdate : pro.stopdate,
					loopstarttime : pro.loopstarttime,
					loopstoptime : pro.loopstoptime,
					days : pro.loopdesc,
					loops : pro.loops,
					downloadStartTime : pro.downloadStartTime,
					downloadEndTime : pro.downloadEndTime
			};
			$
			.ajax({
				url : "role/queryScheduleLevelByRoleID.do",
				type : "POST",
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				async : false,
				success : function(result) {
					schedulelevel = result;
					document.getElementById("pubpower").length=0;
					for (var i = 1; i <= schedulelevel; i++) {
						$("#pubpower").append('<option value="'+i+'">'+i+'</option>'); 
					}
				}
			});	
			$("#programForm").form("load", tempJson);
			$("#copyProgramButton").css("display","inline");
			$("#sendProgramButton").css("display","none");
			$("#sendTreeDiv").css("display","none");
			$('#sendProgramWin').dialog('open');
			$("#sendProgramWin").panel("setTitle","复制节目");
			$('#pubpower').val(pro.pubpower);
			var getRows = $('#highgradegridtable').datagrid('getRows');
			var rowslength = getRows.length;
			for ( var i= 0; i < rowslength; i++) {
				var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', getRows[i]);
				$('#highgradegridtable').datagrid('deleteRow', rowIndex);  
			}
			if(pro.loops[0].loop.length > 1) {
				var loop = pro.loops[0].loop;
				index = 1;
				$("#highgradegrid").show();
				$("#lowgradeview").hide();
				$("#highlevelButton").hide();
				$("#lowlevelButton").show();
				$('#highgradegridtable').datagrid("resize",{  
					height:"210px",
					width:"310px"
				});
				for(var i = 0; i < loop.length; i++){
					var days = "";
					var day = loop[i].days.split(",");
					var dayslength = day.length;
					for ( var j = 0; j < dayslength; j++) {
						switch (day[j]){ 
						case 'Monday' : day[j] = '一';  break;
						case 'Tuesday' : day[j] = '二';  break;
						case 'Wednesday' : day[j] = '三';  break;
						case 'Thursday' : day[j] = '四';  break;
						case 'Friday' : day[j] = '五';  break;
						case 'Saturday' : day[j] = '六';  break;
						case 'Sunday' : day[j] = '日';  break;
						} 
						days = days + day[j] + ",";
					}
					$('#highgradegridtable').datagrid('insertRow',{
						row: {
							starttime: loop[i].starttime,
							endtime: loop[i].endtime,
							circleweek: days.substring(0, days.length - 1)
						}
					});
				}
			} else {
				index = 0;
				$("#highgradegrid").hide();
				$("#lowgradeview").show();
				$("#highlevelButton").show();
				$("#lowlevelButton").hide();
				$("#day_startTime").timespinner('setValue', pro.loopstarttime); 
				$("#day_endTime").timespinner('setValue', pro.loopstoptime); 
				$('#pubpower').val(pro.pubpower);
				var daysEl = document.getElementsByName("days");
				var daystr = pro.loopdesc.split(",");
				for(var i=0;i<daysEl.length;i++){
					daysEl[i].checked = false;
				}
				for(var i=0;i<daystr.length;i++){
					switch (daystr[i]){ 
					case 'Monday' : daysEl[0].checked = true;  break;
					case 'Tuesday' : daysEl[1].checked = true;  break;
					case 'Wednesday' : daysEl[2].checked = true;  break;
					case 'Thursday' : daysEl[3].checked = true;  break;
					case 'Friday' : daysEl[4].checked = true;  break;
					case 'Saturday' : daysEl[5].checked = true;  break;
					case 'Sunday' : daysEl[6].checked = true;  break;
					} 
				}
			}
		}
	});
}
function publish() {
	var row = $("#jmgl").datagrid("getSelections");
	if (row.length == 0) {
		ZENG.msgbox.show("请先选择节目", 3, 2000);
		return;
	} else if (row.length > 1) {
		ZENG.msgbox.show("一次只能发布一个节目", 3, 2000);
		return;
	}
	if (row[0].audit_status != 1) {
		ZENG.msgbox.show("只能选择审批通过的节目", 3, 2000);
		return;
	}
	var urls = new Array();
	urls[0] = row[0].url;
	$.ajax({
		url : 'program/isExitProgram.do',
		method : "POST",
		data : {
			"urls" : urls
		},
		dataType : 'json',
		success : function(data) {
			if (data.haveFile) {
//				if (checkNull(data.srclist) || data.srclist == 0) {
//				ZENG.msgbox.show("节目中没有任何素材，请验证后再发布！", 3, 2000);
//				return;
//				}
				getProgramMsg(row);
			} else {
				ZENG.msgbox.show("节目中缺失素材，请验证后再发布！", 3, 2000);
				return;
			}
		}
	});
}
function getProgramMsg(row) {
	$.ajax({
		url : 'program/getProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"program_id" : row[0].program_id
		},
		success : function(data) {
			var succ = data.success;
			if (!succ) {
				ZENG.msgbox.show(data.msg, 5, 2000);
				return;
			}
			var pro = data.programMap.twinflag.program[0];
			if (pro!=undefined&&pro!=null) {
				$("#download_startTime").timespinner("setValue",pro.downloadStartTime);
				$("#download_endTime").timespinner("setValue",pro.downloadEndTime);
			}
			$("#programForm").form("load", pro);
			var days = pro.loopdesc.split(",");
			var tempJson = {
					days : days,
					program_id : row[0].program_id
			};
			sendDate = {
					name : pro.name,
					startdate : pro.startdate,
					stopdate : pro.stopdate,
					loopstarttime : pro.loopstarttime,
					loopstoptime : pro.loopstoptime,
					days : pro.loopdesc,
					loops : pro.loops,
					downloadStartTime : pro.downloadStartTime,
					downloadEndTime : pro.downloadEndTime
			};
			$
			.ajax({
				url : "role/queryScheduleLevelByRoleID.do",
				type : "POST",
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				async : false,
				success : function(result) {
					schedulelevel = result;
					document.getElementById("pubpower").length=0;
					for (var i = 1; i <= schedulelevel; i++) {
						$("#pubpower").append('<option value="'+i+'">'+i+'</option>'); 
					}
				}
			});
			$("#programForm").form("load", tempJson);
			$("#sendProgramButton").css("display","inline");
			$("#copyProgramButton").css("display","none");
			$("#lowlevelButton").css("display","none");
			$("#sendTreeDiv").css("display","inline");

			if(pro.downloadStartTime != "" && pro.downloadStartTime != undefined){
				$("#download_startTime").timespinner({'disabled':false,'value':pro.downloadStartTime});
				$("#download_endTime").timespinner({'disabled':false,'value':pro.downloadEndTime});
				document.getElementsByName("istiming").item(0).checked=true;
			}else{
				$("#download_startTime").timespinner({'disabled':true,'value':''});
				$("#download_endTime").timespinner({'disabled':true,'value':''});
				document.getElementsByName("istiming").item(0).checked=false;
			}
			$('#sendProgramWin').dialog('open');
			$("#highgradegrid").hide();
			$("#lowgradeview").show();
			$("#highlevelButton").show();
			$('#pubpower').val(pro.pubpower);
			var pp = $('#pubpower').val();
			if(pp == null)
				$('#pubpower').val("1");
			$("#sendProgramWin").panel("setTitle","发布节目");
			$("#day_startTime").timespinner('setValue', pro.loopstarttime); 
			$("#day_endTime").timespinner('setValue', pro.loopstoptime); 
			var rows = $('#highgradegridtable').datagrid('getRows');
			var rowslength = rows.length;
			for ( var i= 0; i < rowslength; i++) {
				var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', rows[i]);
				$('#highgradegridtable').datagrid('deleteRow', rowIndex);  
			}
			var loop = pro.loops[0].loop;
			if(loop.length>1) {
				index = 1;
				$("#highgradegrid").show();
				$("#lowgradeview").hide();
				$("#highlevelButton").hide();
				$("#lowlevelButton").show();
				$('#highgradegridtable').datagrid("resize",{  
					height:"210px",
					width:"310px"
				});
				for(var i = 0; i < loop.length; i++){
					var days = "";
					var day = loop[i].days.split(",");
					var dayslength = day.length;
					for ( var j = 0; j < dayslength; j++) {
						switch (day[j]){ 
						case 'Monday' : day[j] = '一';  break;
						case 'Tuesday' : day[j] = '二';  break;
						case 'Wednesday' : day[j] = '三';  break;
						case 'Thursday' : day[j] = '四';  break;
						case 'Friday' : day[j] = '五';  break;
						case 'Saturday' : day[j] = '六';  break;
						case 'Sunday' : day[j] = '日';  break;
						} 
						days = days + day[j] + ",";
					}
					$('#highgradegridtable').datagrid('insertRow',{
						row: {
							starttime: loop[i].starttime,
							endtime: loop[i].endtime,
							circleweek: days.substring(0, days.length - 1)
						}
					});
				}
			}
			$('#sendtree')
			.tree(
					{
						checkbox : true, // 定义是否在每个节点前边显示 checkbox
						animate : true, // 定义当节点展开折叠时是否显示动画效果
						lines : false,
						url : 'scrollingnews/queryTree.do',
						cache : true, // cache必须设置为false,意思为不缓存当前页，否则更改权限后绑定的权限还是上一次的操作结果
						onLoadSuccess : function() {
							var nodes = $('#sendtree')
							.tree('getChildren');
							for (i = 0; i < nodes.length; i++) {
								if (nodes[i].id >= 40000) {
									$('#sendtree')
									.tree(
											'update',
											{
												target : nodes[i].target,
												iconCls : 'tree-terminal'
											});
								} else {
									$('#sendtree')
									.tree(
											'update',
											{
												target : nodes[i].target,
												iconCls : 'tree-group'
											});
								}
							};
							$.ajax({
								url : 'program/querySendP_T.do',
								type : 'post',
								contentType : "application/x-www-form-urlencoded;charset=UTF-8",
								data : {
									"program_id" : row[0].program_id
								},
								success : function(
										data) {
									var tempList = data.rows;
									for (var i = 0; i < tempList.length; i++) {
										var node = $('#sendtree').tree(
												'find',
												tempList[i].terminal_id);
										$('#sendtree').tree(
												'check',
												node.target);
									}
								}
							});
						},
						onSelect : function(node) {
							if (node.checked) {
								$('#sendtree').tree('uncheck',node.target);
							} else {
								$('#sendtree').tree('check',node.target);
							}
						}
					});
			index = 0;
			var days = pro.loopdesc.split(",");
			var daysEl = document.getElementsByName("days");
			for(var i=0;i<daysEl.length;i++){
				daysEl[i].checked = false;
			}
			for(var i=0;i<days.length;i++){
				switch (days[i]){ 
				case 'Monday' : daysEl[0].checked = true;  break;
				case 'Tuesday' : daysEl[1].checked = true;  break;
				case 'Wednesday' : daysEl[2].checked = true;  break;
				case 'Thursday' : daysEl[3].checked = true;  break;
				case 'Friday' : daysEl[4].checked = true;  break;
				case 'Saturday' : daysEl[5].checked = true;  break;
				case 'Sunday' : daysEl[6].checked = true;  break;
				} 
			}	
		}
	});
}
function stop() {
	var row = $("#jmgl").datagrid("getSelections");
	var ids = new Array();
	for (var i = 0; i < row.length; i++) {
		ids[i] = row[i].program_id;
	}
	$.ajax({
		url : 'program/stopProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"ids" : ids
		},
		success : function() {
			ZENG.msgbox.show("停止成功!", 4, 2000);
			$("#jmgl").datagrid('reload');
		}
	});
}
function showStopWin() {
	var row = $("#jmgl").datagrid("getSelections");
	if (row.length == 0) {
		ZENG.msgbox.show("请先选择节目", 3, 2000);
		return;
	}
	for (var i = 0; i < row.length; i++) {
		if (row[i].isSend != 1) {
			ZENG.msgbox.show("不能选择未发布的节目", 3, 2000);
			return;
		}
	}
	$.messager.defaults = { ok: "确定", cancel: "取消" };
	$.messager.confirm('确认对话框', '确定停止选择节目单？', function(r) {
		if (r) {
			stop();
		}
	});
}
function checkProgramName() {
	var name = $("#programName").val();
	var program_id = $("#program_id").val();
	$.ajax({
		url : 'program/checkProgramName.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"id" : program_id,
			"name" : name
		},
		success : function(data) {
			var succ = data.success;
			if (succ) {
				ZENG.msgbox.show("该节目名称已经存在！", 3, 2000);
				$("#programName").focus();
			}
		}
	});
}
function deleteProgramCheck() {
	var row = $("#jmgl").datagrid("getSelections");
	if (row.length == 0) {
		ZENG.msgbox.show("请先选择节目", 3, 2000);
		return;
	}
	for (var i = 0; i < row.length; i++) {
		if (row[i].isSend == 1 || row[i].isSend == "1") {
			ZENG.msgbox.show("不能删除已经发布的节目单", 3, 2000);
			return;
		}
	}
	$.messager.defaults = { ok: "确定", cancel: "取消" };
	$.messager.confirm('确认对话框', '确定删除选择数据？', function(r) {
		if (r) {
			deleteProgram();
		}
	});
}
function shoAuditWin() {
	var row = $("#jmgl").datagrid("getSelections");
	if (row.length == 0) {
		ZENG.msgbox.show("请先选择节目", 3, 2000);
		return;
	}
	for (var i = 0; i < row.length; i++) {
		if (row[i].audit_status != 0) {
			ZENG.msgbox.show("不能选择已审批的项", 3, 2000);
			return;
		}
	}
	$.messager.defaults = {
			ok : "审核通过",
			cancel : "审核不通过"
	};
	$.messager.confirm("操作提示", "审核节目,审核后将不能更改，请慎重选择！", function(data) {
		if (data) {
			audit(1);
		} else {
			audit(2);
		}
	});
}
function audit(auditStatus) {
	var row = $("#jmgl").datagrid("getSelections");
	var ids = new Array();
	for (var i = 0; i < row.length; i++) {
		ids[i] = row[i].program_id;
	}
	$.ajax({
		url : 'program/auditProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"auditStatus" : auditStatus,
			"ids" : ids
		},
		success : function(data) {
			var succ = data.success;
			if (succ) {
				ZENG.msgbox.show("审核成功！", 4, 2000);
				$("#jmgl").datagrid('reload');
			} else {
				ZENG.msgbox.show("审核出错啦！", 3, 2000);
			}
		}
	});
}
//function openLogWin(programid){
//$("#showProgramLogGrid").datagrid('load', {    
//program_id: programid,
//});  
//$("#showProgramLogWin").dialog("open");
//}
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
function highset() {
	index = 1;                      //高级设置
	$('#lowgradeview').hide();
	$('#highgradegrid').show();
	$("#highlevelButton").css("display","none");
	$("#lowlevelButton").css("display","inline");
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	$('#highgradegridtable').datagrid("resize",{  
		height:"210px",
		width:"310px"
	});
	var day_startTime = $("#day_startTime").val();
	var day_endTime = $("#day_endTime").val();
	var daysEl = document.getElementsByName("days");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			switch (daysEl[i].value){ 
			case 'Monday' : daysEl[i].value = '一';  break;
			case 'Tuesday' :  daysEl[i].value = '二';  break;
			case 'Wednesday' : daysEl[i].value = '三';  break;
			case 'Thursday' : daysEl[i].value = '四';  break;
			case 'Friday' : daysEl[i].value = '五';  break;
			case 'Saturday' :daysEl[i].value = '六';  break;
			case 'Sunday' : daysEl[i].value = '日';  break;
			} 
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if(rowcount.length == 0){
		$('#highgradegridtable').datagrid('insertRow',{
			row: {
				starttime: day_startTime,
				endtime: day_endTime,
				circleweek: days
			}
		});
	}else{
		$('#highgradegridtable').datagrid('updateRow',{
			index: 0,
			row: {
				starttime:  day_startTime,
				endtime: day_endTime,
				circleweek: days
			}
		});
	}
}
function lowset() {
	index = 0;                       //简易设置
	$('#highgradegrid').hide();
	$('#lowgradeview').show();
	$("#lowlevelButton").css("display","none");
	$("#highlevelButton").css("display","inline");
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	var temp = rowcount.length; 
	if(temp > 0) {
		$("#day_startTime").timespinner('setValue', rowcount[0].starttime); 
		$("#day_endTime").timespinner('setValue', rowcount[0].endtime); 
		var daysEl = document.getElementsByName("days");
		var daystr = rowcount[0].circleweek.split(",");
		for(var i=0;i<daysEl.length;i++){
			daysEl[i].checked = false;
		}
		for(var i=0;i<daystr.length;i++){
			switch (daystr[i]){ 
			case '一' : daysEl[0].checked = true;  break;
			case '二' : daysEl[1].checked = true;  break;
			case '三' : daysEl[2].checked = true;  break;
			case '四' : daysEl[3].checked = true;  break;
			case '五' : daysEl[4].checked = true;  break;
			case '六' : daysEl[5].checked = true;  break;
			case '日' : daysEl[6].checked = true;  break;
			} 
		}
	}
}
function addloop() {
	$('#addloopWin').window('open');
	$("#loop_days").timespinner('setValue', "00:00:00"); 
	$("#loop_daye").timespinner('setValue', "23:59:59"); 
	var daysEl = document.getElementsByName("loopdays");
	for(var i=0;i<7;i++){
		daysEl[i].checked = true;
	}
}
function editloop() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	if(rows.length != 1) {
		ZENG.msgbox.show("请选择一条数据", 3, 2000);
		return;
	}	
	$('#editloopWin').window('open');
	$("#edloop_days").timespinner('setValue', rows[0].starttime); 
	$("#edloop_daye").timespinner('setValue', rows[0].endtime); 
	var daysEl = document.getElementsByName("edloopdays");
	var daystr = rows[0].circleweek.split(",");
	for(var i=0;i<daysEl.length;i++){
		daysEl[i].checked = false;
	}
	for(var i=0;i<daystr.length;i++){
		switch (daystr[i]){ 
		case '一' : daysEl[0].checked = true;  break;
		case '二' : daysEl[1].checked = true;  break;
		case '三' : daysEl[2].checked = true;  break;
		case '四' : daysEl[3].checked = true;  break;
		case '五' : daysEl[4].checked = true;  break;
		case '六' : daysEl[5].checked = true;  break;
		case '日' : daysEl[6].checked = true;  break;
		} 
	}
}
function delloop() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	if(rows.length < 1) {
		ZENG.msgbox.show("请选择要删除的数据", 3, 2000);
		return;
	}
	for ( var i= 0; i < rows.length; i++) {
		var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', rows[i]);
		$('#highgradegridtable').datagrid('deleteRow', rowIndex);  
	}
}
function loopsave() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	var loop_days = $("#loop_days").val();
	var loop_daye = $("#loop_daye").val();
	var daysEl = document.getElementsByName("loopdays");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			switch (daysEl[i].value){ 
			case 'Monday' : daysEl[i].value = '一';  break;
			case 'Tuesday' :  daysEl[i].value = '二';  break;
			case 'Wednesday' : daysEl[i].value = '三';  break;
			case 'Thursday' : daysEl[i].value = '四';  break;
			case 'Friday' : daysEl[i].value = '五';  break;
			case 'Saturday' :daysEl[i].value = '六';  break;
			case 'Sunday' : daysEl[i].value = '日';  break;
			} 
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if (checkNull(days)) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	var tempe = loop_daye.split(":");
	var temps = loop_days.split(":");
	var st = temps[0]*3600 + temps[1]*60 + temps[2];
	var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
	if (parseInt(st) > parseInt(se)){
		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
		return;
	}
	$('#highgradegridtable').datagrid('insertRow',{
		row: {
			starttime: loop_days,
			endtime: loop_daye,
			circleweek: days
		}
	});
	$('#addloopWin').window('close');
}
function edloopsave() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', rows[0]);
	var edloop_days = $("#edloop_days").val();
	var edloop_daye = $("#edloop_daye").val();
	var daysEl = document.getElementsByName("edloopdays");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			switch (daysEl[i].value){ 
			case 'Monday' : daysEl[i].value = '一';  break;
			case 'Tuesday' :  daysEl[i].value = '二';  break;
			case 'Wednesday' : daysEl[i].value = '三';  break;
			case 'Thursday' : daysEl[i].value = '四';  break;
			case 'Friday' : daysEl[i].value = '五';  break;
			case 'Saturday' :daysEl[i].value = '六';  break;
			case 'Sunday' : daysEl[i].value = '日';  break;
			} 
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if (checkNull(days)) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	var tempe = edloop_daye.split(":");
	var temps = edloop_days.split(":");
	var st = temps[0]*3600 + temps[1]*60 + temps[2];
	var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
	if (parseInt(st) > parseInt(se)){
		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
		return;
	}
	$('#highgradegridtable').datagrid('updateRow',{
		index: rowIndex,
		row: {
			starttime: edloop_days,
			endtime: edloop_daye,
			circleweek: days
		}
	});
	$('#editloopWin').window('close');
}
function doDiv(value, row, index){
	var btn = '<a onclick="showDetail(\''
		+ row.mac+'_'+row.program_id+'_'+row.program_status
		+ '\')"  href="javascript:void(0)">查看详情</a>';
	return btn; 
}
function showDetail(mac_pids_status){
	$("#showDetailWin").dialog("open");
	$('#showDetailGrid').datagrid('load', {
		"mac_pids_status" : mac_pids_status
	});
	$('#showDetailGrid').datagrid({
		onLoadSuccess: function(){
			getData();
		}
	});
	stopRefreshDetailGrid = setInterval("refreshDetailGrid()", 2000);  
}
function refreshDetailGrid(){
	$('#showDetailGrid').datagrid('reload');
}
function getData(){
	var all = $("#showDetailGrid").datagrid('getData');
	if(all.totalnum == null){
		all.totalnum = 0;
	}
	if(all.downloadnum == null){
		all.downloadnum = 0;
	}
	var undownnum = parseInt(all.totalnum) - parseInt(all.downloadnum);
	$("#numdiv").empty();
	$("#numdiv").append("&nbsp;素材总个数:"+all.totalnum+"个,已下载："+all.downloadnum+"个,未下载："+undownnum+"个");

}
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
function isTerminalTiming(terminalIds,postData){
	$.ajax({
		url : 'program/queryTerminalIsTiming',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"terminalids" : terminalIds
		},
		success : function(data) {
			var terminalCloseTime = data.terminalCloseTime;
			var str = "";
			if(terminalCloseTime.length > 0){
				var closetimearr = [];
				for (var i = 0; i < terminalCloseTime.length; i++) {
					var ishave = false;
					for (var j = 0; j < closetimearr.length; j++) {
						if(closetimearr[j].id == terminalCloseTime[i].terminalId){
							closetimearr[j].time += ',' + terminalCloseTime[i].startTime + '-' + terminalCloseTime[i].endTime;
							ishave = true;
							break;
						}
					}
					if(!ishave){
						closetimearr.push({
							"id":terminalCloseTime[i].terminalId,
							"name":terminalCloseTime[i].terminal_name,
							"time":terminalCloseTime[i].startTime + '-' + terminalCloseTime[i].endTime
						});
					}
				}
				for (var i = 0; i < closetimearr.length; i++) {
					str += "</br><span style='color:blue;'>" + closetimearr[i].name + "</span>终端已设置下载时间：" + closetimearr[i].time;
				}
			}
			if(str != ""){
				$.messager.confirm('确认',str + '!是否继续发布?',function(r){    
					if (!r){    
						return;  
					} else {
						insertProgram(postData);
					}
				});
			}else{
				insertProgram(postData);
			}
		}
	});
}
function insertProgram(postData){
	var jsonStr = JSON.stringify(postData);
	$.ajax({
		url : 'program/sendProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data :{
			"programJson" : jsonStr,
		},
		success : function(data) {
			if (!data.success) {
				ZENG.msgbox.show(data.msg, 3, 2000);
				return;
			}
			$('#sendProgramWin').dialog('close');
			ZENG.msgbox.show("发布成功!", 4, 2000);
			$("#jmgl").datagrid('reload');
		}
	});
}
/*
 * 时间验证
 */
function validationTime(startTime,endTime){
	if(startTime == '' && endTime == ''){
		return true;
	}
	startTime=new Date("2017/01/04 "+startTime).getTime();
	endTime=new Date("2017/01/04 "+endTime).getTime();
	if (endTime>startTime) {
		return true;
	}else{
		return false;
	}
}
/**
 * ie无法下载 暂时修改
 */
function downloadTool(){
	if(!hasPass){
		$('#exportdiv').html("");
		$('#exportWin').window('close');
		$.messager.defaults = {
				ok : "是",
				cancel : "否"
		};
		$.messager.confirm(
				"信息提示",
				"检测到您浏览器未安装上传控件,请点击是下载!安装过程请关闭杀毒软件，如遇阻止请点击允许!",
				function(data) {
					if (data) {
						var jumppath = "element/downLoadMmstools.do";
						var name = navigator.appName;
						if (checkBrowserVision() == 2){
							jumppath = "downLoadMmstools.do";
						}
						window
						.open(jumppath);
					} else {
						return;
					}
				});
	}
}
