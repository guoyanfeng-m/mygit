package util.playProgramPojo;

import java.util.List;
/**
 * <p>          
 *       <discription>概述： 节目管理--节目预览--pojo</discription>
 * </p>   
 * @Author          创建人：    ryl
 * @Project_name    项目名称：iiswoftp
 * @Company         公司名称：双旗科技
 * @Date            创建时间：2017年3月14日 下午4:52:48
 */
public class scene {
	private List<region> region;
	private String sceneName;
	private String id;
	private String life;
	private String backimg;
	private String ismain;
	private String backimgName;
	private String backcolor;
	
	public List<region> getRegion() {
		return region;
	}
	public void setRegion(List<region> region) {
		this.region = region;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
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
	public String getBackimg() {
		return backimg;
	}
	public void setBackimg(String backimg) {
		this.backimg = backimg;
	}
	public String getIsmain() {
		return ismain;
	}
	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}
	public String getBackimgName() {
		return backimgName;
	}
	public void setBackimgName(String backimgName) {
		this.backimgName = backimgName;
	}
	public String getBackcolor() {
		return backcolor;
	}
	public void setBackcolor(String backcolor) {
		this.backcolor = backcolor;
	}

}
