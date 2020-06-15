package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * <p>
 * Description: xml解析类
 * </p>
 * 
 */
public class PlaylistXmlParser {

	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private Document doc;
	private Element root;
	private String fileName;
	private InputStream inputstream;

	/**
	 * 解析文件xml
	 * 
	 * @param file
	 *            String
	 */
	public PlaylistXmlParser(String file) {
		if (file == null) {
			return;
		}
		this.fileName = file;
		try {
			load();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	/**
	 * 初始化XML
	 * 
	 * @throws IOException
	 */
	public void loadStream() throws IOException {
		try {
			loadXMLParser();
			doc = builder.parse(inputstream);
			root = doc.getDocumentElement();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} finally {
			inputstream.close();
		}
	}

	/**
	 * 初始化XML
	 * 
	 * @throws IOException
	 */
	private void load() throws IOException {
		try {
			loadXMLParser();
			File file = new File(fileName);
			doc = builder.parse(file);
			root = doc.getDocumentElement();
		} catch (SAXException SaxEx) {
			SaxEx.printStackTrace();
			throw new IOException(SaxEx.getMessage() + "XML file parse error:"
					+ SaxEx.getException());
		} catch (IOException IoEx) {
			IoEx.printStackTrace();
			throw new IOException(IoEx.getMessage() + "XML file parse error:");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException(ex.getMessage() + "XML file parse error:");
		}
	}

	/**
	 * 初始化XML
	 * 
	 * @throws IOException
	 */
	private void loadXMLParser() throws IOException {
		if (builder == null) {
			try {
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				throw new IOException("XML Parser load error:"
						+ ex.getLocalizedMessage());
			} catch (FactoryConfigurationError ConfErrEx) {
				throw new IOException("XML Parser load error:"
						+ ConfErrEx.getLocalizedMessage());
			} catch (Exception Ex) {
				throw new IOException("XML Parser load error:"
						+ Ex.getLocalizedMessage());
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> getPath() {
		List<String> pathList = new ArrayList<String>();
		NodeList templateList = this.root.getElementsByTagName("scene");
		for (int i = 0; i < templateList.getLength(); i++) {
			Node templateNode = templateList.item(i);
			NamedNodeMap attrs = templateNode.getAttributes();
			Node bgpic = attrs.getNamedItem("backimg");
			if (null != bgpic) {
				String path = attrs.getNamedItem("backimg").getNodeValue();
				if (null != path && !"".equals(path)) {
					pathList.add(path);
				}
			}
		}
		NodeList elementList = this.root.getElementsByTagName("element");
		for (int i = 0; i < elementList.getLength(); i++) {
			Node elementNode = elementList.item(i);
			NamedNodeMap tAttrs = elementNode.getAttributes();
			String type = tAttrs.getNamedItem("type").getNodeValue();
			if(type.equals("8")){
				continue;
			}
			Node tempNode = tAttrs.getNamedItem("src");
			if (null != tempNode) {
				String path = tAttrs.getNamedItem("src").getNodeValue();
				String[] paths = path.split("#");
				if (null != path && !"".equals(path)) {
					for(int j = 0; j < paths.length; j++){
						pathList.add(paths[j]);
					}
					
				}
			}
		}
		NodeList buttonList = this.root.getElementsByTagName("button");
		for (int i = 0; i < buttonList.getLength(); i++) {
			Node buttonNode = buttonList.item(i);
			NamedNodeMap tAttrs = buttonNode.getAttributes();
			Node tempNode = tAttrs.getNamedItem("backimg");
			if (null != tempNode) {
				String path = tAttrs.getNamedItem("backimg").getNodeValue();
				String[] paths = path.split("#");
				if (null != path && !"".equals(path)) {
					for(int j = 0; j < paths.length; j++){
						pathList.add(paths[j]);
					}
					
				}
			}
			Node tempNode1 = tAttrs.getNamedItem("selectimg");
			if (null != tempNode1) {
				String path = tAttrs.getNamedItem("selectimg").getNodeValue();
				String[] paths = path.split("#");
				if (null != path && !"".equals(path)) {
					for(int j = 0; j < paths.length; j++){
						pathList.add(paths[j]);
					}
					
				}
			}
		}
		HashSet h = new HashSet(pathList);
		pathList.clear();
		pathList.addAll(h);
		/*
		 * for(int k=0;k<pathList.size();k++){
		 * System.out.println(pathList.get(k)+"k=============>获得解析完路径!"); }
		 */
		return pathList;
	}
	
//  public static void main(String[] args) {
//	  PlaylistXmlParser parser=new PlaylistXmlParser("E:/ftpFile/programXmlFile/pp.xml");
//	  parser.parserProgram();
//	  List<TemplateVo> list=parser.parserProgram11();
//	  for(TemplateVo templateVo:list)
//	  {
//		  System.out.println(templateVo.getId());
//		  for(RegionVo regionVo:templateVo.getList())
//		  {
//			  System.out.println(regionVo.getId());
//			  for(ElementVo elementVo:regionVo.getList())
//			  {
//				  System.out.println(elementVo.getId());
//			  }
//		  }
//	  }  
//		  
//	
//}
}
