$(document).ready(function() {
	var bodyH=725;//浏览器当前窗口文档body的高度
	var bodyW=1312;//浏览器当前窗口文档body的高度
	var scene=$("#sceneBack")[0];
	scene.style.width=bodyW+"px";
	scene.style.height=bodyH+"px";
	drawPlayProgramDiv(bodyW,bodyH);
});
//通过浏览器宽度高度调整预览节目效果
function drawPlayProgramDiv(bodyW,bodyH){
	var sceneBack=$("#sceneBackChild")[0];
	var sceneW=Number(bodyW-170);
	var sceneH=Number(bodyH-150);
	var sceneBackLeft=0;
	if (sceneW>width) {
		sceneW=width-170;
		sceneBackLeft=bodyW/2-width/2;
	}
	var sceneBackTop=0;
	if (sceneH>height) {
		sceneH=height-150;
		sceneBackTop=bodyH/2-height/2;
	}
	sceneBack.style.width=sceneW+"px";
	sceneBack.style.height=sceneH+"px";
	sceneBack.style.top=sceneBackTop+"px";
	sceneBack.style.left=sceneBackLeft+"px";
	var w=sceneW/Number(width);
	var h=sceneH/Number(height);
	var sceneBackChilds=sceneBack.children;
	//画布scene
	var  sceneBackList=new Array();
	for (var s = 0; s < sceneBackChilds.length; s++) {
		var sWidth=sceneBackChilds[s].style.width;
		var sHeight=sceneBackChilds[s].style.height;
		var sTop=sceneBackChilds[s].style.top;
		var sLeft=sceneBackChilds[s].style.left;
		var sceneBackChildsWidth;
		var sceneBackChildsLeft;
		if (w>1) {
			sceneBackChildsWidth=(Number(sWidth.substring(0,sWidth.length-2))/w)+"px";
			sceneBackChildsLeft=(Number(sLeft.substring(0,sLeft.length-2))/w)+"px";
		}else{
			sceneBackChildsWidth=(Number(sWidth.substring(0,sWidth.length-2))*w)+"px";
			sceneBackChildsLeft=(Number(sLeft.substring(0,sLeft.length-2))*w)+"px";
		}
		var sceneBackChildsHeight;
		var sceneBackChildsTop;
		if (h>1) {
			sceneBackChildsHeight=(Number(sHeight.substring(0,sHeight.length-2))/h)+"px";
			sceneBackChildsTop=(Number(sTop.substring(0,sTop.length-2))/h)+"px";
		}else{
			sceneBackChildsHeight=(Number(sHeight.substring(0,sHeight.length-2))*h)+"px";
			sceneBackChildsTop=(Number(sTop.substring(0,sTop.length-2))*h)+"px";
		}
		sceneBackChilds[s].style.width=sceneBackChildsWidth;
		sceneBackChilds[s].style.height=sceneBackChildsHeight;
		sceneBackChilds[s].style.top=sceneBackChildsTop;
		sceneBackChilds[s].style.left=sceneBackChildsLeft;
		var regionsChild=sceneBackChilds[s].children;
		var regionsChildLength=regionsChild.length;
		//regions素材显示
		if (regionsChildLength>0) {
			for (var i = 0; i <regionsChild.length ; i++) {
				var eWidth=regionsChild[i].style.width;
				var eHeight=regionsChild[i].style.height;
				var eTop=regionsChild[i].style.top;
				var eLeft=regionsChild[i].style.left;
				var regionsChildWidth;
				var regionsChildLeft;
				if (w>1) {
					regionsChildWidth=(Number(eWidth.substring(0,eWidth.length-2))/w)+"px";
					regionsChildLeft=(Number(eLeft.substring(0,eLeft.length-2))/w)+"px";
				}else{
					regionsChildWidth=(Number(eWidth.substring(0,eWidth.length-2))*w)+"px";
					regionsChildLeft=(Number(eLeft.substring(0,eLeft.length-2))*w)+"px";
				}
				var regionsChildHeight;
				var regionsChildTop;
				if (h>1) {
					regionsChildHeight=(Number(eHeight.substring(0,eHeight.length-2))/h)+"px";
					regionsChildTop=(Number(eTop.substring(0,eTop.length-2))/h)+"px";
				}else{
					regionsChildHeight=(Number(eHeight.substring(0,eHeight.length-2))*h)+"px";
					regionsChildTop=(Number(eTop.substring(0,eTop.length-2))*h)+"px";
				}
				regionsChild[i].style.width=regionsChildWidth;
				regionsChild[i].style.height=regionsChildHeight;
				regionsChild[i].style.top=regionsChildTop;
				regionsChild[i].style.left=regionsChildLeft;
				var elementChildChild=regionsChild[i].children[0];
				if (elementChildChild.localName=="video") {
					elementChildChild.width=regionsChildWidth.substring(0, regionsChildWidth.length-2);
					elementChildChild.height=regionsChildHeight.substring(0, regionsChildHeight.length-2);
				}else{
					elementChildChild.style.width=regionsChildWidth;
					elementChildChild.style.height=regionsChildHeight;
				}
				if (Number(regionsChildLength)>1) {
					var sceneBackIdList=sceneBackChilds[s].id;
					sceneBackList.push(sceneBackIdList);
				}
			}
		}
	}
	sceneBackList=unique(sceneBackList);
	drawElement(sceneBackList);
}
//素材轮播控制
function drawElement(sceneBackList){
	if (sceneBackList.length>0) {
		for (var i = 0; i < sceneBackList.length; i++) {
			var ids=sceneBackList[i];
			if (ids!=undefined&&ids!=null) {
				setTimeout(intevalName(ids), 10000);
			}
		}
	}
}
//数组去重扩展
function unique(arr){
	arr.sort();
	var re=[arr[0]];
	for(var i = 1; i < arr.length; i++){
		if( arr[i] !== re[re.length-1]){
			re.push(arr[i]);
		}
	}
	return re;
}

function intevalName(sceneBackId){
	if (sceneBackId!=undefined&&sceneBackId!=null) {
		var num=0;
		setInterval(function(){
			var sceneBackChild=$("#"+sceneBackId)[0].children;
			var secend=null;
			var id=null;
			secend=sceneBackChild[0].attributes.life.value;
			id=sceneBackChild[0].id;
			console.log("id----->"+id);
			console.log("secend----->"+secend);
			num++;
			console.log(num);
			if (id!=undefined&&id!=null&&secend!=undefined&&secend!=null) {
				if (Number(num)>=Number(secend)) {
					var sceneBackChild=document.getElementById(id);
					var bufferedDiv=sceneBackChild;
					sceneBackChild.remove();
					console.log("closed----->"+id);
					$("#"+sceneBackId).append(bufferedDiv);
					num=0;
				}
			}
		},1000);
	}
}