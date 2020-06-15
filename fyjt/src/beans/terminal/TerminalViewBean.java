package beans.terminal;

import java.sql.Timestamp;

public class TerminalViewBean {
	private Integer terminal_id;
	private String terminal_name;
	private String ip;
	private String mac;
	private Integer audit_status;//审核状态
	private Timestamp create_time;
	private Timestamp update_time;
	private String userName;
	private String GroupName;
	private Integer groupId;
	private String groupIds;
	private String GroupNames;
	
	public Integer getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Integer terminal_id) {
		this.terminal_id = terminal_id;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminal_name) {
		this.terminal_name = terminal_name;
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
	public Integer getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(Integer audit_status) {
		this.audit_status = audit_status;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	public String getGroupNames() {
		return GroupNames;
	}
	public void setGroupNames(String groupNames) {
		GroupNames = groupNames;
	}
}
