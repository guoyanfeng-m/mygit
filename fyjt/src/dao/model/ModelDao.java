package dao.model;

import java.util.List;

import org.springframework.stereotype.Repository;

import beans.model.ModelBean;
@Repository
public interface ModelDao {
    public List<ModelBean> queryAll();
    public void updateModel(ModelBean sBean);
    public List<Integer> showLogModule();
    public int queryModuleAudit(int moduleid);
}
