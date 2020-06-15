package serviceImpl.errorLog;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.errorLog.ErrorLogService;
import util.PageInfo;
import beans.errorLog.ErrorLogBean;
import beans.proStatistics.ProStatisticsBean;
import dao.errorLog.ErrorLogDao;

@Service
@Transactional(readOnly=true)
public class ErrorLogServiceImpl implements ErrorLogService{
	@Autowired
	private ErrorLogDao errorLogDao;

	@Override
	@Transactional(readOnly=false)
	public void insertErrorLog(ErrorLogBean elb){
		errorLogDao.insertErrorLog(elb);
	}
	
	@Override
	public int queryTotal(String errormodule, String errordetail,
			Timestamp startime, Timestamp etime){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("errormodule", errormodule);
		map.put("errordetail", errordetail);
		map.put("starttime", startime);
		map.put("endtime", etime);
		return errorLogDao.queryTotal(map);
	}
	
	@Override
	public List<ProStatisticsBean> queryErrorlog(String errormodule,
			String errordetail, Timestamp startime, Timestamp etime,
			PageInfo pageInfo){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("errormodule", errormodule);
		map.put("errordetail", errordetail);
		map.put("starttime", startime);
		map.put("endtime", etime);
		map.put("pageInfo", pageInfo);
		return errorLogDao.queryErrorlog(map);
	}
}
