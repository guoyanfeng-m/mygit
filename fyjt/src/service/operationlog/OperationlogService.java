package service.operationlog;

import java.sql.Timestamp;
import java.util.List;

import util.PageInfo;
import beans.operationlog.OperationlogView;
public interface OperationlogService {
  public boolean insertOperationlog(String userName,String operationName,Integer operationType,Integer operationModel);
  public List<OperationlogView> queryScroll(String userName,String operationName, Timestamp starttime,
		  Timestamp endtime,Integer module,Integer type,PageInfo pageInfo);
  public int queryTotal(String userName,String operationName, Timestamp starttime,
		  Timestamp endtime,Integer module,Integer type);
  public List<OperationlogView> exportExcel(String userName, String operationName, Timestamp startime, Timestamp endtime, Integer operationModel,Integer operationType);
  public boolean delOperationlog(Integer logDelete); //自动删除日志
  public List<OperationlogView> findOperationlog(Integer logDelete);  //查询日志
  public void delOperation(Timestamp starttime,Timestamp endtime); //手动删除日志

}
