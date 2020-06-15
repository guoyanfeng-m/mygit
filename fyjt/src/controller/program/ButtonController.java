package controller.program;

import java.util.HashMap;
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
 * 
 * @author laiyunjian
 * 
 */
@Controller
@Transactional
public class ButtonController {
	@Autowired
	private ConfigService configService;

	@RequestMapping("button/buttonStyleList.do")
	@ResponseBody
	public Map<String, Object> queryButtonList(HttpServletRequest request) {
		String path = configService.queryConfig("ftpMappingUrl");
		String buttonStyleurl = path + SystemConstant.BUTTONSTYLE_PATH;
		XmlUtil xmlUtil = new XmlUtil();
		Map<String, Object> buttonStyleMap = xmlUtil.xmlToMap(buttonStyleurl);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("buttonStylePath", SystemConstant.BUTTONSTYLE_IMG_PATH);
		map.put("buttonStyle", buttonStyleMap);
		return map;
	}
}
