package beans.proStatistics;
/**
 * 播放日志实体类
 * @author fubingxin
 *
 */
public class ProStatisticsBean {
	
	private int proId;
	private String terminalMac;
	private String terminalName;
	private String programName;
	private String programType;
	private int programLevel;
	private int totalTime;
	private String startTime;
	private String endTime;
	private String fileName;
	private int fileType;
	
	public int getProId() {
		return proId;
	}
	public void setProId(int proId) {
		this.proId = proId;
	}
	public String getTerminalMac() {
		return terminalMac;
	}
	public void setTerminalMac(String terminalMac) {
		this.terminalMac = terminalMac;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public int getProgramLevel() {
		return programLevel;
	}
	public void setProgramLevel(int programLevel) {
		this.programLevel = programLevel;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	
	
	
}
