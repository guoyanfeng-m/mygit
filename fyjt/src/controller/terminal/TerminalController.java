package controller.terminal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.collections.list.SetUniqueList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.model.ModelService;
import service.operationlog.OperationlogService;
import service.program.ProgramService;
import service.terminal.TerminalGroupService;
import service.terminal.TerminalService;
import service.terminal.TerminalStatusService;
import service.user.UserService;
import util.PageInfo;
import beans.program.ProgramTerminalBean;
import beans.sys.SystemConstant;
import beans.sys.TreeBeans;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalGroupBean;
import beans.terminal.TerminalViewBean;
import beans.user.UserBean;
@Controller
@Transactional
public class TerminalController {
	@Autowired
	private TerminalGroupService terminalGroupService;
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private ProgramService programService;
	@Autowired
	private UserService userService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private TerminalStatusService terminalStatusService;
	@Autowired
	private OperationlogService operationlogService;
	
	public void operationlog(String userid,String operationName,Integer operationType) {
		operationlogService.insertOperationlog(userid,operationName,operationType,SystemConstant.TERMINAL_ID);
	}
	/**
	 * <p>          
	 *       <discription> 概述：终端入库 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2016年12月28日 下午4:29:55
	 * @UpdateDate     更新时间：   2016年12月28日 下午4:29:55
	 * @Package_name   包名：          iis/controller.terminal
	 * @Param          参数：          @param terminal_name
	 * @Param          参数：          @param ip
	 * @Param          参数：          @param mac
	 * @Param          参数：          @param terminalGroups
	 * @Param          参数：          @param is_verify
	 * @Param          参数：          @param request
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          Map<String,Object>
	 */
	@RequestMapping(value="terminal/insertTerminal.do")  
	@ResponseBody
	 public Map<String,Object> insertTerminal(
			 String terminal_name,
			 String ip,
			 String mac,
			 @RequestParam(value="terminalGroups[]")String []terminalGroups,
			 String is_verify,
			 HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer userId = ub.getUser_id();
		Integer createrId= userService.queryParentUserID(ub.getUser_id());
		List<Integer> userlist=new ArrayList<Integer>();
		if(createrId!=0){
			userlist.add(createrId);
		}
		userlist.add(userId);
		 while(true){
			 if(createrId==0){
				 break;
			 }else{
				 createrId=userService.queryParentUserID(createrId);
				 userlist.add(createrId); 
			 }
		 }
		List<Integer> terminalgrouplist=new ArrayList<Integer>();
		for (int i = 0; i < terminalGroups.length; i++) {
			terminalgrouplist.add(Integer.parseInt(terminalGroups[i]));
		}
		Timestamp createTime = new Timestamp(new Date().getTime());
		TerminalBean terminalBean = new TerminalBean();
		terminalBean.setAudit_status(modelService.queryModuleAudit(SystemConstant.TERMINAL_ID)+"");
		terminalBean.setCreate_time(createTime);
		terminalBean.setCreator_id(userId);
		terminalBean.setDeleted("0");
		terminalBean.setIp(ip);
		terminalBean.setIs_verify("1");
		terminalBean.setMac(mac);
		terminalBean.setTerminal_name(terminal_name);
		List<String> macList = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		macList = terminalService.queryTerminalMac(terminalBean);
		nameList = terminalService.queryTerminalName(terminalBean);
		List<String> strList=new ArrayList<String>();
		strList=terminalService.queryTerminalMacIp(terminalBean); 
		Map<String,Object> map = new HashMap<String,Object>();  
		if(macList.size()>0){
			map.put("success", false); 
			map.put("msg", "终端唯一标识已存在，请重新输入！");
			return map;
		}
		if(nameList.size()>0){
			map.put("success", false); 
			map.put("msg", "终端名称已存在，请重新输入！");
			return map;
		}
		if(strList.size()>0){
			terminalService.deleteTerminalByIpMac(terminalBean);
		}
		terminalService.insertTerminal(terminalBean,userlist,terminalgrouplist);
		operationlog(userId.toString(),terminal_name,SystemConstant.INSERT_OPERATION);
		map.put("success", true);  
		return map;
	}
	/**
	 * <p>          
	 *       <discription> 概述：终端管理页面数据初始化 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2016年12月28日 上午10:36:30
	 * @UpdateDate     更新时间：   2016年12月28日 上午10:36:30
	 * @Package_name   包名：          iis/controller.terminal
	 * @Param          参数：          @param page
	 * @Param          参数：          @param rows
	 * @Param          参数：          @param terminal_id
	 * @Param          参数：          @param terminal_name
	 * @Param          参数：          @param deleted
	 * @Param          参数：          @param is_verify
	 * @Param          参数：          @param terminalGroupId
	 * @Param          参数：          @param request
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          Map<String,Object>
	 */
	@RequestMapping("/terminal/queryTerminal.do")
	@ResponseBody
	public Map<String,Object>  queryTerminal( 
			String page,
			String rows,
			String terminal_id,
			String terminal_name,
			String deleted,
			String is_verify,
			String terminalGroupId,
			HttpServletRequest request) {
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		List<String> list=userService.queryChildUserID(user.getUser_id().toString());
		List<String> userlist=new ArrayList<String>();
		userlist.add(user.getUser_id().toString());
		userlist.addAll(list);
		while(true){
			 list=userService.queryChilds(list);
			 if(list.size()==0){
				 break;
			 }else{
				userlist.addAll(list); 
			 }
		}
		if(terminalGroupId!= null && terminalGroupId != "" && (terminal_name == null || terminal_name.equals(""))){
			int newpage = 1;
			int newrows = 0;
			if(page!=null&&page!=""&&!page.equals("0")){
				newpage = Integer.parseInt(page);
			}
			if(rows!=null&&rows!=""){
				newrows = Integer.parseInt(rows);
			}
			int total = terminalService.queryTerminalTotleByGroupId(Integer.parseInt(terminalGroupId),userlist);
			PageInfo pageInfo = new PageInfo();
			pageInfo.setRows(newrows);
			pageInfo.setPage(newpage);
			pageInfo.setTotal(total);
			List<TerminalViewBean> terminalViewBeanList = new ArrayList<TerminalViewBean>();
			terminalViewBeanList = terminalService.queryTerminalByGroupId(Integer.parseInt(terminalGroupId),pageInfo,userlist);
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("rows", terminalViewBeanList);    
			map.put("total", total);    
			map.put("page", 1); //当前页数
			return map;
		}else if(terminalGroupId!= null && terminalGroupId != "" &&terminal_name != null &&  !terminal_name.equals("")){
			int newpage = 1;
			int newrows = 0;
			if(page!=null&&page!=""&&!page.equals("0")){
				newpage = Integer.parseInt(page);
			}
			if(rows!=null&&rows!=""){
				newrows = Integer.parseInt(rows);
			}
			if(terminal_name != null && !terminal_name.equals("")){
				terminal_name = terminal_name.replaceAll(" ", "");
			}
			if(terminal_name == null || !terminal_name.equals("")){
				terminal_name = null;
			}
			int total = terminalService.queryTerminalNameTotleByGroupId(Integer.parseInt(terminalGroupId),userlist,terminal_name);
			PageInfo pageInfo = new PageInfo();
			pageInfo.setRows(newrows);
			pageInfo.setPage(newpage);
			pageInfo.setTotal(total);
			List<TerminalViewBean> terminalViewBeanList = new ArrayList<TerminalViewBean>();
			terminalViewBeanList = terminalService.queryTerminalNameByGroupId(Integer.parseInt(terminalGroupId),pageInfo,userlist,terminal_name);
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("rows", terminalViewBeanList);    
			map.put("total", total);    
			map.put("page", 1); //当前页数
			return map;
		}else{
			int newpage = 1;
			int newrows = 0;
			if(page!=null&&page!=""&&!page.equals("0")){
				newpage = Integer.parseInt(page);
			}
			if(rows!=null&&rows!=""){
				newrows = Integer.parseInt(rows);
			}
			TerminalBean terminalBean = new TerminalBean();
			if(terminal_name != null && terminal_name != ""){
				terminal_name = terminal_name.replaceAll(" ", "");
			}
			if(terminal_name == null || terminal_name == ""){
				terminal_name = null;
			}
			terminalBean.setTerminal_name(terminal_name);
			if(terminal_id != null && terminal_id != ""){
				terminalBean.setTerminal_id(Integer.parseInt(terminal_id));
			}
			if(deleted != null && deleted != ""){
				terminalBean.setDeleted(deleted);
			}
			if(is_verify != null && is_verify != ""){
				terminalBean.setIs_verify(is_verify);
			}
			Integer total = terminalService.queryTerminalCount(terminalBean,userlist);
			PageInfo pageInfo = new PageInfo();
			pageInfo.setRows(newrows);
			pageInfo.setPage(newpage);
			pageInfo.setTotal(total);
			List<TerminalViewBean> terminalBeanViewList = new ArrayList<TerminalViewBean>();
			terminalBeanViewList = terminalService.queryTerminal(terminalBean,pageInfo,userlist);
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("rows", terminalBeanViewList);//返回的要显示的数据 
			map.put("total", total); //总条数
			return map;
		}
	}
	
	
	@RequestMapping("/terminal/queryBdTerminal.do")
	@ResponseBody
	public Map<String,Object>  queryTerminal(
			String page,
			String rows,
			HttpServletRequest request){
		List<TerminalViewBean> terminalBeanViewList = new ArrayList<TerminalViewBean>();
		int newpage = 1;
		int newrows = 0;
		if(page!=null&&page!=""&&!page.equals("0")){
			newpage = Integer.parseInt(page);
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		int total = terminalService.queryBdTerminalCount();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		terminalBeanViewList = terminalService.queryBdTerminal(pageInfo);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", terminalBeanViewList);//返回的要显示的数据 
		map.put("total", total); //总条数
		return map;
		
	}
	
	
	
	@RequestMapping("/terminal/queryTerminalTree.do")
	@ResponseBody
	public JSONArray  queryTerminalTree( 
			HttpServletRequest request) {
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = terminalService.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(treeBeansList);
		return tree;
	}
	
	/**
	 * @Description 通过用户id查询该角色下的终端组并且生成树 (终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年2月22日下午3:53:50
	 * @param request
	 * @return
	
	@RequestMapping("/terminal/selectTerminalTreeByUserId.do")
	@ResponseBody
	public JSONArray selectTerminalTreeByUserId(HttpServletRequest request){
		// 获取用户id 来获取用户角色下的终端组
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Integer userId = user.getUser_id();
		// 生成树
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = terminalService.selectTerminalTreeByUserId(userId);
		JSONArray tree = new JSONArray();
		tree.addAll(treeBeansList);
		return tree;
	} */
	
	/**
	 * @Description 获取用户下的所属终端组(终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年3月3日下午2:43:58
	 * @param request
	 * @return
	 
	@RequestMapping("terminal/queryTerminalGroupByUserId.do")
	@ResponseBody
	public JSONArray queryTerminalGroupByUserId(HttpServletRequest request){
		// 获取用户id 来获取用户角色下的终端组
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Integer userId = user.getUser_id();
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalService.selectTerminalTreeByUserIds(userId);
		JSONArray terminalGroup = new JSONArray();
		terminalGroup.addAll(terminalGroupBeanList);
		return terminalGroup;
	}*/
	
	@RequestMapping("/terminal/queryTerminalGroup.do")
	@ResponseBody
	public JSONArray  queryTerminalGroup( 
			String terminalGroupId,
			HttpServletRequest request) {
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		if(terminalGroupId != null && terminalGroupId != ""){
			terminalGroupBean.setT_id(Integer.parseInt(terminalGroupId));
		}
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalService.queryTerminalGroup(terminalGroupBean);
		JSONArray terminalGroup = new JSONArray();  
		terminalGroup.addAll(terminalGroupBeanList);
		return terminalGroup;
	}
	/**
	 * <p>          
	 *       <discription> 概述：查找终端组 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2016年12月22日 下午5:34:42
	 * @UpdateDate     更新时间：   2016年12月22日 下午5:34:42
	 * @Package_name   包名：          iis/controller.terminal
	 * @Param          参数：          @param terminalGroupId
	 * @Param          参数：          @param request
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          JSONArray
	 */
	@RequestMapping("terminal/queryTerminalGroups.do")
	@ResponseBody
	public JSONArray  queryTerminalGroups( 
			String terminalGroupId,
			HttpServletRequest request) {
		UserBean userBean=(UserBean) request.getSession().getAttribute("user");
		Integer userId=userBean.getUser_id();
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = terminalService.queryTerminalGroups(userId);
		JSONArray tree = new JSONArray();  
		tree.addAll(treeBeansList);
		return tree;
	}
	
	@RequestMapping(value="terminal/deleteTerminalByTerminalId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<String,Object>  deleteTerminalByTerminalId( 
			@RequestParam("terminalId[]")String[] terminalIds,
//			String[] groupIds,
			String terminalName,
			HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();  
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer userId = ub.getUser_id();
		if (terminalIds.length==0) {
			map.put("success", "false"); 
			map.put("msg", "请正确选择删除终端项！");
			return map;
		}
		//终端转集合
		List<Integer>terminalIdList=SetUniqueList.decorate(new ArrayList<Integer>());
		for (int s = 0; s < terminalIds.length; s++) {
			terminalIdList.add(Integer.parseInt(terminalIds[s]));
		}
		//终端组转集合
//		List<Integer>groupIdList=SetUniqueList.decorate(new ArrayList<Integer>());
//		for (int k = 0; k < groupIds.length; k++) {
//			groupIdList.add(Integer.parseInt(groupIds[k]));
//		}
		ProgramTerminalBean programTerminal=new ProgramTerminalBean();
		programTerminal.setProgram_id(null);
		programTerminal.setPublishTime(null);
		programTerminal.setTerminalIds(terminalIdList);
		if(programService.queryByTerminalId(programTerminal).size()>0){
			map.put("success", "false"); 
			map.put("msg", "终端正在播放中,请先停止任务再删除！");
		}else{
			TerminalBean terminalBean = new TerminalBean();
			terminalBean.setDeleted("1");
			terminalBean.setTerminalIdList(terminalIdList);
//			terminalBean.setGroupIdList(groupIdList);
			terminalService.deleteTerminalByTerminalId(terminalBean);
			String terminalNames=terminalName.substring(0,terminalName.length()-1).toString();
			operationlog(userId.toString(),terminalNames,SystemConstant.DELETE_OPERATION);
			map.put("success", "true"); 
		}
		return map;
	}
	/**
	 * <p>          
	 *       <discription> 概述：终端编辑 </discription>
	 * </p>  
	 * @Author         创建人：      
	 * @CreateDate     创建时间：   
	 * @UpdateDate     更新时间：   
	 * @Package_name   包名：          iis/controller.terminal
	 * @Param          参数：          @param terminalId
	 * @Param          参数：          @param groupId
	 * @Param          参数：          @param terminalGroups
	 * @Param          参数：          @param terminalName
	 * @Param          参数：          @param ip
	 * @Param          参数：          @param mac
	 * @Param          参数：          @param request
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          Map<String,Object>
	 */
	@RequestMapping(value="terminal/updateTerminalByTerminalId.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  updateTerminalByTerminalId( 
			String terminalId,
			String groupId,
			@RequestParam("terminalGroups[]") String [] terminalGroups,
			String terminalName,
			String ip,
			String mac,
			HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Integer> terminalgrouplist=new ArrayList<Integer>();
		for (int i = 0; i < terminalGroups.length; i++) {
			terminalgrouplist.add(Integer.parseInt(terminalGroups[i]));
		}
		TerminalBean terminalBean = new TerminalBean();
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		Integer userId = user.getUser_id();
		if(terminalId != null && terminalId != ""){
			terminalBean.setTerminal_id(Integer.parseInt(terminalId));
		}
		terminalBean.setGroupIdList(terminalgrouplist);
		terminalBean.setAudit_status(modelService.queryModuleAudit(SystemConstant.TERMINAL_ID)+"");
		terminalBean.setTerminal_name(terminalName);
		terminalBean.setIp(ip);
		terminalBean.setMac(mac);
		List<String> macList = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		macList = terminalService.queryTerminalMac(terminalBean);
		nameList = terminalService.queryTerminalName(terminalBean);
		if(macList.size()>0){
			map.put("success", false); 
			map.put("msg", "终端唯一标识已存在，请重新输入！");
			return map;
		}
		if(nameList.size()>0){
			map.put("success", false); 
			map.put("msg", "终端名称已存在，请重新输入！");
			return map;
		}
		terminalService.updateTerminalByTerminalId(terminalBean);
		operationlog(userId.toString(),terminalName,SystemConstant.UPDATE_OPERATION);
		map.put("success", true);  
		return map;
	}
	@RequestMapping(value="terminal/updateTerminalAuditByTerminalId.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  updateTerminalAuditByTerminalId( 
			String audit_status,
			HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer user_id = ub.getUser_id();
		String[] terminal_ids = request.getParameterValues("terminal_id[]");
		for(String terminal_id:terminal_ids){
			TerminalBean terminalBean = new TerminalBean();
			terminalBean.setTerminal_id(Integer.parseInt(terminal_id));
			terminalBean.setAudit_status(audit_status);
			terminalService.updateTerminalAuditByTerminalId(terminalBean);
			TerminalBean terminalBeanName = new TerminalBean();
			terminalBeanName.setTerminal_id(Integer.parseInt(terminal_id));
			String terminalName = terminalService.queryTerminalWithoutPageById(terminalBeanName).getTerminal_name();
			operationlog(user_id.toString(),terminalName,SystemConstant.AUDTI_OPERATION);
		}
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	
	@RequestMapping(value="terminal/updateBdTerminalByTerminalId.do")
	@ResponseBody
	public Map<String,Object>  updateBdTerminalByTerminalId( 
			String terminalId,
			String terminalName,
			@RequestParam(value="terminalGroups[]")String []terminalGroups,
			String ip,
			String mac,
			String is_verify,
			HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>(); 
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Integer user_id = ub.getUser_id();
		Integer createrId= userService.queryParentUserID(ub.getUser_id());
		List<Integer> userlist=new ArrayList<Integer>();
		if(!createrId.equals("0")){
			userlist.add(createrId);
		}
		userlist.add(user_id);
		 while(true){
			 if(createrId==0){
				 break;
			 }else{
				 createrId=userService.queryParentUserID(createrId);
				 userlist.add(createrId); 
			 }
		 }
		List<Integer> terminalgrouplist=new ArrayList<Integer>();
		if (terminalGroups.length==0) {
			map.put("success", false);  
			return map;
		}
		for (int i = 0; i < terminalGroups.length; i++) {
			terminalgrouplist.add(Integer.parseInt(terminalGroups[i]));
		}
		TerminalBean terminalBean = new TerminalBean();
		if(terminalId != null && terminalId != ""){
			terminalBean.setTerminal_id(Integer.parseInt(terminalId));
		}
		terminalBean.setAudit_status(modelService.queryModuleAudit(SystemConstant.TERMINAL_ID)+"");
		terminalBean.setTerminal_name(terminalName);
		terminalBean.setIp(ip);
		terminalBean.setMac(mac);
		terminalBean.setCreate_time(new Timestamp(new Date().getTime()));
		terminalBean.setIs_verify(is_verify);
		terminalBean.setCreator_id(ub.getUser_id());
		terminalBean.setGroupIdList(terminalgrouplist);
		terminalService.updateBdTerminalByTerminalId(terminalBean,userlist);
		operationlog(user_id.toString(),terminalName,SystemConstant.INSERT_OPERATION);
		map.put("success", true);  
		return map;
	}
}
    