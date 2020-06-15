//权限
function init(){
	$.ajax({ 
    	url: "modulepower/queryModulePowerID.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	async: false,
    	success: function(result){
			for(var i=0;i<result.moduleList.length;i++){
				if(result.moduleList[i]==14){
					$("#addCarousel").show();
				}
				if(result.moduleList[i]==15){
					$("#updateCarousel").show();
				}
				if(result.moduleList[i]==16){
					$("#delCarousel").show();
				}
			}
      	}
      });

}
$(document).ready(function(){
	 loadGrid();
});


function loadGrid(){ 
    //加载数据  
    $('#dg').datagrid({  
                width: 'auto',                
                striped: true,  
                singleSelect : false,  
                url:'carousel/queryCarousel.do',  
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
				    {field:'carousel_id',width:'100px',align:'center',title: '轮播图ID',hidden:true},
					{field:'imgurl',width:'200px',align:'center',title: '图片路径'}, 
					{field:'lookimg',width:'136px',align:'center', title:'图片查看',
		            	 formatter : function(value, rowData) {
		            		 var url=rowData.imgurl;
		            		 url=url.replace("\\", "/"); 
		            		 var btn = '<a class="editcls" onclick="showUrl(\''+ url +'\')" href="javascript:void(0)">查询</a>';
		            		 return btn;
		            	 }
		             },
					{field:'isort',width:'100px',align:'center',title: '排序'}, 
					{field : 'update_time',width:'200px',align : 'center',title : '更新日期',
		            	 formatter : function(value, row, index) {
		            		 var date = new Date(value);
		            		 return dateToString(date);
		            	 }
		             }, 
					{field:'realname',width:'100px',align:'center',title: '创建者'} 
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
//查看图片
function showUrl(url){
   $("#showUrl").window("open").dialog('setTitle', '查看轮播图');
   $("#preview-img").attr("src",url);
}

//刷新
function refresh(){
	$('#searchName').textbox('setValue',"");
	$("#dg").datagrid("reload",{
		sname : ""
	});
}

//弹出添加页面
function add(){
	$("#show-img").removeAttr("src");
	page=0;
   $("#dlg").window("open").dialog('setTitle', '添加公告');
   $("#fm").form("clear");
   $("#fm").form('load',{
	   font_size:100,
	   scroll_direction:3,
	   font:0,
	   scroll_speed:10
   });
}
//编辑页面
function update(){
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
      $("#dlg").dialog("open").dialog("setTitle","修改轮播图");
      $("#carousel_id").val(row[0].carousel_id);
      $("#imgurl_old").val(row[0].imgurl);
      $("#isort").val(row[0].isort);
      $("#show-img").attr("src",row[0].imgurl);
      $("#imgurl").attr("src",row[0].imgurl);
  	}
}
//添加和修改
function save(){
	 if(page==0){
		 	var carousel_id=$("#carousel_id").val();
		    var isort=$("#isort").val();
		    var imgurl=$("#imgurl_old").val();
		    if(isort==null || isort=="" || isort=='undefined'){
		    	ZENG.msgbox.show("排序不能为空",3, 1000); 
		    	return;
		    }
		    if(isNaN(isort)){
		    	ZENG.msgbox.show("排序请输入数字",3, 1000); 
		    	return;
		    }

		    if(imgurl==null || imgurl=="" || imgurl=='undefined'){
		    	ZENG.msgbox.show("请上传图片",3, 1000); 
		    	return;
		    }
		    var datas={"carouselname":"轮播图管理","carousel_id":carousel_id,"isort":isort,"imgurl":imgurl}; 
			$.ajax({
				 url:'carousel/insertCarousel.do',
			     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			     data:datas,
			     type:'post',
				 success:function(data){
					 console.log(data);
				     if(data.success==true){
						  $("#dlg").dialog("close");
						  $("#fm").form("clear");
				          ZENG.msgbox.show("添加成功", 4, 1000); 
				          $('#dg').datagrid('reload');
					 }
			     }
			});
	
	  }else {
		    var carousel_id=$("#carousel_id").val();
		    var isort=$("#isort").val();
		    var imgurl=$("#imgurl_old").val();
		    if(isort==null || isort=="" || isort=='undefined'){
		    	ZENG.msgbox.show("排序不能为空",3, 1000); 
		    	return;
		    }
		    if(isNaN(isort)){
		    	ZENG.msgbox.show("排序请输入数字",3, 1000); 
		    	return;
		    }

		    if(imgurl==null || imgurl=="" || imgurl=='undefined'){
		    	ZENG.msgbox.show("请上传图片",3, 1000); 
		    	return;
		    }
		    var datas={"carouselname":"轮播图管理","carousel_id":carousel_id,"isort":isort,"imgurl":imgurl}; 
			$.ajax({
				 url:'carousel/updateCarousel.do',
			     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			     data:datas,
			     type:'post',
				 success:function(data){
				     if(data.success==true){
				    	 ZENG.msgbox.show("修改成功", 4, 1000); 
						  $("#dlg").dialog("close");
				          $('#dg').datagrid('reload');
				          $("#fm").form("clear");
					 }
			     }
			});
		
	    }
	}



//删除
function del(){
  var ids=[];
  var rows=$("#dg").datagrid('getSelections');
  if(rows.length==0){
	   ZENG.msgbox.show("请选择要删除的信息", 3, 1000); 
	   return;
  }
  else{
	  for(var i=0;i<rows.length;i++){
	    ids.push(rows[i].carousel_id);
	  }
	  $.messager.defaults = { ok: "确定", cancel: "取消" };
     $.messager.confirm('提示信息','你确定要删除吗？',function(r){
	    if(r){
		   $.ajax({
		      url:'carousel/delCarouselByIds.do',
		      data:{"ids":ids},
		      type:"post",
		      contentType : "application/x-www-form-urlencoded",
			  success:function(data){
			     if(data.success==true){
			    	  ZENG.msgbox.show("删除成功",4, 1000); 
					  $('#dg').datagrid('reload');
				 }else{
			    	  ZENG.msgbox.show("删除失败", 3, 1000); 
				 }
			  }
		   });
		}
	 });
  }
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

//上传文件操作
$(function(){
	$("#sub").click(function(){
    	var imgurl = $("#imgurl").val();
    	if(imgurl==null || imgurl=="" || imgurl=='undefined'){
	    	ZENG.msgbox.show("请选择图片",3, 1000); 
	    	return;
	    }else{
	    	 $.ajaxFileUpload({
	                    url: 'carousel/fileUpload.do', //用于文件上传的服务器端请求地址
	                    secureuri: false, //是否需要安全协议，一般设置为false
	                    fileElementId: 'imgurl', //文件上传域的ID
	                    dataType: 'json', //返回值类型 一般设置为json
	                    success: function (data)  //服务器成功响应处理函数
	                    {
	                    	if(data.state=="success"){
	                    		ZENG.msgbox.show("上传成功", 4, 1000);
	                    		$("#imgurl_old").val(data.url);
	                    		$("#show-img").attr("src",data.url);
	                    		
	                        }else{
	                        	ZENG.msgbox.show("上传失败", 3, 1000); 
	                        }
	                    },
	                    error: function (data)//服务器响应失败处理函数
	                    {
	                    	ZENG.msgbox.show("上传失败", 3, 1000); 
	                    }
	                }
	            );
	    }
    	
    });
    
});

