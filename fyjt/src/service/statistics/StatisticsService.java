package service.statistics;

import java.sql.Timestamp;
import java.util.List;

import util.PageInfo;
import beans.statistics.StatisticsBean;
import beans.statistics.StatisticsViewBean;
public interface StatisticsService {
	public List<StatisticsBean> queryPage(String eleName,String terminalName,Timestamp starttime,Timestamp endtime,PageInfo pageInfo);
	public int queryTotal(String eleName,String terminalName,Timestamp starttime,Timestamp endtime);
	public List<StatisticsViewBean> querytongji(String eleName,Timestamp starttime,Timestamp endtime,PageInfo pageInfo);
	public List<StatisticsViewBean> querytongjiTotal(String eleName,Timestamp starttime,Timestamp endtime);
	public int statisticsDel(Integer [] tids);
    public List<StatisticsBean> exportEleData(String eleName,String terminalName,Timestamp starttime,Timestamp endtime,int startCount,int endCount);
    public List<StatisticsViewBean> exportStatisticsTJData(String eleName,Timestamp starttime,Timestamp endtime);
    
    /**分部查询***/
    public int queryStatisticsPartCount(String eleName,String terminalName,Timestamp starttime,Timestamp endtime);
    public List<StatisticsBean> exportEleExcel(String eleName,String terminalName,Timestamp starttime,Timestamp endtime,int startCount,int endCount);
    
    public boolean delstatis(Integer statisticsDelete);  //自动删除统计日志
    public void delStatistics(Timestamp starttime,Timestamp endtime); //手动删除统计日志
}
