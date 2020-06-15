package beans.terminal;

import java.sql.Timestamp;

public class TerminalStatusViewBean {

	public int terminal_id;
	public String terminal_name;
	public String terminalJudge;
	public String mac;
	public String sysVersion;
	public String softVersion;
	public String cpuUsage;
	public String memUsage;
	public String diskUsage;
	public String terminalStatus;
	public String runTimeLength;
	public Timestamp updateTime;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getTerminal_name() {
		return terminal_name;
	}
	public void setTerminal_name(String terminalName) {
		terminal_name = terminalName;
	}
	public int getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(int terminalId) {
		terminal_id = terminalId;
	}
	public String getTerminalJudge() {
		return terminalJudge;
	}
	public void setTerminalJudge(String terminalJudge) {
		this.terminalJudge = terminalJudge;
	}
	public String getSysVersion() {
		return sysVersion;
	}
	public void setSysVersion(String sysVersion) {
		this.sysVersion = sysVersion;
	}
	public String getSoftVersion() {
		return softVersion;
	}
	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}
	public String getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public String getMemUsage() {
		return memUsage;
	}
	public void setMemUsage(String memUsage) {
		this.memUsage = memUsage;
	}
	public String getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(String diskUsage) {
		this.diskUsage = diskUsage;
	}
	public String getTerminalStatus() {
		return terminalStatus;
	}
	public void setTerminalStatus(String terminalStatus) {
		this.terminalStatus = terminalStatus;
	}
	public String getRunTimeLength() {
		return runTimeLength;
	}
	public void setRunTimeLength(String runTimeLength) {
		this.runTimeLength = runTimeLength;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
}
