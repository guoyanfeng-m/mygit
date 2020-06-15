package util;

import java.io.Serializable;
import java.util.List;

public class DownLoadList implements Serializable {
	/**
	 * @ Description     概述：       <p></p> 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String xml;
	private List<String> elements;
	private Long size;
	public String getXml() {
		return xml;
	} 
	public void setXml(String xml) {
		this.xml = xml;
	}

	public List<String> getElements() {
		return elements;
	}
	public void setElements(List<String> elements) {
		this.elements = elements;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
