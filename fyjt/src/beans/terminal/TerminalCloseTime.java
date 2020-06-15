package beans.terminal;

public class TerminalCloseTime {
	private String terminalId;
	private String terminal_name;
	private String mac;
	private String startTime;
	private String endTime;
	private String days;
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getTerminalname() {
		return terminal_name;
	}
	public void setTerminalname(String terminal_name) {
		this.terminal_name = terminal_name;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
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
	
}
