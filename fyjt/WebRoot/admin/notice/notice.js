//权限
function init(){
	$.ajax({ 
    	url: "modulepower/queryModulePowerID.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	async: false,
    	success: function(result){
			for(var i=0;i<result.moduleList.length;i++){
				if (result.moduleList[i] == 8) {
					$("#addNotice").show();
				}
				if (result.moduleList[i] == 9) {
					$("#updateNotice").show();
				}
				if (result.moduleList[i] == 10) {
					$("#delNotice").show();
				}
				if (result.moduleList[i] == 23) {
					$("#editNotice").show();
				}
			}
      	}
      });

}

//刷新
function refresh(){
	$('#searchName').textbox('setValue',"");
	var data = {
			page : 1,
			rows : 10
	};
	loadGrid(data)
}
//搜索
function searchName(){
	var sname = $("#searchName").val();
	var data = {
			sname : sname,
			page : 1,
			rows : 10
	};
	loadGrid(data)
}

$(document).ready(function(){
	var data = {
			page : 1,
			rows : 10
	};
	loadGrid(data)
});
function loadGrid(data){ 
    //加载数据  
    $('#noticelist').datagrid({  
                width: 'auto',                
                striped: true,  
                singleSelect : false,  
                url:'notice/queryNotice.do',  
                loadMsg:'数据加载中请稍后……',  
                contentType:"application/x-www-form-urlencoded;charset=UTF-8",
                pagination: true, //分页控件 
    	    	pageSize:10,
                queryParams:data,
                method:'POST',
                rownumbers: true, 
                frozenColumns : [[{
		         field : 'ck',
		         checkbox : true
		        }]],
                columns:[[  
				    {field:'id',width:'100px',align:'center',title: 'ID',hidden:true},
				    {field:'title',width:'300px',align:'center',title: '标题'}, 
					{field:'update_time',width:'200px',align : 'center',title : '更新日期',
		            	 formatter : function(value, row, index) {
		            		 var date = new Date(value);
		            		 return dateToString(date);
		            	 }
		             }, 
					{field:'realname',width:'100px',align:'center',title: '修改者'}, 
					{field:'status',width:'100px',align:'center',title: '状态',formatter:function(value){
						if(value=="Y"){
							return "<font color='green'>审核通过</font>";
						}else if(value=="C"){
							return "<font color='orange'>待审核</font>";
						}else if(value=="F"){
							return "<font color='red'>审核未通过</font>";
						}
					}},
                ]],
                 onLoadSuccess:function(data){  
                    $('.editcls').linkbutton({plain:true,iconCls:'icon-search'});  
                 } 
            });  
    var p = $('#noticelist').datagrid('getPager'); 
    $(p).pagination({ 
    	pageList: [5,10,15],//可以设置每页记录条数的列表 
       // showPageList:false,
        beforePageText: '第',//页数文本框前显示的汉字 
        afterPageText: '页    共 {pages} 页', 
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录' ,
        showRefresh:false
    });  
}  

//弹出添加页面
function add(){
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
var editor = null;
//编辑页面
function update(){
	var n=1;
  	n+=n;
  	page=1;
  	$("#fm").form("clear");
  	var row=$("#noticelist").datagrid('getSelections');
  	if(row.length==0){
	   ZENG.msgbox.show("请选择一条信息", 3, 1000); 
  	}else if(row.length>1){
	   ZENG.msgbox.show("每次只能选择一条信息", 3, 1000); 
  	}else{
      $("#dlg").dialog("open").dialog("setTitle","修改公告");
      $("#id").val(row[0].id);
      $("#title").val(row[0].title);
      editor.html(row[0].description);
  	}
}
//添加和修改
function save(){
	 if(page==0){
		 	var id=$("#id").val();
		    var title=$("#title").val();
		    var description=$("#description").val();
		    if(title==null || title=="" || title=='undefined'){
		    	ZENG.msgbox.show("标题不能为空",3, 1000); 
		    	return;
		    }
		    var datas={"title":title,"description":description}; 
			$.ajax({
				 url:'notice/insertNotice.do',
			     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			     data:datas,
			     type:'post',
				 success:function(data){
					 console.log(data);
				     if(data.success==true){
						  $("#dlg").dialog("close");
						  $("#fm").form("clear");
						  editor.html("");
						  var datas = {
				    				page : 1,
				    				rows : 10
				    		};
				    		loadGrid(datas)
				          ZENG.msgbox.show("添加成功", 4, 1000); 
					 }
			     }
			});
	
	  }else {
		  	var id=$("#id").val();
		    var title=$("#title").val();
		    var description=$("#description").val();
		    if(title==null || title=="" || title=='undefined'){
		    	ZENG.msgbox.show("标题不能为空",3, 1000); 
		    	return;
		    }
		    var datas={"id":id,"title":title,"description":description}; 
			$.ajax({
				 url:'notice/updateNotice.do',
			     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			     data:datas,
			     type:'post',
				 success:function(data){
				     if(data.success==true){
						  $("#dlg").dialog("close");
				          $("#fm").form("clear");
				          editor.html("");
				          var datas = {
				    				page : 1,
				    				rows : 10
				    		};
				    		loadGrid(datas)
				          ZENG.msgbox.show("修改成功", 4, 1000); 
					 }
			     }
			});
		
	    }
	}
//删除
function del(){
  var ids=[];
  var rows=$("#noticelist").datagrid('getSelections');
  if(rows.length==0){
	   ZENG.msgbox.show("请选择要删除的信息", 3, 1000); 
	   return;
  }
  else{
	  for(var i=0;i<rows.length;i++){
	    ids.push(rows[i].id);
	  }
	  $.messager.defaults = { ok: "确定", cancel: "取消" };
      $.messager.confirm('提示信息','你确定要删除吗？',function(r){
	    if(r){
		   $.ajax({
		      url:'notice/delNoticeByIds.do',
		      data:{"ids":ids},
		      type:"post",
		      contentType : "application/x-www-form-urlencoded",
			  success:function(data){
			     if(data.success==true){
			    	  ZENG.msgbox.show("删除成功",4, 1000); 
			    	  var datas = {
			    				page : 1,
			    				rows : 10
			    		};
			    		loadGrid(datas)
				 }else{
			    	  ZENG.msgbox.show("删除失败", 3, 1000); 
				 }
			  }
		   });
		}
	 });
  }
}
//审核公告
function editNotice(){
  	$("#fm").form("clear");
  	var row=$("#noticelist").datagrid('getSelections');
  	if(row.length==0){
	   ZENG.msgbox.show("请选择一条信息", 3, 1000); 
  	}else if(row.length>1){
	   ZENG.msgbox.show("每次只能选择一条信息", 3, 1000); 
  	}else if(row[0].status!="C"){
	   ZENG.msgbox.show("该公告已经审核", 3, 1000); 
  	}else{
  		$.messager.defaults = { ok: "通过", cancel: "不通过" };
        $.messager.confirm('提示信息','该条公告是否通过审核？',function(r){
  	    if(r){
  	    	editStatus(row[0].id,"Y");
  		}else{
  			editStatus(row[0].id,"F");
  		}
  	 });
  	}
}
function editStatus(id,status){
	$.ajax({
	      url:'notice/editNoticeById.do',
	      data:{"id":id,"status":status},
	      type:"post",
	      contentType : "application/x-www-form-urlencoded",
		  success:function(data){
		     if(data.success==true){
		    	  ZENG.msgbox.show("操作成功",4, 1000); 
		    	  var datas = {
		    				page : 1,
		    				rows : 10
		    		};
		    		loadGrid(datas)
			 }else{
		    	  ZENG.msgbox.show("操作失败", 3, 1000); 
			 }
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

$(function () {
    //详情编辑器
	 KindEditor.ready(function (K) {
	    	editor= K.create('#description', {
	            	items:[
						'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
						'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
						'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
						'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
						'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
						'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 
						'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
						'anchor', 'link', 'unlink', '|', 'about'],
	            uploadJson: 'notice/fileUpload.do',//指定上传图片的服务器端程序
	            //fileManagerJson: 'js/kindeditor/jsp/file_manager_json.jsp',//指定浏览远程图片的服务器端程序
	            allowFileManager : true, //浏览图片空间
		        filterMode : false, //HTML特殊代码过滤
		        afterCreate : function() { this.sync(); },
		        afterBlur: function(){ this.sync(); }, //编辑器失去焦点(blur)时执行的回调函数（将编辑器的HTML数据同步到textarea）
	        });
	    });
});


