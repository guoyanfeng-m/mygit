package util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;






import net.sf.json.JSONObject;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import util.PublishUtil;

public class GetXml {
    /***
     * 获取项目的路径
     * @param filename
     * @return
     */
	private static String getPath(String readpath,String fileUrl) {
		URL url = GetXml.class.getResource("");
		File file = new File(url.getFile());
		String path = file.getParent();
		while (-1 != path.lastIndexOf("bin") || -1 != path.lastIndexOf(".jar")) {
			file = new File(path);
			path = file.getParent();
		}
		if (path.startsWith("file")) {
			path = path.replaceAll("file:", "");
		}
		path=readpath+fileUrl;
		return path;
	}
	  public Document load(String readpath,String fileUrl){  
		     String FileUrl=getPath(readpath,fileUrl);
	         Document document=null;  
	         String url=FileUrl;  
	         try {  
	             SAXBuilder reader = new SAXBuilder();   
	             document=reader.build(new File(url));  
	        } catch (Exception e) {  
	             e.printStackTrace();  
	        }  
	         return document;  
	     }  
	     public String XmlToString(String readpath,String fileUrl){  
	         Document document=null;  
	         document=this.load(readpath,fileUrl);  
	           
	         Format format =Format.getPrettyFormat();      
	         format.setEncoding("UTF-8");//设置编码格式   
	           
	         StringWriter out=null; //输出对象  
	         String sReturn =""; //输出字符串  
	         XMLOutputter outputter =new XMLOutputter();   
	         out=new StringWriter();   
	         try {  
	            outputter.output(document,out);  
	         } catch (IOException e) {  
	            e.printStackTrace();  
	         }   
	         sReturn=out.toString();   
	         return sReturn;  
	     }  
	     /***
	      * 
	      * @param XML
	      * @return
	      */
	public  void strXml(String readpath,String fileUrl,String[]mac,String ip){
		GetXml xml=new GetXml();  
		String strxml=xml.XmlToString(readpath,fileUrl); 
//	    PublishUtil u = new PublishUtil();
	    JSONObject jsons = new JSONObject();
		jsons.put("command","publishMessage");  //发送任务的名称
		jsons.put("msg",strxml);            //发送文件字符串
		jsons.put("target", mac);           //服务器端的mac地址
//		u.publishTask(jsons,ip);
		new PublishUtil().publishTask(jsons, ip,"节目管理");
		/*try {   //延迟当前线程
			Thread.currentThread().sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
	/****
	 * 发送停止信息命令的方法
	 * @param args
	 */
	@SuppressWarnings("unused")
	public void stopCommand(String[] mac,String msgid,String ip){
		  PublishUtil u = new PublishUtil();
		  JSONObject jsons = new JSONObject();
		  jsons.put("command", "deleteMessage");
		  jsons.put("target", mac);
	      jsons.put("msgid", msgid);
//		  u.publishTask(jsons,ip);
		  new PublishUtil().publishTask(jsons, ip,"节目管理");
	}
	/****
	 * 根据某一消息发送停止消息的命令
	 * @param args
	 */
	@SuppressWarnings("unused")
	public void TaskstopCommand(String[] mac,String msgid,String ip){
		  PublishUtil u = new PublishUtil();
		  JSONObject jsons = new JSONObject();
		  jsons.put("command", "deleteMessage");
		  jsons.put("target", mac);
	      jsons.put("msgid", msgid);
//		  u.publishTask(jsons,ip);
		  new PublishUtil().publishTask(jsons, ip,"节目管理");
	}
}

