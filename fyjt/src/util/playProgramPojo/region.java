package util.playProgramPojo;

import java.util.List;
/**
 * <p>          
 *       <discription>概述： 节目管理--节目预览--pojo</discription>
 * </p>   
 * @Author          创建人：    ryl
 * @Project_name    项目名称：iiswoftp
 * @Company         公司名称：双旗科技
 * @Date            创建时间：2017年3月14日 下午5:03:11
 */
public class region {
	private String id;
	private String zIndex;
	private String height;
	private String width;
	private String name;
	private String resolutionSize;
	private String left;
	private String elemlife;
	private String type;
	private String top;
	private List<ProgramElement> element;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getzIndex() {
		return zIndex;
	}
	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResolutionSize() {
		return resolutionSize;
	}
	public void setResolutionSize(String resolutionSize) {
		this.resolutionSize = resolutionSize;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getElemlife() {
		return elemlife;
	}
	public void setElemlife(String elemlife) {
		this.elemlife = elemlife;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public List<ProgramElement> getElement() {
		return element;
	}
	public void setElement(List<ProgramElement> element) {
		this.element = element;
	}
	
}
