package controller.roletree;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.roletree.RoleTreeService;
import beans.roletree.RoletreeParametersBean;
import beans.sys.TreeBeans;
import beans.terminal.TerminalGroupBean;
import beans.user.UserBean;

@SuppressWarnings({"unchecked","unused"})
@Controller
@Transactional
public class RoleTreeController {
	@Autowired
	private RoleTreeService roletreeService;

	@RequestMapping("roletree/queryRoleTree.do")
	@ResponseBody
	//根据当前登陆用户找相应角色找相应终端
	public JSONArray queryRoleTredd(
		 HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		RoletreeParametersBean pb= new RoletreeParametersBean();
		pb.setUserid(ub.getUser_id());
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = roletreeService.queryRoleTerminalgroup(pb);
		JSONArray tree = new JSONArray();  
		tree.addAll(treeBeansList);
		return tree;
	
	}
	
	
	@RequestMapping("/roletree/queryRoleTreeByRoleID.do")
	@ResponseBody
	//根据角色id找终端
	public JSONArray queryRoleTreeByRoleID(
			int roleid,
		 HttpServletRequest request) {
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = roletreeService.queryRoleTerminalByRoleID(roleid);
		JSONArray tree = new JSONArray();  
		tree.addAll(treeBeansList);
		return tree;
	
	}
}
