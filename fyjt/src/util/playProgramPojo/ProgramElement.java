package util.playProgramPojo;
/**
 * <p>          
 *       <discription>概述：节目管理--节目预览--pojo </discription>
 * </p>   
 * @Author          创建人：    ryl
 * @Project_name    项目名称：iiswoftp
 * @Company         公司名称：双旗科技
 * @Date            创建时间：2017年3月14日 下午5:03:20
 */
public class ProgramElement {
	private String id;
	private String life;
	private String thumbnailUrl;
	private String name;
	private String elemTempId;
	private String eWidth;
	private String src;
	private String resolution;
	private String eHeight;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLife() {
		return life;
	}
	public void setLife(String life) {
		this.life = life;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getElemTempId() {
		return elemTempId;
	}
	public void setElemTempId(String elemTempId) {
		this.elemTempId = elemTempId;
	}
	public String geteWidth() {
		return eWidth;
	}
	public void seteWidth(String eWidth) {
		this.eWidth = eWidth;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String geteHeight() {
		return eHeight;
	}
	public void seteHeight(String eHeight) {
		this.eHeight = eHeight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
