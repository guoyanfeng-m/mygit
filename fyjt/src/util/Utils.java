package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JSONObject;

public class Utils {

	/**
	     * 获得当前时间
	     * @return
	     */

	    public static String getTime() {
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return df.format(new Date()).toString();
	    }

	    
	    /**
	     * 组织发送命令的JSON格式的数据
	     * @param command  发送命令
	     * @param target   目标数组(MAC地址)
	     * @return  JSON格式的数据
	     */

	    public static JSONObject StringtoJson(String command,String[] target)
	    {
	    	JSONObject tobject = new JSONObject();
	    	if(target!=null && target.length>0)
	    	{
	    		StringBuilder stringBuilder = new StringBuilder();
	    		stringBuilder.append("[");
	    		for(int i=0;i<target.length;i++)
	    		{
	    			if(i!=target.length-1)
	    			{
	    				stringBuilder.append("'"+target[i]+"'").append(",");
	    			}
	    			else
	    			{
	    				stringBuilder.append("'"+target[i]+"'");
	    			}
	    			
	    		}
	    		stringBuilder.append("]");
	    		tobject.put("target", stringBuilder.toString());
	    		
	    	}
	    	tobject.put("command", command);
	    	System.out.println("utils:"+tobject.toString());
	    	return tobject;
	    }

}
