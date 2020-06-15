$(document).ready(function(){
  SHcheck();//审核模块设置
  checklog();//操作日志
  getData(); //查询web设置数据
  getModel();  //审核模块，操作日志设置数据
  getElementPower();//元素权限设置
});
function getData(){
		$.ajax({
			url:'system/queryConfigWEB.do',
			type:'post',
			success:function(data){
			  if(data.listweb.length>0){
				 $("#webServerUrl").val(data.listweb[0].config_value);
				 $("#webServerIp").val(data.listweb[1].config_value);
				 $("#statisticsDeleteWin").html(data.listweb[2].config_value);
				 $("#logDeleteWin").html(data.listweb[3].config_value);
				 $("#lastTerminalAppVer").html(data.listweb[5].config_value);
				 $("#lastAndroidTerminalAppVer").html(data.listweb[4].config_value);
				 $("#outWebServerIp").val(data.listweb[7].config_value);
			  }
		  }
		});
		$.ajax({
			url:'system/queryConfigContact.do',
			type:'post',
			success:function(data){
			  if(data.listContact.length>0){
				 $("#contactPhone").val(data.listContact[0].config_value);
				 $("#contactFax").val(data.listContact[1].config_value);
				 $("#contactEmail").val(data.listContact[2].config_value);
			  }
		  }
		});
		$.ajax({
			url:'system/queryFTP.do',
			type:'post',
			success:function(data){
			  if(data.listftp.length>0){
				$("#ftpMappingUrl").val(data.listftp[0].config_value);
				$("#ftpServerIp").val(data.listftp[1].config_value);
				$("#uploadPort").val(data.listftp[2].config_value);
				$("#monitorSenderMailWin").html(data.listftp[3].config_value);
				$("#monitorRecieverMailWin").html(data.listftp[4].config_value);
				$("#loginname").val(data.listftp[5].config_value);
				$("#password").val(data.listftp[6].config_value);
				$("#url").val(data.listftp[7].config_value);
				$("#outFtpServerIp").val(data.listftp[8].config_value);
				$("#downloadPort").val(data.listftp[9].config_value);
			 }
		  }
		});
		$.ajax({
			url:'system/getTerminalValue.do',
			type:'post',
			success:function(data){
			  $('#terminalTotal').html(data.terminalTotal);
			  $('#terminalHad').html(data.terminalHad);
		   }
		});
		$.ajax({
			url:'system/getListenValue.do',
			type:'post',
			success:function(data){
			  $("input[name='listenradio']").eq(data).attr("checked","checked");
		   }
		});
}
//素材权限
function getElementPower(){
	$.ajax({
		url:'system/queryElementPower.do',
		type:'post',
		success:function(data){
		$("input[name='radio']").eq(data).attr("checked","checked");
	}
	});
}
function saveelepower(data){
 	$.ajax({
 		 url:'system/updateElementPower.do',
 	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
 	     data:{"elementPower":data},
 	     type:'post',
 		 success:function(data){
 	    	$("input[name='radio']").eq(data).attr("checked","checked");
 	    	 $.messager.defaults={ok:"确定",cancel:"取消"};
 	    	 $.messager.confirm("操作提示","是否要选择刷新页面？",function(data){
 	    		if(data){
 	    			ZENG.msgbox.show("操作成功！",4, 1000);
 	    			parent.location.reload();
 	    		} else{
 	    			 ZENG.msgbox.show("操作成功！",4, 1000);
 	    		}
 	    	 });
 		   }
      });
}
function savelisten(data){
	$.ajax({
 		 url:'system/updateListener.do',
 	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
 	     data:{"listener":data},
 	     type:'post',
 		 success:function(data){
 	    	if(data.succ){
 	    		ZENG.msgbox.show("操作成功！",4, 1000);
 	    	}
 		 }
	});
	
}
//审核模块，操作日志设置数据
function getModel(){
	$.ajax({
		url:'model/queryAll.do',
		type:'post',
		success:function(data){
		  if(data.modelList.length>0){
			  var m=0;
			  var n=0;
			$.each(data.modelList,function(i,item){
				$("input[name='checkbox1']").each(function(){
					if(item.module_id==this.value && item.is_auditing==0){
						$(this).attr("checked",true);
						m+=1;
					}
				});
				$("input[name='checkbox2']").each(function(){
					if(item.module_id==this.value && item.is_log==0){
						$(this).attr("checked",true);
						n+=1;
					}
				});
			});
			  $("input[name='checkbox1']").each(function(){
			      if($(this).attr("checked")==true){
			          document.getElementById("s"+$(this).val()).style.color="#FF0000";
			      }
			  });
			  $("input[name='checkbox2']").each(function(){
			    if($(this).attr("checked")==true){
			      document.getElementById("cs"+$(this).val()).style.color="#FF0000";
			      }
			  });
		  }
		  /**设置复选框是否全选**/
		  if(m==4){
			  $("#ckall").attr("checked",true); 
		  }
		  if(m==0){
			  $("#rmall").attr("checked",true);
		  }
		  if(n==9){
			  $("#logckall").attr("checked",true);
		  }
		  if(n==0){
			  $("#logrmall").attr("checked",true);
		  }
	  }
	});
}
//审核模块设置
function SHcheck(val){
	/**全选按钮**/
	$("#ckall").click(function(){
		if($("#ckall").attr("checked")==true){
			$("#rmall").attr("checked",false);
		}else{
			$("input[name='checkbox1']").each(function(){
				document.getElementById("s"+$(this).val()).style.color="#000000";
				$("input[name='checkbox1']").attr("checked",false);
			});	
			return;
		}
		$("input[name='checkbox1']").attr("checked",$(this).attr("checked"));
		$("input[name='checkbox1']").each(function(){
			document.getElementById("s"+$(this).val()).style.color="#FF0000";
		});
	});
	
	//全不选按钮
	$("#rmall").click(function(){
		if($("#rmall").attr("checked")==true){
			$("#ckall").attr("checked",false);
		}
		$("input[name='checkbox1']").attr("checked",false);
		$("input[name='checkbox1']").each(function(){
			document.getElementById("s"+$(this).val()).style.color="#000000";
		});
	});
	//checkbox没有被全部选中的时候改变全选按钮状态
	$("#c"+val).click(function(){
		if($("#c"+val).attr("checked")==true)
		{
			document.getElementById("s"+val).style.color="#FF0000";
		}else{
			document.getElementById("s"+val).style.color="#000000";
		}		
		var chNum=$("input[type='checkbox'][name='checkbox1']").length;     //获取checkbox的个数  
		var chChecked=$("input[type='checkbox'][name='checkbox1']:checked").length;   ////获取选中的checkbox的个数
		if(chNum==chChecked){
			$("#ckall").attr("checked",true);
		}else{
			$("#ckall").attr("checked",false);
		}
		if(chChecked!=0){
			$("#rmall").attr("checked",false);
		}else{
			$("#rmall").attr("checked",true);
		}
	});
}
//操作日志设置
function checklog(val){
	$("#logckall").click(function(){
		if($("#logckall").attr("checked")==true){
			$("#logrmall").attr("checked",false);
		}
		else{
			$("input[name='checkbox2']").each(function(){
				document.getElementById("cs"+$(this).val()).style.color="#000000";
				$("input[name='checkbox2']").attr("checked",false);
			});	
			return;
		}
		$("input[name='checkbox2']").attr("checked",$(this).attr("checked"));
		$("input[name='checkbox2']").each(function(){
			document.getElementById("cs"+$(this).val()).style.color="#FF0000";
		});
	});
	$("#logrmall").click(function(){
		if($("#logrmall").attr("checked")==true){
			$("#logckall").attr("checked",false);
		}
		$("input[name='checkbox2']").attr("checked",false);
		$("input[name='checkbox2']").each(function(){
			document.getElementById("cs"+$(this).val()).style.color="#000000";
		});
	});
	//checkbox没有被全部选中的时候改变全选按钮状态
	$("#ch"+val).click(function(){
		if($("#ch"+val).attr("checked")==true)
		{
			document.getElementById("cs"+val).style.color="#FF0000";
		}else{
			document.getElementById("cs"+val).style.color="#000000";
		}
		var chNum=$("input[type='checkbox'][name='checkbox2']").length;     //获取checkbox的个数  
		var chChecked=$("input[type='checkbox'][name='checkbox2']:checked").length;   ////获取选中的checkbox的个数
		if(chNum==chChecked){
			$("#logckall").attr("checked",true);
		}else{
			$("#logckall").attr("checked",false);
		}
		if(chChecked!=0){
			$("#logrmall").attr("checked",false);
		}else{
			$("#logrmall").attr("checked",true);
		}
	});
}
//审核模块,日志设置模块保存
function checkSave(){
	//获取到所有name为'checkbox1'并选中的checkbox(集合)
	var moduleIds=new Array();
    	var check1=$("input[name='checkbox1']:checked");
    	
       	 //遍历得到每个checkbox的value值
           for(var i=0;i<check1.length;i++)
           {
        	   moduleIds.push(check1[i].value);
           }
       	$.ajax({
      		 url:'model/updateModel.do',
      	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
      	     data:{"moduleIds":moduleIds,"id":1},
      	     type:'post',
      		 success:function(){
      	    	 ZENG.msgbox.show("操作成功！",4, 1000);
      		      getData();
      		   }
           });
}
//日志设置模块保存
function opearLogSave(){
	//获取到所有name为'checkbox1'并选中的checkbox(集合)
	var moduleIds=new Array();
    var check2=$("input[name='checkbox2']:checked");
    	
	 //遍历得到每个checkbox的value值
    for(var i=0;i<check2.length;i++)
    {
    	moduleIds.push(check2[i].value);
    }
	$.ajax({
		 url:'model/updateModel.do',
	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	     data:{"moduleIds":moduleIds,"id":2},
	     type:'post',
		 success:function(){
	    	 ZENG.msgbox.show("操作成功！",4, 1000);
		      getData();
		 }
	});
}
/***
 * 保存webftp设置
 * @return
 */
function saveweb(s){
	if(s==0){
		var value1=$("#webServerUrl").val();
		var value2=$("#webServerIp").val();
		var value11=$("#outWebServerIp").val();
		if(value1.length==0){
			 ZENG.msgbox.show("服务器URL不能为空",5, 1000);
			 return;
		}
		if(value2.length==0){
			 ZENG.msgbox.show("内网服务器IP不能为空",5, 1000);
			 return;
		}
		$.ajax({
			 url:'system/updateConfig.do',
		     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		     data:{"value1":value1,"value2":value2,"value11":value11},
		     type:'post',
			 success:function(){
		    	 ZENG.msgbox.show("修改成功",4, 1000);
			      getData();
			 }
		});
	}
	if(s==1){
		var value3=$("#outFtpServerIp").val();
		var value4=$("#uploadPort").val();
		var value5=$("#ftpMappingUrl").val();
		var value6=$("#ftpServerIp").val();
		var value7=$("#downloadPort").val();
		if(value5.length==0 || value6.length==0 || value4.length==0  || value7.length==0){
			 ZENG.msgbox.show("除外网设置,FTP服务器设置各项设置都不能为空",5, 1000);
			 return;
		}
		$.ajax({
			 url:'system/updateConfig.do',
		     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		     data:{"value3":value3,"value4":value4,"value5":value5,"value6":value6,"value7":value7},
		     type:'post',
			 success:function(){
		    	 ZENG.msgbox.show("修改成功",4, 1000);
			 }
		});
	}
	if(s==2){
		var statisticsDelete=$("#statisticsDelete").val();
		var logDelete=$("#logDelete").val();
		if(statisticsDelete.length!=0 && logDelete.length!=0){
			var re=/^[0-9]*[1-9][0-9]*$/;
			if(!re.test(statisticsDelete) || !re.test(logDelete)){
				ZENG.msgbox.show("输入的必须是正整数",5, 1000);
				return;
			}
		}
		$.ajax({
			url:'system/delLog.do',
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		    data:{"statisticsDelete":statisticsDelete,"logDelete":logDelete},
		    type:'post',
		    success:function(){
		    	ZENG.msgbox.show("修改成功",4, 1000);
			    getData();
		    }
		});
	}
}
/***
 * 保存联系方式设置
 * @return
 */
function saveContact(){
	var value12=$("#contactPhone").val();
	var value13=$("#contactFax").val();
	var value14=$("#contactEmail").val();
	$.ajax({
		 url:'system/updateConfig.do',
	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	     data:{"value12":value12,"value13":value13,"value14":value14},
	     type:'post',
		 success:function(){
	    	 ZENG.msgbox.show("修改成功",4, 1000);
		      getData();
		 }
	});
}

//手动删除操作日志
function delOpearLog(){
	var starttime=$('#starttime').datebox('getValue');
	var endtime=$('#endtime').datebox('getValue');
	if(starttime.length==0 || endtime.length==0){
		ZENG.msgbox.show("开始时间和结束日期不能为空",5, 1000);
		return;
	}
	if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1000);
        return false;
    }
	$.ajax({
		url:'operationlog/delOperation.do',
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    data:{"starttime":starttime,"endtime":endtime},
	    type:'post',
	    success:function(){
	    	ZENG.msgbox.show("操作成功！",4, 1000);
		    getData();
	    }
	});
}
//手动删除统计日志
function delstatis(){
	var starttime=$('#starttime2').datebox('getValue');
	var endtime=$('#endtime2').datebox('getValue');
	if(starttime.length==0 || endtime.length==0){
		ZENG.msgbox.show("开始时间和结束日期不能为空",5, 1000);
		return;
	}
	if (new Date(starttime.replace(/\-/g, '\/')) > new Date(endtime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
		ZENG.msgbox.show("时间选择有误！开始日期必须小于结束时期！",5, 1000);
        return false;
    }
	$.ajax({
		url:'statistics/delStatistics.do',
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    data:{"starttime":starttime,"endtime":endtime},
	    type:'post',
	    dataType:'json',
	    success:function(data){
	    	if(data.success==true){
	    		ZENG.msgbox.show("操作成功！",4, 1000);
			    getData();	
	    	}else{
	    		ZENG.msgbox.show("操作失败！",4, 1000);
			    getData();
	    	}
	    }
	});
}
function close(){
	  $('#progressbarWin').window('close');
}
//终端更新上传
function uploadFile(){
  var version=$('#version').val();
  var filename=document.formname.myfiles.value;
  var versionnumber=$('#versionNumber').val();
  if(version.length==0){
	  ZENG.msgbox.show("请选择版本",5, 1000);
	  return;
  }
  if(filename==""){
	  ZENG.msgbox.show("请选上传文件",5, 1000);
	  return;
  }
  if(versionnumber==""){
	  ZENG.msgbox.show("请输入版本号",5, 1000);
	  return;
  }
  if(version=="1" &&(filename.substring(filename.lastIndexOf('.')+1))!="zip"){
	 ZENG.msgbox.show("windows版本只能选择zip压缩文件",5, 1000);  
  	return;
  }
  if(version=="2" &&(filename.substring(filename.lastIndexOf('.')+1))!="apk"){
	  ZENG.msgbox.show("安卓版本只能选择apk文件",5, 1000);
	  return;
   }
   if(/.*[\u4e00-\u9fa5]+.*$/.test(filename)) 
   { 
	 ZENG.msgbox.show("路径或文件名不能有中文",5, 1000);
	 return;
   }
   if(!(/\d\.\d/.test(versionnumber))) 
   { 
	   ZENG.msgbox.show("版本号只能由数字和小数点组成，最长11位",5, 1500);
	   return;
   }
	document.getElementById("p").style.display="block";
	start();
   $("#upload").form("submit",{
	     url:'system/upload.do',
	     type:'post',
	     dataType:'text/html',
	     onsubmit:function(){
		    return $(this).form("validate");
		 },
		 success:function(data){
			$('#p').progressbar('setValue', 100);
			//$.messager.alert("提示信息",data);
			getData();
			document.getElementById("p").style.display="none";
			$('#p').progressbar('setValue', 0);
			document.formname.myfiles.value="";
			document.getElementById('versionNumber').value="";
			
		 },
		 error:function(data){
		   $.messager.alert("提示信息",data);
		 }
	  });
  }
/****
 * 邮箱管理
 * @return
 */
function sendEmail(){
	var value8=$('#monitorSenderMail').val();
	var value9=$('#monitorSenderPass').val();
	var value10=$('#monitorRecieverMail').val();
	if(value8.length==0 || value9.length==0 || value10.length==0){
		 ZENG.msgbox.show("输入项不能为空",5, 1000);
		 return;
	}
	if(!checkEmail(value8) && !checkEmail(value10)){
		$.ajax({
			 url:'system/updateConfig.do',
		     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		     data:{"value8":value8,"value9":value9,"value10":value10},
		     type:'post',
			 success:function(){
		    	 ZENG.msgbox.show("修改成功",4, 1000);
		    	 getData();
			 }
		});
	}
}

//导出license
function exportLicense(){
	$.ajax({
		 url:'license/checkData.do',
	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	     type:'post',
		 success:function(data){
	    	 var result=data.flag;
	    	 if(result==0){
    		 if(!!window.ActiveXObject || "ActiveXObject" in window){
    				jumppath = "../license/exportLicense.do";
    	      }else{
    	    	  jumppath="license/exportLicense.do";
    	      }
	         	window.location.href=jumppath;
	    	 }else{
	    		ZENG.msgbox.show("没有注册的license信息导出",5, 1000);
	    		return; 
	    	 }
	     }
	});
}
//license上传
//终端更新上传
function upload(){
  var filename=document.uploadform.fileData.value;
  if(filename==""){
	  ZENG.msgbox.show("请选上传文件",5, 1000);
	  return;
  }
  if((filename.substring(filename.lastIndexOf('.')+1))!="xml"){
	 ZENG.msgbox.show("请选择.xml文件",5, 1000);  
  	return;
  }
	document.getElementById("pp").style.display="block";
	begin();
   $("#uploadform").form("submit",{
	     url:'license/upload.do',
	     type:'post',
	     dataType:'text/html',
	     onsubmit:function(){
		    return $(this).form("validate");
		 },
		 success:function(data){
			$('#p').progressbar('setValue', 100);
			getData();
			document.getElementById("pp").style.display="none";
			$('#pp').progressbar('setValue', 0);
			document.uploadform.fileData.value="";
		 },
		 error:function(data){
		   $.messager.alert("提示信息",data);
		 }
	  });
  }
//根据用户登录信息获得license信息
function getLicense(){
	var loginname=$('#loginname').val();
	var password=$('#password').val();
	var url=$('#url').val();
	if(loginname==""){
		  ZENG.msgbox.show("请输入登录用户名",5, 1000);
		  return;
	}
	if(password==""){
		  ZENG.msgbox.show("请输入密码",5, 1000);
		  return;
	}
	if(url==""){
		  ZENG.msgbox.show("请输入license服务器地址",5, 1000);
		  return;
	}
	$.ajax({
		 url:'license/licenseLogin.do',
	     contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	     data:{"loginname":loginname,"password":password,"url":url},
	     type:'post',
		 success:function(data){
	    	 var result=data.msg;
	         if(result==0){
	        	 ZENG.msgbox.show("您输入的用户不存在！",5,1000);
        		 return; 
   		       }else if(result==1){
   		    	 ZENG.msgbox.show("您输入的用户没有权限登录！",5,1000);
        		 return;
   		       }else if(result==2){
   		    	 ZENG.msgbox.show("要注册的license数量大于申请的数量！",5,1000);
   	        	 return;
   	          }else if(result==3){
   	        	 ZENG.msgbox.show("获取license信息失败！",5,1000);
   	        	 return;
   	          }else if(result==4){
   	        	 ZENG.msgbox.show("请检查是否有终端连上服务器！",5,1000);
       	          return; 
   	          }else if(result==5){
   	        	 ZENG.msgbox.show("连接异常！",5,1000);
      	          return;
   	          }else if(result==6){
   	        	 ZENG.msgbox.show("操作成功！",4,1000);
   	             $('#terminalTotal').html(data.terminalTotal);
			     $('#terminalHad').html(data.terminalHad);
       	          return;
       	       }else if(result==7){
     	          ZENG.msgbox.show("license服务器网址不正确！",5,1000);
           	      return;
           	   }else{
       	    	 ZENG.msgbox.show("获取license信息失败！",5,1000);
       	          return;  
       	       }
		 }
	});
}
function bakupServer(){
    $.messager.progress({ 
        title: '提示信息', 
        msg: '正在备份服务器...', 
        text: '备份中...' 
    });

    $.ajax({ 
        type: "GET", 
        url: "system/bakupAndDownServer.do",
        success: function (data) { 
            $.messager.progress('close'); 
            if(data.success==true){
            	ZENG.msgbox.show(data.msg, 4, 3000); 
            }else{
            	ZENG.msgbox.show(data.msg, 1, 3000); 
            }
        } 
    }); 
}
function downServer(){
//check服务器是否已有备份
	$.ajax({
		url : 'system/checkServer.do',
		method:"POST",
		success : function(data) {
		if(data.success==true){
			$.messager.confirm("信息提示", "您已于 "+data.time+" 备份了服务器,点击确定直接下载，也可点击服务器备份重新备份!", function(data) {
				if (data) {
					downloadServer();	
				}
			});
	    	}else{
	    		ZENG.msgbox.show(data.msg, 1, 3000);		
	    	}
		},
		error: function (e) {
		return;
        }
	});
}
function downloadServer(){
	var jumppath = "system/downloadServer.do";
	if (checkBrowserVision()=="2"){
		jumppath = "downloadServer.do";
	}
	window.open(jumppath);
}
function uploadserverFile(){

	  var filename=document.upserverForm.severfiles.value;
	  if(filename==""){
		  ZENG.msgbox.show("请选上传文件",5, 1000);
		  return;
	  }
	
	  if((filename.substring(filename.lastIndexOf('.')+1))!="zip"){
		 ZENG.msgbox.show("只能选择zip压缩文件",5, 1000);  
	  	return;
	  }
	
	   if(/.*[\u4e00-\u9fa5]+.*$/.test(filename)) 
	   { 
		 ZENG.msgbox.show("路径或文件名不能有中文",5, 1000);
		 return;
	   }
	    $.messager.progress({ 
	        title: '提示信息', 
	        msg: '上传服务器备份文件', 
	        text: '正在上传中...' 
	    });
	   $("#uploadserver").form("submit",{
		     url:'system/uploadServerFile.do',
		     type:'post',
		     dataType:'text/html',
		     onsubmit:function(){
			    return $(this).form("validate");
			 },
			 success:function(data){
				$.messager.progress('close'); 
				var data =  $.parseJSON(data); 
			      if(data.success==true){
		            ZENG.msgbox.show(data.msg, 4, 3000);
			      }else{
			    	 ZENG.msgbox.show(data.msg, 5, 3000);  
			      }
			      document.upserverForm.severfiles.value="";
			 },
			 error:function(data){
			   $.messager.alert("提示信息",data);
			 }
		  });
	  
}