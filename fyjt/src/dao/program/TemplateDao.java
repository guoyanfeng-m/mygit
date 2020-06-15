package dao.program;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.program.TemplateBean;
import beans.program.view.TemplateView;
@Repository
public interface TemplateDao {
	public void insertTemplate(TemplateBean templateBean);

	public void deleteTemplateById(Integer template_id);

	public List<TemplateView> queryTemplate(Map<String,Object> map);

	public Integer queryTemplateCount(TemplateBean templateBean);

	public List<TemplateBean> queryTemplateByName(String name);
}
