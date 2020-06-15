var userflag=0;
var passflag=0;
var teleflag=0;
var emailflag=0;
$(document).ready(function(){  
    loadGrid(); 
    $('#updateuser').window({
        onBeforeClose: function () { 
    	$('#updaterolelistone').datagrid("clearSelections");
	 		},
    	onClose: function () { 
	 	    	if(browser.indexOf("Chrome")!= -1 || browser.indexOf("Firefox") != -1){
	 	    		location.href = "admin/user/user.jsp"
	 	    	}else{
	 	    		location.href = "user.jsp"
	 	    	}
	 	    
	 		 		}
    });
    $('#newuser').window({
    	onClose: function () { 
//    	if(browser.indexOf("Chrome")!= -1 || browser.indexOf("Firefox") != -1){
//    		location.href = "user/user.jsp"
//    	}else{
//    		location.href = "user.jsp"
//    	}
    	
    	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
    	if(isChrome){
    		location.href = "admin/user/user.jsp"
    	}else{
    		location.href = "user.jsp"
    	}
    
	 		}
    });
    
    
    $('#updateuser').window({
    	onClose: function () { 
    	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
    	if(isChrome){
    		location.href = "admin/user/user.jsp"
    	}else{
    		location.href = "user.jsp"
    	}
    
	 		}
    });
    $.extend($.fn.validatebox.defaults.rules, {
    	userValid: {
	        validator: function(value){
	    		var userValid=/^[A-Za-z0-9_]+$/;
	    		var temp = userValid.test(value);
	    		var flag1;var flag2;
	    		if(value.length<5||value.length>30){
	 	    		flag1=0;
	    		 }
	    		else if(!userValid.test(value)){
		    		 flag2=0;
 	    		}
	    		if(flag1==0){
	    			$.fn.validatebox.defaults.rules.userValid.message = "长度在5-30位之间"; 
	    			userflag=1;
	    			return false;
	    		}else if(flag2==0){
	    			$.fn.validatebox.defaults.rules.userValid.message = "不能包含特殊字符";
	    			userflag=1;
	    			return false;
	    		}else{
	    			userflag=0;
	    			return true;
	    		}
	        }
        },
    	
    	passValid: {
        	validator: function(value){
	    		var passValid= /^[A-Za-z0-9]+$/;
	    		var flag1;var flag2;
	    		if(value.length<5||value.length>20){
	 	    		flag1=0;
	    		 }
	    		else if(passValid.test(value)==false){
		    		 flag2=0;
 	    		}
	    		if(flag1==0){
	    			$.fn.validatebox.defaults.rules.passValid.message = "长度在5-20位之间"; 
	    			passflag=1;
	    			return false;
	    		}else if(flag2==0){
	    			$.fn.validatebox.defaults.rules.passValid.message = "密码由数字和英文组成";
	    			passflag=1;
	    			return false;
	    		}else{
	    			passflag=0;
	    			return true;
	    		}
	    			
        	}
        },
        telephoneValid: {
        	validator: function(value){
	    		var teleValid=/^[1][3-9][0-9]{9}$/;
//	    		^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$
	    		var flag1;var flag2;
	    		if(value.length!=11){
	    			flag1=0;
	    		}
	    		else if(teleValid.test(value)==false){
	    			flag2=0;
		    	}
	    		if(flag1==0){
	    			$.fn.validatebox.defaults.rules.telephoneValid.message = "手机号长度为11位"; 
	    			teleflag=1
	    			return false;
	    		}else if(flag2==0){
	    			 $.fn.validatebox.defaults.rules.telephoneValid.message = "手机号码格式不正确"; 
	    			 teleflag=1
	    			return false;
	    		}else{
	    			teleflag=0
	    			return true;
	    		}
	    		 
        	}
        },
        emailValid:{
        	validator: function(value){
        	//var mailValid =/^([a-zA-Z0-9]|[._])+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        	var mailValid =/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	    	if(mailValid.test(value)==false){
	    		 $.fn.validatebox.defaults.rules.emailValid.message = "邮箱格式不正确";  
	    		 emailflag=1;
	    		 return false;
	    	}else{
	    		emailflag=0;
	    		 return true;
	    	}
    		}
        }
        
        });
    
    
    
}); 



function loadGrid()  
{  
    //加载数据  
    $('#userlist').datagrid({  
                width: 'auto',                
                striped: true,  
                singleSelect : false,  
                url:'user/queryUser.do',  
               // queryParams:{deleted:1},  
                loadMsg:'数据加载中请稍后……',  
                pagination: true,
                pageSize:10,
                method:'POST',
                rownumbers: true,     
                columns:[[  

{field:'',checkbox:true,align:'center',title: ''},
{field:'user_id',title: '用户id',hidden:true}, 
{field:'username',width:'10%',align:'center',title: '登录名'}, 
{field:'realname',width:'15%',align:'center',title: '真实姓名'},
{field:'telephone',width:'15%',align:'center',title: '电话号码'}, 
{field:'email',width:'15%',align:'center',title: '邮箱'}, 
{field:'description',width:'14%',align:'center',title: '描述'}, 
{field:'brealname',width:'10%',align:'center',title: '创建人'}, 
{field:'create_time',width:'17%',align:'center',title: '创建日期',formatter:function(value,row,index){
    var unixTimestamp = new Date(value);
    return unixTimestamp.toLocaleString();
    }} 
                ]]  
            });  
    var p = $('#userlist').datagrid('getPager'); 
    $(p).pagination({ 
    	pageList: [5,10,15],
        beforePageText: '第',//页数文本框前显示的汉字 
        afterPageText: '页    共 {pages} 页', 
        showRefresh:false,
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录' 
        /*onBeforeRefresh:function(){
            $(this).pagination('loading');
            alert('before refresh');
            $(this).pagination('loaded');
        }*/ 
    });  
} 

function newuser(){
//	$(':input') .css({"color":"#aaaaaa"});
	$("input[id='password']").attr('type','text');
	$("input[id='comfirmpass']").attr('type','text');
	var usernamestr=$('#username').val("请输入登录名称");
	var passwordstr=$('#password').val("请输入密码");
	var comfirmpassstr=$('#comfirmpass').val("请输入确认密码");
	var realnamestr=$('#realname').val("请输入真实姓名");
	var telephonestr=$('#telephone').val("请输入手机号");
	var userdesstr=$('#userdes').val("请输入描述");
	var emailstr=$('#email').val("请输入邮箱");
	$('#newuser').window('open');
	$('#rolelistone').datagrid({  
        width: 'auto',                
        striped: true,  
        singleSelect : false,  
        url:'role/queryRole.do',  
        loadMsg:'数据加载中请稍后……',  
        pagination: false,
        queryParams:{page:1,rows:1000},
        method:'POST',
        rownumbers: false,  
        columns:[[  
            
			{field:'role_id',align:'center',title: '角色id',hidden:true}, 
			{field:'role_name',width:'34%',align:'center',title: '角色名称'}, 
			{field:'description',width:'35%',align:'center',title: '角色描述'},
			{field:'search',width:'32%',align:'center',title: '详细信息',formatter:function(value,rec){
				 var btn = '<input type="button"  onclick="showpower('+rec.role_id+')" value="查看详细" style="width:100%;height:30px">';  
	             return btn; 
			
			}}
			    
        ]]  
    });

}

function showpower(i){
	$('#showpower').window('open');	
	$('#rolepowertree').tree({  
        checkbox: false, 
        animate:true,  
        lines:false,  
        queryParams:{roleid:i},
        url:'role/queryShowRolePower.do',
        cache:true,
        onLoadSuccess:function(node){}
    });
	
	$('#roleterminaltree').tree({  
        checkbox: false, 
        animate:true,  
        lines:false,  
        queryParams:{roleid:i},
        url:'roletree/queryRoleTreeByRoleID.do',
        cache:true,
        onLoadSuccess:function(node){}
    });
}
function saveuser(){
	
	var role= $('#rolelistone').datagrid('getSelections');
	if(role.length==0){
		ZENG.msgbox.show("请选择一个角色", 3, 2000); 
		return;
	}else if(role.length==1){
		if($('#username').css("color")=="rgb(170, 170, 170)"){ 
			var usernamestr="";
		}else{
			var usernamestr=$('#username').val();
		}
		if($('#password').css("color")=="rgb(170, 170, 170)"){
			var passwordstr="";
		}else{
			var passwordstr=$('#password').val();
		}
		if($('#comfirmpass').css("color")=="rgb(170, 170, 170)"){
			var comfirmpassstr="";
		}else{
			var comfirmpassstr=$('#comfirmpass').val();
		}
		if($('#realname').css("color")=="rgb(170, 170, 170)"){
			var realnamestr="";
		}else{
			var realnamestr=$('#realname').val();
		}
		if($('#telephone').css("color")=="rgb(170, 170, 170)"){
			var telephonestr="";
		}else{
			var telephonestr=$('#telephone').val();
		}
		if($('#userdes').css("color")=="rgb(170, 170, 170)"){
			var userdesstr="";
		}else{
			var userdesstr=$('#userdes').val();
		}
		if($('#email').css("color")=="rgb(170, 170, 170)"){
			var emailstr="";
		}else{
			var emailstr=$('#email').val();
		}
		
		var roleids=role[0].role_id;
		var datas = {"username":usernamestr,"password":passwordstr,"realname":realnamestr,"telephone":telephonestr,"description":userdesstr,"email":emailstr,"roleid":roleids};
		
		var nverify=null;
		var datas1 = {"username":usernamestr};
		$.ajax({ 
	    	url: "user/UserVerify.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded",
	    	data: datas1,
	    	async: false,
	    	success: function(result){
			if(result.success==1){
				nverify=1;
			}
	      	}
	      });
		if(usernamestr=="" || usernamestr.length>20){
			ZENG.msgbox.show("用户名称不能为空或者长度不能大于20", 3, 1000); 
			return;
		}else if(usernamestr.length>0){
			if(checkZIFU(usernamestr)){
			return;
			}
		else{
			if(userflag==1){
				ZENG.msgbox.show("用户名不能有中文且长度大于5", 3, 2000); 
				return;
			}else{
				if(nverify==1){
					ZENG.msgbox.show("用户名已经存在", 3, 2000); 
					return;
				}else {
				 if(passwordstr=="" || passwordstr.length>20){
						ZENG.msgbox.show("密码不能为空并且长度不能大于20", 3, 2000); 
						return;
					}else{
						if(passflag==1){
							ZENG.msgbox.show("密码格式不正确", 3, 2000); 
						}else{
							if(realnamestr == "" || realnamestr.length>20){
								ZENG.msgbox.show("真实姓名不能为空并且长度不能大于20", 3, 1000); 
								return;
							}
							if(checkZIFU(realnamestr)){
								return;
							}
							if(passwordstr==comfirmpassstr){
								if($('#telephone').val()!="请输入手机号" && teleflag==1 ){
									ZENG.msgbox.show("电话格式不正确", 3, 2000); 
									return;
								}else{
									if($('#email').val()!="请输入邮箱" && emailflag==1){
										ZENG.msgbox.show("邮箱格式不正确", 3, 2000); 
										return;
									}else{
										if(userdesstr.length>50){
											ZENG.msgbox.show("用户描述不能大于50", 3, 1000); 
											return;
										}else{
											$("#saveuserbut").attr("disabled", "disabled");
											$.ajax({ 
										    	url: "user/insertUser.do", 
										    	type: "POST",
										    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
										    	data: datas,
										    	success: function(){
													ZENG.msgbox.show("添加成功", 4, 2000); 
													setTimeout(function(){
														$('#newuser').window('close');
														$('#userlist').datagrid('reload');
													 },300);
										      	}
										      });
										}
									}
									
								}
							} else{
								ZENG.msgbox.show("两次密码不一样", 3, 2000); 
							}
						}
					}
				}
			}
		}
		}
	}else{
		ZENG.msgbox.show("只能选择一个角色", 3, 2000); 
	}

}
function updateuser(){
	var select;
	user= $('#userlist').datagrid('getSelections');
	if(user.length==0){
		ZENG.msgbox.show("请选择一个用户", 3, 2000); 
	}else if(user.length==1){
		
		$('#updateuser').window('open');
//		$("input[id='password']").attr('type','password');
//		$('#updateusername').textbox("setValue",user[0].username);
//		$('#updaterealname').textbox("setValue",user[0].realname);
//		$('#updatetelephone').textbox("setValue",user[0].telephone);
//		$('#updateemail').textbox("setValue",user[0].email);
//		$('#updateuserdes').textbox("setValue",user[0].description);
		$('#updateusername').val(user[0].username);
		
		
//		alert($('#updaterealname').val()+"0000")
		if(user[0].realname==""){
			$('#updaterealname') .css({"color":"#aaaaaa"})
			$('#updaterealname').val("请输入真实姓名");
		}else{
			$('#updaterealname').val(user[0].realname);
		}
		if(user[0].telephone==""){
			$('#updatetelephone') .css({"color":"#aaaaaa"})
			$('#updatetelephone').val("请输入手机号");
		}else{
			$('#updatetelephone').val(user[0].telephone);
		}
		if(user[0].email==""){
			$('#updateemail') .css({"color":"#aaaaaa"})
			$('#updateemail').val("请输入邮箱");
		}else{
			$('#updateemail').val(user[0].email);
		}
		if(user[0].description==""){
			$('#updateuserdes') .css({"color":"#aaaaaa"})
			$('#updateuserdes').val("请输入描述");
		}else{
			$('#updateuserdes').val(user[0].description);
		}
		
		var datas = {"user_id":user[0].user_id}
		$.ajax({ 
	    	url: "user/queryRoleIDByUserID.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	    	data: datas,
	    	async : false,
	    	success: function(result){
			select=result.userid;
	      	}
	      });
		
		$('#updaterolelistone').datagrid({  
	        width: 'auto',                
	        striped: true,  
	        singleSelect : false,  
	        url:'role/queryRole.do',  
	        loadMsg:'数据加载中请稍后……',  
	        pagination: false,
	        queryParams:{page:1,rows:1000},
	        idField:'role_id',
	        method:'POST',
	        columns:[[  
				{field:'role_id',align:'center',title: '角色id',hidden:true}, 
				{field:'role_name',width:'34%',align:'center',title: '角色名称'}, 
				{field:'description',width:'35%',align:'center',title: '角色描述'},
				{field:'search',width:'32%',align:'center',title: '详细信息',formatter:function(value,rec){
					 var btn = '<input type="button"  onclick="showpower('+rec.role_id+')" value="查看详细" style="width:100%;height:30px">';  
		             return btn; 
				}}
	        ]],
	        onLoadSuccess:function(){
	        	$('#updaterolelistone').datagrid("selectRecord",select);
	        }
	    });
	}else{
		ZENG.msgbox.show("只能选择一个用户", 3, 2000); 
	}
}
function updatesaveuser(){
	var role= $('#updaterolelistone').datagrid('getSelections');
	if(role.length==0){
		ZENG.msgbox.show("请选择一个角色", 3, 2000);
	}else if(role.length==1){
	user= $('#userlist').datagrid('getSelections');
//	var updateusernamestr=$('#updateusername').val();
//	var updaterealnamestr=$('#updaterealname').val();
//	var updatepasswordstr=$('#updatepassword').val();
//	var updatecomfirmpassstr=$('#updatecomfirmpassword').val();
//	var updatetelephonestr=$('#updatetelephone').val();
//	var updateemailstr=$('#updateemail').val();
//	var updateuserdesstr=$('#updateuserdes').val();
	if($('#updateusername').css("color")=="rgb(170, 170, 170)"){ 
		var updateusernamestr="";
	}else{
		var updateusernamestr=$('#updateusername').val();
	}
//	if($('#updatepassword').css("color")=="rgb(170, 170, 170)"){
//		var updatepasswordstr="";
//	}else{
		var updatepasswordstr=$('#updatepassword').val();
//	}
//	if($('#updatecomfirmpassword').css("color")=="rgb(170, 170, 170)"){
//		var updatecomfirmpassstr="";
//	}else{
		var updatecomfirmpassstr=$('#updatecomfirmpassword').val();
//	}
	if($('#updaterealname').css("color")=="rgb(170, 170, 170)"){
		var updaterealnamestr="";
	}else{
		var updaterealnamestr=$('#updaterealname').val();
	}
	if($('#updatetelephone').css("color")=="rgb(170, 170, 170)"){
		var updatetelephonestr="";
	}else{
		var updatetelephonestr=$('#updatetelephone').val();
	}
	if($('#updateuserdes').css("color")=="rgb(170, 170, 170)"){
		var updateuserdesstr="";
	}else{
		var updateuserdesstr=$('#updateuserdes').val();
	}
	if($('#updateemail').css("color")=="rgb(170, 170, 170)"){
		var updateemailstr="";
	}else{
		var updateemailstr=$('#updateemail').val();
	}
//	if(updatepasswordstr=="！@#￥%……&*"){
//		updateusernamestr="";
//	}
	
	var datas = {"roleid":role[0].role_id,"userid":user[0].user_id,"username":updateusernamestr,"password":updatepasswordstr,"realname":updaterealnamestr,"telephone":updatetelephonestr,"description":updateuserdesstr,"email":updateemailstr};
	
	var nverify=null;
	if(updateusernamestr==user[0].username){
	}else{
	var datas1 = {"username":updateusernamestr};
	$.ajax({ 
    	url: "user/UserVerify.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded",
    	data: datas1,
    	async: false,
    	success: function(result){
		if(result.success==1){
			nverify=1;
		}
      	}
      });
	}
		if(nverify==1){
			ZENG.msgbox.show("用户名已经存在", 3, 2000);
			return;
		}else{
			if(updatepasswordstr.length>20){
				ZENG.msgbox.show("密码长度不能大于20", 3, 2000);
				return;
			}
			if(updaterealnamestr=="" ||updaterealnamestr.length>20){
				ZENG.msgbox.show("真实姓名不能为空并且长度不能大于20", 3, 1000);
				return;
			}
			if(checkZIFU(updaterealnamestr)){
				return;
			}
			if(updatetelephonestr!="" && teleflag==1 ){
				ZENG.msgbox.show("电话格式不正确", 3, 2000); 
				return;
			}
			if(updateemailstr!="" && emailflag==1 ){
				ZENG.msgbox.show("邮箱格式不正确", 3, 2000); 
				return;
			}
			if(updateuserdesstr.length>50){
						ZENG.msgbox.show("用户描述不能大于50", 3, 1000); 
						return;
			}
					if(updatepasswordstr==updatecomfirmpassstr){
						$("#saveuserbut").attr("disabled", "disabled");
						$.ajax({ 
					    	url: "user/updateUser.do", 
					    	type: "POST",
					    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
					    	data: datas,
					    	success: function(){
							ZENG.msgbox.show("修改成功", 4, 2000);
							setTimeout(function(){
								$('#updateuser').window('close');
								$('#userlist').datagrid('reload');
							},300);
					      	}
					      });
					}else{
						ZENG.msgbox.show("两次密码不一致", 3, 2000);
					}
					}
	}else{
		ZENG.msgbox.show("只能选择一个角色", 3, 2000);
	}
}


function deleteuser(){
	user= $('#userlist').datagrid('getSelections');
	if(user.length==0){
		ZENG.msgbox.show("请选择一个用户", 3, 2000);
	}else{
		$.messager.defaults = { ok: "是", cancel: "否" };
		 $.messager.confirm("操作提示", "您确定要执行操作吗?", function (data) {
	           if (data) {
					var userId = [];
					for(var i=0;i<user.length;i++){
						userId[i] = user[i].user_id;
					}
					var userName=[];
					for(var i=0;i<user.length;i++){
						if(thisUser == user[i].username){
							ZENG.msgbox.show("不能删除当前用户", 3, 2000);
							return;
						}
						userName[i]=user[i].username;
					}
					var datas = {"userId":userId,"userName":userName};
					$.ajax({ 
				    	url: "user/deleteUserByUserId.do", 
				    	type: "POST",
				    	contentType:"application/x-www-form-urlencoded",
				    	data: datas,
				    	success: function(){
							ZENG.msgbox.show("删除成功", 4, 2000);
							 $("#userlist").datagrid('reload');
				      	}
				      });
	           }else{
	        	   return;
	           }
		 });
		}
}

function datarefresh(){
	$('#searchname').textbox('setValue',"");
	$("#userlist").datagrid('reload',{
		"username" : ""
	});
}
function comsss(id,value){
	$('#'+id) .css({"color":"black"})
	if(value=="请输入登录名称"){
		$('#'+id).val("");
	}else if(value=="请输入密码"){
		$("input[id='password']").attr('type','password');
		$('#'+id).val("");
	}else if(value=="请输入确认密码"){
		$("input[id='comfirmpass']").attr('type','password');
		$('#'+id).val("");
	}else if(value=="请输入真实姓名"){
		$('#'+id).val("");
	}else if(value=="请输入手机号"){
		$('#'+id).val("");
	}else if(value=="请输入邮箱"){
		$('#'+id).val("");
	}else if(value=="请输入描述"){
		$('#'+id).val("");
	}
}



function comddd(id){
	if($('#'+id).val()==''){
		$('#'+id) .css({"color":"#aaaaaa"})
		if(id=="username"){
			$('#'+id).val("请输入登录名称");
		}else if(id=="password"){
			$("input[id='password']").attr('type','text');
			$('#'+id).val("请输入密码");
		}else if(id=="comfirmpass"){
			$("input[id='comfirmpass']").attr('type','text');
			$('#'+id).val("请输入确认密码");
		}else if(id=="realname"){
			$('#'+id).val("请输入真实姓名");
		}else if(id=="telephone"){
			$('#'+id).val("请输入手机号");
		}else if(id=="email"){
			$('#'+id).val("请输入邮箱");
		}else if(id=="userdes"){
			$('#'+id).val("请输入描述");
		}
		
//		
	}
	
}
function updatecomsss(id,value){
	$('#'+id) .css({"color":"black"})
	if(value=="请输入登录名称"){
		$('#'+id).val("");
	}else if(value=="!@#$%^&*"){
		$("input[id='updatepassword']").attr('type','password');
		$('#'+id).val("");
	}else if(value=="!@#$%^&*"){
		$("input[id='updatecomfirmpassword']").attr('type','password');
		$('#'+id).val("");
	}else if(value=="请输入真实姓名"){
		$('#'+id).val("");
	}else if(value=="请输入手机号"){
		$('#'+id).val("");
	}else if(value=="请输入邮箱"){
		$('#'+id).val("");
	}else if(value=="请输入描述"){
		$('#'+id).val("");
	}
}

function updatecomddd(id){
	if($('#'+id).val()==''){
		$('#'+id) .css({"color":"#aaaaaa"})
		if(id=="updateusername"){
			$('#'+id).val("请输入登录名称");
		}
		else if(id=="updatepassword"){
			$('#'+"updatepassword") .css({"color":"black"})
			$("input[id='updatepassword']").attr('type','password');
			$('#'+id).val("!@#$%^&*");
		}
		else if(id=="updatecomfirmpassword"){
			$('#'+"updatecomfirmpassword") .css({"color":"black"})
			$("input[id='updatecomfirmpassword']").attr('type','password');
			$('#'+id).val("!@#$%^&*");
		}
		else if(id=="updaterealname"){
			$('#'+id).val("请输入真实姓名");
		}else if(id=="updatetelephone"){
			$('#'+id).val("请输入手机号");
		}else if(id=="updateemail"){
			$('#'+id).val("请输入邮箱");
		}else if(id=="updateuserdes"){
			$('#'+id).val("请输入描述");
		}
		
//		
	}
	
}

function changeImg(){
	$("#searchimg").attr("src","img/searchchange.png");
	}
function changeImg2(){
	$("#searchimg").attr("src","img/search2.png");
	}


function searchUser(){
	 var grid = $('#userlist')
	    var options = grid.datagrid('getPager').data("pagination").options; 
	    var curr = options.pageNumber;  
		var userName = $('#searchname').val();
		$("#userlist").datagrid('reload',{"username":userName,"page":curr,"rows":10});
	}

function cancel(){
	$('#newuser').window('close');
	$('#newfuck input') .css({"color":"#aaaaaa"});
	$('#userdes') .css({"color":"#aaaaaa"});
	
}
function updatecancel(){
	$('#updateuser').window('close');
}

function refreshRole(){
	$('#rolelistone').datagrid({  
	    width: 'auto',                
	    striped: true,  
	    singleSelect : false,  
	    url:'role/queryRole.do',  
	    loadMsg:'数据加载中请稍后……',  
	    pagination: false,
	    queryParams:{page:1,rows:1000},
	    method:'POST',
	    rownumbers: false,  
	    columns:[[  
	        
			{field:'role_id',align:'center',title: '角色id',hidden:true}, 
			{field:'role_name',width:'34%',align:'center',title: '角色名称'}, 
			{field:'description',width:'35%',align:'center',title: '角色描述'},
			{field:'search',width:'32%',align:'center',title: '详细信息',formatter:function(value,rec){
				 var btn = '<input type="button"  onclick="showpower('+rec.role_id+')" value="查看详细" style="width:100%;height:30px">';  
	             return btn; 
			
			}}
			    
	    ]]  
    });
}
function updaterefreshRole(){
	$('#updaterolelistone').datagrid({  
	    width: 'auto',                
	    striped: true,  
	    singleSelect : false,  
	    url:'role/queryRole.do',  
	    loadMsg:'数据加载中请稍后……',  
	    pagination: false,
	    queryParams:{page:1,rows:1000},
	    method:'POST',
	    rownumbers: false,  
	    columns:[[  
	        
			{field:'role_id',align:'center',title: '角色id',hidden:true}, 
			{field:'role_name',width:'34%',align:'center',title: '角色名称'}, 
			{field:'description',width:'35%',align:'center',title: '角色描述'},
			{field:'search',width:'32%',align:'center',title: '详细信息',formatter:function(value,rec){
				 var btn = '<input type="button"  onclick="showpower('+rec.role_id+')" value="查看详细" style="width:100%;height:30px">';  
	             return btn; 
			
			}}
			    
	    ]]  
    });
}
function nameverify(){
	var usernamestr=$('#username').val();
	var datas = {"username":usernamestr};
	$.ajax({ 
    	url: "user/UserVerify.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded",
    	data: datas,
    	async: false,
    	success: function(result){
		if(result.success==1){
			ZENG.msgbox.show("登录名已存在", 3, 2000);
		}else{
		}
      	}
      });
}
function updatenameverify(){
	user= $('#userlist').datagrid('getSelections');
	var updateusernamestr=$('#updateusername').val();
	var datas = {"username":updateusernamestr};
	if(updateusernamestr==user[0].username){
		
	}else{
		$.ajax({ 
	    	url: "user/UserVerify.do", 
	    	type: "POST",
	    	contentType:"application/x-www-form-urlencoded",
	    	data: datas,
	    	success: function(result){
			if(result.success==1){
				ZENG.msgbox.show("登录名已存在", 3, 2000);
			}else{
				
			}
	      	}
	      });
	}
}
/**
 * 验证特殊字符
 */
var checkInput = function(str) {
	var pattern = /^[\w\u4e00-\u9fa5]+$/gi;
	if (pattern.test(str)) {
		return false;
	}
	return true;
   }
function init(){
	$.ajax({ 
    	url: "modulepower/queryModulePowerID.do", 
    	type: "POST",
    	contentType:"application/x-www-form-urlencoded;charset=UTF-8",
    	async: false,
    	success: function(result){
			for(var i=0;i<result.moduleList.length;i++){
				if(result.moduleList[i]==2){
					$("#addusertd").show();
				}
				if(result.moduleList[i]==3){
					$("#updateusertd").show();
				}
				if(result.moduleList[i]==4){
					$("#deleusertd").show();
				}
			}
      	}
      });

}