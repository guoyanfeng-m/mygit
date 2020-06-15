package controller.program;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author laiyunjian
 * 
 */
@Controller
@Transactional
public class ProgramEditStatusController {
	@RequestMapping("proStatus/getStatus.do")
	@ResponseBody
	public Map<String, Object> getStatus(
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("editProgram", request.getSession().getAttribute("editProgram"));
		return map;
	}
	@RequestMapping("proStatus/setStatus.do")
	@ResponseBody
	public Map<String, Object> setStatus(boolean editProgram,
			HttpServletRequest request) {
		request.getSession().setAttribute("editProgram", editProgram);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
}
