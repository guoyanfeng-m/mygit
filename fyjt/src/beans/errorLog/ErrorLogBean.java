package beans.errorLog;

import java.sql.Timestamp;

public class ErrorLogBean {

	private Integer log_id;
	private String module_name;
	private String function_name;
	private String class_name;
	private String exception_type;
	private String exception_reason;
	private Timestamp update_time;
	private Timestamp happen_time;
	
	public Timestamp getHappen_time() {
		return happen_time;
	}
	public void setHappen_time(Timestamp happenTime) {
		happen_time = happenTime;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp updateTime) {
		update_time = updateTime;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String moduleName) {
		module_name = moduleName;
	}
	public Integer getLog_id() {
		return log_id;
	}
	public void setLog_id(Integer logId) {
		log_id = logId;
	}
	public String getFunction_name() {
		return function_name;
	}
	public void setFunction_name(String functionName) {
		function_name = functionName;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String className) {
		class_name = className;
	}
	public String getException_type() {
		return exception_type;
	}
	public void setException_type(String exceptionType) {
		exception_type = exceptionType;
	}
	public String getException_reason() {
		return exception_reason;
	}
	public void setException_reason(String exceptionReason) {
		exception_reason = exceptionReason;
	}
	
	
}
