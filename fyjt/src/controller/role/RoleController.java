package controller.role;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import service.operationlog.OperationlogService;
import service.role.RoleService;
import service.terminal.TerminalGroupService;
import service.terminal.TerminalTerminalGroupService;
import service.user.UserService;
import util.PageInfo;
import beans.role.ParametersRoleTerminalBean;
import beans.role.ParametersRoleTerminalGroupBean;
import beans.role.RoleBean;
import beans.role.RoleParametersBean;
import beans.role.RoleViewBean;
import beans.sys.TreeBeans;
import beans.terminal.TerminalTerminalGroupBean;
import beans.user.UserBean;

@SuppressWarnings({"unchecked","rawtypes"})
@Controller
@Transactional
public class RoleController {
	@Autowired
	private ConfigService configService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private OperationlogService operationlogService;
	
	@Autowired
	private TerminalTerminalGroupService terminalTerminalGroupService;
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	@RequestMapping(value="role/insertRole.do",method=RequestMethod.POST)  
	@ResponseBody
	//增加角色
	 public Map<String,Object> insertRole(
			 String rolename,
			 String roledes,
			 Integer schedulelevel,
			 HttpServletRequest request) {
		String[] selectId=request.getParameterValues("selectId[]");
		String[] checknodes=request.getParameterValues("checknodes[]");
		String[] indeterminates=request.getParameterValues("indeterminates[]");
		String[] selecthalf=request.getParameterValues("selecthalf[]");
		Timestamp createTime = new Timestamp(new Date().getTime());
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		RoleBean roleBean = new RoleBean();
		roleBean.setRole_name(rolename);
		roleBean.setDescription(roledes);
		roleBean.setDeleted(0);
		roleBean.setCreate_time(createTime);
		roleBean.setCreator_id(ub.getUser_id());
		roleBean.setSchedulelevel(schedulelevel);
		roleService.insertRole(roleBean);
		List<Integer> roleid=roleService.queryRoleID(rolename,ub.getUser_id());
		if(selecthalf==null){
			
		}else{
			RoleParametersBean pb1= new RoleParametersBean();
			pb1.setRole_id(roleid.get(0));
			pb1.setPower_id(Integer.toString(1));
			roleService.insertRolePower(pb1);
			for(int j=0;j<selecthalf.length;j++){
				RoleParametersBean pb= new RoleParametersBean();
				pb.setRole_id(roleid.get(0));
				pb.setPower_id(selecthalf[j]);
				roleService.insertRolePower(pb);
			}
		
		}
		
		
		if(selectId==null){
			RoleParametersBean pb1= new RoleParametersBean();
			pb1.setRole_id(roleid.get(0));
			pb1.setPower_id(Integer.toString(1));
			roleService.insertRolePower(pb1);
		}else{
			RoleParametersBean pb1= new RoleParametersBean();
			pb1.setRole_id(roleid.get(0));
			pb1.setPower_id(Integer.toString(1));
			roleService.insertRolePower(pb1);
			for(int j=0;j<selectId.length;j++){
				RoleParametersBean pb= new RoleParametersBean();
				pb.setRole_id(roleid.get(0));
				pb.setPower_id(selectId[j]);
				roleService.insertRolePower(pb);
			}
		
		}
		if(indeterminates==null){
			
		}else{
			for(int i=0;i<indeterminates.length;i++){
				ParametersRoleTerminalGroupBean prtgb= new ParametersRoleTerminalGroupBean();
				prtgb.setRole_id(roleid.get(0));
				prtgb.setTerminalgroup_id(Integer.parseInt(indeterminates[i]));
				roleService.insertRoleTerminalGroup(prtgb);
			}
		}
		
		if(checknodes==null){
			ParametersRoleTerminalGroupBean prtgb= new ParametersRoleTerminalGroupBean();
			prtgb.setRole_id(roleid.get(0));
			prtgb.setTerminalgroup_id(1);
			roleService.insertRoleTerminalGroup(prtgb);
		}else{
		for(int i=0;i<checknodes.length;i++){
			int k=Integer.parseInt(checknodes[i]);
			if(k>40000){
				ParametersRoleTerminalBean prtb= new ParametersRoleTerminalBean();
				prtb.setRole_id(roleid.get(0));
				prtb.setTerminal_id(Integer.parseInt(checknodes[i]));
				roleService.insertRoleTerminal(prtb);
			}else{
				ParametersRoleTerminalGroupBean prtgb= new ParametersRoleTerminalGroupBean();
				prtgb.setRole_id(roleid.get(0));
				prtgb.setTerminalgroup_id(Integer.parseInt(checknodes[i]));
				roleService.insertRoleTerminalGroup(prtgb);
			}
		}
		}
		Map<String,Object> map = new HashMap<String,Object>();  
		
		operationlog(ub.getUser_id().toString(),rolename,1);  
		map.put("success", "true");  
		return map;
	}
	
	@RequestMapping(value="/role/updateRole.do",method=RequestMethod.POST)  
	@ResponseBody
	//修改角色
	 public Map<String,Object> updateRole(
			 int  roleid,
			 String updaterolename,
			 String updateroledes,
			 Integer schedulelevel,
			 HttpServletRequest request) {
		String[] checknodes=request.getParameterValues("updatechecknodes[]");
		String[] selectId=request.getParameterValues("updateselectId[]");
		String[] indeterminates=request.getParameterValues("indeterminates[]");
		String[] updateselecthalf=request.getParameterValues("updateselecthalf[]");
		RoleBean roleBean = new RoleBean();
		roleBean.setRole_id(roleid);
		roleBean.setRole_name(updaterolename);
		roleBean.setDescription(updateroledes);
		roleBean.setSchedulelevel(schedulelevel);
		roleService.updateRoleByRoleId(roleBean);
		roleService.deleteTerminalByRoleId(roleid);
		roleService.deleteTerminalgroupByRoleId(roleid);
		if(indeterminates==null){
			
		}else{
			for(int i=0;i<indeterminates.length;i++){
				ParametersRoleTerminalGroupBean prtgb= new ParametersRoleTerminalGroupBean();
				prtgb.setRole_id(roleid);
				prtgb.setTerminalgroup_id(Integer.parseInt(indeterminates[i]));
				roleService.insertRoleTerminalGroup(prtgb);
			}
		}
		if(checknodes==null){
			ParametersRoleTerminalGroupBean prtgb= new ParametersRoleTerminalGroupBean();
			prtgb.setRole_id(roleid);
			prtgb.setTerminalgroup_id(1);
			roleService.insertRoleTerminalGroup(prtgb);
		}else{
		for(int i=0;i<checknodes.length;i++){
			int k=Integer.parseInt(checknodes[i]);
			if(k>40000){
				ParametersRoleTerminalBean prtb= new ParametersRoleTerminalBean();
				prtb.setRole_id(roleid);
				prtb.setTerminal_id(Integer.parseInt(checknodes[i]));
				roleService.insertRoleTerminal(prtb);
			}else{
				ParametersRoleTerminalGroupBean prtgb= new ParametersRoleTerminalGroupBean();
				prtgb.setRole_id(roleid);
				prtgb.setTerminalgroup_id(Integer.parseInt(checknodes[i]));
				roleService.insertRoleTerminalGroup(prtgb);
			}
		}
		}
		
		
		
		roleService.deletePowerByRoleId(roleid);
		if(updateselecthalf==null){
			
		}else{
			RoleParametersBean pb1= new RoleParametersBean();
			pb1.setRole_id(roleid);
			pb1.setPower_id(Integer.toString(1));
			roleService.insertRolePower(pb1);
			for(int i=0;i<updateselecthalf.length;i++){
				RoleParametersBean pb= new RoleParametersBean();
				pb.setRole_id(roleid);
				pb.setPower_id(updateselecthalf[i]);
				roleService.insertRolePower(pb);
			}
		
		}
		
		if(selectId==null){
			RoleParametersBean pb1= new RoleParametersBean();
			pb1.setRole_id(roleid);
			pb1.setPower_id(Integer.toString(1));
			roleService.insertRolePower(pb1);
		}else{
			RoleParametersBean pb1= new RoleParametersBean();
			pb1.setRole_id(roleid);
			pb1.setPower_id(Integer.toString(1));
			roleService.insertRolePower(pb1);
			for(int i=0;i<selectId.length;i++){
				RoleParametersBean pb= new RoleParametersBean();
				pb.setRole_id(roleid);
				pb.setPower_id(selectId[i]);
				roleService.insertRolePower(pb);
			}
		}
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Map<String,Object> map = new HashMap<String,Object>();  
		operationlog(ub.getUser_id().toString(),updaterolename,3); 
		map.put("success", "true");  
		return map;
	}
	
	
	@RequestMapping("/role/queryRole.do")
	@ResponseBody
	//角色列表
	public Map<String,Object>  queryAll( 
			String page,//当前页
			String rows,//一页显示条数
			String role_id,
			String role_name,
			Integer schedulelevel,
			HttpServletRequest request) {
		//////////////////////////////////////////////////////////////
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer creatorid =null;
		List <Integer>userids=new ArrayList();
		String elementPower = configService.queryConfig("elementPower");
		if (!ub.getUsername().equals("admin")) {
				if (elementPower.equals("1")) {
					creatorid=ub.getUser_id();
				} else if (elementPower.equals("2")) {
		            userids=userService.queryUserIdsBySameRole(ub.getUser_id());
				}
		}
		RoleBean roleBean = new RoleBean();
		if(role_id != null && role_id != ""){
			roleBean.setRole_id(Integer.parseInt(role_id));
		}
		if(role_name != null && role_name != ""){
			role_name = role_name.replaceAll(" ", "");
			roleBean.setRole_name(role_name);
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
		int total = roleService.queryRoleCount(userlist,roleBean,creatorid,userids);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		List<RoleViewBean> roleViewBeanList = new ArrayList<RoleViewBean>();
		roleViewBeanList = roleService.queryRole(userlist,roleBean,pageInfo,creatorid,userids);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", roleViewBeanList);//返回的要显示的数据 
		map.put("total", total); //总条数
		return map;
	}
	
	@RequestMapping(value="/role/deleteRoleByRoleId.do",method=RequestMethod.POST)
	@ResponseBody
	//删除角色
	public Map<String,Object>  deleteRoleByRoleId( 
			HttpServletRequest request) {
		String[] roleIds = request.getParameterValues("roleId[]");
		String[] rolename=request.getParameterValues("rolename[]");
		Integer[] roleId = new Integer[roleIds.length];
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Map<String,Object> map = new HashMap<String,Object>(); 
		for(int i=0;i<roleId.length;i++){
			roleId[i] = Integer.parseInt(roleIds[i]);
			List list=roleService.queryUserRoleByRoid(roleId[i]);
			if(list.size()>0){
				map.put("success", "false");  
			}else{
				RoleBean roleBean = new RoleBean();
				roleBean.setRole_id(Integer.parseInt(roleIds[i]));
				roleBean.setDeleted(1);
				operationlog(ub.getUser_id().toString(),rolename[i].toString(),2); 
				roleService.updateRoleByRoleId(roleBean);
				roleService.deletePowerByRoleId(roleId[i]);
				roleService.deleteTerminalByRoleId(roleId[i]);
				roleService.deleteTerminalgroupByRoleId(roleId[i]);
				map.put("success", "true");  
			}
		}
		return map;
	}
	
	@RequestMapping("/role/queryPowerByRoleID.do")
	@ResponseBody
	//根据角色id查权限
	public Map<String,Object>  queryPowerByRoleID( 
			int roleid,
			HttpServletRequest request) {
		List powerlist = roleService.queryPowerByRoleID(roleid);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("powerlist", powerlist);
		return map;
	}
	
	
	@RequestMapping("/role/queryTerminalByRoleID.do")
	@ResponseBody
	//根据角色id查终端
	public Map<String,Object>  queryTerminalByRoleID( 
			int roleid,
			HttpServletRequest request) {
		List terminallist = roleService.queryTerminalByRoleID(roleid);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("terminallist", terminallist);
		return map;
	}
	
	@RequestMapping("/role/queryTerminalGroupByRoleID.do")
	@ResponseBody
	//根据角色查终端组
	public Map<String,Object>  queryTerminalGroupByRoleID( 
			int roleid,
			HttpServletRequest request) {
		List<Integer> terminalgrouplist = roleService.queryTerminalGroupByRoleID(roleid);
		List<Integer> list=new ArrayList<Integer>();
		List<Integer> tglist=new ArrayList<Integer>();
		for(int i=0;i<terminalgrouplist.size();i++){
			List<String> terminalgid=new ArrayList<String>();
			terminalgid=terminalGroupService.queryTerminalGroupByParentID(terminalgrouplist.get(i));
			if(terminalgid.size()==0){
				list.add(terminalgrouplist.get(i));
			}
		}
		for(int i=0;i<list.size();i++){
			TerminalTerminalGroupBean tdb=new TerminalTerminalGroupBean();
			tdb.setGroupId(list.get(i));
			List<TerminalTerminalGroupBean> ttgb= terminalTerminalGroupService.queryTerminalTerminalGroupByTerminalId(tdb);
			if(ttgb.size()==0){
				tglist.add(list.get(i));
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("terminallist", tglist);
		return map;
	}
	
	
	
	@RequestMapping("/role/queryShowRolePower.do")
	@ResponseBody
	//查看详细中权限树
	public JSONArray queryShowRolePower(
			int roleid,
		 HttpServletRequest request) {
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = roleService.showrolepower(roleid);
		JSONArray tree = new JSONArray();  
		tree.addAll(treeBeansList);
		return tree;
	}
	//日志
	public void operationlog(String userid,String operationName,Integer operationType) {
		operationlogService.insertOperationlog(userid,operationName,operationType,3);
	}
	
	@RequestMapping("/role/queryScheduleLevelByRoleID.do")
	@ResponseBody
	//根据角色id查权限
	public Integer queryScheduleLevelByRoleID(HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer userids=userService.queryRoleIDByUserID(ub.getUser_id());
		Integer schedulelevel = roleService.queryScheduleLevelByRoleID(userids);
		return schedulelevel;
	}
	
}
    