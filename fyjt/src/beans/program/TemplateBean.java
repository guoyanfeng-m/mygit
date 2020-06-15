package beans.program;

import java.sql.Timestamp;

public class TemplateBean {
	private Integer template_id;
	private String name;
	private String url;
	private Integer creator_id;
	private Integer audit_status;
	private String description;
	private String resolution;
	private Integer deleted;
	private Timestamp create_time;
	private Integer is_touch;
	
	public Integer getIs_touch() {
		return is_touch;
	}
	public void setIs_touch(Integer isTouch) {
		is_touch = isTouch;
	}
	public Integer getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(Integer creator_id) {
		this.creator_id = creator_id;
	}
	public Integer getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(Integer audit_status) {
		this.audit_status = audit_status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
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
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
}
