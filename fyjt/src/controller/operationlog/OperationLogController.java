package controller.operationlog;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import service.errorLog.ErrorLogService;
import service.operationlog.OperationlogService;
import util.ExcelFileGenerator;
import util.PageInfo;
import beans.errorLog.ErrorLogBean;
import beans.operationlog.OperationlogBean;
import beans.operationlog.OperationlogView;
import beans.proStatistics.ProStatisticsBean;
import beans.user.UserBean;
@Controller
@Transactional
public class OperationLogController {
	@Autowired
	private OperationlogService operationlogService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ErrorLogService errorLogService;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	Timestamp datetime = new Timestamp(new Date().getTime());
	/***
	 * 查询所有
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("operationlog/queryAll.do")
    @ResponseBody
	public Map<String,Object> queryAll(
			String userName,
			String operationName,
			String starttime, 
			String endtime,
			Integer operationModel,
			Integer operationType,
			String page,//当前页
			String rows,//一页显示条数
			HttpServletRequest request) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(userName==null || userName.equals("")){
			userName=null;
		}else{
			//String str=new String(userName.getBytes("ISO-8859-1"),"utf-8");
			userName=userName.replaceAll(" ", "");
		}
		if(operationName==null || operationName.equals("")){
			operationName=null;
		}else{
			operationName=operationName.replaceAll(" ", "");
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
		if(operationName==null || operationName.equals("")){
			operationName=null;
		}else{
		}
		int total=operationlogService.queryTotal(userName, operationName, startime, etime, operationModel, operationType);
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		
		List<OperationlogView> views=operationlogService.queryScroll(userName, operationName, startime, etime, operationModel, operationType, pageInfo);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", views);
		map.put("total", total);
		return map;
	}
	/****
	 * 导出
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/operationlog/exportExcel.do")
    @ResponseBody
	public Map<String,Object> exportExcel(
			String userName,
			String operationName,
			String starttime, 
			String endtime,
			Integer operationModel,
			Integer operationType,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(userName==null || userName.equals("")){
			userName=null;
		}/*else{
			String str=new String(userName.getBytes("ISO-8859-1"),"utf-8");
			userName=str;
		}*/
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
		if(operationName==null || operationName.equals("")){
			operationName=null;
		}else{
			
		}
	    ArrayList<String> fieldName=exportExcelTitle();
		List<OperationlogView> logViewList=operationlogService.exportExcel(userName, operationName, startime, etime, operationModel, operationType);
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(user.getUser_id().toString(), "导出操作日志", 11, 7);
		ExcelFileGenerator excelFileGenerator=new ExcelFileGenerator(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("操作日志".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("操作日志","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("操作日志","utf-8")+ ".xls";
				}
			}
			 // 表示以附件的形式把文件发送到客户端
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
				elb.setClass_name(OperationLogController.class.toString());
				elb.setException_type("Exception");
				elb.setException_reason("Excel输出流发送错误");
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
	 * 导出EXCEL标题
	 * @return
	 */
	public ArrayList<String> exportExcelTitle() {
		ArrayList<String> fieldName = new ArrayList<String>();
		fieldName.add("操作人姓名");
		fieldName.add("操作人");
		fieldName.add("操作元素");
		fieldName.add("操作类型");
		fieldName.add("操作模块");
		fieldName.add("操作时间");
		return fieldName;
	}
	/****
	 * 手动删除操作日志
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/operationlog/delOperation.do")
	@ResponseBody
	public Map<String,Object> delOperation(String starttime,String endtime,HttpServletRequest request) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		operationlogService.delOperation(new Timestamp(format.parse(starttime).getTime()), new Timestamp(format.parse(endtime).getTime()));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", "true");
		return map;
	}
	/****
	 * 添加
	 * @return
	 */
	@RequestMapping("/operationlog/insertOperationlog.do")
    @ResponseBody
	public Map<String,Object> insertOperationlog(){
		Map<String,Object> map=new HashMap<String,Object>();
		OperationlogBean op=new OperationlogBean();
		op.setOperationName("播放单1");
		op.setUserName("admin");
		op.setOperationType(2);
		op.setOperationModel(2);
		/*boolean flag=operationlogService.insertOperationlog(op);
		if(flag==true){
		  map.put("success", "success");
		}
  		map.put("failure", "failure");*/
		return map;
	}
	
	@RequestMapping("/errorlog/queryErrorlog.do")
	@ResponseBody
	public Map<String,Object> queryErrorlog(
			String errormodule,
			String errordetail,
			String starttime,
			String endtime,
			String page,//当前页
			String rows,//一页显示条数
			HttpServletRequest request){
		Timestamp startime=null;
		Timestamp etime=null;
		if(errordetail!=null){
			errordetail=errordetail.replaceAll(" ", "");
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
		int total = errorLogService.queryTotal(errormodule, errordetail, startime, etime);
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		Map<String,Object> map = new HashMap<String,Object>();
		List<ProStatisticsBean> errorlist = errorLogService.queryErrorlog(errormodule, errordetail, startime, etime, pageInfo);
		map.put("rows", errorlist);
		map.put("total", total);
		return map;
	}
	
	@RequestMapping(value="/errorlog/insertTerminalErrorlog.do")
	@ResponseBody
	public Map<String,Object> insertTerminalErrorlog(HttpServletRequest request,HttpServletResponse response){
		String modulename = request.getParameter("modulename");  
		String functionname = request.getParameter("functionname");  
		String classname = request.getParameter("classname");  
		String exceptionreason = request.getParameter("exceptionreason");  
		String exceptiontype = request.getParameter("exceptiontype");  
		String happentime = request.getParameter("happentime");  
		System.out.println("终端故障信息：modulename-----"+modulename+"functionname-----"+functionname+",classname-----"+classname
				+",exceptionreason-----"+exceptionreason+",exceptiontype-----"+exceptiontype+",happentime-----"+happentime);
		Map<String,Object> map = new HashMap<String,Object>();
		if(configService.queryConfig("islisten").equals("1")){
			ErrorLogBean elb = new ErrorLogBean();
			elb.setClass_name(classname);
			elb.setException_type(exceptiontype);
			elb.setException_reason(exceptionreason);
			elb.setFunction_name(functionname);
			elb.setModule_name(modulename);
			Date date = new Date();   
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	        try {
				date = sdf.parse(happentime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			elb.setHappen_time(new Timestamp(date.getTime()));
			errorLogService.insertErrorLog(elb);
			map.put("return", "insert success");
		}else{ 
			map.put("return", "closed");
		}
		return map;
	}
	
	
}
