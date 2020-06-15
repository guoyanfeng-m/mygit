package controller.element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import service.config.ConfigService;
import service.element.ElementService;
import service.errorLog.ErrorLogService;
import service.model.ModelService;
import service.operationlog.OperationlogService;
import service.user.UserService;
import util.ElementType;
import util.FileUtil;
import util.PageInfo;
import beans.config.ConfigBean;
import beans.element.ElementBean;
import beans.element.ElemtypeBean;
import beans.errorLog.ErrorLogBean;
import beans.sys.SystemConstant;
import beans.user.UserBean;

/**
* @ClassName: ElementController 
* @Description: TODO(素材增加操作方法) 
* @author litao 
* @date 2014-10-13 下午06:22:24 
 */
@Controller
@Transactional
public class ElementController {
	@Autowired
	private UserService userService;
	@Autowired
	private ElementService elementService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private OperationlogService  oplservice;
	@Autowired
	private ErrorLogService errorLogService;
	
	@RequestMapping("element/addElement.do")
	@ResponseBody
	public Map<String,Object> addElement(
			 @RequestParam(value="elem_name")String elem_name,
			 @RequestParam(value="elem_size")String elem_size,
			 @RequestParam(value="elem_path")String elem_path,
			 @RequestParam(value="type")Integer type,
			 @RequestParam(value="resolution")String resolution,
			 @RequestParam(value="time_length")String time_length,
			 @RequestParam(value="thumbnailUrl")String thumbnailUrl,
			 @RequestParam(value="audit_status")Integer audit_status,
			 @RequestParam(value="MD5")String MD5,
			 @RequestParam(value="description")String description,
			 @RequestParam(value="creator_id")Integer creator_id,
			 @RequestParam(value="create_time")Timestamp create_time,
			 @RequestParam(value="update_time")Timestamp update_time,
			 @RequestParam(value="deleted")Integer deleted,
			 HttpServletRequest request){
		ElementBean elementBean = new ElementBean();
		elementBean.setAudit_status(audit_status);
		elementBean.setCreate_time(create_time);
		elementBean.setCreator_id(creator_id);
		elementBean.setDeleted(deleted);
		elementBean.setDescription(description);
		elementBean.setElem_name(elem_name);
		elementBean.setElem_path(elem_path);
		elementBean.setElem_size(elem_size);
		elementBean.setMD5(MD5);
		elementBean.setResolution(resolution);
		elementBean.setThumbnailUrl(thumbnailUrl);
		elementBean.setTime_length(time_length);
		elementBean.setType(type);
		elementBean.setUpdate_time(update_time);
		elementService.addElement(elementBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "true");  
		return map;
	}
	/**
	 * 
	* @Title: queryElement 
	* @Description: TODO(素材查询方法) 
	* @param @param elem_name
	* @param @param type
	* @param @param request
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	 */
	@RequestMapping("/element/queryElement.do")
	@ResponseBody
	public Map<String,Object> queryElement(
			String page,
			String rows,
			String elem_name,
			Integer type,	
			String audit,	
			String bgimg,
			HttpServletRequest request){
		int newpage = 0;
		int newrows = 0;
		int userid = -1;
		////////////////////////////////////////////////////
		UserBean o = (UserBean) request.getSession().getAttribute("user");
		Integer creatorid=null;
		List<Integer> typeidList = new ArrayList<Integer>();
		List<Integer> userids = new ArrayList<Integer>();
		String elementPower = configService.queryConfig("elementPower");
	if (!o.getUsername().equals("admin")) {
	 if (elementPower.equals("1")) {//创建人可见
			creatorid=o.getUser_id();
		} else if (elementPower.equals("2")) {//角色共享
			userids=userService.queryUserIdsBySameRole(o.getUser_id());
		}
	}
		if(page!=null&&page!=""){
			newpage = Integer.parseInt(page);
		}
		if(page.equals("0")){
			newpage=1;
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		ElementBean elementBean = new ElementBean();
		elementBean.setElem_name(elem_name);
		elementBean.setType(type);
		elementBean.setCreator_id(userid);
	//此参数节目查询素材时用
		if(audit!=null && audit!=""){
			elementBean.setAudit_status(Integer.parseInt(audit));
		}		
		if(bgimg!=null && bgimg.equals("")){
			typeidList = elementService.queryTypeIdByIsprivate();
			
		}
		int total = elementService.queryTerminalCount(elementBean,creatorid,userids,typeidList);
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		List<ElementBean> elementBeanList = new ArrayList<ElementBean>();
		elementBeanList = elementService.queryElement(elementBean,pageInfo,creatorid,userids,typeidList);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", elementBeanList);
		map.put("total", total); //总条数
		return map;
	}
/**
 * 查询分类下的素材	
 */
	@RequestMapping("/element/queryClassifyElement.do")
	@ResponseBody	
	public Map<String,Object> queryClassifyElement(
			String page,
			String rows,
			String elem_name,
			Integer type,		
			HttpServletRequest request){
		int newpage = 0;
		int newrows = 0;
		if(page!=null&&page!=""){
			newpage = Integer.parseInt(page);
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		ElementBean elementBean = new ElementBean();
		elementBean.setElem_name(elem_name);
		elementBean.setType(type);
		int total = elementService.queryClassifyTotalCount(elementBean);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		pageInfo.setTotal(total);
		List<ElementBean> elementBeanList = new ArrayList<ElementBean>();
		//elementBeanList = elementService.queryClassifyElement(elementBean,pageInfo);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", elementBeanList);
		map.put("total", total); //总条数	
		return map;
	}
/**
 * 查询分类文件夹  	
 */
	@RequestMapping("/element/queryClassify.do")
	@ResponseBody
	public Map<String,Object> queryClassify(
			Integer parentId,
			HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		   // 获取Session     
        HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user"); 
		ElemtypeBean elemtypeBean = new ElemtypeBean();
		elemtypeBean.setCreator_id(o.getUser_id());
		elemtypeBean.setParentId(parentId);
		List<ElemtypeBean> elemtypeBeanList = elementService.queryClassify(elemtypeBean);
		map.put("rows", elemtypeBeanList);
		map.put("success", "true");
		return map;
	}

	/**
	 * 查询分类文件夹  （下拉框）	
	 */
		@RequestMapping("/element/queryClassifyName.do")
		@ResponseBody
		public JSONArray queryClassifyName(
				Integer parentId,
				HttpServletRequest request){
			   // 获取Session     
	        HttpSession session = request.getSession();     
	        // 获取Session中存储的对象     
	        UserBean o = (UserBean) session.getAttribute("user");
			ElemtypeBean elemtypeBean = new ElemtypeBean();
			elemtypeBean.setCreator_id(o.getUser_id());
			elemtypeBean.setParentId(parentId);
			List<ElemtypeBean> elemtypeBeanList = new ArrayList<ElemtypeBean>();
			elemtypeBeanList = elementService.queryClassifyName(elemtypeBean);
			JSONArray classifyName = new JSONArray(); 
			classifyName.addAll(elemtypeBeanList);
			return classifyName;
		}
	/**
	 * 素材添加到分类	
	 */
		@RequestMapping("/element/addToClassify.do")
		@ResponseBody
		public Map<String,Object> addToClassify(HttpServletRequest request){
			String[] elemID = request.getParameterValues("element_id[]");
			String[] typeID = request.getParameterValues("type_id");
			HttpSession session = request.getSession();     
	        UserBean o = (UserBean) session.getAttribute("user");   
			for(String element_id : elemID){
				Integer temp;
				temp = elementService.querySameToClassify(Integer.parseInt(element_id),Integer.parseInt(typeID[0]));
				if(temp>0){
					continue;
				}
				elementService.addToClassify(Integer.parseInt(element_id),Integer.parseInt(typeID[0]));
				List<String> elemId = new ArrayList<String>();
				elemId.add(element_id);
				List<ElementBean> ebl = elementService.FindElementIdsWithElementPath(elemId);
				oplservice.insertOperationlog(o.getUser_id().toString(), ebl.get(0).getElem_name(), SystemConstant.INSERT_OPERATION, SystemConstant.ELEM_ID);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", "true");
			return map;
		}
		/**
		 * 素材从分类移除	
		 */
			@RequestMapping("/element/subMoveFromClassify.do")
			@ResponseBody
			public Map<String,Object> subMoveFromClassify(HttpServletRequest request){
				String[] elemID = request.getParameterValues("element_id[]");
				String[] typeID = request.getParameterValues("type_id");
				for(String element_id : elemID){
					elementService.subMoveFromClassify(Integer.parseInt(element_id),Integer.parseInt(typeID[0]));
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", "true");
				return map;
			}
		/**
		 * 删除分类文件夹	
		 */
			@RequestMapping("/element/deleteClassifyById.do")
			@ResponseBody	
			public Map<String,Object> deleteClassifyById(HttpServletRequest request){
				String[] ids = request.getParameterValues("id[]");
				HttpSession session = request.getSession(); 
				UserBean o = (UserBean) session.getAttribute("user");
				for(String classifyid :  ids){
					oplservice.insertOperationlog(o.getUser_id().toString(), getClassifyNameById(Integer.parseInt(classifyid)),
							SystemConstant.DELETE_OPERATION, SystemConstant.ELEM_ID);
					String[] elemids = elementService.deleteTypeById(Integer.parseInt(classifyid));
					deleteElementById(request,elemids);
					//elementService.deleteClassifyById(Integer.parseInt(id));
				}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", "true");
			return map;
			}
	/**
	 * 数据库 记录重复条数查询
	 */
	public List<ElementBean> sameRecordTest(String path){
					List<ElementBean> elementBeanList = new ArrayList<ElementBean>();
					elementBeanList = elementService.sameRecordTest(path);
					return elementBeanList;
		}
						
	public ElementBean queryElementById(Integer elemid){
		return elementService.queryElementById(elemid);
	}
			
	/**
	 * 
	* @Title: deleteElementById 
	* @Description: TODO(素材模块删除功能) 
	* @param @param ids
	* @param @param request
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@RequestMapping("/element/deleteElementById.do")
	@ResponseBody
	public Map<String,Object> deleteElementById(HttpServletRequest request,String[] ids){
		if(ids == null)
			ids = request.getParameterValues("elem_id[]");
		ElementBean elementBean = new ElementBean();
		HttpSession session = request.getSession(); 
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取Session中存储的对象     
		UserBean o = (UserBean) session.getAttribute("user");
		for(String id : ids){
			//删除FTP文件服务器文件
			String tempurl = elementService.findLocation(Integer.parseInt(id));
			List<ElementBean> elementbeanList = sameRecordTest(tempurl);
			if(elementbeanList.size()>1){
				elementBean.setElem_id(Integer.parseInt(id));
				elementService.deleteClassifyElement(Integer.parseInt(id));
				oplservice.insertOperationlog(o.getUser_id().toString(), getElementNameById(Integer.parseInt(id)),SystemConstant.DELETE_OPERATION, SystemConstant.ELEM_ID);
				elementService.deleteElementById(elementBean);
			}else{
				if(tempurl.contains("ftpFile")){
					File file = new File(configService.queryFtpUrl("ftpMappingUrl")
							+elementService.findLocation(Integer.parseInt(id)).substring(8));
					if(file.getName().endsWith(".xps")){
						File dirFile = new File(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")));
						if(dirFile.exists() && dirFile.isDirectory()){
							FileUtil.delFolder(dirFile.getAbsolutePath());
						}
						File imgFile = new File(file.getParent()+File.separator+"img"+FileUtil.getFilePrefix(file.getName().substring(file.getName().indexOf("_")+1))+".jpg");
						if(imgFile.exists()){
							imgFile.delete();
						}
						File largeimgFile = new File(file.getParent()+File.separator+"large_img"+FileUtil.getFilePrefix(file.getName().substring(file.getName().indexOf("_")+1))+".jpg");
						if(largeimgFile.exists()){
							largeimgFile.delete();
						}
						File miniimgFile = new File(file.getParent()+File.separator+"mini_img"+FileUtil.getFilePrefix(file.getName().substring(file.getName().indexOf("_")+1))+".jpg");
						if(miniimgFile.exists()){
							miniimgFile.delete();
						}						
					}else{
						String[] extension = {".jpg",".JPG",".png",".PNG",".Png",".gif",".GIF",".bmp",".BMP",".jpeg",".JPEG",".jfif"};
						for(int i=0; i<extension.length; i++ ){
							
							File thunFile = new File(file.getParentFile()
									.getAbsolutePath()
									+ "\\img"
									+ FileUtil.getFilePrefix(file.getName()) + extension[i]);
							if (thunFile.exists()) {
								thunFile.delete();
							}
							File thunFile2 = new File(file.getParentFile()
									.getAbsolutePath()
									+ "\\mini_img"
									+ FileUtil.getFilePrefix(file.getName()) + extension[i]);
							if (thunFile2.exists()) {
								thunFile2.delete();
							}
							File thunFile3 = new File(file.getParentFile()
									.getAbsolutePath()
									+ "\\large_img"
									+ FileUtil.getFilePrefix(file.getName()) + extension[i]);
							if (thunFile3.exists()) {
								thunFile3.delete();
							}
						}
//					File pngthunFile = new File(file.getParentFile()
//							.getAbsolutePath()
//							+ "\\img"
//							+ FileUtil.getFilePrefix(file.getName()) + ".png");
//					if (pngthunFile.exists()) {
//						pngthunFile.delete();
//					}
//
//					File gifFile = new File(file.getParentFile()
//							.getAbsolutePath()
//							+ "\\img"
//							+ FileUtil.getFilePrefix(file.getName()) + ".gif");
//					if (gifFile.exists()) {
//						gifFile.delete();
//					}
						
					}
					file.delete();
				}else{
					File webFile = new File(configService.queryFtpUrl("ftpMappingUrl")
							+elementService.findWebLocation(Integer.parseInt(id)).substring(8));
					if(!webFile.getName().equals("fail.png") && !webFile.getName().equals("stream.png") && !webFile.getName().equals("audio.png") && !webFile.getName().equals("text.jpg")){
						webFile.delete();
					}
					
				}
				//删除数据库记录
				elementBean.setElem_id(Integer.parseInt(id));
				elementService.deleteClassifyElement(Integer.parseInt(id));
				oplservice.insertOperationlog(o.getUser_id().toString(), getElementNameById(Integer.parseInt(id)),SystemConstant.DELETE_OPERATION, SystemConstant.ELEM_ID);
				elementService.deleteElementById(elementBean);
			}
			}
		map.put("success", "true");
		return map;
	}
	/**
	 * 
	* @Title: getElementNameById 
	* @Description: TODO(查询素材的的name) 
	* @param @param id
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/element/getElementNameById.do")
	@ResponseBody
	public String getElementNameById(Integer id){
		String eleName="";
		eleName = elementService.getElementNameById(id);
		return eleName;
	}
	/**
	 * 
	* @Title: findLocation 
	* @Description: TODO(查询素材的path) 
	* @param @param id
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/element/findLocation.do")
	@ResponseBody	
	public String findLocation(Integer id){
		String filePath = "";
		filePath = elementService.findLocation(id);
		return filePath;
	}
	/**
	 * 
	* @Title: getElementNameById 
	* @Description: TODO(查询素材的的name) 
	* @param @param id
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/element/getClassifyNameById.do")
	@ResponseBody
	public String getClassifyNameById(Integer id){
		String claName=""; 
		claName = elementService.getClassifyNameById(id);
		return claName;
	}
	/**
	 * 
	* @Title: findWebLocation 
	* @Description: TODO(查询网页素材的thumburlpath) 
	* @param @param id
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/element/findWebLocation.do")
	@ResponseBody	
	public String findWebLocation(Integer id){
		String webthumbUrl = "";
		webthumbUrl = elementService.findWebLocation(id);
		return webthumbUrl;
	}

	
	@RequestMapping("/element/findFtpLocation.do")
	@ResponseBody	
	public String findFtpLocation(){
		String location = "";
		location = elementService.findFtpLocation();
		return location;
	}	
	@RequestMapping("/element/queryCreatorById.do")
	@ResponseBody	
	public String queryCreatorById(
			String user_id,
			HttpServletRequest request){	
		String createName = "";						
		createName = elementService.queryCreatorById(user_id);
		JSONObject json = new JSONObject();
		json.put("msg", createName);
		return json.toString();
	}
	/**
	 * 
	* @Title: findElementbeanid 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param elementbean
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	
	@RequestMapping("/element/findElementbeanid.do")
	@ResponseBody	
	public Integer findElementbeanid(ElementBean elementbean){
		Integer beanid;
		beanid = elementService.findElementbeanid(elementbean);
		return beanid;
	}	
/**
 * 修改分类名称	
 */
	@RequestMapping("/element/editClassifyName.do")
	@ResponseBody		
	public Map<String,Object> editClassifyName(
			String type_id,
			String type_name,
			String folderpor,
			String parentId,
			HttpServletRequest request){
		ElemtypeBean typeBean = new ElemtypeBean();
		Map<String,Object> map = new HashMap<String, Object>();
		   // 获取Session     
        HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user"); 
		List<ElemtypeBean> typebeanList = classifySameNameTest(type_name);
		if(typebeanList.size()>0){
			for(int i=0; i<typebeanList.size(); i++){
				if(!type_id.equals(typebeanList.get(i).getType_id().toString())
						&&parentId.equals(typebeanList.get(i).getParentId().toString())){
					map.put("success",false);
					map.put("msg", "该分类名已存在!");
					return map;
				}
			}
		}
		if(type_id != null && type_id != ""){
			typeBean.setType_id(Integer.parseInt(type_id));
		}
		typeBean.setType_name(type_name);
		typeBean.setIs_private(Integer.parseInt(folderpor));
		typeBean.setCreator_id(o.getCreator_id());
		elementService.editClassifyName(typeBean);
		oplservice.insertOperationlog(o.getUser_id().toString(), type_name, SystemConstant.UPDATE_OPERATION, SystemConstant.ELEM_ID);
		map.put("success", "true");
		return map;
	}
	
	private List<ElemtypeBean> classifySameNameTest(String typeName) {
		List<ElemtypeBean> typebeanList = new ArrayList<ElemtypeBean>();
		typebeanList = elementService.classifySameNameTest(typeName);
		return typebeanList;
	}
/**
 * 		
* @Title: updateElementById 
* @Description: TODO(素材修改方法) 
* @param @param elem_id
* @param @param elem_name
* @param @param description
* @param @param request
* @param @return    设定文件 
* @return Map<String,Object>    返回类型 
* @throws
 */
	@RequestMapping("/element/updateElementById.do")
	@ResponseBody
	public Map<String,Object> updateElementById(
			String elem_id,
			String elem_name,
			String description,
			String elem_path,
			HttpServletRequest request){
		ElementBean elementBean = new ElementBean();
		HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user");   
		Map<String,Object> map = new HashMap<String, Object>();
		//重名验证。已去掉。
//		List<ElementBean> beanList =   sameNameTest(elem_name);
//		if(beanList.size()>0&&!elem_id.equals(beanList.get(0).getElem_id().toString())){
//			map.put("success",false);
//			map.put("msg", "素材名已存在!");
//			return map;
//		}
		if(elem_id != null && elem_id != ""){
			elementBean.setElem_id(Integer.parseInt(elem_id));
		}
		if(elem_name != null && elem_name != ""){
			elementBean.setElem_name(elem_name);
		}
		if(elem_path != null && elem_path != ""){
			elementBean.setElem_path(elem_path);
		}
		elementBean.setAudit_status(modelService
				.queryModuleAudit(SystemConstant.ELEM_ID));
		elementBean.setUpdate_time(new java.sql.Timestamp(System
						.currentTimeMillis()));
		elementBean.setDescription(description);
		oplservice.insertOperationlog(o.getUser_id().toString(), elem_name, SystemConstant.UPDATE_OPERATION, SystemConstant.ELEM_ID);
	   elementService.updateElementById(elementBean);
		map.put("success", "true");
		return map;
	}	
	
	
	@RequestMapping("/element/exists.action")
	@ResponseBody
	public Integer exists(String name,String md5,HttpServletRequest request) throws UnsupportedEncodingException {
		int existsCode;
		ElementBean elementBean = new ElementBean();
		elementBean.setMD5(md5);
		List<ElementBean> beanList = new ArrayList<ElementBean>();
		beanList = elementService.exists(elementBean);
		if(beanList.size() == 0){
			existsCode=0;
		}else{
			existsCode = 1;
			for(int i=0;i<beanList.size();i++){
				if(name.equals(beanList.get(i).getElem_name())){
					existsCode = 2;
					break;
				}
			}
		}
		if(existsCode==2){
			elementBean.setElem_name(name);
			elementBean.setUpdate_time(new java.sql.Timestamp(System.currentTimeMillis()));
			elementService.updateElementByNameAndMD5(elementBean);
		}
		System.out.println("------------------"+existsCode);
		return existsCode;
	}
	@RequestMapping("/element/insert.action")
	@ResponseBody
	public void insert(String name,String md5,Integer creater,HttpServletRequest request){
		String  type = request.getParameter("class");
		ElementBean elementBean = new ElementBean();
		elementBean.setElem_name(name);
		elementBean.setMD5(md5);
		elementBean.setCreator_id(creater);
		List<ElementBean> samebeanList = new ArrayList<ElementBean>();
		samebeanList = elementService.findEleByMd5AndCreatorID(elementBean);
		if(samebeanList.size()==0){
			List<ElementBean>  beanList =  new ArrayList<ElementBean>();
			beanList = elementService.findEleByMD5(md5);
			ElementBean newelementBean = new ElementBean();
			newelementBean.setAudit_status(modelService.queryModuleAudit(SystemConstant.ELEM_ID));
			newelementBean.setCreate_time(new java.sql.Timestamp(System.currentTimeMillis()));
			newelementBean.setCreator_id(creater);
			newelementBean.setDeleted(beanList.get(0).getDeleted());
			newelementBean.setDescription(beanList.get(0).getDescription());
			newelementBean.setElem_name(name);
			newelementBean.setElem_path(beanList.get(0).getElem_path());
			newelementBean.setElem_size(beanList.get(0).getElem_size());
			newelementBean.setMD5(beanList.get(0).getMD5());
			newelementBean.setResolution(beanList.get(0).getResolution());
			newelementBean.setThumbnailUrl(beanList.get(0).getThumbnailUrl());
			newelementBean.setTime_length(beanList.get(0).getTime_length());
			newelementBean.setType(beanList.get(0).getType());
			newelementBean.setUpdate_time(new java.sql.Timestamp(System.currentTimeMillis()));
			elementService.addElement(newelementBean);
			insertClassifyType(newelementBean.getElem_id(),beanList.get(0).getType());
			if(type!=null && type!=""){
				insertClassifyType(newelementBean.getElem_id(),Integer.parseInt(type));
			}	
		}else {
			if(type!=null && type!=""){
				Integer count  =  elementService.queryCount(samebeanList.get(0).getElem_id(),Integer.parseInt(type));
				if(count==0){
					insertClassifyType(samebeanList.get(0).getElem_id(),Integer.parseInt(type));
				}
			}	
		}
		
	}
	
	
	
	@RequestMapping("/element/auditElementById.do")
	@ResponseBody
	public Map<String,Object> auditElementById(
			String audit_status,
			HttpServletRequest request){
		String[] ids = request.getParameterValues("elem_id[]");
		HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user");   
		ElementBean elementBean = new ElementBean();
		for(String elem_id : ids){
			if(elem_id != null && elem_id != ""){
				elementBean.setElem_id(Integer.parseInt(elem_id));
			}
			elementBean.setAudit_status(Integer.parseInt(audit_status));
			oplservice.insertOperationlog(o.getUser_id().toString(), getElementNameById(Integer.parseInt(elem_id)), SystemConstant.AUDTI_OPERATION,SystemConstant.ELEM_ID);
			elementService.auditElementById(elementBean);
		}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("success", "true");
			return map;
	}
	@RequestMapping("/element/queryFtpHttpMessage.do")
	@ResponseBody
	public Map<String,Object> queryFtpHttpMessage(){
		ConfigBean configbean = new ConfigBean();
		List<ConfigBean> configBeanList = new ArrayList<ConfigBean>();
		configBeanList = elementService.queryFtpHttpMessage(configbean);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("configBeanList", configBeanList);   
		return map;
	}
	
	@RequestMapping("/element/insertHtml.do")
	@ResponseBody
	public Map<String,Object> insertHtml(
			 @RequestParam(value="elem_name")String elem_name,
			 @RequestParam(value="elem_path")String elem_path,
			 @RequestParam(value="classifytype")String classifytype,
			 HttpServletRequest request	){	
		Map<String,Object> map = new HashMap<String, Object>();
        // 获取Session     
		List<ElementBean> beanList =   sameNameTest(elem_name);
		if(beanList.size()>0){
			map.put("success",false);
			map.put("msg", "网页名已存在!");
			return map;
		}
		HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user");   
		ElementBean elementbean = new ElementBean();
		elementbean.setElem_name(elem_name);
		elementbean.setElem_path(elem_path);
		elementbean.setType(ElementType.html.getValue());
		elementbean.setCreator_id(o.getUser_id());
		elementbean.setDeleted(SystemConstant.ISDELETED_FALSE);
		elementbean.setAudit_status(modelService.queryModuleAudit(SystemConstant.ELEM_ID));
		elementbean.setCreate_time(new java.sql.Timestamp(System
				.currentTimeMillis()));
		oplservice.insertOperationlog(o.getUser_id().toString(), elem_name, SystemConstant.INSERT_OPERATION, SystemConstant.ELEM_ID);
		elementService.saveHtml(elementbean);
		//监听程序中处理
		//insertClassifyType(elementbean.getElem_id(),ElementType.html.getValue());
		File f = new File(findFtpLocation()+"saveHtml&_^"+elem_name+"&_@"+findElementbeanid(elementbean)+"&_@"+classifytype);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
//		ErrorLogBean elb1 = new ErrorLogBean();
//		elb1.setClass_name(ElementController.class.toString());
//		elb1.setException_type("IOException");
//		elb1.setException_reason("网页素材:"+elem_name+"上传失败");
//		elb1.setFunction_name("素材上传");
//		elb1.setModule_id(4);
//		errorLogService.insertErrorLog(elb1);
		try {
			f.createNewFile();
		} catch (IOException e) {
			if(configService.queryConfig("islisten").equals("1")){
				ErrorLogBean elb = new ErrorLogBean();
				elb.setClass_name(ElementController.class.toString());
				elb.setException_type("IOException");
				elb.setException_reason("网页素材:"+elem_name+"创建失败");
				elb.setFunction_name("素材上传");
				elb.setModule_name("素材管理");
				elb.setHappen_time(new Timestamp(System.currentTimeMillis()));
				errorLogService.insertErrorLog(elb);
			}
			e.printStackTrace();
		}
		map.put("success", "true");
		return map;
	
	}

	@RequestMapping("/element/insertStream.do")
	@ResponseBody
	public Map<String,Object> insertStream(
			 @RequestParam(value="elem_name")String elem_name,
			 @RequestParam(value="elem_path")String elem_path,
			 @RequestParam(value="classify")String classify,
			 HttpServletRequest request	){	
		Map<String,Object> map = new HashMap<String, Object>();
        // 获取Session     
		List<ElementBean> beanList =   sameNameTest(elem_name);
		if(beanList.size()>0){
			map.put("success",false);
			map.put("msg", "素材名已存在!");
			return map;
		}

		HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user");   
		ElementBean elementbean = new ElementBean();
		elementbean.setElem_name(elem_name);
		elementbean.setElem_path(elem_path);
		elementbean.setType(ElementType.stream.getValue());
		elementbean.setResolution("");
		elementbean.setCreator_id(o.getUser_id());
		elementbean.setDeleted(SystemConstant.ISDELETED_FALSE);
		elementbean.setThumbnailUrl("ftpFile\\processed\\stream.png");
		elementbean.setAudit_status(modelService.queryModuleAudit(SystemConstant.ELEM_ID));
		elementbean.setCreate_time(new java.sql.Timestamp(System
				.currentTimeMillis()));
		elementbean.setUpdate_time(new java.sql.Timestamp(System
				.currentTimeMillis()));
		oplservice.insertOperationlog(o.getUser_id().toString(), elem_name, SystemConstant.INSERT_OPERATION, SystemConstant.ELEM_ID);
		elementService.saveStream(elementbean);
		insertClassifyType(elementbean.getElem_id(),ElementType.stream.getValue());
		if(Integer.parseInt(classify)>10){
			insertClassifyType(elementbean.getElem_id(),Integer.parseInt(classify));
		}
		map.put("success", "true");
		return map;
	
	}
		
	
	@RequestMapping("/element/insertText.do")
	@ResponseBody
	public Map<String,Object> insertText(
			 Integer id,
			 @RequestParam(value="resolution")String resolution,
			 @RequestParam(value="cen")String cen,
			 @RequestParam(value="textName")String textName,
			 @RequestParam(value="textAreaContent")String textAreaContent,
			 @RequestParam(value="textSize")String textSize,
			 @RequestParam(value="textFont")String textFont,
			 @RequestParam(value="textcolor")String textcolor,
			 @RequestParam(value="textareacolor")String textareacolor,
			 @RequestParam(value="classify")String classify,
			 @RequestParam(value="direction")String direction,
			 @RequestParam(value="speed")String speed,
			 @RequestParam(value="bjtm")String bjtm,
			 HttpServletRequest request	){	
        // 获取Session     
        HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user");   
		ElementBean elementbean = new ElementBean();
		elementbean.setElem_id(id);
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<ElementBean> beanList =   sameNameTest(textName);
		if(beanList.size()>0&&!beanList.get(0).getElem_id().equals(id)){
			map.put("success",false);
			map.put("msg", "该文本已存在!");
			return map;
		}
		if(null!=id ){
			if(id != -1){
				String oldPath = findFtpLocation()+elementService.findLocation(id).substring(8);
				File oldFile = new File(oldPath);
				oldFile.delete();
			}
		}
		File file = new File(findFtpLocation()+"processed\\"+ textName +".xml");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}		
		elementbean.setMD5(Double.toString(new Double(Math.random() * 10000)
						.longValue() + System.currentTimeMillis()));
		elementbean.setCreate_time(new java.sql.Timestamp(System
						.currentTimeMillis()));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Element tempElem  = doc.createElement("text");
		tempElem.setAttribute("content",textAreaContent );
		tempElem.setAttribute("font", textFont);
		if(cen.equals("0")||cen.equals("1"))
			tempElem.setAttribute("center", cen);
		else if(cen.equals("01")){
			cen = "2";
			tempElem.setAttribute("center", cen);
		}else{
			tempElem.setAttribute("center", "");
		}	
		tempElem.setAttribute("size", textSize);
		if (textcolor.equals("") || textcolor == null) {
			textcolor = "000000";
		}
		if (textareacolor.equals("") || textareacolor == null) {
			textareacolor = "FFFFFF";
		}
		if (!textcolor.startsWith("#"))
			textcolor = "#" + textcolor;
		if (!textareacolor.startsWith("#"))
			textareacolor = "#" + textareacolor;
		tempElem.setAttribute("fgcolor", textcolor);
		tempElem.setAttribute("bgcolor", textareacolor);
		tempElem.setAttribute("direction", direction);
		tempElem.setAttribute("speed", speed);
		if(!"".equals(bjtm)){
			tempElem.setAttribute("transparent", bjtm);
		}
		doc.appendChild(tempElem);
		DOMSource source = new DOMSource(doc);
		StreamResult res = null;
		try {
			res = new StreamResult(new FileOutputStream(file));
			TransformerFactory.newInstance().newTransformer().transform(source, res);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}finally {
			try {
				res.getOutputStream().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				res.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		elementbean.setElem_center(cen);
		elementbean.setResolution(resolution);
		elementbean.setElem_name(textName);
		elementbean.setElem_path("ftpFile\\processed\\" + textName + ".xml");
		elementbean.setType(ElementType.text.getValue());
		elementbean.setDeleted(SystemConstant.ISDELETED_FALSE);
		elementbean.setThumbnailUrl("ftpFile\\processed\\text.jpg");
		elementbean.setAudit_status(modelService.queryModuleAudit(SystemConstant.ELEM_ID));
		elementbean.setCreator_id(o.getUser_id());
		elementbean.setUpdate_time(new java.sql.Timestamp(System
				.currentTimeMillis()));
		if(null!=elementbean.getElem_id()&& id != -1){
				oplservice.insertOperationlog(o.getUser_id().toString(), textName, SystemConstant.UPDATE_OPERATION, SystemConstant.ELEM_ID);
				elementService.updateElementById(elementbean);
		}else{
			oplservice.insertOperationlog(o.getUser_id().toString(), textName, SystemConstant.INSERT_OPERATION, SystemConstant.ELEM_ID);
			elementService.insertText(elementbean);
			insertClassifyType(elementbean.getElem_id(),ElementType.text.getValue());
			if(Integer.parseInt(classify)>10){
				insertClassifyType(elementbean.getElem_id(),Integer.parseInt(classify));
			}
		}
		map.put("success", "true");
		return map;
	
	}
	private void insertClassifyType(Integer elemId, int type) {
		elementService.insertClassifyType(elemId,type);
	}
	/**
	 * 
	* @Title: getTextInfo 
	* @Description: TODO(修改文本文件回显) 
	* @param @param elem_id
	* @param @param request
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */

	@RequestMapping("/element/getTextInfo.do")
	@ResponseBody	
	public  Map<String,Object> getTextInfo(
		 @RequestParam(value="elem_id")String elem_id,
		 HttpServletRequest request	){
	    Map<String, Object> textMap = new HashMap<String, Object>();
	    String path = findLocation(Integer.parseInt(elem_id));
		File f = new File(findFtpLocation()+File.separator+path.substring(8));
		if(!f.exists()){
			textMap.put("success","文件不存在!");
			return textMap;
		}
		File tempFile = new File(f.getParent() + File.separator
				+ File.separator + f.getName());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(tempFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NamedNodeMap attrs = doc.getElementsByTagName("text").item(0)
		.getAttributes();
		textMap.put("title", f.getName().substring(0, f.getName().lastIndexOf(".")));
		textMap.put("content", attrs.getNamedItem("content").getNodeValue());
		textMap.put("font", attrs.getNamedItem("font").getNodeValue());
		textMap.put("size", attrs.getNamedItem("size").getNodeValue());
		textMap.put("fgcolor", attrs.getNamedItem("fgcolor").getNodeValue());
		textMap.put("bgcolor", attrs.getNamedItem("bgcolor").getNodeValue());
		textMap.put("direction", attrs.getNamedItem("direction").getNodeValue());
		textMap.put("speed", attrs.getNamedItem("speed").getNodeValue());
		textMap.put("center", attrs.getNamedItem("center").getNodeValue());
		ElementBean elem = queryElementById(Integer.parseInt(elem_id));
		textMap.put("resolution",elem.getResolution());
		if(attrs.getNamedItem("transparent")!=null){
			textMap.put("bjtm", attrs.getNamedItem("transparent").getNodeValue());
		}else{
			textMap.put("bjtm", "");
		}
		textMap.put("success", "true");
		return textMap;
	}
	
	@RequestMapping("/element/downLoadMmstools.do")
	@ResponseBody	
	public ModelAndView download(HttpServletRequest request,  
            HttpServletResponse response) throws Exception {  
  
        String storeName = "mmstools.exe";  
        String realName = "mmstools.exe";  
        String contentType = "application/octet-stream";  
  
        ElementUtil.download(request, response, storeName, contentType,  
                realName);  
  
        return null;  
    }  
	
	/**
	 * 
	* @Title: sameNameTest 
	* @Description: TODO(重名检测) 
	* @param @param eleName
	* @param @return    设定文件 
	* @return List<ElementBean>    返回类型 
	* @throws
	 */
	public List<ElementBean> sameNameTest(String eleName){
		List<ElementBean> elementBeanList = new ArrayList<ElementBean>();
		elementBeanList = elementService.sameNameTest(eleName);
		return elementBeanList;
	}
	
	
	@RequestMapping("/element/insertClassify.do")
	@ResponseBody
	public Map<String,Object> insertClassify(
			@RequestParam(value="type_name")String type_name,
			@RequestParam(value="parentId")String parentId,
			@RequestParam(value="folderpor")String folderpor,
			 HttpServletRequest request	){
		Map<String,Object> map = new HashMap<String, Object>();	
		   // 获取Session     
        HttpSession session = request.getSession();     
        // 获取Session中存储的对象     
        UserBean o = (UserBean) session.getAttribute("user");   
		List<ElemtypeBean> typeBeanList = new ArrayList<ElemtypeBean>();
		typeBeanList = classifySameNameTest(type_name);
		if(typeBeanList.size()>0){
			for(int i=0; i<typeBeanList.size(); i++){
				if(parentId.equals(typeBeanList.get(i).getParentId().toString())){
					map.put("success",false);
					map.put("msg", "该分类名已存在!");
					return map;
				}
			}
		}
		elementService.insertClassify(type_name,Integer.parseInt(parentId),o.getUser_id(),Integer.parseInt(folderpor));
		oplservice.insertOperationlog(o.getUser_id().toString(), type_name, SystemConstant.INSERT_OPERATION, SystemConstant.ELEM_ID);
		map.put("success", "true");
		return map;
	}
	/**
	 * 查询分类文件夹所属类别  	
	 */
		@RequestMapping("/element/queryClassifyByTypeID.do")
		@ResponseBody
		public Map<String,Object> queryClassifyByTypeID(
				Integer typeValue,
				HttpServletRequest request){
			Map<String,Object> map = new HashMap<String, Object>();
			Integer parentid = elementService.queryClassifyByTypeID(typeValue);
			map.put("parentid", parentid);
			map.put("success", "true");
			return map;
		}

}
