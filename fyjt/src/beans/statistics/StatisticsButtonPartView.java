package beans.statistics;

import java.sql.Timestamp;

public class StatisticsButtonPartView {
    private String sceneNameofButton;
    private String buttonType;
    private String sceneNameofJumpbutton;
    private Timestamp starttime;
    private Timestamp endtime;
    private int startCount;
    private int endCount;
    private String curret;
    
	public String getCurret() {
		return curret;
	}
	public void setCurret(String curret) {
		this.curret = curret;
	}
	public String getSceneNameofButton() {
		return sceneNameofButton;
	}
	public void setSceneNameofButton(String sceneNameofButton) {
		this.sceneNameofButton = sceneNameofButton;
	}
	public String getButtonType() {
		return buttonType;
	}
	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}
	public Timestamp getStarttime() {
		return starttime;
	}
	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}
	public Timestamp getEndtime() {
		return endtime;
	}
	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
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
	public String getSceneNameofJumpbutton() {
		return sceneNameofJumpbutton;
	}
	public void setSceneNameofJumpbutton(String sceneNameofJumpbutton) {
		this.sceneNameofJumpbutton = sceneNameofJumpbutton;
	}
    
    
}
