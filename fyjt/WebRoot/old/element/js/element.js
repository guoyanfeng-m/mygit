//记录输入文本时关闭按钮的次数，用于解决点击次数过多字体样式不可用的BUG
	var times = 0;
	//记录分类的个数
	var classifyNum;
	var change = 3;
	var id;
	var typeValue = 3;
	var width = 1100;
	var height = 488;
	var size = 12;
	var font = '黑体';
	var cp3="black";
	var cp4="white";
	var direction="";
	var content="";
	var speed=1;
	var cen1=false;
	var cen2=false;
	var hasPass = false; //ie 走不动判断
	var textvalue="";
	var typeName = [ "", "e_text", "e_vedio", "e_picture", "e_movie", "e_html",
			"e_Flash", "e_office" ,"e_stream","e_other"];
	var typeCName = [ "", "文本", "音频", "图片", "视频", "网页", "FLASH", "文档","流媒体" ,"其它"];
	var viewData;
	
	//格式化时间
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, //month 
			"d+" : this.getDate(), //day 
			"h+" : this.getHours(), //hour 
			"m+" : this.getMinutes(), //minute 
			"s+" : this.getSeconds(), //second 
			"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter 
			"S" : this.getMilliseconds()
		//millisecond 
		}

		if (/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}

		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	}
	//查询数据

	function loadGrid(data) {
		var classifydata = {
			parentId : change
		};
		var totals = 0;
		$('#selectable').html("");
		loadClassify(classifydata);
		$
				.ajax({
					url : 'element/queryElement.do',
					method : "POST",
					data : data,
					dataType : 'json',
					success : function(data) {
						var pagIn = $('#pagInationT');
						pagIn.pagination({
							total : data.total

						});
						viewData = data.rows;
						for (var i = 0; i < data.rows.length; i++) {
							var elementName = data.rows[i].elem_name;
							var elemNameLength = elementName.substring(0,
									elementName.lastIndexOf("."));
							if (elemNameLength.length > 11) {
								var a = elementName.substring(0, 11);
								var b = elementName.substring(elementName
										.lastIndexOf("."));
								elementName = a + "..." + b;
							}
							if (elemNameLength.length == 0 && elementName.length > 11){
								elementName = elementName.substring(0, 11)+ "...";
							}
							var standard = 160;
							var arg;
							var newWidth;
							var newHeight;
							var oldWidth ;
							var oldHeight
							var resolution = data.rows[i].resolution;
							if(resolution!="" && resolution!=undefined){
								oldWidth = parseInt(resolution.split('x')[0]);
								oldHeight = parseInt(resolution.split('x')[1]);
								if(oldHeight>=oldWidth){
									arg = standard / oldHeight;
									newHeight = standard;
									newWidth = oldWidth * arg;
								}else{
									arg = standard / oldWidth;
									newWidth = standard;
									newHeight = oldHeight * arg;
								}
							}else{
								newWidth = 140;
								newHeight = 160;
							}
							if (data.rows[i].audit_status == 0) {
								$('#selectable')
										.append(
												'<li oncontextmenu="property();return false;" eletname="'
														+ data.rows[i].elem_name
														+ '" auditstatus="'
														+ data.rows[i].audit_status
														+ '" elemPath="'
														+ data.rows[i].elem_path
														+ '" description="'
														+ data.rows[i].description
														+ '" id="'
														+ data.rows[i].elem_id
														+ '"  class="ui-state-default">'
														+ '<div><font color=\'#CD9B1D\'>'
														+ elementName
														+ '</font></div><img width="'+newWidth+'px" height="'+newHeight+'px" src="'+data.rows[i].thumbnailUrl+'"></img>' + '</li>');
							} else if (data.rows[i].audit_status == 1) {
								$('#selectable')
										.append(
												'<li oncontextmenu="property();return false;" eletname="'
														+ data.rows[i].elem_name
														+ '" auditstatus="'
														+ data.rows[i].audit_status
														+ '" elemPath="'
														+ data.rows[i].elem_path
														+ '" description="'
														+ data.rows[i].description
														+ '" id="'
														+ data.rows[i].elem_id
														+ '"  class="ui-state-default">'
														+ '<div><font color=\'green\'>'
														+ elementName
														+ '</font></div><img width="'+newWidth+'px" height="'+newHeight+'px" src="'+data.rows[i].thumbnailUrl+'"></img>' + '</li>');
							} else {
								$('#selectable')
										.append(
												'<li oncontextmenu="property();return false;" eletname="'
														+ data.rows[i].elem_name
														+ '" auditstatus="'
														+ data.rows[i].audit_status
														+ '" elemPath="'
														+ data.rows[i].elem_path
														+ '" description="'
														+ data.rows[i].description
														+ '" id="'
														+ data.rows[i].elem_id
														+ '" class="ui-state-default">'
														+ '<div><font color=\'red\'>'
														+ elementName
														+ '</font></div><img width="'+newWidth+'px" height="'+newHeight+'px"  src="'+data.rows[i].thumbnailUrl+'"></img>' + '</li>');
							}
						}
					},
					error : function(e) {

					}
				});

	}
	$(function() {
		$("#selectable").selectable();
		$("#classify").selectable();
	});
	//添加ol右键事件 
	function property() {
		var event = event ? event : window.event;
		var left = event.x;
		var top = event.y;
		if (navigator.appName.indexOf("Microsoft Internet Explorer") != -1
				&& document.all) {
			left = event.clientX;
			top = event.clientY;
		}
		var elems = $('.ui-selected.ui-state-default');
		var idslength = elems.length;
		if (idslength == 0) {
			return;
		}
		if (typeValue > 10) {
			var elems = $('.ui-selected.classify');
			var node = elems[0].id;
			var creatorid = $('#'+node).attr("creatorid");
			if(parseInt(creatorid)!=parseInt(currentuserId) &&  parseInt(currentuserId)!=1){
				$('#regionMenu_moveclassify1').hide();
				$('#regionMenu_moveclassify_11').hide();
				}else{
						$('#regionMenu_moveclassify1').show();
						$('#regionMenu_moveclassify_11').show();
					}
		} else {
			$('#regionMenu_moveclassify1').hide();
			$('#regionMenu_moveclassify_11').hide();
		}
		$('#regionMenu').menu('show', {
			left : left,
			top : top
		});
	}
	//右键事件 属性方法
	function elementProperty() {
		var elems = $('.ui-selected.ui-state-default');
		var idslength = elems.length;
		if (idslength > 1) {
			ZENG.msgbox.show("只能选择一条数据进行查看!", 2, 3000);
			return;
		}
		$('#sx').window('open');
		var tempList = new Array();
		for (var i = 0; i < viewData.length; i++) {
			if (elems[0].id == viewData[i].elem_id) {
				$('#sxelemName').textbox("setValue", viewData[i].elem_name);
				queryCreator(viewData[i].creator_id);
				$('#sxelemSize').textbox("setValue",
						(viewData[i].elem_size / 1048576).toFixed(2) + "M");
				$('#sxelemType').textbox("setValue",
						typeCName[viewData[i].type]);
				if (change == 3 || change == 4) {
					if (viewData[i].resolution == "") {
						$('#sxelemResolution').textbox("setValue",
								"该素材获取分辨率信息失败!");
					}
					$('#sxelemResolution').textbox("setValue",
							viewData[i].resolution);
				} else {
					$('#sxelemResolution').textbox("setValue", "该素材无此信息!");
				}
				if (change == 5|| change == 8) {
					$('#sxelemMD5').textbox("setValue", "该素材无此信息!");
				} else {
					$('#sxelemMD5').textbox("setValue", viewData[i].md5);
				}
				$('#sxelemUploadtime').textbox(
						"setValue",
						new Date(viewData[i].create_time)
								.format("yyyy-MM-dd hh:mm:ss"));
				$('#sxelemEdittime').textbox(
						"setValue",
						new Date(viewData[i].update_time)
								.format("yyyy-MM-dd hh:mm:ss"));
				$('#sxelemDescription').val(viewData[i].description);

			}
		}
	}
	//右键事件  显示分类目录
	function sendTo() {
		if (classifyNum == 0) {
			ZENG.msgbox.show("暂无分类信息!", 4, 3000);
			return;
		}
		$('#classifyName').combobox({
			url : 'element/queryClassifyName.do?parentId=' + change,
			valueField : 'type_id',
			textField : 'type_name',
			onLoadSuccess : function() {
				var data = $('#classifyName').combobox('getData');
				if (data.length > 0) {
					$('#classifyName').combobox('select', data[0].type_id);
				}
			}
		});
		$('#ml').window('open');
	}
	//查询素材上传者
	function queryCreator(creatorId) {
		var data = {
			"user_id" : creatorId
		};
		$.ajax({
			url : 'element/queryCreatorById.do',
			method : "POST",

			data : data,
			dataType : 'json',
			success : function(data) {
				$('#sxcreater').textbox("setValue", data.msg);
			},
			error : function(e) {
				ZENG.msgbox.show("查询素材上传者失败!", 5, 3000);
			}
		});
	}
	// 页面左面button点击切换搜索类别方法   
	function queryBytype(type) {
		typeValue = type;
		var eleName = $("#searchName").val();
		if (type == 1) {
			id = 'e_text';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 2) {
			id = 'e_vedio';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 3) {
			id = 'e_picture';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 4) {
			id = 'e_movie';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 5) {
			id = 'e_html';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 6) {
			id = 'e_Flash';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 7) {
			id = 'e_office';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 8) {
			id = 'e_stream';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}else if (type == 9) {
			id = 'e_other';
			if (change == type) {
				$("#" + id).attr("src", "img/" + id + "1.png");
			} else {
				change = typeName[change];
				$("#" + id).attr("src", "img/" + id + "1.png");
				$("#" + change).attr("src", "img/" + change + ".png");
			}
			change = type;
		}		
		var data = {
			type : typeValue,
			elem_name : eleName,
			page : 1,
			rows : 10
		};
		loadGrid(data);
		var pagInn = $('#pagInationT');
		$(pagInn).pagination({
			pageNumber : 1
		});
	}
	//素材添加
	function addElemt() {
		if(typeValue > 10){
			var elems = $('.ui-selected.classify');
			var node = elems[0].id;
			var  classifyName = $('#'+node).attr("classifyName");
			var creatorid = $('#'+node).attr("creatorid");
			if(parseInt(creatorid)!=parseInt(currentuserId) &&  parseInt(currentuserId)!=1){
				ZENG.msgbox.show("您不能往已选择的文件夹中上传文件!", 5, 3000);
				return;
				}
			}
		$('#zj').window('open');
		//改变文本编辑字体颜色
		$("#cp3").colorpicker({
			fillcolor : true,
			success : function(o, color) {
				$("#text_show").css("color", color);
				$("#textcolor").val(color);
				cp3=color;
			}
		});
		//改变文本编辑背景颜色
		$("#cp4").colorpicker({
			fillcolor : true,
			success : function(o, color) {
				$("#text_show").css("background-color", color);
				$("#textareacolor").val(color);
				cp4=color;
			}
		});
		//改变文本框大小
		$("#resolution").textbox({
			onChange:function(value,rows){
				var text = $("#text_show")[0].localName;
				if(text!=undefined&&text!="textarea"){
					$("#text_show").css("width",value-4);
					width = value;
				}else{
					$("#text_show").css("width",value);
					width = value;
				}
				Obox();
			}
		});
		$("#resolution1").textbox({
			onChange:function(value,rows){
				var text = $("#text_show")[0].localName;
				if(text!=undefined&&text!="textarea"){
					$("#text_show").css("height",value-4);
					height = value;
				}else{
					$("#text_show").css("height",value);
					height = value;
					}
				Obox();
			}
		});
		//改变文本编辑字体样式
		$("#textFont").combobox({
			onChange : function(n, o) {
				switch(n){
				case "0":
					n = "黑体";
					break;
				case "1":
					n = "华文行楷";
					break;
				case "2":
					n = "宋体";
					break;
				case "3":
					n = "仿宋";
					break;
				case "4":
					n = "方正舒体";
					break;
				case "5":
					n = "方正姚体";
					break;
				case "6":
					n = "微软雅黑";
					break;
				case "7":
					n = "幼圆";
					break;
				}
				$("#text_show").css("font-family", n);
				$('#textFont').combobox('setValue', n);
				font=n;
			}
		});
	}
	//获取浏览器版信息
	function getBrowserInfo()
{
var agent = navigator.userAgent.toLowerCase() ;

var regStr_ie = /msie [\d.]+;/gi ;
var regStr_ff = /firefox\/[\d.]+/gi
var regStr_chrome = /chrome\/[\d.]+/gi ;
var regStr_saf = /safari\/[\d.]+/gi ;
//IE
if(agent.indexOf("msie") > 0)
{
return agent.match(regStr_ie) ;
}

//firefox
if(agent.indexOf("firefox") > 0)
{
return agent.match(regStr_ff) ;
}

//Chrome
if(agent.indexOf("chrome") > 0)
{
return agent.match(regStr_chrome) ;
}

//Safari
if(agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0)
{
return agent.match(regStr_saf) ;
}
}


	//增加多媒体方法
	function addElem() {
		var browser = getBrowserInfo() ;
		var verinfo = (browser+"").replace(/[^0-9.]/ig,""); 
		var verNum = parseInt(verinfo.split(".")[0]);
	if(browser!=null && browser[0].indexOf('chrome')>-1 && verNum>42){
				ZENG.msgbox.show("您的浏览器版本不支持上传插件，请更换浏览器!", 5, 3000);
				return;
			}
		$.ajax({
					url : 'element/queryFtpHttpMessage.do',
					method : "POST",

					dataType : 'json',
					success : function(data) {
						$('#z').window({
							onClose : function() {
								reFresh();
							}
						});
						$('#z').window('open');
						var temptype = typeValue;
						if(parseInt(temptype)<10){
							temptype="";
							}
						var info = window.location.hostname;
						var ftpip = data.configBeanList[1].config_value;
						var httpip = data.configBeanList[2].config_value;
						if(info == data.configBeanList[4].config_value){
							ftpip = data.configBeanList[4].config_value;
							httpip = data.configBeanList[5].config_value;
						}
						var temphtml = '<object  id="plugin0" type="application/x-mmstools" width="600" height="300">'
								+ '<param name="type" value="uploadElement" />'
								+ '<param name="proj" value="fyjt" />'
								+ '<param name="debug" value="false" />'
								+ '<param name="lang" value="ch" />'
								+ '<param name="ftpip" value="'+ftpip+'" />'
								+ '<param name="uploadport" value="'+data.configBeanList[6].config_value+'" />'
								+ '<param name="downloadport" value="'+data.configBeanList[0].config_value+'" />'
								+ '<param name="httpip" value="'+httpip+'" />'
								+ '<param name="httpport" value="'+data.configBeanList[3].config_value+'" />'
								+ '<param name="uploadpath" value="/" />'
								+ '<param name="userid" value="'+tid+'" />'
								+ '<param name="class"  value="'+temptype+'"  />'
								+ '</object>';
						setTimeout("downloadTool()",1500);                 //ie走不动
						$('#addCode').html(temphtml);
						hasPass = true;
						var temp = document.getElementById("plugin0")
						if(temp.version!="3.0.0.0"){
							$('#addCode').html("");
							$('#z').window('close');
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
						var pagIn7 = $('#pagInationT');
						$(pagIn7).pagination({
							pageNumber : 1
						});
					},
					error : function(e) {
						alert("加载上传控件失败!请确认后重试!");
					}
				});

	}
	//增加文本本件方法
	function addText() {
		$('#wb').window({
						onClose : function() {
							resetText();
						}
					});
		//改变文本框大小
		$("#resolution").textbox({
			onChange:function(value,rows){
				var text = $("#text_show")[0].localName;
				if(text!=undefined&&text!="textarea"){
					$("#text_show").css("width",value-4);
					width = value;
				}else{
					$("#text_show").css("width",value);
					width = value;
				}
				Obox();
			}
		});
		$("#resolution1").textbox({
			onChange:function(value,rows){
				var text = $("#text_show")[0].localName;
				if(text!=undefined&&text!="textarea"){
					$("#text_show").css("height",value-4);
					height = value;
				}else{
					$("#text_show").css("height",value);
					height = value;
					}
				Obox();
			}
		});
		$("#direction").combobox({
			onChange:function(value,d){
				if(cen1&&cen2==false&&value==1){
					direction='left';
					marquee();
				}else if(cen2&&cen1==false&&value==0){
					direction='up';
					marquee();
				}else if((cen1||cen2)&&value==2){
					direction="";
					$("#textarea span").html('<textarea id="text_show" name ="textarea" onKeyUp="Obox()" style="border:2px solid #E8F2FC;resize:none;width: '+width+'px;height: '+height+'px;color:'+cp3+'; background-color: '+cp4+';font-size:'+size+'; font-family: '+font+';overflow:hidden;position: absolute;line-height:'+size+'px;"></textarea>');
					$("#text_show").val(content);
					Obox();
				}else if(!(cen1||cen2)&&value==0){
					direction='up';
					marquee();
				}else if(!(cen1||cen2)&&value==1){
					direction='left';
					marquee();
				}else if(!(cen1||cen2)&&value==2){
					direction="";
					$("#textarea span").html('<textarea id="text_show" name ="textarea" onKeyUp="Obox()" style="border:2px solid #E8F2FC;resize:none;width: '+width+'px;height: '+height+'px;color:'+cp3+'; background-color: '+cp4+';font-size:'+size+'; font-family: '+font+';overflow:hidden;position: absolute;line-height:'+size+'px;"></textarea>');
					$("#text_show").val(content);
					Obox();
				}else{
					if(value==0){
						ZENG.msgbox.show("请取消上下居中选项!", 4, 3000);
						return;
					}
					if(value==1){
						ZENG.msgbox.show("请取消左右居中选项!", 4, 3000);
						return;
					}
					$("#direction").combobox('setValue',2);
				}
			}
		});
		$('#zj').window('close');
		$('#wb').window('open');
		$('#textFont').combobox('setValue', '黑体');
		$("#direction").combobox('setValue',2);
		direction="";
		textvalue="1";
	}
	function addWeb(){
		$('#zj').window('close');
		$('#web').window('open');
	}
	function addStream(){
		$('#zj').window('close');
		$('#stream').window('open');
		}
	//删除方法 	  
	function deleteElemById(id) {
		var datas = {
			"elem_id" : id
		};
		$.ajax({
			url : 'element/deleteElementById.do',
			method : "POST",

			data : datas,
			dataType : 'json',
			success : function(data) {
				var pagIn2 = $('#pagInationT');
				$(pagIn2).pagination({
					pageNumber : 1
				});
				//$('#searchName').textbox("setValue","");
				queryBytype(typeValue);
				ZENG.msgbox.show("删除成功!", 4, 3000);
			},
			error : function(e) {
				ZENG.msgbox.show("删除失败!", 5, 3000);
			}
		});
	}
	function deleteElem() {
		var elems = $('.ui-selected.ui-state-default');
		var idslength = elems.length;
		if (idslength == 0) {
			ZENG.msgbox.show("请先选择一条数据!", 2, 3000);
			return;
		}
		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
		$.messager.confirm("操作提示", "您确定要执行操作吗?", function(data) {
			if (data) {
				var id = [];
				for (var i = 0; i < elems.length; i++) {
					id[i] = elems[i].id;
				}
				deleteElemById(id);
			} else {
				return;
			}
		});
	}
	function txChange(s,textsize){
		var num = Math.floor(s/Math.round(textsize));
		if(num==0)num=1;
		else if (s%Math.round(textsize)>0){
			num = num+1;
		}
		return num;
		}
	var linage =0;
	var height_y = "";
	function highAndLowCenter(){
		var centerup = document.getElementsByName("cen");
		var resolution1 = $("#resolution1").val();
		var resolution = $("#resolution").val();
		var textsize = $("#textSize").val();
		var value = $("#text_show")[0].value;
		var textLength = (parseInt(resolution)-parseInt(textsize)*1/4)/parseInt(textsize);
		var cs = "";
		linage=0;
		if(value==undefined||value=="")cs = content.split("\n");
		else if(value==content){
			cs = content.split("\n");
		}else{
			cs = value.split("\n");
		}
		$("#compare").css("font-family", font);
		$("#compare").css("font-size",size+'px');
		if(cs[0]==""){
			$("#compare").val("a");
			height_y = $("#compare")[0].scrollHeight-2;
		}
		if((cs.length==1&&cs[0]!="")||cs.length>1)
			a:for(var i=0;i<cs.length;i++){
				$("#compare").val("");
				$("#compare").val(cs[i]);
				height_y = $("#compare")[0].scrollHeight-2;
				if($("#compare")[0].scrollWidth+3>=resolution){
					var com = "";
					b:for(var j =0 ;j<cs[i].length;j++){
						$("#compare").val("");
						com+=cs[i][j];
						$("#compare").val(com);
						if($("#compare")[0].scrollWidth+3>=resolution){
							if(cs[i][j]==" "){
								com += cs[i][j].replace(/\s/g, "");
							}else{
								linage+=1;
								if(j==cs[i].length-1){
									j--;
									com="";
								}else{
									com=cs[i][j];
								}
							}
						}else if(j>=cs[i].length-1){
							linage+=1;
						}
					}
				}else{
					if(i==0){
						linage=1;
					}else
					linage+=1;
				}
//						if(j==0)com = cs[i].substring(0,textLength);
//						if($("#compare")[0].scrollWidth*2<resolution)
//							com+=cs[i][Math.floor(textLength*count+textLength/2*count+j)];
//						else if((textLength*count+j)<cs[i].length)
//							com+=cs[i][Math.floor(textLength*count+j)];
//						$("#compare").val("");
//						$("#compare").val(com);
//						if($("#compare")[0].scrollWidth>resolution){
//							linage = linage+1;
//							count+=1;
//							if($("#compare")[0].scrollWidth*2<resolution)
//								com=cs[i].substring(Math.floor(textLength*(count-1)+(textLength/2*count)+j-1),Math.floor(textLength*count+j));
//							else if((textLength*count+j)<cs[i].length)
//								com=cs[i].substring(Math.floor(textLength*(count-1)+j-1),Math.floor(textLength*count+j));
//							else{
//								com = cs[i].substring(Math.floor(textLength*(count-1)+j-1),cs[i].length);
//								break b;
//							}
//						}
				
			}
			var top =""
		if(linage<=Math.floor(resolution1/parseInt(textsize))){
			if(centerup[0].checked){
				top = (height - parseInt(size)*linage)/2;
				$("#text_show")[0].style.paddingTop=top+"px";
				cen1=true;
			}else {
				$("#text_show")[0].style.paddingTop="";
				centerup[0].checked = false;
				cen1=false;
			}
		}else{
			if(centerup[0].checked){
				top = 0;
				$("#text_show")[0].style.paddingTop=top+"px";
				cen1=true;
			}else{
				$("#text_show")[0].style.paddingTop="";
				centerup[0].checked = false;
				cen1=false;
			}
			if(direction==""){
				$("#msg").text("当前行数为："+Math.floor(resolution1/parseInt(textsize))+"/"+linage+"  您已超出最大行数！！！");
				$("#msg").css("color","#FF0000");
				return;
			}
		}
			$("#msg").text("当前行数为："+Math.floor(resolution1/parseInt(textsize))+"/"+linage);
			$("#msg").css("color","#AAAAAA");
	}
	function Obox(){
		var resolution = $("#resolution1").val();
		var m_top = Math.floor((parseInt(resolution)-parseInt(size))/2);
		var text = $("#text_show")[0].localName;
		var centerup = document.getElementsByName("cen");
		var text_show = $("#text_show");
		if(text!='marquee')text_show[0].style.lineHeight = size+"px";
		if(direction=="up"&&text=='marquee'){
			text_show = $("#mq");
		}
			if(direction!="up"&&centerup[0].checked){
				if(direction==""){
					highAndLowCenter();
					if(centerup[1].checked){
						if(text!='marquee')$("#text_show")[0].style.textAlign="center";
//						text_show.css("align","center");
							cen2=true;
					}else{
						if(text!='marquee')$("#text_show")[0].style.textAlign="";
//						text_show.css("align","");
						centerup[1].checked = false;
						cen2=false;
					}
					return;
				}else if(direction=="left"&&!centerup[1].checked){
					text_show[0].style.paddingTop = m_top+"px";
					text_show.css("height",parseInt(resolution)-m_top+"px");
					cen1=true;
					cen2=false;
					return;
				}else{
					if(text!='marquee')$("#text_show")[0].style.textAlign="";
//					text_show.css("align","");
					centerup[1].checked = false;
					cen2=false;
					ZENG.msgbox.show("选择左右滚动特效时不可进行左右居中设置!", 5, 3000);
					return;
				}
			}else if(centerup[0].checked&&direction=="up"){
				$("#text_show")[0].style.paddingTop="";
				centerup[0].checked = false;
				cen1=false;
				ZENG.msgbox.show("选择上下滚动特效时不可进行上下居中设置!", 5, 3000);
				return;
			}else if(centerup[1].checked&&direction!="left"){
				if(direction==""){
					if(text!='marquee')
						$("#text_show")[0].style.textAlign="center";
//					text_show.css("align","center");
						cen2=true;
					if(!centerup[0].checked){
						$("#text_show")[0].style.paddingTop="";
						centerup[0].checked = false;
						cen1=false;
					}
					highAndLowCenter();
					return;
				}else if(direction=="up"&&!centerup[0].checked){
					if(text!='marquee')$("#text_show")[0].style.textAlign="center";
//					text_show.css("align","center");
						cen1=false;
						cen2=true;
				}else{
					$("#text_show")[0].style.paddingTop="";
					centerup[0].checked = false;
					cen1=false;
					ZENG.msgbox.show("选择上下滚动特效时不可进行上下居中设置!", 5, 3000);
					return;
				}
			}else if(centerup[1].checked&&direction=="left"){
				if(text!='marquee')$("#text_show")[0].style.textAlign="";
//				text_show.css("align","");
				centerup[1].checked = false;
				cen2=false;
				ZENG.msgbox.show("选择左右滚动特效时不可进行左右居中设置!", 5, 3000);
				return;
			}else{
				highAndLowCenter();
				document.getElementById("text_show").style.paddingTop = "";
				if(text!='marquee')$("#text_show")[0].style.textAlign="";
//				text_show.css("align","");
				centerup[1].checked = false;
				cen2=false;
				if(text!='marquee')
				return;
			}
			marquee();
	}
	
	
	//编辑素材方法		
	function showMessage() {
		var elems = $('.ui-selected.ui-state-default');
		if (elems.length == 0) {
			ZENG.msgbox.show("请先选择一条数据!", 1, 3000);
			return;
		}
		if (elems.length > 1) {
			ZENG.msgbox.show("只能选择一条数据操作!", 1, 3000);
			return;
		}
		if (change == 1) {
			var data = {
				"elem_id" : elems[0].id
			};
			$.ajax({
				url : "element/getTextInfo.do",
				type : "POST",
				data : data,
				success : function(data) {
					$("#textId").val(elems[0].id);
					//改变文本编辑字体颜色
					$("#cp3").colorpicker({
						fillcolor : true,
						success : function(o, color) {
							$("#text_show").css("color", color);
							$("#textcolor").val(color);
							cp3=color;
						}
					});
					//改变文本编辑背景颜色
					$("#cp4").colorpicker({
						fillcolor : true,
						success : function(o, color) {
							$("#text_show").css("background-color", color);
							$("#textareacolor").val(color);
							cp4=color;
						}
					});
					//改变文本框大小
					$("#resolution").textbox({
						onChange:function(value,rows){
							var text = $("#text_show")[0].localName;
						if(text!=undefined&&text=='marquee')
						$("#text_show").css("width",value-4);
						else{
							$("#text_show").css("width",value);
						}
						Obox();
						width = value;
						}
					});
					$("#resolution1").textbox({
						onChange:function(value,rows){
							var text = $("#text_show")[0].localName;
						if(text!=undefined&&text=='marquee'){
							$("#text_show").css("height",value-4);
						}else{
							$("#text_show").css("height",value);
						}
						Obox();
						height = value;
						}
					});
					var centerup = document.getElementsByName("cen");
					if(data.center=="0"){
						cen1=true;
						cen2=false;
						centerup[0].checked=true;
						centerup[1].checked=false;
					}else if(data.center == "1"){
						cen1=false;
						cen2=true;
						centerup[0].checked=false;
						centerup[1].checked=true;
					}else if(data.center == "2"){
						cen1=true;
						cen2=true;
						centerup[0].checked=true;
						centerup[1].checked=true;
					}else {
						cen1=false;
						cen2=false;
						centerup[0].checked=false;
						centerup[1].checked=false;
					}
					$("#direction").combobox({
						onChange:function(value,d){
							if(cen1&&cen2==false&&value==1){
								direction='left';
								marquee();
							}else if(cen2&&cen1==false&&value==0){
								direction='up';
								marquee();
							}else if((cen1||cen2)&&value==2){
								direction="";
								$("#textarea span").html('<textarea id="text_show" name ="textarea" onKeyUp="Obox()" style="border:2px solid #E8F2FC;resize:none;width: '+width+'px;height: '+height+'px;color:'+cp3+'; background-color: '+cp4+';font-size:'+size+'; font-family: '+font+';overflow:hidden;position: absolute;line-height:'+size+'px;"></textarea>');
								$("#text_show").val(content);
								Obox();
							}else if(!(cen1||cen2)&&value==0){
								direction='up';
								marquee();
							}else if(!(cen1||cen2)&&value==1){
								direction='left';
								marquee();
							}else if(!(cen1||cen2)&&value==2){
								direction="";
								$("#textarea span").html('<textarea id="text_show" name ="textarea" onKeyUp="Obox()" style="border:2px solid #E8F2FC;resize:none;width: '+width+'px;height: '+height+'px;color:'+cp3+'; background-color: '+cp4+';font-size:'+size+'; font-family: '+font+';overflow:hidden;position: absolute;line-height:'+size+'px;"></textarea>');
								$("#text_show").val(content);
								Obox();
							}else{
								if(value==0){
									ZENG.msgbox.show("请取消上下居中选项!", 4, 3000);
									return;
								}
								if(value==1){
									ZENG.msgbox.show("请取消左右居中选项!", 4, 3000);
									return;
								}
								$("#direction").combobox('setValue',2);
							}
						}
					});
					//改变文本编辑字体样式
					$("#textFont").combobox({
						onChange : function(n, o) {
							switch(n){
								case "0":
									n = "黑体";
									break;
								case "1":
									n = "华文行楷";
									break;
								case "2":
									n = "宋体";
									break;
								case "3":
									n = "仿宋";
									break;
								case "4":
									n = "方正舒体";
									break;
								case "5":
									n = "方正姚体";
									break;
								case "6":
									n = "微软雅黑";
									break;
								case "7":
									n = "幼圆";
									break;
								}
							$("#text_show").css("font-family", n);
							$('#textFont').combobox('setValue', n);
							font = n;
						}
					});
					$('#wb').window({
						onClose : function() {
							resetText();
						}
					});
					content=data.content;
					if(data.direction==0){
						direction='up';
					}else if(data.direction==1){
						direction='left';
					}else{
						direction="";
					}
					cp3 = data.fgcolor;
					cp4 = data.bgcolor;
					var resolut = data.resolution.split("x");
					$("#resolution").textbox("setValue",resolut[0]);
					$("#resolution1").textbox("setValue",resolut[1]);
					$("#text_show").text(data.content);
					$('#textSize').numberspinner('setValue', data.size);
					$('#textName').textbox("setValue", data.title);
					$('#textFont').combobox('setValue', data.font);
					$("#text_show").css("font-family", data.font);
					$("#textareacolor").val(data.bgcolor);
					$("#text_show").css("background-color", data.bgcolor);
					$("#textcolor").val(data.fgcolor);
					$("#text_show").css("color", data.fgcolor);
					$("#speed").numberspinner('setValue',data.speed);
					$("#direction").combobox('setValue',data.direction);
					if(data.bjtm!=""){
						document.getElementById('bjtm').checked=true;
						$("#text_show").css("background-color", "");
						document.getElementById("cp4").style.visibility = "hidden";
						//$('#cp4').hide();
					}else{
						document.getElementById('bjtm').checked=false;
						document.getElementById("cp4").style.visibility = "visible";
						//$('#cp4').show();
					}
					$('#wb').window('open').dialog('setTitle', '文本编辑');
					// $(".easyui-tabs").tabs('select', '文本编辑');
					// $(".easyui-tabs").tabs('disableTab', '网页上传');
					// $(".easyui-tabs").tabs('disableTab', '媒体文件上传');
					textvalue="1";
				},
				error : function(e) {
					//$('#b').window('close');
					ZENG.msgbox.show("修改失败!", 5, 3000);
				}

			})
		} else {
			document.getElementById("elementAddressTR").style.visibility="hidden";
			//document.getElementById("elementAddressTR").style.display="none";
			var node = elems[0].id;
			var elename = $('#' + node).attr("eletname");
			var eleName = elename.substring(0, elename.lastIndexOf("."));
			$('#elementId').textbox("setValue", elems[0].id);
			$('#elementprifix').textbox("setValue",
					elename.substring(elename.lastIndexOf(".")));
			if (change == 5) {
				document.getElementById("elementAddressTR").style.visibility="visible";
				//$("#elementAddressTR").style("display","block");
				$('#elementName').textbox("setValue", elename);
				var path =  $('#' + node).attr("elemPath");
				$("#elementAddress").textbox("setValue",path);
			}else if (change == 8) {
				document.getElementById("elementAddressTR").style.visibility="visible";
				//$("#elementAddressTR").style("display","block");
				$('#elementName').textbox("setValue", elename);
				var path =  $('#' + node).attr("elemPath");
				$("#elementAddress").textbox("setValue",path);
			}else{
				$('#elementName').textbox("setValue", eleName);
			}
			if (null == $('#' + node).attr("description")
					|| "null" == $('#' + node).attr("description")) {
				$('#description').val($('#' + node).attr(""));
			} else {
				$('#description').val($('#' + node).attr("description"));
			}
			$('#b').window('open');
		}
	}
	function editElement() {
		var elementId = $('#elementId').val();
		var elementprifix = $('#elementprifix').val();
		var elementName = $('#elementName').val();
		var elemPath = $('#elementAddress').val();
		var elementRealName = ""
		if (change == 5) {
			if (elemPath.match(/http:\/\/.+/) == null&&elemPath.match(/https:\/\/.+/)==null) {
				ZENG.msgbox.show("您输入的网页地址有误,请确认后重新输入!样例: http://www.sohu.com", 5,
						3000);
				return;
			}
			if (checkInput(elementName)) {
				ZENG.msgbox.show("网页名称不能有特殊字符!", 5, 3000);
				return;
			}
//			if (elementName.length > 20) {
//			ZENG.msgbox.show("网页名称长度不能大于20个字符!", 3, 3000);
//			return;
//			}
		}
//		else if(change ==8){
//			var elementRealName = elementName;
//			if (elemPath.match(/http:\/\/.+/) == null&&elemPath.match(/https:\/\/.+/)==null) {
//				ZENG.msgbox.show("您输入的网页地址有误,请确认后重新输入!样例: http://www.sohu.com", 5,
//						3000);
//				return;
//			}
//		}
		if(change == 5 || change ==8){
			elementRealName = elementName;
		}else{
			elementRealName = elementName + elementprifix;
		}
		if (elementName==null || elementName=="") {
			ZENG.msgbox.show("素材名称不能为空!", 3, 3000);
			return;
		}
		if (elementRealName.length > 50) {
			ZENG.msgbox.show("素材名称长度过长!", 3, 3000);
			return;
		}
		
		var description = $('#description').val();
		if (description.length > 200) {
			ZENG.msgbox.show("素材描述长度过长!", 3, 3000);
			return;
		}
		var data = {
			"elem_id" : elementId,
			"elem_name" : elementRealName,
			"description" : description,
			"elem_path":elemPath
		};
		$.ajax({
			url : "element/updateElementById.do",
			type : "POST",
			data : data,
			success : function(data) {

					$('#b').window('close');
					ZENG.msgbox.show("修改成功!", 4, 3000);
					$('#searchName').textbox("setValue", "");
					queryBytype(typeValue);
					//loadGrid({type:change,page:1,rows:10});
					var pagIn5 = $('#pagInationT');
					$(pagIn5).pagination({
						pageNumber : 1
					});

			},
			error : function(e) {
				$('#b').window('close');
				ZENG.msgbox.show("修改失败!", 5, 3000);
				var pagIn6 = $('#pagInationT');
				$(pagIn6).pagination({
					pageNumber : 1
				});
			}

		})
	}
//编辑窗口关闭事件
	$(document).ready(function(){
	    
		   $('#b').window({
		       onBeforeClose:function(){ 
			   $('#elementAddress').val("");   
		       }
		   });
		});

	//审核方法
	function auditE() {
		var elems = $('.ui-selected.ui-state-default');
		if (elems.length == 0) {
			ZENG.msgbox.show("请先选择一条数据!", 1, 3000);
			return;
		}
		for (var i = 0; i < elems.length; i++) {
			var sta = $('#' + elems[i].id).attr("auditstatus");
			if (sta == 1 || sta == 2) {
				ZENG.msgbox.show("包含已审核项!", 1, 3000);
				return;
			}
		}
		var auditdata;
		var id = [];
		$.messager.defaults = {
			ok : "审核通过",
			cancel : "审核不通过"
		};
		$.messager.confirm("操作提示", "素材审核", function(data) {
			if (data) {
				for (var j = 0; j < elems.length; j++) {
					id[j] = elems[j].id;
				}
				auditdata = {
					"elem_id" : id,
					"audit_status" : 1
				};
				auditElement(auditdata);
			} else {
				for (var k = 0; k < elems.length; k++) {
					id[k] = elems[k].id;
				}
				auditdata = {
					"elem_id" : id,
					"audit_status" : 2
				};
				auditElement(auditdata);
			}
		});
	}

	function auditElement(data) {
		$.ajax({
			url : "element/auditElementById.do",
			type : "POST",
			data : data,
			success : function(data) {
				ZENG.msgbox.show("审核成功!", 4, 3000);
				//$('#searchName').textbox("setValue","");
				queryBytype(typeValue);
			},
			error : function(e) {
				ZENG.msgbox.show("审核失败!", 5, 3000);
				$('#searchName').textbox("setValue", "");
				queryBytype(typeValue);
			}

		})

	}
	//刷新方法
	function reFresh() {
		$('#searchName').textbox("setValue", "");
		queryBytype(change);
	}
	//搜索框切换特效
	function changeImg() {
		$("#searchimg").attr("src", "img/searchchange.png");
	}
	function changeImg2() {
		$("#searchimg").attr("src", "img/search2.png");
	}
	//搜索框搜索方法	
	function searchName() {
		var eleName = $("#searchName").val().trim();
		var temp = $("#classify .ui-selected");
		var temptype = change;
		if(temp.length>0) temptype=temp[0].id;
		var data = {
			type : temptype,
			elem_name : eleName,
			page : 1,
			rows : 10
		};
		loadGrid(data)

	}
	//初始化加载	
	function initLoad() {
		$.ajax({
			url : "modulepower/queryModulePowerID.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			async : false,
			success : function(result) {
				for (var i = 0; i < result.moduleList.length; i++) {
					if (result.moduleList[i] == 8) {
						$("#eleadd").show();
					}
					if (result.moduleList[i] == 9) {
						$("#eleedit").show();
						$("#regionMenu_edit1").show();
						$("#regionMenu_edit11").show();
					}
					if (result.moduleList[i] == 10) {
						$("#eledele").show();
						$("#regionMenu_delete1").show();
						$("#regionMenu_delete11").show();
					}
					if (result.moduleList[i] == 23) {
						$("#eleaudit").show();
						$("#regionMenu_audit1").show();
						$("#regionMenu_audit11").show();
					}
				}
			}
		});

		var data = {
			type : 3,
			page : 1,
			rows : 10
		};
		if (change == 3) {
			id = 'e_picture';
			$("#" + id).attr("src", "img/" + id + "1.png");
		}
		loadGrid(data);
		$("#pagInationT").pagination({
			onBeforeRefresh : function() {
				// alert('before refresh');  
			},
			onRefresh : function(pageNumber, pageSize) {
				// alert("onRefresh");  
			},
			onChangePageSize : function() {
				/// alert('pagesize changed');  
			},
			onSelectPage : function(pageNumber, pageSize) {
				var eleName = $("#searchName").val();
				var data = {
					type : typeValue,
					elem_name : eleName,
					page : pageNumber,
					rows : pageSize
				};
				loadGrid(data);
			}
		});
	}
	//图片预览
	function preView() {
		var widths = $(window).width()-90;
		var heights = $(window).height()-85;
		if (change == 1) {
			ZENG.msgbox.show("文本文件无法预览!", 3, 3000);
			return;
		}
		if (change == 2) {
			ZENG.msgbox.show("音频文件无法预览!", 3, 3000);
			return;
		}
		if (change == 8) {
			ZENG.msgbox.show("流媒体文件无法预览!", 3, 3000);
			return;
		}		
		var elems = $('.ui-selected.ui-state-default');
		var tempList = new Array();
		//var temp = new Array();
		if (elems.length == 0) {
			$(function() {
				for (var i = 0; i < viewData.length; i++) {
					var thumburl = viewData[i].thumbnailUrl;
					if(change==4||change==5||change==6||change=="4"||change=="5"||change=="6"){
						thumburl = thumburl.substring(0, 18) +thumburl.substring(18);
					}else{
						thumburl = thumburl.substring(0, 18) + 'large_'
								+ thumburl.substring(18);
					}
					var h=0;
					var w=0;
					var s=0;
					if(viewData[i].resolution!=undefined && viewData[i].resoltuion != ""){
						var he=parseInt(viewData[i].resolution.split("x")[1]);
						var wi=parseInt(viewData[i].resolution.split("x")[0]);
						var b=heights/he>widths/wi ? true:false;
						if(b){
							s=widths/wi;
						}else{
							s=heights/he;
						}
						h=he*s;
						w=wi*s;
					}
					temp = {
						url : thumburl,
						caption : viewData[i].elem_name,
						height:h,
						width:w,
						options : {
							side : 'top'
						}
					}
					tempList.push(temp);
				}
				if(tempList.length==0){
					ZENG.msgbox.show("没有任何素材可供预览!", 3, 3000);
				}else{
					Strip.show(tempList);
				}
			});
		} else {
			for (var j = 0; j < elems.length; j++) {
				for (var k = 0; k < viewData.length; k++) {
					if (elems[j].id == viewData[k].elem_id) {
						var thumburl = viewData[k].thumbnailUrl;
						if(change==4||change==5||change=="4"||change==6||change=="6"||change=="5"){
							thumburl = thumburl.substring(0, 18) +thumburl.substring(18);
						}else{
							thumburl = thumburl.substring(0, 18) + 'large_'
									+ thumburl.substring(18);
						}
						var h=0;
						var w=0;
						var s=0;
						if(viewData[k].resolution!=undefined && viewData[k].resoltuion != ""){
							var he=parseInt(viewData[k].resolution.split("x")[1]);
							var wi=parseInt(viewData[k].resolution.split("x")[0]);
							var b=heights/he>widths/wi ? true:false;
							if(b){
								s=widths/wi;
							}else{
								s=heights/he;
							}
							h=he*s;
							w=wi*s;
						}
						temp = {
							url : thumburl,
							caption : viewData[k].elem_name,
							height:h,
							width:w,
							options : {
								side : 'top'
							}
						};
						tempList.push(temp);
						break;
					}
				}
				
			}
			Strip.show(tempList);
		}
	}
	//上传网页
	//重置网页名称地址输入框
	function reset() {
		$('#htmlName').textbox("setValue", "");
		$("#htmladdress").textbox("setValue", "");
	}
	//重置流媒体地址输入框
	function resetStream() {
		$('#streamName').textbox("setValue", "");
		$("#streamaddress").textbox("setValue", "");
	}	
	//验证特殊字符
	var checkInput = function(str) {
		var pattern = /^[\w\u4e00-\u9fa5]+$/gi;
		if (pattern.test(str)) {
			return false;
		}
		return true;
	}
	//提交流媒体数据
	function submintStream() {

		var streamName = $('#streamName').val();
		var streamAddress = $("#streamaddress").val();
		if (streamName.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '') == ""
				|| streamName == null || streamName == 'undefined') {
			ZENG.msgbox.show("流媒体名称不能为空!", 5, 3000);
			return;
		}
		if (streamAddress == "" || streamAddress == null
				|| streamAddress == 'undefined') {
			ZENG.msgbox.show("流媒体地址不能为空!", 5, 3000);
			return;
		}
		if (checkInput(streamName)) {
			ZENG.msgbox.show("流媒体名称不能有特殊字符!", 5, 3000);
			return;
		}
		$.ajax({
			url : "element/queryClassifyByTypeID.do",
			type : "POST",
			data : {
				"typeValue" : typeValue
			},
			success : function(data) {
				if (!data.success) {
					ZENG.msgbox.show(data.msg, 5, 3000);
				} else {
					if(data.parentid != 8){
						typeValue = 8;
					}
					var address = streamAddress;
					var data = {
						elem_name : streamName,
						elem_path : address,
						classify:typeValue
					};
					$.ajax({
						url : "element/insertStream.do",
						type : "POST",
						data : data,
						success : function(data) {
							if (!data.success) {
								ZENG.msgbox.show(data.msg, 5, 3000);
							} else {
								ZENG.msgbox.show("上传成功!", 4, 3000);
								resetStream();
								queryBytype(typeValue);
							}
						},
						error : function(e) {
			
						}
					})
					}
			}
		});

	}
	function IsURL(str_url){
        var reg = /((((^https?)|(^ftp)):\/\/)?([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i   
        //re.test()
        if (reg.test(str_url)){
            return (true);
        }else{
            return (false);
        }
	}

	//提交网页数据
	function submintHtml() {

		var htmlName = $('#htmlName').val();
		var htmlAddress = $("#htmladdress").val();
		if (htmlName.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '') == ""
				|| htmlName == null || htmlName == 'undefined') {
			ZENG.msgbox.show("网页名称不能为空!", 5, 3000);
			return;
		}
		if (htmlAddress == "" || htmlAddress == null
				|| htmlAddress == 'undefined') {
			ZENG.msgbox.show("网页地址不能为空!", 5, 3000);
			return;
		}
		if (checkInput(htmlName)) {
			ZENG.msgbox.show("网页名称不能有特殊字符!", 5, 3000);
			return;
		}
		if (!IsURL(htmlAddress)||(htmlAddress.match(/http:\/\/.+/) == null&&htmlAddress.match(/https:\/\/.+/)==null)) {
			ZENG.msgbox.show("您输入的网页地址有误,请确认后重新输入!样例: http://www.sohu.com", 5,
					3000);
			return;
		}
//		if(htmlName.length > 20){
//			ZENG.msgbox.show("网页名称不能大于20个字符!", 5,
//					3000);
//			return;
//		}
		var address = htmlAddress;
		var data = {
			elem_name : htmlName,
			elem_path : address,
			classifytype:typeValue
		};
		$.ajax({
			url : "element/insertHtml.do",
			type : "POST",
			data : data,
			success : function(data) {
				if (!data.success) {
					ZENG.msgbox.show(data.msg, 5, 3000);
				} else {
					ZENG.msgbox.show("上传成功!", 4, 3000);
					reset();
					queryBytype(typeValue);
				}
			},
			error : function(e) {

			}
		})

	}
	//从分类中移除
	function moveFromClassify() {
		var elems = $('.ui-selected.ui-state-default');
		var idslength = elems.length;
		var element_id = [];
		for (var i = 0; i < idslength; i++) {
			element_id[i] = elems[i].id;
		}
		var data = {
			"element_id" : element_id,
			"type_id" : typeValue
		};
		subMoveFromClassify(data);
	}
	function subMoveFromClassify(data) {
		$.ajax({
			url : 'element/subMoveFromClassify.do',
			method : "POST",
			data : data,
			dataType : 'json',
			success : function(data) {
				queryBytype(typeValue);
				ZENG.msgbox.show("已从分类中移除!", 4, 3000);
			},
			error : function(e) {
				ZENG.msgbox.show("从分类中移除失败!", 5, 3000);
			}
		});
	}
	//文本编辑
	//改变字体大小
	function changeSize() {
		var textSize = $("#textSize").val();
		var sp = $("#speed").val();
		//textarea.style.fontSize = textSize;
//		var width = $("text_show").css("width");
//		$("#text_show").css("width", "1100px");
		setTimeout(function() {
//			$("#text_show").css("width", width);
			$("#text_show").css("font-size", textSize);
			$("#text_show")[0].scrollamount=sp;
		}, 0);
		speed=sp;
		size=textSize;
		if(direction!=""){
			marquee();
			Obox();
		}
		else Obox()
	}
	//重置文本输入
	function resetText() {
		textvalue="";
		content = "";
		width = 1100;
		height = 488;
		size = 12;
		font = '黑体';
		cp3="black";
		cp4="white";
		direction="";
		speed=1;
		cen1=false;
		cen2=false;
		$("#resolution").textbox("setValue",1100);
		$("#resolution1").textbox("setValue",488);
		var centerup = document.getElementsByName("cen");
			centerup[0].checked=false;
			centerup[1].checked=false;
		$('#textName').textbox("setValue", "");
		$("#textId").val(-1);
		$('#textSize').numberspinner('setValue', 12);
		$('#textFont').combobox('setValue', '黑体');
		$("#text_show").css("background-color", '#FFFFFF');
		$("#text_show").css("color", '#000000');
		$("#textareacolor").css("color", '#FFFFFF');
		$("#textcolor").val('#000000');
		$("#textareacolor").val('#FFFFFF');
		$("#direction").combobox('setValue',2);
		$("#speed").numberspinner('setValue',1);
		$("#bjtm").attr('checked',false);
		$("#text_show").val("");
		document.getElementById("cp4").style.visibility = "visible";
		//$('#cp4').show();
	}
	//提交
	function submintText() {
		highAndLowCenter();
		var id = $("#textId").val();
		var textName = $("#textName").val();
		var values = $("#text_show").val();
		if(values!=""&&values!=undefined)content=values;
		var textAreaContent = content;
		var textSize = $("#textSize").val();
		var textFont = $("#textFont").combobox("getValue");
		var textcolor = $("#textcolor").val();
		var direction=$("#direction").combobox('getValue');
		if(direction==0&&cen1){
			ZENG.msgbox.show("上下滚动时不能上下居中!", 5, 3000);
			return;
		}else if(direction==1&&cen2){
			ZENG.msgbox.show("左右滚动式不能左右居中!", 5, 3000);
			return;
		}else if(direction==2&&Math.floor(parseInt(height)/parseInt(height_y))<linage){
			ZENG.msgbox.show("无特效时文字不能超过编辑框边界!", 5, 3000);
			return;
		}
		var speed=$("#speed").numberspinner('getValue');
		var textareacolor = $("#textareacolor").val();	
		var resolution = "";
		var radio =document.getElementsByName("cen");
		var cen = "";
		for ( var i = 0; i < radio.length; i++) {
			if	(radio[i].checked){
				if(radio[i].value=="01"){
					cen += "0";
				}else if(radio[i].value=="02"){
					cen += "1";
				}
			}
		}
		if(parseInt($("#resolution").val().trim()) < 30 || parseInt($("#resolution1").val().trim()) < 30){
			ZENG.msgbox.show("分辨率不能小于30!", 5, 3000);
			return;
		}
		if($("#resolution").val().length > 0 && $("#resolution1").val().length > 0)
			resolution = $("#resolution").val()+"x"+$("#resolution1").val();
		if(direction == "从右自左")
			direction = 1;
		else if(direction == "从下自上")
			direction = 0;
		else if(direction == "无特效")
			direction = 2;
		var bjtm="";
		if($("#bjtm").is(":checked")){
		   bjtm=$("#bjtm").val();
		   textareacolor="";
		}
		if (textName.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '') == "") {
			ZENG.msgbox.show("标题名称不能为空!", 5, 3000);
			return;
		}
		if (textName.length > 10) {
			ZENG.msgbox.show("标题名称过长!", 5, 3000);
			return;
		}
		if (textAreaContent.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '') == "") {
			ZENG.msgbox.show("文本内容不能为空!", 5, 3000);
			return;
		}
		if (textAreaContent.length > 20000) {
			ZENG.msgbox.show("文本内容过长!", 5, 3000);
			return;
		}

		if (checkInput(textName)) {
			ZENG.msgbox.show("标题名称不能有特殊字符!", 5, 3000);
			return;
		}
		if ("" == textSize || null == textSize) {
			ZENG.msgbox.show("字体大小不能为空!", 5, 3000);
			return;
		}
		if ("" == speed || null == speed) {
			ZENG.msgbox.show("滚动速度不能为空!", 5, 3000);
			return;
		}
		$.ajax({
			url : "element/queryClassifyByTypeID.do",
			type : "POST",
			data : {
				"typeValue" : typeValue
			},
			success : function(data) {
				if (!data.success) {
					ZENG.msgbox.show(data.msg, 5, 3000);
				} else {
					if(data.parentid != 1){
						typeValue = 1;
					}
					var data = {
						id : id,
						textName : textName,
						resolution : resolution,
						cen : cen,
						textAreaContent : textAreaContent,
						textSize : textSize,
						textFont : textFont,
						textcolor : textcolor,
						textareacolor : textareacolor,
						classify:typeValue,
						direction:direction,
						speed:speed,
						bjtm:bjtm
					};
					$.ajax({
						url : "element/insertText.do",
						type : "POST",
						data : data,
						success : function(data) {
							if (!data.success) {
								ZENG.msgbox.show(data.msg, 5, 3000);
							} else {
								ZENG.msgbox.show("文本已保存!", 4, 3000);
								resetText();
								$('#wb').window('close');
								queryBytype(typeValue);
							}
						},
						error : function(e) {
			
						}
					})
				}
			},
			error : function(e) {

			}
		});

	}

	function testTimes(){
			if(times>6){
				 window.location.href="element/element.jsp";
				}
		}
//分类相关js
	//新建分类
	function newClassify(){
		$('#flelemName').textbox("setValue","");
		$('#fl').window('open');
	}
	//加载分类文件夹
	function loadClassify(data){
		//$('#classifyDiv').show();
		$('#classify').html("");
		$.ajax({ 
	    	url: "element/queryClassify.do", 
	    	type: "POST",
	    	data: data,
	    	success: function(data){
				if(data.rows.length==0){
					classifyNum=0;
					//$('#classifyDiv').hide();
				}else{
					classifyNum=1;
					for(var i=0; i<data.rows.length; i++){
						var classifyName = data.rows[i].type_name;
						var is_private = data.rows[i].is_private;
						var creator_id = data.rows[i].creator_id;
						var classifyRealName = classifyName;
						if(classifyName.length>10){
							classifyName = classifyName.substring(0,7)+"...";
						}
						var typeid = data.rows[i].type_id;
						$('#classify').append('<li  oncontextmenu="cproperty();return false;"  onmousedown="queryClassify('+typeid+')" creatorid='+creator_id+' isprivate='+is_private+' classifyName='+classifyRealName+' id='+typeid+' class="classify"><img title="'+classifyRealName+'" src="ftpFile/processed/classify.png"></img>'+'<br>'+'<font color=\'blue\'>'+classifyName+'</font>'+'</li>');
						//$('#classifyDiv').hide();
					}
					$("#"+typeValue).addClass("ui-selected");
				}
	      	},
			error: function (e) {

	            }	  
		})
	}
	//提交分类名称
	function subClassifyName(){
		var classifyName = $('#flelemName').val();
		var folderpor = $('#folderproperty').combobox("getValue");
		if(classifyName.length>50){
			ZENG.msgbox.show("分类名称长度过长!", 3, 3000);
			return;
		}
		if(checkInput(classifyName)){
			ZENG.msgbox.show("分类名称不能为空或含有特殊字符!", 3, 3000);
			return;
			}
		var data = {type_name:classifyName,parentId:change,folderpor:folderpor};
		var classifydata = {parentId:change};
		$.ajax({ 
	    	url: "element/insertClassify.do", 
	    	type: "POST",
	    	data: data,
	    	success: function(data){
			if(!data.success){
	    		ZENG.msgbox.show(data.msg, 3, 3000);						
		    	}else{
		    		showClassifyDiv();
		    		ZENG.msgbox.show("创建分类成功!", 4, 3000);
		    		$('#flelemName').textbox("setValue","");
		    		$('#fl').window('close');
		    		loadClassify(classifydata);
		    	}
	      	},
			error: function (e) {
	      		ZENG.msgbox.show("创建分类失败!", 5, 3000);
	            }	  
		})
	}
	//选择分类

	function subclassify(){
		var elems = $('.ui-selected.ui-state-default');
		var idslength = elems.length;
		var type_id = $("#classifyName").combobox("getValue");
		var element_id = [];
		 for(var i=0; i<idslength; i++){
			 element_id[i] = elems[i].id;
		 }
		 var classifyData = {"element_id":element_id,"type_id":type_id};
		 addToClassify(classifyData);
	}
	//添加到分类

	function addToClassify(data){
		$.ajax({
			url : 'element/addToClassify.do',
			method:"POST",
			data : data,
			dataType : 'json',
			success : function(data) {
			$('#ml').window('close');
			ZENG.msgbox.show("已添加到分类!", 4, 3000); 
			},
			error: function (e) {
			ZENG.msgbox.show("添加到分类失败!", 5, 3000); 
	        }
		});
		
	}


	//查询分类里的素材
	function queryClassify(id){
		
		var event = event ? event : window.event;
		var btnNum = event.button;
		if (btnNum==2)
		{
		return;
		}
		typeValue=id;
		$('#selectable').html("");
		var eleName = $("#searchName").val();
		var data = {type:id,elem_name:eleName,page:1,rows:10};
		$.ajax({
			url : 'element/queryElement.do',
			method:"POST",
			
			data : data,
			dataType : 'json',
			success : function(data) {
				var pagIn = $('#pagInationT');
				pagIn.pagination({
					total:data.total,
					 pageNumber: 1
				});
			
				viewData = data.rows;
				for(var i=0;i<data.rows.length;i++){
					var elementName = data.rows[i].elem_name;
					var elemNameLength = elementName.substring(0,elementName.lastIndexOf("."));
					if(elemNameLength.length>11){
						var a = elementName.substring(0,11);
						var b = elementName.substring(elementName.lastIndexOf("."));
						elementName = a+"..."+b;
						}
					var standard = 160;
					var arg;
					var newWidth;
					var newHeight;
					var oldWidth ;
					var oldHeight
					var resolution = data.rows[i].resolution;
					if(resolution!="" && resolution!=undefined){
						oldWidth = parseInt(resolution.split('x')[0]);
						oldHeight = parseInt(resolution.split('x')[1]);
						if(oldHeight>=oldWidth){
							arg = standard / oldHeight;
							newHeight = standard;
							newWidth = oldWidth * arg;
						}else{
							arg = standard / oldWidth;
							newWidth = standard;
							newHeight = oldHeight * arg;
						}
					}else{
						newWidth = 140;
						newHeight = 160;
					}
					if(data.rows[i].audit_status == 0){
					$('#selectable').append('<li oncontextmenu="property();return false;" elemPath="'+data.rows[i].elem_path+'" eletname="'+data.rows[i].elem_name+'" auditstatus="'+data.rows[i].audit_status+'" description="'+data.rows[i].description+'" id="'+data.rows[i].elem_id+'"  class="ui-state-default">'+'<div><font color=\'#CD9B1D\'>'+elementName+'</font></div><img width="'+newWidth+'px" height="'+newHeight+'px" src="'+data.rows[i].thumbnailUrl+'"></img>'+'</li>');
						}else if(data.rows[i].audit_status == 1){
							$('#selectable').append('<li oncontextmenu="property();return false;"  elemPath="'+data.rows[i].elem_path+'" eletname="'+data.rows[i].elem_name+'" auditstatus="'+data.rows[i].audit_status+'" description="'+data.rows[i].description+'" id="'+data.rows[i].elem_id+'"  class="ui-state-default">'+'<div><font color=\'green\'>'+elementName+'</font></div><img width="'+newWidth+'px" height="'+newHeight+'px" src="'+data.rows[i].thumbnailUrl+'"></img>'+'</li>');
							}else{
								$('#selectable').append('<li oncontextmenu="property();return false;"  elemPath="'+data.rows[i].elem_path+'" eletname="'+data.rows[i].elem_name+'" auditstatus="'+data.rows[i].audit_status+'" description="'+data.rows[i].description+'" id="'+data.rows[i].elem_id+'" class="ui-state-default">'+'<div><font color=\'red\'>'+elementName+'</font></div><img width="'+newWidth+'px" height="'+newHeight+'px" src="'+data.rows[i].thumbnailUrl+'"></img>'+'</li>');
								}
					}
			},
			error: function (e) {
	            
	        }
		});
	}
	//删除分类
	function deleteClassify(){
		var elems = $('.ui-selected.classify');
		if(elems.length==0){
			ZENG.msgbox.show("请先选择要删除的文件夹!", 3, 3000); 
			return;
		}

		var str="";
		$.messager.defaults = { ok: "确认", cancel: "取消" };
		  $.messager.confirm("操作提示", "您确定要删除此分类吗?", function (data) {
	         if (data) {
		           var id = [];
	      	   for(var i=0;i<elems.length;i++){
	      		   id[i] = elems[i].id;
	     		}
	      	   for(var j=0;j<id.length;j++){
	      		 var creatorid = $('#'+id[j]).attr("creatorid");
	      		 if(parseInt(creatorid)!=parseInt(currentuserId) &&  parseInt(currentuserId)!=1){
	      			 var folderName = $('#'+id[j]).attr("classifyName");
	      			 if(j==(id.length-1)){
	      				 str += folderName+" ";
	      			 }else{
	      				str += folderName+",";
	      			 }
	      		 }
	      	   }
	      	   if(str!=""){
	      			ZENG.msgbox.show("您不能删除文件夹"+str+"!", 3, 3000); 
	      			return;
	      	   }
	      	   deleteClassifyById(id);
	         }else{
		           return;
	         }
	     });
	}
	function deleteClassifyById(id){
		var datas = {"id":id};		
		$.ajax({
			url : 'element/deleteClassifyById.do',
			method:"POST",
			data : datas,
			dataType : 'json',
			success : function(data) {
			queryBytype(change);
			ZENG.msgbox.show("删除分类成功!", 4, 3000); 
			},
			error: function (e) {
			ZENG.msgbox.show("删除分类失败!", 5, 3000); 
	        }
		});
	}
	//重命名分类
	function reNameClassify(){
		var elems = $('.ui-selected.classify');
		if(elems.length==0){
			ZENG.msgbox.show("请先选择需要重命名的文件夹!", 3, 3000); 
			return;
		}
		if(elems.length>1){
			ZENG.msgbox.show("重命名只能选择一个文件夹!", 3, 3000); 
			return;
		}	
		var node = elems[0].id;
		var  classifyName = $('#'+node).attr("classifyName");
		var isprivate = $('#'+node).attr("isprivate");
		var creatorid = $('#'+node).attr("creatorid");
		if(parseInt(creatorid)!=parseInt(currentuserId) &&  parseInt(currentuserId)!=1){
			ZENG.msgbox.show("您不能更改此文件夹属性!", 3, 3000); 
			return;
		}
		$('#cmmflelemName').textbox("setValue",classifyName);
		$('#cmmfolderproperty').combobox("setValue",isprivate);
		$('#cmm').window('open');
	}
	function subcmmClassidy(){
		var elems = $('.ui-selected.classify');
		var node = elems[0].id;
		var  classifyNameOld = $('#'+node).attr("classifyName");
		var classifyNameNew = $('#cmmflelemName').val();
		var folderpor = $('#cmmfolderproperty').combobox("getValue");
		if(classifyNameNew.length>50){
			ZENG.msgbox.show("分类名称长度过长!", 3, 3000);
			return;
		}
		if(checkInput(classifyNameNew)){
			ZENG.msgbox.show("分类名称不能有特殊字符!", 3, 3000);
			return;
			}
//		if(classifyNameOld==classifyNameNew){
//			ZENG.msgbox.show("无任何修改!", 3, 3000); 
//			return;
//		}
		var datas = {"type_id":node,"type_name":classifyNameNew,"folderpor":folderpor,"parentId":change};
		$.ajax({
			url : 'element/editClassifyName.do',
			method:"POST",
			data : datas,
			dataType : 'json',
			success : function(data) {
			if(!data.success){
	    		ZENG.msgbox.show(data.msg, 3, 3000);						
		    	}else{
		    		 queryBytype(typeValue);
		    		$('#cmm').window('close');
		    		ZENG.msgbox.show("修改分类成功!", 4, 3000); 
		    	}
			},
			error: function (e) {
			ZENG.msgbox.show("修改分类失败!", 5, 3000); 
	        }
		});
	}
	//分类文件夹默认隐藏，点击是显示
	function showClassifyDiv(){
		$('#classifyDiv').show();
	}
	//分类文件夹右键事件
	//添加ol右键事件 
	function cproperty(){
		var event = event ? event : window.event;
		var left = event.x;
		var top = event.y;
		if (navigator.appName.indexOf("Microsoft Internet Explorer") != -1
				&& document.all) {
			left = event.clientX;
			top = event.clientY;
		}
		var elems = $('.ui-selected.classify');
		var idslength = elems.length;
		if(idslength==0){
			return;
			}
//		$("#"+typeValue).addClass("ui-selected");
		$('#cregionMenu').menu('show', {
			left : left,
			top : top
		});
	}	
	function marquee(){
		if($("textarea[name='textarea']").length>0&&textvalue=="1")
			content = $("#text_show").val();
		$("#textarea span").html('<marquee id="text_show" name ="marquee" onclick="textarea()" width="'+(width-4)+'px" height="'+(height-4)+'px" direction='+direction+' behavior="scroll" scrollamount='+speed+' scrolldelay="0"' +
				'style="border:2px solid #E8F2FC;resize:none;color:'+cp3+'; background-color: '+cp4+';line-height:'+size+'px;font-size:'+size+'; font-family: '+font+';overflow-y:auto;word-wrap:break-word;position: absolute;"></marquee>');
		var centerup = document.getElementsByName("cen");
		if(direction=="up"&&centerup[1].checked){
			$("#text_show").html("<center><table></table></center>");
		}else if(direction=="up"){
			$("#text_show").html("<table></table>");
		}
		var con=[];
		var c="";
		var cc="";
		if(cen2)cc="center";
		con = content.split("\n");
		if(direction=="up"){
			for(var i=0;i<con.length;i++){
				c+="<tr><td name='mq' align='"+cc+"' style='font-size:"+size+";color:"+cp3+";line-height:"+size+"px;'>"+con[i]+"</td></tr>";
			}
			$("#text_show table").html(c);
		}else if(direction=="left"){
			for(var i=0;i<con.length;i++){
				c+=con[i];
			}
			$("#text_show").text(c);
			if(centerup[0].checked)
			Obox();
		}	
	}
	function textarea(){
		$("#textarea span").html('<textarea id="text_show" name ="textarea" onBlur="marquee()" onKeyUp="highAndLowCenter()" onmouseout="marquee()" style=";border:2px solid #E8F2FC;resize:none;width: '+width+'px;height: '+height+'px;color:'+cp3+'; background-color: '+cp4+';font-size:'+size+'; font-family: '+font+';overflow:hidden;position: absolute;line-height:'+size+'px;"></textarea>');
		$("#text_show").text(content);
	}
	$(function(){
		init();
	});
	function setbacktran(){
		if($('#bjtm').is(':checked')){
			document.getElementById("cp4").style.visibility = "hidden";
			//$('#cp4').hide();
			$("#text_show").css("background-color", "");
		}else{
			document.getElementById("cp4").style.visibility = "visible";
			//$('#cp4').show();
			$("#text_show").css("background-color", cp4);
		}
	}
	function init(){
		$("#textarea span").html('<textarea id="text_show" name ="textarea" onKeyUp="Obox()" style="border:2px solid #E8F2FC;resize:none;width: '+width+'px;height: '+height+'px;color:'+cp3+'; background-color: '+cp4+';font-size:'+size+'; font-family: '+font+';overflow:hidden;position: absolute;line-height:'+size+'px;"></textarea>');
		if($('#bjtm').is(':checked')){
			document.getElementById("cp4").style.visibility = "hidden";
			//$('#cp4').hide();
			$("#text_show").css("background-color", "");
		}else{
			document.getElementById("cp4").style.visibility = "visible";
			//$('#cp4').show();
			$("#text_show").css("background-color", cp4);
		}
		$('#direction').combobox({ 
			data:[{ 
				id:0, 
				text:"从下自上" 
				},{ 
				id:1, 
				text:"从右自左" 
				},{
				id:2,
				text:"无特效"
				}] , 
			valueField:'id', 
			textField:'text' 
		}); 
		$('#text_show').css('width','1096px');
		$('#text_show').css('height','484px');
		$('#direction').combobox('setValue',2);
		$('#resolution').textbox('setValue',1100);
		$('#resolution1').textbox('setValue',488);
		$('#textFont').combobox({ 
			data: [{
				id: '0',
				text: '黑体'
			},{
				id: '1',
				text: '华文行楷'
			},{
				id: '2',
				text: '宋体'
			},{
				id: '3',
				text: '仿宋'
			},{
				id: '4',
				text: '方正舒体'
			},{
				id: '5',
				text: '方正姚体'
			},{
				id: '6',
				text: '微软雅黑'
			},{
				id: '7',
				text: '幼圆'
			}], 
			valueField:'id', 
			textField:'text' 
		}); 
		$('#textFont').combobox('setValue', '黑体');
    }
	
	/**
	 * ie无法下载暂时修改 
	 */
	function downloadTool(){
		if(!hasPass){
			$('#addCode').html("");
			$('#z').window('close');
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
	}