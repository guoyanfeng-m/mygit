/**
 * Description:
 * author:zhen
 * 2016年3月23日 下午5:37:03
 */
package beans.nav;

import java.util.List;



/**
 * @author gyf
 *
 */
public class NavBean {

	private int id;
	private int pid;
	private String name;
	private String url;
	private List<NavBean> listNavvo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<NavBean> getListNavvo() {
		return listNavvo;
	}
	public void setListNavvo(List<NavBean> listNavvo) {
		this.listNavvo = listNavvo;
	}
	
	

	
	
	
	
}
