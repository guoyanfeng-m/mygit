package controller.login;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.operationlog.OperationlogService;
import service.user.UserService;
import util.DES;
import beans.user.UserBean;

@Controller
@Transactional
public class UserLoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private OperationlogService operationlogService;
	//	private String successPage ;
	//	private String failurePage ;
	private String key="shuangqi";

	@RequestMapping(value="admin") 
	public String Index(HttpServletResponse response,HttpServletRequest request,Model model) throws IOException {
		return "admin/login/login";
	}

	
	@RequestMapping(value="login/userlogin") 
	public String UserLogin(HttpServletResponse response,HttpServletRequest request,Model model) throws IOException {
		System.out.println(request.getReader());
		String message = null;
		String username = request.getParameter("usernames");
		String password = request.getParameter("passwords");
		String newpass = null;
		if (StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
			return "admin/login/outTime";
		}
		try {
			newpass = DES.encryptDES(password, key);
		} catch (Exception e) {
			System.out.println("密码加密错误");
			e.printStackTrace();
		}
		List<Integer> users = userService.queryLoginUser(username, newpass);
		if (users.size() == 0) {
			message = "用户名或密码错误！";
			model.addAttribute("msg", message);
			return "admin/login/login";
		} else {
			UserBean userb = new UserBean();
			userb.setUser_id(users.get(0));
			userb.setUsername(username);
			request.getSession().setAttribute("user", userb);
			model.addAttribute("username", username);
			operationlog(userb.getUser_id().toString(),"登录",13); 
			return "admin/main/index";
		}
	}

	@RequestMapping("/login/userloginout.do")
	@ResponseBody
	public String UserLoginOut(
			HttpServletResponse response,
			HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		return "true";
	}

	public void operationlog(String userid,String operationName,Integer operationType) {
		operationlogService.insertOperationlog(userid,operationName,operationType,3);
	}
}