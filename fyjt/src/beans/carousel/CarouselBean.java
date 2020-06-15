/**
 * Description:
 * author:zhen
 * 2016年3月23日 下午5:37:03
 */
package beans.carousel;

import java.sql.Timestamp;


/**
 * @author zhen
 *
 */
public class CarouselBean {

	private int carousel_id;
	private String imgurl;
	private int isort;
	private Timestamp create_time;
	private Timestamp update_time;
	private int creator_id;
	private String realname;
	
	
	public int getCarousel_id() {
		return carousel_id;
	}
	public void setCarousel_id(int carousel_id) {
		this.carousel_id = carousel_id;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public int getIsort() {
		return isort;
	}
	public void setIsort(int isort) {
		this.isort = isort;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	public int getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(int creator_id) {
		this.creator_id = creator_id;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}


	
	
	
	
}
