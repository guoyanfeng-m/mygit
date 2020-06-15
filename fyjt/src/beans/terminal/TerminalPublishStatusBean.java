package beans.terminal;

public class TerminalPublishStatusBean {
	private String groupname;
	private Integer schedulelevel;
	private String terminal_name;
	private Integer terminal_id;
	private Integer GroupID;
	private Integer program_id;
	private String name;
	private String mac;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getProgram_id() {
		return program_id;
	}
	public void setProgram_id(Integer programId) {
		program_id = programId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Integer terminalId) {
		terminal_id = terminalId;
	}
	public Integer getGroupID() {
		return GroupID;
	}
	public void setGroupID(Integer groupID) {
		GroupID = groupID;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public Integer getSchedulelevel() {
		return schedulelevel;
	}
	public void setSchedulelevel(Integer schedulelevel) {
		this.schedulelevel = schedulelevel;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminalName) {
		terminal_name = terminalName;
	}

	
}
