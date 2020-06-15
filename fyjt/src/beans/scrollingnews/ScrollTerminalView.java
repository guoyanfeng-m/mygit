package beans.scrollingnews;


public class ScrollTerminalView {
	public int terminal_id;
	public String terminal_name;
	public String ip;
	public String mac;
	public String GroupName;
	public Integer groupId;
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
