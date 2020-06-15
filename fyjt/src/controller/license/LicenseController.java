package controller.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import service.config.ConfigService;
import service.errorLog.ErrorLogService;
import service.license.LicenseService;
import util.ParseXml;
import beans.config.ConfigBean;
import beans.errorLog.ErrorLogBean;
import beans.license.LicenseBean;

@SuppressWarnings({"unused"})
@Controller
@Transactional
public class LicenseController {
	@Autowired
	private ConfigService configService;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private ErrorLogService errorLogService;
	
	@RequestMapping(value="license/checkData.do")
	@ResponseBody
	public Map<String,Object> checkData(){
		Map<String,Object> map=new HashMap<String,Object>();
		List<LicenseBean> list=licenseService.queryByLicense("0"); //查询license为null的
		List<LicenseBean> listTotal=licenseService.queryByLicense("1"); //查询所有license
		
		if(listTotal.size()>0 && listTotal.size()-list.size()>0){
			map.put("flag", 0);  //存在已经注册的license信息
		}else{
			map.put("flag", 1);  //没有要导出的license信息
		}
		return map;
	}
	//导出license
	@RequestMapping(value="license/exportLicense.do")
	@ResponseBody
	public String exportLicense(HttpServletRequest request,HttpServletResponse response){
		List<LicenseBean> list=licenseService.queryByLicense("1"); //查询所有
			SimpleDateFormat nowtime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//设置日期格式
			String now = nowtime.format(new Date()).toString();  
			//String fileName=now+".xml";
			String fileName="license.xml";
			String realPath=request.getSession().getServletContext().getRealPath("/exportlicense");
		    try{
		    	ParseXml p=new ParseXml();
		    	boolean flag=p.createXml(list, realPath,fileName); //生成xml文件
		        if(flag){
		        	//设置响应头，控制浏览器下载该文件
	            //    response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		              response.setHeader("content-disposition", "attachment;filename=" + fileName);
	            	response.setContentType("application/vnd.ms-xml"); //这 个方法设置发送到客户端的响应的内容类型，此时响应还没有提交，定义输出类型  response.setContentType(MIME)的作用是使客户端浏览器，区分不同种类的数据，并根据不同的MIME调用浏览器内不同的程序嵌入模块来处理相应的数据。
	    	        //读取要下载的文件，保存到文件输入流
	    	        FileInputStream in = new FileInputStream(realPath+"\\"+fileName);
	    	       //创建输出流
	    	        OutputStream out = response.getOutputStream();
	    	         //创建缓冲区
	    	         byte buffer[] = new byte[1024];
	    	         int len = 0;
	    	         //循环将输入流中的内容读取到缓冲区当中
	    	        while((len=in.read(buffer))>0){
	    	             //输出缓冲区的内容到浏览器，实现文件下载
	    	             out.write(buffer, 0, len);
	    	         }
	    	         //关闭文件输入流
	    	         in.close();
	    	         //关闭输出流
	    	         out.close();
		        }
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		return null;
	}
	/****
	 * license上传
	 * @return
	 * @throws IOException 
	 * @throws IOException 
	 */
	@RequestMapping("/license/upload.do")
	@ResponseBody
	public String upload(@RequestParam MultipartFile[] fileData,HttpServletRequest request,HttpServletResponse response) throws IOException {
		SimpleDateFormat nowtime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//设置日期格式
		String now = nowtime.format(new Date()).toString();  
		String filename=now+".xml";
		 try {     
		        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件  
			 for(MultipartFile myfile : fileData){  
	                //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
	               // String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
				    String path=request.getSession().getServletContext().getRealPath("/importlicense");
	      
	                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
	                FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(path,filename));  
	        
	                //解析上传xml文件
	                ParseXml p=new ParseXml();
	                List<LicenseBean> list=p.getXml(path+"/"+filename);
	                if(list.size()>0){
	                	//修改数据库
	                	List<String> list2=licenseService.queryMac();
	                	if(list2.size()>0){
	                		for(int i=0;i<list.size();i++){
	                			if(list2.contains(list.get(i).getMac())){ //假如数据库中存在对应的mac
	                				LicenseBean license=new LicenseBean();
	                				license.setMac(list.get(i).getMac());
	                				license.setLicense(list.get(i).getLicense());
	                				licenseService.updateLicense(license); //修改
	                			}else{
	                				LicenseBean license=new LicenseBean();
	                				license.setMac(list.get(i).getMac());
	                				license.setHardinfo(list.get(i).getHardinfo());
	                				license.setLicense(list.get(i).getLicense());
	                				licenseService.insertLicense(license); //添加
	                			}
	                		}
	                	}else{
	                		for(int i=0;i<list.size();i++){
                				LicenseBean license=new LicenseBean();
                				license.setMac(list.get(i).getMac());
                				license.setHardinfo(list.get(i).getHardinfo());
                				license.setLicense(list.get(i).getLicense());
                				licenseService.insertLicense(license); //添加
	                		}
	                	}
	                }
	                response.setContentType("text/html;charset=utf-8");
	                response.getWriter().write("上传成功！");
	             } 
	        }catch (IOException e) {
	        	if(configService.queryConfig("islisten").equals("1")){
		        	ErrorLogBean elb = new ErrorLogBean();
					elb.setClass_name(LicenseController.class.toString());
					elb.setException_type("Exception");
					elb.setException_reason("license上传失败");
					elb.setFunction_name("license上传");
					elb.setModule_name("系统设置");
					elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
					errorLogService.insertErrorLog(elb);
	        	}
	        	response.setContentType("text/html;charset=utf-8");
	            response.getWriter().write("上传失败！");
	        }     
		return null;
	} 
	//获取数据库无license的数据信息
	@RequestMapping(value="license/licenseLogin.do")
	@ResponseBody
	public Map<String,Object> licenseLogin(@RequestParam(value="loginname") String loginname,
			@RequestParam(value="password") String password,
			@RequestParam(value="url") String url)
		{  
		  Map<String,Object> map=new HashMap<String,Object>();
		  //构造HttpClient的实例
		  HttpClient httpClient = new HttpClient();
		  List<LicenseBean> list = licenseService.queryByLicense("0");
		  if(list.size()>0){
			  JSONArray json=JSONArray.fromObject(list);
			  String jsonStr="{jsonStr:"+json.toString()+"}";
			  //创建GET方法的实例
			  GetMethod getMethod = new GetMethod(url+"?loginname="+loginname+"&password="+password+"&jsonStr="+jsonStr);
			  //使用系统提供的默认的恢复策略
			  getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					  new DefaultHttpMethodRetryHandler());
			  try {
				  //执行getMethod
				  int statusCode = httpClient.executeMethod(getMethod);
				  if (statusCode != HttpStatus.SC_OK) {
					  System.err.println("Method failed: "
							  + getMethod.getStatusLine());
				  }
				  //读取内容 
				  // HttpClient警告“Going to buffer response body of large or unknown size. Using getResponseBody
				  //返回的HTTP头没有指定contentLength，或者是contentLength大于上限（默认是1M）
				  /* byte[] responseBody = getMethod.getResponseBody();
		          //处理内容
		          System.out.println(new String(responseBody));*/
				  
				  BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream()));  
				  StringBuffer stringBuffer = new StringBuffer();  
				  String str = "";  
				  while((str = reader.readLine())!=null){  
					  stringBuffer.append(str);  
				  }  
				  String result = stringBuffer.toString(); 
				  if (!"".equals(result) && result!= null) {
							if ("0".equals(result)) {
								//表示用户名和密码不正确   "不存在此用户"
								map.put("msg", 0);
							} else if ("1".equals(result)) {
								//表示登录的用户没权限
								map.put("msg", 1);
							} else if ("2".equals(result)) {
								//要注册的license数量大于申请的数量
								map.put("msg", 2);
							} else if ("3".equals(result)) {
								//获取licnese信息失败
								map.put("msg", 3);
							} else if("4".equals(result)){
								map.put("msg", 4); //没有要获取的license信息
							}else{
								map.put("msg", 6);;
								JSONArray array = JSONArray.fromObject(result);  
								for(int i = 0; i < array.size(); i++){     
									JSONObject jsonObject = array.getJSONObject(i);    
									LicenseBean license=new LicenseBean();
									license.setMac(jsonObject.getString("mac"));
								//	license.setHardinfo(jsonObject.getString("hardinfo"));
									license.setLicense(jsonObject.getString("licenses"));
									licenseService.updateLicense(license); //更新数据库
						  	    }
								//修改系统设置config表数据
								ConfigBean configBean=new ConfigBean();
								configBean.setConfig_key("license_loginname");
								configBean.setConfig_value(loginname);
								configService.updateConfig(configBean);
								
								ConfigBean configBean2=new ConfigBean();
								configBean2.setConfig_key("license_password");
								configBean2.setConfig_value(password);
								configService.updateConfig(configBean2);
								
								ConfigBean configBean3=new ConfigBean();
								configBean3.setConfig_key("license_url");
								configBean3.setConfig_value(url);
								configService.updateConfig(configBean3);
								
								int terminalTotal=licenseService.queryLicenseCount(); //要注册的终端总数
								int terminalHad=terminalTotal-licenseService.queryByLicense("0").size(); //已经注册的终端总数
								map.put("terminalTotal", terminalTotal);
								map.put("terminalHad", terminalHad);
						   }
				 }else{
					 map.put("msg", 7);
				}
			   } catch (HttpException e) {
				  //发生致命的异常，可能是协议不对或者返回的内容有问题
				   if(configService.queryConfig("islisten").equals("1")){
					   ErrorLogBean elb = new ErrorLogBean();
					   elb.setClass_name(LicenseController.class.toString());
					   elb.setException_type("HttpException");
					   elb.setException_reason("获取license失败");
					   elb.setFunction_name("获取license");
					   elb.setModule_name("系统设置");
					   elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
					   errorLogService.insertErrorLog(elb);
				   }
				  System.out.println("Please check your provided http address!");
				  map.put("msg", 7);  //网络连接异常
				  e.printStackTrace();
			   } catch (IOException e) {
				  //发生网络异常
				   if(configService.queryConfig("islisten").equals("1")){
					   ErrorLogBean elb = new ErrorLogBean();
					   elb.setClass_name(LicenseController.class.toString());
					   elb.setException_type("IOException");
					   elb.setException_reason("获取license失败");
					   elb.setFunction_name("获取license");
					   elb.setModule_name("系统设置");
					   elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
					   errorLogService.insertErrorLog(elb);
				   }
				   map.put("msg", 5);  //网络连接异常
				  e.printStackTrace();
			   } finally {
				  //释放连接
				  getMethod.releaseConnection();
			   }
		   }else{
			   map.put("msg", 4);;  
		   }
		  return map;
	   }
}
