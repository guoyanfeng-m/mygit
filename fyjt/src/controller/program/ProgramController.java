package controller.program;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import service.element.ElementService;
import service.model.ModelService;
import service.operationlog.OperationlogService;
import service.program.ProgramService;
import service.terminal.TerminalService;
import service.terminal.TerminalStatusService;
import service.user.UserService;
import util.DownLoadList;
import util.FileCheck;
import util.GetStatusByMacOrPid;
import util.MapUtil;
import util.PageInfo;
import util.XmlUtil;
import util.playProgramPojo.PlayProgram;
import util.playProgramPojo.region;
import beans.program.ProgramBean;
import beans.program.ProgramTerminalBean;
import beans.program.ProgramViewBean;
import beans.program.view.ProgramDownloadView;
import beans.program.view.ProgramTerminalView;
import beans.sys.SystemConstant;
import beans.terminal.TerminalDownloadTime;
import beans.user.UserBean;
import controller.redisManager.RedisUtil;

/**
 * @author laiyunjian
 */
@SuppressWarnings({"unchecked","rawtypes","unused","static-access","deprecation"})
@Controller
@Transactional
public class ProgramController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProgramService programService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private OperationlogService operationlogService;
	@Autowired
	private GetStatusByMacOrPid getStatusByMacOrPid;
	@Autowired
	private ConfigService configService;
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private ElementService elementService;
	@Autowired
	private TerminalStatusService terminalStatusService;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	private List<Map<String,Object>> staticscenelist = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> staticregionlist = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> staticbuttonlist = new ArrayList<Map<String,Object>>();
	/**
	 * @param programJson节目内容json字符串
	 * @param type
	 * @param request
	 * @return
	 * 新建/编辑节目单
	 */
	@RequestMapping("program/insertProgram.do")
	@ResponseBody
	public Map<String, Object> createProgram(
			@RequestParam(value = "programJson") String programJson,
			@RequestParam(value = "type") String type,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] idsStr = request.getParameterValues("ids[]");
		Integer[] ids = null;
		if (idsStr != null) {
			ids = new Integer[idsStr.length];
			for (int i = 0; i < idsStr.length; i++) {
				ids[i] = Integer.parseInt(idsStr[i]);
			}
		}
		Map<String, Object> programMap = MapUtil.parseJSON2Map(programJson);
		boolean isMainScene = false;
		if ((null != type && "send".equals(type)) || (null != type && "save".equals(type))) {
			staticregionlist.clear();
			staticbuttonlist.clear();
			Map<String,Object> tempMap3 = new HashMap<String,Object>();
			if(!programMap.get("staticscene").toString().equals("null")){
				staticscenelist = (List<Map<String, Object>>) programMap.get("staticscene");
				if(staticscenelist != null && staticscenelist.size() > 0){
					Boolean hbIsOverlap = IsOverlap(staticscenelist,true);
					if(hbIsOverlap){
						map.put("success", false);
						map.put("msg", "互动节目网页不能与互动按钮重叠！");
						return map;
					}
					Map<String,Object> staticscenemap = staticscenelist.get(0);
					tempMap3 = programService.checkMap(staticscenemap,isMainScene);
				}
			}
			List<Map<String,Object>> scenelist = (List<Map<String, Object>>) programMap.get("scene");
			Boolean hbIsOverlap = IsOverlap(scenelist,false);
			if(hbIsOverlap){
				map.put("success", false);
				map.put("msg", "互动节目网页不能与互动按钮重叠！");
				return map;
			}
			Map<String, Object> tempMap = programService.checkMap(programMap,isMainScene);
			for (int i = 0; i < scenelist.size(); i++) {
				Map<String,Object> scenemap = scenelist.get(i);
				if(scenemap.get("ismain").toString().equals("true")){
					isMainScene = true;
					Map<String,Object> tempMap2 = programService.checkMap(scenemap,isMainScene);
					if ((null != tempMap2.get("srcListSize") && (Integer) tempMap2.get("srcListSize") == 0 
							&& (Integer) tempMap2.get("plugListSize") == 0 && ((Integer) tempMap2.get("backimgSize") == 0
							|| ((Integer) tempMap2.get("backimgSize") > 0 
									&& ((Integer) tempMap.get("srcl") - (Integer) tempMap.get("screenListSize")) == 0))
							) && ( tempMap3.size() == 0 || (null != tempMap3.get("srcListSize") && (Integer) tempMap3.get("srcListSize") == 0 
							&& (Integer) tempMap3.get("plugListSize") == 0 && ((Integer) tempMap3.get("backimgSize") == 0
							|| ((Integer) tempMap3.get("backimgSize") > 0 
									&& ((Integer) tempMap.get("srcl") - (Integer) tempMap.get("screenListSize")) == 0))))) {
						map.put("success", false);
						map.put("msg", "主场景没有任何素材，请验证后再发布！");
						return map;
					}
				}
			}
			//			if (null != tempMap.get("srcListSize")
			//					&& ((Integer) tempMap.get("srcListSize") - (Integer) tempMap.get("screenListSize")) == 0 && (Integer) tempMap.get("backimgSize") == 0
			//					&& (Integer) tempMap.get("plugListSize") == 0) {
			//				map.put("success", false);
			//				map.put("msg", "节目中没有任何素材，请验证后再发布！");
			//				return map;
			//			} else 
			if (null != tempMap.get("succ")
					&& !(Boolean) tempMap.get("succ")) {
				map.put("success", false);
				map.put("msg", "该节目包含缺失素材！");
				return map;
			}
		}
		Integer program_id = null;
		if (null != programMap.get("id")&& !"".equals(programMap.get("id").toString())) {
			program_id = Integer.parseInt(programMap.get("id").toString());
		}
		// 检查用户名是否重复
		if (checkName(programMap.get("name").toString(), program_id)) {
			map.put("success", false);
			map.put("msg", "该节目名已经存在！");
			return map;
		}
		ProgramBean programBean = new ProgramBean();
		//节目类型  0：普通节目      1：互动节目
		programBean.setType(programMap.get("istouch").equals("true")?1:0);
		programBean.setSchedulelevel(Integer.parseInt(programMap.get("pubpower").toString()));
		programBean.setName(programMap.get("name").toString());
		programBean.setStartTime(string2Timestamp(programMap.get("startdate").toString()));
		programBean.setEndTime(string2Timestamp(programMap.get("stopdate").toString()));
		programMap.put("updatetime", df.format(new Date()));
		programBean.setCreate_time(new Timestamp(System.currentTimeMillis()));
		programBean.setDeleted(SystemConstant.ISDELETED_FALSE);
		programBean.setUrl("programXmlFile/" + programBean.getName() + ".xml");
		if (null != type && "send".equals(type)) {
			programBean.setIsSend(SystemConstant.ISSEND_TRUE);
		} else {
			programBean.setIsSend(SystemConstant.ISSEND_FALSE);
		}
		programBean.setAudit_status(modelService.queryModuleAudit(SystemConstant.PROGRAM_ID));
		UserBean o = (UserBean) request.getSession().getAttribute("user");
		if (null != program_id) {
			// 修改日志
			operationlogService.insertOperationlog(o.getUser_id().toString(),
					programMap.get("name").toString(),
					SystemConstant.UPDATE_OPERATION, SystemConstant.PROGRAM_ID);
			programBean.setProgram_id(program_id);
			programService.updateProgram(programBean, programMap);
		} else {
			// 增加日志
			operationlogService.insertOperationlog(o.getUser_id().toString(),
					programMap.get("name").toString(),
					SystemConstant.INSERT_OPERATION, SystemConstant.PROGRAM_ID);

			// 创建者信息
			programBean.setCreator_id(o.getUser_id());
			programService.insertProgram(programBean, programMap);
		}
		if (null != type && "send".equals(type)) {
			// 发布日志
			operationlogService.insertOperationlog(o.getUser_id().toString(), programMap
					.get("name").toString(),
					SystemConstant.PUBLISH_OPERATION,
					SystemConstant.PROGRAM_ID);
			programService.send(ids, programBean);
			// operationlogService.insertOperationlog(o.getUsername(),
			// programBean.getName(), operationType, operationModel)
		}
		map.put("program_id", programBean.getProgram_id());
		map.put("success", true);
		return map;
	}
	/**
	 * /判断网页元素与互动按钮是否重叠
	 */
	private Boolean IsOverlap(List<Map<String, Object>> scenelist,boolean isstatic) {
		if(isstatic){
			for (int i = 0; i < scenelist.size(); i++) {
				if(scenelist.get(i).get("region") != null 
						&& !scenelist.get(i).get("region").equals("null"))
					staticregionlist.addAll((List<Map<String, Object>>) scenelist.get(i).get("region"));
				if(scenelist.get(i).get("button") != null
						&& !scenelist.get(i).get("button").equals("null"))
					staticbuttonlist.addAll((List<Map<String, Object>>) scenelist.get(i).get("button"));
				if(scenelist.get(i).get("region") != null 
						&& !scenelist.get(i).get("region").equals("null") 
						&& scenelist.get(i).get("button") != null
						&& !scenelist.get(i).get("button").equals("null")){
					List<Map<String, Object>> regionlist = (List<Map<String, Object>>) scenelist.get(i).get("region");
					List<Map<String, Object>> buttonlist = (List<Map<String, Object>>) scenelist.get(i).get("button");
					for (int j = 0; j < staticregionlist.size(); j++) {
						String regiontype = staticregionlist.get(j).get("type").toString();
						if(regiontype.equals("5") && staticbuttonlist != null && staticbuttonlist.size() > 0){
							Integer x1 = Integer.parseInt(staticregionlist.get(j).get("left").toString());        //网页x坐标
							Integer y1 = Integer.parseInt(staticregionlist.get(j).get("top").toString());			//网页y坐标
							Integer m1 = Integer.parseInt(staticregionlist.get(j).get("width").toString());		//网页宽
							Integer n1 = Integer.parseInt(staticregionlist.get(j).get("height").toString());      //网页高
							for (int k = 0; k < staticbuttonlist.size(); k++) {
								Integer x2 = Integer.parseInt(staticbuttonlist.get(k).get("left").toString());    //按钮x坐标
								Integer y2 = Integer.parseInt(staticbuttonlist.get(k).get("top").toString()); 	//按钮y坐标
								Integer m2 = Integer.parseInt(staticbuttonlist.get(k).get("width").toString()); 	//按钮宽
								Integer n2 = Integer.parseInt(staticbuttonlist.get(k).get("height").toString());  //按钮高
								Integer x12 = x1+m1;
								Integer y12 = y1+n1;
								Integer x22 = x2+m2;
								Integer y22 = y2+n2;
								if((x2 >= x1 && x2 <= x12 && y2 >= y1 && y2 <= y12)           //网页区域包含互动按钮
										|| (x22 >= x1 && x22 <= x12 && y2 >= y1 && y2 <= y12)
										|| (x2 >= x1 && x2 <= x12 && y22 >= y1 && y22 <= y12)
										|| (x22 >= x1 && x22 <= x12 && y22 >= y1 && y22 <= y12)){
									return true;
								}else if((x1 >= x2 && x1 <= x22 && y1 >= y2 && y1 <= y22)     //互动按钮包含网页区域
										|| (x12 >= x2 && x12 <= x22 && y1 >= y2 && y1 <= y22)
										|| (x1 >= x2 && x1 <= x22 && y12 >= y2 && y12 <= y22)
										|| (x12 >= x2 && x12 <= x22 && y12 >= y2 && y12 <= y22)){
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}else{
			for (int i = 0; i < scenelist.size(); i++) {
				List<Map<String, Object>> regionlist = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> buttonlist = new ArrayList<Map<String, Object>>();
				if(scenelist.get(i).get("region") != null 
						&& !scenelist.get(i).get("region").equals("null")){
					regionlist = (List<Map<String, Object>>) scenelist.get(i).get("region");
					regionlist.addAll(staticregionlist);
				}else{
					regionlist.addAll(staticregionlist);
				}
				if(scenelist.get(i).get("button") != null
						&& !scenelist.get(i).get("button").equals("null")){
					buttonlist = (List<Map<String, Object>>) scenelist.get(i).get("button");
					buttonlist.addAll(staticbuttonlist);
				}else{
					buttonlist.addAll(staticbuttonlist);
				}
				if(regionlist.size() > 0 && buttonlist.size() > 0){
					for (int j = 0; j < regionlist.size(); j++) {
						String regiontype = regionlist.get(j).get("type").toString();
						if(regiontype.equals("5") && buttonlist != null && buttonlist.size() > 0){
							Integer x1 = Integer.parseInt(regionlist.get(j).get("left").toString());        //网页x坐标
							Integer y1 = Integer.parseInt(regionlist.get(j).get("top").toString());			//网页y坐标
							Integer m1 = Integer.parseInt(regionlist.get(j).get("width").toString());		//网页宽
							Integer n1 = Integer.parseInt(regionlist.get(j).get("height").toString());      //网页高
							for (int k = 0; k < buttonlist.size(); k++) {
								if(buttonlist.get(k).get("id").toString().equals("screenbutton"))
									continue;
								Integer x2 = Integer.parseInt(buttonlist.get(k).get("left").toString());    //按钮x坐标
								Integer y2 = Integer.parseInt(buttonlist.get(k).get("top").toString()); 	//按钮y坐标
								Integer m2 = Integer.parseInt(buttonlist.get(k).get("width").toString()); 	//按钮宽
								Integer n2 = Integer.parseInt(buttonlist.get(k).get("height").toString());  //按钮高
								Integer x12 = x1+m1;
								Integer y12 = y1+n1;
								Integer x22 = x2+m2;
								Integer y22 = y2+n2;
								if((x2 >= x1 && x2 <= x12 && y2 >= y1 && y2 <= y12)           //网页区域包含互动按钮
										|| (x22 >= x1 && x22 <= x12 && y2 >= y1 && y2 <= y12)
										|| (x2 >= x1 && x2 <= x12 && y22 >= y1 && y22 <= y12)
										|| (x22 >= x1 && x22 <= x12 && y22 >= y1 && y22 <= y12)){
									return true;
								}else if((x1 >= x2 && x1 <= x22 && y1 >= y2 && y1 <= y22)     //互动按钮包含网页区域
										|| (x12 >= x2 && x12 <= x22 && y1 >= y2 && y1 <= y22)
										|| (x1 >= x2 && x1 <= x22 && y12 >= y2 && y12 <= y22)
										|| (x12 >= x2 && x12 <= x22 && y12 >= y2 && y12 <= y22)){
									return true;
								}
							}
						}
					}
				}
				regionlist.removeAll(staticregionlist);
				buttonlist.removeAll(staticbuttonlist);

			}
			return false;
		}
	}
	/**
	 * 查询节目是否需要审核
	 * @return
	 */
	@RequestMapping("/program/showSendButton.do")
	@ResponseBody
	public Map<String, Object> showSendButton() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("auditStatus", modelService
				.queryModuleAudit(SystemConstant.PROGRAM_ID));
		return map;
	}
	/**
	 * 请求心跳 保证节目编辑过程中session不失效
	 * @return
	 */
	@RequestMapping("/program/conectRequest.do")
	@ResponseBody
	public Map<String, Object> conectRequest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	/**
	 * 删除节目单
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("/program/deleteProgram.do")
	@ResponseBody
	public Map<String, Object> deleteProgramByIds(
			@RequestParam(value = "ids[]") Integer[] ids,
			HttpServletRequest request) {
		UserBean o = (UserBean) request.getSession().getAttribute("user");
		List<ProgramBean> beanList = new ArrayList<ProgramBean>();
		List<Integer> idss = new ArrayList<Integer>();
		for (int i = 0; i < ids.length; i++) {
			idss.add(ids[i]);
		}
		beanList = programService.queryProgramByIds(idss);
		for (int j = 0; j < beanList.size(); j++) {
			operationlogService.insertOperationlog(o.getUser_id().toString(),
					beanList.get(j).getName(), SystemConstant.DELETE_OPERATION,
					SystemConstant.PROGRAM_ID);
		}

		programService.deleteProgramById(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "true");
		return map;
	}
	/**
	 * 字符串转换时间类型
	 * @param timestr
	 * @return
	 */
	private Timestamp string2Timestamp(String timestr) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(timestr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}
	/**
	 * 节目查询
	 * @param name
	 * @param program_id
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping("/program/queryProgram.do")
	@ResponseBody
	public Map<String, Object> queryProgram(String name, String program_id,
			String page, String rows, HttpServletRequest request) {
		ProgramBean programBean = new ProgramBean();
		int newpage = 1;
		int newrows = 0;
		if (page != null && page != "" && !page.equals("0")) {
			newpage = Integer.parseInt(page);
		}
		if (rows != null && rows != "") {
			newrows = Integer.parseInt(rows);
		}
		if (name != null && name != "") {
			programBean.setName(name);
		}
		if (program_id != null && program_id != "") {
			programBean.setProgram_id(Integer.parseInt(program_id));
		}
		// ///////////////////////////////////////
		UserBean o = (UserBean) request.getSession().getAttribute("user");
		Integer creatorid = null;
		List<Integer> userids = new ArrayList<Integer>();
		String elementPower = configService.queryConfig("elementPower");
		if (!o.getUsername().equals("admin")) {
			if (elementPower.equals("1")) {
				creatorid = o.getUser_id();
			} else if (elementPower.equals("2")) {
				userids = userService.queryUserIdsBySameRole(o.getUser_id());
			}
		}
		int total = programService.queryProgramCount(programBean, creatorid,
				userids);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		List<ProgramViewBean> programViewBeanList = new ArrayList<ProgramViewBean>();
		programViewBeanList = programService.queryProgram(programBean,
				pageInfo, creatorid, userids);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", programViewBeanList);
		map.put("total", total);
		return map;
	}
	/**
	 * 获取节目详细信息
	 * @param program_id
	 * @param request
	 * @return
	 */
	@RequestMapping("program/getProgram.do")
	@ResponseBody
	public Map<String, Object> getTemplate(
			@RequestParam(value = "program_id") String program_id,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String path = configService.queryConfig("ftpMappingUrl");
		List<Integer> id = new ArrayList<Integer>();
		id.add(Integer.parseInt(program_id));
		List<ProgramBean> program = programService.queryProgramByIds(id);
		if (program == null || program.size() < 1) {
			map.put("message", "该节目已经不存在！");
			map.put("success", false);
			return map;
		}
		String url = path + program.get(0).getUrl();
		XmlUtil xmlUtil = new XmlUtil();
		if (!xmlUtil.checkFile(url)) {
			map.put("success", false);
			map.put("msg", "节目文件已丢失！");
			return map;
		}
		Map<String, Object> programMap = xmlUtil.xmlToMap(url);
		map.put("programMap", programMap);
		map.put("success", true);
		return map;
	}
	/**
	 * 发布节目单
	 * @param programJson
	 * @param request
	 * @return
	 */
	@RequestMapping("program/sendProgram.do")
	@ResponseBody
	public Map<String, Object> sendProgram(
			@RequestParam(value = "programJson") String programJson,
			HttpServletRequest request) {
		Map<String, Object> programMap = MapUtil.parseJSON2Map(programJson);
		Integer program_id = Integer.parseInt(programMap.get("program_id").toString());
		String name = programMap.get("name").toString();
		String startdate = programMap.get("startdate").toString();
		String stopdate = programMap.get("stopdate").toString();
		String loopstarttime = programMap.get("loopstarttime").toString();
		String loopstoptime = programMap.get("loopstoptime").toString();
		String downloadEndTime = programMap.get("downloadEndTime").toString();
		String downloadStartTime = programMap.get("downloadStartTime").toString();
		String days = programMap.get("days").toString();
		boolean hasChange = Boolean.parseBoolean(programMap.get("hasChange").toString());
		boolean isloop = Boolean.parseBoolean(programMap.get("isloop").toString());
		String ids = programMap.get("ids").toString();
		Integer pubpower = Integer.parseInt(programMap.get("pubpower").toString());
		List<Map<String, Object>> loopsMap = (List<Map<String, Object>>) programMap.get("loops");
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查用户名是否重复
		if (checkName(name, program_id)) {
			map.put("success", false);
			map.put("msg", "该节目名已经存在！");
			return map;
		}
		List<Integer> tempIds = new ArrayList<Integer>();
		tempIds.add(program_id);
		ProgramBean programBean = new ProgramBean();
		List<ProgramBean> programs = programService.queryProgramByIds(tempIds);
		if (programs.size() > 0) {
			programBean = programs.get(0);
		}
		if (programBean.getSchedulelevel() != pubpower) {
			hasChange = true;
		}
		if (hasChange) {
			programBean.setSchedulelevel(pubpower);
			programBean.setProgram_id(program_id);
			programBean.setName(name);
			programBean.setStartTime(string2Timestamp(startdate));
			programBean.setEndTime(string2Timestamp(stopdate));
			programBean.setIsSend(SystemConstant.ISSEND_TRUE);
			String path = configService.queryConfig("ftpMappingUrl");
			String url = path + programBean.getUrl();
			Map<String, Object> tempMap = new XmlUtil().xmlToMap(url);
			Map<String, Object> twinflag = (Map<String, Object>) tempMap.get("twinflag");
			List<Map> program = (List<Map>) twinflag.get("program");
			df.format(new Date());
			program.get(0).put("updatetime", df.format(new Date()));
			program.get(0).put("startdate", startdate);
			program.get(0).put("stopdate", stopdate);
			program.get(0).put("loopdesc", days);
			program.get(0).put("loopstarttime", loopstarttime);
			program.get(0).put("loopstoptime", loopstoptime);
			program.get(0).put("name", name);
			program.get(0).put("pubpower", pubpower);
			program.get(0).put("isloop", isloop);
			program.get(0).put("loops", loopsMap);
			program.get(0).put("downloadEndTime", downloadEndTime);
			program.get(0).put("downloadStartTime", downloadStartTime);
			programService.updateProgram(programBean, program.get(0));
		}
		String[] idsStr = ids.split(",");
		Integer[] idsInt = new Integer[idsStr.length];
		for (int i = 0; i < idsStr.length; i++) {
			idsInt[i] = Integer.parseInt(idsStr[i]);
		}
		programService.send(idsInt, programBean);
		// programService.addRedis(programBean);
		map.put("success", true);
		return map;
	}
	/**
	 * 查询节目发布状态
	 * @param program_id
	 * @param page
	 * @param rows
	 * @param flag
	 * @param request
	 * @return
	 */
	@RequestMapping("/program/querySendP_T.do")
	@ResponseBody
	public Map<String, Object> querySendP_T(Integer program_id,
			String page,
			String rows,
			String flag,
			HttpServletRequest request) {
		String ip = configService.queryConfig("tfbsip");// 获取服务器IP
		if(ip == null){
			ip = configService.queryConfig("httpip");
		}
		List<ProgramTerminalView> programTerminalList = new ArrayList<ProgramTerminalView>();
		Map<String, Object> programmap = new HashMap<String, Object>();
		List<String> mac_pids = new ArrayList<String>();
		if(program_id != null){
			ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
			programTerminalBean.setProgram_id(program_id);
			programTerminalList = programService
					.queryProgramTerminal(programTerminalBean);
			//			Integer[] pids = new Integer[programTerminalList.size()];
			//			if (pids.length < 1) {
			//				return null;
			//			}
			//			pids[0] = programTerminalList.get(0).getProgram_id();
			//			List terminallist = programService.queryTerminalIdByProgramId(pids[0]);
			//			List terminalmaclist = terminalService
			//					.queryTerminalMacById(terminallist);
			//			for (int i = 0; i < programTerminalList.size(); i++) {
			//				mac_pids.add(programTerminalList.get(i).getMac() + "_" + program_id);
			//			}
		}else if(flag != null){
			String[] macList = request.getParameterValues("mac[]");
			programTerminalList = terminalService.queryTerminalPublishStatus(macList);
		}else{
			return null;
		}
		for (int i = 0; i < programTerminalList.size(); i++) {
			mac_pids.add(programTerminalList.get(i).getMac()+"_"+programTerminalList.get(i).getProgram_id());
		}
		programmap = getProgramStatus(ip,mac_pids);
		List<Map<String,Object>> list = (List) programmap.get("programmaplist");
		if (list != null && programTerminalList != null) {
			for (int i = 0; i < list.size(); i++) {
				String statu = (String) list.get(i).get("statu");
				if(statu.equals("100%")){
					programTerminalList.get(i).setProgram_status(RedisUtil.STATUS_SUCC.toString());
				}else{
					programTerminalList.get(i).setProgram_status(statu);
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("rows", programTerminalList);
		return map;
	}

	/**
	 * 查询节目下载状态
	 * @param ip
	 * @param mac_pids
	 * @return
	 */
	public Map<String, Object> getProgramStatus(String ip, List<String> mac_pids) {
		List<Map<String, Object>> programmaplist = new ArrayList<Map<String, Object>>();
		programmaplist = getStatusByMacOrPid.getProStatus(mac_pids,ip);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("programmaplist", programmaplist);
		return map;
	}

	@RequestMapping("program/queryMaterialDownload.do")
	@ResponseBody
	public Map<String, Object> queryMaterialDownload(String mac_pids_status,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (mac_pids_status == null || mac_pids_status.equals("")) {
			map.put("rows", "");
			return map;
		}
		Integer lastindex = mac_pids_status.lastIndexOf("_");
		String mac_pids = mac_pids_status.substring(0,lastindex);
		String ip = configService.queryConfig("tfbsip");// 获取服务器IP
		if (ip == null) {
			ip = configService.queryConfig("httpip");
		}
		List<Map<String, Object>> programmaplist = new ArrayList<Map<String, Object>>();
		programmaplist = getStatusByMacOrPid.getDownloadInfo(mac_pids,ip);
		List<ProgramDownloadView> programDownloadList = new ArrayList<ProgramDownloadView>();
		int downnum = 0;
		if (programmaplist != null) {
			for (int i = 0; i < programmaplist.size(); i++) {
				ProgramDownloadView pdv = new ProgramDownloadView();
				pdv.setElement_name((String) programmaplist.get(i).get("elem_name"));
				if(programmaplist.get(i).get("elem_size") != null){
					pdv.setElement_size(programmaplist.get(i).get("elem_size").toString());
					String terminalstatus = mac_pids_status.substring(lastindex + 1);
					if(terminalstatus.equals(RedisUtil.STATUS_SUCC.toString())){
						pdv.setElement_status(RedisUtil.STATUS_SUCC.toString());
					}else if(terminalstatus.equals("100%")){
						pdv.setElement_status(RedisUtil.STATUS_SUCC.toString());
					}else{
						pdv.setElement_status((String) programmaplist.get(i).get("statu"));
					}
				}else{
					pdv.setElement_size("0");
					pdv.setElement_status(RedisUtil.STATUS_SUCC.toString());
				}
				pdv.setElement_type((Integer) programmaplist.get(i).get("elem_type"));
				if(pdv.getElement_status().equals(RedisUtil.STATUS_SUCC.toString()))
					downnum++;
				programDownloadList.add(pdv);
			}
		}
		map.put("success", true);
		map.put("rows", programDownloadList);
		map.put("downloadnum", downnum);
		map.put("totalnum", programDownloadList.size());
		return map;
	}

	/**
	 * 检查节目完整性
	 * @param program_id
	 * @param request
	 * @return
	 */
	@RequestMapping("/program/checkProgram.do")
	@ResponseBody
	public Map<String, Object> checkProgram(Integer program_id,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String path = configService.queryConfig("ftpMappingUrl");
		List<Integer> id = new ArrayList<Integer>();
		id.add(program_id);
		List<ProgramBean> program = programService.queryProgramByIds(id);
		if (program == null || program.size() < 1) {
			map.put("message", "该节目已经不存在！");
			map.put("success", false);
			return map;
		}
		String url = path + program.get(0).getUrl();
		XmlUtil xmlUtil = new XmlUtil();
		if (!xmlUtil.checkFile(url)) {
			map.put("success", false);
			map.put("msg", "节目文件已丢失！");
			return map;
		}
		map.put("success", true);
		return map;
	}
	/**
	 * 停止节目发布
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("program/stopProgram.do")
	@ResponseBody
	public Map<String, Object> stopProgram(
			@RequestParam(value = "ids[]") Integer[] ids,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 获取Session中存储的对象
		UserBean o = (UserBean) session.getAttribute("user");
		List<ProgramBean> beanList = new ArrayList<ProgramBean>();
		List<Integer> idss = new ArrayList<Integer>();
		for (int i = 0; i < ids.length; i++) {
			idss.add(ids[i]);
		}
		beanList = programService.queryProgramByIds(idss);
		for (int j = 0; j < beanList.size(); j++) {
			operationlogService.insertOperationlog(o.getUser_id().toString(),
					beanList.get(j).getName(),
					SystemConstant.DEL_STOP_OPERATION,
					SystemConstant.PROGRAM_ID);
		}
		programService.stopProgram(ids);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "true");
		return map;
	}
	/**
	 * 停止节目中某个/多个终端的发布
	 * @param terminal_id
	 * @param program_id
	 * @param request
	 * @return
	 */
	@RequestMapping("program/stopProgramTerminal.do")
	@ResponseBody
	public Map<String, Object> stopProgramTerminal(
			@RequestParam(value = "terminal_id[]") Integer[] terminal_id,
			@RequestParam(value = "program_id") Integer program_id,
			HttpServletRequest request) {
		ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
		HttpSession session = request.getSession();
		// 获取Session中存储的对象
		UserBean o = (UserBean) session.getAttribute("user");
		for (Integer integer : terminal_id) {
			programTerminalBean.setProgram_id(program_id);
			programTerminalBean.setTerminal_id(integer);
			List<ProgramTerminalView> programTerminalList = programService.queryProgramTerminal(programTerminalBean);
			String elemName = "";
			if (null != programTerminalList && programTerminalList.size() > 0) {
				elemName = programTerminalList.get(0).getProgram_name() + "下的"
						+ programTerminalList.get(0).getTerminal_name() + "终端";
			}
			operationlogService.insertOperationlog(o.getUser_id().toString(),
					elemName, SystemConstant.DEL_STOP_OPERATION,
					SystemConstant.PROGRAM_ID);
		}
		// boolean returnStr =
		// programService.stopProgramTerminal(program_id,terminal_id);
		boolean returnStr = programService.stopProgramTerminals(program_id,terminal_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", returnStr);
		return map;
	}
	/**
	 * 检查节目名字合法性
	 * @param name
	 * @param id
	 * @return
	 */
	@RequestMapping("/program/checkProgramName.do")
	@ResponseBody
	public Map<String, Object> checkProgramName(
			@RequestParam(value = "name") String name, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", checkName(name, id));
		return map;
	}

	private boolean checkName(String name, Integer id) {
		List<ProgramBean> proList = programService.queryProgramByName(name);
		boolean hasName = false;
		if (null != id) {
			if (proList.size() > 1) {
				hasName = true;
			} else if (proList.size() == 1
					&& !proList.get(0).getProgram_id().equals(id)) {
				hasName = true;
			}
		} else if (proList.size() > 0) {
			hasName = true;
		}
		return hasName;
	}
	/**
	 * 审核节目
	 * @param auditStatus
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("/program/auditProgram.do")
	@ResponseBody
	public Map<String, Object> auditProgram(
			@RequestParam(value = "auditStatus") Integer auditStatus,
			@RequestParam(value = "ids[]") Integer[] ids,
			HttpServletRequest request) {
		ProgramBean bean = new ProgramBean();
		HttpSession session = request.getSession();
		// 获取Session中存储的对象
		UserBean o = (UserBean) session.getAttribute("user");
		bean.setAudit_status(auditStatus);
		List<ProgramBean> beanList = new ArrayList<ProgramBean>();
		List<Integer> idss = new ArrayList<Integer>();
		for (int i = 0; i < ids.length; i++) {
			idss.add(ids[i]);
		}
		beanList = programService.queryProgramByIds(idss);
		for (Integer id : ids) {
			bean.setProgram_id(id);
			programService.auditProgram(bean);
		}
		for (int j = 0; j < beanList.size(); j++) {
			operationlogService.insertOperationlog(o.getUser_id().toString(),
					beanList.get(j).getName(), SystemConstant.AUDTI_OPERATION,
					SystemConstant.PROGRAM_ID);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/program/getAuditStatus.do")
	@ResponseBody
	public Map<String, Object> getAuditStatus() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("auditStatus", modelService
				.queryModuleAudit(SystemConstant.PROGRAM_ID));
		return map;
	}

	// wangyulin 2014/12/18
	@RequestMapping("/program/isExitProgram.do")
	@ResponseBody
	public Map<String, Object> isExitProgram(HttpServletRequest request) {
		String[] urls = request.getParameterValues("urls[]");
		boolean haveFile = true;
		List<String> fileName = new ArrayList<String>();
		String path = configService.queryConfig("ftpMappingUrl");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < urls.length; i++) {
			if (!new FileCheck().checkFile(path + urls[i])) {
				haveFile = false;
				fileName.add(urls[i].substring(15, urls[i].length() - 4));
			} else {
				//				Set<String> Srclist = new FileCheck()
				//						.getSrcList(path + urls[i]);
				Map<String,Object> elemmap = new FileCheck().getSrcList(path + urls[i]);
				Set<String> Srclist = (Set<String>) elemmap.get("Srclist");
				if (Srclist.size() > 0) {
					if (!programService.queryElementCount(Srclist)) {
						haveFile = false;
						fileName.add(urls[i]
								.substring(15, urls[i].length() - 4));
					}
				}
				map.put("srclist", Srclist.size());
				if((Integer) elemmap.get("pluglist") > 0){
					haveFile = true;
				}
			}
		}
		map.put("haveFile", haveFile);
		map.put("fileName", fileName);
		return map;
	}

	// wangyulin 2014/12/19 start
	@RequestMapping("/playlistdown/GetDownloadListAction.action")
	@ResponseBody
	public Map<String, Object> ExportProgram(int id, HttpServletRequest request) {
		DownLoadList downloadList;
		Long size = 0L;
		List<String> listemp = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(id);
		downloadList = programService.findPlaylist(ids);
		for (int i = 0; i < downloadList.getElements().size(); i++) {
			if (isDocument(downloadList.getElements().get(i))) {
				String namePath = downloadList.getElements().get(i);
				namePath = namePath.substring(0, namePath.lastIndexOf("."))
						.substring(8);
				List<String> urllist = FileName(namePath);
				listemp.addAll(urllist);
			} else {
				listemp.add(downloadList.getElements().get(i).substring(8));
			}

		}
		downloadList.setElements(listemp);
		size = elementSize(downloadList.getElements());
		size += elementXMLSize(configService.queryConfig("ftpMappingUrl")
				+ downloadList.getXml());
		downloadList.setSize(size);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("downloadList", downloadList);
		map.put("id", id);
		return map;
	}

	private static boolean isDocument(String str) {
		String[] docs = { "xlsx", "docx", "pptx", "doc", "ppt", "xls", "pdf",
		"xps" };
		for (String s : docs) {
			if (str.toLowerCase().endsWith(s))
				return true;
		}
		return false;
	}

	public Long elementSize(List<String> list) {
		Long size = 0L;
		for (int i = 0; i < list.size(); i++) {
			File file = new File(configService.queryConfig("ftpMappingUrl")
					+ list.get(i));
			size += file.length();
		}
		return size;
	}

	public Long elementXMLSize(String path) {
		Long size = 0L;
		File file = new File(path);
		size = file.length();
		return size;
	}

	public List<String> FileName(String fileAbsolutePath) {
		List<String> tempList = new ArrayList<String>();

		File file = new File(configService.queryConfig("ftpMappingUrl")
				+ fileAbsolutePath);
		File[] subFile = file.listFiles();

		for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {

			// tempList.add(subFile[iFileLength].getAbsolutePath().substring(configService.queryConfig("ftpMappingUrl").length()));
			tempList.add(fileAbsolutePath + File.separator
					+ subFile[iFileLength].getName());

		}
		return tempList;
	}

	@RequestMapping("/program/	copyProgram.do")
	@ResponseBody
	public Map<String, Object> copyProgram(
			@RequestParam(value = "programJson") String programJson,
			HttpServletRequest request) {
		Map<String, Object> programMap = MapUtil.parseJSON2Map(programJson);
		Integer program_id = Integer.parseInt(programMap.get("program_id")
				.toString());
		String name = programMap.get("name").toString();
		String startdate = programMap.get("startdate").toString();
		String stopdate = programMap.get("stopdate").toString();
		String loopstarttime = programMap.get("loopstarttime").toString();
		String loopstoptime = programMap.get("loopstoptime").toString();
		String days = programMap.get("days").toString();
		Integer pubpower = Integer.parseInt(programMap.get("pubpower")
				.toString());
		List<Map<String, Object>> loopsMap = (List<Map<String, Object>>) programMap
				.get("loops");
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查用户名是否重复
		if (checkName(name, null)) {
			map.put("success", false);
			map.put("msg", "该节目名已经存在！");
			return map;
		}
		List<Integer> tempIds = new ArrayList<Integer>();
		tempIds.add(program_id);
		ProgramBean programBean = new ProgramBean();
		List<ProgramBean> programs = programService.queryProgramByIds(tempIds);
		if (programs.size() > 0) {
			programBean = programs.get(0);
		}
		String path = configService.queryConfig("ftpMappingUrl");
		String url = path + programBean.getUrl();
		programBean.setProgram_id(null);
		programBean.setName(name);
		programBean.setStartTime(string2Timestamp(startdate));
		programBean.setEndTime(string2Timestamp(stopdate));
		programBean.setIsSend(SystemConstant.ISSEND_FALSE);
		programBean.setCreate_time(new Timestamp(System.currentTimeMillis()));
		programBean.setDeleted(SystemConstant.ISDELETED_FALSE);
		programBean.setUrl("programXmlFile/" + programBean.getName() + ".xml");
		programBean.setAudit_status(modelService
				.queryModuleAudit(SystemConstant.PROGRAM_ID));
		programBean.setSchedulelevel(pubpower);
		UserBean o = (UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(o.getUser_id().toString(), name,
				SystemConstant.INSERT_OPERATION, SystemConstant.PROGRAM_ID);
		// 创建者信息
		programBean.setCreator_id(o.getUser_id());
		Map<String, Object> tempMap = new XmlUtil().xmlToMap(url);
		Map<String, Object> twinflag = (Map<String, Object>) tempMap
				.get("twinflag");
		List<Map> program = (List<Map>) twinflag.get("program");
		df.format(new Date());
		program.get(0).put("updatetime", df.format(new Date()));
		program.get(0).put("startdate", startdate);
		program.get(0).put("stopdate", stopdate);
		program.get(0).put("loopdesc", days);
		program.get(0).put("loopstarttime", loopstarttime);
		program.get(0).put("loopstoptime", loopstoptime);
		program.get(0).put("name", name);
		program.get(0).put("pubpower", pubpower);
		program.get(0).put("loops", loopsMap);
		programService.insertProgram(programBean, program.get(0));
		map.put("success", true);
		return map;
	}

	@RequestMapping("/program/queryIsConflict.do")
	@ResponseBody
	public Map<String, Object> queryIsConflict(
			@RequestParam(value = "startdate") String startdate,
			@RequestParam(value = "stopdate") String stopdate,
			@RequestParam(value = "pubpower") Integer pubpower,
			@RequestParam(value = "istouch") String istouch,
			Integer program_id, HttpServletRequest request)
					throws ParseException {
		String[] terminalIds = request.getParameterValues("terminalIds[]");
		String[] timearr = request.getParameterValues("timearr[]");
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Boolean isconflict = false;
		List<String> conflictterminalList = new ArrayList<String>();
		for (int m = 0; m < terminalIds.length; m++) {
			List<ProgramBean> programList = programService
					.queryProgramTimeByTerminalId(terminalIds[m], program_id);
			Long sendstarttime = SDF.parse(startdate).getTime();
			Long sendstoptime = SDF.parse(stopdate).getTime();
			XmlUtil util = new XmlUtil();
			for (int i = 0; i < programList.size(); i++) {
				Integer comparepubpower = programList.get(i).getSchedulelevel();
				Long comparestartdate = SDF.parse(programList.get(i).getStartTime().toString()).getTime();
				Long compareenddate = SDF.parse(programList.get(i).getEndTime().toString()).getTime();
				String xmlcontent = programList.get(i).getXmlcontent();
				xmlcontent = xmlcontent.replace("\\", "/");
				if ((pubpower < comparepubpower                          //普通节目查询冲突时，若同一终端存在相同优先级的互动节目
						&& (sendstarttime <= compareenddate)             //有冲突提示；互动节目查询冲突，没有冲突提示
						&& (sendstoptime >= comparestartdate) && istouch.equals("true")) 
						|| (pubpower <= comparepubpower
						&& (sendstarttime <= compareenddate)
						&& (sendstoptime >= comparestartdate) && istouch.equals("false"))) {
					for (int j = 0; j < timearr.length; j++) {
						Map<String, Object> xmlMap = util
								.stringToMap(xmlcontent);
						Map<String, Object> twinflagMap = (Map<String, Object>) xmlMap
								.get("twinflag");
						List<Map<String, Object>> programMap = (List<Map<String, Object>>) twinflagMap
								.get("program");
						List<Map<String, Object>> loopsMap = (List<Map<String, Object>>) programMap
								.get(0).get("loops");
						List<Map<String, Object>> loopMap = (List<Map<String, Object>>) loopsMap
								.get(0).get("loop");
						String[] comparedays = {};
						String[] senddays = timearr[j].split("@")[2].split(",");
						for (int k = 0; k < loopMap.size(); k++) {
							comparedays = loopMap.get(k).get("days").toString()
									.split(",");
							if (checkHasSameDay(senddays, comparedays)) {
								String comparestarttime = loopMap.get(k).get(
										"starttime").toString();
								String compareendtime = loopMap.get(k).get(
										"endtime").toString();
								if (checkHasSameTime(timearr, comparestarttime,
										compareendtime)) {
									SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
									Date comet = sdf.parse(compareendtime);
									Date nowdate = new Date();
									long l = nowdate.getHours()*3600000+nowdate.getMinutes()*60000+nowdate.getSeconds()*1000;
									long cl = comet.getHours()*3600000+comet.getMinutes()*60000+comet.getSeconds()*1000;
									if(cl >= l){
										map.put("isconflict", true);
										isconflict = true;
									}
								}
							}
						}
					}
				}
			}
			if (isconflict) {
				isconflict = false;
				Integer terminalId = Integer.parseInt(terminalIds[m]);
				String contername = programService
						.queryTerminalNameByTerminalId(terminalId);
				conflictterminalList.add(contername);
			}
		}
		map.put("conflictterminals", conflictterminalList);
		map.put("success", true);
		return map;
	}

	@RequestMapping("/program/queryTerminalIsTiming")
	@ResponseBody
	public Map<String,Object> queryTerminalIsTiming(HttpServletRequest request){
		String[] terminalId = request.getParameterValues("terminalids[]");
		List<TerminalDownloadTime> terminalCloseTime = terminalStatusService.queryTerminalTimingSet(terminalId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("terminalCloseTime", terminalCloseTime);
		return map;
	}

	private boolean checkHasSameTime(String[] timearr, String comparestarttime,
			String compareendtime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		for (int i = 0; i < timearr.length; i++) {
			Date sendstarttime = sdf.parse(timearr[i].split("@")[0]);
			Date sendendtime = sdf.parse(timearr[i].split("@")[1]);
			Date comst = sdf.parse(comparestarttime);
			Date comet = sdf.parse(compareendtime);
			if ((sendstarttime.getTime() <= comet.getTime())
					&& (sendendtime.getTime() >= comst.getTime())) {
				return true;
			}
		}
		return false;
	}
	private boolean checkHasSameDay(String[] senddays, String[] comparedays) {
		for (int i = 0; i < senddays.length; i++) {
			for (int j = 0; j < comparedays.length; j++) {
				if (senddays[i].equals(comparedays[j])) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * <p>          
	 *       <discription> 概述：节目预览 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2017年3月13日 下午2:49:57
	 * @UpdateDate     更新时间：   2017年3月13日 下午2:49:57
	 * @Package_name   包名：          iiswoftp/controller.program
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          String
	 */
	@RequestMapping("program/playProgram/{programId}")
	public String playProgram(@PathVariable String programId,Model model){
		if (StringUtils.isBlank(programId)) {
			model.addAttribute("msg", "参数Null");
			return "program/play/PlayProgram";
		}
		ProgramBean program=new ProgramBean();
		program.setProgram_id(Integer.parseInt(programId));
		String xmlContent=programService.selectProgramXmlById(program);
		Map<String, Object> map=XmlUtil.stringToMap(xmlContent);
		PlayProgram  Program=MapUtil.mapToEntity(map);
		List<region> regionList=MapUtil.getregionList(Program);
		String height=MapUtil.getProgramHeight(Program);
		String  width=MapUtil.getProgramWidth(Program);
		String  name=MapUtil.getProgramName(Program);
		model.addAttribute("region", regionList);
		model.addAttribute("height", height);
		model.addAttribute("width", width);
		model.addAttribute("name", name);
		return "program/play/PlayProgram";
	}
}
