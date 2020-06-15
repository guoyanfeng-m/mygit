package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import beans.terminal.TaskDownLoadBean;

public class getDownloadByMac {
	private Jedis jedis;
	public getDownloadByMac(String ip){
		jedis = new Jedis(ip,6379);  
	}
	
	@SuppressWarnings("rawtypes")
	public List<TaskDownLoadBean> getDownload(String mac,String key,String terminalName){
		
		List<TaskDownLoadBean> taskDownLoadBeanList = new ArrayList<TaskDownLoadBean>();
		try{
		   Map<String,String> maps = jedis.hgetAll(key);
		   for(Map.Entry entry: maps.entrySet()) {
		       JSONObject obj = JSONObject.fromObject(entry.getValue());
		       TaskDownLoadBean taskDownLoadBean = new TaskDownLoadBean();
		       //taskDownLoadBean.setTaskId(entry.getKey()+"");
		       taskDownLoadBean.setMac(mac);
		       taskDownLoadBean.setTaskName(obj.getString("name"));
		       taskDownLoadBean.setTaskPercent(obj.getString("percent"));
		       taskDownLoadBean.setTerminal_name(terminalName);
		       taskDownLoadBeanList.add(taskDownLoadBean);
		   }
		}catch(Exception e){
			System.out.println("查询下载状态失败！");
		}finally{
			closeJedis(jedis);
		}
		
		
		return taskDownLoadBeanList;
		
	}
	
	public void delDownload(String mac,String taskId){
		
		jedis.hdel(mac, taskId);
		closeJedis(jedis);
		
	}
	 
	   public static void closeJedis(Jedis jedis) {
		   		if (jedis.isConnected()) {
		   			try {
		   				try {
		   					jedis.quit();
		   				} catch (Exception e) {
		   				}
		   				jedis.disconnect();
		   			} catch (Exception e) {
		   				System.out.println("jedis close error!");
		   			}
		   		}
		   	}
}



