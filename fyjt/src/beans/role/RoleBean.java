package beans.role;

import java.sql.Timestamp;

public class RoleBean {
	private Integer role_id;
	private String role_name;
	private String description;
	private Integer creator_id;
	private Timestamp update_time;
	private Integer deleted;
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
	public Integer getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(Integer creatorId) {
		creator_id = creatorId;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp updateTime) {
		update_time = updateTime;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp createTime) {
		create_time = createTime;
	}
	
	
}
