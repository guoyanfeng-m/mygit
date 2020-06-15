package serviceImpl.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.model.ModelService;
import beans.model.ModelBean;
import beans.sys.SystemConstant;
import dao.model.ModelDao;
@Service
@Transactional(readOnly=true)
public class ModelServiceImpl implements ModelService {
	@Autowired
    private ModelDao modelDao;
    

	@Override
	public List<ModelBean> queryAll() {
		return modelDao.queryAll();
	}

	@Override
	@Transactional(readOnly=false)
	 public void updateModel(ModelBean sBean){
       modelDao.updateModel(sBean);		
	}

	@Override
	public List<Integer> showLogModule() {
		return modelDao.showLogModule();
	}

	@Override
	public int queryModuleAudit(int moduleid) {
		int s = modelDao.queryModuleAudit(moduleid);
		if(s==SystemConstant.AUDIT_TRUE){
			return SystemConstant.APPROVE_STATUS_WAIT;
		}else{
			return SystemConstant.APPROVE_STATUS_SUCCESS;
		}
	}
}
