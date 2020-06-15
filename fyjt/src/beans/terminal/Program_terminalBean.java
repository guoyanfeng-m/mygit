package beans.terminal;

public class Program_terminalBean {
	 private String terminal_name;
	 private String mac;
	 private int program_id;
     private String name;
     private int terminal_id;
     
	public int getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(int terminalId) {
		terminal_id = terminalId;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminalName) {
		terminal_name = terminalName;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getProgram_id() {
		return program_id;
	}
	public void setProgram_id(int programId) {
		program_id = programId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
     
}
