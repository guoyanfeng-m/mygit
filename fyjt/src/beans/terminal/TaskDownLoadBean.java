package beans.terminal;

import java.sql.Timestamp;

public class TaskDownLoadBean {
	private String terminal_name;
	private Integer taskId;
	private String mac;
	private String taskName;
	private String TaskPercent;
	private Timestamp UpdateTime;
	private Integer program_style;
	private Integer pub_status;
	private Integer schedulelevel;
	private String group_name;
	private String[] materialarr;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getProgram_style() {
		return program_style;
	}
	public void setProgram_style(Integer programStyle) {
		program_style = programStyle;
	}
	public Integer getPub_status() {
		return pub_status;
	}
	public void setPub_status(Integer pubStatus) {
		pub_status = pubStatus;
	}
	public Integer getSchedulelevel() {
		return schedulelevel;
	}
	public void setSchedulelevel(Integer schedulelevel) {
		this.schedulelevel = schedulelevel;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String groupName) {
		group_name = groupName;
	}
	public String[] getMaterialarr() {
		return materialarr;
	}
	public void setMaterialarr(String[] materialarr) {
		this.materialarr = materialarr;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminalName) {
		terminal_name = terminalName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskPercent() {
		return TaskPercent;
	}
	public void setTaskPercent(String taskPercent) {
		TaskPercent = taskPercent;
	}
	public Timestamp getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		UpdateTime = updateTime;
	}
	
	
}
