package beans.program.view;

public class ProgramDownloadView {
	private String element_id;
	private Integer element_type;
	private String element_name;
	private String element_size;
	private String element_status;
	private Integer type;

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getElement_id() {
		return element_id;
	}
	public void setElement_id(String elementId) {
		element_id = elementId;
	}
	public Integer getElement_type() {
		return element_type;
	}
	public void setElement_type(Integer elementType) {
		element_type = elementType;
	}
	public String getElement_name() {
		return element_name;
	}
	public void setElement_name(String elementName) {
		element_name = elementName;
	}
	public String getElement_size() {
		return element_size;
	}
	public void setElement_size(String elementSize) {
		element_size = elementSize;
	}
	public String getElement_status() {
		return element_status;
	}
	public void setElement_status(String elementStatus) {
		element_status = elementStatus;
	}
}
