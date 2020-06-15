/****
 * 验证空字符
 * @param str
 * @return
 */
var checkNull=function(str){
	if(str.length==0 || str == null  || str == "null" ||  str == "" || str == undefined){
		ZENG.msgbox.show("输入框不能为空！", 5, 1000);
		return true;
	}
	return false;
}
/**
 * 验证特殊字符
 */
var checkZIFU = function(str) {
	if(str.length==0 || str == null  || str == "null" ||  str == "" || str == undefined){
		return false;
	}else{
		var pattern =/^[\w\u4e00-\u9fa5]+$/gi;
		if (!pattern.test(str)) {
			ZENG.msgbox.show("不能输入特殊的字符！", 5, 1000);
			return true;
		}
		  return false;
	    }
 }
/****
 * 
 * 验证MAC
 * @param str
 * @return
 */
var checkMAC=function(str){
	var mac=/[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/; 
	if (!mac.test(str)) 
	{
		 ZENG.msgbox.show("输入的MAC格式不正确！",5, 1000);
	     return true;
	}
	return false;
}

/****
 * 验证ip
 * 
 */
var checkIP=function(str){
	var ip = /^([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])\.([0,1]?\d{0,2}|2[0-4]\d|25[0-5])$/;
    if(!ip.test(str)){
		ZENG.msgbox.show("IP地址格式不正确！", 5, 1000);
        return true;
    }
    return false;
}
/***
 * 验证手机号码
 * 
 * 
 */
var checkPhone=function(str){
	 var phone =/^[1][3-9][0-9]{9}$/;
     if(!phone.test(str))
     {
 		 ZENG.msgbox.show("请输入有效的手机号码！", 5, 1000);
         return true;
     }
     return false;
}
/****
 * 验证邮箱格式
 */
var checkEmail=function(email){
	var mailValid =/^([a-zA-Z0-9]|[._])+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	if(!mailValid.test(email)){
		 ZENG.msgbox.show("输入的邮箱格式不正确！", 5, 1000);
		 return true;
	}
	return false;
}
/****
 * 验证网页格式
 */
var checkHttp=function(str){
	var mailValid =/http:\/\/.+/;
	if(!mailValid.test(str)){
		 ZENG.msgbox.show("输入的网页格式不正确！", 5, 1000);
		 return true;
	}
	return false;
}
/**
 * 验证结束时间是否小于开始时间
 */
var checkDateTime=function(starttime,endtime){
	if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1000);
        return true;
     }
	return false;
}
/**
 * 验证浏览器版本2:ie9 1:ie11 0:其他
 */
var checkBrowserVision=function() {
	var name = navigator.appName;
	if (window.ActiveXObject){
		return '2';                //ie9
	}else if  ("ActiveXObject" in window) {
		return '1';                //ie11
	}else{
		return '0'; 
	}
}