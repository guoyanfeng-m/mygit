package serviceImpl.terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.terminal.TerminalStatusService;
import util.CreateTree;
import util.PageInfo;
import util.PublishUtil;
import util.XmlUtil;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalCloseTime;
import beans.terminal.TerminalDownloadTime;
import beans.terminal.TerminalGroupBean;
import beans.terminal.TerminalStatusBean;
import beans.terminal.TerminalStatusViewBean;
import beans.view.TerminalMac;
import dao.config.ConfigDao;
import dao.terminal.TerminalDao;
import dao.terminal.TerminalStatusDao;
import dao.user.UserDao;

@SuppressWarnings({"rawtypes","unchecked","unused"})
@Service
@Transactional(readOnly=true)
public class TerminalStatusServiceImpl implements TerminalStatusService{
	private static String serverIp = null;
	@Autowired
	private TerminalStatusDao terminalStatusDao;
	@Autowired
	private TerminalDao terminalDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ConfigDao configDao;

	@Override
	@Transactional(readOnly=false)
	public void insertTerminalStatus(TerminalStatusBean terminalStatusBean) {
		terminalStatusDao.insertTerminalStatus(terminalStatusBean);
	}

	@Override
	public List<TerminalStatusViewBean> queryTerminalStatus(
			List<Integer> terminalId) {
		return terminalStatusDao.queryTerminalStatus(terminalId);
	}

	@Override
	public List<TerminalStatusViewBean> queryTerminalStatusById(Integer[] terminal_id) {
			return terminalStatusDao.queryTerminalStatusById(terminal_id);
		
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteTerminalStatusByTerminalId(TerminalBean terminalBean) {
		terminalStatusDao.deleteTerminalStatusByTerminalId(terminalBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateTerminalStatusByTerminalId(
			TerminalStatusBean terminalStatusBean) {
		terminalStatusDao.updateTerminalStatusByTerminalId(terminalStatusBean);
	}

	@Override
	public List<TerminalStatusViewBean> queryTerminalStatusByUserId(List<String> userlist,
			PageInfo pageInfo, Integer filter, Integer system) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("list", rolelist);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		map.put("filter", filter);
		map.put("system", system);
		return terminalStatusDao.queryTerminalStatusByUserId(map);
	}

	@Override
	public Integer queryTerminalStatusByUserIdCount(List<String> roleId, Integer filter, Integer system) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : roleId){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		return terminalStatusDao.queryTerminalStatusByUserIdCount(rolelist,filter,system);
	}

	@Override
	public List<TerminalStatusViewBean> queryTerminalStatusByGroupId(Integer terminalGroupId,List<String> role_id,PageInfo pageInfo, Integer filter, Integer system) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : role_id){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupId = new CreateTree().getGroup(tree, terminalGroupId);
		return terminalStatusDao.queryTerminalStatusByGroupId(groupId, rolelist, pageInfo.getStart(), pageInfo.getEnd(),filter,system);
	}

	@Override
	public Integer queryTerminalStatusByGroupIdCount(Integer terminalGroupId,List<String> role_id,Integer filter, Integer system) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : role_id){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupId = new CreateTree().getGroup(tree, terminalGroupId);
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("list", groupId);
		map.put("role_id", role_id);
		return terminalStatusDao.queryTerminalStatusByGroupIdCount(groupId, rolelist,filter,system);
	}

	
	@Override
	@Transactional(readOnly=false)
	public List selectDiskUsage() {
		return terminalStatusDao.selectDiskUsage();
	}

	@Override
	@Transactional(readOnly=false)
	public void deletecloseTimeByTerminalId(Integer terminalid) {
		String[] terminalids = new String[1];
		terminalids[0] = terminalid.toString();
		terminalStatusDao.deletecloseTimeByTerminalId(terminalids);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertcloseTimeByTerminalId(TerminalCloseTime terminalCloseTime) {
		
	}

	@Override
	public List<TerminalCloseTime> querycloseTimeByTerminalId(String[] terminalId) {
		return terminalStatusDao.querycloseTimeByTerminalId(terminalId);
	}

	@Override
	@Transactional(readOnly=false)
	public void updatecloseTimeByTerminalId(List<Map<String, Object>> dataMap, String[] delterminal) {
		Integer result = terminalStatusDao.querycloseTimeCountByTerminalId(delterminal);
		if(result > 0) {
			terminalStatusDao.deletecloseTimeByTerminalId(delterminal);
		}
		if (dataMap.size() != 0)
			terminalStatusDao.insertcloseTimeByTerminalId(dataMap);
	}

	@Override
	public List<TerminalMac> queryMacAndTime() {
		return terminalStatusDao.queryMacAndTime();
	}

	@Override
    @Transactional(readOnly=false)
    public void updatecloseTimeXml(List<Map<String, Object>> timeAndMacList, String[] delterminalmac) {
    	String ip = getServerIp();
    	try {
	    	for (int i = 0; i < timeAndMacList.size(); i++) {
	    		Map<String, Object> xmlMap = new HashMap<String, Object>();
	    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				Map<String, Object> xmlMap1 = new HashMap<String, Object>();
				xmlMap1.put("type", "timedPowerOnOff");
				list.add(xmlMap1);
				xmlMap.put("command", list);
	    		String mac = timeAndMacList.get(i).get("mac").toString();
	    		String[] macs = new String[1];
	    		macs[0] = mac;
	    		List<Map<String, Object>> timeMapList = (List<Map<String, Object>>) timeAndMacList.get(i).get("time");
	    		Map<String, Object> twinMap = new HashMap<String, Object>();
	    		String terminalname = timeAndMacList.get(i).get("terminalname").toString();
	    		xmlMap.put("systime", timeMapList);
	    		twinMap.put("systimes", xmlMap);
			    XmlUtil xmlUtil = new XmlUtil();
			    String comentStr = xmlUtil.mapToString(twinMap);
				System.out.println(comentStr);
				JSONObject json = new JSONObject();
				json.put("command", "sendCommand");
				json.put("target", macs);
				json.put("xml", comentStr);
				PublishUtil publishUtil = new PublishUtil();
//				publishUtil.publishTask(json, ip);
				new PublishUtil().publishTask(json, ip,"终端监控");
			}
	    	if(null != timeAndMacList && timeAndMacList.size() == 0){
	    		Map<String, Object> xmlMap = new HashMap<String, Object>();
	    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				Map<String, Object> xmlMap1 = new HashMap<String, Object>();
				xmlMap1.put("type", "timedPowerOnOff");
				list.add(xmlMap1);
				xmlMap.put("command", list);
	    		Map<String, Object> twinMap = new HashMap<String, Object>();
	    		xmlMap.put("systime",new ArrayList<String>());
	    		twinMap.put("systimes", xmlMap);
			    XmlUtil xmlUtil = new XmlUtil();
			    String comentStr = xmlUtil.mapToString(twinMap);
				System.out.println(comentStr);
				JSONObject json = new JSONObject();
				json.put("command", "sendCommand");
				json.put("target", delterminalmac);
				json.put("xml", comentStr);
				PublishUtil publishUtil = new PublishUtil();
				new PublishUtil().publishTask(json, ip,"终端监控");
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
	private String getServerIp() {
		if (null == TerminalStatusServiceImpl.serverIp) {
			String ip=configDao.queryConfig("tfbsip");// 获取服务器IP
			if(ip==null){
			   ip=configDao.queryConfig("httpip");
			}
			TerminalStatusServiceImpl.serverIp = ip;
		}
		return TerminalStatusServiceImpl.serverIp;
	}
	
	public List<TerminalDownloadTime> queryTerminalTimingSet(String[] terminalId){
		return terminalStatusDao.queryTerminalTimingSet(terminalId);
	}
	
	@Override
	public void updateDownloadByTerminalId(List<Map<String, Object>> dataMap, String[] delterminal){
		String[] temp = new String[dataMap.size()];
		for (int i = 0; i < dataMap.size(); i++) {
			temp[i] = dataMap.get(i).get("terminalId").toString();
		}
		Integer result = 0;
		if(temp.length == 0){
			result = terminalStatusDao.queryDownloadCountByTerminalId(delterminal);
			if(result > 0) {
				terminalStatusDao.deleteDownloadByTerminalId(delterminal);
			}
		}else{
			result = terminalStatusDao.queryDownloadCountByTerminalId(temp);
			if(result > 0) {
				terminalStatusDao.deleteDownloadByTerminalId(temp);
			}

		}
		if (dataMap.size() != 0)
			terminalStatusDao.insertDownloadByTerminalId(dataMap);
	}
	
	@Override
	public void updateDownloadXml(List<Map<String, Object>> timeAndMacList, String[] delmac){
		String ip = getServerIp();
    	try {
	    	for (int i = 0; i < timeAndMacList.size(); i++) {
	    		Map<String, Object> twinMap = new HashMap<String, Object>();
	    		Map<String, Object> xmlMap = new HashMap<String, Object>();
	    		List<Map<String,Object>> ml = (List<Map<String, Object>>) timeAndMacList.get(i).get("time");
//				xmlMap1.put("terminalId",timeAndMacList.get(i).get("terminalid"));
//				list.add(xmlMap1);
				List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
				char[] weeks = {'一','二','三','四','五','六','日'};
				for (int j = 0; j < weeks.length; j++) {
					Map<String,Object> xmlMap3 = new HashMap<String,Object>();
					xmlMap3.put("week", "星期" + weeks[j]);
					List<Map<String,Object>> timemaplist = new ArrayList<Map<String,Object>>();
					for (int m = 0; m < ml.size(); m++) {
						if(ml.get(m).get("days").toString().indexOf(weeks[j]) > -1){
							Map<String,Object> timemap = new HashMap<String,Object>();
							timemap.put("endTime", ml.get(m).get("closetime").toString() + ":00");
							timemap.put("startTime", ml.get(m).get("opentime").toString() + ":00");
							timemaplist.add(timemap);
						}
					}
					if(timemaplist.size() > 0){
						xmlMap3.put("element", timemaplist);
						list1.add(xmlMap3);
					}
				}
				xmlMap.put("terminalId", timeAndMacList.get(i).get("terminalid"));
//				for (int j = 0; j < ml.size(); j++) {
//					String[] days = ml.get(j).get("days").toString().split(",");
//					for (int k = 0; k < days.length; k++) {
//						if(days[k].equals("一")){
//							list1.get(1).
//						}
//					}
//				}
				xmlMap.put("region", list1);
				twinMap.put("terminalDownLoad", xmlMap);
	    		String mac = timeAndMacList.get(i).get("mac").toString();
	    		String[] macs = new String[1];
	    		macs[0] = mac;
	    		List<Map<String, Object>> timeMapList = (List<Map<String, Object>>) timeAndMacList.get(i).get("time");
	    		String terminalname = timeAndMacList.get(i).get("terminalname").toString();
			    XmlUtil xmlUtil = new XmlUtil();
			    String comentStr = xmlUtil.mapToString(twinMap);
				System.out.println(comentStr);
				JSONObject json = new JSONObject();
				json.put("command", "preDownloadTime");
				json.put("target", macs);
				json.put("xml", comentStr);
				System.out.println(json);
				PublishUtil publishUtil = new PublishUtil();
				publishUtil.publishTask(json, ip,"终端监控");
			}
	    	if(null != timeAndMacList && timeAndMacList.size() == 0){
	    		JSONObject json = new JSONObject();
				json.put("command", "preDownloadTime");
				json.put("target", delmac);
				json.put("xml", "");
				System.out.println(json);
				PublishUtil publishUtil = new PublishUtil();
				publishUtil.publishTask(json, ip,"终端监控");
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}
}
