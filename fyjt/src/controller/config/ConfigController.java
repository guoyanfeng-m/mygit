package controller.config;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import service.config.ConfigService;
import service.errorLog.ErrorLogService;
import service.license.LicenseService;
import service.operationlog.OperationlogService;
import service.statistics.StatisticsButtonService;
import service.statistics.StatisticsService;
import util.JavaExportMysql;
import util.ServerDownLoadUtil;
import util.ZipFile;
import util.ZipUtil;
import beans.config.ConfigBean;
import beans.errorLog.ErrorLogBean;

@SuppressWarnings({"unchecked","rawtypes","unused"})
@Controller
@Transactional
public class ConfigController {
	@Autowired
	private ConfigService configService;
	@Autowired
	private OperationlogService operationlogService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private StatisticsButtonService statisticsButtonService;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private ErrorLogService errorLogService;
	@RequestMapping("system/insertConfig.do")
	@ResponseBody
	 public Map<String,Object> insertMenu(
			 @RequestParam(value="config_key")String config_key,
			 @RequestParam(value="config_value")String config_value,
			 HttpServletRequest request) {
		ConfigBean configBean = new ConfigBean();
		configBean.setConfig_key(config_key);
		configBean.setConfig_value(config_value);
		configService.insertConfig(configBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "true");  
		return map;
	}
	
	/****
	 * 根据key查询key-value
	 * @return
	 */
	@RequestMapping("/system/queryConfig.do")
	@ResponseBody
	public String queryConfig(HttpServletRequest request) {
	    String config_value=configService.queryConfig("httpip");
		return config_value;
	}
	/****
	 * 根据key查询key-value
	 * @return
	 */
	@RequestMapping("/system/queryConfigWEB.do")
	@ResponseBody
	public Map<String,Object>  queryConfigWEB(
			HttpServletRequest request) {
		List<String> list = new ArrayList<String>();
		list = configService.queryConfigWEB();
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("listweb", list);    
		return map;
	}
	/****
	 * 根据key查询key-value
	 * @return
	 */
	@RequestMapping("/system/queryConfigContact.do")
	@ResponseBody
	public Map<String,Object>  queryConfigContact(
			HttpServletRequest request) {
		List<String> list = new ArrayList<String>();
		list = configService.queryConfigContact();
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("listContact", list);    
		return map;
	}
	/****
	 * 根据key查询key-value
	 * @return
	 */
	@RequestMapping("/system/queryFTP.do")
	@ResponseBody
	public Map<String,Object>  queryFtp(
			HttpServletRequest request) {
		List<String> list = new ArrayList<String>();
		list = configService.queryConfigFTP();
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("listftp", list);    
		return map;
	}
	/****
	 * 根据key查询key-value
	 * @return
	 */
	@RequestMapping("/system/queryConfigFTP.do")
	@ResponseBody
	public Integer queryFtpUrl(String key){
		String ftpurl = "";
		ftpurl = configService.queryFtpUrl(key);
		return Integer.parseInt(ftpurl);
	}
	/****
	 * 查询元素权限
	 * @return
	 */
	@RequestMapping("/system/queryElementPower.do")
	@ResponseBody
	public String queryElementPower(){
		String elementPower = "";
		elementPower = configService.queryElementPower();
		return elementPower;
	}	
	
	/****
	 * 修改元素权限
	 * @return
	 */
	@RequestMapping("/system/updateElementPower.do")
	@ResponseBody
	public Integer updateElementPower(
			@RequestParam(value="elementPower")String elementPower,
    		HttpServletRequest request){
		configService.updateElementPower(Integer.parseInt(elementPower));
		return Integer.parseInt(elementPower);
	}
	
	/**
	 * 
	 * 修改监听设置
	 * @return
	 */
	@RequestMapping("system/updateListener.do")
	@ResponseBody
	public Map<String,Object> updateListener(
			@RequestParam(value="listener")String listener){
		Map<String,Object> map = new HashMap<String,Object>();
		ConfigBean configBean = new ConfigBean();
		configBean.setConfig_key("islisten");
		configBean.setConfig_value(listener);
		configService.updateConfig(configBean);
		map.put("succ", true);
		return map;
	}
	
	@RequestMapping("/updateConfig.do")
	@ResponseBody
	public Map<String,Object>  queryConfigFTP(
			HttpServletRequest request) {
		List<String> list = new ArrayList<String>();
		list = configService.queryConfigFTP();
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("listftp", list);    
		return map;
	}
	/****
	 * 终端license导入
	 */
	/****
	 * 根据key修改key-value web ftp 配置修改
	 * @return
	 */
	@RequestMapping("/system/updateConfig.do")
	@ResponseBody
	public Map<String,Object> updateConfig(
			String value1,
			String value2,
			String value3,
			String value4,
			String value5,
			String value6,
			String value7,
			String value8,
			String value9,
			String value10, 
			String value11, 
			String value12,
			String value13,
			String value14,
			HttpServletRequest request){
		List<ConfigBean> config=new ArrayList<ConfigBean>();
		if(value1!=null){
			ConfigBean c1=new ConfigBean();
			c1.setConfig_key("webServerUrl");
			c1.setConfig_value(value1);
			config.add(c1);
		}
		if(value2!=null){
			ConfigBean c2=new ConfigBean();
			c2.setConfig_key("httpip");
			c2.setConfig_value(value2);
			config.add(c2);
		}
	    if(value3!=null){
	    	ConfigBean c3=new ConfigBean();
	    	c3.setConfig_key("outftpip");
	    	c3.setConfig_value(value3);
	    	config.add(c3);
	    }
		if(value4!=null){
			ConfigBean c4=new ConfigBean();
			c4.setConfig_key("uploadport");
			c4.setConfig_value(value4);
			config.add(c4);
		}
		if(value5!=null){
			ConfigBean c5=new ConfigBean();
			c5.setConfig_key("ftpMappingUrl");
			c5.setConfig_value(value5);
			config.add(c5);
		}
		if(value6!=null){
			ConfigBean c6=new ConfigBean();
			c6.setConfig_key("ftpip");
			c6.setConfig_value(value6);
			config.add(c6);
		}
		if(value7!=null){
			ConfigBean c7=new ConfigBean();
			c7.setConfig_key("downloadport");
			c7.setConfig_value(value7);
			config.add(c7);
		}
		if(value8!=null){
			ConfigBean c8=new ConfigBean();
			c8.setConfig_key("monitorSenderMail");
			c8.setConfig_value(value8);
			config.add(c8);
		}
		if(value9!=null){
			ConfigBean c9=new ConfigBean();
			c9.setConfig_key("monitorSenderPass");
			c9.setConfig_value(value9);
			config.add(c9);
		}
		if(value10!=null){
			ConfigBean c10=new ConfigBean();
			c10.setConfig_key("monitorRecieverMail");
			c10.setConfig_value(value10);
			config.add(c10);
		}
		if(value11!=null){
			ConfigBean c11=new ConfigBean();
			c11.setConfig_key("outhttpip");
			c11.setConfig_value(value11);
			config.add(c11);
		}
		if(value12!=null){
			ConfigBean c12=new ConfigBean();
			c12.setConfig_key("contactPhone");
			c12.setConfig_value(value12);
			config.add(c12);
		}
		if(value13!=null){
			ConfigBean c13=new ConfigBean();
			c13.setConfig_key("contactFax");
			c13.setConfig_value(value13);
			config.add(c13);
		}
		if(value14!=null){
			ConfigBean c14=new ConfigBean();
			c14.setConfig_key("contactEmail");
			c14.setConfig_value(value14);
			config.add(c14);
		}
		for(int i=0;i<config.size();i++){
			configService.updateConfig(config.get(i));
		}
		
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success"); 
		return map;
	}
	/****
	 * 自动删除日志设置(修改statisticsDelete和logDelete两个值)
	 * @return
	 */
	@RequestMapping("/system/delLog.do")
	@ResponseBody
	public Map<String,Object>  delLog(String statisticsDelete,String logDelete,
			HttpServletRequest request) {
		if(!statisticsDelete.equals("")){
			ConfigBean c=new ConfigBean();
			c.setConfig_key("statisticsDelete");
			c.setConfig_value(statisticsDelete);
			configService.updateConfig(c);
		}
		if(!logDelete.equals("")){
			ConfigBean c2=new ConfigBean();
			c2.setConfig_key("logDelete");
			c2.setConfig_value(logDelete);
			configService.updateConfig(c2);
		}
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success");    
		return map;
	}
	/****
	 *  手动删除操作日设置
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/system/delOpearLog.do")
	@ResponseBody
	public Map<String,Object>  delOpearLog(String starttime,String endtime,
			HttpServletRequest request) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		operationlogService.delOperation(new Timestamp(format.parse(starttime).getTime()),new Timestamp(format.parse(endtime).getTime()));
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success");    
		return map;
	}
	/****
	 *  手动删除统计日志设置
	 * @return
	 */
	@RequestMapping("/system/delstatisticsLog.do")
	@ResponseBody
	public Map<String,Object>  delstatisticsLog(String starttime,String endtime,
			HttpServletRequest request) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    statisticsService.delStatistics(java.sql.Timestamp.valueOf(format.format(starttime)),java.sql.Timestamp.valueOf(endtime));
	    statisticsButtonService.delStatistics(java.sql.Timestamp.valueOf(format.format(starttime)),java.sql.Timestamp.valueOf(endtime));
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success");    
		return map;
	}
	/****
	 * 终端更新上传
	 * @return
	 * @throws IOException 
	 * @throws IOException 
	 */
	@RequestMapping("/system/upload.do")
	@ResponseBody
	public String upload(@RequestParam MultipartFile[] myfiles,HttpServletRequest request,HttpServletResponse response) throws IOException {
		String version=request.getParameter("version");  //版本类型
		String versionnumber=request.getParameter("versionnumber");  //版本号
		 try {     
			    //如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解  
		        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解  
		        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件  
			 for(MultipartFile myfile : myfiles){  
	                //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
	               // String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
	                String ftpMappingUrl=configService.queryConfig("ftpMappingUrl");
	        		String realPath = ftpMappingUrl+"systemUpload";
	                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
	                FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));  
	                
	                File f = new File(realPath+"/"+myfile.getOriginalFilename());
	                if(f.exists()){
	                	try {
							new ZipUtil().unZipFile(realPath+"/"+myfile.getOriginalFilename(), realPath+"\\iisclient") ;
						} catch (Exception e) {
							if(configService.queryConfig("islisten").equals("1")){
								ErrorLogBean elb = new ErrorLogBean();
								elb.setClass_name(ConfigController.class.toString());
								elb.setException_type("Exception");
								elb.setException_reason("终端更新包解压错误");
								elb.setFunction_name("终端更新");
								elb.setModule_name("系统设置");
								elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
								errorLogService.insertErrorLog(elb);
							}
							e.printStackTrace();
						}
//						f.delete();
	                }
	                
	                
	                //修改数据库
	                if(version.equals("1")){
	                	ConfigBean configBean1=new ConfigBean();
	                	configBean1.setConfig_key("lastTerminalAppPath");
	                	configBean1.setConfig_value("systemUpload\\"+myfile.getOriginalFilename());
	                	configService.updateConfig(configBean1);
	                	
	                	ConfigBean configBean2=new ConfigBean();
	                	configBean2.setConfig_key("lastTerminalAppVer");
	                	configBean2.setConfig_value(versionnumber);
	                	configService.updateConfig(configBean2);
	                }else{
	                	ConfigBean configBean1=new ConfigBean();
	                	configBean1.setConfig_key("lastAndroidTerminalAppPath");
	                	configBean1.setConfig_value("systemUpload\\"+myfile.getOriginalFilename());
	                	configService.updateConfig(configBean1);
	                	
	                	ConfigBean configBean2=new ConfigBean();
	                	configBean2.setConfig_key("lastAndroidTerminalAppVer");
	                	configBean2.setConfig_value(versionnumber);
	                	configService.updateConfig(configBean2);
	                }
	                response.setContentType("text/html;charset=utf-8");
	                response.getWriter().write("上传成功！");
	             } 
	        }catch (IOException e) {
	        	if(configService.queryConfig("islisten").equals("1")){
		        	ErrorLogBean elb = new ErrorLogBean();
					elb.setClass_name(ConfigController.class.toString());
					elb.setException_type("IOException");
					elb.setException_reason("终端更新包上传失败");
					elb.setFunction_name("终端更新");
					elb.setModule_name("系统设置");
					elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
					errorLogService.insertErrorLog(elb);
	        	}
	        	response.setContentType("text/html;charset=utf-8");
	            response.getWriter().write("上传失败！");
	        }     
		return null;
	}
	//查询终端总数
	@RequestMapping(value="/system/getTerminalValue.do")
	@ResponseBody
	public Map<String,Object> getTerminalValue(){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			int terminalTotal=licenseService.queryLicenseCount(); //要注册的终端总数
			int terminalHad=terminalTotal-licenseService.queryByLicense("0").size(); //已经注册的终端总数
			map.put("flag", true);
			map.put("terminalTotal", terminalTotal);
			map.put("terminalHad", terminalHad);
		}catch(Exception ex){
			ex.printStackTrace();
			map.put("flag", false);
		}
		return map;
	}
	//查询监听设置
	@RequestMapping(value="/system/getListenValue.do")
	@ResponseBody
	public String getListenValue(){
		String islisten = configService.queryConfig("islisten");
		return islisten;
	}
	/****
	 * 服务器的备份
	 * @return
	 */
	@RequestMapping("/system/bakupAndDownServer.do")
	@ResponseBody
	public Map<String,Object>  bakupAndDownServer(
			HttpServletRequest request,  HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();  
		Map m = System.getenv();
		String path =  (String) m.get("IIS_HOME");
		String[] paths = path.split(",");
		String IIS_HOME = paths[paths.length-1];
		//System.out.println("IIS_HOME----->"+IIS_HOME);
		File serverFile = new File(IIS_HOME);
		if(serverFile.exists()){
			String sqlFilePath = IIS_HOME+File.separator+"ftpFile"+File.separator+"iisbak.sql";
			JavaExportMysql.backup(IIS_HOME, sqlFilePath);
			File sqlFile = new File(sqlFilePath);
			if(sqlFile.exists()){
				String serverzipPath = IIS_HOME+File.separator+"iisserver.zip";
				//System.out.println("serverzipPath---->"+serverzipPath);
				ZipFile zca = new ZipFile(serverzipPath); 
				zca.compressExe(IIS_HOME+File.separator+"ftpFile");
				File serverzipFile = new File(serverzipPath);
				if(serverzipFile.exists() && serverzipFile.length()>0){
					map.put("success", true); 
					map.put("msg", "备份完成!");
					return map;
					
//					String storeName = "iisserver.zip";  
//			        String realName = "iisserver.zip";  
//			        String contentType = "application/octet-stream";  
//			        try {
//			        	ServerDownLoadUtil.download(request, response, storeName, contentType,  
//						        realName);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}  
				}
			}else{
				map.put("success", false); 
				map.put("msg", "数据库备份失败!");
				return map;
			}
		}else{
			map.put("success", false); 
			map.put("msg", "服务器目录错误,确认后重试!");
			return map;
		}
		map.put("success",false);   
		map.put("msg", "备份失败!");
		return map;
	}
	/**
	 * 检查服务器是否已备份
	* @Title: checkServer 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@RequestMapping(value="/system/checkServer.do")
	@ResponseBody
	public Map<String,Object> checkServer(){
		Map<String,Object> map=new HashMap<String,Object>();
		Map m = System.getenv();
		String path =  (String) m.get("IIS_HOME");
		String[] paths = path.split(",");
		String IIS_HOME = paths[paths.length-1];
		//System.out.println("IIS_HOME----->"+IIS_HOME);
		File serverFile = new File(IIS_HOME+File.separator+"iisserver.zip");
		if(serverFile.exists()){
	        long time = serverFile.lastModified();//返回文件最后修改时间，是以个long型毫秒数
	        String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
	    	map.put("success", true); 
	    	map.put("time", ctime); 
			return map;
		}else{
			map.put("success", false); 
			map.put("msg", "服务器暂无备份,请先备份!");
			return map;
		}
		
	}	
	/**
	 * 下载服务器备份文件
	* @Title: downloadServer 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws Exception    设定文件 
	* @return ModelAndView    返回类型 
	* @throws
	 */
	
	@RequestMapping("/system/downloadServer.do")
	@ResponseBody	
	public ModelAndView downloadServer(HttpServletRequest request,  
            HttpServletResponse response) throws Exception {  
  
		String storeName = "iisserver.zip";  
        String realName = "iisserver.zip";  
        String contentType = "application/octet-stream";  
        ServerDownLoadUtil.download(request, response, storeName, contentType,  
		        realName);
        return null;  
    }  	
	
	/**
	 * 上传服务器备份文件
	* @Title: uploadServerFile 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param myfiles
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws IOException    设定文件 
	* @return String    返回类型 
	* @throws
	 */
@RequestMapping("/system/uploadServerFile.do")
@ResponseBody
public String uploadServerFile(@RequestParam MultipartFile[] severfiles,HttpServletRequest request,HttpServletResponse response) throws IOException {
	String content = null;
	Map<String,Object> map=new HashMap<String,Object>();
	ObjectMapper mapper = new ObjectMapper();  
		Map m = System.getenv();
		String path =  (String) m.get("IIS_HOME");
		String[] paths = path.split(",");
		String IIS_HOME = paths[paths.length-1];
	try {     
		//如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解  
        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解  
        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件  
		String realPath = configService.queryConfig("ftpMappingUrl");
		// File file = new File(realPath);
		// deleteAll(file);
		for(MultipartFile myfile : severfiles){  
            //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
            // String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
            // String ftpMappingUrl=configService.queryConfig("ftpMappingUrl");
    		//这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
                FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));  
                
                File f = new File(realPath+"/"+myfile.getOriginalFilename());
                if(f.exists()){
                	try {
                		ZipFile.unzip(realPath+"/"+myfile.getOriginalFilename(), realPath);
						//new ZipUtil().unZipFile(realPath+"/"+myfile.getOriginalFilename(), realPath) ;
					} catch (Exception e) {
						if(configService.queryConfig("islisten").equals("1")){
							ErrorLogBean elb = new ErrorLogBean();
							elb.setClass_name(ConfigController.class.toString());
							elb.setException_type("Exception");
							elb.setException_reason("上传服务器备份文件包解压失败");
							elb.setFunction_name("上传服务器备份文件");
							elb.setModule_name("系统设置");
							errorLogService.insertErrorLog(elb);
						}
						e.printStackTrace();
					}
					f.delete();
                }
                String sqlfilePath = realPath+File.separator+"iisbak.sql";
                File sqlFile = new File(sqlfilePath);
                boolean b = true;
                b = JavaExportMysql.load(IIS_HOME,sqlfilePath);
                if(!b){
                	
//                	map.put("success", false); 
//                	map.put("msg", "数据库还原失败!确认后重试!");
                    response.setContentType("text/html;charset=utf-8");
	                response.getWriter().write("{\"success\": false,\"msg\": \"数据库还原失败!确认后重试!\"}");
	                return null;
	            }else{
	            	sqlFile.delete();
//	            	map.put("success", true); 
//	            	map.put("msg", "服务器还原成功!");
	                response.setContentType("text/html;charset=utf-8");
	                response.getWriter().write("{\"success\": true,\"msg\": \"服务器还原成功!\"}");
	                return null;
	            }
             } 
        }catch (IOException e) {   
//        	map.put("success", false); 
//			map.put("msg", "服务器还原失败!确认后重试!");
        	if(configService.queryConfig("islisten").equals("1")){
	        	ErrorLogBean elb = new ErrorLogBean();
				elb.setClass_name(ConfigController.class.toString());
				elb.setException_type("Exception");
				elb.setException_reason("服务器还原失败");
				elb.setFunction_name("服务器还原");
				elb.setModule_name("系统设置");
				elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
				errorLogService.insertErrorLog(elb);
        	}
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("{\"success\": false,\"msg\": \"服务器还原失败!确认后重试!\"}");
            return null;
        }     
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("{\"success\": false,\"msg\": \"服务器还原失败!确认后重试!\"}");
        return null;
}
//递归删除指定路径下的所有文件
//public static void deleteAll(File file){
//	
//	if(file.isFile() || file.list().length == 0){
//		  file.delete();
//		} else{
//		  File[] files = file.listFiles();
//		  for(File f : files) {
//		   deleteAll(f);//递归删除每一个文件
//		   f.delete();//删除该文件夹
//		  }
//		  }
//}
}