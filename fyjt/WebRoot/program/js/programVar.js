/**
 * 节目编辑过程中所使用的全局变量
 * @type String
 */
var setRegionAttr = "";//标识当前编辑的区域属性 有：regionAttrX regionAttrY regionAttrW regionAttrH
// 场景编号 用来生成新场景ID
var sceneNum = 1;
// 节目分辨率 默认1920*1080
var resolution = "1920*1080";
// 场景真实宽（页面显示像素）
var realSceneW = 0;
// 场景真实宽高（页面显示像素）
var realSceneH = 0;
var sceneWMax = 0;
var sceneHMax = 0;
var resolutionX = 1920;
var resolutionY = 1080;
//当前选中场景的所有数据
var sceneSelectData;
//当前选中区域的所有数据
var regionSelectData;
//场景默认背景图
var sceneBackAll = "";
//锁定区域属性检查方法，当区域在鼠标拖拽过程中不调用属性检查方法，增加流畅度
var removelock = false;
//区域编号 用来生成新区域ID
var regionCount = 1;
//按钮编号 用来生成新按钮ID
var buttonCount = 1;
//添加的素材编号 生成素材表示
var elemCount = 1;
//事件类型
var backclicktype;
//素材类型对应的中文名称
var typeName = {
	0 : "混播",
	1 : "文本",
	2 : "音频",
	3 : "图片",
	4 : "视频",
	5 : "网页",
	6 : "Flash",
	7 : "office",
	8 : "流媒体"
};
//素材类型
var initType = [{
			type_id : 1,
			type_name : "文本"
		}, {
			type_id : 2,
			type_name : "音频"
		}, {
			type_id : 3,
			type_name : "图片"
		}, {
			type_id : 4,
			type_name : "视频"
		}, {
			type_id : 5,
			type_name : "网页"
		}, {
			type_id : 6,
			type_name : "Flash"
		}, {
			type_id : 7,
			type_name : "office"
		}, {
			type_id : 8,
			type_name : "流媒体"
		}];
		//按钮类型对应中文名称
var buttonTypeName = {
	1 : "互动按钮",
	2 : "返回",
	3 : "首页",
	4 : "互动按钮"
};
//插件类型对应中文名称
var plugTypeName = {
	1 : "互动相册",
	5 : "时钟",
	6 : "天气预报",
	7 : "互动office",
	8 : "互动视频",
	9 : "倒计时",
	10 : "unity3d",
	11 : "插件"
};
// 组件素材类型 相册：3（图片）office:7（office） 视频：9
var plugElemType = [null, 3, null, null, null, null, null, 7, 4, null,9,9];
//标记当前节目中所有区域的z-index属性的值范围 
var lastZIndex = 2000;
var firstZIndex = 2000;
// 按钮样式列表
var buttonStyleList;
// 按钮样式效果图存放路径
var buttonStylePath = "";
// 天气组件数据列表
var weatherList = null;
// 天气组件效果图存放路径
var weatherImgPath = "";
// 时钟组件数据列表
var timeList = null;
// 时钟组件效果图存放路径
var timeImgPath = "";
// 倒计时组件数据列表
var countList = null;
// 倒计时组件效果图存放路径
var countImgPath = "";
//查询出的素材列表数据
var elemListData;
//节目单所有数据
var program = {
	istouch : false,
	editProp : 1,
	scene : null,
	staticscene:null,
	id : "",
	name : "",
	startdate : "",
	stopdate : "",
	height : "",
	saverid : "",
	saverlife : 60,
	width : "",
	loopstarttime : "",
	loopstoptime : "",
	loopdesc : "",
	looptype : ""
};
//初始化节目
function initVar() {
	sceneNum = 1;
	realSceneW = 0;
	realSceneH = 0;
	sceneSelectData = undefined;
	regionSelectData = undefined;
	regionCount = 1;
	buttonCount = 1;
	elemCount = 1;
	program.editProp = 1;
	program.scene = null;
	//program.screenscene = null;
	$('#screenlabel').hide();
	$('#screendelete').hide();
}