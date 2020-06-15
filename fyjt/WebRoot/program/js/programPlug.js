/**
 * 绘制区域div
 * 
 * @param regionData
 */
function drawRegion(regionData) {
	var regionRealW = parseInt(regionData.width) * program.editProp - 6;
	var regionRealH = parseInt(regionData.height) * program.editProp - 6;
	var regionLeft = parseInt(regionData.left) * program.editProp;
	var regionTop = parseInt(regionData.top) * program.editProp;
	/*
	 * if(regionData.id.indexOf("region")>-1){ alert(111); }else
	 * if(regionData.id.indexOf("button")>-1){ alert(222); }
	 * 添加窗口 ondblclick双击事件 oncontextmenu右击事件 onMouseDown点击事件
	 */
	var regionhtml = '';
	if(regionData.id == "screenregion"){
		regionhtml = '<div title="" ondblclick="elemManager()" id="'
			+ regionData.id
			+ '" fit="true" style="z-index:'
			+ regionData.zIndex
			+ ';opacity : 0.8;filter:alpha(opacity=80);position:absolute;left:'
			+ regionLeft
			+ 'px;top:'
			+ regionTop
			+ 'px;width:'
			+ regionRealW
			+ 'px;height:'
			+ regionRealH
			+ 'px;background:#ccc;border:3px solid #bbb;margin:0;" class="easyui-tooltip">'
			+ regionData.name + '</div>';
		$("#sceneBack").append(regionhtml);
		//$("#" + regionData.id).html("混播");
		showRegionTitle(regionData);
		showRegionImg(regionData);
		//屏保混播区域不能拖拽及改变大小
		return;
	}else{
		regionhtml = '<div title="" ondblclick="elemManager()" oncontextmenu="showRegionMenu(\''
			+ regionData.id
			+ '\');return false;" onMouseDown="changeSlelectRegion(\''
			+ regionData.id
			+ '\')" id="'
			+ regionData.id
			+ '" fit="true" style="z-index:'
			+ regionData.zIndex
			+ ';opacity : 0.8;filter:alpha(opacity=80);position:absolute;left:'
			+ regionLeft
			+ 'px;top:'
			+ regionTop
			+ 'px;width:'
			+ regionRealW
			+ 'px;height:'
			+ regionRealH
			+ 'px;background:#ccc;border:3px solid #bbb;margin:0;" class="easyui-tooltip">'
			+ regionData.name + '</div>';
	}
	$("#sceneBack").append(regionhtml);
	showRegionTitle(regionData);
	showRegionImg(regionData);
	
	$("#" + regionData.id).draggable({
		scroll : false,
		delay : 0,
		isDrag : true,
		onStopDrag : function(e) {
			checkRegionSize(this, "remove");
		},
		onDrag : function(e) {
			/*
			 * var d = e.data; if (d.left < 0){d.left = 0} if (d.top < 0){d.top =
			 * 0} if (d.left + $(d.target).outerWidth() > $(d.parent).width()){
			 * d.left = $(d.parent).width() - $(d.target).outerWidth(); } if
			 * (d.top + $(d.target).outerHeight() > $(d.parent).height()){ d.top =
			 * $(d.parent).height() - $(d.target).outerHeight(); }
			 */
			var left = parseInt(this.offsetLeft);
			var top = parseInt(this.offsetTop);
			if (Math.abs(parseInt(parseInt(regionSelectData.left)
					* program.editProp)
					- left) > 1) {
				var templeft = parseInt(left / program.editProp);
				/*
				 * var magnetleft = magnetX(templeft); if(magnetleft!=templeft){
				 * e.data.left = parseInt(magnetleft*program.editProp); }
				 * regionSelectData.left = magnetleft;
				 */
				regionSelectData.left = templeft;
			}
			if (Math.abs(parseInt(parseInt(regionSelectData.top)* program.editProp)- top) > 1) {
				var temptop = parseInt(top / program.editProp);
				/*
				 * var magnettop = magnetY(temptop); if(magnettop!=temptop){
				 * e.data.top = parseInt(magnettop*program.editProp); }
				 * regionSelectData.top = magnettop;
				 */
				regionSelectData.top = temptop;
			}
			showRegionAttr();
		}
	});
	// 插件修改
	// 天气组件不能改变组件大小
	if (regionData.id.indexOf("plug") > -1 && regionData.type == 6) {
		if (checkNull(regionData.weather)) {
			$("#" + regionData.id).html("请选择天气样式");
		} else {
			// 类型大于100的可以改变尺寸
			var weather = regionData.weather[0];
			if (parseInt(weather.style) > 100) {
				$("#" + regionData.id).resizable({
					handles : 'e,w',
					maxWidth : realSceneW > (parseInt(weather.width) * 2 * program.editProp)
							? (parseInt(weather.width) * 2 * program.editProp)
							: realSceneW,
					maxHeight : realSceneH > (parseInt(weather.height) * 2 * program.editProp)
							? (parseInt(weather.height) * 2 * program.editProp)
							: realSceneH,
					minWidth : parseInt(weather.width) * 0.4 * program.editProp,
					disabled : false,
					minHeight : 20,
					onStopResize : function(e) {
						var width = parseInt(this.offsetWidth);
						var height = (width * parseInt(weather.height))/ parseInt(weather.width);
						$("#" + regionData.id).css("height", height - 6 + "px");
						checkRegionSize(this, "resize");
						removelock = false;
						checkRegionSize(this, "remove");
						updatePercent(regionSelectData.id);
					},
					onResize : function(e) {
						removelock = true;
						var width = parseInt(this.offsetWidth);
						var height = parseInt(this.offsetHeight);
						regionSelectData.width = parseInt(width
								/ program.editProp);
						regionSelectData.height = parseInt(height
								/ program.editProp);
						showRegionAttr();
					}
				});
			} else {
				$("#" + regionData.id).resizable({disabled : true});
			}
		}
		return;
	}
	//文本固定大小不能改变组件大小
	if(regionData.type=="1" && (regionData.resolutionSize=="1")){
		return ;
	}
	// 时钟组件不能改变组件大小
	if (regionData.id.indexOf("plug") > -1 && regionData.type == 5) {
		if (checkNull(regionData.timepiece)) {
			$("#" + regionData.id).html("请选择时钟样式");
		}
		return;
	}
	drafting(regionData);
}
function drafting(regionData){
	$("#" + regionData.id).resizable({
		maxWidth : realSceneW,
		maxHeight : realSceneH,
		disabled : false,
		minWidth : 20,
		minHeight : 20,
		onStopResize : function(e) {
			checkRegionSize(this, "resize");
			removelock = false;
			checkRegionSize(this, "remove");
		},
		onResize : function(e) {
			removelock = true;
			var height = parseInt(this.offsetHeight);
			var width = parseInt(this.offsetWidth);
			regionSelectData.width = parseInt(width / program.editProp);
			regionSelectData.height = parseInt(height
					/ program.editProp);
			showRegionAttr();
		}
	});
}
/**
 * 显示区域缩略效果图
 * 
 * @param region
 */
function showRegionImg(regionData) {
	if (regionData.id.indexOf("region") > -1) {
		if (!checkNull(regionData.element) && regionData.element.length > 0) {
			var path = regionData.element[regionData.element.length - 1].thumbnailUrl;
			var resolution1 =[];
			var width = "";
			var height = "";
			if(regionData.type=="1" && (regionData.resolutionSize=="1")){
				resolution1 = regionData.element[regionData.element.length-1].resolution.split("x");
				width = resolution1[0];
				height = resolution1[1];
				if(width != undefined && width != ''){
					var regionWidth = parseInt(($('#sceneBack').width() / parseInt(resolutionX)) * width);
					var regionHeight = parseInt(($('#sceneBack').height() / parseInt(resolutionY)) * height);
					$("#"+regionData.id).css('left',$('#sceneBack').width() / parseInt(resolutionX) *regionData.left);
					$("#"+regionData.id).css('top',$('#sceneBack').height() / parseInt(resolutionY) *regionData.top);
					$("#"+regionData.id).css('width',regionWidth);
					$("#"+regionData.id).css('height',regionHeight);
					$("#regionAttrW").val(width);
					$("#regionAttrH").val(height);
					$("#"+regionData.id).resizable({disabled : true});
					$("#regionAttrW").attr('readonly', true );
					$("#regionAttrH").attr('readonly', true );
				}
			}
			$("#" + regionData.id)
					.html(	'<img width="100%" height="100%"  src="' + path
									+ '">');
			if(regionData.resolutionSize == "2"){	
				$("#"+regionData.id).resizable({disabled : false});
				$("#regionAttrW").attr('readonly', false );
				$("#regionAttrH").attr('readonly', false );
			}
		} else {
			if(regionData.id != "screenregion")
				$("#" + regionData.id).html(typeName[regionData.type]);
			else
				$("#" + regionData.id).html("");
		}
	} else if (regionData.id.indexOf("plug") > -1) {
		// 插件修改
		if (regionData.type == 6) {
			if (checkNull(regionData.weather)) {
				$("#" + regionData.id).html("请选择天气样式");
			} else {
				var path = regionData.weather[0].path;
				$("#" + regionData.id)
						.html(	'<img width="100%" height="100%"  src="' + path
										+ '">');
			}
		} else if (regionData.type == 5) {
			if (checkNull(regionData.timepiece)) {
				$("#" + regionData.id).html("请选择时间样式");
			} else {
				var path = regionData.timepiece[0].path;
				$("#" + regionData.id)
						.html(	'<img width="100%" height="100%"  src="' + path
										+ '">');
			}
		} else if (regionData.type == 1) {
			if (checkNull(regionData.album)) {
				$("#" + regionData.id).html("请选择相册样式");
			} else {
				var style = regionData.album[0].style;
				$("#" + regionData.id)
						.html(	'<img width="100%" height="100%"  src="ftpFile\\plugXmlFile\\img\\album\\album_type_'
										+ style + '.jpg">');
			}
		} else if (regionData.type == 8) {
			if (!checkNull(regionData.touchvideo)
					&& !checkNull(regionData.touchvideo[0].element)
					&& regionData.touchvideo[0].element.length > 0) {
				var path = regionData.touchvideo[0].element[regionData.touchvideo[0].element.length
						- 1].thumbnailUrl;
				$("#" + regionData.id)
						.html(	'<img width="100%" height="100%"  src="' + path
										+ '">');
			} else {
				$("#" + regionData.id).html(plugTypeName[regionData.type]);
			}
		} else if (regionData.type == 9) {
			if (checkNull(regionData.count)) {
				$("#" + regionData.id).html("请选择倒计时样式");
			} else {
				var path = regionData.count[0].path;
				$("#" + regionData.id)
						.html(	'<img width="100%" height="100%"  src="' + path
										+ '">');
			}
		} else if (regionData.type == 12) {
			if (checkNull(regionData.count)) {
				$("#" + regionData.id).html("流媒体");
			} else {
				var path = regionData.count[0].path;
				$("#" + regionData.id)
						.html(	'<img width="100%" height="100%"  src="' + path
										+ '">');
			}
		}
	} else if (regionData.id.indexOf("button") > -1) {

		if (!checkNull(regionData.backimg)) {
			var path = regionData.backimg;
			$("#" + regionData.id)
					.html(	'<img width="100%" height="100%"  src="' + path
									+ '">');
		} else {
			$("#" + regionData.id).html(buttonTypeName[regionData.type]);
		}
	}
}
/**
 * 拷贝区域（仅大小、类型）
 */
function copySRegion() {
	var copyData = "";
	var tempElemlife;
	if (checkNull(regionSelectData.elemlife)) {
		tempElemlife = "";
	} else {
		tempElemlife = parseInt(regionSelectData.elemlife);
	}
	if (regionSelectData.id.indexOf("region") > -1) {
		copyData = {
			element : new Array(),
			id : "region" + regionCount,
			width : parseInt(regionSelectData.width),
			height : parseInt(regionSelectData.height),
			top : 0,
			elemlife : tempElemlife,
			name : typeName[regionSelectData.type],
			left : 0,
			zIndex : lastZIndex,
			type : regionSelectData.type,
			isstatic:regionSelectData.isstatic
		};
		regionCount++;
		drawRegion(copyData);
		if(copyData.isstatic=="true"){
			program.staticscene[0].region.push(copyData);
		}else{
			sceneSelectData.region.push(copyData);
		}
		changeSlelectRegion(copyData.id);
		lastZIndex = lastZIndex + 2;
	} else if (regionSelectData.id.indexOf("button") > -1) {
		copyData = {
			id : "button" + buttonCount,
			width : parseInt(regionSelectData.width),
			height : parseInt(regionSelectData.height),
			zIndex : lastZIndex + 4000,
			name : regionSelectData.name,
			type : regionSelectData.type,
			sceneid : "",
			style : regionSelectData.style,
			imgstyle : regionSelectData.imgstyle,
			backimg : regionSelectData.backimg,
			value : regionSelectData.value,
			selectimg:regionSelectData.selectimg,
			fontfamily : regionSelectData.fontfamily,
			fontcolor : regionSelectData.fontcolor,
			fontsize : regionSelectData.fontsize,
			backcolor : regionSelectData.backcolor,
			left : 0,
			top : 0,
			isstatic:regionSelectData.isstatic,
			bubackimgName : regionSelectData.bubackimgName,
			buselectimgName : regionSelectData.buselectimgName
		};
		buttonCount++;
		drawRegion(copyData);
		if(copyData.isstatic=="true"){
			program.staticscene[0].button.push(copyData);
		}else{
			if(checkNull(sceneSelectData.button)){
				sceneSelectData.button = new Array();
			}
			sceneSelectData.button.push(copyData);
		}
		changeSlelectRegion(copyData.id);
		lastZIndex = lastZIndex + 2;

	} else if (regionSelectData.id.indexOf("plug") > -1) {
		var plugData = {
			id : "plug" + buttonCount,
			width : parseInt(regionSelectData.width),
			height : parseInt(regionSelectData.height),
			zIndex : lastZIndex,
			elemlife : tempElemlife,
			name : plugTypeName[regionSelectData.type],
			type : regionSelectData.type,
			left : 0,
			top : 0,
			isstatic:regionSelectData.isstatic
		};
		// 插件修改
		if (regionSelectData.type == 1) {
			var album = {
				style : 1,
				element : new Array()
			};
			var temp = new Array();
			temp.push(album);
			plugData.album = temp;
		} else if (regionSelectData.type == 8) {
			var touchvideo = {
				style : 1,
				element : new Array()
			};
			var temp = new Array();
			temp.push(touchvideo);
			plugData.touchvideo = temp;
		} else if (regionSelectData.type == 7) {
			var album = {
				style : 7,
				element : new Array()
			};
			var temp = new Array();
			temp.push(album);
			plugData.album = temp;
		} else if (regionSelectData.type == 6) {
			var tempWeather = copyjson(regionSelectData.weather)
			plugData.weather = tempWeather;
		} else if (regionSelectData.type == 5) {
			var tempTime = copyjson(regionSelectData.timepiece)
			plugData.timepiece = tempTime;
		} else if (regionSelectData.type == 9) {
			var tempcount = copyjson(regionSelectData.count)
			plugData.count = tempcount;
		}
		drawRegion(plugData);
		if(plugData.isstatic=="true"){
			program.staticscene[0].plug.push(plugData);
		}else{
			sceneSelectData.plug.push(plugData);
		}
		changeSlelectRegion("plug" + buttonCount);
		buttonCount++;
		lastZIndex = lastZIndex + 2;
	}
}
/**
 * 区域素材管理窗口(双击弹出)
 */
function elemManager() {
//	elemListData = new Array();
//	elemarr = new Array();
	$("#elemListDiv_back").html("");
	if (regionSelectData.id.indexOf("button") > -1) {
		if (regionSelectData.type == 2 || regionSelectData.type == 3) {
			$("#buttonEvent_tabs").tabs('disableTab', 0);
			$("#buttonEvent_tabs").tabs('select', 1);
			$("#backimg").html('<img src="'+regionSelectData.backimg+'" style="width:100px;">'); 
			$("#selectimg").html('<img src="'+regionSelectData.selectimg+'" style="width:100px;">'); 
			return;
		} else {
			$("#buttonEvent_tabs").tabs('enableTab', 0);
			$("#backimg").html('<img src="'+regionSelectData.backimg+'" style="width:100px;">'); 
			$("#selectimg").html('<img src="'+regionSelectData.selectimg+'" style="width:100px;">'); 
			
		}
		initButtonStyle();
	} else {
		// 插件修改
		if (regionSelectData.id.indexOf("plug") > -1
				&& (regionSelectData.type == 6 || regionSelectData.type == 5 || regionSelectData.type == 9)) {
			ZENG.msgbox.show("该类型元素暂不支持!", 3, 2000);
			return;
		}
		$("#pagInationElem").pagination({
			onSelectPage : function(pageNumber, pageSize) {
				var data = {
					page : pageNumber,
					rows : pageSize,
					elem_name : ""
				};
				if (regionSelectData.id.indexOf("region") > -1) {
					// if (parseInt(regionSelectData.type) != 0) {
					var tempType = $("#elemTypeSelect").combobox("getValue");
					if (!checkNull(tempType)) {
						data.type = tempType;
					} else {
						data.type = regionSelectData.type;
					}
					// }
				} else if (regionSelectData.id.indexOf("plug") > -1) {
					if (parseInt(regionSelectData.type) != 0) {
						var tempType = $("#elemTypeSelect")
								.combobox("getValue");
						if (!checkNull(tempType)) {
							data.type = tempType;
						} else {
							data.type = plugElemType[regionSelectData.type];
						}
					}
				}
				var tName = $("#searchNameElem").val().trim();
				tName = tName.replace("%", "/%");
				data.elem_name = tName;
				elemManagerLoad(data);
			}
		});
		var data = {
			page : 1,
			rows : 10,
			elem_name : tName
		};
		if (regionSelectData.id.indexOf("region") > -1) {
			if (parseInt(regionSelectData.type) != 0) {
				data.type = regionSelectData.type;
			}
		} else if (regionSelectData.id.indexOf("plug") > -1) {
			if (parseInt(regionSelectData.type) != 0) {
				data.type = plugElemType[regionSelectData.type];
			}
		}
		var temptype;
		if (checkNull(data.type)) {
			temptype = "";
		} else {
			temptype = data.type;
		}
		$.ajax({
					url : 'element/queryClassify.do',
					type : 'post',
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					data : {
						"parentId" : temptype
					},
					success : function(data) {
						var tempdata = new Array();
						// if (data.rows.length > 0) {
						// temptype = data.rows[0].parentId;
						// }
						if (temptype != "") {
							tempdata.push({
										type_id : temptype,
										type_name : "全部",
										"selected" : true
									});
						} else {
							initType[2].selected = true;
						}
						if (checkNull(temptype)) {
							if(regionSelectData.id == "screenregion"){
								tempdata.push(initType[2]);
								tempdata.push(initType[3]);
							}else{
								for (var i = 0; i < initType.length; i++) {
									tempdata.push(initType[i]);
								}
							}
						}
						if(regionSelectData.id == "screenregion"){
							for (var i = 0; i < data.rows.length; i++) {
								if(data.rows[i].parentId == 3 || data.rows[i].parentId == 4){
									tempdata.push(data.rows[i]);
								}
							}
						}else{
							for (var i = 0; i < data.rows.length; i++) {
								tempdata.push(data.rows[i]);
							}
						}
						// var temptype = regionSelectData.type;
						// if (checkNull(temptype)) {
						// temptype = "";
						// }
						if(regionSelectData.type==1&&regionSelectData.name=="文本"){
							$("#reso").css('display','block');
						$("#resolutionSize").combobox({
							data:[{id:"1",text:"固定大小"},{id:"2",text:"自识别"}],
							valueField:'id', 
							textField:'text' 
						});
						if(regionSelectData.resolutionSize == 1 || regionSelectData.resolutionSize == "1")
						$('#resolutionSize').combobox('setValue', '1');
						else
						$('#resolutionSize').combobox('setValue', '2');
						}else{
							$("#reso").css('display','none');
						}
						
						$("#elemTypeSelect").combobox({
							valueField : 'type_id',
							textField : 'type_name',
							panelHeight : '80px',
							editable : false,
							data : tempdata,
							onChange : function(newValue, oldValue) {
								var tempType = $("#elemTypeSelect")
										.combobox("getValue");
								var tName = $("#searchNameElem").val().trim();
								tName = tName.replace("%", "/%");
								var data = {
									page : 1,
									rows : 10,
									elem_name : tName
								};
								if (!checkNull(tempType)) {
									data.type = tempType;
								}
								elemManagerLoad(data);
								$('#pagInationElem').pagination('select', 1);
							}
						});
					}
				});

		$("#regionManagerWin").dialog('open');
		$("#elemListDiv").html("");
		drawRegionElem();
		var tName = $("#searchNameElem").val().trim();
		tName = tName.replace("%", "/%");
		if (data.type == undefined) {
			data.type = 3;
		}
		elemManagerLoad(data);
		$('#pagInationElem').pagination('select', 1);
	}
}
// 有素材的
function addElemToRegion() {
	var elem = $(".elemdivSelect");
//	var elem = elemarr;
//	var thiselem = $(".elemdivSelect");
//	for (var i = 0; i < thiselem.length; i++) {
//		var ishave = false;
//		for (var j = 0; j < elemarr.length; j++) {
//			if(thiselem[i].id == elemarr[j].id){
//				ishave = true;
//				break;
//			}
//		}
//		if(!ishave){
//			elemarr.push(thiselem[i]);
//		}
//	}
	// 如果区域为插件11（zip后缀）或unity3d(unity3d后缀)类型则只能加一个素材并且类型正确
	if (regionSelectData.id.indexOf("plug") > -1
			&& (parseInt(regionSelectData.type) == 10 || parseInt(regionSelectData.type) == 11)) {
		if(elem.length!=1||(!checkNull(regionSelectData.element)&&regionSelectData.element.length!=0)){
			ZENG.msgbox.show(plugTypeName[regionSelectData.type]+"组件只能加一个素材", 3, 1200);
			return;
		}
		var elemDataTemp = copyjson(getElemDataById(elem[0].id));
		if(parseInt(regionSelectData.type) == 10&&elemDataTemp.name.indexOf("\.unity3d")==-1){
			ZENG.msgbox.show(plugTypeName[regionSelectData.type]+"组件只能加unity3d类型素材", 3, 1200);
			return;
		}
		if(parseInt(regionSelectData.type) == 11&&elemDataTemp.name.indexOf("\.zip")==-1){
			ZENG.msgbox.show(plugTypeName[regionSelectData.type]+"组件只能加zip类型素材", 3, 1200);
			return;
		}
	}
	if((program.istouch=="true" && program.istouch) && regionSelectData.type == 5 ){
		var r_elem = $(".r_elemdiv");
		if((elem.length + r_elem.length) > 1){
			ZENG.msgbox.show("互动节目只能添加一个网页", 3, 1200);
			return;
		}
	}
//	if((program.istouch=="true" && program.istouch) && regionSelectData.type == 6 ){
//		var r_elem = $(".r_elemdiv");
//		if((elem.length + r_elem.length) > 1){
//			ZENG.msgbox.show("互动节目只能添加一个flash", 3, 1200);
//			return;
//		}
//	}
	if((program.istouch=="true" && program.istouch) && regionSelectData.type == 8 ){
		var r_elem = $(".r_elemdiv");
		if((elem.length + r_elem.length) > 1){
			ZENG.msgbox.show("流媒体只能添加一个片源", 3, 1200);
			return;
		}
	}
	for (var i = 0; i < elem.length; i++) {
		var elemDataTemp = copyjson(getElemDataById(elem[i].id));
		var resolution1 = [];
		var resolutionSize = "";
			if(elemDataTemp.type=="1")resolutionSize = $("#resolutionSize").combobox('getValue');
			regionSelectData.resolutionSize = resolutionSize;
		if((elemDataTemp.type == 1 || elemDataTemp.type == '1') && (resolutionSize=="1" || resolutionSize == 1)){
			if(elem.length>1 || $('#regionElemDiv>li').length>=1){
				ZENG.msgbox.show("选择固定大小不可添加多个!", 3, 2000);
				return;
			}
			resolution1 = elemDataTemp.resolution.split("x");
			width = resolution1[0];
			height = resolution1[1];
			regionSelectData.width = parseInt(width);
			regionSelectData.height = parseInt(height);
			regionSelectData.resolutionSize = '1';
			if(parseInt(width) > parseInt(resolutionX) || parseInt(height) > parseInt(resolutionY)){
				ZENG.msgbox.show("文件类型宽高值过大!", 3, 2000);
				regionSelectData.width = $("#regionAttrW").val();
				regionSelectData.height = $("#regionAttrH").val();
				return;
			}
			if(parseInt(regionSelectData.left)+parseInt(width)>parseInt(resolutionX)){
				regionSelectData.left=regionSelectData.left - (parseInt(regionSelectData.left)+parseInt(width)-parseInt(resolutionX))-10;
				$("#regionAttrX").val(regionSelectData.left);
			}
			if(parseInt(regionSelectData).top+parseInt(height)>parseInt(resolutionY)){
				regionSelectData.top=regionSelectData.top - (parseInt(regionSelectData.top)+parseInt(height)-parseInt(resolutionY))-10;
				$("#regionAttrX").val(regionSelectData.top);
			}
		}else
			drafting(regionSelectData);
		if(elemDataTemp.type == 7){
			elemDataTemp.life = elemDataTemp.life;
		}
		var tempName = elemDataTemp.name;
		elemDataTemp.elemTempId = "selected_elem_" + elemCount;
		if (!checkNull(regionSelectData.elemlife)) {
			elemDataTemp.life = regionSelectData.elemlife;
		}
		elemCount++;
		if (regionSelectData.id.indexOf("region") > -1) {
			if (checkNull(regionSelectData.element)) {
				regionSelectData.element = new Array();
			}
			regionSelectData.element.push(elemDataTemp);
		} else if (regionSelectData.id.indexOf("plug") > -1) {
			// 插件修改
			if (parseInt(regionSelectData.type) == 1) {
				if (checkNull(regionSelectData.album[0].element)) {
					regionSelectData.album[0].element = new Array();
				}
				regionSelectData.album[0].element.push(elemDataTemp);
			} else if (parseInt(regionSelectData.type) == 8) {
				if (checkNull(regionSelectData.touchvideo[0].element)) {
					regionSelectData.touchvideo[0].element = new Array();
				}
				regionSelectData.touchvideo[0].element.push(elemDataTemp);
			} else if (parseInt(regionSelectData.type) == 7) {
				if (checkNull(regionSelectData.album[0].element)) {
					regionSelectData.album[0].element = new Array();
				}
				regionSelectData.album[0].element.push(elemDataTemp);
			} else if (parseInt(regionSelectData.type) == 10
					|| parseInt(regionSelectData.type) == 11) {
				if (checkNull(regionSelectData.element)) {
					regionSelectData.element = new Array();
				}
				regionSelectData.element.push(elemDataTemp);
			}

		}
		
		var showName = tempName;
		if (showName.length > 12) {
			showName = showName.substring(0, 9) + "..."
					+ showName.substring(showName.length - 3);
		}
		if (showName.length > 6) {
			showName = showName.substring(0, 6) + "<br>"
					+ showName.substring(6);
		}
		var tempUrl = elemDataTemp.thumbnailUrl;
		if (elemDataTemp.type != 1 && elemDataTemp.type != 2
				&& elemDataTemp.type != 4 && elemDataTemp.type != 5
				&& elemDataTemp.type != 6) {
			tempUrl = tempUrl.substring(0, 18) + "mini_"
					+ tempUrl.substring(18);
		}
		$("#regionElemDiv")
				.append('<li oncontextmenu="setElemAttr(\''
								+ elemDataTemp.elemTempId
								+ '\');return false;" onclick="selectHadElem(\''
								+ elemDataTemp.elemTempId
								+ '\')" id="'
								+ elemDataTemp.elemTempId
								+ '" class="r_elemdiv r_elemdivUnSelect easyui-tooltip"title="'
								+ tempName
								+ '" style="margin-left:1%;display:block;">' 
									+'<div style="float:left; display: inline;text-align:center;"><img   width="'+elemDataTemp.eWidth+'" height="'+elemDataTemp.eHeight+'" style="margin:5 '+(60-elemDataTemp.eWidth)/2+';"  src="'
									+ tempUrl
									+ '"  >' 
									+'</div>'
									+	'<div style="float:left;width:88px;height:60px;margin-top:5px;  display: inline ;position: relative;text-align:left;">'
											+showName
											+'<br><br>'+elemDataTemp.resolution
								    +'</div>'
							+'</li>');
		showRegionImg(regionSelectData);
	}
	$('#regionElemDiv').sortable().bind('sortupdate', reSort);
	// 如果是普通节目则计算区域时长
	if (program.istouch == "false" || !program.istouch) {
		flashRegionlife(regionSelectData.element);
		flashScenelife(sceneSelectData.region);
	}
}
function flashRegionlife(elems) {
	var regionlife = 0;
	for (var index = 0; index < elems.length; index++) {
		if(regionSelectData.type == 7){
			regionlife += parseInt(elems[index].partlife == 
				undefined ? elems[index].originlife : elems[index].partlife) * parseInt(elems[index].src.split("_")[0].
				split("\\")[2]);
		}else if(regionSelectData.type == 0 && elems[index].type == 7){
			regionlife += parseInt(elems[index].partlife == 
				undefined ? elems[index].originlife : elems[index].partlife) * parseInt(elems[index].src.split("_")[0].
				split("\\")[2]);
		}else{
			regionlife += parseInt(elems[index].life);
		}
	}
	regionSelectData.life = regionlife;
}
function flashScenelife(regions) {
	var scenelife = 0;
	for (var index = 0; index < regions.length; index++) {
		scenelife = parseInt(regions[index].life) > scenelife
				? parseInt(regions[index].life)
				: scenelife;
	}
	sceneSelectData.life = scenelife;
}
// 增加有素材的插件需修改
function drawRegionElem() {
	$("#regionElemDiv").html("");
	var elemData = new Array();
	if (regionSelectData.id.indexOf("region") > -1) {
		elemData = regionSelectData.element;
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		// 插件修改
		if (parseInt(regionSelectData.type) == 1) {
			elemData = regionSelectData.album[0].element;
		} else if (parseInt(regionSelectData.type) == 8) {
			elemData = regionSelectData.touchvideo[0].element;
		} else if (parseInt(regionSelectData.type) == 7) {
			elemData = regionSelectData.album[0].element;
		}else if (parseInt(regionSelectData.type) == 10||parseInt(regionSelectData.type) == 11) {
			elemData = regionSelectData.element;
		}
	}
	if (elemData == undefined || elemData.length < 1)
		return;
	for (var i = 0; i < elemData.length; i++) {
		var elemDataTemp = elemData[i];
		var tempName = elemDataTemp.name;
		var showName = tempName;
		if (showName.length > 12) {
			showName = showName.substring(0, 9) + "..."
					+ showName.substring(showName.length - 3);
		}
		if (showName.length > 6) {
			showName = showName.substring(0, 6) + "<br>"
					+ showName.substring(6);
		}
		var tempUrl = elemDataTemp.thumbnailUrl;
		if(elemDataTemp.type!=1&&elemDataTemp.type!=2&&elemDataTemp.type!=4&&elemDataTemp.type!=5&&elemDataTemp.type!=6){
			tempUrl = tempUrl.substring(0,18)+"mini_"+tempUrl.substring(18);
		}
		$("#regionElemDiv")
				.append('<li oncontextmenu="setElemAttr(\''
								+ elemDataTemp.elemTempId
								+ '\');return false;" onclick="selectHadElem(\''
								+ elemDataTemp.elemTempId
								+ '\')" id="'
								+ elemDataTemp.elemTempId
								+ '" class="r_elemdiv r_elemdivUnSelect easyui-tooltip" title="'
								+ tempName
								+ '" style="margin-left:1%;">' 
									+'<div style="float:left;"><img  width="'+elemDataTemp.eWidth+'" height="'+elemDataTemp.eHeight+'" style="margin:5 '+(60-elemDataTemp.eWidth)/2+';" src="'
									+ tempUrl
									+ '"  >' 
									+'</div>'
										+'<div style="float:left;width:80px;height:60px;margin-top:5px;  display: inline ;position: relative;text-align:left;">'
											+showName
											+'<br><br>'+elemDataTemp.resolution
								        +'</div>'
							+'</li>');
	}
	$('#regionElemDiv').sortable().bind('sortupdate', reSort);
	// $("#regionElemDiv").sortable();
	// $('#regionElemDiv').sortable().bind('sortupdate', reSort);
}
// 素材重排序增加插件时需修改
function reSort() {
	var elemData = new Array();
	var temp;
	// 插件修改
	if (parseInt(regionSelectData.type) == 8) {
		temp = regionSelectData.touchvideo[0].element;
	} else if (parseInt(regionSelectData.type) == 7) {
		temp = regionSelectData.album[0].element;
	} else if (parseInt(regionSelectData.type) == 1) {
		temp = regionSelectData.element;
	} else {
		temp = regionSelectData.element;
	}
	var elems = $(".r_elemdiv");
	for (var i = 0; i < elems.length; i++) {
		var tempId = elems[i].id;
		for (var j = 0; j < temp.length; j++) {
			if (tempId == temp[j].elemTempId) {
				elemData.push(temp[j]);
				break;
			}
		}
	};
	if (parseInt(regionSelectData.type) == 8) {
		regionSelectData.touchvideo[0].element = elemData;
	} else if (parseInt(regionSelectData.type) == 7) {
		regionSelectData.album[0].element = elemData;
	} else if (parseInt(regionSelectData.type) == 1) {
		regionSelectData.element = elemData;
	} else {
		regionSelectData.element = elemData;
	}
}
// 增加插件时需修改
function deleElemByTempId(tempId) {
	var tempArray = new Array();
	if (regionSelectData.id.indexOf("region") > -1) {
		for (var i = 0; i < regionSelectData.element.length; i++) {
			if (tempId != regionSelectData.element[i].elemTempId)
				tempArray.push(regionSelectData.element[i]);
		}
		regionSelectData.element = tempArray;
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		// 插件修改
		if (parseInt(regionSelectData.type) == 1) {
			for (var i = 0; i < regionSelectData.album[0].element.length; i++) {
				if (tempId != regionSelectData.album[0].element[i].elemTempId)
					tempArray.push(regionSelectData.album[0].element[i]);
			}
			regionSelectData.album[0].element = tempArray;
		} else if (parseInt(regionSelectData.type) == 7) {
			for (var i = 0; i < regionSelectData.album[0].element.length; i++) {
				if (tempId != regionSelectData.album[0].element[i].elemTempId)
					tempArray.push(regionSelectData.album[0].element[i]);
			}
			regionSelectData.album[0].element = tempArray;
		} else if (parseInt(regionSelectData.type) == 8) {
			for (var i = 0; i < regionSelectData.touchvideo[0].element.length; i++) {
				if (tempId != regionSelectData.touchvideo[0].element[i].elemTempId)
					tempArray.push(regionSelectData.touchvideo[0].element[i]);
			}
			regionSelectData.touchvideo[0].element = tempArray;
		}else{
			for (var i = 0; i < regionSelectData.element.length; i++) {
			if (tempId != regionSelectData.element[i].elemTempId)
				tempArray.push(regionSelectData.element[i]);
		}
		regionSelectData.element = tempArray;
		}
	}
}

// 增加插件需修改
function newPlug(type) {
	if (checkNull(sceneSelectData)) {
		ZENG.msgbox.show("请先选中一个场景!", 3, 800);
		return;
	}
	var plugWidth = parseInt(resolutionX / 2);
	var plugHeight = parseInt(resolutionY / 2);
	var plugData = {
		id : "plug" + buttonCount,
		width : plugWidth,
		height : plugHeight,
		elemlife : "",
		zIndex : lastZIndex + 3000,
		name : plugTypeName[type],
		type : type,
		left : 0,
		top : 0
	};
	// 插件修改
	if (type == 1) {
		var album = {
			style : 1,
			element : new Array()
		};
		var temp = new Array();
		temp.push(album);
		plugData.album = temp;
	} else if (type == 6) {
		plugData.weather = new Array();

	} else if (type == 5) {
		plugData.timepiece = new Array();
	} else if (type == 7) {
		var album = {
			style : 7,
			element : new Array()
		};
		var temp = new Array();
		temp.push(album);
		plugData.album = temp;
	} else if (type == 8) {
		var touchvideo = {
			style : 1,
			element : new Array()
		};
		var temp = new Array();
		temp.push(touchvideo);
		plugData.touchvideo = temp;
	} else if (type == 9) {
		plugData.count = new Array();
	}
	drawRegion(plugData);
	if (sceneSelectData.plug == null || sceneSelectData.plug.length == 0) {
		sceneSelectData.plug = new Array();
	}
	sceneSelectData.plug.push(plugData);
	changeSlelectRegion("plug" + buttonCount);
	buttonCount++;
	lastZIndex = lastZIndex + 2;
}
// 增加插件需修改
/**
 * 弹出区域属性设置窗口（一般插件有属性）
 */
function showRegionAttrWin() {
	// 插件修改
	if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 1) {
		var temp = regionSelectData.album[0].style;
		if (!checkNull(temp)) {
			$("#albumStyleType").val(temp);
		}
		$("#regionAttrSetWin").dialog('open');
		showAlbumImg();
	} else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 7) {
		ZENG.msgbox.show("暂不支持互动office属性设置!", 3, 2000);
	} else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 8) {
		ZENG.msgbox.show("暂不支持互动视频属性设置!", 3, 2000);
	}else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 10) {
		ZENG.msgbox.show("暂不支持unity3d属性设置!", 3, 2000);
	}else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 11) {
		ZENG.msgbox.show("暂不支持插件属性设置!", 3, 2000);
	} else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 6) {
		$("#weatherAttrSetWin").dialog('open');
		queryWeatherList();
	} else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 5) {
		$("#timeAttrSetWin").dialog('open');
		queryTimeList();
	} else if (regionSelectData.id.indexOf("plug") > -1
			&& parseInt(regionSelectData.type) == 9) {
		$("#countAttrSetWin").dialog('open');
		if (!checkNull(regionSelectData.count)
				&& !checkNull(regionSelectData.count[0].text)) {
			$("#countAttr_text").textbox("setValue",
					regionSelectData.count[0].text);
			var setendtime = regionSelectData.count[0].endtime.replace("/","-")
																	.replace("/","-");
			$("#countAttr_time").datetimebox("setValue",setendtime);
		}else{
			countList = null;
		}
		queryCountList();
	} else if (parseInt(regionSelectData.type) == 5) {
		$("#htmlattrWin").dialog('open');
		$("#showtoolbarbox")
				.attr(
						"checked",
						(regionSelectData.showtoolbar == "true" || regionSelectData.showtoolbar == true)
								? true
								: false);
	}
}
/**
 * 初始化复制场景插件类数据 增加插件需修改
 * 
 * @param data
 */

function initPlugData(plugData) {
	var newData = {
		id : "plug" + buttonCount,
		top : plugData.top,
		left : plugData.left,
		width : plugData.width,
		height : plugData.height,
		zIndex : lastZIndex,
		name : plugTypeName[plugData.type],
		type : plugData.type
	};
	// 插件修改
	// 如果是互动相册
	if (plugData.type == 1) {
		var album = {
			style : plugData.album[0].style,
			element : new Array()
		};
		var temp = new Array();
		temp.push(album);
		newData.album = temp;
	} else if (plugData.type == 6) {// 天气预报
		newData.weather = copyjson(plugData.weather);
	} else if (plugData.type == 5) {// 时钟
		newData.timepiece = copyjson(plugData.timepiece);
	} else if (plugData.type == 7) {// 互动office
		var album = {
			style : 7,
			element : new Array()
		};
		var temp = new Array();
		temp.push(album);
		newData.album = temp;
	} else if (plugData.type == 8) {// 互动视频
		var touchvideo = {
			style : 7,
			element : new Array()
		};
		var temp = new Array();
		temp.push(touchvideo);
		newData.touchvideo = temp;
	} else if (plugData.type == 9) {// 倒计时
		newData.count = copyjson(plugData.count);
	}
	buttonCount++;
	lastZIndex++;
	return newData;
}
function subElemAttrSet() {
	var elemTime = $("#elemAttrSet").numberbox("getValue");// 如果是图片区域设置则获取特效设置
	if (regionSelectData.type == 3) {
		regionSelectData.piceffect = $("#piceffect").combo("getValue");
		if(regionSelectData.piceffect != 0 && elemTime < 2 && !checkNull(elemTime)){
			$("#elemAttrSet").numberbox("setValue",2);
			elemTime = $("#elemAttrSet").numberbox("getValue",2);
		}
	}else if(regionSelectData.type == 7){
		if(elemTime < 3){
			ZENG.msgbox.show("设置失败,每张素材最短播放3秒", 3, 2000);
			return;
		}
	}
	// 如果时间设置不为空则更新区域素材时长属性
	if (!checkNull(elemTime)) {
		if (regionSelectData.id.indexOf("region") > -1) {
			for (var i = 0; i < regionSelectData.element.length; i++) {
				if(regionSelectData.element[i].originlife == null)
					regionSelectData.element[i].originlife = regionSelectData.element[i].partlife == undefined ? 3 : regionSelectData.element[i].partlife;
				regionSelectData.element[i].partlife = elemTime;
				regionSelectData.element[i].life = elemTime;
			}
		} else if (regionSelectData.id.indexOf("plug") > -1) {
			// 插件修改
			if (parseInt(regionSelectData.type) == 1) {
				for (var i = 0; i < regionSelectData.album[0].element.length; i++) {
					if(regionSelectData.album[0].element[i].originlife == null)
						regionSelectData.album[0].element[i].originlife = 
										regionSelectData.album[0].element[i].life;
					regionSelectData.album[0].element[i].life = elemTime;
				}
			} else if (parseInt(regionSelectData.type) == 7) {
				for (var i = 0; i < regionSelectData.album[0].element.length; i++) {
					if(regionSelectData.album[0].element[i].originlife == null)
						regionSelectData.album[0].element[i].originlife =
										regionSelectData.album[0].element[i].life;
					regionSelectData.album[0].element[i].life = elemTime;
				}
			} else if (parseInt(regionSelectData.type) == 8) {
				for (var i = 0; i < regionSelectData.touchvideo[0].element.length; i++) {
					if(regionSelectData.touchvideo[0].element[i].originlife == null)
						regionSelectData.touchvideo[0].element[i].originlife =
										regionSelectData.touchvideo[0].element[i].life;
					regionSelectData.touchvideo[0].element[i].life = elemTime;
				}
			}else{
				for (var i = 0; i < regionSelectData.element.length; i++) {
					if(regionSelectData.element[i].originlife == null)
						regionSelectData.element[i].originlife =
										regionSelectData.element[i].life;
					regionSelectData.element[i].life = elemTime;
				}
			}
		}
		regionSelectData.elemlife = elemTime;
		// 如果是普通节目则计算区域时长
		if (program.istouch == "false" || !program.istouch) {
			flashRegionlife(regionSelectData.element);
			flashScenelife(sceneSelectData.region);
		}
	}else{
		regionSelectData.elemlife = "";
		if (regionSelectData.id.indexOf("region") > -1) {
			for (var i = 0; i < regionSelectData.element.length; i++) {
				if(regionSelectData.element[i].originlife == null){
//					regionSelectData.element[i].originlife = regionSelectData.element[i].life;
//					regionSelectData.element[i].life = "";
				}else{
					regionSelectData.element[i].life = regionSelectData.element[i].originlife;
				}
			}
		} else if (regionSelectData.id.indexOf("plug") > -1) {
			// 插件修改
			if (parseInt(regionSelectData.type) == 1) {
				for (var i = 0; i < regionSelectData.album[0].element.length; i++) {
					if(regionSelectData.album[0].element[i].originlife == null){
//						regionSelectData.element[i].originlife = 
//									regionSelectData.album[0].element[i].life;
//						regionSelectData.album[0].element[i].life = "";
					}else{
						regionSelectData.album[0].element[i].life = 
							regionSelectData.album[0].element[i].originlife;
					}
				}
			} else if (parseInt(regionSelectData.type) == 7) {
				for (var i = 0; i < regionSelectData.album[0].element.length; i++) {
					if(regionSelectData.album[0].element[i].originlife == null){
//						regionSelectData.element[i].originlife =
//								regionSelectData.album[0].element[i].life;
//						regionSelectData.album[0].element[i].life = "";
					}else{
						regionSelectData.album[0].element[i].life = 
							regionSelectData.album[0].element[i].originlife;
					}
				}
			} else if (parseInt(regionSelectData.type) == 8) {
				for (var i = 0; i < regionSelectData.touchvideo[0].element.length; i++) {
					if(regionSelectData.touchvideo[0].element[i].originlife == null){
//						regionSelectData.element[i].originlife =
//								regionSelectData.touchvideo[0].element[i].life;
//						regionSelectData.touchvideo[0].element[i].life = "";
					}else{
						regionSelectData.touchvideo[0].element[i].life = 
							regionSelectData.touchvideo[0].element[i].originlife;
					}
				}
			}else{
				for (var i = 0; i < regionSelectData.element.length; i++) {
					if(regionSelectData.element[i].originlife == null){
//						regionSelectData.element[i].originlife =
//										regionSelectData.element[i].life;
//						regionSelectData.element[i].life = "";
					}else{
						regionSelectData.element[i].life = 
							regionSelectData.element[i].originlife;
					}
				}
			}
		}
		regionSelectData.elemlife = elemTime;
		// 如果是普通节目则计算区域时长
		if (program.istouch == "false" || !program.istouch) {
			flashRegionlife(regionSelectData.element);
			flashScenelife(sceneSelectData.region);
		}
	}
	ZENG.msgbox.show("设置成功", 4, 800);
	$("#elemPlayAttrWin").dialog("close");
}
function setElemAttr(elem_id) {
	var elemData = new Array();
	if (regionSelectData.id.indexOf("region") > -1) {
		elemData = regionSelectData.element;
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		// 插件修改
		if (parseInt(regionSelectData.type) == 1) {
			elemData = regionSelectData.album[0].element;
		} else if (parseInt(regionSelectData.type) == 7) {
			elemData = regionSelectData.album[0].element;
		} else if (parseInt(regionSelectData.type) == 8) {
			elemData = regionSelectData.touchvideo[0].element;
		}else{
			elemData = regionSelectData.element;
		}
	}
	if (elemData == undefined || elemData.length < 1)
		return;
	for (var i = 0; i < elemData.length; i++) {
		if (elem_id == elemData[i].elemTempId) {
			$("#oneelemPlayAttrWin").dialog("open");
			$("#elemAttrElemName").val(elemData[i].name);
			$("#oneelemAttrSet").numberbox("setValue", elemData[i].life);
			if(elemData[i].type == 7){
				$("#oneelemAttrSet").numberbox("setValue", elemData[i].partlife == undefined ? 3 : elemData[i].partlife);
			}
			$("#setelemId").val(elem_id);
		}
	}
}
function subOneElemAttrSet() {
	var elemTime = $("#oneelemAttrSet").numberbox("getValue");
	if (checkNull(elemTime)) {
		ZENG.msgbox.show("素材时长不能为空", 3, 1000);
		return;
	}
	var elem_id = $("#setelemId").val();
	var elemData = new Array();
	if (regionSelectData.id.indexOf("region") > -1) {
		elemData = regionSelectData.element;
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		// 插件修改
		if (parseInt(regionSelectData.type) == 1) {
			elemData = regionSelectData.album[0].element;
		} else if (parseInt(regionSelectData.type) == 7) {
			elemData = regionSelectData.album[0].element;
		} else if (parseInt(regionSelectData.type) == 8) {
			elemData = regionSelectData.touchvideo[0].element;
		}else{
			elemData = regionSelectData.element;
		}
	}
	if (elemData == undefined || elemData.length < 1)
		return;
	for (var i = 0; i < elemData.length; i++) {
		if (elem_id == elemData[i].elemTempId) {
			if(elemData[i].type == 7 && ((elemData[i].originlife == null && parseInt(elemTime) < elemData[i].life)
				||(parseInt(elemTime) < elemData[i].originlife))){
				var	st = elemData[i].originlife == undefined ? elemData[i].life:elemData[i].originlife;
				ZENG.msgbox.show("设置失败,每张素材最短播放3秒", 3, 1500);
				return;
			}else if(elemData[i].type == 3 && elemTime < 3){
				ZENG.msgbox.show("设置失败,图片素材最短播放3秒", 3, 1500);
				return;
			}
			elemData[i].partlife = elemTime;
			elemData[i].life = elemTime;
			ZENG.msgbox.show("设置成功", 4, 1000);
		}
	}
	$("#oneelemPlayAttrWin").dialog("close");
	// 如果是普通节目则计算区域时长
	if (program.istouch == "false" || !program.istouch) {
		flashRegionlife(regionSelectData.element);
		flashScenelife(sceneSelectData.region);
	}
}
// 更新天气插件大小百分比
function updatePercent(plugid) {
	var rwidth = parseInt(regionSelectData.width);
	var wwidth = parseInt(regionSelectData.weather[0].width);
	var percent = parseInt((rwidth * 100) / wwidth)
	for (var i = 0; i < regionSelectData.weather.length; i++) {
		if(regionSelectData.id == plugid){
			regionSelectData.weather[i].percent = percent;
			break;
		}
	}
}