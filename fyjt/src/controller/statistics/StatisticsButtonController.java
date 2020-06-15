package controller.statistics;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import service.statistics.StatisticsButtonService;
import util.PageInfo;
import util.StatisticsButtonCMExcel;
import util.StatisticsButtonExcelFileGenerator;
import beans.errorLog.ErrorLogBean;
import beans.statistics.StatisticsButtonBean;
import beans.statistics.StatisticsButtonPartView;
import beans.statistics.StatisticsButtonView;
import beans.user.UserBean;
@Controller
@Transactional
public class StatisticsButtonController {
	@Autowired
	private StatisticsButtonService statisticsButtonService;
	@Autowired
	private OperationlogService operationlogService;
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
	@RequestMapping("statisticsButton/queryPage.do")
    @ResponseBody
	public Map<String,Object> queryPage(
			String sceneNameofButton,
			String buttonName,
			String buttonType, 
			String sceneNameofJumpbutton,
			String starttime, 
			String endtime,
			String page,  //当前页
			String rows,//一页显示条数
			HttpServletRequest request) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(sceneNameofButton!=null){
			sceneNameofButton=sceneNameofButton.replaceAll(" ", "");
		}
		if(sceneNameofJumpbutton!=null){
			sceneNameofJumpbutton=sceneNameofJumpbutton.replaceAll(" ", "");
		}
		if(buttonType!=null){
			buttonType=buttonType.replaceAll(" ", "");
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
		
		int total=statisticsButtonService.queryTotal(sceneNameofButton,sceneNameofJumpbutton,buttonName,buttonType,startime,etime);
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		
		List<StatisticsButtonBean> views=statisticsButtonService.queryPage(sceneNameofButton,sceneNameofJumpbutton,buttonName,buttonType,startime,etime,pageInfo);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", views);
		map.put("total", total);
		return map;
	}
	
	/****
	 * 触摸下载列表
	 */
	@RequestMapping("/statisticsButton/queryStatisticsButtonPartCount.do")
    @ResponseBody
	public Map<String,Object> queryStatisticsButtonPartCount(
			String sceneNameofButton,
			//String buttonName,
			String buttonType,
			String sceneNameofJumpbutton,
			String starttime, 
			String endtime,
			HttpServletRequest request) throws Exception{
	    List<StatisticsButtonPartView> statisticsList=new ArrayList<StatisticsButtonPartView>();
		Timestamp startime=null;
		Timestamp etime=null;
		if(sceneNameofButton!=null){
			sceneNameofButton=sceneNameofButton.replaceAll(" ", "");
		}
		if(sceneNameofJumpbutton!=null){
			sceneNameofJumpbutton=sceneNameofJumpbutton.replaceAll(" ", "");
		}
		if(buttonType!=null){
			buttonType=buttonType.replaceAll(" ", "");
		}
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
		int counts=statisticsButtonService.queryStatisticsButtonPartCount(sceneNameofButton,sceneNameofJumpbutton,buttonType,startime, etime);
	    int part=counts/10000;
	    int partset=counts%10000;
	    int realpart=0;
	    if(partset>0){
	    	realpart=part+1;
	    }else{
	    	realpart=part;
	    }
	    for(int i=1;i<=realpart;i++){
	    	StatisticsButtonPartView statisView=new StatisticsButtonPartView();
	    	statisView.setStartCount((i-1)*10000);
	    	if(i!=realpart){
	    		statisView.setEndCount(i*10000-1);
	    	}else{
	    		statisView.setEndCount(counts-1);
	    	}
	    	statisView.setCurret(i+"");
	    	statisView.setSceneNameofButton(sceneNameofButton);
	    	statisView.setSceneNameofJumpbutton(sceneNameofJumpbutton);
	    	statisView.setStarttime(startime);
	    	statisView.setEndtime(etime);
	    	statisView.setButtonType(buttonType);
	    	statisticsList.add(statisView);
	    }
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", statisticsList);
		map.put("total", realpart);
		return map;
	}
	/****
	 * 导出触摸(一部分一部分导出，对于数据量大可用)
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/statisticsButton/exportData.do")
    @ResponseBody
	public Map<String,Object> exportData(
			String sceneNameofButton,
		//	String buttonName,
			String buttonType,
			String sceneNameofJumpbutton,
			String starttime, 
			String endtime,
			String startCount,
			String endCount,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(sceneNameofButton!=null){
			sceneNameofButton=sceneNameofButton.replaceAll(" ", "");
		}
		if(sceneNameofJumpbutton!=null){
			sceneNameofJumpbutton=sceneNameofJumpbutton.replaceAll(" ", "");
		}
		if(buttonType!=null){
			buttonType=buttonType.replaceAll(" ", "");
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
		List<StatisticsButtonBean> logViewList=statisticsButtonService.exportData(sceneNameofButton,sceneNameofJumpbutton,buttonType,startime, etime,Integer.parseInt(startCount),Integer.parseInt(endCount)-Integer.parseInt(startCount)+1);
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(user.getUsername(), "导出触摸信息", 11, 7);
		StatisticsButtonExcelFileGenerator excelFileGenerator=new StatisticsButtonExcelFileGenerator(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("触摸信息".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("触摸信息","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("触摸信息","utf-8")+ ".xls";
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
				elb.setClass_name(StatisticsButtonController.class.toString());
				elb.setException_type("Exception");
				elb.setException_reason("导出触摸信息错误");
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
	 * 导出触摸统计信息
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/statisticsButton/exportCMExcel.do")
    @ResponseBody
	public Map<String,Object> exportCMExcel(
			String sceneNameofButton,
		//	String buttonName,
			String buttonType,
			String sceneNameofJumpbutton,
			String starttime, 
			String endtime,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(sceneNameofButton!=null){
			sceneNameofButton=sceneNameofButton.replaceAll(" ", "");
		}
		if(sceneNameofJumpbutton!=null){
			sceneNameofJumpbutton=sceneNameofJumpbutton.replaceAll(" ", "");
		}
		if(buttonType!=null){
			buttonType=buttonType.replaceAll(" ", "");
		}
		if(starttime==null || starttime.equals("")){
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			etime=Timestamp.valueOf(endtime);
		}
	    ArrayList<String> fieldName=exportCMExcelTitle();
		List<StatisticsButtonView> logViewList=statisticsButtonService.exportCMExcel(sceneNameofButton,sceneNameofJumpbutton,buttonType, startime, etime);
		UserBean user=(UserBean) request.getSession().getAttribute("user");
		operationlogService.insertOperationlog(user.getUsername(), "导出触摸统计信息", 11, 7);
		StatisticsButtonCMExcel excelFileGenerator=new StatisticsButtonCMExcel(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("触摸统计信息".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("触摸统计信息","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("触摸统计信息","utf-8")+ ".xls";
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
				elb.setClass_name(StatisticsButtonController.class.toString());
				elb.setException_type("Exception");
				elb.setException_reason("导出触摸统计信息错误");
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
	/***
	 * 触摸查询
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/statisticsButton/queryCMtj.do")
    @ResponseBody
	public Map<String,Object> queryCMtj(
			String sceneNameofButton,
			String sceneNameofJumpbutton,
			String buttonName,
			String buttonType, 
			String starttime, 
			String endtime,
			String page,  //当前页
			String rows,//一页显示条数
			HttpServletRequest request) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(sceneNameofButton!=null){
			sceneNameofButton=sceneNameofButton.replaceAll(" ", "");
		}
		if(sceneNameofJumpbutton!=null){
			sceneNameofJumpbutton=sceneNameofJumpbutton.replaceAll(" ", "");
		}
		if(buttonType!=null){
			buttonType=buttonType.replaceAll(" ", "");
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
		
		int total=statisticsButtonService.queryCMTotal(sceneNameofButton,sceneNameofJumpbutton,buttonName,buttonType,startime,etime).size();
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		
		List<StatisticsButtonView> views=statisticsButtonService.queryCMtj(sceneNameofButton,sceneNameofJumpbutton,buttonName,buttonType,startime,etime,pageInfo);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", views);
		map.put("total", total);
		return map;
	}
	
	/****
	 * 导出(触摸)EXCEL标题
	 * @return
	 */
	public ArrayList<String> exportExcelTitle() {
		ArrayList<String> fieldName = new ArrayList<String>();
		fieldName.add("终端ID");
		fieldName.add("终端名称");
		fieldName.add("节目名称");
		fieldName.add("场景名称");
		fieldName.add("按钮名称");
		fieldName.add("按钮类型");
		fieldName.add("跳转场景名称");
		fieldName.add("触摸时间");
		return fieldName;
	}
	/****
	 * 导出(触摸统计)EXCEL标题
	 * @return
	 */
	public ArrayList<String> exportCMExcelTitle() {
		ArrayList<String> fieldName = new ArrayList<String>();
		fieldName.add("场景名称");
		fieldName.add("触摸按钮名称");
		fieldName.add("触摸按钮类型");
		fieldName.add("跳转场景");
		fieldName.add("按钮触摸次数");
		return fieldName;
	}
}
