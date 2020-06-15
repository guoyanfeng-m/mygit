$(document).ready(function(){
	 loadGrid();
	 loadtree();
	 change();
	  $("#cp3").colorpicker({
          fillcolor:true,
          success:function(o,color){
              $("#font_color").css("background-color",color);
              $("#font_color").val(RGBToHex( $("#font_color").css("background-color")));
		 }
      }); 
	  $('#cp3').click(function(){
		  $("#HexColor").val(RGBToHex( $("#font_color").css("background-color")));
          $("#DisColor").css("background-color",$('#font_color').val()); 
	  })
});
//查看终端
function searchTerminal(new_id,isSend){
	if(isSend!=1){
		 ZENG.msgbox.show("选择的信息还没有发布!", 3, 1000);
		return;
	}else{
		$('#scrollterminal').window('open').dialog('setTitle', '滚动消息终端列表');
		$('#terminallist').datagrid({
			width:'auto',
			height:450,
			//pagination: true, //分页控件 
			//queryParams:{page:1,rows:10},
			nowrap:true, //设置为true,当数据长度超出列宽的时候将会自动截取
			striped:true,  //设置为true将交替显示行背景
			collapsible : true,//显示可折叠按钮
		    url:'queryTerminalGroup.do?new_id='+new_id,//url调用Action方法
		    loadMsg : '数据装载中......',
		   // singleSelect:true,//为true时只能选择单行
		    fitColumns:true,//允许表格自动缩放，以适应父容器
		    //sortName : 'xh',//当数据表格初始化时以哪一列来排序
		    //sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
		     remoteSort : false,
		     frozenColumns : [[{
		         field : 'ck',
		         checkbox : true
		     }]],
		     rownumbers : true,//行数,
		     columns:[[
		        {field:'terminal_id',title:'终端ID',align:'center',width:'70px',hidden:true},
		        {field:'terminal_name',title:'终端名称',align:'center',width:'172px'},
		        {field:'ip',title:'终端IP',align:'center',width:'158px'},
		        {field:'mac',title:'终端MAC',align:'center',width:'158px'},
		        {field:'groupName',title:'所属终端组名称',align:'center',width:'190px'}
		     ]],
		     toolbar:[{
		    	 text:'停止命令',
		    	 iconCls:'icon-cancel',
		    	 handler:function(){
		    	 var rows=$("#terminallist").datagrid("getSelections");
		    	 if(rows.length==0){
		    		 ZENG.msgbox.show("请选择要停止终端!", 3, 1000);
		    		// $.messager.alert("提示信息","请选择要停止终端");
		    		 return;
		    	 }
		    	 var terminalIds=new Array();
		    	 var mac=new Array();
		    	 for(i in rows){
		    		 terminalIds.push(rows[i].terminal_id);
		    		 mac.push(rows[i].mac);
		    	 }
		    	  $.messager.defaults = { ok: "确定", cancel: "取消" };
		    	     $.messager.confirm('提示信息','你确定要发送停止命令吗？',function(r){
		    		    if(r){
		    		    	$.ajax({
		    		    		url:'scrollingnews/stopOneCommand.do',
		    		    		type:'post',
		    		    		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		    		    		data:{"new_id":new_id,"terminalIds":terminalIds,"mac":mac},
		    		    		success:function(){
		    		    			ZENG.msgbox.show("操作成功!", 4, 1000);
		    		    			$('#terminallist').datagrid('reload');
		    		    			$('#dg').datagrid('reload');
		    		    		}
		    		    	});
		    		    }
		    	     });
		        }
		     }]
		  });
	}
}
//背景颜色转换
function RGBToHex(rgb){ 
	   var regexp = /[0-9]{0,3}/g;  
	   var re = rgb.match(regexp);//利用正则表达式去掉多余的部分，将rgb中的数字提取
	   var hexColor = "#"; var hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'];  
	   for (var i = 0; i < re.length; i++) {
	        var r = null, c = re[i], l = c; 
	        var hexAr = [];
	        while (c > 16){  
	              r = c % 16;  
	              c = (c / 16) >> 0; 
	              hexAr.push(hex[r]);  
	         } hexAr.push(hex[c]);
	         if(l < 16&&l != ""){        
	             hexAr.push(0)
	         }
	       hexColor += hexAr.reverse().join(''); 
	    }  
	   return hexColor;  
	}
function loadGrid(){ 
	    //加载数据  
	    $('#dg').datagrid({  
	                width: 'auto',                
	                striped: true,  
	                singleSelect : false,  
	                url:'scrollingnews/queryAll.do',  
	                loadMsg:'数据加载中请稍后……',  
	                contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	                pagination: true, //分页控件 
	    	    	pageSize:10,
	                queryParams:{page:1,rows:10},
	                method:'POST',
	                rownumbers: true, 
	                frozenColumns : [[{
			         field : 'ck',
			         checkbox : true
			        }]],
	                columns:[[  
					    {field:'new_id',width:'100px',align:'center',title: '消息ID',hidden:true},
						{field:'sname',width:'136px',align:'center',title: '消息名称'}, 
						{field:'text',width:'100px',align:'center',title: '消息内容'}, 
						{field:'font',width:'100px',align:'center',title: '字体'}, 
						{field:'font_size',width:'70px',align:'center',title: '字体大小'}, 
						{field:'font_color',width:'100px',align:'center',title: '字体颜色',hidden:true},
						{field:'scroll_direction',width:'80px',align:'center',title: '滚动方向'}, 
						{field:'scroll_speed',width:'80px',align:'center',title: '滚动速度',hidden:true}, 
						{field:'start_time',width:'170px',align:'center',title: '开始时间'},
						{field:'end_time',width:'170px',align:'center',title: '结束时间'},                                                         
						{field:'update_time',width:'170px',align:'center',title: '修改时间',hidden:true,formatter:function(val,row){
							if (val != null) {
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
							}
					      }}, 
						{field:'realname',width:'100px',align:'center',title: '创建者'} ,                                                         
						{field:'audit_status',width:'100px',align:'center',title: '审核状态',formatter:function(val,row){
							if(val==0 || val==3){
								return '未审核';
							}else if(val==1){
								return '审核通过';
							}else{
								return '<span style="color:red">未通过审核</span>';
							}
						}},                                                          
						{field:'isSend',width:'70px',align:'center',title: '是否发布',formatter:function(val,row){
							if(val==1){
								return '是';
							}else{
								return '否';
							}
						}},
						{field:'search',align:'center',width:'90px', title:'查看终端',formatter:function(value,rec){ 
			                var btn = '<a class="editcls" onclick="searchTerminal(\''+rec.new_id+'\',\''+rec.isSend+'\')" href="javascript:void(0)">查询终端</a>';  
			                return btn;  
			            }}
	                ]],
	                 onLoadSuccess:function(data){  
                        $('.editcls').linkbutton({plain:true,iconCls:'icon-search'});  
                     } 
	            });  
	    var p = $('#dg').datagrid('getPager'); 
	    $(p).pagination({ 
	    	pageList: [5,10,15],//可以设置每页记录条数的列表 
	       // showPageList:false,
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录' ,
	        showRefresh:false
	    });  
	}  

//条件搜索
function doSearch(){
        var name=$("#searchName").val().replace("%","/%"); //转译，去空格特殊符号
		$('#dg').datagrid('load',{
			page : 1,
			rows : 10,
			sname : name
    	});
}
//刷新
function refresh(){
	$('#searchName').textbox('setValue',"");
	$("#dg").datagrid("reload",{
		sname : ""
	});
}
//加载tree
function loadtree(){
	$('#sendtree').tree({  
	        checkbox: true,  //定义是否在每个节点前边显示 checkbox 
	        animate:true,   //定义当节点展开折叠时是否显示动画效果
	        lines:false,  
	       // onlyLeafCheck:true,  //定义是否只在叶节点前显示 checkbox
	        url:'scrollingnews/queryTree.do',
	        cache:true,  //cache必须设置为false,意思为不缓存当前页，否则更改权限后绑定的权限还是上一次的操作结果
	        onClick:function(node){
		      //alert(node.id);   
		
	        },
	        onLoadSuccess: function(node,data){
				var nodes =$('#sendtree').tree('getChildren');  
				for(i=0;i<nodes.length;i++){
					if(nodes[i].id>=40000){
				     $('#sendtree').tree('update', {
				            target: nodes[i].target,
				            iconCls: 'tree-terminal'
				        });
					}else{
						$('#sendtree').tree('update', {
				            target: nodes[i].target,
				            iconCls: 'tree-group'
				        });
					  }
				   };
		     },
		     onSelect : function(node) {
					if (node.checked) {
						$('#sendtree').tree('uncheck',node.target);
					} else {
						$('#sendtree').tree('check',node.target);
					}
				}
			});
}
//弹出发布页面
function publishWin(){
	var nowdate=new Date();
	$("#sendtree").tree("reload");
	 var rows=$("#dg").datagrid('getSelections');
     if(rows.length==0){
    	 ZENG.msgbox.show("请先选择一条数据!", 3, 1000);
    	 return;
     }
     var audit_status=new Array();
	 for(x in rows){   //得到选择的消息ID
		 audit_status[x]=rows[x].audit_status;
		 if(audit_status[x]!=1){
		    ZENG.msgbox.show("选择的信息中包含未审核或者审批未通过的项!", 3, 1000);
		    return;
	    }
		 var end_time=rows[x].end_time; //当前时间
		 var endtime=new Date(end_time.replace("-", "/").replace("-", "/")); //结束时间
		 if(endtime<=nowdate){
			 ZENG.msgbox.show("选择的信息中包含播放时间过期的项!", 3, 1000);
			 return; 
		 }
	 }
	 $('#tree').window('open').dialog('setTitle', '发布终端');
}
/****
 * 发布
 * @return
 */
function publish(){
	     var rows=$("#dg").datagrid('getSelections'); 
    	 var terminalIds=new Array();
    	 var new_ids=new Array();
    	 for(x in rows){   //得到选择的消息ID
    		 new_ids[x]=rows[x].new_id;
    	 }
         var checked=$("#sendtree").tree("getChecked");  //得到选择的终端
         for(var i=0;i<checked.length;i++){
       	   if(checked[i].id<40000){  //去除终端组
       		   continue;
       	   }
       	   else{
       		    if(terminalIds.toString().indexOf(checked[i].id)>-1){  //去掉重复的终端ID
       		    	continue;
       		    }else{
       		    	terminalIds.push(checked[i].id);
       		    }
       	    }
         }
         if(terminalIds.length==0){
        	ZENG.msgbox.show("请先选择终端!", 3, 1000);
        	 return;
         }
        else{
    	    $.ajax({
   			   url:'scrollingnews/publish.do',
		       contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		       data:{"terminalIds":terminalIds,"new_ids":new_ids},
		       type:'post',
			   success:function(){
		    	   ZENG.msgbox.show("发布成功!", 4, 1000); 
				  $("#tree").dialog("close");
		          $('#dg').datagrid('reload');
    		   }
       	    }); 
         }
     }
/****
 * 发布停止命令
 * @return
 */
function stopCommand(){
	var new_ids=new Array();
    var rows=$("#dg").datagrid('getSelections'); 
    if(rows.length==0){
    	ZENG.msgbox.show("请先选择一条数据!", 3, 1000);
    	return;
    }
    for(m in rows){
    	if(rows[m].isSend!=1){
    		ZENG.msgbox.show("没有发布的信息不能执行此命令!", 3, 1000);
    		return;
    	}
    	new_ids.push(rows[m].new_id);
    }
    $.messager.defaults = { ok: "确定", cancel: "取消" };
    $.messager.confirm('提示信息','你确定要发送停止命令吗？',function(r){
	    if(r){
		    $.ajax({
				   url:'scrollingnews/stopCommand.do',
			       contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			       data:{"new_ids":new_ids},
			       type:'post',
				   success:function(){
			    	   ZENG.msgbox.show("命令发布成功", 4, 1000); 
			          $('#dg').datagrid('reload');
				   }
		   	    });
	    }
    });
   }

//弹出添加页面
function add(){
    $("#colorpanel").hide();  //关闭颜色面板
	page=0;
   $("#dlg").window("open").dialog('setTitle', '添加滚动消息');
   $("#fm").form("clear");
   $("#fm").form('load',{
	   font_size:100,
	   scroll_direction:3,
	   font:0,
	   scroll_speed:10
   });
   var date=new Date();
   var month=date.getMonth()+1;
   var day=date.getDate();
   if(month<10){
	   month="0"+month;
   }
   if(day<10){
	   day="0"+day;
   }
   var starttime=date.getFullYear()+"-"+month+"-"+day;
   $("#start_time").datetimebox('setValue',starttime+" "+"00:00:00");
   $("#end_time").datetimebox('setValue',starttime+" "+"23:59:59");
   document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
   $("#font_color").val("#000000");
   $("#font_color").css("background-color","#000000");
   $("#ceshi111").val("#000000");
}

//添加和修改
function save(){
	if(checkDate()==true) //验证数据
	{
		 if(page==0){
			    var new_id=$("#new_id").val();
			    var name=$("#sname").val().split(' ').join('');
			    if(name.length>50){
			    	 ZENG.msgbox.show("滚动消息名称不能大于50",3, 1000); 
			    	 return;
			    }
			    var testStr=$("#text").val();
			    if(testStr.length>5000){
			    	 ZENG.msgbox.show("滚动消息内容不能超过5000个字",3, 1000); 
			    	 return;
			    }
				var text=testStr.replace(/[\r\n]/g,"");
				var textSize=$("#font_size").val();
				var textColor=$("#ceshi111").val();
				var scrollDirection=$("#scroll_direction").find("option:selected").text();
				var scrollSpeed=$("#scroll_speed").val();
				var font=$("#font").find("option:selected").text();
				var start_time=$("#start_time").datetimebox("getValue");
				var end_time=$("#end_time").datetimebox("getValue");
				var datas={"sname":name,"text":text,"font_size":textSize,"font_color":textColor,"scroll_direction":scrollDirection,"scroll_speed":scrollSpeed,"font":font,"start_time":start_time,"end_time":end_time}; 
				$.ajax({
					 url:'scrollingnews/checkName.do',
				     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
				     data:{"new_id":new_id,"sname":name,"type":"add"},
				     type:'post',
					 success:function(data){
					     if(data.success==false){
					 		$.ajax({
								 url:'scrollingnews/insertScroll.do',
							     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
							     data:datas,
							     type:'post',
								 success:function(){
					 			  ZENG.msgbox.show("添加成功", 4, 1000); 
									  $("#dlg").dialog("close");
							          $('#dg').datagrid('reload');
							          $("#fm").form("clear");
							          document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
								 }
							});
					     }else{
					    	 ZENG.msgbox.show("名称已经存在！", 3, 1000); 
					     }
					 }
				});
		
		  }else {
			    var new_id=$("#new_id").val();
			    var name=$("#sname").val().split(' ').join('');
			    if(name.length>50){
			    	 ZENG.msgbox.show("滚动消息名称不能大于50个字", 3, 1000); 
			    	 return;
			    }
			    var testStr=$("#text").val();
			    if(testStr.length>5000){
			    	 ZENG.msgbox.show("滚动消息内容不能超过5000个字",3, 1000); 
			    	 return;
			    }
			    var text=testStr.replace(/[\r\n]/g,"");
			    var textSize=$("#font_size").val();
				var textColor=$("#ceshi111").val();
				var scrollDirection=$("#scroll_direction").find("option:selected").text();
				var scrollSpeed=$("#scroll_speed").val();
				var font=$("#font").find("option:selected").text();
				var start_time=$("#start_time").datetimebox("getValue");
				var end_time=$("#end_time").datetimebox("getValue");
				var datas={"new_id":new_id,"sname":name,"text":text,"font_size":textSize,"font_color":textColor,"scroll_direction":scrollDirection,"scroll_speed":scrollSpeed,"font":font,"start_time":start_time,"end_time":end_time};
				$.ajax({
					 url:'scrollingnews/checkName.do',
				     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
				     data:{"new_id":new_id,"sname":name,"type":"edit"},
				     type:'post',
					 success:function(data){
					     if(data.success==false){
				    		$.ajax({
								 url:'scrollingnews/updateScroll.do',
							     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
							     data:datas,
							     type:'post',
								 success:function(){
				    			     ZENG.msgbox.show("修改成功", 4, 1000); 
									  $("#dlg").dialog("close");
							          $('#dg').datagrid('reload');
								 }
							});
					     }else{
					    	 ZENG.msgbox.show("名称已经存在!", 3,1000);
					     }
				     }
				});
			
		    }
	}
}
//编辑页面
function update(){
      $("#colorpanel").hide();  //关闭颜色面板
	  var n=1;
	  n+=n;
	  page=1;
	  $("#fm").form("clear");
   var row=$("#dg").datagrid('getSelections');
   if(row.length==0){
	   ZENG.msgbox.show("请选择一条信息", 3, 1000); 
   }else if(row.length>1){
	   ZENG.msgbox.show("每次只能选择一条信息", 3, 1000); 
   }else{
      $("#dlg").dialog("open").dialog("setTitle","编辑页面");
      $("#start_time").textbox("setValue",row[0].start_time);
	  $("#end_time").textbox("setValue",row[0].end_time);
	  $("#fm").form("load",row[0]);
	  if(n!=1){ 
			document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
	  }
	  var font=aa(row[0].font);
	  var scroll_direction=bb(row[0].scroll_direction);
	  $("#fm").form('load',{
		   scroll_direction:scroll_direction,
		   font:font
	   });
	var testStr=row[0].text;
	var text=testStr.replace(/[\r\n]/g,"");//去掉空格换行回车
	var textSize=row[0].font_size;
	$("#font_color").css("background-color",row[0].font_color);
	var textColor=RGBToHex($("#font_color").css("background-color"));
 
	var scrollDirection=row[0].scroll_direction;
	/* if(scrollDirection=="right"){  //反转
		  text=text.split("").reverse().join("");
	  }*/
	var scrollSpeed=row[0].scroll_speed;
	var font=row[0].font;
  if(scrollDirection=="right"){
	$("#yulan").append('<div class="c1"><marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee></div>');
  }else if(scrollDirection=="left"){
	$("#yulan").append('<marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee>');
  }else if(scrollDirection=="up"){
	$("#yulan").append('<marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div>');
  }else if(scrollDirection=="down"){
	$("#yulan").append('<div style="position: relative;width:100%;height:96%;"><div style="width:100%;position:absolute;bottom:0px;"><marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div></div>');
	   }
   }
}

//删除
function del(){
  var nowDate=new Date();
  var ids=[];
  var rows=$("#dg").datagrid('getSelections');
  if(rows.length==0){
	   ZENG.msgbox.show("请选择要删除的信息", 3, 1000); 
	   return;
  }
  else{
	  for(var i=0;i<rows.length;i++){
	    if(rows[i].isSend==1 && new Date(rows[i].end_time.replace(/\-/g, '\/')) >nowDate){
	    	  ZENG.msgbox.show("消息还在播放日期内，请先停止再删除", 3, 1000); 
	    	return;
	    }
	    ids.push(rows[i].new_id);
	  }
	  $.messager.defaults = { ok: "确定", cancel: "取消" };
     $.messager.confirm('提示信息','你确定要删除吗？',function(r){
	    if(r){
		   $.ajax({
		      url:'scrollingnews/delScroll.do',
		      data:{"new_ids":ids},
		      type:"post",
			  success:function(data){
			     if(data){
			    	  ZENG.msgbox.show("删除成功",4, 1000); 
					  $('#dg').datagrid('reload');
				 }else{
			    	  ZENG.msgbox.show("删除失败", 3, 1000); 
				 }
			  }
		   })
		}
	 });
  }
}
/****
 * 审核
 * @return
 */
function audit(val){
   var ids=new Array();
   var rows=$('#dg').datagrid('getSelections')
   if(rows.length==0){
	   ZENG.msgbox.show("请选择要审批的信息", 3, 1000); 
	   return;
   }
   for(var i=0;i<rows.length;i++){
	   if(rows[i].audit_status==1 || rows[i].audit_status==2){
		   ZENG.msgbox.show("不能选择已经审批的项", 3, 1000); 
           return;
	   }
	   ids.push(rows[i].new_id);
   }
   $.messager.defaults = { ok: "审核通过", cancel: "审核不通过" };
	$.messager.confirm("操作提示", "审核消息,审核后将不能更改，请慎重选择！", function (data) {
	    if (data) {
	    	auditFun(ids,1);
	    }else{
	    	auditFun(ids,2);
	    }
	});
}
function auditFun(ids,i){
   $.ajax({
	      url:'scrollingnews/audit_statis.do',
	      data:{"new_ids":ids,"val":i},
	      type:"post",
		  success:function(data){
		     if(data){
		    	  ZENG.msgbox.show("操作成功",4, 1000); 
		    	  $('#auditWin').dialog('close');
				  $('#dg').datagrid('reload');
			 }else{
		    	  ZENG.msgbox.show("操作失败", 3, 1000); 
			 }
		  }
	   });
}
/****
 * 预览
 * @return
 */
function change(){
	 $("#font").change(function(){
		 document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
		  var testStr=$("#text").val();
		  var text=testStr.replace(/[\r\n]/g,"");//去掉空格换行回车
		  var textSize=$("#font_size").val();
	      var textColor=RGBToHex($("#font_color").css("background-color"));
		  var scrollDirection=$("#scroll_direction").find("option:selected").text();
		 /* if(scrollDirection=="right" || scrollDirection=="down"){  //反转
			  text=text.split("").reverse().join("");
		  }*/
		  var scrollSpeed=$("#scroll_speed").val();
		  var font=$("#font").find("option:selected").text();
		  if(scrollDirection=="right"){
			$("#yulan").append('<div class="c1"><marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee></div>');
		  }else if(scrollDirection=="left"){
			$("#yulan").append('<marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee>');
		  }else if(scrollDirection=="up"){
			$("#yulan").append('<marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div>');
		  }else if(scrollDirection=="down"){
			$("#yulan").append('<div style="position: relative;width:100%;height:96%;"><div style="width:100%;position:absolute;bottom:0px;"><marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div></div>');
		  }
	  });
	  $("#scroll_direction").change(function(){  //下拉框change事件
		   document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
		   var testStr=$("#text").val();
		   var text=testStr.replace(/[\r\n]/g,"");//去掉换行回车
			var textSize=$("#font_size").val();
			var textColor=RGBToHex($("#font_color").css("background-color"));
			var scrollDirection=$("#scroll_direction").find("option:selected").text();
			var scrollSpeed=$("#scroll_speed").val();
			var font=$("#font").find("option:selected").text();
			  if(scrollDirection=="right"){
					$("#yulan").append('<div class="c1"><marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee></div>');
				  }else if(scrollDirection=="left"){
					$("#yulan").append('<marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee>');
				  }else if(scrollDirection=="up"){
					$("#yulan").append('<marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div>');
				  }else if(scrollDirection=="down"){
					$("#yulan").append('<div style="position: relative;width:100%;height:96%;"><div style="width:100%;position:absolute;bottom:0px;"><marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div></div>');
				  }
	  });
	   $("#scroll_speed").bind('input propertychange',function(){   //文本框change事件 input
		document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
		  var testStr=$("#text").val();
		  var text=testStr.replace(/[\r\n]/g,"");//去掉换行回车
			var textSize=$("#font_size").val();
			var textColor=RGBToHex($("#font_color").css("background-color"));
			var scrollDirection=$("#scroll_direction").find("option:selected").text();
			var scrollSpeed=$("#scroll_speed").val();
			if(check(scrollSpeed)){
				 ZENG.msgbox.show("速度范围是1-70之间的整数", 3, 1000);
				 return;
			}
			var font=$("#font").find("option:selected").text();
			  if(scrollDirection=="right"){
					$("#yulan").append('<div class="c1"><marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee></div>');
				  }else if(scrollDirection=="left"){
					$("#yulan").append('<marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee>');
				  }else if(scrollDirection=="up"){
					$("#yulan").append('<marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div>');
				  }else if(scrollDirection=="down"){
						$("#yulan").append('<div style="position: relative;width:100%;height:96%;"><div style="width:100%;position:absolute;bottom:0px;"><marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div></div>');
				  }
	  });
	  $("#font_size").bind('input propertychange',function(){
		document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
		    var testStr=$("#text").val();
		    var text=testStr.replace(/[\r\n]/g,"");//去掉换行回车
			var textSize=$("#font_size").val();
			if(check2(textSize)){
				ZENG.msgbox.show("字体大小范围在10-180之间的整数",3, 1000); 
		    	//$("#font_size").val("");
		    	$("#font_size").focus();
		    	return false;
		    }
			var textColor=RGBToHex($("#font_color").css("background-color"));
			var scrollDirection=$("#scroll_direction").find("option:selected").text();
			var scrollSpeed=$("#scroll_speed").val();
			var font=$("#font").find("option:selected").text();
			  if(scrollDirection=="right"){
					$("#yulan").append('<div class="c1"><marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee></div>');
				  }else if(scrollDirection=="left"){
					$("#yulan").append('<marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee>');
				  }else if(scrollDirection=="up"){
					$("#yulan").append('<marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div>');
				  }else if(scrollDirection=="down"){
						$("#yulan").append('<div style="position: relative;width:100%;height:96%;"><div style="width:100%;position:absolute;bottom:0px;"><marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div></div>');
				  }
	   });
	   $("#text").bind('input propertychange',function(){
		    document.getElementById('yulan').innerHTML=''; //清除预览面板里的数据
		    var testStr=$("#text").val();
		    var text=testStr.replace(/[\r\n]/g,"");//去掉换行回车
			var textSize=$("#font_size").val();
			var textColor=RGBToHex($("#font_color").css("background-color"));
			var scrollDirection=$("#scroll_direction").find("option:selected").text();
			var scrollSpeed=$("#scroll_speed").val();
			var font=$("#font").find("option:selected").text();
			  if(scrollDirection=="right"){
					$("#yulan").append('<div class="c1"><marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee></div>');
				  }else if(scrollDirection=="left"){
					$("#yulan").append('<marquee direction="up" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><h1 style="width:'+textSize+'px;font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</h1></marquee>');
				  }else if(scrollDirection=="up"){
					$("#yulan").append('<marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div>');
				  }else if(scrollDirection=="down"){
					$("#yulan").append('<div style="position: relative;width:100%;height:96%;"><div style="width:100%;position:absolute;bottom:0px;"><marquee direction="left" scrollamount="'+scrollSpeed+'"  behavior="scroll"  onMouseOver="this.stop()" onMouseOut="this.start()"><span style="font-size:'+textSize+'px; color:'+textColor+'; font-family:'+font+';">'+text+'</span></marquee></div></div>');
				  }
	  });
}  
function changeImg(){
	$("#searchimg").attr("src","img/searchchange.png");
	}
function changeImg2(){
	$("#searchimg").attr("src","img/search2.png");
	}
function aa(val){
	switch(val){
	case '黑体':
	    return 0;
	case '楷体':
		return 1;
	case '宋体':
		return 2;
	case '仿宋体':
		return 3;
	case '隶书':
		return 4;
	case '方正姚体':
		return 5;
	case '楷书':
		return 6;
	case '幼圆':
		return 7;
	case '新宋体':
		return 8;
	}
}
function bb(val){
	switch(val){
	case 'left':
		return 3;
	case 'right':
		return 4;
	case 'down':
		return 2;
	case 'up':
		return 1;
	}
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
				if(result.moduleList[i]==14){
					$("#addScroll").show();
				}
				if(result.moduleList[i]==15){
					$("#updateScroll").show();
				}
				if(result.moduleList[i]==16){
					$("#delScroll").show();
				}
				if(result.moduleList[i]==25){
					$("#auditScroll").show();
				}
				if(result.moduleList[i]==29){
					$("#publishScroll").show();
					$("#stopScroll").show();
				}
			}
      	}
      });

}


