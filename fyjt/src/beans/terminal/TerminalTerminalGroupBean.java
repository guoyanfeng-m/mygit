package beans.terminal;

import java.util.List;

public class TerminalTerminalGroupBean {
	private Integer terminalId;
	private Integer groupId;
	private List<Integer>groupIds;
	
	public List<Integer> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<Integer> groupIds) {
		this.groupIds = groupIds;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
}
