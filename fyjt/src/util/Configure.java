package util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Properties;


/**
 * 配置文件读取工具
 * @author zhny
 *
 */
public class Configure {
private static Hashtable<String, Properties> env=new Hashtable<String, Properties>();
	
	private static class Hold{
		private final static Configure INSTANCE=new Configure(); 
	}
	
	public static Configure getInstance(){
		return Hold.INSTANCE;
	}
	
	public Object getValue(String pro,String key){
		return  env.get(pro).getProperty(key);
	}
	
	public Properties getProperties(String pro){
		return env.get(pro);
	}
	
	private Configure(){
		File dir=new File(this.getClass().getResource("/").getPath());
		if(!dir.isDirectory()&&!dir.exists()){
			dir.mkdirs();
		}else{
			try {
				File[] confFiles=dir.listFiles(new FileFilter() {
					
					public boolean accept(File file) {
						if(file.getName().endsWith(".properties")){
							return true;
						}
						return false;
					}
				});
				for(File file:confFiles){
					String fileName=getFileName(file.getName());
					InputStreamReader is= new InputStreamReader(new FileInputStream(file),"UTF-8"); //添加编码转换以适应中文
					Properties p=new Properties();
					p.load(is);
					is.close();
					env.put(fileName, p);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("加载配置文件出错");
			}
		}
			
	}
	
	private String getFileName(String name){
		int last=name.lastIndexOf(".");
		if(last>=0){
			return name.substring(0,last);
		}else{
			return name;
		}
	}

}
