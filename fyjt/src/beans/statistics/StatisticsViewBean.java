package beans.statistics;


public class StatisticsViewBean {
   private Integer t_id;
   private String elemName;
   private Integer elemType;
   private String totaltime;  //播放总时长
   private int counts;   //播放总次数
public Integer getT_id() {
	return t_id;
}
public void setT_id(Integer tId) {
	t_id = tId;
}
public String getElemName() {
	return elemName;
}
public void setElemName(String elemName) {
	this.elemName = elemName;
}
public Integer getElemType() {
	return elemType;
}
public void setElemType(Integer elemType) {
	this.elemType = elemType;
}
public String getTotaltime() {
	return totaltime;
}
public void setTotaltime(String totaltime) {
	this.totaltime = totaltime;
}
public int getCounts() {
	return counts;
}
public void setCounts(int counts) {
	this.counts = counts;
}

   
   
	
}
