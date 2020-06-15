package util.sendEmail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;




import beans.config.ConfigBean;
import service.config.ConfigService;
import service.terminal.TerminalStatusService;
import util.TTerminal;
@Component
public class DistListener{
	@Autowired
	private TerminalStatusService terminalStatusService;
	@Autowired
	private ConfigService configService;
	public DistListener(){};
	//@Scheduled(cron="30 * * * * ?")   //每30秒触发
	//@Scheduled(cron="6 8 20,22,23 * * ?")   //每天上午10点，下午18点，23点
	@Scheduled(cron="0 0 0/1 * * ?")  //每小时
	public void init() {
		Map<String, String> map = queryEmail();
		String str = queryWarning();
		String user = map.get("monitorSenderMail");
		if(null==user){
			try {
				Thread.sleep(60*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String  password =map.get("monitorSenderPass");
		String  toaddress = map.get("monitorRecieverMail");
		String emailstmp = map.get("emailstmp");
		String emailport = map.get("emailport");
		String theme = "终端磁盘使用异常";
		Timer timer = new Timer(true);
		timer.schedule(new WriteWarning(str),0,1000*60*60);  //1个小时写向warning.txt文件写一次
		timer.schedule(new SendEmail(user, password, toaddress, theme,emailstmp,emailport),0,1*60*60*1000);  //1个小时向邮箱发送一次
	}
	public Map<String, String> queryEmail() {
		Map<String, String> map = new HashMap<String, String>();
		try{
			List<ConfigBean> list =configService.selectConfig();
			for (int i = 0; i < list.size(); i++) {
				map.put(list.get(i).getConfig_key(),(String)list.get(i).getConfig_value());
			}
		}finally{
		}
		return map;
	}
	@SuppressWarnings("unchecked")
	public String  queryWarning() {
			String content = "";
			List<TTerminal> tlist = new ArrayList<TTerminal>();
			List<TTerminal> list = terminalStatusService.selectDiskUsage();
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
				 TTerminal obs = new TTerminal();
				 obs = list.get(i);
				 obs.setName((String) obs.getName());
				 obs.setDiskUsage((String) obs.getDiskUsage());
				 tlist.add(obs);
				}
			}
			if (tlist.size() > 0) {
				for (int i = 0; i < tlist.size(); i++) {
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 设置日期格式
					content = "\n" + content + tlist.get(i).toString() + "	"
							+ df.format(new Date()) + "\n";
				}
				content = "计算机：" + System.getProperty("user.name") + content;
			}
			return content;
	}
}

