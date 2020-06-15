package beans.operationlog;

import java.sql.Timestamp;

public class OperationlogBean {
   private Integer t_id;
   private String userName;
   private String operationName;
   private Timestamp time;
   private Integer operationType;
   private Integer operationModel;
   

	public Integer getT_id() {
		return t_id;
	}
	public void setT_id(Integer tId) {
		t_id = tId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Integer getOperationType() {
		return operationType;
	}
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
	public Integer getOperationModel() {
		return operationModel;
	}
	public void setOperationModel(Integer operationModel) {
		this.operationModel = operationModel;
	}
}
