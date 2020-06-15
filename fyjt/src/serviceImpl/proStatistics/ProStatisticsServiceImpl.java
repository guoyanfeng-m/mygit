package serviceImpl.proStatistics;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.proStatistics.ProStatisticsService;
import util.PageInfo;
import beans.proStatistics.ProStatisticsBean;
import dao.proStatistics.ProStatisticsDao;
@Service
@Transactional(readOnly=true)
public class ProStatisticsServiceImpl implements ProStatisticsService {
	@Autowired
	private ProStatisticsDao proDao;
	
	@Override
	public int selectFileNameCount(String fileName) {
		return proDao.selectFileNameCount(fileName);
	}
	@Override
	@Transactional(readOnly=false)
	public void insertProStatistics(ProStatisticsBean proBean) {
		proDao.insertProStatistics(proBean);
	}
	@Override
	public int queryTotal(String terminalName, String programName,
			Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("terminalName", terminalName);
		map.put("programName", programName);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		return proDao.queryTotal(map);
	}
	@Override
	public List<ProStatisticsBean> queryProgramlog(String terminalName,
			String programName, Timestamp starttime, Timestamp endtime,
			PageInfo pageInfo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("terminalName", terminalName);
		map.put("programName", programName);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return proDao.queryProgramlog(map);
	}
	@Override
	public List<ProStatisticsBean> exportProStatisticsTJData(
			String terminalName, String programName, Timestamp starttime,
			Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("terminalName", terminalName);
		map.put("programName", programName);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		return proDao.exportProStatisticsTJData(map);
	}
}
