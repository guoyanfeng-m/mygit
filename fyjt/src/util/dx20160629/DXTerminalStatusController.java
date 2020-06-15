//package dx20160629;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import net.sf.json.JSONObject;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import service.ConfigService;
//import service.operationlog.OperationlogService;
//import service.program.ProgramService;
//import service.terminal.TaskDownLoadService;
//import service.terminal.TerminalService;
//import service.terminal.TerminalStatusService;
//import service.user.UserService;
//import util.GetScreenViewPath;
//import util.MapUtil;
//import util.PageInfo;
//import util.PublishUtil;
//import util.Utils;
//import util.XmlUtil;
//import util.getDownloadByMac;
//import beans.SystemConstant;
//import beans.program.ProgramTerminalBean;
//import beans.terminal.Program_terminalBean;
//import beans.terminal.ScreenViewBean;
//import beans.terminal.TaskDownLoadBean;
//import beans.terminal.TerminalBean;
//import beans.terminal.TerminalCloseTime;
//import beans.terminal.TerminalStatusBean;
//import beans.terminal.TerminalStatusViewBean;
//import beans.terminal.TerminalViewBean;
//import beans.user.UserBean;
//
//
//@Controller
//public class DXTerminalStatusController {
//	@Autowired
//	private TerminalService terminalService;
//	@Autowired
//	private TerminalStatusService terminalStatusService;
//	@Autowired
//	private TaskDownLoadService taskDownLoadService;
//	@Autowired
//	private ConfigService configService;
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private OperationlogService operationlogService;
//	@Autowired
//	private ProgramService programService;
//	public static List<Map<String,Object>> openList = new ArrayList<Map<String,Object>>();
//	public static List<Map<String,Object>> closeList = new ArrayList<Map<String,Object>>();
//	public void operationlog(String userid,String operationName,Integer operationType) {
//		operationlogService.insertOperationlog(userid,operationName,operationType,SystemConstant.MONITOR_ID);
//	}
//	
//	@RequestMapping("/terminal/insertTerminalStatus.do")
//	@ResponseBody
//	 public Map<String,Object> insertMenu(
//			 HttpServletRequest request) {
//		TerminalStatusBean terminalStatusBean = new TerminalStatusBean();
//		terminalStatusBean.setCpuUsage(request.getParameter("cpu"));
//		terminalStatusService.insertTerminalStatus(terminalStatusBean);
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("success", "true");  
//		return map;
//	}
//	
//	@RequestMapping("/terminal/queryTerminalStatus.do")
//	@ResponseBody
//	public List<TerminalStatusViewBean>  queryTerminalStatus( 
//			HttpServletRequest request) {
//		String[] result = request.getParameterValues("terminalIds[]");
//		List<Integer> terminalId = new ArrayList<Integer>();
//		List<TerminalStatusViewBean> terminalStatusViewBeanList = new ArrayList<TerminalStatusViewBean>();
//		if(result!=null){
//			for(String s : result){
//				terminalId.add(Integer.parseInt(s));
//			}
//			terminalStatusViewBeanList = terminalStatusService.queryTerminalStatus(terminalId);
//		}
//		
//		
//		return terminalStatusViewBeanList;
//	}
//	
//	@RequestMapping("/terminal/queryTerminalStatusById.do")
//	@ResponseBody
//	public Map<String,Object>  queryTerminalStatusById( 
//			Integer[] terminal_id,
//			HttpServletRequest request) {
//		List<TerminalStatusViewBean> terminalStatusViewBean = new ArrayList<TerminalStatusViewBean>();
//		terminalStatusViewBean = terminalStatusService.queryTerminalStatusById(terminal_id);
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("terminalStatusViewBean", terminalStatusViewBean);    
//		return map;
//	}
//	
//	@RequestMapping("/terminal/queryTerminalStatusByUserId.do")
//	@ResponseBody
//	public Map<String,Object>  queryTerminalStatusByUserId( 
//			String page,
//			String rows,
//			Integer filter,
//			String terminalGroupId,
//			HttpServletRequest request) {
//		UserBean ub=(UserBean) request.getSession().getAttribute("user");
//		List<String> list=userService.queryChildUserID(ub.getUser_id().toString());
//		List<String> userlist=new ArrayList<String>();
//		 userlist.add(ub.getUser_id().toString());
//		 userlist.addAll(list);
//		
//		 while(true){
//			 list=userService.queryChilds(list);
//			 if(list.size()==0){
//				 break;
//			 }else{
//				userlist.addAll(list); 
//			 }
//		 }
//		
//		
//		int newpage = 1;
//		int newrows = 0;
//		if(page!=null&&page!=""&&!page.equals("0")){
//			newpage = Integer.parseInt(page);
//		}
//		if(rows!=null&&rows!=""){
//			newrows = Integer.parseInt(rows);
//		}
//		if(terminalGroupId!= null && terminalGroupId != ""){
//			List<TerminalStatusViewBean> terminalStatusViewBean = new ArrayList<TerminalStatusViewBean>();
//			int total = terminalStatusService.queryTerminalStatusByGroupIdCount(Integer.parseInt(terminalGroupId), userlist);
//			PageInfo pageInfo = new PageInfo();
//			pageInfo.setRows(newrows);
//			pageInfo.setPage(newpage);
//			pageInfo.setTotal(total);
//			terminalStatusViewBean = terminalStatusService.queryTerminalStatusByGroupId(Integer.parseInt(terminalGroupId), userlist, pageInfo);
//			Map<String,Object> map = new HashMap<String,Object>();  
//			map.put("rows", terminalStatusViewBean);   
//			map.put("total", total);   
//			return map;
//		}else{
//			List<TerminalStatusViewBean> terminalStatusViewBean = new ArrayList<TerminalStatusViewBean>();
//			System.out.println("-------------------:terminalStatusService.queryTerminalStatusByUserIdCount(userlist)" );
//			System.out.println("-------------------:" + userlist.size() );
//			int total = terminalStatusService.queryTerminalStatusByUserIdCount(userlist);
//			System.out.println("---over:");
//			PageInfo pageInfo = new PageInfo();
//			pageInfo.setRows(newrows);
//			pageInfo.setPage(newpage);
//			pageInfo.setTotal(total);
//			terminalStatusViewBean = terminalStatusService.queryTerminalStatusByUserId(userlist, pageInfo,filter);
//			Map<String,Object> map = new HashMap<String,Object>();  
//			map.put("rows", terminalStatusViewBean);   
//			map.put("total", total);   
//			return map;
//		}
//	}
//	
//	@RequestMapping("/terminal/queryTerminalStatusByGroupId.do")
//	@ResponseBody
//	public Map<String,Object>  queryTerminalStatusByGroupId( 
//			String page,
//			String rows,
//			Integer terminalGroupId,
//			HttpServletRequest request) {
//		UserBean ub=(UserBean) request.getSession().getAttribute("user");
//		List<String> list=userService.queryChildUserID(ub.getUser_id().toString());
//		List<String> userlist=new ArrayList<String>();
//		 userlist.add(ub.getUser_id().toString());
//		 userlist.addAll(list);
//		
//		 while(true){
//			 list=userService.queryChilds(list);
//			 if(list.size()==0){
//				 break;
//			 }else{
//				userlist.addAll(list); 
//			 }
//		 }
//		int newpage = 1;
//		int newrows = 0;
//		if(page!=null&&page!=""&&!page.equals("0")){
//			newpage = Integer.parseInt(page);
//		}
//		if(rows!=null&&rows!=""){
//			newrows = Integer.parseInt(rows);
//		}
//		List<TerminalStatusViewBean> terminalStatusViewBean = new ArrayList<TerminalStatusViewBean>();
//		int total = terminalStatusService.queryTerminalStatusByGroupIdCount(terminalGroupId, userlist);
//		PageInfo pageInfo = new PageInfo();
//		pageInfo.setRows(newrows);
//		pageInfo.setPage(newpage);
//		pageInfo.setTotal(total);
//		terminalStatusViewBean = terminalStatusService.queryTerminalStatusByGroupId(terminalGroupId, userlist, pageInfo);
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("rows", terminalStatusViewBean);   
//		map.put("total", total);   
//		return map;
//	}
//	@RequestMapping("/terminal/queryScreenView.do")
//	@ResponseBody
//	public List<ScreenViewBean>  queryScreenView( 
//			HttpServletRequest request) {
//		List<ScreenViewBean> screenViewBeanList = new ArrayList<ScreenViewBean>();
//		String[] result = request.getParameterValues("mac[]");
//		String ftpLocalPath = configService.queryConfig("ftpMappingUrl");
//		for(String item : result){
//			String mac = item.split("&&&&")[0];
//			String terminalName = item.split("&&&&")[1];
//			GetScreenViewPath getScreenViewPath = new GetScreenViewPath(mac,ftpLocalPath);
//			String path = getScreenViewPath.getPath();
//			ScreenViewBean screenViewBean = new ScreenViewBean();
//			screenViewBean.setPath(path);
//			screenViewBean.setMac(mac);
//			screenViewBean.setTerminalName(terminalName);
//			String ip = configService.queryConfig("tfbsip");// 获取服务器IP
//			if(ip == null){
//			  ip = configService.queryConfig("httpip");
//			}
//			String[] macs = new String[1];
//			macs[0] = mac;
//			sendImgPath(ip,macs);
//			screenViewBeanList.add(screenViewBean);
//		}
//		return screenViewBeanList;
//	}
//	private void sendImgPath(String ip,String[] macs){
//    	try {
//    		Map<String, Object> xmlMap = new HashMap<String, Object>();
//    		Map<String, Object> xmlMap1 = new HashMap<String, Object>();
//    		Map<String,Object> xmlMap2 = new HashMap<String,Object>();
//    		Map<String, Object> twinMap = new HashMap<String, Object>();
//    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//    		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
//    		xmlMap1.put("type", "screenCapture");
//    		list.add(xmlMap1);
//    		xmlMap2.put("name", "screencapture");
//    		list1.add(xmlMap2);
//    		xmlMap.put("target", list1);
//    		xmlMap.put("command", list);
//    		twinMap.put("systimes", xmlMap);
//    		XmlUtil xmlUtil = new XmlUtil();
//    		String comentStr = xmlUtil.mapToString(twinMap);
//    		System.out.println(comentStr);;
//			JSONObject json = new JSONObject();
//			json.put("command", "sendCommand");
//			json.put("target", macs);
//			json.put("xml", comentStr);
//			PublishUtil publishUtil = new PublishUtil();
//			publishUtil.publishTask(json, ip);
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//	}
//	
//	@RequestMapping("/terminal/control.do")
//	@ResponseBody
//	public Map<String,Object>  control( 
//			String flag,
//			String value,
//			HttpServletRequest request) {
//		UserBean ub=(UserBean) request.getSession().getAttribute("user");
//		Integer user_id = ub.getUser_id();
//		
//		String[] Macs = request.getParameterValues("mac[]");
//		String[] Mac = new String[Macs.length];
//		String[] Names = new String[Macs.length];
//		List<Integer> ids = new ArrayList<Integer>();
//		for(int i=0;i<Macs.length;i++){
//			String[] temp = Macs[i].split("&&&&");
//			Mac[i] = temp[0];
//			Names[i] = temp[1];
//			ids.add(Integer.parseInt(temp[2]));
//		}
//		
//		JSONObject cobject = new JSONObject();
//		//String ip = configService.queryConfig("httpip");
//		String ip=configService.queryConfig("tfbsip");
//		if(ip==null){
//		  ip=configService.queryConfig("httpip");
//		}
//		PublishUtil orderUtils = new PublishUtil();
//		boolean b =true;
//		if (flag.equals("updateApp")) {
//			String command = "updateAPP";
//			cobject = Utils.StringtoJson(command, Mac);
//			b = orderUtils.publishTask(cobject, ip);
//			for(String name:Names){
//				operationlog(user_id.toString(),name,SystemConstant.RENEW_OPERATION);
//			}
//		}
//		if (flag.equals("close")) {
//			String command = "shutdown";
//			cobject = Utils.StringtoJson(command, Mac);
//			b = orderUtils.publishTask(cobject, ip);
//			for(String name:Names){
//				operationlog(user_id.toString(),name,SystemConstant.CLOSE_OPERATION);
//			}
//		}if(flag.equals("open")){
//			JSONObject tempjson = new JSONObject();
//			tempjson.put("command", "wakeup");
//			JSONObject[] tempArray = new JSONObject[Mac.length];
//			List<TerminalViewBean> tList =  terminalService.queryTerminalByTerminalIds(ids);
//			for (int i = 0; i < tList.size(); i++) {
//				JSONObject tempjson1 = new JSONObject();
//				tempjson1.put("mac", Mac[i]);
//				tempjson1.put("ip", tList.get(i).getIp());
//				tempArray[i] = tempjson1;
//			}
//			tempjson.put("target", tempArray);
//			b = orderUtils.publishTask(tempjson, ip);
//			for(String name:Names){
//				operationlog(user_id.toString(),name,SystemConstant.OPEN_OPERATION);
//			}
//		}
//		if (flag.equals("openDisplay")) {
//			String command = "openDisplay";
//			cobject = Utils.StringtoJson(command, Mac);
//			b = orderUtils.publishTask(cobject, ip);
//		}if(flag.equals("closeDisplay")){
//			String command = "closeDisplay";
//			cobject = Utils.StringtoJson(command, Mac);
//			b = orderUtils.publishTask(cobject, ip);
//		}
//		if (flag.equals("restart")) {
//			String command = "reboot";
//			cobject = Utils.StringtoJson(command, Mac);
//			b = orderUtils.publishTask(cobject, ip);
//			for(String name:Names){
//				operationlog(user_id.toString(),name,SystemConstant.RESET_OPERATION);
//			}
//		}
//		if (flag.equals("setvolume")) {
//			if (StringUtils.isNotBlank(value)) {
//				String command = "setvolume";
//				cobject = Utils.StringtoJson(command, Mac);
//				cobject.put("value", value);
//				b = orderUtils.publishTask(cobject, ip);
//				for(String name:Names){
//					operationlog(user_id.toString(),name,SystemConstant.VOICE_OPERATION);
//				}
//			}
//		}
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("success", b);  
//		return map;
//	}
//	
//	@RequestMapping("/terminal/queryTaskDownLoadByTerminalId.do")
//	@ResponseBody
//	public Map<String,Object>  queryTaskDownLoadByTerminalId( 
//			String page,
//			String rows,
//			String flag,
//			HttpServletRequest request) {
//		
//		String[] macList = request.getParameterValues("mac[]");
//		List<TaskDownLoadBean> taskDownLoadBeanList = new ArrayList<TaskDownLoadBean>();
//		String ip=configService.queryConfig("tfbsip");
//		if(ip==null){
//		  ip=configService.queryConfig("httpip");
//		}
//		String terminalName = "";
//		if(flag.equals("1")){
//			int newpage = 1;
//			int newrows = 0;
//			if(page!=null&&page!=""&&!page.equals("0")){
//				newpage = Integer.parseInt(page);
//			}
//			if(rows!=null&&rows!=""){
//				newrows = Integer.parseInt(rows);
//			}
//			int total=0;
//			for(String mac : macList){
//				TerminalBean terminalBean = new TerminalBean();
//				terminalBean.setMac(mac);
//				List<String> terminalNameList = terminalService.queryTerminalName(terminalBean);
//				if(terminalNameList.size()>0){
//					terminalName = terminalNameList.get(0);
//				}
//				total = 1;
//				PageInfo pageInfo = new PageInfo();
//				pageInfo.setRows(newrows);
//				pageInfo.setPage(newpage);
//				pageInfo.setTotal(total);
//				taskDownLoadBeanList.addAll(new getDownloadByMac(ip).getDownload(mac,"downloadhistory:"+mac,terminalName));
//			}
//			
//			Map<String,Object> map = new HashMap<String,Object>();  
//			map.put("rows", taskDownLoadBeanList);
////			map.put("total", total);   
//			return map;
//		}else{
//			for(String mac : macList){
//				TerminalBean terminalBean = new TerminalBean();
//				terminalBean.setMac(mac);
//				List<String> terminalNameList = terminalService.queryTerminalName(terminalBean);
//				if(terminalNameList.size()>0){
//					terminalName = terminalNameList.get(0);
//				}
//				taskDownLoadBeanList.addAll(new getDownloadByMac(ip).getDownload(mac,"downloadstatus:"+mac,terminalName));
//			}
//			Map<String,Object> map = new HashMap<String,Object>();  
//			map.put("rows", taskDownLoadBeanList);
//			return map;
//		}
//	}
//	
//	@RequestMapping("/terminal/delTaskDownLoadByTerminalId.do")
//	@ResponseBody
//	public Map<String,Object>  delTaskDownLoadByTerminalId( 
//			HttpServletRequest request) {
//		String[] requestList = request.getParameterValues("mac[]");
//		String ip=configService.queryConfig("tfbsip");
//		if(ip==null){
//		  ip=configService.queryConfig("httpip");
//		}
//		for(int i=0;i<requestList.length;i++){
//			String[] result = requestList[i].split("&&&&");
//			if(result.length==2){
//				new getDownloadByMac(ip).delDownload("downloadhistory:"+result[0],result[1] );
//			}
//		}
//		
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("success", true);
//		return map;
//	}
//		
//	
//	
//	@RequestMapping("/terminal/queryProgram_terminalByTerminalId.do")
//	@ResponseBody
//	public Map<String,Object>  queryProgram_terminalByTerminalId( 
//			HttpServletRequest request) {
//		String[] terminal_id = request.getParameterValues("terminal_id[]");
//		List<Program_terminalBean> program_terminalBeanList = new ArrayList<Program_terminalBean>();
//		program_terminalBeanList = taskDownLoadService.queryProgram_terminalByTerminalId(terminal_id);
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("rows", program_terminalBeanList);
//		return map;
//	}
//	
//	@RequestMapping("/terminal/deleteProgram.do")
//	@ResponseBody
//	public Map<String,Object>  deleteProgram( 
//			HttpServletRequest request) {
//		UserBean ub=(UserBean) request.getSession().getAttribute("user");
//		Integer user_id = ub.getUser_id();
//		String[] deleprogram = request.getParameterValues("deleprogram[]");
//		JSONObject object = new JSONObject();
//		String ip=configService.queryConfig("tfbsip");
//		if(ip==null){
//		  ip=configService.queryConfig("httpip");
//		}
//		PublishUtil orderUtils = new PublishUtil();
//		boolean b =true;
//		for(int i=0;i<deleprogram.length;i++){
//			String[] mac = new String[1];
//			mac[0] = deleprogram[i].split("&&&&")[0];
//			String terminal_id = deleprogram[i].split("&&&&")[2];
//			String program_id = deleprogram[i].split("&&&&")[1];
//			String program_name = "";
//			if(deleprogram[i].split("&&&&").length>=4){
//				program_name = deleprogram[i].split("&&&&")[3];
//			}
//		object.put("command","deleteTask");
//		object.put("target",mac);
//		object.put("taskid",program_id);
//		orderUtils.publishTask(object, ip);
//		ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
//		programTerminalBean.setProgram_id(Integer.parseInt(program_id));
//		programTerminalBean.setTerminal_id(Integer.parseInt(terminal_id));
//		taskDownLoadService.deleteProgram_terminalByTerminalId(programTerminalBean);
//		programService.checkProgramPublish(Integer.parseInt(program_id));
//		operationlog(user_id.toString(),program_name,SystemConstant.DEL_STOP_OPERATION);
//		}
//		
//		
//		
//		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("success", true);  
//		return map;
//	}
//	
//	//瀹氭椂鍏虫満
//	@RequestMapping("/terminal/querycloseTimeByTerminalId.do")
//	@ResponseBody
//	public Map<String,Object>  querycloseTimeByTerminalId( 
//			HttpServletRequest request) {
//		String[] terminalId = request.getParameterValues("terminals[]");
//		List<TerminalCloseTime> terminalCloseTime = new ArrayList<TerminalCloseTime>();
//		terminalCloseTime = terminalStatusService.querycloseTimeByTerminalId(terminalId);
// 		Map<String,Object> map = new HashMap<String,Object>();  
//		map.put("TerminalCloseTime", terminalCloseTime);
//		return map;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/terminal/updatecloseTimeByTerminalId.do")
//	@ResponseBody
//	public Map<String,Object>  updatecloseTimeByTerminalId( 
//			String terminalJson,
//			HttpServletRequest request) {
//		Map<String, Object> programMap = MapUtil.parseJSON2Map(terminalJson);
//		List<Map<String, Object>> dataMap = (List<Map<String, Object>>) programMap.get("data");
//		List<Map<String, Object>> delterminalList = (List<Map<String, Object>>) programMap.get("delterminal");
//		String[] delterminal = new String[delterminalList.size()];
//		boolean hasmac = false;
//		for (int i = 0; i < delterminalList.size(); i++) {
//			delterminal[i] = (String) delterminalList.get(i).get("terminal_id").toString();
//		}
//		terminalStatusService.updatecloseTimeByTerminalId(dataMap,delterminal);
//		List<Map<String, Object>> timeAndMacList = new ArrayList<Map<String,Object>>();
//		for (int i = 0; i < dataMap.size(); i++) {
//			Map<String, Object> timeAndMacMap = new HashMap<String, Object>();
//			String terminalname = (String) dataMap.get(i).get("terminalname").toString();
//			String mac = (String) dataMap.get(i).get("terminalmac").toString();
//			for (int m = 0; m < timeAndMacList.size(); m++) {
//				String usedmac = (String) timeAndMacList.get(m).get("mac");
//				if (mac.equals(usedmac)) {
//					hasmac = true;
//					break;
//				}
//			}
//			if(hasmac) {
//				hasmac = false;
//				continue;
//			}
//			timeAndMacMap.put("mac", mac);
//			timeAndMacMap.put("terminalname", terminalname);
//			String openstr = (String) dataMap.get(i).get("opentime").toString();
//			String closestr = (String) dataMap.get(i).get("closetime").toString();
//			String daystr = (String) dataMap.get(i).get("days").toString();
//			Map<String, Object> tempMap = new HashMap<String, Object>();
//			List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
//			tempMap.put("opentime", openstr);
//			tempMap.put("closetime", closestr);
//			tempMap.put("days", daystr);
//			tempList.add(tempMap);
//			for (int j = 0; j < dataMap.size(); j++) {
//				if(i != j) {
//					String comparemac = (String) dataMap.get(j).get("terminalmac").toString();
//					if(mac.equals(comparemac)) {
//						Map<String, Object> addtempMap = new HashMap<String, Object>();
//						openstr = (String) dataMap.get(j).get("opentime").toString();
//						closestr = (String) dataMap.get(j).get("closetime").toString();
//						daystr = (String) dataMap.get(j).get("days").toString();
//						addtempMap.put("opentime", openstr);
//						addtempMap.put("closetime", closestr);
//						addtempMap.put("days", daystr);
//						tempList.add(addtempMap);
//					}
//				}
//			}
//			timeAndMacMap.put("time", tempList);
//			timeAndMacList.add(timeAndMacMap);
//		}
//		System.out.println(timeAndMacList);
//		terminalStatusService.updatecloseTimeXml(timeAndMacList);
// 		Map<String,Object> map = new HashMap<String,Object>();  
// 		programMap.put("success","true");
//		return programMap;
//	}
//}
//    