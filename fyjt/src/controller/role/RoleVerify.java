package controller.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.role.RoleService;
import beans.user.UserBean;

@Controller
@Transactional
public class RoleVerify {
	@Autowired
	private RoleService roleService;
	

	@RequestMapping("role/RoleVerify.do")
	@ResponseBody
	//角色名称验证
	public Map<String, Object> insertUser(String rolename,
//			String realname,
//			String password, String telephone,
//			String description,
//			String email,
//			int roleid,
			 HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		int creatid=ub.getUser_id();
		List<Integer> roleid=	roleService.queryRoleID(rolename,creatid);
		if(roleid.size()>0){
			map.put("success", "1");
			return map;
		}else{
			map.put("success", "0");
			return map;
		}
	}

	
}