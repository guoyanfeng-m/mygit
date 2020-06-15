/**
 * 添加节目时候初始选择自定义分辨率弹框
 */
function zdyResolution(){
	$('#initnewResolutionWin').dialog('open');
}
/**
 * 提交分辨率设置
 */
function subinitNewResolution(){
	var w = $('#initnewResolution_width').val();
	var h = $('#initnewResolution_height').val();
	if(w==""){
		ZENG.msgbox.show("分辨率宽度不能为空!", 3, 1000);
		return;
	}
	if(h==""){
		ZENG.msgbox.show("分辨率高度不能为空!", 3, 1000);
		return;
	}
	var val = w+"*"+h;
	 $("#proxy").combobox('setValue', val);

	 $('#initnewResolutionWin').dialog('close');
}
/**
 * 新建节目时初始化节目属性（是否触摸、节目分辨率等）
 */
function initProDef(){
	var proxy = $("#proxy").combo("getText");
	if(proxy==""){
		ZENG.msgbox.show("分辨率不能为空!", 3, 1000);
		return;
	}
	if(proxy.split("*").length!=2){
		ZENG.msgbox.show("分辨率宽高请用*隔开!", 3, 1000);
		return;
	}
	var prox = proxy.split("*")[0];
	var proy = proxy.split("*")[1];
	if(isNaN(prox)||isNaN(proy)){
		ZENG.msgbox.show("请正确填写分辨率!", 3, 1000);
		return;
	}
	if(parseInt(prox)>100000||parseInt(proy)>100000){
		ZENG.msgbox.show("分辨率不能超过100000!", 3, 1000);
		return;
	}
	if(parseInt(prox)<200||parseInt(proy)<200){
		ZENG.msgbox.show("分辨率不能小于200!", 3, 1000);
		return;
	}
	var protypes = document.getElementsByName("protype"); 
	for (var i = 0; i < protypes.length;i++) {
		if(protypes[i].checked){
			program.istouch = protypes[i].value;
			//当节目单类型为普通节目时隐藏互动元素
			if(program.istouch=="false"||!program.istouch){
				$("#touchDiv").hide();
				//$("#screensaverdiv").hide();
				$("#screenbutton").hide();
				$("#regionAttr_static").addClass('menu-item-disabled');
				document.getElementById("regionAttr_static").onclick = "";
			}else{
				$("#scenelifediv").hide();
			}
		}
	}
	initScene();
	setResolutionText(parseInt(prox), parseInt(proy));
	closeWin('prodefsetwin');
}
/**
 * 初始化主界面
 */
function initAll() {
	$("#istiming").bind('change',function(){
		if($('#istiming').is(':checked')) {
			$("#download_startTime").timespinner({'disabled':false,'value':'00:00:00'});
			$("#download_endTime").timespinner({'disabled':false,'value':'23:59:59'});
		}else{
			$("#download_startTime").timespinner({'disabled':true,'value':''});
			$("#download_endTime").timespinner({'disabled':true,'value':''});
		}
	});
	auditButtonShow();
	initRegionGroup();
	keyListen();
	requestPulse();
	//新建
	if (checkNull(program.id)) {
		$("#prodefsetwin").dialog('open');
	} else {//编辑
		$.ajax({
			url : 'program/getProgram.do',
			type : 'post',
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			data : {
				"program_id" : program.id
			},
			success : function(data) {
				var pro = data.programMap.twinflag.program[0];
				setResolutionText(pro.width, pro.height);
				$("#sceneBack").html("");
				initVar();
				if(pro.istouch=="false"||!pro.istouch){
					$("#regionAttr_static").addClass('menu-item-disabled');
					document.getElementById("regionAttr_static").onclick = "";
					$("#touchDiv").hide();
				}
				resolution = $('#resolution').val();
				resolutionX = parseInt(resolution.split("*")[0]);
				resolutionY = parseInt(resolution.split("*")[1]);
				regionCount = parseInt(pro.regionCount);
				buttonCount = parseInt(pro.buttonCount);
				lastZIndex = parseInt(pro.lastZIndex);
				sceneNum = parseInt(pro.sceneNum);
				firstZIndex = parseInt(pro.firstZIndex);
				elemCount = parseInt(pro.elemCount);
				initScene();
				pro.editProp = program.editProp;
				program = pro;
				initProgram();
				isedit = program.id;
			}
		});
	}
}
//保持浏览器session不失效 30秒请求一下数据库
function requestPulse() {
	window.setInterval('conectRequest()', 300000);
}
//保持浏览器session不失效的ajax请求
function conectRequest() {
	$.ajax({
		url : 'program/conectRequest.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8"
	});
}
/**
 * 显示新增场景窗口
 */
function showAddScene() {
	var sceneTempName = "场景" + sceneNum;
	$("#newSceneName").val(sceneTempName);
	$("#newSceneWin").dialog('open');
}
/**
 * 选中场景
 * 
 * @param sceneId
 */
function sceneSelect(sceneId) {
//	var a = document.getElementById("pingbaoanniu");
//	a.style.display = "";
//	var b = document.getElementById("pingbaoanniu1");
//	b.style.display = "none";
	if (sceneSelectData != undefined) {
		$("#" + sceneSelectData.id).removeClass("ui-selectee ui-selected");
		$("#screenid").removeClass("ui-selectee ui-selected");
	}
	sceneSelectData = getSceneById(sceneId);
	regionSelectData = undefined;
	showRegionAttr();
	flashScene(sceneId);
	$("#" + sceneId).addClass("ui-selectee ui-selected");
	if(sceneId != "screensaver"){
		$("#radioDiv").show();
		$("#templateDiv").show();
		if(program.istouch == "true")
			$("#touchDiv").show();
	}
}
/**
 * 根据场景id查询场景数据
 * 
 * @param sceneId
 * @returns
 */
function getSceneById(sceneId) {
	for (var i = 0; i < program.scene.length; i++) {
		if (sceneId == program.scene[i].id) {
			return program.scene[i];
		}
	}
	return null;
}
/**
 * 根据场景名称查询场景数据
 * 
 * @param sceneId
 * @returns
 */
function getSceneByName(sceneName) {
	for (var i = 0; i < program.scene.length; i++) {
		if (sceneName == program.scene[i].sceneName) {
			return program.scene[i];
		}
	}
	return null;
}
/**
 * 根据场景ID删除场景
 * 
 * @param sceneId
 */
function removeSceneById(sceneId) {
	var tempArray = new Array();
	for (var i = 0; i < program.scene.length; i++) {
		if (sceneId != program.scene[i].id) {
			tempArray.push(program.scene[i]);
		}
	}
	program.scene = tempArray;
}
/**
 * 刷新场景画布
 * 
 * @param sceneId
 */
function flashScene(sceneId) {
	var sceneBack = $('#sceneBack');
	var backimg = sceneSelectData.backimg;
	if (checkNull(backimg)) {
		backimg = "img/u106.png";
	}
	sceneBack.html('<img id="sceneBackImg" src="' + backimg
			+ '" style=" height:100%; width:100%;"align="middle" />');
	flashRegion(sceneId);
	if(sceneId != "screensaver")
		flashstaticRegion();
}

/**
 * 刷新并绘制指定场景的区域
 * 
 * @param sceneId
 */
function flashRegion(sceneId) {
	//重绘区域
	if(sceneId == "screensaver"){
		$("#screenid").addClass("ui-selectee ui-selected");
		if (!checkNull(sceneSelectData.region)) {
			drawRegion(sceneSelectData.region[0]);
		}
	}else{
		if (!checkNull(sceneSelectData.region)) {
			for (var i = 0; i < sceneSelectData.region.length; i++) {
				drawRegion(sceneSelectData.region[i]);
			}
		}
		//重绘按钮
		if (!checkNull(sceneSelectData.button)) {
			for (var i = 0; i < sceneSelectData.button.length; i++) {
				drawRegion(sceneSelectData.button[i]);
			}
		}
		//重绘组件
		if (!checkNull(sceneSelectData.plug)) {
			for (var i = 0; i < sceneSelectData.plug.length; i++) {
				drawRegion(sceneSelectData.plug[i]);
			}
		} 
	}
}
/**
 * 刷新并绘制静态区域
 * 
 */
function flashstaticRegion() {
	if(checkNull(program.staticscene)){
		return;
	}
	//重绘区域
	if (!checkNull(program.staticscene[0].region)) {
		for (var i = 0; i < program.staticscene[0].region.length; i++) {
			drawRegion(program.staticscene[0].region[i]);
		}
	}
	//重绘按钮
	if (!checkNull(program.staticscene[0].button)) {
		for (var i = 0; i < program.staticscene[0].button.length; i++) {
			drawRegion(program.staticscene[0].button[i]);
		}
	}
	//重绘组件
	if (!checkNull(program.staticscene[0].plug)) {
		for (var i = 0; i < program.staticscene[0].plug.length; i++) {
			drawRegion(program.staticscene[0].plug[i]);
		}
	} 
}
/**
 * 提交新场景
 */
function subNewScene() {
	var sceneName = $("#newSceneName").val();
	if (checkZIFU(sceneName)) {
		return;
	}
	if (checkNull(sceneName)) {
		ZENG.msgbox.show("场景名称不能为空!", 3, 1000);
		$("#newSceneName").focus();
		return;
	}
	if (null != getSceneByName(sceneName)) {
		ZENG.msgbox.show("场景名称已存在!", 3, 800);
		$("#newSceneName").focus();
		return;
	}
	$("#newSceneWin").window('close');
	var newSceneJson = {
		ismain : "false",
		issaver:"false",
		region : null,
		life : 0,
		sceneName : sceneName,
		id : "scene" + sceneNum,
		backimg : sceneBackAll,
		backcolor : "",
		backimgName : ""
	};
	newScene(newSceneJson);
	if (checkNull(program.scene)) {
		program.scene = new Array();
	}
	program.scene.push(newSceneJson);
	sceneSelect(newSceneJson.id);
	sceneSelectData = newSceneJson;
	sceneNum++;
}

/**
 * 新增场景
 */
function newScene(sceneData) {
	var sceneText = sceneData.sceneName;
	if (sceneData.sceneName.length > 4) {
		sceneText = sceneText.substring(0, 4) + "...";
	}
	var title = sceneData.sceneName;
	var stylestr = "cursor:pointer;";
	//如果是屏保场景
//	if(program.saverid==sceneData.id){
//		title = "(屏保)"+title;
//		stylestr = stylestr+"text-decoration:underline;"
//		//将原来的屏保去掉
//		$("#selectable li div").css("text-decoration","none");
//	}
	var tempStr = "<li id='"
			+ sceneData.id
			+ "' oncontextmenu='showSceneMenu(\""
			+ sceneData.id
			+ "\");return false;' class='ui-widget-content'><div onclick='sceneSelect(\"" + sceneData.id
			+ "\")'  class='tooltipDiv' style='"+stylestr+"' title = '"
			+ title + "'>" + sceneText + "</div></li>";
	$("#selectable").append(tempStr);
	$(".tooltipDiv").tooltip({
		show : {
			effect : "slideDown",
			delay : 250
		},
		hide : {
			effect : "slideUp",
			delay : 250
		}
	});
	$('#selectable').sortable();
}
/**
 * 提交场景背景图片
 */
function subBackImg() {
	var elem = $(".elemdivSelect")[0];
	var imgPathStr = "";
	if (checkNull(elem)) {
		$.messager.confirm('空背景图', '您未选择图片,确定将会清空原背景图!', function(r) {
			if (r) {
				var allImg = $("#isAllSceneBack")[0].checked;
				if (allImg == true) {
					sceneBackAll = imgPathStr;
					for (var i = 0; i < program.scene.length; i++) {
						program.scene[i].backimg = imgPathStr;
						program.scene[i].backimgName = "";
					}
				} else {
					//sceneSelectData.backimg = imgPathStr;
				}
				if (checkNull(imgPathStr)) {
					imgPathStr = "img/u106.png";
				}
				if (backclicktype==3) {//场景背景图
					sceneSelectData.oldbackimg = sceneSelectData.backimg;
					sceneSelectData.backimg = imgPathStr;
					sceneSelectData.backimgName = "";
					$("#sceneBackImg").attr("src", imgPathStr);
				}else if(backclicktype==2){//互动按钮背景图片
					regionSelectData.oldbackimg = regionSelectData.backimg;
					regionSelectData.backimg = "";
					regionSelectData.bubackimgName = "";
					$("#backimg").html('<img src="" style="width:100px;">');
					showRegionImg(regionSelectData);
					
				}else if(backclicktype==1){//互动按钮点击图片
					regionSelectData.oldselectimg = regionSelectData.selectimg;
					regionSelectData.selectimg = "";
					regionSelectData.buselectimgName = "";
					$("#selectimg").html('<img src="" style="width:100px;">');
				}
			}
			$("#selectSceneBack").window('close');
		});
	} else {
		var elemDataTemp = getElemDataById(elem.id);
		var resolutionx = elemDataTemp.resolution.split('x')[0];
		var resolutiony = elemDataTemp.resolution.split('x')[1];
		imgPathStr = elemDataTemp.src;
		var imgview = elemDataTemp.src;
		if(resolutionx > 5000 || resolutiony > 5000){
			var index = imgPathStr.lastIndexOf("\\");
			imgview = imgPathStr.slice(0,index+1) + "large_img" + imgPathStr.slice(index+1);
		}
		var allImg = $("#isAllSceneBack")[0].checked;
		
		if (allImg == true) {//将该背景图应用到整个节目
			sceneBackAll = imgPathStr;
			for (var i = 0; i < program.scene.length; i++) {
				program.scene[i].backimg = imgPathStr;
				program.scene[i].backimgName = elemDataTemp.name;
				$("#sceneBackImg").attr("src", imgview);
			}
		} else {//将该背景图应用到当前场景
			if (backclicktype==3) {//场景背景图
					sceneSelectData.oldbackimg = sceneSelectData.backimg;
					sceneSelectData.backimg = imgPathStr;
					sceneSelectData.backimgName = elemDataTemp.name;
					$("#sceneBackImg").attr("src", imgview);
				}else if(backclicktype==2){//背景图片
					//创建相应按钮的图片
					regionSelectData.oldbackimg = regionSelectData.backimg;
					regionSelectData.backimg = imgPathStr;
					regionSelectData.bubackimgName = elemDataTemp.name;
					showRegionImg(regionSelectData);
					$("#backimg").html('<img src="'+imgview+'" style="width:100px;">'); 
				}else if(backclicktype==1){//点击图片
					regionSelectData.oldselectimg = regionSelectData.selectimg;
					regionSelectData.selectimg= imgPathStr;
					regionSelectData.buselectimgName = elemDataTemp.name;
					$("#selectimg").html('<img src="'+imgview+'" style="width:100px;">'); 
				}
		}
	}
	$("#selectSceneBack").window('close');
}
/**
 * 弹出背景图设置框
 */
function sceneBackWin(t) {
	if (checkNull(sceneSelectData)) {
		ZENG.msgbox.show("请先选中一个场景!", 3, 800);
		return;
	}
	backclicktype = t;
	document.getElementById("isAllSceneBack").checked = false;
	if(backclicktype == 2 || backclicktype == 1){
		$("#backImgLeftBottom").css("display","none");
		//document.getElementById("isAllSceneBack").checked = false;
	}else{
		$("#backImgLeftBottom").css("display","inline");
		//document.getElementById("isAllSceneBack").checked = false;
	}
	if(backclicktype == 2 || backclicktype == 3){
		$("#selectSceneBack").dialog({
			title : "&nbsp;选择背景图片"
		});
	}else{
		$("#selectSceneBack").dialog({
			title : "&nbsp;选择点击图片"
		});
	}
	$("#selectSceneBack").dialog('open');
	var tName = $("#searchName_back").val();
	tName = tName.replace("%", "/%");
	var data = {
		type : 3,
		page : 1,
		rows : 10,
		elem_name : tName,
		bgimg:1
	};
	$("#pagInationElem_back").pagination({
		onSelectPage : function(pageNumber, pageSize) {
			var data = {
				page : pageNumber,
				rows : pageSize,
				type : 3,
				bgimg:1
			};
			var tName = $("#searchName_back").val();
			tName = tName.replace("%", "/%");
			data.elem_name = tName;
			backImgLoad(data);
		}
	});
	backImgLoad(data);
	$('#pagInationElem_back').pagination('select', 1);
	// mouseUps('bjtp');
}
//查询背景图片
function searchNameBack() {
	var elemName = $("#searchName_back").val().trim();
	elemName = elemName.replace("%", "/%"); // 转译，去空格特殊符号
	var data = {
		type : 3,
		elem_name : elemName,
		page : 1,
		rows : 10,
		bgimg:1
	};
	if(elemName != null)
		data.bgimg = "";
	//backImgLoad(data);
	$('#pagInationElem_back').pagination('select', 1);
}
function backImgLoad(data) {
	data.audit = 1;
	//解决在IE11下数据不刷新问题
	data.temp = Math.random();
	$.ajax({
				url : 'element/queryElement.do',
				method : "POST",
				data : data,
				dataType : 'json',
				success : function(data) {
					var pagIn = $('#pagInationElem_back');
					pagIn.pagination({
						total : data.total
					});
					$("#elemListDiv_back").html("");
					elemListData = new Array();
					for (var i = 0; i < data.rows.length; i++) {
						var standard = 50;
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
							newWidth = 50;
							newHeight = 50;
						}
						var elemDataTemp = {
							id : data.rows[i].elem_id,
							name : data.rows[i].elem_name,
							resolution : data.rows[i].resolution,
							type : data.rows[i].type,
							src : data.rows[i].elem_path,
							thumbnailUrl : data.rows[i].thumbnailUrl,
							eWidth:newWidth,
							eHeight:newHeight,
							elemTempId : null
						};
						elemListData.push(elemDataTemp);
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
						$("#elemListDiv_back")
								.append(
										'<div onclick="elemAllUnSelect();selectElem(\''
												+ elemDataTemp.id
												+ '\')" id="'
												+ elemDataTemp.id
												+ '" class="elemdiv elemdivUnSelect easyui-tooltip" title="'
												+ tempName
												+ '"><div style="float:left;"><img  width="'+elemDataTemp.eWidth+'" height="'+elemDataTemp.eHeight+'" style="margin:5 '+(60-elemDataTemp.eWidth)/2+';" src="'
												+ tempUrl
												+ '"  ></div><div style="float:left;width:80px;"><ul style="margin: 10 0 0 5;padding: 0;list-style: none;"><li>'
												+ showName
												+ '</li><li style="margin-top:10px;">'
												+ elemDataTemp.resolution
												+ '</li></ul></div></div>');
					}
				},
				error : function(e) {
				}
			});
}
/**
 * 删除场景
 */
function deleteScene() {
	if (checkNull(sceneSelectData)) {
		ZENG.msgbox.show("没有选中的场景!", 3, 800);
		return;
	}
	  $.messager.defaults = { ok: "确定", cancel: "取消" };
	$.messager.confirm('删除', '删除后不可恢复，确定删除？', function(r) {
		if (r) {
			$("#" + sceneSelectData.id).remove();
			removeSceneById(sceneSelectData.id);
			sceneSelectData = undefined;
			checkbuttonscene();
			$('#sceneBack').html("");
		}
	});
}
/**
 * 更改分辨率
 */
function resolutionChange() {
	resolution = $('#resolution').val();
	resolutionX = parseInt(resolution.split("*")[0]);
	resolutionY = parseInt(resolution.split("*")[1]);
	$("#sceneBack").html("");
	initVar();
	initScene();
	sceneNum = 1;
	var newSceneJson = {
		ismain : "false",
		region : null,
		life:0,
		sceneName : "场景1",
		id : "scene" + sceneNum,
		backimg : sceneBackAll,
		backcolor : "",
		backimgName : ""
	};
	newScene(newSceneJson);
	if (checkNull(program.scene)) {
		program.scene = new Array();
	}
	program.scene.push(newSceneJson);
	sceneSelect(newSceneJson.id);
	sceneSelectData = newSceneJson;
	sceneNum++;
}
/**
 * 初始化场景画布
 */
function initScene() {
	var sceneBack = $('#sceneBack');
	if (sceneWMax == 0) {
		sceneWMax = parseInt(sceneBack.css("width"));
		sceneHMax = parseInt(sceneBack.css("height"));
	}
	var editProp;
	if (sceneWMax / resolutionX > sceneHMax / resolutionY) {
		editProp = changeDecimal(sceneHMax / resolutionY, 4);
	} else {
		editProp = changeDecimal(sceneWMax / resolutionX, 4);
	}
	program.editProp = editProp;
	realSceneH = resolutionY * editProp;
	realSceneW = resolutionX * editProp;
	sceneBack.css("width", realSceneW);
	sceneBack.css("height", realSceneH);
	var imgPath = sceneBackAll;
	if (checkNull(imgPath)) {
		imgPath = "img/u106.png";
	}
	sceneBack.html('<img id="sceneBackImg" src="' + imgPath
			+ '" style=" height:100%; width:100%;"align="middle" />');
	$("#selectable").html("");
}
/**
 * 小数处理函数
 * 
 * @param floatvar
 * @param number
 * @returns
 */
function changeDecimal(floatvar, number) {
	var f_x = parseFloat(floatvar);
	if (isNaN(f_x)) {
		return false;
	}
	var temp = Math.pow(10, number);
	var f_x = Math.round(floatvar * temp) / temp;
	return f_x;
}
/**
 * 新建区域
 */
function newRegion(type) {
	if (checkNull(sceneSelectData)) {
		ZENG.msgbox.show("请先选中一个场景!", 3, 800);
		return;
	}
	if (type == 2) {
		if (!checkNull(sceneSelectData.region)
				&& sceneSelectData.region.length > 0) {
			for (var i = 0; i < sceneSelectData.region.length; i++) {
				if (sceneSelectData.region[i].type == 2) {
					ZENG.msgbox.show("一个场景不能添加多个音频区域!", 3, 800);
					return;
				}
			}
		}
	}
	var regionWidth = parseInt(resolutionX / 2);
	var regionHeight = parseInt(resolutionY / 2);
	var regionData = {
		element : new Array(),
		id : "region" + regionCount,
		width : regionWidth,
		height : regionHeight,
		zIndex : lastZIndex,
		elemlife : "",
		name : typeName[type],
		type : type,
		left : 0,
		top : 0
	};
	//如果是网页则加上是否显示工具栏属性，默认为显示
	if(regionData.type==5){
		regionData.showtoolbar = false;
	}
	//如果是图片则加上图片切换特效属性，默认为无
	if(regionData.type==3){
		regionData.piceffect = 0;
	}
	drawRegion(regionData);
	if (checkNull(sceneSelectData.region)) {
		sceneSelectData.region = new Array();
	}
	sceneSelectData.region.push(regionData);
	changeSlelectRegion("region" + regionCount);
	regionCount++;
	lastZIndex = lastZIndex + 2;
}
//检查互动按钮中是否有指向不存在的场景，如果有至空其指向
function checkbuttonscene() {
	if (checkNull(program.scene))
		return;
	for (var i = 0; i < program.scene.length; i++) {
		var tempscene = program.scene[i];
		if (checkNull(tempscene.button))
			continue;
		for (var j = 0; j < tempscene.button.length; j++) {
			var jumpScene = tempscene.button[j].sceneid;
			var temp = getSceneById(jumpScene);
			if (checkNull(temp)) {
				tempscene.button[j].sceneid = "";
			}
		}
	}
}

/**
 * 显示区域title
 * 
 * @param region
 */
function showRegionTitle(regionData) {
	if (regionData.id.indexOf("button") > -1) {
		if (regionData.type == 1) {
			var jumpScene = regionData.sceneid;
			var temp = getSceneById(jumpScene);
			if (checkNull(temp)) {
				regionData.sceneid = "";
			}
			if (checkNull(regionData.sceneid)) {
				$("#" + regionData.id).attr("title", "未设置跳转场景！")
			} else {
				$("#" + regionData.id).attr("title", "跳转至：" + temp.sceneName);
			}
		}
	}
}
/**
 * 区域数组按层次排序
 */
function orderRegionByZIndex() {
	for (var i = sceneSelectData.region.length; i > 1; i--) {
		for (var j = 0; j < i - 1; j++) {
			var data1 = sceneSelectData.region[j];
			var data2 = sceneSelectData.region[j + 1];
			if (data1.zIndex > data2.zIndex) {
				sceneSelectData.region[j] = data2;
				sceneSelectData.region[j + 1] = data1;
			}
		}
	}
}
/**
 * 校验区域位置、大小
 * 
 * @param region
 * @param type
 */
function checkRegionSize(region, type) {
	var id = region.id;
	var left = parseInt(region.offsetLeft);
	var top = parseInt(region.offsetTop);
	var maxleft = resolutionX - regionSelectData.width;
	var maxtop = resolutionY - regionSelectData.height;
	var width = parseInt(region.offsetWidth);
	var height = parseInt(region.offsetHeight);
	var isanimate = false;
	if (isNaN(left) || left < 0) {
		left = 0;
		isanimate = true;
	}
	if (isNaN(top) || top < 0) {
		top = 0;
		isanimate = true;
	}
	if (left >= (realSceneW - width)) {
		left = realSceneW - width;
		if (left < 0)
			left = 0;
		isanimate = true;
	}
	if (top >= (realSceneH - height)) {
		isanimate = true;
		top = realSceneH - height;
		if (top < 0)
			top = 0;
	}
	if (isanimate) {
		$("#" + id).css("top", top + "px");
		$("#" + id).css("left", left + "px");
		// $("#" + id).animate({
		// top : top + "px",
		// left : left + "px"
		// }, "slow", function() {
		// });
	}
	if (type == "resize") {
		regionSelectData.width = parseInt(width / program.editProp);
		regionSelectData.height = parseInt(height / program.editProp);
		if (width == parseInt(resolutionX * program.editProp)) {
			regionSelectData.width = resolutionX;
		}
		if (height == parseInt(resolutionY * program.editProp)) {
			regionSelectData.height = resolutionY;
		}
	} else if (type == "remove" && !removelock) {
		// 实际像素绝对值大于1时才变化值（用来保证单击区域时不会移动）
		if (Math.abs(parseInt(parseInt(regionSelectData.left)
				* program.editProp)
				- left) > 1) {
			if(left==0){
				regionSelectData.left = left;
			}else if((parseInt(regionSelectData.left)
					- maxleft)>1){
				regionSelectData.left =maxleft;
			}else{
				regionSelectData.left = parseInt(left / program.editProp);
			}
		}
		if (Math
				.abs(parseInt(parseInt(regionSelectData.top) * program.editProp)
						- top) > 1) {
			if(top==0){
				regionSelectData.top = top;
			}else if((parseInt(regionSelectData.top)
					- maxtop)>1){
				regionSelectData.top =maxtop;
			}else{
				regionSelectData.top = parseInt(top / program.editProp);
			}
		}
		if (parseInt(regionSelectData.left) + parseInt(regionSelectData.width) > resolutionX) {
			regionSelectData.left = maxleft;
		}
		if (parseInt(regionSelectData.top) + parseInt(regionSelectData.height) > resolutionY) {
			regionSelectData.top = maxtop;
		}
		if (regionSelectData.left < 0) {
			regionSelectData.left = 0;
		}
		if (regionSelectData.top < 0) {
			regionSelectData.top = 0;
		}
	}
	showRegionAttr();
}
/**
 * 切换选择区域
 * 
 * @param regionId
 */
function changeSlelectRegion(regionId) {
	if (regionSelectData != undefined && regionSelectData.id == regionId) {
		
		return;
	}
	if (undefined != regionSelectData)
		$("#" + regionSelectData.id).css("border", " solid #bbb");
	regionSelectData = getRegionById(regionId);
	$("#" + regionId).css("border", " solid #7EB9EC");
	showRegionAttr();
}
/**
 * 通过区域ID获取区域数据
 * 
 * @param regionId
 * @returns
 */
function getRegionById(regionId) {
	//静态区域
	if(!checkNull(program.staticscene)){
		if(regionId.indexOf("region") > -1&&!checkNull(program.staticscene[0].region)){
			for (var i = 0; i < program.staticscene[0].region.length; i++) {
				if (program.staticscene[0].region[i].id == regionId) {
					return program.staticscene[0].region[i];
				}
			}
		}
		if(regionId.indexOf("button") > -1&&!checkNull(program.staticscene[0].button)){
			for (var i = 0; i < program.staticscene[0].button.length; i++) {
				if (program.staticscene[0].button[i].id == regionId) {
					return program.staticscene[0].button[i];
				}
			}
		}
		if(regionId.indexOf("plug") > -1&&!checkNull(program.staticscene[0].plug)){
			for (var i = 0; i < program.staticscene[0].plug.length; i++) {
				if (program.staticscene[0].plug[i].id == regionId) {
					return program.staticscene[0].plug[i];
				}
			}
		}
	}
	if (regionId.indexOf("region") > -1) {
		for (var i = 0; i < sceneSelectData.region.length; i++) {
			if (sceneSelectData.region[i].id == regionId) {
				return sceneSelectData.region[i];
			}
		}
	} else if (regionId.indexOf("button") > -1) {
		for (var i = 0; i < sceneSelectData.button.length; i++) {
			if (sceneSelectData.button[i].id == regionId) {
				return sceneSelectData.button[i];
			}
		}
	} else if (regionId.indexOf("plug") > -1) {
		for (var i = 0; i < sceneSelectData.plug.length; i++) {
			if (sceneSelectData.plug[i].id == regionId) {
				return sceneSelectData.plug[i];
			}
		}
	}
	
	
}
/**
 * 显示区域属性，同事校验当前选中区能否编辑属性
 */
function showRegionAttr() {
	var disabled = false;
	var valueShow = true;
	if (regionSelectData == undefined) {
		disabled = true;
		valueShow = false;
	} else if (regionSelectData.id.indexOf("plug") > -1
			&& (regionSelectData.type == 6||regionSelectData.type == 5)) {
		disabled = true;
	}
	if (!disabled) {
		$("#regionAttrX").attr("disabled", false);
		$("#regionAttrY").attr("disabled", false);
		$("#regionAttrW").attr("disabled", false);
		$("#regionAttrH").attr("disabled", false);
	} else {
		$("#regionAttrX").attr("disabled", "disabled");
		$("#regionAttrY").attr("disabled", "disabled");
		$("#regionAttrW").attr("disabled", "disabled");
		$("#regionAttrH").attr("disabled", "disabled");
	}
	if (valueShow) {
		$("#regionAttrX").val(regionSelectData.left);
		$("#regionAttrY").val(regionSelectData.top);
		$("#regionAttrW").val(regionSelectData.width);
		$("#regionAttrH").val(regionSelectData.height);
	} else {
		$("#regionAttrX").val("");
		$("#regionAttrY").val("");
		$("#regionAttrW").val("");
		$("#regionAttrH").val("");
	}
}
/**
 * 区域右键菜单显示
 * 
 * @param regionId
 * @returns {Boolean}
 */
function showRegionMenu(regionId) {
	var event = event ? event : window.event;
	var left = event.x;
	var top = event.y;
	if (navigator.appName.indexOf("Microsoft Internet Explorer") != -1
			&& document.all) {
		left = event.clientX;
		top = event.clientY;
	}
	if ("ActiveXObject" in window){
		left = event.clientX;
		top = event.clientY;
	}
	changeSlelectRegion(regionId);
	if (regionId.indexOf("region") > -1) {
		$("#regionMenu_upAll").show();
		$("#regionMenu_downAll").show();
		$("#regionMenu_up").show();
		$("#regionMenu_down").show();
		$(".hideorshow").show();
		//如果是网页则显示属性设置（是否显示工具条）
		if (regionSelectData.type==5) {
			$("#regionAttr_menu").show();
		}else{
			$("#regionAttr_menu").hide();
		}
	} else if (regionId.indexOf("button") > -1) {
		$("#regionMenu_upAll").hide();
		$("#regionMenu_downAll").hide();
		$("#regionMenu_up").hide();
		$("#regionMenu_down").hide();
		$("#regionAttr_menu").hide();
		$(".hideorshow").hide();
		$("#regionAttr_menu").hide();
	} else if (regionId.indexOf("plug") > -1) {
		$("#regionMenu_upAll").hide();
		$("#regionMenu_downAll").hide();
		$("#regionMenu_up").hide();
		$("#regionMenu_down").hide();
		$("#regionAttr_menu").show();
		$(".hideorshow").hide();
	}
	//静态属性
	if(regionSelectData.isstatic=="true"){
		$("#regionAttr_unstatic").show();
		$("#regionAttr_static").hide();
	}else{
		$("#regionAttr_static").show();
		$("#regionAttr_unstatic").hide();
	}
	
	$('#regionMenu').menu('show', {
		left : left,
		top : top
	});
	return false;
}
/**
 * 场景右键菜单显示
 * 
 * @param sceneId
 * @returns {Boolean}
 */
function showSceneMenu(sceneId) {
	var event = event ? event : window.event;
	var left = event.x;
	var top = event.y;
	if (navigator.appName.indexOf("Microsoft Internet Explorer") != -1
			&& document.all) {
		left = event.clientX;
		top = event.clientY;
	}
	if ("ActiveXObject" in window){
		left = event.clientX;
		top = event.clientY;
	}
	sceneSelect(sceneId);
	$('#sceneMenu').menu('show', {
		left : left,
		top : top
	});
	return false;
}
function setScreenWin(){
	$("#screensaverlife").numberbox("setValue",program.saverlife);
	$("#screenSceneWin").dialog('open');
}
/**
 * 删除已选区域
 */
function deleteSRegion() {
	deleteSRegionData();
	$('#' + regionSelectData.id).remove();
	regionSelectData = undefined;
	showRegionAttr();
	//如果是普通节目则计算场景时长
	if(program.istouch=="false"||!program.istouch){
		flashScenelife(sceneSelectData.region);
	}
}
/**
 * 删除选中区域的缓存数据
 */
function deleteSRegionData() {
	var tempArray = new Array();
	if(regionSelectData.isstatic=="true"){
		deleteStaticRegionData();
	}
	if (regionSelectData.id.indexOf("region") > -1) {
		if(sceneSelectData.region == null || sceneSelectData.region == "null"){
			sceneSelectData.region = regionSelectData;
		}
		for (var i = 0; i < sceneSelectData.region.length; i++) {
			if (regionSelectData.id != sceneSelectData.region[i].id) {
				tempArray.push(sceneSelectData.region[i]);
			}
		}
		sceneSelectData.region = tempArray;
	} else if (regionSelectData.id.indexOf("button") > -1) {
		if(sceneSelectData.button == null){
			sceneSelectData.button = regionSelectData;
		}
		for (var i = 0; i < sceneSelectData.button.length; i++) {
			if (regionSelectData.id != sceneSelectData.button[i].id) {
				tempArray.push(sceneSelectData.button[i]);
			}
		}
		sceneSelectData.button = tempArray;
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		if(sceneSelectData.plug == null){
			sceneSelectData.plug = regionSelectData;
		}
		for (var i = 0; i < sceneSelectData.plug.length; i++) {
			if (regionSelectData.id != sceneSelectData.plug[i].id) {
				tempArray.push(sceneSelectData.plug[i]);
			}
		}
		sceneSelectData.plug = tempArray;
	}
}
/**
 * 删除选中静态区域的缓存数据
 */
function deleteStaticRegionData() {
	var tempArray = new Array();
	if (regionSelectData.id.indexOf("region") > -1) {
		for (var i = 0; i < program.staticscene[0].region.length; i++) {
			if (regionSelectData.id != program.staticscene[0].region[i].id) {
				tempArray.push(program.staticscene[0].region[i]);
			}
		}
		program.staticscene[0].region = tempArray;
	} else if (regionSelectData.id.indexOf("button") > -1) {
		for (var i = 0; i < program.staticscene[0].button.length; i++) {
			if (regionSelectData.id != program.staticscene[0].button[i].id) {
				tempArray.push(program.staticscene[0].button[i]);
			}
		}
		program.staticscene[0].button = tempArray;
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		for (var i = 0; i < program.staticscene[0].plug.length; i++) {
			if (regionSelectData.id != program.staticscene[0].plug[i].id) {
				tempArray.push(program.staticscene[0].plug[i]);
			}
		}
		program.staticscene[0].plug = tempArray;
	}
}
/**
 * 区域上移一层
 */
function upRegionZIndex() {
	if (sceneSelectData.region.length <= 1) {
		return;
	}
	for (var i = 0; i < sceneSelectData.region.length; i++) {
		var item = sceneSelectData.region[i];
		var tempId = item.id;
		if (tempId == regionSelectData.id) {
			if (i == (sceneSelectData.region.length - 1)) {
				return;
			} else {
				var item1 = sceneSelectData.region[i + 1];
				var regionId1 = item1.id;
				document.getElementById(tempId).style.zIndex = parseInt(item1.zIndex);
				document.getElementById(regionId1).style.zIndex = parseInt(item.zIndex);
				var temp = item.zIndex;
				sceneSelectData.region[i].zIndex = item1.zIndex;
				sceneSelectData.region[i + 1].zIndex = temp;
				orderRegionByZIndex();
				return;
			}
		}
	}
}
/**
 * 区域移至顶层
 */
function upAllRegionZIndex() {
	if (sceneSelectData.region.length <= 1) {
		return;
	}
	lastZIndex = lastZIndex + 10;
	document.getElementById(regionSelectData.id).style.zIndex = lastZIndex;
	regionSelectData.zIndex = lastZIndex;
	orderRegionByZIndex();
}
/**
 * 区域下移一层
 */
function downRegionZIndex() {
	if (sceneSelectData.region.length <= 1) {
		return;
	}
	for (var i = 0; i < sceneSelectData.region.length; i++) {
		var item = sceneSelectData.region[i];
		var tempId = item.id;
		if (tempId == regionSelectData.id) {
			if (i == 0) {
				return;
			} else {
				var item1 = sceneSelectData.region[i - 1];
				var regionId1 = item1.id;
				document.getElementById(tempId).style.zIndex = parseInt(item1.zIndex);
				document.getElementById(regionId1).style.zIndex = parseInt(item.zIndex);
				var temp = item.zIndex;
				sceneSelectData.region[i].zIndex = item1.zIndex;
				sceneSelectData.region[i - 1].zIndex = temp;
				orderRegionByZIndex();
				return;
			}
		}
	}
}
/**
 * 区域移至底层
 */
function downAllRegionZIndex() {
	if (sceneSelectData.region.length <= 1) {
		return;
	}
	firstZIndex = firstZIndex - 2;
	document.getElementById(regionSelectData.id).style.zIndex = firstZIndex;
	regionSelectData.zIndex = firstZIndex;
	orderRegionByZIndex();
}
//新建按钮
function newButton(type) {
	if (checkNull(sceneSelectData)) {
		ZENG.msgbox.show("请先选中一个场景!", 3, 800);
		return;
	}
	var buttonWidth = parseInt(resolutionX / 4);
	var buttonHeight = parseInt(resolutionY / 4);
	var buttonData = {
		id : "button" + buttonCount,
		width : buttonWidth,
		height : buttonHeight,
		zIndex : lastZIndex + 4000,
		name : buttonTypeName[type],
		sceneid : "",
		style : 0,
		imgstyle : "",
		value : buttonTypeName[type],
		fontfamily : "",
		fontcolor : "",
		fontsize : "",
		backcolor : "",
		type : type,
		left : 0,
		top : 0,
		selectimg : "",//点击事件
		backimg : "",//背景图片,
		bubackimgName : "",
		buselectimgName : ""
		
	};
	drawRegion(buttonData);
	if (sceneSelectData.button == null || sceneSelectData.button.length == 0) {
		sceneSelectData.button = new Array();
	}
	sceneSelectData.button.push(buttonData);
	changeSlelectRegion("button" + buttonCount);
	
	
	buttonCount++;
	lastZIndex = lastZIndex + 2;
}
//键盘监听
function keyListen() {
	document.onkeydown = function(e) {
		if (checkNull(regionSelectData))
			return;
		var keycode = null;
		if (navigator.appName == "Microsoft Internet Explorer") {
			keycode = event.keyCode;
		} else {
			keycode = e.which;
		}
		event = event ? event : e;
		if (keycode == 13 && setRegionAttr != "") {//回车
			regionAttrCheck();
		} else if (keycode == 37) {//左箭头
			setRegionAttr = 'regionAttrX';
			$("#regionAttrX").val(parseInt($("#regionAttrX").val()) - 1);
			regionAttrCheck();
			setRegionAttr = "";
		} else if (keycode == 38) {//上箭头
			setRegionAttr = 'regionAttrY';
			$("#regionAttrY").val(parseInt($("#regionAttrY").val()) - 1);
			regionAttrCheck();
			setRegionAttr = "";
		} else if (keycode == 39) {//右箭头
			var tempx = parseInt($("#regionAttrX").val()) + 1;
			if ((tempx + parseInt(regionSelectData.width)) > resolutionX)
				return;
			setRegionAttr = 'regionAttrX';
			$("#regionAttrX").val(tempx);
			regionAttrCheck();
			setRegionAttr = "";
		} else if (keycode == 40) {//下箭头
			var tempy = parseInt($("#regionAttrY").val()) + 1;
			if ((tempy + parseInt(regionSelectData.height)) > resolutionY)
				return;
			setRegionAttr = 'regionAttrY';
			$("#regionAttrY").val(tempy);
			regionAttrCheck();
			setRegionAttr = "";
		}
	};
	document.onmousedown = function(e) {
		initButton(e);
	}
}

//确定按钮
function subButtonEvent() {
	var buttonStyles = document.getElementsByName("buttonStyle");
	var style = "0";
	for (var i = 0; i < buttonStyles.length; i++) {
		if (buttonStyles[i].checked) {
			style = buttonStyles[i].value;
		}
	}
	regionSelectData.style = style;
	regionSelectData.oldbackimg = regionSelectData.backimg;
	regionSelectData.oldselectimg = regionSelectData.selectimg;
	var jumpSceneId = $("#jumpSceneSelect").combo("getValue");
	regionSelectData.sceneid = jumpSceneId;
	showRegionTitle(regionSelectData);
	$('#buttonManagerWin').dialog('close');
	/*
	 * var buttontext = $("#buttonText").val(); regionSelectData.value =
	 * buttontext; regionSelectData.name = buttontext; if(style==2){
	 * regionSelectData.imgstyle = $("#buttonStyleSelect").combobox("getValue");
	 * regionSelectData.backimg=$("#buttonStyleImg").attr("src"); }
	 * $("#"+regionSelectData.id).html(buttontext);
	 */
	/*
	 * var tab = $('#buttonEvent_tabs').tabs('getSelected'); var eventType =
	 * tab[0].id; if (eventType == "sceneJump_tab") { }
	 */
}

//打开保存窗口
function saveProgramWin() {
	$("#sendTreeDiv").hide();
	$("#sendProgramButton").hide();
	$("#saveProgramButton").show();
	$("#highlevelButton").show();
	$("#lowlevelButton").hide();
	$("#highgradegrid").hide();
	$("#lowgradeview").show();
	$('#saveProgramWin').dialog({
		title : "&nbsp;保存节目"
	});
	var getRows = $('#highgradegridtable').datagrid('getRows');
	var rowslength = getRows.length;
	for ( var i= 0; i < rowslength; i++) {
		var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', getRows[i]);
    	$('#highgradegridtable').datagrid('deleteRow', rowIndex);  
	}
	$('#saveProgramWin').dialog('open');
	$.ajax({
			url : "role/queryScheduleLevelByRoleID.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			async : false,
			success : function(result) {
				schedulelevel = result;
				document.getElementById("pubpower").length=0;
				for (var i = 1; i <= schedulelevel; i++) {
					$("#pubpower").append('<option value="'+i+'">'+i+'</option>'); 
				}
			}
		});	
	loadProgramForm();
	if(isedit != ""){
		$.ajax({
					url : 'program/getProgram.do',
					type : 'post',
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					data : {
						"program_id" : isedit
					},
					success : function(data) {
						var pro = data.programMap.twinflag.program[0];
						if (pro!=undefined&&pro!=null) {
							$("#download_startTime").timespinner("setValue",pro.downloadStartTime);
							$("#download_endTime").timespinner("setValue",pro.downloadEndTime);
						}
						var loop = pro.loops[0].loop;
						index = 0;
						if(loop.length>1) {
							index = 1;
							$("#highgradegrid").show();
							$("#lowgradeview").hide();
							$("#highlevelButton").hide();
							$("#lowlevelButton").show();
							$('#highgradegridtable').datagrid("resize",{  
								height:"190px",
								width:"290px"
							});
							for(var i = 0; i < loop.length; i++){
								var days = "";
								var day = loop[i].days.split(",");
								var dayslength = day.length;
								for ( var j = 0; j < dayslength; j++) {
									switch (day[j]){ 
										case 'Monday' : day[j] = '一';  break;
										case 'Tuesday' : day[j] = '二';  break;
										case 'Wednesday' : day[j] = '三';  break;
										case 'Thursday' : day[j] = '四';  break;
										case 'Friday' : day[j] = '五';  break;
										case 'Saturday' : day[j] = '六';  break;
										case 'Sunday' : day[j] = '日';  break;
									} 
									days = days + day[j] + ",";
								}
								$('#highgradegridtable').datagrid('insertRow',{
									row: {
										starttime: loop[i].starttime,
										endtime: loop[i].endtime,
										circleweek: days.substring(0, days.length - 1)
									}
								});
							}
						}
					}
				});
	}
}
//查询节目是否审批过
function querystatus() {
	var status=null;
	$
			.ajax({
				url : 'program/getProgram.do',
				type : 'post',
				async : false,
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				data : {
					"program_id" : isedit
				},
				success : function(data) {
					status=data.audit_status;
				}
			});
	return status;
}
//打开发布窗口
function sendProgramWin() {
	var status=querystatus();
	  if(status==0){
		  $.messager.defaults = { ok: "确定", cancel: "取消" };
			$.messager
					.confirm(
							'确认',
							'节目还未审批，请先进行审批！',
							function(r) {
								if(r){
								window.location.href="program/programList.jsp";
								}
							});
	  }
	  else{
	$("#saveProgramButton").hide();
	$("#sendTreeDiv").show();
	$("#sendProgramButton").show();
	$("#lowlevelButton").hide();
	$("#highgradegrid").hide();
	$("#lowgradeview").show();
	$("#highlevelButton").show();
	if(program.downloadStartTime != "" && program.downloadStartTime != undefined){
		$("#download_startTime").timespinner({'disabled':false});
		$("#download_endTime").timespinner({'disabled':false});
		document.getElementsByName("istiming").item(0).checked=true;
	}
	$('#saveProgramWin').dialog({
		title : "&nbsp;发布节目"
	});
	var getRows = $('#highgradegridtable').datagrid('getRows');
	var rowslength = getRows.length;
	for ( var i= 0; i < rowslength; i++) {
		var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', getRows[i]);
    	$('#highgradegridtable').datagrid('deleteRow', rowIndex);  
	}
	$('#saveProgramWin').dialog('open');
	$
		.ajax({
			url : "role/queryScheduleLevelByRoleID.do",
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			async : false,
			success : function(result) {
				schedulelevel = result;
				document.getElementById("pubpower").length=0;
				for (var i = 1; i <= schedulelevel; i++) {
					$("#pubpower").append('<option value="'+i+'">'+i+'</option>'); 
				}
			}
		});
	$('#sendtree').tree({
		checkbox : true, // 定义是否在每个节点前边显示 checkbox
		animate : true, // 定义当节点展开折叠时是否显示动画效果
		lines : false,
		url : 'scrollingnews/queryTree.do',
		cache : true, // cache必须设置为false,意思为不缓存当前页，否则更改权限后绑定的权限还是上一次的操作结果
		onSelect : function(node) {
			if (node.checked) {
				$('#sendtree').tree('uncheck', node.target);
			} else {
				$('#sendtree').tree('check', node.target);
			}
		},
		onLoadSuccess : function() {
			var nodes = $('#sendtree').tree('getChildren');
			for (i = 0; i < nodes.length; i++) {
				if (nodes[i].id >= 40000) {
					$('#sendtree').tree('update', {
						target : nodes[i].target,
						iconCls : 'tree-terminal'
					});
				} else {
					$('#sendtree').tree('update', {
						target : nodes[i].target,
						iconCls : 'tree-group'
					});
				}
			}
		}
	});
	loadProgramForm();
	// 加载终端
	setTimeout(loadSendTree(), 1000);
	if(isedit != ""){
		$
				.ajax({
					url : 'program/getProgram.do',
					type : 'post',
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					data : {
						"program_id" : isedit
					},
					success : function(data) {
						var pro = data.programMap.twinflag.program[0];
						var loop = pro.loops[0].loop;
						if (pro!=undefined&&pro!=null) {
							$("#download_startTime").timespinner("setValue",pro.downloadStartTime);
							$("#download_endTime").timespinner("setValue",pro.downloadEndTime);
						}
						
						index = 0;
						if(loop.length>1) {
							index = 1;
							$("#highgradegrid").show();
							$("#lowgradeview").hide();
							$("#highlevelButton").hide();
							$("#lowlevelButton").show();
							$('#highgradegridtable').datagrid("resize",{  
								height:"190px",
								width:"290px"
							});
							for(var i = 0; i < loop.length; i++){
								var days = "";
								var day = loop[i].days.split(",");
								var dayslength = day.length;
								for ( var j = 0; j < dayslength; j++) {
									switch (day[j]){ 
										case 'Monday' : day[j] = '一';  break;
										case 'Tuesday' : day[j] = '二';  break;
										case 'Wednesday' : day[j] = '三';  break;
										case 'Thursday' : day[j] = '四';  break;
										case 'Friday' : day[j] = '五';  break;
										case 'Saturday' : day[j] = '六';  break;
										case 'Sunday' : day[j] = '日';  break;
									} 
									days = days + day[j] + ",";
								}
								$('#highgradegridtable').datagrid('insertRow',{
									row: {
										starttime: loop[i].starttime,
										endtime: loop[i].endtime,
										circleweek: days.substring(0, days.length - 1)									
									}
								});
							}
						}
					 }
				});
	}
	  }
}
//加载终端树
function loadSendTree() {
	if (!checkNull(program.id)) {
		$.ajax({
			url : 'program/querySendP_T.do',
			type : 'post',
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			data : {
				"program_id" : program.id
			},
			success : function(data) {
				var tempList = data.rows;
				for (var i = 0; i < tempList.length; i++) {
					var node = $('#sendtree').tree('find',
							tempList[i].terminal_id);
					$('#sendtree').tree('check', node.target);
				}
			}
		});
	}
}
//保存节目方法
function saveProgram() {
	if (!checkProgramData()) {
		return;
	}
	for ( var i = 0; i < program.loops[0].loop.length; i++) {
		var tempe = program.loops[0].loop[i].endtime.split(":");
		var temps = program.loops[0].loop[i].starttime.split(":");
		var st = temps[0]*3600 + temps[1]*60 + temps[2];
		var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
		if (parseInt(st) > parseInt(se)){
			ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
			return;
		}
		if (program.loops[0].loop[i].days == "") {
			ZENG.msgbox.show("周循环不能为空!", 3, 2000);
			return;		
		}
	}
	program.pubpower = document.getElementById("pubpower").value;
	
	var hasscreen = false;
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].backimg == "img/u106.png"){
			program.scene[i].backimg = "";
		}
		if(program.scene[i].id == "screensaver"){
			hasscreen = true;
			//program.saverid = "screensaver";
			for (var j = 0; j < program.scene.length; j++) {
				if(program.scene[j].ismain == "true"){
					program.scene[i].button[0].sceneid = program.scene[j].id;
					break;
				}
			}
		}
	}
	if(!hasscreen){
		program.saverlife = "60";
	}
//	if(program.screenscene != null && program.screenscene != "null"){
//		program.saverid = "screensaver"
//		for (var i = 0; i < program.scene.length; i++) {
//			if(program.scene[i].ismain == "true"){
//				program.screenscene[0].button[0].sceneid = program.scene[i].id;
//				break;
//			}
//			program.screenscene[0].button[0].sceneid = program.scene[0].id;
//		}
//	}
	var daysEl = document.getElementsByName("days");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			switch (daysEl[i].value){ 
					case '一' : daysEl[i].value = 'Monday';  break;
					case '二' : daysEl[i].value = 'Tuesday';  break;
					case '三' : daysEl[i].value = 'Wednesday';  break;
					case '四' : daysEl[i].value = 'Thursday';  break;
					case '五' : daysEl[i].value = 'Friday';  break;
					case '六' : daysEl[i].value = 'Saturday';  break;
					case '日' : daysEl[i].value = 'Sunday';  break;
			} 
			days = days + daysEl[i].value + ",";
		}
	}
	if (checkNull(days)&&index != 1) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	program.loopdesc = days.substring(0, days.length - 1);
	if(index == 1) {
		var rowcount = $('#highgradegridtable').datagrid('getRows');
		var circleweek = (rowcount[0].circleweek).split(",");
		days = "";
		for ( var i = 0; i < circleweek.length; i++) {
			switch (circleweek[i]){ 
					case '一' : temp = 'Monday';  break;
					case '二' : temp = 'Tuesday';  break;
					case '三' : temp = 'Wednesday';  break;
					case '四' : temp = 'Thursday';  break;
					case '五' : temp = 'Friday';  break;
					case '六' : temp = 'Saturday';  break;
					case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		program.loopstarttime = rowcount[0].starttime;
		program.loopstoptime = rowcount[0].endtime;
		program.loopdesc = days.substring(0, days.length - 1);
	}else if(index == 0) {
		var temp = {
			'starttime':$("#day_startTime").val(),
			'endtime':$("#day_endTime").val(),
			'days':days
		};
		program.loops[0].loop = [];
		program.loops[0].loop.push(temp);
	}
	if(index == 1) {
		var rowcount = $('#highgradegridtable').datagrid('getRows');
		var circleweek = (rowcount[0].circleweek).split(",");
		days = "";
		for ( var i = 0; i < circleweek.length; i++) {
			switch (circleweek[i]){ 
					case '一' : temp = 'Monday';  break;
					case '二' : temp = 'Tuesday';  break;
					case '三' : temp = 'Wednesday';  break;
					case '四' : temp = 'Thursday';  break;
					case '五' : temp = 'Friday';  break;
					case '六' : temp = 'Saturday';  break;
					case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		program.loopstarttime = rowcount[0].starttime;
		program.loopstoptime = rowcount[0].endtime;
		program.loopdesc = days.substring(0, days.length - 1);
	}else if(index == 0) {
		var stoptime = program.loopstoptime.split(":");
		var starttime = program.loopstarttime.split(":");
		var stoptemp = stoptime[0]*3600 + stoptime[1]*60 + stoptime[2];
		var starttemp = starttime[0]*3600 + starttime[1]*60 + starttime[2];
		if(parseInt(starttemp) > parseInt(stoptemp)) {
			ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
			return;
		}
		program.loops[0].loop = [];
		var temp = {
			'starttime':$("#day_startTime").val(),
			'endtime':$("#day_endTime").val(),
			'days':program.loopdesc
		};
		program.loops[0].loop.push(temp);
	}
	checkScene();
	if(program.scene.length == 0 || (program.scene.length == 1 && program.scene[0].id == "screensaver")){
		ZENG.msgbox.show("请添加主场景!", 3, 2000);
		return;
	}
	if($("#download_startTime").val() != ""){
		program.downloadStartTime=$("#download_startTime").val() + ":00";
		program.downloadEndTime=$("#download_endTime").val() + ":00";
	}else{
		program.downloadStartTime=$("#download_startTime").val();
		program.downloadEndTime=$("#download_endTime").val();
	}
	var flag=validationTime(program.downloadStartTime,program.downloadEndTime);
	if (!flag) {
		ZENG.msgbox.show("节目下载开始结束时间不能等于或者小于开始时间！！",5,2000);
		return;
	}
	sortscene();
	var jsonStr = JSON.stringify(program);
	$.ajax({
		url : 'program/insertProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"programJson" : jsonStr,
			"type" : "save"
		},
		success : function(data) {
			if (!data.success) {
				ZENG.msgbox.show(data.msg, 3, 1000);
				return;
			}
			$.messager.confirm('确认', '保存成功,确认继续编辑取消返回列表？', function(r) {
				if (r) {
					program.id = data.program_id;
					$('#saveProgramWin').dialog('close');
				} else {
					setProStatus(false);
					var jumppath = "program/programList.jsp";
					var name = navigator.appName;
					if ("ActiveXObject" in window){
						jumppath = "programList.jsp";
					}
					window.location.href = jumppath;
				}
			});
		}
	});
}
function checkScene(){
	var scenearr = program.scene;
	for (var i = 0; i < scenearr.length; i++) {
		if(scenearr[i].backimg != "" && scenearr[i].backimg != "img/u106.png"){
			if(scenearr[i].life == 0)
				scenearr[i].life = 10;
		}else{
			var sceneplugarr = scenearr[i].plug;
			if(sceneplugarr != undefined){
				for (var j = 0; j < sceneplugarr.length; j++) {
					var plugweather = sceneplugarr[j].weather;
					var plugtimepiece = sceneplugarr[j].timepiece;
					var plugcount = sceneplugarr[j].count;
					if(plugweather != undefined && sceneplugarr[j].weather.length > 0 && scenearr[i].life == 0){
						scenearr[i].life = 10;
						break;
					}else if(plugtimepiece != undefined && sceneplugarr[j].timepiece.length > 0 && scenearr[i].life == 0){
						scenearr[i].life = 10;
						break;
					}else if(plugcount != undefined && sceneplugarr[j].count.length > 0 && scenearr[i].life == 0){
						scenearr[i].life = 10;
						break;
					}
				}
			}
		}
	}
}
//
function convertToDateTime(s) {
	s = s.replace("-/g", "/");
	return new Date(s);
}
function sendProgram() {
	if (!checkProgramData()) {
		return;
	}
	var stopdate = $("#endTime").combo("getValue");
	var tempend = convertToDateTime(stopdate.replace("-","/").replace("-","/"));
	var nowDate = new Date();
	if (nowDate > tempend) {
		ZENG.msgbox.show("该节目单已经过期，如需发布请重新设置结束时间！", 3, 2000);
		return false;
	}
	if($("#download_startTime").val() != ""){
		program.downloadStartTime=$("#download_startTime").val() + ":00";
		program.downloadEndTime=$("#download_endTime").val() + ":00";
	}else{
		program.downloadStartTime=$("#download_startTime").val();
		program.downloadEndTime=$("#download_endTime").val();
	}
	var flag=validationTime(program.downloadStartTime,program.downloadEndTime);
	if (!flag) {
		ZENG.msgbox.show("节目下载开始结束时间不能等于或者小于开始时间！！",5,2000);
		return;
	}
	program.pubpower = document.getElementById("pubpower").value;
	var hasscreen = false;
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].backimg == "img/u106.png"){
			program.scene[i].backimg = "";
		}
		if(program.scene[i].id == "screensaver"){
			hasscreen = true;
			//program.saverid = "screensaver";
			for (var j = 0; j < program.scene.length; j++) {
				if(program.scene[j].ismain == "true"){
					program.scene[i].button[0].sceneid = program.scene[j].id;
					break;
				}
			}
		}
	}
	if(!hasscreen){
		program.saverlife = "60";
	}
//	if(program.screenscene != null && program.screenscene != "null"){
//		program.saverid = "screensaver"
//		for (var i = 0; i < program.scene.length; i++) {
//			if(program.scene[i].ismain == "true"){
//				program.screenscene[0].button[0].sceneid = program.scene[i].id;
//				break;
//			}
//			program.screenscene[0].button[0].sceneid = program.scene[0].id;
//		}
//	}
	var terminalIds = [];
	var checked = $("#sendtree").tree("getChecked"); // 得到选择的终端
	for (var i = 0; i < checked.length; i++) {
		if (checked[i].id < 40000) { // 去除终端组
			continue;
		} else {
			if (terminalIds.toString().indexOf(checked[i].id) > -1) { // 去掉重复的终端ID
				continue;
			} else {
				terminalIds.push(checked[i].id);
			}
		}
	}
	if (terminalIds.length == 0) {
		ZENG.msgbox.show("请先选择终端!", 5, 1000);
		return;
	}
	if(index == 1) {
		var rowcount = $('#highgradegridtable').datagrid('getRows');
		var circleweek = (rowcount[0].circleweek).split(",");
		days = "";
		for ( var i = 0; i < circleweek.length; i++) {
			switch (circleweek[i]){ 
					case '一' : temp = 'Monday';  break;
					case '二' : temp = 'Tuesday';  break;
					case '三' : temp = 'Wednesday';  break;
					case '四' : temp = 'Thursday';  break;
					case '五' : temp = 'Friday';  break;
					case '六' : temp = 'Saturday';  break;
					case '日' : temp = 'Sunday';  break;
			} 
			days = days + temp + ",";
		}
		program.loopstarttime = rowcount[0].starttime;
		program.loopstoptime = rowcount[0].endtime;
		program.loopdesc = days.substring(0, days.length - 1);
	}else if(index == 0) {
		var stoptime = program.loopstoptime.split(":");
		var starttime = program.loopstarttime.split(":");
		var stoptemp = stoptime[0]*3600 + stoptime[1]*60 + stoptime[2];
		var starttemp = starttime[0]*3600 + starttime[1]*60 + starttime[2];
		if(parseInt(starttemp) > parseInt(stoptemp)) {
			ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
			return;
		}
		program.loops[0].loop = [];
		var temp = {
			'starttime':$("#day_startTime").val(),
			'endtime':$("#day_endTime").val(),
			'days':program.loopdesc
		};
		program.loops[0].loop.push(temp);
	}
	for ( var i = 0; i < program.loops[0].loop.length; i++) {
		var tempe = program.loops[0].loop[i].endtime.split(":");
		var temps = program.loops[0].loop[i].starttime.split(":");
		var st = temps[0]*3600 + temps[1]*60 + temps[2];
		var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
		if (parseInt(st) > parseInt(se)){
			ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
			return;
		}
		if (program.loops[0].loop[i].days == "") {
			ZENG.msgbox.show("周循环不能为空!", 3, 2000);
			return;		
		}
	}
	var timearr = [];
	for (var i = 0; i < program.loops[0].loop.length; i++) {
		var ttemp = program.loops[0].loop[i].starttime+"@"+program.loops[0].loop[i].endtime+"@"+program.loops[0].loop[i].days;
		timearr.push(ttemp);
	}
	$.ajax({
		url : 'program/queryIsConflict.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"program_id" : isedit,
			"startdate" : program.startdate,
			"stopdate" : program.stopdate,
			"timearr" : timearr,
			"terminalIds" : terminalIds,
			"pubpower" : program.pubpower,
			"istouch" : program.istouch
		},
		success : function(result) {
			if (!result.success) {
				ZENG.msgbox.show("查询节目冲突失败", 3, 2000);
				return;
			}
			if (result.isconflict) {
				$.messager.confirm('确认','以下终端：<br>'+result.conflictterminals+'<br>在该时间段已有节目单发布,需排队等候!是否继续发布?',function(r){    
				    if (!r){    
				       return;  
				    } else {
				    	if(!$('#istiming').is(':checked')){
				    		isTerminalTiming(terminalIds);
				    	}else{
				    		insertProgram(terminalIds);
				    	}
				    }
				});
			} else {
				if(!$('#istiming').is(':checked')){
		    		isTerminalTiming(terminalIds);
		    	}else{
		    		insertProgram(terminalIds);
		    	}
			} 
		}
	});	
}
function loadProgramForm() {
	if (checkNull(program.id)) {
		return;
	}
	$("#programForm").form("load", program);
	var days = program.loopdesc.split(",");
	var tempJson = {
		p_name:program.name
	};
	$("#pubpower").val(program.pubpower);
	$("#programForm").form("load", tempJson);
	var daysEl = document.getElementsByName("days");
	for(var i=0;i<daysEl.length;i++){
		daysEl[i].checked = false;
	}
	for(var i=0;i<days.length;i++){
		switch (days[i]){ 
				case 'Monday' : daysEl[0].checked = true;  break;
				case 'Tuesday' : daysEl[1].checked = true;  break;
				case 'Wednesday' : daysEl[2].checked = true;  break;
				case 'Thursday' : daysEl[3].checked = true;  break;
				case 'Friday' : daysEl[4].checked = true;  break;
				case 'Saturday' : daysEl[5].checked = true;  break;
				case 'Sunday' : daysEl[6].checked = true;  break;
		} 
	}
}
function checkProgramname(programName){
	var reg = new RegExp('^[^\\\\\\/:*?\\"<>|]+$');// 转义 \ 符号也不行
	if(reg.test(programName))
		return true;
	else
		return false;
}
function checkProgramData() {
	var programName = $("#programName").val();
	if (checkNull(programName)) {
		ZENG.msgbox.show("节目名称不能为空！", 3, 2000);
		return false;
	}
	if (programName.length > 40) {
		ZENG.msgbox.show("节目名称不能大于40个字符", 3, 2000);
		return false;
	}
	if (!checkProgramname(programName)) {
		ZENG.msgbox.show('节目名称不能包含‘\’,‘/’,‘:’,‘*’,‘?’,‘"’,‘<’,‘>’字符!', 3, 2000);
		return false;	
	}
	programName = programName.replace("%", "\%").replace(" ", "");
	var startTime = $("#startTime").combo("getText");
	var endTime = $("#endTime").combo("getText");
	var tempstart = convertToDateTime(startTime);
	var tempend = convertToDateTime(endTime);
	if (parseInt(tempstart) > parseInt(tempend)) {
		ZENG.msgbox.show("开始时间必须早于结束时间！", 3, 2000);
		return false;
	}
	var day_startTime = $("#day_startTime").val();
	if (checkNull(day_startTime)) {
		ZENG.msgbox.show("循环开始时间不能为空！", 3, 2000);
		return false;
	}
	var day_endTime = $("#day_endTime").val();
	if (checkNull(day_endTime)) {
		ZENG.msgbox.show("循环结束时间不能为空！", 3, 2000);
		return false;
	}
	var daysEl = document.getElementsByName("days");
	var days = "";
	for (var i = 0; i < daysEl.length; i++) {
		if (daysEl[i].checked) {
			days = days + daysEl[i].value + ",";
		}
	}
	days = days.substring(0, days.length - 1);
	if (checkNull(program.scene) || program.scene.length == 0) {
		ZENG.msgbox.show("不能保存空节目！", 3, 2000);
		return false;
	}
	var hasData = false;
	for (var i = 0; i < program.scene.length; i++) {
		var tempData = program.scene[i];
		if ((!checkNull(tempData.region) && tempData.region.length > 0)
				|| (!checkNull(tempData.button) && tempData.button.length > 0)
				|| (!checkNull(tempData.plug) && tempData.plug.length > 0)) {
			hasData = true;
			break;
		}
	}
	
	if(program.staticscene != null && program.staticscene != "null"){
		if ((!checkNull(program.staticscene[0].region) && program.staticscene[0].region.length > 0)
				|| (!checkNull(program.staticscene[0].button) && program.staticscene[0].button.length > 0)
				|| (!checkNull(program.staticscene[0].plug) && program.staticscene[0].plug.length > 0)) {
			hasData = true;
		}
	}
	if (!hasData) {
		ZENG.msgbox.show("不能保存空节目！", 3, 2000);
		return false;
	}
	program.name = programName;
	program.startdate = startTime;
	program.stopdate = endTime;
	program.height = resolutionY;
	program.width = resolutionX;
	program.loopstarttime = day_startTime;
	program.loopstoptime = day_endTime;
	program.loopdesc = days;
	program.looptype = 2;
	program.regionCount = regionCount;
	program.buttonCount = buttonCount;
	program.elemCount = elemCount;
	program.lastZIndex = lastZIndex;
	program.firstZIndex = firstZIndex;
	program.sceneNum = sceneNum;
	program.isloop = true;
	program.loops = new Array();
	program.loops[0]=new Object();
	program.loops[0].loop = new Array();
	if(program.scene.length == 0 || (program.scene.length == 1 && program.scene[0].id == "screensaver")){
		ZENG.msgbox.show("请添加主场景!", 3, 2000);
		return;
	}
	//排序场景
	sortscene();
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	for ( var i = 0; i < rowcount.length; i++) {
		var days = "";
		var circleweek = (rowcount[i].circleweek).split(",");
		for ( var j = 0; j < circleweek.length; j++) {
			switch (circleweek[j]){ 
					case '一' : circleweek[j] = 'Monday';  break;
					case '二' : circleweek[j] = 'Tuesday';  break;
					case '三' : circleweek[j] = 'Wednesday';  break;
					case '四' : circleweek[j] = 'Thursday';  break;
					case '五' : circleweek[j] = 'Friday';  break;
					case '六' : circleweek[j] = 'Saturday';  break;
					case '日' : circleweek[j] = 'Sunday';  break;
			} 
			days = days + circleweek[j] + ",";
		}
		var temp = {
			'starttime':rowcount[i].starttime,
			'endtime':rowcount[i].endtime,
			'days':days.substring(0, days.length - 1)
		};
		program.loops[0].loop.push(temp);
	}
	return true;
}
//字符串转时间
function convertToDateTime(s) {
	s = s.replace("-/g", "/");
	return new Date(s)
}
//选中素材库中的素材
function selectElem(elemId) {
	if ($("#" + elemId).attr("class").indexOf("elemdivUnSelect") > 0) {
		$("#" + elemId).removeClass("elemdivUnSelect");
		$("#" + elemId).addClass("elemdivSelect");
	} else {
		$("#" + elemId).addClass("elemdivUnSelect");
		$("#" + elemId).removeClass("elemdivSelect");
	}
}
//选中已添加的素材
function selectHadElem(elemId) {
	if ($("#" + elemId).attr("class").indexOf("r_elemdivUnSelect") > 0) {
		$("#" + elemId).removeClass("r_elemdivUnSelect");
		$("#" + elemId).addClass("r_elemdivSelect");
		$("#" + elemId).css("background-color","#7EB9EC");
	} else {
		$("#" + elemId).addClass("r_elemdivUnSelect");
		$("#" + elemId).removeClass("r_elemdivSelect");
		$("#" + elemId).css("background-color","#ffffff");
	}
}
//全选素材库当前页的素材
function elemAllSelect() {
	var elem = $(".elemdiv");
	elem.removeClass("elemdivUnSelect");
	elem.addClass("elemdivSelect");
}
//全选选中页的素材
function rElemAllSelect() {
	var elem = $(".r_elemdiv");
	elem.removeClass("r_elemdivUnSelect");
	elem.addClass("r_elemdivSelect");
	elem.css("background-color","#7EB9EC");
}
//全不选素材库当前页的素材
function elemAllUnSelect() {
	var elem = $(".elemdiv");
	elem.removeClass("elemdivSelect");
	elem.addClass("elemdivUnSelect");
}
//通过素材id获取已添加素材信息
function getElemDataById(elemId) {
	if (elemListData == null || elemListData == undefined)
		return null;
	for (var i = 0; i < elemListData.length; i++) {
		if (elemListData[i].id == elemId)
			return elemListData[i];
	}
}
//删除已添加素材方法
function deleteElem() {
	var elem = $(".r_elemdivSelect");
	if (elem == undefined || elem.length == 0)
		return;
	for (var i = 0; i < elem.length; i++) {
		deleElemByTempId(elem[i].id);
	}
	elem.remove();
	showRegionImg(regionSelectData);
	//如果是普通节目则计算区域时长
	if(program.istouch=="false"||!program.istouch){
		flashRegionlife(regionSelectData.element);
		flashScenelife(sceneSelectData.region);
	}
}
//保存模板方法
function saveTemplate() {
	var templateName = $("#saveTemplate_name").val();
	templateName = templateName.replace("%", "/%").replace(" ", "");
	var templateDes = $("#saveTemplate_des").val();
	if (checkNull(templateName)) {
		ZENG.msgbox.show("模板名称不能为空!", 3, 2000);
		return;
	}
	if (templateName.length > 40) {
		ZENG.msgbox.show("模板名称长度最大不能超过40个字符!", 3, 2000);
		return;
	}
	if (templateDes.length > 150) {
		ZENG.msgbox.show("模板描述长度最大不能超过150个字符!", 3, 2000);
		return;
	}
	if (checkZIFU(templateName)) {
		return;
	}
	program.width = resolutionX;
	program.height = resolutionY;
	program.regionCount = regionCount;
	program.buttonCount = buttonCount;
	program.sceneNum = sceneNum;
	program.elemCount = elemCount;
	program.lastZIndex = lastZIndex;
	program.firstZIndex = firstZIndex;
	var jsonStr = JSON.stringify(program);
	$.ajax({
		url : 'template/insertTemplate.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"templateJson" : jsonStr,
			name : templateName,
			des : templateDes,
			replace:false
		},
		success : function(data) {
			if (!data.success) {
				if(data.msg=="模板名称已经存在"){
					$.messager.confirm('覆盖', '该模板名已存在，是否覆盖原模板？', function(r) {
						if (r) {
							$.ajax({
								url : 'template/insertTemplate.do',
								type : 'post',
								contentType : "application/x-www-form-urlencoded;charset=UTF-8",
								data : {
									"templateJson" : jsonStr,
									name : templateName,
									des : templateDes,
									replace:true
								},
								success : function(data) {
									if (!data.success) {
										ZENG.msgbox.show(data.msg, 5, 2000);
										return;
									}
									$("#addTemplateWin").dialog('close');
									ZENG.msgbox.show("保存成功!", 4, 2000);
								}
							});
						}
					})
				}else{
					ZENG.msgbox.show(data.msg, 5, 2000);
					return;
				}
			}else{
				$("#addTemplateWin").dialog('close');
				ZENG.msgbox.show("保存成功!", 4, 2000);
			}
		}
	});
}
//显示保存模板窗口
function saveTemplateWin() {
	if (checkNull(program.scene) || program.scene.length == 0) {
		ZENG.msgbox.show("场景不能为空！", 3, 2000);
		return;
	}
	$("#addTemplateWin").dialog('open');
}
//弹出已有模板列表以供选择
function selectTemplateWin() {
	$("#searchName_template").textbox("setValue", "");
	$('#templateList')
			.datagrid(
					{
						width : '100%',
						height : 410,
						pagination : true, // 分页控件
						queryParams : {
							page : 1,
							rows : 10
						},
						nowrap : true, // 设置为true,当数据长度超出列宽的时候将会自动截取
						striped : true, // 设置为true将交替显示行背景
						collapsible : true,// 显示可折叠按钮
						url : 'template/queryTemplate.do',// url调用Action方法
						loadMsg : '数据装载中......',
						singleSelect : false,// 为true时只能选择单行
						fitColumns : true,// 允许表格自动缩放，以适应父容器
						columns : [ [
								{
									field : '',
									checkbox : true,
									align : 'center',
									title : ''
								},
								{
									field : 'name',
									title : '模板名称',
									align : 'center',
									width : '150px',
									formatter : function(value, row, index) {
										if (value.length > 10) {
											return "<p class='easyui-tooltip' title='"
													+ value
													+ "'>"
													+ value.substring(0, 9)
													+ "...</p>";
										} else {
											return "<p class='easyui-tooltip' title='"
													+ value
													+ "'>"
													+ value
													+ "</p>";
										}
									}
								},
								{
									field : 'is_touch',
									title : '模板类型',
									align : 'center',
									width : '60px',
									formatter : function(value, row, index) {
										if(value == 0){
											return "<p class='easyui-tooltip'>普通</p>";
										}else{
											return "<p class='easyui-tooltip'>互动</p>";
										}
									}
								},
								{
									field : 'description',
									title : '模板描述',
									align : 'center',
									width : '140px',
									formatter : function(value, row, index) {
										if (value.length > 15) {
											return "<p class='easyui-tooltip' title='"
													+ value
													+ "'>"
													+ value.substring(0, 14)
													+ "...</p>";
										} else {
											return "<p class='easyui-tooltip' title='"
													+ value
													+ "'>"
													+ value
													+ "</p>";
										}
									}
								},
								{
									field : 'resolution',
									title : '分辨率',
									align : 'center',
									width : '85px'
								},
								{
									field : 'creator_name',
									title : '创建人',
									align : 'center',
									width : '90px',
									formatter : function(value, row, index) {
										if (value.length > 6) {
											return "<p class='easyui-tooltip' title='"
													+ value
													+ "'>"
													+ value.substring(0, 5)
													+ "...</p>";
										} else {
											return "<p class='easyui-tooltip' title='"
													+ value
													+ "'>"
													+ value
													+ "</p>";
										}
									}
								} ] ],
						toolbar : [
								{
									text : '确定',
									iconCls : 'icon-add',
									handler : function() {
										var rows = $("#templateList").datagrid(
												"getSelections");
										if (rows.length == 0) {
											ZENG.msgbox.show("请先选择模板", 3, 1000);
											return;
										}
										if (rows.length > 1) {
											ZENG.msgbox.show("只能选择一个模板", 3,
													1000);
											return;
										}
										var templateId = rows[0].template_id;
										var url = rows[0].url;
										$("#saveTemplate_name").val(rows[0].name);
										$("#saveTemplate_des").val(rows[0].description);
										$
												.ajax({
													url : 'template/getTemplate.do',
													type : 'post',
													contentType : "application/x-www-form-urlencoded;charset=UTF-8",
													data : {
														"template_id" : templateId,
														"url" : url
													},
													success : function(data) {
														if (!data.success) {
															ZENG.msgbox.show(
																	data.msg,
																	3, 1000);
															return;
														}
														var pro = data.templateMap.program;
														setResolutionText(
																pro.width,
																pro.height);
														if(pro.istouch == "true"){
															$("#regionAttr_static").removeClass('menu-item-disabled');
															//var a = document.getElementById("regionAttr_static");
															//a.onclick = "setRegionToStatic()";
															$("#regionAttr_static").attr("onclick","setRegionToStatic()");
															program.istouch = "true";
															//当节目单类型为普通节目时隐藏互动元素
																$("#touchDiv").show();
																//$("#screensaverdiv").show();
																$("#scenelifediv").hide();
														}else{
															$("#touchDiv").hide();
														}
														$("#sceneBack")
																.html("");
														initVar();
														resolution = $(
																'#resolution')
																.val();
														resolutionX = parseInt(resolution
																.split("*")[0]);
														resolutionY = parseInt(resolution
																.split("*")[1]);
														regionCount = parseInt(pro.regionCount);
														buttonCount = parseInt(pro.buttonCount);
														lastZIndex = parseInt(pro.lastZIndex);
														firstZIndex = parseInt(pro.firstZIndex);
														elemCount = parseInt(pro.elemCount);
														sceneNum = parseInt(pro.sceneNum);
														initScene();
														pro.editProp = program.editProp;
														program = pro;
														initProgram();
														$("#selectTemplateWin")
																.dialog('close');
													}
												});
									}
								},
								{
									text : '删除',
									iconCls : 'icon-remove',
									handler : function() {
										var rows = $("#templateList").datagrid(
												"getSelections");
										if (rows.length == 0) {
											$.messager.alert("提示信息", "请选择模板");
											return;
										}
										  $.messager.defaults = { ok: "确定", cancel: "取消" };
										$.messager
												.confirm(
														'确认',
														'确定删除该模板？',
														function(r) {
															if (r) {
																var ids = new Array();
																var urls = new Array();
																for (var i = 0; i < rows.length; i++) {
																	ids[i] = rows[i].template_id;
																	urls[i] = rows[i].url;
																}
																$
																		.ajax({
																			url : 'template/deleteTemplate.do',
																			type : 'post',
																			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
																			data : {
																				"ids" : ids,
																				"urls" : urls
																			},
																			success : function(
																					data) {
																				$(
																						"#templateList")
																						.datagrid(
																								"reload");
																				ZENG.msgbox
																						.show(
																								"删除成功!",
																								4,
																								2000);
																			}
																		});
															}
														});
									}
								} ]
					});
	$("#selectTemplateWin").dialog('open');
}
//搜索模板
function searchTemplate() {
	var tName = $("#searchName_template").textbox("getValue");
	tName = tName.replace("%", "/%").replace(" ", ""); // 转译，去空格特殊符号
	$('#templateList').datagrid('load', {
		name : tName
	});
}
//初始化节目（编辑节目时调用）
function initProgram() {
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id != "screensaver")
			newScene(program.scene[i]);
	}
	sceneSelectData = undefined;
	regionSelectData = undefined;
	sceneSelect(program.scene[0].id);
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id == "screensaver"){
			$('#screenlabel').show();
		    $('#screendelete').show();
		    $('#screenbutton').show();
		    break;
		}
	}
//	if(program.screenscene != null && program.screenscene != "null"){
//		$('#screenlabel').show();
//	    $('#screendelete').show();
//	    $('#screenbutton').show();
//	}
	if(program.istouch=="false"||!program.istouch){
		$("#screenbutton").hide();
	}
}
//检查入参是否为空
function checkNull(str) {
	if (str == null || str == undefined || str == "null" || str == ""
			|| str == undefined)
		return true;
	return false;
}
//添加默认场景
function addInitScene() {
	var newSceneJson = {
		ismain : "false",
		region : null,
		life:0,
		sceneName : "场景1",
		id : "scene" + sceneNum,
		backimg : sceneBackAll,
		backcolor : "",
		backimgName : ""
	};
	newScene(newSceneJson);
	if (checkNull(program.scene)) {
		program.scene = new Array();
	}
	program.scene.push(newSceneJson);
	sceneSelect(newSceneJson.id);
	sceneSelectData = newSceneJson;
	sceneNum++;
	initPoint();
}
//校验当前选中区域的区域坐标、尺寸
function regionAttrCheck() {
	var x = $("#regionAttrX").val();
	var y = $("#regionAttrY").val();
	var w = $("#regionAttrW").val();
	var h = $("#regionAttrH").val();
	var r = /^[0-9]*[1-9][0-9]*$/;
	if (!r.test(x) && x != "0") {
		x = regionSelectData.left;
	}
	if (!r.test(y) && y != "0") {
		y = regionSelectData.top;
	}
	if (!r.test(w)) {
		w = regionSelectData.width;
	} else if (parseInt(w) > resolutionX) {
		w = resolutionX;
		regionSelectData.width = resolutionX;
	}
	if (!r.test(h)) {
		h = regionSelectData.height;
	} else if (parseInt(h) > resolutionY) {
		h = resolutionY;
		regionSelectData.height = resolutionY;
	}
	$("#regionAttrX").val(parseInt(x));
	$("#regionAttrY").val(parseInt(y));
	$("#regionAttrW").val(parseInt(w));
	$("#regionAttrH").val(parseInt(h));
	regionAttrSet();
	checkRegionSize($("#" + regionSelectData.id)[0], "remove");
}
//将区域属性框内的值同步至节目缓存数据中
function regionAttrSet() {
	var region = $("#" + regionSelectData.id);
	if (setRegionAttr == "regionAttrX") {
		regionSelectData.left = parseInt($("#regionAttrX").val());
		var temp = parseInt(parseInt($("#regionAttrX").val())
				* program.editProp);
		region.css("left", temp + "px");
	} else if (setRegionAttr == "regionAttrY") {
		regionSelectData.top = parseInt($("#regionAttrY").val());
		var temp = parseInt(parseInt($("#regionAttrY").val())
				* program.editProp);
		region.css("top", temp + "px");
	} else if (setRegionAttr == "regionAttrW") {
		regionSelectData.width = parseInt($("#regionAttrW").val());
		var temp = parseInt(parseInt($("#regionAttrW").val())
				* program.editProp);
		region.css("width", temp - 6 + "px");
	} else if (setRegionAttr == "regionAttrH") {
		regionSelectData.height = parseInt($("#regionAttrH").val());
		var temp = parseInt(parseInt($("#regionAttrH").val())
				* program.editProp);
		region.css("height", temp - 6 + "px");
	}
}
//加载并绘制区域素材列表
//function elemManagerLoad(getdata) {
//	getdata.audit = 1;
//	if(getdata.type==0||getdata.type==undefined){
//		getdata.type=3;
//	}
//	getdata.temp = Math.random();
//	$
//	.ajax({
//		url : 'element/queryElement.do',
//		method : "POST",
//		data : getdata,
//		dataType : 'json',
//		success : function(data) {
//			var pagIn = $('#pagInationElem');
////			pagIn.pagination({
////				onSelectPage : function(pageNumber, pageSize) {
////					var elem = $(".elemdivSelect");
////					for (var i = 0; i< elem.length; i++) {
////						var ishave = false;
////						for (var j = 0; j < elemarr.length; j++) {
////							if(elem[i].id == elemarr[j].id){
////								ishave = true;
////								break;
////							}
////						}
////						if(!ishave){
////							elemarr.push(elem[i]);
////						}
////					}
////					var elemdata={
////						"elem_name":$("#searchNameElem").val(),
////						"page":pageNumber,
////						"rows":10,
////						"type":getdata.type
////					};
////					elemManagerLoad(elemdata);
////				}
////			});
//			pagIn.pagination({
//				total : data.total
//			});
//			getdata.rows = data.total;
//			getdata.page = 1;
//			$
//			.ajax({
//				url : 'element/queryElement.do',
//				method : "POST",
//				data : getdata,
//				dataType : 'json',
//				success : function(data) {
//					elemListData = new Array();
//					for (var i = 0; i < data.rows.length; i++) {
//						var lifeStr = data.rows[i].time_length;
//						var life = 10;
//						if (!checkNull(lifeStr)) {
//							var temp = lifeStr.split(":");
//							var life = parseInt(temp[0]) * 3600 + parseInt(temp[1])
//									* 60 + parseInt(temp[2]);
//						}
//						if (data.rows[i].type == 7) {
//							var elempath = data.rows[i].elem_path;
//							life = 3;
//
//						}
//						var standard = 50;
//						var arg;
//						var newWidth;
//						var newHeight;
//						var oldWidth;
//						var oldHeight
//						var resolution = data.rows[i].resolution;
//						if (resolution != "" && resolution != undefined) {
//							oldWidth = parseInt(resolution.split('x')[0]);
//							oldHeight = parseInt(resolution.split('x')[1]);
//							if (oldHeight >= oldWidth) {
//								arg = standard / oldHeight;
//								newHeight = standard;
//								newWidth = oldWidth * arg;
//							} else {
//								arg = standard / oldWidth;
//								newWidth = standard;
//								newHeight = oldHeight * arg;
//							}
//						} else {
//							newWidth = 50;
//							newHeight = 50;
//						}
//						var elemDataTemp = {
//							id : data.rows[i].elem_id,
//							name : data.rows[i].elem_name,
//							resolution : data.rows[i].resolution,
//							type : data.rows[i].type,
//							src : data.rows[i].elem_path,
//							thumbnailUrl : data.rows[i].thumbnailUrl,
//							elemTempId : "",
//							eWidth : newWidth,
//							eHeight : newHeight,
//							life : life
//						};
//						if (data.rows[i].type == 7) {
//							elemDataTemp.originlife = life;
//						}
//						if (checkNull(elemDataTemp.resolution)) {
//							elemDataTemp.resolution = "";
//						}
//						if (elemDataTemp.type == 5) {
//							elemDataTemp.websrc = elemDataTemp.src;
//							elemDataTemp.src = "";
//						}
//						elemListData.push(elemDataTemp);
//					}
//				}
//			});
//			$("#elemListDiv").html("");
////			elemListData = new Array();
//			for (var i = 0; i < data.rows.length; i++) {
//				var lifeStr = data.rows[i].time_length;
//				var life = 10;
//				if (!checkNull(lifeStr)) {
//					var temp = lifeStr.split(":");
//					var life = parseInt(temp[0]) * 3600 + parseInt(temp[1])
//							* 60 + parseInt(temp[2]);
//				}
//				if (data.rows[i].type == 7) {
//					var elempath = data.rows[i].elem_path;
//					life = 3;
//
//				}
//				var standard = 50;
//				var arg;
//				var newWidth;
//				var newHeight;
//				var oldWidth;
//				var oldHeight
//				var resolution = data.rows[i].resolution;
//				if (resolution != "" && resolution != undefined) {
//					oldWidth = parseInt(resolution.split('x')[0]);
//					oldHeight = parseInt(resolution.split('x')[1]);
//					if (oldHeight >= oldWidth) {
//						arg = standard / oldHeight;
//						newHeight = standard;
//						newWidth = oldWidth * arg;
//					} else {
//						arg = standard / oldWidth;
//						newWidth = standard;
//						newHeight = oldHeight * arg;
//					}
//				} else {
//					newWidth = 50;
//					newHeight = 50;
//				}
//				var elemDataTemp = {
//					id : data.rows[i].elem_id,
//					name : data.rows[i].elem_name,
//					resolution : data.rows[i].resolution,
//					type : data.rows[i].type,
//					src : data.rows[i].elem_path,
//					thumbnailUrl : data.rows[i].thumbnailUrl,
//					elemTempId : "",
//					eWidth : newWidth,
//					eHeight : newHeight,
//					life : life
//				};
//				if (data.rows[i].type == 7) {
//					elemDataTemp.originlife = life;
//				}
//				if (checkNull(elemDataTemp.resolution)) {
//					elemDataTemp.resolution = "";
//				}
//				if (elemDataTemp.type == 5) {
//					elemDataTemp.websrc = elemDataTemp.src;
//					elemDataTemp.src = "";
//				}
////				elemListData.push(elemDataTemp);
//				var tempName = elemDataTemp.name;
//				var showName = tempName;
//				if (showName.length > 12) {
//					showName = showName.substring(0, 9) + "..."
//							+ showName.substring(showName.length - 3);
//				}
//				if (showName.length > 6) {
//					showName = showName.substring(0, 6) + "<br>"
//							+ showName.substring(6);
//				}
//				var teUrl = elemDataTemp.thumbnailUrl;
//				if (elemDataTemp.type != 1 && elemDataTemp.type != 2
//						&& elemDataTemp.type != 4 && elemDataTemp.type != 5
//						&& elemDataTemp.type != 6) {
//					teUrl = teUrl.substring(0, 18) + "mini_"
//							+ teUrl.substring(18);
//				}
//				$("#elemListDiv")
//						.append('<div onclick="selectElem(\''
//										+ elemDataTemp.id
//										+ '\')" id="'
//										+ elemDataTemp.id
//										+ '" class="elemdiv elemdivUnSelect easyui-tooltip" title="'
//										+ tempName
//										+ '"><div style="float:left;"><img width="'
//										+ elemDataTemp.eWidth
//										+ '" height="'
//										+ elemDataTemp.eHeight
//										+ '" style="margin:5 '
//										+ (60 - elemDataTemp.eWidth)
//										/ 2
//										+ ';" src="'
//										+ teUrl
//										+ '"  ></div><div style="float:left;width:90px;"><ul style="margin: 10 0 0 5;padding: 0;list-style: none;"><li>'
//										+ showName
//										+ '</li><li style="margin-top:10px;">'
//										+ elemDataTemp.resolution
//										+ '</li></ul></div></div>');
//				for (var j = 0; j < elemarr.length; j++) {
//					if(elemarr[j].id == elemDataTemp.id){
//						$("#" + elemDataTemp.id).addClass("elemdivSelect");
//					}
//				}
//			}
//			
//		},
//		error : function(e) {
//		}
//	});
//	
//}
function elemManagerLoad(data) {
	data.audit = 1;
	if(data.type==0||data.type==undefined){
		data.type=3;
	}
	//解决在IE11下数据不刷新问题
	data.temp = Math.random();
	$
			.ajax({
				url : 'element/queryElement.do',
				method : "POST",
				data : data,
				dataType : 'json',
				success : function(data) {
					var pagIn = $('#pagInationElem');
					pagIn.pagination({
						total : data.total
					});
					$("#elemListDiv").html("");
					elemListData = new Array();
					for (var i = 0; i < data.rows.length; i++) {
						var lifeStr = data.rows[i].time_length;
						var life = 10;
						if (!checkNull(lifeStr)) {
							var temp = lifeStr.split(":");
							var life = parseInt(temp[0]) * 3600
									+ parseInt(temp[1]) * 60
									+ parseInt(temp[2]);
						}
						if(data.rows[i].type == 7){
							var elempath = data.rows[i].elem_path;
							life = 3;
							
						}
						var standard = 50;
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
							newWidth = 50;
							newHeight = 50;
						}
						var elemDataTemp = {
							id : data.rows[i].elem_id,
							name : data.rows[i].elem_name,
							resolution : data.rows[i].resolution,
							type : data.rows[i].type,
							src : data.rows[i].elem_path,
							thumbnailUrl : data.rows[i].thumbnailUrl,
							elemTempId : "",
							eWidth:newWidth,
							eHeight:newHeight,
							life : life
						};
						if(data.rows[i].type == 7){
							elemDataTemp.originlife = life;
						}
						if(checkNull(elemDataTemp.resolution)){
							elemDataTemp.resolution="";
						}
						if(elemDataTemp.type==5){
							elemDataTemp.websrc=elemDataTemp.src;
							elemDataTemp.src="";
						}
						elemListData.push(elemDataTemp);
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
						var teUrl = elemDataTemp.thumbnailUrl;
						if(elemDataTemp.type!=1&&elemDataTemp.type!=2&&elemDataTemp.type!=4&&elemDataTemp.type!=5&&elemDataTemp.type!=6){
							teUrl = teUrl.substring(0,18)+"mini_"+teUrl.substring(18);
						}
						$("#elemListDiv")
								.append(
										'<div onclick="selectElem(\''
												+ elemDataTemp.id
												+ '\')" id="'
												+ elemDataTemp.id
												+ '" class="elemdiv elemdivUnSelect easyui-tooltip" title="'
												+ tempName
												+ '"><div style="float:left;"><img width="'+elemDataTemp.eWidth+'" height="'+elemDataTemp.eHeight+'" style="margin:5 '+(60-elemDataTemp.eWidth)/2+';" src="'
												+ teUrl
												+ '"  ></div><div style="float:left;width:90px;"><ul style="margin: 10 0 0 5;padding: 0;list-style: none;"><li>'
												+ showName
												+ '</li><li style="margin-top:10px;">'
												+ elemDataTemp.resolution
												+ '</li></ul></div></div>');
					}
				},
				error : function(e) {
				}
			});
}
//搜索区域素材
function searchNameElem() {
	var elemName = $("#searchNameElem").val();
	elemName = $.trim(elemName);
	var data = {
		elem_name : elemName,
		page : 1,
		rows : 10
	};
	if (regionSelectData.id.indexOf("region") > -1) {
		if (parseInt(regionSelectData.type) != 0) {
			var tempType = $("#elemTypeSelect").combobox("getValue");
			if (!checkNull(tempType)) {
				data.type = tempType;
			} else {
				data.type = regionSelectData.type;
			}
		}
	} else if (regionSelectData.id.indexOf("plug") > -1) {
		if (parseInt(regionSelectData.type) != 0) {
			data.type = plugElemType[regionSelectData.type];
		}
	}
	//elemManagerLoad(data);
	$('#pagInationElem').pagination('select', 1);
}
/**
 * 提交相册类型设置
 */
function subRegionAttrSet() {
	var styleType = $("#albumStyleType").val();
	regionSelectData.album[0].style = styleType;
	ZENG.msgbox.show("设置成功！", 4, 2000);
	$("#" + regionSelectData.id)
			.html(
					'<img width="100%" height="100%"  src="ftpFile\\plugXmlFile\\img\\album\\album_type_'
							+ styleType + '.jpg">');
	$("#regionAttrSetWin").dialog("close");
}
/**
 * 提交天气预报类型
 */
function subWeatherAttrSet() {
	var weatherStyleType = $("#weatherStyleType").combo('getValue');
	if (checkNull(weatherStyleType)) {
		ZENG.msgbox.show("请先选择天气预报类型", 3, 1000);
		return;
	}
	var weather;
	for (var i = 0; i < weatherList.length; i++) {
		if (weatherStyleType == weatherList[i].style) {
			weather = weatherList[i];
		}
	}
	$("#" + regionSelectData.id).html(
			'<img width="100%" height="100%"  src="' + weather.path + '">');
	if (checkNull(regionSelectData.weather)) {
		regionSelectData.weather = new Array();
	}
	regionSelectData.weather[0] = weather;
	$("#regionAttrW").val(weather.width);
	setRegionAttr = "regionAttrW";
	regionAttrCheck();
	$("#regionAttrH").val(weather.height);
	setRegionAttr = "regionAttrH";
	regionAttrCheck();
	setRegionAttr = "";
	ZENG.msgbox.show("设置成功！", 4, 800);
	$("#weatherAttrSetWin").dialog("close");
	//类型大于100的可以改变尺寸
	if(parseInt(weather.style)>100){
		$("#" + regionSelectData.id).resizable({
		handles:'e,w',
		maxWidth : realSceneW>(parseInt(weather.width)*2*program.editProp)?(parseInt(weather.width)*2*program.editProp):realSceneW,
		maxHeight : realSceneH>(parseInt(weather.height)*2*program.editProp)?(parseInt(weather.height)*2*program.editProp):realSceneH,
		minWidth : parseInt(weather.width)*0.4*program.editProp,
		disabled:false,
		minHeight : 20,
		onStopResize : function(e) {
			var width = parseInt(this.offsetWidth);
			var height = (width*parseInt(regionSelectData.weather[0].height))/parseInt(regionSelectData.weather[0].width);
			$("#" + regionSelectData.id).css("height",height-6+"px");
			checkRegionSize(this, "resize");
			removelock = false;
			checkRegionSize(this, "remove");
			updatePercent(regionSelectData.id);
		},
		onResize : function(e) {
			removelock = true;
			var width = parseInt(this.offsetWidth);
			var height = parseInt(this.offsetHeight);
			regionSelectData.width = parseInt(width / program.editProp);
			regionSelectData.height = parseInt(height / program.editProp);
			showRegionAttr();
		}
	});
	}else{
		$("#" + regionSelectData.id).resizable({
			disabled:true
		})
	}
}
/**
 * 提交倒计时类型
 */
function subCountAttrSet() {
	var counttext = $("#countAttr_text").val();
	var counttime = $("#countAttr_time").combo("getText");
	if(checkNull(counttext)){
		ZENG.msgbox.show("请填写显示名称", 2, 1000);
		return;
	}else if(checkNull(counttime)){
		ZENG.msgbox.show("请填写到期时间", 2, 1000);
		return;
	}
	var countStyleType = $("#countStyleType").combo('getValue');
	if (checkNull(countStyleType)) {
		ZENG.msgbox.show("请先选择倒计时类型", 2, 1000);
		return;
	}
	var nowDate = new Date();
	counttime = counttime.replace("-","/").replace("-","/");
	if(new Date(counttime) <= nowDate){
		ZENG.msgbox.show("倒计时时间必须大于当前时间", 2, 1000);
		return;
	}
	var count;
	for (var i = 0; i < countList.length; i++) {
		if (countStyleType == countList[i].style) {
			count = copyjson(countList[i]);
			count.text = counttext;
			count.endtime = counttime;
		}
	}
	$("#" + regionSelectData.id).html(
			'<img width="100%" height="100%"  src="' + count.path + '">');
	if (checkNull(regionSelectData.count)) {
		regionSelectData.count = new Array();
	}
	regionSelectData.count[0] = count;
	$("#regionAttrW").val(count.width);
	setRegionAttr = "regionAttrW";
	regionAttrCheck();
	$("#regionAttrH").val(count.height);
	setRegionAttr = "regionAttrH";
	regionAttrCheck();
	setRegionAttr = "";
	ZENG.msgbox.show("设置成功！", 4, 800);
	$("#countAttrSetWin").dialog("close");
}
/**
 * 提交时钟类型
 */
function subTimeAttrSet() {
	var timeStyleType = $("#timeStyleType").combo('getValue');
	if (checkNull(timeStyleType)) {
		ZENG.msgbox.show("请先选择时钟类型", 3, 1000);
		return;
	}
	var time;
	for (var i = 0; i < timeList.length; i++) {
		if (timeStyleType == timeList[i].clockstyle) {
			time = timeList[i];
		}
	}
	$("#" + regionSelectData.id).html(
			'<img width="100%" height="100%"  src="' + time.path + '">');
	if (checkNull(regionSelectData.timepiece)) {
		regionSelectData.timepiece = new Array();
	}
	regionSelectData.timepiece[0] = time;
	$("#regionAttrW").val(time.width);
	setRegionAttr = "regionAttrW";
	regionAttrCheck();
	$("#regionAttrH").val(time.height);
	setRegionAttr = "regionAttrH";
	regionAttrCheck();
	setRegionAttr = "";
	ZENG.msgbox.show("设置成功！", 4, 800);
	$("#timeAttrSetWin").dialog("close");
}
/**
 * 屏保属性设置（跳转时长）
 */
function showScreenSceneMenu() {
	$("#touchDiv").hide();
	$("#radioDiv").hide();
	$("#templateDiv").hide();
	var event = event ? event : window.event;
	var left = event.x;
	var top = event.y;
	if (navigator.appName.indexOf("Microsoft Internet Explorer") != -1
			&& document.all) {
		left = event.clientX;
		top = event.clientY;
	}
	if ("ActiveXObject" in window){
		left = event.clientX;
		top = event.clientY;
	}
	sceneSelect("screensaver");
	changeSlelectRegion("screenregion");
	$('#screenSceneMenu').menu('show', {
		left : left,
		top : top
	});
	return false;
}
/**
 * 场景属性设置
 */
function sceneRenameWin() {
	$("#sceneName_input").textbox("setValue",sceneSelectData.sceneName);
	//普通节目单
	if(program.istouch=="false"||!program.istouch){
		$("#scenelifediv").show();
		//$("#screensaverdiv").hide();
		var life = 0;
		var maxlife = sceneSelectData.life;
		if(program.staticscene != undefined && program.staticscene != "null"){
			var psr = program.staticscene[0].region;
			for (var i = 0; i < psr.length; i++) {
				maxlife = psr[i].life > maxlife ? psr[i].life:maxlife;
			}
		}
		if(maxlife < 10){
			var scenearr = program.scene;
			for (var i = 0; i < scenearr.length; i++) {
				var sceneplugarr = scenearr[i].plug;
				if(sceneplugarr != undefined){
					for (var j = 0; j < sceneplugarr.length; j++) {
						var plugweather = sceneplugarr[j].weather;
						var plugtimepiece = sceneplugarr[j].timepiece;
						var plugcount = sceneplugarr[j].count;
						if(plugweather != undefined && sceneplugarr[j].weather.length > 0 && scenearr[i].life == 0){
							maxlife = 10;
							break;
						}else if(plugtimepiece != undefined && sceneplugarr[j].timepiece.length > 0 && scenearr[i].life == 0){
							maxlife = 10;
							break;
						}else if(plugcount != undefined && sceneplugarr[j].count.length > 0 && scenearr[i].life == 0){
							maxlife = 10;
							break;
						}
					}
				}
			}
		}
		$("#scene_life").numberbox("setValue",maxlife);
	}else{
		$("#scenelifediv").hide();
		//$("#screensaverdiv").show();
		var checked = sceneSelectData.id==program.saverid?true:false;
		//$("#screensaver").attr("checked",checked);
		$("#saverlife").numberbox("setValue",program.saverlife);
		if(checked){
			$("#saverlifediv").show();
		}else{
			$("#saverlifediv").hide();
		}
	}
	$("#sceneRenameWin").dialog('open');
}
//提交场景设置
function subSceneSet() {
	var sceneName = $("#sceneName_input").textbox("getValue");
	var scenelife = $("#scene_life").numberbox("getValue");
	//var issaver = $("#screensaver").attr("checked");
	if (checkNull(sceneName)) {
		ZENG.msgbox.show("场景名称不能为空!", 3, 2000);
		return;
	}
	if (null != getSceneByName(sceneName)&&sceneName!=sceneSelectData.sceneName) {
		ZENG.msgbox.show("场景名称已存在!", 3, 800);
		$("#newSceneName").focus();
		return;
	}
	sceneSelectData.life = scenelife;
	sceneSelectData.sceneName = sceneName;
	var stylestr = "cursor:pointer;";
//	if(issaver){
//		var saverlife = $("#saverlife").numberbox("getValue");
//		//将原来的屏保样式改变
//		if(!checkNull(program.saverid)){
//			var tempScene = getSceneById(program.saverid);
//			var text = tempScene.sceneName.length > 4?(tempScene.sceneName.substring(0, 4) + "..."):tempScene.sceneName;
//			var str = "<div onclick='sceneSelect(\"" + tempScene.id
//				+ "\")'  class='tooltipDiv' style='"+stylestr+" ' title = '"
//				+ tempScene.sceneName + "'>" + text + "</div>";
//			$("#" + program.saverid).html(str);
//		}
//		program.saverid = sceneSelectData.id;
//		program.saverlife = saverlife;
//		sceneName = "(屏保)"+sceneName;
//		stylestr = stylestr+"text-decoration:underline;"
//		$("#selectable li div").css("text-decoration","none");
//	}
	if(scenelife==""){
		scenelife = 0;
	}
	var sceneText = sceneSelectData.sceneName;
	if (sceneSelectData.sceneName.length > 4) {
		sceneText = sceneText.substring(0, 4) + "...";
	}
	var tempStr = "<div onclick='sceneSelect(\"" + sceneSelectData.id
			+ "\")'  class='tooltipDiv' style='"+stylestr+" ' title = '"
			+ sceneName + "'>" + sceneText + "</div>";
	$("#" + sceneSelectData.id).html(tempStr);
	$("#sceneRenameWin").dialog('close');
}
function subScreenSceneSet(){
	var screensaverlife = $("#screensaverlife").numberbox("getValue");
	screensaverlife == "" ? program.saverlife = 1 : program.saverlife = screensaverlife;
	$("#screenSceneWin").dialog('close');
}
/**
 * 拷贝场景窗口弹出
 */
function copyScene() {
	var sceneTempName = "场景" + sceneNum;
	$("#copyandnewscene").attr("checked", "checked");
	$("#sceneName_copy").val(sceneTempName);
	var tempData = new Array();
	var j = 0;
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id != "screensaver"){
			tempData[j++] = {
				id : program.scene[i].id,
				name : program.scene[i].sceneName
			};
		}
	}
	$("#copySceneSelect").combobox({
		valueField : 'id',
		textField : 'name',
		editable : false,
		value : "",
		data : tempData
	});
	$("#copySceneWin").dialog('open');
}
/**
 * 提交拷贝场景
 */
function subCopyScene() {
	var copyAttr = document.getElementsByName("copyAttr");
	var attr;
	for (var i = 0; i < copyAttr.length; i++) {
		if (copyAttr[i].checked) {
			attr = copyAttr[i].value;
		}
	}
	if (attr == "copy") {
		var name = $("#sceneName_copy").val();
		if (checkNull(name)) {
			ZENG.msgbox.show("场景名称不能为空!", 3, 2000);
			return;
		}
		if (null != getSceneByName(name)) {
			ZENG.msgbox.show("场景名称已存在!", 3, 1000);
			$("#newSceneName").focus();
			return;
		}
		var newSceneJson = {
			life : 0,
			ismain : "false",
			region : null,
			button : null,
			plug : null,
			sceneName : name,
			id : "scene" + sceneNum,
			backimg : sceneBackAll,
			backcolor : "",
			backimgName : ""
		};
		if ($("#copyBackimg_box")[0].checked) {
			newSceneJson.backcolor = sceneSelectData.backcolor;
			newSceneJson.backimg = sceneSelectData.backimg;
			newSceneJson.backimgName = sceneSelectData.backimgName;
		}
		if ($("#copySize_box")[0].checked) {
			var tempRegions = new Array();
			var tempButtons = new Array();
			var tempPlugs = new Array();
			if (!checkNull(sceneSelectData.region)
					&& sceneSelectData.region.length > 0) {
				for (var i = 0; i < sceneSelectData.region.length; i++) {
					var temp = {
						element : new Array(),
						id : "region" + regionCount,
						zIndex : lastZIndex,
						name : typeName[sceneSelectData.region[i].type],
						type : sceneSelectData.region[i].type,
						top : sceneSelectData.region[i].top,
						left : sceneSelectData.region[i].left,
						width : sceneSelectData.region[i].width,
						height : sceneSelectData.region[i].height
					};
					tempRegions.push(temp);
					regionCount++;
					lastZIndex++;
				}
			}
			newSceneJson.region = tempRegions;
			if (!checkNull(sceneSelectData.button)
					&& sceneSelectData.button.length > 0) {
				for (var i = 0; i < sceneSelectData.button.length; i++) {
					var temp = {
						id : "button" + buttonCount,
						top : sceneSelectData.button[i].top,
						left : sceneSelectData.button[i].left,
						width : sceneSelectData.button[i].width,
						height : sceneSelectData.button[i].height,
						zIndex : lastZIndex + 4000,
						name : sceneSelectData.button[i].name,
						backimg : sceneSelectData.button[i].backimg,
						value : sceneSelectData.button[i].value,
						fontfamily : sceneSelectData.button[i].fontfamily,
						fontcolor : sceneSelectData.button[i].fontcolor,
						fontsize : sceneSelectData.button[i].fontsize,
						backcolor : sceneSelectData.button[i].backcolor,
						style : sceneSelectData.button[i].style,
						sceneid : sceneSelectData.button[i].sceneid,
						type : sceneSelectData.button[i].type,
						selectimg : sceneSelectData.button[i].selectimg,
						backimgName : sceneSelectData.button[i].backimgName,
						bubackimgName : sceneSelectData.button[i].bubackimgName,
						buselectimgName : sceneSelectData.button[i].buselectimgName
					};
					tempButtons.push(temp);
					buttonCount++;
					lastZIndex++;
				}
			}
			newSceneJson.button = tempButtons;
			if (!checkNull(sceneSelectData.plug)
					&& sceneSelectData.plug.length > 0) {
				for (var i = 0; i < sceneSelectData.plug.length; i++) {
					var temp = initPlugData(sceneSelectData.plug[i]);
					tempPlugs.push(temp);
				}
			}
			newSceneJson.plug = tempPlugs;
		}
		newScene(newSceneJson);
		program.scene.push(newSceneJson);
		sceneSelect(newSceneJson.id);
		sceneSelectData = newSceneJson;
		sceneNum++;
		$("#copySceneWin").dialog('close');
	} else if (attr == "replace") {
		var replaceSceneId = $("#copySceneSelect").combo("getValue");
		if (checkNull(replaceSceneId)) {
			ZENG.msgbox.show("替换场景不能为空", 3, 1000);
			return;
		}
		if (replaceSceneId == sceneSelectData.id) {
			ZENG.msgbox.show("替换场景不能选自己", 3, 1000);
			return;
		}
		var temp = getSceneById(replaceSceneId);
		if ($("#copyBackimg_box")[0].checked) {
			temp.backcolor = sceneSelectData.backcolor;
			temp.backimg = sceneSelectData.backimg;
			temp.backimgName = sceneSelectData.backimgName;
		}
		if ($("#copySize_box")[0].checked) {
			var tempRegions = new Array();
			var tempButtons = new Array();
			var tempPlugs = new Array();
			if (!checkNull(sceneSelectData.region)
					&& sceneSelectData.region.length > 0) {
				for (var i = 0; i < sceneSelectData.region.length; i++) {
					var tempData = {
						element : new Array(),
						id : "region" + regionCount,
						zIndex : lastZIndex,
						name : typeName[sceneSelectData.region[i].type],
						type : sceneSelectData.region[i].type,
						top : sceneSelectData.region[i].top,
						left : sceneSelectData.region[i].left,
						width : sceneSelectData.region[i].width,
						height : sceneSelectData.region[i].height
					};
					tempRegions.push(tempData);
					regionCount++;
					lastZIndex++;
				}
			}
			temp.region = tempRegions;
			if (!checkNull(sceneSelectData.button)
					&& sceneSelectData.button.length > 0) {
				for (var i = 0; i < sceneSelectData.button.length; i++) {
					var tempData = {
						id : "button" + buttonCount,
						top : sceneSelectData.button[i].top,
						left : sceneSelectData.button[i].left,
						width : sceneSelectData.button[i].width,
						height : sceneSelectData.button[i].height,
						zIndex : lastZIndex + 4000,
						name : sceneSelectData.button[i].name,
						backimg : sceneSelectData.button[i].backimg,
						value : sceneSelectData.button[i].value,
						fontfamily : sceneSelectData.button[i].fontfamily,
						fontcolor : sceneSelectData.button[i].fontcolor,
						fontsize : sceneSelectData.button[i].fontsize,
						backcolor : sceneSelectData.button[i].backcolor,
						style : sceneSelectData.button[i].style,
						sceneid : sceneSelectData.button[i].sceneid,
						type : sceneSelectData.button[i].type,
						selectimg : sceneSelectData.button[i].selectimg,
						bubackimgName : sceneSelectData.button[i].bubackimgName,
						buselectimgName : sceneSelectData.button[i].buselectimgName
						
					};
					tempButtons.push(tempData);
					buttonCount++;
					lastZIndex++;
				}
			}
			temp.button = tempButtons;
			if (!checkNull(sceneSelectData.plug)
					&& sceneSelectData.plug.length > 0) {
				for (var i = 0; i < sceneSelectData.plug.length; i++) {
					var tempData = initPlugData(sceneSelectData.plug[i]);
					tempPlugs.push(tempData);
				}
			}
			temp.plug = tempPlugs;
		}
		sceneSelect(temp.id);
		sceneSelectData = temp;
		$("#copySceneWin").dialog('close');
	}
}
/**
 * 提交自定义分辨率
 */
function subNewResolution() {
	var width = $("#newResolution_width").val();
	var height = $("#newResolution_height").val();
	if (checkNull(width) || checkNull(height)) {
		ZENG.msgbox.show("请将分辨率填写完整!", 3, 2000);
		return;
	}
	resolutionX = parseInt(width);
	resolutionY = parseInt(height);
	setResolutionText(width, height);
	$("#newResolutionWin").dialog("close");

}
//设置节目分辨率输入框
function setResolutionText(width, height) {
	var temp = width + "*" + height;
	var temphtml = $("#resolution").html();
	if (temphtml.indexOf(temp) == -1) {
		$("#resolution").append("<option>" + temp + "</option>");
	}
	$("#resolution").val(temp);
	resolutionChange();
}
//检查节目名称
function checkProgramName() {
	var name = $("#programName").val();
	$.ajax({
		url : 'program/checkProgramName.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"id" : program.id,
			"name" : name
		},
		success : function(data) {
			var succ = data.success;
			if (succ) {
				ZENG.msgbox.show("该节目名已经存在！", 3, 2000);
				$("#programName").focus();
			}
		}
	});
}
// 深度复制json对象
function copyjson(json) {
	if (typeof json == 'number' || typeof json == 'string'
			|| typeof json == 'boolean') {
		return json;
	} else if (typeof json == 'object') {
		if (json instanceof Array) {
			var newArr = [], i, len = json.length;
			for (i = 0; i < len; i++) {
				newArr[i] = copyjson(json[i]);
			}
			return newArr;
		} else {
			var newObj = {};
			for ( var name in json) {
				newObj[name] = copyjson(json[name]);
			}
			return newObj;
		}
	}
}
/**
 * 查询天气预报样式列表
 */
function queryWeatherList() {
	if (null == weatherList) {
		$.ajax({
			url : 'weather/queryWeatherList.do',
			type : 'post',
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success : function(data) {
				var tempList = data.weatherList;
				weatherList = new Array();
				weatherImgPath = data.weatherImgPath;
				for (var i = 0; i < tempList.length; i++) {
					weatherList.push(tempList[i].weather);
					weatherList[i].path = weatherImgPath + weatherList[i].path;
				}
				$('#weatherStyleType').combobox('loadData', weatherList);
				if (checkNull(regionSelectData.weather)) {
					$("#weatherStyleType").combobox('clear');
					$("#weatherStyleType").combo('setText', "请选择");
				} else {
					$("#weatherStyleType").combobox('setValue',regionSelectData.weather[0].style);
				}
				showWeatherImg();
			}
		});
	} else {
		if (checkNull(regionSelectData.weather)) {
			$("#weatherStyleType").combobox('clear');
			$("#weatherStyleType").combo('setText', "请选择");
		} else {
			$("#weatherStyleType").combobox("select",regionSelectData.weather[0].style);
			// $("#weatherStyleType").combo('setValue',regionSelectData.weather[0].style);
		}
		showWeatherImg();
	}
}
/**
 * 查询时钟样式列表
 */
function queryTimeList() {
	if (null == timeList) {
		$.ajax({
			url : 'time/queryTimeList.do',
			type : 'post',
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success : function(data) {
				var tempList = data.timeList;
				timeList = new Array();
				timeImgPath = data.timeImgPath;
				for (var i = 0; i < tempList.length; i++) {
					timeList.push(tempList[i].timepiece);
					timeList[i].path = timeImgPath + timeList[i].path;
				}
				$('#timeStyleType').combobox('loadData', timeList);
				if (checkNull(regionSelectData.timepiece)) {
					$("#timeStyleType").combobox('clear');
					$("#timeStyleType").combo('setText', "请选择");
				} else {
					$("#timeStyleType").combobox('setValue',regionSelectData.timepiece[0].clockstyle);
				}
				showTimeImg();
			}
		});
	} else {
		if (checkNull(regionSelectData.timepiece)) {
			$("#timeStyleType").combobox('clear');
			$("#timeStyleType").combo('setText', "请选择");
		} else {
			$("#timeStyleType").combobox("select",regionSelectData.timepiece[0].clockstyle);
			// $("#weatherStyleType").combo('setValue',regionSelectData.weather[0].style);
		}
		showTimeImg();
	}
}
//查询倒计时插件列表
function queryCountList() {
	if (null == countList) {
		$.ajax({
			url : 'count/queryCountList.do',
			type : 'post',
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success : function(data) {
				var tempList = data.countList;
				countList = new Array();
				countImgPath = data.countImgPath;
				for (var i = 0; i < tempList.length; i++) {
					countList.push(tempList[i].count);
					countList[i].path = countImgPath + countList[i].path;
					if(regionSelectData.count.length > 0){
						countList[i].height = regionSelectData.height;
						countList[i].width = regionSelectData.width;
					}
					
				}
				$('#countStyleType').combobox('loadData', countList);
				if (checkNull(regionSelectData.count)) {
					$("#countAttr_text").textbox("setValue","");
					$("#countAttr_time").datetimebox("setValue",getNowDate());
					$("#countStyleType").combobox('clear');
					$("#countStyleType").combo('setText', "请选择");
				} else {
					$("#countStyleType").combobox('setValue',regionSelectData.count[0].style);
				}
				showCountImg();
			}
		});
	} else {
		if (checkNull(regionSelectData.count)) {
			$("#countAttr_text").textbox("setValue","");
			$("#countAttr_time").datetimebox("setValue",getNowDate());
			$("#countStyleType").combobox('clear');
			$("#countStyleType").combo('setText', "请选择");
		} else {
			if(regionSelectData.count.length > 0){
				countList[0].height = regionSelectData.height;
				countList[0].width = regionSelectData.width;
			}
			$("#countStyleType").combobox("select",regionSelectData.count[0].style);
		}
		showCountImg();
	}
}
function getNowDate(){
	var date = new Date();
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	var o = date.getHours();
	var min = date.getMinutes();
	var s = date.getSeconds();
	if(o<10){
		o = '0'+o;
	}
	if(m<10){
		m = '0'+m;
	}
	if(d<10){
		d = '0'+d;
	}
	if(min<10){
		min = '0'+min;
	}
	if(s<10){
		s = '0'+s;
	}
	return y+'-'+m+'-'+d+' '+'00:00:00';
        
}
/**
 * 显示所选天气预报的效果图
 */
function showWeatherImg() {
	var style = $("#weatherStyleType").combo('getValue');
	if (checkNull(style)) {
		$("#weatherImg").attr("src", "");
		$("#weatherImg").hide();
	}
	for (var i = 0; i < weatherList.length; i++) {
		if (style == weatherList[i].style) {
			$("#weatherImg").attr("src", weatherList[i].path);
			$("#weatherImg").show();
			break;
		}
	}
}
/**
 * 显示所选时钟的效果图
 */
function showTimeImg() {
	var style = $("#timeStyleType").combo('getValue');
	if (checkNull(style)) {
		$("#timeImg").attr("src", "");
		$("#timeImg").hide();
	}
	for (var i = 0; i < timeList.length; i++) {
		if (style == timeList[i].clockstyle) {
			$("#timeImg").attr("src", timeList[i].path);
			$("#timeImg").show();
			break;
		}
	}
}
/**
 * 显示所选倒计时的效果图
 */
function showCountImg() {
	var style = $("#countStyleType").combo('getValue');
	if (checkNull(style)) {
		$("#countImg").attr("src", "");
		$("#countImg").hide();
	}
	for (var i = 0; i < countList.length; i++) {
		if (style == countList[i].style) {
			$("#countImg").attr("src", countList[i].path);
			$("#countImg").show();
			break;
		}
	}
}
/**
 * 显示所选互动相册的效果图
 */
function showAlbumImg() {
	var style = $("#albumStyleType").val();
	if (checkNull(style)) {
		$("#albumBackimg").attr("src", "");
	}
	$("#albumBackimg").attr("src",
			"ftpFile\\plugXmlFile\\img\\album\\album_type_" + style + ".jpg");
}
/**
 * 显示所选按钮样式的效果图
 */
function showButtonImg(newValue) {
	if (checkNull(buttonStyleList)) {
		return;
	}
	for (var i = 0; i < buttonStyleList.length; i++) {
		if (newValue == buttonStyleList[i].imgstyle) {
			if (!checkNull(buttonStyleList[i].backimg)) {
				$("#buttonStyleImg").attr("src",
						buttonStylePath + buttonStyleList[i].backimg);
			} else {
				$("#buttonStyleImg").attr("src", "")
			}
		}
	}
}
function selectButtonStyle() {
	// var style = $("#buttonStyleType").combobox("getValue");
	showButtonImg(regionSelectData.imgstyle);
}
/**
 * 查询按钮样式列表
 */
function queryButtonStyle() {
	if (null == buttonStyleList) {
		$.ajax({
			url : 'button/buttonStyleList.do',
			type : 'post',
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success : function(data) {
				buttonStyleList = data.buttonStyle.buttons.button;
				buttonStylePath = data.buttonStylePath;
				$("#buttonStyleSelect").combobox({
					valueField : 'imgstyle',
					textField : 'name',
					editable : false,
					value : regionSelectData.imgstyle,
					data : buttonStyleList,
					onChange : function(newValue, oldValue) {
						showButtonImg(newValue);
					}


				});
				showButtonImg(regionSelectData.imgstyle);
			}
		});
	}
}

function initPoint() {
	pointX = new Array();
	pointY = new Array();
	addPoint(0, 0);
	addPoint(1920, 1080);
}
function addPoint(x, y) {
	var start = x - magnet;
	var stop = x + magnet;
	for (var i = start; i < stop; i++) {
		if (i < 0 || i > resolutionX)
			continue;
		pointX[i] = x;
	}
	start = y - magnet;
	stop = y + magnet;
	for (var i = start; i < stop; i++) {
		if (i < 0 || i > resolutionY)
			continue;
		pointY[i] = y;
	}
}
function magnetX(x) {
	if (undefined != pointX[x]) {
		return pointX[x];
	}
	return x;
}
function magnetY(y) {
	if (undefined != pointY[y]) {
		return pointY[y];
	}
	return y;
}
function buttonStyleChange(value) {
	if (value == 0) {
		$("#buttonStyle2").css("display", "none");
	} else if (value == 1) {
		$("#buttonStyle2").css("display", "none");

	} else if (value == 2) {
		$("#buttonStyle2").css("display", "inline");
	}
}
function initButtonStyle() {
	$("#buttonManagerWin").dialog('open');
	// 初始化场景选择
	var tempData = new Array();
	var screenindex = -1;
	for (var i = 0; i < program.scene.length; i++) {
			tempData[i] = {
				id : program.scene[i].id,
				name : program.scene[i].sceneName
			};
		if(program.scene[i].id == "screensaver"){
			screenindex = i;
		}
	}
	if(screenindex > 0)
		tempData.splice(screenindex,1);
	var defaultValue = "";
	if (regionSelectData.sceneid != null && regionSelectData.sceneid != "") {
		var temp = getSceneById(regionSelectData.sceneid);
		if (temp != null)
			defaultValue = regionSelectData.sceneid;
	}
	$("#jumpSceneSelect").combobox({
		valueField : 'id',
		textField : 'name',
		editable : false,
		value : defaultValue,
		data : tempData
	});
	$("#buttonStyleSelect").combobox({
		value : regionSelectData.imgstyle
	});
	// 按钮样式初始化
	var buttonStyles = document.getElementsByName("buttonStyle");
	for (var i = 0; i < buttonStyles.length; i++) {
		if (buttonStyles[i].value == regionSelectData.style) {
			buttonStyles[i].checked = true;
		}
	}
	$("#buttonText").val(regionSelectData.value);
	$("#buttonfontcolor_select").colorpicker({
		fillcolor : true,
		success : function(o, color) {
			$("#textarea").css("background-color", color);
			$("#textareacolor").val(color);
		}
	});
	buttonStyleChange(regionSelectData.style);
	//queryButtonStyle();
}
//已选素材属性设置
function rElemSet() {
	$("#elemPlayAttrWin").dialog("open");
	//如果是图片则有图片切换效果设置
	if(regionSelectData.type==3){
		$("#piceffectdiv").show();
		$("#piceffect").combobox("setValue",regionSelectData.piceffect);
	}else{
		$("#piceffectdiv").hide();
	}
	$("#elemAttrSet").numberbox("setValue", regionSelectData.elemlife);
}
//点击 按钮组（多媒体元素和触摸互动元素的弹出框） 时触发
function clickgroup(type) {
	if (type == "radio") {
		document.getElementById("radioGroup").style.zIndex = 8667;
		document.getElementById("touchGroup").style.zIndex = 8665;
	} else if (type == "touch") {
		document.getElementById("touchGroup").style.zIndex = 8667;
		document.getElementById("radioGroup").style.zIndex = 8665;
	}
}
//节目单循环属性高级设置
function highset() {
	index = 1;                      //高级设置
	$('#lowgradeview').hide();
	$('#highgradegrid').show();
	$("#highlevelButton").css("display","none");
	$("#lowlevelButton").css("display","inline");
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	$('#highgradegridtable').datagrid("resize",{  
		height:"185px",
		width:"285px"
	});
	var day_startTime = $("#day_startTime").val();
	var day_endTime = $("#day_endTime").val();
	var daysEl = document.getElementsByName("days");
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
	if(rowcount.length == 0){
		$('#highgradegridtable').datagrid('insertRow',{
			row: {
				starttime: day_startTime,
				endtime: day_endTime,
				circleweek: days
			}
		});
	}else{
		$('#highgradegridtable').datagrid('updateRow',{
		index: 0,
		row: {
				starttime:  day_startTime,
				endtime: day_endTime,
				circleweek: days
			}
		});
	}
}
//节目单循环属性简易设置
function lowset() {
	index = 0;                       //简易设置
	$('#highgradegrid').hide();
	$('#lowgradeview').show();
	$("#lowlevelButton").css("display","none");
	$("#highlevelButton").css("display","inline");
	var rowcount = $('#highgradegridtable').datagrid('getRows');
	var temp = rowcount.length; 
	if(temp > 0) {
		$("#day_startTime").timespinner('setValue', rowcount[0].starttime); 
		$("#day_endTime").timespinner('setValue', rowcount[0].endtime); 
		var daysEl = document.getElementsByName("days");
		var daystr = rowcount[0].circleweek.split(",");
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
}
//新增循环
function addloop() {
	$('#addloopWin').window('open');
	$("#loop_days").timespinner('setValue', "00:00:00"); 
	$("#loop_daye").timespinner('setValue', "23:59:59"); 
	var daysEl = document.getElementsByName("loopdays");
	for(var i=0;i<7;i++){
		daysEl[i].checked = true;
	}
}
//编辑循环
function editloop() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	if(rows.length != 1) {
		ZENG.msgbox.show("请选择一条数据", 3, 2000);
		return;
	}	
	$('#editloopWin').window('open');
	$("#edloop_days").timespinner('setValue', rows[0].starttime); 
	$("#edloop_daye").timespinner('setValue', rows[0].endtime); 
	var daysEl = document.getElementsByName("edloopdays");
	var daystr = rows[0].circleweek.split(",");
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
//删除循环
function delloop() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	if(rows.length < 1) {
		ZENG.msgbox.show("请选择要删除的数据", 3, 2000);
		return;
	}
	for ( var i= 0; i < rows.length; i++) {
		var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', rows[i]);
    	$('#highgradegridtable').datagrid('deleteRow', rowIndex);  
	}
}
//保存循环
function loopsave() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	var loop_days = $("#loop_days").val();
	var loop_daye = $("#loop_daye").val();
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
	if (checkNull(days)) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	var tempe = loop_daye.split(":");
	var temps = loop_days.split(":");
	var st = temps[0]*3600 + temps[1]*60 + temps[2];
	var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
	if (parseInt(st) > parseInt(se)){
		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
		return;
	}
	$('#highgradegridtable').datagrid('insertRow',{
		row: {
			starttime: loop_days,
			endtime: loop_daye,
			circleweek: days
		}
	});
	$('#addloopWin').window('close');
}
//确定循环设置
function edloopsave() {
	var rows = $("#highgradegridtable").datagrid("getSelections");
	var rowIndex = $('#highgradegridtable').datagrid('getRowIndex', rows[0]);
	var edloop_days = $("#edloop_days").val();
	var edloop_daye = $("#edloop_daye").val();
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
	if (checkNull(days)) {
		ZENG.msgbox.show("周循环不能为空!", 3, 2000);
		return;
	}
	var tempe = edloop_daye.split(":");
	var temps = edloop_days.split(":");
	var st = temps[0]*3600 + temps[1]*60 + temps[2];
	var se = tempe[0]*3600 + tempe[1]*60 + tempe[2];
	if (parseInt(st) > parseInt(se)){
		ZENG.msgbox.show("开始时间必须早于结束时间!", 3, 2000);
		return;
	}
	$('#highgradegridtable').datagrid('updateRow',{
	index: rowIndex,
	row: {
			starttime: edloop_days,
			endtime: edloop_daye,
			circleweek: days
		}
	});
	$('#editloopWin').window('close');
}
/**
 * 当保存或发布节目时重新排序场景顺序，以页面展示为准.并且将第一个场景赋值为主场景
 */
function sortscene(){
	var scenes = $("#selectable li");
	var tempscenes = new Array();
	for(var i=0;i<scenes.length;i++){
		var tempId = scenes[i].id;
		 for(var j=0;j<program.scene.length;j++){
			 if(tempId==program.scene[j].id){
			 	program.scene[j].ismain = "false";
				 tempscenes.push(program.scene[j]);
				 break;
			 }
		 }
	}
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id == "screensaver"){
			tempscenes.push(program.scene[i]);
			break;
		}
	}
	program.scene = tempscenes;
	if(program.scene[0].id != "screensaver")
		program.scene[0].ismain = "true";
}	
//提交网页区域属性设置
function subhtmlattr(){
	var checked = $("#showtoolbarbox").attr("checked");
	regionSelectData.showtoolbar = checked=="checked"?true:false;
	closeWin('htmlattrWin');
	
}
//隐藏或显示屏保跳转时长设置
function showorhidessd(){
	var checked = $("#screensaver").attr("checked");
	if(checked){
		$("#saverlifediv").show();
	}else{
		$("#saverlifediv").hide();
	}
}
/**
 * 将该选中区域设置为全局静态
 */
function setRegionToStatic(){
	deleteSRegionData();
	if(checkNull(program.staticscene)){
		program.staticscene = new Array();
		program.staticscene[0] = {
			backimg:"",
			region:null,
			button:null,
			plug:null,
			backimgName:""
		}
	}
	if(regionSelectData.id.indexOf("region") > -1){
		if(checkNull(program.staticscene[0].region)){
			program.staticscene[0].region = new Array();
		}
		program.staticscene[0].region.push(regionSelectData);
	}
	if(regionSelectData.id.indexOf("button") > -1){
		if(checkNull(program.staticscene[0].button)){
			program.staticscene[0].button = new Array();
		}
		program.staticscene[0].button.push(regionSelectData);
	}
	if(regionSelectData.id.indexOf("plug") > -1){
		if(checkNull(program.staticscene[0].plug)){
			program.staticscene[0].plug = new Array();
		}
		program.staticscene[0].plug.push(regionSelectData);
	}
	regionSelectData.isstatic = "true";
	
}
/**
 * 将该选中区域设置为非全局静态
 */
function setRegionToUnstatic(){
	deleteStaticRegionData();
	if(regionSelectData.id.indexOf("region") > -1){
		if(checkNull(sceneSelectData.region)){
			sceneSelectData.region = new Array();
		}
		sceneSelectData.region.push(regionSelectData);
	}
	if(regionSelectData.id.indexOf("button") > -1){
		if(checkNull(sceneSelectData.button)){
			sceneSelectData.button = new Array();
		}
		sceneSelectData.button.push(regionSelectData);
	}
	if(regionSelectData.id.indexOf("plug") > -1){
		if(checkNull(sceneSelectData.plug)){
			sceneSelectData.plug = new Array();
		}
		sceneSelectData.plug.push(regionSelectData);
	}
	regionSelectData.isstatic = "false";
}
/**
 * 删除屏保
 */
function deleteScreen(){
//	var a = document.getElementById("pingbaoanniu");
//	a.style.display = "";
//	var b = document.getElementById("pingbaoanniu1");
//	b.style.display = "none";
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id == "screensaver"){
			program.scene.splice(i,1);
		}
	}
	//program.screenscene = null;
	$('#screenlabel').hide();
	$('#screendelete').hide();
	$("#touchDiv").show();
	$("#radioDiv").show();
	$("#templateDiv").show();
	if(program.scene != null){
		var selected = $(".ui-selectee.ui-selected");
		if(selected.length != 0){
			$("#" + program.scene[0].id).addClass("ui-selectee ui-selected");
			sceneSelectData = getSceneById(program.scene[0].id);
			regionSelectData = undefined;
			showRegionAttr();
			flashScene(program.scene[0].id);
		}
	}
	program.saverid = "";
	program.saverlife = "0";
	
}
function addScreen(){
//	var a = document.getElementById("pingbaoanniu");
//	a.style.display = "none";
//	var b = document.getElementById("pingbaoanniu1");
//	b.style.display = "";
	
	$('#screenlabel').show();
	$('#screendelete').show();
	if (sceneSelectData != undefined) {
		$("#" + sceneSelectData.id).removeClass("ui-selectee ui-selected");
	}
	$("#screenid").addClass("ui-selectee ui-selected");
	$("#touchDiv").hide();
	$("#radioDiv").hide();
	$("#templateDiv").hide();
	var hasScreen = false;
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id == "screensaver"){
			hasScreen = true;
			sceneSelectData = program.scene[i];
			flashScreen();
			if (sceneSelectData != undefined) {
				$("#" + sceneSelectData.id).removeClass("ui-selectee ui-selected");
			}
			$("#screenid").addClass("ui-selectee ui-selected");
			changeSlelectRegion("screenregion");
		}
	}
	if(!hasScreen){
		var newScreenJson = {
			ismain : "false",
			issaver:"false",
			region : null,
			life : "",
			sceneName : "屏保",
			id : "screensaver",
			backimg : "",
			backcolor : "",
			button : new Array(),
			backimgName : ""
		};
		program.scene.push(newScreenJson);
		program.saverid = "screensaver";
		program.saverlife = "60";
		sceneSelectData = newScreenJson;
		flashScreen();
		//添加全屏混播区域
		var regionWidth = parseInt(resolutionX);
		var regionHeight = parseInt(resolutionY);
		var regionData = {
			element : new Array(),
			id : "screenregion",
			width : regionWidth,
			height : regionHeight,
			zIndex : lastZIndex,
			elemlife : "",
			name : typeName[0],
			type : 0,
			left : 0,
			top : 0
		};
		drawRegion(regionData);
		if (checkNull(sceneSelectData.region)) {
			sceneSelectData.region = new Array();
		}
		sceneSelectData.region.push(regionData);
		lastZIndex = lastZIndex + 2;
		var buttonData = {
			backcolor : "",
			backimg : "",
			fontcolor : "",
			fontfamily : "",
			fontsize : "",
			height : regionData.height,
			id : "screenbutton",
			imgstyle : "",
			left : 0,
			name : "屏保按钮",
			sceneid : "",
			selectimg : "",
			bubackimgName : "",
			buselectimgName : "",
			style : 0,
			top : 0,
			type : 1,
			value : "屏保按钮",
			width : regionData.width,
			zIndex : lastZIndex
		};
		sceneSelectData.button.push(buttonData);
		changeSlelectRegion("screenregion");
		regionCount++;
		lastZIndex = lastZIndex + 2;
	}else{
		for (var i = 0; i < program.scene.length; i++) {
			if(program.scene[i].id == "screensaver"){
				sceneSelectData = program.scene[i];
				break;
			}
		}
		flashScreen();
		if (sceneSelectData != undefined) {
			$("#" + sceneSelectData.id).removeClass("ui-selectee ui-selected");
		}
		$("#screenid").addClass("ui-selectee ui-selected");
		changeSlelectRegion("screenregion");
	}
//	if(program.screenscene != null && program.screenscene != "null"){
//		$("#touchDiv").hide();
//		$("#radioDiv").hide();
//		$("#templateDiv").hide();
//		
//		sceneSelectData = program.screenscene[0];
//		flashScreen();
//		if (sceneSelectData != undefined) {
//			$("#" + sceneSelectData.id).removeClass("ui-selectee ui-selected");
//		}
//		$("#screenid").addClass("ui-selectee ui-selected");
//		changeSlelectRegion("screenregion");
//	}else{
//		$("#touchDiv").hide();
//		$("#radioDiv").hide();
//		$("#templateDiv").hide();
//		//showRegionAttr();
//		var newScreenJson = {
//			ismain : "false",
//			issaver:"false",
//			region : null,
//			life : "",
//			sceneName : "屏保",
//			id : "screensaver",
//			backimg : "",
//			backcolor : "",
//			button : new Array()
//		};
//		program.screenscene = new Array();
//		program.screenscene.push(newScreenJson);
//		sceneSelectData = newScreenJson;
//		flashScreen();
//		//添加全屏混播区域
//		var regionWidth = parseInt(resolutionX);
//		var regionHeight = parseInt(resolutionY);
//		var regionData = {
//			element : new Array(),
//			id : "screenregion",
//			width : regionWidth,
//			height : regionHeight,
//			zIndex : lastZIndex,
//			elemlife : "",
//			name : typeName[0],
//			type : 0,
//			left : 0,
//			top : 0
//		};
//		drawRegion(regionData);
//		if (checkNull(sceneSelectData.region)) {
//			sceneSelectData.region = new Array();
//		}
//		sceneSelectData.region.push(regionData);
//		lastZIndex = lastZIndex + 2;
//		var buttonData = {
//			backcolor : "",
//			backimg : "",
//			fontcolor : "",
//			fontfamily : "",
//			fontsize : "",
//			height : regionData.height,
//			id : "screenbutton",
//			imgstyle : "",
//			left : 0,
//			name : "屏保按钮",
//			sceneid : "",
//			selectimg : "",
//			style : 0,
//			top : 0,
//			type : 1,
//			value : "屏保按钮",
//			width : regionData.width,
//			zIndex : lastZIndex
//		};
//		sceneSelectData.button.push(buttonData);
//		changeSlelectRegion("screenregion");
//		regionCount++;
//		lastZIndex = lastZIndex + 2;
//	}
}
/**
 * 选中屏保
 */
function screenSelect() {
//	var a = document.getElementById("pingbaoanniu");
//	a.style.display = "none";
//	var b = document.getElementById("pingbaoanniu1");
//	b.style.display = "";
	if (sceneSelectData != undefined) {
		$("#" + sceneSelectData.id).removeClass("ui-selectee ui-selected");
	}
	$("#screenid").addClass("ui-selectee ui-selected");
	$("#touchDiv").hide();
	$("#radioDiv").hide();
	$("#templateDiv").hide();
	for (var i = 0; i < program.scene.length; i++) {
		if(program.scene[i].id == "screensaver"){
			sceneSelectData = program.scene[i];
			break;
		}
	}
	//sceneSelectData = program.screenscene[0];
	//showRegionAttr();
	flashScreen();
	changeSlelectRegion("screenregion");
	
}
function flashScreen(){
	var sceneBack = $('#sceneBack');
	sceneBack.html('<img id="sceneBackImg" src="img/u106.png" style=" height:100%; width:100%;"align="middle" />');
	flashRegion("screensaver");
}
function closebuttonmanagerwin(){
//	if(sceneSelectData.oldbackimg != undefined){
//		sceneSelectData.backimg = sceneSelectData.oldbackimg;
//		$("#sceneBackImg").attr("src", sceneSelectData.backimg);
//	}
	if(regionSelectData.oldbackimg != undefined){
		regionSelectData.backimg = regionSelectData.oldbackimg;
		$("#backimg").html('<img src="'+regionSelectData.backimg+'" style="width:100px;">'); 
	}
	if(regionSelectData.oldselectimg != undefined){
		regionSelectData.selectimg = regionSelectData.oldselectimg;
		$("#selectimg").html('<img src="'+regionSelectData.selectimg+'" style="width:100px;">'); 
		
	}
	showRegionImg(regionSelectData);
	$('#buttonManagerWin').dialog('close');
}
function isTerminalTiming(terminalIds){
	$.ajax({
		url : 'program/queryTerminalIsTiming',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"terminalids" : terminalIds
		},
		success : function(data) {
			var terminalCloseTime = data.terminalCloseTime;
			var str = "";
			if(terminalCloseTime.length > 0){
	    		var closetimearr = [];
	    		for (var i = 0; i < terminalCloseTime.length; i++) {
	    			var ishave = false;
	    			for (var j = 0; j < closetimearr.length; j++) {
						if(closetimearr[j].id == terminalCloseTime[i].terminalId){
							closetimearr[j].time += ',' + terminalCloseTime[i].startTime + '-' + terminalCloseTime[i].endTime;
							ishave = true;
							break;
						}
					}
	    			if(!ishave){
	    				closetimearr.push({
	    					"id":terminalCloseTime[i].terminalId,
	    					"name":terminalCloseTime[i].terminal_name,
	    					"time":terminalCloseTime[i].startTime + '-' + terminalCloseTime[i].endTime
	    				});
	    			}
	    		}
	    		for (var i = 0; i < closetimearr.length; i++) {
					str += "</br><span style='color:blue;'>" + closetimearr[i].name + "</span>终端已设置下载时间：" + closetimearr[i].time;
				}
	    	}
			if(str != ""){
    			$.messager.confirm('确认',str + '!是否继续发布?',function(r){    
	    			if (!r){    
	    				return;  
	    			} else {
	    				insertProgram(terminalIds);
			    	}
	    		});
    		}else{
    			insertProgram(terminalIds);
    		}
		}
	});
}
function insertProgram(terminalIds){
	checkScene();
	var jsonStr = JSON.stringify(program);
	$.ajax({
		url : 'program/insertProgram.do',
		type : 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			"programJson" : jsonStr,
			"type" : "send",
			"ids" : terminalIds
		},
		success : function(data) {
			if (!data.success) {
				ZENG.msgbox.show(data.msg, 3, 2000);
				return;
			}
			ZENG.msgbox.show("发布成功!", 4, 1000);
			setTimeout(function() {
				setProStatus(false);
				var jumppath = "program/programList.jsp";
				var name = navigator.appName;
				if ("ActiveXObject" in window){
					jumppath = "programList.jsp";
				}
				window.location.href = jumppath;
			}, 1000);
		}
	});

}
/*
 * 时间验证
 */
function validationTime(startTime,endTime){
	if(startTime == '' && endTime == ''){
		return true;
	}
	startTime=new Date("2017/01/04 "+startTime).getTime();
	endTime=new Date("2017/01/04 "+endTime).getTime();
	if (endTime>startTime) {
		return true;
	}else{
		return false;
	}
}