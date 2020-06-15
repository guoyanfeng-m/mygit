package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import service.config.ConfigService;
import service.terminal.TerminalStatusService;
import beans.view.TerminalMac;

@Component
public class TerminalBootAndShutDownUtil {
	@Autowired
	private TerminalStatusService terminalStatusService;
	@Autowired
	private ConfigService configService;
	
	public static boolean task = false;
	String ip;
	public static List<Map<String,Object>> openList = new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> closeList = new ArrayList<Map<String,Object>>();
	
	@SuppressWarnings("rawtypes")
	@Scheduled(fixedRate = 1000*60)
    public void execute() throws ParseException {
        List timeandmac =terminalStatusService.queryMacAndTime();
	    ip=configService.queryConfig("tfbsip");// 获取服务器IP
		if(ip==null){
		  ip=configService.queryConfig("httpip");
		}
        openList.clear();
        closeList.clear();
        try {
        	Calendar cal = Calendar.getInstance();
        	int str = cal.get(Calendar.DAY_OF_WEEK)-1;
        	String date = ""; 
        	switch (str){ 
        	case 1 : date = "一";  break;
        	case 2 : date = "二";  break;
        	case 3 : date = "三";  break;
        	case 4 : date = "四";  break;
        	case 5 : date = "五";  break;
        	case 6 : date = "六";  break;
        	case 0 : date = "日";  break;
        	}
	        for (int i = 0; i < timeandmac.size(); i++) {
	        	boolean hascheckin = false;
	        	TerminalMac temp = (TerminalMac) timeandmac.get(i);
	        	String[] days = temp.getDays().split(",");
	        	for (int j = 0; j < days.length; j++) {
					if (days[j].equals(date)) {
						hascheckin = true;
						if(checkTime(temp.getStartTime())){
							insertOpen(temp.getMac(),temp.getStartTime(),temp.getIp());
						}
						if(checkTime(temp.getEndTime())){
							insertClose(temp.getMac(),temp.getEndTime());
						}
					}
					if (hascheckin) {
						break;
					}
				}
			}
	        if(openList.size()!=0){
	        	opentask();
	        }
	        if(closeList.size()!=0){
	        	closetask();
	        }} catch (Exception e) {
				e.printStackTrace();
			}
    }
	
	@SuppressWarnings("deprecation")
	private boolean checkTime(String openstr) throws ParseException {
    	Date opendate = new Date();
    	SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    	if(openstr!=null&&!"".equals(openstr)){
    		opendate = sdf.parse(openstr);
        	Calendar c = Calendar.getInstance();
        	int hour = c.get(Calendar.HOUR_OF_DAY); 
        	int minute = c.get(Calendar.MINUTE); 
        	int now = hour*3600+minute*60;
        	int database = opendate.getHours()*3600+opendate.getMinutes()*60;
        	if(database == now){
        		return true;
        	}
    	}
        	return false;
//        	
//        		Map<String ,Object> tempmap = new HashMap<String,Object>();
//        		tempmap .put("time", database);
//        		tempmap.put("macs", macs);
//        		openList.add(tempmap);
	}
	
	@SuppressWarnings("unchecked")
	private void insertClose(String mac ,String closestr){
		boolean hasclosestr = false;
		if(null==closestr||closestr.equals("")){
			return;
		}
		for (int i = 0; i < closeList.size(); i++) {
			if(closeList.get(i).get("closestr").equals(closestr)){
				hasclosestr = true;
				List<String> macs = (List<String>) closeList.get(i).get("macs");
				macs.add(mac);
				closeList.get(i).put("macs",macs);
				break;
			}
		}
		if(!hasclosestr){
			Map<String,Object> map = new HashMap<String,Object>();
			List<String> macs = new ArrayList<String>();
			macs.add(mac);
			map.put("closestr", closestr);
			map.put("macs", macs);
			closeList.add(map);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void insertOpen(String mac ,String openstr,String ip){
		boolean hasopenstr = false;
		if(null==openstr||openstr.equals("")){
			return;
		}
		for (int i = 0; i < openList.size(); i++) {
			if(openList.get(i).get("openstr").equals(openstr)){
				hasopenstr = true;
				List<String> macs = (List<String>) openList.get(i).get("macs");
				List<String> ips = (List<String>) openList.get(i).get("ips");
				macs.add(mac);
				ips.add(ip);
				openList.get(i).put("macs",macs);
				openList.get(i).put("ips",ips);
				break;
			}
		}
		if(!hasopenstr){
			Map<String,Object> map = new HashMap<String,Object>();
			List<String> macs = new ArrayList<String>();
			List<String> ips = new ArrayList<String>();
			macs.add(mac);
			ips.add(ip);
			map.put("openstr", openstr);
			map.put("macs", macs);
			map.put("ips", ips);
			openList.add(map);
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void opentask() throws ParseException{
		Date opendate = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		for (int i = 0; i < TerminalBootAndShutDownUtil.openList.size(); i++) {
			Timer timer = new Timer();
			opendate = sdf.parse(openList.get(i).get("openstr").toString());
			Calendar c = Calendar.getInstance();
	    	int hour = c.get(Calendar.HOUR_OF_DAY); 
	    	int minute = c.get(Calendar.MINUTE); 
	    	int now = hour*3600+minute*60;
	    	int database = opendate.getHours()*3600+opendate.getMinutes()*60;
	    	List<String> macs = (List<String>) openList.get(i).get("macs");
	    	for (int j = 0; j < macs.size(); j++) {
	    		if(macs.get(j).length() > 17){
	    			macs.remove(j);
	    			j--;
	    		}
	    	}
	    	List<String> ips = (List<String>) openList.get(i).get("ips");
	    	String [] ips1 = (String[]) ips.toArray(new String[ips.size()]);
	    	String [] macs1 = (String[]) macs.toArray(new String[macs.size()]);
	    	timer.schedule(new MyTask(macs1,ips1), (database-now)*1000);
//	    		}}, (database-now)*1000);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void closetask() throws ParseException{
		Date closedate = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		for (int i = 0; i < TerminalBootAndShutDownUtil.closeList.size(); i++) {
			Timer timer = new Timer();
			closedate = sdf.parse(closeList.get(i).get("closestr").toString());
			Calendar c = Calendar.getInstance();
	    	int hour = c.get(Calendar.HOUR_OF_DAY); 
	    	int minute = c.get(Calendar.MINUTE); 
	    	int now = hour*3600+minute*60;
	    	int database = closedate.getHours()*3600+closedate.getMinutes()*60;
	    	List<String> macs = (List<String>) closeList.get(i).get("macs");
	    	for (int j = 0; j < macs.size(); j++) {
	    		if(macs.get(j).length() > 17){
	    			macs.remove(j);
	    			j--;
	    		}
	    	}
	    	String [] macs1 = (String[]) macs.toArray(new String[macs.size()]);
	    	timer.schedule(new cMyTask(macs1), (database-now)*1000);
//	    		}}, (database-now)*1000);
		}
	}
	
	public void controlTerminal(String[] Mac,String command){
		JSONObject cobject = new JSONObject();
//		PublishUtil orderUtils = new PublishUtil();
		cobject = Utils.StringtoJson(command, Mac);
		new PublishUtil().publishTask(cobject, ip,"终端监控");
//		orderUtils.publishTask(cobject, ip);
	}
	//开机新增参数终端Ip地址
	public void controlTerminalOpen(String[] Mac,String[]ips,String command){
//		PublishUtil orderUtils = new PublishUtil();
		JSONObject tempjson = new JSONObject();
		tempjson.put("command", "wakeup");
		JSONObject[] tempArray = new JSONObject[Mac.length];
		for (int i = 0; i < Mac.length; i++) {
			JSONObject tempjson1 = new JSONObject();
			tempjson1.put("mac", Mac[i]);
			tempjson1.put("ip", ips[i]);
			tempArray[i] = tempjson1;
		}
		tempjson.put("target", tempArray);
		System.out.println("utils:"+tempjson.toString());
		new PublishUtil().publishTask(tempjson, ip,"终端监控");
//		orderUtils.publishTask(tempjson, ip);
	}
	
	public class MyTask extends TimerTask{
		String[]Mac;
		String[] ip;
		public MyTask(String[] macs,String[] ips){
			Mac = macs;
			ip=ips;
		};
		public void run() {
//			for(int i=0;i<10;i++){
				controlTerminalOpen(Mac,ip,"wakeup");
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	public class cMyTask extends TimerTask{
		String[]Mac;
		public cMyTask(String[] macs){
			Mac = macs;
		};
		public void run() {
//			for(int i=0;i<10;i++){
				controlTerminal(Mac,"shutdown");
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
}
