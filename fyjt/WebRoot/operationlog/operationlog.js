$(document).ready(function(){
	loadgrid(); //加载操作日志信息
	checkDate(); //日期验证
});
function uploadProData(){
	$('#zj').window('open');
}
var fileList = [];
function PreviewImage(docObj) {
    fileList = docObj.files;
    for(var i=0;i<fileList.length;i++){
    	var file = fileList[i];
    	if(file.name.substring(file.name.indexOf(".")+1)!="txt"){
    		document.getElementById("profileurl").value = "";
    		ZENG.msgbox.show("文件格式不符 errtxt",5,1500);
    		return;
    	}
    	if(file.name.indexOf("@") != -1){
	    	var tname = file.name.substring(0,file.name.indexOf("@"));
	    	//var regexp = /^([1][7-9][0-9][0-9]|[2][0][0-9][0-9])(\-)([0][1-9]|[1][0-2])(\-)([0-2][1-9]|[3][0-1])$/g;
	    	var regexp = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
	    	if(!regexp.test(tname)){
	    		document.getElementById("profileurl").value = "";
	    		ZENG.msgbox.show("文件格式不符 errtime",5,1500);
	    		return;
	    	}
	    	var mname = file.name.substring(file.name.indexOf("@")+1,file.name.indexOf("."));
	    	var reg = /[\u4e00-\u9fa5]/;
	    	if(reg.test(mname)){
	    		document.getElementById("profileurl").value = "";
	    		ZENG.msgbox.show("文件格式不符 errmac",5,1500);
	    		return;
	    	}
    	}else{
    		document.getElementById("profileurl").value = "";
    		ZENG.msgbox.show("文件格式不符 err@",5,1500);
    		return;
    	}
    }
    
    return true;
}
function uploadProFile(){
	var profile = $('#profileurl').val();
	if(profile == ""){
		ZENG.msgbox.show("请选择文件",5, 1500);
		return;
	}
		$.ajaxFileUpload({
                    url: 'programlog/insertProgramlog.do', //用于文件上传的服务器端请求地址
                    method:'POST',
                    fileElementId: 'profileurl', //文件上传空间的id属性  <input type="file" id="file" name="file" />
                    dataType: "xml", //返回值类型 一般设置为json
                    data : {},
					async : true,
                    success: function(data){
                    	//alert(data.body.innerText);
                    	var str = data.body.innerText;
                    	var obj = $.parseJSON(str);  ;
                        if(obj.msg){
                        	ZENG.msgbox.show("导入统计数据成功",4, 1500); 
                        	$('#zj').window('close');
                        }else{
                        	for(var i=0;i<obj.listname.length;i++){
                        		var item = obj.listname[i];
                        		ZENG.msgbox.show(item+"已经存在",5, 1500);
                        	}
                        	$('#zj').window('close');
                        }
                    },
                   error : function(data) {
					//alert("网络异常，请重试");
				}
                   
    });
}
function change(val){
	if(val==0){
		i=0;
	   $('#programlogTbDiv').hide();
	   $('#programlogBgDiv').hide();
	   $('#errorlogTbDiv').hide();
	   $('#errorlogBgDiv').hide();
	   $('#staticlogBgDiv').hide();
	   $('#staticlogTbDiv').hide(); //素材信息列表隐藏
	   $('#staticlogTbTJ').hide();  //统计信息列表隐藏
	   $('#operalogBgDiv').show();  
	   $('#operalogTbDiv').show();  //操作日志列表
	}else if(val==10){  //播放日志列表加载
		$('#operalogTbDiv').hide();
		$('#staticlogBgDiv').hide();
		$('#programlogTbDiv').show();
		$('#programlogBgDiv').show();
		loadProgramData(); 
	}else if(val==12){  //播放日志模糊查询
		$('#operalogTbDiv').hide();
		$('#staticlogBgDiv').hide();
		$('#programlogTbDiv').show();
		$('#programlogBgDiv').show();
		searchProStatictis();  
	}else if(val==1){ //素材信息
		i=1;
	   $('#programlogTbDiv').hide();
	   $('#programlogBgDiv').hide();
	   $('#errorlogTbDiv').hide();
	   $('#errorlogBgDiv').hide();
	   $('#importsc').hide();
	   $('#importtj').show();
	   $('#operalogBgDiv').hide();
	   $('#operalogTbDiv').hide();
	   $('#staticlogTbTJ').hide();  //统计信息列表隐藏
	   $('#staticlogBgDiv').show();
	   $('#staticlogTbDiv').show();
	   loadStaticData(); //加载统计信息
	}else if (val==2){
		i=2;
   	   $('#delTD').show();
	   $('#importtj').show();
	   $('#importsc').hide();
	   $('#operalogBgDiv').hide();
	   $('#operalogTbDiv').hide();
	   $('#staticlogTbTJ').hide();  //统计信息列表隐藏
	   $('#staticlogBgDiv').show();
	   $('#staticlogTbDiv').show();
	   searchStatictis();
	}else if( val==3){  //统计信息
		i=3;
		$('#delTD').hide();  //隐藏删除按钮
		$('#importtj').hide();
		$('#importsc').show();
		var terminalName=$('#terminalName').val();
		if(terminalName!=""){
			$.messager.alert("提示信息","查询统计信息不需要输入终端名称");
			$('#terminalName').val="";
			return;
		}
		//查询统计信息
	    var eleName=$('#eleName').val();
	    var starttime=$('#statictisstarttime').datetimebox('getValue');
	    var endtime=$('#statictisendtime').datetimebox('getValue');
	    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 3000);
	        return false;
	    }
		$('#operalogBgDiv').hide();
		$('#operalogTbDiv').hide();
	    $('#staticlogBgDiv').show();
		$('#staticlogTbDiv').hide(); //素材信息列表隐藏
		$('#staticlogTbTJ').show();  //统计信息列表显示
	   /* if(eleName!=""){
	    	var eleName=eleName.replace("%","/%"); //转译，去空格特殊符号
	    }*/
	    $('#statictj').datagrid({
			width:'auto',
			striped:true,
			singleSelect:false,
			url:'statistics/querytongji.do',
			loadMsg:'数据加载中请稍后......',
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	        pagination:true,
	        queryParams:{eleName:eleName,starttime:starttime,endtime:endtime,page:1,rows:10},
	        method:'post',
	        rownumbers:true,
	        columns:[[
	                {field:'t_id',width:'10px',align:'center',title: '统计信息ID',hidden:true},
	      			{field:'elemName',width:'450px',align:'center',title: '素材名称'}, 
	      			{field:'elemType',width:'225px',align:'center',title: '素材类型',formatter:function(val,row){
	      				 if(val!=null){
	          				 switch(val){
	          				 case 1:
	          					 return '文本';
	          				 case 2:
	          					 return '音频';
	          				 case 3:
	          					 return '图片';
	          				 case 4:
	          					 return '视频';
	          				 case 5:
	          					 return '网页';
	          				 case 6:
	          					 return 'Falsh';
	          				 case 7:
	          					 return 'Office';
	          				 case 8:
	          					 return '流媒体';
	          				 default:
	          					 return '未知格式文件';
	          				 }
	         		    	}
	      			}}, 
	      			{field:'totaltime',width:'275px',align:'center',title: '素材播放时长(秒)'}, 
	      			{field:'counts',width:'265px',align:'center',title: '素材播放次数'}
	          ]]
		});
		var p=$('#statictj').datagrid('getPager');
		$(p).pagination({
			pageSize:10,
			pageList:[5,10,15],
			beforePageText:'第',
			afterPageText:'页 共{pages}页',
	        showRefresh:false,
			displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
		});
	}else if(val==4){
		//触摸互动查询统计
		i=4;
		$('#operalogBgDiv').hide();
		$('#operalogTbDiv').hide();
	    $('#staticlogBgDiv').hide();
		$('#staticlogTbDiv').hide(); //素材信息列表隐藏
		$('#staticlogTbTJ').hide();  //统计信息列表隐藏
		$('#importCMTJ').hide();  //导出触摸统计信按钮隐藏
		$('#importCMCX').show();  //导出触摸统计信息按钮显示
		$('#CMBGDiv').show();  //统计信息列表显示
		$('#CMDiv').show();  //统计信息列表显示
		var sceneNameofButton=$('#sceneNameofButton').val();
		var sceneNameofJumpbutton=$('#sceneNameofJumpbutton').val();
		var buttonType=$('#buttonType').combobox('getValue');
		if(buttonType==5){
			buttonType=0;
		}
		var starttime=$('#clickStarttime').datetimebox('getValue');
		var endtime=$('#clickEndtime').datetimebox('getValue');
		$('#CMDiv').datagrid({  
	        width: 'auto',                
	        striped: true,  
	        singleSelect : false,  
	        url:'statisticsButton/queryPage.do',  
	        loadMsg:'数据加载中请稍后……',  
	        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	        pagination: true, //分页控件 
	        queryParams:{page:1,rows:10,sceneNameofButton:sceneNameofButton,sceneNameofJumpbutton:sceneNameofJumpbutton,buttonType:buttonType,starttime:starttime,endtime:endtime},
	        method:'POST',
	        rownumbers: true,     
	        columns:[[  
				{field:'terminalName',width:'120px',align:'center',title: '所属终端名称'}, 
				{field:'programName',width:'145px',align:'center',title: '节目名称'}, 
				{field:'sceneNameofButton',width:'200px',align:'center',title: '场景名称'}, 
				{field:'buttonName',width:'180px',align:'center',title: '按钮名称'}, 
				{field:'sceneNameofJumpbutton',width:'245px',align:'center',title: '跳转场景名称'}, 
				{field:'buttonType',width:'170px',align:'center',title: '按钮类型',formatter:function(val,row){
				 if(val!=null){
	  				 switch(val){
	  				 case "0":
	  					 return '无';
	  				 case "1":
	  					 return '场景跳转';
	  				 case "2":
	  					 return '返回上一场景';
	  				 case "3":
	  					 return '跳转首页';
	  				 case "4":
	  					 return '关联可执行程序';
	  				 default:
	  					 return '未知格式文件';
	  				 }
	 		    	}
				}},                                                         
				{field:'clickTime',width:'150px',align:'center',title: '点击时间',formatter:function(val,row){/*
					if (value != null) {  var unixTimestamp = new Date(value);
				    return unixTimestamp.toLocaleString();}
			      */
					 if(val!=null){
						 	var date;
							if(checkBrowserVision() == 1){
								var aa = val.replace(" ","T");
								var a = aa.replace(/(\d{4})-(\d{2})-(\d{2})T(.*)?\.(.*)/, "$1/$2/$3 $4")
								date = new Date(a)
							}else{
								date = new Date(val);
							}
							var month=date.getMonth()+1;
							var day=date.getDate();
							var h=date.getHours();
							var m=date.getMinutes();
							var s=date.getSeconds();
							if(month<10){
							   month='0'+month;
							}
							if(day<10){
							  day='0'+day;
							}
							if(h<10){
								h='0'+h;
							}
							if(m<10){
								m='0'+m;
							}
							if(s<10){
								s='0'+s;
							}
							return date.getFullYear() + '-' + month + '-'+ day+' '+h+':'+m+':'+s;
						}
					}}
	        ]]
	    });
		var p=$('#CMDiv').datagrid('getPager');
		$(p).pagination({
			pageSize:10,
			pageList:[5,10,15],
			beforePageText:'第',
			afterPageText:'页 共{pages}页',
			showRefresh:false,
			displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
		});
	/*	$('#sceneNameofButton').val('');
		$('#buttonType').combobox('clear');*/
	}else if(val==11){
		var errorModule = $("#errorModule").combobox('getText');
		var errordetail = $("#errordetail").val();
		var starttime=$('#errorstarttime').datetimebox('getValue');
    	var endtime=$('#errorendtime').datetimebox('getValue');
    	if(starttime != '' && endtime != '' && starttime > endtime){
    		if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
				ZENG.msgbox.show("时间选择有误！开始时间必须小于结束时间！",5, 1500);
		        return false;
    		}
    	}
		$('#operalogTbDiv').hide();
		$('#staticlogBgDiv').hide();
		$('#programlogTbDiv').hide();
		$('#programlogBgDiv').hide();
		$('#errorlogTbDiv').show();
		$('#errorlogBgDiv').show();
		loadErrorData(errorModule,errordetail,starttime,endtime);
	}else{
		//触摸互动统计
		i=5;
		$('#operalogBgDiv').hide();
		$('#operalogTbDiv').hide();
	    $('#staticlogBgDiv').hide();
		$('#staticlogTbDiv').hide(); //素材信息列表隐藏
		$('#staticlogTbTJ').hide();  //统计信息列表隐藏
		$('#importCMCX').hide();  //导出触摸信息查询按钮隐藏
		$('#importCMTJ').show();  //导出触摸统计信息按钮显示
		$('#CMBGDiv').show();  //统计信息列表显示
		$('#CMDiv').show();  //统计信息列表显示
		var sceneNameofButton=$('#sceneNameofButton').val();
		var sceneNameofJumpbutton=$('#sceneNameofJumpbutton').val();
		var buttonType=$('#buttonType').combobox('getValue');
		if(buttonType==5){
			buttonType=0;
		}
		var starttime=$('#clickStarttime').datetimebox('getValue');
		var endtime=$('#clickEndtime').datetimebox('getValue');
		$('#CMDiv').datagrid({  
	        width: 'auto',                
	        striped: true,  
	        singleSelect : false,  
	        url:'statisticsButton/queryCMtj.do',  
	        loadMsg:'数据加载中请稍后……',  
	        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	        pagination: true, //分页控件 
	        queryParams:{page:1,rows:10,sceneNameofButton:sceneNameofButton,sceneNameofJumpbutton:sceneNameofJumpbutton,buttonType:buttonType,starttime:starttime,endtime:endtime},
	        method:'POST',
	        rownumbers: true,     
	        columns:[[  
				{field:'sceneNameofButton',width:'280px',align:'center',title: '场景名称'}, 
				{field:'buttonName',width:'230px',align:'center',title: '按钮名称'}, 
				{field:'buttonType',width:'230px',align:'center',title: '按钮类型',formatter:function(val,row){
				 if(val!=null){
	  				 switch(val){
	  				 case "0":
	  					 return '无';
	  				 case "1":
	  					 return '场景跳转';
	  				 case "2":
	  					 return '返回上一场景';
	  				 case "3":
	  					 return '跳转首页';
	  				 case "4":
	  					 return '关联可执行程序';
	  				 default:
	  					 return '未知格式文件';
	  				 }
	 		    	}
				}},                                                         
				{field:'sceneNameofJumpbutton',width:'280px',align:'center',title: '跳转场景'},
				{field:'count',width:'195px',align:'center',title: '按钮触摸次数'}
	        ]]
	    });
		var p=$('#CMDiv').datagrid('getPager');
		$(p).pagination({
			pageSize:10,
			pageList:[5,10,15],
			beforePageText:'第',
			afterPageText:'页 共{pages}页',
			showRefresh:false,
			displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
		});
	}
	/*$('#sceneNameofButton').val('');
	$('#buttonType').combobox('clear');*/
}
/****
 * 加载播放日志列表
 * @return
 */
function loadProgramData(){
	$('#programlogdg').datagrid({  
        width: 'auto',                
        striped: true,  
        singleSelect : false,  
        url:'programlog/queryProgramlog.do',  
        loadMsg:'数据加载中请稍后……',  
        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        pagination: true, //分页控件 
        queryParams:{page:1,rows:10},
        method:'POST',
        frozenColumns : [[{
	         field : 'ck',
	         checkbox : true
	     }]],
        rownumbers: true,     
        columns:[[  
		    {field:'proId',width:'10px',align:'center',title: '统计信息ID',hidden:true},
			{field:'terminalName',width:'180px',align:'center',title: '所属终端名称'}, 
			{field:'programName',width:'250px',align:'center',title: '节目名称'}, 
			{field:'programType',width:'180px',align:'center',title: '节目类型'}, 
			{field:'programLevel',width:'100px',align:'center',title: '优先级'},
			{field:'programLoop',width:'132px',align:'center',title: '循环次数',hidden:true},
			{field:'totalTime',width:'115px',align:'center',title: '播放总时间'},                                                         
			{field:'startTime',width:'180px',align:'center',title: '播放开始时间'}, 
			{field:'endTime',width:'180px',align:'center',title: '播放结束时间'}                                
        ]]
    });
	var p=$('#programlogdg').datagrid('getPager');
	$(p).pagination({
		pageSize:10,
		pageList:[5,10,15],
		beforePageText:'第',
		afterPageText:'页 共{pages}页',
		showRefresh:false,
		displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
	});
}
/****
 * 查询故障信息
 * @return
 */
function loadErrorData(errorModule,errordetail,starttime,endtime){
	$('#errorlogdg').datagrid({  
        width: 'auto',                
        striped: true,  
        singleSelect : false,  
        url:'errorlog/queryErrorlog.do',  
        loadMsg:'数据加载中请稍后……',  
        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        pagination: true, //分页控件 
        queryParams:{page:1,rows:10,errormodule:errorModule,errordetail:errordetail,
        	starttime:starttime,endtime:endtime},
        method:'POST',
        frozenColumns : [[{
	         field : 'ck',
	         checkbox : true
	     }]],
        rownumbers: true,     
        columns:[[  
		    {field:'log_id',width:'10px',align:'center',title: '故障信息ID',hidden:true},
			{field:'module_name',width:'230px',align:'center',title: '故障模块'}, 
			{field:'function_name',width:'180px',align:'center',title: '子功能'}, 
			{field:'exception_reason',width:'235px',align:'center',title: '故障描述'},                                                         
			{field:'happen_time',width:'180px',align:'center',title: '发生时间',formatter:function(val,row){
				 if(val!=null){
	  				return new Date(val).getFullYear() + '-' + 
	  					(new Date(val).getMonth() + 1) + '-' + new Date(val).getDate() + ' ' + 
	  					new Date(val).getHours() + ':' + new Date(val).getMinutes() + ':' + 
	  					new Date(val).getSeconds();
 		    	 }
			}}                            
        ]]
    });
	var p=$('#errorlogdg').datagrid('getPager');
	$(p).pagination({
		pageSize:10,
		pageList:[5,10,15],
		beforePageText:'第',
		afterPageText:'页 共{pages}页',
		showRefresh:false,
		displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
	});
}
/****
 * 查询素材信息
 * @return
 */
function loadStaticData(){
	$('#staticdg').datagrid({  
        width: 'auto',                
        striped: true,  
        singleSelect : false,  
        url:'statistics/queryPage.do',  
        loadMsg:'数据加载中请稍后……',  
        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        pagination: true, //分页控件 
        queryParams:{page:1,rows:10},
        method:'POST',
        frozenColumns : [[{
	         field : 'ck',
	         checkbox : true
	     }]],
        rownumbers: true,     
        columns:[[  
		    {field:'t_id',width:'10px',align:'center',title: '统计信息ID',hidden:true},
			{field:'terminalName',width:'120px',align:'center',title: '所属终端名称'}, 
			{field:'programName',width:'120px',align:'center',title: '节目名称'}, 
			{field:'sceneName',width:'125px',align:'center',title: '场景名称'}, 
			{field:'regionName',width:'132px',align:'center',title: '区域名称'}, 
			{field:'elemName',width:'170px',align:'center',title: '素材名称'}, 
			{field:'elemType',width:'98px',align:'center',title: '素材类型',formatter:function(val,row){
			 if(val!=null){
  				 switch(val){
  				 case "1":
  					 return '文本';
  				 case "2":
  					 return '音频';
  				 case "3":
  					 return '图片';
  				 case "4":
  					 return '视频';
  				 case "5":
  					 return '网页';
  				 case "6":
  					 return 'Falsh';
  				 case "7":
  					 return 'Office';
  				 case "8":
  					 return '流媒体';
  				 default:
  					 return '未知格式文件';
  				 }
 		    	}
			}}, 
			{field:'elemplayTime',width:'115px',align:'center',title: '素材播放时长(秒)'},                                                         
			{field:'elemstartTime',width:'153px',align:'center',title: '播放开始时间',formatter:function(val,row){ 
				if(val!=null){
					var date;
					if(checkBrowserVision() == 1){
						var aa = val.replace(" ","T");
						var a = aa.replace(/(\d{4})-(\d{2})-(\d{2})T(.*)?\.(.*)/, "$1/$2/$3 $4")
						date = new Date(a)
					}else{
						date = new Date(val);
					}
					var month=date.getMonth()+1;
					var day=date.getDate();
					var h=date.getHours();
					var m=date.getMinutes();
					var s=date.getSeconds();
					if(month<10){
					   month='0'+month;
					}
					if(day<10){
					  day='0'+day;
					}
					if(h<10){
						h='0'+h;
					}
					if(m<10){
						m='0'+m;
					}
					if(s<10){
						s='0'+s;
					}
					return date.getFullYear() + '-' + month + '-'+ day+' '+h+':'+m+':'+s;
			}}}, {field:'elemendTime',width:'153px',align:'center',title: '播放结束时间',formatter:function(val,row){ 
		    	  if(val!=null){
		    	  	var date;
		    	  	if(checkBrowserVision() == 1){
						var aa = val.replace(" ","T");
						var a = aa.replace(/(\d{4})-(\d{2})-(\d{2})T(.*)?\.(.*)/, "$1/$2/$3 $4")
						date = new Date(a)
					}else{
						date = new Date(val);
					}
					var month=date.getMonth()+1;
					var day=date.getDate();
					var h=date.getHours();
					var m=date.getMinutes();
					var s=date.getSeconds();
					if(month<10){
					   month='0'+month;
					}
					if(day<10){
					  day='0'+day;
					}
					if(h<10){
						h='0'+h;
					}
					if(m<10){
						m='0'+m;
					}
					if(s<10){
						s='0'+s;
					}
					return date.getFullYear() + '-' + month + '-'+ day+' '+h+':'+m+':'+s;
				}}
		      }                                
        ]]
    });
	var p=$('#staticdg').datagrid('getPager');
	$(p).pagination({
		pageSize:10,
		pageList:[5,10,15],
		beforePageText:'第',
		afterPageText:'页 共{pages}页',
		showRefresh:false,
		displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
	});
}
/****
 * 查询操作日志信息
 * @return
 */
function loadgrid(){
	$('#logdg').datagrid({
		width:'auto',
		striped:true,
		singleSelect:false,
		url:'operationlog/queryAll.do',
		loadMsg:'数据加载中请稍后......',
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        pagination:true,
        queryParams:{page:1,rows:10},
        method:'post',
        rownumbers:true,
        columns:[[
           {field:'t_id',width:'130px',align:'center',title:'日志ID',hidden:true},
           {field:'realname',width:'180px',align:'center',title:'操作人姓名'},
          /* {field:'userName',width:'160px',align:'center',title:'操作人'},*/
           {field:'operationName',width:'299px',align:'center',title:'操作元素' ,
              formatter:function(val,row){
              	if (row.operationName==null||row.operationNamel=="") {
              		return "被动添加终端";
              	}else{
              	    return row.operationName; 
              	}
              }
           },
           {field:'description',width:'245px',align:'center',title:'操作类型'},
           {field:'module_name',width:'230px',align:'center',title:'操作模块'},
           {field:'time',width:'260px',align:'center',title:'操作时间',formatter:function(val,row){ 
        	   if(val!=null){
				var date = new Date(val);
				var month=date.getMonth()+1;
				var day=date.getDate();
				var h=date.getHours();
				var m=date.getMinutes();
				var s=date.getSeconds();
				if(month<10){
				   month='0'+month;
				}
				if(day<10){
				  day='0'+day;
				}
				if(h<10){
					h='0'+h;
				}
				if(m<10){
					m='0'+m;
				}
				if(s<10){
					s='0'+s;
				}
				return date.getFullYear() + '-' + month + '-'+ day+' '+h+':'+m+':'+s;
			}}}
          ]]
	});
	var p=$('#logdg').datagrid('getPager');
	$(p).pagination({
		pageSize:10,
		pageList:[5,10,15],
		beforePageText:'第',
		afterPageText:'页 共{pages}页',
		showRefresh:false,
		displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
	});
}
//素材统计
function searchButton(){
	$('#staticdg').datagrid({  
        width: 'auto',                
        striped: true,  
        singleSelect : false,  
        url:'statistics/queryPage.do',  
        loadMsg:'数据加载中请稍后……',  
        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        pagination: true, //分页控件 
        queryParams:{page:1,rows:10},
        method:'POST',
        frozenColumns : [[{
	         field : 'ck',
	         checkbox : true
	     }]],
        rownumbers: true,     
        columns:[[  
		    {field:'t_id',width:'10px',align:'center',title: '统计信息ID',hidden:true},
			{field:'terminalName',width:'120px',align:'center',title: '所属终端名称'}, 
			{field:'programName',width:'120px',align:'center',title: '节目名称'}, 
			{field:'sceneName',width:'125px',align:'center',title: '场景名称'}, 
			{field:'regionName',width:'132px',align:'center',title: '区域名称'}, 
			{field:'elemName',width:'170px',align:'center',title: '素材名称'}, 
			{field:'elemType',width:'98px',align:'center',title: '素材类型',formatter:function(val,row){
			 if(val!=null){
  				 switch(val){
  				 case "1":
  					 return '文本';
  				 case "2":
  					 return '音频';
  				 case "3":
  					 return '图片';
  				 case "4":
  					 return '视频';
  				 case "5":
  					 return '网页';
  				 case "6":
  					 return 'Falsh';
  				 case "7":
  					 return 'Office';
  				 case "8":
  					 return '流媒体';
  				 default:
  					 return '未知格式文件';
  				 }
 		    	}
			}}, 
			{field:'elemplayTime',width:'115px',align:'center',title: '素材播放时长(秒)'},                                                         
			{field:'elemstartTime',width:'153px',align:'center',title: '播放开始时间',formatter:function(val,row){
				 if(val!=null){
						var date = new Date(val);
						var month=date.getMonth()+1;
						var day=date.getDate();
						var h=date.getHours();
						var m=date.getMinutes();
						var s=date.getSeconds();
						if(month<10){
						   month='0'+month;
						}
						if(day<10){
						  day='0'+day;
						}
						if(h<10){
							h='0'+h;
						}
						if(m<10){
							m='0'+m;
						}
						if(s<10){
							s='0'+s;
						}
						return date.getFullYear() + '-' + month + '-'+ day+' '+h+':'+m+':'+s;
					}}}, {field:'elemendTime',width:'153px',align:'center',title: '播放结束时间',formatter:function(val,row){
						if(val!=null){
						var date = new Date(val);
						var month=date.getMonth()+1;
						var day=date.getDate();
						var h=date.getHours();
						var m=date.getMinutes();
						var s=date.getSeconds();
						if(month<10){
						   month='0'+month;
						}
						if(day<10){
						  day='0'+day;
						}
						if(h<10){
							h='0'+h;
						}
						if(m<10){
							m='0'+m;
						}
						if(s<10){
							s='0'+s;
						}
						return date.getFullYear() + '-' + month + '-'+ day+' '+h+':'+m+':'+s;
					}}
		      }                                
        ]]
    });
	var p=$('#staticdg').datagrid('getPager');
	$(p).pagination({
		pageSize:10,
		pageList:[5,10,15],
		beforePageText:'第',
		afterPageText:'页 共{pages}页',
		showRefresh:false,
		displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
	});

}
/****
 * 操作日志条件查询
 * @return
 */
function search(){
	var userName=$('#userName').val();  //操作人姓名realname
	var operationName=$('#operationName').val();
	var starttime=$('#starttime').datetimebox('getValue');
	var endtime=$('#endtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
//	var allModule=$('#allModule').combobox('getText');
	var allModule=$('#allModule').combobox('getValue');
	var allOpera=$('#allOpera').combobox('getValue');
	var userName=userName.replace("%","/%"); //转译，去空格特殊符号
	var operationName=operationName.replace("%","/%"); //转译，去空格特殊符号
	    $('#logdg').datagrid('load',{
	    	page:1,
	    	rows:10,
	    	userName:userName,
	    	operationName:operationName,
	    	starttime:starttime,
	    	endtime:endtime,
	    	operationModel:allModule,
	    	operationType:allOpera
	   });
}
/****
 * 查询播放日志
 */
function searchProStatictis(){
    var terminalName=$('#proterminalName').val();
    var programName=$('#proName').val();
    var starttime=$('#prostarttime').datetimebox('getValue');
    var endtime=$('#proendtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
    if(terminalName!=""){
    	var terminalName=terminalName.replace("%","/%"); //转译，去空格特殊符号
    }
    if(programName!=""){
    	var programName=programName.replace("%","/%"); //转译，去空格特殊符号
    }
	$('#programlogdg').datagrid('load',{
		page:1,
		rows:10,
		terminalName:terminalName,
		programName:programName,
		starttime:starttime,
		endtime:endtime
	});

}
/****
 * 查询素材信息
 */
function searchStatictis(){
    var eleName=$('#eleName').val();
    var terminalName=$('#terminalName').val();
    var starttime=$('#statictisstarttime').datetimebox('getValue');
    var endtime=$('#statictisendtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
    if(eleName!=""){
    	var eleName=eleName.replace("%","/%"); //转译，去空格特殊符号
    }
    if(terminalName!=""){
    	var terminalName=terminalName.replace("%","/%"); //转译，去空格特殊符号
    }
	$('#staticdg').datagrid('load',{
		page:1,
		rows:10,
		eleName:eleName,
		terminalName:terminalName,
		starttime:starttime,
		endtime:endtime
	});
 //  $('#eleName').val("");
 //  $('#terminalName').val("");
}
/****
 * 查询素材统计信息
 */
function searchStatictisTJ(){
    var eleName=$('#eleName').val();
    var starttime=$('#statictisstarttime').datetimebox('getValue');
    var endtime=$('#statictisendtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
    if(eleName!=""){
    	var eleName=eleName.replace("%","/%"); //转译，去空格特殊符号
    }
    $('#statictj').datagrid({
		width:'auto',
		striped:true,
		singleSelect:false,
		url:'statistics/querytongji.do',
		loadMsg:'数据加载中请稍后......',
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        pagination:true,
        queryParams:{eleName:eleName,starttime:starttime,endtime:endtime,page:1,rows:10},
        method:'post',
        rownumbers:true,
        columns:[[
                {field:'t_id',width:'10px',align:'center',title: '统计信息ID',hidden:true},
      			{field:'eleName',width:'170px',align:'center',title: '素材名称'}, 
      			{field:'type',width:'80px',align:'center',title: '素材类型',formatter:function(val,row){
      			 if(val!=null){
      				 switch(val){
      				 case 1:
      					 return '文本';
      				 case 2:
      					 return '音频';
      				 case 3:
      					 return '图片';
      				 case 4:
      					 return '视频';
      				 case 5:
      					 return '网页';
      				 case 6:
      					 return 'Falsh';
      				 case 7:
      					 return 'Office';
      				 case 8:
      					 return '流媒体';
      				 default:
      					 return '未知格式文件';
      				 }
     		    	}
      			}}, 
      			{field:'totaltime',width:'130px',align:'center',title: '素材播放时长(秒)'}, 
      			{field:'counts',width:'130px',align:'center',title: '素材播放次数'}
          ]]
	});
	var p=$('#statictj').datagrid('getPager');
	$(p).pagination({
		pageSize:10,
		pageList:[5,10,15],
		beforePageText:'第',
		afterPageText:'页 共{pages}页',
		displayMsg:'当前显示{from}-{to} 条记录 共{total} 条记录'
	});
}
/****
 * 删除播放日志
 * @return
 */
function statisticsDel(){
	  var tids=[];
	  rows=$('#staticdg').datagrid('getSelections');
	  if(rows.length==0){
		   ZENG.msgbox.show("请选择要删除的信息", 5, 1500); 
	  }else{
		  for(var i=0;i<rows.length;i++){
		    tids.push(rows[i].t_id);
		  }
	     $.messager.confirm('提示信息','你确定要删除吗？',function(r){
		    if(r){
			   $.ajax({
			      url:'statistics/statisticsDel.do',
			      data:{"tids":tids},
			      type:"post",
				  success:function(data){
				     if(data){
				    	  ZENG.msgbox.show("删除成功",4, 1500); 
						$('#staticdg').datagrid('reload');
					 }else{
				    	  ZENG.msgbox.show("删除失败", 5, 1500); 
					 }
				  }
			   })
			}
		 });
	  }
}
/****
 * 导出播放日志统计信息
 */
function exportProStatisticsTJData(){
	var terminalName=$('#proterminalName').val();
    var programName=$('#proName').val();
    var starttime=$('#prostarttime').datetimebox('getValue');
    var endtime=$('#proendtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
    /*if(eleName!=""){
    	var eleName=eleName.replace("%","/%"); //转译，去空格特殊符号
    }*/
	if ("ActiveXObject" in window){  //IE浏览器
	    window.location.href = '../programlog/exportProStatisticsTJData.do?terminalName='+terminalName+"&programName"+programName+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
	    window.location.href = 'programlog/exportProStatisticsTJData.do?terminalName='+terminalName+"&programName"+programName+"&starttime="+starttime+"&endtime="+endtime;
	}else if(isChrome){ //谷歌浏览器
	    window.location.href = 'programlog/exportProStatisticsTJData.do?terminalName='+terminalName+"&programName"+programName+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera')
    }else{  //其他浏览器
	    window.location.href = 'programlog/exportProStatisticsTJData.do?terminalName='+terminalName+"&programName"+programName+"&starttime="+starttime+"&endtime="+endtime;
    }
 }
/****
 * 导出素材统计信息
 */
function exportStatisticsTJData(){
    var eleName=$('#eleName').val();
    var starttime=$('#statictisstarttime').datetimebox('getValue');
    var endtime=$('#statictisendtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
    /*if(eleName!=""){
    	var eleName=eleName.replace("%","/%"); //转译，去空格特殊符号
    }*/
	if ("ActiveXObject" in window){  //IE浏览器
	    window.location.href = '../statistics/exportStatisticsTJData.do?eleName='+eleName+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
	    window.location.href = 'statistics/exportStatisticsTJData.do?eleName='+eleName+"&starttime="+starttime+"&endtime="+endtime;
	}else if(isChrome){ //谷歌浏览器
	    window.location.href = 'statistics/exportStatisticsTJData.do?eleName='+eleName+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera')
    }else{  //其他浏览器
	    window.location.href = 'statistics/exportStatisticsTJData.do?eleName='+eleName+"&starttime="+starttime+"&endtime="+endtime;
    }
 }
/****
 * 导出素材信息
 * @return
 */
function exportEleData(){
    var eleName=$('#eleName').val();
    var terminalName=$('#terminalName').val();
    var starttime=$('#statictisstarttime').datetimebox('getValue');
    var endtime=$('#statictisendtime').datetimebox('getValue');
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
 /*   if(eleName!=""){
    	var eleName=eleName.replace("%","/%"); //转译，去空格特殊符号
    }
    if(terminalName!=""){
    	var terminalName=terminalName.replace("%","/%"); //转译，去空格特殊符号
    }*/
	/*var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
	if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){  //IE浏览器
	    window.location.href = 'exportEleData.do?eleName='+eleName+"&terminalName"+terminalName+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
	    window.location.href = 'statistics/exportEleData.do?eleName='+eleName+"&terminalName"+terminalName+"&starttime="+starttime+"&endtime="+endtime;
	}else if(isChrome){ //谷歌浏览器
	    window.location.href = 'statistics/exportEleData.do?eleName='+eleName+"&terminalName"+terminalName+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera');
    }else{  //其他浏览器
	    window.location.href = 'statistics/exportEleData.do?eleName='+eleName+"&terminalName"+terminalName+"&starttime="+starttime+"&endtime="+endtime;
    }*/
	$('#downWin').window('open').dialog('setTitle', '下载列表');
	$('#downTB').datagrid({
		width:'auto',
		striped:true,
		singleSelect:false,
		url:'statistics/queryStatisticsPartCount.do',
		loadMsg:'数据加载中请稍后......',
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
      //  pagination:true,
        queryParams:{eleName:eleName,terminalName:terminalName,starttime:starttime,endtime:endtime},
        method:'post',
        rownumbers:true,
        columns:[[
      			{field:'curret',width:'200px',align:'center',title: '名称',formatter:function(value,row){
                	var val=value;
                	if(val!=null && val!=""){
                		return '第'+val+"部分";
                	}else{
                		return null;
                	}
      			}}, 
      			{field:'eleName',width:'200px',align:'center',title: '下载',formatter:function(value,rowData,rowIndex){
					var divStr = "<a onclick='downloadStatistics(\""
						  +rowData.elemName
						  +"\",\""
						  +rowData.terminalName
						  +"\",\""
						  +rowData.starttime
						  +"\",\""
						  +rowData.endtime
						  +"\",\""
						  +rowData.startCount
						  +"\",\""
						  +rowData.endCount
						  +"\")'<font onmouseOver='this.style.cursor=\"pointer\"' color='blue'>点此下载</font></a>"
				    return divStr;
		      }},
      			{field:'starttime',hidden:true}, 
      			{field:'endtime',hidden:true}, 
      			{field:'terminalName',hidden:true},
      			{field:'startCount',hidden:true},
      			{field:'endCount',hidden:true}
          ]]
	});
}
/****
 * 调用下载方法
 * @param eleName
 * @param starttime
 * @param endtime
 * @param teminalName
 * @param startCount
 * @param endCount
 * @return
 */
function downloadStatistics(eleName,terminalName,starttime,endtime,startCount,endCount){
	var starttime=$('#statictisstarttime').datetimebox('getValue');
	var endtime=$('#statictisendtime').datetimebox('getValue');
	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
	if ("ActiveXObject" in window){ //IE浏览器
		var html = "../statistics/exportEleExcel.do?eleName="+eleName+"&terminalName="+terminalName+"&startCount="+startCount+"&endCount="+endCount;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
		var html = "statistics/exportEleExcel.do?eleName="+eleName+"&terminalName="+terminalName+"&startCount="+startCount+"&endCount="+endCount;
	}else if(isChrome){ //谷歌浏览器
		var html = "statistics/exportEleExcel.do?eleName="+eleName+"&terminalName="+terminalName+"&startCount="+startCount+"&endCount="+endCount;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera浏览器');
    }else{  //其他浏览器
    	var html = "statistics/exportEleExcel.do?eleName="+eleName+"&temrinalName="+terminalName+"&startCount="+startCount+"&endCount="+endCount;
    }
	if(starttime!="null"&&starttime!=null){
		html +="&starttime="+starttime;
	}
	if(endtime!="null"&&endtime!=null){
		html +="&endtime="+endtime;
	}
	location.href = html;
 	
}
/****
 * 导出操作日志信息excel
 * @return
 */
function importExcel(){
	var userName=$('#userName').val();
	var operationName=$('#operationName').val();
	var starttime=$('#starttime').datetimebox('getValue');
	var endtime=$('#endtime').datetimebox('getValue');
	var allModule=$('#allModule').combobox('getValue');
	var allOpera=$('#allOpera').combobox('getValue');
	var userName=userName.replace("%","/%"); //转译，去空格特殊符号
	var operationName=operationName.replace("%","/%"); //转译，去空格特殊符号
    if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
        return false;
    }
	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
	/*if(isChrome){//谷歌浏览器
	    window.location.href = 'operationlog/exportExcel.do?userName='+userName+"&operationName="+operationName+"&starttime="+starttime+"&endtime="+endtime+"&operationType="+allOpera+"&operationModule="+allModule;
	}else{//IE浏览器
	    window.location.href = 'exportExcel.do?userName='+userName+"&operationName="+operationName+"&starttime="+starttime+"&endtime="+endtime+"&operationType="+allOpera+"&operationModule="+allModule;
	}*/
	if ("ActiveXObject" in window){ //IE浏览器
	    window.location.href = 'exportExcel.do?userName='+userName+"&operationName="+operationName+"&starttime="+starttime+"&endtime="+endtime+"&operationType="+allOpera+"&operationModule="+allModule;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
	    window.location.href = 'operationlog/exportExcel.do?userName='+userName+"&operationName="+operationName+"&starttime="+starttime+"&endtime="+endtime+"&operationType="+allOpera+"&operationModule="+allModule;
	}else if(isChrome){ //谷歌浏览器
	    window.location.href = 'operationlog/exportExcel.do?userName='+userName+"&operationName="+operationName+"&starttime="+starttime+"&endtime="+endtime+"&operationType="+allOpera+"&operationModule="+allModule;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera')
    }else{  //其他浏览器
	    window.location.href = 'operationlog/exportExcel.do?userName='+userName+"&operationName="+operationName+"&starttime="+starttime+"&endtime="+endtime+"&operationType="+allOpera+"&operationModule="+allModule;
    }
}
/****
 * 导出触摸信息
 */
function exportCMExcel(){
	var sceneNameofButton=$('#sceneNameofButton').val();
	var sceneNameofJumpbutton=$('#sceneNameofJumpbutton').val();
	var buttonType=$('#buttonType').combobox('getValue');
	if(buttonType==5){
		buttonType=0;
	}
	var starttime=$('#clickStarttime').datetimebox('getValue');
	var endtime=$('#clickEndtime').datetimebox('getValue');
	if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
	        return false;
	}
  /*  if(sceneNameofButton!=""){
    	var sceneNameofButton=sceneNameofButton.replace("%","/%"); //转译，去空格特殊符号
    }
    if(buttonType!=""){
    	var buttonType=buttonType.replace("%","/%"); //转译，去空格特殊符号
    }*/
	
	$('#downWin2').window('open').dialog('setTitle', '下载列表');
	$('#downTB2').datagrid({
		width:'auto',
		striped:true,
		singleSelect:false,
		url:'statisticsButton/queryStatisticsButtonPartCount.do',
		loadMsg:'数据加载中请稍后......',
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		queryParams:{sceneNameofButton:sceneNameofButton,sceneNameofJumpbutton:sceneNameofJumpbutton,buttonType:buttonType,starttime:starttime,endtime:endtime},
        method:'post',
        columns:[[
      			{field:'curret',width:'200px',align:'center',title: '名称',formatter:function(value,row){
                	var val=value;
                	if(val!=null && val!=""){
                		return '第'+val+"部分";
                	}else{
                		return null;
                	}
      			}}, 
      			{field:'sceneNameofButton',width:'200px',align:'center',title: '下载',formatter:function(value,rowData,rowIndex){
					var divStr = "<a onclick='downloadStatisticsButon(\""
						  +rowData.sceneNameofButton
						  +"\",\""
						  +rowData.sceneNameofJumpbutton
						  +"\",\""
						  +rowData.buttonType
						  +"\",\""
						  +rowData.starttime
						  +"\",\""
						  +rowData.endtime
						  +"\",\""
						  +rowData.startCount
						  +"\",\""
						  +rowData.endCount
						  +"\")'<font onmouseOver='this.style.cursor=\"pointer\"' color='blue'>点此下载</font></a>"
				    return divStr;
		      }},
      			{field:'starttime',hidden:true}, 
      			{field:'endtime',hidden:true}, 
      			{field:'sceneNameofJumpbutton',hidden:true},
      			{field:'buttonType',hidden:true},
      			{field:'startCount',hidden:true},
      			{field:'endCount',hidden:true}
        ]]
	});
}
//导出触摸信息（分段导出）
function downloadStatisticsButon(sceneNameofButton,sceneNameofJumpbutton,buttonType,starttime,endtime,startCount,endCount){
	var sceneNameofButton=$('#sceneNameofButton').val();
	var sceneNameofJumpbutton=$('#sceneNameofJumpbutton').val();
	var buttonType=$('#buttonType').combobox('getValue');
	if(buttonType==5){
		buttonType=0;
	}
	var starttime=$('#clickStarttime').datetimebox('getValue');
	var endtime=$('#clickEndtime').datetimebox('getValue');
	if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
	        return false;
	}
 /*   if(sceneNameofButton!=""){
    	var sceneNameofButton=sceneNameofButton.replace("%","/%"); //转译，去空格特殊符号
    }
    if(buttonType!=""){
    	var buttonType=buttonType.replace("%","/%"); //转译，去空格特殊符号
    }*/
    var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
	if ("ActiveXObject" in window){ //IE浏览器
	    window.location.href = 'statisticsButton/exportData.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime+"&startCount="+startCount+"&endCount="+endCount;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
	    window.location.href = 'statisticsButton/exportData.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime+"&startCount="+startCount+"&endCount="+endCount;
	}else if(isChrome){ //谷歌浏览器
	    window.location.href = 'statisticsButton/exportData.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime+"&startCount="+startCount+"&endCount="+endCount;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera')
    }else{  //其他浏览器
	    window.location.href = 'statisticsButton/exportData.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime+"&startCount="+startCount+"&endCount="+endCount;
    }
}
/****
 * 导出触摸统计信息
 */
function exportCMTJExcel(){
	var sceneNameofButton=$('#sceneNameofButton').val();
	var sceneNameofJumpbutton=$('#sceneNameofJumpbutton').val();
	var buttonType=$('#buttonType').combobox('getValue');
	if(buttonType==5){
		buttonType=0;
	}
	var starttime=$('#clickStarttime').datetimebox('getValue');
	var endtime=$('#clickEndtime').datetimebox('getValue');
	if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
	        return false;
	}
/*    if(sceneNameofButton!=""){
    	var sceneNameofButton=sceneNameofButton.replace("%","/%"); //转译，去空格特殊符号
    }
    if(buttonType!=""){
    	var buttonType=buttonType.replace("%","/%"); //转译，去空格特殊符号
    }*/
    var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
	if ("ActiveXObject" in window){ //IE浏览器
	    window.location.href = '../statisticsButton/exportCMExcel.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Firefox') >= 0){ //火狐浏览器
	    window.location.href = 'statisticsButton/exportCMExcel.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime;
	}else if(isChrome){ //谷歌浏览器
	    window.location.href = 'statisticsButton/exportCMExcel.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime;
	}else if (navigator.userAgent.indexOf('Opera') >= 0){
        alert('你是使用Opera')
    }else{  //其他浏览器
	    window.location.href = 'statisticsButton/exportCMExcel.do?sceneNameofButton='+sceneNameofButton+"&sceneNameofJumpbutton="+sceneNameofJumpbutton+"&buttonType="+buttonType+"&starttime="+starttime+"&endtime="+endtime;
    }
}
/****
 * 操作日志重置搜索
 * @return
 */
function reset(){
	var userName=$('#userName').val("");
	var operationName=$('#operationName').val("");
	var starttime=$('#starttime').datetimebox('clear');
	var endtime=$('#endtime').datetimebox('clear');
	var allModule=$('#allModule').combobox('setValue',"");
	var allOpera=$('#allOpera').combobox('setValue',"");
	  $('#logdg').datagrid('load',{
	    	page:1,
	    	rows:10
	   });
}
/**
 * 播放日志搜索重置
 */
function prostatisticsReset(){
	var terminalName=$('#proterminalName').val("");
    var programName=$('#proName').val("");
    var starttime=$('#prostarttime').datetimebox('clear');
    var endtime=$('#proendtime').datetimebox('clear');
    $('#programlogdg').datagrid('load',{
	    	page:1,
	    	rows:10
	   })
}
/****
 * 统计重置搜索
 * @return
 */
function statisticsReset(){
	var eleName=$('#eleName').val("");
	var terminalName=$('#terminalName').val("");
	var starttime=$('#statictisstarttime').datetimebox('clear');
	var endtime=$('#statictisendtime').datetimebox('clear');
	if(i==1){
	  $('#staticdg').datagrid('load',{
	    	page:1,
	    	rows:10
	   })
	}else if(i==2){
	  $('#staticdg').datagrid('load',{
	    	page:1,
	    	rows:10
	   })
	}else if(i==3){
	  $('#statictj').datagrid('load',{
	    	page:1,
	    	rows:10
	   })
	}else{
		var sceneNameofButton=$('#sceneNameofButton').val("");
		var sceneNameofJumpbutton=$('#sceneNameofJumpbutton').val("");
		var buttonType=$('#buttonType').combobox('clear');
		var starttime=$('#clickStarttime').datetimebox('clear');
		var endtime=$('#clickEndtime').datetimebox('clear');
		$('#CMDiv').datagrid('load',{
			page:1,
			rows:10
		})
	}
}

/***
 * 时间验证
 * @return
 */
function checkDate(){
	$('#starttime').datetimebox({
	 onSelect:function(date){
	   var time=$('#starttime').datetimebox('spinner').spinner('getValue');
	   $('#starttime').datetimebox('setValue',date.getFullYear()+'-'+ ((date.getMonth()+1)<10 ? ('0'+(date.getMonth()+1)) : (date.getMonth()+1))+'-'+((date.getDate())<10 ? ('0'+(date.getDate())) : (date.getDate()))+' '+time);
	   var starttime=$('#starttime').datetimebox('getValue');
	   var endtime=$('#endtime').datetimebox('getValue');
	   if(starttime && endtime){
		if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
			$('#starttime').datetimebox('setValue',"");
            return false;
         }
	  }
    }
});
$('#endtime').datetimebox({
	 onSelect:function(date){
	   var time=$('#endtime').datetimebox('spinner').spinner('getValue');
	   $('#endtime').datetimebox('setValue',date.getFullYear()+'-'+ ((date.getMonth()+1)<10 ? ('0'+(date.getMonth()+1)) : (date.getMonth()+1))+'-'+((date.getDate())<10 ? ('0'+(date.getDate())) : (date.getDate()))+' '+time);
	   var starttime=$('#starttime').datetimebox('getValue');
	   var endtime=$('#endtime').datetimebox('getValue');
	   if(starttime && endtime){
		if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
			$('#endtime').datetimebox('setValue',"");
            return false;
         }
	  }
    }
});
/***统计日志条件查询时间验证****/
$('#statictisstarttime').datetimebox({
	onSelect:function(date){
	var time=$('#statictisstarttime').datetimebox('spinner').spinner('getValue');
	$('#statictisstarttime').datetimebox('setValue',date.getFullYear()+'-'+ ((date.getMonth()+1)<10 ? ('0'+(date.getMonth()+1)) : (date.getMonth()+1))+'-'+((date.getDate())<10 ? ('0'+(date.getDate())) : (date.getDate()))+' '+time);
	var starttime=$('#statictisstarttime').datetimebox('getValue');
	var endtime=$('#statictisendtime').datetimebox('getValue');
	if(starttime && endtime){
		if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
			$('#statictisstarttime').datetimebox('setValue',"");
			return false;
		}
	}
}
});
$('#statictisendtime').datetimebox({
	onSelect:function(date){
	var time=$('#statictisendtime').datetimebox('spinner').spinner('getValue');
	$('#statictisendtime').datetimebox('setValue',date.getFullYear()+'-'+ ((date.getMonth()+1)<10 ? ('0'+(date.getMonth()+1)) : (date.getMonth()+1))+'-'+((date.getDate())<10 ? ('0'+(date.getDate())) : (date.getDate()))+' '+time);
	var starttime=$('#statictisstarttime').datetimebox('getValue');
	var endtime=$('#statictisendtime').datetimebox('getValue');
	if(starttime && endtime){
		if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1500);
			$('#statictisendtime').datetimebox('setValue',"");
			return false;
		}
	}
}
});
}

/**按钮权限**/
function init(){
	$.ajax({ 
    	url: "modulepower/queryModulePowerID.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	async: false,
    	success: function(result){
			for(var i=0;i<result.moduleList.length;i++){
				if(result.moduleList[i]==30){
					$("#importLog").show();
				}
			}
      	}
      });

}

function errorReset(){
	var errorModule = $("#errorModule").combobox('setValue','1');
	var errordetail = $("#errordetail").val('');
	var starttime=$('#errorstarttime').datetimebox('clear');
	var endtime=$('#errorendtime').datetimebox('clear');
	$('#errorlogdg').datagrid('load',{
    	page:1,
    	rows:10
    })
}