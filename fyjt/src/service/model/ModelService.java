package service.model;

import java.util.List;

import beans.model.ModelBean;

public interface ModelService {
	  public List<ModelBean> queryAll();
	  public void updateModel(ModelBean sBean);
	  List<Integer> showLogModule();
	  int queryModuleAudit(int moduleid);
}
