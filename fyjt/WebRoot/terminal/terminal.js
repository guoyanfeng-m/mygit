$(document).ready(function(){  
	/*
	 * 初始化主页数据界面
	 */
    loadGrid();  
    init();
    /*
     * tooltip
     */
	$('#add').tooltip({
		position: 'right',
		content: '<span style="color:#fff">添加终端</span>',
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
			});
		}
	});
	$('#edit').tooltip({
		position: 'right',
		content: '<span style="color:#fff">编辑</span>',
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
			});
		}
	});
	$('#del').tooltip({
		position: 'right',
		content: '<span style="color:#fff">删除</span>',
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
			});
		}
	});
	$('#aud').tooltip({
		position: 'right',
		content: '<span style="color:#fff">审核</span>',
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
			});
		}
	});
	$('#ref').tooltip({
		position: 'right',
		content: '<span style="color:#fff">刷新</span>',
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
			});
		}
	});

}); 
/**
 * 数据加载
 */
var treeCheckId = 0;
var treeCheckName;
function init(){
	$.ajax({ 
    	url: "modulepower/queryModulePowerID.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	async: false,
    	success: function(result){
			for(var i=0;i<result.moduleList.length;i++){
				if(result.moduleList[i]==17){
					$("#addtd").show();
					$("#bdtd").show();
					$("#addGroup").show();
				}
				if(result.moduleList[i]==18){
					$("#edittd").show();
					$("#editGroup").show();
				}
				if(result.moduleList[i]==19){
					$("#deltd").show();
					$("#delGroup").show();
				}
				if(result.moduleList[i]==26){
					$("#audtd").show();
				}
			}
      	}
      });
  $("#addTerminalDownWin").window({
	  onClose:function(){
	   $('#starttime').timespinner('setValue','');
	   $('#endtime').timespinner('setValue','');
	   $('input[type="checkbox"][name="day"]').each(function(){
		   $(this).attr("checked",false);
	   });
    } 
  });
}

//加载数据  
function loadGrid(){  
    $('#cxdm').datagrid({  
                width: 'auto',                
                striped: true,  
                singleSelect : false,
                url:'terminal/queryTerminal.do',
                loadMsg:'数据加载中请稍后……',  
                pagination: true,
                pageSize:10,
                queryParams:{is_verify:1,page:1,rows:10},
                method:'POST',
                rownumbers: true, 
                onLoadSuccess : function() {$('.editcls').linkbutton({});},  
                columns:[[  
							{
								field:'',
								checkbox:true,
								align:'center',
								title: ''
							},
                            {
								field:'terminal_id',
								hidden:"true",
								align:'center',
								title: '终端ID'
							}, 
                            {
								field:'terminal_name',
								width : '15%',
								align:'center',
								title: '<font color="#333333"><b>终端名称</b></font>'
							}, 
                            {
								field:'groupIds',
								hidden:"true",
								align:'center',
								title: '所属终端组ID'
							}, 
                            {
								field:'groupNames',
								width : '15%',
								align:'center',
								title: '<font color="#333333"><b>所属终端组</b></font>',
								formatter:function(value,row,index){
								    if (value.indexOf(",")!=0) {
								    	var grous=value.split(",");
								    	var groupsHtml="";
								    	for (var i = 0; i < grous.length; i++) {
								    		groupsHtml+="<p title='"+grous[i]+"'>"+grous[i]+"</p>";
										}
//								    	var button="<button class='editcls' onclick='showTerminalGroups('"+value+"')' >多终端查看</button>";
										return groupsHtml;
									}
								    return value;
						       }
							}, 
                            {
								field:'ip',
								align:'center',
								width : '10%',
								title: '<font color="#333333"><b>IP地址</b></font>'
							}, 
                            {
								field:'mac',
								align:'center',
								width : '15%',
								title: '<font color="#333333"><b>终端唯一标识</b></font>'
							},
                            {
								field:'audit_status',
								align:'center',
								width : '8%',
								title: '<font color="#333333"><b>审核状态</b></font>',
								formatter:function(value,row,index){
										    var status = value;
										    if(status==0){
										      	return "待审核";
										    }else if(status==1){
										    	return "审核通过";
										    }else{
										    	return "审核未通过";
										    }
								   }
							}, 
                            {
								field:'create_time',
								align:'center',
								width : '18%',
								title: '<font color="#333333"><b>创建日期</b></font>',
								formatter:function(value,row,index){
										    var unixTimestamp = new Date(value);
										    return unixTimestamp.toLocaleString();
								}
							}, 
                            {
								field:'update_time',
								align:'center',
								width : '18%',
								title: '<font color="#333333"><b>更新日期</b></font>',
								formatter:function(value,row,index){
								        var unixTimestamp = new Date(value);
								        return unixTimestamp.toLocaleString();
						        }
							},
                            {
								field:'userName',
								align:'center',
								width : '8%',
								title: '<font color="#333333"><b>创建者名称</b></font>'
							}
                ]]
			      
            });  
    var p = $('#cxdm').datagrid('getPager'); 
    $(p).pagination({ 
        pageList:[5,10,15],
        beforePageText: '第',//页数文本框前显示的汉字 
        afterPageText: '页    共 {pages} 页', 
        showRefresh:false,
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录' 
        /*onBeforeRefresh:function(){
            $(this).pagination('loading');
            alert('before refresh');
            $(this).pagination('loaded');
        }*/ 
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
        	treeCheckId = terminalGroupId;
        	 var p = $('#cxdm').datagrid('getPager'); 
    			$(p).pagination({
                                pageNumber: 1
                            }); 
        	$("#cxdm").datagrid('reload',{"terminalGroupId":terminalGroupId,"page":1,"rows":10});
		}
    });  
    bdchecks();
} 
/*
 * 多终端组展示
 */
function showTerminalGroups(value){
	if (value.length==0) {
		return ZENG.msgbox.show("无终端组",5,2000);
	}
	$('#ShowTerminalGroup').panel('open');
	$('#ShowTerminalGroup').html(value);
}
/**
 * 判断是否有被动添加终端
 */
function bdchecks(){
	 $.ajax({ 
	    	url: "terminal/queryBdTerminal.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    	data: {is_verify:0,page:1,rows:10},
	    	success: function(data){
	    		if(data.rows.length>0){
	    			$('#serchBd1').show();
	    			$('#serchBd').hide();
	    		}else{
	    			$('#serchBd').show();
	    			$('#serchBd1').hide();
	    			
	    		}
	      	}
	      });
}
function serchBds(){
	$('#bdtj').datagrid({  
        width: 'auto',                
        striped: true,  
        singleSelect : false,  
        url:'terminal/queryBdTerminal.do',
        loadMsg:'数据加载中请稍后……',  
        pagination: true,
        toolbar:'#tbs',
        pageSize:10,
        queryParams:{is_verify:0,page:1,rows:10},
        method:'POST',
        emptyMsg: '没有搜索到对应的信息',
        columns:[[  
					{
						field:'terminal_id',
						hidden:"true",
						align:'center',
						title: '终端ID'
					}, 
                    {
						field:'ip',
						width:'54%',
						align:'center',
						title: '<font style="font-family: 微软雅黑;">IP地址</font>' 
					} ,
                    {
						field:'mac',
						width:'50%',
						align:'center',
						title: '<font style="font-family: 微软雅黑;">终端唯一标识</font>' 
					}                                                        
        ]]  
    });  
		var p = $('#bdtj').datagrid('getPager'); 
		$(p).pagination({ 
						showPageList:false,
						beforePageText: '第',//页数文本框前显示的汉字 
						afterPageText: '页    共 {pages} 页', 
						showRefresh:false,
						displayMsg: ''
		});  
		$('#zdjk').datagrid({
			toolbar: '#tbs'
		});
}

/**
 * 按名称查询终端
 */
function searchTerminal(){
	var grid = $('#cxdm');
    var options = grid.datagrid('getPager').data("pagination").options; 
    var curr = options.pageNumber;  
	var terminalName = $.trim($('#searchName').val());
	var p = $('#cxdm').datagrid('getPager'); 
    			$(p).pagination({
                                pageNumber: 1
                            }); 
	 if(treeCheckId!=0){
		 $("#cxdm").datagrid('load',{"terminal_name":terminalName,is_verify:"1",page:1,rows:10,"terminalGroupId":treeCheckId});
	 }else{
		 $("#cxdm").datagrid('load',{"terminal_name":terminalName,is_verify:"1",page:1,rows:10});
	 }
}
/**
 * 初始化添加终端窗口
 */
function adds(){
	$("#w").panel({
        title: '添加终端'
    });
	$('#terminalId').textbox("setValue","");
	$('#terminalName').textbox("setValue","");
	$('#IP').textbox("setValue","");
	$('#MAC').textbox("setValue","");
	$('#windowButton').attr("onclick","insertTerminal()");
	$('#terminalGroup').tree({
								url:'terminal/queryTerminalGroups.do',
								checkbox : true,
						    	onSelect : function(node) {
									if (node.checked) {
										$('#terminalGroup').tree('uncheck', node.target);
									} else {
										$('#terminalGroup').tree('check', node.target);
									}
								}
								});
}
/*
 * 添加终端
 */
function insertTerminal(){
	var terminalName = $('#terminalName').val().split(' ').join('');
	var terminalGroup = $("#terminalGroup").tree('getChecked');
	if (terminalGroup==undefined||terminalGroup.length==0) {
		ZENG.msgbox.show("请至少选择一个终端组！", 3, 3000);
		return;
	}
	if (terminalGroup.length > 1) {
		ZENG.msgbox.show("只能选择一个终端组！", 3, 3000);
		return;
	}
	var terminalGroups=new Array();
	for (var i = 0; i < terminalGroup.length; i++) {
		terminalGroups.push(terminalGroup[i].id);
	}
	var IP = $('#IP').val();
	var MAC = $('#MAC').val();
	
	var reg_name1=/[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/; 
	var reg_name2=/[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{8}/; 
	var ip_name = /^([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])$/;
	if($.trim(terminalName)==null||$.trim(terminalName)==""){
		ZENG.msgbox.show("终端名称不能为空或全为空格！", 3, 3000);
		return;
	}
	if(!ip_name.test(IP)){
		ZENG.msgbox.show("IP地址为空或格式不正确！", 3, 3000);
		return;
	}
	
	if(!reg_name1.test(MAC) && !reg_name2.test(MAC)){ 
		ZENG.msgbox.show("终端唯一标识为空或格式不正确！", 3, 3000);
		return;
	}
	if(terminalName.length>15){ 
		ZENG.msgbox.show("终端名称不能超过15个字，请重新输入！", 3, 3000);
		return;
	}
	
	var insertTerminaldatas = {"terminal_name":terminalName,"ip":IP,"mac":MAC,"terminalGroups":terminalGroups};
	$.ajax({ 
    	url: "terminal/insertTerminal.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: insertTerminaldatas,
    	success: function(data){
			if(data.success){
	    		$('#w').window('close');
	    		ZENG.msgbox.show("添加成功！", 4, 3000);
				 $("#cxdm").datagrid('reload');
			}else{
				ZENG.msgbox.show(data.msg, 5, 3000);
			}
      	}
      });
}
/*
 * 初始化被动添加终端窗口
 */
function bdtjs(){
	var terminal= $('#bdtj').datagrid('getSelections');
	if(terminal.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}else if(terminal.length>1){
			ZENG.msgbox.show("只能选择一行数据！", 3, 3000);
		return;
		}
	$('#w').window('open');
	var terminalId = terminal[0].terminal_id;
	var mac = terminal[0].mac;
	var IP = terminal[0].ip;
	$('#terminalId').textbox("setValue",terminalId);
	$('#terminalName').textbox("setValue","");
	$('#IP').textbox("setValue",IP);
	$('#MAC').textbox("setValue",mac);
	$("#texts").show();
	$('#windowButton').attr("onclick","bdtjedit()");
	$('#terminalGroup').tree({
		url:'terminal/queryTerminalGroups.do',
		checkbox : true,
    	onSelect : function(node) {
			if (node.checked) {
				$('#terminalGroup').tree('uncheck', node.target);
			} else {
				$('#terminalGroup').tree('check', node.target);
			}
		}
		});
}
/*
* 被动添加终端
*/
function bdtjedit(){
	var terminal_id = $('#terminalId').val();
	var terminalGroup = $("#terminalGroup").tree('getChecked');
	if (terminalGroup==undefined||terminalGroup.length==0) {
		ZENG.msgbox.show("请至少选择一个终端组！", 3, 3000);
		return;
	}
	if (terminalGroup.length > 1) {
		ZENG.msgbox.show("只能选择一个终端组！", 3, 3000);
		return;
	}
	var terminalGroups=new Array();
	for (var i = 0; i < terminalGroup.length; i++) {
		terminalGroups.push(terminalGroup[i].id);
	}
	var terminalName = $('#terminalName').val();
	var IP = $('#IP').val();
	var MAC = $('#MAC').val();
	var reg_name1=/[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/; 
	var reg_name2=/[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{8}/; 
	var ip_name = /^([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])$/;
	if($.trim(terminalName)==null||$.trim(terminalName)==""){
		ZENG.msgbox.show("终端名称不能为空或全为空格！", 3, 3000);
		return;
	}
	if(!ip_name.test(IP)){
		ZENG.msgbox.show("IP地址为空或格式不正确！", 3, 3000);
		return;
	}
	if(!reg_name1.test(MAC) && !reg_name2.test(MAC)){ 
		ZENG.msgbox.show("终端唯一标识为空或格式不正确！", 3, 3000);
		return;
	}
	if(terminalName.length>15){ 
		ZENG.msgbox.show("终端名称不能超过15个字，请重新输入！", 3, 3000);
		return;
	}
	var editTerminaldatas = {
								"terminalId":terminal_id,
								"terminalGroups":terminalGroups,
								"terminalName":terminalName,
								"ip":IP,
								"mac":MAC,
								"is_verify":1
							};
	$.ajax({ 
    	url: "terminal/updateBdTerminalByTerminalId.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: editTerminaldatas,
    	success: function(data){
    		if (data.success) {
    			$('#w').window('close');
       		    ZENG.msgbox.show("添加成功！", 4, 3000);
   			    $("#cxdm").datagrid('reload');
   			    $("#bdtj").datagrid('reload');
   			   bdchecks();
			}else{
				$('#w').window('close');
       		    ZENG.msgbox.show("添加异常！！！", 5, 3000);
			}
      	}
      });
}
/**
 * 终端编辑窗口初始化
 */
function edits(){
	var terminal= $('#cxdm').datagrid('getSelections');
	if(terminal.length>1){
		return ZENG.msgbox.show("只能选择一行数据！", 3, 3000);
	}
	if(terminal.length==0){
		return ZENG.msgbox.show("请先选择一行数据！", 3, 3000);
	}
	var terminal_id = terminal[0].terminal_id;
	var terminal_name = terminal[0].terminal_name;
	var IP = terminal[0].ip;
	var MAC = terminal[0].mac;
	$('#terminalId').textbox("setValue",terminal_id);
	$('#terminalName').textbox("setValue",terminal_name);
	$('#IP').textbox("setValue",IP);
	$('#MAC').textbox("setValue",MAC);
	$('#windowButton').attr("onclick","editTerminal()");
	$("#w").panel({
        title: '编辑终端'
    });
	$('#w').window('open');
	$('#terminalGroup').tree({
		url:'terminal/queryTerminalGroups.do',
		checkbox : true,
		onLoadSuccess : function() {
			   setTerminalGroupsCheck(terminal_id);
		},
    	onSelect : function(node) {
			if (node.checked) {
				$('#terminalGroup').tree('uncheck', node.target);
			} else {
				$('#terminalGroup').tree('check', node.target);
			}
		}
	});
}
/*
 * 编辑终端--终端组选中
 */
function setTerminalGroupsCheck(terminalId){
	$.ajax({
		url:'terminalGroup/selectTerminalGroupIds.do',
		data:{
			'terminalId':terminalId
		},
		success:function(data){
			if (data!=undefined&&data!=null&&data.length!=0) {
				if (data.indexOf(",")!=0) {
					var idArray=data.split(",");
					for (var i = 0; i < idArray.length; i++) {
						checkTerminalGroup(idArray[i]);
					}
				}else{
					checkTerminalGroup(id);
				}
			}
		}
	});
}
/*
 *编辑终端选中 
 */
function checkTerminalGroup(id){
	var node = $('#terminalGroup').tree('find', id);
	$('#terminalGroup').tree('check', node.target);	
}
/*
 * 添加终端组窗口初始化
 */
function addGroup(){
	$('#z').panel('setTitle',"添加终端组");
	$("#texts").show();
	$('#terminalGroupName').textbox("setValue","");
	$('#terminalGroupDescription').val("");
	$('#groupWindowButton').attr("onclick","insertTerminalGroup()");
	$('#terminalGroupGroup').combobox({
//		url:'terminal/queryTerminalGroupByUserId.do',
		url:'terminal/queryTerminalGroup.do',
		valueField:'t_id',
		textField:'groupName'
		});
	$("#terminalGroupGroup").combobox({
	    editable:false
	});
	$("#terminalGroupGroup").combobox("setValue","1");
}
/**
 * 添加终端组
 */
function insertTerminalGroup(){
	var terminalGroupName = $('#terminalGroupName').val();
	var terminalGroupGroup = $("#terminalGroupGroup").combobox("getValue");
	var terminalGroupDescription = $('#terminalGroupDescription').val();
	if($.trim(terminalGroupName)==null||$.trim(terminalGroupName)==""){
		ZENG.msgbox.show("终端组名称不能为空或全为空格！", 3, 3000);
		return;
	}
	if(terminalGroupName.length>15){ 
		ZENG.msgbox.show("终端组名称不能超过15个字，请重新输入！", 3, 3000);
		return;
	}
	var insertTerminalGroupdatas = {"terminalGroupName":terminalGroupName,"terminalGroupGroup":terminalGroupGroup,"terminalGroupDescription":terminalGroupDescription};
	$.ajax({ 
    	url: "terminalGroup/insertTerminalGroup.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: insertTerminalGroupdatas,
    	success: function(data){
		
			if(data.success){
				$('#z').window('close');
				ZENG.msgbox.show("添加成功！", 4, 3000);
				$('#tt').tree('reload'); 
			}else{
				ZENG.msgbox.show(data.msg, 5, 3000);
			}
    		
      	}
      });
}
/**
 * 编辑终端组窗口初始化
 */
function editGroup(){
	$('#z').panel('setTitle',"编辑终端组");
	var terminalGroups = $('#tt').tree('getSelected');
	if(terminalGroups==null){
		ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
	}
	var terminalGroupId = $('#tt').tree('getSelected').id; 
	var terminalGroupName = $('#tt').tree('getSelected').text; 
	var editGroupdatas = {"terminalGroupId":terminalGroupId};
	$.ajax({ 
    	url: "terminal/queryTerminalGroup.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: editGroupdatas,
    	success: function(data){
    		var description = data[0].description;
    		$('#z').window('open');
    		$('#terminalGroupId').textbox("setValue",terminalGroupId);
    		$('#terminalGroupName').textbox("setValue",terminalGroupName);
    		$('#terminalGroupDescription').val(description);
    		$("#texts").hide();
    		$('#groupWindowButton').attr("onclick","editTerminalGroupUrl()");
    		$('#terminalGroupGroup').combobox({
//    			url:'terminal/queryTerminalGroupByUserId.do',
    			url:'terminal/queryTerminalGroup.do',
    			valueField:'t_id',
    			textField:'groupName'
    			});
    		$("#terminalGroupGroup").combobox({
    		    editable:false
    		});
    		$("#terminalGroupGroup").combobox("setValue","1");
    		
      	}
      });
}
/**
 * 编辑终端
 */
function editTerminal(){
	var terminal_id = $('#terminalId').val();
	var terminalGroup = $("#terminalGroup").tree('getChecked');
	if (terminalGroup==undefined||terminalGroup.length==0) {
		ZENG.msgbox.show("请至少选择一个终端组！", 3, 3000);
		return;
	}
	if (terminalGroup.length > 1) {
		ZENG.msgbox.show("只能选择一个终端组！", 3, 3000);
		return;
	}
	var terminalGroups=new Array();
	for (var i = 0; i < terminalGroup.length; i++) {
		terminalGroups.push(terminalGroup[i].id);
	}
	var terminalName = $('#terminalName').val().split(' ').join('');
	var IP = $('#IP').val();
	var MAC = $('#MAC').val();
	
	var reg_name1=/[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/; 
	var reg_name2=/[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{8}/; 
	var ip_name = /^([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])$/;
	if($.trim(terminalName)==null||$.trim(terminalName)==""){
		ZENG.msgbox.show("终端名称不能为空或全为空格！", 3, 3000);
		return;
	}
	if(!ip_name.test(IP)){
		ZENG.msgbox.show("IP地址为空或格式不正确！", 3, 3000);
		return;
	}
	if(!reg_name1.test(MAC)&& !reg_name2.test(MAC)){ 
		ZENG.msgbox.show("终端唯一标识为空或格式不正确！", 3, 3000);
		return;
	} 
	if(terminalName.length>15){ 
		ZENG.msgbox.show("终端名称不能超过15个字，请重新输入！", 3, 3000);
		return;
	}
	var editTerminaldatas = {"terminalId":terminal_id,"terminalGroups":terminalGroups,"terminalName":terminalName,"ip":IP,"mac":MAC};
	$.ajax({ 
    	url: "terminal/updateTerminalByTerminalId.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: editTerminaldatas,
    	success: function(data){
			if(data.success){
	    		$('#w').window('close');
	    		 ZENG.msgbox.show("修改成功！", 4, 3000);
				 $("#cxdm").datagrid('reload');
			}else{
				ZENG.msgbox.show(data.msg, 5, 3000);
			}
      	}
      });
}
/*
 * 删除终端
 */
function deleteTerminal(){
	var terminal= $('#cxdm').datagrid('getSelections');
	if(terminal.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
		}
		
		$.messager.defaults = { ok: "确定", cancel: "取消" };
		$.messager.confirm("操作提示", "确定删除？", function (data) {
        if (data) {
        	var terminalName="";
			var terminalId = [];
			var groupIds=new Array();
			for(var i=0;i<terminal.length;i++){
				terminalId[i] = terminal[i].terminal_id;
				var terminalGroupIds=terminal[i].groupIds;
				if (terminalGroupIds.indexOf(",")!=0) {
					var groupArray=terminalGroupIds.split(",");
					for (var k = 0; k < groupArray.length; k++) {
						groupIds.push(groupArray[k]);
					}
				}else{
					groupIds.push(groupIds);
				}
				terminalName+=terminal[i].terminal_name+",";
			}
			var deleteTerminaldatas = {
					"terminalId":terminalId,
					"groupIds[]":groupIds,
					"terminalName":terminalName
					};
			$.ajax({ 
		    	url: "terminal/deleteTerminalByTerminalId.do", 
		    	type: "POST",
		    	contentType:"application/x-www-form-urlencoded",
		    	data: deleteTerminaldatas,
		    	success: function(data){
				 if(data.success=="true"){
					 ZENG.msgbox.show("删除成功！", 4, 3000);
					 $("#cxdm").datagrid('reload');
				  }else{
					  ZENG.msgbox.show(data.msg, 4, 3000);
					  $("#cxdm").datagrid('reload');
				  }
		      	}
              });
        }else{
      	return;
      }
      });
}
/**
 * 编辑终端组
 */

function editTerminalGroupUrl(){
	var terminalGroupId = $('#terminalGroupId').val();
	var terminalGroupName = $('#terminalGroupName').val();
	var description = $('#terminalGroupDescription').val();
	if($.trim(terminalGroupName) ==null||$.trim(terminalGroupName)==""){
		ZENG.msgbox.show("终端组名称不能为空或全为空格！", 3, 3000);
		return;
	}
	if(terminalGroupName.length>15){ 
		ZENG.msgbox.show("终端组名称不能超过15个字，请重新输入！", 3, 3000);
		return;
	}
	var editTerminalGroupData = {"terminalGroupId":terminalGroupId,"terminalGroupName":terminalGroupName,"terminalGroupDescription":description};
	$.ajax({ 
    	url: "terminalGroup/updateTerminalGroupById.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: editTerminalGroupData,
    	success: function(data){
			if(data.success){
				$('#z').window('close');
	    		ZENG.msgbox.show("修改成功！", 4, 3000);
				$('#tt').tree('reload'); 
			}else{
				ZENG.msgbox.show(data.msg, 5, 3000);
			}
      	}
      });
}
/**
 * 删除终端组
 */
function deleteTerminalGroup(){
	var TerminalGroupSelect =  $('#tt').tree('getSelected');
	if(TerminalGroupSelect==null){
		 ZENG.msgbox.show("请先选择终端组！", 3, 3000);
		return;
	  }
	else if(TerminalGroupSelect.length>1){
		 ZENG.msgbox.show("只能选择一个终端组！", 3, 3000);
		return;
	  }else if(TerminalGroupSelect.id==1){
		  ZENG.msgbox.show("该终端组不可被删除,请选择其他终端组！", 3, 3000);
		  return;
	  }
	
	$.messager.defaults = { ok: "继续", cancel: "放弃" };
	$.messager.confirm("操作提示", "删除该终端组将会同时删除该终端组下所有的终端和终端组，您确定要继续吗？", function (data) {
        if (data) {
			var terminalGroupId = $('#tt').tree('getSelected').id; 
			var Groupdata = {"terminalGroupId":terminalGroupId};
			$.ajax({ 
		    	url: "terminalGroup/deleteTerminalGroupById.do", 
		    	type: "POST",
		    	contentType:"application/x-www-form-urlencoded",
		    	data: Groupdata,
		    	success: function(data){
		    		if (data.success=="true"||data.success==true) {
		    			ZENG.msgbox.show("删除成功！", 4, 2000);
		    			$("#cxdm").datagrid('reload',{"terminalGroupId":null});
					    $('#tt').tree('reload'); 
		    		} else if (data.success=="false"||data.success==false) {
		    			ZENG.msgbox.show(data.msg, 4, 2000);
		    			 $("#cxdm").datagrid('reload');
		    		}
		      	}
		      });
        }
        else {
            return;
        }
    });
}
/**
 * 刷新被动添加终端列表
 */
function refreshBdtj(){
	$("#bdtj").datagrid('reload');
}

/**
 * 删除被动添加终端
 */
function deteleBdtj(){
var terminal= $('#bdtj').datagrid('getSelections');
	if(terminal.length==0){
		 ZENG.msgbox.show("请先选择数据！", 3, 3000);
		return;
	 }
	 $.messager.defaults = { ok: "继续", cancel: "放弃" };
	 $.messager.confirm("操作提示", "确定删除？", function (data) {
        if (data) {
			var terminalId = [];
			var terminalName="";
			for(var i=0;i<terminal.length;i++){
				terminalId[i] = terminal[i].terminal_id;
				terminalName+=terminal[i].mac+",";
			}
			var deleteTerminaldatas = {"terminalId":terminalId,"terminalName":terminalName};
			$.ajax({ 
		    	url: "terminal/deleteTerminalByTerminalId.do", 
		    	type: "POST",
		    	contentType:"application/x-www-form-urlencoded",
		    	data: deleteTerminaldatas,
		    	success: function(){
		    		 ZENG.msgbox.show("删除成功！", 4, 3000);
					 $("#bdtj").datagrid('reload');
					 bdchecks();
		      	}
			});
      }else{
      	return;
      }
      });
		
}
/**
 * 审核终端
 */
function audit_terminal(){
	var terminal= $('#cxdm').datagrid('getSelections');
	if(terminal.length==0){
		ZENG.msgbox.show("请先选择一行数据！", 3, 3000);
		return;
		}
	for(var i=0;i<terminal.length;i++){
		var audit_status = terminal[i].audit_status;
		if(audit_status!=0){
			ZENG.msgbox.show("包含已审核项，请重新选择！", 3, 3000);
			return;
		}
	}
	var terminal_id = [];
	for(var j=0;j<terminal.length;j++){
		terminal_id[j] = terminal[j].terminal_id;
	}
	var audit_status = 0;
	$.messager.defaults = { ok: "审核通过", cancel: "审核不通过" };
	$.messager.confirm("操作提示", "审核终端,审核后将不能更改，请慎重选择！", function (data) {
	    if (data) {
	    	audit_status = 1;
	    	audit_terminal_to(terminal_id,audit_status);
	    }else{
	    	audit_status = 2;
	    	audit_terminal_to(terminal_id,audit_status);
	    }
	});
}
/**
 * 审核终端
 */
function audit_terminal_to(terminal_id,audit_status){
	var auditTerminal = {"audit_status":audit_status,"terminal_id":terminal_id};
	$.ajax({ 
    	url: "terminal/updateTerminalAuditByTerminalId.do",
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	data: auditTerminal,
    	success: function(data){
			ZENG.msgbox.show("审核成功！", 4, 3000);
			$("#cxdm").datagrid('reload');
      	}
      });
}

/**
 * 刷新终端列表
 */
function datarefresh(){
	$('#searchName').textbox('setValue',"");
	$("#cxdm").datagrid('reload',{
		terminal_name : "",
		is_verify : 1
	});
}


/*//定时下载任务
function download(){
	var terminal= $('#cxdm').datagrid('getSelections');
	if(terminal.length==0){
	   ZENG.msgbox.show("请先选择终端！", 3, 1000);
	   return;
	}else{
		$('#downloadWin').window('open');
	}
}
//添加定时下载任务
function addDownTask(){
	 var terminal= $('#cxdm').datagrid('getSelections');
 	 var starttime=$('#starttime').timespinner('getValue');
	 var endtime=$('#endtime').timespinner('getValue');
	 var weeks=new Array();
	 var terminalIDs=new Array();
     $('input[type="checkbox"][name="day"]:checked').each(function(){
		   week.push($(this).val());
     });
     for(var i in terminal){
    	 terminalIDs.push(terminal[i]);
     }
     if(weeks.length==0){
       ZENG.msgbox.show("请选择星期！", 3, 1000);
  	   return; 
     }
}*/

