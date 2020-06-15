var pro_sceneWMax=0;
var pro_sceneHMax=0;
function showPreviewWin(){
	if(checkNull(program.scene)||program.scene.length==0){
		ZENG.msgbox.show("节目中没有任何场景！", 3, 800);
		return;
	}
	$("#previewWin").dialog("open");
	initPreviewWin();
}
function initPreviewWin() {
	var pro_sceneBack = $('#pre_sceneBack');
	if (pro_sceneWMax == 0) {
		pro_sceneWMax = parseInt(pro_sceneBack.css("width"));
		pro_sceneHMax = parseInt(pro_sceneBack.css("height"));
	}
	var RW = parseInt(resolution.split("*")[0]);
	var RH = parseInt(resolution.split("*")[1]);
	var editProp;
	if (sceneWMax / RW > sceneHMax / RH) {
		editProp = changeDecimal(sceneHMax / RH, 4);
	} else {
		editProp = changeDecimal(sceneWMax / RW, 4);
	}
	program.editProp = editProp;
	realSceneH = RH * editProp;
	realSceneW = RW * editProp;
	pro_sceneBack.css("width", realSceneW);
	pro_sceneBack.css("height", realSceneH);
	var imgPath = sceneBackAll;
	if (checkNull(imgPath)) {
		imgPath = "img/u106.png";
	}
	pro_sceneBack.html('<img id="sceneBackImg" src="' + imgPath
			+ '" style=" height:100%; width:100%;"align="middle" />');
	$("#selectable").html("");
}