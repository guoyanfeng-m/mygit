package controller.modulepower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.modulepower.ModulePowerService;
import beans.modulepower.ModulePowerRoleResultBean;
import beans.modulepower.ModulepowerParametersBean;
import beans.user.UserBean;

@SuppressWarnings({"unchecked","rawtypes"})
@Controller
@Transactional
public class ModulePowerController {
	@Autowired
	private ModulePowerService modulpowerService;

	@RequestMapping("modulepower/queryModulePower.do")
	@ResponseBody
	public Map<String, Object> queryModulePower(
		 HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		int userid=ub.getUser_id();
		List<ModulePowerRoleResultBean> module=modulpowerService.queryModule(userid);
		List<Object> moduleList = new ArrayList<Object>();
		for(int i=0;i<module.size();i++){
			Map<String,List> tempMap = new HashMap<String,List>();
			ModulepowerParametersBean pb=new ModulepowerParametersBean();
			pb.setUserid(userid);
			pb.setModulename(module.get(i).getModule_name());
			List modulepower=modulpowerService.queryModulePower(pb);
			tempMap.put(module.get(i).getModule_name()+"-"+module.get(i).getModule_id(), modulepower);
			moduleList.add(tempMap);
		}
		map.put("moduleList", moduleList);
		return map;
	}
	
	@RequestMapping("/modulepower/queryModulePowerID.do")
	@ResponseBody
	public Map<String, Object> queryModulePowerID(
		 HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		int userid=ub.getUser_id();
		List<String> moduleList=modulpowerService.queryModulePowerID(userid);
		map.put("moduleList", moduleList);
		map.put("username", ub.getUsername());
		map.put("userId", ub.getUser_id());
		return map;
	}
}
