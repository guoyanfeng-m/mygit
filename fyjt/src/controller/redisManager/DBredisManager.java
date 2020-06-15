package controller.redisManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import service.config.ConfigService;

@SuppressWarnings({"unchecked"})
@Controller
@Transactional
public class DBredisManager {
	@Autowired
	private ConfigService configService;
	private static String ip;
	private Jedis jedis;
	private ShardedJedis shardedJedis;

	private String getIp() {
		if (DBredisManager.ip == null) {
			DBredisManager.ip = configService.queryConfig("tfbsip");// 获取服务器IP
			if (DBredisManager.ip == null) {
				DBredisManager.ip = configService.queryConfig("httpip");
			}
		}
		return DBredisManager.ip;
	}

	@SuppressWarnings("static-access")
	@RequestMapping("redisManage/serviceRedisManage.do")
	@ResponseBody
	public Map<String, Object> redisManage(
			@RequestParam(value = "context") String context) {
		System.out.println(context);
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject().fromObject(context);
		String key = "";
		String value = "";
		String status = "";
		String comand = json.getString("command");
		String mac = json.getString("mac");
		String proid = json.getString("proid");
		jedis = RedisUtil.getJedis(getIp());
		shardedJedis = RedisUtil.getShardedJedis(getIp());
		try {
			/**
			 * @description 播出单状态值 0:待下载，1：下载中，2：下载成功，3：下载失败
			 */
			// 更新播出单状态
			if (null != comand && comand.equals("updataprosta")) {
				key = mac + "_" + proid;
				String pro = jedis.hget(key, key);
				JSONObject projson = new JSONObject().fromObject(pro);
				projson.put("status", json.getString("status"));
				value = projson.toString();
				shardedJedis.hset(key, key, value);
				map.put("command", "succ");
				// 更新素材下载状态
			} else if (null != comand && comand.equals("updataelemsta")) {
				Integer elemdownnum = 0;
				List<Map<String, Object>> items = json.getJSONArray("items");
				if (items.size() > 0) {
					for (int i = 0; i < items.size(); i++) {
						String elemmd5 = items.get(i).get("elemmd5").toString();
						boolean isOffice = false;
						if(elemmd5.contains("_")){//是office文件
							key = mac + "_" + proid + "_"
							+ elemmd5.split("_")[0];
							isOffice = true;
						}else{
							key = mac + "_" + proid + "_"+ elemmd5;
						}
						status = items.get(i).get("status").toString();
						//获取redis中下载进度
						String values = jedis.hget(key, key);
						if(values != null && !values.equals("")){
							new JSONObject();
							JSONObject jsonMacPidEid = JSONObject.fromObject(values);
							if (null != status&& status.equals(RedisUtil.STATUS_DOWNING.toString())) {
								//String old_down_size = jsonMacPidEid.get("downsize").toString();
								if(!isOffice){
									String down_size = items.get(i).get("downsize").toString();
									jsonMacPidEid.put("downsize", down_size);
								}
							} else if (null != status&& status.equals(RedisUtil.STATUS_SUCC.toString())) {
								if(!isOffice){
									jsonMacPidEid.put("downsize", jsonMacPidEid.get("size"));
									elemdownnum += 1;
								}else{
									jsonMacPidEid.put("downsize", Integer.parseInt(jsonMacPidEid.get("downsize").toString())+1);
								}
							}
							if(!isOffice){
								jsonMacPidEid.put("status", status);
							}else{
								if(jsonMacPidEid.get("downsize").equals(jsonMacPidEid.get("size"))){
									jsonMacPidEid.put("status", RedisUtil.STATUS_SUCC.toString());
								}else
									jsonMacPidEid.put("status", RedisUtil.STATUS_DOWNING.toString());
							}
							
							value = jsonMacPidEid.toString();
						}
						shardedJedis.hset(key, key, value);
					}
					// 计算该节目单现在的下载状况（elemdownnum）
					// String prokey =key = mac + "_" + proid;
					// updateproelemdownnum(prokey,ip,elemdownnum);
				}
				map.put("command", "succ");
				// 更新正在播放的终端节目单
			} else if (null != comand && comand.equals("updataonlinepro")) {
				key = mac;
				String values = jedis.hget(key, key);
				JSONObject jsonMac = new JSONObject().fromObject(values);
				String online = jsonMac.getString("online");
				jsonMac.put("online", online);
				value = jsonMac.toString();
				shardedJedis.hset(key, key, value);
				map.put("command", "succ");
			}
		} catch (Exception e) {
			System.out.println("key--------"+key);
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisUtil.returnJedis(jedis);
			}
			if (null != shardedJedis) {
				RedisUtil.returnShardedJedis(shardedJedis);
			}
		}
		return map;
	}
	// 更新素材的时候更新节目中已经下载素材数量
	// public void updateproelemdownnum(String prokey,String ip,Integer
	// elemdownnum){
	// Jedis redis = RedisUtil.getJedis(ip);
	// String prostr = redis.hget(prokey, prokey);
	// if(null!=prostr){
	// JSONObject projson = new JSONObject().fromObject(prostr);
	// projson.put("elemdownnum",elemdownnum+projson.getInt("elemdownnum"));
	// String provalue = projson.toString();
	// shardedJedis.hset(prokey, prokey, provalue);
	// }
	// }
}
