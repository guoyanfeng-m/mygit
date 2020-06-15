//package dx20160629;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import service.ConfigService;
//import service.terminal.TerminalStatusService;
//import serviceImpl.program.ProgramServiceImpl;
//import util.CreateTree;
//import util.PageInfo;
//import util.PublishUtil;
//import util.TTerminal;
//import util.XmlUtil;
//import view.TerminalMac;
//import beans.terminal.TerminalBean;
//import beans.terminal.TerminalCloseTime;
//import beans.terminal.TerminalGroupBean;
//import beans.terminal.TerminalStatusBean;
//import beans.terminal.TerminalStatusViewBean;
//import dao.terminal.TerminalDao;
//import dao.terminal.TerminalStatusDao;
//import dao.user.UserDao;
//
//
//@Service
//@Transactional
//public class DXTerminalStatusServiceImpl implements TerminalStatusService{
//	private TerminalStatusDao terminalStatusDao;
//	private TerminalDao terminalDao;
//	private UserDao userDao;
//	private ConfigService configService;
//	public void setConfigService(ConfigService configService) {
//		this.configService = configService;
//	}
//
//	private static String serverIp = null;
//	public UserDao getUserDao() {
//		return userDao;
//	}
//
//	public void setUserDao(UserDao userDao) {
//		this.userDao = userDao;
//	}
//
//	public TerminalDao getTerminalDao() {
//		return terminalDao;
//	}
//
//	public void setTerminalDao(TerminalDao terminalDao) {
//		this.terminalDao = terminalDao;
//	}
//
//	public TerminalStatusDao getTerminalStatusDao() {
//		return terminalStatusDao;
//	}
//
//	public void setTerminalStatusDao(TerminalStatusDao terminalStatusDao) {
//		this.terminalStatusDao = terminalStatusDao;
//	}
//
//	@Override
//	public void insertTerminalStatus(TerminalStatusBean terminalStatusBean) {
//		// TODO Auto-generated method stub
//		terminalStatusDao.insertTerminalStatus(terminalStatusBean);
//	}
//
//	@Override
//	public List<TerminalStatusViewBean> queryTerminalStatus(
//			List<Integer> terminalId) {
//		// TODO Auto-generated method stub
//		return terminalStatusDao.queryTerminalStatus(terminalId);
//	}
//
//	@Override
//	public List<TerminalStatusViewBean> queryTerminalStatusById(Integer[] terminal_id) {
//		// TODO Auto-generated method stub
//			return terminalStatusDao.queryTerminalStatusById(terminal_id);
//		
//	}
//
//	@Override
//	public void deleteTerminalStatusByTerminalId(
//			TerminalBean terminalBean) {
//		// TODO Auto-generated method stub
//		terminalStatusDao.deleteTerminalStatusByTerminalId(terminalBean);
//	}
//
//	@Override
//	public void updateTerminalStatusByTerminalId(
//			TerminalStatusBean terminalStatusBean) {
//		// TODO Auto-generated method stub
//		terminalStatusDao.updateTerminalStatusByTerminalId(terminalStatusBean);
//	}
//
//	@Override
//	public List<TerminalStatusViewBean> queryTerminalStatusByUserId(List<String> userlist,
//			PageInfo pageInfo, Integer filter) {
//		// TODO Auto-generated method stub
//		List<String> rolelist = new ArrayList<String>();
//		for(String user_id : userlist){
//			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
//		}
//		Map<String, Object> map = new HashMap<String, Object>();  
//		map.put("list", rolelist);
//		map.put("offset", pageInfo.getStart());
//		map.put("limit", pageInfo.getEnd());
//		map.put("filter", filter);
//		return terminalStatusDao.queryTerminalStatusByUserId(map);
//	}
//
//	@Override
//	public Integer queryTerminalStatusByUserIdCount(List<String> roleId) {
//		// TODO Auto-generated method stub
//		System.out.println("----------inner:");
//		List<String> rolelist = new ArrayList<String>();
//		for(String user_id : roleId){
//			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
//		}
//		return terminalStatusDao.queryTerminalStatusByUserIdCount(rolelist);
//	}
//
//	@Override
//	public List<TerminalStatusViewBean> queryTerminalStatusByGroupId(Integer terminalGroupId,List<String> role_id,PageInfo pageInfo) {
//		// TODO Auto-generated method stub
//		List<String> rolelist = new ArrayList<String>();
//		for(String user_id : role_id){
//			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
//		}
//		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
//		terminalGroupBean.setIsDeleted(0);
//		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
//		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
//		JSONArray tree = new JSONArray();  
//		tree.addAll(terminalGroupBeanList);
//		List<Integer> groupId = new CreateTree().getGroup(tree, terminalGroupId);
//		return terminalStatusDao.queryTerminalStatusByGroupId(groupId, rolelist, pageInfo.getStart(), pageInfo.getEnd());
//	}
//
//	@Override
//	public Integer queryTerminalStatusByGroupIdCount(Integer terminalGroupId,List<String> role_id) {
//		// TODO Auto-generated method stub
//		List<String> rolelist = new ArrayList<String>();
//		for(String user_id : role_id){
//			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
//		}
//		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
//		terminalGroupBean.setIsDeleted(0);
//		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
//		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
//		JSONArray tree = new JSONArray();  
//		tree.addAll(terminalGroupBeanList);
//		List<Integer> groupId = new CreateTree().getGroup(tree, terminalGroupId);
//		Map<String, Object> map = new HashMap<String, Object>();  
//		map.put("list", groupId);
//		map.put("role_id", role_id);
//		return terminalStatusDao.queryTerminalStatusByGroupIdCount(groupId, rolelist);
//	}
//
//	
//	@Override
//	public List selectDiskUsage() {
//		return terminalStatusDao.selectDiskUsage();
//	}
//
//	@Override
//	public void deletecloseTimeByTerminalId(TerminalCloseTime terminalCloseTime) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//
//
//	@Override
//	public void insertcloseTimeByTerminalId(TerminalCloseTime terminalCloseTime) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<TerminalCloseTime> querycloseTimeByTerminalId(String[] terminalId) {
//		return terminalStatusDao.querycloseTimeByTerminalId(terminalId);
//	}
//
//	@Override
//	public void updatecloseTimeByTerminalId(List<Map<String, Object>> dataMap, String[] delterminal) {
//		Integer result = terminalStatusDao.querycloseTimeCountByTerminalId(delterminal);
//		if(result > 0) {
//			terminalStatusDao.deletecloseTimeByTerminalId(delterminal);
//		}
//		if (dataMap.size() != 0)
//			terminalStatusDao.insertcloseTimeByTerminalId(dataMap);
//	}
//
//	@Override
//	public List<TerminalMac> queryMacAndTime() {
//		return terminalStatusDao.queryMacAndTime();
//	}
//
//    @Override
//	public void updatecloseTimeXml(List<Map<String, Object>> timeAndMacList) {
//    	String ip = getServerIp();
//    	try {
//	    	for (int i = 0; i < timeAndMacList.size(); i++) {
//	    		Map<String, Object> xmlMap = new HashMap<String, Object>();
//	    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//				Map<String, Object> xmlMap1 = new HashMap<String, Object>();
//				xmlMap1.put("type", "timedPowerOnOff");
//				list.add(xmlMap1);
//				xmlMap.put("command", list);
//	    		String mac = timeAndMacList.get(i).get("mac").toString();
//	    		
//	    		String[] macs = new String[1];
//	    		macs[0] = mac;
//	    		List<Map<String, Object>> timeMapList = (List<Map<String, Object>>) timeAndMacList.get(i).get("time");
//	    		Map<String, Object> twinMap = new HashMap<String, Object>();
//	    		String terminalname = timeAndMacList.get(i).get("terminalname").toString();
//	    		xmlMap.put("systime", timeMapList);
//	    		twinMap.put("systimes", xmlMap);
//			    XmlUtil xmlUtil = new XmlUtil();
//			    String comentStr = xmlUtil.mapToString(twinMap);
//				System.out.println(comentStr);
//				JSONObject json = new JSONObject();
//				json.put("command", "sendCommand");
//				json.put("target", macs);
//				json.put("xml", comentStr);
//				PublishUtil publishUtil = new PublishUtil();
//				publishUtil.publishTask(json, ip);
//			}
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    }
//	
//	private String getServerIp() {
//		if (null == DXTerminalStatusServiceImpl.serverIp) {
//			String ip=configService.queryConfig("tfbsip");// 鑾峰彇鏈嶅姟鍣↖P
//			if(ip==null){
//			   ip=configService.queryConfig("httpip");
//			}
//			DXTerminalStatusServiceImpl.serverIp = ip;
//		}
//		return DXTerminalStatusServiceImpl.serverIp;
//	}
//	
//}
