package beans.element;

import java.sql.Timestamp;

public class ElementBean {

	private Integer elem_id;
	private String elem_name;
	private String elem_size;
	private String elem_path;
	private Integer type; 
	private String resolution;
	private String elem_center;
	private String time_length;
	private String thumbnailUrl;
	private Integer audit_status = -1;
	private String MD5;
	private String description;
	private Integer creator_id;
	private Timestamp create_time;
	private Timestamp update_time;
	private Integer deleted;
	
	public Integer getElem_id() {
		return elem_id;
	}
	public void setElem_id(Integer elemId) {
		elem_id = elemId;
	}
	public String getElem_name() {
		return elem_name;
	}
	public void setElem_name(String elemName) {
		elem_name = elemName;
	}
	public String getElem_size() {
		return elem_size;
	}
	public void setElem_size(String elemSize) {
		elem_size = elemSize;
	}
	public String getElem_path() {
		return elem_path;
	}
	public void setElem_path(String elemPath) {
		elem_path = elemPath;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getTime_length() {
		return time_length;
	}
	public void setTime_length(String timeLength) {
		time_length = timeLength;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public Integer getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(Integer auditStatus) {
		audit_status = auditStatus;
	}
	public String getMD5() {
		return MD5;
	}
	public void setMD5(String mD5) {
		MD5 = mD5;
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
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp createTime) {
		create_time = createTime;
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
	public String getElem_center() {
		return elem_center;
	}
	public void setElem_center(String center) {
		this.elem_center = center;
	}


}
