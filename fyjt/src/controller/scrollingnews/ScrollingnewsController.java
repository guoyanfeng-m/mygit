package controller.scrollingnews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



import util.GetXml;
import util.PageInfo;
import service.config.ConfigService;
import service.model.ModelService;
import service.operationlog.OperationlogService;
import service.scrollingnews.ScrollingnewsService;
import service.scrollingnews.ScrollingnewsTerminalService;
import service.terminal.TerminalService;
import service.user.UserService;
import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsBean;
import beans.scrollingnews.ScrollingnewsTerminalBean;
import beans.sys.SystemConstant;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalViewBean;
import beans.user.UserBean;

@SuppressWarnings({"unchecked","rawtypes"})
@Controller
@Transactional
public class ScrollingnewsController {
	@Autowired
	private UserService userService;
	@Autowired
	private ScrollingnewsService sService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ScrollingnewsTerminalService scrollTerminalService;
	@Autowired
	private OperationlogService operationlogService;
	@Autowired
	private TerminalService terminalService;
	Timestamp datetime = new Timestamp(new Date().getTime());
	private GetXml xml = new GetXml();
	/**
	 * <p>          
	 *       <discription> 概述：添加 </discription>
	 * </p>  
	 * @Author         创建人：       
	 * @CreateDate     创建时间：   
	 * @UpdateDate     更新时间：   
	 * @Package_name   包名：          iis/controller.scrollingnews
	 * @Param          参数：          @param sname
	 * @Param          参数：          @param font
	 * @Param          参数：          @param font_size
	 * @Param          参数：          @param font_color
	 * @Param          参数：          @param text
	 * @Param          参数：          @param scroll_direction
	 * @Param          参数：          @param scroll_speed
	 * @Param          参数：          @param start_time
	 * @Param          参数：          @param end_time
	 * @Param          参数：          @param request
	 * @Param          参数：          @return
	 * @Param          参数：          @throws ParseException  
	 * @Rerurn         返回：          String
	 */
	@RequestMapping("scrollingnews/insertScroll.do")
    @ResponseBody
	 public String insertScroll(
			 @RequestParam(value="sname")String sname,
			 @RequestParam(value="font")String font,
			 @RequestParam(value="font_size")String font_size,
			 @RequestParam(value="font_color")String font_color,
			 @RequestParam(value="text")String text,
			 @RequestParam(value="scroll_direction")String scroll_direction,
			 @RequestParam(value="scroll_speed")Integer scroll_speed,
			 @RequestParam(value="start_time")String start_time,
			 @RequestParam(value="end_time")String end_time,
			 HttpServletRequest request) throws ParseException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ScrollingnewsBean sBean = new ScrollingnewsBean();
		sBean.setSname(sname);
		sBean.setFont(font);
		sBean.setFont_size(font_size);
		sBean.setFont_color(font_color);
		sBean.setText(text);
		sBean.setScroll_direction(scroll_direction);
		sBean.setScroll_speed(scroll_speed);
		sBean.setStart_time(start_time);
		sBean.setEnd_time(end_time);
		sBean.setXmlcontent("");
		int audit=modelService.queryModuleAudit(SystemConstant.SCROLINGNEWS_ID); //判断是否需要审核
		sBean.setAudit_status(audit);
		sBean.setUpdate_time(datetime);
		sBean.setCreator_id(user.getUser_id());
		sBean.setDeleted(SystemConstant.ISDELETED_FALSE);
		sBean.setIsSend(SystemConstant.ISSEND_FALSE);
		sService.insertScroll(sBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "true");  
		
		operationlog(user.getUser_id().toString(),sBean.getSname(),SystemConstant.INSERT_OPERATION);
		return "success";
	}
	/****
	 * 修改
	 * @param new_id
	 * @param request
	 * @return
	 */
	@RequestMapping("/scrollingnews/updateScroll.do")
	@ResponseBody
	public Map<String,Object>  updateScroll(ScrollingnewsBean scrollBean, 
			HttpServletRequest request) {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		int audit=modelService.queryModuleAudit(SystemConstant.SCROLINGNEWS_ID); //判断是否需要审核
		if(audit==0){ //如果需要审核
			audit=3;  //修改后将审批状态变为’未审批‘
		}
		scrollBean.setAudit_status(audit);  
		scrollBean.setCreator_id(user.getUser_id());
		scrollBean.setIsSend(3);
	    sService.updateScroll(scrollBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success"); 
		
		operationlog(user.getUser_id().toString(),scrollBean.getSname(),SystemConstant.UPDATE_OPERATION);
		return map;
	}
	/****
	 * 删除(包含批量删除)
	 * @param new_id
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/scrollingnews/delScroll.do")
	@ResponseBody  //在SpringMVC中可以在Controller的某个方法上加@ResponseBody注解，表示该方法的返回结果直接写入HTTP response body中。
	public Map<String,Object> delScroll(HttpServletRequest request) throws IOException {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String path = configService.queryConfig("ftpMappingUrl"); // 获取服务器路径
		String [] id=request.getParameterValues("new_ids[]");
		Integer[] ids = new Integer[id.length];
		for(int i=0;i<id.length;i++){
			ids[i] = Integer.parseInt(id[i]);
			ScrollingnewsBean sc=new ScrollingnewsBean();
			sc.setNew_id(ids[i]);
			sc=sService.queryScroll(sc); //查询
			
			sc.setDeleted(SystemConstant.ISDELETED_TRUE);
			sc.setXmlcontent("");
			sc.setIsSend(3);  
			sService.updateScroll(sc); //修改
			
			//删除对应的终端id
			ScrollingnewsTerminalBean ts=new ScrollingnewsTerminalBean();
			ts.setScrollingNews_id(Integer.parseInt(id[i]));
			scrollTerminalService.delScrollTerminal(ts); // 删除对应的终端
			
			/** 删除发布后存放在服务器上的xml文件 **/
			String sPath = path + "scrollingnewsXmlFile/" + sc.getSname()+ ".xml";
			deleteFile(sPath);
			
			operationlog(user.getUser_id().toString(),sc.getSname(),SystemConstant.DELETE_OPERATION);
		}
		
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success");  
		return map;
	}
	/***
	 * 条件查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/scrollingnews/queryScroll.do")
	@ResponseBody
	public Map<String,Object>  queryScroll(ScrollingnewsBean scrollBean,
			HttpServletRequest request) {
		ScrollingnewsBean sBeanList = new ScrollingnewsBean();
		sBeanList = sService.queryScroll(scrollBean);
		sBeanList.setStart_time(sBeanList.getStart_time().toString().substring(0,sBeanList.getStart_time().indexOf(".")));
		sBeanList.setEnd_time(sBeanList.getEnd_time().toString().substring(0,sBeanList.getEnd_time().indexOf(".")));
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("sBeanList", sBeanList);
		return map;
	}
	/***
	 * 判断滚动消息名称是否已经存在
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/scrollingnews/checkName.do")
	@ResponseBody
	public Map<String,Object>  checkName(String new_id,String sname,String type,
			HttpServletRequest request) {
		ScrollingnewsBean scrollBean=new ScrollingnewsBean();
		boolean success=false;
		Map<String,Object> map = new HashMap<String,Object>();  
		ScrollingnewsBean sBean = new ScrollingnewsBean();
	    if(type.equals("add")){
	    	scrollBean.setNew_id(null);
	    	scrollBean.setSname(sname);
			sBean = sService.queryScroll(scrollBean);
			if(null==sBean){
				success=false; //表示不存在
			    map.put("success",success);	
			}else{
				success=true;
			    map.put("success",success);	
			}
	    }else{  //修改 type="edit"
	    	scrollBean.setNew_id(Integer.parseInt(new_id));
			sBean = sService.queryScroll(scrollBean);
	    	if(sname.equals(sBean.getSname())){
	    		success=false;  
	    		map.put("success", success);
	    	}else{
	    		scrollBean.setNew_id(null);
	    		scrollBean.setSname(sname);
	    		sBean = sService.queryScroll(scrollBean);
	    		if(sBean!=null){
	    			if(sname.equals(sBean.getSname())){
		    			success=true;  //表示已经存在
		    			map.put("success", success);
		    		}
	    		}else{
	    			success=false; //表示不存在
				    map.put("success",success);	
	    		}
	    	}
	    }	
		return map;
	}
	/***
	 * 查询所有
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/scrollingnews/queryAll.do")
    @ResponseBody
	public Map<String,Object> queryAll(
			ScrollingnewsBean scroll,
			String page,//当前页
			String rows,//一页显示条数
			HttpServletRequest request) throws UnsupportedEncodingException{
		List<ScrollingnewsBean> sBeanList = new ArrayList<ScrollingnewsBean>();
		List<ScrollingnewsBean> sBeanList2 = new ArrayList<ScrollingnewsBean>();
		////////////////////////////////////////
		Integer creatorid=null;
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		String elementPower = configService.queryElementPower();
//		int userid=-1;
		List<Integer> userids=new ArrayList();
		if (!ub.getUsername().equals("admin")) {
			if(elementPower.equals("1")){
				creatorid=ub.getUser_id();
			
			}else if (elementPower.equals("2")) {
				userids=userService.queryUserIdsBySameRole(ub.getUser_id());
			}
		}
//		scroll.setCreator_id(userid);
		if((scroll.getSname()!=null)){
			//String str=new String(scroll.getName().getBytes("ISO-8859-1"),"utf-8"); //乱码转换
			scroll.setSname(scroll.getSname().replaceAll(" ", ""));
		}
		sBeanList = sService.queryAll(scroll,creatorid,userids);
		
		int newpage = 1;
		int newrows = 0;
		if(page!=null&&page!="" &&!page.equals("0")){
			newpage = Integer.parseInt(page);
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		int total = sBeanList.size();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		List<ScrollingnewsBean> pageList=sService.queryPage(scroll, pageInfo,creatorid,userids);
		ScrollingnewsBean s=new ScrollingnewsBean();
		for(int i=0;i<pageList.size();i++){
			s=pageList.get(i);
			s.setStart_time(s.getStart_time().toString().substring(0,s.getStart_time().indexOf(".")));
			s.setEnd_time(s.getEnd_time().toString().substring(0,s.getEnd_time().indexOf(".")));
			sBeanList2.add(s);
		}
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", pageList);  //返回的要显示的数据 
		map.put("total", total);//共多少数据
		return map;
	}
	/****
	 * 关联查询
	 * @return
	 */
	@RequestMapping("/scrollingnews/queryView.do")
	@ResponseBody
	public Map<String,Object> queryView(HttpServletRequest request){
		List<ScrollTerminalView> views=new ArrayList<ScrollTerminalView>();
		views=sService.queryView();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("views", views);
		return map;
	}
	/****
	 * 发布
	 */
	@RequestMapping("/scrollingnews/publish.do")
	@ResponseBody
	public Map<String,Object> publish(HttpServletRequest request){
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String [] terminalIds=request.getParameterValues("terminalIds[]");   //获得要发布的终端ID
		String [] new_ids=request.getParameterValues("new_ids[]");           //获得要发布消息的ID
		
		List<ScrollingnewsBean> list=new ArrayList<ScrollingnewsBean>();
		for(int i=0;i<new_ids.length;i++){
			ScrollingnewsBean scrollBean=new ScrollingnewsBean();
			scrollBean.setNew_id(Integer.parseInt(new_ids[i]));
			scrollBean=sService.queryScroll(scrollBean);
			list.add(scrollBean);  //根据消息ID查询信息
			
			operationlog(user.getUser_id().toString(),scrollBean.getSname(),SystemConstant.PUBLISH_OPERATION);
		}
		String ip=configService.queryConfig("tfbsip");// 获取服务器ip
		if(ip==null){
		  ip=configService.queryConfig("httpip");
		}
		BuildXML(list, new_ids, terminalIds, ip); // 调用发布方法

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", "success");
		
		
		return map;
	}
	
	// 将发布的信息保存到xml文件中去
	public void BuildXML(List<ScrollingnewsBean> list, String[] new_ids,String[] terminalIds, String ip) {
		String path = configService.queryConfig("ftpMappingUrl"); // 获取服务器路径
		String new_id = null;
		String[] fileUrl = new String[list.size()];
		// 获取选中终端的MAC地址
		String[] mac = new String[terminalIds.length];
		for (int i = 0; i < terminalIds.length; i++) {
			TerminalBean terminalBean = new TerminalBean();
			terminalBean.setTerminal_id(Integer.parseInt(terminalIds[i]));
			List<TerminalViewBean> viewBean=new ArrayList<TerminalViewBean>();
			viewBean = terminalService.queryTerminalWithoutPage(terminalBean); //根据终端ID查询终端
			if(viewBean.size() == 0) {
				continue;
			}
			mac[i] = viewBean.get(0).getMac();
		}
		try {
				for (int i = 0; i < list.size(); i++) {
					new_id = (Integer.toString(list.get(i).getNew_id()));
					String name=list.get(i).getSname();
					String text = replaceBlank(list.get(i).getText()); // ]java去除字符串中的空格、回车、换行符
					String font = list.get(i).getFont();
					String textSize = list.get(i).getFont_size();
					String textColor = list.get(i).getFont_color();
					String scrollDirection = list.get(i).getScroll_direction();
					String scrollSpeed = (Integer.toString(list.get(i).getScroll_speed()));
					String startTime =(list.get(i).getStart_time().toString()).substring(0,(list.get(i).getStart_time().toString()).indexOf("."));
					String endTime = (list.get(i).getEnd_time().toString()).substring(0,(list.get(i).getEnd_time().toString()).indexOf("."));
					if (scrollDirection.equals("left")) {
						scrollDirection = "3";
					} else if (scrollDirection.equals("right")) {
						scrollDirection = "4";
					}else if(scrollDirection.equals("down")){
						scrollDirection="2";
					}else{
						scrollDirection="1";
					}
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); // 创建操作的Document对象
					Element root = (Element) doc.createElement("scrollingnew");
					doc.appendChild((Node) root); // 将根节点添加到Document对象中去

					Element element = doc.createElement("runmsgdata");
					element.setAttribute("id", new_id);
					element.setAttribute("name", name);
					element.setAttribute("font", font);
					element.setAttribute("fgcolor", textColor);
					element.setAttribute("bgcolor", "#00ffffff");
					element.setAttribute("size", textSize);
					element.setAttribute("direction", scrollDirection);
					element.setAttribute("position", scrollDirection);
					element.setAttribute("speed", scrollSpeed);
					element.setAttribute("startdate", startTime);
					element.setAttribute("stopdate", endTime);
					element.setAttribute("updatetime", list.get(i).getUpdate_time().toString().substring(0, 19));
					element.setAttribute("state", "1"); // 发布的时候将状态变为1
					element.setAttribute("content", text);

					root.setAttribute("id", new_id);
					root.appendChild(element);

					fileUrl[i] = "scrollingnewsXmlFile/"+ list.get(i).getSname() + ".xml";

					String savePath = path + fileUrl[i];
					TransformerFactory transFactory = TransformerFactory.newInstance(); // 创建转换对象
					Transformer transformer = null;
					transformer = transFactory.newTransformer();

					DOMSource domSource = new DOMSource(doc); // 转换源DOM树
					File file = new File(savePath);

					FileOutputStream out = new FileOutputStream(file);
					StreamResult xmlResult = new StreamResult(out);
					transformer.transform(domSource, xmlResult);

					// 调用发布的方法
					xml.strXml(path, fileUrl[i], mac, ip);
					// 调用方法，将发布的信息生成的xml文件读取出来，转换成字符串让后保存到数据库表中xmlcontent字段中去
					String xmlcontent = xml.XmlToString(path, fileUrl[i]);
					ScrollingnewsBean news = new ScrollingnewsBean();
					news.setNew_id(Integer.parseInt(new_ids[i]));
					news = sService.queryScroll(news); 
					news.setXmlcontent(xmlcontent); // 将发布的信息的xml信息保存到数据库中的xmlcontent
					news.setIsSend(SystemConstant.ISSEND_TRUE); // 发布之后将isSend变为 "是"
					news.setAudit_status(SystemConstant.APPROVE_STATUS_SUCCESS);
					sService.updateScroll(news);
					
				}		
			// 将滚动消息和对应终端id保存到数据库中
			List<ScrollingnewsTerminalBean> lists = new ArrayList<ScrollingnewsTerminalBean>();
			for (int m = 0; m < new_ids.length; m++) {
				ScrollingnewsTerminalBean t=new ScrollingnewsTerminalBean();
				t.setScrollingNews_id((Integer.parseInt(new_ids[m])));
				lists = scrollTerminalService.queryScrollTerminal(t); // 根据滚动消息Id在TScrollingnewsTerminalBean中间表中查找对应的terminalId
				
				if (lists.size() == 0) { //这是之前没有发布过的
					for (int n = 0; n < terminalIds.length; n++) {
						ScrollingnewsTerminalBean scrollingnewsTerminal = new ScrollingnewsTerminalBean();
						scrollingnewsTerminal.setScrollingNews_id(Integer.parseInt(new_ids[m]));
						scrollingnewsTerminal.setTerminal_id(Integer.parseInt(terminalIds[n]));
						scrollTerminalService.insertScrollTerminal(scrollingnewsTerminal);
					}
				} else {
					// 将数据库中查出来的终端对象中的TerminalId放入集合
					List<Integer> listTerId = new ArrayList<Integer>();
					for (int i = 0; i < lists.size(); i++) {
						listTerId.add(lists.get(i).getTerminal_id());
					}
					for (int n = 0; n < terminalIds.length; n++) {
						if (listTerId.contains((Integer.parseInt(terminalIds[n])))) { // 判断是否包含
							continue;
						} else {// 不包含则存放到数据库
							ScrollingnewsTerminalBean scrollingnewsTerminal = new ScrollingnewsTerminalBean();
							scrollingnewsTerminal.setScrollingNews_id(Integer.parseInt(new_ids[m]));
							scrollingnewsTerminal.setTerminal_id(Integer.parseInt(terminalIds[n]));
							scrollTerminalService.insertScrollTerminal(scrollingnewsTerminal);
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/****
	 * 发送停止消息的命令
	 * 
	 * @return
	 */
	@RequestMapping("/scrollingnews/stopCommand.do")
    @ResponseBody
	public Map<String,Object> stopCommand(HttpServletRequest request) {
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		String [] new_ids=request.getParameterValues("new_ids[]");
		String path = configService.queryConfig("ftpMappingUrl"); // 获取服务器路径
		ScrollingnewsTerminalBean scrollTerminalBean=new ScrollingnewsTerminalBean();
		List<ScrollingnewsTerminalBean> lists = new ArrayList<ScrollingnewsTerminalBean>();
		for (int i = 0; i < new_ids.length; i++) {
			lists.clear(); // 清除集合重新存入下一个TerminalId 终端ID
			scrollTerminalBean.setScrollingNews_id((Integer.parseInt(new_ids[i])));
			lists = scrollTerminalService.queryScrollTerminal(scrollTerminalBean); // 根据TId查询Terminal表得到TerminalId

			/** 根据终端id查找终端对应的MAC地址 **/
			String[] mac = new String[lists.size()]; // 存放终端对应的MAC地址
			for (int n = 0; n < lists.size(); n++) {
				TerminalBean termina = new TerminalBean();
				termina.setTerminal_id(lists.get(n).getTerminal_id());
				List<TerminalViewBean> terminaview = new ArrayList<TerminalViewBean>();
			    terminaview = terminalService.queryTerminalWithoutPage(termina);
				if (terminaview.size() ==0) {
					continue;
				}
				mac[n] = terminaview.get(0).getMac();
			}
			String ip=configService.queryConfig("tfbsip");// 获取服务器IP
			if(ip==null){
			  ip=configService.queryConfig("httpip");
			}
			xml.stopCommand(mac, new_ids[i], ip); // 发送一个滚动消息的ID 和他对应的终端的MAC地址
			ScrollingnewsTerminalBean ts=new ScrollingnewsTerminalBean();
			ts.setScrollingNews_id(Integer.parseInt(new_ids[i]));
			scrollTerminalService.delScrollTerminal(ts); // 删除对应的终端

			// 根据传过来的编号查找信息，让后将isDelete变为true,其他的字段不变然后修改到数据库中，假删除
			ScrollingnewsBean sBean=new ScrollingnewsBean();
			sBean.setNew_id(Integer.parseInt(new_ids[i]));
			ScrollingnewsBean news = sService.queryScroll(sBean);
			news.setIsSend(3); // 停止之后将isSend变为 "否"
			news.setXmlcontent(""); // 清空xmlcontent

			sService.updateScroll(news);
			operationlog(user.getUser_id().toString(),news.getSname(),SystemConstant.DEL_STOP_OPERATION);

			/** 删除发布后存放在服务器上的xml文件 **/
			String sPath = path + "scrollingnewsXmlFile/" + news.getSname()+ ".xml";
			File f=new File(sPath);
			if(f.exists()){
				f.delete();
			}
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", "success");
		
		return map;
	}
	/****
	 * 根据某一消息发送停止消息的命令
	 * 
	 * @return
	 */
   @RequestMapping("/scrollingnews/stopOneCommand.do")
   @ResponseBody
   public Map<String,Object> stopOneCommand(HttpServletRequest request) {
	    String  new_id=request.getParameter("new_id");
	    String [] terminalIds=request.getParameterValues("terminalIds[]");
	    String [] mac=request.getParameterValues("mac[]");
		String ip=configService.queryConfig("tfbsip");// 获取服务器IP
		if(ip==null){
		  ip=configService.queryConfig("httpip");
		}
		xml.TaskstopCommand(mac, new_id, ip); // 发送一个滚动消息的ID 和他对应的终端的MAC地址
	    ScrollingnewsTerminalBean scrollTerminalBean=new ScrollingnewsTerminalBean();
	    scrollTerminalBean.setScrollingNews_id(Integer.parseInt(new_id));
		for(int m=0;m<terminalIds.length;m++){
			scrollTerminalBean.setTerminal_id(Integer.parseInt(terminalIds[m]));
			scrollTerminalService.delScrollTerminal(scrollTerminalBean); // 删除对应的中间表
		}
		ScrollingnewsBean scrollnewsBean=new ScrollingnewsBean();
		ScrollingnewsTerminalBean scrollnewsTerminalBean=new ScrollingnewsTerminalBean();
		scrollnewsTerminalBean.setScrollingNews_id(Integer.parseInt(new_id));
		List<ScrollingnewsTerminalBean> lists=scrollTerminalService.queryScrollTerminal(scrollnewsTerminalBean);
		if(lists.size()==0){
			scrollnewsBean.setNew_id(Integer.parseInt(new_id));
			scrollnewsBean=sService.queryScroll(scrollnewsBean);
			scrollnewsBean.setXmlcontent("");
			scrollnewsBean.setIsSend(3);
			sService.updateScroll(scrollnewsBean);
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", "success");
		//setLogPower();
		//operationlog(user.getUsername(),scrollnewsBean.getName(),6);
		return map;
	}
	/****
	 * 判断要发布的文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public String isFile() {
		String path = configService.queryConfig("ftpMappingUrl");
		String fileName = path + "scrollingnewsXmlFile/" +" name" + ".xml";
		File file = new File(fileName);
		if (!file.exists()) {
			//success = false;
		} else {
			//success = true;
		}
		return "success";
	}

	/***
	 * 删除服务器上的xml
	 * 
	 * @param sPath
	 */
	public void deleteFile(String sPath) {
		File file = new File(sPath);
		// 路径为文件不为空则进行删除
		if (file.exists()) {
			file.delete();
		}
	}

	// java去除字符串中的空格、回车、换行符、制表符
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			//Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Pattern p = Pattern.compile("\r\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			 /*  注：\n 回车(\u000a) 
			    \t 水平制表符(\u0009) 
			    \s 空格(\u0008) 
			    \r 换行(\u000d)*/
		}
		return dest;
	}
	/****
	 * 调用日志
	 */
	public void operationlog(String userid,String operationName,Integer operationType) {
			operationlogService.insertOperationlog(userid,operationName,operationType,SystemConstant.SCROLINGNEWS_ID);
	}

	/****
	 * 审批
	 * 
	 * @return
	 */
   @RequestMapping("/scrollingnews/audit_statis.do")
   @ResponseBody
   public Map<String,Object> audit_statis(@RequestParam(value = "val") Integer val,HttpServletRequest request) {
	        UserBean user=(UserBean) request.getSession().getAttribute("user");
	        String []ids=request.getParameterValues("new_ids[]");
	        for(int i=0;i<ids.length;i++){
	        	ScrollingnewsBean scrollingnews=new ScrollingnewsBean();
	        	scrollingnews.setNew_id(Integer.parseInt(ids[i]));
	        	ScrollingnewsBean s=sService.queryScroll(scrollingnews);
	        	if(val==1){  //审批通过
	        		scrollingnews.setAudit_status(SystemConstant.APPROVE_STATUS_SUCCESS);
	        	}else{
	        		scrollingnews.setAudit_status(SystemConstant.APPROVE_STATUS_FAILURE);
	        	}
	        	sService.updateScroll(scrollingnews);
	        	operationlog(user.getUser_id().toString(),s.getSname(),SystemConstant.AUDTI_OPERATION);
	        }
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("success", "success");
			return map;
	}
}
    