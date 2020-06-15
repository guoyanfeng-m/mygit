package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * 
 * @ClassName: XmlUtil 
 * @Description: (xml工具类) 
 * @author laiyunjian 
 * @date 2014年10月14日 下午12:56:52 
 *
 */
public class XmlUtil {
	/**
	 * @Title: xmlToMap 
	 * @Description: (指定路径的xml转换成Map) 
	 * @param url
	 * @return Map<String,Object>
	 * @throws
	 */
	public  Map<String, Object> xmlToMap(String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			Map<String, Object> beanMap = new HashMap<String, Object>();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(url);
			Document doc = builder.parse(file);
			Element elem = doc.getDocumentElement();
			//调用构造Map键值方法
			appendChildMap(elem, beanMap);
			//根节点Map及根节点名
			map.put(elem.getNodeName(), beanMap);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * @Title: StringToMap 
	 * @Description: (将xml文件字符串转换成Map) 
	 * @param String xmlstr
	 * @return Map<String,Object>
	 * @throws
	 */
	public static Map<String, Object> stringToMap(String xmlstr) {
		Map<String, Object> map = new HashMap<String, Object>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			Map<String, Object> beanMap = new HashMap<String, Object>();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse( new  ByteArrayInputStream(xmlstr.getBytes("UTF-8"))); 
			Element elem = doc.getDocumentElement();
			//调用构造Map键值方法
			appendChildMap(elem, beanMap);
			//根节点Map及根节点名
			map.put(elem.getNodeName(), beanMap);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 
	 * @Title: appendChildMap 
	 * @Description: (将xml节点转换成Map的迭代方法) 
	 * @param tempNode xml节点
	 * @param beanMap Map对象
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private  static void appendChildMap(Element tempNode, Map<String, Object> beanMap) {
		// 复制节点的属性至Map对象中
		if (tempNode.hasAttributes()) {
			NamedNodeMap attributes = tempNode.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				String attribute_name = attributes.item(i).getNodeName();
				String attribute_value = attributes.item(i).getNodeValue();
				beanMap.put(attribute_name, attribute_value);
			}
		}
		// 给子结点建新的Map对象
		NodeList sub_messageItems = tempNode.getChildNodes();
		int sub_item_number = sub_messageItems.getLength();
		//没有子节点
		if (sub_item_number < 1) {
			return;
		} else {
			Map<String,List<Object>> tMap = new HashMap<String,List<Object>>();
			String nodeName ;
			List<Object> tempList = null;
			for (int j = 0; j < sub_item_number; j++) {
				//验证子节点是否为合法Element
				if (sub_messageItems.item(j) instanceof Element){
					//如果子节点是合法Element则新建Map对象并调用方法appendChildMap
					Map<String, Object> tempMap = new HashMap<String, Object>();
					Element sub_messageItem = (Element) sub_messageItems.item(j);
					appendChildMap(sub_messageItem, tempMap);
					nodeName = sub_messageItem.getNodeName();
					//给子节点归类放入不同的list内
					if(tMap.containsKey(nodeName)){
						tempList = (List<Object>) tMap.get(nodeName);
						tempList.add(tempMap);
					}else{
						tempList = new ArrayList<Object>();
						tempList.add(tempMap);
						tMap.put(nodeName, tempList);
					}
				}
			}
			//构造子节点list对象
			Set<Entry<String, List<Object>>> set = tMap.entrySet();
			Iterator<Entry<String, List<Object>>> records = set.iterator();
			while (records.hasNext()) {
				List<Object> addList = null;
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) records.next();
				if (entry.getValue().getClass().equals(ArrayList.class)) {
					addList = (ArrayList<Object>) entry.getValue();
					beanMap.put(entry.getKey().toString(), addList);
				}
			}

		}
	}
	/**
	 * <p>          
	 *       <discription> 概述：map 转换xml并保存文件 </discription>
	 * </p>  
	 * @Author         创建人：       
	 * @CreateDate     创建时间：   
	 * @UpdateDate     更新时间：   
	 * @Package_name   包名：          iis/util
	 * @Param          参数：          @param url
	 * @Param          参数：          @param map
	 * @Param          参数：          @return
	 * @Param          参数：          @throws Exception  
	 * @Rerurn         返回：          boolean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  boolean mapToXml(String url, Map<String, Object> map) throws Exception {
		// 解析器工厂类
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		FileOutputStream out = null;
		try {
			// 解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 操作的Document对象
			Document document = builder.newDocument();
			// 设置XML的版本
			document.setXmlVersion("1.0");
			// 创建根节点
			Set<?> set = map.entrySet();
			Iterator<?> records = set.iterator();
			// 循环读取Map下的所有键值
			while (records.hasNext()) {
				Map.Entry entry = (Map.Entry) records.next();
				if (entry.getValue().getClass().equals(HashMap.class)){
					Element root = document.createElement(entry.getKey().toString());
					appendChildNode(document, root, (Map<String, Object>) entry.getValue());
					document.appendChild(root);
				}
			}
			// 开始把Document映射到文件
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFactory.newTransformer();
			Properties properties = transFormer.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			transFormer.setOutputProperties(properties);
			// 设置输出结果
			DOMSource domSource = new DOMSource(document);
			// 生成xml文件
			File file = new File(url);
			// 判断是否存在,如果不存在,则创建
			if (!file.exists()) {
				file.createNewFile();
			}
			// 文件输出流
			out = new FileOutputStream(file);
			// 设置输入源
			StreamResult xmlResult = new StreamResult(out);
			// 输出xml文件
			transFormer.transform(domSource, xmlResult);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	/**
	 * 
	 * @Title: appendChildNode
	 * @Description: (将Map转换成xml节点的迭代方法)
	 * @param @param document
	 * @param @param node
	 * @param @param map
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private  void appendChildNode(Document document, Element node,
			Map<String, Object> map) {
		/** 开始对map进行解析 */
		if (map == null)
			throw new NullPointerException("map 数据为空,不能解析!");
		Set<?> set = map.entrySet();
		Iterator<?> records = set.iterator();
		// 循环读取Map下的所有键值
		while (records.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) records.next();
			// 如果值为List
			if (entry.getValue().getClass().equals(ArrayList.class)) {
				List<Object> tempList = (ArrayList<Object>) entry.getValue();
				// 循环list新建节点list
				for (Object object : tempList) {
					if (object.getClass().equals(HashMap.class)) {
						Element tempnode = document.createElement(entry
								.getKey().toString());
						// 调用迭代方法
						appendChildNode(document, tempnode,
								(HashMap<String, Object>) object);
						node.appendChild(tempnode);
					}
				}
				// 如果值为Map
			} else if (entry.getValue().getClass().equals(HashMap.class)) {
				// 新建节点
				Element tempnode = document.createElement(entry.getKey()
						.toString());
				appendChildNode(document, tempnode,
						(HashMap<String, Object>) entry.getValue());
				node.appendChild(tempnode);
			} else {
				// 到达顶点 新增属性
				node.setAttribute(entry.getKey().toString(), entry.getValue()
						.toString());
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String mapToString(Map<String,Object> map) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String sReturn =null;
		try {
			// 解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 操作的Document对象
			Document document = builder.newDocument();
			// 设置XML的版本
			document.setXmlVersion("1.0");
			// 创建根节点
			Set<?> set = map.entrySet();
			Iterator<?> records = set.iterator();
			// 循环读取Map下的所有键值
			while (records.hasNext()) {
				Map.Entry entry = (Map.Entry) records.next();
				if (entry.getValue().getClass().equals(HashMap.class)){
					Element root = document.createElement(entry.getKey().toString());
					appendChildNode(document, root, (Map<String, Object>) entry.getValue());
					document.appendChild(root);
				}
			}
			// 开始把Document映射到文件
			//			TransformerFactory transFactory = TransformerFactory.newInstance();
			//			Transformer transFormer = transFactory.newTransformer();
			//			Properties properties = transFormer.getOutputProperties();
			//			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			//			transFormer.setOutputProperties(properties);
			// 设置输出结果
			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			Result result = new StreamResult(writer);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(domSource, result);
			sReturn =  writer.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sReturn;
	}

	public String XmlToString(String url){  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String sReturn =""; //输出字符串  
		try {  
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(url);
			Document doc = builder.parse(file);
			DOMSource source = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			Result result = new StreamResult(writer);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
			sReturn =  writer.getBuffer().toString();	
		} catch (IOException e) {  
			e.printStackTrace();  
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return sReturn;  
	}  
	/**
	 * 
	 * @Title: checkFile 
	 * @Description:(检查文件是否存在) 
	 * @param fileName
	 * @return
	 * @throws
	 */
	public boolean checkFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		}
		return true;
	}
	public static String getNodeValue(String nodeName)
			throws NullPointerException {
		try {
			Document doc = getConfig();
			NodeList list = doc.getElementsByTagName(nodeName);
			return list.item(0).getFirstChild().getNodeValue();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private synchronized static Document getConfig()
			throws ParserConfigurationException, SAXException, IOException {
		File f = new File(System.getProperty("user.dir") + "\\..\\..\\tomcat\\webapps\\WEB-INF\\classes\\DBConf.xml");//XML配置文件路径
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(f);
		return doc;
	}
}
