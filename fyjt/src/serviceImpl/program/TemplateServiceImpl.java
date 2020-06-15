package serviceImpl.program;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.program.TemplateService;
import util.PageInfo;
import util.XmlUtil;
import beans.program.TemplateBean;
import beans.program.view.TemplateView;
import dao.config.ConfigDao;
import dao.program.TemplateDao;

@SuppressWarnings({"rawtypes","unchecked"})
@Service
@Transactional(readOnly=true)
public class TemplateServiceImpl implements TemplateService{
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private ConfigDao configDao;


	/**
	 * 新增模板方法（数据库、xml文件）
	 */
	@Transactional(readOnly=false)
	public boolean createTemplate(TemplateBean templateBean,Map<String,Object> dateMap) {
		//数据库操作
		templateDao.insertTemplate(templateBean);
		initTemplateMap(dateMap);
		String path = configDao.queryConfig("ftpMappingUrl");
		checkFolder(path);
		XmlUtil xmlUtil = new XmlUtil();
		Map<String,Object> xmlMap = new HashMap<String,Object>();
		xmlMap.put("program", dateMap);
		try {
			//保存路径
			String  url= path+"templateXmlFile/" + templateBean.getName() + ".xml";
			//生成xml文件
			xmlUtil.mapToXml(url, xmlMap);
		} catch (Exception e) {
			templateDao.deleteTemplateById(templateBean.getTemplate_id());
			return false;
		}
		return true;
	}
	private void initTemplateMap(Map<String, Object> dateMap) {
		dateMap.put("editProp", "");
		dateMap.put("id", "");
		dateMap.put("loopdesc", "");
		dateMap.put("loopstarttime", "");
		dateMap.put("loopstoptime", "");
		dateMap.put("looptype", "");
		dateMap.put("name", "");
		dateMap.put("startdate", "");
		dateMap.put("stopdate", "");
		dateMap.put("updatetime", "");
		removeElem(dateMap);
	}
	@Transactional(readOnly=false)
	private void removeElem(Map<String, Object> dateMap) {
		for( Object m : dateMap.keySet()){
			if(m.toString().equals("element")){
				dateMap.remove(m);
				break;
			}
			Object v = dateMap.get(m); 
			 if(v instanceof List){
				 List tempList = (List)v;
				 for (Object t : tempList) {
					 if(t instanceof Map){
						 removeElem((Map)t) ;
					 }
				}
			 }
		}
	}

	@Override
	@Transactional(readOnly=false)
	public boolean deleteTemplateById(Integer[] template_ids) {
		for (Integer integer : template_ids) {
			templateDao.deleteTemplateById(integer);
		}
		return true;
	}

	@Override
	public List<TemplateView> queryTemplate(TemplateBean templateBean,PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("name", templateBean.getName());
		map.put("template_id", templateBean.getTemplate_id());
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		List<TemplateView> tviewList = templateDao.queryTemplate(map);
		return tviewList;
	}
	private void checkFolder(String path){
		File file = null;
		try {
			file = new File(path + "templateXmlFile/");
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer queryTemplateCount(TemplateBean templateBean) {
		return templateDao.queryTemplateCount(templateBean);
	}

	@Override
	public List<TemplateBean> queryTemplateByName(String name) {
		return templateDao.queryTemplateByName(name);
	}
}
