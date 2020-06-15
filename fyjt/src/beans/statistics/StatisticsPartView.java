package beans.statistics;

import java.sql.Timestamp;

public class StatisticsPartView {
    private String elemName;
    private String curret;
    private Timestamp elemstartTime;
    private Timestamp elemendTime;
    private String terminalName;
    private int startCount;
    private int endCount;
	public String getElemName() {
		return elemName;
	}
	public void setElemName(String elemName) {
		this.elemName = elemName;
	}
	public String getCurret() {
		return curret;
	}
	public void setCurret(String curret) {
		this.curret = curret;
	}
	public Timestamp getElemstartTime() {
		return elemstartTime;
	}
	public void setElemstartTime(Timestamp elemstartTime) {
		this.elemstartTime = elemstartTime;
	}
	public Timestamp getElemendTime() {
		return elemendTime;
	}
	public void setElemendTime(Timestamp elemendTime) {
		this.elemendTime = elemendTime;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public int getStartCount() {
		return startCount;
	}
	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	public int getEndCount() {
		return endCount;
	}
	public void setEndCount(int endCount) {
		this.endCount = endCount;
	}
    
    
    
    
}
