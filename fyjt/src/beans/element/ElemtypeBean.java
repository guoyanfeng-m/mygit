package beans.element;

public class ElemtypeBean {

	private Integer type_id;
	private String type_name;
	private Integer parentId;
	private String description;
	private Integer creator_id;
	private Integer is_private;
	
	public Integer getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(Integer creatorId) {
		creator_id = creatorId;
	}
	public Integer getIs_private() {
		return is_private;
	}
	public void setIs_private(Integer isPrivate) {
		is_private = isPrivate;
	}
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer typeId) {
		type_id = typeId;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String typeName) {
		type_name = typeName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
