package serviceImpl.operationlog;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.operationlog.OperationlogService;
import util.PageInfo;
import beans.operationlog.OperationlogBean;
import beans.operationlog.OperationlogView;
import dao.model.ModelDao;
import dao.operationlog.OperationlogDao;
@Service
@Transactional(readOnly=true)
public class OperationlogServiceImpl implements OperationlogService {
	@Autowired
    private OperationlogDao operationlogDao;
    @Autowired
    private ModelDao modelDao;
    

	@Override
	@Transactional(readOnly=false)
	public boolean insertOperationlog(String userName,String operationName,Integer operationType,Integer operationModel) {
		List<Integer> islog=modelDao.showLogModule();
		if(!islog.contains(operationModel)){
			return true;
		}
		Timestamp time = new Timestamp(new Date().getTime());
		OperationlogBean operation=new OperationlogBean();
		operation.setUserName(userName);
		operation.setOperationName(operationName);
		operation.setOperationModel(operationModel);
		operation.setOperationType(operationType);
		operation.setTime(time);
        operationlogDao.insertOperationlog(operation);
		return true;
	}

	@Override
	public List<OperationlogView> queryScroll(String realname,
			String operationName, Timestamp starttime, Timestamp endtime, Integer operationModel,
			Integer operationType, PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("realname",realname);
		map.put("operationName",operationName);
		map.put("starttime",starttime);
		map.put("endtime",endtime);
		map.put("operationModel",operationModel);
		map.put("operationType",operationType);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return operationlogDao.queryOperationlog(map);
	}

	@Override
	public int queryTotal(String realname, String operationName,
			Timestamp starttime, Timestamp endtime, Integer operationModel, Integer operationType) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("realname",realname);
		map.put("operationName",operationName);
		map.put("starttime",starttime);
		map.put("endtime",endtime);
		map.put("operationModel",operationModel);
		map.put("operationType",operationType);
		return operationlogDao.queryTotal(map);
	}
    /****
     * 导出excel
     */
	@Override
	public List<OperationlogView> exportExcel(String realname, String operationName, Timestamp starttime, Timestamp endtime, Integer operationModel,Integer operationType) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("realname",realname);
		map.put("operationName",operationName);
		map.put("starttime",starttime);
		map.put("endtime",endtime);
		map.put("operationModel",operationModel);
		map.put("operationType",operationType);
		return operationlogDao.exportExcel(map);
	}

	@Override
	@Transactional(readOnly=false)
	public boolean delOperationlog(Integer logDelete) {
		if(operationlogDao.delOperationlog(logDelete)){
			return true;
		}else{
			return false;
		}
		//return operationlogDao.delOperationlog(logDelete);
	}
	//手动删除日志
	@Override
	@Transactional(readOnly=false)
	 public void delOperation(Timestamp starttime,Timestamp endtime){
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		operationlogDao.delOperation(map);
	 }

	@Override
	public List<OperationlogView> findOperationlog(Integer logDelete) {
		return operationlogDao.findOperationlog(logDelete);
	} 
}
