/**
 * 验证
 */
var checkInput = function(str) {
	var pattern = /^[\w\u4e00-\u9fa5]+$/gi;
	if (pattern.test(str)) {
		return false;
	}
	return true;
   }
/***验证速度***/
var check=function(str){
    var ary=[];
    for(var i=1;i<=70;i++){
      ary.push(i);
   }
     if(ary.toString().indexOf(str)<=-1){
  	 return true;
   }
     if(str==0){
       return true;
   }
      return false;
  }
/***验证字体***/
  var check2=function(str){
	  if(str.charAt(0)==0){
		  return true;
	  }
	 if(!(/^[-]?\d+$/.test(str))){
		 return  true;
	 }
	 if(str<10 || str>180){
		 return true;
	 }
       return false;
     }
  /****
   * 验证
   * @return
   */
var checkDate=function(){
	    var name=$("#sname").val().replace("%", "/%").replace(" ", "");
	    var testStr=$("#text").val();
	    var textColor;
		var text=testStr.replace(/\ +/g,"").replace(/[ ]/g,"").replace(/[\r\n]/g,"");//去掉空格换行回车
		if(page==1){
		  textColor=RGBToHex($("#font_color").css("background-color"));
		}else{
		  textColor=$("#ceshi111").val();
		}
		var textSize=$("#font_size").val();
		var scrollDirection=$("#scroll_direction").val();
		var scrollSpeed=$("#scroll_speed").val();
		var font=$("#font").val();
		var start_time=$("#start_time").datetimebox("getValue");
		var end_time=$("#end_time").datetimebox("getValue");
		var datas={"name":name,"text":text,"font_size":textSize,"font_color":textColor,"scroll_direction":scrollDirection,"scroll_speed":scrollSpeed,"font":font,"start_time":start_time,"end_time":end_time};
		if(name.length==0){
	    	ZENG.msgbox.show("消息名称不能为空",5, 1000); 
	    	$("#name").focus();
	    	return false;
	    }
		if(checkInput(name)){
			ZENG.msgbox.show("消息名称不能是特殊字符",5, 1000); 
	    	$("#name").val("");
	    	$("#name").focus();
	    	return false;
	    }
		if(textSize.length==0){
			ZENG.msgbox.show("字体大小不能为空",5, 1000); 
		    $("#font_size").focus();
		    return false;
		 }
		if(check2(textSize)){
			ZENG.msgbox.show("字体大小范围在10-180之间的整数",5, 1000); 
	    	$("#font_size").val("");
	    	$("#font_size").focus();
	    	return false;
	    }
		if(scrollDirection==null){
			ZENG.msgbox.show("消息滚动方向不能为空",5, 1000);
		    $("#scroll_direction").focus();
		    return false;
		 }
		if(font==null){
			ZENG.msgbox.show("消息字体不能为空",5, 1000);
		    $("#font").focus();
		    return false;
		 }
		if(scrollSpeed.length==0){
			ZENG.msgbox.show("消息滚动速度不能为空",5, 1000);
			$("#scroll_speed").focus();
			return false;
		}
		if(check(scrollSpeed)){
			ZENG.msgbox.show("速度范围是1-70之间的整数",5, 1000);
			$("#scroll_speed").val("");
			$("#scroll_speed").focus();
			return false;
		}
		if(text.length==0){
			ZENG.msgbox.show("消息内容不能为空",5, 1000);
			$("#text").focus();
			return false;
		}
		if(textColor.length==0){
			$.messager.alert("提示信息","消息颜色不能为空");
			return false;
		}
		if (new Date(start_time.replace(/\-/g, '\/')) > new Date(end_time.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
			ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1000);
            return false;
         }
		return true;
}