package beans.scrollingnews;

import java.sql.Timestamp;

public class ScrollingnewsBean {
   private Integer new_id;
   private String sname;
   private String font;
   private String font_size;
   private String font_color;
   private String text;
   private String scroll_direction;
   private Integer scroll_speed;
   private String start_time;
   private String end_time;
   private String xmlcontent;
   private Integer audit_status;
   private Timestamp update_time;
   private Integer creator_id;
   private Integer deleted;
   private Integer IsSend;
   private String realname;
   
public String getRealname() {
	return realname;
}
public void setRealName(String realName) {
	this.realname = realName;
}
public Integer getNew_id() {
	return new_id;
}
public void setNew_id(Integer newId) {
	new_id = newId;
}

public String getSname() {
	return sname;
}
public void setSname(String sname) {
	this.sname = sname;
}
public String getFont() {
	return font;
}
public void setFont(String font) {
	this.font = font;
}
public String getFont_size() {
	return font_size;
}
public void setFont_size(String fontSize) {
	font_size = fontSize;
}
public String getFont_color() {
	return font_color;
}
public void setFont_color(String fontColor) {
	font_color = fontColor;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getScroll_direction() {
	return scroll_direction;
}
public void setScroll_direction(String scrollDirection) {
	scroll_direction = scrollDirection;
}
public Integer getScroll_speed() {
	return scroll_speed;
}
public void setScroll_speed(Integer scrollSpeed) {
	scroll_speed = scrollSpeed;
}
public String getStart_time() {
	return start_time;
}
public void setStart_time(String startTime) {
	start_time = startTime;
}
public String getEnd_time() {
	return end_time;
}
public void setEnd_time(String endTime) {
	end_time = endTime;
}
public String getXmlcontent() {
	return xmlcontent;
}
public void setXmlcontent(String xmlcontent) {
	this.xmlcontent = xmlcontent;
}
public Integer getAudit_status() {
	return audit_status;
}
public void setAudit_status(Integer auditStatus) {
	audit_status = auditStatus;
}
public Timestamp getUpdate_time() {
	return update_time;
}
public void setUpdate_time(Timestamp updateTime) {
	update_time = updateTime;
}
public Integer getCreator_id() {
	return creator_id;
}
public void setCreator_id(Integer creatorId) {
	creator_id = creatorId;
}
public Integer getDeleted() {
	return deleted;
}
public void setDeleted(Integer deleted) {
	this.deleted = deleted;
}
public Integer getIsSend() {
	return IsSend;
}
public void setIsSend(Integer isSend) {
	IsSend = isSend;
}
   
   
}

   