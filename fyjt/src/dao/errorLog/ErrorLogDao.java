package dao.errorLog;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.errorLog.ErrorLogBean;
import beans.proStatistics.ProStatisticsBean;
@Repository
public interface ErrorLogDao {
	
	public void insertErrorLog(ErrorLogBean elb);

	public int queryTotal(Map<String, Object> map);

	public List<ProStatisticsBean> queryErrorlog(Map<String, Object> map);
	
}
