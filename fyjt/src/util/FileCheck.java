package util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
@Component
public class FileCheck {
	
	public boolean checkFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSrcByMap(Set<String> srcList,Set<String> idList,Set<String> backimglist,
			Set<String> plugList,Set<String> screenList,Map<String, Object> map,
			boolean isMainScene,List<String> srcl) {
		if(isMainScene){
			List<Map<String, Object>> pluglist = (List<Map<String, Object>>) map.get("plug");
			if(pluglist != null){
				for (int i = 0; i < pluglist.size(); i++) {
					List<Map<String,Object>> tm = (List<Map<String, Object>>) pluglist.get(i).get("touchvideo");
					List<Map<String,Object>> am = (List<Map<String,Object>>) pluglist.get(i).get("album");
					if((pluglist.get(i).get("weather") != null && ((ArrayList) pluglist.get(i).get("weather")).size() > 0)
							|| (pluglist.get(i).get("timepiece") != null && ((ArrayList) pluglist.get(i).get("timepiece")).size() > 0)
							|| (pluglist.get(i).get("count") != null && ((ArrayList) pluglist.get(i).get("count")).size() > 0) 
							|| (tm != null && tm.get(0).get("element") != null)
							|| (am != null && am.get(0).get("element") != null)){
						if(tm != null && tm.get(0).get("element") != null){
							List<Map<String, Object>> tmelist = (List<Map<String, Object>>) tm.get(0).get("element");
							if(tmelist.size() > 0){
								plugList.add(pluglist.get(i).toString());
								continue;
							}else
								continue;
						}
						if(am != null && am.get(0).get("element") != null){
							List<Map<String, Object>> amelist = (List<Map<String, Object>>) am.get(0).get("element");
							if(amelist.size() > 0){
								plugList.add(pluglist.get(i).toString());
								continue;
							}else
								continue;
						}
						plugList.add(pluglist.get(i).toString());
					}
				}
			}
			if(map.get("region") != null && !map.get("region").toString().equals("null") ){
				List<Map<String, Object>> regionlist = (List<Map<String, Object>>) map.get("region");
				for (int i = 0; i < regionlist.size(); i++) {
					List<Map<String,Object>> elementlist = 
						(List<Map<String, Object>>) regionlist.get(i).get("element");
					if(elementlist != null){
						for (int j = 0; j < elementlist.size(); j++) {
							if(elementlist.get(j).get("src") != null && (!elementlist.get(j).get("src").toString().equals(""))){
								srcList.add(elementlist.get(j).get("src").toString());
								srcl.add(elementlist.get(j).get("src").toString());
								idList.add(elementlist.get(j).get("id").toString());
							}else if(elementlist.get(j).get("websrc") != null && (!elementlist.get(j).get("websrc").toString().equals(""))){
								srcList.add(elementlist.get(j).get("websrc").toString());
								srcl.add(elementlist.get(j).get("websrc").toString());
								idList.add(elementlist.get(j).get("id").toString());
							}
						}
					}
					
				}
			}
			if(map.get("backimg") != null && !map.get("backimg").toString().equals("") 
					&& !map.get("backimg").toString().equals("img/u106.png")){
				backimglist.add(map.get("backimg").toString());
			}
		}else{
			Set<?> set = map.entrySet();
			Iterator<?> records = set.iterator();
			while (records.hasNext()) {
				Map.Entry entry = (Map.Entry) records.next();
				if (entry.getValue().getClass().equals(ArrayList.class)) {
					List<Object> tempList = (ArrayList<Object>) entry.getValue();
					// 循环list新建节点list
					for (Object object : tempList) {
	//					getSrcByMap(srcList, (Map<String, Object>) object);
						if (object.getClass().equals(HashMap.class)) {
							if (entry.getKey().toString().equals("element")
									&& ((Map<String, Object>) object).get("src") != null
									&& !((Map<String, Object>) object).get("src").equals("")) {
								String src = ((Map<String, Object>) object).get("src").toString();
								srcList.add(src);
								srcl.add(src);
								idList.add(((Map<String, Object>) object).get(
									"id").toString());
							}else if (entry.getKey().toString().equals("element")
									&& ((Map<String, Object>) object).get("websrc") != null
									&& !((Map<String, Object>) object).get("websrc")
											.equals("")) {
								srcList.add(((Map<String, Object>) object).get(
										"websrc").toString());
								srcl.add(((Map<String, Object>) object).get(
									"websrc").toString());
								idList.add(((Map<String, Object>) object).get(
								"id").toString());
							} else if (entry.getKey().toString().equals("scene")
									&& ((Map<String, Object>) object).get("backimg") != null
									&& !((Map<String, Object>) object).get("backimg")
											.equals("")) {
								if(!((Map<String, Object>) object).get("backimg").toString()
										.equals("img/u106.png")){
									backimglist.add(((Map<String, Object>) object).get(
									"backimg").toString());
	//								srcList.add(((Map<String, Object>) object).get(
	//								"backimg").toString());
								}
								if(((Map<String, Object>) object).get("plug") != null && !((Map<String, Object>) object).get("plug")
										.equals("")){
									if(!((Map<String, Object>) object).get("plug").toString()
											.equals("")){
										Object plugobj = ((Map<String, Object>) object).get("plug");
										ArrayList plugarr =  (ArrayList) plugobj;
										for (int i = 0; i < plugarr.size(); i++) {
											Map<String,Object> plugmap = (Map<String, Object>) plugarr.get(i);
											ArrayList weather =  (ArrayList) plugmap.get("weather");
											ArrayList timepiece = (ArrayList) plugmap.get("timepiece");
											ArrayList count = (ArrayList) plugmap.get("count");
											if(weather != null && weather.size() > 0){
												plugList.add(plugmap.get("weather").toString());
											}else if(timepiece != null && timepiece.size() > 0){
												plugList.add(plugmap.get("timepiece").toString());
											}else if(count != null && count.size() > 0){
												plugList.add(plugmap.get("count").toString());
											}
										}
									}
								}
								getSrcByMap(srcList, idList,backimglist,plugList,screenList,(Map<String, Object>) object,isMainScene,srcl);
							}else if (entry.getKey().toString().equals("scene") 
									&& ((Map<String, Object>) object).get("plug") != null
									&& !((Map<String, Object>) object).get("plug")
									.equals("")) {
								if(!((Map<String, Object>) object).get("plug").toString()
										.equals("")){
									Object plugobj = ((Map<String, Object>) object).get("plug");
									ArrayList plugarr =  (ArrayList) plugobj;
									for (int i = 0; i < plugarr.size(); i++) {
										Map<String,Object> plugmap = (Map<String, Object>) plugarr.get(i);
										ArrayList weather =  (ArrayList) plugmap.get("weather");
										ArrayList timepiece = (ArrayList) plugmap.get("timepiece");
										ArrayList count = (ArrayList) plugmap.get("count");
										if(weather != null && weather.size() > 0){
											plugList.add(plugmap.get("weather").toString());
										}else if(timepiece != null && timepiece.size() > 0){
											plugList.add(plugmap.get("timepiece").toString());
										}else if(count != null && count.size() > 0){
											plugList.add(plugmap.get("count").toString());
										}
									}
								}
								getSrcByMap(srcList, idList,backimglist,plugList,screenList,(Map<String, Object>) object,isMainScene,srcl);
							}else if (entry.getKey().toString().equals("scene") 
									&& ((Map<String, Object>) object).get("id").equals("screensaver")) {
								List regionlist = (List) ((Map<String, Object>) object).get("region");
								Map<String,Object> elementmap = (Map<String,Object>)regionlist.get(0);
								List<Map<String,Object>> peblist = (List<Map<String,Object>>) elementmap.get("element");
								if(peblist != null){
									for (int i = 0; i < peblist.size(); i++) {
										screenList.add(peblist.get(i).get("src").toString());
									}
								}
								getSrcByMap(srcList, idList,backimglist,plugList,screenList,(Map<String, Object>) object,isMainScene,srcl);
							}else{
								getSrcByMap(srcList, idList,backimglist,plugList,screenList,(Map<String, Object>) object,isMainScene,srcl);
							}
						}
					}
					// 如果值为Map
				} else if (entry.getValue().getClass().equals(HashMap.class)) {
					getSrcByMap(srcList,idList,backimglist,plugList,screenList,(Map<String, Object>) entry.getValue(),isMainScene,srcl);
	//				if (entry.getKey().toString().equals("element")
	//						&& ((Map<String, Object>) entry.getValue()).get("src") != null
	//						&& !((Map<String, Object>) entry.getValue()).get("src")
	//								.equals("")) {
	//					srcList.add(((Map<String, Object>) entry.getValue()).get(
	//							"src").toString());
	//				}else if (entry.getKey().toString().equals("element")
	//						&& ((Map<String, Object>) entry.getValue()).get("websrc") != null
	//						&& !((Map<String, Object>) entry.getValue()).get("websrc")
	//								.equals("")) {
	//					srcList.add(((Map<String, Object>) entry.getValue()).get(
	//							"websrc").toString());
	//				}  else if (entry.getKey().toString().equals("scene")
	//						&& ((Map<String, Object>) entry.getValue()).get("backimg") != null
	//						&& !((Map<String, Object>) entry.getValue()).get("backimg")
	//								.equals("")) {
	//					srcList.add(((Map<String, Object>) entry.getValue()).get(
	//							"backimg").toString());
	//				}else{
	//				}
				}
			}
		}
	}
	/**
	 * @description 通过map字符串查找发布节目的所有id
	 * @param srcList
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public void getProgramIDByMap(Set<String> pIdList, Map<String, Object> map) {
		Set<?> set = map.entrySet();
		Iterator<?> records = set.iterator();
		while (records.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) records.next();
			if (entry.getValue().getClass().equals(ArrayList.class)) {
				List<Object> tempList = (ArrayList<Object>) entry.getValue();
				// 循环list新建节点list
				for (Object object : tempList) {
					if (object.getClass().equals(HashMap.class)) {
						if (entry.getKey().toString().equals("program")
								&& ((Map<String, Object>) object).get("id") != null
								&& !((Map<String, Object>) object).get("id").equals("")) {
							pIdList.add(((Map<String, Object>) object).get(
									"id").toString());
							getProgramIDByMap(pIdList, (Map<String, Object>) object);
						}else{
							getProgramIDByMap(pIdList, (Map<String, Object>) object);
						}
					}
				}
				// 如果值为Map
			} else if (entry.getValue().getClass().equals(HashMap.class)) {
				getProgramIDByMap(pIdList, (Map<String, Object>) entry.getValue());
			}
		}
	}
	public Map<String,Object> getSrcList(String filePath) {
		Map<String,Object> map = new HashMap<String,Object>();
		Set<String> srcList = new HashSet<String>();
		Integer plugnum = 0;
		try {
			// 解析器工厂类
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			// 解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 操作的Document对象
			Document document = builder.newDocument();
			File file = new File(filePath);
			document = builder.parse(file);
			NodeList elemList = document.getElementsByTagName("element");
			for (int i = 0; i < elemList.getLength(); i++) {
				String typeValue = elemList.item(i).getAttributes()
						.getNamedItem("type").getNodeValue();
				if (null != typeValue
						&& ElementType.html.getValue() == Integer
								.parseInt(typeValue)) {
					String tempValue = elemList.item(i).getAttributes()
							.getNamedItem("websrc").getNodeValue();
					if (null != tempValue && !"".equals(tempValue)) {
						srcList.add(tempValue);
					}
				} else {
					Object tempValue = elemList.item(i).getAttributes()
							.getNamedItem("src");
					if (null != tempValue && !"".equals(tempValue)) {
						srcList.add(elemList.item(i).getAttributes()
								.getNamedItem("src").getNodeValue());
					}
				}
			}
			NodeList tempList = document.getElementsByTagName("scene");
			for (int i = 0; i < tempList.getLength(); i++) {
				Object tempValue = tempList.item(i).getAttributes()
						.getNamedItem("backimg").getNodeValue();
				if (null != tempValue && !"".equals(tempValue)) {
					srcList.add(tempList.item(i).getAttributes()
							.getNamedItem("backimg").getNodeValue());
				}
			}
			NodeList buttonList = document.getElementsByTagName("button");
			for (int i = 0; i < buttonList.getLength(); i++) {
				String tempValue1 = buttonList.item(i).getAttributes()
					.getNamedItem("backimg").getNodeValue();
				if (null != tempValue1 && !"".equals(tempValue1)) {
					srcList.add(tempValue1);
				}
				String tempValue2 = buttonList.item(i).getAttributes()
				.getNamedItem("selectimg").getNodeValue();
				if (null != tempValue2 && !"".equals(tempValue2)) {
					srcList.add(tempValue2);
				}
			}
			Integer plugw = document.getElementsByTagName("weather").getLength();
			Integer plugt = document.getElementsByTagName("timepiece").getLength();
			Integer plugc = document.getElementsByTagName("count").getLength();
			plugnum = plugw + plugt + plugc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("pluglist", plugnum);
		map.put("Srclist", srcList);
		return map;
	}

}
