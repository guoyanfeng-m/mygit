/**
 * Description:
 * author:zhen
 * 2016年3月23日 下午5:37:03
 */
package beans.cooperation;

import java.sql.Timestamp;


/**
 * @author gyf
 *
 */
public class CooperationBean {

	private int id;
	private String link_url;
	private String dark_img_url;
	private String light_img_url;
	private String name;
	private String status;
	private Timestamp create_time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public String getDark_img_url() {
		return dark_img_url;
	}
	public void setDark_img_url(String dark_img_url) {
		this.dark_img_url = dark_img_url;
	}
	public String getLight_img_url() {
		return light_img_url;
	}
	public void setLight_img_url(String light_img_url) {
		this.light_img_url = light_img_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	
	

	
	
	
	
}
