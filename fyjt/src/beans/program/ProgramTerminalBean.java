package beans.program;

import java.sql.Timestamp;
import java.util.List;


public class ProgramTerminalBean {
	private Integer program_id;
	private Integer terminal_id;
	private Timestamp publishTime;
	private String mac;
	private List<Integer>terminalIds;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Timestamp getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
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
	public List<Integer> getTerminalIds() {
		return terminalIds;
	}
	public void setTerminalIds(List<Integer> terminalIds) {
		this.terminalIds = terminalIds;
	}
	
}
