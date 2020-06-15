package util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import beans.license.LicenseBean;



public class ParseXml {
   public   List<LicenseBean>  getXml(String path){
	   List<LicenseBean> views=new ArrayList<LicenseBean>();
	   
	   //实例化一个文档构建器工厂
	   DocumentBuilderFactory doc=DocumentBuilderFactory.newInstance();
	   try {
		 //通过文档构建器工厂获取一个文档构建器
		DocumentBuilder db=doc.newDocumentBuilder();
        //通过文档通过文档构建器构建一个文档实例
		org.w3c.dom.Document document=db.parse(path);
		 //获取所有名字为 “info” 的节点
		NodeList nls= document.getElementsByTagName("info");
		for(int n=0;n<nls.getLength();n++){
			LicenseBean view=new LicenseBean();
			Element el=(Element) nls.item(n);
			view.setMac(el.getElementsByTagName("mac").item(0).getFirstChild().getNodeValue());
			view.setHardinfo(el.getElementsByTagName("hardinfo").item(0).getFirstChild().getNodeValue());
			NodeList ns=el.getElementsByTagName("licenses");
			if(ns.getLength()>0){
				if(el.getElementsByTagName("licenses").item(0).getFirstChild()==null){
					view.setLicense("");
				}else{
					view.setLicense(el.getElementsByTagName("licenses").item(0).getFirstChild().getNodeValue());
				}
			}else{
				view.setLicense("");
			}
		    views.add(view);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	   return views;
   }
   
   //创建xml文件
   @SuppressWarnings("unused")
public boolean createXml(List<LicenseBean> list,String path,String fileName){
	   boolean flag=false;
	   try {
		Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();// 创建操作的Document对象
		Element root=doc.createElement("messages");   //创建根节点
		doc.appendChild(root); // 将根节点添加到Document对象中去
		
		for(int i=0;i<list.size();i++){
			Element info=doc.createElement("info");
			root.appendChild(info);
			Element mac=doc.createElement("mac");
			info.appendChild(mac);
			Element hardinfo=doc.createElement("hardinfo");
			info.appendChild(hardinfo);
			Element licenses=doc.createElement("licenses");
			info.appendChild(licenses);
			mac.setTextContent(list.get(i).getMac());
			hardinfo.setTextContent(list.get(i).getHardinfo());
			licenses.setTextContent(list.get(i).getLicense());
		}
		    TransformerFactory transFactory=TransformerFactory.newInstance(); //创建转换对象
			Transformer transformer=transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");//设置文档的换行
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4"); //设置文档缩进
			DOMSource domSource=new DOMSource(doc);  //转换源DOM树
			SimpleDateFormat nowtime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//设置日期格式
		//	String now = nowtime.format(new Date()).toString();  
		//	String xmlPath="d:\\license.xml";
			File file=new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			FileOutputStream out;
			out = new FileOutputStream(file+"/"+fileName);
			StreamResult xmlResult=new StreamResult(out);
			transformer.transform(domSource, xmlResult);
			flag=true;
			//System.out.println("生成XML文件成功!");  
		} catch (Exception e) {
			e.printStackTrace();
		}
	return flag;
   }
   //创建xml文件
   public boolean createXml2(List<LicenseBean> list,String path,String fileName){
	   boolean flag=false;
	   try {
		Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();// 创建操作的Document对象
		Element root=doc.createElement("messages");   //创建根节点
		doc.appendChild(root); // 将根节点添加到Document对象中去
		
		for(int i=0;i<list.size();i++){
			Element info=doc.createElement("info");
			root.appendChild(info);
			Element mac=doc.createElement("mac");
			info.appendChild(mac);
			Element hardinfo=doc.createElement("hardinfo");
			info.appendChild(hardinfo);
			Element licenses=doc.createElement("licenses");
			info.appendChild(licenses);
			mac.setTextContent(list.get(i).getMac());
			hardinfo.setTextContent(list.get(i).getHardinfo());
			licenses.setTextContent(list.get(i).getLicense());
		}
		    TransformerFactory transFactory=TransformerFactory.newInstance(); //创建转换对象
			Transformer transformer=transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");//设置文档的换行
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4"); //设置文档缩进
			DOMSource domSource=new DOMSource(doc);  //转换源DOM树
		//	SimpleDateFormat nowtime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//设置日期格式
		//	String now = nowtime.format(new Date()).toString();  
		//	String xmlPath="d:\\license.xml";
			File file=new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			FileOutputStream out;
			out = new FileOutputStream(file+"/"+fileName);
			StreamResult xmlResult=new StreamResult(out);
			transformer.transform(domSource, xmlResult);
			flag=true;
			//System.out.println("生成XML文件成功!");  
		} catch (Exception e) {
			e.printStackTrace();
		}
	return flag;
   }
}
