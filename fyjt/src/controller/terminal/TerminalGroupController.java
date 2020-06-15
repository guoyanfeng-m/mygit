package controller.terminal;

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

import service.operationlog.OperationlogService;
import service.terminal.TerminalGroupService;
import service.terminal.TerminalTerminalGroupService;
import beans.sys.SystemConstant;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalGroupBean;
import beans.user.UserBean;


@Controller
@Transactional
public class TerminalGroupController {
	@Autowired
	private TerminalGroupService terminalGroupService;
	@Autowired
	private OperationlogService operationlogService;
	@Autowired
	private TerminalTerminalGroupService terminalTerminalGroupService;
	
	public void operationlog(String userid,String operationName,Integer operationType) {
		operationlogService.insertOperationlog(userid,operationName,operationType,SystemConstant.TERMINAL_ID);
	}
	
	public String getTerminalGroupName(String terminalGroupId){
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setT_id(Integer.parseInt(terminalGroupId));
		return terminalGroupService.queryTerminalGroupName(terminalGroupBean).getGroupName();
	}
	@RequestMapping(value="terminalGroup/insertTerminalGroup.do",method=RequestMethod.POST)  
	@ResponseBody
	 public Map<String,Object> insertTerminalGroup(
			 String terminalGroupName,
			 String terminalGroupGroup,
			 String terminalGroupDescription,
			 HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		Timestamp createTime = new Timestamp(new Date().getTime());
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setGroupName(terminalGroupName);
		terminalGroupBean.setParentId(Integer.parseInt(terminalGroupGroup));
		terminalGroupBean.setDescription(terminalGroupDescription);
		terminalGroupBean.setCreate_time(createTime);
		terminalGroupBean.setIsDeleted(0);
		terminalGroupBean.setCreatorID(ub.getUser_id());
		
		TerminalGroupBean QterminalGroupBean = new TerminalGroupBean();
		QterminalGroupBean.setGroupName(terminalGroupName);
		List<TerminalGroupBean> checkList = new ArrayList<TerminalGroupBean>();
		checkList = terminalGroupService.checkTerminalGroupName(QterminalGroupBean);
		if(checkList.size()>0){
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("success", false);  
			map.put("msg", "终端组名称已存在，请重新输入！");  
			return map;
		}
		else{
			terminalGroupService.insertTerminalGroup(terminalGroupBean);
			operationlog(ub.getUser_id().toString(),terminalGroupName,SystemConstant.INSERT_OPERATION);
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("success", true);  
			return map;
		}
	}
	
	@RequestMapping(value="/terminalGroup/deleteTerminalGroupById.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  deleteTerminalGroupById( 
			String terminalGroupId,
			HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();  
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		String name = getTerminalGroupName(terminalGroupId);
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setT_id(Integer.parseInt(terminalGroupId));
       boolean flag=terminalGroupService.deleteTerminalGroupById(terminalGroupBean);
       if (flag) {
    	   operationlog(ub.getUser_id().toString(),name,SystemConstant.DELETE_OPERATION);
    		map.put("success", "true");  
	}else{
		    map.put("success", "false"); 
		    map.put("msg", "终端正在播放中,请先停止任务再删除！");
	}
		return map;
	}
	
	@RequestMapping(value="/terminalGroup/updateTerminalGroupById.do",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String,Object>  updateTerminalGroupById( 
			 String terminalGroupId,
			 String terminalGroupName,
			 String terminalGroupDescription,
			HttpServletRequest request) {
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setT_id(Integer.parseInt(terminalGroupId));
		terminalGroupBean.setGroupName(terminalGroupName);
		terminalGroupBean.setDescription(terminalGroupDescription);
		
		TerminalGroupBean QterminalGroupBean = new TerminalGroupBean();
		QterminalGroupBean.setT_id(Integer.parseInt(terminalGroupId));
		QterminalGroupBean.setGroupName(terminalGroupName);
		List<TerminalGroupBean> checkList = new ArrayList<TerminalGroupBean>();
		checkList = terminalGroupService.checkTerminalGroupName(QterminalGroupBean);
		if(checkList.size()>0){
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("success", false);  
			map.put("msg", "终端组名称已存在，请重新输入！");  
			return map;
		}else{
			terminalGroupService.updateTerminalGroupByTerminalId(terminalGroupBean);
			operationlog(ub.getUser_id().toString(),getTerminalGroupName(terminalGroupId),SystemConstant.UPDATE_OPERATION);
			Map<String,Object> map = new HashMap<String,Object>();  
			map.put("success", "true");  
			return map;
		}
	}
	/**
	 * <p>          
	 *       <discription> 概述：根据终端id查询终端所属终端组 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2016年12月28日 下午5:11:38
	 * @UpdateDate     更新时间：   2016年12月28日 下午5:11:38
	 * @Package_name   包名：          iis/controller.terminal
	 * @Param          参数：          @param terminalId
	 * @Param          参数：          @param request
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          String
	 */
	@RequestMapping(value="terminalGroup/selectTerminalGroupIds")
	@ResponseBody
	public String  selectTerminalGroupIds( 
			String terminalId,
			HttpServletRequest request) {
		String terminalGroupIds="";
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		TerminalBean TerminalBean= new TerminalBean();
		if (null!=terminalId&&""!=terminalId&&!terminalId.equals("")) {
			TerminalBean.setTerminal_id(Integer.parseInt(terminalId.trim()));
		}
		TerminalBean.setCreator_id(user.getUser_id());
		terminalGroupIds= terminalTerminalGroupService.selectTerminalGroupIds(TerminalBean);
		return terminalGroupIds;
	}
	
	/**
	 * @Description 通过开始拖拽的终端组id来修改其父级id 修改值为 拖拽后目标节点的终端组id 接口(终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年2月20日下午2:23:26
	 * @param 		targetId	开始拖拽的终端组id
	 * @param 		sourceId	拖拽后的目标终端组id（开始终端组的父级id）
	 * @return
	 
	@RequestMapping(value = "terminalGroup/updateTerminalGroupIdParent.do")
	@ResponseBody
	public Map<String, Object> updateTerminalGroupIdParent(String targetId, String sourceId) {
		Map<String, Object> map = new HashMap<>();
		if (targetId == null || targetId == "" || sourceId == null || sourceId == "") {
			return null;
		}
		int num = terminalGroupService.updateTerminalGroupIdParent(Integer.parseInt(sourceId), Integer.parseInt(targetId));
		
		if (num < 0) {
			map.put("success", false);
		} else {
			map.put("success", true);
		}
		return map;
	}
	*/
}
    