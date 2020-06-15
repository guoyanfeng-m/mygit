var flagvalid = 0;
$(document)
		.ready(
				function() {

					loadGrid();
					$('#newrole')
							.window(
									{
										onClose : function() {
											var isChrome = navigator.userAgent
													.toLowerCase().match(
															/chrome/) != null;
											if (isChrome) {
												location.href = "admin/role/role.jsp"
											} else {
												location.href = "role.jsp"
											}

										}
									});
					$('#updaterole')
							.window(
									{
										onClose : function() {
											var isChrome = navigator.userAgent
													.toLowerCase().match(
															/chrome/) != null;
											if (isChrome) {
												location.href = "admin/role/role.jsp"
											} else {
												location.href = "role.jsp"
											}

										}
									});
					$
							.extend(
									$.fn.validatebox.defaults.rules,
									{
										roleValid : {
											validator : function(value) {
												var roleValid = /^[^#@!$%^&*~]*$/;

												var flag1;
												var flag2;
												if (value.length < 5) {
													flag1 = 0;
												} else if (roleValid
														.test(value) == false) {
													flag2 = 0;
												}
												if (flag1 == 0) {
													$.fn.validatebox.defaults.rules.roleValid.message = "长度不能小于5";
													flagvalid = 1;
													return false;
												} else if (flag2 == 0) {
													$.fn.validatebox.defaults.rules.roleValid.message = "角色名称不能有特殊字符";
													flagvalid = '角色名称不能有特殊字符';
													return false;
												} else {
													flagvalid = 0;
													return true;
												}
											}
										}
									});
			});
var role;
var map;
function loadGrid() {

	// $('#roleterminaltree').tree({
	// checkbox: true,
	// animate:true,
	// lines:false,
	// url:'roletree/queryRoleTree.do',
	// cache:true,
	// onLoadSuccess: function(node,data){
	// var nodes =$('#roleterminaltree').tree('getChildren');
	// for(i=0;i<nodes.length;i++){
	// if(nodes[i].id>=40000){
	// $('#roleterminaltree').tree('update', {
	// target: nodes[i].target,
	// text:'456',
	// iconCls: 'tree-terminal'
	// });
	// }else{
	// $('#roleterminaltree').tree('update', {
	// target: nodes[i].target,
	// text:'123',
	// iconCls: 'tree-group'
	// });
	// }
	// };
	// }
	// });

	// 加载数据
	$('#rolelist')
			.datagrid(
					{
						width : 'auto',
						striped : true,
						singleSelect : false,
						url : 'role/queryRole.do',
						loadMsg : '数据加载中请稍后……',
						pagination : true,
						queryParams : {
							page : 1,
							rows : 10
						},
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
									field : 'role_id',
									align : 'center',
									title : '角色id',
									hidden : true
								},
								{
									field : 'role_name',
									width : '20%',
									align : 'center',
									title : '角色名称'
								},
								{
									field : 'description',
									width : '25%',
									align : 'center',
									title : '角色描述'
								},
								{
									field : 'create_time',
									width : '25%',
									align : 'center',
									title : '创建日期',
									formatter : function(value, row, index) {
										var unixTimestamp = new Date(value);
										return unixTimestamp.toLocaleString();
									}
								},
								{
									field : 'realname',
									width : '17%',
									align : 'center',
									title : '创建者名称'
								},
								{
									field : 'search',
									width : '10%',
									align : 'center',
									title : '详细信息',
									formatter : function(value, rec) {
										var btn = '<input type="button"  onclick="showpower(' + rec.role_id + ')" value="查看详细" style="width:100%;height:30px">';
										return btn;

									}
								},
								{
									field : 'schedulelevel',
									align : 'center',
									title : '发布权限',
									hidden : true
								}

						] ]
					});

	var p = $('#rolelist').datagrid('getPager');
	$(p).pagination( {
		pageList : [ 5, 10, 15 ],
		// showPageList:false,
		beforePageText : '第',// 页数文本框前显示的汉字
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		showRefresh : false
	});

}
function selectall() {
	$("#newroleaa :input[type='checkbox']").each(function() {
		this.indeterminate = false;
	});
	$("#newroleaa :input[type='checkbox']").each(function() {
		this.checked = true;
	});

}
function noselect() {
	$("#newroleaa :input[type='checkbox']").each(function() {
		this.indeterminate = false;
	});
	$("#newroleaa :input[type='checkbox']").each(function() {
		this.checked = false;
	});
}

function updateselectall() {
	$("#updateroleaa :input[type='checkbox']").each(function() {
		this.indeterminate = false;
	});
	$("#updateroleaa :input[type='checkbox']").each(function() {
		this.checked = true;
	});

}
function updatenoselect() {
	$("#updateroleaa :input[type='checkbox']").each(function() {
		this.indeterminate = false;
	});
	$("#updateroleaa :input[type='checkbox']").each(function() {
		this.checked = false;
	});
}

function newrole() {
	$
		.ajax({
			url : "role/queryScheduleLevelByRoleID.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			async : false,
			success : function(result) {
				schedulelevel = result;
				document.getElementById("pubpower").length=0;
				for (var i = schedulelevel; i > 0; i--) {
					$("#pubpower").append('<option value="'+i+'">'+i+'</option>'); 
				}
			}
		});	
	// $('#rolename').textbox("setValue","");
	$('#rolename').val("请输入角色名称");
	// $('#roledes').textbox("setValue","");
	$('#roledes').val("请输入描述");
	$('#newrole').window('open');
	$('#roletree').tree( {
		checkbox : true,
		animate : true,
		lines : false,
		url : 'roletree/queryRoleTree.do',
		cache : true,
		onLoadSuccess : function(node, data) {
			var nodes = $('#roletree').tree('getChildren');
			for (i = 0; i < nodes.length; i++) {
				if (nodes[i].id >= 40000) {
					$('#roletree').tree('update', {
						target : nodes[i].target,
						iconCls : 'tree-terminal'
					});
				} else {
					$('#roletree').tree('update', {
						target : nodes[i].target,
						iconCls : 'tree-group'
					});
				}
			}
			;
		}
	});
	$
			.getJSON(
			
					"modulepower/queryModulePower.do",
					null,
					function(response) {
						map = eval(response);
						var listHtml = '';
						listHtml += " <table class='stripe' id='newroleaa' style='width:100%;position:relative;'>";
						listHtml += " <tr><td style='width:18%;'><input type='button'  style='color:#666666;background:#f1f1f1' name='selectall' id='selectall' value='&nbsp全&nbsp&nbsp选&nbsp' onclick='selectall()';/></td><td style='width:82%;'><input type='button'  style='color:#666666;background:#f1f1f1' name='noselect' id='noselect' value='&nbsp全不选&nbsp' onclick='noselect()';/></td></tr>"
						for ( var i = 0; i < map.moduleList.length; i++) {
							for ( var key in map.moduleList[i]) {
								var power = (map.moduleList[i])[key];
								var name=key;
								var index=name.toString().indexOf('-');
								var BoxName=name.substring(0,index);
								var BoxValue=parseInt(name.substring(index+1,name.length))+50;
								listHtml += " <tr>";
								listHtml += " <td ><div style='float:left;margin-left:5px;'><input type='checkbox' name='"
										+ BoxValue
										+ "1'  id="
										+ BoxValue
										+ " value="
										+ BoxValue
										+ " onclick='checksingle(this.id)' style=border:none;/><label style='color:#626161;font-size:14px;font-weight:bold;'>"
										+ BoxName + ":" + "</label></div></td><td>";
								for ( var k = 0; k < power.length; k++) {
									listHtml += "<div style='float:left;margin-left:5px;'><input type='checkbox'";
									listHtml += " name=" + BoxValue;
									listHtml += " id=" + power[k].t_id;
									listHtml += " value=" + power[k].t_id;
									listHtml += " onclick='checkfinal(this.name)'";
									listHtml += " style=border:none;/><font style='color:#666666'>"
											+ power[k].powerName;
									listHtml += " </font></div>"
								}
								listHtml += " </td>";
								listHtml += " </tr>";
							}
						}
						listHtml += " </table>";
						$("#selectmodule").html(listHtml);
						$(".stripe table").css("width", "100%");
						$(".stripe tr").attr("bgColor", "#f1f1f1");// 为单数行表格设置背景颜色
						$(".stripe tr").css("height", "35px");
						$(".stripe tr:even").css("background-color", "#ffffff");// 为双数行表格设置背颜色素
					}
					);

}

function updaterole() {
	role = $('#rolelist').datagrid('getSelections');
	if (role.length == 0) {
		ZENG.msgbox.show("请选择一个角色", 1, 3000);
		return;
	} else {
		if (role[0].role_id == 1) {
			ZENG.msgbox.show("此角色不能修改", 1, 2000);
		} else if (role.length == 1) {
			$('#updaterole').window('open');
			$('#updaterolename').val(role[0].role_name);
			$('#updateroledes').val(role[0].description);
			$
				.ajax({
					url : "role/queryScheduleLevelByRoleID.do",
					type : "POST",
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					async : false,
					success : function(result) {
						schedulelevel = result;
						document.getElementById("updatepubpower").length=0;
						for (var i = schedulelevel; i > 0; i--) {
							$("#updatepubpower").append('<option value="'+i+'">'+i+'</option>'); 
						}
					}
				});	
			$('#updatepubpower').val(role[0].schedulelevel);
			var datas = {
				"roleid" : role[0].role_id
			};
			var showpower;
			var showterminal;
			var showterminalgroup;
			$
					.ajax( {
						url : "role/queryPowerByRoleID.do",
						type : "POST",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						data : datas,
						async : false,
						success : function(result) {
							showpower = result;
						}
					});
			$
					.ajax( {
						url : "role/queryTerminalByRoleID.do",
						type : "POST",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						data : datas,
						async : false,
						success : function(result) {
							showterminal = result;
						}
					});
			$
					.ajax( {
						url : "role/queryTerminalGroupByRoleID.do",
						type : "POST",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						data : datas,
						async : false,
						success : function(result) {
							showterminalgroup = result;
						}
					});
			$
					.getJSON(
					   
							"modulepower/queryModulePower.do",
							null,
							function(response) {
								var map = eval(response);
								var listHtml = '';
							
								listHtml += " <table id='updateroleaa' class='stripe' style='width:98%;'>";
								listHtml += " <tr><td style='width:18%;'><input type='button'   style='color:#666666;background:#f1f1f1' name='selectall' id='selectall' value='&nbsp全&nbsp&nbsp选&nbsp' onclick='updateselectall()';/></td><td style='width:82%;'><input type='button'  style='color:#666666;background:#f1f1f1' name='noselect' id='noselect' value='全不选' onclick='updatenoselect()';/></td></tr>"
								for ( var i = 0; i < map.moduleList.length; i++) {
										
									for ( var key in map.moduleList[i]) {
										var power = (map.moduleList[i])[key];
										var name=key;
										var index=name.toString().indexOf('-');
										var BoxName=name.substring(0,index);
										var BoxValue=parseInt(name.substring(index+1,name.length))+50;
										listHtml += " <tr >";
										listHtml += " <td ><div style='float:left;margin-left:5px;'><input type='checkbox' name='"
												+ BoxValue
												+ "1' id="
												+ BoxValue
												+ " value="
												+ BoxValue
												+ " onclick='checksingle(this.id)' style=border:none;/><label style='color:#626161;font-size:14px;font-weight:bold;'>"
												+ BoxName + ":" + "</label></div></td><td>";
										for ( var k = 0; k < power.length; k++) {
											listHtml += "<div style='float:left;margin-left:5px;'><input type='checkbox'";
											listHtml += " name=" + BoxValue;
											listHtml += " id=" + power[k].t_id;
											listHtml += " value="
													+ power[k].t_id;
											listHtml += " onclick='checkfinal(this.name)'";
											listHtml += " onchange='checkfinal(this.name)'";
											listHtml += " style=border:none;/><font style='color:#666666'>"
													+ power[k].powerName;
											listHtml += " </font></div>"
										}
										listHtml += " </td>";
										listHtml += " </tr>";
									}
								}
								listHtml += " </table>";
								$("#updateselectmodule").html(listHtml);
								$(".stripe table").css("width", "100%");
								$(".stripe tr").attr("bgColor", "#f1f1f1");// 为单数行表格设置背景颜色
								$(".stripe tr").css("height", "35px");
								$(".stripe tr:even").css("background-color",
										"#ffffff");// 为双数行表格设置背颜色素
								for ( var j = 0; j < showpower.powerlist.length; j++) {
									if (showpower.powerlist[j] < 1000) {
										$(
												"input[value='"
														+ showpower.powerlist[j]
														+ "']:checkbox").each(
												function() {
													this.checked = true;
												});
									} else {
										$(
												"input[value='"
														+ (showpower.powerlist[j] - 1000)
														+ "']:checkbox").each(
												function() {
													this.indeterminate = true;
												});
									}

								}
							});

			$('#updateroletree')
					.tree(
							{
								checkbox : true,
								animate : true,
								lines : false,
								url : 'roletree/queryRoleTree.do',
								cache : true,
								onLoadSuccess : function(node) {

									var nodes = $('#updateroletree').tree(
											'getChildren');
									for (i = 0; i < nodes.length; i++) {
										if (nodes[i].id >= 40000) {
											$('#updateroletree')
													.tree(
															'update',
															{
																target : nodes[i].target,
																iconCls : 'tree-terminal'
															});
										} else {
											$('#updateroletree')
													.tree(
															'update',
															{
																target : nodes[i].target,
																iconCls : 'tree-group'
															});
										}
									}
									;

									for ( var k = 0; k < showterminal.terminallist.length; k++) {
										var node1 = $('#updateroletree').tree(
												'find',
												showterminal.terminallist[k]);
										$('#updateroletree').tree('check',
												node1.target);
									}
									for ( var k = 0; k < showterminalgroup.terminallist.length; k++) {
										var node1 = $('#updateroletree')
												.tree(
														'find',
														showterminalgroup.terminallist[k]);
										$('#updateroletree').tree('check',
												node1.target);
									}
								}
							});

		} else {
			ZENG.msgbox.show("只能选择一个角色", 1, 3000);
		}
	}

}

function checksingle(key) {
	if ($("#" + key).is(":checked") == true) {
		$("input[name='" + key + "']:checkbox").each(function() {
			this.checked = true;
		});
	} else {
		$("input[name='" + key + "']:checkbox").each(function() {
			this.checked = false;
		});
	}
}

function checkfinal(key) {
	var i = 0;
	var j = 0;
	$("[name ='" + key + "']:checkbox").each(function() {
		if (this.checked == true) {
			i++;
		} else if (this.checked == false) {
			j++;
		}
	});
	if (i == 0) {
		$("input[name='" + key + "1']:checkbox").each(function() {
			this.indeterminate = false;
		});
		$("input[name='" + key + "1']:checkbox").each(function() {
			this.checked = false;
		});
	} else if (j == 0) {
		$("input[name='" + key + "1']:checkbox").each(function() {
			this.indeterminate = false;
		});
		$("input[name='" + key + "1']:checkbox").each(function() {
			this.checked = true;
		});
	} else if (i > 0 && j > 0) {
		$("input[name='" + key + "1']:checkbox").each(function() {
			this.indeterminate = true;
		});
	}
}
function deleteRole() {
	var role = $('#rolelist').datagrid('getSelections');
	if (role.length == 0) {
		ZENG.msgbox.show("请选择一个角色", 1, 2000);
		return;
	} else {
		if (role[0].role_id == 1) {
			ZENG.msgbox.show("此角色不能被删除", 1, 2000);
			return;
		} else {
			$.messager.defaults = {
				ok : "是",
				cancel : "否"
			};
			$.messager.confirm("操作提示", "您确定要执行操作吗?", function(data) {
				if (data) {
					var roleId = [];
					var roleName = [];
					for ( var i = 0; i < role.length; i++) {
						roleId[i] = role[i].role_id;
						roleName[i] = role[i].role_name;
					}

					var datas = {
						"roleId" : roleId,
						"rolename" : roleName
					};
					$.ajax( {
						url : "role/deleteRoleByRoleId.do",
						type : "POST",
						contentType : "application/x-www-form-urlencoded",
						data : datas,
						success : function(data) {
							if (data.success == "true") {
								ZENG.msgbox.show("删除成功", 4, 3000);
								$("#rolelist").datagrid('reload');
							} else {
								ZENG.msgbox.show("角色正在被使用,不能删除", 4, 3000);
							}
						}
					});
				} else {
					return;
				}
			});

		}
	}
}
function saveRole() {
	var selectId = [];

	$("#newroleaa :checked").each(function(i) {
		if (this.value == "on" || this.value == "") {
			return;
		} else {
			selectId[selectId.length] = this.value;
		}
	})
	var selecthalf = [];
	$("#newroleaa :indeterminate").each(function(i) {
		selecthalf[i] = this.value;
	})
	var checknodes = [];
	var nodes = $('#roletree').tree('getChecked');
	for ( var i = 0; i < nodes.length; i++) {
		checknodes[i] = nodes[i].id;
	}
	var indeterminates = [];
	var nodess = $('#roletree').tree('getChecked', 'indeterminate');
	for ( var i = 0; i < nodess.length; i++) {
		indeterminates[i] = nodess[i].id;
	}

	var rolename = $('#rolename').val().split(' ').join('');
	var roledes = $('#roledes').val();
	if (roledes == "请输入描述") {
		roledes = "";
	}
	if (selectId.length == 0) {
		ZENG.msgbox.show("角色权限不能为空", 3, 2000);
		return;
	}
	var schedulelevel =  document.getElementById("pubpower").value;
	var datas = {
		"rolename" : rolename,
		"roledes" : roledes,
		"selectId" : selectId,
		"checknodes" : checknodes,
		"indeterminates" : indeterminates,
		"selecthalf" : selecthalf,
		"schedulelevel" : schedulelevel
		
	};

	var nverify = null;
	var datas1 = {
		"rolename" : rolename
	};

	$.ajax( {
		url : "role/RoleVerify.do",
		type : "POST",
		contentType : "application/x-www-form-urlencoded",
		data : datas1,
		async : false,
		success : function(result) {
			if (result.success == 1) {
				nverify = 1;
			}
		}
	});

	if (rolename == "请输入角色名称") {
		ZENG.msgbox.show("角色名称不能为空", 3, 2000);
		return;
	} else {
		if (flagvalid == 1) {
			ZENG.msgbox.show("角色名称不能有特殊字符且长度大于5", 3, 2000);
		} else if (rolename.length > 20) {
			ZENG.msgbox.show("角色名称长度不能大于20", 3, 2000);
			return;
		} else if (flagvalid == 0) {
			if (nverify == 1) {
				ZENG.msgbox.show("角色名称已存在", 3, 2000);
			} else {
				var isChrome = navigator.userAgent.toLowerCase()
						.match(/chrome/) != null;
				if (isChrome) {
					$("#savebut").css("visibility", "hidden");
				} else {
					$("#savebut").attr("disabled", "disabled");
				}
				$.messager.progress( {
					title : "提交中",
					msg : "数据提交中......",
					interval : 200
				});
				$
						.ajax( {
							url : "role/insertRole.do",
							type : "POST",
							contentType : "application/x-www-form-urlencoded;charset=UTF-8",
							data : datas,
							success : function() {
								ZENG.msgbox.show("添加成功", 4, 3000);
								setTimeout(function() {
									$('#newrole').window('close');
									$('#rolelist').datagrid('reload');
								}, 700);

							}
						});
			}
		}

	}
}

function updatesaveRole() {
	var updateselectId = [];
	$("#updateroleaa :checked").each(function(i) {
		if (this.value == "on" || this.value == "") {
			return;
		} else {
			updateselectId[updateselectId.length] = this.value;
		}

	})
	var updateselecthalf = [];

	$("#updateroleaa :indeterminate").each(function(i) {
		updateselecthalf[i] = this.value;
	})

	var updatechecknodes = [];
	var nodes = $('#updateroletree').tree('getChecked');
	for ( var i = 0; i < nodes.length; i++) {
		updatechecknodes[i] = nodes[i].id;
	}

	var indeterminates = [];
	var nodess = $('#updateroletree').tree('getChecked', 'indeterminate');
	for ( var i = 0; i < nodess.length; i++) {
		indeterminates[i] = nodess[i].id;
	}
	var udpaterolenameStr = $('#updaterolename').val().split(' ').join('');
	var udpateroledesStr = $('#updateroledes').val();
	if (udpateroledesStr == "请输入描述") {
		udpateroledesStr = "";
	}
	var schedulelevel =  document.getElementById("updatepubpower").value;
	var datas = {
		"roleid" : role[0].role_id,
		"updaterolename" : udpaterolenameStr,
		"updateroledes" : udpateroledesStr,
		"updateselectId" : updateselectId,
		"updatechecknodes" : updatechecknodes,
		"indeterminates" : indeterminates,
		"updateselecthalf" : updateselecthalf,
		"schedulelevel" : schedulelevel
	};

	var nverify = null;
	if (udpaterolenameStr == role[0].role_name) {

	} else {
		var datas1 = {
			"rolename" : udpaterolenameStr
		};
		$.ajax( {
			url : "role/RoleVerify.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded",
			data : datas1,
			async : false,
			success : function(result) {
				if (result.success == 1) {
					nverify = 1;
				}
			}
		});
	}

	if (flagvalid == 1) {
		ZENG.msgbox.show("角色名称不能有特殊字符", 3, 2000);
	} else if (udpaterolenameStr.length > 20) {
		ZENG.msgbox.show("角色名称长度不能大于20", 3, 2000);
		return;
	} else if (flagvalid == 0) {
		if (nverify == 1) {
			ZENG.msgbox.show("角色名已存在", 3, 2000);
			return;
		} else {

			var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
			if (isChrome) {
				$("#updaterolebut").css("visibility", "hidden");
			} else {
				$("#updaterolebut").attr("disabled", "disabled");
			}
			$.messager.progress( {
				title : "提交中",
				msg : "数据提交中......",
				interval : 200
			});
			$
					.ajax( {
						url : "role/updateRole.do",
						type : "POST",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						data : datas,
						success : function() {
							ZENG.msgbox.show("修改成功", 4, 2000);
							setTimeout(function() {
								$('#updaterole').window('close');
								$('#rolelist').datagrid('reload');
							}, 700);

						}
					});
		}
	}
}
function datarefresh() {
	$('#searchname').textbox('setValue',"");
	$("#rolelist").datagrid('reload', {
		"role_name" : ""
	});
}

function changeImg() {
	$("#searchimg").attr("src", "img/searchchange.png");
}
function changeImg2() {
	$("#searchimg").attr("src", "img/search2.png");
}
function showpower(i) {
	$('#showpower').window('open');
	$('#rolepowertree').tree( {
		checkbox : false,
		animate : true,
		lines : false,
		queryParams : {
			roleid : i
		},
		url : 'role/queryShowRolePower.do',
		cache : true,
		onLoadSuccess : function(node) {
		}
	});

	$('#roleterminaltree').tree( {
		checkbox : false,
		animate : true,
		lines : false,
		queryParams : {
			roleid : i
		},
		url : 'roletree/queryRoleTreeByRoleID.do',
		cache : true,
		onLoadSuccess : function(node) {
			var nodes = $('#roleterminaltree').tree('getChildren');
			for (i = 0; i < nodes.length; i++) {
				if (nodes[i].id >= 40000) {
					$('#roleterminaltree').tree('update', {
						target : nodes[i].target,
						iconCls : 'tree-terminal'
					});
				} else {
					$('#roleterminaltree').tree('update', {
						target : nodes[i].target,
						iconCls : 'tree-group'
					});
				}
			}
			;
		}
	});
}

function searchRole() {
	var grid = $('#rolelist')
	var options = grid.datagrid('getPager').data("pagination").options;
	var curr = options.pageNumber;
	var roleName = $('#searchname').val();
	$("#rolelist").datagrid('reload', {
		"role_name" : roleName,
		"page" : curr,
		"rows" : 10
	});
}
function updatecancel() {
	$('#updaterole').window('close');

}

function onfocusrole(id, value) {
	$('#' + id).css( {
		"color" : "black"
	})
	if (value == "请输入角色名称") {
		$('#' + id).val("");
	} else if (value == "请输入描述") {
		$('#' + id).val("");
	}
}

function blurrole(id) {
	if ($('#' + id).val() == '') {
		$('#' + id).css( {
			"color" : "#aaaaaa"
		})
		if (id == "rolename") {
			$('#' + id).val("请输入角色名称");
		} else if (id == "roledes") {
			$('#' + id).val("请输入描述");
		}
	}

}

function updateblurrole(id) {
	if ($('#' + id).val() == '') {
		$('#' + id).css( {
			"color" : "#aaaaaa"
		})
		if (id == "updaterolename") {
			$('#' + id).val("请输入角色名称");
		} else if (id == "updateroledes") {
			$('#' + id).val("请输入描述");
		}
	}

}

function nameverify() {
	var rolenamestr = $('#rolename').val();
	var datas = {
		"rolename" : rolenamestr
	};
	$.ajax( {
		url : "role/RoleVerify.do",
		type : "POST",
		contentType : "application/x-www-form-urlencoded",
		data : datas,
		async : false,
		success : function(result) {
			if (result.success == 1) {
				ZENG.msgbox.show("角色名称名已存在,请更换", 3, 2000);
			} else {
			}
		}
	});
}

function updatenameverify() {
	role = $('#rolelist').datagrid('getSelections');
	var updaterolenamestr = $('#updaterolename').val();
	var datas = {
		"rolename" : updaterolenamestr
	};
	if (updaterolenamestr == role[0].role_name) {

	} else {
		$.ajax( {
			url : "role/RoleVerify.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded",
			data : datas,
			success : function(result) {
				if (result.success == 1) {
					ZENG.msgbox.show("角色名称名已存在,请更换", 3, 2000);
				} else {

				}
			}
		});
	}
}

function init() {
	$.ajax( {
		url : "modulepower/queryModulePowerID.do",
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		async : false,
		success : function(result) {
			for ( var i = 0; i < result.moduleList.length; i++) {
				if (result.moduleList[i] == 5) {
					$("#addroletd").show();
				}
				if (result.moduleList[i] == 6) {
					$("#updateroletd").show();
				}
				if (result.moduleList[i] == 7) {
					$("#deleroletd").show();
				}
			}
		}
	});

}
