package tag;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import util.StringUtil;

/**
 * 自定义的字符串处理标签
 *
 */
public class StringTag extends TagSupport {
 
	private static final long serialVersionUID = -8683014812426654300L;
	  
	private String value;//对应jstl表达式中的date值
	private int  length;
  
	public void setValue(String value) {
		   this.value = value;
	}
	
	public void setLength(int length) {
		this.length = length;
	}



	public int doStartTag() throws JspException {
			if(value==null){
				value ="";
			}
		    try {
		    	String str = StringUtil.html2text(value);
		    	if(length>0 && str.length()>length){
		    		str = str.substring(0, length)+"...";
		    	}
		    	pageContext.getOut().write(str);
		    } catch (IOException e) {
		    	e.printStackTrace();
		      	return SKIP_BODY;
		    } 
		    return super.doStartTag();
	}

	public static void main(String[] args) {
		System.out.println(Locale.US);
	}

}
