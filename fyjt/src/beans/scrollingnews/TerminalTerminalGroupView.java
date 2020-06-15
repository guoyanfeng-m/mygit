package beans.scrollingnews;


public class TerminalTerminalGroupView {
    private Integer terminal_id;
	private String terminal_name;
	private String ip;
	private String mac;
	private String GroupName;
	private Integer groupId;
	public Integer getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Integer terminalId) {
		terminal_id = terminalId;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminalName) {
		terminal_name = terminalName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	
}
