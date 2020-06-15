package beans.sys;

import java.util.ArrayList;
import java.util.List;

public class TreeBeans {
	private String text;      //树节点显示的文本
	private String state;   //当前节点是否是叶子节点
	private int id;        //树节点的Id
	private int parentId;
	private List<TreeBeans> children = new ArrayList<TreeBeans>();
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public List<TreeBeans> getChildren() {
		return children;
	}
	public void setChildren(List<TreeBeans> children) {
		this.children = children;
	}  
	
}
