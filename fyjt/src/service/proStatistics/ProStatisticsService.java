package service.proStatistics;

import java.sql.Timestamp;
import java.util.List;

import util.PageInfo;
import beans.proStatistics.ProStatisticsBean;

/**
 * 播放日志service
 * @author fubingxin
 */
public interface ProStatisticsService {
	/**
	 * 查询filename个数
	 */
	public int selectFileNameCount(String fileName);
	/**
	 * 添加播放日志
	 * @param proBean
	 */
	public void insertProStatistics(ProStatisticsBean proBean);
	/**
	 * 查询数据total
	 */
	public int queryTotal(String terminalName,String programName,Timestamp starttime,Timestamp endtime);
	/**
	 * 查询所有
	 */
	public List<ProStatisticsBean> queryProgramlog(String terminalName,String programName,Timestamp starttime,Timestamp endtime,PageInfo pageInfo);
	/**
	 * 查询导出excel数据
	 */
	public List<ProStatisticsBean> exportProStatisticsTJData(String terminalName,String programName,Timestamp starttime,Timestamp endtime);
}
