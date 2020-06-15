package beans.role;

import java.sql.Timestamp;

public class RoleViewBean {
	private Integer role_id;
	private String role_name;
	private String description;
	private String realname;
	private Timestamp create_time;
	private Integer schedulelevel;
	public Integer getSchedulelevel() {
		return schedulelevel;
	}
	public void setSchedulelevel(Integer schedulelevel) {
		this.schedulelevel = schedulelevel;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer roleId) {
		role_id = roleId;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String roleName) {
		role_name = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp createTime) {
		create_time = createTime;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	
}
