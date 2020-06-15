package beans.program.view;



public class ProgramTerminalView {
	private Integer program_id;
	private Integer terminal_id;
	private String terminal_name;
	private String program_name;
	private String group_name;
	private String mac;
	private String terminal_ip;
	private Integer schedulelevel;
	private String program_status;
	private Integer type;
	private Integer group_id;
	
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer groupId) {
		group_id = groupId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	private String[] materialarr;

	public String[] getMaterialarr() {
		return materialarr;
	}
	public void setMaterialarr(String[] materialarr) {
		this.materialarr = materialarr;
	}
	public String getProgram_status() {
		return program_status;
	}
	public void setProgram_status(String programStatus) {
		program_status = programStatus;
	}
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String programName) {
		program_name = programName;
	}
	
	public String getTerminal_ip() {
		return terminal_ip;
	}
	public void setTerminal_ip(String terminal_ip) {
		this.terminal_ip = terminal_ip;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminal_name) {
		this.terminal_name = terminal_name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getProgram_id() {
		return program_id;
	}
	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}
	public Integer getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Integer terminal_id) {
		this.terminal_id = terminal_id;
	}
	public Integer getSchedulelevel() {
		return schedulelevel;
	}
	public void setSchedulelevel(Integer schedulelevel) {
		this.schedulelevel = schedulelevel;
	}
	
}
