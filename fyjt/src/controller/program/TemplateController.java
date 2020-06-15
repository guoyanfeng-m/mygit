package controller.program;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import service.program.TemplateService;
import util.MapUtil;
import util.PageInfo;
import util.XmlUtil;
import beans.program.TemplateBean;
import beans.program.view.TemplateView;
import beans.sys.SystemConstant;
import beans.user.UserBean;

/**
 * @author laiyunjian
 */
@Controller
@Transactional
public class TemplateController {
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ConfigService configService;


	@RequestMapping("template/insertTemplate.do")
	@ResponseBody
	public Map<String, Object> createTemplate(
			@RequestParam(value = "replace") boolean replace,
			@RequestParam(value = "templateJson") String templateJson,
			@RequestParam(value = "name") String name, String des,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		TemplateBean templateBean = new TemplateBean();
		Map<String, Object> templateMap = MapUtil.parseJSON2Map(templateJson);
		List<TemplateBean>tempList = templateService
				.queryTemplateByName(name);
		if(replace){
			if(tempList.size()>0){
				Integer[] ids = new Integer[tempList.size()];
				String path = configService.queryConfig("ftpMappingUrl");
				for(int i=0;i<tempList.size();i++){
					ids[i]=tempList.get(i).getTemplate_id();
					File file = new File(path+tempList.get(i).getUrl());
					if(file.isFile() && file.exists()){
						file.delete();
					}
				}
				templateService.deleteTemplateById(ids);
			}
		}else{
			if(tempList.size()>0){
				map.put("success", false);
				map.put("msg", "模板名称已经存在");
				return map;
			}
		}
		templateBean.setName(name);
		templateBean.setDescription(des);
		templateBean.setCreate_time(new Timestamp(System.currentTimeMillis()));
		templateBean.setAudit_status(SystemConstant.APPROVE_STATUS_SUCCESS);
		UserBean o = (UserBean) request.getSession().getAttribute("user");   
		templateBean.setCreator_id(o.getUser_id());
		templateBean.setDeleted(SystemConstant.ISDELETED_FALSE);
		templateBean.setResolution(templateMap.get("width") + "*"
				+ templateMap.get("height"));
		templateBean.setUrl("templateXmlFile/" + name + ".xml");
		Integer is_touch = 0;      //0：普通  1：互动
		if(templateMap.get("istouch").toString().equals("true")){
			is_touch = 1;
		}
		templateBean.setIs_touch(is_touch);
		templateService.createTemplate(templateBean, templateMap);
		map.put("success",true);
		return map;
	}

	@RequestMapping("/template/deleteTemplate.do")
	@ResponseBody
	public Map<String, Object> deleteTemplateByIds(
			@RequestParam(value = "ids[]") Integer[] ids,
			@RequestParam(value = "urls[]") String[] urls,
			HttpServletRequest request) {
		templateService.deleteTemplateById(ids);
		String path = configService.queryConfig("ftpMappingUrl");
		for (String fileName : urls) {
			File file = new File(path+fileName);
			if(file.isFile() && file.exists()){
				file.delete();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "true");
		return map;
	}

	@RequestMapping("/template/queryTemplate.do")
	@ResponseBody
	public Map<String, Object> queryTemplate(String template_id, String page,
			String rows, String name, HttpServletRequest request) {
		TemplateBean templateBean = new TemplateBean();
		int newpage = 1;
		int newrows = 0;
		if (page != null && page != "" && !page.equals("0")) {
			newpage = Integer.parseInt(page);
		}
		if (rows != null && rows != "") {
			newrows = Integer.parseInt(rows);
		}
		templateBean.setName(name);
		if (template_id != null && template_id != "") {
			templateBean.setTemplate_id(Integer.parseInt(template_id));
		}
		int count = templateService.queryTemplateCount(templateBean);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setTotal(count);
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		List<TemplateView> templateList = templateService.queryTemplate(
				templateBean, pageInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", templateList);
		map.put("total", count);
		map.put("success", "true");
		return map;
	}

	@RequestMapping("/template/getTemplate.do")
	@ResponseBody
	public Map<String, Object> getTemplate(String template_id, String url,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String path = configService.queryConfig("ftpMappingUrl");
		url = path + url;
		File file = new File(url);
		if(!file.exists()){
			map.put("success", false);
			map.put("msg", "模板文件已经丢失！");
			return map;
		}
		Map<String, Object> templateMap = new XmlUtil().xmlToMap(url);
		map.put("templateMap", templateMap);
		map.put("success", "true");
		return map;
	}

}
