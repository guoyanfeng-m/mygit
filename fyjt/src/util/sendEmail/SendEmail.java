package util.sendEmail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SendEmail extends TimerTask{
	private String user;
	private String password;
	private String toAddress;
	private String theme;
	private String content;
	private String stmp;
	private String port;
	public SendEmail(String user,String password,String toAddress,String theme,String emailstmp,String emailport){
		this.user = user;
		this.password = password;
		this.toAddress = toAddress;
		this.theme = theme;
		this.port = emailport;
		this.stmp = emailstmp;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStmp() {
		return stmp;
	}
	public void setStmp(String stmp) {
		this.stmp = stmp;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	@SuppressWarnings("rawtypes")
	public void run(){
		if(checkEmpty()){
			return;
		}
		File file = null;
		File emialFile=null;
		try {
			 file = new File(new File(this.getClass().getClassLoader()
					.getResource("").toURI()).getParentFile()
					.getParentFile()
					+ File.separator
					+ "warning"
					+ File.separator
					+ "warning.txt");
			 emialFile = new File(new File(this.getClass().getClassLoader()
						.getResource("").toURI()).getParentFile()
						.getParentFile()
						+ File.separator
						+ "warning"
						+ File.separator
						+ "email.xml");
			content = readFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Exception in thread "Timer-1" java.lang.ArrayIndexOutOfBoundsException: 1
		String emailtype="";
		if (user.indexOf("@")>-1) {
			emailtype = user.split("@")[1];
		}
		MailSenderInfo mailInfo = new MailSenderInfo();
		Map<String,String> map=parXml(emialFile.toString());
		Iterator it = map.entrySet().iterator(); 
		 while (it.hasNext()) {  							  
	        Map.Entry entry = (Map.Entry) it.next();  			
	        Object key = entry.getKey();
	        Object value = entry.getValue();  
	        if(emailtype.equals(key.toString())){
	        	stmp = value.toString();
				port = "25";	
			}							       
		}
		mailInfo.setMailServerHost(stmp);
		mailInfo.setMailServerPort(port);
		mailInfo.setValidate(true);
		mailInfo.setUserName(user);
		mailInfo.setPassword(password);// 您的邮箱密码
		mailInfo.setFromAddress(user);
		mailInfo.setToAddress(toAddress);
		mailInfo.setSubject(theme);
		mailInfo.setContent(content);
		// 这个类主要来发送邮件
		if(content!=null&&content.trim().length()>0){
			SimpleMailSender sms = new SimpleMailSender();
			sms.sendTextMail(mailInfo);// 发送文体格式
			System.err.println("终端磁盘使用超出异样请注意查看");
				try {
					writeFile("", file.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	/**
	 * 检查邮箱配置是否设置完成
	 * @return
	 */
	private boolean checkEmpty() {
		if(null==this.password||this.password==""){
			return true;
		}
		if(null==this.port||this.port==""){
			return true;
		}
		if(null==this.stmp||this.stmp==""){
			return true;
		}
		if(null==this.toAddress||this.toAddress==""){
			return true;
		}
		if(null==this.user||this.user==""){
			return true;
		}
		return false;
	}
	/**
	  * 读取文件
	  * 
	  * @param file
	  * @return
	  * @throws Exception
	  */
	 public String readFile(File file) throws Exception {
	  BufferedReader br = new BufferedReader(new FileReader(file));
	  StringBuffer sbf = new StringBuffer("");
	  String line = null;
	  while ((line = br.readLine()) != null) {
	   sbf.append(line).append("\r\n");// 按行读取，追加换行\r\n
	  }
	  br.close();
	  return sbf.toString();
	 }
	 /**
	  * 写入文件
	  * 
	  * @param str
	  *            要保存的内容
	  * @param savePath
	  *            保存的文件路径
	  * @throws Exception
	  *             找不到路径
	  */
	 public void writeFile(String str, String savePath) throws Exception {
	  BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
	  bw.write(str);
	  bw.close();
	 }
	 
	 /**
		 * 解析weather.xml配置文件
		 * @param args
		 * @throws Exception
		 */

		public static Map<String,String> parXml(String path){
			Map<String,String> list=new HashMap<String,String>();
			try{
				//建立解析工厂
				DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
				//创建解析器
				DocumentBuilder db=dbf.newDocumentBuilder();
				org.w3c.dom.Document doc=db.newDocument();
				//开始解析
				doc=db.parse(new File(path));  //path：解析路径   将xml文件解析为Document对象，代表DOM树  
				doc.normalize();      //将文档标准化，去除无用的空格和空行，即删除无用的text node  
				NodeList nList =doc.getElementsByTagName("type"); //返回指定标签名的所有元素
				   for(int  i = 0 ; i<nList.getLength();i++){  	              
		                Node  node = nList.item(i);  
		                Element  ele = (Element)node;  
		                if(node.getNodeType() == Element.ELEMENT_NODE){    
		                    String s= ele.getElementsByTagName("emailType").item(0).getTextContent(); 
		                    String n=ele.getElementsByTagName("emailHost").item(0).getTextContent();
		                    list.put(s, n);
		                } 
				   }
			}catch(Exception e){
			   e.printStackTrace(); 
			 }
			return list;
		}
}
