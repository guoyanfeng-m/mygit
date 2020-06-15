package util.sendEmail;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.TimerTask;

public class WriteWarning extends TimerTask {

	private String msg;
	public WriteWarning(String str){
		this.msg = str;
		
	}
	public void run() {
		FileWriter fw;
		try {
			File file = null;
			file = new File(new File(this.getClass().getClassLoader()
					.getResource("").toURI()).getParentFile()
					.getParentFile()
					+ File.separator
					+ "warning"
					+ File.separator
					+ "warning.txt");
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(msg);
			pw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
