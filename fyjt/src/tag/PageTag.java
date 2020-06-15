package tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import vo.PageEnt;



/**
 * 分页标签
 * 
 * @ClassName: PageTag
 * @author hxw
 * 
 */
public class PageTag extends TagSupport {

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
		str.append("<div class='"+style+"'>");
		int pageCount = pe.getTotalPages();
		//if(pageCount>1){
			if(pageCount!=0){
				if(pe.getHavePrePage()){
					str.append("<a href='javascript:void(0);'   onclick='goPaging_"+formId+"("+"\""+formId+"\","+(pe.getPageNo()-1)+")'  class='direct unable'>上一页</a>");
				}else{
					str.append("<a href='javascript:void(0);' class='direct unable'>上一页</a>");
				}
			}
			
			
			int a = 9;
			long i =0; 
			long cou = pageCount+1;
			if(cou>a){
				if(pe.getPageNo()>a-1){
					if(cou-pe.getPageNo()>-1){
						
						if(pageCount-pe.getPageNo()<1){
							i = pageCount-a+1;
						}if(pageCount==pe.getPageNo()){
							i = pageCount-a;
						}else{
							i = pe.getPageNo()+1-a;
						}
						if(pageCount-pe.getPageNo()>=1){
							cou = pe.getPageNo()+1;
						}else{
							cou = pe.getPageNo();
						}
					}else{
						i= cou-a-1;
						cou = a;
					}
				}else{
					cou = a;
				}
			}else{
				cou = pageCount;
			}
			for (; i < cou; i++) {
				if(pe.getPageNo()==i+1){
					
					//str.append("<span>"+(i+1)+"</span>");
					str.append("<a href='javascript:void(0);' class='cur'>"+(i+1)+"</a>");
				}else{
					str.append("<a href='javascript:void(0);'   onclick='goPaging_"+formId+"("+"\""+formId+"\","+(i+1)+")'>"+(i+1)+"</a>");
				}
			}
			if(cou>=9 && pe.getHaveNextPage()){
				str.append("<a href='javascript:void(0);' class='omit'  onclick='goPaging_"+formId+"("+"\""+formId+"\","+(pe.getPageNo()+1)+")'>...</a>");
			}
			if(pageCount!=0){
				if(pe.getHaveNextPage()){
					str.append("<a href='javascript:void(0);'  onclick='goPaging_"+formId+"("+"\""+formId+"\","+(pe.getPageNo()+1)+")'  class='direct'>下一页</a>");
				}else{
					str.append("<a href='javascript:void(0);' class='direct'>下一页</a>");
				}
				/**
				str.append("<span>共"+pe.getTotalPages()+"页</span>");
				
				
				str.append("<span class='up_jump'>"
						+ "跳到 <input type='text' value='"+pe.getPageNo()+"' id='input_page_"+formId+"' onafterpaste=\"this.value=this.value.replace(/\\D/g,'')\" onkeyup=\"this.value=this.value.replace(/\\D/g,'')\"  class='input_unipage'>"
						+ " 页</span>");

				str.append("<input type='button'  onclick='goPaging_button_"+formId+"()' value='确定' class='sub_unipage'>");
				**/
				str.append("<script type=\"text/javascript\"> \n");
				str.append("  function goPaging_button_"+formId+"(){ \n");
				str.append("  var input_page_no =   $('#input_page_"+formId+"').val(); \n");
				str.append("  if(input_page_no!=null && input_page_no!='' && input_page_no<= "+pe.getTotalPages()+"){ \n");
				str.append("   goPaging_"+formId+"('"+formId+"', input_page_no); \n");
				str.append("  }\n");
				str.append(" else{alert('请输入正确的页数！'); }");
				str.append("  }\n");
				str.append("  function goPaging_"+formId+"(form,page){ \n");
				str.append("    $(\"#\"+form+\" :input[name='pageNo']\").val(page); \n");
				str.append("    $(\"#\"+form).submit(); \n");
				str.append("  }\n</script>");
			}else{
				//str.append("<p>没有任何数据</p>");
			}
		//}
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
