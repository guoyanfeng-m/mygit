<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>"><link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1" />
		<title>分组管理--用户管理</title>
	</head>
<body>

<form id="editNav" method="post" >
		<input type="hidden" name="id" value="${info.id }" />
		<table  class="table" style="width: 100%; line-height: 30px;" >
			<tr>
				<td style="text-align: center;">名称：</td>
				<td><input class="easyui-validatebox" type="text" name="name" value="${info.name }" data-options="required:true,missingMessage:'请填写名称！'" /></td>
			</tr>
			<tr>
				<td style="text-align: center;">地址：</td>
				<td><input class="easyui-validatebox" type="text" name="url" value="${info.url }" data-options="required:true,missingMessage:'请填写url！'" /></td>
			</tr>
			<tr>
				<td style="text-align: center;">所属菜单：</td>
				<td>
					<select id="parentMenu2"  name="pid" value="" style="width:200px;">
					</select>
				</td>
			</tr>
		</table>
</form>
<script type="text/javascript">
	$(function(){
		$.ajax({
			url:'nav/navParentList',
			success:function(data){
				$.each(data,function(i,opt){
					
					if( '${info.pid}'==opt.id ){
						$('#parentMenu2').append('<option value='+opt.id+' selected="selected" >'+opt.text+'</option>');
					}else{
						$('#parentMenu2').append('<option value='+opt.id+'>'+opt.text+'</option>');
					}
				});
			}
		});
		
		
		
	});
</script>
</body>
</html>
