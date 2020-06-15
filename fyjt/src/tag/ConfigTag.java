package tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import service.config.ConfigService;
import util.DateUtil;
/**
 * 自定义系统配置
 *
 */
public class ConfigTag extends TagSupport {
 
	private static final long serialVersionUID = -8683014812426654300L;
	private static Logger log = LoggerFactory.getLogger(ConfigTag.class);

	private String mark; 

	@Autowired
	private ConfigService configService;
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}

	public int doStartTag() throws JspException {
			 
		    try {
		    	if(configService == null) {
		    		configService = WebApplicationContextUtils.getWebApplicationContext((super.pageContext).getServletContext()).getBean(ConfigService.class);
		    	}
	    		String val = configService.queryConfig(mark);
	    		if(val==null){
	    			log.error("ConfigTag：   读取系统配置（"+mark+"）没有找到这个系统配置    "+DateUtil.getCurrentTime());
	    		}
		    	pageContext.getOut().write(val);
		    } catch (Exception e) {
		    	e.printStackTrace();
		      	return SKIP_BODY;
		    } 
		    return super.doStartTag();
	}

	 

}
