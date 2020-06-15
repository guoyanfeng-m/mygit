package controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.user.UserService;
import util.DES;
import beans.user.UserBean;

@Controller
@Transactional
public class UserVerify {
	@Autowired
	private UserService userService;
	private String key="shuangqi";

	/**
	 * <p>          
	 *       <discription> 概述：用户名称验证 </discription>
	 * </p>  
	 * @Author         创建人：       
	 * @CreateDate     创建时间：   
	 * @UpdateDate     更新时间：   
	 * @Package_name   包名：          iis/controller.user
	 * @Param          参数：          @param username
	 * @Param          参数：          @param request
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          Map<String,Object>
	 */
	@SuppressWarnings("unused")
	@RequestMapping("user/UserVerify.do")
	@ResponseBody
	public Map<String, Object> UsernameVerify(String username,
//			String realname,
//			String password, String telephone,
//			String description,
//			String email,
//			int roleid,
			 HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		int creatid=ub.getUser_id();
		Map<String, Object> map = new HashMap<String, Object>();
	//	List<Integer> userid=	userService.queryUserID(username,creatid);
		List<Integer> userid=	userService.queryUserID(username);
		if(userid.size()>0){
			map.put("success", "1");
			return map;
		}else{
			map.put("success", "0");
			return map;
		}
		
	
	}

	
	@RequestMapping("/user/UserPassVerify.do")
	@ResponseBody
	//修改密码验证
	public Map<String, Object> UserPassVerify(String oldpassword,
			 HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String newpass=null;
		try {
			newpass = DES.encryptDES(oldpassword, key);
		} catch (Exception e) {
			System.out.println("密码加密错误");
		}
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		
		List<Integer> users = userService.queryLoginUser(ub.getUsername(), newpass);
		if(users.size()>0){
			map.put("success", "1");
			return map;
		}else{
			map.put("success", "0");
			return map;
		}
		
	
	}

	
}