package util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Dom4j 获取xml 谁以后有时间完善吧
 * 
 * @author weihuiming
 * 
 */
public class XmlDom4j {
	
	/**
	 * 节目单xml存储的信息
	 */
	private Map<String, Object> impMap = new HashMap<String, Object>();
	
	public Map<String, Object> getImpMap() {
		return impMap;
	}

	public void setImpMap(Map<String, Object> impMap) {
		this.impMap = impMap;
	}

	/**
	 * 获取xml文档对象
	 * 
	 * @param xmlUrl
	 * @return
	 * @throws Exception
	 */
	public Element DomRoot(String xmlUrl) throws Exception {
		// 创建一个SAXReader对象
		SAXReader sax = new SAXReader();
		// 根据指定的路径创建file对象
		File xmlFile = new File(xmlUrl);
		// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		Document document = sax.read(xmlFile);
		// 获取根节点
		Element root = document.getRootElement();
		return root;
	}

	/**
	 * 从指定节点开始,递归遍历所有子节点 有什么好的办法可以继续修改
	 * 
	 * @author weihuiming
	 */
	@SuppressWarnings("unchecked")
	public void getNodes(Element node) {

		// 获取节目单xml中数据
		// 当前节点的名称、文本内容和属性
		// System.out.println("当前节点名称：" + node.getName());// 当前节点名称
		// System.out.println("当前节点的内容：" + node.getTextTrim());// 当前节点名称
		//节目单的src
		String key = "";
		//节目单素材的原名
		String value = "";
		//节目单素材的分辨率
		String xmlRes = "";
		Map<String, String> programMap = null;
		//当前节点的所有属性的list
		List<Attribute> listAttr = null;
		// 节目单信息节点
		if ("program".equals(node.getName())) {
			listAttr = node.attributes();
			programMap = new HashMap<String, String>();
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				programMap.put(attr.getName(), attr.getValue());
			}
			if (!programMap.isEmpty()) {
				impMap.put("program", programMap);
			}
		} else if ("element".equals(node.getName())) {
			listAttr = node.attributes();
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				String name = attr.getName();// 属性名称
				if ("name".equals(name)) {
					// 节目节点的图片原名
					value = attr.getValue();
				} else if ("src".equals(name)) {
					// 目录节点的加密名
					key = attr.getValue();
				} else if ("resolution".equals(name)) {
					xmlRes = attr.getValue();
				}
				// String values = attr.getValue();// 属性的值
				// System.out.println("属性名称：" + name + "属性值：" + values);
			}
			// 有的场景没有素材，防止错误
			if (key != "") {
				// 文本要存储大小
				if (key.contains(".xml")) {
					key = key + "_" + xmlRes;
				}
				impMap.put(key, value);
			}
			// 解析背景图
		} else if ("scene".equals(node.getName())) {
			listAttr = node.attributes();
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				String name = attr.getName();// 属性名称
				if ("backimgName".equals(name)) {
					// 背景图片的原名
					value = attr.getValue();
				} else if ("backimg".equals(name)) {
					// 背景图片的加密名
					key = attr.getValue();
				}
				// String values = attr.getValue();// 属性的值
				// System.out.println("属性名称：" + name + "属性值：" + values);
			}
			impMap.put(key, value);
			// 解析互动按钮
		} else if ("button".equals(node.getName())) {
			listAttr = node.attributes();
			String key1 = "";//
			String value1 = "";
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				String name = attr.getName();// 属性名称
				if ("buselectimgName".equals(name)) {
					// 互动按钮第一个图片
					value = attr.getValue();
				} else if ("oldselectimg".equals(name)) {
					// 第一个图片名称
					key = attr.getValue();
				} else if ("bubackimgName".equals(name)) {
					// 第二个图片
					value1 = attr.getValue();
				} else if ("oldbackimg".equals(name)) {
					// 第二个图片名称
					key1 = attr.getValue();
				}
				// String values = attr.getValue();// 属性的值
				// System.out.println("属性名称：" + name + "属性值：" + values);
			}
			// 点击图片或背景图片防止为空
			if (key != "") {
				impMap.put(key, value);
			}
			if (key1 != "") {
				impMap.put(key1, value1);
			}
		}
		//递归遍历当前节点所有的子节点
		//所有一级子节点的list
		List<Element> listElement = node.elements();
		//遍历所有一级子节点
		for (Element e : listElement) {
			//递归
			this.getNodes(e);
		}
	}

}
