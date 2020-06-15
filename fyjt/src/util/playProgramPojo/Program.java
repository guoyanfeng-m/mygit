package util.playProgramPojo;

import java.util.List;

/**
 * <p>          
 *       <discription>概述： 节目管理--节目预览--pojo</discription>
 * </p>   
 * @Author          创建人：    ryl
 * @Project_name    项目名称：iiswoftp
 * @Company         公司名称：双旗科技
 * @Date            创建时间：2017年3月14日 下午4:46:55
 */
public class Program {
	private String id;
	private String name;
	private String width;
	private String height;
	private List<scene> scene;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public List<scene> getScene() {
		return scene;
	}
	public void setScene(List<scene> scene) {
		this.scene = scene;
	}
	
}
