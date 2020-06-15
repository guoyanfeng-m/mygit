package controller.statistics;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import service.errorLog.ErrorLogService;
import service.operationlog.OperationlogService;
import service.statistics.StatisticsButtonService;
import service.statistics.StatisticsService;
import util.PageInfo;
import util.StatisticsExcelFileGenerator;
import util.StatisticscountExcelFileGenerator;
import beans.errorLog.ErrorLogBean;
import beans.statistics.StatisticsBean;
import beans.statistics.StatisticsPartView;
import beans.statistics.StatisticsViewBean;
import beans.user.UserBean;
@Controller
@Transactional
public class StatisticsController {
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private OperationlogService operationlogService;
	Timestamp datetime = new Timestamp(new Date().getTime());
	@Autowired
	private StatisticsButtonService statisticsButtonService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ErrorLogService errorLogService;
	/***
	 * 查询所有
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("statistics/queryPage.do")
    @ResponseBody
	public Map<String,Object> queryPage(
			String eleName,
			String terminalName,
			String starttime, 
			String endtime,
			String page,  //当前页
			String rows,//一页显示条数
			HttpServletRequest request) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(eleName!=null){
			eleName=eleName.replaceAll(" ", "");
		}
		if(terminalName!=null){
			terminalName=terminalName.replaceAll(" ", "");
		}
		int newpage=1;
		int newrows=0;
		if(page!=null && page!="" &&!page.equals("0")){
			newpage=Integer.parseInt(page);
		}
		if(rows!=null && rows!=""){
			newrows=Integer.parseInt(rows);
		}
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
		
		int total=statisticsService.queryTotal(eleName, terminalName, startime, etime);
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		
		List<StatisticsBean> views=statisticsService.queryPage(eleName, terminalName, startime, etime, pageInfo);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", views);
		map.put("total", total);
		return map;
	}
	
	/***
	 * 统计查询
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/statistics/querytongji.do")
    @ResponseBody
	public Map<String,Object> querytongji(
			String eleName,
			String starttime, 
			String endtime,
			String page,//当前页
			String rows,//一页显示条数
			HttpServletRequest request) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(eleName!=null){
			eleName=eleName.replaceAll(" ", "");
		}
		/*if(eleName==null || eleName.equals("")){
			
		}else{
			String str=new String(eleName.getBytes("ISO-8859-1"),"utf-8");
			eleName=str;
		}*/
		int newpage=1;
		int newrows=0;
		if(page!=null && page!="" &&!page.equals("0")){
			newpage=Integer.parseInt(page);
		}
		if(rows!=null && rows!=""){
			newrows=Integer.parseInt(rows);
		}
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
		
		int total=statisticsService.querytongjiTotal(eleName, startime, etime).size();
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
	    List<StatisticsViewBean> views=statisticsService.querytongji(eleName, startime, etime, pageInfo);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", views);
		map.put("total", total);
		return map;
	}
	/****
	 * 删除
	 * @param request
	 * @return
	 */
	@RequestMapping("/statistics/statisticsDel.do")
    @ResponseBody
	public Map<String,Object> statisticsDel(HttpServletRequest request){
		String [] tids=request.getParameterValues("tids[]");
		Integer [] ids=new Integer[tids.length];
		for(int n=0;n<tids.length;n++){
			ids[n]=Integer.parseInt(tids[n]);
		}
		int i=statisticsService.statisticsDel(ids);
		Map<String,Object> map=new HashMap<String,Object>();
		if(i==1){
			map.put("success", "true");
		}else{
			map.put("success", "false");
		}
		return map;
	}
	/****
	 * 导出素材统计信息
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/statistics/exportStatisticsTJData.do")
    @ResponseBody
	public Map<String,Object> exportExcel(
			String eleName,
			String starttime, 
			String endtime,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(eleName==null || eleName.equals("")){
			eleName=null;
		}
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
	    ArrayList<String> fieldName=exportExcelTitle();
		List<StatisticsViewBean> logViewList=statisticsService.exportStatisticsTJData(eleName, startime, etime);
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(user.getUsername(), "素材统计信息", 11, 7);
		StatisticscountExcelFileGenerator excelFileGenerator=new StatisticscountExcelFileGenerator(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("素材统计信息".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("素材统计信息","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("素材统计信息","utf-8")+ ".xls";
				}
			}
			//  表示以附件的形式把文件发送到客户端
			response.setHeader("Content-disposition", "attachment; fileName="+myfilename);
			response.setContentType("application/vnd.ms-excel"); //定义输出类型
			excelFileGenerator.expordExcel(os);   // 通过response的输出流把工作薄的流发送浏览器形成文件
            os.flush();
            if(os!=null){
            	os.close();
            }
		}catch(Exception e){
			if(configService.queryConfig("islisten").equals("1")){
				ErrorLogBean elb = new ErrorLogBean();
				elb.setClass_name(StatisticsController.class.toString());
				elb.setException_type("Exception");
				elb.setException_reason("导出素材统计信息错误");
				elb.setFunction_name("导出日志");
				elb.setModule_name("日志管理");
				elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
				errorLogService.insertErrorLog(elb);
			}
			e.printStackTrace();
		}
		Map<String,Object> map=new HashMap<String,Object>();
		return map;
	}
	/****
	 * 导出素材信息(一次性全部导出，对于数据量大的不可用)
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	/*@RequestMapping("/statistics/exportEleData.do")
    @ResponseBody
	public Map<String,Object> exportEleData(
			String eleName,
			String terminalName,
			String starttime, 
			String endtime,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(eleName==null || eleName.equals("")){
			eleName=null;
		}
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
	    ArrayList<String> fieldName=exportStatisticsTJExcelTitle();
		List<StatisticsBean> logViewList=statisticsService.exportEleData(eleName,terminalName, startime, etime);
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(user.getUsername(), "导出素材信息", 11, 7);
		StatisticsExcelFileGenerator excelFileGenerator=new StatisticsExcelFileGenerator(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("素材信息".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("素材信息","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("素材信息","utf-8")+ ".xls";
				}
			}
			 // 表示以附件的形式把文件发送到客户端
			response.setHeader("Content-disposition", "attachment; fileName="+myfilename);
			response.setContentType("application/vnd.ms-excel");// 定义输出类型
			excelFileGenerator.expordExcel(os);    //通过response的输出流把工作薄的流发送浏览器形成文件
            os.flush();
            if(os!=null){
            	os.close();
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		Map<String,Object> map=new HashMap<String,Object>();
		return map;
	}*/
	/****
	 * 导出播放日志(一部分一部分导出，对于数据量大可用)
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/statistics/exportEleExcel.do")
    @ResponseBody
	public Map<String,Object> exportEleExcel(
			String eleName,
			String terminalName,
			String starttime, 
			String endtime,
			String startCount,
			String endCount,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(eleName==null || eleName.equals("")){
			eleName=null;
		}
		if(starttime==null || starttime.equals("")){
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			etime=Timestamp.valueOf(endtime);
		}
	    ArrayList<String> fieldName=exportStatisticsTJExcelTitle();
		List<StatisticsBean> logViewList=statisticsService.exportEleData(eleName,terminalName, startime, etime,Integer.parseInt(startCount),Integer.parseInt(endCount)-Integer.parseInt(startCount)+1);
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(user.getUsername(), "播放日志统计信息", 11, 7);
		StatisticsExcelFileGenerator excelFileGenerator=new StatisticsExcelFileGenerator(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("播放日志统计信息".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("播放日志统计信息","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("播放日志统计信息","utf-8")+ ".xls";
				}
			}
			 // 表示以附件的形式把文件发送到客户端
			response.setHeader("Content-disposition", "attachment; fileName="+myfilename);
			response.setContentType("application/vnd.ms-excel");// 定义输出类型
			excelFileGenerator.expordExcel(os);    //通过response的输出流把工作薄的流发送浏览器形成文件
            os.flush();
            if(os!=null){
            	os.close();
            }
		}catch(Exception e){
			if(configService.queryConfig("islisten").equals("1")){
				ErrorLogBean elb = new ErrorLogBean();
				elb.setClass_name(StatisticsController.class.toString());
				elb.setException_type("Exception");
				elb.setException_reason("导出播放日志统计信息错误");
				elb.setFunction_name("导出日志");
				elb.setModule_name("日志管理");
				elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
				errorLogService.insertErrorLog(elb);
			}
			e.printStackTrace();
		}
		Map<String,Object> map=new HashMap<String,Object>();
		return map;
	}
	/****
	 * 导出(统计信息)EXCEL标题
	 * @return
	 */
	public ArrayList<String> exportExcelTitle() {
		ArrayList<String> fieldName = new ArrayList<String>();
		fieldName.add("素材名称");
		fieldName.add("素材类型");
		fieldName.add("素材播放时长");
		fieldName.add("素材播放次数");
		return fieldName;
	}
	/****
	 * 导出(素材信息)EXCEL标题
	 * @return
	 */
	public ArrayList<String> exportStatisticsTJExcelTitle() {
		ArrayList<String> fieldName = new ArrayList<String>();
		fieldName.add("所属终端名称");
		fieldName.add("节目名称");
		fieldName.add("区域名称");
		fieldName.add("场景名称");
		fieldName.add("素材名称");
		fieldName.add("素材类型");
		fieldName.add("素材播放时长");
		fieldName.add("播放开始时间");
		fieldName.add("播放结束时间");
		return fieldName;
	}
	
	/****
	 * 分部查询素材信息
	 */
	@RequestMapping("/statistics/queryStatisticsPartCount.do")
    @ResponseBody
	public Map<String,Object> queryStatisticsPartCount(
			String eleName,
			String terminalName,
			String starttime, 
			String endtime,
			HttpServletRequest request) throws UnsupportedEncodingException{
	    List<StatisticsPartView> statisticsList=new ArrayList<StatisticsPartView>();
		Timestamp startime=null;
		Timestamp etime=null;
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
		int counts=statisticsService.queryStatisticsPartCount(eleName, terminalName, startime, etime);
	    int part=counts/10000;
	    int partset=counts%10000;
	    int realpart=0;
	    if(partset>0){
	    	realpart=part+1;
	    }else{
	    	realpart=part;
	    }
	    for(int i=1;i<=realpart;i++){
	    	StatisticsPartView statisView=new StatisticsPartView();
	    	statisView.setStartCount((i-1)*10000);
	    	if(i!=realpart){
	    		statisView.setEndCount(i*10000-1);
	    	}else{
	    		statisView.setEndCount(counts-1);
	    	}
	    	statisView.setCurret(i+"");
	    	statisView.setElemName(eleName);
	    	statisView.setElemstartTime(startime);
	    	statisView.setElemendTime(etime);
	    	statisView.setTerminalName(terminalName);
	    	statisticsList.add(statisView);
	    }
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", statisticsList);
		map.put("total", realpart);
		return map;
	}
	/****
	 * 手动删除统计日志
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/statistics/delStatistics.do")
	@ResponseBody
	public JSONObject  delStatistics(String starttime,String endtime,HttpServletRequest request){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject json=new JSONObject();
		try {
			statisticsService.delStatistics(new Timestamp(format.parse(starttime).getTime()), new Timestamp(format.parse(endtime).getTime()));
		    statisticsButtonService.delStatistics(new Timestamp(format.parse(starttime).getTime()), new Timestamp(format.parse(endtime).getTime()));
			json.put("success", true);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.put("success", false);
			return json;
		}
	
	}
}
