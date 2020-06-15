package beans.operationlog;

import java.util.Date;

public class OperationlogView{
	private Integer t_id;
	private String userName;
	private String description;
	private String module_name;
	private String operationName;
	private Date time;
	private String realname;
	   
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
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

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String moduleName) {
		module_name = moduleName;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	   
	   
	   
}
