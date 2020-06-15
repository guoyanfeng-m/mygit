package service.program;

import java.util.List;
import java.util.Map;

import util.PageInfo;
import beans.program.TemplateBean;
import beans.program.view.TemplateView;

public interface TemplateService {
	public boolean createTemplate(TemplateBean templateBean, Map<String, Object> dateMap);
	public boolean deleteTemplateById(Integer[] template_id);
	public Integer queryTemplateCount(TemplateBean templateBean);
	public List<TemplateView> queryTemplate(TemplateBean templateBean,PageInfo pageInfo);
	public List<TemplateBean> queryTemplateByName(String name);
}
