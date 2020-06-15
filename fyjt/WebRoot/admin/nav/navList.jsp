<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="beans.user.UserBean" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1" />
		<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
		<link rel="stylesheet" type="text/css" href="js/easyui.css">
		<link rel="stylesheet" type="text/css" href="js/icon1.css">
		<link rel="stylesheet" type="text/css" href="js/demo.css">
		<link rel="stylesheet" type="text/css" href="js/style.css">
		<script type="text/javascript" src="js/jquery.js"></script> 
		<script type="text/javascript" src="js/ajaxfileupload.js"></script> 
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="js/check.js"></script> 
		<script type="text/javascript" src="js/msgbox.js"></script> 
		<title>导航管理</title>
	</head>
<body>
<!-- <div style="height:7%; background-color:transparent;">
	<img src="img/elementtop.png" width="100%" />
</div> -->
<table id="nav_treegrid"></table>

<div id="addDialog"></div>
<div id="editDialog"></div>

</div>
</body>
<script type="text/javascript">
	//$.ajaxSetup ({cache: false});//禁用缓存
	var treeGrid = $('#nav_treegrid').treegrid({
		title:'数据查询',
		rownumbers: true,
		sortable:true,
		pagination:true,
		collapsible: true,
		singleSelect:false,
		fitColumns: true,
		closable:false,
		url: 'nav/queryNav.do',
		method: 'get',
		idField: 'id',
		treeField: 'name',
		pageSize:20,
	    columns:[[
				{ field:'ck',checkbox:true },
	  			{title:'名称',field:'name',width:160},
				{title:'路径',field:'url',width:180}
	    ]],
	    toolbar: [{
			iconCls: 'icon-add',
			text:'新增',
			handler: function(){
				// 添加导航条diaog
				var addDialog = $('#addDialog').dialog({
				    title: '新增',
				    width: 350,
				    height: 200,
				    closed: false,
				    modal:true,
				    href:'nav/pageAdd.do',
				    buttons:[{
						text:'保存',
						handler:function(){
							$('#addNav').form('submit', {
							    url:'nav/insertNav.do',
							    onSubmit: function(){
							    	// 验证数据
									if( $(this).form('validate') ){
										return true;
									}
									return false;
							    },
							    success:function(data){
							    	data = $.parseJSON(data);
									
									if(data.success==true){
										 addDialog.dialog('close');
										 treeGrid.treegrid('reload');
								         ZENG.msgbox.show("添加成功", 4, 1000); 
									 }else{
										 ZENG.msgbox.show("添加失败",3,1000);
									 }
							    }
							});
						}
					},{
						text:'取消',
						handler:function(){
							addDialog.dialog('close');
						}
					}]
				});
			}
		},'-',{
			iconCls: 'icon-edit',
			text:'修改',
			handler: function(){
				var selectedRow = treeGrid.treegrid('getSelections');
				if( selectedRow.length==0 ){
		    		ZENG.msgbox.show("请选择删除数据!",3,1000);
		    		return;
		    	}else if( selectedRow.length>1 ){
		    		ZENG.msgbox.show("只能选择一条数据进行操作!",3,1000);
		    		return;
		    	}else{
					// 修改导航条diaog
					var editDialog = $('#editDialog').dialog({
					    title: '编辑',
					    width: 350,
					    height: 200,
					    closed: false,
					    modal:true,
					    href:'nav/pageEdit?name='+selectedRow[0].name+'&url='+selectedRow[0].url+'&id='+selectedRow[0].id,
					    buttons:[{
							text:'保存',
							handler:function(){
								$('#editNav').form('submit', {
								    url:'nav/updateNav.do',
								    onSubmit: function(){
								    	// 验证数据
										if( $(this).form('validate') ){
											return true;
										}
										return false;
								    },
								    success:function(data){
								    	data = $.parseJSON(data);
										if(data.success==true){
											 editDialog.dialog('close');
											 treeGrid.treegrid('reload');
									         ZENG.msgbox.show("操作成功", 4, 1000); 
										 }else{
											 ZENG.msgbox.show("操作失败",3,1000);
										 }
								    }
								});
							}
						},{
							text:'取消',
							handler:function(){
								editDialog.dialog('close');
							}
						}]
					});
				}
			}
		},'-',{
			iconCls: 'icon-remove',
			text:'删除',
			handler: function(){
				var ids=[];
				var selectedRows = treeGrid.treegrid('getSelections');
		    	if( selectedRows.length==0 ){
		    		ZENG.msgbox.show("请选择删除数据!",3,1000);
		    		return;
		    	}else{
					for(var i=0;i<selectedRows.length;i++){
					    ids.push(selectedRows[i].id);
					  }
					$.messager.confirm('提示信息','你确定要删除吗？',function(r){
					    if(r){
						   $.ajax({
						      url:'nav/delNavByIds.do',
						      data:{"ids":ids},
						      type:"post",
						      contentType : "application/x-www-form-urlencoded",
							  success:function(data){
							     if(data.success==true){
							    	  ZENG.msgbox.show("删除成功",4, 1000); 
							    	  treeGrid.treegrid('reload');
								 }else{
							    	  ZENG.msgbox.show("删除失败", 3, 1000); 
								 }
							  }
						   });
						}
					 });
		    	}
			}
		}]
	});

	
</script>
</html>

