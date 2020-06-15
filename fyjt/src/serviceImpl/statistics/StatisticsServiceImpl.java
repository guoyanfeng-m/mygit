package serviceImpl.statistics;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.statistics.StatisticsService;
import util.PageInfo;
import beans.statistics.StatisticsBean;
import beans.statistics.StatisticsViewBean;
import dao.statistics.StatisticsDao;
@Service
@Transactional(readOnly=true)
public class StatisticsServiceImpl implements StatisticsService {
	@Autowired
    private StatisticsDao statisticsDao;

	@Override
	public List<StatisticsBean> queryPage(String eleName, String terminalName,
			Timestamp starttime, Timestamp endtime, PageInfo pageInfo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("terminalName", terminalName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return statisticsDao.queryPage(map);
	}

	@Override
	public int queryTotal(String eleName, String terminalName,
			Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("terminalName", terminalName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		return statisticsDao.queryTotal(map);
	}

	/****
	 * 分组查询统计信息
	 */
	@Override
	public List<StatisticsViewBean> querytongji(String eleName,
			Timestamp starttime, Timestamp endtime,PageInfo pageInfo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("elemstartTime", starttime);
		map.put("elemendtime", endtime);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return statisticsDao.querytongji(map);
	}
    /****
     * 分组查询统计总数
     */
	@Override
	public List<StatisticsViewBean> querytongjiTotal(String eleName, Timestamp starttime,
			Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		return statisticsDao.querytongjiTotal(map);
	}

	@Override
	public int statisticsDel(Integer [] tids) {
		int i=statisticsDao.statisticsDel(tids);
		i=1;
		return i;
	}

	@Override
	public List<StatisticsBean> exportEleData(String eleName,
			String terminalName, Timestamp starttime, Timestamp endtime,int startCount,int endCount) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("terminalName", terminalName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		map.put("offset", startCount);
		map.put("limit", endCount);
		return statisticsDao.exportEleData(map);
	}

	@Override
	public List<StatisticsViewBean> exportStatisticsTJData(String eleName,
			Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		return statisticsDao.exportStatisticsTJData(map);
	}

	/****
	 * 分部分查询
	 */
	@Override
	public int queryStatisticsPartCount(String eleName, String terminalName,
			Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("eleName", eleName);
		map.put("terminalName", terminalName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		return statisticsDao.queryStatisticsPartCount(map);
	}
    /****
     * 部分导出
     */
	@Override
	public List<StatisticsBean> exportEleExcel(String eleName,
			String terminalName, Timestamp starttime, Timestamp endtime,
			int startCount, int endCount) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemName", eleName);
		map.put("terminalName", terminalName);
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
		map.put("offset", startCount);
		map.put("limit", endCount);
		return statisticsDao.exportEleData(map);
	}

	/****
	 * 手动删除统计日志
	 */
	@Override
	@Transactional(readOnly=false)
	public void delStatistics(Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("elemstartTime", starttime);
		map.put("elemendTime", endtime);
	    statisticsDao.delStatistics(map);
	}
    /***
     * 自动删除统计日志
     */
	@Override
	@Transactional(readOnly=false)
	public boolean delstatis(Integer statisticsDelete) {
		return statisticsDao.delstatis(statisticsDelete);
	}
	

}
