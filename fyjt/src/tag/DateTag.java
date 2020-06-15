package tag;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import util.DateUtil;

/**
 * 自定义的时间处理标签
 *
 */
public class DateTag extends TagSupport {
 
	private static final long serialVersionUID = -8683014812426654300L;
	  
	private String date;//对应jstl表达式中的date值
	private String pattern;//对应表达式中的pattern指定的时间格式

  
	public void setDate(String date) {
		   this.date = date;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int doStartTag() throws JspException {
			if (this.date == null) {
				return SKIP_BODY;
			}
		  	SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US); 
		    try {
		    	pageContext.getOut().write(DateUtil.format(sdf.parse(date), pattern));
		    } catch (IOException e) {
		    	e.printStackTrace();
		      	return SKIP_BODY;
		    } catch (ParseException e) {
				e.printStackTrace();
				return SKIP_BODY;
			}
		    return super.doStartTag();
	}

	public static void main(String[] args) {
		System.out.println(Locale.US);
	}

}
