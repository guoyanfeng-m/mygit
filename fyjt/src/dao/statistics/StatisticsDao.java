package dao.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.statistics.StatisticsBean;
import beans.statistics.StatisticsViewBean;
@SuppressWarnings("rawtypes")
@Repository
public interface StatisticsDao {
    
	public List<StatisticsBean> queryPage(Map map);
    public int queryTotal(Map map);
    public List<StatisticsViewBean> querytongjiTotal(Map map);
    public List<StatisticsViewBean> querytongji(Map map);
    public List<StatisticsBean> exportEleData(Map map);
    //素材统计导出
    public List<StatisticsViewBean> exportStatisticsTJData(Map map);
    public int statisticsDel(Integer [] tids);
    
    /***分段查询****/
    public int queryStatisticsPartCount(Map map);
    public List<StatisticsBean> exportEleExcel(Map map);
    
    public boolean delstatis(Integer statisticsDelete);  //自动删除统计日志
    public void delStatistics(Map map); //手动删除统计日志
}
