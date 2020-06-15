package controller.pad;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.terminal.TerminalService;
import util.PublishUtil;
import beans.terminal.TerminalViewBean;

@Controller
public class PadController {
	@Autowired
	private TerminalService terminalService;
	
	@RequestMapping("/pad/padController")
	@ResponseBody
	public String medicalChannel(String json,
			String[] mac,
			String Ip,
			HttpServletRequest request){
		
		PublishUtil pub = new PublishUtil();
		String chennel = "NO";
			try {
				JSONObject jsons = new JSONObject();
				if(Ip==null||Ip==""){
					Ip="127.0.0.1";
				}
				
				if(mac!=null&&mac.length>0){
					jsons.put("target", mac);
				}else {
					List<TerminalViewBean> beanList = terminalService.queryTerminalByTerminalIds(null);
					String[] padMAC = new String[beanList.size()];
					for(int i=0;i<beanList.size();i++){
						padMAC[i] = beanList.get(i).getMac();
						
					}
					jsons.put("target", padMAC);
				}
				jsons.put("command", "sendCommand");
				jsons.put("xml", json);
				if(pub.publishTask(jsons, "127.0.0.1", "医疗通道")){
					chennel="OK";
				}
			} catch (Exception e) {
				chennel="NO";
			}
		return chennel;
	}
}
