package dao.proStatistics;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.proStatistics.ProStatisticsBean;

/**
 * 播放日志dao
 * @author fubingxin
 */
@SuppressWarnings("rawtypes")
@Repository
public interface ProStatisticsDao {
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
	public int queryTotal(Map map);
	/**
	 * 查询所有
	 */
	public List<ProStatisticsBean> queryProgramlog(Map map);
	/**
	 * 查询导出excel数据
	 */
	public List<ProStatisticsBean> exportProStatisticsTJData(Map map);
}
