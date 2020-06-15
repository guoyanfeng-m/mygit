package controller.scrollingnews;

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
import service.user.UserService;
import beans.roletree.RoletreeParametersBean;
import beans.sys.TreeBeans;
import beans.terminal.TerminalGroupBean;
import beans.user.UserBean;

@SuppressWarnings({"unchecked","unused"})
@Controller
@Transactional
public class TerminalTree {
	@Autowired
	private RoleTreeService roletreeService;
	@Autowired
	private UserService userService;
    /**
     * <p>          
     *       <discription> 概述：终端树 </discription>
     * </p>  
     * @Author         创建人：       RYL
     * @CreateDate     创建时间：   2016年12月22日 下午5:18:28
     * @UpdateDate     更新时间：   2016年12月22日 下午5:18:28
     * @Package_name   包名：          iis/controller.scrollingnews
     * @Param          参数：          @param request
     * @Param          参数：          @return  
     * @Rerurn         返回：          JSONArray
     */
	@RequestMapping("scrollingnews/queryTree.do")
	@ResponseBody
	public JSONArray queryTree(
		 HttpServletRequest request) {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		int roleid=userService.queryRoleIDByUserID(user.getUser_id());
		RoletreeParametersBean pb= new RoletreeParametersBean();
		pb.setUserid(1);
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = roletreeService.queryRoleTerminalByRoleID(roleid);
		JSONArray tree = new JSONArray();  
		tree.addAll(treeBeansList);
		return tree;
	
	}
}
