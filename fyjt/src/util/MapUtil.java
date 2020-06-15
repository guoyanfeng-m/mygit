package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.playProgramPojo.PlayProgram;
import util.playProgramPojo.region;

import com.alibaba.fastjson.JSON;
/**
 * <p>          
 *       <discription>概述：JSON数据解析 </discription>
 * </p>   
 * @Author          创建人：   
 * @Project_name    项目名称：iis
 * @Company         公司名称：双旗科技
 * @Date            创建时间：2017年1月4日 下午12:26:01
 */
public class MapUtil {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String jsonStr){
		Map<String, Object> map = new HashMap<String, Object>();
		//最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for(Object k : json.keySet()){
			Object v = json.get(k); 
			//如果内层还是数组的话，继续解析
			if(v instanceof JSONArray){
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				Iterator<JSONObject> it = ((JSONArray)v).iterator();
				while(it.hasNext()){
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}


	/**
	 * <p>          
	 *       <discription> 概述：节目管理--节目预览--节目xml解析成pojo </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2017年3月14日 下午5:09:41
	 * @UpdateDate     更新时间：   2017年3月14日 下午5:09:41
	 * @Package_name   包名：          iiswoftp/util
	 * @Param          参数：          @param map
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          PlayProgram
	 */
	public static PlayProgram mapToEntity(Map<String,Object>map){
		String json=JSON.toJSONString(map); 
		PlayProgram playProgram=new PlayProgram();
		playProgram=JSON.parseObject(json, PlayProgram.class);
		return playProgram;
	}
	/**
	 * <p>          
	 *       <discription> 概述：获取节目名称 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2017年3月15日 下午3:52:26
	 * @UpdateDate     更新时间：   2017年3月15日 下午3:52:26
	 * @Package_name   包名：          iiswoftp/util
	 * @Param          参数：          @param playProgram
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          String
	 */
	public static String getProgramName(PlayProgram playProgram){
		String name=playProgram.getTwinflag().getProgram().get(0).getName();
		return name;
	}
	/**
	 * <p>          
	 *       <discription> 概述：获取节目区域的高度 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2017年3月15日 下午3:52:26
	 * @UpdateDate     更新时间：   2017年3月15日 下午3:52:26
	 * @Package_name   包名：          iiswoftp/util
	 * @Param          参数：          @param playProgram
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          String
	 */
	public static String getProgramHeight(PlayProgram playProgram){
		String height=playProgram.getTwinflag().getProgram().get(0).getHeight();
		return height;
	}
	/**
	 * <p>          
	 *       <discription> 概述：获取节目区域的宽度 </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2017年3月15日 下午3:52:33
	 * @UpdateDate     更新时间：   2017年3月15日 下午3:52:33
	 * @Package_name   包名：          iiswoftp/util
	 * @Param          参数：          @param playProgram
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          String
	 */
	public static String getProgramWidth(PlayProgram playProgram){
		String width=playProgram.getTwinflag().getProgram().get(0).getWidth();
		return width;
	}

	/**
	 * <p>          
	 *       <discription> 概述：取值节目模板中的region </discription>
	 * </p>  
	 * @Author         创建人：       RYL
	 * @CreateDate     创建时间：   2017年3月14日 上午10:13:39
	 * @UpdateDate     更新时间：   2017年3月14日 上午10:13:39
	 * @Package_name   包名：          iiswoftp/util
	 * @Param          参数：          @return  
	 * @Rerurn         返回：          int
	 */
	public static List<region> getregionList(PlayProgram program) {
		List<region> region=program.getTwinflag().getProgram().get(0).getScene().get(0).getRegion();
		return region;
	}
}
