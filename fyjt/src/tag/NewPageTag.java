package tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import vo.PageEnt;


/**
 * 分页标签 新ui的标签
 * 
 * @ClassName: PageTag
 * @author hln
 * 
 */
public class NewPageTag extends TagSupport {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 2771143709830017519L;
	private String style;
	private String formId;
	@SuppressWarnings("rawtypes")
	private PageEnt pe;

	@SuppressWarnings("rawtypes")
	public void setName(String name) {
		Object result = this.pageContext.findAttribute(name);
		if (result != null && result instanceof PageEnt) {
			this.pe = (PageEnt) result;
		}
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@Override
	public int doEndTag() throws JspException {
		if (this.pe == null) {
			return SKIP_BODY;
		}

		StringBuilder str = new StringBuilder(800);
		str.append("<div class='" + style + "'>");
		int pageCount = pe.getTotalPages();
		if (pageCount != 0) {
			if (pe.getHavePrePage()) {
				str.append("<a href='javascript:void(0);'   onclick='goPaging_" + formId + "("
						+ "\"" + formId + "\"," + (pe.getPageNo() - 1)
						+ ")'  class='pagePrev'><b></b></a>");
			}
			int i = 0;
			if (pageCount < 5) {
				for (; i < pageCount; i++) {
					if (pe.getPageNo() == i + 1) {
						str.append("<a href='javascript:void(0);' class='selected'>" + (i + 1)
								+ "</a>");
					} else {
						str.append("<a href='javascript:void(0);'   onclick='goPaging_" + formId
								+ "(" + "\"" + formId + "\"," + (i + 1) + ")'>" + (i + 1) + "</a>");
					}
				}
			} else {
				int quotient = (pe.getPageNo() - 1) / 5;
				if (quotient > 0) {
					str.append("<a href='javascript:void(0);' class='omit'  onclick='goPaging_"
							+ formId + "(" + "\"" + formId + "\"," + (quotient * 5) + ")'>...</a>");
				}
				for (i = quotient * 5; i < (quotient + 1) * 5 && i < pageCount; i++) {
					if (pe.getPageNo() == i + 1) {
						str.append("<a href='javascript:void(0);' class='selected'>" + (i + 1)
								+ "</a>");
					} else {
						str.append("<a href='javascript:void(0);'   onclick='goPaging_" + formId
								+ "(" + "\"" + formId + "\"," + (i + 1) + ")'>" + (i + 1) + "</a>");
					}
				}
				if (i < pageCount) {
					str.append("<a href='javascript:void(0);' class='omit'  onclick='goPaging_"
							+ formId + "(" + "\"" + formId + "\"," + (i + 1) + ")'>...</a>");
				}
			}
			if (pe.getHaveNextPage()) {
				str.append("<a href='javascript:void(0);'  onclick='goPaging_" + formId + "("
						+ "\"" + formId + "\"," + (pe.getPageNo() + 1)
						+ ")'  class='pageNext'><b></b></a>");
			}
			str.append(" <span style='float: left;margin-top: 10px'>跳转到:</span><input class='isTxtBig' id='input_page_"
					+ formId
					+ "' type='text' value=''><a href='javascript:void(0)' onclick='goPaging_button_"
					+ formId + "()'>GO</a>");
			/**
			 * str.append("<span>共"+pe.getTotalPages()+"页</span>");
			 * 
			 * 
			 * str.append("<span class='up_jump'>" +
			 * "跳到 <input type='text' value='"
			 * +pe.getPageNo()+"' id='input_page_"+formId+
			 * "' onafterpaste=\"this.value=this.value.replace(/\\D/g,'')\" onkeyup=\"this.value=this.value.replace(/\\D/g,'')\"  class='input_unipage'>"
			 * + " 页</span>");
			 * 
			 * str.append("<input type='button'  onclick='goPaging_button_"+
			 * formId+"()' value='确定' class='sub_unipage'>");
			 **/
			str.append("<script type=\"text/javascript\"> \n");
			str.append("  function goPaging_button_" + formId + "(){ \n");
			str.append("  var input_page_no =   $('#input_page_" + formId + "').val(); \n");
			str.append("  if(input_page_no!=null && input_page_no!='' && /^[1-9]\\d*$/.test(input_page_no) && input_page_no<= "
					+ pe.getTotalPages() + "){ \n");
			str.append("   goPaging_" + formId + "('" + formId + "', input_page_no); \n");
			str.append("  }\n");
			str.append(" else{window.wxc.xcConfirm('请输入正确的页数！', window.wxc.xcConfirm.typeEnum.warning); }");
			str.append("  }\n");
			str.append("  function goPaging_" + formId + "(form,page){ \n");
			str.append("    $(\"#\"+form+\" :input[name='pageNo']\").val(page); \n");
			str.append("    $(\"#\"+form).submit(); \n");
			str.append("  }\n</script>");
		} else {
			// str.append("<p>没有任何数据</p>");
		}
		// }
		str.append("</div>");

		try {

			JspWriter writer = this.pageContext.getOut();
			writer.print(str.toString());
			writer.flush();
			writer.clearBuffer();

		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

}
