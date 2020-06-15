package beans.terminal;

public class ScreenViewBean {
	String terminalName;
	String path;
	String mac;
	String program_name;
	
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String programName) {
		program_name = programName;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
