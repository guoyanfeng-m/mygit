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
public class TimeController {
	@Autowired
	private ConfigService configService;

	@RequestMapping("time/queryTimeList.do")
	@ResponseBody
	public Map<String, Object> queryTimeList(
			HttpServletRequest request) {
		String path = configService.queryConfig("ftpMappingUrl");
		String folderurl = path + SystemConstant.TIME_PATH;
		File file = new File(folderurl);
		XmlUtil xmlUtil = new XmlUtil();
		List<Map<String,Object>> timeList = new ArrayList<Map<String,Object>>();
		if(!file.exists()){
			file.mkdirs();
		}else{
			File[] list = file.listFiles();
			for (File tempFile : list) {
				String realPath = tempFile.getAbsolutePath();
				Map<String, Object> timeMap = xmlUtil.xmlToMap(realPath);
				timeList.add(timeMap);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("timeImgPath",SystemConstant.TIME_IMG_PATH);
		map.put("timeList", timeList);
		return map;
	}
}
