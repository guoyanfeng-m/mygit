package controller.program;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.config.ConfigService;
import util.XmlUtil;
import beans.sys.SystemConstant;

/**
 * @author laiyunjian
 */
@Controller
@Transactional
public class WeatherController {
	@Autowired
	private ConfigService configService;
	
	@RequestMapping("weather/queryWeatherList.do")
	@ResponseBody
	public Map<String, Object> queryWeatherList(
			HttpServletRequest request) {
		String path = configService.queryConfig("ftpMappingUrl");
		String folderurl = path + SystemConstant.WEATHER_PATH;
		File file = new File(folderurl);
		XmlUtil xmlUtil = new XmlUtil();
		List<Map<String,Object>> weatherList = new ArrayList<Map<String,Object>>();
		if(!file.exists()){
			file.mkdirs();
		}else{
			File[] list = file.listFiles();
			for (File tempFile : list) {
				String realPath = tempFile.getAbsolutePath();
				Map<String, Object> weatherMap = xmlUtil.xmlToMap(realPath);
				weatherList.add(weatherMap);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("weatherImgPath",SystemConstant.WEATHER_IMG_PATH);
		map.put("weatherList", weatherList);
		return map;
	}
	@RequestMapping("/count/queryCountList.do")
	@ResponseBody
	public Map<String, Object> queryCountList(
			HttpServletRequest request) {
		String path = configService.queryConfig("ftpMappingUrl");
		String folderurl = path + SystemConstant.COUNT_PATH;
		File file = new File(folderurl);
		XmlUtil xmlUtil = new XmlUtil();
		List<Map<String,Object>> countList = new ArrayList<Map<String,Object>>();
		if(!file.exists()){
			file.mkdirs();
		}else{
			File[] list = file.listFiles();
			for (File tempFile : list) {
				String realPath = tempFile.getAbsolutePath();
				Map<String, Object> countMap = xmlUtil.xmlToMap(realPath);
				countList.add(countMap);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("countImgPath",SystemConstant.COUNT_IMG_PATH);
		map.put("countList", countList);
		return map;
	}
}
