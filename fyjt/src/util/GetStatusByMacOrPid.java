package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import service.element.ElementService;
import service.terminal.TerminalService;
import beans.element.ElementBean;
import controller.redisManager.RedisUtil;
@Component
@Transactional(readOnly=true)
public class GetStatusByMacOrPid {
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private ElementService elementService;

	@SuppressWarnings({ "rawtypes", "unused" })
	public List<Map<String, Object>> getProStatus(List<String> mac_pids,String ip) {
		Jedis jedis = RedisUtil.getJedis(ip);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Map<String, String> maps = new HashMap<String, String>();
			List<String> elem_ids = null;
			one:for (int i = 0; i < mac_pids.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String key = mac_pids.get(i).split("_")[0];
				maps = jedis.hgetAll("downloadprogram:" + key);
				String program_name = "";
				String programmapvalue = maps.get(mac_pids.get(i).split("_")[1]);
				JSONObject obj = JSONObject.fromObject(programmapvalue.substring(1, programmapvalue.length()-1));
				String statu = obj.getString("status");
				map.put("statu", statu);
				map.put("mac", key);
				if(statu.equals(RedisUtil.STATUS_DOWNING.toString())){
					int tstu = getTerminalStatus(key);
					if(tstu == 0){
						map.put("statu", RedisUtil.STATUS_INTERRUPT.toString());
					}else{
						double totalsize = 0;
						double downloadsize = 0;
						String elemkey = "downloadelem:" + key + ":" + mac_pids.get(i).split("_")[1];
						Map<String, String> elemmap = jedis.hgetAll(elemkey);
						List<String> elems = new ArrayList<String>();
						List<String> elemmd5s = new ArrayList<String>();
						for (Map.Entry entry : elemmap.entrySet()) {
//							JSONObject obj = JSONObject.fromObject(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
//							Object a  = obj.get("elemmd5");
							elems.add(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
							elemmd5s.add(entry.getKey().toString());
						}
						List<ElementBean> ebl = elementService.selectElementByMD5s(elemmd5s);
						two:for (int j = 0; j < ebl.size(); j++) {
							Map<String, Object> sizemap = new HashMap<String, Object>();
							sizemap.put("elem_name", ebl.get(j).getElem_name());
							sizemap.put("elem_size", ebl.get(j).getElem_size());
							sizemap.put("elem_type", ebl.get(j).getType());
							totalsize += Double.parseDouble(ebl.get(j).getElem_size());
							three:for (int k = 0; k < elems.size(); k++) {
								if(elems.get(k).indexOf(ebl.get(j).getMD5()) > -1){
									String str = elems.get(k);
									sizemap.put("statu", str.substring((str.indexOf("\"status\":")+9),str.length()-2));
									String status = sizemap.get("statu").toString();
									if(status.equals(RedisUtil.STATUS_DOWNING.toString())){
										long downsize = Long.parseLong(str.substring(str.indexOf("\"downsize\":")+11,str.indexOf(",")));
										downloadsize += downsize;
										break three;
									}else if(status.equals(RedisUtil.STATUS_SUCC.toString())){
										downloadsize += Double.parseDouble(ebl.get(j).getElem_size());
										break three;
									}
								}
							}
						}
						if(totalsize == 0){
							map.put("statu", RedisUtil.STATUS_SUCC);
						}else{
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.##");
							double downloadpercent =  (downloadsize * 100) / totalsize;
							map.put("statu", df.format(downloadpercent) + "%");
						}
					}
				}else{
					map.put("statu", statu.toString());
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisUtil.returnJedis(jedis);
			}
		}
		return list;
	}

	private int getTerminalStatus(String mac) {
		List<String> macList = new ArrayList<String>();
		macList.add(mac);
		int result = terminalService.getTerminalStatus(macList);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> getDownloadInfo(String key,String ip) {
		if (key == null) {
			return null;
		}
		Jedis jedis = RedisUtil.getJedis(ip);
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		try {
			Map<String, String> maps = new HashMap<String, String>();
			String elemkey = "downloadelem:" + key.split("_")[0] + ":" + key.split("_")[1];
			maps = jedis.hgetAll(elemkey);
			List<String> elems = new ArrayList<String>();
			List<String> elemmd5s = new ArrayList<String>();
			for (Map.Entry entry : maps.entrySet()) {
				JSONObject obj = JSONObject.fromObject(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				Object elemtype  = obj.get("elemtype");
				if(elemtype != null && (elemtype.equals(5) || elemtype.equals(1))){
					Map<String, Object> sizemap = new HashMap<String, Object>();
					sizemap.put("elem_name", obj.get("elemname"));
					sizemap.put("elem_size", "0");
					sizemap.put("elem_type", elemtype);
					sizemap.put("statu", RedisUtil.STATUS_SUCC.toString());
					maplist.add(sizemap);
				}else{
					elems.add(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
					elemmd5s.add(entry.getKey().toString());
				}
			}
			if(elemmd5s.size() > 0){
				List<ElementBean> ebl = elementService.selectElementByMD5s(elemmd5s);
				for (int j = 0; j < ebl.size(); j++) {
					Map<String, Object> sizemap = new HashMap<String, Object>();
					sizemap.put("elem_name", ebl.get(j).getElem_name());
					String elemsize = ebl.get(j).getElem_size();
					sizemap.put("elem_size", elemsize);
					sizemap.put("elem_type", ebl.get(j).getType());
					for (int i = 0; i < elems.size(); i++) {
						if(elems.get(i).indexOf(ebl.get(j).getMD5()) > -1){
							String str = elems.get(i);
							sizemap.put("statu", str.substring((str.indexOf("\"status\":")+9),str.indexOf("\"status\":")+10));
							if(sizemap.get("statu").equals(RedisUtil.STATUS_DOWNING.toString())){
								int tstu = getTerminalStatus(key.split("_")[0]);
								if(tstu == 0){
									sizemap.put("statu", RedisUtil.STATUS_INTERRUPT.toString());
								}else{
									long downsize = Long.parseLong(str.substring(str.indexOf("\"downsize\":")+11,str.indexOf(",")));
									sizemap.put("downsize", downsize);
									if(downsize == 0){
										sizemap.put("statu", RedisUtil.STATUS_WAIT.toString());
									}else{
										java.text.DecimalFormat df = new java.text.DecimalFormat(
											"#.##");
										sizemap.put("statu", df.format((double)downsize * 100/Double.parseDouble(ebl.get(j).getElem_size())) + "%");
									}
								}
							}
							break;
						}
					}
					maplist.add(sizemap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisUtil.returnJedis(jedis);
			}
		}
		return maplist;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> getOnlineProgram(String mac,String ip) {
		Jedis jedis = RedisUtil.getJedis(ip);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String key = mac;
			Map<String, String> maps = new HashMap<String, String>();
			maps = jedis.hgetAll(key);
			for (Map.Entry entry : maps.entrySet()) {
				JSONObject obj = JSONObject.fromObject(entry.getValue());
				map.put("online_program_id", obj.getInt("online"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisUtil.returnJedis(jedis);
			}
		}
		return map;
	}
}
