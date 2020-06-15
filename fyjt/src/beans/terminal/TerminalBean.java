package beans.terminal;

import java.sql.Timestamp;
import java.util.List;

public class TerminalBean {
	private Integer terminal_id;
	private String terminal_name;
	private String ip;
	private String mac;
	private String audit_status;
	private Timestamp create_time;
	private Timestamp update_time;
	private Integer creator_id;
	private String deleted;
	private Integer groupId;
	private String is_verify;
	private List<Integer> terminalIdList;
	private List<Integer> groupIdList;
	
	
	public Integer getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Integer terminal_id) {
		this.terminal_id = terminal_id;
	}
	public List<Integer> getTerminalIdList() {
		return terminalIdList;
	}
	public void setTerminalIdList(List<Integer> terminalIdList) {
		this.terminalIdList = terminalIdList;
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
	public String getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(String auditStatus) {
		audit_status = auditStatus;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp createTime) {
		create_time = createTime;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp updateTime) {
		update_time = updateTime;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getIs_verify() {
		return is_verify;
	}
	public void setIs_verify(String isVerify) {
		is_verify = isVerify;
	}
	public List<Integer> getGroupIdList() {
		return groupIdList;
	}
	public void setGroupIdList(List<Integer> groupIdList) {
		this.groupIdList = groupIdList;
	}
	public Integer getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(Integer creator_id) {
		this.creator_id = creator_id;
	}
}
