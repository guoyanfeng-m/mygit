package controller.proStatistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
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

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import service.proStatistics.ProStatisticsService;
import service.terminal.TerminalService;
import util.PageInfo;
import util.ProStatisticsExcelFileGenerator;
import beans.proStatistics.ProStatisticsBean;
import beans.terminal.TerminalBean;
@Controller
@Transactional
public class ProStatisticsController {
	@Autowired
	private ProStatisticsService proService;
	@Autowired
	private TerminalService terService;
	/**
	 * 加载播放日志
	 */
	@RequestMapping("programlog/queryProgramlog.do")
	@ResponseBody
	public Map<String,Object> queryProgramLog(
			String terminalName,
			String programName,
			String starttime, 
			String endtime,
			String page,  //当前页
			String rows,//一页显示条数
			HttpServletRequest request){
		Timestamp startime=null;
		Timestamp etime=null;
		if(terminalName!=null){
			terminalName=terminalName.replaceAll(" ", "");
		}
		if(programName!=null){
			programName=programName.replaceAll(" ", "");
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
		int total=proService.queryTotal(terminalName, programName, startime, etime);
		PageInfo pageInfo=new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<ProStatisticsBean> prolist = proService.queryProgramlog(terminalName, programName, startime, etime, pageInfo);
		map.put("rows", prolist);
		map.put("total", total);
		return map;
	}
	/**
	 * 上传离线播放统计日志
	 * 
	 */
	@RequestMapping("/programlog/insertProgramlog.do")
	@ResponseBody
	public void insertProgramlog(
			String id,
			HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultiValueMap<String, MultipartFile> mm = multiRequest.getMultiFileMap();
		Object[] a = mm.get("profileurl").toArray();
		//上传文件到ftp临时路径(fipFile/pro_Statistics)
		for(int i=0; i<a.length; i++){
			
			MultipartFile file = (MultipartFile) a[i];
			System.out.println("file文件大小:"+file.getSize());
			if(!file.isEmpty()){
				//文件原名
				String originalFileName = file.getOriginalFilename();
				System.out.println("file文件名："+ originalFileName);
				//临时路径
				String path = request.getSession().getServletContext().getRealPath("");
				String ftpPath = path.substring(0,path.indexOf("tomcat"))
					+ "ftpFile" + File.separator + "proStatistics" +File.separator + originalFileName;
				File localFile = new File(ftpPath);
				if(!localFile.exists()){
					localFile.mkdirs();
				}
				try {
					file.transferTo(localFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String p = request.getSession().getServletContext().getRealPath("");
		//日志临时存储路径（ftpFile/proStatistics）
		String filesPath = p.substring(0,p.indexOf("tomcat"))
						+"ftpFile" + File.separator + "proStatistics";
		//String s = System.getProperty("user.dir");
		//System.out.println("当前目录"+s);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String str = df.format(new Date());// new Date()为获取当前系统时间
		//日志上传成功存储路径
		String newPath = p.substring(0,p.indexOf("tomcat"))
					+"ftpFile" + File.separator + "proStatistics_old" + File.separator + "succ_proFile" +File.separator + str;
		//日志mac未添加存储路径
		String errmacPath = p.substring(0,p.indexOf("tomcat"))
					+"ftpFile" + File.separator + "proStatistics_old" + File.separator + "err_macFile" +File.separator + str;
		//日志参数缺失存储路径
		String errelementPath = p.substring(0, p.indexOf("tomcat"))
					+"ftpFile" + File.separator + "proStatistics_old" + File.separator + "err_elementFile" + File.separator +str;
		
		//遍历文件夹
		File[] files = new File(filesPath).listFiles();
		boolean logelement = true;
		boolean flag = true;
		List<String> listname = new ArrayList<String>();
		for(File f:files){
			//查看日志文件是否已经入库
			String fileName = f.getName().substring(0,f.getName().indexOf("."));
			int fCount = proService.selectFileNameCount(fileName);
			if(fCount == 0){
			//终端mac
			String mac = f.getName().split("@")[1].substring(0, f.getName()
					.split("@")[1].lastIndexOf("."));
			TerminalBean terminalBean = new TerminalBean();
			List<String> terminalName = null;
			//判断mac是否已经添加到服务器
			if(mac!=null&&!mac.equals("")){
				terminalBean.setMac(mac);
				terminalName = terService.queryTerminalName(terminalBean);
				if(terminalName ==null||terminalName.size() == 0){
					System.out.println("----终端尚未添加到服务器");
					//移动文件到ftp/proStatistics_old/errfile
					try {
						FileUtils.moveToDirectory(f, new File(errmacPath), true);
					} catch (IOException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
			
			InputStreamReader input = null;
			BufferedReader bufferedReader = null;
			try {
				input = new InputStreamReader(new FileInputStream(f),"unicode");
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bufferedReader = new BufferedReader(input);
			String sql = null;
			try {
				//遍历文件
				while (!(sql = bufferedReader.readLine()).equals("")) {
					//System.out.println("播放日志"+sql);
					//数据入库
					System.out.println("日志解析："+sql.trim().replaceAll(";", ""));
					String[] strArr = sql.trim().replaceAll(";", "").split(",");
					//判断数据类型
					if(strArr.length == 6){
						ProStatisticsBean proBean = new ProStatisticsBean();
							proBean.setTerminalMac(mac);
							proBean.setProgramName(strArr[0]);
							proBean.setProgramType(strArr[1]);
							if(strArr[2]!=null&&!strArr[2].equals("")){
								proBean.setProgramLevel(Integer.parseInt(strArr[2]));
							}
							if(strArr[3]!=null&&!strArr[3].equals("")){
								proBean.setTotalTime(Integer.parseInt(strArr[3]));
							}
							if(isValidDate(strArr[4])){
								proBean.setStartTime(strArr[4]);
							}
							if(isValidDate(strArr[5])){
								proBean.setEndTime(strArr[5]);
							}
							proBean.setFileName(fileName);
							proBean.setFileType(0);  //0--离线日志
						proService.insertProStatistics(proBean);
					
					}else{
						System.out.println("----日志参数部分缺失");
						logelement = false;
					} 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bufferedReader.close();
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				if(logelement == true){
					//上传成功，移动日志
					FileUtils.moveToDirectory(f, new File(newPath), true);
				}else{
					//日志文件参数缺失，移动日志
					FileUtils.moveToDirectory(f, new File(errelementPath), true);
					logelement = true;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			//flag = true;
			}else{
				flag = false;
				f.delete(); 
				listname.add(fileName);
			}
		}
		map.put("msg",flag);
		map.put("listname", listname);
		//return map;
		  JSONObject jsonObject = new JSONObject();
		  jsonObject.put("msg", flag);
		  jsonObject.put("listname", listname);
		   response.setContentType("text/html;charset=UTF-8");
	       try {
			response.getWriter().print(jsonObject.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}
	/**
	 * 判断时间数据格式
	 * @param strdate
	 * @return
	 */
	public static boolean isValidDate(String strdate){
		boolean convertSuccess=true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(strdate);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess=false;
			System.out.println("----日志时间参数格式错误");
		}
		return convertSuccess;
	}
	/****
	 * 导出播放日志统计信息
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/programlog/exportProStatisticsTJData.do")
    @ResponseBody
	public Map<String,Object> exportExcel(
			String terminalName,
			String programName,
			String starttime, 
			String endtime,
			HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Timestamp startime=null;
		Timestamp etime=null;
		if(terminalName!=null){
			terminalName=terminalName.replaceAll(" ", "");
		}
		if(programName!=null){
			programName=programName.replaceAll(" ", "");
		}
		if(starttime==null || starttime.equals("")){
			
		}else{
			 startime=Timestamp.valueOf(starttime);
		}
		if(endtime==null || endtime.equals("")){
			
		}else{
			 etime=Timestamp.valueOf(endtime);
		}
	    //1.导出(统计信息)EXCEL标题
		ArrayList<String> fieldName=exportExcelTitle();
		 //2.查询数据库
		List<ProStatisticsBean> logViewList = proService.exportProStatisticsTJData(terminalName, programName, startime, etime);
	    //写日志
		//UserBean user=(UserBean) request.getSession().getAttribute("user");
		//operationlogService.insertOperationlog(user.getUsername(), "素材统计信息", 11, 7);
		//3.导出Excel 生成器
		ProStatisticsExcelFileGenerator excelFileGenerator=new ProStatisticsExcelFileGenerator(fieldName,logViewList);
		try{
			OutputStream os=response.getOutputStream();
			response.reset();
			String Agent=request.getHeader("User-Agent");
			String myfilename="";
			if(null!=Agent){
				Agent=Agent.toLowerCase();
				if(Agent.indexOf("firefox")!=-1){
					myfilename=new String("节目播放统计信息".getBytes("utf-8"),"iso-8859-1")+ ".xls";
				}else if(Agent.indexOf("msie")!=-1){
					myfilename=URLEncoder.encode("节目播放统计信息","utf-8")+ ".xls";
				}else{
					myfilename=URLEncoder.encode("节目播放统计信息","utf-8")+ ".xls";
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
		fieldName.add("所属终端名称");
		fieldName.add("节目名称");
		fieldName.add("节目类型");
		fieldName.add("优先级");
		fieldName.add("播放总时间");
		fieldName.add("播放开始时间");
		fieldName.add("播放结束时间");
		return fieldName;
	}
}
