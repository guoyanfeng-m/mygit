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
				if(data.rows[i].audit_status == 0){
				$('#selectable').append('<li oncontextmenu="property();return false;" eletname="'+data.rows[i].elem_name+'" auditstatus="'+data.rows[i].audit_status+'" description="'+data.rows[i].description+'" id="'+data.rows[i].elem_id+'"  class="ui-state-default"><img  src="'+data.rows[i].thumbnailUrl+'"></img>'+'<font color=\'#CD9B1D\'>'+elementName+'</font>'+'</li>');
					}else if(data.rows[i].audit_status == 1){
						$('#selectable').append('<li oncontextmenu="property();return false;" eletname="'+data.rows[i].elem_name+'" auditstatus="'+data.rows[i].audit_status+'" description="'+data.rows[i].description+'" id="'+data.rows[i].elem_id+'"  class="ui-state-default"><img  src="'+data.rows[i].thumbnailUrl+'"></img>'+'<font color=\'green\'>'+elementName+'</font>'+'</li>');
						}else{
							$('#selectable').append('<li oncontextmenu="property();return false;" eletname="'+data.rows[i].elem_name+'" auditstatus="'+data.rows[i].audit_status+'" description="'+data.rows[i].description+'" id="'+data.rows[i].elem_id+'" class="ui-state-default"><img   src="'+data.rows[i].thumbnailUrl+'"></img>'+'<font color=\'red\'>'+elementName+'</font>'+'</li>');
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
//	if(classifyNameOld==classifyNameNew){
//		ZENG.msgbox.show("无任何修改!", 3, 3000); 
//		return;
//	}
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
//	$("#"+typeValue).addClass("ui-selected");
	$('#cregionMenu').menu('show', {
		left : left,
		top : top
	});
}