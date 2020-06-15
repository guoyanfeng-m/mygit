package vo;

/**
 * easyui combo基本数据定义
 * @author dean
 *
 */
public class ComboBase {

	protected int id;
	protected String text;
	protected boolean selected;
	
	public ComboBase(int id, String text, boolean selected) {
		super();
		this.id = id;
		this.text = text;
		this.selected = selected;
	}
	public ComboBase() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
