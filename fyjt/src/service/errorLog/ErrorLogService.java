package service.errorLog;

import java.sql.Timestamp;
import java.util.List;

import util.PageInfo;
import beans.errorLog.ErrorLogBean;
import beans.proStatistics.ProStatisticsBean;

public interface ErrorLogService {
	
	public void insertErrorLog(ErrorLogBean elb);

	public int queryTotal(String errormodule, String errordetail,
			Timestamp startime, Timestamp etime);

	public List<ProStatisticsBean> queryErrorlog(String errormodule,
			String errordetail, Timestamp startime, Timestamp etime,
			PageInfo pageInfo);

}
