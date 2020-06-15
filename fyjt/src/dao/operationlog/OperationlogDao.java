package dao.operationlog;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.operationlog.OperationlogBean;
import beans.operationlog.OperationlogView;
@SuppressWarnings("rawtypes")
@Repository
public interface OperationlogDao {
   public void insertOperationlog(OperationlogBean operation);
   public List<OperationlogView> queryOperationlog(Map map);
   public int queryTotal(Map map);
   public List<OperationlogView> exportExcel(Map map);
   public boolean delOperationlog(Integer logDelete);  //自动删除日志
   public List<OperationlogView> findOperationlog(Integer logDelete);  //查询日志
   public void delOperation(Map map); //手动删除日志
}
