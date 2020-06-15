var task1;
var task2;
var task3;
//刷新
function refresh(){
	$("#zdjk").datagrid("reload");
	$('#tt').tree('reload'); 
}
$(document).ready(function(){
		$('#filterCondition').combobox({
			onChange: function(){
				var filter = $('#filterCondition').combobox('getValue');
				var sys = $('#sysCondition').combobox('getValue');
				if (filter=='全部') {
					filter = 10;
				}
				if (sys=='全部') {
					sys = 10;
				}
				loadGrid(filter,sys);
			}
		});
		$('#sysCondition').combobox({
			onChange: function(){
				var sys = $('#sysCondition').combobox('getValue');
				var filter = $('#filterCondition').combobox('getValue');
				if (filter=='全部') {
					filter = 10;
				}
				if (sys=='全部') {
					sys = 10;
				}
				loadGrid(filter,sys);
				
			}
		});
		$('#filterCondition').combobox('setValue', '全部');
		$('#sysCondition').combobox('setValue', '全部');
    	loadGrid();  
    	task1 = setInterval("refreshData()", 5000);  
	});
var myview = $.extend({},$.fn.datagrid.defaults.view,{
    onAfterRender:function(target){
        $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
       var opts = $(target).datagrid('options');
        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
        vc.children('div.datagrid-empty').remove();
        if (!$(target).datagrid('getRows').length){
            var d = $('<div class="datagrid-empty"></div>').html(opts.emptyMsg || 'no records').appendTo(vc);
            d.css({
               position:'absolute',
              left:0,
               top:50,
               width:'100%',
               textAlign:'center'
           });
       }
   }
});
	function loadGrid(filter,sys)  
{  
    //加载数据  
    $('#zdjk').datagrid({  
                width: 'auto',                
                striped: true, 
                singleSelect : false,  
                url:'terminal/queryTerminalStatusByUserId.do',
                loadMsg:'数据加载中请稍后……',  
                pagination: true,
                pageSize:10,
                queryParams:{page:1,rows:10,filter:filter,system:sys},
                method:'POST',
                rownumbers: true,     
                columns:[[  
{field:'',checkbox:true,align:'center',title: ''},
{field:'terminalStatus',width : '8%',align:'left',title: '<font color="#333333"><b>播放状态',formatter:function(value,row,index){
    if(value==0){
      	return "<img src=\"img/red.jpg\" height=17px width=17px />未连接";
    }else if(value==1){
    	return "<img src=\"img/green.jpg\" height=17px width=17px/>播放中";
    }else{
    	return "<img src=\"img/red.jpg\" height=17px width=17px/>未连接";
    }
  
    }}, 
{field:'terminal_id',hidden:"true",align:'center',title: 'id2'}, 
{field:'terminal_name',width : '15%',align:'center',title: '<font color="#333333"><b>终端名称</b></font>'}, 
{field:'mac',hidden:"true",align:'center',title: '<font color="#333333"><b>终端唯一标识</b></font>'}, 
{field:'terminalJudge',hidden:"true",align:'center',title: '<font color="#333333"><b>所属终端组ID</b></font>'}, 
{field:'sysVersion',width : '10%',align:'center',title: '<font color="#333333"><b>操作系统版本</b></font>'}, 
{field:'softVersion',width : '10%',align:'center',title: '<font color="#333333"><b>软件版本</b></font>'}, 
{field:'cpuUsage',width : '8%',align:'center',title: '<font color="#333333"><b>CPU使用率</b></font>',formatter:function(value,row,index){
    return value+"%";
    }},
{field:'memUsage',width : '8%',align:'center',title: '<font color="#333333"><b>内存使用率</b></font>',formatter:function(value,row,index){
    return value+"%";
    }},
{field:'diskUsage',width : '10%',align:'center',title: '<font color="#333333"><b>磁盘空间使用率</b></font>',formatter:function(value,row,index){
    return value+"%";
    }},
{field:'runTimeLength',width : '8%',align:'center',title: '<font color="#333333"><b>播放时长</b></font>',formatter:function(value,row,index){
    return value+"分钟";
    }},   
{field:'updateTime',align:'center',width : '20%',title: '<font color="#333333"><b>更新日期</b></font>',formatter:function(value,row,index){
    var unixTimestamp = new Date(value);
    return unixTimestamp.toLocaleString();
    }}                                                          
                ]]  
            });  
    
    
    var pagers = $('#zdjk').datagrid('getPager'); 
    $(pagers).pagination({ 
    	pageList:[5,10,15],
        beforePageText: '第',//页数文本框前显示的汉字 
        afterPageText: '页    共 {pages} 页', 
        showRefresh:false,
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });  

    
   

    
    $('#tt').tree({  
        checkbox: false, 
        animate:true,  
        lines:true,  
        url:'terminal/queryTerminalTree.do',
//        url:'terminal/selectTerminalTreeByUserId.do',
        cache:false,
        onLoadSuccess: function(node,data){
			var nodes =$('#tt').tree('getChildren');  
			for(var i=0;i<nodes.length;i++){
					$('#tt').tree('update', {
			            target: nodes[i].target,
			            iconCls: 'tree-group'
			        });
			   };
	},
        onClick:function(node){  
        	var terminalGroupId = $('#tt').tree('getSelected').id;  
        	var filter = $('#filterCondition').combobox('getValue');
			var sys = $('#sysCondition').combobox('getValue');
        	if(filter == "全部")
        		filter = 10;
			if(sys == "全部")
				sys = 10;
			var p = $('#zdjk').datagrid('getPager'); 
    			$(p).pagination({
                                pageNumber: 1
                            }); 
        	$("#zdjk").datagrid('reload',{"terminalGroupId":terminalGroupId,"page":1,"rows":10,"filter":filter,"system":sys});
		}
    });  
}  
	
	$(function(){
		$('#pp1').tooltip({
			position: 'right',
			content: '<span style="color:#fff">查看</span>',
			onShow: function(){
				$(this).tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
			}
		});
	});
	
	
	function getValue(value,row,index){
		return value+"%";
	}

	
function screenView(){
	var terminalStatue= $('#zdjk').datagrid('getSelections');
	if(terminalStatue.length==0){
		ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}
	$('#screen').window('open');
	$('#screen').html("");
	var mac = [];
	for(var i=0;i<terminalStatue.length;i++){
		mac[i] = terminalStatue[i].mac+"&&&&"+terminalStatue[i].terminal_name;
		}
	var queryScreen = {"mac":mac};
	task2 = setInterval("refreshView()", 10000);
	$.ajax({ 
    	url: "terminal/queryScreenView.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: queryScreen,
    	success: function(data){
    		for(var i=0;i<data.length;i++){
    			var path;
    			var flag=0;
    			for(var j=0;j<terminalStatue.length;j++){
    				if(data[i].mac==terminalStatue[j].mac){
    					if(terminalStatue[j].terminalStatus!=1){
    						flag++;
    					}
    					
    				}
    			}
    			if(flag==0){
	    			if(data[i].path==0){
	    				path = "img/0.jpg";
	    			}else{
	    				path = "/fyjt/ftpFile/screen/"+ data[i].mac+'/'+data[i].path;
	    			}
    			}else{
    				path = "img/0.jpg";
    			}
    			if(terminalStatue[i].terminalStatus == 0){
    				var html = '<div style="border:inset 4px #EEEEEE;float:left;margin-left:10px; width:153px; background-color:#EEEEEE"><p align="center"><strong>终端名称'
					+ '</strong><br/>&nbsp;&nbsp;<font color=blue face="微软雅黑">'
					+ data[i].terminalName
					+ '</font></p><p align="center">未连接</p></br><div align="center"><a id="large'+data[i].mac+'" data-strip-group-options="side:\'top\'" href="'+path+'" class="strip" data-strip-caption="'+data[i].terminalName+'&&&&'+data[i].mac+'"><img id="sim'+data[i].mac+'" onclick="openView(/fyjt/ftpFile/screen/'+data[i].mac+'/'+data[i].path+','+data[i].terminalName+')" width="145" height="100" src="'
					+ path
					+ '"/></a></div></div>';
    			    $("#screen").append(html);
    			}else{
    				if(data[i].program_name == ""){
    					var html = '<div style="border:inset 4px #EEEEEE;float:left;margin-left:10px; width:153px; background-color:#EEEEEE"><p align="center"><strong>终端名称'
						+ '</strong><br/>&nbsp;&nbsp;<font color=blue face="微软雅黑">'
						+ data[i].terminalName
						+ '</font></p><p align="center"><br></p></br><div align="center"><a id="large'+data[i].mac+'" data-strip-group-options="side:\'top\'" href="'+path+'" class="strip" data-strip-caption="'+data[i].terminalName+'&&&&'+data[i].mac+'"><img id="sim'+data[i].mac+'" onclick="openView(/fyjt/ftpFile/screen/'+data[i].mac+'/'+data[i].path+','+data[i].terminalName+')" width="145" height="100" src="'
						+ path
						+ '"/></a></div></div>';
						$("#screen").append(html);
    				}else{
		    			var html = '<div style="border:inset 4px #EEEEEE;float:left;margin-left:10px; width:153px; background-color:#EEEEEE"><p align="center"><strong>终端名称'
							+ '</strong><br/>&nbsp;&nbsp;<font color=blue face="微软雅黑">'
							+ data[i].terminalName
							+ '</font></p><p align="center">'
							+ data[i].program_name
							+ '</p></br><div align="center"><a id="large'+data[i].mac+'" data-strip-group-options="side:\'top\'" href="'+path+'" class="strip" data-strip-caption="'+data[i].terminalName+'&&&&'+data[i].mac+'"><img id="sim'+data[i].mac+'" onclick="openView(/fyjt/ftpFile/screen/'+data[i].mac+'/'+data[i].path+','+data[i].terminalName+')" width="145" height="100" src="'
							+ path
							+ '"/></a></div></div>';
		    			$("#screen").append(html);
    				}
    			}
    		}
      	}
      });
}

//下载中任务列表
function showTaskDownLoad(){
	var terminalStatue= $('#zdjk').datagrid('getSelections');
	if(terminalStatue.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}
	var mac = [];
	for(var i=0;i<terminalStatue.length;i++){
		mac[i] = terminalStatue[i].mac;
		}
	var querydownLoad = {"mac":mac,"flag":0};
	$('#taskDownLoad').window('open');
	$('#downed').show();
	$('#downing').hide();
	$('#downdel').hide();
	$('#rwxz').datagrid({  
        width: 'auto',                
        striped: true, 
        fitColumns:true,
        singleSelect : false, 
        view: myview,
        emptyMsg: '无数据！',
        url:'program/querySendP_T.do',
        loadMsg:null,  
        pagination: false,
        toolbar:'#downtbs',
        queryParams:querydownLoad,
        method:'POST',
        rownumbers: true,     
        columns:[[  
{field:'mac',hidden:"true",align:'center',title: ''}, 
{field:'program_id',hidden:"true",align:'center',title: ''}, 
{field:'terminal_name',width : '18%',align:'center',title: '终端名称'}, 
{field:'program_name',align:'center',width : '18%',title: '节目名称'}, 
{field:'type',width : '12%',align:'center',title: '节目类型',formatter:function(value,row,index){
										return value==0?'普通':'互动';}}, 
{field:'program_status',align:'center',width : '12%',title: '下载进度',formatter:function(value,row,index){
	if(value == null){
		return '';
	}
	var statustr = ["下载失败","待下载","","下载成功","下载中断"];
	return value.indexOf("\%")>-1?value:statustr[value];
}},
{field:'schedulelevel',width : '10%',align:'center',title: '优先级'},                                                          
{field:'group_name',width : '14%',align:'center',title: '所属终端组'}, 
{field:'materials',width : '14%',align:'center',title: '素材',formatter:function(value,row,index){
	var btn = '<a onclick="showDetail(\''
				+ row.mac+'_'+row.program_id+'_'+row.program_status
				+ '\')" href="javascript:void(0)">查看详情</a>';
	return btn; 
}}                                                          
        ]]  
    }); 
	stopRefreshTerminalGrid = setInterval("refreshProsess()", 2000);  
}
function stopRefresh(gridname){
		clearInterval(gridname);
}

//下载完成任务列表
//function showTaskDownLoaded(){
//	var terminalStatue= $('#zdjk').datagrid('getSelections');
//	if(terminalStatue.length==0){
//		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
//		return;
//		}
//	var mac = [];
//	for(var i=0;i<terminalStatue.length;i++){
//		mac[i] = terminalStatue[i].mac;
//		}
//	var querydownLoad = {"mac":mac,page:1,rows:10,"flag":1};
//	$('#taskDownLoad').window('open');
//	$('#downed').hide();
//	$('#downing').show();
//	$('#downdel').show();
//	$('#rwxz').datagrid({  
//        width: 'auto',                
//        striped: true, 
//        singleSelect : false, 
//        view: myview,
//        emptyMsg: '无数据！',
//        url:'terminal/queryTaskDownLoadByTerminalId.do',
//        loadMsg:'数据加载中请稍后……',  
//        pagination: false,
//        pageSize:10,
//        toolbar:'#downtbs',
//        queryParams:querydownLoad,
//        method:'POST',
//        rownumbers: true,     
//        columns:[[  
//{field:'',checkbox:true,align:'center',title: ''},
//{field:'mac',hidden:"true",align:'center',title: ''}, 
//{field:'taskId',hidden:"true",align:'center',title: ''}, 
//{field:'terminal_name',width : '30%',align:'center',title: '终端名称'}, 
//{field:'taskName',width : '30%',align:'center',title: '任务名称'}, 
//{field:'taskPercent',width : '30%',align:'center',title: '任务下载进度',formatter:function(value,row,index){
//	if(value=="-1"){
//		return "下载失败";
//	}else{
//		return value+"%";
//	}
//    }}                                                         
//        ]]  
//    }); 
//	
//	var p = $('#rwxz').datagrid('getPager'); 
//	$(p).pagination({ 
//	pageList:[5,10,15],
//	beforePageText: '第',//页数文本框前显示的汉字 
//	afterPageText: '页    共 {pages} 页', 
//	showRefresh:false,
//	displayMsg: ''
//	}); 
//	
//}

//查看下载中任务
//function downing(){
//	showTaskDownLoad();
//}

//查看下载完成任务
//function downed(){
//	showTaskDownLoaded();
//	
//}
//删除所选下载完成任务
//function downdel(){
//	var taskdown = $('#rwxz').datagrid('getSelections');
//	if(taskdown.length==0){
//		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
//		return;
//	}
//	  $.messager.defaults = { ok: "确定", cancel: "取消" };
//	$.messager.confirm('删除', '删除后不可恢复，确定删除？', function(r) {
//		if (r) {
//			var mac = [];
//			for(var i=0;i<taskdown.length;i++){
//				mac[i] = taskdown[i].mac+"&&&&"+taskdown[i].taskId;
//			}
//			var deldownLoad = {"mac":mac};
//			$.ajax({ 
//				url: "terminal/delTaskDownLoadByTerminalId.do", 
//				type: "POST",
//				contentType:"application/x-www-form-urlencoded;charset=UTF-8",
//				data: deldownLoad,
//				success: function(data){
//					ZENG.msgbox.show("删除成功！", 4, 3000);
//					$('#rwxz').datagrid("reload");
//					//downed();
//				}
//			
//			});
//		}
//	});
//	
//	
//}

function showTerminalTask(){
	var terminalStatue= $('#zdjk').datagrid('getSelections');
	if(terminalStatue.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}
	var terminal_id = [];
	for(var i=0;i<terminalStatue.length;i++){
		terminal_id[i] = terminalStatue[i].terminal_id;
		}
	var queryprogram_terminal = {"terminal_id":terminal_id};
	$('#program_terminal').window('open');
	$('#zdrw').datagrid({  
        striped: true, 
        fitColumns:true,
        singleSelect : false, 
        view: myview,
        emptyMsg: '无数据！',
        toolbar:'#pt',
        url:'terminal/queryProgram_terminalByTerminalId.do',
        loadMsg:'数据加载中请稍后……',  
        pagination: false,
        queryParams:queryprogram_terminal,
        method:'POST',
        rownumbers: true,     
        columns:[[  
{field:'',checkbox:true,align:'center',title: ''},
{field:'terminal_id',hidden:"true",align:'center',title: '终端ID'},             
{field:'terminal_name',align:'center',width:'45%',title: '终端名称'}, 
{field:'mac',hidden:"true",align:'center',title: ''}, 
{field:'program_id',hidden:"true",align:'center',title: ''}, 
{field:'name',align:'center',width:'45%',title: '任务名称'}
        ]]  
    }); 
	
}

function stopProgram(){
	var terminalStatue= $('#zdrw').datagrid('getSelections');
	if(terminalStatue.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}
	var deleprogram = [];
	for(var i=0;i<terminalStatue.length;i++){
		deleprogram[i] = terminalStatue[i].mac+"&&&&"+terminalStatue[i].program_id+"&&&&"+terminalStatue[i].terminal_id+"&&&&"+terminalStatue[i].name;
		}
	var deleprogram = {"deleprogram":deleprogram};
	$.ajax({ 
    	url: "terminal/deleteProgram.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: deleprogram,
    	success: function(data){
				 ZENG.msgbox.show("停止任务成功！", 4, 3000);
				 $('#zdrw').datagrid("reload");
      	}
 
      });
	
}

//修改音量
function setVolum(){
	var terminal= $('#zdjk').datagrid('getSelections');
	if(terminal.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}
	for(var i=0;i<terminal.length;i++){
		if(terminal[i].terminalStatus==2||terminal[i].terminalStatus==0){
//			ZENG.msgbox.show("请选择播放中状态的终端，否则命令无法生效！", 3, 3000);
//			return;
		}
		}
	var mac = [];
	for(var i=0;i<terminal.length;i++){
		mac[i] = terminal[i].mac+"&&&&"+terminal[i].terminal_name+"&&&&"+terminal[i].terminal_id;
		}
	
	var values = $('#sliders').slider("getValue");
	$('#yl').window('close');
	var control = {"flag":"setvolume","value":values,"mac":mac};
	 $.ajax({ 
	    	url: "terminal/control.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    	data: control,
	    	success: function(data){
				 if(data.success){
					 ZENG.msgbox.show("发送命令成功！", 4, 3000);
				 }else{
					 ZENG.msgbox.show("发送命令失败，请检查服务器状态！", 2, 3000);
				 }
	      	}
	 
	      });
}

//远程控制
function control(flag,value){
var terminal= $('#zdjk').datagrid('getSelections');
if(terminal.length==0){
	 ZENG.msgbox.show("请先选择数据！", 3, 3000);
	return;
	}
if(flag!="open"){
	for(var i=0;i<terminal.length;i++){
		if(terminal[i].terminalStatus==2||terminal[i].terminalStatus==0){
//			ZENG.msgbox.show("请选择播放中状态的终端，否则命令无法生效！", 3, 3000);
//			return;
		}
		}
}

	var mac = [];
	for(var i=0;i<terminal.length;i++){
		mac[i] = terminal[i].mac+"&&&&"+terminal[i].terminal_name+"&&&&"+terminal[i].terminal_id;
		}
	
	if(flag!=null&&flag!=""){
		var control = {"flag":flag,"value":value,"mac":mac};
		 $.ajax({ 
		    	url: "terminal/control.do", 
		    	type: "POST",
		    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		    	data: control,
		    	success: function(data){
					 if(data.success){
						 ZENG.msgbox.show("发送命令成功！", 4, 3000);
					 }else{
						 ZENG.msgbox.show("发送命令失败，请检查服务器状态！", 2, 3000);
					 }
		      	}
		      });
	}
	
}
//定时刷新关机
function setCloseTime(){
	var terminal= $('#zdjk').datagrid('getSelections');
	var rowcount = $('#timesettable').datagrid('getRows');
	if(terminal.length==0){
		ZENG.msgbox.show("请选择数据！", 3, 2000);
		return;
		}
	for (var i = 0; i < terminal.length; i++) {
		if(terminal[i].terminalStatus!=1){
//			ZENG.msgbox.show("请选择“播放中”的终端，否则设置无法生效！", 3, 2000);
//			return;
		}
	}
	var rowslength = rowcount.length;
	if(rowslength > 0){
		for ( var i= 0; i < rowslength; i++) {
			var rowIndex = $('#timesettable').datagrid('getRowIndex', rowcount[i]);
	    	$('#timesettable').datagrid('deleteRow', rowIndex);  
		}
	}
	$('#closeTime').window('open');
	$('#timesettable').datagrid("resize",{  
		height:"230px",
		width:"380px"
	});
	var terminals = new Array();
	for (var i = 0; i < terminal.length; i++) {
		terminals[i]= terminal[i].terminal_id;
	}
//	var terminal_id = terminal[0].terminal_id;
//	var control = {"terminalId":terminal_id};
	$.ajax({ 
    	url: "terminal/querycloseTimeByTerminalId.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data:{
				"terminals" : terminals
			},
    	success: function(data){
    		var TerminalCloseTime = data.TerminalCloseTime;
    		for (var i = 0; i < TerminalCloseTime.length; i++) {
				$('#timesettable').datagrid('insertRow',{
					row: {
						terminalid: TerminalCloseTime[i].terminalId,
						terminalname: TerminalCloseTime[i].terminalname,
						terminalmac:TerminalCloseTime[i].mac,
						opentime: TerminalCloseTime[i].startTime,
						closetime: TerminalCloseTime[i].endTime,
						days: TerminalCloseTime[i].days
					}
				});
    		}
      	}
      });
}


function updateCloseTime(){
	var terminal= $('#zdjk').datagrid('getSelections');
	var rowcount = $('#timesettable').datagrid('getRows');
	
	if(rowcount.length!=0){
		if(!terminaldata(rowcount)){
			 ZENG.msgbox.show("每个终端每天只能设置一组定时，请检查时间设置", 4, 3000);
			 return;
		}
	}
	
	var checkdel = false;
	var tempdata = {};
	tempdata.data = new Array();
	
	for (var i = 0; i < rowcount.length; i++) {
		tempdata.data.push(rowcount[i]);
	}
	tempdata.delterminal = new Array();
	for (var i = 0; i < terminal.length; i++) {
		tempdata.delterminal.push(terminal[i]);
	}
	var jsonStr = JSON.stringify(tempdata);
////	var terminalId = $('#terminalId').textbox("getValue");
////	var boo = $('#endTime').timespinner('isValid');
////	if(!boo){
////		ZENG.msgbox.show("关机时间格式有误！", 2, 3000);
////		return;
////	}
//	var mac = $('#zdjk').datagrid('getSelections')[0].mac;
//	var control = {"terminalId":terminalId,"endTime":endTime,"startTime":startTime,"days":days};
	$.ajax({ 
    	url: "terminal/updatecloseTimeByTerminalId.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data:{
			"terminalJson" : jsonStr
		},
    	success: function(data){
		if(data.success){
			$('#closeTime').window('close');
			 ZENG.msgbox.show("设置成功！", 4, 3000);
			 $('#zdjk').datagrid("reload");
		 }else{
			 ZENG.msgbox.show("设置失败，请检查服务器状态！", 2, 3000);
		 }
      	}
      });
}

//定时时间周期判断
function terminaldata(rowcount){
	var flag = false;
	var terminalid; //终端id
	var days = []; //周期
	if(rowcount.length == 1){
		return true;
	}
	for (var i = 0; i < rowcount.length; i++) {
		 terminalid = rowcount[i].terminalid;
		 days = rowcount[i].days.split(",");
		 for(var j = 0;j < rowcount.length; j++){
		 	if(i != j){ //相同数据不比较
				if(terminalid == rowcount[j].terminalid){ //终端id是否相同
		 			if(rowcount[i].days == rowcount[j].days){ //周期是否相同
		 				if(rowcount[i].closetime == "" && rowcount[j].closetime!="" && rowcount[i].opentime != "" && rowcount[j].opentime == ""){ //判断开关机时间
		 						 flag = true;
		 				}else if(rowcount[i].closetime!=""&&rowcount[j].closetime!=""){
		 					return false;
		 				}else if(rowcount[i].opentime!=""&&rowcount[j].opentime!=""){
		 					return false;
		 				}
		 			}else{//周期不相同
		 				for(k=0;k<days.length;k++){
		 					if(rowcount[j].days.indexOf(days[k])>-1){ //周期是否重复
		 						if(rowcount[i].closetime == "" && rowcount[j].closetime!="" && rowcount[i].opentime != "" && rowcount[j].opentime == ""){
		 							flag = true;
		 						}else if(rowcount[i].closetime!=""&&rowcount[j].closetime!=""){
				 					return false;
				 				}else if(rowcount[i].opentime!=""&&rowcount[j].opentime!=""){
				 					return false;
				 				}
		 					}else{
				 				flag = true;
		 					
		 					}
		 				}
		 			}
		 	}else{ //终端id不相同
		 		flag = true;
		 	}
		}
	 }
	}
	return flag;
}

//定时下载时间周期判断
function timingterminaldata(rowcount){
	var flag = true;
	if(rowcount.length == 1){
		return flag;
	}
	var terminalId; //终端id
	var days = []; //周期
	
	for (var i = 0; i < rowcount.length; i++) {
		terminalId = rowcount[i].terminalId;
		days = rowcount[i].days.split(",");
		for (var j = 0; j < rowcount.length; j++) {
			if(i != j) { //相同数据不比较
				if (terminalId == rowcount[j].terminalId) { //相同终端
					if (rowcount[i].days == rowcount[j].days) { // 相同周期
						// 判断时间
					    flag = compareTime(rowcount[i].endTime,rowcount[i].startTime,rowcount[j].endTime,rowcount[j].startTime);
					    if(!flag){ // 返回false直接return
					    	return false;
					    }
					    
					} else { // 周期不同
						for (var k = 0; k < days.length; k++) {
							if(rowcount[j].days.indexOf(days[k])>-1){ //周期是否重复
								// 判断时间
								 flag = compareTime(rowcount[i].endTime,rowcount[i].startTime,rowcount[j].endTime,rowcount[j].startTime);
								 if(!flag){ // 返回false直接return
								    	return false;
								    }
							}else{ // 周期不重复
								flag = true;
							}
						}
					}
				}else { // 终端不相同 
					flag = true;
				}
			}
		}
	}
	return flag;
}

/**
 * 判断相同终端的时间是否冲突 冲突返回false 不冲突返回true
 * @param endTimei  终端i的结束时间
 * @param startTimei 终端i的开始时间
 * @param endTimej 终端j的结束时间
 * @param startTimej 终端j的开始时间
 * @returns {Boolean} 
 */
function compareTime(endTimei,startTimei,endTimej,startTimej){
	var flag = true;
	// 判断时间
    var dateEndTimei = parseInt(endTimei.split(":")[0]*60) + parseInt(endTimei.split(":")[1]); // i的结束时间
    var dateStartTimei = parseInt(startTimei.split(":")[0]*60) + parseInt(startTimei.split(":")[1]);// i的开始时间
    var dateEndTimej = parseInt(endTimej.split(":")[0]*60) + parseInt(endTimej.split(":")[1]);// j的开始时间
    var dateStartTimej = parseInt(startTimej.split(":")[0]*60) + parseInt(startTimej.split(":")[1]);// j的结束时间
    //  终端i的开始时间 == 终端j的开始时间  
    //	或者 终端i的结束时间 == 终端j的结束时间  
    //	或者 终端i的结束时间 == 终端j的开始时间 
    //	或者 终端i的开始时间 == 终端j的结束时间
    if (dateStartTimei == dateStartTimej 
    	|| dateEndTimei == dateEndTimej
    	|| dateEndTimei == dateStartTimej
    	|| dateStartTimei == dateEndTimej) {
    	return false; //时间段错误
    	// 终端j的开始时间  在 终端i的结束时间 之前 并且 终端j的开始时间在 终端j的开始时间 在 终端i的 开始时间之后
    } else if (dateStartTimej < dateEndTimei &&　dateStartTimej > dateStartTimei) {
    	return false;
    	// 或者 终端j的结束时间  在  终端i的开始时间 之后 并且 终端j的结束时间 在 终端i的结束时间 之前
    } else if (dateEndTimej > dateStartTimei && dateEndTimej < dateEndTimei){
    	return  false; // 时间段错误
    	// 终端i的开始时间 在 终端j的开始时间之前  并且 终端i的结束时间 在 终端j的结束时间 之后
    } else if (dateStartTimej > dateStartTimei && dateEndTimei > dateEndTimej) {
    	return false;
    // 终端j的开始时间 在 终端i的开始时间之前  并且 终端j的结束时间 在 终端i的结束时间之后
    } else if (dateStartTimei > dateStartTimej && dateEndTimej > dateEndTimei) {
    	return false;
    }
    return flag;
}

function delAllCloseTime() {
	var rowcount = $('#timesettable').datagrid('getRows');
	var rows = rowcount.length;
	for ( var i= 0; i < rows; i++) {
		var rowIndex = $('#timesettable').datagrid('getRowIndex', rowcount[i]);
    	$('#timesettable').datagrid('deleteRow', rowIndex);  
	}
}

//取消设置关机时间
function resetCloseTime(){
	var startTime = $('#startTime').timespinner('setValue',"");
	var endTime = $('#endTime').timespinner('setValue',"");
	updateCloseTime();
//	var endTime = "";
//	var terminalId = $('#terminalId').textbox("getValue");
//	var mac = $('#zdjk').datagrid('getSelections')[0].mac;
//	var control = {"terminalId":terminalId,"endTime":endTime,"mac":mac};
//	$.ajax({ 
//    	url: "terminal/updatecloseTimeByTerminalId.do", 
//    	type: "POST",
//    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
//    	data: control,
//    	success: function(data){
//		if(data.success){
//			$('#closeTime').window('close');
//			 ZENG.msgbox.show("设置成功！", 4, 3000);
//			 
//		 }else{
//			 ZENG.msgbox.show("设置失败，请检查服务器状态！", 2, 3000);
//		 }
//      	}
//      });
}
function refreshProsess(){
	$('#rwxz').datagrid("reload");
}
//定时刷新
function refreshData(){
	var terminalStatue= $('#zdjk').datagrid('getRows');
	var terminalid = [];
	var terminalCheck = $('#zdjk').datagrid('getSelections');
	for(var i=0;i<terminalStatue.length;i++){
		terminalid[i] = terminalStatue[i].terminal_id;
	}
	var refreshQuerys = {"terminalIds":terminalid};
	$.ajax({ 
    	url: "terminal/queryTerminalStatus.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: refreshQuerys,
    	success: function(data){
    		for(var i=0;i<terminalStatue.length;i++){
    			var oldTerminalId = terminalStatue[i].terminal_id;
    			for(var j=0;j<data.length;j++){
    				var newTerminalId = data[j].terminal_id;
    				if(newTerminalId == oldTerminalId){
    					var index = $('#zdjk').datagrid('getRowIndex', terminalStatue[i]);
    					terminalStatue[i].terminal_name = data[j].terminal_name;
    					terminalStatue[i].terminalStatus =data[j].terminalStatus;
    					terminalStatue[i].mac =data[j].mac;
    					terminalStatue[i].terminalJudge =data[j].terminalJudge;
    					terminalStatue[i].sysVersion =data[j].sysVersion;
    					terminalStatue[i].softVersion =data[j].softVersion;
    					terminalStatue[i].cpuUsage =data[j].cpuUsage;
    					terminalStatue[i].memUsage =data[j].memUsage;
    					terminalStatue[i].diskUsage =data[j].diskUsage;
    					terminalStatue[i].runTimeLength =data[j].runTimeLength;
    					terminalStatue[i].updateTime = data[j].updateTime;
    					$('#zdjk').datagrid('refreshRow',index);
    					
    				}
    			}
    		}
    		if(terminalStatue.length == terminalCheck.length){
    			$('#zdjk').datagrid('selectAll');
    		}
      	}
      });
}


function refreshView(){
	var terminalStatue= $('#zdjk').datagrid('getSelections');
	var mac = [];
	for(var i=0;i<terminalStatue.length;i++){
		mac[i] = terminalStatue[i].mac+"&&&&"+terminalStatue[i].terminal_name;
		}
	var queryScreen = {"mac":mac};
	$.ajax({ 
    	url: "terminal/queryScreenView.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: queryScreen,
    	success: function(data){
    		for(var i=0;i<data.length;i++){
    			var path;
    			var flag=0;
    			for(var j=0;j<terminalStatue.length;j++){
    				if(data[i].mac==terminalStatue[j].mac){
    					if(terminalStatue[j].terminalStatus!=1){
    						flag++;
    					}
    				}
    			}
    			if(flag==0){
	    			if(data[i].path==0){
	    				path = "img/0.jpg";
	    			}else{
	    				path = "/fyjt/ftpFile/screen/"+ data[i].mac+'/'+data[i].path;
	    			}
    			}else{
    				path = "img/0.jpg";
    			}
    			$("#"+data[i].mac).attr("src",path);
    			$("#large"+data[i].mac).attr("href",path);
    			$("#sim"+data[i].mac).attr("src",path);
    			
    		}
      	}
      });
	
	
}
//刷新下载进度
function refreshDownload(){
	showTaskDownLoad();
}


function closeScreen(){
	
	alert(closeScreen);
}

function addloop() {
//	var zdjkrows = $("#zdjk").datagrid("getSelections");
//	var alwros = $("#timesettable").datagrid("getRows");
//	for (var i = 0; i < zdjkrows.length; i++) {
//		for (var j = 0; j < alwros.length; j++) {
//			if(zdjkrows[i].terminal_id == alwros[j].terminalid){
//				ZENG.msgbox.show("一个终端一天只能设置一组开关机时间", 3, 2000);
//				return;
//			}
//		}
//	}
	$('#addloopWin').window('open');
	$("#open_time").timespinner('setValue', "00:00:00"); 
	$("#close_time").timespinner('setValue', "23:59:59"); 
	var daysEl = document.getElementsByName("loopdays");
	for(var i=0;i<7;i++){
		daysEl[i].checked = true;
	}
}
function editloop() {
	var rows = $("#timesettable").datagrid("getSelections");
	if(rows.length != 1) {
		ZENG.msgbox.show("请选择一条数据", 3, 2000);
		return;
	}	
	$('#editloopWin').window('open');
	$("#edopen_time").timespinner('setValue', rows[0].opentime); 
	$("#edclose_time").timespinner('setValue', rows[0].closetime); 
	var daysEl = document.getElementsByName("edloopdays");
	var daystr = rows[0].days.split(",");
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
	var rows = $("#timesettable").datagrid("getSelections");
	if(rows.length < 1) {
		ZENG.msgbox.show("请选择要删除的数据", 3, 2000);
		return;
	}
	for ( var i= 0; i < rows.length; i++) {
		var rowIndex = $('#timesettable').datagrid('getRowIndex', rows[i]);
    	$('#timesettable').datagrid('deleteRow', rowIndex);  
	}
}
function loopsave() {
	var rows = $("#zdjk").datagrid("getSelections");
	var timesetrows = $("#timesettable").datagrid("getSelections");
	var rowcount = $('#timesettable').datagrid('getRows');
	var startTime = $("#open_time").val();
	var closeTime = $("#close_time").val();
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
	if (days == "") {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	if (startTime == "" && closeTime == "") {
		ZENG.msgbox.show("开关机时间至少设置一个!", 3, 2000);
		return;
	}
//	var tempe = closeTime.split(":");
//	var temps = startTime.split(":");
//	var st = temps[0]*3600 + temps[1]*60;
//	var se = tempe[0]*3600 + tempe[1]*60;
//	if (parseInt(st) > parseInt(se)){
//		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
//		return;
//	}
	if (rows.length > 1) {
		for (var i = 0; i < rows.length; i++) {
			$('#timesettable').datagrid('insertRow',{
				row: {
						terminalid:rows[i].terminal_id,
						terminalname:rows[i].terminal_name,
						terminalmac:rows[i].mac,
						opentime: startTime,
						closetime: closeTime,
						days: days
				}
			});
		}
	} else {
		$('#timesettable').datagrid('insertRow',{
			row: {
					terminalid:rows[0].terminal_id,
					terminalname:rows[0].terminal_name,
					terminalmac:rows[0].mac,
					opentime: startTime,
					closetime: closeTime,
					days: days
			}
		});
	}
	$('#addloopWin').window('close');
}
function edloopsave() {
	var rows = $("#zdjk").datagrid("getSelections");
	var timesetrows = $("#timesettable").datagrid("getSelections");
	var rowIndex = $('#timesettable').datagrid('getRowIndex', timesetrows[0]);
	var edopen_time = $("#edopen_time").val();
	var edclose_time = $("#edclose_time").val();
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
	if (days == "") {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	if (edopen_time == "" && edclose_time == "") {
		ZENG.msgbox.show("开关机时间至少设置一个!", 3, 2000);
		return;
	}
//	var tempe = edclose_time.split(":");
//	var temps = edopen_time.split(":");
//	var st = temps[0]*3600 + temps[1]*60;
//	var se = tempe[0]*3600 + tempe[1]*60;
//	if (parseInt(st) > parseInt(se)){
//		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
//		return;
//	}
	$('#timesettable').datagrid('updateRow',{
	index: rowIndex,
	row: {
			//terminalid:rows[0].terminal_id,
			//terminalname:rows[0].terminal_name,
			opentime: edopen_time,
			closetime: edclose_time,
			days: days
		}
	});
	$('#editloopWin').window('close');
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
	function refreshDetailGrid(){
		$('#showDetailGrid').datagrid('reload');
	}
function t_operation(){
	if(document.all){
		window.event.cancelBubble = true;
 	}else{
		event.stopPropagation(); 
	}
	var flag = $("#t_operation")[0].style.display;
	if(flag=="none"||flag==""){
		$("#t_operation").fadeIn(500);
		flag="block";
	}else if(flag=="block"){
		$("#t_operation").fadeOut(500);
		flag="none";
		
	}
}
function showTerminalTimingWin(){
	var rows = $("#zdjk").datagrid("getSelections");
	var terminalarr = new Array();
	if(rows.length < 1){
		ZENG.msgbox.show("请选择终端", 3, 2000);
		return;
	}
	for (var i = 0; i < rows.length; i++) {
		if(rows[i].terminalStatus != 1){
			ZENG.msgbox.show("请选择在线终端", 3, 2000);
			return;
		}else{
			terminalarr[i] = rows[i].terminal_id;
		}
	}
	
	$("#terminalTimingWin").window('open');
	
	$('#dsxz').datagrid({  
    striped: true, 
    fitColumns:true,
    singleSelect : false, 
    view: myview,
    emptyMsg: ' ',
    url:'terminal/queryTerminalTimingSet.do',
    queryParams:{terminalids:terminalarr},
    loadMsg:'数据加载中请稍后……',  
    pagination: false,
    method:'POST',
    rownumbers: true,     
    columns:[[  
{field:'',checkbox:true,align:'center',title: '',width:'20px'},
{field:'terminalId',hidden:"true",align:'center',title: '终端ID'}, 
{field:'mac',hidden:"true",align:'center',title: ''}, 
{field:'terminal_name',align:'center',width:'22%',title: '终端名称'}, 
{field:'startTime',align:'center',width:'22%',title: '开始下载时间'},
{field:'endTime',align:'center',width:'22%',title: '结束下载时间'},
{field:'days',align:'center',width:'28%',title: '执行周期'}
        ]]  
    }); 
	
}
function addTimingSet(){
	$("#adddownloadsetWin").window('open');
}
function delTimingSet(){
	var rows = $("#dsxz").datagrid("getSelections");
	if(rows.length < 1) {
		ZENG.msgbox.show("请选择要删除的数据", 3, 2000);
		return;
	}
	for ( var i= 0; i < rows.length; i++) {
		var rowIndex = $('#dsxz').datagrid('getRowIndex', rows[i]);
    	$('#dsxz').datagrid('deleteRow', rowIndex);  
	}
}
function downloadsetsave(){
	var rows = $("#zdjk").datagrid("getSelections");
	var startTime = $("#start_download_time").val();
	var endTime = $("#end_download_time").val();
	var daysEl = document.getElementsByName("loopdownloaddays");
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
	if (days == "") {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	if (startTime == "" || endTime == "") {
		ZENG.msgbox.show("请设置开关机时间!", 3, 2000);
		return;
	}
	var tempe = endTime.split(":");
	var temps = startTime.split(":");
	var st = temps[0]*3600 + temps[1]*60;
	var se = tempe[0]*3600 + tempe[1]*60;
	if (parseInt(st) == parseInt(se)){
		ZENG.msgbox.show("开始时间和结束时间不能相同!", 3, 2000);
		return;
	}
	if (parseInt(st) >= parseInt(se)){
		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
		return;
	}
		for (var i = 0; i < rows.length; i++) {
			$('#dsxz').datagrid('insertRow',{
				row: {
						terminalId:rows[i].terminal_id,
						terminal_name:rows[i].terminal_name,
						mac:rows[i].mac,
						startTime: startTime,
						endTime: endTime,
						days: days
				}
			});
		}
	$('#adddownloadsetWin').window('close');
}
function saveDownloadSet(){
	var terminal= $('#zdjk').datagrid('getSelections');
	var rowcount = $('#dsxz').datagrid('getRows');
	
	// 一个终端5条记录 n个终端5n条 大于5n直接提示
	if(rowcount.length > ((terminal.length) * 5)){
		ZENG.msgbox.show("定时下载同一个终端最多设置5条记录", 3, 3000);
		 return;
	}
	
	if(!timingterminaldata(rowcount)){
		 ZENG.msgbox.show("同一天时间不能交叉", 3, 3000);
		 return;
	}
	var tempdata = {};
	tempdata.data = new Array();
	
	for (var i = 0; i < rowcount.length; i++) {
		tempdata.data.push(rowcount[i]);
	}
	tempdata.delterminal = new Array();
	for (var i = 0; i < terminal.length; i++) {
		tempdata.delterminal.push(terminal[i]);
	}
	var jsonStr = JSON.stringify(tempdata);
	$.ajax({ 
    	url: "terminal/updateDownloadSetByTerminalId.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data:{
			"terminalJson" : jsonStr
		},
    	success: function(data){
		if(data.success){
			$('#terminalTimingWin').window('close');
			 ZENG.msgbox.show("设置成功！", 4, 3000);
			 $('#zdjk').datagrid("reload");
		 }else{
			 ZENG.msgbox.show("设置失败，请检查服务器状态！", 2, 3000);
		 }
      	}
      });
}