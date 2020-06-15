package serviceImpl.program;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.json.JSONObject;

import org.apache.commons.collections.list.SetUniqueList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import service.program.ProgramService;
import util.DownLoadList;
import util.FileCheck;
import util.PageInfo;
import util.PlaylistXmlParser;
import util.PubUtil;
import util.PublishUtil;
import util.XmlUtil;
import beans.element.ElementBean;
import beans.program.ProgramBean;
import beans.program.ProgramTerminalBean;
import beans.program.ProgramViewBean;
import beans.program.view.ProgramTerminalView;
import beans.sys.SystemConstant;
import beans.terminal.TerminalViewBean;
import controller.redisManager.RedisUtil;
import dao.config.ConfigDao;
import dao.element.ElementDao;
import dao.program.ProgramDao;
import dao.program.ProgramTerminalDao;
import dao.terminal.TerminalDao;

@SuppressWarnings({"rawtypes","unchecked", "static-access","unused" })
@Service
@Transactional(readOnly=true)
public class ProgramServiceImpl implements ProgramService {
	private static String serverIp = null;
	private static AtomicInteger counter = new AtomicInteger(0);
	@Autowired
	private ProgramDao programDao;
	@Autowired
	private ProgramTerminalDao programTerminalDao;
	@Autowired
	private ConfigDao configDao;
	@Autowired
	private ElementDao elementDao;
	@Autowired
	private TerminalDao terminalDao;


	@Transactional(readOnly=false)
	public void insertProgram(ProgramBean programBean,
			Map<String, Object> programMap) {
		programDao.insertProgram(programBean);
		programMap.put("id", programBean.getProgram_id());
		String path = configDao.queryConfig("ftpMappingUrl");
		checkFolder(path);
		Map<String, Object> xmlMap = new HashMap<String, Object>();
		xmlMap.put("program", programMap);
		Map<String, Object> twinMap = new HashMap<String, Object>();
		twinMap.put("twinflag", xmlMap);
		String url = path + "programXmlFile/" + programBean.getName() + ".xml";
		XmlUtil xmlUtil = new XmlUtil();
		try {
			xmlUtil.mapToXml(url, twinMap);
			String xmlContext = xmlUtil.XmlToString(url);
			programBean.setXmlcontent(xmlContext);
			programDao.updateProgram(programBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 节目导入
	 */
	@Transactional(readOnly=false)
	public boolean insertImpProgram(ProgramBean programBean, String proXml ,String xmlUrl) {
		boolean hasName = false;
		//保存节目单获取id
		programDao.insertProgram(programBean);
		//修改节目单xml节目单属性id
		// 获取xml文件工具
		XmlUtil xmlUtil = new XmlUtil();
		//获取节目单map
		Map<String, Object> proMaps = xmlUtil.xmlToMap(proXml);
		//获取第一个标签map集合
		Map<String, Object> twinflag = (Map<String, Object>) proMaps
				.get("twinflag");
		//获取节目单信息
		List<Object> program = (ArrayList<Object>) twinflag.get("program");
		//获取节目单详情map
		Map<String, Object> p = (Map<String, Object>) program.get(0);
		//修改节目单id
		p.put("id", programBean.getProgram_id());
		//修改节目名称
		p.put("name", programBean.getName());
		
		String xmlContext = null;
		try {
			xmlContext = xmlUtil.mapToString(proMaps);
			hasName = xmlUtil.mapToXml(xmlUrl + programBean.getName() + ".xml", proMaps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//数据库保存节目单xml
		programBean.setXmlcontent(xmlContext);
		programDao.updateProgram(programBean);
		return hasName;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteProgramById(Integer[] ids) {
		List<Integer> idList = new PubUtil().integers2List(ids);
		List<ProgramBean> programs = programDao.queryProgramByIds(idList);
		String path = configDao.queryConfig("ftpMappingUrl");
		for (ProgramBean bean : programs) {
			File file = new File(path + bean.getUrl());
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		}
		programDao.deleteProgramByIds(idList);
	}

	@Override
	@Transactional(readOnly=false)
	public void send(Integer[] ids, ProgramBean programBean) {
		List<Integer> idList = new ArrayList<Integer>();
		for (Integer id : ids) {
			idList.add(id);
		}
		String ip = getServerIp();
		// String ip="192.168.13.129";
		insertProgramTerminal(ids, programBean.getProgram_id());
		List<TerminalViewBean> beanList = terminalDao.queryTerminalByTerminalIds(idList);

		String[] macs = new String[beanList.size()];
		for (int i = 0; i < beanList.size(); i++) {
			macs[i] = beanList.get(i).getMac();
		}
		JSONObject json = new JSONObject();
		json.put("command", "publishTask");
		json.put("target", macs);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.format(new Date());
		json.put("publishTime", df.format(new Date()));
		System.out.println("publishTime:" + json.get("publishTime") + "--------idList:" + idList);
		json.put("task", programBean.getXmlcontent());
//		PublishUtil publishUtil = new PublishUtil();
		addRedis(ids, programBean);
		new PublishUtil().publishTask(json, ip,"节目管理");
//		publishUtil.publishTask(json, ip);
		if (programBean.getIsSend().equals(SystemConstant.ISSEND_FALSE)) {
			programBean.setIsSend(SystemConstant.ISSEND_TRUE);
			programDao.updateProgram(programBean);
		}
	}

	private void checkFolder(String path) {
		File file = null;
		try {
			file = new File(path + "programXmlFile/");
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ProgramViewBean> queryProgram(ProgramBean programBean,
			PageInfo pageinfo, Integer creatorid, List<Integer> userids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ProgramBean", programBean);
		map.put("offset", pageinfo.getStart());
		map.put("limit", pageinfo.getEnd());
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		return programDao.queryProgram(map);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertProgramTerminal(Integer[] ids, int program_id) {
		ProgramTerminalBean ptBean = new ProgramTerminalBean();
		ptBean.setProgram_id(program_id);
		List<ProgramTerminalView> tempList = programTerminalDao
				.queryProgramTerminal(ptBean);
		ptBean.setPublishTime(new Timestamp(System.currentTimeMillis()));
		for (int i = 0; i < ids.length; i++) {
			boolean hasBean = false;
			for (int j = 0; j < tempList.size(); j++) {
				if (ids[i].equals(tempList.get(j).getTerminal_id())) {
					hasBean = true;
					ptBean.setTerminal_id(ids[i]);
					programTerminalDao.updateProgramTerminal(ptBean);
					break;
				}
			}
			if (!hasBean) {
				ptBean.setTerminal_id(ids[i]);
				programTerminalDao.insertProgramTerminal(ptBean);
			}
		}
	}

	@Override
	public List<ProgramTerminalView> queryProgramTerminal(
			ProgramTerminalBean programTerminalBean) {
		List<ProgramTerminalView> list = programTerminalDao.queryProgramTerminal(programTerminalBean);
		return list;
	}

	@Override
	public Integer queryProgramCount(ProgramBean programBean,
			Integer creatorid, List<Integer> userids) {
		return programDao.queryProgramCount(programBean, creatorid, userids);
	}

	@Override
	public List<ProgramBean> queryProgramByIds(List<Integer> idList) {
		return programDao.queryProgramByIds(idList);
	}

	/**
	 * 更新保存节目单
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateProgram(ProgramBean programBean,Map<String, Object> programMap) {
		List<Integer> tempIds = new ArrayList<Integer>();
		tempIds.add(programBean.getProgram_id());
		// 获取原始播出单信息
		List<ProgramBean> program = programDao.queryProgramByIds(tempIds);
		if (program == null || program.size() < 1) {
			return;
		}
		String path = configDao.queryConfig("ftpMappingUrl");
		String oldFilePath = path + program.get(0).getUrl();
		// 删除原始播出单文件
		File file = new File(oldFilePath);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		// 生成新播出单文件
		checkFolder(path);
		Map<String, Object> xmlMap = new HashMap<String, Object>();
		xmlMap.put("program", programMap);
		Map<String, Object> twinMap = new HashMap<String, Object>();
		twinMap.put("twinflag", xmlMap);
		XmlUtil xmlUtil = new XmlUtil();
		try {
			xmlUtil.mapToXml(path + programBean.getUrl(), twinMap);
			String xmlContext = xmlUtil.XmlToString(path + programBean.getUrl());
			programBean.setXmlcontent(xmlContext);
			// 更新播出单实体信息
			programDao.updateProgram(programBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void stopProgram(Integer[] program_ids) {
		ProgramBean programBean = new ProgramBean();
		ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
		List<ProgramTerminalView> ptList = new ArrayList<ProgramTerminalView>();
		for (Integer program_id : program_ids) {
			programTerminalBean.setProgram_id(program_id);
			ptList = programTerminalDao
					.queryProgramTerminal(programTerminalBean);
			String[] macs = new String[ptList.size()];
			for (int i = 0; i < ptList.size(); i++) {
				macs[i] = ptList.get(i).getMac();
			}
			if (macs.length > 0) {
				this.stop(macs, program_id);
			}
			programBean.setProgram_id(program_id);
			programBean.setIsSend(SystemConstant.ISSEND_FALSE);
			programDao.updateProgram(programBean);
			programTerminalDao.deleteProgramTerminal(programTerminalBean);
			checkProgramPublish(program_id);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void checkProgramPublish(Integer program_id) {
		ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
		programTerminalBean.setProgram_id(program_id);
		List<ProgramTerminalView> list = programTerminalDao
				.queryProgramTerminal(programTerminalBean);
		if (null == list || list.size() == 0) {
			ProgramBean programBean = new ProgramBean();
			programBean.setProgram_id(program_id);
			programBean.setIsSend(SystemConstant.ISSEND_FALSE);
			programDao.updateProgram(programBean);
		}
	}

	private boolean stop(String[] macs, Integer program_id) {
		String ip = getServerIp();
//		PublishUtil u = new PublishUtil();
		JSONObject jsons = new JSONObject();
		jsons.put("command", "deleteTask");
		jsons.put("target", macs);
		jsons.put("taskid", program_id.toString());
//		return u.publishTask(jsons, ip);
		return new PublishUtil().publishTask(jsons, ip,"节目管理");
	}

	private String getServerIp() {
		if (null == ProgramServiceImpl.serverIp) {
			String ip = configDao.queryConfig("tfbsip");// 获取服务器IP
			if (ip == null) {
				ip = configDao.queryConfig("httpip");
			}
			ProgramServiceImpl.serverIp = ip;
		}
		return ProgramServiceImpl.serverIp;
	}

	@Override
	public List<ProgramBean> queryProgramByName(String name) {
		return programDao.queryProgramByName(name);
	}

	@Override
	public void auditProgram(ProgramBean programBean) {
		programDao.auditProgram(programBean);

	}

	@Override
	public boolean stopProgramTerminal(Integer program_id, Integer terminal_id) {
		ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
		programTerminalBean.setProgram_id(program_id);
		programTerminalBean.setTerminal_id(terminal_id);
		List<ProgramTerminalView> tempList = programTerminalDao
				.queryProgramTerminal(programTerminalBean);
		if (null == tempList || tempList.size() == 0) {
			return false;
		}
		String[] macs = { tempList.get(0).getMac() };
		stop(macs, program_id);
		programTerminalDao.deleteProgramTerminal(programTerminalBean);
		checkProgramPublish(program_id);
		return true;
	}

	@Override
	public boolean queryElementCount(Set<String> urllist) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", urllist);
		Integer a = programDao.queryElementCount(map);
		boolean flag = false;
		if (a == urllist.size()) {
			flag = true;
		}
		return flag;
	}

	@Override
	public DownLoadList findPlaylist(List<Integer> idList) {
		List<ProgramBean> pb = programDao.queryProgramByIds(idList);
		DownLoadList downloadList = new DownLoadList();
		downloadList.setXml(pb.get(0).getUrl());
		downloadList.setName(pb.get(0).getName());
		downloadList.setElements(new PlaylistXmlParser(configDao.queryConfig("ftpMappingUrl")+ downloadList.getXml()).getPath());
		return downloadList;
	}

	@Override
	public Map<String, Object> checkMap(Map<String, Object> programMap,boolean isMainScene) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		FileCheck fileCheck = new FileCheck();
		Set<String> srcList = new HashSet<String>();
		Set<String> idList = new HashSet<String>();
		Set<String> backimglist = new HashSet<String>();
		Set<String> plugList = new HashSet<String>();
		Set<String> screenList = new HashSet<String>();
		List<String> srcl = new ArrayList<String>();
		// Set<String> pIdList = new HashSet<String>();
		fileCheck.getSrcByMap(srcList, idList, backimglist, plugList,screenList, programMap,isMainScene,srcl);
		// fileCheck.getProgramIDByMap(pIdList, programMap);
		returnMap.put("srcListSize", srcList.size());
		returnMap.put("srcList", srcList);// 素材所有的路径
		returnMap.put("idList", idList);// 素材所有id
		returnMap.put("plugListSize", plugList.size());
		returnMap.put("backimgSize", backimglist.size());// 素材所有背景图片路径
		returnMap.put("screenListSize", screenList.size());
		returnMap.put("srcl", srcl.size());
		
		// returnMap.put("pIdList", pIdList);//所有的节目id
		if (srcList.size() == 0) {
			returnMap.put("succ", true);
		} else {
			returnMap.put("succ", queryElementCount(srcList));
		}
		return returnMap;
	}

	@Override
	@Transactional(readOnly=false)
	public boolean stopProgramTerminals(Integer program_id,Integer[] terminal_id) {
		String[] macs = new String[terminal_id.length];
		List<Integer>terminalIds=SetUniqueList.decorate(new ArrayList<Integer>());
		for (Integer terminalId : terminal_id) {
			terminalIds.add(terminalId);
		}
		for (int i = 0; i < terminalIds.size(); i++) {
			ProgramTerminalBean programTerminalBean = new ProgramTerminalBean();
			programTerminalBean.setProgram_id(program_id);
			programTerminalBean.setTerminal_id(terminalIds.get(i));
			List<ProgramTerminalView> tempList = programTerminalDao.queryProgramTerminal(programTerminalBean);
			if (null == tempList || tempList.size() == 0) {
				return false;
			}
			macs[i] = tempList.get(0).getMac();
			programTerminalDao.deleteProgramTerminal(programTerminalBean);
		}
		checkProgramPublish(program_id);
		stop(macs, program_id);
		return true;
	}

	@Override
	public List<ProgramTerminalView> queryByTerminalId(ProgramTerminalBean bean) {
		return programTerminalDao.queryByTerminalId(bean);
	}

	@Override
	public List<ProgramBean> queryProgramTimeByTerminalId(String terminal_id,
			Integer program_id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("terminal_id", terminal_id);
		param.put("program_id", program_id);
		List<Integer> programIdList = programTerminalDao
				.queryProgramIdByTerminalId(param);
		List<ProgramBean> list = new ArrayList<ProgramBean>();
		if (programIdList.size() != 0) {
			list = programTerminalDao.queryPubProgramByIds(programIdList);
		}
		return list;
	}

	@Override
	public String queryTerminalNameByTerminalId(int terminalId) {
		return programTerminalDao.queryTerminalNameByTerminalId(terminalId);
	}

	@Override
	public List queryTerminalIdByProgramId(int programId) {
		return programTerminalDao.queryTerminalIdByProgramId(programId);
	}

	@Override
	public List<ProgramTerminalBean> queryTerminalIdByMacs(String[] macList) {
		return programTerminalDao.queryTerminalIdByMacs(macList);
	}

	/**
	 * 发布播出单是初始化redis库中下载状态
	 * @param idsInt
	 * @param programBean
	 */
	@Transactional(readOnly=false)
	public void addRedis(Integer[] idsInt, ProgramBean programBean) {
		// redis库数据处理
		Integer htmlcount = 0;
		Integer textcount = 0;
		Integer streamcount = 0;
		String value = programBean.getXmlcontent();
		XmlUtil util = new XmlUtil();
		Map<String, Object> mapValue = util.stringToMap(value);
		Map<String, Object> tempMap = checkMap(mapValue,false);
		String ip = "";
		ip = configDao.queryConfig("tfbsip");// 获取服务器IP
		if (ip == null) {
			ip = configDao.queryConfig("httpip");
		}
		Jedis jedis = RedisUtil.getJedis(ip);
		try {
			List<Integer> terminalids = new ArrayList<Integer>();// 终端id
			List<ElementBean> elemList = new ArrayList<ElementBean>();// 素材数据
			for (int i = 0; i < idsInt.length; i++) {
				terminalids.add(idsInt[i]);
			}
			List<TerminalViewBean> terminal = terminalDao.queryTerminalByTerminalIds(terminalids);
			if (null != tempMap.get("srcListSize")
					&& (Integer) tempMap.get("srcListSize") > 0) {
				Set<String> listset = (Set<String>) tempMap.get("srcList");
				Set<String> idset = (Set<String>) tempMap.get("idList");
				elemList = elementDao.FindElementIdsWithElementPath(new ArrayList<String>(idset));
			}
			JSONObject jsonstr = new JSONObject();
			List<String> elemmd5list = new ArrayList<String>();
			for (int i = 0; i < terminal.size(); i++) {
//				String prokey = terminal.get(i).getMac() + "_"
//						+ programBean.getProgram_id();
				String prokey = terminal.get(i).getMac();
				// 如果库中已有数据则先删除原始数据
				Map<String, String> prostr = jedis.hgetAll("downloadprogram:" + prokey);
				JSONObject projson = JSONObject.fromObject(prostr);
				if (projson.size() != 0) {
//					System.out.println("redis中将要删除的原始数据：-----" + prostr);
//					List<String> elems = (List<String>) projson.get(programBean.getProgram_id());
//					if (null != elems) {
//						for (int j = 0; j < elems.size(); j++) {
//						}
//					}
					String[] proids = {programBean.getProgram_id().toString()};
					jedis.del("downloadelem:" + prokey + ":" + programBean.getProgram_id());
					jedis.hdel("downloadprogram:" + prokey,proids);
				}
				// 清空MD5列表
				elemmd5list.clear();
				// 写入素材初始数据
				for (int j = 0; j < elemList.size(); j++) {
					if(elemList.get(j).getType() == 5){
						htmlcount += 1;
						Integer htmlindex = counter.incrementAndGet();
						String elemmd5 = "html" + htmlindex.toString();
						elemmd5list.add(elemmd5);
						String elemkey = prokey + "_" + elemmd5;
						// 清空数据
						jsonstr.clear();
						jsonstr.put("elemname", elemList.get(j).getElem_name());
						jsonstr.put("filepath", elemList.get(j).getElem_path());
						jsonstr.put("status", RedisUtil.STATUS_SUCC);
						jsonstr.put("size", "0");
						jsonstr.put("downsize", "0");
						jsonstr.put("elemtype", elemList.get(j).getType());
//						jedis.hset(elemkey, elemkey, jsonstr.toString());
						jedis.hset("downloadelem:" + prokey + ":" + programBean.getProgram_id(), elemmd5, "\"" +jsonstr.toString() + "\"");
					}else if(elemList.get(j).getType() == 1){
						textcount += 1;
						String elemmd5 = elemList.get(j).getMD5().toString();
						elemmd5list.add(elemmd5);
						String elemkey = prokey + "_" + elemmd5;
						// 清空数据
						jsonstr.clear();
						jsonstr.put("elemname", elemList.get(j).getElem_name());
						jsonstr.put("filepath", elemList.get(j).getElem_path());
						jsonstr.put("status", RedisUtil.STATUS_SUCC);
						jsonstr.put("size", "0");
						jsonstr.put("downsize", "0");
						jsonstr.put("elemtype", elemList.get(j).getType());
//						jedis.hset(elemkey, elemkey, jsonstr.toString());
						jedis.hset("downloadelem:" + prokey + ":" + programBean.getProgram_id(), elemmd5, "\"" +jsonstr.toString() + "\"");
					}else if(elemList.get(j).getType() == 7){
						String elemmd5 = elemList.get(j).getMD5().toString();
						elemmd5list.add(elemmd5);
						String elemkey = prokey + "_" + elemmd5;
						// 清空数据
						jsonstr.clear();
						String path = elemList.get(j).getElem_path();
						jsonstr.put("elemname", elemList.get(j).getElem_name());
						jsonstr.put("filepath", path);
						jsonstr.put("status", RedisUtil.STATUS_WAIT);
						String[] pathTem = path.split("\\\\");
						String realsize = pathTem[pathTem.length-1].split("_")[0];
						jsonstr.put("size", realsize);
						jsonstr.put("downsize", "0");
						jsonstr.put("elemtype", elemList.get(j).getType());
//						jedis.hset(elemkey, elemkey, jsonstr.toString());
						jedis.hset("downloadelem:" + prokey + ":" + programBean.getProgram_id(), elemmd5, "\"" +jsonstr.toString() + "\"");
					}else if(elemList.get(j).getType() == 8){                 //流媒体
						streamcount += 1;
						Integer streamindex = counter.incrementAndGet();
						String elemmd5 = "stream" + streamindex.toString();
						elemmd5list.add(elemmd5);
						String elemkey = prokey + "_" + elemmd5;
						// 清空数据
						jsonstr.clear();
						jsonstr.put("elemname", elemList.get(j).getElem_name());
						jsonstr.put("filepath", elemList.get(j).getElem_path());
						jsonstr.put("status", RedisUtil.STATUS_SUCC);
						jsonstr.put("size", "0");
						jsonstr.put("downsize", "0");
						jsonstr.put("elemtype", elemList.get(j).getType());
//						jedis.hset(elemkey, elemkey, jsonstr.toString());
						jedis.hset("downloadelem:" + prokey + ":" + programBean.getProgram_id(), elemmd5, "\"" +jsonstr.toString() + "\"");
					}else{
						String elemmd5 = elemList.get(j).getMD5().toString();
						elemmd5list.add(elemmd5);
						// 清空数据
						jsonstr.clear();
//						jsonstr.put("elemname", elemList.get(j).getElem_name());
//						jsonstr.put("filepath", elemList.get(j).getElem_path());
						jsonstr.put("status", RedisUtil.STATUS_WAIT);
						jsonstr.put("elemmd5", elemmd5);
//						jsonstr.put("size", elemList.get(j).getElem_size());
//						jsonstr.put("downsize", "0");
//						jsonstr.put("elemtype", elemList.get(j).getType());
						jedis.hset("downloadelem:" + prokey + ":" + programBean.getProgram_id(), elemmd5, "\"" +jsonstr.toString() + "\"");
					}
				}
				jsonstr.clear();
				// 写入节目初始数据
//				jsonstr.put("programname", programBean.getName());
//				jsonstr.put("elemnum", elemList.size());
//				jsonstr.put("elemdownnum", "0");
				if(htmlcount + textcount + streamcount == elemList.size()){
					jsonstr.put("status", RedisUtil.STATUS_SUCC);
				}else{
					jsonstr.put("status", RedisUtil.STATUS_WAIT);
				}
				jedis.hset("downloadprogram:" + prokey, programBean.getProgram_id().toString(), "\"" +jsonstr.toString() + "\"");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisUtil.returnJedis(jedis);
			}
		}
	}
    //通过节目的id查找节目xml协议信息
	@Override
	public String selectProgramXmlById(ProgramBean program) {
		return	programDao.selectProgramXmlById(program);
	}
}
