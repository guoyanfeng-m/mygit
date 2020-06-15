package controller.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import service.operationlog.OperationlogService;
import service.user.UserService;
import util.DES;
import util.PageInfo;
import beans.user.UserBean;
import beans.user.UserParametersBean;
import beans.user.UserViewBean;

@SuppressWarnings({"unchecked","rawtypes"})
@Controller
@Transactional
public class UserController {
	@Autowired
	private ConfigService configService;
	@Autowired
	private UserService userService;
	@Autowired
	private OperationlogService operationlogService;
	
	private String key="shuangqi";
	@RequestMapping("user/insertUser.do")
	@ResponseBody
	//增加用户
	public Map<String, Object> insertUser(String username, String realname,
			String password, String telephone,
			String description,
			String email,
			int roleid,
			 HttpServletRequest request) {
		Timestamp createTime = new Timestamp(new Date().getTime());
		UserBean userBean = new UserBean();
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		userBean.setUsername(username);
		userBean.setRealname(realname);
		String newpass = null;
		try {
			 newpass=DES.encryptDES(password,key);
		} catch (Exception e) {
			System.out.println("密码加密错误");
		}
		userBean.setPassword(newpass);
		userBean.setTelephone(telephone);
		userBean.setEmail(email);
		userBean.setDescription(description);
		userBean.setCreator_id(ub.getUser_id());
		userBean.setDeleted(0);
		userBean.setCreate_time(createTime);
		userService.insertUser(userBean);
	//	List<Integer> userid=	userService.queryUserID(username,ub.getUser_id());
		List<Integer> userid=	userService.queryUserID(username);
		UserParametersBean pb=new UserParametersBean();
		pb.setRole_id(roleid);
		pb.setUser_id(userid.get(0));
		userService.insertUserRole(pb);
		Map<String, Object> map = new HashMap<String, Object>();
		operationlog(ub.getUser_id().toString(),username,1); 
		map.put("success", "true");
		return map;
	}

	@RequestMapping("/user/queryUser.do")
	@ResponseBody
	//用户列表
	public Map<String,Object>  queryAll( 
			String page,//当前页
			String rows,//一页显示条数
			String user_id,
			String username,
			HttpServletRequest request) {
		UserBean userBean = new UserBean();
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		userBean.setUser_id(ub.getUser_id());
		if(username != null && username != ""){
			username = username.replaceAll(" ", "");
			userBean.setUsername(username);
		}
		int newpage = 1;
		int newrows = 0;
		if(page!=null&&page!=""&&!page.equals("0")){
			newpage = Integer.parseInt(page);
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		List<String> list=userService.queryChildUserID(ub.getUser_id().toString());
		List<String> userlist=new ArrayList<String>();
		 userlist.add(ub.getUser_id().toString());
		 userlist.addAll(list);
		
		 while(true){
			 list=userService.queryChilds(list);
			 if(list.size()==0){
				 break;
			 }else{
				userlist.addAll(list); 
			 }
		 }
		//////////////////////////////////////////////////////////////
		 Integer creatorid=null;
		 List<Integer>userids=new ArrayList();
		String elementPower = configService.queryConfig("elementPower");
		if (!ub.getUsername().equals("admin")) {
			if (elementPower.equals("1")) {
				creatorid=ub.getUser_id();
			} else if (elementPower.equals("2")) {
				userids=userService.queryUserIdsBySameRole(ub.getUser_id());
			}
		}
	    int total =userService.queryUserCount(userlist,userBean,creatorid,userids);
//		int total =userlist.size();
	    PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		List<UserViewBean> userViewBeanList = new ArrayList<UserViewBean>();
		userViewBeanList = userService.queryUser(userlist,userBean,pageInfo,creatorid,userids);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", userViewBeanList);//返回的要显示的数据 
		map.put("total", total); //总条数
		return map;
	}
	
	
	@RequestMapping("/user/queryRoleIDByUserID.do")
	@ResponseBody
	//根据用户id查角色
	public Map<String,Object>  queryRoleIDByUserID( 
			int user_id,
			HttpServletRequest request) {
		int userid=userService.queryRoleIDByUserID(user_id);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("userid", userid);
		return map;
	}
	
	@RequestMapping(value="/user/updateUser.do",method=RequestMethod.POST)  
	@ResponseBody
	//修改用户
	 public Map<String,Object> updateUser(
			 	String username, String realname,
				String password, String telephone,
				String description,String email,
				int userid,int roleid,
			 HttpServletRequest request) {
		UserBean userBean = new UserBean();
		userBean.setUser_id(userid);
		userBean.setUsername(username);
		userBean.setRealname(realname);
		if(password.equals("!@#$%^&*")){
			userBean.setPassword(null);
		}else{
			String newpass = null;
			try {
				 newpass=DES.encryptDES(password,key);
			} catch (Exception e) {
				System.out.println("密码加密错误");
			}
			userBean.setPassword(newpass);
		}
		userBean.setTelephone(telephone);
		userBean.setEmail(email);
		userBean.setDescription(description);
		userService.updateUserByUserId(userBean);
		UserParametersBean pb=new UserParametersBean();
		pb.setRole_id(roleid);
		pb.setUser_id(userid);
		userService.updateUserRoleByUserID(pb);
		Map<String,Object> map = new HashMap<String,Object>();  
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		operationlog(ub.getUser_id().toString(),username,3);
		map.put("success", "true");
		return map;
	
	}
	
	
	@RequestMapping(value="/user/deleteUserByUserId.do",method=RequestMethod.POST)
	@ResponseBody
	//删除用户
	public Map<String,Object>  deleteUserByUserId( 
			HttpServletRequest request) {
		String[] userIds = request.getParameterValues("userId[]");
		String[] userNames = request.getParameterValues("userName[]");
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer[] userId = new Integer[userIds.length];
		for(int i=0;i<userId.length;i++){
			userId[i] = Integer.parseInt(userIds[i]);
			UserBean userBean = new UserBean();
			userBean.setUser_id(userId[i]);
			userBean.setDeleted(1);
			userService.updateUserByUserId(userBean);
			
			userService.delRoleUser(userId[i]); //删除用户角色关系表user_role
			
			operationlog(ub.getUser_id().toString(),userNames[i],2);
		}
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "true");  
		return map;
	}
	
	
	@RequestMapping(value="/user/updateUserPass.do",method=RequestMethod.POST)  
	@ResponseBody
	//右上角修改密码
	 public Map<String,Object> updateUserPass(
				String password,
			 HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		UserBean userBean = new UserBean();
		userBean.setUser_id(ub.getUser_id());
		String newpass = null;
		try {
			 newpass=DES.encryptDES(password,key);
		} catch (Exception e) {
			System.out.println("密码加密错误");
		}
		userBean.setPassword(newpass);
		userService.updateUserByUserId(userBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "true");
		return map;
	
	}
	//日志
	public void operationlog(String userid,String operationName,Integer operationType) {
		operationlogService.insertOperationlog(userid,operationName,operationType,2);
}
}